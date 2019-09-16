
package com.goodcol.ext.interceptor;

import com.goodcol.aop.Interceptor;
import com.goodcol.aop.Invocation;
import com.goodcol.core.Controller;

/**
 * Accept GET method only.
 */
public class GET implements Interceptor {
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		if ("GET".equalsIgnoreCase(controller.getRequest().getMethod())) {
			inv.invoke();
		} else {
			controller.renderError(404);
		}
	}
}
