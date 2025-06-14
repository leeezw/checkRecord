package com.leezw.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leezw.springbootinit.model.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
/**
 * @description 用户Mapper
 * @author verytools
 * @date 2025-06-14
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    @Select(
    "<script>select t0.* from student t0 " +
    //add here if need left join
    "where 1=1" +
    "<when test='id!=null and id!=&apos;&apos; '> and t0.id=#{id}</when> " +
    "<when test='userid!=null and userid!=&apos;&apos; '> and t0.userid=#{userid}</when> " +
    "<when test='stuid!=null and stuid!=&apos;&apos; '> and t0.stuid=#{stuid}</when> " +
    "<when test='userphone!=null and userphone!=&apos;&apos; '> and t0.userphone=#{userphone}</when> " +
    "<when test='usergrade!=null and usergrade!=&apos;&apos; '> and t0.usergrade=#{usergrade}</when> " +
    "<when test='username!=null and username!=&apos;&apos; '> and t0.username=#{username}</when> " +
    "<when test='userclass!=null and userclass!=&apos;&apos; '> and t0.userclass=#{userclass}</when> " +
    "<when test='schoolname!=null and schoolname!=&apos;&apos; '> and t0.schoolname=#{schoolname}</when> " +
    "<when test='schoolprovince!=null and schoolprovince!=&apos;&apos; '> and t0.schoolprovince=#{schoolprovince}</when> " +
    "<when test='schoolarea!=null and schoolarea!=&apos;&apos; '> and t0.schoolarea=#{schoolarea}</when> " +
    "<when test='schoolcity!=null and schoolcity!=&apos;&apos; '> and t0.schoolcity=#{schoolcity}</when> " +
    "<when test='schooltown!=null and schooltown!=&apos;&apos; '> and t0.schooltown=#{schooltown}</when> " +
    "<when test='schooladdress!=null and schooladdress!=&apos;&apos; '> and t0.schooladdress=#{schooladdress}</when> " +
    "<when test='birthday!=null and birthday!=&apos;&apos; '> and t0.birthday=#{birthday}</when> " +
    "<when test='checkday!=null and checkday!=&apos;&apos; '> and t0.checkday=#{checkday}</when> " +
    "<when test='userprofile!=null and userprofile!=&apos;&apos; '> and t0.userprofile=#{userprofile}</when> " +
    "<when test='createtime!=null and createtime!=&apos;&apos; '> and t0.createtime=#{createtime}</when> " +
    "<when test='updatetime!=null and updatetime!=&apos;&apos; '> and t0.updatetime=#{updatetime}</when> " +
    "<when test='isdelete!=null and isdelete!=&apos;&apos; '> and t0.isdelete=#{isdelete}</when> " +
    //add here if need page limit
    //" limit ${page},${limit} " +
    " </script>")
    List<Student> pageAll(Student queryParamDTO,int page,int limit);

    @Select("<script>select count(1) from student t0 " +
    //add here if need left join
    "where 1=1" +
    "<when test='id!=null and id!=&apos;&apos; '> and t0.id=#{id}</when> " +
    "<when test='userid!=null and userid!=&apos;&apos; '> and t0.userid=#{userid}</when> " +
    "<when test='stuid!=null and stuid!=&apos;&apos; '> and t0.stuid=#{stuid}</when> " +
    "<when test='userphone!=null and userphone!=&apos;&apos; '> and t0.userphone=#{userphone}</when> " +
    "<when test='usergrade!=null and usergrade!=&apos;&apos; '> and t0.usergrade=#{usergrade}</when> " +
    "<when test='username!=null and username!=&apos;&apos; '> and t0.username=#{username}</when> " +
    "<when test='userclass!=null and userclass!=&apos;&apos; '> and t0.userclass=#{userclass}</when> " +
    "<when test='schoolname!=null and schoolname!=&apos;&apos; '> and t0.schoolname=#{schoolname}</when> " +
    "<when test='schoolprovince!=null and schoolprovince!=&apos;&apos; '> and t0.schoolprovince=#{schoolprovince}</when> " +
    "<when test='schoolarea!=null and schoolarea!=&apos;&apos; '> and t0.schoolarea=#{schoolarea}</when> " +
    "<when test='schoolcity!=null and schoolcity!=&apos;&apos; '> and t0.schoolcity=#{schoolcity}</when> " +
    "<when test='schooltown!=null and schooltown!=&apos;&apos; '> and t0.schooltown=#{schooltown}</when> " +
    "<when test='schooladdress!=null and schooladdress!=&apos;&apos; '> and t0.schooladdress=#{schooladdress}</when> " +
    "<when test='birthday!=null and birthday!=&apos;&apos; '> and t0.birthday=#{birthday}</when> " +
    "<when test='checkday!=null and checkday!=&apos;&apos; '> and t0.checkday=#{checkday}</when> " +
    "<when test='userprofile!=null and userprofile!=&apos;&apos; '> and t0.userprofile=#{userprofile}</when> " +
    "<when test='createtime!=null and createtime!=&apos;&apos; '> and t0.createtime=#{createtime}</when> " +
    "<when test='updatetime!=null and updatetime!=&apos;&apos; '> and t0.updatetime=#{updatetime}</when> " +
    "<when test='isdelete!=null and isdelete!=&apos;&apos; '> and t0.isdelete=#{isdelete}</when> " +
     " </script>")
    int countAll(Student queryParamDTO);

}