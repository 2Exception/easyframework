package com.yuyenews.easy.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty服务
 * @author yuye
 *
 */
public class EasyServer {

	
	/**
	 * 启动netty服务
	 * @param clazz
	 * @param portNumber
	 */
	public static void start(Class<?> clazz,final int portNumber) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup);
			b.channel(NioServerSocketChannel.class);
			b.childHandler(new EasyServerInitializer());

			/* 服务器绑定端口监听 */
			ChannelFuture f = b.bind(portNumber).sync();
			
			System.out.println("启动成功");
			
			/* 监听服务器关闭监听 */
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
