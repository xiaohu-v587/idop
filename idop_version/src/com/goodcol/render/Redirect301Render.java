
package com.goodcol.render;

import javax.servlet.http.HttpServletResponse;

/**
 * Redirect301Render.
 */
public class Redirect301Render extends RedirectRender {
	
	public Redirect301Render(String url) {
		super(url);
	}
	
	public Redirect301Render(String url, boolean withQueryString) {
		super(url, withQueryString);
	}
	
	public void render() {
		String finalUrl = buildFinalUrl();
		
		response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		// response.sendRedirect(url);	// always 302
		response.setHeader("Location", finalUrl);
		response.setHeader("Connection", "close");
	}
}






