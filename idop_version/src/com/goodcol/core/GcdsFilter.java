package com.goodcol.core;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.goodcol.config.Constants;
import com.goodcol.config.GcdsConfig;
import com.goodcol.handler.Handler;
import com.goodcol.log.Log;

/**
 * Gcds framework filter
 */
public class GcdsFilter implements Filter {
	
	private Handler handler;
	private String encoding;
	private GcdsConfig gcdsConfig;
	private Constants constants;
	private static final Gcds gcds = Gcds.me();
	private static Log log;
	private int contextPathLength;
	
	public void init(FilterConfig filterConfig) throws ServletException {
		createGcdsConfig(filterConfig.getInitParameter("configClass"));
		
		if (gcds.init(gcdsConfig, filterConfig.getServletContext()) == false) {
			throw new RuntimeException("Gcds init error!");
		}
		
		handler = gcds.getHandler();
		constants = Config.getConstants();
		encoding = constants.getEncoding();
		gcdsConfig.afterGcdsStart();
		
		String contextPath = filterConfig.getServletContext().getContextPath();
		contextPathLength = (contextPath == null || "/".equals(contextPath) ? 0 : contextPath.length());
	}
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		request.setCharacterEncoding(encoding);
		
		String target = request.getRequestURI();
		if (contextPathLength != 0) {
			target = target.substring(contextPathLength);
		}
		
		boolean[] isHandled = {false};
		try {
			handler.handle(target, request, response, isHandled);
		}
		catch (Exception e) {
			if (log.isErrorEnabled()) {
				String qs = request.getQueryString();
				log.error(qs == null ? target : target + "?" + qs, e);
			}
		}
		
		if (isHandled[0] == false) {
			chain.doFilter(request, response);
		}
	}
	
	public void destroy() {
		gcdsConfig.beforeGcdsStop();
		gcds.stopPlugins();
	}
	
	private void createGcdsConfig(String configClass) {
		if (configClass == null) {
			throw new RuntimeException("Please set configClass parameter of Filter in web.xml");
		}
		
		Object temp = null;
		try {
			temp = Class.forName(configClass).newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Can not create instance of class: " + configClass, e);
		}
		
		if (temp instanceof GcdsConfig) {
			gcdsConfig = (GcdsConfig)temp;
		} else {
			throw new RuntimeException("Can not create instance of class: " + configClass + ". Please check the config in web.xml");
		}
	}
	
	static void initLog() {
		log = Log.getLog(GcdsFilter.class);
	}
}
