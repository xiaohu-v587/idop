
package com.goodcol.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

 
public abstract class Handler {
	
	/**
	 * The next handler
	 */
	protected Handler next;
	
	/**
	 * Use next instead of nextHandler
	 */
	@Deprecated
	protected Handler nextHandler;
	
	/**
	 * Handle target
	 * @param target url target of this web http request
	 * @param request HttpServletRequest of this http request
	 * @param response HttpServletRequest of this http request
	 * @param isHandled  Filter will invoke doFilter() method if isHandled[0] == false,
	 * 			it is usually to tell Filter should handle the static resource.
	 */
	public abstract void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled);
}




