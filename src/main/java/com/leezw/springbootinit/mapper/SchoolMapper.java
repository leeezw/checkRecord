package com.leezw.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leezw.springbootinit.model.entity.School;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
/**
 * @description 学校地址表Mapper
 * @author verytools
 * @date 2025-06-18
 */
@Mapper
public interface SchoolMapper extends BaseMapper<School> {

    @Select("SELECT  * from school  ")
    List<School> All();

    @Select("<script>select count(1) from school t0 " +
    //add here if need left join
    "where 1=1" +
    "<when test='id!=null and id!=&apos;&apos; '> and t0.id=#{id}</when> " +
    "<when test='name!=null and name!=&apos;&apos; '> and t0.name=#{name}</when> " +
    "<when test='createdAt!=null and createdAt!=&apos;&apos; '> and t0.created_at=#{createdAt}</when> " +
    "<when test='updatedAt!=null and updatedAt!=&apos;&apos; '> and t0.updated_at=#{updatedAt}</when> " +
    "<when test='name!=null and name!=&apos;&apos; '> and t0.name=#{name}</when> " +
    "<when test='createdAt!=null and createdAt!=&apos;&apos; '> and t0.created_at=#{createdAt}</when> " +
    "<when test='updatedAt!=null and updatedAt!=&apos;&apos; '> and t0.updated_at=#{updatedAt}</when> " +
     " </script>")
    int countAll(School queryParamDTO);



}