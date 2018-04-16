package com.yuyenews.easy.netty.server;

import java.net.InetAddress;

import com.yuyenews.easy.netty.thread.RequestThread;
import com.yuyenews.easy.netty.thread.ThreadPool;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class EasyServerHandler extends ChannelHandlerAdapter {

	/**
	 * 接收并处理 客户端请求
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (!(msg instanceof FullHttpRequest)) {
			send(ctx, "未知请求!", HttpResponseStatus.BAD_REQUEST);
			return;
		}
		FullHttpRequest httpRequest = (FullHttpRequest) msg;
		
		try {
			
			/* 用新线程处理请求 */
			RequestThread requestThread = new RequestThread();
			requestThread.setHttpRequest(httpRequest);
			requestThread.setCtx(ctx);
			ThreadPool.execute(requestThread);
			
		} catch (Exception e) {
			System.out.println("处理请求失败!");
			e.printStackTrace();
			/* 已经通过线程中的finally 释放请求了，所以这里，在出异常的时候，才释放*/
			try {
				httpRequest.release();
			} catch (Exception e2) {
			}
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

	/**
	 * 建立连接时，返回消息
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//System.out.println("连接的客户端地址:" + ctx.channel().remoteAddress());
		ctx.writeAndFlush("客户端" + InetAddress.getLocalHost().getHostName() + "成功与服务端建立连接！ ");
		super.channelActive(ctx);
	}
}
