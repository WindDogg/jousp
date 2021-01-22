package com.censoft.out.hwzhao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.censoft.database.MysqlHelper;
import com.censoft.out.utils.Utils;
import com.censoft.util.CoordinateTransform;
import com.censoft.util.FileManager;
import com.censoft.util.Pinyin4jUtils;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Test {

	public void go(String filepath) throws IOException, BadHanyuPinyinOutputFormatCombination {
		
		File jsonFile = new File(filepath);
		String readFile = FileManager.readFile(jsonFile);
		System.out.println(readFile);
		JSONObject jsonObject = JSON.parseObject(readFile);
		JSONArray jsonArray = jsonObject.getJSONArray("features");
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jdObject = jsonArray.getJSONObject(i);
			
			JSONObject jdGeometry = jdObject.getJSONObject("geometry");
			
			JSONObject jdProperties = jdObject.getJSONObject("properties");
			String name = jdProperties.getString("gridname");
			String geotype = jdGeometry.getString("type");
			String jdenname = Pinyin4jUtils.toPinYinLowercase(name);
			if(jdenname.endsWith("jd")){
				jdenname = jdenname.substring(0, jdenname.length()-2);
			}
			
			JSONArray jsonArray2 = jdGeometry.getJSONArray("coordinates");
			
			for(int j=0;j<jsonArray2.size();j++){
				StringBuffer sb = new StringBuffer("//"+name+"\nstatic String jd_");
				sb.append(jdenname);
				if("MultiPolygon".equals(geotype)){
					sb.append(j+1);
				}
				sb.append(" = \"[");
				JSONArray jsonArray3 = jsonArray2.getJSONArray(j);
				for(int k=0;k<jsonArray3.size();k++){
					JSONArray lnglats = jsonArray3.getJSONArray(k);
					double[] lnglat = CoordinateTransform.transformWGS84ToBD09(lnglats.getDoubleValue(0), lnglats.getDoubleValue(1));
					sb.append("{'lat':'"+lnglat[1]+"','lng':'"+lnglat[0]+"'},");
				}
				String s = sb.substring(0, sb.length()-1);
				s += "]\";";
				System.out.println(s);
			}
			
			
		}
		
	}
	
	public static void main(String[] args) throws IOException, BadHanyuPinyinOutputFormatCombination {
//		Test obj = new Test();
//		obj.go("E:/work/workspace2016/report/WebRoot/map/json/hdjd_new_baidu.json");
		
		String a = "116.29663989151879,39.943040947382713],[116.29660770478279,39.943057040750716],[116.29654869666604,39.943116048867466],[116.29664525597479,39.943255523824291],[116.29726216391884,39.943035582926711],[116.29714951124197,39.942858556777878],[116.29663989151879,39.943040947382713";
		List<String[]> list = new ArrayList<String[]>();
		String as[] = a.split("],\\[");
		for(int i=0; i<as.length;i++){
			String lnglat[] = as[i].split(",");
			list.add(lnglat);
		}
		System.out.println(Utils.getCenterPoint(list));
	}
	
}
