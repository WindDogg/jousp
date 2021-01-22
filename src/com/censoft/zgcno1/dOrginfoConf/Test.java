package com.censoft.zgcno1.dOrginfoConf;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.censoft.database.MysqlHelper;

public class Test {

	public void jxwsmxw20092804294_pro_zgcno1() {
		MysqlHelper.getConnection();
		String sql = "select sys_id,investment_name from jxwsmxw20092804294_pro_zgcno1 ";
		List<Map<String, Object>> list = MysqlHelper.executeQuery(sql);
		for(int i=0;i<list.size();i++){
			Map<String, Object> map = list.get(i);
			String investment_name = (String)map.get("investment_name");
			String sys_id = (String)map.get("sys_id");
			JSONArray arrs = JSONArray.parseArray(investment_name);
			String tzf = "";
			for(int j=0;j<arrs.size();j++){
				JSONObject jo = arrs.getJSONObject(j);
				tzf += jo.getString("orgname") + ",";
			}
			if(tzf.length()>1){
				tzf = tzf.substring(0, tzf.length()-1);
			}
			
			String sql2 = "update jxwsmxw20092804294_pro_zgcno1 set tzf='" + tzf + "' where sys_id = '" + sys_id + "'";
			MysqlHelper.executeUpdate(sql2);
		}
		
		MysqlHelper.close();
	}
	
	public static void main(String[] args) {
		Test t = new Test();
		t.jxwsmxw20092804294_pro_zgcno1();
	}
}
