
package com.goodcol.render;

import java.io.File;
import java.util.Locale;
import javax.servlet.ServletContext;
import com.goodcol.config.Constants;
import com.goodcol.kit.LogKit;
import com.goodcol.kit.PathKit;
import com.goodcol.template.Engine;

/**
 * RenderManager.
 */
public class RenderManager {
	
	private Engine engine;
	private Constants constants;
	private ServletContext servletContext;
	private IRenderFactory renderFactory = null;
	
	private static final RenderManager me = new RenderManager();
	private RenderManager() {}
	
	public static RenderManager me() {
		return me;
	}
	
	public IRenderFactory getRenderFactory() {
		return renderFactory;
	}
	
	public void setRenderFactory(IRenderFactory renderFactory) {
		if (renderFactory == null) {
			throw new IllegalArgumentException("renderFactory can not be null");
		}
		this.renderFactory = renderFactory;
	}
	
	public void init(Engine engine, Constants constants, ServletContext servletContext) {
		this.engine = engine;
		this.constants = constants;
		this.servletContext = servletContext;
		
		// init Render
		Render.init(constants.getEncoding(), constants.getDevMode());
		initTemplateRender();
		initFreeMarkerRender(servletContext);
		initVelocityRender(servletContext);
		initJspRender(servletContext);
		initFileRender(servletContext);
		
		// create renderFactory
		if (renderFactory == null) {
			renderFactory = new RenderFactory();
		}
		renderFactory.init(engine, constants, servletContext);
	}
	
	private void initTemplateRender() {
		TemplateRender.init(engine);
	}
	
	private void initFreeMarkerRender(ServletContext servletContext) {
		try {
			Class.forName("freemarker.template.Template");	// detect freemarker.jar
			FreeMarkerRender.init(servletContext, Locale.getDefault(), constants.getFreeMarkerTemplateUpdateDelay());
		} catch (ClassNotFoundException e) {
			// System.out.println("freemarker can not be supported!");
			LogKit.logNothing(e);
		}
	}
	
	private void initVelocityRender(ServletContext servletContext) {
		try {
			Class.forName("org.apache.velocity.VelocityContext");
			VelocityRender.init(servletContext);
		}
		catch (ClassNotFoundException e) {
			// System.out.println("Velocity can not be supported!");
			LogKit.logNothing(e);
		}
	}
	
	private void initJspRender(ServletContext servletContext) {
		try {
			Class.forName("javax.el.ELResolver");
			Class.forName("javax.servlet.jsp.JspFactory");
			com.goodcol.plugin.activerecord.ModelRecordElResolver.init(servletContext);
		}
		catch (ClassNotFoundException e) {
			// System.out.println("Jsp or JSTL can not be supported!");
			LogKit.logNothing(e);
		}
		catch (IllegalStateException e) {
			throw e;
		}
		catch (Exception e) {
			LogKit.logNothing(e);
		}
	}
	
	private void initFileRender(ServletContext servletContext) {
		String downloadPath = constants.getBaseDownloadPath();
		downloadPath = downloadPath.trim();
		downloadPath = downloadPath.replaceAll("\\\\", "/");
		
		String baseDownloadPath;
		// 如果为绝对路径则直接使用，否则把 downloadPath 参数作为项目根路径的相对路径
		if (PathKit.isAbsolutelyPath(downloadPath)) {
			baseDownloadPath = downloadPath;
		} else {
			baseDownloadPath = PathKit.getWebRootPath() + File.separator + downloadPath;
		}
		
		// remove "/" postfix
		if (baseDownloadPath.equals("/") == false) {
			if (baseDownloadPath.endsWith("/")) {
				baseDownloadPath = baseDownloadPath.substring(0, baseDownloadPath.length() - 1);
			}
		}
		
		FileRender.init(baseDownloadPath, servletContext);
	}
	
	public Engine getEngine() {
		return engine;
	}
	
	public Constants getConstants() {
		return constants;
	}
	
	public ServletContext getServletContext() {
		return servletContext;
	}
}


