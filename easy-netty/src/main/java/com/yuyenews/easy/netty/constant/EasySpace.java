package com.yuyenews.easy.netty.constant;

import java.util.Hashtable;
import java.util.Map;

/**
 * 全局存储空间
 * @author yuye
 *
 */
public class EasySpace {

	private static EasySpace constants;
	
	private Map<String, Object> map = new Hashtable<>();

	private EasySpace() {
	}

	public static EasySpace getEasySpace() {
		if (constants == null) {
			constants = new EasySpace();
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
