package com.example.demo.mapper;

import com.example.demo.pojo.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EnrolResultMapper {

    @Select("select DISTINCT major_id from enrol_result_tbl;")
    List<String> getAllMajorid();


    @Select("SELECT COUNT(*) FROM enrol_result_tbl where login_no = #{no}")
    int getEnrolNumByLoginNO(@Param("no")String no);

    @Select("SELECT * FROM enrol_result_tbl where login_no = #{no}")
    Student getEnrol(@Param("no")String no);

    @Select("select * from enrol_result_tbl where examination_id = #{id}")
    Student getEnrolByEid(@Param("id")String id);

    @Select("select * from enrol_result_tbl")
    List<Student> getAllEnrol();


    @Select("select major_id from enrol_result_tbl where examination_id = #{id}")
    String getMajorId(@Param("id")String id);



    @Select("select * from enrol_result_tbl ORDER BY enrol_school_name desc , major_id asc;")
    List<Student> getAllEnrolSort();

    @Select("select DISTINCT enrol_school_name from enrol_result_tbl ORDER BY enrol_school_name desc;")
    List<String> getAllEnrolSchoolSort();

    @Select("select * from enrol_result_tbl where enrol_type = 5")
    List<Student> getSupplementEnrols();
}
