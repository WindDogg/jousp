package com.censoft.yiqu16yuan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.censoft.database.MysqlHelper;

public class YiQu16YuanQiYe {

	public void getQyJson() {
		String sql = "SELECT t.QYMC, t.QYDZ, t.LONGITUDE, t.LATITUDE, t.yq, count(1) qynum FROM HD_DATARESULTNEW188 t WHERE t.LONGITUDE IS NOT NULL AND t.LATITUDE IS NOT NULL and t.yq != '' group by qymc, t.QYDZ, t.LONGITUDE, t.LATITUDE, t.yq order by yq ";
		List<Map<String, Object>> resultList = MysqlHelper.executeQuery(sql, null);
		for(int i=0;i<resultList.size();i++){
			Map<String, Object> map = resultList.get(i);
			map.put("id", (i+1)+"");
			map.put("oname", map.get("QYMC"));
			map.remove("QYMC");
			map.put("lng", map.get("LONGITUDE"));
			map.remove("LONGITUDE");
			map.put("dom", map.get("QYDZ"));
			map.remove("QYDZ");
			map.put("yq", map.get("YQ"));
			map.remove("YQ");
			map.put("lat", map.get("LATITUDE"));
			map.remove("LATITUDE");
		}
		
		String json = JSON.toJSONString(resultList);
		System.out.println(json);
		
		
	}
	
	public static void main(String[] args) {
		YiQu16YuanQiYe a = new YiQu16YuanQiYe();
		a.getQyJson();
	}
}
