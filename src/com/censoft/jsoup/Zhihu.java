package com.censoft.jsoup;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Zhihu {
	
	
	public void praseHtml(String url) throws IOException{
		Document doc = Jsoup.connect(url).get();
		System.out.println(doc);
	}
	
	
	public static void main(String[] args) {
		
		String url1 = "https://www.zhihu.com/people/mllh/asks";
		String url2 = "https://www.zhihu.com/people/mllh/answers";
		
		Zhihu zh = new Zhihu();
		try {
//			zh.praseHtml(url1);
			zh.praseHtml(url2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
