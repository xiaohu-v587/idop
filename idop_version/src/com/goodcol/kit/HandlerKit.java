

package com.goodcol.kit;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.goodcol.render.RenderException;
import com.goodcol.render.RenderManager;

/**
 * HandlerKit.
 */
public class HandlerKit {
	
	private HandlerKit(){}
	
	public static void renderError404(String view, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		isHandled[0] = true;
		
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		RenderManager.me().getRenderFactory().getRender(view).setContext(request, response).render();
	}
	
	public static void renderError404(HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		isHandled[0] = true;
		
		RenderManager.me().getRenderFactory().getErrorRender(404).setContext(request, response).render();
	}
	
	public static void redirect301(String url, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		isHandled[0] = true;
		
		String queryString = request.getQueryString();
		if (queryString != null)
			url += "?" + queryString;
		
		response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		response.setHeader("Location", url);
		response.setHeader("Connection", "close");
	}
	
	public static void redirect(String url, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		isHandled[0] = true;
		
		String queryString = request.getQueryString();
		if (queryString != null)
			url = url + "?" + queryString;
		
		try {
			response.sendRedirect(url);	// always 302
		} catch (IOException e) {
			throw new RenderException(e);
		}
	}
}
