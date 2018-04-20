package com.yuyenews.ioc.factory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuyenews.aop.proxy.CglibProxy;
import com.yuyenews.aop.traction.TractionAop;
import com.yuyenews.core.annotation.EasyAop;
import com.yuyenews.core.annotation.Traction;
import com.yuyenews.easy.netty.constant.EasySpace;
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
			boolean hasTrac = false;
			boolean hasAopCls = false;
			
			/* 判断当前类中有没有方法有 aop注解 */
			Class<?> aopClass = null;
			Map<String,String> list = new HashMap<>();
			Method[] methods = className.getMethods();
			for(Method method : methods) {
				EasyAop easyAop = method.getAnnotation(EasyAop.class);
				Traction traction = method.getAnnotation(Traction.class);
				
				/* 校验同一个方法上不能同时存在aop和trac注解 */
				if(easyAop != null && traction != null) {
					log.error(className.getName()+"类中的["+method.getName()+"]方法同时存在EasyAop和Traction注解");
					throw new Exception(className.getName()+"类中的["+method.getName()+"]方法同时存在EasyAop和Traction注解");
				}
				
				if(easyAop != null) {
					hasAopCls = true;
					list.put(method.getName(),"yes");
					if(aopClass == null) {
						aopClass = easyAop.className();
					} else if(!aopClass.getName().equals(easyAop.className().getName())) {
						log.error(className.getName()+"类中的aop注解，className属性存在不同");
						throw new Exception(className.getName()+"类中的aop注解，className属性存在不同");
					}
				} else if(traction != null) {
					hasTrac = true;
					list.put(method.getName(),"yes");
					aopClass = TractionAop.class;
				}
			}
			
			/* 校验同一个类里面的方法只能用同一个aop注解 */
			if(hasAopCls && hasTrac) {
				log.error(className.getName()+"类中同时存在EasyAop和Traction注解");
				throw new Exception(className.getName()+"类中同时存在EasyAop和Traction注解");
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
			EasySpace constants = EasySpace.getEasySpace();
			
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
