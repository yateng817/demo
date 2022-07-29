package com.example.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Export {
    private String examinationID;
    private String loginNo;
    private String name;
    private String resident;
    private String graduationSchool;
    private String sex;
    private String majorID;
    private String majorName;
    private String desc;
    private String identityID;
}
