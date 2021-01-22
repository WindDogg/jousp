package com.censoft.jsoup.gaode;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.censoft.baidutools.GetLatAndLngByBaidu;
import com.censoft.common.base.Pub;
import com.censoft.database.MysqlHelper;
import com.censoft.jsoup.Utils;
import com.censoft.util.CoordinateTransform;

/**
 * 海淀区+学校
 *
 */
public class GaodePoiInfo {

//	private final static String keyword = "海淀区+学校";
	private final static String keyword = "医院";
	private final static String url_pre = "https://www.amap.com/service/poiInfo?query_type=TQUERY&pagesize=20&pagenum=";
	private final static String url_after = "&qii=true&cluster_state=5&need_utd=true&utd_sceneid=1000&div=PC1000&addr_poi_merge=true&is_classify=true&zoom=16.14&city=110000&geoobj=116.299657%7C39.955604%7C116.351802%7C40.004469&classify_data=business_area_flag%3D1%3Badcode%3D110108%2Breserved_keywords%3Dtrue%2Bsort_rule%3D0%3Breserved_keywords%3Dtrue&user_loc=116.308348%2C39.978793&keywords="+keyword;
	private final static String cookies = "UM_distinctid=176d208b4ac440-0ece6466de0326-5c19341b-13c680-176d208b4ad6c2; cna=P+HKFVSb9G0CAXL332rXhAfc; xlly_s=1; guid=13bd-f2f4-5718-225c; CNZZDATA1255626299=1835711296-1609839820-https%253A%252F%252Fwww.amap.com%252F%7C1610354464; x5sec=7b22617365727665723b32223a226164393965306361633636643163616636366131326530383137313734616138434e2b303850384645506641684c32556c2b577864673d3d227d; tfstk=cXqVBbVmCiI2Spo1Jmiw5X1hIo3AZ9E3XQksoPsDgMsAWf0cimAtE8Grz2Gdqqf..; l=eBLGmtmVQTcNqT3FBOfZnurza779QIRAguPzaNbMiOCPOe5e5QlGWZ8pVmYwCnGVh6-eR3luOqSgBeYBq1bh_XOwsThLxuMmn; isg=BCMjFX54ePb4cjUrMqnXKCWHsmfNGLdai-baBlWAagL5lEO23eiXqzWKjmSaNA9S";

	
	public static void main(String[] args) throws InterruptedException, IOException {
		Connection conn = null;
		conn = MysqlHelper.getConnection(
				"jdbc:mysql://192.168.5.72:3311/cenmds?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true",
				"root", "mysql");
		Document doc = null;
		
		int pagenum = 1;
		int pagetotal = 16;
		Random random = new Random();
		
		a:for(int i = pagenum; i<=pagetotal; i++){
			Map<String, String> cookiem = Utils.convertCookie(cookies);
			
			doc = Jsoup.connect(url_pre + i + url_after)
					.cookies(cookiem)
					.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
					.header("Accept-Encoding", "gzip, deflate, br")
					.header("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2")
					.header("amapuuid", "93f80bfb-4080-40f6-a050-e80790bbde87")
					.header("Cache-Control", "no-cache")
					.header("Connection", "keep-alive")
					.header("Host", "www.amap.com")
					.header("Pragma", "no-cache")
					.header("sec-ch-ua", "\"Google Chrome\";v=\"87\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"87\"")
					.header("sec-ch-ua-mobile", "?0")
					.header("Sec-Fetch-Dest", "document")
					.header("Sec-Fetch-Mode", "navigate")
					.header("Sec-Fetch-Site", "none")
					.header("Sec-Fetch-User", "?1")
					.header("Upgrade-Insecure-Requests", "1")
					.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36")
//					.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0")
					.ignoreContentType(true)
					.get();
			if (doc == null) {
				System.out.println("访问地址" + url_pre + i + "失败...");
			}
			Element body = doc.body();
	
			JSONObject json = JSONObject.parseObject(body.text());
			System.out.println(json);
			JSONObject data = json.getJSONObject("data");
			if(data==null){
				System.out.println("第"+i+"页----失败");
			}
			JSONArray poi_list = data.getJSONArray("poi_list");
			b:for(int j=0;j<poi_list.size();j++){
				JSONObject poi = poi_list.getJSONObject(j);
				String id = poi.getString("id");
				String name = poi.getString("name");
				List<Map<String, Object>> isExistId = MysqlHelper.executeQuery("select 1 from xxandyy where id = '"+id+"'");
				if(isExistId!=null && !isExistId.isEmpty()){
					System.out.println(name+"----------------------存在");
					MysqlHelper.executeUpdate("update xxandyy set type = 'hospital' where id = '"+id+"'");
					continue b;
				}
				
				System.out.println("新增"+name+"----------------------");
				MysqlHelper.executeUpdate("insert into xxandyy values ('"+id+"','"+name+"',null,'','','y','','school')");
			}
			
			Thread.sleep(random.nextInt(10000)+8000);
		}
		
		MysqlHelper.close();

	}
	

}
