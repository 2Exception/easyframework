package com.yuyenews.resolve;

import com.yuyenews.easy.netty.request.HttpContext;
import com.yuyenews.easy.netty.request.HttpRequest;

/**
 * 解析请求
 * @author yuye
 *
 */
public class ResolveRequest {

	private static ResolveRequest resolveRequest;
	
	private ResolveRequest() {}
	
	public static ResolveRequest getResolveRequest() {
		if(resolveRequest == null) {
			resolveRequest = new ResolveRequest();
		}
		return resolveRequest;
	}
	
	/**
	 * 解释请求，并调用对应的控制层方法进行处理
	 * @param request
	 * @return
	 */
	public Object resolve(HttpRequest request) {
		
		/* 获取context对象 */
		HttpContext context = HttpContext.getHttpContext();
		
		
		
		return null;
	}
}
