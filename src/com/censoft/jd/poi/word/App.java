package com.censoft.jd.poi.word;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class App {
	public static void main(String[] args) {
		// doc文档路径
		String filePath = "C:\\Users\\Administrator\\Desktop\\京东数据库设计v2.0.doc";

		System.out.println(App.read(filePath, "名称"));
		;
	}

	/**
	 * 读取文档中表格
	 * 
	 * @param filePath
	 *            doc路径
	 * @param set
	 *            第几个表格
	 */
	public static String read(String filePath, String tableName) {

		Connection conn = null;
		Statement stmt = null;
		int n=0;
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://192.168.3.12:3311/jd?useUnicode=true&characterEncoding=utf8", "root", "mysql");
			stmt = conn.createStatement();
			
			FileInputStream in = new FileInputStream(filePath); // 载入文档
			
			POIFSFileSystem pfs = new POIFSFileSystem(in);
			HWPFDocument hwpf = new HWPFDocument(pfs);

			Range range = hwpf.getRange();// 得到文档的读取范围
			
			List<Map<String, String>> listTitle = new ArrayList<Map<String, String>>();
			for(int i=0;i<range.numSections();i++){
				Section s = range.getSection(i);
				for(int j=0;j<s.numParagraphs();j++){
					Paragraph p = s.getParagraph(j);
					for(int k=0;k<p.numCharacterRuns();k++){
						CharacterRun run = p.getCharacterRun(k);
						String text = run.text().trim();

						if("表的清单".equals(text)){
							continue;
						}
						
						if(text.indexOf("的清单")>-1){
							if(text.startsWith("表")){
								text = text.substring(1, text.length());
								Map<String, String> map = chaifen(text);
								listTitle.add(map);
							}
						}
					}
				}
			}
			
			
			TableIterator it = new TableIterator(range);
			// 迭代文档中的表格
			while (it.hasNext()) {
				Table tb = (Table) it.next();
				if(n==0){n++;continue;}
				
				Map<String, String> mapTitle = listTitle.get(n-1);
				String tablename = mapTitle.get("tablename");
				String tablenote = mapTitle.get("tablenote");
				
				StringBuffer sql = new StringBuffer();
				sql.append("create table "+tablename+"(");
				
				// 迭代行，默认从0开始,可以依据需要设置i的值,改变起始行数，也可设置读取到那行，只需修改循环的判断条件即可
				outer: for (int i = 1; i < tb.numRows(); i++) {
					TableRow tr = tb.getRow(i);
					String col = "",type="",note="",key="";
					// 迭代列，默认从0开始
					for (int j = 0; j < tr.numCells(); j++) {
						TableCell td = tr.getCell(j);// 取得单元格
						// 取得单元格的内容
						for (int k = 0; k < td.numParagraphs(); k++) {
							Paragraph para = td.getParagraph(k);
							String s = para.text();
							
							s = s.trim();
							if(j==0){
								s = s.replace(" ", "_");
								col = "`"+s+"`";
								if(col.toLowerCase().equals("id")){
									key = "PRIMARY KEY";
								}
							}else if(j==1){
								note = s;
								if(note.indexOf("主键")>-1 && note.length()<5){
									key = "PRIMARY KEY";
								}
							}else if(j==2){
								if("varchar(1024)".equals(s)){
									s = "varchar(512)";
								}
								type = s;
							}else{
								System.out.println("多列");
							}
						}
					}
					sql.append(col+" "+type+" "+key+" COMMENT '"+note+"',");
				}
				
				String rsql = sql.substring(0, sql.length()-1)+") COMMENT='"+tablenote+"'";
				
				System.out.println(rsql);
				boolean b = stmt.execute(rsql);
				
				n++;
			}

		} catch (Exception e) {
			System.out.println(n);
			e.printStackTrace();
		}finally{
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return "";
	}
	
	
	private static Map<String, String> chaifen(String text){
		Map<String, String> map = new HashMap<String, String>();
		String tablename="";
		String tablenote="";
		char strs[]=text.toCharArray();
		for (int i = 0; i < strs.length; i++) {
			int num = (int)strs[i];
		    if(num>=65&&num<=122) {
		        //字母
		    	tablename += strs[i];
		    }else{
		    	tablenote += strs[i];
		    }
		}
		map.put("tablename", tablename.trim());
		map.put("tablenote", tablenote.trim());
		
		return map;
	}

}
