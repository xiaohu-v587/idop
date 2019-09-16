package com.goodcol.ext.interceptor;

import com.goodcol.aop.Interceptor;
import com.goodcol.aop.Invocation;
import com.goodcol.core.Controller;

/**
 * Accept POST method only.
 */
public class POST implements Interceptor {
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		if ("POST".equalsIgnoreCase(controller.getRequest().getMethod().toUpperCase())) {
			inv.invoke();
		} else {
			controller.renderError(404);
		}
	}
}
