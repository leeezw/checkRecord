package com.leezw.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leezw.springbootinit.annotation.AuthCheck;
import com.leezw.springbootinit.common.BaseResponse;
import com.leezw.springbootinit.common.DeleteRequest;
import com.leezw.springbootinit.common.ErrorCode;
import com.leezw.springbootinit.common.ResultUtils;
import com.leezw.springbootinit.constant.UserConstant;
import com.leezw.springbootinit.exception.BusinessException;
import com.leezw.springbootinit.exception.ThrowUtils;
import com.leezw.springbootinit.model.dto.student.StudentAddRequest;
import com.leezw.springbootinit.model.dto.student.StudentEditRequest;
import com.leezw.springbootinit.model.dto.student.StudentQueryRequest;
import com.leezw.springbootinit.model.dto.student.StudentUpdateRequest;
import com.leezw.springbootinit.model.entity.Student;
import com.leezw.springbootinit.model.entity.User;
import com.leezw.springbootinit.model.vo.StudentVO;
import com.leezw.springbootinit.service.StudentService;
import com.leezw.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 用户文件接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
@RestController
@RequestMapping("/student")
@Slf4j
public class StudentController {

    @Resource
    private StudentService studentService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建用户文件
     *
     * @param studentAddRequest
     * @return
     */
    @PostMapping("/add")
    public BaseResponse addStudent(@RequestBody Student studentAddRequest,HttpServletRequest request) {
        ThrowUtils.throwIf(studentAddRequest == null, ErrorCode.PARAMS_ERROR);
        userService.getLoginUser(request);
        studentService.addStudent(studentAddRequest);
        return ResultUtils.success();
    }

    /**
     * 删除用户文件
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteStudent(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Student oldStudent = studentService.getById(id);
        ThrowUtils.throwIf(oldStudent == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldStudent.getUserid().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = studentService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新用户文件（仅管理员可用）
     *
     * @param studentUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateStudent(@RequestBody StudentUpdateRequest studentUpdateRequest) {
        if (studentUpdateRequest == null || studentUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Student student = new Student();
        BeanUtils.copyProperties(studentUpdateRequest, student);
        // 数据校验
        studentService.validStudent(student, false);
        // 判断是否存在
        long id = studentUpdateRequest.getId();
        Student oldStudent = studentService.getById(id);
        ThrowUtils.throwIf(oldStudent == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = studentService.updateById(student);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取用户文件（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<StudentVO> getStudentVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Student student = studentService.getById(id);
        ThrowUtils.throwIf(student == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(studentService.getStudentVO(student, request));
    }

    /**
     * 分页获取用户文件列表（仅管理员可用）
     *
     * @param studentQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Student>> listStudentByPage(@RequestBody StudentQueryRequest studentQueryRequest) {
        long current = studentQueryRequest.getCurrent();
        long size = studentQueryRequest.getPageSize();
        // 查询数据库
        Page<Student> studentPage = studentService.page(new Page<>(current, size),
                studentService.getQueryWrapper(studentQueryRequest));
        return ResultUtils.success(studentPage);
    }

    /**
     * 分页获取用户文件列表（封装类）
     *
     * @param studentQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<StudentVO>> listStudentVOByPage(@RequestBody StudentQueryRequest studentQueryRequest,
                                                               HttpServletRequest request) {
        long current = studentQueryRequest.getCurrent();
        long size = studentQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Student> studentPage = studentService.page(new Page<>(current, size),
                studentService.getQueryWrapper(studentQueryRequest));
        // 获取封装类
        return ResultUtils.success(studentService.getStudentVOPage(studentPage, request));
    }

    /**
     * 分页获取当前登录用户创建的用户文件列表
     *
     * @param studentQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<StudentVO>> listMyStudentVOByPage(@RequestBody StudentQueryRequest studentQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(studentQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        studentQueryRequest.setUserId(loginUser.getId());
        long current = studentQueryRequest.getCurrent();
        long size = studentQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Student> studentPage = studentService.page(new Page<>(current, size),
                studentService.getQueryWrapper(studentQueryRequest));
        // 获取封装类
        return ResultUtils.success(studentService.getStudentVOPage(studentPage, request));
    }

    /**
     * 编辑用户文件（给用户使用）
     *
     * @param studentEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit/{id}")
    public BaseResponse<Boolean> editStudent(@PathVariable Long id,@RequestBody Student studentEditRequest, HttpServletRequest request) {
        if (studentEditRequest == null || id <= 0L) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Student oldStudent = studentService.getById(id);
        ThrowUtils.throwIf(oldStudent == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldStudent.getUserid().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        studentEditRequest.setId(id);
        boolean result = studentService.updateById(studentEditRequest);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion

    @PostMapping("/import")
    public BaseResponse<?> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        studentService.importExcel(file);
        return ResultUtils.success("导入成功");
    }
}
