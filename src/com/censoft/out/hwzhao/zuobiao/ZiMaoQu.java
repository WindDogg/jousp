package com.censoft.out.hwzhao.zuobiao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.censoft.database.MysqlHelper;
import com.censoft.out.utils.Utils;
import com.censoft.util.FileManager;

public class ZiMaoQu {
	
	//自贸区1-百度坐标系
	static String zmq_1 ="[{'lat':'40.07343759625784','lng':'116.15590007246836'},{'lat':'40.07368390848234','lng':'116.1566702569846'},{'lat':'40.0773399255265','lng':'116.15660682404366'},{'lat':'40.078115772005916','lng':'116.15652401039955'},{'lat':'40.07782639058841','lng':'116.1538788743617'},{'lat':'40.07906353979267','lng':'116.15074776761189'},{'lat':'40.08039928707387','lng':'116.151261216942'},{'lat':'40.08807325222641','lng':'116.15456232325866'},{'lat':'40.08926481047783','lng':'116.164442448502'},{'lat':'40.087286455777296','lng':'116.16485122400903'},{'lat':'40.08878967804373','lng':'116.17353155876717'},{'lat':'40.09019095622848','lng':'116.17725211666884'},{'lat':'40.095537708566276','lng':'116.18693899232954'},{'lat':'40.09484617384315','lng':'116.18768804144342'},{'lat':'40.097684113067096','lng':'116.19649712231814'},{'lat':'40.096847676006604','lng':'116.19715978219725'},{'lat':'40.09620983542558','lng':'116.19768959824553'},{'lat':'40.09560629760241','lng':'116.19806134304285'},{'lat':'40.09462496928396','lng':'116.19856276849825'},{'lat':'40.09362126464793','lng':'116.19917338257679'},{'lat':'40.09292336237283','lng':'116.19965414849572'},{'lat':'40.09242193229395','lng':'116.200156772717'},{'lat':'40.089903200942594','lng':'116.20263244862011'},{'lat':'40.08641661427451','lng':'116.20315981959625'},{'lat':'40.08590877100528','lng':'116.20318795590751'},{'lat':'40.08489268872263','lng':'116.20292853579937'},{'lat':'40.084497002808426','lng':'116.2027528553653'},{'lat':'40.08394873320709','lng':'116.20246752916844'},{'lat':'40.083183271825604','lng':'116.20205811406926'},{'lat':'40.08295674109334','lng':'116.20192158267506'},{'lat':'40.08274555082716','lng':'116.2016107478577'},{'lat':'40.0827570062613','lng':'116.20080083817844'},{'lat':'40.08294326041333','lng':'116.19997817557277'},{'lat':'40.08317955194763','lng':'116.19903077861777'},{'lat':'40.08345388098289','lng':'116.19809572273785'},{'lat':'40.0841969185142','lng':'116.1965615038815'},{'lat':'40.084458278446064','lng':'116.19634907856482'},{'lat':'40.08449879257866','lng':'116.19342507084183'},{'lat':'40.082482437320536','lng':'116.19317314010445'},{'lat':'40.082361244840406','lng':'116.19659929055167'},{'lat':'40.081318006585605','lng':'116.19660178621103'},{'lat':'40.081388107915444','lng':'116.20121262306195'},{'lat':'40.08168644228437','lng':'116.20119638083924'},{'lat':'40.08183524468513','lng':'116.20127701947352'},{'lat':'40.081905469481526','lng':'116.2013516060012'},{'lat':'40.081953581356544','lng':'116.20146361378251'},{'lat':'40.08206493567745','lng':'116.20164088046799'},{'lat':'40.082183451024974','lng':'116.20181813088578'},{'lat':'40.08230799300894','lng':'116.20206076661506'},{'lat':'40.08236376333311','lng':'116.20214472761043'},{'lat':'40.08244734159229','lng':'116.20227534034643'},{'lat':'40.0825453924674','lng':'116.20239657840249'},{'lat':'40.08262246975622','lng':'116.20248983473276'},{'lat':'40.08270718498857','lng':'116.20255504680996'},{'lat':'40.08282670553672','lng':'116.20267623782422'},{'lat':'40.08287565715417','lng':'116.2027415281144'},{'lat':'40.08298835065922','lng':'116.20284404915849'},{'lat':'40.0832080289385','lng':'116.20296502288309'},{'lat':'40.08339226308368','lng':'116.20306738969447'},{'lat':'40.08349908610061','lng':'116.2030951857442'},{'lat':'40.08364567659129','lng':'116.20318571413591'},{'lat':'40.08380547424703','lng':'116.20380925439832'},{'lat':'40.0838221890057','lng':'116.20472929866382'},{'lat':'40.08380251920278','lng':'116.20505627058347'},{'lat':'40.08380163450418','lng':'116.20555131625714'},{'lat':'40.08377716997894','lng':'116.20618647523862'},{'lat':'40.08369887001078','lng':'116.20661623891166'},{'lat':'40.08363924223211','lng':'116.20723270330858'},{'lat':'40.08355599240632','lng':'116.20753168498598'},{'lat':'40.08344452196369','lng':'116.20794664571652'},{'lat':'40.083384786105235','lng':'116.20811483096365'},{'lat':'40.08323283128165','lng':'116.20915737146946'},{'lat':'40.08321905515639','lng':'116.20968355106544'},{'lat':'40.08238203156446','lng':'116.21073985278453'},{'lat':'40.08141416420732','lng':'116.2118303613497'},{'lat':'40.07344558271131','lng':'116.21274006977569'},{'lat':'40.067023759048276','lng':'116.21345407826558'},{'lat':'40.05260606128079','lng':'116.22012240869044'},{'lat':'40.050229598320804','lng':'116.21133824545362'},{'lat':'40.05059992204223','lng':'116.20932316690298'},{'lat':'40.05090096576211','lng':'116.2082776574653'},{'lat':'40.053898304136645','lng':'116.2067752628575'},{'lat':'40.05363868990184','lng':'116.20169941847855'},{'lat':'40.05784478248498','lng':'116.18360843073167'},{'lat':'40.05856561645593','lng':'116.18099626828511'},{'lat':'40.059187808946774','lng':'116.17880130292441'},{'lat':'40.059561704700414','lng':'116.17717064375326'},{'lat':'40.063875137510855','lng':'116.1778346290191'},{'lat':'40.06673578755355','lng':'116.17788651650585'},{'lat':'40.06709015873049','lng':'116.16982158316205'},{'lat':'40.066177788936685','lng':'116.16031761054883'},{'lat':'40.06634278363062','lng':'116.15710760028128'},{'lat':'40.06885327528674','lng':'116.15161027824844'},{'lat':'40.07350673215384','lng':'116.15168896465433'},{'lat':'40.07343759625784','lng':'116.15590007246836'}]";
	//自贸区2-百度坐标系
	static String zmq_2 ="[{'lat':'40.07590191296925','lng':'116.23756130572399'},{'lat':'40.085399505778','lng':'116.23297529178672'},{'lat':'40.08978522972403','lng':'116.25031101040588'},{'lat':'40.08784174311411','lng':'116.25092910289315'},{'lat':'40.08856199710524','lng':'116.25376628766031'},{'lat':'40.087044585148774','lng':'116.25419884488808'},{'lat':'40.0880794788211','lng':'116.25845505579136'},{'lat':'40.082784493826054','lng':'116.2608015191796'},{'lat':'40.08151850637082','lng':'116.25691541010188'},{'lat':'40.075282182371595','lng':'116.25987909587366'},{'lat':'40.07373507930506','lng':'116.25377493026062'},{'lat':'40.06359606334636','lng':'116.25734758911518'},{'lat':'40.06218882144124','lng':'116.25853281793671'},{'lat':'40.06119035961596','lng':'116.26016165115573'},{'lat':'40.05971590261377','lng':'116.24869872321054'},{'lat':'40.06187796153875','lng':'116.24802967865334'},{'lat':'40.06105391342147','lng':'116.24299902546936'},{'lat':'40.07021368533296','lng':'116.23913421318258'},{'lat':'40.07590191296925','lng':'116.23756130572399'}]";
	//自贸区3-百度坐标系
	static String zmq_3 ="[{'lat':'40.08862690186956','lng':'116.27239365683842'},{'lat':'40.09472233418159','lng':'116.26809835260954'},{'lat':'40.09123840551607','lng':'116.25671482096887'},{'lat':'40.09784647853944','lng':'116.2532405932195'},{'lat':'40.098800339395844','lng':'116.25640250684854'},{'lat':'40.09971295850053','lng':'116.25894788547893'},{'lat':'40.10224343297325','lng':'116.2572875229639'},{'lat':'40.101586186257','lng':'116.2552574535647'},{'lat':'40.104012137194474','lng':'116.25347813785696'},{'lat':'40.106564695874994','lng':'116.26140253899948'},{'lat':'40.095149665552654','lng':'116.26858977745732'},{'lat':'40.098153269381456','lng':'116.27680718756443'},{'lat':'40.09462016814333','lng':'116.2822507919196'},{'lat':'40.09338105834001','lng':'116.27880602528474'},{'lat':'40.09110447186747','lng':'116.27624842832434'},{'lat':'40.08862690186956','lng':'116.27239365683842'}]";
	
	static String type = "zdqy";
	static String geotype = "MultiPolygon";
	static String name = "自贸区";
	static String code = "120010";
	static String targetUrl = "E:/work/workspace2016/report/WebRoot/map/json/hd_zmq.json";

	public void gotoJson() throws IOException {
		
		JSONObject jsonObject = Utils.getGeoJsonTemplate();
		JSONArray jsonArray = jsonObject.getJSONArray("features");
		
		JSONObject zmqObject = jsonArray.getJSONObject(0);
		JSONObject geometryObject = zmqObject.getJSONObject("geometry");
		geometryObject.put("type", geotype);

		//////////////////////////////////////////////////////////////
		
		Double strs[][][] = new Double[3][][];
		List<Double[]> clist = new ArrayList<Double[]>();
		
		JSONArray zmq_1Array = JSONArray.parseArray(zmq_1);
		List<Double[]> list1 = new ArrayList<Double[]>();
		for(int i=0;i<zmq_1Array.size();i++){
			JSONObject lnglat = zmq_1Array.getJSONObject(i);
			Double lnglats[] = new Double[]{lnglat.getDouble("lng"),lnglat.getDouble("lat")};
			list1.add(lnglats);
			clist.add(lnglats);
		}
		Double arr1[][] = new Double[list1.size()][];
		for(int i=0;i<list1.size();i++){
			arr1[i] = list1.get(i);
		}
		strs[0] = arr1;
		
		JSONArray zmq_2Array = JSONArray.parseArray(zmq_2);
		List<Double[]> list2 = new ArrayList<Double[]>();
		for(int i=0;i<zmq_2Array.size();i++){
			JSONObject lnglat = zmq_2Array.getJSONObject(i);
			Double lnglats[] = new Double[]{lnglat.getDouble("lng"),lnglat.getDouble("lat")};
			list2.add(lnglats);
			clist.add(lnglats);
		}
		Double arr2[][] = new Double[list2.size()][];
		for(int i=0;i<list2.size();i++){
			arr2[i] = list2.get(i);
		}
		strs[1] = arr2;
		
		JSONArray zmq_3Array = JSONArray.parseArray(zmq_3);
		List<Double[]> list3 = new ArrayList<Double[]>();
		for(int i=0;i<zmq_3Array.size();i++){
			JSONObject lnglat = zmq_3Array.getJSONObject(i);
			Double lnglats[] = new Double[]{lnglat.getDouble("lng"),lnglat.getDouble("lat")};
			list3.add(lnglats);
			clist.add(lnglats);
		}
		Double arr3[][] = new Double[list3.size()][];
		for(int i=0;i<list3.size();i++){
			arr3[i] = list3.get(i);
		}
		strs[2] = arr3;
		
		geometryObject.put("coordinates", strs);
		
		JSONObject propertiesObject = zmqObject.getJSONObject("properties");
		propertiesObject.put("gridname", name);
		propertiesObject.put("gridid", code);
		Map<String, Double> centerPoint = Utils.getCenterPoint(clist);
		propertiesObject.put("X", centerPoint.get("lng"));
		propertiesObject.put("Y", centerPoint.get("lat"));
		
		FileManager.saveFile(targetUrl, jsonObject.toJSONString());
		
	}
	
	
	public void jsonToDb() throws IOException {
		MysqlHelper.getConnection("jdbc:mysql://192.168.5.72:3311/cenmds?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true","root","mysql");
		
		File jsonFile = new File(targetUrl);
		String readFile = FileManager.readFile(jsonFile);
		System.out.println(readFile);
		JSONObject jsonObject = JSON.parseObject(readFile);
		JSONArray jsonArray = jsonObject.getJSONArray("features");
		String type = this.type;
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jdObject = jsonArray.getJSONObject(i);
			
			JSONObject jdGeometry = jdObject.getJSONObject("geometry");
			
			JSONObject jdProperties = jdObject.getJSONObject("properties");
			String name = jdProperties.getString("gridname");
			String code = jdProperties.getString("gridid");
			String centerlng = jdProperties.getString("X");
			String centerlat = jdProperties.getString("Y");
			String geotype = jdGeometry.getString("type");
			
			String lnglat = jdGeometry.getString("coordinates");
			
			MysqlHelper.executeUpdate("insert into hdq_area_lnglat values(?,?,?,?,?,?,?,?,?,?)", new String[]{null, code, name, type, lnglat, "bd", geotype, centerlng, centerlat, ""});
			
		}
		
		MysqlHelper.close();
	}
	
	public static void main(String[] args) throws IOException {
		ZiMaoQu obj = new ZiMaoQu();
		obj.gotoJson();
		obj.jsonToDb();
	}
}
