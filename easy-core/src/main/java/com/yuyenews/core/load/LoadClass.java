package com.yuyenews.core.load;

import java.io.IOException;
import java.util.Set;

import com.yuyenews.core.util.ReadClass;

/**
 * 获取项目中的所有class
 * @author yuye
 *
 */
public class LoadClass {

	public static void loadBeans(String packageName) {
			
			try {
				Set<String> classList = ReadClass.loadClassList(packageName);
				for(String str : classList) {
					System.out.println(str);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
	
	public static void main(String[] args) {
		loadBeans("org.dom4j");
	}
}
