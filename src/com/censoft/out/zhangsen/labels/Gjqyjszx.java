package com.censoft.out.zhangsen.labels;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

/**
 * 国家企业技术中心
 * 
 * @author Administrator
 *
 */
public class Gjqyjszx {

	public void parsePdf(String fileName) throws IOException {
		InputStream inputStream = new FileInputStream(new File(fileName));
		PdfReader pr = new PdfReader(inputStream);
		int sumPage = pr.getNumberOfPages();
		
		for(int i = 1; i <= sumPage; i++){
            String pageContent = PdfTextExtractor.getTextFromPage(pr, i).trim();
            System.out.println(pageContent);
		}
		
	}
	
	
	public static void main(String[] args) throws IOException {
		Gjqyjszx obj = new Gjqyjszx();
		obj.parsePdf("E:/work/projectDoc/楼宇经济/2021/国家企业技术中心名单（全部）.pdf");
	}
	
	
}
