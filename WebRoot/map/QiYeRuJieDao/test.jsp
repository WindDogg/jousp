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
<script type="text/javascript" src="../js/coordtransform.js"></script>
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
	

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		var p2 = new BMapGL.Point('116.32200847923357', '40.026184468641226');
		var marker2 = new BMapGL.Marker(p2);
		map.addOverlay(marker2);
		
		
		var points2 = [];
		var l2 = '116.30938231993912,40.019027129399433],[116.30939521441871,40.018932289594375],[116.30958441738801,40.018947107723761],[116.30959994688112,40.01883278680458],[116.30941061530871,40.018818148539651],[116.3090162931685,40.018789484448064],[116.3089736706994,40.01913136532113],[116.30917546327805,40.019146205933566],[116.30936506824435,40.019159636408972],[116.30955896927208,40.019174577745446],[116.3095763153957,40.019040568868036],[116.30938231993912,40.019027129399433';
		
		var arrs = l2.split('],[');
		for(var i=0;i<arrs.length;i++){
			var arr = arrs[i].split(',');
			var lnglat = wgs84togcj02tobd09(arr[0], arr[1]);
			points2.push(new BMapGL.Point(lnglat.lng, lnglat.lat));
		}
		var poly2 = addPolyline(points2);
		map.addOverlay(poly2);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	
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
