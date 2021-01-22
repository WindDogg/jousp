package com.censoft.zgcno1.excel;

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
import com.censoft.excel.Misstion1;
import com.censoft.excel.mypoi.ExcelUtils;
import com.censoft.util.Tools;

public class PiPeiShuiShouAndLou {
	
	private Connection conn = null;
	
	public void go(String fileName) {
		Workbook workbook = null;
        FileInputStream inputStream = null;
        File NewxlsFile = new File("C:/Users/Administrator/Desktop/入园企业表(3).xlsx");
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

                
                Map<String, String> qymcs = new HashMap<String, String>();
                List<Map<String, Object>> list_fenglongs = MysqlHelper.executeQuery("select qymc from zgc_icpark_fenglong group by qymc");
                for(int i=0;i<list_fenglongs.size();i++){
                	qymcs.put((String)list_fenglongs.get(i).get("qymc"),"0");
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

                    Map<String, Object> resultData = convertRowToData(row,qymcs);
                    if (null == resultData) {
                    	System.out.println("第 " + row.getRowNum() + "行数据不合法，已忽略！");
                        continue;
                    }
                    
                }
                
                int i=0;
                for (String key : qymcs.keySet()) {
                	i++;
                	List<Map<String, Object>> list_fenglong = MysqlHelper.executeQuery("select qymc,GROUP_CONCAT(DISTINCT cjjzwbm) cjjzwbm,GROUP_CONCAT(DISTINCT lymc) lymc,GROUP_CONCAT(DISTINCT cs) cs,GROUP_CONCAT(DISTINCT fjh) fjh from zgc_icpark_fenglong where qymc like '%"+key+"%' group by qymc");
                	Row newrow = sheet.createRow(92+i);
                	Cell cell = newrow.createCell(0);
                	cell.setCellValue(i);
                	
                	cell = newrow.createCell(3);
                	cell.setCellValue(key);
                	
                	cell = newrow.createCell(7);
                	cell.setCellValue((String)list_fenglong.get(0).get("lymc"));
                	
                	cell = newrow.createCell(8);
                	cell.setCellValue((String)list_fenglong.get(0).get("cjjzwbm"));
                	
                	cell = newrow.createCell(9);
                	cell.setCellValue((String)list_fenglong.get(0).get("cs"));
                	
                	cell = newrow.createCell(10);
                	cell.setCellValue((String)list_fenglong.get(0).get("fjh"));
                }
                
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
	
	
    private static Map<String, Object> convertRowToData(Row row,Map<String, String> qymcs) {
    	Map<String, Object> resultData = new HashMap<String, Object>();

        Cell cell;
        cell = row.getCell(6);
        String oname = Tools.biaozhunhua(StringUtils.trimToEmpty(ExcelUtils.convertCellValueToString(cell)));
        resultData.put("oname",oname);
        
        System.out.println(oname);
        
        if("".equals(oname)){
        	cell = row.getCell(3);
        	oname = Tools.biaozhunhua(StringUtils.trimToEmpty(ExcelUtils.convertCellValueToString(cell)));
        	List<Map<String, Object>> list_fenglong = MysqlHelper.executeQuery("select qymc,GROUP_CONCAT(DISTINCT cjjzwbm) cjjzwbm,GROUP_CONCAT(DISTINCT lymc) lymc,GROUP_CONCAT(DISTINCT cs) cs,GROUP_CONCAT(DISTINCT fjh) fjh from zgc_icpark_fenglong where qymc like '%"+oname+"%' group by qymc");
        	if(list_fenglong!=null && !list_fenglong.isEmpty()){
        		Map<String, Object> map_fenglong = list_fenglong.get(0);
        		cell = row.createCell(7);
        		cell.setCellValue((String)map_fenglong.get("lymc"));
        		cell = row.createCell(8);
        		cell.setCellValue((String)map_fenglong.get("cjjzwbm"));
        		cell = row.createCell(9);
        		cell.setCellValue((String)map_fenglong.get("cs"));
        		cell = row.createCell(10);
        		cell.setCellValue((String)map_fenglong.get("fjh"));
        		qymcs.remove((String)map_fenglong.get("qymc"));
        	}
        }else{
        	String excelname = "";
            if("北京四方兴业贸易有限公司".equals(oname)){
            	cell = row.getCell(3);
            	excelname = Tools.biaozhunhua(StringUtils.trimToEmpty(ExcelUtils.convertCellValueToString(cell)));
            }else{
            	excelname = oname;
            }
        	List<Map<String, Object>> list_fenglong = MysqlHelper.executeQuery("select qymc,GROUP_CONCAT(DISTINCT cjjzwbm) cjjzwbm,GROUP_CONCAT(DISTINCT lymc) lymc,GROUP_CONCAT(DISTINCT cs) cs,GROUP_CONCAT(DISTINCT fjh) fjh from zgc_icpark_fenglong where qymc like '%"+excelname+"%' group by qymc");
        	List<Map<String, Object>> list_lyjj = MysqlHelper.executeQuery("select oname,zzrl,(select rglm_hb from b_base_louyu_202001to07 where cjjzwbm = a.zzrl) lymc from hb_end_base a where oname = '"+oname+"'");
        	List<Map<String, Object>> list19 = MysqlHelper.executeQuery("select oname,sjss,qjss from `hb_end_202004091648031_rel_rel3——20200527old` where oname = '"+oname+"'");
        	List<Map<String, Object>> list20 = MysqlHelper.executeQuery("select oname,sjss,qjss from hb_end_202010151411043_rel_rel3 where oname = '"+oname+"'");
        	if(list19.size()==1){
        		Map<String, Object> map19 = list19.get(0);
        		String sjss = (String)map19.get("sjss");
        		resultData.put("sjss19", sjss);
        		String qjss = (String)map19.get("qjss");
        		resultData.put("qjss19", qjss);
        	}else{
        		resultData.put("sjss19", "");
        		resultData.put("qjss19", "");
        		resultData.put("business_income", "");
        	}
        	
        	if(list20.size()==1){
        		Map<String, Object> map20 = list20.get(0);
        		String sjss = (String)map20.get("sjss");
        		resultData.put("sjss20", sjss);
        		String qjss = (String)map20.get("qjss");
        		resultData.put("qjss20", qjss);
        	}else{
        		resultData.put("sjss20", "");
        		resultData.put("qjss20", "");
        	}
        	
        	if(list_fenglong!=null && !list_fenglong.isEmpty()){
        		Map<String, Object> map_fenglong = list_fenglong.get(0);
        		cell = row.createCell(7);
        		cell.setCellValue((String)map_fenglong.get("lymc"));
        		cell = row.createCell(8);
        		cell.setCellValue((String)map_fenglong.get("cjjzwbm"));
        		cell = row.createCell(9);
        		cell.setCellValue((String)map_fenglong.get("cs"));
        		cell = row.createCell(10);
        		cell.setCellValue((String)map_fenglong.get("fjh"));
        		qymcs.remove((String)map_fenglong.get("qymc"));
        	}
        	
        	if(list_lyjj!=null && !list_lyjj.isEmpty()){
        		Map<String, Object> map_lyjj = list_lyjj.get(0);
        		cell = row.createCell(11);
        		cell.setCellValue((String)map_lyjj.get("lymc"));
        		cell = row.createCell(12);
        		cell.setCellValue((String)map_lyjj.get("zzrl"));
        	}
        	
        	
        	cell = row.createCell(13);
        	cell.setCellValue((String)resultData.get("sjss19"));
        	
        	cell = row.createCell(14);
        	cell.setCellValue((String)resultData.get("qjss19"));
        	
        	
        	cell = row.createCell(15);
        	cell.setCellValue((String)resultData.get("sjss20"));
        	
        	cell = row.createCell(16);
        	cell.setCellValue((String)resultData.get("qjss20"));
        }
        
        return resultData;
    }
	
	public static void main(String[] args) {
		PiPeiShuiShouAndLou m1 = new PiPeiShuiShouAndLou();
		m1.go("C:/Users/Administrator/Desktop/入园企业表(2).xlsx");
	}

}
