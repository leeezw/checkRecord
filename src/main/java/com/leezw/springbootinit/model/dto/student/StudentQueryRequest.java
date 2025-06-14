package com.leezw.springbootinit.model.dto.student;

import com.leezw.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询用户文件请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StudentQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;


    /**
     * 搜索词
     */
    private String searchText;


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
     * 用户名
     */
    private String username;


    private static final long serialVersionUID = 1L;
}