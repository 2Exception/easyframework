package com.yuyenews.easy.netty.request;

import java.util.Hashtable;
import java.util.Map;

/**
 * 全局对象，类似于tomcat的servletcontext
 * 
 * @author yuye
 *
 */
public class HttpContext {

	private static HttpContext context;

	private Map<String, Object> map = new Hashtable<>();

	private HttpContext() {
	}

	public static HttpContext getHttpContext() {
		if (context == null) {
			context = new HttpContext();
		}

		return context;
	}

	/**
	 * 往context里添加数据
	 * 
	 * @param key
	 * @param value
	 */
	public void setAttr(String key, String value) {
		map.put(key, value);
	}

	/**
	 * 从context里获取数据
	 * 
	 * @param key
	 * @return
	 */
	public Object getAttr(String key) {
		return map.get(key);
	}
}
