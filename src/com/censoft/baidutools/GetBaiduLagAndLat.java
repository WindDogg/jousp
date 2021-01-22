package com.censoft.baidutools;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import com.censoft.common.base.Pub;
import com.censoft.common.db.ConnectionFactory;

import java.sql.PreparedStatement;

public class GetBaiduLagAndLat {
	static ConnectionFactory dbclass = new ConnectionFactory();
	static double x_pi = 3.14159265358979324 * 3000.0 / 180.0; 

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Connection conn = null;
		Pub pub = new Pub();
		try {
			conn = dbclass.getConnection("com.mysql.jdbc.Driver","jdbc:mysql://192.168.3.12:3311/buliding_lab","root","mysql");
			conn.setAutoCommit(false);
			
			Vector temV = dbclass.doQuery(conn, " select * from s_ly_2475_jiaoyan");
			if(temV == null){
				System.out.println(dbclass.getMsg());
				return;
			}
			if(!temV.isEmpty()){
				for (int i = 0; i < temV.size(); i++) {
					Hashtable ht = (Hashtable) temV.get(i);
					String id = pub.trimNull((String) ht.get("cjjzwbm"));
					String dom = pub.trimNull((String) ht.get("dom"));
					GetLatAndLngByBaidu getLatAndLngByBaidu = new GetLatAndLngByBaidu();
					Object[] o = getLatAndLngByBaidu.getCoordinate(dom);// 使用百度
					if (o != null) {
						String lng = pub.trimNull((String) o[0]);
						String lat = pub.trimNull((String) o[1]);// longitude\latitude
						boolean bl = dbclass.executeUpdate(conn, "update s_ly_2475_jiaoyan set longitude='" + lng
								+ "',latitude='" + lat + "' where cjjzwbm ='" + id + "'");
						if (!bl) {
							System.out.println(" 处理错误！" + dbclass.getMsg());
						} else {
							System.out.println("处理成功"+i);
						}
					}

				}
			}
			
			conn.commit();
			// conn.setAutoCommit(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	
	public static Map<String,Float> bdEncrypt(double gg_lat, double gg_lon){
	             Map<String,Float> data = new HashMap<String,Float>();
	             double x = gg_lon, y = gg_lat;
	             double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
	             double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
	            double bd_lon = z * Math.cos(theta) + 0.0065;
	             double bd_lat = z * Math.sin(theta) + 0.006;
	            System.out.println(bd_lon+","+bd_lat);
	            System.out.println(new BigDecimal(String.valueOf(bd_lon)).floatValue()+","+new BigDecimal(String.valueOf(bd_lat)).floatValue());
	            data.put("lon", new BigDecimal(String.valueOf(bd_lon)).floatValue());
	          data.put("lat",new BigDecimal(String.valueOf(bd_lat)).floatValue());
	          return data;
	         }


}
