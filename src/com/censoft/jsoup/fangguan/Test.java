package com.censoft.jsoup.fangguan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.censoft.util.CoordinateTransform;

public class Test {

	
	public static void main(String[] args) {
		
		String strs = "116.29663989151879,39.943040947382713],[116.29660770478279,39.943057040750716],[116.29654869666604,39.943116048867466],[116.29664525597479,39.943255523824291],[116.29726216391884,39.943035582926711],[116.29714951124197,39.942858556777878],[116.29663989151879,39.943040947382713";
		String arrs[] = strs.split("],\\[");
		List<String[]> list = new ArrayList<String[]>();
		for(int i=0;i<arrs.length;i++){
			list.add(arrs[i].split(","));
		}
		Map<String, Double> centerPoint = Utils.getCenterPoint(list);
		System.out.println(centerPoint);
		double[] bdlnglat = CoordinateTransform.transformWGS84ToBD09(centerPoint.get("lng"), centerPoint.get("lat"));
		System.out.println(bdlnglat[0]);
		System.out.println(bdlnglat[1]);
	}
}
