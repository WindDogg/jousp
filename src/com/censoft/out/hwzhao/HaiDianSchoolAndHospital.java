package com.censoft.out.hwzhao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.censoft.database.MysqlHelper;
import com.censoft.out.utils.Utils;
import com.censoft.util.CoordinateTransform;
import com.censoft.util.FileManager;

public class HaiDianSchoolAndHospital {
	
	private static String type = "学校医院";
	private static String geotype = "Polygon";

	public void go() throws IOException {
		MysqlHelper.getConnection("jdbc:mysql://192.168.5.72:3311/cenmds?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true","root","mysql");
		List<Map<String, Object>> list = MysqlHelper.executeQuery(" select * from xxandyy t where t.shape <> '' and t.sfhd <> '' union select * from xxandyy t where t.bdshape <> '' and t.sfhd <> 'n' ");
		for(int i=0;i<list.size();i++){
			Map<String, Object> map = list.get(i);
			String id = StringUtils.trimToEmpty((String)map.get("id"));
			String name = StringUtils.trimToEmpty((String)map.get("name"));
			String gdshape = StringUtils.trimToEmpty((String)map.get("shape"));
			String bdshape = StringUtils.trimToEmpty((String)map.get("bdshape"));
			String shape = "";
			if("".equals(bdshape)){
				String[] gdstrs = gdshape.split(";");
				StringBuffer tempshape = new StringBuffer();
				for(String gdstr: gdstrs){
					String[] gds = gdstr.split(",");
					double[] gaode_to_bd = CoordinateTransform.gaode_to_bd(Double.valueOf(gds[1]), Double.valueOf(gds[0]));
					tempshape.append(gaode_to_bd[0]);
					tempshape.append(",");
					tempshape.append(gaode_to_bd[1]);
					tempshape.append(";");
				}
				shape = tempshape.toString();
			}else{
				shape = bdshape;
			}
			
			String[] arrs = shape.split(";");
			StringBuffer tempshape = new StringBuffer("[[");
			List<String[]> templist = new ArrayList<String[]>();
			for(String str: arrs){
				String[] lnglats = str.split(",");
				tempshape.append("[");
				tempshape.append(lnglats[0]);
				tempshape.append(",");
				tempshape.append(lnglats[1]);
				tempshape.append("]");
				tempshape.append(",");
				templist.add(new String[]{lnglats[0],lnglats[1]});
				
			}
			
			String lnglat = tempshape.substring(0, tempshape.length()-1) + "]]";
			
			Map<String, Double> centerPoint = Utils.getCenterPoint(templist);
			String centerlng = String.valueOf(centerPoint.get("lng"));
			String centerlat = String.valueOf(centerPoint.get("lat"));
			
			MysqlHelper.executeUpdate("insert into hdq_area_lnglat_school_hospital values(?,?,?,?,?,?,?,?,?,?)", new String[]{null, id, name, type, lnglat, "bd", geotype, centerlng, centerlat, ""});
		}
		
		MysqlHelper.close();
	}
	
	public static void main(String[] args) throws IOException {
		HaiDianSchoolAndHospital obj = new HaiDianSchoolAndHospital();
		obj.go();
	}
	
}
