<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.alibaba.*"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.censoft.common.base.Pub"%>
<%@page import="com.censoft.common.db.ConnectionFactory"%>
<%
	Connection conn = null;
	Pub pub = new Pub();
	ConnectionFactory dbclass = new ConnectionFactory();
	Vector qyList = null;
	Vector qyList_new = null;
	try {
		conn = dbclass.getConnection("com.mysql.jdbc.Driver",
				"jdbc:mysql://192.168.4.73:3311/lyjj_0508?useUnicode=true&characterEncoding=utf8", "root",
				"mysql");
		if (conn == null) {
			out.println(dbclass.getMsg());
			return;
		}

		qyList = dbclass.doQuery(conn, "select id,latitude,longitude from b_base_louyu_201801to12 where latitude!='' and longitude!='' ");
		
		qyList_new = dbclass.doQuery(conn, "select qy_name,qk, GROUP_CONCAT(CONCAT('[',bd_longitude,',',bd_latitude,']')) a from new_quyulnglat GROUP BY qy_name,qk");
		if (qyList == null) {
			out.println(dbclass.getMsg());
		}
	} catch (Exception ex) {
		System.out.println("err" + ex);
	} finally {
		try {
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	String qyJson = JSONArray.toJSONString(qyList);
	String qylist_new = JSONArray.toJSONString(qyList_new);
	System.out.println(qyJson);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>北京市</title>
<style type="text/css">
body, html, #allmap {
	width: 100%;
	height: 100%;
	overflow: hidden;
	margin: 0;
	font-family: "微软雅黑";
	position: absolute;
}

.page-content-wrapper .page-content {
	padding: 0px;
}

label {
	min-width: 100px;
}
</style>
<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=2.0&ak=sSelQoVi2L3KofLo1HOobonW"></script>
<script type="text/javascript" src="../js/GeoUtils.js"></script>
<script type="text/javascript" src="../../js/jquery.js"></script>
</head>
<body>
	<div id="allmap"></div>
	<script>
	
		var qyList = eval('<%=qyJson%>');
		var qylist_new = eval('<%=qylist_new%>');
		var polys = [];
		// 百度地图API功能
	
		var map = new BMap.Map("allmap", {
			minZoom : 9,
			maxZoom : 19
		});
		var point = new BMap.Point(116.403968, 39.915115);
		map.centerAndZoom(point, 10); // 初始化地图，设置中心点坐标和地图级别
		map.enableScrollWheelZoom(); // 允许滚轮缩放
	
	    var points = [];
		$.each(qylist_new, function(k, o) {
		console.log(o.a)
		//$.each(o.a, function(k, obj) {
		
		//})
			//points.push(new BMap.Point(o[0], o[1]));
		})
		var poly = addPolyline(points);
			
	
	
		function addPolyline(plPoints) {
			var poly = new BMap.Polygon(plPoints, {
				strokeColor : "red",
				strokeWeight : 1,
				strokeOpacity : 1
			});
			return poly;
		}
	</script>

</body>
</html>
