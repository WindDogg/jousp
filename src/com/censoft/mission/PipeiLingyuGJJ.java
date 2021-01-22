package com.censoft.mission;

import java.util.List;
import java.util.Map;

import com.censoft.database.MysqlHelper;

public class PipeiLingyuGJJ {

	public static void main(String[] args) {
		MysqlHelper.getConnection();
		String [] types = PipeiLingyuDatas.getTypes();
		for(int i=0;i<types.length;i++){
			System.out.println(types[i]);
			StringBuffer sb = new StringBuffer();
			String sql = "select id from t_qy_content where " + types[i] + " = 1 ";
			List<Map<String, Object>> list = MysqlHelper.executeQuery(sql);
			for(int j=0;j<list.size();j++){
				Map<String, Object> map = list.get(j);
				String id = (String)map.get("id");
				sb.append(id).append(",");
			}
			String ids = sb.substring(0, sb.length()-1);
			MysqlHelper.executeUpdate("update t_qy_content set gjj = 1 where id in ("+ids+")");
		}
		
		MysqlHelper.close();
	}
	
}
