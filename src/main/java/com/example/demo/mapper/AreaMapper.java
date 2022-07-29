package com.example.demo.mapper;

import com.example.demo.pojo.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AreaMapper {
    @Select("select area_name from area_tbl where area_id = #{id}")
    String getAreaNameById(@Param("id")String id);

    @Select("select count(*) from area_tbl where area_name = #{areaName}")
    int hasAreaName(@Param("areaName")String areaName);

    @Select("select area_id,area_name from area_tbl;")
    List<Student> getIdAndName();

}
