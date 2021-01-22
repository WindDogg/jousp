package com.censoft.jsoup.gaode;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.censoft.baidutools.GetLatAndLngByBaidu;
import com.censoft.common.base.Pub;
import com.censoft.database.MysqlHelper;
import com.censoft.util.CoordinateTransform;

/**
 * 抓楼坐标
 *
 */
public class GaodeXxAndYy {

	private final static String url_pre = "https://www.amap.com/detail/get/detail?id=";
	private final static String url_after = "&smToken=as&smSign=undefined";

	public static void main(String[] args) {
		Connection conn = null;
		conn = MysqlHelper.getConnection(
				"jdbc:mysql://192.168.5.72:3311/cenmds?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true",
				"root", "mysql");
		List<Map<String, Object>> list1 = MysqlHelper.executeQuery("select * from xxandyy");
		for (Map<String, Object> map1 : list1) {
			String sys_id = (String) map1.get("sys_id");
			String id = (String) map1.get("id");
			Document doc = null;
			
			try {
				Response res = Jsoup.connect(url_pre + id + url_after).timeout(30000).execute();
				Map<String, String> cookies = res.cookies();
				System.out.println(cookies);
				doc = Jsoup.connect(url_pre + id + url_after).cookies(cookies).get();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (doc == null) {
				System.out.println("访问地址" + url_pre + id + "失败...");
			}
			Element body = doc.body();

			JSONObject json = JSONObject.parseObject(body.text());
			System.out.println(json);
			JSONObject data = json.getJSONObject("data");
			System.out.println(data);
			JSONObject spec = data.getJSONObject("spec");
			JSONObject mining_shape = spec.getJSONObject("mining_shape");
			if (mining_shape.isEmpty()) {
				System.out.println("id=" + id + "无空间数据...");
				continue;
			}

			String shape = mining_shape.getString("shape");

			MysqlHelper.executeUpdate("update xxandyy set shape='" + shape + "' where sys_id = '" + sys_id + "'");
		}

	}
}
