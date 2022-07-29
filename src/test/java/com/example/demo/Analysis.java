package com.example.demo;

import com.example.demo.mapper.EnrolPlanMapper;
import com.example.demo.mapper.EnrolResultMapper;
import com.example.demo.pojo.AreaPassStu;
import com.example.demo.pojo.RemainAnalysis;
import com.example.demo.pojo.Student;
import org.apache.ibatis.javassist.expr.Expr;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.management.monitor.StringMonitor;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class Analysis {

    @Autowired
    private EnrolResultMapper enrolResultMapper;

    @Autowired
    private EnrolPlanMapper enrolPlanMapper;


    void ExportExcel(List<RemainAnalysis> remainAnalyses){
        HSSFWorkbook workbook =new HSSFWorkbook();
        HSSFSheet sheet =workbook.createSheet("各专业统计表");
        sheet.setColumnWidth(1,40*256);
        sheet.setColumnWidth(2,35*256);
        sheet.setColumnWidth(14,40*256);
        HSSFRow row=sheet.createRow(0);
        HSSFCellStyle style =workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFCell cell=null;
        String []title={"录取学校","报考代码","录取专业","最高分","最低分","该批次录取人数","该批次计划数","该批次完成率","其他批次录取数","总录取完成率"};

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
        for(int i=0;i<remainAnalyses.size();i++){
            row=sheet.createRow(i+1);

            RemainAnalysis data = remainAnalyses.get(i);
            info[0]=data.getEnrolSchoolName();
            info[1]=data.getMajorId();
            info[2]=data.getMajorName();
            info[3]= String.valueOf(data.getMaxScore());
            info[4]= String.valueOf(data.getMinScore());
            info[5]= String.valueOf(data.getSelectEnrolNum());
            info[6]= String.valueOf(data.getSPlanNum());
            info[7]= data.getSelectEnrolRate();
            info[8] = String.valueOf(data.getOtherEnrolNum());
            info[9]= data.getAllEnrolRate();
            for(int j=0;j<title.length;j++){
                cell=row.createCell(j);
                cell.setCellValue(info[j]);
                cell.setCellStyle(style);
            }
        }


        HSSFSheet sheet1 =workbook.createSheet("剩余计划汇总表");
        HSSFRow row1 = sheet1.createRow(0);
        HSSFCell cell1 = null;
        String []title1={"学校名称","报考代码","专业名称","专业性质","该批次计划数","该批次录取人数","其他批次录取人数","剩余计划数","签名"};

        /**
         * 为excel的标题设置样式
         */
        for(int i=0;i<title1.length;i++){
            cell1=row1.createCell(i);
            cell1.setCellValue(title1[i]);
            cell1.setCellStyle(style);
        }
        /**
         * 封装每一条招生计划修改数据
         */
        String []info1=new String[title1.length];
        for(int i=0;i<remainAnalyses.size();i++){
            row1=sheet1.createRow(i+1);

            RemainAnalysis data = remainAnalyses.get(i);
            info[0]=data.getEnrolSchoolName();
            info[1]=data.getMajorId();
            info[2]=data.getMajorName();
            info[3]=data.getMajorType();
            info[4]= String.valueOf(data.getSPlanNum());
            info[5]= String.valueOf(data.getSelectEnrolNum());
            info[6]= String.valueOf(data.getOtherEnrolNum());
            info[7]= String.valueOf(data.getSRemainNum());
            info[8]= "";
            for(int j=0;j<title1.length;j++){
                cell1=row1.createCell(j);
                cell1.setCellValue(info[j]);
                cell1.setCellStyle(style);
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
    void chkRemainAnalysis(){
        List<RemainAnalysis> remainAnalyses = getReamainAnalysis(2);

        ExportExcel(remainAnalyses);
    }

    public List<RemainAnalysis> getReamainAnalysis(Integer type){

        List<RemainAnalysis> ans = new ArrayList<>();

        List<Student> allMajors = enrolPlanMapper.getAllmajor();
        List<Student> allEnrols = enrolResultMapper.getAllEnrol();
        List<String> enrolMajors = enrolResultMapper.getAllMajorid();

        String [] schoolMap = new String[1005];//专业编号映射学校名称
        String [] majorTypeMap = new String[1005];
        String [] majorMap = new String[1005];//专业编号映射专业名称
        Double [] minScoreMap = new Double[1005];//专业编号映射最低分
        Double [] maxScoreMap = new Double[1005];//专业便哈映射最高分
        Integer [] ohterEnrolNumMap = new Integer[1005];//专业编号映射非本彼此录取数
        Integer [] sEnrolNumMap = new Integer[1005];//专业编号映射本批次录取数
        Integer [] allEnrolNumMap = new Integer[1005];//专业编号映射总录取数

        Double [] sEnrolRateMap = new Double[1005];//专业编号映射本批次录取率
        Double [] allEnrolRateMap = new Double[1005];//专业编号映射所有录取率
        Integer [] periodPlanMap = new Integer[1005];//专业编号映射本批次计划书
        Integer [] allPlanMap = new Integer[1005];//专业编号映射所有批次计划数


        /**
         * 初始化该批次计划总数，总计划数，专业编号对应的专业名称
         */
        for(Student student:allMajors){
            schoolMap[Integer.parseInt(student.getMajorId())] = student.getOrganizationName();
            majorMap[Integer.parseInt(student.getMajorId())]=student.getMajorName();
            allPlanMap[Integer.parseInt(student.getMajorId())]= Integer.valueOf(student.getEnrolQuantity());
            majorTypeMap[Integer.parseInt(student.getMajorId())]=student.getMajorType();

            String [] des = student.getEnrolDescribe().split(";");
            String cityPlan = des[0].substring(des[0].indexOf(':')+1,des[0].indexOf(')'));
            Integer citPlanNm=0;
            Integer areaPlanNum=0;

            for(int i=1;i<des.length;i++){
                String areaPlan = des[i].substring(des[i].indexOf(':')+1,des[i].indexOf(')'));
                if(("").equals(areaPlan))
                    areaPlan="0";
                areaPlanNum += Integer.valueOf(areaPlan);
            }

            if(("").equals(cityPlan))
                cityPlan = "0";

            citPlanNm = Integer.valueOf(cityPlan);

            if(type == 3){
                    periodPlanMap[Integer.parseInt(student.getMajorId())] = areaPlanNum;
            }else
            {
                periodPlanMap[Integer.parseInt(student.getMajorId())] = citPlanNm;
            }

        }

        for(int i=0 ;i<1000;i++){
            minScoreMap[i]=999d;
            maxScoreMap[i]=0d;
            sEnrolRateMap[i]=0d;
            allEnrolRateMap[i]=0d;
            ohterEnrolNumMap[i]=0;
            sEnrolNumMap[i]=0;
            allEnrolNumMap[i]=0;
        }

        for(Student student:allEnrols){
            allEnrolNumMap[Integer.parseInt(student.getMajorId())]++;
            if(student.getEnrolType() == type){
                sEnrolNumMap[Integer.parseInt(student.getMajorId())]++;
                minScoreMap[Integer.parseInt(student.getMajorId())] = Math.min(minScoreMap[Integer.parseInt(student.getMajorId())],student.getTotalScore());
                maxScoreMap[Integer.parseInt(student.getMajorId())] = Math.max(maxScoreMap[Integer.parseInt(student.getMajorId())],student.getTotalScore());
            }else
            {
                if(type==3){
                    if(!student.getAreaId().equals("16"))
                        ohterEnrolNumMap[Integer.parseInt(student.getMajorId())]++;
                }else
                {
                    if(student.getAreaId().equals("16"))
                    ohterEnrolNumMap[Integer.parseInt(student.getMajorId())]++;
                }

            }
        }

        for(int i = 0; i< 1000;i++){
            if(minScoreMap[i]>800){
                minScoreMap[i]=0d ;
            }
        }
//        处理所得数据
        for(String data:enrolMajors){
            RemainAnalysis  remainAnalysis = new RemainAnalysis();
            Integer majorId = Integer.valueOf(data);
            remainAnalysis.setEnrolSchoolName(schoolMap[majorId]);//录取学校
            remainAnalysis.setMajorId(data);//报考代码
            remainAnalysis.setMajorName(majorMap[majorId]);//专业名称
            remainAnalysis.setMaxScore(maxScoreMap[majorId]);//最高分
            remainAnalysis.setMinScore(minScoreMap[majorId]);//最低分
            remainAnalysis.setSelectEnrolNum(sEnrolNumMap[majorId]);//当前批次录取数
            remainAnalysis.setOtherEnrolNum(ohterEnrolNumMap[majorId]);//其他录取
            remainAnalysis.setSPlanNum(periodPlanMap[majorId]);//当前批次计划数
            remainAnalysis.setAllPlanNum(allPlanMap[majorId]);//所有批次计划数
            remainAnalysis.setMajorType(majorTypeMap[majorId]);//专业性质
            remainAnalysis.setSRemainNum(periodPlanMap[majorId]-sEnrolNumMap[majorId]-ohterEnrolNumMap[majorId]);//当前批次剩余计划数
            remainAnalysis.setAllRemainNum(allPlanMap[majorId]-allEnrolNumMap[majorId]);//所有批次剩余计划数

            if(periodPlanMap[majorId]!=0)
                sEnrolRateMap[majorId] = Double.valueOf(sEnrolNumMap[majorId]*100/periodPlanMap[majorId]);
            else
                sEnrolRateMap[majorId] = 0d;

            if(periodPlanMap[majorId] != 0 )
                allEnrolRateMap[majorId] = Double.valueOf((sEnrolNumMap[majorId]+ohterEnrolNumMap[majorId])*100/periodPlanMap[majorId]);
            else
                allEnrolRateMap[majorId]=0d;


            remainAnalysis.setSelectEnrolRate(sEnrolRateMap[majorId]+"%");//当前批次完成率
            remainAnalysis.setAllEnrolRate(allEnrolRateMap[majorId]+"%");//所有批次完成率
            ans.add(remainAnalysis);
        }

        return ans;
    }
}
