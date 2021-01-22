/*package com.censoft.report.util;

import java.io.File;
import java.net.ConnectException;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

public class FileToPdf {

	public static File fileToPdf(File file) {
	    // 文件全路径名
	    String fileName = file.getPath();
	
	    // 存放转换结果的 pdf 文件
	    File pdfFile = new File(fileName.substring(0, fileName.lastIndexOf(".")) + ".pdf");
	
	    // 非空验证
	    if (!file.exists() || !file.isFile()) {
	        return null;
	    }
	
	    // 存在则不再转换
	    if (pdfFile.exists() && pdfFile.isFile()) {
	        return pdfFile;
	    }
	
	    // 获取连接
	    OpenOfficeConnection connection = new SocketOpenOfficeConnection("127.0.0.1",8100);
	
	    try {
	        // 建立连接
	        connection.connect();
	
	        // 开始转换
	        DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
	        converter.convert(file, pdfFile);
	
	        // 关闭连接
	        connection.disconnect();
	    } catch (ConnectException e) {
	        System.out.println(e);
	    }finally {
	    	connection.disconnect();
		}
	
	    return pdfFile;
	}
	
	public static void main(String[] args) {
		String path = "D:\\upload\\数据报告\\季度\\八里庄街道\\八里庄街道楼宇2019年第1季度报告.docx";
		FileToPdf.fileToPdf(new File(path));
	}

}
*/