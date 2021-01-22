package com.censoft.out.liuquanfeng;

import java.sql.Connection;
import java.util.Map;

import com.censoft.database.MysqlHelper;

public class LiuQuanFeng {
	
	private Connection conn = null;
	
	public void go21() {
        
        try {
        	conn = MysqlHelper.getConnection();
        	String qy_zd_xs_count = (String)Utils.getQyCount(" and sfzdqy = 'y' and sfxs = 'y' ").get("c");
        	String qy_zd_djs_count = (String)Utils.getQyCount(" and sfzdqy = 'y' and sfdjs = 'y' ").get("c");
        	String qy_zd_cz200_count = (String)Utils.getQyCount(" and sfzdqy = 'y' and sfcz200 = 'y' ").get("c");
        	String qy_zd_zdqy600_count = (String)Utils.getQyCount(" and sfzdqy = 'y' and sfzdqy600 = 'y' ").get("c");
        	
        	String qy_zd_xs_count2 = (String)Utils.getQyCount(" and sfzdqy = 'y' and sfxs = 'y' ", "DISTINCT zzrl").get("c");
        	String qy_zd_djs_count2 = (String)Utils.getQyCount(" and sfzdqy = 'y' and sfdjs = 'y' ", "DISTINCT zzrl").get("c");
        	String qy_zd_cz200_count2 = (String)Utils.getQyCount(" and sfzdqy = 'y' and sfcz200 = 'y' ", "DISTINCT zzrl").get("c");
        	String qy_zd_zdqy600_count2 = (String)Utils.getQyCount(" and sfzdqy = 'y' and sfzdqy600 = 'y' ", "DISTINCT zzrl").get("c");
        	
        	System.out.println("规上企业："+qy_zd_xs_count+"家,涉及"+qy_zd_xs_count2+"栋楼宇");
        	System.out.println("独角兽企业："+qy_zd_djs_count+"家,涉及"+qy_zd_djs_count2+"栋楼宇");
        	System.out.println("财政800强企业："+qy_zd_cz200_count+"家,涉及"+qy_zd_cz200_count2+"栋楼宇");
        	System.out.println("重点企业600："+qy_zd_zdqy600_count+"家,涉及"+qy_zd_zdqy600_count2+"栋楼宇");
        	

        } catch (Exception e) {
        	System.out.println(e);
            return;
        } finally {
            try {
        		if(conn!=null){
        			MysqlHelper.close();
        		}
            } catch (Exception e) {
            	System.out.println("关闭数据流出错！错误信息：" + e.getMessage());
                return;
            }
        }
		
	}
	
	
	public void go22() {
		try {
			conn = MysqlHelper.getConnection();
			Map<String, Object> qy_all = Utils.getQyCount(" ");
			Map<String, Object> qy_cz200 = Utils.getQyCount(" and sfcz200 = 'y' ");
			Map<String, Object> last_qy_cz200 = Utils.getLastQyCount(" and sfcz200 = 'y' ");

			System.out.println("2020年11月，全区财政800强企业实现三级税收"+Utils.wanzhuanyi((String)qy_cz200.get("sjss_dy"))+"亿元（占全区三级税收的"+Utils.zb((String)qy_cz200.get("sjss_dy"), (String)qy_all.get("sjss_dy"))+"%），"
							+ "同比"+Utils.tb((String)qy_cz200.get("sjss_dy"),(String)last_qy_cz200.get("sjss_dy"))+"；"
							+ "其中区级税收"+Utils.wanzhuanyi((String)qy_cz200.get("qjss_dy"))+"亿元（占全区区级税收的"+Utils.zb((String)qy_cz200.get("qjss_dy"), (String)qy_all.get("qjss_dy"))+"%），"
							+ "同比"+Utils.tb((String)qy_cz200.get("qjss_dy"),(String)last_qy_cz200.get("qjss_dy"))+"。"
							+ "截至本月，全区财政800强企业累计实现三级税收"+Utils.wanzhuanyi((String)qy_cz200.get("sjss"))+"亿元，"
							+ "同比"+Utils.tb((String)qy_cz200.get("sjss"),(String)last_qy_cz200.get("sjss"))+"，"
							+ "其中区级税收"+Utils.wanzhuanyi((String)qy_cz200.get("qjss"))+"亿元，"
							+ "同比"+Utils.tb((String)qy_cz200.get("qjss"),(String)last_qy_cz200.get("qjss"))+"。");
			
		} catch (Exception e) {
			System.out.println(e);
			return;
		} finally {
			try {
				if(conn!=null){
					MysqlHelper.close();
				}
			} catch (Exception e) {
				System.out.println("关闭数据流出错！错误信息：" + e.getMessage());
				return;
			}
		}
		
	}
	
	public void go23() {
		try {
			conn = MysqlHelper.getConnection();
//			Map<String, Object> qy_all = Utils.getQyCount(" ");
			Map<String, Object> qy_cz200 = Utils.getQyCount(" and sfzdqy600 = 'y' ");
			Map<String, Object> last_qy_cz200 = Utils.getLastQyCount(" and sfzdqy600 = 'y' ");
			
			System.out.println("2020年11月，全区重点600强企业实现三级税收"+Utils.wanzhuanyi((String)qy_cz200.get("sjss_dy"))+"亿元，"
					+ "同比"+Utils.tb((String)qy_cz200.get("sjss_dy"),(String)last_qy_cz200.get("sjss_dy"))+"；"
					+ "其中区级税收"+Utils.wanzhuanyi((String)qy_cz200.get("qjss_dy"))+"亿元，"
					+ "同比"+Utils.tb((String)qy_cz200.get("qjss_dy"),(String)last_qy_cz200.get("qjss_dy"))+"。"
					+ "截至本月，全区重点600强企业累计实现三级税收"+Utils.wanzhuanyi((String)qy_cz200.get("sjss"))+"亿元，"
					+ "同比"+Utils.tb((String)qy_cz200.get("sjss"),(String)last_qy_cz200.get("sjss"))+"，"
					+ "其中区级税收"+Utils.wanzhuanyi((String)qy_cz200.get("qjss"))+"亿元，"
					+ "同比"+Utils.tb((String)qy_cz200.get("qjss"),(String)last_qy_cz200.get("qjss"))+"。");
			
		} catch (Exception e) {
			System.out.println(e);
			return;
		} finally {
			try {
				if(conn!=null){
					MysqlHelper.close();
				}
			} catch (Exception e) {
				System.out.println("关闭数据流出错！错误信息：" + e.getMessage());
				return;
			}
		}
		
	}
	
	
	public void go24() {
		try {
			conn = MysqlHelper.getConnection();
			Map<String, Object> qy_all = Utils.getQyCount(" ");
			Map<String, Object> qy_cz200 = Utils.getQyCount(" and sfqjtop100 = 'y' ");
			Map<String, Object> last_qy_cz200 = Utils.getLastQyCount(" and sfqjtop100 = 'y' ");

			System.out.println("2020年11月，区级税收前100企业实现三级税收"+Utils.wanzhuanyi((String)qy_cz200.get("sjss_dy"))+"亿元（占全区三级税收的"+Utils.zb((String)qy_cz200.get("sjss_dy"), (String)qy_all.get("sjss_dy"))+"%），"
							+ "同比"+Utils.tb((String)qy_cz200.get("sjss_dy"),(String)last_qy_cz200.get("sjss_dy"))+"；"
							+ "其中区级税收"+Utils.wanzhuanyi((String)qy_cz200.get("qjss_dy"))+"亿元（占全区区级税收的"+Utils.zb((String)qy_cz200.get("qjss_dy"), (String)qy_all.get("qjss_dy"))+"%），"
							+ "同比"+Utils.tb((String)qy_cz200.get("qjss_dy"),(String)last_qy_cz200.get("qjss_dy"))+"。"
							+ "截至本月，区级税收前100企业累计实现三级税收"+Utils.wanzhuanyi((String)qy_cz200.get("sjss"))+"亿元，"
							+ "同比"+Utils.tb((String)qy_cz200.get("sjss"),(String)last_qy_cz200.get("sjss"))+"，"
							+ "其中区级税收"+Utils.wanzhuanyi((String)qy_cz200.get("qjss"))+"亿元，"
							+ "同比"+Utils.tb((String)qy_cz200.get("qjss"),(String)last_qy_cz200.get("qjss"))+"。");
			
		} catch (Exception e) {
			System.out.println(e);
			return;
		} finally {
			try {
				if(conn!=null){
					MysqlHelper.close();
				}
			} catch (Exception e) {
				System.out.println("关闭数据流出错！错误信息：" + e.getMessage());
				return;
			}
		}
		
	}
	
	public void go25() {
		try {
			conn = MysqlHelper.getConnection();
			Map<String, Object> qy_cz200 = Utils.getQyCount(" and sfxs = 'y' ");
			Map<String, Object> last_qy_cz200 = Utils.getLastQyCount(" and sfxs = 'y' ");
			
			System.out.println("2020年11月，"+(String)qy_cz200.get("c")+"家规上企业实现三级税收"+Utils.wanzhuanyi((String)qy_cz200.get("sjss_dy"))+"亿元，"
					+ "同比"+Utils.tb((String)qy_cz200.get("sjss_dy"),(String)last_qy_cz200.get("sjss_dy"))+"；"
					+ "其中区级税收"+Utils.wanzhuanyi((String)qy_cz200.get("qjss_dy"))+"亿元，"
					+ "同比"+Utils.tb((String)qy_cz200.get("qjss_dy"),(String)last_qy_cz200.get("qjss_dy"))+"。"
					+ "截至本月，累计实现三级税收"+Utils.wanzhuanyi((String)qy_cz200.get("sjss"))+"亿元，"
					+ "同比"+Utils.tb((String)qy_cz200.get("sjss"),(String)last_qy_cz200.get("sjss"))+"，"
					+ "其中区级税收"+Utils.wanzhuanyi((String)qy_cz200.get("qjss"))+"亿元，"
					+ "同比"+Utils.tb((String)qy_cz200.get("qjss"),(String)last_qy_cz200.get("qjss"))+"。");
			
		} catch (Exception e) {
			System.out.println(e);
			return;
		} finally {
			try {
				if(conn!=null){
					MysqlHelper.close();
				}
			} catch (Exception e) {
				System.out.println("关闭数据流出错！错误信息：" + e.getMessage());
				return;
			}
		}
		
	}
	
	public void go26() {
		try {
			conn = MysqlHelper.getConnection();
			Map<String, Object> qy_all = Utils.getQyCount(" ");
			Map<String, Object> qy_cz200 = Utils.getQyCount(" and sfzczbyy = 'y' ");
			Map<String, Object> last_qy_cz200 = Utils.getLastQyCount(" and sfzczbyy = 'y' ");

			System.out.println("2020年11月，"+(String)qy_cz200.get("c")+"家注册资本亿元以上企业实现三级税收"+Utils.wanzhuanyi((String)qy_cz200.get("sjss_dy"))+"亿元（占全区三级税收的"+Utils.zb((String)qy_cz200.get("sjss_dy"), (String)qy_all.get("sjss_dy"))+"%），"
							+ "同比"+Utils.tb((String)qy_cz200.get("sjss_dy"),(String)last_qy_cz200.get("sjss_dy"))+"；"
							+ "其中区级税收"+Utils.wanzhuanyi((String)qy_cz200.get("qjss_dy"))+"亿元（占全区区级税收的"+Utils.zb((String)qy_cz200.get("qjss_dy"), (String)qy_all.get("qjss_dy"))+"%），"
							+ "同比"+Utils.tb((String)qy_cz200.get("qjss_dy"),(String)last_qy_cz200.get("qjss_dy"))+"。"
							+ "截至本月，注册资本亿元以上企业累计实现三级税收"+Utils.wanzhuanyi((String)qy_cz200.get("sjss"))+"亿元，"
							+ "同比"+Utils.tb((String)qy_cz200.get("sjss"),(String)last_qy_cz200.get("sjss"))+"，"
							+ "其中区级税收"+Utils.wanzhuanyi((String)qy_cz200.get("qjss"))+"亿元，"
							+ "同比"+Utils.tb((String)qy_cz200.get("qjss"),(String)last_qy_cz200.get("qjss"))+"。");
			
		} catch (Exception e) {
			System.out.println(e);
			return;
		} finally {
			try {
				if(conn!=null){
					MysqlHelper.close();
				}
			} catch (Exception e) {
				System.out.println("关闭数据流出错！错误信息：" + e.getMessage());
				return;
			}
		}
		
	}
	
	public void go29() {
		try {
			conn = MysqlHelper.getConnection();
			Map<String, Object> qy_all_gxqy = Utils.getQyCount(" and (hdgx = 'y' or zgcgx = 'y') ");
			Map<String, Object> qy_ggx_gxqy = Utils.getQyCount(" and hdgx = 'y' ");
			Map<String, Object> qy_cgx_gxqy = Utils.getQyCount(" and zgcgx = 'y' ");
			Map<String, Object> last_qy_ggx_gxqy = Utils.getLastQyCount(" and hdgx = 'y' ");
			
			System.out.println("全区高新企业共"+(String)qy_all_gxqy.get("c")+"家，其中国家高新技术企业"+(String)qy_ggx_gxqy.get("c")+"家。"
							+ "2020年11月，实现三级税收"+Utils.wanzhuanyi((String)qy_ggx_gxqy.get("sjss_dy"))+"亿元，"
							+ "同比"+Utils.tb((String)qy_ggx_gxqy.get("sjss_dy"),(String)last_qy_ggx_gxqy.get("sjss_dy"))+"；"
							+ "其中区级税收"+Utils.wanzhuanyi((String)qy_ggx_gxqy.get("qjss_dy"))+"亿元，"
							+ "同比"+Utils.tb((String)qy_ggx_gxqy.get("qjss_dy"),(String)last_qy_ggx_gxqy.get("qjss_dy"))+"。"
							+ "截至2020年11月，国家高新企业累计实现三级税收"+Utils.wanzhuanyi((String)qy_ggx_gxqy.get("sjss"))+"亿元，"
							+ "同比"+Utils.tb((String)qy_ggx_gxqy.get("sjss"),(String)last_qy_ggx_gxqy.get("sjss"))+"，"
							+ "累计实现区级税收"+Utils.wanzhuanyi((String)qy_ggx_gxqy.get("qjss"))+"亿元，"
							+ "同比"+Utils.tb((String)qy_ggx_gxqy.get("qjss"),(String)last_qy_ggx_gxqy.get("qjss"))+"。");
			System.out.println("全区高新企业共"+(String)qy_all_gxqy.get("c")+"家，其中中关村高新技术企业"+(String)qy_cgx_gxqy.get("c")+"家。");
			
		} catch (Exception e) {
			System.out.println(e);
			return;
		} finally {
			try {
				if(conn!=null){
					MysqlHelper.close();
				}
			} catch (Exception e) {
				System.out.println("关闭数据流出错！错误信息：" + e.getMessage());
				return;
			}
		}
		
	}
	
	
	public void go31() {
		try {
			conn = MysqlHelper.getConnection();
			Map<String, Object> qy_cz200 = Utils.getQyCount(" and sfyq = 'y' ");
			Map<String, Object> last_qy_cz200 = Utils.getLastQyCount(" and sfyq = 'y' ");
			
			System.out.println("2020年11月，全区"+(String)qy_cz200.get("c")+"家央企实现三级税收"+Utils.wanzhuanyi((String)qy_cz200.get("sjss_dy"))+"亿元，"
					+ "同比"+Utils.tb((String)qy_cz200.get("sjss_dy"),(String)last_qy_cz200.get("sjss_dy"))+"；"
					+ "其中区级税收"+Utils.wanzhuanyi((String)qy_cz200.get("qjss_dy"))+"亿元，"
					+ "同比"+Utils.tb((String)qy_cz200.get("qjss_dy"),(String)last_qy_cz200.get("qjss_dy"))+"。"
					+ "截至本月，累计实现三级税收"+Utils.wanzhuanyi((String)qy_cz200.get("sjss"))+"亿元，"
					+ "同比"+Utils.tb((String)qy_cz200.get("sjss"),(String)last_qy_cz200.get("sjss"))+"，"
					+ "其中区级税收"+Utils.wanzhuanyi((String)qy_cz200.get("qjss"))+"亿元，"
					+ "同比"+Utils.tb((String)qy_cz200.get("qjss"),(String)last_qy_cz200.get("qjss"))+"。");
			
		} catch (Exception e) {
			System.out.println(e);
			return;
		} finally {
			try {
				if(conn!=null){
					MysqlHelper.close();
				}
			} catch (Exception e) {
				System.out.println("关闭数据流出错！错误信息：" + e.getMessage());
				return;
			}
		}
		
	}
	
	public void go32() {
		try {
			conn = MysqlHelper.getConnection();
			Map<String, Object> qy_cz200 = Utils.getQyCount(" and sfss = 'y' ");
			Map<String, Object> last_qy_cz200 = Utils.getLastQyCount(" and sfss = 'y' ");
			
			System.out.println("2020年11月，全区"+(String)qy_cz200.get("c")+"家上市企业实现三级税收"+Utils.wanzhuanyi((String)qy_cz200.get("sjss_dy"))+"亿元，"
					+ "同比"+Utils.tb((String)qy_cz200.get("sjss_dy"),(String)last_qy_cz200.get("sjss_dy"))+"；"
					+ "其中区级税收"+Utils.wanzhuanyi((String)qy_cz200.get("qjss_dy"))+"亿元，"
					+ "同比"+Utils.tb((String)qy_cz200.get("qjss_dy"),(String)last_qy_cz200.get("qjss_dy"))+"。"
					+ "截至本月，累计实现三级税收"+Utils.wanzhuanyi((String)qy_cz200.get("sjss"))+"亿元，"
					+ "同比"+Utils.tb((String)qy_cz200.get("sjss"),(String)last_qy_cz200.get("sjss"))+"，"
					+ "其中区级税收"+Utils.wanzhuanyi((String)qy_cz200.get("qjss"))+"亿元，"
					+ "同比"+Utils.tb((String)qy_cz200.get("qjss"),(String)last_qy_cz200.get("qjss"))+"。");
			
		} catch (Exception e) {
			System.out.println(e);
			return;
		} finally {
			try {
				if(conn!=null){
					MysqlHelper.close();
				}
			} catch (Exception e) {
				System.out.println("关闭数据流出错！错误信息：" + e.getMessage());
				return;
			}
		}
		
	}
	
	public void go33() {
		try {
			conn = MysqlHelper.getConnection();
			Map<String, Object> qy_cz200 = Utils.getQyCount(" and sfzcsy = 'y' ");
			Map<String, Object> last_qy_cz200 = Utils.getLastQyCount(" and sfzcsy = 'y' ");
			
			System.out.println("2020年11月，全区"+(String)qy_cz200.get("c")+"家企业享受过各类奖励政策，政策受益企业实现三级税收"+Utils.wanzhuanyi((String)qy_cz200.get("sjss_dy"))+"亿元，"
					+ "同比"+Utils.tb((String)qy_cz200.get("sjss_dy"),(String)last_qy_cz200.get("sjss_dy"))+"；"
					+ "其中区级税收"+Utils.wanzhuanyi((String)qy_cz200.get("qjss_dy"))+"亿元，"
					+ "同比"+Utils.tb((String)qy_cz200.get("qjss_dy"),(String)last_qy_cz200.get("qjss_dy"))+"。"
					+ "截至本月，累计实现三级税收"+Utils.wanzhuanyi((String)qy_cz200.get("sjss"))+"亿元，"
					+ "同比"+Utils.tb((String)qy_cz200.get("sjss"),(String)last_qy_cz200.get("sjss"))+"，"
					+ "其中区级税收"+Utils.wanzhuanyi((String)qy_cz200.get("qjss"))+"亿元，"
					+ "同比"+Utils.tb((String)qy_cz200.get("qjss"),(String)last_qy_cz200.get("qjss"))+"。");
			
		} catch (Exception e) {
			System.out.println(e);
			return;
		} finally {
			try {
				if(conn!=null){
					MysqlHelper.close();
				}
			} catch (Exception e) {
				System.out.println("关闭数据流出错！错误信息：" + e.getMessage());
				return;
			}
		}
		
	}
	
	
	public static void main(String[] args) {
		LiuQuanFeng l = new LiuQuanFeng();
//		l.go21();
//		l.go22();
//		l.go23();
//		l.go24();
//		l.go25();
//		l.go26();
//		l.go29();
//		l.go31();
//		l.go32();
		l.go33();
	}

}
