package com.censoft.out.utils;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.censoft.util.FileManager;

public class Utils {

	public static JSONObject getGeoJsonTemplate() {
		File jsonFile = new File("E:/work/workspace2016/report/WebRoot/map/json/geoJsonTemplate.json");
		String readFile = FileManager.readFile(jsonFile);
		System.out.println(readFile);
		JSONObject jsonObject = JSON.parseObject(readFile);
		return jsonObject;
	}
	
	
	/**
     *  根据输入的地点坐标计算中心点
     * @param geoCoordinateList
     * @return
     */

    public static Map<String,Double> getCenterPoint(List<?> list) {
    	String[] arr = new String[list.size()];
    	for(int i=0;i<list.size();i++){
    		Object strs[] = (Object[]) list.get(i);
    		arr[i] = strs[0] + "," + strs[1];
    	}
    	return getCenterPoint(arr);
    }
    
    
    public static Map<String,Double> getCenterPoint(String[] arr) {
    	
    	int total = arr.length;
    	double X = 0, Y = 0, Z = 0;
    	for(int i=0;i<arr.length;i++){
    		double lat, lon, x, y, z;
    		lon = Double.parseDouble(arr[i].split(",")[0]) * Math.PI / 180;
    		lat = Double.parseDouble(arr[i].split(",")[1]) * Math.PI / 180;
    		x = Math.cos(lat) * Math.cos(lon);
    		y = Math.cos(lat) * Math.sin(lon);
    		z = Math.sin(lat);
    		X += x;
    		Y += y;
    		Z += z;
    	}
    	
    	X = X / total;
    	Y = Y / total;
    	Z = Z / total;
    	double Lon = Math.atan2(Y, X);
    	double Hyp = Math.sqrt(X * X + Y * Y);
    	double Lat = Math.atan2(Z, Hyp);
    	
    	Map<String,Double> map = new HashMap<String,Double>();
    	map.put("lng", Lon * 180 / Math.PI);
    	map.put("lat", Lat * 180 / Math.PI);
    	return map;
    }

    
	// double转string
	public static String convertDoubleToString(Object val) {
		if(val == null){return "--";}
		BigDecimal bd = new BigDecimal(String.valueOf(val));
		return bd.stripTrailingZeros().toPlainString();
	}
}
