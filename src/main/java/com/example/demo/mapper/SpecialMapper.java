package com.example.demo.mapper;

import com.example.demo.pojo.SpecialtyStuInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SpecialMapper {

    @Select("select * from special_stu_apply_tbl;")
    List<SpecialtyStuInfo> getAllSpacialty();
}
