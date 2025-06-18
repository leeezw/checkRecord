package com.leezw.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leezw.springbootinit.annotation.AuthCheck;
import com.leezw.springbootinit.common.BaseResponse;
import com.leezw.springbootinit.common.ResultUtils;
import com.leezw.springbootinit.constant.UserConstant;
import com.leezw.springbootinit.mapper.SchoolAddressMapper;
import com.leezw.springbootinit.mapper.SchoolMapper;
import com.leezw.springbootinit.model.dto.student.StudentQueryRequest;
import com.leezw.springbootinit.model.entity.School;
import com.leezw.springbootinit.model.entity.SchoolAddress;
import com.leezw.springbootinit.model.entity.Student;
import com.leezw.springbootinit.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/student")
@Slf4j
public class SchoolController {

    @Autowired
    private SchoolMapper schoolMapper;
    @Autowired
    private SchoolAddressMapper addressMapper;

    @PostMapping("/school/list")
    public BaseResponse<List<School>> listSchool() {
        QueryWrapper<School> queryWrapper = new QueryWrapper<>();
        return ResultUtils.success(schoolMapper.selectList(queryWrapper));
    }

    @PostMapping("/address/list")
    public BaseResponse<List<SchoolAddress>> listSchoolAddress() {
        QueryWrapper<SchoolAddress> queryWrapper = new QueryWrapper<>();
        return ResultUtils.success(addressMapper.selectList(queryWrapper));
    }



}
