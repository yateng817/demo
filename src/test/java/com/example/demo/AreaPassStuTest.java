package com.example.demo;

import com.alibaba.excel.EasyExcel;
import com.example.demo.listener.AdvanceListener;
import com.example.demo.listener.AreaPassListener;
import com.example.demo.mapper.AreaMapper;
import com.example.demo.mapper.EnrolPlanMapper;
import com.example.demo.mapper.StuMapper;
import com.example.demo.pojo.AdvanceStuInfo;
import com.example.demo.pojo.AreaPassStu;
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
public class AreaPassStuTest {

    @Autowired
    private StuMapper stuMapper;

    @Autowired
    private EnrolPlanMapper enrolPlanMapper;

    @Autowired
    private AreaMapper areaMapper;
    void ExportExcel(List<AreaPassStu> areaPassStus){
        HSSFWorkbook workbook =new HSSFWorkbook();
        HSSFSheet sheet =workbook.createSheet("面试数据");
        sheet.setColumnWidth(1,40*256);
        sheet.setColumnWidth(2,35*256);
        sheet.setColumnWidth(14,40*256);
        HSSFRow row=sheet.createRow(0);
        HSSFCellStyle style =workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFCell cell=null;
        String []title={"准考证号","报名序号","姓名","身份证号","生源地","性别","初中毕业学校","学校名称","报考代码","专业名称"};

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
        for(int i=0;i<areaPassStus.size();i++){
            row=sheet.createRow(i+1);

            info[0]=areaPassStus.get(i).getExaminationId();
            info[1]=areaPassStus.get(i).getLoginNo();
            info[2]=areaPassStus.get(i).getName();
            info[3]=areaPassStus.get(i).getIdentifId();
            info[4]=areaPassStus.get(i).getAreaName();
            info[5]=areaPassStus.get(i).getGender();
            info[6]=areaPassStus.get(i).getGratuatedSchoolName();
            info[7]=areaPassStus.get(i).getEnrollSchoolName();
            info[8] = areaPassStus.get(i).getWill1();
            info[9]=areaPassStus.get(i).getMajorName();
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
    void getTemplate(){
        List<AreaPassStu> areaPassStus = new ArrayList<>();

        EasyExcel.read("1/1.xlsx", AreaPassStu.class, new AreaPassListener(areaPassStus)).sheet().doRead();

        for(AreaPassStu data:areaPassStus){
            Student student = new Student();
            student = stuMapper.getStuByLoginNoAndName(data.getLoginNo(),data.getName());
            data.setExaminationId(student.getExaminationId());
            data.setGender(student.getGender());
            data.setEnrollSchoolName(enrolPlanMapper.getSchoolNameByMajorId(data.getWill1()));
            data.setIdentifId(student.getIdentifId());
            data.setMajorName(enrolPlanMapper.getMajorNameById(data.getWill1()));
            if(areaMapper.hasAreaName(data.getAreaName())<1){
                System.out.println(student.getLoginNo());
            }
        }
        ExportExcel(areaPassStus);

    }

    @Test
    void getLoginNo(){
        List<AreaPassStu> areaPassStus = new ArrayList<>();
        EasyExcel.read("1/1.xlsx", AreaPassStu.class, new AreaPassListener(areaPassStus)).sheet().doRead();
        for(AreaPassStu data:areaPassStus){
            Student student = new Student();
            student = stuMapper.getStuByEidAndName(data.getExaminationId(),data.getName());
            data.setLoginNo(student.getLoginNo());
            data.setGratuatedSchoolName(student.getGratuatedSchoolName());
            data.setEnrollSchoolName(enrolPlanMapper.getSchoolNameByMajorId(data.getWill1()));
            data.setMajorName(enrolPlanMapper.getMajorNameById(data.getWill1()));
            if(areaMapper.hasAreaName(data.getAreaName())<1){
                System.out.println(student.getLoginNo());
            }
        }
        ExportExcel(areaPassStus);
    }
}
