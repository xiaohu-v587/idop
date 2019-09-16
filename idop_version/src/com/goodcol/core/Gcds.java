
package com.goodcol.core;

import java.util.List;
import javax.servlet.ServletContext;
import com.goodcol.config.Constants;
import com.goodcol.config.GcdsConfig;
import com.goodcol.handler.Handler;
import com.goodcol.handler.HandlerFactory;
import com.goodcol.kit.LogKit;
import com.goodcol.kit.PathKit;
import com.goodcol.plugin.IPlugin;
import com.goodcol.render.RenderManager;
import com.goodcol.server.JettyServerForIDEA;
import com.goodcol.server.IServer;
import com.goodcol.server.ServerFactory;
import com.goodcol.token.ITokenCache;
import com.goodcol.token.TokenManager;
import com.goodcol.upload.OreillyCos;

/**
 * Gcds
 */
public final class Gcds {
	
	private Constants constants;
	private ActionMapping actionMapping;
	private Handler handler;
	private ServletContext servletContext;
	private String contextPath = "";
	private static IServer server;
	
	private static final Gcds me = new Gcds();
	
	private Gcds() {
	}
	
	public static Gcds me() {
		return me;
	}
	
	boolean init(GcdsConfig gcdsConfig, ServletContext servletContext) {
		this.servletContext = servletContext;
		this.contextPath = servletContext.getContextPath();
		
		initPathUtil();
		
		Config.configGcds(gcdsConfig);	// start plugin, init log factory and init engine in this method
		constants = Config.getConstants();
		
		initActionMapping();
		initHandler();
		initRender();
		initOreillyCos();
		initTokenManager();
		
		return true;
	}
	
	private void initTokenManager() {
		ITokenCache tokenCache = constants.getTokenCache();
		if (tokenCache != null) {
			TokenManager.init(tokenCache);
		}
	}
	
	private void initHandler() {
		Handler actionHandler = new ActionHandler(actionMapping, constants);
		handler = HandlerFactory.getHandler(Config.getHandlers().getHandlerList(), actionHandler);
	}
	
	private void initOreillyCos() {
		OreillyCos.init(constants.getBaseUploadPath(), constants.getMaxPostSize(), constants.getEncoding());
	}
	
	private void initPathUtil() {
		String path = servletContext.getRealPath("/");
		PathKit.setWebRootPath(path);
	}
	
	private void initRender() {
		RenderManager.me().init(Config.getEngine(), constants, servletContext);
	}
	
	private void initActionMapping() {
		actionMapping = new ActionMapping(Config.getRoutes(), Config.getInterceptors());
		actionMapping.buildActionMapping();
		Config.getRoutes().clear();
	}
	
	void stopPlugins() {
		List<IPlugin> plugins = Config.getPlugins().getPluginList();
		if (plugins != null) {
			for (int i=plugins.size()-1; i >= 0; i--) {		// stop plugins
				boolean success = false;
				try {
					success = plugins.get(i).stop();
				} 
				catch (Exception e) {
					success = false;
					LogKit.error(e.getMessage(), e);
				}
				if (!success) {
					System.err.println("Plugin stop error: " + plugins.get(i).getClass().getName());
				}
			}
		}
	}
	
	Handler getHandler() {
		return handler;
	}
	
	public Constants getConstants() {
		return Config.getConstants();
	}
	
	public String getContextPath() {
		return contextPath;
	}
	
	public ServletContext getServletContext() {
		return this.servletContext;
	}
	
	public Action getAction(String url, String[] urlPara) {
		return actionMapping.getAction(url, urlPara);
	}
	
	public List<String> getAllActionKeys() {
		return actionMapping.getAllActionKeys();
	}
	
	public static void start() {
		server = ServerFactory.getServer();
		server.start();
	}
	
	/**
	 * 用于在 Eclipse 中，通过创建 main 方法的方式启动项目，支持执加载
	 */
	public static void start(String webAppDir, int port, String context, int scanIntervalSeconds) {
		server = ServerFactory.getServer(webAppDir, port, context, scanIntervalSeconds);
		server.start();
	}
	
	/**
	 * 用于在 IDEA 中，通过创建 main 方法的方式启动项目，不支持热加载
	 * 本方法存在的意义在于此方法启动的速度比 maven 下的 jetty 插件要快得多
	 * 
	 * 注意：不支持热加载。建议通过 Ctrl + F5 快捷键，来快速重新启动项目，速度并不会比 eclipse 下的热加载慢多少
	 *     实际操作中是先通过按 Alt + 5 打开 debug 窗口，才能按 Ctrl + F5 重启项目
	 */
	public static void start(String webAppDir, int port, String context) {
		server = new JettyServerForIDEA(webAppDir, port, context);
		server.start();
	}
	
	public static void stop() {
		server.stop();
	}
	
	/**
	 * Run   Server with Debug Configurations or Run Configurations in Eclipse or IDEA
	 * Example for Eclipse:	src/main/webapp 80 / 5
	 * Example for IDEA:	src/main/webapp 80 /
	 */
	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			server = ServerFactory.getServer();
			server.start();
			return ;
		}
		
		// for Eclipse
		if (args.length == 4) {
			String webAppDir = args[0];
			int port = Integer.parseInt(args[1]);
			String context = args[2];
			int scanIntervalSeconds = Integer.parseInt(args[3]);
			server = ServerFactory.getServer(webAppDir, port, context, scanIntervalSeconds);
			server.start();
			return ;
		}
		
		// for IDEA
		if (args.length == 3) {
			start(args[0], Integer.parseInt(args[1]), args[2]);
			return ;
		}
		
		throw new RuntimeException("Boot parameter error. The right parameter like this: src/main/webapp 80 / 5");
	}
}










