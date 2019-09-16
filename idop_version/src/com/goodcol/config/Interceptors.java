

package com.goodcol.config;

import com.goodcol.aop.Interceptor;
import com.goodcol.aop.InterceptorManager;

/**
 * The Interceptors is used to config global action interceptors and global service interceptors.
 */
final public class Interceptors {
	
	/**
	 * The same as addGlobalActionInterceptor. It is used to compatible with earlier version of gcds
	 */
	public Interceptors add(Interceptor globalActionInterceptor) {
		if (globalActionInterceptor == null) {
			throw new IllegalArgumentException("globalActionInterceptor can not be null.");
		}
		InterceptorManager.me().addGlobalActionInterceptor(globalActionInterceptor);
		return this;
	}
	
	/**
	 * Add the global action interceptor to intercept all the actions.
	 */
	public Interceptors addGlobalActionInterceptor(Interceptor globalActionInterceptor) {
		if (globalActionInterceptor == null) {
			throw new IllegalArgumentException("globalActionInterceptor can not be null.");
		}
		InterceptorManager.me().addGlobalActionInterceptor(globalActionInterceptor);
		return this;
	}
	
	/**
	 * Add the global service interceptor to intercept all the method enhanced by aop Enhancer.
	 */
	public Interceptors addGlobalServiceInterceptor(Interceptor globalServiceInterceptor) {
		if (globalServiceInterceptor == null) {
			throw new IllegalArgumentException("globalServiceInterceptor can not be null.");
		}
		InterceptorManager.me().addGlobalServiceInterceptor(globalServiceInterceptor);
		return this;
	}
}

