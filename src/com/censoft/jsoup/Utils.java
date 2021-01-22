package com.censoft.jsoup;

import java.util.HashMap;
import java.util.Map;

public class Utils {
	
	public static Map<String, String> convertCookie(String cookie) {
		Map<String, String> cookiesMap = new HashMap<String, String>();
		String[] items = cookie.trim().split(";");
		for (String item:items) cookiesMap.put(item.split("=")[0], item.split("=")[1]);
		return cookiesMap;
	}
}
