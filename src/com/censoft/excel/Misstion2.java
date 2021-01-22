package com.censoft.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.censoft.database.MysqlHelper;
import com.censoft.util.Tools;

public class Misstion2 {
	
	private static final String XLS = "xls";
    private static final String XLSX = "xlsx";
    private Connection conn = null;
	
	public void go(String fileName) {
		Workbook workbook = null;
        FileInputStream inputStream = null;
        File NewxlsFile = new File("C:/Users/Administrator/Desktop/请产业经济科协助查询税收及营收数据-0814.xlsx");
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
            workbook = getWorkbook(inputStream, fileType);

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

                // 解析每一行的数据，构造数据对象
                // 每个sheet从第2行开始
                int rowStart = firstRowNum + 1;
                int rowEnd = sheet.getPhysicalNumberOfRows();
                for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
                    Row row = sheet.getRow(rowNum);

                    if (null == row) {
                        continue;
                    }

                    
                    if(sheetNum==0){
                    	Map<String, Object> resultData = convertRowToData0(row);
                    	if (null == resultData) {
                    		System.out.println("第 " + row.getRowNum() + "行数据不合法，已忽略！");
                    		continue;
                    	}
                    }else if(sheetNum==1){
                    	Map<String, Object> resultData = convertRowToData(row);
                    	if (null == resultData) {
                    		System.out.println("第 " + row.getRowNum() + "行数据不合法，已忽略！");
                    		continue;
                    	}
                    }
                    
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
	
    private static Map<String, Object> convertRowToData0(Row row) {
    	Map<String, Object> resultData = new HashMap<String, Object>();

        Cell cell;
        cell = row.getCell(1);
        String oname = StringUtils.trimToEmpty(convertCellValueToString(cell));
        resultData.put("oname",Tools.biaozhunhua(oname));
        
        List<Map<String, Object>> list19 = MysqlHelper.executeQuery("select oname, uni_scid, sjss, qjss, business_income from hb_end_202004091648031_rel_rel3 where oname = '"+oname+"'");
        if(list19.size()==1){
        	Map<String, Object> map19 = list19.get(0);
        	String sjss = (String)map19.get("sjss");
        	resultData.put("sjss19", sjss);
        	String qjss = (String)map19.get("qjss");
        	resultData.put("qjss19", qjss);
        }else{
        	resultData.put("sjss19", "");
        	resultData.put("qjss19", "");
        }
        
//        System.out.println(resultData);
        
        cell = row.createCell(4);
        cell.setCellValue((String)resultData.get("sjss19"));
        
        cell = row.createCell(5);
        cell.setCellValue((String)resultData.get("qjss19"));
        
        return resultData;
    }
    
    
    private static Map<String, Object> convertRowToData(Row row) {
    	Map<String, Object> resultData = new HashMap<String, Object>();
    	
    	Cell cell;
    	cell = row.getCell(1);
    	String oname = StringUtils.trimToEmpty(convertCellValueToString(cell));
    	resultData.put("oname",Tools.biaozhunhua(oname));
    	cell = row.getCell(2);
    	String uni_scid = StringUtils.trimToEmpty(convertCellValueToString(cell));
    	resultData.put("uni_scid",uni_scid);
    	
    	List<Map<String, Object>> list19 = MysqlHelper.executeQuery("select oname, uni_scid, sjss, qjss, business_income from hb_end_202004091648031_rel_rel3 where oname = '"+oname+"' union select oname, uni_scid, sjss, qjss, business_income from hb_end_202004091648031_rel_rel3 where uni_scid = '"+uni_scid+"'");
    	List<Map<String, Object>> list20 = MysqlHelper.executeQuery("select oname, uni_scid, sjss, qjss, business_income from hb_end_202008102014031_rel_rel3 where oname = '"+oname+"' union select oname, uni_scid, sjss, qjss, business_income from hb_end_202008102014031_rel_rel3 where uni_scid = '"+uni_scid+"'");
    	if(list19.size()==1){
    		Map<String, Object> map19 = list19.get(0);
    		String sjss = (String)map19.get("sjss");
    		resultData.put("sjss19", getJieduan(sjss));
    		String qjss = (String)map19.get("qjss");
    		resultData.put("qjss19", getJieduan(qjss));
    		String business_income = (String)map19.get("business_income");
    		resultData.put("business_income", getJieduan(business_income));
    	}else{
    		resultData.put("sjss19", "");
    		resultData.put("qjss19", "");
    		resultData.put("business_income", "");
    	}
    	
    	if(list20.size()==1){
    		Map<String, Object> map20 = list20.get(0);
    		String sjss = (String)map20.get("sjss");
    		resultData.put("sjss20", getJieduan(sjss));
    		String qjss = (String)map20.get("qjss");
    		resultData.put("qjss20", getJieduan(qjss));
    	}else{
    		resultData.put("sjss20", "");
    		resultData.put("qjss20", "");
    	}
    	
    	
//        System.out.println(resultData);
    	
    	cell = row.createCell(3);
    	cell.setCellValue((String)resultData.get("sjss19"));
    	
    	cell = row.createCell(4);
    	cell.setCellValue((String)resultData.get("qjss19"));
    	
    	cell = row.createCell(5);
    	cell.setCellValue((String)resultData.get("business_income"));
    	
    	cell = row.createCell(6);
    	cell.setCellValue((String)resultData.get("sjss20"));
    	
    	cell = row.createCell(7);
    	cell.setCellValue((String)resultData.get("qjss20"));
    	
    	return resultData;
    }
    
    
    private static String convertCellValueToString(Cell cell) {
        if(cell==null){
            return null;
        }
        String returnValue = null;
        switch (cell.getCellType()) {
            case NUMERIC:   //数字
                Double doubleValue = cell.getNumericCellValue();

                // 格式化科学计数法，取一位整数
                DecimalFormat df = new DecimalFormat("0");
                returnValue = df.format(doubleValue);
                break;
            case STRING:    //字符串
                returnValue = cell.getStringCellValue();
                break;
            case BOOLEAN:   //布尔
                Boolean booleanValue = cell.getBooleanCellValue();
                returnValue = booleanValue.toString();
                break;
            case BLANK:     // 空值
                break;
            case FORMULA:   // 公式
                returnValue = cell.getCellFormula();
                break;
            case ERROR:     // 故障
                break;
            default:
                break;
        }
        return returnValue;
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
    
	public static String formatNum(String numstr) {
		if (numstr == null || "".equals(numstr.trim()) || "--".equals(numstr.trim()) || "null".equals(numstr.trim())){
			return "--";
		}
		BigDecimal b = new BigDecimal(numstr);
		BigDecimal num = new BigDecimal("1");
		BigDecimal num2 = new BigDecimal("-1");
		if(b.compareTo(num2)==1 && b.compareTo(num)==-1){
		    BigDecimal divisor = BigDecimal.ONE;  
		    MathContext mc = new MathContext(2);
		    return b.divide(divisor, mc).stripTrailingZeros().toPlainString();  
		}else{
			String rs = b.divide(new BigDecimal(1), 1, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString();
			if(rs.endsWith(".0")){
				rs = rs.substring(0, rs.length()-2);
			}
		    return rs;
		}
		
	}
    
	
	/*
	 * 0 区长 返回数值 1 领导阶段 2 普通阶段
	 */
	public static String getJieduan(String numstr) {
		if (numstr == null || "".equals(numstr.trim()) || "--".equals(numstr.trim()) || "null".equals(numstr.trim())){
			return "--";
		}
		
		if(!Tools.checkNumber(numstr)){
			return numstr;
		}
		
		numstr = formatNum(numstr.trim());
		
		String type = "1";
		return leadJd(numstr, type);
	}
	
	public String getJieduan(String numstr, String type, Map<String, String> ymmap) {
		return getJieduan(numstr);
	}

	/**
	 * @param numstr
	 * @param type
	 *            0 区长 1局领导 2或默认 看大阶段
	 * @return
	 */
	public static String leadJd(String numstr, String type) {
		String endnum = "";
		try {
			if (numstr == null || "".equals(numstr))
				return "";
			double num = Double.parseDouble(numstr);
			String bf = "";
			String ed = "";
			String bff = "";
			String edd = "";
			if ("0".equals(type)) {
				return numstr;
			} else if ("1".equals(type)) {
				if (num < 5) {
					endnum = "5万以下";
				} else if (5 <= num && num < 100) {
					bf = NonScientificNotation(String.valueOf(Math.round((num) / 10) * 10));
					bff = ppts(Double.parseDouble(bf) + "");
					endnum = bff + "万";

				} else if (100 <= num && num < 1000) {
					bf = NonScientificNotation(String.valueOf(Math.round((num) / 50) * 50));
					bff = ppts(Double.parseDouble(bf) + "");
					endnum = bff + "万";
				} else if (1000 <= num && num < 9950) {
					bf = NonScientificNotation(String.valueOf(Math.round((num) / 100) * 100));
					bff = ppts(Double.parseDouble(bf) + "");
					endnum = bff + "万";
				} else {
					bf = NonScientificNotation(String.valueOf(Math.round(num / 1000) * 1000));
					bff = ppts(Double.parseDouble(bf) + "");
					endnum = ppts(Double.parseDouble(bff) / 10000 + "") + "亿";
				}

			} else {
				if (num < 10) {
					endnum = "10万以下";
				} else if (10 <= num && num < 100) {
					bf = NonScientificNotation(String.valueOf(Math.floor((num) / 10) * 10));
					ed = NonScientificNotation(String.valueOf((Math.floor((num) / 10) + 1) * 10));
					bff = ppts(Double.parseDouble(bf) + "");
					edd = ppts(Double.parseDouble(ed) + "");
					endnum = bff + "万~" + edd + "万";
				} else if (100 <= num && num < 1000) {
					bf = NonScientificNotation(String.valueOf(Math.floor((num) / 100) * 100));
					ed = NonScientificNotation(String.valueOf((Math.floor((num) / 100) + 1) * 100));
					bff = ppts(Double.parseDouble(bf) + "");
					edd = ppts(Double.parseDouble(ed) + "");
					endnum = bff + "万~" + edd + "万";
				} else if (1000 <= num && num < 10000) {
					bf = NonScientificNotation(String.valueOf(Math.floor((num) / 500) * 500));
					ed = NonScientificNotation(String.valueOf((Math.floor((num) / 500) + 1) * 500));
					bff = ppts(Double.parseDouble(bf) + "");
					edd = ppts(Double.parseDouble(ed) + "");
					if ("10000".equals(edd)) {
						endnum = bff + "万~" + ppts(Double.parseDouble(ed) / 10000 + "") + "亿";
					} else {
						endnum = bff + "万~" + edd + "万";
					}
				} else if (10000 <= num && num < 100000) {
					bf = NonScientificNotation(String.valueOf(Math.floor(num / 10000) * 10000));
					ed = NonScientificNotation(String.valueOf((Math.floor(num / 10000) + 1) * 10000));
					bff = ppts(Double.parseDouble(bf) + "");
					edd = ppts(Double.parseDouble(ed) + "");
					endnum = ppts(Double.parseDouble(bf) / 10000 + "") + "亿~"
							+ ppts(Double.parseDouble(ed) / 10000 + "") + "亿";
				} else if (100000 <= num && num < 1000000) {
					bf = NonScientificNotation(String.valueOf(Math.floor(num / 50000) * 50000));
					ed = NonScientificNotation(String.valueOf((Math.floor(num / 50000) + 1) * 50000));
					bff = ppts(Double.parseDouble(bf) + "");
					edd = ppts(Double.parseDouble(ed) + "");
					endnum = ppts(Double.parseDouble(bf) / 10000 + "") + "亿~"
							+ ppts(Double.parseDouble(ed) / 10000 + "") + "亿";
				} else {
					bf = NonScientificNotation(String.valueOf(Math.floor(num / 100000) * 100000));
					ed = NonScientificNotation(String.valueOf((Math.floor(num / 100000) + 1) * 100000));
					bff = ppts(Double.parseDouble(bf) + "");
					edd = ppts(Double.parseDouble(ed) + "");
					endnum = ppts(Double.parseDouble(bf) / 10000 + "") + "亿~"
							+ ppts(Double.parseDouble(ed) / 10000 + "") + "亿";
				}
			}
		} catch (Exception ex) {

		}
		return endnum;
	}

	public static String ppts(String nums) {
		String str = nums;
		if (nums != null && !"".equals(nums)) {
			if (nums.length() > 2) {
				String strh = nums.substring(nums.length() - 2, nums.length());
				if (".0".equals(strh)) {
					str = str.replace(".0", "");
				}
			}
		}
		return str;
	}

	public static String NonScientificNotation(String num) {
		// Pattern pattern = Pattern.compile("-?[0-9]*.[0-9]*E[0-9]*");
		Pattern pattern = Pattern.compile("-?[0-9]*.[0-9]*E[0-9]*");
		Matcher match = null;
		match = pattern.matcher(num);
		if (match.matches()) {
			BigDecimal decimal = new BigDecimal(num);
			num = decimal.toPlainString();
		}
		return num;
	}
	
	public static void main(String[] args) {
		Misstion2 m1 = new Misstion2();
		m1.go("C:/Users/Administrator/Desktop/请产业经济科协助查询税收及营收数据-0814.xlsx");
	}
	
}
