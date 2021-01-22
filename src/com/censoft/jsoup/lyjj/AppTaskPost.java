package com.censoft.jsoup.lyjj;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class AppTaskPost {
	
	public void go() throws IOException {
//		Connection.Response res = Jsoup.connect("http://lyjj.bjhdnet.com/lyjj/authenticate-pms")
//			    .data("userid", "root", "password", "cens0ft").ignoreContentType(true)
//			    .method(Method.POST)
//			    .execute();
		
		Connection c = Jsoup.connect("http://lyjj.bjhdnet.com/lyjj/authenticate-appcheckcode")
				.data("userid", "18810569428", "sfdl", "y").ignoreContentType(true)
			    .method(Method.POST);
		
		Connection.Response res = c.execute();
			
			
			
 
			Document doc = res.parse();
			System.out.println(doc);
			//这儿的SESSIONID需要根据要登录的目标网站设置的session Cookie名字而定
			Map<String, String> map = res.cookies(); 
			Connection connection = Jsoup.connect("http://lyjj.bjhdnet.com/lyjj/scheduler/run_once.json");
			Set<Entry<String, String>> entries = map.entrySet();
			for(Entry<String, String> entry:entries){
				connection.cookie(entry.getKey(), entry.getValue());
			}
			Document objectDoc = connection.data("id","117d8117038c4370b3503c1e199d5572","group","").ignoreContentType(true).post();
			System.out.println(objectDoc);

	}
	
	public static void main(String[] args) throws IOException {
		AppTaskPost obj = new AppTaskPost();
		obj.go();
	}

}
