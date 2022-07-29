package com.example.demo;

import com.alibaba.excel.EasyExcel;
import com.example.demo.listener.CheckListener;
import com.example.demo.listener.StudentListener;
import com.example.demo.listener.SupplementListener;
import com.example.demo.listener.SupplementRuleListener;
import com.example.demo.mapper.*;
import com.example.demo.pojo.Check;
import com.example.demo.pojo.Student;
import com.example.demo.pojo.SupplementRule;
import com.example.demo.pojo.SupplementStu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class Chek {
    @Autowired
    private StuMapper stuMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private EnrolPlanMapper enrolPlanMapper;
    @Autowired
    private SpecialMapper specialMapper;
    @Autowired
    private EnrolResultMapper enrolResultMapper;
    @Autowired
    private SupplementRuleMapper supplementRuleMapper;


    @Test
    void checkSupplementEnrol(){
        List<Student> students = enrolResultMapper.getSupplementEnrols();

        for(Student student:students){
            String conditions = null;
            if(student.getAreaId().equals("16")){
                conditions = enrolPlanMapper.getConditions(student.getMajorId());
            }else
            {
                conditions = enrolPlanMapper.getAreaConditions(student.getMajorId());
            }

            String [] des = conditions.split(";");
            Double minTotalScore = 0d;
            minTotalScore= Double.valueOf(des[0].substring(des[0].indexOf(":")+1,des[0].indexOf(")")));
            if(student.getTotalScore()<minTotalScore)
            {
                System.out.println(student);
                System.out.println(conditions);
            }

        }
    }


    @Test
    void chkMajorId(){
        List<Check> checks = new ArrayList<>();
        EasyExcel.read("1/1.xls", Check.class, new CheckListener(checks)).sheet().doRead();
        Map<String,String> mp = new HashMap<>();
        for(Check data: checks){
            if(mp.get(data.getMajorId())==null){
                mp.put(data.getMajorId(),data.getMajorType());
            }else
            {
                if(mp.get(data.getMajorId()).equals(data.getMajorType()));
                else
                {
                    System.out.println(data.getMajorId());
                }
            }
        }
    }

    @Test
    void chkAreaEnrol(){
        List<SupplementStu> supplementStus = new ArrayList<>();
        EasyExcel.read("1/1.xlsx", SupplementStu.class, new SupplementListener(supplementStus)).sheet().doRead();

        int num =0;
        int err=0;
        for(SupplementStu data:supplementStus){
            Student student = enrolResultMapper.getEnrol(data.getLoginNo());
            Student student1 = stuMapper.getStuByLoginNo(data.getLoginNo());
            if(!student.getMajorId().equals(data.getWill1())||
                    !student.getIdentifId().equals(student1.getIdentifId())||
                    !student.getExaminationId().equals(student1.getExaminationId())||
                    !student.getName().equals(student1.getName())
            )
            {
//                System.out.println(student.getLoginNo());
                if(!student.getMajorId().equals(data.getWill1())&&student.getEnrolType()==0)
                {
                    err++;
                    System.out.println(1);
                }

                if(!student.getIdentifId().equals(student1.getIdentifId()))
                System.out.println(2);
                if(!student.getExaminationId().equals(student1.getExaminationId()))
                    System.out.println(3);
                if(!student.getName().equals(student1.getName()))
                    System.out.println(4);
            }
            if(student.getEnrolType()!=0){
                num++;
            }
        }
        System.out.println("sum:"+supplementStus.size()+" fail:"+num+" err:"+err);

    }

    @Test
    void chkYongjiang(){
        List<SupplementStu> supplementStus = new ArrayList<>();
        EasyExcel.read("1/1.xlsx", SupplementStu.class, new SupplementListener(supplementStus)).sheet().doRead();

        int num =0;
        for(SupplementStu data:supplementStus){
            Student student = enrolResultMapper.getEnrol(data.getLoginNo());
            Student student1 = stuMapper.getStuByLoginNo(data.getLoginNo());
            if(!student.getMajorId().equals(data.getWill1())||
                    !student.getIdentifId().equals(student1.getIdentifId())||
                    !student.getExaminationId().equals(student1.getExaminationId())||
                    !student.getName().equals(student1.getName())
            )
            {
                if(student.getEnrolType()!=5)
                    System.out.println(student.getLoginNo());
            }
            if(student.getEnrolType()!=5){
                num++;
            }
        }
        System.out.println("sum:"+supplementStus.size()+" fail:"+num);
    }

    @Test
    void chkNingBoSportAndZhijiao(){


        List<SupplementStu> supplementStus = new ArrayList<>();
        EasyExcel.read("1/1.xlsx", SupplementStu.class, new SupplementListener(supplementStus)).sheet().doRead();

        int num =0;
        for(SupplementStu data:supplementStus){
            Student student = enrolResultMapper.getEnrolByEid(data.getExaminationId());
            Student student1 = stuMapper.getStuByEidAndName(data.getExaminationId(), data.getName());
            if(student==null||student1==null||!student.getMajorId().equals(data.getWill1())||
                    !student.getIdentifId().equals(student1.getIdentifId())||
                    !student.getExaminationId().equals(student1.getExaminationId())||
                    !student.getName().equals(student1.getName())
            )
            {
                if(student==null)
                    System.out.println(data.getExaminationId());
                else
                    System.out.println(student.getLoginNo());
            }
            if(student==null || student.getEnrolType()!=5){
                num++;
            }
        }
        System.out.println("sum:"+supplementStus.size()+" fail:"+num);
    }

    @Test
    void chkZhijiao(){
        List<SupplementStu> supplementStus = new ArrayList<>();
        EasyExcel.read("1/1.xlsx", SupplementStu.class, new SupplementListener(supplementStus)).sheet().doRead();

        int num =0;
        for(SupplementStu data:supplementStus){
            Student student = enrolResultMapper.getEnrolByEid(data.getExaminationId());
            Student student1 = stuMapper.getStuByEidAndName(data.getExaminationId(), data.getName());
            if(student==null||!student.getMajorId().equals(data.getWill1())||
                    !student.getIdentifId().equals(data.getIdentifId())||
                    !student.getExaminationId().equals(data.getExaminationId())||
                    !student.getName().equals(data.getName())
            )
            {
                if(student==null)
                    System.out.println(data.getExaminationId());
                else
                    System.out.println(student.getLoginNo());
            }
            if(student==null || student.getEnrolType()!=6){
                num++;
            }
        }
        System.out.println("sum:"+supplementStus.size()+" fail:"+num);
    }


    @Test
    void chkSuppleRule(){
        List<SupplementRule> supplementRules = new ArrayList<>();
        EasyExcel.read("1/1.xls", SupplementRule.class, new SupplementRuleListener(supplementRules)).sheet().doRead();
        List<SupplementRule> databases = supplementRuleMapper.getAllSupplementRule();
        List<Student> enrols = enrolResultMapper.getAllEnrol();

        Double [] minCityScoreMap = new Double[1005];
        Double [] minAreaScoreMap = new Double[1005];

        for(SupplementRule data:supplementRules){
            minCityScoreMap[Integer.parseInt(data.getMajorId())] = data.getCityMinScore();
            minAreaScoreMap[Integer.parseInt(data.getMajorId())] = data.getAreaMinScore();
        }

        System.out.println(enrols.size());
        System.out.println("student");
        for(Student data:enrols){
            if(data.getEnrolType()!=6&&data.getEnrolType()!=0&&data.getEnrolType()!=5&&(data.getMemo()==null || !data.getMemo().equals("Specialty"))){
                if(data.getAreaId().equals("16")){
                    if(minCityScoreMap[Integer.parseInt(data.getMajorId())]>data.getTotalScore())
                    {
                        System.out.println(data.getExaminationId());
                    }
                }else
                {
                    if(minAreaScoreMap[Integer.parseInt(data.getMajorId())]>data.getTotalScore())
                    {
                        System.out.println(data.getExaminationId());
                    }
                }
            }
        }
        System.out.println("major");
        for(SupplementRule data:databases){
            String conditions = data.getConditions();
            String areaConditions = data.getAreaConditions();
            Double minCity = Double.valueOf(conditions.substring(conditions.indexOf(':')+1,conditions.indexOf(')')));
            Double minArea = Double.valueOf(areaConditions.substring(areaConditions.indexOf(':')+1,areaConditions.indexOf(')')));
            if(minCityScoreMap[Integer.parseInt(data.getMajorId())]-minCity >0.1||
                    minCityScoreMap[Integer.parseInt(data.getMajorId())]-minCity <-0.1||
                    minAreaScoreMap[Integer.parseInt(data.getMajorId())]-minArea>0.1||
                    minAreaScoreMap[Integer.parseInt(data.getMajorId())]-minArea<-0.1)
                System.out.println(data.getMajorId()+" city:"+minCityScoreMap[Integer.parseInt(data.getMajorId())]+"--"+minCity+" area:"+minAreaScoreMap[Integer.parseInt(data.getMajorId())]+"-"+minArea);
        }
    }

    @Test
    public void testGit(){
        System.out.println("hhh");
        System.out.println("hhn2");
        System.out.println("起飞喽");
    }


}
