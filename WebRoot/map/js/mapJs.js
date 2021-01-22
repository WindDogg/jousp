	/*
    if(!isSupportCanvas()){
    	alert('热力图目前只支持有canvas支持的浏览器,您所使用的浏览器不能使用热力图功能~')
    }
    
    if (!document.createElement('canvas').getContext) {  // 判断当前浏览器是否支持绘制海量点
        alert('请在chrome、safari、IE8+以上浏览器查看本示例');
    }
    */
	
	function getBoundary(cityname){
		var bdary = new BMap.Boundary();
		bdary.get(cityname, function(rs){       //获取行政区域
			//map.clearOverlays();        		//清除地图覆盖物
			var count = rs.boundaries.length; //行政区域的点有多少个
          	//alert(count);
			if (count === 0) {
				//alert('未能获取当前输入行政区域');
				return;
			}
          	var pointArray = [];
			for (var i = 0; i < count; i++) {
				var ply;
				if(cityname=='北京市'){
					ply = new BMap.Polygon(rs.boundaries[i], {strokeWeight: 4, strokeColor: "#ff0000", fillOpacity: 0.000001,strokeOpacity: 1}); //建立多边形覆盖物
				}else{
					ply = new BMap.Polygon(rs.boundaries[i], {strokeWeight: 2, strokeColor: "#ff0000", fillOpacity: 0.2,strokeOpacity: 0.2}); //建立多边形覆盖物
				}
				map.addOverlay(ply);  //添加覆盖物
				pointArray = pointArray.concat(ply.getPath());
			}    
			//map.setViewport(pointArray);    //调整视野
			//addlabel(); 
		});   
	}

	function addlabel() {
	    var pointArray = [
			new BMap.Point(121.716076,23.703799),
			new BMap.Point(112.121885,14.570616),
			new BMap.Point(123.776573,25.695422)];
	    var optsArray = [{},{},{}];
	    var labelArray = [];
	    var contentArray = [
			"台湾是中国的！",
			"南海是中国的！",
			"钓鱼岛是中国的！"];
	    for(var i = 0;i < pointArray.length; i++) {
			optsArray[i].position = pointArray[i];
			labelArray[i] = new BMap.Label(contentArray[i],optsArray[i]);
			labelArray[i].setStyle({
				color : "red",
				fontSize : "12px",
				height : "20px",
				lineHeight : "20px",
				fontFamily:"微软雅黑"
			});
			map.addOverlay(labelArray[i]);
	    }	  
	}
	
    //向地图中添加多边形函数
    function addPolyline(plPoints){
		for(var i=0;i<plPoints.length;i++){
			var json = plPoints[i];
			var points = [];
			for(var j=0;j<json.points.length;j++){
				var p1 = json.points[j].split("|")[0];
				var p2 = json.points[j].split("|")[1];
				points.push(new BMap.Point(p1,p2));
			}
			var line = new BMap.Polygon(points,{strokeStyle:'dashed',strokeWeight:'2',strokeColor:'#FF0000',strokeOpacity:'0.2',fillColor:'#E5EEFB'});
			map.addOverlay(line);
		}
	}
	
	
	//判断浏览区是否支持canvas
    function isSupportCanvas(){
        var elem = document.createElement('canvas');
        return !!(elem.getContext && elem.getContext('2d'));
    }