package com.yuyenews.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * 文件帮助
 * @author yuye
 *
 */
public class FileUtil {

	/**
	 * 根据文件路径 获取文件中的字符串内容
	 * @param path
	 * @return
	 */
	public static String readFileString(String path) {
		
		try {
			InputStream inputStream = FileUtil.class.getResourceAsStream(path);
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));  
			StringBuffer sb = new StringBuffer();  
			String str = "";
			while ((str = reader.readLine()) != null)  
			{  
			    sb.append(str);  
			}  
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
