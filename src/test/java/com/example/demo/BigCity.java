package com.example.demo;

import com.alibaba.excel.EasyExcel;
import com.example.demo.listener.StudentListener;
import com.example.demo.mapper.AreaMapper;
import com.example.demo.mapper.EnrolPlanMapper;
import com.example.demo.mapper.SpecialMapper;
import com.example.demo.mapper.StuMapper;
import com.example.demo.pojo.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class BigCity {
    @Autowired
    private StuMapper stuMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private EnrolPlanMapper enrolPlanMapper;
    @Autowired
    private SpecialMapper specialMapper;

    @Test
    void updateStuScore(){//更新大市基本表中的学生数据
        List<Student> students = new ArrayList<>();
        EasyExcel.read("1/1.xlsx", Student.class, new StudentListener(students)).sheet().doRead();
        for(Student student : students){
            if(stuMapper.addScore2(student)!=1){
                System.out.println(student.getExaminationId());
            }
        }
    }
}
