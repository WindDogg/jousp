package com.censoft.shujugxqk;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.censoft.database.MysqlHelper;

public class Test {

	
	public static void main(String[] args) {
		Connection conn = MysqlHelper.getConnection();
		
		String sql = "select t.table_name_rel,t.table_name_merge, CONCAT(year,'-',month) ym from da_merge t where ustate = '01'";
		List<Map<String, Object>> list = MysqlHelper.executeQuery(sql);
		
		int n = 0;
		for(int i=0;i<list.size();i++){
			Map<String, Object> map = list.get(i);
			String tableName = (String)map.get("table_name_rel");
			String sql2 = "select count(id) c from " + tableName + " where sfgs = 'y'";
			List<Map<String, Object>> list2 = MysqlHelper.executeQuery(sql2);
			int c = Integer.valueOf(((String) list2.get(0).get("c")));
			System.out.println(c);
			n += c;
		}
		System.out.println("n="+n);
		
		
		if(conn!=null){
			MysqlHelper.close();
		}
	}
	
}
