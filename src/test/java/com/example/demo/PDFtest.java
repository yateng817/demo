package com.example.demo;

import ch.qos.logback.core.util.FileUtil;
import com.example.demo.mapper.AreaMapper;
import com.example.demo.mapper.EnrolResultMapper;
import com.example.demo.pojo.Area;
import com.example.demo.pojo.Student;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.FileSystemUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.springframework.util.FileCopyUtils.BUFFER_SIZE;

@SpringBootTest
public class PDFtest {

    @Autowired
    private EnrolResultMapper enrolResultMapper;

    @Autowired
    private AreaMapper areaMapper;

    private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                 boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }
            }
        }
    }

    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure)
            throws RuntimeException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
            long end = System.currentTimeMillis();
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static ArrayList<File> getFiles(String path) throws Exception {
        //目标集合fileList
        ArrayList<File> fileList = new ArrayList<File>();
        File file = new File(path);
        if(file.isDirectory()){
            File []files = file.listFiles();
            for(File fileIndex:files){
                //如果这个文件是目录，则进行递归搜索
                if(fileIndex.isDirectory()){
                    getFiles(fileIndex.getPath());
                }else {
                    //如果文件是普通文件，则将文件句柄放入集合中
                    fileList.add(fileIndex);
                }
            }
        }
        return fileList;
    }



    @Test
    void testPDF() throws FileNotFoundException {
        File file = new File("schoolEnrol");
        if(file.exists()){
            FileSystemUtils.deleteRecursively(file);
            file.mkdir();
        }else{
            file.mkdir();
        }
        List<Student> enrols = enrolResultMapper.getAllEnrolSort();
        List<String> schoolList = enrolResultMapper.getAllEnrolSchoolSort();
        String filename = "schoolEnrol/全部.pdf";
        createPdf(enrols,filename);
        String schoolName = enrols.get(0).getEnrolSchoolName();
        List<Student> schoolEnrols =new ArrayList<>();
        for(Student student:enrols){
            if(student.getEnrolSchoolName().equals(schoolName)){
                schoolEnrols.add(student);
            }else
            {
                filename = "schoolEnrol/"+schoolName+".pdf";
                createPdf(schoolEnrols,filename);
                schoolName=student.getEnrolSchoolName();
                schoolEnrols.clear();
            }
        }
        filename = "schoolEnrol/"+schoolName+".pdf";
        createPdf(schoolEnrols,filename);
        schoolEnrols.clear();

        FileOutputStream fos = new FileOutputStream(new File("1/录取.zip"));
        toZip("schoolEnrol", fos, true);

    }

    void createPdf(List<Student> enrols,String filename){


        Document document = new Document(PageSize.A4.rotate(),12.0F, 12.0F, 12.0F, 12.0F);
        Map<String,String> areaMap = getArea();
        String majorId = enrols.get(0).getMajorId();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.addTitle("录取信息");
            document.open();
            List<Student> exportList = new ArrayList<>();
            int tot =0;
            for(Student student:enrols){
                tot ++;
                if(student.getMajorId().equals(majorId)||tot%15==0){
                    exportList.add(student);
                }else
                {
                    majorId = student.getMajorId();
                    document.setPageSize(new Rectangle(PageSize.A4.getWidth(), PageSize.A4.getHeight()).rotate());
                    document.add(getTable(exportList,areaMap));
                    exportList.clear();
                    document.newPage();
                    exportList.add(student);
                }
            }
            document.add(getTable(exportList,areaMap));
            exportList.clear();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

    }

    PdfPTable getTable(List<Student> students,Map<String,String> areaMap) throws DocumentException, IOException {
        Integer fontsize = 10;
        BaseFont bfChinexe = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font font = new Font(bfChinexe, fontsize ,Font.NORMAL);
        PdfPTable table = new PdfPTable(16);//生成一个两列的表格
        table.setTotalWidth(800);
        table.setLockedWidth(true);//使绝对宽度模式生效
        int [] widths = {35,35,60,60,40,100,100,200,35,35,35,35,35,35,35,35};
        table.setWidths(widths);
        PdfPCell cell;
        int size = 15;
        Font tfont = new Font(bfChinexe, 16 ,Font.NORMAL);
        Font tfont2 = new Font(bfChinexe, 12 ,Font.NORMAL);
        cell = new PdfPCell(new Phrase("2022年 宁波市中职录取名单",tfont));
        cell.setFixedHeight(size*2);//设置高度
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
        cell.setColspan(16);
        table.addCell(cell);

        if(students.get(0).getEnrolSchoolName()==null)
            System.out.println(students.get(0).getExaminationId());
        cell = new PdfPCell(new Phrase("学校名称："+(students.get(0).getEnrolSchoolName()==null?"":students.get(0).getEnrolSchoolName())+"    "
                +"报考代码："+students.get(0).getMajorId()+"    "
                +"专业名称"+students.get(0).getMajorId()+"    "
                +"人数："+students.size(),tfont));
        cell.setFixedHeight(size*2);//设置高度
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
        cell.setColspan(16);
        table.addCell(cell);

        String [] title = {"序号","区域","准考证号","姓名","性别","户口所在地","联系电话","家庭住址",
                "语文","数学","英语","自然","社会","体育","加分","总分"};
        for(int i=0;i<title.length;i++){
            cell = new PdfPCell(new Phrase(title[i],tfont2));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
            table.addCell(cell);
        }


        for(int i=0;i<students.size();i++){
            Student student = students.get(i);
            cell = new PdfPCell(new Phrase(String.valueOf(i+1),font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(areaMap.get(student.getAreaId()),font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(student.getExaminationId(),font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(student.getName(),font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(student.getGender(),font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(student.getResident(),font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(student.getTel(),font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(student.getAddress(),font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(student.getYw()),font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(student.getSx()),font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(student.getYy()),font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(student.getZr()),font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(student.getSh()),font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(student.getTy()),font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(student.getAdditionalScore()),font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(student.getTotalScore()),font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
            table.addCell(cell);
        }
//        cell = new PdfPCell(new Phrase("学校签名：            招生办签名：            日期：            ",tfont));
//        cell.setFixedHeight(size*2);//设置高度
//        cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
//        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
//        cell.setColspan(16);
//        table.addCell(cell);
        return table;
    }

    Map<String,String> getArea(){
        List<Student> areas = areaMapper.getIdAndName();

        Map<String,String> ans = new HashMap<>();
        for(Student student:areas){
            ans.put(student.getAreaId(),student.getAreaName());
        }
        return ans;
    }


}
