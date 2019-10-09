package com.app.main.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
@Component
public class PdfUtilImg {
     public static File Pdf(String jpgFile,String mOutputPdfFileName) {  
            Document doc = new Document(PageSize.A4, 20, 20, 20, 20); //new一个pdf文档 
            try {  
                PdfWriter.getInstance(doc, new FileOutputStream(mOutputPdfFileName)); //pdf写入 
                doc.open();//打开文档  
                    doc.newPage();  //在pdf创建一页
                    Image png1 = Image.getInstance(jpgFile); //通过文件路径获取image 
                    float heigth = png1.getHeight();  
                    float width = png1.getWidth();  
                    int percent = getPercent2(heigth, width);  
                    png1.setAlignment(Image.MIDDLE);  
                    png1.scalePercent(percent+3);// 表示是原来图像的比例;  
                    doc.add(png1);  
                doc.close();  
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
            } catch (DocumentException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
      
            File mOutputPdfFile = new File(mOutputPdfFileName);  //输出流
            if (!mOutputPdfFile.exists()) {  
                mOutputPdfFile.deleteOnExit();  
                return null;  
            }  
            return mOutputPdfFile; //反回文件输出流
        }  
      
        public static int getPercent(float h, float w) {  
            int p = 0;  
            float p2 = 0.0f;  
            if (h > w) {  
                p2 = 297 / h * 100;  
            } else {  
                p2 = 210 / w * 100;  
            }  
            p = Math.round(p2);  
            return p;  
        }  
        public static int getPercent2(float h, float w) {  
            int p = 0;  
            float p2 = 0.0f;  
            p2 = 530 / w * 100;  
            p = Math.round(p2);  
            return p;  
        }  
    public String imgOfPdf(String filepath) {
            boolean result = false;
            String pdfUrl = "";
            try {
                  String pdffile = filepath.substring(0, filepath.lastIndexOf("."));
                  pdfUrl = pdffile+".pdf";  //输出pdf文件路径          
                result = true;
                if (result == true) {
                    File file = PdfUtilImg.Pdf(filepath, pdfUrl);//生成pdf  
                   file.createNewFile();  
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return pdfUrl;
        }
    public static void main(String[] args) {
    	PdfUtilImg img = new PdfUtilImg();
		img.imgOfPdf("H:\\workspace\\YasenGateway\\report\\img_1\\101\\2\\2019\\08\\13\\10120190813910636\\20190813162657printreport.jpg");//filePaths为存储位置
	}
}

