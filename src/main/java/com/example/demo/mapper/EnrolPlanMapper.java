package com.example.demo.mapper;

import com.example.demo.pojo.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EnrolPlanMapper {

    @Select("select major_id,major_name,enrol_quantity,enrol_describe,organization_name,major_type from enrol_plan_tbl;")
    List<Student> getAllmajor();


    @Select("select * from enrol_plan_tbl where organization_name LIKE #{enrolSchool} AND major_name LIKE #{majorName} AND major_type = #{majorType} AND ((major_id >100 && major_id<200)||(major_id>700)&&(major_id<800))")
    String getMajorIdByCondition(@Param("majorName")String majorName,@Param("majorType")String majorType,@Param("enrolSchool")String enrolSchool);
    @Select("select count(*) from enrol_plan_tbl where organization_name LIKE #{enrolSchool} AND major_name LIKE #{majorName} AND major_type = #{majorType} AND ((major_id >100 && major_id<200)||(major_id>700)&&(major_id<800))")
    int getMajorIdByConditionCount(@Param("majorName")String majorName,@Param("majorType")String majorType,@Param("enrolSchool")String enrolSchool);

    @Select("select major_name from enrol_plan_tbl where major_id = #{id}")
    String getMajorNameById(@Param("id")String id);

    @Select("select organization_name from enrol_plan_tbl where major_id = #{id};")
    String getSchoolNameByMajorId(@Param("id")String id);

    @Select("select area_conditions from supplement_rule_tbl where major_id = #{major}")
    String getAreaConditions(@Param("major")String major);

    @Select("select conditions from supplement_rule_tbl where major_id = #{major}")
    String getConditions(@Param("major")String major);
}
