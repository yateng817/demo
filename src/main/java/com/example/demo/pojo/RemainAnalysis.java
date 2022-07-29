package com.example.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemainAnalysis {
    String enrolSchoolName;
    String majorId;
    String majorName;
    String majorType;

    Double maxScore;//所选阶段的最高分
    Double MinScore;//所选阶段的最低分
    Integer selectEnrolNum;//所选阶段录取人数
    Integer otherEnrolNum;//其他阶段录取人数
    Integer sPlanNum;//所选阶段计划数
    Integer allPlanNum;//计划数
    String selectEnrolRate;//所选批次录取完成率
    String allEnrolRate;//所有录取完成率
    Integer sRemainNum;//当前批次剩余计划数
    Integer allRemainNum;//所有批次剩余计划数
}
