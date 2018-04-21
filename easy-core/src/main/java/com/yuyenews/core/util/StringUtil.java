package com.yuyenews.core.util;

public class StringUtil {

	/**
	 * 将字符串首字母转成小写
	 * @param str
	 * @return
	 */
	public static String getFirstLowerCase(String str) {
		String str2 = str.substring(1);
		String str3 = str.substring(0,1);
		
		return str3.toLowerCase()+str2;
	}
	
	public static void main(String[] args) {
		String a = "SdfFvg";
		
		System.out.println(getFirstLowerCase(a));
	}
}
