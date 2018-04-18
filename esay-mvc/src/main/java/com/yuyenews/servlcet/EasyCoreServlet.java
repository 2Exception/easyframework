package com.yuyenews.servlcet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.yuyenews.config.Config;
import com.yuyenews.core.util.RequestUtil;
import com.yuyenews.easy.netty.constant.Constants;
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
	
	private static Logger log = LoggerFactory.getLogger(EasyCoreServlet.class);

	@Override
	public Object doRequest(HttpRequest request, HttpResponse response) {
		try {
			/* 获取路径 */
			String uri = RequestUtil.getUriName(request);
			/* 只有符合指定后缀的请求，才会被识别为控制层接口 */
			if(uri.endsWith(getHz())) {
				
				/* 将请求丢给解释器 去解释，并调用对应的控制层方法进行处理 */
				ResolveRequest resolveRequest = ResolveRequest.getResolveRequest();
				Object result = resolveRequest.resolve(request,response);
				
				/*将控制层 返回的结果 返回给netty，让其响应给客户端*/
				return result;
			} else {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("error_code", 404);
				jsonObject.put("error_info", "只有.html结尾的请求，才会被识别为控制层接口");
				return jsonObject;
			}
		} catch (Exception e) {
			log.error("解释请求的时候报错",e);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("error_code", 500);
		jsonObject.put("error_info", "解析请求报错");
		return jsonObject;
	}
	
	/**
	 * 控制层请求规则
	 * @return
	 */
	private String getHz() {

		Constants constants = Constants.getConstants();
		
		JSONObject jsonObject = Config.getConfig(constants);
		
		Object contrl = jsonObject.get("contrl");
		if(contrl != null) {
			return contrl.toString();
		}
		
		return ".html";
	}
}
