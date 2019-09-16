package com.goodcol.ext.handler;

import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.goodcol.handler.Handler;
import com.goodcol.kit.StrKit;

/**
 * Skip the excluded url request from browser.
 * The skiped url will be handled by next Filter after GcdsFilter
 * <p>
 * Example: me.add(new UrlSkipHandler(".+\\.\\w{1,4}", false));
 */
public class UrlSkipHandler extends Handler {
	
	private Pattern skipedUrlPattern;
	
	public UrlSkipHandler(String skipedUrlRegx, boolean isCaseSensitive) {
		if (StrKit.isBlank(skipedUrlRegx)) {
			throw new IllegalArgumentException("The para excludedUrlRegx can not be blank.");
		}
		skipedUrlPattern = isCaseSensitive ? Pattern.compile(skipedUrlRegx) : Pattern.compile(skipedUrlRegx, Pattern.CASE_INSENSITIVE);
	}
	
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		if (skipedUrlPattern.matcher(target).matches()) {
			return ;
		} else {
			next.handle(target, request, response, isHandled);
		}
	}
}


