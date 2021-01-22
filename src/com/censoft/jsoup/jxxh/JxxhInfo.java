package com.censoft.jsoup.jxxh;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
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

public class JxxhInfo {

	private final static String loginUrl = "https://qyfg.yqgz.beijing.gov.cn/api/authorization/apaaslogin/sign-in-auth";
	private final static String zhuaquUrl2 = "https://qyfg.yqgz.beijing.gov.cn/wyj-statistics/company/info/searchCompanyInfoPage";
	Connection conn = null;
	private final static String username = "haidianqu";
	private final static String pwd = "880184335d152da88d82886b3ec6d682";
	public static String USER_AGENT = "User-Agent";
	public static String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36";
	private final static String token = "52e377ebcd08b6830933fc6579d12c90";

	public void go(int pageNum, int pageSize) throws IOException, InterruptedException {
//		org.jsoup.Connection con = Jsoup.connect(loginUrl).ignoreContentType(true); // 获取connection
//		con.header(USER_AGENT, USER_AGENT_VALUE); // 配置模拟浏览器
//		Map<String, String> datas1 = new HashMap<String, String>();
//		datas1.put("userName", username);
//		datas1.put("password", pwd);
//		datas1.put("uuid", "c710a58a-64d6-42cf-83ce-075baa394692");
//		datas1.put("code", "yymg5");
//		Response rs1 = con.data(datas1).method(Method.POST).execute(); // 获取响应
//		Map<String, String> cookies = rs1.cookies();
//		Document docLogin = Jsoup.parse(rs1.body()); // 通过Jsoup将返回信息转换为Dom树
//		System.out.println(cookies);
//		System.out.println(docLogin);
//		Element body = docLogin.body();
//		JSONObject json = JSONObject.parseObject(body.text());
//		JSONObject data = json.getJSONObject("data");
//		String token = data.getString("token");
//		System.out.println(token);
		
		
		org.jsoup.Connection con2 = Jsoup.connect(zhuaquUrl2)
			.header(USER_AGENT, USER_AGENT_VALUE)
			.header("X-Auth0-Token", token)
			.header("Content-Type","application/json")
			.ignoreContentType(true); // 获取connection

		Response rs2 = con2.requestBody("{page: "+pageNum+", limit: "+pageSize+", datePickerValue: [], startTime: \"\", endTime: \"\", area: \"\", buildingName: \"\", companyName: \"\", street: \"\"}").method(Method.POST).execute(); // 获取响应
//		System.out.println(rs2.body());
		Document doc2 = Jsoup.parse(rs2.body()); // 通过Jsoup将返回信息转换为Dom树
		JSONObject json = JSONObject.parseObject(doc2.text());
		//System.out.println(json);
		JSONObject dataJson = json.getJSONObject("data");
		JSONArray listJson = dataJson.getJSONArray("list");
		conn = MysqlHelper.getConnection(
				"jdbc:mysql://192.168.5.72:3311/cenmds?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true",
				"root", "mysql");
		
		for(int i=0;i<listJson.size();i++){
			JSONObject obj = listJson.getJSONObject(i);
			StringBuffer sb_col = new StringBuffer();
			StringBuffer sb_val = new StringBuffer();
			for(String str:obj.keySet()){
				sb_col.append("`"+str+"`,");
				sb_val.append("'"+String.valueOf(obj.get(str)).replace("null", "").replace("'", "")+"',");
			}
			
			String col = sb_col.substring(0,sb_col.length()-1);
			String val = sb_val.substring(0,sb_val.length()-1);
			
			String insertSql = "insert into jxxh_lysydw("+col+") values("+val+")";
			/*System.out.println(insertSql);*/
			MysqlHelper.executeUpdate(insertSql);
			
		}
		

		MysqlHelper.close();
	}

	public static void main(String[] args) {
		JxxhInfo obj = new JxxhInfo();
		try {
			//每页500
			int pageSize = 500;
			//总页数
			int totalPage = 61;
			Random random = new Random();
			for(int i=49;i<=totalPage;i++){
				obj.go(i,pageSize);
				Thread.sleep(random.nextInt(10000)+3000);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

}
