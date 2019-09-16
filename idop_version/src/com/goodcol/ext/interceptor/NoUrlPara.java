
package com.goodcol.ext.interceptor;

import com.goodcol.aop.Interceptor;
import com.goodcol.aop.Invocation;
import com.goodcol.core.Controller;

/**
 * Force action no urlPara, otherwise render error 404.
 */
public class NoUrlPara implements Interceptor {
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		if (controller.getPara() == null) {
			inv.invoke();
		} else {
			controller.renderError(404);
		}
	}
}