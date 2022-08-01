package com.example.demo.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvanceStuInfo {
    @ExcelProperty("序号")
    int nId;            //序号
    String areaId;          //区域编号
    @ExcelProperty("区域")
    String areaName;         //区域名
    @ExcelProperty("准考证号")
    String examinationId;       //准考证号
    @ExcelProperty("姓名")
    String name;                //姓名
    String gender;              //性别
    String resident;
    String examineeArea;     //生源地
    @ExcelProperty("报名序号")
    String loginNo;          //报名序号
    String tel;             //联系电话
    String address;         //家庭住址
    @ExcelProperty("身份证号")
    String identifId;           //身份证号
    @ExcelProperty("录取学校")
    String gratuatedSchoolName;          //初中毕业学校名
    float yw;                   //各门课的成绩
    float sx;
    float yy;
    float zr;
    float sh;
    float ty;
    float additionalScore;
    float listenSpeakScore;        //听力口语
    @ExcelProperty("成绩")
    float totalScore;           //总分
    @ExcelProperty("录取志愿")
    String will1;               //志愿1
    @ExcelProperty("专业名称")
    String majorName;           //专业的名称
    @ExcelProperty("结果")
    String otherDesc;            //其他说明
    String refuseReason;      //审核结果
    String memo;        //备注
    String userId;      //上传者用户名userid
    String userName;    //操作者name
    @ExcelProperty("录取学校")
    String enrollSchoolName;    //录取学校名
    int state;                  //上报状态,0表示保存，1表示上传，2表示已审核
    int flag;                   //判断是否在全大市应届初中信息表中存在
}
