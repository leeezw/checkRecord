package com.leezw.springbootinit.excel;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
@ColumnWidth(20)
public class StudentVoExcel {


    /**
     * 身份证号
     */
    @ExcelProperty(value = "身份证号",index = 0)
    private String userid;

    /**
     * 学籍卡号
     */
    @ExcelProperty(value = "学籍卡号",index = 1)
    private String stuid;

    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号",index = 2)
    private String userphone;

    /**
     * 年级
     */
    @ExcelProperty(value = "年级",index = 3)
    private String usergrade;

    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名",index = 4)
    private String username;

    /**
     * 班级
     */
    @ExcelProperty(value = "班级",index = 5)
    private String userclass;

    /**
     * 学校名称
     */
    @ExcelProperty(value = "学校名称",index = 6)
    private String schoolname;

    /**
     * 学校所在地
     */
    @ExcelProperty(value = "学校所在地",index = 7)
    private String schoolprovince;

    /**
     * 学校所在地
     */
    @ExcelProperty(value = "性别",index = 8)
    private String schoolarea;

    /**
     * 出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ExcelProperty(value = "出生日期",index = 9)
    private LocalDate birthday;

    /**
     * 检查日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ExcelProperty(value = "检查日期",index = 10)
    private LocalDate checkday;

    /**
     * 用户简介
     */
    @ExcelIgnore
    private String userprofile; // 原始 JSON 数据


    @ExcelProperty(value = "一般检查",index = 11)
    private String spineScoliosisGeneralCheck;

    @ExcelProperty(value = "胸段",index = 12)
    private String spineScoliosisThoracicSection;

    @ExcelProperty(value = "腰胸段",index = 13)
    private String spineScoliosisThoracolumbarSection;

    @ExcelProperty(value = "腰段",index = 14)
    private String spineScoliosisLumbarSection;

    @ExcelProperty(value = "是否进行脊柱运动实验",index = 15)
    private String isSpineScoliosisExerciseExperiment;

    @ExcelProperty(value = "一般检查",index = 16)
    private String spineCheck;

    @ExcelProperty(value = "俯卧试验",index = 17)
    private String spineProneTest;

    @ExcelProperty(value = "病史",index = 18)
    private String spineHistory;

    @ExcelProperty(value = "常见不良体态筛查",index = 19)
    private String commonPoorPostureScreening;

    @ExcelProperty(value = "其他特殊情况",index = 20)
    private String otherSpecialCircumstances;

    @ExcelProperty(value = "筛查结果",index = 21)
    private String screeningResult;

    @ExcelProperty(value = "建议",index = 22)
    private String suggestion;


}
