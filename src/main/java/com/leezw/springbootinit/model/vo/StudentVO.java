package com.leezw.springbootinit.model.vo;

import cn.hutool.json.JSONUtil;
import com.leezw.springbootinit.model.entity.Student;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户文件视图
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
@Data
public class StudentVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 封装类转对象
     *
     * @param studentVO
     * @return
     */
    public static Student voToObj(StudentVO studentVO) {
        if (studentVO == null) {
            return null;
        }
        Student student = new Student();
        BeanUtils.copyProperties(studentVO, student);
        List<String> tagList = studentVO.getTagList();
        return student;
    }

    /**
     * 对象转封装类
     *
     * @param student
     * @return
     */
    public static StudentVO objToVo(Student student) {
        if (student == null) {
            return null;
        }
        StudentVO studentVO = new StudentVO();
        BeanUtils.copyProperties(student, studentVO);
        return studentVO;
    }
}
