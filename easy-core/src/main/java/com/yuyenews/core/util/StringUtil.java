package com.yuyenews.core.util;

/**
 * 字符串工具类
 * @author yuye
 *
 */
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
	
	/**
	 * 判断字符串是否为空
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj) {
		if(obj == null || obj.toString().trim().equals("")) {
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		String a = "SdfFvg";
		
		System.out.println(getFirstLowerCase(a));
	}
}
