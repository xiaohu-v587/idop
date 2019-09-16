package com.goodcol.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.goodcol.util.ParamContainer;

/**
 * 数据字段监听器
 * @author king
 */
public class ParamListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ParamContainer container = new ParamContainer();
		container.init();
	}
	

}
