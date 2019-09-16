 package com.goodcol.util.ext.plugin;

import java.util.List;



import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.core.config.Routes;
import com.goodcol.core.core.Controller;

/**
 * Routes 工具类 自动绑定Controller
 */
public class MyRoutesUtil{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void add(Routes me){
		List<Class> list= ClassSearcher.findClasses();
		if(list!=null&&list.isEmpty()==false){
			for(Class clz:list){
				RouteBind rb=(RouteBind)clz.getAnnotation(RouteBind.class);
				if(rb!=null){
					me.add(rb.path(),clz,rb.viewPath());
				}else if(clz.getSuperclass()!=null){
					if(clz.getSuperclass()==Controller.class||clz.getSuperclass().getSuperclass()==Controller.class){
						me.add("/"+clz.getSimpleName().replace("Controller", "").toLowerCase(),clz);
					}
				}
			}
		}
	}
}
