<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.alibaba.fastjson.JSONArray"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.censoft.common.base.Pub"%>
<%@page import="com.censoft.common.db.ConnectionFactory"%>
<%

	Connection conn = null; 
	Pub pub = new Pub();
	
	String ids = pub.trimNull((String)request.getParameter("ids"));
	String jd = pub.trimNull((String)request.getParameter("jd"));
	
	//System.out.println(ids);
	//System.out.println(jd);
	
	ConnectionFactory dbclass = new ConnectionFactory();
	try{
		conn = dbclass.getConnection("com.mysql.jdbc.Driver", "jdbc:mysql://192.168.4.73:3311/lyjj_0508?useUnicode=true&characterEncoding=utf8", "root", "mysql");
		
		if(!"".equals(ids)){
			boolean bl = dbclass.executeUpdate(conn, "update lyjj_zxw_pro set jd = '"+jd+"' where id in ("+ids+")");
			
		    if(bl){
		    	System.out.println(jd+"======成功"+ids);
		    }else{
		    	System.out.println(dbclass.getMsg());
		    }
		}
			
		out.println("ok");

	} catch (Exception ex) {
		System.out.println("err" + ex);
	} finally {
		try {
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

%>