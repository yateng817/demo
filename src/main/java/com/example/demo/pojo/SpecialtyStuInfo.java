package com.example.demo.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecialtyStuInfo {

    int nId;        //序号
    String areaId;      //区域
    @ExcelProperty("区域")
    String areaName;    //区域名
    @ExcelProperty("报名序号")
    String loginNo;       //报名序号
    @ExcelProperty("姓名")
    String name;        //姓名
    String examineeArea;        //生源地
    @ExcelProperty("性别")
    String gender;          //性别
    @ExcelProperty("初中毕业学校")
    String gratuatedSchoolName;     //初中毕业学校
    @ExcelProperty("中职志愿代码")
    String will1;           //专业代码
    @ExcelProperty("专业名称")
    String majorName;       //专业名称
    @ExcelProperty("特长项目")
    String specialType;     //特长类别
    String tel;//联系电话
    String address;
    String resident;//生源地
    @ExcelProperty("特长成绩")
    float specialScore;     //特长测试成绩
    String scoreDescribe;   //特长成绩说明
    int state;              //当前状态
    String userId;             //操作者
    @ExcelProperty("学校")
    String enrollSchoolName;    //录取学校
    @ExcelProperty("语文")
    float yw;       //语文成绩
    @ExcelProperty("数学")
    float sx;          //数学成绩
    @ExcelProperty("英语")
    float yy;       //英语成绩
    @ExcelProperty("自然")
    float zr;       //自然成绩
    @ExcelProperty("社会")
    float sh;       //社会成绩
    @ExcelProperty("体育")
    float ty;        //体育成绩
    @ExcelProperty("加分")
    float additionalScore;          //照顾分
    @ExcelProperty("中考成绩")
    float totalScore;         //中考成绩分
    @ExcelProperty("α")
    float alpha;     //α系数
    @ExcelProperty("综合成绩")
    float synthesisScore;    //综合成绩
    String identifId;     //身份证号
    @ExcelProperty("准考证号")
    String examinationId;  //准考证号
    @ExcelProperty("结果")
    String otherDesc;
}
