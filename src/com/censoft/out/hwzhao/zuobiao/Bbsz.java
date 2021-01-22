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

public class Bbsz {
	
	static String type = "zdqy";
	static String geotype = "MultiPolygon";
	static String name = "北部四镇";
	static String code = "120070";
	static String targetUrl = "E:/work/workspace2016/report/WebRoot/map/json/hd_bbsz.json";

	public void gotoJson() throws IOException {
		
		MysqlHelper.getConnection("jdbc:mysql://192.168.5.72:3311/cenmds?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true","root","mysql");
		
		JSONObject jsonObject = Utils.getGeoJsonTemplate();
		JSONArray jsonArray = jsonObject.getJSONArray("features");
		
		JSONObject zmqObject = jsonArray.getJSONObject(0);
		JSONObject geometryObject = zmqObject.getJSONObject("geometry");
		geometryObject.put("type", geotype);

		//////////////////////////////////////////////////////////////
		
		Double strs[][][] = new Double[4][][];
		List<Double[]> clist = new ArrayList<Double[]>();
		
		List<Map<String, Object>> list1 = MysqlHelper.executeQuery(" select * from hdq_area_lnglat where name = '苏家坨地区' and type = 'jd' ");
		if(list1.size()==1){
			Map<String, Object> map = list1.get(0);
			String lnglatStr = (String)map.get("lnglat");
			List<Double[][]> lnglatList = JSONArray.parseArray(lnglatStr, Double[][].class);
			strs[0] = lnglatList.get(0);
			Double centerlng = Double.valueOf((String) map.get("centerlng"));
			Double centerlat = Double.valueOf((String) map.get("centerlat"));
			clist.add(new Double[]{centerlng,centerlat});
		}else{
			System.out.println("表hdq_area_lnglat 无街道数据....");
		}
		
		List<Map<String, Object>> list2 = MysqlHelper.executeQuery(" select * from hdq_area_lnglat where name = '温泉地区' and type = 'jd' ");
		if(list1.size()==1){
			Map<String, Object> map = list2.get(0);
			String lnglatStr = (String)map.get("lnglat");
			List<Double[][]> lnglatList = JSONArray.parseArray(lnglatStr, Double[][].class);
			strs[1] = lnglatList.get(0);
			Double centerlng = Double.valueOf((String) map.get("centerlng"));
			Double centerlat = Double.valueOf((String) map.get("centerlat"));
			clist.add(new Double[]{centerlng,centerlat});
		}else{
			System.out.println("表hdq_area_lnglat 无街道数据....");
		}
		
		List<Map<String, Object>> list3 = MysqlHelper.executeQuery(" select * from hdq_area_lnglat where name = '西北旺地区' and type = 'jd' ");
		if(list1.size()==1){
			Map<String, Object> map = list3.get(0);
			String lnglatStr = (String)map.get("lnglat");
			List<Double[][]> lnglatList = JSONArray.parseArray(lnglatStr, Double[][].class);
			strs[2] = lnglatList.get(0);
			Double centerlng = Double.valueOf((String) map.get("centerlng"));
			Double centerlat = Double.valueOf((String) map.get("centerlat"));
			clist.add(new Double[]{centerlng,centerlat});
		}else{
			System.out.println("表hdq_area_lnglat 无街道数据....");
		}
		
		List<Map<String, Object>> list4 = MysqlHelper.executeQuery(" select * from hdq_area_lnglat where name = '上庄地区' and type = 'jd' ");
		if(list1.size()==1){
			Map<String, Object> map = list4.get(0);
			String lnglatStr = (String)map.get("lnglat");
			List<Double[][]> lnglatList = JSONArray.parseArray(lnglatStr, Double[][].class);
			strs[3] = lnglatList.get(0);
			Double centerlng = Double.valueOf((String) map.get("centerlng"));
			Double centerlat = Double.valueOf((String) map.get("centerlat"));
			clist.add(new Double[]{centerlng,centerlat});
		}else{
			System.out.println("表hdq_area_lnglat 无街道数据....");
		}
		
		
		geometryObject.put("coordinates", strs);
		
		JSONObject propertiesObject = zmqObject.getJSONObject("properties");
		propertiesObject.put("gridname", name);
		propertiesObject.put("gridid", code);
		Map<String, Double> centerPoint = Utils.getCenterPoint(clist);
		propertiesObject.put("X", centerPoint.get("lng"));
		propertiesObject.put("Y", centerPoint.get("lat"));
		
		FileManager.saveFile(targetUrl, jsonObject.toJSONString());
		
		MysqlHelper.close();
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
		Bbsz obj = new Bbsz();
		obj.gotoJson();
		obj.jsonToDb();
	}
}
