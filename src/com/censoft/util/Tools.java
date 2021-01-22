package com.censoft.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

	public static String biaozhunhua(String str) {
		str = str.replace("）", ")").replace("（", "(").replace("\n", "").replace("\t", "").replace(" ", "")
				.replace("１", "1").replace("２", "2").replace("３", "3").replace("４", "4").replace("５", "5")
				.replace("６", "6").replace("７", "7").replace("８", "8").replace("９", "9").replace("０", "0")
				.replace("Ｂ", "B").replace("Ａ", "A");
		return str;
	}

	public static boolean checkNumber(String str) {
		Pattern pattern = Pattern.compile("-?(0|[1-9]\\d*)(\\.\\d+)?");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public static Boolean isEndWithNumber(String str) {
		Boolean flag = null;
		try {
			flag = true;
			Pattern pattern = Pattern.compile("[0-9]*");
			Matcher isNum = pattern.matcher(str.charAt(str.length() - 1) + "");
			if (!isNum.matches()) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return flag;
	}
	
}
