package com.yuyenews.config;

import com.alibaba.fastjson.JSONObject;
import com.yuyenews.core.util.FileUtil;
import com.yuyenews.easy.netty.constant.Constants;

/**
 * 加载配置数据
 * @author yuye
 *
 */
public class Config {

	/**
	 * 加载配置文件
	 */
	public static void loadConfig(Constants constants) {
		/* TODO(需要用特殊办法获取路径，先读物理路径，如果读不到在读jar包里的文件) */
		String content = FileUtil.readFileString("");
		
		JSONObject object = JSONObject.parseObject(content);
		
		constants.setAttr("config", object);
	}
}
