package com.yuyenews.servlcet;

import com.alibaba.fastjson.JSON;
import com.yuyenews.easy.netty.request.HttpRequest;
import com.yuyenews.easy.netty.request.HttpResponse;
import com.yuyenews.easy.netty.servlet.EasyServlet;

/**
 * 核心servlet，用于接收所有请求，并调用相应的方法进行处理
 * @author yuye
 *
 */
public class EasyCoreServlet implements EasyServlet{

	@Override
	public Object doRequest(HttpRequest request, HttpResponse response) {
		System.out.println(request.getUri());
		
		return JSON.toJSONString(request.getParemeters());
	}

}
