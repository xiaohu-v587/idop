
package com.goodcol.render;

import java.io.IOException;
import com.goodcol.core.Gcds;

/**
 * RedirectRender with status: 302 Found.
 */
public class RedirectRender extends Render {
	
	private String url;
	private boolean withQueryString;
	private static final String contextPath = getContxtPath();
	
	static String getContxtPath() {
		String cp = Gcds.me().getContextPath();
		return ("".equals(cp) || "/".equals(cp)) ? null : cp;
	}
	
	public RedirectRender(String url) {
		this.url = url;
		this.withQueryString = false;
	}
	
	public RedirectRender(String url, boolean withQueryString) {
		this.url = url;
		this.withQueryString =  withQueryString;
	}
	
	public String buildFinalUrl() {
		String result;
		// 如果一个url为/login/connect?goto=http://www.goodcol.com，则有错误
		// ^((https|http|ftp|rtsp|mms)?://)$   ==> indexOf 取值为 (3, 5)
		if (contextPath != null && (url.indexOf("://") == -1 || url.indexOf("://") > 5)) {
			result = contextPath + url;
		} else {
			result = url;
		}
		
		if (withQueryString) {
			String queryString = request.getQueryString();
			if (queryString != null) {
				if (result.indexOf("?") == -1) {
					result = result + "?" + queryString;
				} else {
					result = result + "&" + queryString;
				}
			}
		}
		
		return result;
	}
	
	public void render() {
		String finalUrl = buildFinalUrl();
		
		try {
			response.sendRedirect(finalUrl);	// always 302
		} catch (IOException e) {
			throw new RenderException(e);
		}
	}
}

