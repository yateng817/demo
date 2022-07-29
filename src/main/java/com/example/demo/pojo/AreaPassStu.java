package com.example.demo.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaPassStu {
    int nId;            //序号
    String areaId;          //区域编号
    @ExcelProperty("生源地")
    String areaName;         //区域名
    @ExcelProperty("准考证号")
    String examinationId;       //准考证号
    @ExcelProperty("姓名")
    String name;                //姓名
    @ExcelProperty("性别")
    String gender;              //性别
    String resident;
    String examineeArea;     //生源地
    @ExcelProperty("报名序号")
    String loginNo;          //报名序号
    String tel;             //联系电话
    String address;         //家庭住址
    @ExcelProperty("身份证号")
    String identifId;           //身份证号
    @ExcelProperty("初中毕业学校")
    String gratuatedSchoolName;          //初中毕业学校名
    @ExcelProperty("语文")
    float yw;                   //各门课的成绩
    @ExcelProperty("数学")
    float sx;
    @ExcelProperty("英语")
    float yy;
    @ExcelProperty("科学")
    float zr;
    @ExcelProperty("社会")
    float sh;
    @ExcelProperty("体育")
    float ty;
    @ExcelProperty("加分")
    float additionalScore;
    @ExcelProperty("听力口语")
    float listenSpeakScore;        //听力口语
    @ExcelProperty("总分")
    float totalScore;           //总分
    @ExcelProperty("报考代码")
    String will1;               //志愿1
    @ExcelProperty("专业名称")
    String majorName;           //专业的名称
    String otherDesc;            //其他说明
    String refuseReason;      //审核结果
    String memo;        //备注
    String userId;      //上传者用户名userid
    String userName;    //操作者name
    @ExcelProperty("学校名称")
    String enrollSchoolName;    //录取学校名
    int state;                  //上报状态,0表示保存，1表示上传，2表示已审核
    int flag;                   //判断是否在全大市应届初中信息表中存在
}
