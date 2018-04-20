package com.yuyenews.aop.proxy;

import java.lang.reflect.Method;
import java.util.Map;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 代理类
 * @author yuye
 *
 */
public class CglibProxy implements MethodInterceptor {

	private Enhancer enhancer;
	
	private Class<?> c;
	
	private Map<String,String> list;

	/**
	 * 获取代理对象
	 * @param clazz  bean的class
	 * @param cl aop类的class
	 * @return
	 */
	public Object getProxy(Class<?> clazz,Class<?> cl,Map<String,String> list) {
		
		this.c = cl;
		this.list = list;
		enhancer = new Enhancer();
		// 设置需要创建子类的类
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(this);
		// 通过字节码技术动态创建子类实例
		return enhancer.create();
	}
	
	/**
	 * 绑定代理
	 */
	@Override
	public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		Boolean isProxy = false;
		Object obj = null;
		String str = list.get(method.getName());
		if(str != null && str.equals("yes")){
			isProxy = true;
			obj = c.getDeclaredConstructor().newInstance();
		}
		
		if(isProxy){
			Method m2 = c.getDeclaredMethod("startMethod",new Class[] {Object[].class}); 
			m2.invoke(obj,new Object[] {args});
		}
		
		Object o1 = null;
		try {
			o1 = methodProxy.invokeSuper(o, args);
			
			if(isProxy){
				Method m3 = c.getDeclaredMethod("endMethod",new Class[] {Object[].class}); 
				m3.invoke(obj,new Object[] {args});
			}
		} catch (Exception e) {
			if(isProxy){
				Method m4 = c.getDeclaredMethod("exp"); 
				m4.invoke(obj);
			}
		}
		return o1;
	}

}
