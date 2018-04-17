package com.yuyenews.core.load;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yuyenews.core.annotation.Controller;
import com.yuyenews.core.annotation.EasyBean;
import com.yuyenews.core.util.ReadClass;
import com.yuyenews.easy.netty.constant.Constants;

/**
 * 获取项目中的所有class
 * 
 * @author yuye
 *
 */
public class LoadClass {
	
	private static Constants constants = Constants.getConstants();

	/**
	 * 加载所有bean，包括controller 的class对象
	 * @param packageName
	 */
	public static void loadBeans(String packageName) {

		try {
			Set<String> classList = ReadClass.loadClassList(packageName);
			for (String str : classList) {
				Class<?> cls = Class.forName(str);
				Controller controller = cls.getDeclaredAnnotation(Controller.class);
				EasyBean easyBean = cls.getAnnotation(EasyBean.class);
				if(controller != null) {
					loadController(cls, controller);
				}
				if(easyBean != null) {
					loadEasyBean(cls, easyBean);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 将所有controller存到全局存储空间
	 * @param cls
	 * @param controller
	 */
	@SuppressWarnings("unchecked")
	private static void loadController(Class<?> cls,Controller controller) {
		Object objs = constants.getAttr("contorls");
		List<Map<String,Object>> contorls = null;
		if(objs != null) {
			contorls = (List<Map<String,Object>>)objs;
		} else {
			contorls = new ArrayList<>();
		}
		Map<String,Object> contorl = new HashMap<>();
		contorl.put("className", cls);
		contorl.put("annotation", controller);
		contorls.add(contorl);
		constants.setAttr("contorls", contorls);
	}
	
	/**
	 * 将所有easybean存到全局存储空间
	 * @param cls
	 * @param controller
	 */
	@SuppressWarnings("unchecked")
	private static void loadEasyBean(Class<?> cls,EasyBean easyBean ) {
		Object objs = constants.getAttr("easyBeans");
		List<Map<String,Object>> contorls = null;
		if(objs != null) {
			contorls = (List<Map<String,Object>>)objs;
		} else {
			contorls = new ArrayList<>();
		}
		Map<String,Object> eb = new HashMap<>();
		eb.put("className", cls);
		eb.put("annotation", easyBean);
		contorls.add(eb);
		constants.setAttr("easyBeans", eb);
		
	}
	
	public static void main(String[] args) {
		loadBeans("com.yuyenews");
		System.out.println(constants.getAttr("contorls"));
	}
}
