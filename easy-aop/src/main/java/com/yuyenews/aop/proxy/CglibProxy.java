package com.yuyenews.aop.proxy;

import java.lang.reflect.Method;
import java.util.List;

import com.yuyenews.core.util.MatchUtil;

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
	
	private List<String> list;

	/**
	 * 获取代理对象
	 * @param clazz  bean的class
	 * @param cl aop类的class
	 * @return
	 */
	public Object getProxy(Class<?> clazz,Class<?> cl,List<String> list) {
		
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
		for(String rulm : list){
			Boolean boolean1 = MatchUtil.isMatch(rulm, method.getName());
			if(boolean1){
				isProxy = true;
				obj = c.newInstance();
			}
		}
		if(isProxy){
			Method m2 = c.getDeclaredMethod("startMethod",new Class[] {Object[].class}); 
			m2.invoke(obj,new Object[] {args});
		}
		
		Object o1 = methodProxy.invokeSuper(o, args);
		
		if(isProxy){
			Method m3 = c.getDeclaredMethod("endMethod",new Class[] {Object[].class}); 
			m3.invoke(obj,new Object[] {args});
		}
		return o1;
	}

}
