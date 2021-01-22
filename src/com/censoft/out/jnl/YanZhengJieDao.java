package com.censoft.out.jnl;

import java.io.File;
import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.censoft.util.CoordinateTransform;
import com.censoft.util.FileManager;

public class YanZhengJieDao {

	public void go(String filepath) throws IOException {
		
		File jsonFile = new File(filepath);
		String readFile = FileManager.readFile(jsonFile);
		System.out.println(readFile);
		JSONObject jsonObject = JSON.parseObject(readFile);
		JSONArray jsonArray = jsonObject.getJSONArray("features");
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jdObject = jsonArray.getJSONObject(i);
			JSONObject jdPropertie = jdObject.getJSONObject("properties");
			String jdname = jdPropertie.getString("JDNAME");
			if(jdname.endsWith("办事处")){
				jdname = jdname.substring(0, jdname.length()-3);
			}
			jdPropertie.put("gridname", jdname);
			jdPropertie.put("gridid", (110108100+i)+"");
//			double[] xys = CoordinateTransform.transformWGS84ToBD09(jdPropertie.getDoubleValue("X"), jdPropertie.getDoubleValue("Y"));
//			jdPropertie.put("X", xys[0]);
//			jdPropertie.put("Y", xys[1]);
			
			JSONObject jdGeometry = jdObject.getJSONObject("geometry");
			JSONArray jsonArray2 = jdGeometry.getJSONArray("coordinates");
			if(jsonArray2.size()>1){
				jdGeometry.put("type", "MultiPolygon");
			}
			for(int j=0;j<jsonArray2.size();j++){
				JSONArray jsonArray3 = jsonArray2.getJSONArray(j);
				for(int k=0;k<jsonArray3.size();k++){
					JSONArray lnglats = jsonArray3.getJSONArray(k);
//					double[] lnglat = CoordinateTransform.transformWGS84ToBD09(lnglats.getDoubleValue(0), lnglats.getDoubleValue(1));
//					lnglats.set(0, lnglat[0]);
//					lnglats.set(1, lnglat[1]);
					lnglats.set(0, lnglats.getDoubleValue(0));
					lnglats.set(1, lnglats.getDoubleValue(1));
				}
			}
		}
		
		FileManager.saveFile("海淀街道_CGCS2000_拆分_BaiduLngLat2.json", jsonObject.toJSONString());
	}
	
	public static void main(String[] args) throws IOException {
		YanZhengJieDao obj = new YanZhengJieDao();
		obj.go("E:/work/workspace2016/report/WebRoot/map/json/海淀街道_CGCS2000_拆分_BaiduLngLat.json");
	}
	
}
