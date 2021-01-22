<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.alibaba.fastjson.JSONArray"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.censoft.common.base.Pub"%>
<%@page import="com.censoft.common.db.ConnectionFactory"%>
<%
	Connection conn = null;
	Pub pub = new Pub();
	ConnectionFactory dbclass = new ConnectionFactory();
	Vector lyList = null;
	Vector qyList = null;
	try {
		conn = dbclass.getConnection("com.mysql.cj.jdbc.Driver",
				"jdbc:mysql://192.168.5.72:3311/buliding_lab?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true", "root",
				"mysql");
		if (conn == null) {
			out.println(dbclass.getMsg());
			return;
		}

		lyList = dbclass.doQuery(conn, " select cjjzwbm,rglm_hb,lng,lat from s_ly_jiaoyan_all_chuli ");
		qyList = dbclass.doQuery(conn, " select code,lnglat from cenmds.hdq_area_lnglat_school_hospital ");
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

	String lyJson = JSONArray.toJSONString(lyList);
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
<script type="text/javascript" src="../js/GeoUtilsGL.js"></script>
<script type="text/javascript" src="../../js/jquery.js"></script>
</head>
<body>
	<div id="allmap"></div>
	<script>
	
		var qyList = eval('<%=qyJson%>');
		var lyList = eval('<%=lyJson%>');
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
		
		
		$.each(qyList, function(i, data) {
			var code = data.code;
			var lnglat = data.lnglat;
			var array =  eval('(' + lnglat + ')');
			var points = [];
			$.each(array[0], function(j, obj) {
				points.push(new BMapGL.Point(obj[0], obj[1]));
			})
			var poly = addPolyline(points);
			map.addOverlay(poly);
			
			var ids = [];
			$.each(lyList, function(j, lyo) {
				var p = new BMapGL.Point(lyo.lng, lyo.lat);
				if (BMapLib.GeoUtils.isPointInPolygon(p, poly)) {
					ids.push(lyo.cjjzwbm);
					var marker1 = new BMapGL.Marker(p);
					map.addOverlay(marker1);
					var infoWindow1 = new BMapGL.InfoWindow(lyo.rglm_hb, opts);
					// 点标记添加点击事件
					marker1.addEventListener('click', function () {
					    map.openInfoWindow(infoWindow1, p); // 开启信息窗口
					});
				}
			})

			var id = ids.join('\',\'');
			
			//console.log(id)
			/*
			$.ajax({
				url : 'insert_hdjd_xuexiaoandyiyuan_ok.jsp',
				type : 'POST',
				datatype : 'json',
				async : true,
				data : {
					jd : code,
					ids : id
				},
				success : function(rdata) {
					console.log(rdata);
				}
			})
			*/
			
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
