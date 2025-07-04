package com.kite.analyze.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kite.analyze.model.dto.student.StudentQueryRequest;
import com.kite.analyze.model.entity.Student;
import com.kite.analyze.model.vo.StudentVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户文件服务
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
public interface StudentService extends IService<Student> {

    /**
     * 校验数据
     *
     * @param student
     * @param add 对创建的数据进行校验
     */
    void validStudent(Student student, boolean add);

    /**
     * 获取查询条件
     *
     * @param studentQueryRequest
     * @return
     */
    QueryWrapper<Student> getQueryWrapper(StudentQueryRequest studentQueryRequest);
    
    /**
     * 获取用户文件封装
     *
     * @param student
     * @param request
     * @return
     */
    StudentVO getStudentVO(Student student, HttpServletRequest request);

    /**
     * 分页获取用户文件封装
     *
     * @param studentPage
     * @param request
     * @return
     */
    Page<StudentVO> getStudentVOPage(Page<Student> studentPage, HttpServletRequest request);
}
