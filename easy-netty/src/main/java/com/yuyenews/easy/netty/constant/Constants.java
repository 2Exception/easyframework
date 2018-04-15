package com.yuyenews.easy.netty.constant;

import java.util.Hashtable;
import java.util.Map;

/**
 * 全局存储空间
 * @author yuye
 *
 */
public class Constants {

	private static Constants constants;
	
	private Map<String, Object> map = new Hashtable<>();

	private Constants() {
	}

	public static Constants getConstants() {
		if (constants == null) {
			constants = new Constants();
		}

		return constants;
	}
	
	/**
	 * 往Constants里添加数据
	 * @param key
	 * @param value
	 */
	public void setAttr(String key,Object value) {
		map.put(key, value);
	}
	
	/**
	 * 从Constants里获取数据
	 * @param key
	 * @return
	 */
	public Object getAttr(String key) {
		return map.get(key);
	}
}
