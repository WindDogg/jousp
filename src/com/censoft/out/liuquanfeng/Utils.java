package com.censoft.out.liuquanfeng;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.censoft.database.MysqlHelper;

public class Utils {
	
	public static Map<String, Object> getQyCount(String tiaojian){
		return getQyCount(tiaojian, "1");
	}
	
	public static Map<String, Object> getQyCount(String tiaojian, String countType){
		String sql_qy = "select count("+countType+") c, sum(sjss) sjss, sum(qjss) qjss, sum(sjss_dy) sjss_dy, sum(qjss_dy) qjss_dy from hb_end_202012181857006_rel_rel3 where 1=1 ";
		List<Map<String, Object>> list1 = MysqlHelper.executeQuery(sql_qy+tiaojian);
		return list1.get(0);
	}
	
	public static Map<String, Object> getLastQyCount(String tiaojian){
		String sql_qy = "select count(1) c, sum(sjss) sjss, sum(qjss) qjss, sum(sjss_dy) sjss_dy, sum(qjss_dy) qjss_dy from hb_end_202004090253019_rel_rel3 where 1=1 ";
		List<Map<String, Object>> list1 = MysqlHelper.executeQuery(sql_qy+tiaojian);
		return list1.get(0);
	}
	
	// 占比
	public static String zb(String n1, String n2) {
		DecimalFormat df = new DecimalFormat("0.0");
		BigDecimal b0 = new BigDecimal(n1);
		BigDecimal b1 = new BigDecimal(n2);
		return df.format((b0.divide(b1, 3, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100)));
	}
	
	// 同比
	public static String tb(String n1, String n2) {
		DecimalFormat df = new DecimalFormat("0.0");
		BigDecimal b0 = new BigDecimal(n1);
		BigDecimal b1 = new BigDecimal(n2);
		String tb = df.format(((b0.subtract(b1)).divide(b1, 3, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100)));
		String returnS = "";
		if(tb.startsWith("-")){
			returnS = "减少";
			tb = tb.substring(1, tb.length());
		}else{
			returnS = "增加";
		}
		returnS = returnS + tb + "%";
		return returnS;
	}
	
	
	// double转string
	public static String convertDoubleToString(Object val) {
		if(val == null){return "--";}
		BigDecimal bd = new BigDecimal(String.valueOf(val));
		return bd.stripTrailingZeros().toPlainString();
	}

	// double 万转亿
	public static String wanzhuanyi(Object val) {
		if (val == null) {
			val = 0;
		}
		BigDecimal b = new BigDecimal(String.valueOf(val));
		return b.divide(new BigDecimal(10000), 1, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString();
	}
}
