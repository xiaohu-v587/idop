
package com.goodcol.ext.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.goodcol.core.Controller;
import com.goodcol.handler.Handler;

/**
 * RoutesHandler. Not finish yet.
 * <p>
 * RoutesHandler convert url to route format that Routes can find Controller and Method 
 */
public class RoutesHandler extends Handler {
	
	public void addRoute(String regex, String controllerKey) {
		throw new RuntimeException("Not finished");
	}
	
	public void addRoute(String regex, String controllerKey, String method) {
		throw new RuntimeException("Not finished");
	}
	
	public void addRoute(String regex, Controller controller, String method) {
		throw new RuntimeException("Not finished");
	}
	
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		throw new RuntimeException("Not finished");
	}
}
