package com.yuyenews.servlcet;

import com.alibaba.fastjson.JSONObject;
import com.yuyenews.easy.netty.request.HttpRequest;
import com.yuyenews.easy.netty.request.HttpResponse;
import com.yuyenews.easy.netty.servlet.EasyServlet;
import com.yuyenews.resolve.ResolveRequest;

/**
 * 核心servlet，用于接收所有请求，并调用相应的方法进行处理
 * @author yuye
 *
 */
public class EasyCoreServlet implements EasyServlet{

	@Override
	public Object doRequest(HttpRequest request, HttpResponse response) {
		/* 获取路径 */
		String uri = request.getUri();
		if(uri.indexOf("?")>-1) {
			uri = uri.substring(0,uri.indexOf("?"));
		}
		/* 只有html后缀的请求，才会被识别为控制层接口 */
		if(uri.endsWith(".html")) {
			
			/* 将请求丢给解释器 去解释，并调用对应的控制层方法进行处理 */
			ResolveRequest resolveRequest = ResolveRequest.getResolveRequest();
			Object result = resolveRequest.resolve(request);
			
			/*将控制层 返回的结果 返回给netty，让其响应给客户端*/
			return result;
		} else {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("error_code", 404);
			jsonObject.put("error_info", "只有.html结尾的请求，才会被识别为控制层接口");
			return jsonObject;
		}
	}
}
