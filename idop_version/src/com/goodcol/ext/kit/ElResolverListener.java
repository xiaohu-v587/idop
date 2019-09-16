
package com.goodcol.ext.kit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.goodcol.plugin.activerecord.ModelRecordElResolver;

/**
 * 针对 weblogic 等部分容器无法 register ModelRecordElResolver 增强对象的情况，
 * 添加此 Listern 到 web.xml 即可解决
 * 
 * 用法，在 web.xml 中添加 ElResolverListener 的配置如下：
 * <listener>
 * 		<listener-class>com.goodcol.ext.kit.ElResolverListener</listener-class>
 * </listener>
 */
public class ElResolverListener implements ServletContextListener {
	
	public void contextInitialized(ServletContextEvent sce) {
		ModelRecordElResolver.init(sce.getServletContext());
	}
	
	public void contextDestroyed(ServletContextEvent sce) {
		
	}
}



