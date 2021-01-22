package com.censoft.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class FileManager {

	private final static String PATH = "D:/upload";

	public static void main(String[] args) {
//		saveFile("notice", "************");
		File[] search = search("noti");
		if (search != null) {
			for (int i = 0; i < search.length; i++) {
				System.out.println(i + " : " + search[i].getName() + " - " + readFile(search[i]));
			}
		}else {
			System.out.println("null");
		}
	}

	public static File[] search(String name) {
		File file = new File(PATH);
		if (!file.isDirectory()) {
			return new File[0];
		}
		return file.listFiles((f, n) -> {
			return n.contains(name);
		});
	}

	public static File[] exactSearch(String name) {
		File file = new File(PATH);
		if (!file.isDirectory()) {
			return new File[0];
		}
		return file.listFiles((f, n) -> {
			return n.equals(name);
		});
	}
	
	public static String saveFile(String name, String content) {
		File file = null;
		if(name.contains(":")){
			file = new File(name);
		}else{
			file = new File(PATH + "/" + name);
		}
		try {
			save(file, content.getBytes("UTF-8"));
			return file.getName();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String appendToFile(File file, String content) {
		String read = readFile(file);
		content = read + content;
		try {
			save(file, content.getBytes("UTF-8"));
			return file.getName();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void save(File file, byte[] content) {
		try {
			FileOutputStream out = new FileOutputStream(file);
			out.write(content);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String readFile(File file) {
		if (null == file || !file.isFile()) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		try {
			FileInputStream in = new FileInputStream(file);
			InputStreamReader reader = new InputStreamReader(in, "UTF-8");
			BufferedReader br = new BufferedReader(reader);
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
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
		return sb.toString();
	}
}