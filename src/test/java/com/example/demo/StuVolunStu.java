package com.example.demo;

import com.alibaba.excel.EasyExcel;
import com.example.demo.listener.SpecialityListener;
import com.example.demo.listener.StuVolunListener;
import com.example.demo.listener.StudentListener;
import com.example.demo.mapper.*;
import com.example.demo.pojo.SpecialtyStuInfo;
import com.example.demo.pojo.StuVolun;
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
public class StuVolunStu {
    @Autowired
    private StuMapper stuMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private EnrolPlanMapper enrolPlanMapper;
    @Autowired
    private SpecialMapper specialMapper;
    @Autowired
    private EnrolResultMapper enrolResultMapper;


    String [] transWill(String will1){
        String [] ans;
        ans = will1.split(",");
        for(int i =0 ;i< ans.length ; i++){
            ans[i] = ans[i].substring(ans[i].indexOf("]")+1);
        }
        System.out.println(ans);
        return ans;
    }

    String [] transWill2(String will){
        String [] tans;
        tans = will.split("]");
        String [] ans = new String[10];
        for(int i = 1; i<tans.length;i++){
            ans[i-1] = tans[i].substring(0,3);
        }
        for(int i=tans.length-1;i<=9;i++){
            ans[i]=null;
        }
        return ans;
    }


    @Test
    void getExportVolun(){
        List<StuVolun> stuVoluns = new ArrayList<>();
        EasyExcel.read("1/1.xlsx", StuVolun.class, new StuVolunListener(stuVoluns)).sheet().doRead();
        for(StuVolun data : stuVoluns){

            String [] t = transWill(data.getWill1());
            for(int i=0;i<t.length;i++) {
                System.out.print(t[i] + " ");
            }
            System.out.println();
        }
    }

    void ExportVolunExcel(List<StuVolun> stuVoluns){
        HSSFWorkbook workbook =new HSSFWorkbook();
        HSSFSheet sheet =workbook.createSheet("面试数据");
        sheet.setColumnWidth(1,40*256);
        sheet.setColumnWidth(2,35*256);
        sheet.setColumnWidth(14,40*256);
        HSSFRow row=sheet.createRow(0);
        HSSFCellStyle style =workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFCell cell=null;
        String []title={"区域","准考证号","姓名","性别","户口所在地","联系电话","家庭地址","身份证号","语文","数学","英语","科学","社思","体育","加分","总分","志愿1","志愿2","志愿3","志愿4","志愿5","志愿6","志愿7","志愿8","志愿9","志愿10","报名序号"};

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
        for(int i=0;i<stuVoluns.size();i++){
            Student student = stuMapper.getStuByLoginNoAndName(stuVoluns.get(i).getLoginNo().trim(),stuVoluns.get(i).getName().trim());
            row=sheet.createRow(i+1);
            if(stuVoluns.get(i).getAreaName()==null || stuVoluns.get(i).getAreaName().equals("")){
                info[0]=areaMapper.getAreaNameById(student.getAreaId());
            }else{
                info[0]=stuVoluns.get(i).getAreaName();
            }

            if(stuVoluns.get(i).getExaminationId()==null || stuVoluns.get(i).getExaminationId().equals("")){
                info[1]=student.getExaminationId();
            }else{
                info[1]=stuVoluns.get(i).getExaminationId();
            }

            info[2]=stuVoluns.get(i).getName().trim();

            if(stuVoluns.get(i).getGender()==null || stuVoluns.get(i).getGender().equals("")){
                info[3]=student.getGender();
            }else{
                info[3]=stuVoluns.get(i).getGender();
            }

            if(stuVoluns.get(i).getResident()==null || stuVoluns.get(i).getResident().equals("")){
                info[4]=student.getResident();
            }else{
                info[4]=stuVoluns.get(i).getResident();
            }
            if(stuVoluns.get(i).getTel()==null || stuVoluns.get(i).getTel().equals("")){
                info[5]=student.getTel();
            }else{
                info[5]=stuVoluns.get(i).getTel();
            }
            if(stuVoluns.get(i).getAddress()==null || stuVoluns.get(i).getAddress().equals("")){
                info[6]=student.getAddress();
            }else{
                info[6]=stuVoluns.get(i).getAddress();
            }
            if(stuVoluns.get(i).getIdentifId()==null || stuVoluns.get(i).getIdentifId().equals("")){
                info[7]=student.getIdentifId();
            }else{
                info[7]=stuVoluns.get(i).getIdentifId();
            }

            info[8]= String.valueOf(stuVoluns.get(i).getYw());
            info[9]= String.valueOf(stuVoluns.get(i).getSx());
            info[10]= String.valueOf(stuVoluns.get(i).getYy());
            info[11]= String.valueOf(stuVoluns.get(i).getZr());
            info[12]= String.valueOf(stuVoluns.get(i).getSh());
            info[13]= String.valueOf(stuVoluns.get(i).getTy());
            info[14]= String.valueOf(stuVoluns.get(i).getAdditionalScore());
            info[15]= String.valueOf(stuVoluns.get(i).getTotalScore());

            String[] wills= transWill(stuVoluns.get(i).getWill1());
            for(int w=0;w<wills.length;w++){
                info[16+w]=wills[w];

            }
            for(int w = wills.length;w<10;w++){
                info[16+w]=null;
            }
            info[26]=stuVoluns.get(i).getLoginNo().trim();


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
    void getVolunStuList(){//特长生有分数的版本
        List<StuVolun> stuVoluns = new ArrayList<>();
        EasyExcel.read("1/1.xlsx", StuVolun.class, new StuVolunListener(stuVoluns)).sheet().doRead();
        String [] sex =new String[2];
        sex[0] = "女";
        sex[1] = "男";
        for(StuVolun data:stuVoluns){
            data.setGender(sex[Integer.parseInt(data.getGender())%2]);
        }
        ExportVolunExcel(stuVoluns);
    }

    @Test
    void getVolunStuListRemain(){//导出剩余计划
        List<StuVolun> stuVoluns = new ArrayList<>();
        EasyExcel.read("1/1.xlsx", StuVolun.class, new StuVolunListener(stuVoluns)).sheet().doRead();

        List<StuVolun> exportLists = new ArrayList<>();

        String [] sex =new String[2];
        sex[0] = "女";
        sex[1] = "男";
        for(StuVolun data:stuVoluns){
            if(enrolResultMapper.getEnrolNumByLoginNO(data.getLoginNo())==0){
                data.setGender(sex[Integer.parseInt(data.getGender())%2]);
                exportLists.add(data);
            }
        }
        ExportVolunExcel(exportLists);
    }






}
