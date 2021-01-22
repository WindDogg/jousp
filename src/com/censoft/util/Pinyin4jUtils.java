package com.censoft.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Pinyin4jUtils{

	static HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
    public static enum Type {
        UPPERCASE,			  //全部大写
        LOWERCASE,			  //全部小写
        FIRSTUPPER            //首字母大写
    }

    static{
        format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    }

    /**
     * 将汉字转换成拼音首字母大写
     * @param str 字符串
     * @return str为'我爱编程' ,return获取到的是'WABC'
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public String toPinYinUppercase(String str) throws BadHanyuPinyinOutputFormatCombination {
        return toPinYin(str, "", Type.UPPERCASE);
    }

    /**
     * 将汉字转换成拼音首字母大写,可设置字母间的间隔符
     * @param str 字符串
     * @param spera 转换字母间隔加的字符串,如果不需要为""
     * @return str为'我爱编程' ,spera为'@' return获取到的是'W@A@B@C'
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public String toPinYinUppercase(String str,String spera) throws BadHanyuPinyinOutputFormatCombination{
        return toPinYin(str, spera, Type.UPPERCASE);
    }

    /**
     *  将汉字转换成拼音首字母小写
     * @param str 字符串
     * @return	str为'我爱编程' ,return获取到的是'wabc'
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public static String toPinYinLowercase(String str) throws BadHanyuPinyinOutputFormatCombination{
        return toPinYin(str, "", Type.LOWERCASE);
    }

    /**
     * 将汉字转换成拼音首字母小写,可设置字母间的间隔符
     * @param str 字符串
     * @param spera 转换字母间隔加的字符串,如果不需要为""
     * @return	str为'我爱编程',spera为'@' return获取到的是'w@a@b@c'
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public String toPinYinLowercase(String str,String spera) throws BadHanyuPinyinOutputFormatCombination{
        return toPinYin(str, spera, Type.LOWERCASE);
    }

    /**
     * 获取拼音首字母大写
     * @param str 字符串
     * @return str为'我爱编程' ,return获取到的是'W'
     * @throws BadHanyuPinyinOutputFormatCombination 异常信息
     */
    public String toPinYinUppercaseInitials(String str) throws BadHanyuPinyinOutputFormatCombination {
        String initials = null;
        String py = toPinYinUppercase(str);
        if(py.length()>1){
            initials = py.substring(0, 1);
        }
        if(py.length()<=1){
            initials = py;
        }
        return initials.trim();
    }

    /**
     * 获取拼音首字母小写
     * @param str 字符串
     * @return str为'我爱编程' ,return获取到的是'w'
     * @throws BadHanyuPinyinOutputFormatCombination 异常信息
     */
    public String toPinYinLowercaseInitials(String str) throws BadHanyuPinyinOutputFormatCombination {
        String initials = null;
        String py = toPinYinLowercase(str);
        if(py.length()>1){
            initials = py.substring(0, 1);
        }
        if(py.length()<=1){
            initials = py;
        }
        return initials.trim();
    }

    /**
     * 将str转换成拼音，如果不是汉字或者没有对应的拼音，则不作转换
     * @param str    字符串
     * @param spera  默认,可为""
     * @param type   转换格式
     * @return 按照转换格式转换成字符串
     * @throws BadHanyuPinyinOutputFormatCombination 异常信息
     */
    public static String toPinYin(String str, String spera, Type type) throws BadHanyuPinyinOutputFormatCombination {
        if(str == null || str.trim().length()==0) {
            return "";
        }
        if(type == Type.UPPERCASE) {
            format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        } else{
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        }
        String py = "";
        String temp = "";
        String[] t;
        for(int i=0;i<str.length();i++){
            char c = str.charAt(i);
            if((int)c <= 128)  {
                py += c;
            }else{
                t = PinyinHelper.toHanyuPinyinStringArray(c, format);
                if(t == null) {
                    py += c;
                }else{
                    temp = t[0];
                    if(type == Type.FIRSTUPPER) {
                        temp = t[0].toUpperCase().charAt(0)+temp.substring(1);
                    }
                    if(temp.length()>=1){
                        temp = temp.substring(0, 1);
                    }
                    py += temp+(i==str.length()-1?"":spera);
                }
            }
        }
        return py.trim();
    }
    
    public static String toPinYin(String str){
    	try {
			return toPinYin(str,"",null);
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
    	return null;
    }
    
 // 将汉字转换为全拼  
    public static String getPingYin(String src) {  

        char[] t1 = null;  
        t1 = src.toCharArray();  
        String[] t2 = new String[t1.length];  
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();  

        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);  
        String t4 = "";  
        int t0 = t1.length;  
        try {  
            for (int i = 0; i < t0; i++) {  
                // 判断是否为汉字字符  
                if (java.lang.Character.toString(t1[i]).matches(  
                        "[\\u4E00-\\u9FA5]+")) {  
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);  
                    t4 += t2[0];  
                } else  
                    t4 += java.lang.Character.toString(t1[i]);  
            }  

            t4 = t4.replaceAll("[^a-zA-Z]", "");   //去除字符串中非英文字符
            //System.out.println(t4);  
            return t4;  
        } catch (BadHanyuPinyinOutputFormatCombination e1) {  
            e1.printStackTrace();  
        }  
        return t4;  
    }

}