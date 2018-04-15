package com.yuyenews.start;

import com.yuyenews.easy.netty.constant.Constants;
import com.yuyenews.easy.netty.server.EasyServer;
import com.yuyenews.servlcet.EasyCoreServlet;

public class StartEasy {

	/**
	 * 启动easy框架
	 * @param clazz
	 */
	public static void start(Class<?> clazz,int port) {
		/* 加载框架数据 */
		Constants constants = Constants.getConstants();
		constants.setAttr("core", EasyCoreServlet.class.getName());
		
		/* 启动netty */
		EasyServer.start(clazz,port);
	}
}
