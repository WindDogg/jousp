<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.alibaba.fastjson.JSONArray"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.censoft.common.base.Pub"%>
<%@page import="com.censoft.common.db.ConnectionFactory"%>
<%
	Connection conn = null;
	Pub pub = new Pub();
	ConnectionFactory dbclass = new ConnectionFactory();
	Vector qyList = null;
	try {
		conn = dbclass.getConnection("com.mysql.cj.jdbc.Driver",
				"jdbc:mysql://192.168.5.72:3311/cenmds?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true", "root",
				"mysql");
		if (conn == null) {
			out.println(dbclass.getMsg());
			return;
		}

		qyList = dbclass.doQuery(conn, "select id,latitude,longitude from b_base_louyu_202001to09 ");
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
		var polys = [];
		// 百度地图API功能
	
		var map = new BMap.Map("allmap", {
			minZoom : 9,
			maxZoom : 19
		});
		var point = new BMap.Point(116.403968, 39.915115);
		map.centerAndZoom(point, 10); // 初始化地图，设置中心点坐标和地图级别
		map.enableScrollWheelZoom(); // 允许滚轮缩放
	
	
		$.get('../json/wldq.json', function(data) {
			$.each(data.features, function(i, obj) {
				var array = obj.geometry.coordinates;
				var type = obj.geometry.type;
				if (type == 'Polygon') {
					var points = [];
					$.each(array[0], function(j, o) {
						points.push(new BMap.Point(o[0], o[1]));
					})
					var poly = addPolyline(points);
					polys.push({
						poly : poly,
						jd : obj.properties.name
					});
					map.addOverlay(poly);
				} else if (type == 'MultiPolygon') {
					$.each(array, function(j, ob) {
						var points = [];
						$.each(ob[0], function(k, o) {
							points.push(new BMap.Point(o[0], o[1]));
						})
						var poly = addPolyline(points);
						polys.push({
							poly : poly,
							jd : obj.properties.name
						});
						map.addOverlay(poly);
					})
				} else {
					console.log(obj.properties.name);
				}
			})
	
			$.each(polys, function(i, obj) {
				var ids = [];
				$.each(qyList, function(j, qyo) {
					var p = new BMap.Point(qyo.longitude, qyo.latitude);
					if (BMapLib.GeoUtils.isPointInPolygon(p, obj.poly)) {
						ids.push(qyo.id);
					}
				})
	
				var id = ids.join(',');
	
				$.ajax({
					url : 'insert_wldq_ok.jsp',
					type : 'POST',
					datatype : 'json',
					async : true,
					data : {
						jd : obj.jd,
						ids : id
					},
					success : function(rdata) {
						console.log(rdata);
					}
				})
	
			})
		});
	
	
	
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
