package com.censoft.jsoup.fangguan;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.censoft.database.MysqlHelper;
import com.censoft.jsoup.Utils;

public class Zhua45Yuanqu {

	private final static String loginUrl = "http://123.56.211.235:8099/SJCJProject/login/userLogin";
	private final static String zhuaquUrl2 = "http://123.56.211.235:8099/SJCJProject/gisAction/projectQueryYq";
	Connection conn = null;
	private final static String username = "fgj1031";
	private final static String pwd = "010gaojinping";
	public static String USER_AGENT = "User-Agent";
	public static String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36";

	public void go() throws IOException, InterruptedException {
		org.jsoup.Connection con = Jsoup.connect(loginUrl).ignoreContentType(true); // 获取connection
		con.header(USER_AGENT, USER_AGENT_VALUE); // 配置模拟浏览器
		Map<String, String> datas1 = new HashMap<String, String>();
		datas1.put("username", username);
		datas1.put("password", pwd);
		Response rs1 = con.data(datas1).method(Method.POST).execute(); // 获取响应
		Map<String, String> cookies = rs1.cookies();
//		Document docLogin = Jsoup.parse(rs.body()); // 通过Jsoup将返回信息转换为Dom树
//		System.out.println(docLogin);
		
		
		
		org.jsoup.Connection con2 = Jsoup.connect(zhuaquUrl2).ignoreContentType(true); // 获取connection
		Map<String, String> datas2 = new HashMap<String, String>();
		datas2.put("numberPerPage", "50");
		datas2.put("currentPageIndex", "1");
		datas2.put("parkName", "");
		Response rs2 = con2.cookies(cookies).data(datas2).method(Method.POST).execute(); // 获取响应
		Document doc2 = Jsoup.parse(rs2.body()); // 通过Jsoup将返回信息转换为Dom树
		System.out.println(doc2);
		
		
		conn = MysqlHelper.getConnection(
				"jdbc:mysql://192.168.5.72:3311/cenmds?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true",
				"root", "mysql");

		MysqlHelper.close();
	}

	public static void main(String[] args) {
		Zhua45Yuanqu obj = new Zhua45Yuanqu();
		try {
			obj.go();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

}
