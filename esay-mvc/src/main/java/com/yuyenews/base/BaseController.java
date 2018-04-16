package com.yuyenews.base;

import java.util.Hashtable;
import java.util.Map;

import com.yuyenews.easy.netty.request.HttpContext;
import com.yuyenews.easy.netty.request.HttpRequest;

/**
 * 控制层基类，强制继承
 * @author yuye
 *
 */
public class BaseController {

	private HttpContext context = HttpContext.getHttpContext();

	/**
	 * 获取sessionId
	 * 
	 * @param httpRequest
	 * @return
	 */
	private String getSessionId(HttpRequest httpRequest) {
		Object sessionId = httpRequest.getParemeter("sessionId");
		return sessionId.toString();
	}

	/**
	 * 获取session对象
	 * @param httpRequest
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getSessionObj(HttpRequest httpRequest, String key) {
		String sessionId = getSessionId(httpRequest);

		Object obj = context.getAttr("session");

		Map<String, Map<String, Object>> map = null;
		if (obj != null) {
			/* 如果session的空间不是空的，则从session的空间里获取session列表 */
			map = (Map<String, Map<String, Object>>) obj;
			if (map != null) {
				/* 如果session列表不是空的，则从session列表里获取session */
				Map<String, Object> httpSession2 = map.get(sessionId);
				if (httpSession2 != null) {
					/* 如果获取的session不是空的 */
					return httpSession2;
				}
			}
		}

		return null;
	}

	/**
	 * 获取session中的值
	 * 
	 * @param httpRequest
	 * @param key
	 * @return
	 */
	public Object getHttpSessionValue(HttpRequest httpRequest, String key) {

		Map<String, Object> httpSession2 = getSessionObj(httpRequest, key);
		if (httpSession2 != null) {
			/* 如果获取的session不是空的 */
			return httpSession2.get(key);
		}
		
		return null;
	}

	/**
	 * 往session里插入数据
	 * @param httpRequest
	 * @param key
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	public void setSessionValue(HttpRequest httpRequest, String key, Object value) {
		Map<String, Object> httpSession2 = getSessionObj(httpRequest, key);
		
		Map<String, Map<String, Object>> map = null;
		/* 从context获取session列表 */
		Object obj = context.getAttr("session");
		if(obj != null) {
			map = (Map<String, Map<String, Object>>)obj;
		} else {
			map = new Hashtable<>();
		}
		
		if (httpSession2 == null) {
			httpSession2 = new Hashtable<>();
		}
		
		httpSession2.put(key,value);
		map.put(getSessionId(httpRequest), httpSession2);
		context.setAttr("session",map);
	}
}
