package com.censoft.gaode.nidizhi;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.censoft.database.MysqlHelper;
import com.censoft.util.CoordinateTransform;

public class GetGaoDePoi {
	
	private Connection conn = null;
	private final static String url_pre = "https://restapi.amap.com/v3/geocode/regeo?output=json&location=";
//	private final static String url_after = "&key=13432b0daf5b980a9444f058e3322ddd&radius=1&extensions=all";
	private final static String url_after = "&key=a7bc09d8cc6cfa5f98731baec37ae4cc&radius=1&extensions=all";
	
	
	public synchronized void handleList(List<Map<String, Object>> data, int threadNum) {
        int length = data.size();
        int tl = length % threadNum == 0 ? length / threadNum : (length / threadNum + 1);

        for (int i = 0; i < threadNum; i++) {
            int end = (i + 1) * tl;
            HandleThread thread = new HandleThread("线程[" + (i + 1) + "] ",  data, i * tl, end > length ? length : end);
            thread.start();
        }
    }
	
	public void go() {
        try {
        	conn = MysqlHelper.getConnection("jdbc:mysql://192.168.5.72:3311/buliding_lab?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true","root","mysql");
        	List<Map<String, Object>> list1 = MysqlHelper.executeQuery(" select id,lng,lat,gaodedom from s_ly_jiaoyan_all_chuli where gaodedom is null ");
        	System.out.println(list1.size());
        	handleList(list1,1);

        } catch (Exception e) {
        	System.out.println(e);
            return;
        } finally {
            try {
        		if(conn!=null){
        			MysqlHelper.close();
        		}
            } catch (Exception e) {
            	System.out.println("关闭数据流出错！错误信息：" + e.getMessage());
                return;
            }
        }
		
	}
	
	
	class HandleThread extends Thread {
        private String threadName;
        private List<Map<String, Object>> data;
        private int start;
        private int end;

        public HandleThread(String threadName, List<Map<String, Object>> data, int start, int end) {
            this.threadName = threadName;
            this.data = data;
            this.start = start;
            this.end = end;
        }

        public void run() {
        	
        	MysqlHelper.getConnection("jdbc:mysql://192.168.5.72:3311/buliding_lab?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true","root","mysql");
        	
            List<Map<String, Object>> subList = data.subList(start, end);
            
            for(Map<String, Object> map1: subList){
        		String id = (String)map1.get("id");
        		double blng = Double.valueOf((String)map1.get("lng"));
        		double blat = Double.valueOf((String)map1.get("lat"));
        		
        		double[] gdlnglats = CoordinateTransform.bd_to_gaode(blat, blng);
        		
        		String lng = String.valueOf(gdlnglats[0]);
        		String lat = String.valueOf(gdlnglats[1]);
        		
        		Document doc = null;
				try {
					doc = Jsoup.connect(url_pre+lng+","+lat+url_after)
						.ignoreContentType(true)
						.userAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.15)")
						.get();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(doc==null){
					System.out.println("访问地址"+url_pre+lng+","+lat+url_after+"失败...");
				}
        		Element body = doc.body();
        		JSONObject json = JSONObject.parseObject(body.text());
        		System.out.println(json);
        		JSONObject jsonObject = json.getJSONObject("regeocode");
        		
        		String addr = jsonObject.getString("formatted_address");
        		System.out.println(addr);
        		if(addr!=null && !"".equals(addr)){
        			MysqlHelper.executeUpdate("update s_ly_jiaoyan_all_chuli set gaodedom = '"+addr+"' where id = '"+id+"'");
        		}
        	}
            
            
			System.out.println(threadName+"处理了"+subList.size()+"条！");
        }

    }
	
	public static void main(String[] args) {
		
		GetGaoDePoi obj = new GetGaoDePoi();
		obj.go();

	}
	
//	public static void main(String[] args) {
//		double[] bd_to_gaode = CoordinateTransform.bd_to_gaode(39.94762747161147,116.26110983075517);
//		System.out.println(bd_to_gaode[0]);
//		System.out.println(bd_to_gaode[1]);
//	}
}
