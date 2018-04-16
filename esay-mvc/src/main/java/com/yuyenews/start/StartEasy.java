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
		load(clazz);
		
		/* 启动netty */
		EasyServer.start(clazz,port);
	}
	
	/**
	 * 加载控制层所有的类和所需数据
	 */
	private static void load(Class<?> clazz) {
		/* 获取全局存储空间 */
		Constants constants = Constants.getConstants();
		/* 配置核心servlet */
		constants.setAttr("core", EasyCoreServlet.class.getName());
		
		/*扫描所有的controller*/
		String className = clazz.getName();
		
		System.out.println(className);
	}

}
