package com.yuyenews.easy.netty.request;

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
 * 响应对象，对netty元素response的扩展
 * 
 * 暂时没有提供response的支持
 * @author yuye
 *
 */
public class HttpResponse {

	private FullHttpResponse response;
	
	/**
	 * 构造函数，框架自己用的，程序员用不到，用了也没意义
	 * @param httpRequest
	 */
	public HttpResponse(FullHttpResponse response) {
		this.response = response;
	}
	
	/**
	 * 设置请求头
	 * @param key
	 * @param value
	 */
	public void setHeader(String key,String value) {
		response.headers().set(key,value);
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
	public void send(ChannelHandlerContext ctx, String context, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
				Unpooled.copiedBuffer(context, CharsetUtil.UTF_8));
		
		this.response = response;
		
		crossDomain();
		
		//responseHeaders();
		
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}
	
	/**
	 * 设置跨域
	 */
	private void crossDomain() {
		JSONObject jsonObject = getConfig();
		Object object = jsonObject.get("cross_domain");
		if(object != null) {
			JSONObject ob = JSONObject.parseObject(JSON.toJSONString(object));
			
			response.headers().set("Access-Control-Allow-Origin",ob.get("origin").toString());  
			response.headers().set("Access-Control-Allow-Methods", ob.get("methods").toString());  
			response.headers().set("Access-Control-Max-Age","3600");  
			response.headers().set("Access-Control-Allow-Headers","x-requested-with,Cache-Control,Pragma,Content-Type,Token");  
			response.headers().set("Access-Control-Allow-Credentials","true");  
		}
	}
	
	/**
	 * 设置响应头
	 * 
	 * 暂时不用
	 */
	/*private void responseHeaders() {
		JSONObject jsonObject = getConfig();
		Object object = jsonObject.get("response_header");
		if(object != null) {
			JSONArray ob = JSONArray.parseArray(JSON.toJSONString(object));
			if(ob != null) {
				for(int i=0;i<ob.size();i++) {
					JSONObject jso = ob.getJSONObject(0);
					response.headers().set(jso.getString("name"),jso.getString("value"));  
				}
			}
		}
	}*/
	
	/**
	 * 获取配置文件
	 * @return
	 */
	private JSONObject getConfig() {
		EasySpace constants = EasySpace.getEasySpace();
		Object obj = constants.getAttr("config");
		if(obj != null) {
			JSONObject jsonObject = (JSONObject)obj;
			return jsonObject;
		}
		
		return new JSONObject();
	}
}
