
package com.goodcol.core;

import java.util.List;
import com.goodcol.config.Constants;
import com.goodcol.config.GcdsConfig;
import com.goodcol.config.Routes;
import com.goodcol.config.Plugins;
import com.goodcol.config.Handlers;
import com.goodcol.config.Interceptors;
import com.goodcol.kit.PathKit;
import com.goodcol.log.Log;
import com.goodcol.log.LogManager;
import com.goodcol.plugin.IPlugin;
import com.goodcol.template.Engine;

class Config {
	
	private static final Constants constants = new Constants();
	private static final Routes routes = new Routes(){public void config() {}};
	private static final Engine engine = new Engine("Gcds Web");
	private static final Plugins plugins = new Plugins();
	private static final Interceptors interceptors = new Interceptors();
	private static final Handlers handlers = new Handlers();
	private static Log log;
	
	// prevent new Config();
	private Config() {
	}
	
	/*
	 * Config order: constant, route, engine, plugin, interceptor, handler
	 */
	static void configGcds(GcdsConfig gcdsConfig) {
		gcdsConfig.configConstant(constants);			initLogFactory();	initEngine();
		gcdsConfig.configRoute(routes);
		gcdsConfig.configEngine(engine);
		gcdsConfig.configPlugin(plugins);				startPlugins();		// very important!!!
		gcdsConfig.configInterceptor(interceptors);
		gcdsConfig.configHandler(handlers);
	}
	
	/**
	 * Set the default base template path and devMode by Gcds before configEngine(engine) invoked
	 * They can be reconfigured in configEngine(engine)
	 */
	private static void initEngine() {
		engine.setDevMode(constants.getDevMode());
		engine.setBaseTemplatePath(PathKit.getWebRootPath());
	}
	
	public static final Constants getConstants() {
		return constants;
	}
	
	public static final Routes getRoutes() {
		return routes;
	}
	
	public static final Engine getEngine() {
		return engine;
	}
	
	public static final Plugins getPlugins() {
		return plugins;
	}
	
	public static final Interceptors getInterceptors() {
		return interceptors;
	}
	
	public static Handlers getHandlers() {
		return handlers;
	}
	
	private static void startPlugins() {
		List<IPlugin> pluginList = plugins.getPluginList();
		if (pluginList == null) {
			return ;
		}
		
		for (IPlugin plugin : pluginList) {
			try {
				// process ActiveRecordPlugin devMode
				if (plugin instanceof com.goodcol.plugin.activerecord.ActiveRecordPlugin) {
					com.goodcol.plugin.activerecord.ActiveRecordPlugin arp = (com.goodcol.plugin.activerecord.ActiveRecordPlugin)plugin;
					if (arp.getDevMode() == null) {
						arp.setDevMode(constants.getDevMode());
					}
				}
				
				if (plugin.start() == false) {
					String message = "Plugin start error: " + plugin.getClass().getName();
					log.error(message);
					throw new RuntimeException(message);
				}
			}
			catch (Exception e) {
				String message = "Plugin start error: " + plugin.getClass().getName() + ". \n" + e.getMessage();
				log.error(message, e);
				throw new RuntimeException(message, e);
			}
		}
	}
	
	private static void initLogFactory() {
		LogManager.me().init();
		log = Log.getLog(Config.class);
		GcdsFilter.initLog();
	}
}
