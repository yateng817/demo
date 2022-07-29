package com.example.demo;

import com.alibaba.excel.EasyExcel;
import com.example.demo.listener.AdvanceListener;
import com.example.demo.mapper.AreaMapper;
import com.example.demo.mapper.EnrolPlanMapper;
import com.example.demo.mapper.SpecialMapper;
import com.example.demo.mapper.StuMapper;
import com.example.demo.pojo.AdvanceStuInfo;
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
public class AdvanceStu {

    @Autowired
    private StuMapper stuMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private EnrolPlanMapper enrolPlanMapper;
    @Autowired
    private SpecialMapper specialMapper;

    void ExportExcel(List<AdvanceStuInfo> advanceStuInfos){
        HSSFWorkbook workbook =new HSSFWorkbook();
        HSSFSheet sheet =workbook.createSheet("面试数据");
        sheet.setColumnWidth(1,40*256);
        sheet.setColumnWidth(2,35*256);
        sheet.setColumnWidth(14,40*256);
        HSSFRow row=sheet.createRow(0);
        HSSFCellStyle style =workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFCell cell=null;
        String []title={"区域","准考证号","姓名","身份证号","报考代码","专业名称","录取学校","报名序号"};

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
        for(int i=0;i<advanceStuInfos.size();i++){
            row=sheet.createRow(i+1);

            info[0]=advanceStuInfos.get(i).getAreaName();
            info[1]=advanceStuInfos.get(i).getExaminationId();
            info[2]=advanceStuInfos.get(i).getName();
            info[3]=advanceStuInfos.get(i).getIdentifId();
            info[4]=advanceStuInfos.get(i).getWill1();
            info[5]=advanceStuInfos.get(i).getMajorName();
            info[6]=advanceStuInfos.get(i).getEnrollSchoolName();
            info[7]=advanceStuInfos.get(i).getLoginNo();
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
    void getExportAdvance(){//自主招生录取
        List<AdvanceStuInfo> advanceStuInfos=new ArrayList<>();
        Student student = null;
        EasyExcel.read("1/1.xlsx", AdvanceStuInfo.class, new AdvanceListener(advanceStuInfos)).sheet().doRead();

        for(AdvanceStuInfo data : advanceStuInfos){
            String majorType=data.getMajorName().substring(data.getMajorName().indexOf('(')+1,data.getMajorName().indexOf(')'));
            String majorName=data.getMajorName().substring(0,data.getMajorName().indexOf('('));

            String enrolSchool=data.getEnrollSchoolName();
            if(majorName!=null)
                majorName= majorName+'%';
            if(enrolSchool!=null)
                enrolSchool = enrolSchool +'%';

            String will1=null;

            if(enrolPlanMapper.getMajorIdByConditionCount(majorName,majorType,enrolSchool)==1)
                will1= enrolPlanMapper.getMajorIdByCondition(majorName,majorType,enrolSchool);

            System.out.println(majorName+" "+majorType+" "+enrolSchool );
            System.out.println(will1);
            if(will1!=null)
                data.setWill1(will1);
            student = stuMapper.getStuByLoginNoAndName(data.getLoginNo(),data.getName());
            data.setAreaId(student.getAreaId());
            data.setAreaName(areaMapper.getAreaNameById(data.getAreaId()));
            data.setIdentifId(student.getIdentifId());
            data.setExaminationId(student.getExaminationId());
        }


        ExportExcel(advanceStuInfos);


    }
}
