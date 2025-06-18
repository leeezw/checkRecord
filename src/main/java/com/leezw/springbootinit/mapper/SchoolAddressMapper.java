package com.leezw.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leezw.springbootinit.model.entity.School;
import com.leezw.springbootinit.model.entity.SchoolAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
/**
 * @description 学校地址表Mapper
 * @author verytools
 * @date 2025-06-18
 */
@Mapper
public interface SchoolAddressMapper extends BaseMapper<SchoolAddress> {

    @Select(
    "<script>select t0.* from SchoolAddress t0 " +
    //add here if need left join
    "where 1=1" +
    "<when test='id!=null and id!=&apos;&apos; '> and t0.id=#{id}</when> " +
    "<when test='name!=null and name!=&apos;&apos; '> and t0.name=#{name}</when> " +
    "<when test='createdAt!=null and createdAt!=&apos;&apos; '> and t0.created_at=#{createdAt}</when> " +
    "<when test='updatedAt!=null and updatedAt!=&apos;&apos; '> and t0.updated_at=#{updatedAt}</when> " +
    //add here if need page limit
    //" limit ${page},${limit} " +
    " </script>")
    List<SchoolAddress> pageAll(SchoolAddress queryParamDTO,int page,int limit);

    @Select("SELECT  * from school  ")
    List<SchoolAddress> All();

    @Select("<script>select count(1) from SchoolAddress t0 " +
    //add here if need left join
    "where 1=1" +
    "<when test='id!=null and id!=&apos;&apos; '> and t0.id=#{id}</when> " +
    "<when test='name!=null and name!=&apos;&apos; '> and t0.name=#{name}</when> " +
    "<when test='createdAt!=null and createdAt!=&apos;&apos; '> and t0.created_at=#{createdAt}</when> " +
    "<when test='updatedAt!=null and updatedAt!=&apos;&apos; '> and t0.updated_at=#{updatedAt}</when> " +
     " </script>")
    int countAll(SchoolAddress queryParamDTO);

}