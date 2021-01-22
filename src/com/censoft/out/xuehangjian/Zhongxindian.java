package com.censoft.out.xuehangjian;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.censoft.database.MysqlHelper;
import com.censoft.out.utils.Utils;

public class Zhongxindian {
	
	public void go() throws IOException {
		MysqlHelper.getConnection("jdbc:mysql://192.168.5.72:3311/cenmds?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true","root","mysql");
		
		List<Map<String, Object>> list = MysqlHelper.executeQuery("select id,bdshape from xxandyy_bd");
		for(int i=0;i<list.size();i++){
			List<String[]> zblist = new ArrayList<String[]>();
			Map<String, Object> map = list.get(i);
			String id = StringUtils.trimToEmpty((String)map.get("id"));
			String zuobiaos = StringUtils.trimToEmpty((String)map.get("bdshape"));
			String[] arrays = zuobiaos.split(";");
			for(String arr: arrays){
				zblist.add(arr.split(","));
			}
			Map<String, Double> centerPoint = Utils.getCenterPoint(zblist);
			MysqlHelper.executeUpdate("update xxandyy_bd set lng='"+centerPoint.get("lng")+"', lat='"+centerPoint.get("lat")+"' where id='"+id+"'");
		}
		
	}
	
	
	public static void main(String[] args) throws IOException {
		Zhongxindian obj = new Zhongxindian();
		obj.go();
	}
	
}
