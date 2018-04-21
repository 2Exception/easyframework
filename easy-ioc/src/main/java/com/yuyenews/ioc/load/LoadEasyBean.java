package com.yuyenews.ioc.load;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuyenews.core.annotation.EasyBean;
import com.yuyenews.core.annotation.Resource;
import com.yuyenews.easy.server.constant.EasySpace;
import com.yuyenews.ioc.factory.BeanFactory;
import com.yuyenews.ioc.load.model.EasyBeanModel;

/**
 * 加载easyBean
 * @author yuye
 *
 */
public class LoadEasyBean {
	
	private static Logger log = LoggerFactory.getLogger(LoadEasyBean.class);

	/**
	 * 创建easybean对象，并完成对象注入
	 * @param constants
	 */
	@SuppressWarnings({ "unchecked" })
	public static void loadBean(EasySpace constants) {
		try {
			/* 获取所有的bean数据 */
			Object objs = constants.getAttr("easyBeans");
			List<Map<String,Object>> contorls = null;
			if(objs != null) {
				contorls = (List<Map<String,Object>>)objs;
			} else {
				return;
			}
			
			
			/* 创建bean对象，并保存起来 */
			Object objs2 = constants.getAttr("easyBeanObjs");
			Map<String,EasyBeanModel> easyBeanObjs = null;
			if(objs2 != null) {
				easyBeanObjs = (Map<String,EasyBeanModel>)objs2;
			} else {
				easyBeanObjs = new HashMap<>();
			}
			for(Map<String,Object> map : contorls) {
				
				Class<?> cls = (Class<?>)map.get("className");
				EasyBean easyBean = (EasyBean)map.get("annotation");
				
				EasyBeanModel beanModel = new EasyBeanModel();
				beanModel.setName(easyBean.name());
				beanModel.setCls(cls);
				beanModel.setObj(BeanFactory.createBean(cls));
				easyBeanObjs.put(easyBean.name(), beanModel);
				
			}
			/* 注入对象 */
			iocBean(constants,easyBeanObjs);
		} catch (Exception e) {
			log.error("加载并注入EasyBean的时候出现错误",e);
		} 
	}
	
	/**
	 * easyBean注入
	 * @param constants
	 * @param easyBeanObjs
	 */
	private static void iocBean(EasySpace constants,Map<String,EasyBeanModel> easyBeanObjs) {
		
		try {
			for(String key : easyBeanObjs.keySet()) {
				EasyBeanModel easyBeanModel = easyBeanObjs.get(key);
				Object obj = easyBeanModel.getObj();
				Class<?> cls = easyBeanModel.getCls();
				/* 获取对象属性，完成注入 */
				Field[] fields = cls.getDeclaredFields();
				for(Field f : fields){
					Resource resource = f.getAnnotation(Resource.class);
					if(resource!=null){
						f.setAccessible(true);
						
						EasyBeanModel beanModel = easyBeanObjs.get(resource.name());
						if(beanModel!=null){
							f.set(obj, beanModel.getObj());
							log.info(cls.getName()+"的属性"+f.getName()+"注入成功");
						}else{
							throw new Exception("不存在name为"+resource.name()+"的easyBean");
						}
					}
				}
				/* 保险起见，重新插入数据 */
				easyBeanModel.setCls(cls);
				easyBeanObjs.put(key, easyBeanModel);
			}
			
			constants.setAttr("easyBeanObjs", easyBeanObjs);
		} catch (Exception e) {
			log.error("加载并注入EasyBean的时候出现错误",e);
		} 
	}
}
