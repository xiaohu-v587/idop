
package com.goodcol.plugin.ehcache;

import com.goodcol.aop.Interceptor;
import com.goodcol.aop.Invocation;

/**
 * EvictInterceptor.
 */
public class EvictInterceptor implements Interceptor {
	
	final public void intercept(Invocation inv) {
		inv.invoke();
		
		CacheKit.removeAll(buildCacheName(inv));
	}
	
	private String buildCacheName(Invocation inv) {
		CacheName cacheName = inv.getMethod().getAnnotation(CacheName.class);
		if (cacheName != null)
			return cacheName.value();
		
		cacheName = inv.getController().getClass().getAnnotation(CacheName.class);
		if (cacheName == null)
			throw new RuntimeException("EvictInterceptor need CacheName annotation in controller.");
		return cacheName.value();
	}
}

