package com.goodcol.util.activiti;

import javax.sql.DataSource;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;

import com.alibaba.druid.util.JdbcConstants;
import com.goodcol.core.plugin.IPlugin;
import com.goodcol.core.plugin.activerecord.IDataSourceProvider;
import com.goodcol.util.PropertiesContent;

public class ActivitiPlugins implements IPlugin {

	@Override
	public boolean start() {
		// TODO Auto-generated method stub
		if (isStarted) {
			return true;
		}
		System.out.println("start activiti...");
		processEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
				.setDatabaseType(JdbcConstants.ORACLE)
				.setDataSource(dataSourceProvider.getDataSource())
				.setTransactionsExternallyManaged(true) // 使用托管事务工厂
				.setActivityFontName("宋体")
				.setLabelFontName("宋体")
				.setAnnotationFontName("宋体")
				.setDatabaseSchema(PropertiesContent.get("jdbc.username").toUpperCase())
				.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);// 数据库中没有表则创建表
		buildProcessEngine();//开启流程引擎
		// processEngine.getRepositoryService();
		// configuration
		//System.out.println("流程引擎:" +buildProcessEngine());
		// System.out.println("流程部署服务："+processEngine.getRepositoryService());
		isStarted = true;
		//ProcessEngines.init();//开启流程引擎
		System.out.println("start activiti success");
		return isStarted;
	}

	@Override
	public boolean stop() {
		// TODO Auto-generated method stub
		ProcessEngines.destroy();// 关闭流程引擎
		isStarted = false;
		return true;
	}

	// 开启流程服务引擎
	public static ProcessEngine buildProcessEngine() {
		if (processEngine == null)
			if (processEngineConfiguration != null) {
				processEngine = processEngineConfiguration.buildProcessEngine();
			}
		return processEngine;
	}

	private static ProcessEngine processEngine = null;

	private static ProcessEngineConfiguration processEngineConfiguration = null;
	// 基本属性 url、user、password
	private IDataSourceProvider dataSourceProvider = null;

	private boolean isStarted = false;

	public ActivitiPlugins(IDataSourceProvider dataSourceProvider) {
		this.dataSourceProvider = dataSourceProvider;
	}

	// get/set方法

	public IDataSourceProvider getDataSourceProvider() {
		return dataSourceProvider;
	}

	public void setDataSourceProvider(IDataSourceProvider dataSourceProvider) {
		this.dataSourceProvider = dataSourceProvider;
	}

}
