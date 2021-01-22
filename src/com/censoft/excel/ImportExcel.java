package com.censoft.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.censoft.database.MysqlHelper;
import com.censoft.excel.mypoi.ExcelUtils;
import com.censoft.util.Tools;
import com.censoft.zgcno1.excel.PiPeiShuiShouAndLou;

public class ImportExcel {

	private Connection conn = null;
	
	public void go(String fileName) {
		Workbook workbook = null;
        FileInputStream inputStream = null;
        conn = MysqlHelper.getConnection();
        
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
            workbook = ExcelUtils.getWorkbook(inputStream, fileType);

            // 解析sheet
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                Sheet sheet = workbook.getSheetAt(sheetNum);

                if(sheetNum==0){
                	continue;
                }
                
                // 校验sheet是否合法
                if (sheet == null) {
                    continue;
                }

                // 获取第一行数据
                int firstRowNum = sheet.getFirstRowNum();
                Row firstRow = sheet.getRow(firstRowNum);
                if (null == firstRow) {
                    System.out.println("解析Excel失败，在第一行没有读取到任何数据！");
                }

                // 解析每一行的数据，构造数据对象
                // 每个sheet从第2行开始
                int rowStart = firstRowNum + 1;
                int rowEnd = sheet.getPhysicalNumberOfRows();
                for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
                    Row row = sheet.getRow(rowNum);

                    if (null == row) {
                        continue;
                    }

                    Map<String, Object> resultData = convertRowToData(row);
                    if (null == resultData) {
                    	System.out.println("第 " + row.getRowNum() + "行数据不合法，已忽略！");
                        continue;
                    }
                    
                }
            }

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
	
	
	private static Map<String, Object> convertRowToData(Row row) {
    	Map<String, Object> resultData = new HashMap<String, Object>();

        Cell cell;
        cell = row.getCell(1);
        String cjjzwbm = StringUtils.trimToEmpty(ExcelUtils.convertCellValueToString(cell));
        
        cell = row.getCell(2);
        String lymc = StringUtils.trimToEmpty(ExcelUtils.convertCellValueToString(cell));
        
        cell = row.getCell(3);
        String cs = StringUtils.trimToEmpty(ExcelUtils.convertCellValueToString(cell));
        
        cell = row.getCell(4);
        String fjh = StringUtils.trimToEmpty(ExcelUtils.convertCellValueToString(cell));
        
        cell = row.getCell(5);
        String qymc = Tools.biaozhunhua(StringUtils.trimToEmpty(ExcelUtils.convertCellValueToString(cell)));
        
        System.out.println(qymc);
        
        MysqlHelper.executeUpdate("insert into zgc_icpark_fenglong(id,cjjzwbm,lymc,cs,fjh,qymc) values(null,'"+cjjzwbm+"','"+lymc+"','"+cs+"','"+fjh+"','"+qymc+"')");
        
        
        return resultData;
    }
	
	
	
	public static void main(String[] args) {
		ImportExcel m1 = new ImportExcel();
		m1.go("C:/Users/Administrator/Desktop/中关村集成电路设计园建筑物及入驻企业信息-封龙版本.xlsx");
	}
}
