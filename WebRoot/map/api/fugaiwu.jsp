<%@page import="com.censoft.util.CoordinateTransform"%>
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
	CoordinateTransform ct = new CoordinateTransform();
	try {
		conn = dbclass.getConnection("com.mysql.cj.jdbc.Driver",
				"jdbc:mysql://192.168.5.72:3311/buliding_lab?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true", "root",
				"mysql");
		if (conn == null) {
			out.println(dbclass.getMsg());
			return;
		}

		qyList = dbclass.doQuery(conn, "select a.rglm_hb, a.cjjzwbm, b.pfhouseid, a.latitude, a.longitude, a.lat2, a.lng2, a.lat3, a.lng3 from s_ly_jiaoyan_20202959_2 a left join 2965_to_2907 b on a.cjjzwbm = b.cjjzwbm where a.cjjzwbm = '11010800100538' ");
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
<script type="text/javascript" src="../js/coordtransform.js"></script>
</head>
<body>
	<div id="allmap"></div>
	<script>
	
		var qyList = eval('<%=qyJson%>');
		var s1 = "116.2612517254878,39.898521259549398],[116.26136636656565,39.898520823378192],[116.26136626044558,39.898421736974456],[116.26109215967654,39.898422429452467],[116.26109257786129,39.898521452903651],[116.26120722073767,39.898521016732445],[116.26120724861664,39.898527186081651],[116.26125148177152,39.898527086256934],[116.2612517254878,39.898521259549398";
		var arrs = s1.split("],[");
		var polys = [];
		
		// 百度地图API功能
		var map = new BMapGL.Map("allmap", {
			minZoom : 9,
			maxZoom : 19
		});
		var point = new BMapGL.Point(116.403968, 39.915115);
		map.centerAndZoom(point, 10); // 初始化地图，设置中心点坐标和地图级别
		map.enableScrollWheelZoom(); // 允许滚轮缩放
	
		var points = [];
		$.each(arrs, function(j, obj) {
			var o = obj.split(",");
			var lnglat = wgs84togcj02tobd09(o[0], o[1])
			var p = new BMapGL.Point(lnglat.lng, lnglat.lat);
			points.push(p);
			
			if(j==0){
				var marker = new BMapGL.Marker(p);
				map.addOverlay(marker);
			}
		})
		var poly = addPolyline(points);
		polys.push({
			poly : poly
		});
		map.addOverlay(poly);
		
		
		// 创建信息窗口
		var opts = {
		    width: 200,
		    height: 100,
		    title: '信息title'
		};
		$.each(qyList, function(j, qyo) {
			var p1 = new BMapGL.Point(qyo.longitude, qyo.latitude);
			var p2 = new BMapGL.Point(qyo.lng2, qyo.lat2);
			var p3 = new BMapGL.Point(qyo.lng3, qyo.lat3);
			var marker1 = new BMapGL.Marker(p1);
			var marker2 = new BMapGL.Marker(p2);
			var marker3 = new BMapGL.Marker(p3);
			map.addOverlay(marker1);
			map.addOverlay(marker2);
			map.addOverlay(marker3);
			var infoWindow1 = new BMapGL.InfoWindow('longitude', opts);
			// 点标记添加点击事件
			marker1.addEventListener('click', function () {
			    map.openInfoWindow(infoWindow1, p1); // 开启信息窗口
			});
			var infoWindow2 = new BMapGL.InfoWindow('lng2', opts);
			// 点标记添加点击事件
			marker2.addEventListener('click', function () {
			    map.openInfoWindow(infoWindow2, p2); // 开启信息窗口
			});
			var infoWindow3 = new BMapGL.InfoWindow('lng3', opts);
			// 点标记添加点击事件
			marker3.addEventListener('click', function () {
			    map.openInfoWindow(infoWindow3, p3); // 开启信息窗口
			});
		})
		
		
		
		//画街道
		$.get('../json/hdjd.json', function(data) {
			$.each(data.features, function(i, obj) {
				var array = obj.geometry.coordinates;
				var type = obj.geometry.type;
				if (type == 'Polygon') {
					var points = [];
					$.each(array[0], function(j, o) {
						points.push(new BMapGL.Point(o[0], o[1]));
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
							points.push(new BMapGL.Point(o[0], o[1]));
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
	
		});
		
		
		
		
	
		function addPolyline(plPoints) {
			var poly = new BMapGL.Polygon(plPoints, {
				strokeColor : "red",
				strokeWeight : 1,
				strokeOpacity : 1
			});
			return poly;
		}
		
		function wgs84togcj02tobd09(lng,lat){
			var wgs84togcj02 = coordtransform.wgs84togcj02(lng, lat);
			var gcj02tobd09 = coordtransform.gcj02tobd09(wgs84togcj02[0], wgs84togcj02[1]);
			return {lng:gcj02tobd09[0], lat:gcj02tobd09[1]}
		}
		
	</script>

</body>
</html>
