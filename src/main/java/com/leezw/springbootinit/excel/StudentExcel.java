package com.leezw.springbootinit.excel;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class StudentExcel {

    /**
     * 身份证号
     */
    private String userid;

    /**
     * 学籍卡号
     */
    private String stuid;

    /**
     * 手机号
     */
    private String userphone;

    /**
     * 年级
     */
    private String usergrade;

    /**
     * 用户名
     */
    private String username;

    /**
     * 班级
     */
    private String userclass;

    /**
     * 学校名称
     */
    private String schoolname;

    /**
     * 学校所在地
     */
    private String schoolprovince;

    /**
     * 出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate birthday;


}
