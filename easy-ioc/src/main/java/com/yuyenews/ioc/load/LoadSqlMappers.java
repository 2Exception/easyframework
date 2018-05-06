package com.yuyenews.ioc.load;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuyenews.core.annotation.EasyBean;
import com.yuyenews.core.util.ConfigUtil;
import com.yuyenews.core.util.ReadClass;
import com.yuyenews.core.util.StringUtil;
import com.yuyenews.easy.server.constant.EasySpace;
import com.yuyenews.ioc.load.model.EasyBeanModel;

/**
 * 加载所有的dao对象
 * @author yuye
 *
 */
public class LoadSqlMappers {
	
	private static Logger log = LoggerFactory.getLogger(LoadSqlMappers.class);

	/**
	 * 创建easybean对象，并完成对象注入
	 * @param constants
	 */
	@SuppressWarnings({ "unchecked" })
	public static void loadMapper(EasySpace constants) {
		try {
			
			Set<String> classes = ReadClass.loadClassList(getDaosPath(constants));
			
			/* 创建bean对象，并保存起来 */
			Object objs2 = constants.getAttr("easyBeanObjs");
			Map<String,EasyBeanModel> easyBeanObjs = new HashMap<>();
			if(objs2 != null) {
				easyBeanObjs = (Map<String,EasyBeanModel>)objs2;
			} 
			for(String className : classes) {
				
				Class<?> cls = Class.forName(className);
				
				EasyBean easyBean = cls.getAnnotation(EasyBean.class);
				if(easyBean != null) {
					throw new Exception("Dao 不允许加EasyBean注解，相关Dao["+cls.getName()+"]");
				}
				String beanName = StringUtil.getFirstLowerCase(cls.getSimpleName());
				EasyBeanModel beanModel = new EasyBeanModel();
				beanModel.setName(beanName);
				beanModel.setCls(cls);
				beanModel.setObj(getSqlPoryObject(cls));
				easyBeanObjs.put(beanName, beanModel);
				
			}
			constants.setAttr("easyBeanObjs", easyBeanObjs);
		} catch (Exception e) {
			log.error("加载并注入dao的时候出现错误",e);
		} 
	}
	
	/**
	 * 通过代理获得dao对象
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	private static Object getSqlPoryObject(Class<?> cls) throws Exception {
		/* 这个类在 整合mybatis的中间包里，所以这里要用反射的方式来调用，不然无法散耦 */
		Class<?> sqlPory = Class.forName("com.yuyenews.easy.porxy.SqlProxy");
		Object object = sqlPory.getDeclaredConstructor().newInstance();
		Method helloMethod = sqlPory.getDeclaredMethod("getProxy", new Class[] { Class.class});
		Object result = helloMethod.invoke(object, new Object[] { cls });
		return result;
	}
	
	/**
	 * 获取dao所在的包
	 * @param constants
	 * @return
	 */
	private static String getDaosPath(EasySpace constants) {
		JSONObject jsonObject = ConfigUtil.getConfig(constants);
		if(jsonObject != null) {
			Object jdbc = jsonObject.get("jdbc");
			if(jdbc != null) {
				JSONObject jdbc2 = JSONObject.parseObject(JSON.toJSONString(jdbc));
				Object daos = jdbc2.get("daos");
				if(!StringUtil.isNull(daos)) {
					return daos.toString();
				}
			}
		}
		return null;
	}
}
