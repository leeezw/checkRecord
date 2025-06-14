package com.leezw.springbootinit.model.entity;

import lombok.Data;
import java.util.Date;
import java.util.List;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * @description 用户
 * @author verytools
 * @date 2025-06-14
 */
@Data
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
    * id
    */
    private Long id;

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
    * 学校所在地
    */
    private String schoolarea;

    /**
    * 学校所在地
    */
    private String schoolcity;

    /**
    * 学校所在地
    */
    private String schooltown;

    /**
    * 学校所在地
    */
    private String schooladdress;

    /**
    * 出生日期
    */
    private String birthday;

    /**
    * 检查日期
    */
    private String checkday;

    /**
    * 用户简介
    */
    private String userprofile;

    /**
    * 创建时间
    */
    private Date createtime;

    /**
    * 更新时间
    */
    private Date updatetime;

    /**
    * 是否删除
    */
    private Integer isdelete;

    public Student() {}
}
