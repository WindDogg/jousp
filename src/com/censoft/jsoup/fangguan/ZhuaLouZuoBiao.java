package com.censoft.jsoup.fangguan;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class ZhuaLouZuoBiao {

	private Connection conn = null;
	private final static String url_pre = "http://123.56.210.122:6080/arcgis/rest/services/T_FWXZ/MapServer/0/query?f=json&where=PFHOUSEID%20like%20%27";
	private final static String url_after = "%27&returnGeometry=true&spatialRel=esriSpatialRelIntersects&outFields=*&outSR=4326";
	
	/**
     * 多线程处理list
     *
     * @param data  数据list
     * @param threadNum  线程数
     */
    public synchronized void handleList(List<Map<String, Object>> data, int threadNum) {
        int length = data.size();
        int tl = length % threadNum == 0 ? length / threadNum : (length
                / threadNum + 1);

        for (int i = 0; i < threadNum; i++) {
            int end = (i + 1) * tl;
            HandleThread thread = new HandleThread("线程[" + (i + 1) + "] ",  data, i * tl, end > length ? length : end);
            thread.start();
        }
    }
    
	public void go() {
        try {
        	conn = MysqlHelper.getConnection("jdbc:mysql://192.168.5.72:3311/buliding_lab?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true","root","mysql");
        	List<Map<String, Object>> list1 = MysqlHelper.executeQuery("select a.cjjzwbm, b.pfhouseid from s_ly_jiaoyan_20202959_2 a left join (select cjjzwbm,GROUP_CONCAT(pfhouseid) pfhouseid from 2965_to_2907 group by cjjzwbm) b on a.cjjzwbm = b.cjjzwbm where b.cjjzwbm is not null ");//and a.lat3 is null
        	handleList(list1,10);

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
        	
            List<Map<String, Object>> subList = data.subList(start, end)/*.add("^&*")*/;
            
            for(Map<String, Object> map1: subList){
        		String cjjzwbm = (String)map1.get("cjjzwbm");
        		String pfhouseid = (String)map1.get("pfhouseid");
        		Document doc = null;
				try {
					doc = Jsoup.connect(url_pre+pfhouseid+url_after).get();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(doc==null){
					System.out.println("访问地址"+url_pre+pfhouseid+url_after+"失败...");
				}
        		Element body = doc.body();
        		if(body.text().length()>1000000){
        			System.out.println("房管id="+pfhouseid+"空间数据异常...");
        			continue;
        		}
        		JSONObject json = JSONObject.parseObject(body.text());
        		JSONArray jsonArray = json.getJSONArray("features");
        		if(jsonArray.isEmpty()){
        			System.out.println("房管id="+pfhouseid+"无空间数据...");
        			continue;
        		}
        		List<String[]> lists = new ArrayList<String[]>();
        		for(int i=0;i<jsonArray.size();i++){
        			JSONObject jo = jsonArray.getJSONObject(i);
        			JSONObject j2 = jo.getJSONObject("geometry");
        			JSONArray oss = j2.getJSONArray("rings");
        			JSONArray os = oss.getJSONArray(0);
        			for(int j=0;j<os.size();j++){
        				String js = os.getString(j);
        				js = js.substring(1, js.length()-1);
        				String strs[] = js.split(",");
        				lists.add(strs);
        			}
        		}
        		
        		Map<String, Double> centerPoint = Utils.getCenterPoint(lists);
        		double[] d = CoordinateTransform.transformWGS84ToBD09(centerPoint.get("lng"), centerPoint.get("lat"));
        		String lng = Utils.convertDoubleToString(d[0]);
        		String lat = Utils.convertDoubleToString(d[1]);
        		
        		MysqlHelper.executeUpdate("update s_ly_jiaoyan_20202959_2 set lat3='"+lat+"', lng3='"+lng+"' where cjjzwbm = '"+cjjzwbm+"'");
        	}
            
            
			System.out.println(threadName+"处理了"+subList.size()+"条！");
        }

    }
	
	public static void main(String[] args) {
		ZhuaLouZuoBiao zlzb = new ZhuaLouZuoBiao();
		zlzb.go();
	}
}
