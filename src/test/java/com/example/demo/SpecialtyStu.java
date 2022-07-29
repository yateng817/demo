package com.example.demo;

import com.alibaba.excel.EasyExcel;
import com.example.demo.listener.SpecialityListener;
import com.example.demo.mapper.AreaMapper;
import com.example.demo.mapper.EnrolPlanMapper;
import com.example.demo.mapper.SpecialMapper;
import com.example.demo.mapper.StuMapper;
import com.example.demo.pojo.Export;
import com.example.demo.pojo.SpecialtyStuInfo;
import com.example.demo.pojo.Student;
import org.apache.poi.hssf.usermodel.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class SpecialtyStu {
    @Autowired
    private StuMapper stuMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private EnrolPlanMapper enrolPlanMapper;
    @Autowired
    private SpecialMapper specialMapper;

    void ExportSpecial(List<SpecialtyStuInfo> specialtyStuInfoList){
        HSSFWorkbook workbook =new HSSFWorkbook();
        HSSFSheet sheet =workbook.createSheet("面试数据");
        sheet.setColumnWidth(1,40*256);
        sheet.setColumnWidth(2,35*256);
        sheet.setColumnWidth(14,40*256);
        HSSFRow row=sheet.createRow(0);
        HSSFCellStyle style =workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFCell cell=null;
        String []title={"区域","报名序号","姓名","性别","身份证号","准考证号","初中毕业学校","特长成绩","特长项目","语文","数学","英语","自然","社会","体育","中考成绩","α","综合成绩","志愿填报","加分","专业名称","学校","结果"};

        /**
         * 为excel的标题设置样式
         */
        for(int i=0;i<title.length;i++){
            cell=row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }
        /**
         * 封装每一条招生计划修改数据
         */
        String []info=new String[title.length];
        for(int i=0;i<specialtyStuInfoList.size();i++){
            row=sheet.createRow(i+1);

            info[0] = specialtyStuInfoList.get(i).getAreaName();
            info[1] = specialtyStuInfoList.get(i).getLoginNo();
            info[2] = specialtyStuInfoList.get(i).getName();
            info[3] = specialtyStuInfoList.get(i).getGender();
            info[4] = specialtyStuInfoList.get(i).getIdentifId();
            info[5] = specialtyStuInfoList.get(i).getExaminationId();
            info[6] = specialtyStuInfoList.get(i).getGratuatedSchoolName();
            info[7] = String.valueOf(specialtyStuInfoList.get(i).getSpecialScore());
            info[8] = specialtyStuInfoList.get(i).getSpecialType();
            info[9] = String.valueOf(specialtyStuInfoList.get(i).getYw());
            info[10] = String.valueOf(specialtyStuInfoList.get(i).getSx());
            info[11] = String.valueOf(specialtyStuInfoList.get(i).getYy());
            info[12] = String.valueOf(specialtyStuInfoList.get(i).getZr());
            info[13] = String.valueOf(specialtyStuInfoList.get(i).getSh());
            info[14] = String.valueOf(specialtyStuInfoList.get(i).getTy());
            info[15] = String.valueOf(specialtyStuInfoList.get(i).getTotalScore());
            info[16] = String.valueOf(specialtyStuInfoList.get(i).getAlpha());
            info[18] = specialtyStuInfoList.get(i).getWill1();
            info[19] = String.valueOf(specialtyStuInfoList.get(i).getAdditionalScore());
            info[20] = specialtyStuInfoList.get(i).getMajorName();
            info[21] = specialtyStuInfoList.get(i).getEnrollSchoolName();
            info[22] = specialtyStuInfoList.get(i).getOtherDesc();
            for(int j=0;j<title.length;j++){
                cell=row.createCell(j);
                cell.setCellValue(info[j]);
                cell.setCellStyle(style);
            }
        }
        try {
            File file = new File("1/2.xls");
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void getExportSpecial2(){//特长生有分数的版本
        List<SpecialtyStuInfo> specialtyStuInfoList = new ArrayList<>();
        EasyExcel.read("1/1.xls", SpecialtyStuInfo.class, new SpecialityListener(specialtyStuInfoList)).sheet().doRead();

        for(SpecialtyStuInfo data:specialtyStuInfoList){
            Student student = stuMapper.getStuByLoginNo(data.getLoginNo());
            String areaName = areaMapper.getAreaNameById(student.getAreaId());

            data.setAreaName(areaName.trim());
            data.setGender(student.getGender());
            data.setIdentifId(student.getIdentifId());
            data.setExaminationId(student.getExaminationId());
            data.setGratuatedSchoolName(student.getGratuatedSchoolName());
            data.setOtherDesc("合格");
        }

        ExportSpecial(specialtyStuInfoList);
    }

    @Test
    void getExportSpecial(){//特长生excel转换模板，没有分数的版本
        List<SpecialtyStuInfo> specialtyStuInfoList = new ArrayList<>();
        EasyExcel.read("1/1.xls", SpecialtyStuInfo.class, new SpecialityListener(specialtyStuInfoList)).sheet().doRead();

        for(SpecialtyStuInfo data:specialtyStuInfoList){

            Student student = stuMapper.getStuByLoginNo(data.getLoginNo());

            String areaName = areaMapper.getAreaNameById(student.getAreaId());

            data.setAreaName(areaName.trim());
            data.setGender(student.getGender());
            data.setIdentifId(student.getIdentifId());
            data.setExaminationId(student.getExaminationId());
            data.setGratuatedSchoolName(student.getGratuatedSchoolName());
            data.setYw(student.getYw());
            data.setSx(student.getSx());
            data.setYy(student.getYy());
            data.setZr(student.getZr());
            data.setSh(student.getSh());
            data.setTy(student.getTy());
            data.setTotalScore(student.getTotalScore());
            data.setOtherDesc("合格");
        }

        ExportSpecial(specialtyStuInfoList);

    };

}
