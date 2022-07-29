package com.example.demo.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplementRule {
    int id;         //序号
    @ExcelProperty("报考代码")
    String majorId;            //专业代码
    @ExcelProperty("专业名称")
    String majorName;           //专业名称
    @ExcelProperty("专业类型")
    String majorType;
    String conditions;           //条件类型
    String areaConditions;           //条件类型
    String memo1;           //备注1
    String memo2;           //备注2
    @ExcelProperty("学校名称")
    String organizationName;            //学校名称
    String admissionMinScore;           //录取最低分
    String areaAdmissionMinScore;           //录取最低分
    String englishMinScore;             //英语最低分
    String interview;                   //面试
    String manToWomanRate;              //男女比例
    String rate;                        //比例
    String areaName;                    //地区
    int period;                         //批次编号
    int areaType;//跨区还是直属
    @ExcelProperty("城区最低分")
    Double cityMinScore;
    @ExcelProperty("跨区最低分")
    Double areaMinScore;
    String profession;                  //批次
}
