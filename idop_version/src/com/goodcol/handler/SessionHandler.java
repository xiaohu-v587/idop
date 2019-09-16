package com.goodcol.handler;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.goodcol.core.handler.Handler;

/**
 * session参数填入到request
 */
public class SessionHandler extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		HttpSession session = request.getSession();
		Enumeration<String> atts = session.getAttributeNames();
		while (atts.hasMoreElements() == true) {
			String an = atts.nextElement();
			request.setAttribute(an, session.getAttribute(an));
		}
		nextHandler.handle(target, request, response, isHandled);
	}

}
