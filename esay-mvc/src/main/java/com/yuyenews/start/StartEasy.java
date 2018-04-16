package com.yuyenews.start;

import com.alibaba.fastjson.JSONObject;
import com.yuyenews.config.Config;
import com.yuyenews.easy.netty.constant.Constants;
import com.yuyenews.easy.netty.server.EasyServer;
import com.yuyenews.resolve.LoadController;
import com.yuyenews.servlcet.EasyCoreServlet;

/**
 * 启动easy框架
 * @author yuye
 *
 */
public class StartEasy {
	
	/**
	 * 获取全局存储空间 
	 */
	private static Constants constants = Constants.getConstants();

	/**
	 * 启动easy框架
	 * @param clazz
	 */
	public static void start(Class<?> clazz) {
		/* 加载框架数据 */
		load(clazz);

		/* 启动netty */
		EasyServer.start(clazz,getPort());
	}
	
	/**
	 * 加载控制层所有的类和所需数据
	 */
	private static void load(Class<?> clazz) {
		
		/* 配置核心servlet */
		constants.setAttr("core", EasyCoreServlet.class.getName());
		
		/* 加载配置文件 */
		Config.loadConfig(constants);
		
		/*扫描所有的controller 并完成服务层的注入*/
		String className = clazz.getName();
		className = className.substring(0,className.lastIndexOf("."));
		
		/* 将要扫描的包名存到全局存储空间，给别的需要的地方使用 */
		constants.setAttr("rootPath", className);
		
		/* 扫描此包下，包括子包的所有的类 */
		LoadController.loadContrl(className);
		
	}
	
	/**
	 * 获取端口号，默认8080
	 * @return
	 */
	private static int getPort() {
		int port = 8080;
		Object obj = constants.getAttr("config");
		if(obj != null) {
			JSONObject jsonObject = (JSONObject)obj;
			Object por = jsonObject.get("port");
			if(por!=null) {
				port = Integer.parseInt(por.toString());
			}
		}
		return port;
	}

}
