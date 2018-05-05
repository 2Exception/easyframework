package com.yuyenews.start;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.yuyenews.config.Config;
import com.yuyenews.core.load.LoadClass;
import com.yuyenews.core.util.ConfigUtil;
import com.yuyenews.easy.netty.server.EasyServer;
import com.yuyenews.easy.server.constant.EasySpace;
import com.yuyenews.ioc.load.LoadEasyBean;
import com.yuyenews.ioc.load.LoadSqlMappers;
import com.yuyenews.resolve.LoadController;
import com.yuyenews.servlcet.EasyCoreServlet;

/**
 * 启动easy框架
 * @author yuye
 *
 */
public class StartEasy {
	
	private static Logger log = LoggerFactory.getLogger(StartEasy.class);
	
	/**
	 * 获取全局存储空间 
	 */
	private static EasySpace constants = EasySpace.getEasySpace();

	/**
	 * 启动easy框架
	 * @param clazz
	 */
	public static void start(Class<?> clazz) {
		try {
			/* 加载框架数据 */
			load(clazz);

			/* 启动netty */
			EasyServer.start(getPort());
		} catch (Exception e) {
			log.error("",e);
		}
	}
	
	/**
	 * 加载控制层所有的类和所需数据
	 */
	private static void load(Class<?> clazz) {
		
		/* 配置核心servlet */
		constants.setAttr("core", EasyCoreServlet.class.getName());
		
		/* 加载配置文件 */
		Config.loadConfig(constants);
		
		/*获取要扫描的包*/
		String className = clazz.getName();
		className = className.substring(0,className.lastIndexOf("."));
		
		/* 将要扫描的包名存到全局存储空间，给别的需要的地方使用 */
		constants.setAttr("rootPath", className);
		
		/* 获取此包下面的所有类（包括jar中的） */
		LoadClass.loadBeans(className);
		
		/* 创建所有的dao对象 */
		LoadSqlMappers.loadMapper(constants);
		
		/* 创建bean对象 */
		LoadEasyBean.loadBean(constants);
		
		/* 创建controller对象 */
		LoadController.loadContrl(constants);
		
		/* 加载sqlMapper文件 */
		loadSqlMappers();
	}
	
	/**
	 * 获取端口号，默认8080
	 * @return
	 */
	private static int getPort() {
		int port = 8088;
	
		JSONObject jsonObject = ConfigUtil.getConfig(constants);
		Object por = jsonObject.get("port");
		if(por!=null) {
			port = Integer.parseInt(por.toString());
		}
		
		return port;
	}

	/**
	 * 加载sqlMapper
	 */
	private static void loadSqlMappers() {
		try {
			Class<?> cls = Class.forName("com.yuyenews.easy.init.InitSqlMapper");
			Object object = cls.getDeclaredConstructor().newInstance();
			Method helloMethod = cls.getDeclaredMethod("readSqlMapper");
			helloMethod.invoke(object);
		} catch (Exception e) {
		}
	}
}
