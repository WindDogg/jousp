package com.censoft.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import java.sql.*;

public class MysqlHelper {
	// 定义变量
	private static Connection ct = null;
	// 大多数情况下用preparedstatement替代statement
	private static PreparedStatement ps = null;
	private static ResultSet rs = null;

	// 连接数据库的参数
	private static String url = null;
	private static String username = null;
	private static String driver = null;
	private static String passwd = null;

	private static CallableStatement cs = null;

	public static CallableStatement getCs() {
		return cs;
	}

	private static Properties pp = null;
	private static InputStream fis = null;

	// 加载驱动，只需要一次，用静态代码块
	static {
		try {
			// 从dbinfo.properties
			pp = new Properties();
			fis = MysqlHelper.class.getClassLoader().getResourceAsStream("config.properties");
			pp.load(fis);
			url = pp.getProperty("url");
			driver = pp.getProperty("driver");
			username = pp.getProperty("username");
			passwd = pp.getProperty("password");

			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			fis = null;// 垃圾回收站上收拾
		}

	}

	// 得到连接
	public static Connection getConnection() {
		try {
			ct = DriverManager.getConnection(url, username, passwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ct;
	}
	// 得到连接
	public static Connection getConnection(String url2, String username2, String passwd2) {
		try {
			ct = DriverManager.getConnection(url2, username2, passwd2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ct;
	}

	// *************callPro1存储过程函数1*************
	public static CallableStatement callPro1(String sql, String[] parameters) {
		try {
			ct = getConnection();
			cs = ct.prepareCall(sql);
			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					cs.setObject(i + 1, parameters[i]);
				}
			}
			cs.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally {
			close();
		}
		return cs;
	}

	// *******************callpro2存储过程2************************
	public static CallableStatement callPro2(String sql, String[] inparameters, Integer[] outparameters) {
		try {
			ct = getConnection();
			cs = ct.prepareCall(sql);
			if (inparameters != null) {
				for (int i = 0; i < inparameters.length; i++) {
					cs.setObject(i + 1, inparameters[i]);
				}
			}
			// cs.registerOutparameter(2,oracle.jdbc.OracleTypes.CURSOR);
			if (outparameters != null) {
				for (int i = 0; i < outparameters.length; i++) {
					cs.registerOutParameter(inparameters.length + 1 + i, outparameters[i]);
				}
			}
			cs.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally {

		}
		return cs;
	}

	/**
	 *
	 * @param sql
	 * @param parameters
	 * @return
	 * @Description
	 * @return ResultSet
	 */
	public static List<Map<String, Object>> executeQuery(String sql) {
		return executeQuery(sql, null);
	}
	
	public static List<Map<String, Object>> executeQuery(String sql, String[] parameters) {
		ArrayList<Map<String, Object>> list = null;
		boolean b = false;
		try {
			if(ct == null){
				b = true;
				ct = getConnection();
			}
			
			ps = ct.prepareStatement(sql);
			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					ps.setString(i + 1, parameters[i]);
				}
			}
			rs = ps.executeQuery();

			// 得到结果集（rs）的结构
			ResultSetMetaData rsmd = rs.getMetaData();

			List<String> colNameList = new ArrayList<String>();
			// if(rs.getRow()!=0)
			list = new ArrayList<Map<String, Object>>();

			// 通过rsmd可以得到该结果集有多少列
			int columnNum = rsmd.getColumnCount();

			for (int i = 0; i < columnNum; i++) {
				//String col_name = rsmd.getColumnName(i + 1); // 获取第 i列的字段名称
				//String col_Tpye = rsmd.getColumnTypeName(i + 1);// 类型
				colNameList.add(rsmd.getColumnName(i + 1));
			}

			// 从rs中取出数据，并且封装到ArrayList中
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < columnNum; i++) {
					String key = colNameList.get(i);
					Object value = StringUtils.trimToEmpty(rs.getString(colNameList.get(i)));
					map.put(key, value);
				}

				list.add(map);
			}
			return list;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally {
			if(b){
				close();
			}
		}
	}

	public static Connection getCt() {
		return ct;
	}

	public static PreparedStatement getPs() {
		return ps;
	}

	public static ResultSet getRs() {
		return rs;
	}

	public static void executeUpdate2(String[] sql, String[][] parameters) {
		try {
			ct = getConnection();
			ct.setAutoCommit(false);

			for (int i = 0; i < sql.length; i++) {

				if (null != parameters[i]) {
					ps = ct.prepareStatement(sql[i]);
					for (int j = 0; j < parameters[i].length; j++) {
						ps.setString(j + 1, parameters[i][j]);
					}
					ps.executeUpdate();
				}

			}

			ct.commit();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				ct.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new RuntimeException(e.getMessage());
		} finally {
			close();
		}

	}

	// 先写一个update、delete、insert
	// sql格式：update 表名 set 字段名 =？where 字段=？
	// parameter神应该是（”abc“,23）
	public static void executeUpdate(String sql) {
		executeUpdate(sql, null);
	}
	
	public static void executeUpdate(String sql, String[] parameters) {
		boolean b = false;
		try {
			if(ct == null){
				b = true;
				ct = getConnection();
			}
			ps = ct.prepareStatement(sql);
			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					ps.setString(i + 1, parameters[i]);
				}
				
			}
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();// 开发阶段
			// 抛出异常
			// 可以处理，也可以不处理
			throw new RuntimeException(e.getMessage());
		} finally {
			if(b){
				close();
			}
		}
	}

	public static void close() {
		// 关闭资源(先开后关)
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			rs = null;
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ps = null;
		}
		if (cs != null) {
			try {
				cs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			cs = null;
		}
		if (null != ct) {
			try {
				ct.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ct = null;
		}
	}

	public static List<Object> resultSetToList(ResultSet rs) throws java.sql.SQLException {
		if (rs == null)
			return Collections.emptyList();
		ResultSet md = (ResultSet) rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
		int columnCount = ((ResultSetMetaData) md).getColumnCount(); // 返回此
																		// ResultSet
																		// 对象中的列数
		List<Object> list = new ArrayList<Object>();
		Map<Object, Object> rowData = new HashMap<Object, Object>();
		while (rs.next()) {
			rowData = new HashMap<Object, Object>(columnCount);
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(((ResultSetMetaData) md).getColumnName(i), rs.getObject(i));
			}
			list.add(rowData);
		}
		return list;
	}
}