package com.censoft.out.fangguan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.censoft.common.db.ConnectionFactory;
import com.censoft.database.MysqlHelper;

/**
 * 地址不具体
 *
 */
public class ErrorDom {

	private static final String XLS = "xls";
    private static final String XLSX = "xlsx";
    private static final String targetFileUrl = "C:/Users/Administrator/Desktop/地址不具体.xlsx";
    private static final String sourveFileUrl = "C:/Users/Administrator/Desktop/地址不具体1.xlsx";
	private Connection conn = null;
	private Connection conn2 = null;
	static ConnectionFactory dbclass = new ConnectionFactory();
	
	public void go() {
		String fileName = sourveFileUrl;
		Workbook workbook = null;
        FileInputStream inputStream = null;
        File NewxlsFile = new File(targetFileUrl);
        conn = MysqlHelper.getConnection();
        conn2 = dbclass.getConnection("com.mysql.jdbc.Driver","jdbc:mysql://192.168.5.110:3311/lyjj","root","mysql");
        
        
        try {
            // 获取Excel后缀名
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            // 获取Excel文件
            File excelFile = new File(fileName);
            if (!excelFile.exists()) {
                System.out.println(("指定的Excel文件不存在！"));
                return;
            }

            // 获取Excel工作簿
            inputStream = new FileInputStream(excelFile);
            workbook = getWorkbook(inputStream, fileType);

            Sheet sheet = workbook.getSheetAt(0);
            
            List<Map<String, Object>> list1 = MysqlHelper.executeQuery("select b.bm,b.dom,a.BUILD_SITE from temp_error_dom b left join t_fwxz a on a.PFHOUSEID = b.bm");
            int k = 1;
            for(Map<String, Object> map1: list1){
            	Row row = createRow(sheet,k);
            	
            	String bm = StringUtils.trimToEmpty((String)map1.get("bm"));
            	String dom = StringUtils.trimToEmpty((String)map1.get("dom"));
            	String BUILD_SITE = StringUtils.trimToEmpty((String)map1.get("BUILD_SITE"));
            	
            	Cell cell0 = createCell(row,0);
            	cell0.setCellValue(bm);
            	
            	Cell cell2 = createCell(row,2);
            	cell2.setCellValue(dom);
            	
            	Cell cell3 = createCell(row,3);
            	cell3.setCellValue(BUILD_SITE);
            	
            	Vector temV = dbclass.doQuery(conn2, "select c.rglm_hb,a.oname,a.gsdom,a.swdom,a.sjpdom from hb_end_202010261943049_rel_rel3 a, b_base_louyu_202001to09 c where a.zzrl = c.cjjzwbm and EXISTS (select 1 from 2965_to_2907 b where PFHOUSEID = '"+bm+"' and a.zzrl = b.cjjzwbm);");
            	for(int i=0;i<temV.size();i++){
            		Hashtable ht = (Hashtable)temV.get(i);
            		String oname = StringUtils.trimToEmpty((String)ht.get("oname"));
            		String rglm_hb = StringUtils.trimToEmpty((String)ht.get("rglm_hb"));
            		String gsdom = StringUtils.trimToEmpty((String)ht.get("gsdom"));
            		String swdom = StringUtils.trimToEmpty((String)ht.get("swdom"));
            		String sjpdom = StringUtils.trimToEmpty((String)ht.get("sjpdom"));
            		if(i==0){
            			Cell cell1 = createCell(row,1);
            			cell1.setCellValue(rglm_hb);
            		}
                	
                	k++;
                	Row row2 = createRow(sheet,k);
                	
                	Cell cell4 = createCell(row2,4);
                	cell4.setCellValue(oname);
                	
                	Cell cell5 = createCell(row2,5);
                	cell5.setCellValue(gsdom);
                	
                	Cell cell6 = createCell(row2,6);
                	cell6.setCellValue(swdom);
                	
                	Cell cell7 = createCell(row2,7);
                	cell7.setCellValue(sjpdom);
                	
            	}
            	k++;
            }
            
            
            //将excel写入
            FileOutputStream fileOutputStream = new FileOutputStream(NewxlsFile);       
            workbook.write(fileOutputStream);

        } catch (Exception e) {
            System.out.println("解析Excel失败，文件名：" + fileName + " 错误信息：" + e.getMessage());
            return;
        } finally {
            try {
                if (null != workbook) {
                    workbook.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
        		if(conn!=null){
        			MysqlHelper.close();
        		}
            } catch (Exception e) {
            	System.out.println("关闭数据流出错！错误信息：" + e.getMessage());
                return;
            }
        }
		
	}
	
    public static Workbook getWorkbook(InputStream inputStream, String fileType) throws IOException {
        Workbook workbook = null;
        if (fileType.equalsIgnoreCase(XLS)) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (fileType.equalsIgnoreCase(XLSX)) {
            workbook = new XSSFWorkbook(inputStream);
        }
        return workbook;
    }
    
    
    //创建单元格cell
    public static Cell createCell(Row row , int cellNum) {
        Cell cell = row.getCell(cellNum);
        if(cell == null) {
            cell = row.createCell(cellNum);
        }
        return cell;
    }
	
    //创建行row
    public static Row createRow(Sheet sheet , int rowNum) {
        Row row = sheet.getRow(rowNum);
        if(row == null) {
            row = sheet.createRow(rowNum);
        }
        return row;
    }
    
    public static void main(String[] args) {
		ErrorDom ed = new ErrorDom();
		ed.go();
	}
}
