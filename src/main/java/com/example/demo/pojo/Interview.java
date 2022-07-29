package com.example.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Interview {
    int nId;            //序号
    String areaId;          //区域编号
    String areaName;         //区域名
    String loginNo;         //报名序号
    String examinationId;       //准考证号
    String name;                //姓名
    String examineeArea;     //生源地
    String gender;          //性别
    String schoolName;          //学校名
    float totalScore;           //总分
    String will1;               //志愿1
    String majorName;           //专业的名称
    String resultDescribe;      //成绩说明
    String enrollSchoolName;    //录取学校
    String reportOperator;      //上传者用户名userid
    String organizationId;      //上传者的学校Id;
    String organizationName;    //上传者的学校名;
    String identityId;
    int state;                  //上报状态,0表示保存，1表示上传，2表示已审核
}
