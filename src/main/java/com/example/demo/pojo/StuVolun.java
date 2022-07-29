package com.example.demo.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StuVolun {
    int nId;           //序号
    String areaId;          //区域
    @ExcelProperty("区县")
    String areaName;            //区域名称
    @ExcelProperty("准考证号")
    String examinationId;      //准考证号
    @ExcelProperty("姓名")
    String name;            //姓名
    @ExcelProperty("性别")
    String gender;                //性别
    @ExcelProperty("生源地")
    String resident;
    @ExcelProperty("联系方式")
    String tel;           //联系电话
    @ExcelProperty("家庭地址")
    String address;         //家庭住址
    @ExcelProperty("身份证号")
    String identifId;  //身份证号
    @ExcelProperty("学校")
    String organizationName;            //录取学校
    @ExcelProperty("专业名称")
    String majorName;               //录取专业名称
    @ExcelProperty("报考代码")
    String majorId;             //录取专业代码
    @ExcelProperty("语文")
    float yw;       //语文成绩
    @ExcelProperty("数学")
    float sx;       //数学成绩
    @ExcelProperty("英语")
    float yy;       //英语成绩
    @ExcelProperty("科学")
    float zr;       //自然成绩
    @ExcelProperty("社会")
    float sh;       //社会成绩
    @ExcelProperty("体育")
    float ty;       //体育成绩
    @ExcelProperty("加分")
    float additionalScore;    //照顾分
    @ExcelProperty("总分")
    float totalScore;         //总分
    @ExcelProperty("志愿代码")
    String will1;      //志愿1
    String will2;      //志愿2
    String will3;      //志愿3
    String will4;      //志愿4
    String will5;      //志愿5
    String will6;      //志愿6
    String will7;      //志愿7
    String will8;      //志愿8
    String will9;      //志愿9
    String will10;      //志愿10
    @ExcelProperty("报名序号")
    String loginNo;         //报名序号
    int state;               //数据状态
    String memo;         //备注
    int willType;           //批次
    int interviewNumber;                //面试数
    String interviewResult;             //面试结果
    String userId;          //操作者
    // String organizationId;      //上报学校的id
    int willNo;
}
