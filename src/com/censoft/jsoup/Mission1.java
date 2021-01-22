package com.censoft.jsoup;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.censoft.database.MysqlHelper;

// http://ss.zhongguancun.com.cn/sl/jzbgq/ 抓 孵化器企业地址
public class Mission1 {

	private static final String rootUrl = "http://ss.zhongguancun.com.cn";
	
	public void saveDataBase(List<Map<String, String>> list) throws SQLException{
		Connection conn = MysqlHelper.getConnection();
		ResultSet rs = null;
		Statement ps = conn.createStatement();
		
		StringBuffer sql = new StringBuffer("insert into cj_fhq values ");
		for(int i=0;i<list.size();i++){
			sql.append("('");
			Map<String, String> map = list.get(i);
			String id = map.get("id");
			sql.append(id);
			sql.append("','");
			String type = map.get("type");
			sql.append(type);
			sql.append("','");
			String qymc = map.get("qymc");
			sql.append(qymc);
			sql.append("','");
			String dz = map.get("dz");
			sql.append(dz);
			sql.append("','");
			String lxr = map.get("lxr");
			sql.append(lxr);
			sql.append("','");
			String dh = map.get("dh");
			sql.append(dh);
			sql.append("'),");
		}
		
		String s = sql.substring(0, sql.length()-1);
		System.out.println(s);
		ps.execute(s);
		
		MysqlHelper.close();
	}
	
	public void praseHtml(String url) throws IOException, SQLException{
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		Document doc = Jsoup.connect(url).get();
		String nextPage = doc.select(".pages .an_R").attr("onclick").replace("channelUrlInterceptor('", "").replace("');", "");
		Elements listEle = doc.select("#con2 li .ico");
		for(Element liEle : listEle){
			String detailUrl = liEle.attr("href");
			Map<String, String> map = praseDetail(rootUrl+detailUrl);
			list.add(map);
		}
		saveDataBase(list);
		
		System.out.println(nextPage);
		if(!"".equals(nextPage)){
			String[] urlArr = url.split("/");
			urlArr[urlArr.length-1] = nextPage;
			praseHtml(StringUtils.join(urlArr, "/"));
		}
	}
	
	public Map<String, String> praseDetail(String url) throws IOException{
		Document doc = Jsoup.connect(url).get();
		Element titleEle = doc.select(".tit").get(0);
		String title = titleEle.text();
		String type = "";
		String qymc = "";
		if(title.indexOf(">")>-1){
			String[] titles = title.split(">");
			type = titles[titles.length-2].trim();
			qymc = titles[titles.length-1].trim();
		}
		
		String html = doc.select(".js").html();
		String[] htmlArr = html.split("<br>");
		String dz = htmlArr[1].split("：")[1].trim();
		String lxr = htmlArr[2].split("：")[1].trim();
		String dh = htmlArr[3].split("：")[1].trim();

		Map<String, String> map = new HashMap<String, String>();
		map.put("type", type);
		map.put("qymc", qymc);
		map.put("dz", dz);
		map.put("lxr", lxr);
		map.put("dh", dh);
		map.put("id", UUID.randomUUID().toString().replace("-", ""));
		
		return map;
	}
	
	public static void main(String[] args) throws IOException, SQLException {
		
		String url1 = rootUrl + "/sl/jzbgq/index.html";
		String url2 = rootUrl + "/sl/mlfhq/index.html";
		String url3 = rootUrl + "/sl/mlcyy/index.html";
		String url4 = rootUrl + "/sl/mlkjy/index.html";
		String url5 = rootUrl + "/sl/mljsq/index.html";
		String url6 = rootUrl + "/sl/mlyq/index.html";
		
		
		Mission1 m1 = new Mission1();
		m1.praseHtml(url1);
		m1.praseHtml(url2);
		m1.praseHtml(url3);
		m1.praseHtml(url4);
		m1.praseHtml(url5);
		m1.praseHtml(url6);
	}
}
