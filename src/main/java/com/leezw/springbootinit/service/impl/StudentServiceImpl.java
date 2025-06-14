package com.leezw.springbootinit.service.impl;

import cn.hutool.core.bean.BeanUtil;
import java.time.LocalDateTime;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leezw.springbootinit.common.ErrorCode;
import com.leezw.springbootinit.constant.CommonConstant;
import com.leezw.springbootinit.exception.BusinessException;
import com.leezw.springbootinit.exception.ThrowUtils;
import com.leezw.springbootinit.mapper.StudentMapper;
import com.leezw.springbootinit.model.dto.student.StudentQueryRequest;
import com.leezw.springbootinit.model.entity.Student;
import com.leezw.springbootinit.model.entity.User;
import com.leezw.springbootinit.model.vo.StudentVO;
import com.leezw.springbootinit.model.vo.UserVO;
import com.leezw.springbootinit.service.StudentService;
import com.leezw.springbootinit.service.UserService;
import com.leezw.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户文件服务实现
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
@Service
@Slf4j
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Resource
    private UserService userService;

    @Resource
    private StudentMapper studentMapper;

    /**
     * 校验数据
     *
     * @param student
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validStudent(Student student, boolean add) {
        ThrowUtils.throwIf(student == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String title = student.getUsername();
        // 创建数据时，参数不能为空
        if (add) {
            // todo 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR);
        }
        // 修改数据时，有参数则校验
        // todo 补充校验规则
        if (StringUtils.isNotBlank(title)) {
            ThrowUtils.throwIf(title.length() > 80, ErrorCode.PARAMS_ERROR, "标题过长");
        }
    }

    /**
     * 获取查询条件
     *
     * @param studentQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Student> getQueryWrapper(StudentQueryRequest studentQueryRequest) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        if (studentQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = studentQueryRequest.getId();
        Long notId = studentQueryRequest.getNotId();
        String title = studentQueryRequest.getTitle();
        String content = studentQueryRequest.getContent();
        String searchText = studentQueryRequest.getSearchText();
        String sortField = studentQueryRequest.getSortField();
        String sortOrder = studentQueryRequest.getSortOrder();
        List<String> tagList = studentQueryRequest.getTags();
        Long userId = studentQueryRequest.getUserId();
        // todo 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("title", searchText).or().like("content", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        // JSON 数组查询
        if (CollUtil.isNotEmpty(tagList)) {
            for (String tag : tagList) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取用户文件封装
     *
     * @param student
     * @param request
     * @return
     */
    @Override
    public StudentVO getStudentVO(Student student, HttpServletRequest request) {
        // 对象转封装类
        StudentVO studentVO = StudentVO.objToVo(student);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = Long.valueOf(student.getUserid());
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        studentVO.setUser(userVO);
        // 2. 已登录，获取用户点赞、收藏状态
        long studentId = student.getId();
        User loginUser = userService.getLoginUserPermitNull(request);
        return studentVO;
    }

    /**
     * 分页获取用户文件封装
     *
     * @param studentPage
     * @param request
     * @return
     */
    @Override
    public Page<StudentVO> getStudentVOPage(Page<Student> studentPage, HttpServletRequest request) {
        List<Student> studentList = studentPage.getRecords();
        Page<StudentVO> studentVOPage = new Page<>(studentPage.getCurrent(), studentPage.getSize(), studentPage.getTotal());
        if (CollUtil.isEmpty(studentList)) {
            return studentVOPage;
        }
        // 对象列表 => 封装对象列表
        List<StudentVO> studentVOList = studentList.stream().map(student -> {
            return StudentVO.objToVo(student);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = studentList.stream().map(Student::getId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> studentIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> studentIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUserPermitNull(request);
        // 填充信息
        studentVOList.forEach(studentVO -> {
            Long userId = studentVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            studentVO.setUser(userService.getUserVO(user));
        });
        // endregion

        studentVOPage.setRecords(studentVOList);
        return studentVOPage;
    }

    @Override
    public void addStudent(Student studentAddRequest) {
        Student exist = studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getUsername, studentAddRequest.getUsername()));
        if(Objects.nonNull(exist)){
            throw new BusinessException(ErrorCode.STUDENT_IS_EXIST);
        }
        studentAddRequest.setCreatetime(LocalDateTime.now());
        studentMapper.insert(studentAddRequest);
    }

}
