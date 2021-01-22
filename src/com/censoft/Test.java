package com.censoft;

import java.util.ArrayList;
import java.util.List;

public class Test {
	
	public static void main(String[] args) {
		List<String> list1 = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();
		
		list1.add("a");
		list1.add("b");
		list1.add("c");
		
		list2.add("a");
		list2.add("b");
		
		list1.addAll(list2);
		
		System.out.println(list1);
		
	}
}
