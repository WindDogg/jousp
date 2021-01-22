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

public class ZgcYh {
	
	//中关村壹号-百度坐标系
	static String zgcyh="[{'lat': '40.08210157572066','lng': '116.25113875097904'}{'lat': '40.08249461694605','lng': '116.25152974362074'}{'lat': '40.08271284899299','lng': '116.25182017231518'}{'lat': '40.0829444209769','lng': '116.25218415260193'}{'lat': '40.08307313097156','lng': '116.25244466455926'}{'lat': '40.08338249982813','lng': '116.25312572597969'}{'lat': '40.08355484275975','lng': '116.25369333805838'}{'lat': '40.08364351155357','lng': '116.25427774556897'}{'lat': '40.08367294579193','lng': '116.25462955255992'}{'lat': '40.08365691121192','lng': '116.25528339652608'}{'lat': '40.08364220199246','lng': '116.25540858806072'}{'lat': '40.0836133977248','lng': '116.25552879697709'}{'lat': '40.08359433931069','lng': '116.25558045037153'}{'lat': '40.08354787352871','lng': '116.25566486868476'}{'lat': '40.083479674308364','lng': '116.25576417095404'}{'lat': '40.08201272247878','lng': '116.25650005718981'}{'lat': '40.0817360723891','lng': '116.25657829104848'}{'lat': '40.08166373801148','lng': '116.25656132858172'}{'lat': '40.081632325975676','lng': '116.25653943717522'}{'lat': '40.081606788112836','lng': '116.25651059562915'}{'lat': '40.081581593207105','lng': '116.25644598167602'}{'lat': '40.08095673223363','lng': '116.25444605743766'}{'lat': '40.08022976833205','lng': '116.2521954490204'}{'lat': '40.08020739208202','lng': '116.25211890123622'}{'lat': '40.08020723742935','lng': '116.25205430036293'}{'lat': '40.08021967340797','lng': '116.25202250858159'}{'lat': '40.080235057353846','lng': '116.25198773792083'}{'lat': '40.080265570650205','lng': '116.25195993857315'}{'lat': '40.08189038246375','lng': '116.25113158858065'}{'lat': '40.08195898475625','lng': '116.25110780053656'}{'lat': '40.082007983600725','lng': '116.25110685411506'}{'lat': '40.08205408662021','lng': '116.25111186854662'}{'lat': '40.08210157572066','lng': '116.25113875097904'}]";
	
	static String type = "zdqy";
	static String geotype = "Polygon";
	static String name = "中关村壹号";
	static String code = "120020";
	static String targetUrl = "E:/work/workspace2016/report/WebRoot/map/json/hd_zgcyh.json";

	public void gotoJson() throws IOException {
		
		JSONObject jsonObject = Utils.getGeoJsonTemplate();
		JSONArray jsonArray = jsonObject.getJSONArray("features");
		
		JSONObject zmqObject = jsonArray.getJSONObject(0);
		JSONObject geometryObject = zmqObject.getJSONObject("geometry");
		geometryObject.put("type", geotype);

		//////////////////////////////////////////////////////////////
		
		Double strs[][][] = new Double[1][][];
		List<Double[]> clist = new ArrayList<Double[]>();
		
		JSONArray zmq_1Array = JSONArray.parseArray(zgcyh);
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
		ZgcYh obj = new ZgcYh();
		obj.gotoJson();
		obj.jsonToDb();
	}
}
