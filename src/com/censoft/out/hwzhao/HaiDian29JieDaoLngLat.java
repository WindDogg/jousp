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

public class HaiDian29JieDaoLngLat {
	
	private static String type = "jd";

	public void go(String filepath) throws IOException {
		MysqlHelper.getConnection("jdbc:mysql://192.168.5.72:3311/cenmds?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true","root","mysql");
		
		File jsonFile = new File(filepath);
		String readFile = FileManager.readFile(jsonFile);
		System.out.println(readFile);
		JSONObject jsonObject = JSON.parseObject(readFile);
		JSONArray jsonArray = jsonObject.getJSONArray("features");
		JSONObject lastJsonObject = new JSONObject();
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jdObject = jsonArray.getJSONObject(i);
			JSONObject jdProperties = jdObject.getJSONObject("properties");
			String name = jdProperties.getString("gridname");
			
			if(i>0){
				JSONObject ljdProperties = lastJsonObject.getJSONObject("properties");
				String lname = ljdProperties.getString("gridname");
				if(lname.equals(name)){
					boolean b = false;
					if(i==jsonArray.size()){
						b = true;
					}
					jdObject = hebingJSON(lastJsonObject,jdObject,b);
				}else{
					insertJSONObject(lastJsonObject);
				}
			}
			
			lastJsonObject = jdObject;
			
		}
		
		insertJSONObject(lastJsonObject);
		
		MysqlHelper.close();
	}
	
	public void insertJSONObject(JSONObject jdObject) {
		JSONObject jdGeometry = jdObject.getJSONObject("geometry");
		
		JSONObject jdProperties = jdObject.getJSONObject("properties");
		String name = jdProperties.getString("gridname");
		String code = jdProperties.getString("gridid");
		String centerlng = jdProperties.getString("X");
		String centerlat = jdProperties.getString("Y");
		String geotype = jdGeometry.getString("type");
		
		String lnglat = jdGeometry.getString("coordinates");
		
		MysqlHelper.executeUpdate("insert into hdq_area_lnglat values(?,?,?,?,?,?,?,?,?,?)", new String[]{null, code, name, type, lnglat, "bd", geotype, centerlng, centerlat, ""});
	}
	
	public JSONObject hebingJSON(JSONObject mainObject,JSONObject sObject,boolean b) {
		
		List<Double[]> clist = new ArrayList<Double[]>();
		JSONObject mainjdGeometry = mainObject.getJSONObject("geometry");
		JSONObject sjdGeometry = sObject.getJSONObject("geometry");
		
		JSONObject mainjdProperties = mainObject.getJSONObject("properties");
		JSONObject sjdProperties = sObject.getJSONObject("properties");
		
		String maincenterlng = StringUtils.trimToEmpty(mainjdProperties.getString("X"));
		String maincenterlat = StringUtils.trimToEmpty(mainjdProperties.getString("Y"));
		
		if(!"".equals(maincenterlng)){
			Double[] cd = new Double[]{Double.valueOf(maincenterlng),Double.valueOf(maincenterlat)};
			clist.add(cd);
		}
		
		String scenterlng = StringUtils.trimToEmpty(sjdProperties.getString("X"));
		String scenterlat = StringUtils.trimToEmpty(sjdProperties.getString("Y"));
		
		if(!"".equals(scenterlng)){
			Double[] cd = new Double[]{Double.valueOf(scenterlng),Double.valueOf(scenterlat)};
			clist.add(cd);
		}
		
		Map<String, Double> centerPoint = Utils.getCenterPoint(clist);
		mainjdProperties.put("X", centerPoint.get("lng"));
		mainjdProperties.put("Y", centerPoint.get("lat"));
		
		mainjdGeometry.put("type", "MultiPolygon");
		
		String mainlnglat = mainjdGeometry.getString("coordinates");
		
		String slnglat = sjdGeometry.getString("coordinates");
		slnglat = slnglat.substring(1, slnglat.length()-1);
		
		mainlnglat = mainlnglat.substring(0,mainlnglat.length()-1)+","+slnglat+"]";
		
		mainjdGeometry.put("coordinates", mainlnglat);
		
		if(b){
			insertJSONObject(mainObject);
		}
		
		return mainObject;
	}
	
	public static void main(String[] args) throws IOException {
		HaiDian29JieDaoLngLat obj = new HaiDian29JieDaoLngLat();
		obj.go("E:/work/workspace2016/report/WebRoot/map/json/海淀街道_CGCS2000_拆分_BaiduLngLat2.json");
	}
	
}
