package com.censoft.out.hwzhao.parsetxt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.censoft.database.MysqlHelper;
import com.censoft.util.Tools;

public class FuWuBao {

	public void go(String filepath) {
		MysqlHelper.getConnection("jdbc:mysql://192.168.5.72:3311/cenmds?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&autoReconnect=true","root","mysql");
		File file = new File(filepath);
		try {
			FileInputStream in = new FileInputStream(file);
			InputStreamReader reader = new InputStreamReader(in, "UTF-8");
			BufferedReader br = new BufferedReader(reader);
			String line;
			int pagenum=0;
			int curid=0;
			int step=0;
			List<String> muluList = new ArrayList<String>();
			List<String> muluList2 = new ArrayList<String>();
			List<String> contentList = new ArrayList<String>();
			boolean mulub = false;
			boolean once1 = true;
			StringBuffer temp = new StringBuffer();
			while ((line = br.readLine()) != null) {
				
				if(line.startsWith("一、") || line.startsWith("二、") || line.startsWith("三、") || line.startsWith("四、") || line.startsWith("五、") || line.startsWith("六、") || line.startsWith("七、") || line.startsWith("八、")){
					continue;
				}
				
				if(line.length()<5 && Tools.checkNumber(line)){
					int line_i = Integer.valueOf(line);
					if(line_i-pagenum==1){
						pagenum++;
						continue;
					}
				}
				
				if(pagenum>=2 && pagenum<32){
					if(line.length()>5){
						String temps = line.substring(0, 5);
						if(temps.contains(".")){
							String[] muluarrs = temps.split("[.]");
							if(muluarrs.length>0){
								String mulunum = muluarrs[0];
								if(Tools.checkNumber(mulunum)){
									if(temp.length()!=0){
										muluList.add(temp.toString());
										temp = new StringBuffer();
										step = 0;
									}
									String titlea = line.substring(mulunum.length()+1, line.length());
									temp.append(titlea);
									mulub = true;
									continue;
								}
							}
						}
					}
					
					if(mulub){
						temp.append(line);
						if(Tools.isEndWithNumber(line)){
							mulub = false;
						}
					}
//					
					
				}
				
				if(pagenum >= 32){
					if(once1){
						once1 = false;
						muluList.add(temp.toString());
						temp = new StringBuffer();
						for(int j=0; j<muluList.size(); j++){
							String title = StringUtils.trimToEmpty(muluList.get(j));
							while(Tools.isEndWithNumber(title)){
								title = StringUtils.trimToEmpty(title.substring(0, title.length()-1));
							}
							while(title.endsWith(".")){
								title = StringUtils.trimToEmpty(title.substring(0, title.length()-1));
							}
							muluList2.add(title);
						}
					}
					
					if(line.length()>5){
						String temps = line.substring(0, 5);
						if(temps.contains(".")){
							String[] titlearrs = temps.split("[.]");
							if(titlearrs.length>0){
								String titlenum = titlearrs[0];
								if(Tools.checkNumber(titlenum) && (((curid+1)+"").equals(titlenum) || ((curid+2)+"").equals(titlenum))){
									if(temp.toString().length()!=0){
										contentList.add(temp.toString());
										temp = new StringBuffer();
										curid++;
									}
									step = 1;
									String titleb = line.substring(titlenum.length()+1, line.length());
									temp.append(titleb);
									String title = muluList2.get(curid);
									if(title.equals(titleb)){
										step = 2;
										temp = new StringBuffer();
									}
									continue;
								}
							}
						}
					}
					
					
					if(step==1){
						temp.append(line);
						String titleb = temp.toString();
						String title = muluList2.get(curid);
						if(title.equals(titleb)){
							step = 2;
							temp = new StringBuffer();
							continue;
						}
					}else if(step==2){
						temp.append(line);
					}
				}
			}
			
			contentList.add(temp.toString());

			for(int k=0; k<muluList2.size(); k++){
				String mulunum = k+1+"";
				String title = muluList2.get(k);
				String content = contentList.get(k);
				String dw = "";
				String riqi = "";
				if(content.endsWith("日") && !content.endsWith("工作日")){
					int lastnian = content.lastIndexOf("年");
					String endStr = content.substring(content.length()-15, content.length());
					int last20 = endStr.lastIndexOf(" 年")<0? lastnian-4 : lastnian-5;
					int lastjuhao = content.lastIndexOf("。")+1;
					if(last20>=lastjuhao){
						dw = content.substring(lastjuhao,last20);
					}
					riqi = content.substring(last20,content.length());
					content = content.substring(0, lastjuhao);
				}
				MysqlHelper.executeUpdate("insert into fwb_zc_zdqy values(null,?,?,?,?,?)",new String[]{mulunum,title,content,dw,riqi});
			}
			
			br.close();
			reader.close();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		FuWuBao fwb = new FuWuBao();
		fwb.go("C:/Users/Administrator/Desktop/test.txt");
	}
	
}
