package com.yuyenews.easy.netty.sessionm;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.yuyenews.easy.netty.constant.EasySpace;
import com.yuyenews.easy.netty.request.HttpContext;
import com.yuyenews.easy.netty.request.HttpRequest;
import com.yuyenews.easy.netty.request.HttpSession;

/**
 * 管理session
 * 
 * @author yuye
 *
 */
public class SessionManager {
	
	private static HttpContext context = HttpContext.getHttpContext();
	
	/**
	 * 获取sessionId
	 * @return
	 */
	public static String getSessionId(HttpRequest request) {
		Object obj = request.getHeaders().get("sessionId");
		if(obj == null) {
			obj = request.getParemeter("sessionId");
		}
		if(obj == null) {
			return null;
		}
		return obj.toString();
	}
	
	/**
	 * 获取session
	 * @param request
	 * @param context
	 * @return
	 */
	public static HttpSession getHttpSession(HttpRequest request) {
		
		String sessionId = getSessionId(request);
		
		if(sessionId == null) {
			return null;
		}
		
		Map<String,HttpSession> sessions = getSessionList();
		
		HttpSession httpSession = sessions.get(sessionId);
		
		if(httpSession != null) {
			if(hasShiXiao(httpSession)) {
				/* 如果失效了，就remove掉 */
				sessions.remove(sessionId);
				
				httpSession = null;
			}
		}
		
		if(httpSession == null) {
			/* 如果没有这个id对应的session 就新建一个，并保存 */
			httpSession = new HttpSession();
			sessions.put(sessionId, httpSession);
			
			/* 保险起见，重新给值 */
			context.setAttr("session",sessions);
		} 
		
		return httpSession;
	}
	
	
	/**
	 * 获取session列表
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,HttpSession> getSessionList() {
		Map<String,HttpSession> sessions = null;
		
		Object sessionList = context.getAttr("session");
		if(sessionList != null) {
			sessions = (Map<String,HttpSession>)sessionList;
		} else {
			/* 如果session列表不存在，则新建一个session列表 */
			sessions = new Hashtable<>();
		}
		
		return sessions;
	}

	/**
	 * 判断session是否已经失效了
	 * @param httpSession
	 * @return
	 */
	private static boolean hasShiXiao(HttpSession httpSession) {
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
