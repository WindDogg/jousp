/*package com.censoft.report.util;


import java.awt.image.BufferedImage;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.util.Iterator;
 
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
 
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;
 
import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
 
*//**
 * 这是一个工具类，主要是为了使Office2003-2007全部格式的文档(.doc|.docx|.xls|.xlsx|.ppt|.pptx)转化为pdf文件
 * (.doc|.docx|.xls|.xlsx|.ppt|.pptx)转化为html文件 pdf 转 img
 * 根据操作系统的名称，获取OpenOffice.org 4的安装目录 如我的OpenOffice.org 4安装在：C:/Program Files
 * (x86)/OpenOffice 4
 * 
 * @return OpenOffice.org 4的安装目录 执行 soffice
 *         -headless-accept="socket,host=127.0.0.1,port=8100;urp;"
 *         -nofirststartwizard
 *//*
public class OpenOfficeUtil {
	private final static String OFFICE_HOME = "C:/Program Files (x86)/OpenOffice 4"; // openOffice 安装路径
	private static final String FILETYPE_DOC = "doc"; // 文件格式后缀
	private static final String FILETYPE_PDF = "pdf";// 文件格式后缀
	private static final String FILETYPE_PNG = "png"; // 文件后缀名
	private static final String SUFF_IMAGE = "." + FILETYPE_PNG; // 图片文件格式 .png
	private static final int OFFICE_PORT = 8100; // openOffice 端口号
	private static final String SUCCESS_MESSAGE = "SUCCESS"; // 转换成功信息
	private static final String ERROR_MESSAGE = "ERROR"; // 转换失败信息
	private static final String PATH_FIELD = "_show"; // 拼装新路径需要的字段
 
	*//**
	 * (.doc|.docx|.xls|.xlsx|.ppt|.pptx|.txt) 转化为html文件
	 * 
	 * @param inputFilePath
	 *            源文件路径，如："F:\openOffice\word2pdf.docx"
	 * @return
	 *//*
	public static File alltohtml(String inputFilePath) {
		OfficeManager officeManager = null;
		try {
			if (!StringUtils.isNotBlank(inputFilePath)) {
				return null;
			}
			String path = getPath(inputFilePath) + PATH_FIELD + File.separator + getFileName(inputFilePath);// 生成新的文件地址
			File inputFile = new File(inputFilePath); // 分别生成源文件file 和新原件file
			File outFile = new File(path);
			if (!inputFile.exists()) {
				return null;
			}
			officeManager = getOfficeManager();// 获取officemanager
			officeManager.start();// 开始
			OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);// openOffice 的文档操作对象
			converterFile(inputFile, path + ".html", inputFilePath, converter);
			return outFile;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (officeManager != null) {
				officeManager.stop();
			}
		}
		return null;
	}
 
	*//**
	 * .doc|.docx|.xls|.xlsx|.pptx|.ppt |.txt | 转pdf word转pdf 执行前，请启动openoffice服务
	 * 
	 * @param xlsfile
	 * @param targetfile
	 * @throws Exception
	 *//*
	public static File alltoPdf(String inputFilePath) throws Exception {
		try {
			makeDir(inputFilePath);
			String path = getPath(inputFilePath) + PATH_FIELD + File.separator + getFileName(inputFilePath) + ".pdf";
			File xlsf = new File(inputFilePath);
			File targetF = new File(path);
			DefaultDocumentFormatRegistry formatReg = new DefaultDocumentFormatRegistry();
			DocumentFormat pdfFormat = formatReg.getFormatByFileExtension(FILETYPE_PDF);
			DocumentFormat docFormat = formatReg.getFormatByFileExtension(FILETYPE_DOC); //支持其他格式文件转换pdf，但这个格式必须为doc 如果换成其他会报错
			InputStream inputStream = new FileInputStream(xlsf);
			OutputStream outputStream = new FileOutputStream(targetF);
			OpenOfficeConnection connection = new SocketOpenOfficeConnection(OFFICE_PORT);
			connection.connect();
			DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
			converter.convert(inputStream, docFormat, outputStream, pdfFormat);
			return targetF;
		} catch (ConnectException e) {
			e.printStackTrace();
		}
		return null;
	}
 
	*//**
	 * pdf转换成img
	 * 
	 * @param inputFilePath
	 * @return
	 * @throws Exception
	 *//*
	public static String pdftoImg(String inputFilePath) throws Exception {
		try {
			String fileName = getFileName(inputFilePath);
			Document document = null;
			BufferedImage img = null;
			float rotation = 0f; // 图片垂直角度 为0时 图片垂直
			float zoom = 1.5f; // 图片质量 数值越大 质量越低
			String path = getPath(inputFilePath) + PATH_FIELD + File.separator + fileName;
			File file = new File(path);
			if (!file.exists()) {
				makeDir(inputFilePath);
			}
			File inputFile = new File(inputFilePath);
			if (!inputFile.exists()) {
				return null;
			}
			String pdfName = getFileName(inputFilePath);
			document = new Document();
			document.setFile(inputFilePath);// 获取文档对象
			PDDocument doc = PDDocument.load(inputFile);
			int pages = doc.getNumberOfPages();// 获取文档的总页数
			for (int i = 0; i < document.getNumberOfPages(); i++) {
				img = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX,
						rotation, zoom);
				Iterator iter = ImageIO.getImageWritersBySuffix(FILETYPE_PNG);
				ImageWriter writer = (ImageWriter) iter.next();
				File outFile = new File(path + fileName + PATH_FIELD + (i + 1) + SUFF_IMAGE);
				FileOutputStream out = new FileOutputStream(outFile);
				ImageOutputStream outImage = ImageIO.createImageOutputStream(out);
				writer.setOutput(outImage);
//				inceptMessage.getMessage(pdfName, outFile, i + 1, pages);
				writer.write(new IIOImage(img, null, null)); // 生成图片
			}
			img.flush();
			document.dispose();
			return SUCCESS_MESSAGE;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR_MESSAGE;
		}
	}
       //没找到直接将文件转成img的方法 ， 索性就把文件先转换成pdf 再转成img
	public static String alltoImg(String inputFilePath) throws Exception {
		String res = getFormat(inputFilePath);
		if (res.equals(FILETYPE_PDF)) {
			return pdftoImg(inputFilePath);
		}
		return pdftoImg(alltoPdf(inputFilePath).getName());
	}
         
	private static OfficeManager getOfficeManager() {
		DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
		config.setPortNumber(OFFICE_PORT);
		config.setOfficeHome(OFFICE_HOME);
		OfficeManager officeManager = config.buildOfficeManager();
		officeManager.start();
		return officeManager;
	}
 
	private static File converterFile(File inputFile, String outputFilePath_end, String inputFilePath,
			OfficeDocumentConverter converter) {
		File outputFile = new File(outputFilePath_end);
		if (!outputFile.getParentFile().exists()) {
			outputFile.getParentFile().mkdirs();
		}
		converter.convert(inputFile, outputFile);
		System.out.println("文件:" + inputFilePath + "\n转换为\n目标文件:" + outputFile + "\n成功!");
		return outputFile;
	}
       *//**
	 * 获取输入文件路径带文件名字 eg:D:\\text\\PPTTestFinal.pdf 获取后 D:\\text\\PPTTestFinal
	 * 
	 * @param inputpath
	 * @return
	 *//*
	private static String getPath(String inputFilePath) {
		String outputpath = inputFilePath.substring(0, inputFilePath.lastIndexOf("."));
		return outputpath;
	}
*//**
	 * 获取输入文件格式 eg:D:\\text\\PPTTestFinal.pdf 获取后 pdf
	 * 
	 * @param inputpath
	 * @return
	 *//*
	private static String getFormat(String inputFilePath) {
		return inputFilePath.substring(inputFilePath.lastIndexOf(".") + 1).toLowerCase();
	}
        *//**
	 * 获取文件名称 eg:D:\\text\\PPTTestFinal.pdf 获取后 PPTTestFinal
	 * 
	 * @param inputpath
	 * @return
	 *//*
	private static String getFileName(String inputFilePath) {
		String name = new File(inputFilePath).getName();
		String filename = name.substring(0, name.lastIndexOf("."));
		return filename;
	}
         *//**
	 * 转换后文件存放地址并在此目录下创建文件夹 eg:D:\\text\\PPTTestFinal 调用此方法后
	 * D:\\text\\PPTTestFinal_show\\
	 * 
	 * @param inputpath
	 *//*
	private static void makeDir(String inputFilePath) {
		new File(getPath(inputFilePath) + PATH_FIELD + File.separator).mkdirs();
	}
 
	public static void main(String[] args) {
		String path = "D:\\upload\\数据报告\\季度\\八里庄街道\\八里庄街道楼宇2019年第2季度报告.docx";
		OpenOfficeUtil.alltohtml(path);
	}
}
*/