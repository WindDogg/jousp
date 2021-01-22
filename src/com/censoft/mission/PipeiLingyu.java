package com.censoft.mission;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.censoft.database.MysqlHelper;

public class PipeiLingyu {
	
	private static List<Map<String, Object>> dicList;
	
	public static void InitDicList(String type){
		String sql = "select keyword,dic from t_dic_gjj_hb where dic = '"+type+"' ";
		dicList = MysqlHelper.executeQuery(sql, null);
	}
	
	public List<Map<String, Object>> getZlJyfw(int pageNum, int pageSize) {
		String sql = "select id,zlcontent,zltitle,jyfwold,jyfwn from t_qy_content limit "+(pageNum-1)*pageSize+","+pageSize;
		List<Map<String, Object>> list = MysqlHelper.executeQuery(sql, null);
		return list;
	}
	
	public String dabiaoji(Map<String, Object> map) {
		String zltitle = (String)map.get("zltitle");
		String id = (String)map.get("id");
		String zlcontent = (String)map.get("zlcontent");
		String jyfwold = (String)map.get("jyfwold");
		String jyfwn = (String)map.get("jyfwn");
		if("".equals(zltitle) && "".equals(zlcontent) && "".equals(jyfwold) && "".equals(jyfwn)){
			return "0";
		}
		StringBuffer sb = new StringBuffer("专利title:");
		sb.append(zltitle).append(".专利内容:").append(zlcontent).append(".经营范围old:")
		.append(jyfwold).append(".经营范围new:").append(jyfwn);
		String text = sb.toString();
		text = qukuohao(text);
		for(int i=0;i<dicList.size();i++){
			Map<String, Object> dicMap = dicList.get(i);
			String keyword = (String)dicMap.get("keyword");
			if(isExsit(text,keyword,id)){
				return id;
			}
		}
		return "0";
	}
	
	public boolean isExsit(String text, String keyword, String id) {
		boolean b = false;
		if(text.contains(keyword)){
			b = true;
		}
		return b;
	}
	
	private String qukuohao(String text){
		return text.replaceAll("\\（[^）]+\\）","").replaceAll("\\([^)]+\\)",""); 
	}

	public static void main(String[] args) {
		String[] types = PipeiLingyuDatas.getTypes();
		
		Connection conn = MysqlHelper.getConnection();
		PipeiLingyu pl = new PipeiLingyu();
		for(int k=0;k<types.length;k++){
			
			InitDicList(types[k]);
			System.out.println(dicList);
			
			int pageCount = 7;
			int pageSize = 50000;
			StringBuffer sb = new StringBuffer();
			for(int p=1;p<=pageCount;p++){
				System.out.println("第"+p+"页");
				List<Map<String, Object>> list = pl.getZlJyfw(p, pageSize);
				for(int i=0;i<list.size();i++){
					Map<String, Object> map = list.get(i);
					String num = pl.dabiaoji(map);//匹配到返回id 否则返回0
					if(Integer.valueOf(num)>0){
						sb.append(map.get("id"));
						sb.append(",");
					}
				}
			}
			String sql = "update t_qy_content set " + types[k] + " = 1 where id in (" + sb.substring(0, sb.length()-1) + ")";
			System.out.println(sql);
			MysqlHelper.executeUpdate(sql);
		}
		
		if(conn!=null){
			MysqlHelper.close();
		}
	}
	
	public static void main0(String[] args) {
		MysqlHelper.getConnection();
		PipeiLingyu pl = new PipeiLingyu();
		InitDicList("rgzn");
		String text = "出版、发行《网络运维与管理》杂志；出版、发行《工业经济论坛》杂志（期刊出版许可证有效期至2018年12月31日）；出版、发行《工业技术创新》杂志（期刊出版许可证有效期至2018年12月31日）；出版、发行《互联网经济》（期刊出版许可证有效期至2018年12月31日）；出版、发行《电子科学技术》（期刊出版许可证有效期至2018年12月31日）；出版、发行《中国工业评论》（期刊出版许可证有效期至2018年12月31日）；出版、发行《新能源汽车报》（报纸出版许可证有效期至2018年12月31日）；设计和制作印刷品广告，利用自有《网络运维与管理》、《工业经济论坛》、《工业技术创新》、《互联网经济》、《电子科学技术》、《中国工业评论》杂志发布广告（广告经营许可证有效期至2018年12月31日）；出版、发行《机器人产业》（期刊出版许可证有效期至2018年12月31日）。";
		text = pl.qukuohao(text);
		for(int i=0;i<dicList.size();i++){
			Map<String, Object> dicMap = dicList.get(i);
			String keyword = (String)dicMap.get("keyword");
			if(pl.isExsit(text,keyword,"")){
				System.out.println(keyword);
			}
		}
		MysqlHelper.close();
	}
}
