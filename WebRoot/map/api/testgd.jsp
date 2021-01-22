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
		conn = dbclass.getConnection("com.mysql.cj.jdbc.Driver", "jdbc:mysql://192.168.5.72:3311/cenmds?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true", "root", "mysql");
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
		//var point = new BMapGL.Point(116.403968, 39.915115);
		var zb = bd_encrypt(116.350876,39.986439);
		var point = new BMapGL.Point(zb.bd_lng,zb.bd_lat);
		map.centerAndZoom(point, 15); // 初始化地图，设置中心点坐标和地图级别
		map.enableScrollWheelZoom(); // 允许滚轮缩放
	

		// 创建信息窗口
		var opts = {
		    width: 200,
		    height: 100,
		    title: '信息title'
		};
	
		var strs = '116.350876,39.986439;116.351053,39.986426;116.351248,39.986432;116.351281,39.986433;116.352507,39.986462;116.352847,39.986467;116.352916,39.986466;116.352996,39.98644;116.353089,39.986405;116.353118,39.986377;116.35315,39.986322;116.353164,39.986272;116.353233,39.984865;116.35328,39.984051;116.353337,39.98312;116.353398,39.982506;116.353395,39.982353;116.353549,39.98117;116.35358,39.980485;116.353628,39.979067;116.353443,39.979059;116.353449,39.978971;116.35346,39.978762;116.353462,39.978551;116.353435,39.978518;116.353403,39.978503;116.352222,39.978466;116.352232,39.978232;116.352245,39.978;116.352253,39.977877;116.352275,39.977858;116.352317,39.977847;116.352771,39.977862;116.352794,39.977859;116.352807,39.977848;116.352807,39.977836;116.352802,39.977601;116.35279,39.977591;116.352762,39.977585;116.352692,39.977577;116.352669,39.977569;116.352656,39.977558;116.352644,39.97753;116.352693,39.976772;116.352682,39.976746;116.352664,39.976736;116.352638,39.976731;116.350567,39.976714;116.350541,39.976717;116.350525,39.976727;116.350501,39.977227;116.3505,39.977242;116.350489,39.977255;116.35047,39.977258;116.350342,39.977255;116.347931,39.977158;116.347292,39.977134;116.346199,39.977107;116.346194,39.977419;116.346194,39.977451;116.34609,39.977543;116.346051,39.977618;116.34603,39.977908;116.346018,39.977932;116.345994,39.977943;116.345863,39.977986;116.345547,39.978;116.345477,39.977967;116.345484,39.977844;116.34496,39.977823;116.34487,39.977809;116.344805,39.977794;116.344743,39.977776;116.344688,39.977759;116.344632,39.977731;116.344583,39.977703;116.344508,39.977632;116.344441,39.977562;116.344405,39.977461;116.344413,39.977254;116.34437,39.977215;116.344294,39.977192;116.344136,39.97719;116.344138,39.977158;116.34416,39.976693;116.344162,39.97668;116.344156,39.976664;116.344137,39.976655;116.343469,39.976639;116.343444,39.976641;116.343419,39.976646;116.343333,39.976697;116.342884,39.97699;116.342855,39.97701;116.342636,39.977164;116.342014,39.977661;116.341967,39.977705;116.341898,39.977793;116.341821,39.977896;116.34176,39.978026;116.34175,39.978055;116.341635,39.978487;116.341504,39.978804;116.341423,39.978975;116.341355,39.979181;116.341321,39.979425;116.341224,39.980143;116.341106,39.980799;116.341073,39.981035;116.340982,39.981664;116.340888,39.982364;116.340829,39.982914;116.340836,39.983197;116.340839,39.983389;116.340786,39.984599;116.340755,39.985123;116.340759,39.985143;116.340764,39.985162;116.3408,39.985209;116.340809,39.985235;116.34079,39.985867;116.340792,39.985903;116.340805,39.985951;116.340831,39.98597;116.340869,39.98598;116.343427,39.986109;116.343924,39.986146;116.344082,39.986162;116.344771,39.986221;116.346413,39.986303;116.347763,39.98636;116.350651,39.986479;116.350728,39.986481;116.350798,39.986457;116.350876,39.986439';

		var array = strs.split(';');
		
		
		var points = [];
		$.each(array, function(j, obj) {
			var ob = obj.split(',');
			var zbs = bd_encrypt(ob[0],ob[1]);
			var p = new BMapGL.Point(zbs.bd_lng, zbs.bd_lat);
			//var marker0 = new BMapGL.Marker(p);
			//map.addOverlay(marker0);
			points.push(p);
		})
		var poly = addPolyline(points);
		map.addOverlay(poly);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	
		function addPolyline(plPoints) {
			var poly = new BMapGL.Polygon(plPoints, {
				strokeColor : "red",
				strokeWeight : 1,
				strokeOpacity : 1
			});
			return poly;
		}
		
		
		
		//高德坐标转百度（传入经度、纬度）
		function bd_encrypt(gg_lng, gg_lat) {
		    var X_PI = Math.PI * 3000.0 / 180.0;
		    var x = gg_lng, y = gg_lat;
		    var z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * X_PI);
		    var theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * X_PI);
		    var bd_lng = z * Math.cos(theta) + 0.0065;
		    var bd_lat = z * Math.sin(theta) + 0.006;
		    return {
		        bd_lat: bd_lat,
		        bd_lng: bd_lng
		    };
		}
		
	</script>

</body>
</html>
