package com.censoft.out.jnl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.censoft.common.base.Pub;
import com.censoft.database.MysqlHelper;
import com.censoft.jsoup.fangguan.Utils;
import com.censoft.util.CoordinateTransform;

public class Yyly {
	
	public static void main(String[] args) {
		Yyly l = new Yyly();
		Connection conn = null;
		Pub pub = new Pub();
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		try {
			conn = MysqlHelper.getConnection("jdbc:mysql://192.168.5.72:3311/buliding_lab?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true","root","mysql");
			
			List<Map<String, Object>> executeQuery = MysqlHelper.executeQuery(" select cjjzwbm,lng3,lat3 from s_ly_jiaoyan_20202959_2 where sfyyly = 'y' ");
			if(!executeQuery.isEmpty()){
				for (int i = 0; i < executeQuery.size(); i++) {
					Map<String, Object> mm = executeQuery.get(i);
					String buildcode = pub.trimNull((String) mm.get("cjjzwbm"));
					double latitude = Double.valueOf((String) mm.get("lat3"));
					double longitude =Double.valueOf((String) mm.get("lng3"));
					double[] ds = CoordinateTransform.transformBD09ToWGS84(longitude,latitude);
					Map<String, String> rm = new HashMap<String, String>();
					rm.put("buildcode", buildcode);
					rm.put("wgs84lon", String.valueOf(ds[0]));
					rm.put("wgs84lat", String.valueOf(ds[1]));
					list.add(rm);
				}
			}
			
			
			System.out.println(JSON.toJSONString(list));
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			MysqlHelper.close();
		}
	}
}
