
package com.goodcol.ext.interceptor;

import com.goodcol.aop.Interceptor;
import com.goodcol.aop.Invocation;

/**
 * NotAction
 */
public class NotAction implements Interceptor {
	public void intercept(Invocation inv) {
		inv.getController().renderError(404);
	}
}