package com.leezw.springbootinit.service.impl;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leezw.springbootinit.common.ErrorCode;
import com.leezw.springbootinit.constant.CommonConstant;
import com.leezw.springbootinit.excel.ExcelListener.StudentExcelListener;
import com.leezw.springbootinit.excel.StudentExcel;
import com.leezw.springbootinit.excel.StudentVoExcel;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        String searchText = studentQueryRequest.getSearchText();
        String sortField = studentQueryRequest.getSortField();
        String sortOrder = studentQueryRequest.getSortOrder();
        String userId =studentQueryRequest.getUserid();
        // todo 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(studentQueryRequest.getUsername())
                || StringUtils.isNotBlank(studentQueryRequest.getUserphone())
                || StringUtils.isNotBlank(studentQueryRequest.getStuid())
                || StringUtils.isNotBlank(userId)) {

            queryWrapper.and(qw -> {
                if (StringUtils.isNotBlank(studentQueryRequest.getUsername())) {
                    qw.like("userName", studentQueryRequest.getUsername());
                }
                if (StringUtils.isNotBlank(studentQueryRequest.getUserphone())) {
                    qw.or().like("userPhone", studentQueryRequest.getUserphone());
                }
                if (StringUtils.isNotBlank(studentQueryRequest.getStuid())) {
                    qw.or().like("stuId", studentQueryRequest.getStuid());
                }
                if (StringUtils.isNotBlank(userId)) {
                    qw.or().like("userId", userId);
                }
            });
        }
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
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
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userid", studentAddRequest.getUserid());
        boolean exist = studentMapper.exists(queryWrapper);
        if(exist){
            throw new BusinessException(ErrorCode.STUDENT_IS_EXIST);
        }

        QueryWrapper queryWrapper2 = new QueryWrapper();
        queryWrapper.eq("stuid", studentAddRequest.getUserid());
        boolean exist2 = studentMapper.exists(queryWrapper2);
        if(exist2){
            throw new BusinessException(ErrorCode.STUDENT_IS_EXIST);
        }
        LambdaQueryWrapper<Student> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Student::getStuid, studentAddRequest.getStuid());
        if(studentMapper.exists(lambdaQueryWrapper)){
            throw new BusinessException(ErrorCode.STUDENT_ID_IS_EXIST);
        }
        studentAddRequest.setCreatetime(LocalDateTime.now());
        studentMapper.insert(studentAddRequest);
    }

    @Override
    public void importExcel(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), StudentExcel.class, new StudentExcelListener(studentMapper))
                .sheet()
                .doRead();
    }

    @Override
    public void listExport(String name, HttpServletResponse response) throws IOException {
        try {
            String fileName = "个人基本情况表.xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

            List<StudentVoExcel> dataList = studentMapper.exportPage(name);
           for (StudentVoExcel item : dataList) {
               // 新增：拆分字段填充
               fillStudentVoExcelFromUserProfile(item);
            }

            EasyExcel.write(response.getOutputStream(), StudentVoExcel.class)
                    .sheet("个人基本情况")
                    .doWrite(dataList);

        } catch (Exception e) {
            log.error("导出异常：", e);
            // 终止方法，不能写回任何字符内容！
        }
    }

    private void fillStudentVoExcelFromUserProfile(StudentVoExcel item) {
        String json = item.getUserprofile();
        if (json == null || json.trim().isEmpty()) return;

        try {
            JSONObject obj = JSONObject.parseObject(json);

            // 一般检查
            if (obj.containsKey("general_check")) {
                JSONObject generalCheck = obj.getJSONObject("general_check");
                item.setSpineScoliosisGeneralCheck(String.join("，",
                        generalCheck.getJSONArray("result").toJavaList(String.class)));
            }

            // 前屈试验
            if (obj.containsKey("forward_bending_test")) {
                JSONObject bend = obj.getJSONObject("forward_bending_test");

                JSONObject thoracic = bend.getJSONObject("thoracic_section");
                item.setSpineScoliosisThoracicSection(thoracic.getString("result") + " ATR: " + thoracic.getString("atr_value"));

                JSONObject thoracolumbar = bend.getJSONObject("thoracolumbar_section");
                item.setSpineScoliosisThoracolumbarSection(thoracolumbar.getString("result") + " ATR: " + thoracolumbar.getString("atr_value"));

                JSONObject lumbar = bend.getJSONObject("lumbar_section");
                item.setSpineScoliosisLumbarSection(lumbar.getString("result") + " ATR: " + lumbar.getString("atr_value"));
            }

            // 脊柱运动实验
            if (obj.containsKey("spine_motion_test")) {
                JSONObject motion = obj.getJSONObject("spine_motion_test");
                item.setIsSpineScoliosisExerciseExperiment(motion.getString("performed"));
            }

            // 前后观检查
            if (obj.containsKey("anterior_posterior_check")) {
                JSONObject apCheck = obj.getJSONObject("anterior_posterior_check");
                item.setSpineCheck(String.join("，",
                        apCheck.getJSONArray("general_result").toJavaList(String.class)));
                item.setSpineProneTest(String.join("，",
                        apCheck.getJSONArray("prone_test_result").toJavaList(String.class)));
            }

            // 病史
            if (obj.containsKey("medical_history")) {
                item.setSpineHistory(String.join("，",
                        obj.getJSONArray("medical_history").toJavaList(String.class)));
            }

            // 不良体态
            if (obj.containsKey("bad_posture")) {
                item.setCommonPoorPostureScreening(String.join("，",
                        obj.getJSONArray("bad_posture").toJavaList(String.class)));
            }

            // 其他特殊情况
            if (obj.containsKey("other_special_situation")) {
                item.setOtherSpecialCircumstances(obj.getString("other_special_situation"));
            }

            // 筛查结果
            if (obj.containsKey("screening_result")) {
                JSONObject result = obj.getJSONObject("screening_result");
                item.setScreeningResult(String.join("，",
                        result.getJSONArray("results").toJavaList(String.class)));
            }

            // 建议
            if (obj.containsKey("suggestion")) {
                item.setSuggestion(obj.getString("suggestion"));
            }

        } catch (Exception e) {
            log.warn("解析 userprofile JSON 失败：{}", e.getMessage());
        }
    }
}

//    public String convertUserProfileToText(String json) {
//        if (json == null || json.trim().isEmpty()) return "";
//
//        try {
//            JSONObject obj = JSONObject.parseObject(json);
//            StringBuilder sb = new StringBuilder();
//
//            if (obj.containsKey("general_check")) {
//                JSONObject generalCheck = obj.getJSONObject("general_check");
//                sb.append("一般检查: ").append(generalCheck.getJSONArray("result").toJSONString()).append("\n");
//            }
//
//            if (obj.containsKey("forward_bending_test")) {
//                JSONObject bend = obj.getJSONObject("forward_bending_test");
//                sb.append("前屈试验:\n");
//                JSONObject thoracic = bend.getJSONObject("thoracic_section");
//                JSONObject thoracolumbar = bend.getJSONObject("thoracolumbar_section");
//                JSONObject lumbar = bend.getJSONObject("lumbar_section");
//
//                sb.append(" - 胸段: ").append(thoracic.getString("result")).append(" ATR: ").append(thoracic.getString("atr_value")).append("\n");
//                sb.append(" - 胸腰段: ").append(thoracolumbar.getString("result")).append(" ATR: ").append(thoracolumbar.getString("atr_value")).append("\n");
//                sb.append(" - 腰段: ").append(lumbar.getString("result")).append(" ATR: ").append(lumbar.getString("atr_value")).append("\n");
//            }
//
//            if (obj.containsKey("spine_motion_test")) {
//                JSONObject motion = obj.getJSONObject("spine_motion_test");
//                sb.append("脊柱活动度测试: \n");
//                sb.append(" - 是否检查: ").append(motion.getString("performed")).append("\n");
//                sb.append(" - 胸段 ATR: ").append(motion.getString("thoracic_atr")).append("\n");
//                sb.append(" - 胸腰段 ATR: ").append(motion.getString("thoracolumbar_atr")).append("\n");
//                sb.append(" - 腰段 ATR: ").append(motion.getString("lumbar_atr")).append("\n");
//            }
//
//            if (obj.containsKey("anterior_posterior_check")) {
//                JSONObject apCheck = obj.getJSONObject("anterior_posterior_check");
//                sb.append("前后观检查: ").append(apCheck.getJSONArray("general_result").toJSONString()).append("\n");
//                sb.append(" - 俯卧检查: ").append(apCheck.getJSONArray("prone_test_result").toJSONString()).append("\n");
//            }
//
//            if (obj.containsKey("medical_history")) {
//                sb.append("病史: ").append(obj.getJSONArray("medical_history").toJSONString()).append("\n");
//            }
//
//            if (obj.containsKey("bad_posture")) {
//                sb.append("不良姿势: ").append(obj.getJSONArray("bad_posture").toJSONString()).append("\n");
//            }
//
//            if (obj.containsKey("other_special_situation")) {
//                sb.append("其他情况: ").append(obj.getString("other_special_situation")).append("\n");
//            }
//
//            if (obj.containsKey("screening_result")) {
//                JSONObject result = obj.getJSONObject("screening_result");
//                sb.append("筛查结果: ").append(result.getJSONArray("results").toJSONString()).append("\n");
//            }
//
//            if (obj.containsKey("suggestion")) {
//                sb.append("建议: ").append(obj.getString("suggestion")).append("\n");
//            }
//
//            return sb.toString().trim();
//        } catch (Exception e) {
//            return "【数据解析失败】";
//        }
//    }
//}
