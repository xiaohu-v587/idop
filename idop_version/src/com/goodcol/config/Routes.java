
package com.goodcol.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.goodcol.aop.Interceptor;
import com.goodcol.aop.InterceptorManager;
import com.goodcol.core.Controller;
import com.goodcol.kit.StrKit;

/**
 * Routes.
 */
public abstract class Routes {
	
	private static List<Routes> routesList = new ArrayList<Routes>();
	private static Set<String> controllerKeySet = new HashSet<String>();
	
	private String baseViewPath = null;
	private List<Route> routeItemList = new ArrayList<Route>();
	private List<Interceptor> injectInters = new ArrayList<Interceptor>();
	
	/**
	 * Implement this method to add route, add interceptor and set baseViewPath
	 */
	public abstract void config();
	
	/**
	 * Add Routes
	 */
	public Routes add(Routes routes) {
		routes.config();
		routesList.add(routes);
		return this;
	}
	
	/**
	 * Add route
	 * @param controllerKey A key can find controller
	 * @param controllerClass Controller Class
	 * @param viewPath View path for this Controller
	 */
	public Routes add(String controllerKey, Class<? extends Controller> controllerClass, String viewPath) {
		routeItemList.add(new Route(controllerKey, controllerClass, viewPath));
		return this;
	}
	
	/**
	 * Add route. The viewPath is controllerKey
	 * @param controllerKey A key can find controller
	 * @param controllerClass Controller Class
	 */
	public Routes add(String controllerKey, Class<? extends Controller> controllerClass) {
		return add(controllerKey, controllerClass, controllerKey);
	}
	
	/**
	 * Add inject interceptor for controller in this Routes
	 */
	public Routes addInterceptor(Interceptor interceptor) {
		injectInters.add(interceptor);
		return this;
	}
	
	/**
	 * Set base view path for controller in this routes
	 */
	public Routes setBaseViewPath(String baseViewPath) {
		if (StrKit.isBlank(baseViewPath)) {
			throw new IllegalArgumentException("baseViewPath can not be blank");
		}
		
		baseViewPath = baseViewPath.trim();
		if (! baseViewPath.startsWith("/")) {			// add prefix "/"
			baseViewPath = "/" + baseViewPath;
		}
		if (baseViewPath.endsWith("/")) {				// remove "/" in the end of baseViewPath
			baseViewPath = baseViewPath.substring(0, baseViewPath.length() - 1);
		}
		
		this.baseViewPath = baseViewPath;
		return this;
	}
	
	public String getBaseViewPath() {
		return baseViewPath;
	}
	
	public static List<Routes> getRoutesList() {
		return routesList;
	}
	
	public List<Route> getRouteItemList() {
		return routeItemList;
	}
	
	public Interceptor[] getInterceptors() {
		return injectInters.size() > 0 ?
				injectInters.toArray(new Interceptor[injectInters.size()]) :
				InterceptorManager.NULL_INTERS;
	}
	
	public void clear() {
		routesList = null;
		controllerKeySet = null;
		baseViewPath = null;
		routeItemList = null;
		injectInters = null;
	}
	
	public static class Route {
		
		private String controllerKey;
		private Class<? extends Controller> controllerClass;
		private String viewPath;
		
		public Route(String controllerKey, Class<? extends Controller> controllerClass, String viewPath) {
			if (StrKit.isBlank(controllerKey)) {
				throw new IllegalArgumentException("controllerKey can not be blank");
			}
			if (controllerClass == null) {
				throw new IllegalArgumentException("controllerClass can not be null");
			}
			if (StrKit.isBlank(viewPath)) {
				// throw new IllegalArgumentException("viewPath can not be blank");
				viewPath = "/";
			}
			
			this.controllerKey = processControllerKey(controllerKey);
			this.controllerClass = controllerClass;
			this.viewPath = processViewPath(viewPath);
		}
		
		private String processControllerKey(String controllerKey) {
			controllerKey = controllerKey.trim();
			if (!controllerKey.startsWith("/")) {
				controllerKey = "/" + controllerKey;
			}
			if (controllerKeySet.contains(controllerKey)) {
				throw new IllegalArgumentException("controllerKey already exists: " + controllerKey);
			}
			controllerKeySet.add(controllerKey);
			return controllerKey;
		}
		
		private String processViewPath(String viewPath) {
			viewPath = viewPath.trim();
			if (!viewPath.startsWith("/")) {			// add prefix "/"
				viewPath = "/" + viewPath;
			}
			if (!viewPath.endsWith("/")) {				// add postfix "/"
				viewPath = viewPath + "/";
			}
			return viewPath;
		}
		
		public String getControllerKey() {
			return controllerKey;
		}
		
		public Class<? extends Controller> getControllerClass() {
			return controllerClass;
		}
		
		public String getFinalViewPath(String baseViewPath) {
			return baseViewPath != null ? baseViewPath + viewPath : viewPath;
		}
	}
}









