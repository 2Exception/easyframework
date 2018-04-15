package com.yuyenews.easy.netty.servlet;

import com.yuyenews.easy.netty.request.HttpRequest;
import com.yuyenews.easy.netty.request.HttpResponse;

/**
 * servlet 模板
 * @author yuye
 *
 */
public interface EasyServlet {
	
	
	/**
	 * 请求接受方法
	 */
	Object doRequest(HttpRequest request,HttpResponse response);
}
