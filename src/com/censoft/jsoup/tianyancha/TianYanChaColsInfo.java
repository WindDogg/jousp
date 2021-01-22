package com.censoft.jsoup.tianyancha;

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
 * 抓取天眼查接口返回的字段信息
 * code 452 报错
 *
 */
public class TianYanChaColsInfo {

	
	private final static String[] code = new String[]{"817","818","819","459","1042","1045","1047","1046","820","1050","821","877","997","998","823","876","824","963","825","822","878","1041","842","874","840","875","841","873","962","961","756","757","1015","1016","1036","1037","839","871","843","872","1014","1013","888","867","889","869","883","1024","1025","884","881","948","880","1029","1049","1048","887","886","885","949","950","951","952","882","946","947","879","953","1026","834","847","870","852","865","851","957","958","848","996","846","849","960","1023","850","796","844","866","955","956","795","845","868","1021","1022","1019","1020","1018","1017","838","1051","1027","837","836","833","1038","995","1033","449","450","451","448","783","453","455","945","747","1028","1044","943","630","427","426","632","633","778","458","457","776","755","1001","1002","1003","1004","1005","1034","1035","799","826","827","828","829","830","831","832","944","954","798","853","854","855","856","857","859","860","861","862","863","864","964","965","966","967","968","969","970","791","792","973","999","978","979","980","981","982","983","984","985","986","987","988","989","990","991","992","993","994","1030","1007","1008","1009","1010","1011","1012","816"};
	private final static String[] name = new String[]{"企业基本信息","企业基本信息（含联系方式）","企业基本信息（含主要人员）","特殊企业基本信息","企业三要素验证","工商快照","企业类型","企业联系方式","主要人员","历史主要人员","企业股东","历史股东信息","公司公示-股东出资","公司公示-股权变更","对外投资","历史对外投资","分支机构","总公司","企业年报","变更记录","历史工商信息","司法解析","法律诉讼","历史法律诉讼","开庭公告","历史开庭公告","法院公告","历史法院公告","送达公告","立案信息","司法协助","司法协助详情","历史司法协助","历史司法协助详情","破案重整","破案重整详情","被执行人","历史被执行人","失信人","历史失信人","限制消费令","终本案件","行政许可（工商局）","历史行政许可（工商局）","行政许可（信用中国）","历史行政许可（信用中国）","抽查检查","双随机抽查","双随机抽查详情","税务评级","进出口信用","电信许可","资质证书","公告研报","企业信用评级","一般纳税人","招投标","债券信息","购地信息","地块公示","地块公示详情","土地转让","土地转让详情","产品信息","供应商","客户","企业招聘","企业招聘（百聘）","企业微博","企业微信公众号","行政处罚（工商局）","历史行政处罚（工商局）","行政处罚（信用中国）","历史行政处罚（信用中国）","欠税公告","税收违法","税收违法详情","经营异常","历史经营异常","严重违法","清算信息","简易注销","询价评估","司法拍卖","公示催告","动产抵押","历史动产抵押","土地抵押","土地抵押详情","知识产权出质","股权出质","历史股权出质","质押明细","质押明细详情","重要股东质押","重要股东质押详情","质押比例","质押走势","商标信息","商标信息详情","历史商标信息","专利信息","软件著作权","作品著作权","网站备案","历史网站备案","人员控股企业","人员所有角色","人员所有公司","人员所有合作伙伴","企业人员简介","企业图谱","股权结构图","股权穿透图","最终受益人","实际控制权","疑似实际控制人","最短路径发现","新闻舆情","天眼地图","人员天眼风险","天眼风险详情","企业无水印logo","验证企业","企业经纬度","企业曾用名","纳税人识别号","按行业/区域查询公司","企业简介","工商信息","司法风险","知识产权","企业发展","历史信息","企业信用报告（基础版）","企业信用报告（专业版）","投资动态","融资历史","核心团队","企业业务","投资事件","竞品信息","获取标签","搜索项目品牌/投资机构","投资机构","私募基金","上市公司财务简析","股票行情","上市公司企业简介","高管信息","参股控股","上市公告","十大股东（十大流通股东）","发行相关","股本结构","股本变动","分红情况","配股情况","证券信息","重要人员","联系信息","主要指标（年度）","主要指标（季度）","对外担保","违规处理","利润表","资产负债表","现金流量表","招股书","工商追踪","相关新闻","机构成员","公开投资事件","未公开投资","管理基金","对外投资基金","基金管理","投资动态","统计分析","机构信息","会员信息","法律意见书信息","高管信息","高管情况","产品信息","诚信信息","建筑资质-不良行为","建筑资质-资质资格","建筑资质-资质资格详情","建筑资质-注册人员","建筑资质-注册人员详情","建筑资质-工程项目","建筑资质-工程项目详情","搜索"};
	
	private final static String url_pre = "https://open.tianyancha.com/cloud-open-admin/_feign/interface/";
	private final static String url_after = "/uni.json";
	private final static String cookies = "UM_distinctid=176d208b4ac440-0ece6466de0326-5c19341b-13c680-176d208b4ad6c2; cna=P+HKFVSb9G0CAXL332rXhAfc; xlly_s=1; guid=13bd-f2f4-5718-225c; CNZZDATA1255626299=1835711296-1609839820-https%253A%252F%252Fwww.amap.com%252F%7C1610354464; tfstk=cp6GIwaDT1R_iWRhNR91qmW5GG4eC_8tYFQV59WISq-puTDcbh50dXlP13PH-NXxG; l=eBLGmtmVQTcNqAdEKO5ahurza77tqhOXBsPzaNbMiIncI6o3TFFwVgcqMJ4X2Cil5HG5-tCeH_xrZWgVjRevJtadg7fZuHO1tTfS0xvO.; isg=BFtbqnZQgB5EZP2DStGfEK0f6r_FMG8yEw4Sbk2NfdpQLFYOrwBQgg7uxoyiDMcq";

	
	public static void main(String[] args) throws InterruptedException, IOException {
		TianYanChaColsInfo obj = new TianYanChaColsInfo();
		Connection conn = null;
		conn = MysqlHelper.getConnection(
				"jdbc:mysql://192.168.5.72:3311/cenmds?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true",
				"root", "mysql");
		Document doc = null;
		
		Random random = new Random();
		
		for(int i = 0; i<code.length; i++){
			Map<String, String> cookiem = Utils.convertCookie(cookies);
			
			doc = Jsoup.connect(url_pre + code[i] + url_after)
					.cookies(cookiem)
					.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
					.header("Accept-Encoding", "gzip, deflate, br")
					.header("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2")
					.header("Cache-Control", "no-cache")
					.header("Connection", "keep-alive")
					.header("Host", "open.tianyancha.com")
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
			System.out.println(url_pre + code[i] + url_after);
			if (doc == null) {
				System.out.println("访问地址" + url_pre + code[i] + url_after + "失败...");
			}
			Element body = doc.body();
	
			JSONObject json = JSONObject.parseObject(body.text());
			
			JSONObject dataJson = json.getJSONObject("data");
			
			String returnParam = dataJson.getString("returnParam");
			String api_url = dataJson.getString("openUrl");
			
			JSONObject returnParamJ = (JSONObject)JSONObject.parse(returnParam);
			
			String result = returnParamJ.getString("result");
			if(result==null){
				int k=1;
				w:while(true){
					String tempResult = returnParamJ.getString("result(entityType="+k+")");
					if(tempResult==null){
						break w;
					}
					obj.saveDb(tempResult,i,api_url);
					k++;
				}
			}else{
				obj.saveDb(result,i,api_url);
			}
			
			Thread.sleep(random.nextInt(10)+20);
		}
		
		MysqlHelper.close();

	}
	
	
	public void saveDb(String jsonstr, int i, String api_url) {
		System.out.println(i);
		String port_id = code[i];
		String table_comment = name[i];
		String table_name = "tyc_zhaozhenwei";
		
		JSONObject resultJson = (JSONObject)JSONObject.parse(jsonstr);
		String result = resultJson.getString("_");
		if(result==null){
			String en_field = "";
			String field_type = "";
			String field_length = "";
			String field_explain = "";
			en_field = resultJson.getString("name");
			field_type = resultJson.getString("type");
			field_length = resultJson.getString("notice");
			field_explain = resultJson.getString("remark");
			
			MysqlHelper.executeUpdate("insert into tyc_table_field values(null,?,?,?,?,?,null,?,?,?,null)", new String[]{table_name,table_comment,port_id,api_url,en_field,field_type,field_length,field_explain});
			System.out.println("插入成功...");
		}else{
			JSONObject colsJson = (JSONObject)JSONObject.parse(result);
			System.out.println(colsJson);
			for(String str: colsJson.keySet()){
				String en_field = "";
				String field_type = "";
				String field_length = "";
				String field_explain = "";
				JSONObject valueJson = (JSONObject)JSONObject.parse(colsJson.getString(str));
				en_field = valueJson.getString("name");
				field_type = valueJson.getString("type");
				field_length = valueJson.getString("notice");
				field_explain = valueJson.getString("remark");
				MysqlHelper.executeUpdate("insert into tyc_table_field values(null,?,?,?,?,?,null,?,?,?,null)", new String[]{table_name,table_comment,port_id,api_url,en_field,field_type,field_length,field_explain});
				System.out.println("插入成功...");
			}
		}
	}

}
