package com.yuyenews.ioc.factory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuyenews.aop.proxy.CglibProxy;
import com.yuyenews.core.annotation.EasyAop;
import com.yuyenews.easy.netty.constant.Constants;
import com.yuyenews.ioc.load.model.EasyBeanModel;

/**
 * bean工厂
 * @author yuye
 *
 */
public class BeanFactory {
	
	private static Logger log = LoggerFactory.getLogger(BeanFactory.class);
	
	/**
	 * 创建bean
	 * @param className
	 * @return
	 */
	public static Object createBean(Class<?> className) {
		try {
			/* 判断当前类中有没有方法有 aop注解 */
			Class<?> aopClass = null;
			List<String> list = new ArrayList<>();
			Method[] methods = className.getMethods();
			for(Method method : methods) {
				EasyAop easyAop = method.getAnnotation(EasyAop.class);
				if(easyAop != null) {
					list.add(method.getName());
					aopClass = easyAop.className();
				}
			}
			
			/* 如果有aop注解，则通过动态代理来创建bean */
			if(list != null && list.size()>0) {
				CglibProxy cglibProxy = new CglibProxy();
				return cglibProxy.getProxy(className,aopClass, list);
			} else {
				/* 如果没有aop注解，则直接new一个bean */
				return className.getDeclaredConstructor().newInstance();
			}
			
		} catch (Exception e) {
			log.error("创建["+className.getName()+"]类型的bean对象出现错误",e);
		} 
		return null;
	}

	/**
	 * 获取bean
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object getBean(String name) {
		
		try {
			Constants constants = Constants.getConstants();
			
			Object objs2 = constants.getAttr("easyBeanObjs");
			Map<String,EasyBeanModel> easyBeanObjs = null;
			if(objs2 != null) {
				easyBeanObjs = (Map<String,EasyBeanModel>)objs2;
			} else {
				easyBeanObjs = new HashMap<>();
			}
			
			return easyBeanObjs.get(name).getObj();
		} catch (Exception e) {
			log.error("找不到name为["+name+"]的bean",e);
		}
		
		return null;
	}
}
