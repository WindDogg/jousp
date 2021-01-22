package com.censoft.jd.poi.word;

import java.io.FileInputStream;
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

public class readWord {
	public static void main(String[] args) {
		// doc文档路径
		String filePath = "C:\\Users\\Administrator\\Desktop\\京东数据库设计v2.0.doc";

		System.out.println(readWord.read(filePath, "名称"));
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

		StringBuilder sb = new StringBuilder();

		try {
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

						if(text.indexOf("的清单")>-1){
							Map<String, String> map = chaifen(text);
							listTitle.add(map);
						}
					}
				}
			}
			
			System.out.println(listTitle.size());
			
			
			TableIterator it = new TableIterator(range);
			// 迭代文档中的表格
			int n=0;
			while (it.hasNext()) {
				Table tb = (Table) it.next();
				//String title = listTitle.get(n);
				
				// 迭代行，默认从0开始,可以依据需要设置i的值,改变起始行数，也可设置读取到那行，只需修改循环的判断条件即可
				outer: for (int i = 0; i < tb.numRows(); i++) {
					TableRow tr = tb.getRow(i);
					// 迭代列，默认从0开始
					for (int j = 0; j < tr.numCells(); j++) {
						TableCell td = tr.getCell(j);// 取得单元格
						// 取得单元格的内容
						for (int k = 0; k < td.numParagraphs(); k++) {
							Paragraph para = td.getParagraph(k);
							String s = para.text();
							// 去除后面的特殊符号
							if (null != s && !"".equals(s)) {
								s = s.substring(0, s.length() - 1);
							}
							s = s.trim();

						}
					}
					sb.append("\n");
				}
				
				
				
				
				
				
				n++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}
	
	
	private static Map<String, String> chaifen(String text){
		Map<String, String> map = new HashMap<String, String>();
		
		return map;
	}

}
