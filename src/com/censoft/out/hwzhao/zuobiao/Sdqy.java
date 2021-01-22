package com.censoft.out.hwzhao.zuobiao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.censoft.database.MysqlHelper;
import com.censoft.out.utils.Utils;
import com.censoft.util.FileManager;

public class Sdqy {
	
	//上地区域-百度坐标系
	static String sdqystr = "[{'lat':'40.053961','lng':'116.263852'},{'lat':'40.063017','lng':'116.311354'},{'lat':'40.053905','lng':'116.31696'},{'lat':'40.045124','lng':'116.323212'},{'lat':'40.040926','lng':'116.32659'},{'lat':'40.035954','lng':'116.329392'},{'lat':'40.026894','lng':'116.334495'},{'lat':'40.024849','lng':'116.324362'},{'lat':'40.023192','lng':'116.314373'},{'lat':'40.022308','lng':'116.309126'},{'lat':'40.021313','lng':'116.303808'},{'lat':'40.020208','lng':'116.293819'},{'lat':'40.022253','lng':'116.292166'},{'lat':'40.024518','lng':'116.290585'},{'lat':'40.026562','lng':'116.288933'},{'lat':'40.028717','lng':'116.287208'},{'lat':'40.037225','lng':'116.27959'},{'lat':'40.053961','lng':'116.263852'}]";
	
	static String type = "zdqy";
	static String geotype = "Polygon";
	static String name = "上地区域";
	static String code = "120050";
	static String targetUrl = "E:/work/workspace2016/report/WebRoot/map/json/hd_sdqy.json";

	public void gotoJson() throws IOException {
		
		JSONObject jsonObject = Utils.getGeoJsonTemplate();
		JSONArray jsonArray = jsonObject.getJSONArray("features");
		
		JSONObject zmqObject = jsonArray.getJSONObject(0);
		JSONObject geometryObject = zmqObject.getJSONObject("geometry");
		geometryObject.put("type", geotype);

		//////////////////////////////////////////////////////////////
		
		Double strs[][][] = new Double[1][][];
		List<Double[]> clist = new ArrayList<Double[]>();
		
		JSONArray zmq_1Array = JSONArray.parseArray(sdqystr);
		List<Double[]> list1 = new ArrayList<Double[]>();
		for(int i=0;i<zmq_1Array.size();i++){
			JSONObject lnglat = zmq_1Array.getJSONObject(i);
			Double lnglats[] = new Double[]{lnglat.getDouble("lng"),lnglat.getDouble("lat")};
			list1.add(lnglats);
			clist.add(lnglats);
		}
		Double arr1[][] = new Double[list1.size()][];
		for(int i=0;i<list1.size();i++){
			arr1[i] = list1.get(i);
		}
		strs[0] = arr1;
		
		
		
		geometryObject.put("coordinates", strs);
		
		JSONObject propertiesObject = zmqObject.getJSONObject("properties");
		propertiesObject.put("gridname", name);
		propertiesObject.put("gridid", code);
		Map<String, Double> centerPoint = Utils.getCenterPoint(clist);
		propertiesObject.put("X", centerPoint.get("lng"));
		propertiesObject.put("Y", centerPoint.get("lat"));
		
		FileManager.saveFile(targetUrl, jsonObject.toJSONString());
		
	}
	
	
	public void jsonToDb() throws IOException {
		MysqlHelper.getConnection("jdbc:mysql://192.168.5.72:3311/cenmds?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true","root","mysql");
		
		File jsonFile = new File(targetUrl);
		String readFile = FileManager.readFile(jsonFile);
		System.out.println(readFile);
		JSONObject jsonObject = JSON.parseObject(readFile);
		JSONArray jsonArray = jsonObject.getJSONArray("features");
		String type = this.type;
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jdObject = jsonArray.getJSONObject(i);
			
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
		
		MysqlHelper.close();
	}
	
	public static void main(String[] args) throws IOException {
		Sdqy obj = new Sdqy();
		obj.gotoJson();
		obj.jsonToDb();
	}
}
