package com.censoft.baidutools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import com.censoft.common.base.Pub;
import com.censoft.common.db.ConnectionFactory;

import java.sql.PreparedStatement;

public class NewGetBaiduLagAndLat {
	static ConnectionFactory dbclass = new ConnectionFactory();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Connection conn = null;
		Pub pub = new Pub();
		try {
			conn = dbclass.getConnection("com.mysql.jdbc.Driver","jdbc:mysql://192.168.5.72:3311/buliding_lab","root","mysql");
			conn.setAutoCommit(false);
			
			Vector temV = dbclass.doQuery(conn, " select * from s_ly_jiaoyan_20202959_2 where dom!='' and lng2='' ");
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
						boolean bl = dbclass.executeUpdate(conn, "update s_ly_jiaoyan_20202959_2 set lng2='" + lng
								+ "',lat2='" + lat + "' where cjjzwbm ='" + id + "'");
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


}
