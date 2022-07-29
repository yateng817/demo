package com.example.demo.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Check {
    @ExcelProperty("中职志愿代码")
    private String majorId;
    @ExcelProperty("专业性质")
    private String majorType;
}
