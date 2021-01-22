package com.censoft.baidutools;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import com.censoft.common.base.Pub;
import com.censoft.common.db.ConnectionFactory;


public class GetBaiduLagAndLat2 {
	static ConnectionFactory dbclass = new ConnectionFactory();
	
	/**
     * 多线程处理list
     *
     * @param data  数据list
     * @param threadNum  线程数
     */
    public synchronized void handleList(List<Map<String, String>> data, int threadNum) {
        int length = data.size();
        int tl = length % threadNum == 0 ? length / threadNum : (length
                / threadNum + 1);

        for (int i = 0; i < threadNum; i++) {
            int end = (i + 1) * tl;
            HandleThread thread = new HandleThread("线程[" + (i + 1) + "] ",  data, i * tl, end > length ? length : end);
            thread.start();
        }
    }
    
    class HandleThread extends Thread {
        private String threadName;
        private List<Map<String, String>> data;
        private int start;
        private int end;

        public HandleThread(String threadName, List<Map<String, String>> data, int start, int end) {
            this.threadName = threadName;
            this.data = data;
            this.start = start;
            this.end = end;
        }

        public void run() {
            List<Map<String, String>> subList = data.subList(start, end)/*.add("^&*")*/;
            Pub pub = new Pub();
            Connection conn = null;
            try {
            	conn = dbclass.getConnection("com.mysql.cj.jdbc.Driver", "jdbc:mysql://192.168.5.72:3311/cenmds?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true", "root", "mysql");

    			GetLatAndLngByBaidu getLatAndLngByBaidu = new GetLatAndLngByBaidu();
    			
    			for (Map<String, String> map : subList) {
				System.out.println(map.get("dom"));	
    				Object[] o = getLatAndLngByBaidu.getCoordinate(map.get("dom"));// 使用百度
    				if (o != null) {
    					String lng = pub.trimNull((String) o[0]);
    					String lat = pub.trimNull((String) o[1]);// longitude\latitude
    					boolean bl = dbclass.executeUpdate(conn, "update fwxz_all_1118 set lng='" + lng + "',lat='" + lat + "' where id ='" + map.get("id") + "'");
    					if (!bl) {
    						System.out.println(" 处理错误！" + dbclass.getMsg());
    					} else {
//    						System.out.println("处理成功" + map.get("id"));
    					}
    				}
				}
            } catch (Exception ex) {
    			ex.printStackTrace();
    		} finally {
    			try {
    				conn.close();
    			} catch (SQLException e) {
    				e.printStackTrace();
    			}
    		}
			System.out.println(threadName+"处理了"+subList.size()+"条！");
        }

    }
    
    public static void main(String[] args) {
    	Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("现在的时间是：" + sf.format(date));
    	GetBaiduLagAndLat2 test = new GetBaiduLagAndLat2();
    	List<Map<String,String>> data = new ArrayList<Map<String,String>>();
    	Connection conn = null;
    	Pub pub = new Pub();
		try {
			conn = dbclass.getConnection("com.mysql.cj.jdbc.Driver", "jdbc:mysql://192.168.5.72:3311/cenmds?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true", "root", "mysql");
		/*Vector temV = dbclass.doQuery(conn, " select '00004A2012B744708847EEAD0D51158A' id, '建材城西路32号' dom ");*/
		       Vector temV = dbclass.doQuery(conn, " select  id,  dom from fwxz_all_1118 where dom != '' and dom is not null and lng = '' ");
			if(temV == null){
				System.out.println(dbclass.getMsg());
				return;
			}
			if(!temV.isEmpty()){
				for (int i = 0; i < temV.size(); i++) {
					Hashtable ht = (Hashtable) temV.get(i);
					String id = pub.trimNull((String) ht.get("id"));
					String dom = pub.trimNull((String) ht.get("jzdz"));
					Map<String,String> map = new HashMap<String, String>();
					map.put("id", id);
					map.put("dom", dom);
					data.add(map);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
        test.handleList(data, 10);
    }


}
