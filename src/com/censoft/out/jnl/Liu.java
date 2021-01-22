package com.censoft.out.jnl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.censoft.common.base.Pub;
import com.censoft.database.MysqlHelper;

public class Liu {
	
	public Double transformLon(Double x, Double y) {
		Double PI = 3.14159265358979324;
		Double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
		return ret;
	}

	public Double transformLat(Double x, Double y) {
		Double PI = 3.14159265358979324;
		Double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	public Map<String, String> baiduToWgs84(String bdLat, String bdLon) {
		Map<String, String> map = new HashMap<String, String>();
		Double PI = 3.14159265358979324;
		Double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
		Double x = Double.valueOf(bdLon) - 0.0065;
		Double y = Double.valueOf(bdLat) - 0.006;
		Double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		Double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		Double gcjLon = z * Math.cos(theta);
		Double gcjLat = z * Math.sin(theta);
		Double a = 6378245.0;
		Double ee = 0.00669342162296594323;
		Double dLat = transformLat(gcjLon - 105.0, gcjLat - 35.0);
		Double dLon = transformLon(gcjLon - 105.0, gcjLat - 35.0);
		Double radLat = gcjLat / 180.0 * PI;
		Double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		Double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * PI);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * PI);
		dLat = gcjLat - dLat;
		dLon = gcjLon - dLon;
		map.put("wgs84lat", String.valueOf(dLat));
		map.put("wgs84lon", String.valueOf(dLon));
		return map;
	}
	
	
	public static void main(String[] args) {
		Liu l = new Liu();
		Connection conn = null;
		Pub pub = new Pub();
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		try {
			conn = MysqlHelper.getConnection("jdbc:mysql://192.168.5.73:3312/cenmds?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true","root","mysql");
			
			List<Map<String, Object>> executeQuery = MysqlHelper.executeQuery(" select cjjzwbm,latitude,longitude from b_base_louyu_202001to11 where sfyyly = 'y' ");
			if(!executeQuery.isEmpty()){
				for (int i = 0; i < executeQuery.size(); i++) {
					Map<String, Object> mm = executeQuery.get(i);
					String buildcode = pub.trimNull((String) mm.get("cjjzwbm"));
					String latitude = pub.trimNull((String) mm.get("latitude"));
					String longitude = pub.trimNull((String) mm.get("longitude"));
					Map<String, String> rm = l.baiduToWgs84(latitude,longitude);
					rm.put("buildcode", buildcode);
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
