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

public class SLyJiaoyanAllChuliRGBM {
	
	private static String type = "学校医院";
	private static String geotype = "Polygon";

	public void go() throws IOException {
		MysqlHelper.getConnection("jdbc:mysql://192.168.5.72:3311/buliding_lab?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true","root","mysql");
		List<Map<String, Object>> list = MysqlHelper.executeQuery(" select xxyyid, count(*) c, max(dsmj) dsmj  from s_ly_jiaoyan_all_chuli where xxyyid is not null group by xxyyid ");
		for(int i=0;i<list.size();i++){
			System.out.println(i);
			Map<String, Object> map = list.get(i);
			String xxyyid = StringUtils.trimToEmpty((String)map.get("xxyyid"));
			String c = StringUtils.trimToEmpty((String)map.get("c"));
			String dsmj = StringUtils.trimToEmpty((String)map.get("dsmj"));
			if("1".equals(c)){
				MysqlHelper.executeUpdate("update s_ly_jiaoyan_all_chuli set rgbm = concat('H',cjjzwbm) where xxyyid = '"+xxyyid+"'");
			}else{
				String sql2 = " select cjjzwbm from s_ly_jiaoyan_all_chuli where xxyyid = '"+xxyyid+"'";
				if(!"".equals(dsmj)){
					sql2 += " and dsmj = '"+dsmj+"' ";
				}
				List<Map<String, Object>> list2 = MysqlHelper.executeQuery(sql2);
				Map<String, Object> map2 = list2.get(0);
				String cjjzwbm = StringUtils.trimToEmpty((String)map2.get("cjjzwbm"));
				MysqlHelper.executeUpdate("update s_ly_jiaoyan_all_chuli set rgbm = concat('H','"+cjjzwbm+"') where xxyyid = '"+xxyyid+"'");
			}
		}
		
		MysqlHelper.close();
	}
	
	public static void main(String[] args) throws IOException {
		SLyJiaoyanAllChuliRGBM obj = new SLyJiaoyanAllChuliRGBM();
		obj.go();
	}
	
}
