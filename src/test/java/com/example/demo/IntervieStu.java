package com.example.demo;

import com.example.demo.mapper.AreaMapper;
import com.example.demo.mapper.EnrolPlanMapper;
import com.example.demo.mapper.SpecialMapper;
import com.example.demo.mapper.StuMapper;
import com.example.demo.pojo.Export;
import com.example.demo.pojo.Interview;
import com.example.demo.pojo.Student;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SpringBootTest
public class IntervieStu {
    @Autowired
    private StuMapper stuMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private EnrolPlanMapper enrolPlanMapper;
    @Autowired
    private SpecialMapper specialMapper;

    List<Export> readExcel(String path) throws IOException {
        List<Export> exports = new ArrayList();           //excel数据list
        File file = new File(path);
        FileInputStream inputStream = new FileInputStream(file);
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        int type = 0;
        if(suffix == "xls")
            type=0;
        else type = 1;

        XSSFWorkbook wb1 = new XSSFWorkbook();
        HSSFWorkbook wb2 = new HSSFWorkbook();
        Sheet sheet;
        type =1;
        if(type == 0) {
            wb2 = new HSSFWorkbook(inputStream);
            sheet = wb2.getSheetAt(0);
        } else {
            wb1 = new XSSFWorkbook(inputStream);
            sheet = wb1.getSheetAt(0);
        }
        try {

            Iterator var7 = sheet.iterator();

            while(var7.hasNext()) {
                Row row = (Row)var7.next();
                int index = 0;

                for(Iterator var10 = row.iterator(); var10.hasNext(); ++index) {
                    Cell cell = (Cell)var10.next();
                    cell.setCellType(1);
                }
            }

            int isEmptyId = 0;
            int k = 0;

            while(isEmptyId < 3) {
                Row row2 = sheet.getRow(1 + k);
                if(row2==null)
                {
                    isEmptyId++;
                    k++;
                    continue;
                }
                Interview interview = new Interview();
                Cell cell = row2.getCell(0);
                if(cell!=null)
                    interview.setExaminationId(cell.getStringCellValue());
                cell = row2.getCell(1);
                if(cell!=null)
                    interview.setLoginNo(cell.getStringCellValue());
                cell = row2.getCell(2);
                if(cell!=null)
                    interview.setName(cell.getStringCellValue());
                cell = row2.getCell(3);
                if(cell!=null)
                    interview.setAreaName(cell.getStringCellValue());
                cell = row2.getCell(4);
                if(cell!=null)
                    interview.setGender(cell.getStringCellValue());
                cell = row2.getCell(5);
                if(cell!=null)
                    interview.setSchoolName(cell.getStringCellValue());
                cell = row2.getCell(6);
                if(cell!=null)
                    interview.setWill1(cell.getStringCellValue());
                cell = row2.getCell(7);
                if(cell!=null)
                    interview.setMajorName(cell.getStringCellValue());
                cell = row2.getCell(8);
                if(cell!=null)
                    interview.setResultDescribe(cell.getStringCellValue());
                cell = row2.getCell(9);
                if(cell!=null)
                    interview.setIdentityId(cell.getStringCellValue());

                if(interview.getLoginNo()==null||interview.getLoginNo().equals("")){
                    isEmptyId++;
                    k++;
                    continue;
                }

                Student student = null;
                student=stuMapper.getStuByLoginNoAndName(interview.getLoginNo().trim(),interview.getName().trim());
                if(student!=null){
                    Export export = new Export();
                    export.setExaminationID(student.getExaminationId());
                    export.setLoginNo(student.getLoginNo());
                    export.setName(student.getName());
                    export.setResident(student.getResident());
                    export.setSex(student.getGender());
                    export.setGraduationSchool(student.getGratuatedSchoolName());
                    export.setMajorName(interview.getMajorName());
                    export.setMajorID(interview.getWill1());
                    export.setIdentityID(student.getIdentifId());
                    export.setDesc("合格");
                    exports.add(export);
                }else
                {
                    System.out.println(k);
                }


                k++;

            }

            wb1.close();
            wb2.close();
        } catch (Exception var47) {
            var47.printStackTrace();
        }finally {
            System.out.println("unHappy");
        }

        System.out.println(exports.size());
        return exports;
    }

    @Test
    void testSplice(){
        String a = "123";
        System.out.println(a.split("\\(")[0]);
    }

    //    面试数据转化
    @Test
    void getExport() throws IOException {

        HSSFWorkbook workbook =new HSSFWorkbook();
        HSSFSheet sheet =workbook.createSheet("面试数据");
        sheet.setColumnWidth(1,40*256);
        sheet.setColumnWidth(2,35*256);
        sheet.setColumnWidth(14,40*256);
        HSSFRow row=sheet.createRow(0);
        HSSFCellStyle style =workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFCell cell=null;
        String []title={"准考证号","报名序号","姓名","生源地","性别","初中毕业学校","报考代码","专业名称","备注","身份证号"};

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
        List<Export>exports = readExcel("1/1.xlsx");
        String []info=new String[title.length];
        for(int i=0;i<exports.size();i++){
            row=sheet.createRow(i+1);

            info[0] = exports.get(i).getExaminationID();
            info[1] = exports.get(i).getLoginNo();
            info[2] = exports.get(i).getName();
            info[3] = exports.get(i).getResident();
            info[4] = exports.get(i).getSex();
            info[5] = exports.get(i).getGraduationSchool();
            info[6] = exports.get(i).getMajorID();
            info[7] = exports.get(i).getMajorName();
            info[8] = exports.get(i).getDesc();
            info[9] = exports.get(i).getIdentityID();
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
}
