package com.yuyenews.easy.netty.thread;

import java.lang.reflect.Method;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuyenews.easy.netty.constant.Constants;
import com.yuyenews.easy.netty.request.HttpRequest;
import com.yuyenews.easy.netty.request.HttpResponse;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * 处理请求的线程
 * @author yuye
 *
 */
public class RequestThread implements Runnable {

	/**
	 * netty的request对象
	 */
	private FullHttpRequest httpRequest;

	private ChannelHandlerContext ctx;
	
	/**
	 * body参数
	 */
	private String body;

	/**
	 * 请求数据（包含文件）
	 */
	private Map<String, Object> parms;

	public void setHttpRequest(FullHttpRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setParms(Map<String, Object> parms) {
		this.parms = parms;
	}

	public void run() {

		try {
			
			/* 获取全局存储空间 */
			Constants constants = Constants.getConstants();
			/* 从存储空间里获取核心servlet的全限名 */
			String className = constants.getAttr("core").toString();

			/* 获取请求的方式 */
			HttpMethod method = httpRequest.method();
			
			/* 获取路径 */
			String uri = httpRequest.uri();
			
			/* 组装httprequest对象 */
			HttpRequest request = new HttpRequest();
			request.setMethod(method);
			request.setParemeters(parms);
			request.setBody(body);
			request.setUri(uri);
			request.setUrl(uri);
			
			/* 通过反射执行核心servlet */
			Class<?> cls = Class.forName(className);
			Object object = cls.getDeclaredConstructor().newInstance();
			Method helloMethod = cls.getDeclaredMethod("doRequest", new Class[] { HttpRequest.class ,HttpResponse.class});
			Object result = helloMethod.invoke(object, new Object[] { request ,new HttpResponse()});
			
			/* 将控制层的数据，转成json字符串返回 */
			send(ctx, JSON.toJSONString(result), HttpResponseStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放请求
			httpRequest.release();
		}
	}

	/**
	 * 发送的返回值
	 * 
	 * @param ctx
	 *            返回
	 * @param context
	 *            消息
	 * @param status
	 *            状态
	 */
	private void send(ChannelHandlerContext ctx, String context, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
				Unpooled.copiedBuffer(context, CharsetUtil.UTF_8));
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}
}
