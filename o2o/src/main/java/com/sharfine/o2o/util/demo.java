package com.sharfine.o2o.util;

public class demo {
	private static String seperator = System.getProperty("file.separator");
	public static void main(String[] args) {
	String	basePath ="/home/sharfine/";
	System.out.println(basePath);
	basePath = basePath.replace("/", seperator);
	System.out.println(basePath);
	}
}
