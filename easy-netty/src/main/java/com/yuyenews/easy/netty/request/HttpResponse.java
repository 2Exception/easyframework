package com.yuyenews.easy.netty.request;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuyenews.easy.netty.constant.EasySpace;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * 响应对象，对netty原生response的扩展
 * 
 * 暂时没有提供response的支持
 * 
 * @author yuye
 *
 */
public class HttpResponse {

	private ChannelHandlerContext ctx;
	
	/**
	 * 构造函数，框架自己用的，程序员用不到，用了也没意义
	 * 
	 * @param httpRequest
	 */
	public HttpResponse(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	/**
	 * 设置请求头
	 * 
	 * @param key
	 * @param value
	 */
	@Deprecated
	public void setHeader(String key, String value) {
	}

	/**
	 * 响应数据
	 * 
	 * @param context
	 *            消息
	 * @param status
	 *            状态
	 */
	public void send(String context, HttpResponseStatus status) {
		send(context, null, status);
	}

	/**
	 * 响应数据
	 * 
	 * @param context
	 *            消息
	 */
	public void send(String context) {
		send(context, null, HttpResponseStatus.OK);
	}

	/**
	 * 响应数据
	 * 
	 * @param context
	 *            消息
	 * @param header
	 *            头信息
	 */
	public void send(String context, Map<String, String> header) {
		send(context, header, HttpResponseStatus.OK);
	}

	/**
	 * 响应数据
	 * 
	 * @param context
	 *            消息
	 * @param header
	 *            头信息
	 * @param status
	 *            状态
	 */
	public void send(String context, Map<String, String> header, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
				Unpooled.copiedBuffer(context, CharsetUtil.UTF_8));

		crossDomain(response);

		if (header != null) {
			for (String key : header.keySet()) {
				response.headers().set(key, header.get(key));
			}
		}
		
		Object contentType = getConfig().get("content_type");
		if(contentType == null) {
			contentType = "text/json; charset=UTF-8";
		}
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType.toString());
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * 设置跨域
	 */
	private void crossDomain(FullHttpResponse response) {
		JSONObject jsonObject = getConfig();
		Object object = jsonObject.get("cross_domain");
		if (object != null) {
			JSONObject ob = JSONObject.parseObject(JSON.toJSONString(object));

			response.headers().set("Access-Control-Allow-Origin", ob.get("origin").toString());
			response.headers().set("Access-Control-Allow-Methods", ob.get("methods").toString());
			response.headers().set("Access-Control-Max-Age", "3600");
			response.headers().set("Access-Control-Allow-Headers", "x-requested-with,Cache-Control,Pragma,Content-Type,Token");
			response.headers().set("Access-Control-Allow-Credentials", "true");
		}
	}

	/**
	 * 获取配置文件
	 * 
	 * @return
	 */
	private JSONObject getConfig() {
		EasySpace constants = EasySpace.getEasySpace();
		Object obj = constants.getAttr("config");
		if (obj != null) {
			JSONObject jsonObject = (JSONObject) obj;
			return jsonObject;
		}

		return new JSONObject();
	}
}
