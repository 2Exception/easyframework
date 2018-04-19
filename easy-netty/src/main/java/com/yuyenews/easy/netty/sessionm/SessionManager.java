package com.yuyenews.easy.netty.sessionm;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.yuyenews.easy.netty.constant.EasySpace;
import com.yuyenews.easy.netty.request.HttpSession;

/**
 * 管理session的失效时间
 * 
 * @author yuye
 *
 */
public class SessionManager {

	/**
	 * 判断session是否已经失效了
	 * @param httpSession
	 * @return
	 */
	public static boolean hasShiXiao(HttpSession httpSession) {
		long mil = httpSession.getDate().getTime();
		long nmil = new Date().getTime();

		JSONObject jsonObject = getConfig();

		Object sessionTimeo = jsonObject.get("sessionTime");
		/* 默认一个小时失效 */
		Long sessionTime = 3600000L;
		if (sessionTimeo != null) {
			sessionTime = Long.parseLong(sessionTimeo.toString());
		}

		long cha = nmil - mil;
		if (cha < 0) {
			cha = cha * -1;
		}
		if (cha > sessionTime) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 获取配置文件
	 * 
	 * @return
	 */
	private static JSONObject getConfig() {
		EasySpace constants = EasySpace.getEasySpace();
		Object obj = constants.getAttr("config");
		if (obj != null) {
			JSONObject jsonObject = (JSONObject) obj;
			return jsonObject;
		}

		return new JSONObject();
	}
}
