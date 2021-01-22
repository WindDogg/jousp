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

		qyList = dbclass.doQuery(conn, "select (1+1) ");
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
<script src="//api.map.baidu.com/api?type=webgl&v=1.0&ak=sSelQoVi2L3KofLo1HOobonW"></script>
<script type="text/javascript" src="../js/GeoUtils.js"></script>
<script type="text/javascript" src="../../js/jquery.js"></script>
</head>
<body>
	<div id="allmap"></div>
	<script>
	
		var qyList = eval('<%=qyJson%>');
		var polys = [];
		// 百度地图API功能
	
		var map = new BMapGL.Map("allmap", {
			minZoom : 9,
			maxZoom : 19
		});
		var point = new BMapGL.Point(116.403968, 39.915115);
		map.centerAndZoom(point, 10); // 初始化地图，设置中心点坐标和地图级别
		map.enableScrollWheelZoom(); // 允许滚轮缩放
	
		// 创建信息窗口
		var opts = {
		    width: 200,
		    height: 100,
		    title: '信息title'
		};
		$.get('../json/hdjd_new_baidu.json', function(data) {
			$.each(data.features, function(i, obj) {
				var array = obj.geometry.coordinates;
				var type = obj.geometry.type;
				var p1 = new BMapGL.Point(obj.properties.X, obj.properties.Y);
				var marker1 = new BMapGL.Marker(p1);
				map.addOverlay(marker1);
				var infoWindow1 = new BMapGL.InfoWindow(obj.properties.gridname, opts);
				// 点标记添加点击事件
				marker1.addEventListener('click', function () {
				    map.openInfoWindow(infoWindow1, p1); // 开启信息窗口
				});

				$.each(array, function(j, ob) {
					var points = [];
					$.each(ob, function(k, o) {
						points.push(new BMapGL.Point(o[0], o[1]));
					})
					var poly = addPolyline(points);
					polys.push({
						poly : poly,
						jd : obj.properties.gridname
					});
					map.addOverlay(poly);
				})
				
			})
	
		});
	
	
		function addPolyline(plPoints) {
			var poly = new BMapGL.Polygon(plPoints, {
				strokeColor : "red",
				strokeWeight : 1,
				strokeOpacity : 1
			});
			return poly;
		}
	</script>

</body>
</html>
