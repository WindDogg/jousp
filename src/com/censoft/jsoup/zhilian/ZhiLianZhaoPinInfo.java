package com.censoft.jsoup.zhilian;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.censoft.database.MysqlHelper;
import com.censoft.jsoup.Utils;
import com.censoft.jsoup.tianyancha.TianYanChaColsInfo;

/**
 * 抓取智联
 * SELECT * from hb_end_202012181857006_rel_rel3 where sfzdqy='y'
 */

public class ZhiLianZhaoPinInfo {

	
	private final static String url_pre = "https://fe-api.zhaopin.com/c/i/company/area-position?S_SOU_COMPANY_ID=";
	private final static String url_after = "&pageSize=100&pageIndex=1&S_SOU_SALARY_MIN=,&S_SOU_SALARY_MAX=,&S_SOU_WORK_CITY=&S_SOU_JD_JOB_LEVEL=&at=03be05e5278c4440b2c7cbc552ac8355&rt=de488d5d416a426e8a553f58b30b12fd&_v=0.75848302&x-zp-page-request-id=7788078f9e0b4340884577c995c2c6c2-1610432237408-882565&x-zp-client-id=b7e449cf-5e30-4044-828f-528d69bc9745";
	private final static String cookies = "x-zp-client-id=85a08f44-cb6a-43ff-a2ce-bb337039f6e7; urlfrom=121126445; urlfrom2=121126445; adfcid=none; adfcid2=none; adfbid=0; adfbid2=0; sts_deviceid=176f5f32c34463-044c355ddfc352-3a3d530a-1296000-176f5f32c35720; acw_tc=2760829416104446615973878eeb6f96d45d394ee05543fefe111b6024a678; sajssdk_2015_cross_new_user=1; sts_sg=1; sts_sid=176f5f32c62727-0d6cffe168a5a-3a3d530a-1296000-176f5f32c635f2; sts_chnlsid=Unknown; Hm_lvt_38ba284938d5eddca645bb5e02a02006=1610444123; locationInfo_search={%22code%22:%22530%22%2C%22name%22:%22%E5%8C%97%E4%BA%AC%22%2C%22message%22:%22%E5%8C%B9%E9%85%8D%E5%88%B0%E5%B8%82%E7%BA%A7%E7%BC%96%E7%A0%81%22}; zp_passport_deepknow_sessionId=e265672ase1e4342b19ca8fe55d0d596ca7e; at=1346490274094a4b8cd39d612dcf7e4d; rt=619d3f8d5c414219ab233de421b1c8ba; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22149941932%22%2C%22first_id%22%3A%22176f5f32c44456-0332b22eeee6d7-3a3d530a-1296000-176f5f32c4574d%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22%24device_id%22%3A%22176f5f32c44456-0332b22eeee6d7-3a3d530a-1296000-176f5f32c4574d%22%7D; ssxmod_itna=QqUx97eYwx2WDXDnnG=5DKgKGw4qWTqitFeFDlcD4xA5D8D6DQeGTi2bC7is3G7BiDP0xF9GxcYQokGePxiqTRL4GLDmKDyKA74eDxaq0rD74irDDxD3cD7PGmDieC0E5qAQDKxB=DRx07B15DWxDFEPPKEo5F/h5GDiyDRciqExG1DQ5Dsx4vd4DC1/oKDm43w25sDCKDj=YC8xYDUnweAAGei0qNqmkqeGh4w02tCDrxYlw57C4q3i+5wBaPa2dDi1xXFYD===; ssxmod_itna2=QqUx97eYwx2WDXDnnG=5DKgKGw4qWTqitFeD613h=D0Hw4P03bGwnj6D6nYwoqkHw5RnxbxiHlDamq0r41fMKWzZnrDYKhVnOiK5hHOF6ryjh6Flkg2R3NA799LxXTxbzY5zV32KEOF1wC4Dw6PDLxG77YD=; zp_src_url=http%3A%2F%2Fjobs.zhaopin.com%2F; LastCity=%E5%8C%97%E4%BA%AC; LastCity%5Fid=530; Hm_lpvt_38ba284938d5eddca645bb5e02a02006=1610445448; selectCity_search=530; ZL_REPORT_GLOBAL={%22jobs%22:{%22recommandActionidShare%22:%22bb2cad81-e73a-443a-8deb-8050a781ca80-job%22%2C%22funczoneShare%22:%22dtl_best_for_you%22}%2C%22company%22:{%22actionid%22:%227a729961-166c-432d-a628-6ea67ebb32ca-company%22%2C%22funczone%22:%22hiring_jd%22}}; sts_evtseq=18";

	public void go() throws InterruptedException, IOException {
		Connection conn = null;
		conn = MysqlHelper.getConnection(
				"jdbc:mysql://192.168.5.72:3311/cenmds?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true",
				"root", "mysql");
		
		List<Map<String, Object>> onameList = MysqlHelper.executeQuery("SELECT zpid from company_net_zhaopin where state ='1'");
		
		Document doc = null;
		Random random = new Random();
		
		for(int i = 0; i<onameList.size(); i++){
			String oname = StringUtils.trimToEmpty((String)onameList.get(i).get("zpid"));
			System.out.println(i+"---------"+oname);
			Map<String, String> cookiem = Utils.convertCookie(cookies);
			String url = url_pre + oname + url_after;
			doc = Jsoup.connect(url)
					.cookies(cookiem)
					.header("authority", "fe-api.zhaopin.com")
					.header("method", "GET")
					.header("path", url.substring(url.indexOf("/c/i/"),url.length()))
					.header("scheme", "https")
					.header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
					.header("accept-encoding", "gzip, deflate, br")
					.header("accept-language", "zh-CN,zh;q=0.9")
					.header("cache-control", "no-cache")
					.header("pragma", "no-cache")
					.header("sec-ch-ua", "\"Google Chrome\";v=\"87\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"87\"")
					.header("sec-ch-ua-mobile", "?0")
					.header("sec-fetch-dest", "document")
					.header("sec-fetch-mode", "navigate")
					.header("sec-fetch-site", "none")
					.header("sec-fetch-user", "?1")
					.header("upgrade-insecure-requests", "1")
					.header("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36")
//					.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0")
					.ignoreContentType(true)
					.get();
			if (doc == null) {
				System.out.println("访问地址" + url_pre + oname + url_after + "失败...");
			}
			Element body = doc.body();
	
			JSONObject json = JSONObject.parseObject(body.text());
			JSONObject bdataJson = json.getJSONObject("data");
			if(bdataJson==null){
				/*MysqlHelper.executeUpdate("update company_net_zhaopin set zpid = ?, state = '0' where type='zhilian' and oname=?",new String[]{"",oname});*/
				continue;
			}
			JSONArray ja = bdataJson.getJSONArray("list");
			
			if(ja.size()>0){
			    
			    for (int j=0;j<ja.size(); j++) {
				JSONObject jo = ja.getJSONObject(j);
				MysqlHelper.executeUpdate("insert into zhaopin_info( cityDistrict, cityId, companyName, companyNumber, companySize, companyUrl, education, emplType, expandCount, industryName, industryNameLevel, jobDesc, jobId, jobType, jobTypeLevel, name, number, positionURL, property, publishTime, publishTimeDt, salary, tradingArea, workCity, workingExp, workingExpCode) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					                   new String[]{jo.getString("cityDistrict"),jo.getString("cityId"),jo.getString("companyName"),jo.getString("companyNumber"),jo.getString("companySize"),jo.getString("companyUrl"),jo.getString("education"),jo.getString("emplType"),jo.getString("expandCount"),jo.getString("industryName"),jo.getString("industryNameLevel"),
					                                jo.getString("jobDesc"),jo.getString("jobId"),jo.getString("jobType"),jo.getString("jobTypeLevel"),jo.getString("name"),jo.getString("number"),jo.getString("positionURL"),jo.getString("property"),jo.getString("publishTime"),jo.getString("publishTimeDt"),jo.getString("salary"),jo.getString("tradingArea"),jo.getString("workCity"),jo.getString("workingExp"),jo.getString("workingExpCode")});
			    }
				
			}
			
			
			Thread.sleep(random.nextInt(10000)+8000);
		}
		
		MysqlHelper.close();    
	}
	
	
	public static void main(String[] args) throws InterruptedException, IOException {
		
		ZhiLianZhaoPinInfo obj = new ZhiLianZhaoPinInfo();
		obj.go();

	}
	
	
	
}
