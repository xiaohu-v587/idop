package com.goodcol.plugin.druid;

import javax.servlet.http.HttpServletRequest;

/**
 * 授权
 */
public interface IDruidStatViewAuth {
	boolean isPermitted(HttpServletRequest request);
}
