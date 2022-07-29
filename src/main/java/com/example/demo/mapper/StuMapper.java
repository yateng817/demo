package com.example.demo.mapper;


import com.example.demo.pojo.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface StuMapper {
    @Select("SELECT * from big_city_stu_info_tbl where login_no = #{no}")
    Student getStuByLoginNo(@Param("no") String no);

    @Select("select * from big_city_stu_info_tbl where examination_id = #{id} and name =#{name}")
    Student getStuByEidAndName(@Param("id")String id,@Param("name")String name);

    @Select("SELECT * from big_city_stu_info_tbl where identif_id = #{no}")
    Student getStuById(@Param("no") String no);

    @Select("SELECT * from big_city_stu_info_tbl where login_no = #{no} and identif_id = #{id}")
    Student getStuByLoginNoAndId(@Param("no") String no,@Param("id") String id);

    @Select("SELECT * from big_city_stu_info_tbl where login_no = #{no} AND name = #{name}")
    Student getStuByLoginNoAndName(@Param("no") String no,@Param("name")String name);

    @Update("UPDATE big_city_stu_info_tbl \n" +
            "SET  yw=#{stu.yw},sx=#{stu.sx},yy=#{stu.yy},zr=#{stu.zr},sh=#{stu.sh},ty=#{stu.ty},additional_score=#{stu.additionalScore},total_score =#{stu.totalScore}\n" +
            "WHERE login_no =#{stu.loginNo} AND name =#{stu.name};")
    int addScore(@Param("stu")Student student);

    @Update("UPDATE big_city_stu_info_tbl \n" +
            "SET  yw=#{stu.yw},sx=#{stu.sx},yy=#{stu.yy},zr=#{stu.zr},sh=#{stu.sh},ty=#{stu.ty},additional_score=#{stu.additionalScore},total_score =#{stu.totalScore}\n" +
            "where examination_id = #{stu.examinationId} and name = #{stu.name} and identif_id =#{stu.identifId};")
    int addScore2(@Param("stu")Student student);

}
