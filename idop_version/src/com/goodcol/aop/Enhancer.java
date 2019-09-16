
package com.goodcol.aop;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Enhancer
 */
@SuppressWarnings("unchecked")
public class Enhancer {
	
	private static final ConcurrentHashMap<String, Object> singleton = new ConcurrentHashMap<String, Object>();
	
	private Enhancer(){}

	public static <T> T enhance(Class<T> targetClass) {
		return (T)net.sf.cglib.proxy.Enhancer.create(targetClass, new Callback());
	}
	
	public static <T> T enhance(Class<T> targetClass, Interceptor... injectInters) {
		return (T)net.sf.cglib.proxy.Enhancer.create(targetClass, new Callback(injectInters));
	}
	
	public static <T> T enhance(Class<T> targetClass, Class<? extends Interceptor>... injectIntersClasses) {
		return (T)enhance(targetClass, createInjectInters(injectIntersClasses));
	}
	
	public static <T> T enhance(Class<T> targetClass, Class<? extends Interceptor> injectIntersClass) {
		return (T)enhance(targetClass, createInjectInters(injectIntersClass));
	}
	
	public static <T> T enhance(Class<T> targetClass, Class<? extends Interceptor> injectIntersClass1, Class<? extends Interceptor> injectIntersClass2) {
		return (T)enhance(targetClass, createInjectInters(injectIntersClass1, injectIntersClass2));
	}
	
	public static <T> T enhance(Class<T> targetClass, Class<? extends Interceptor> injectIntersClass1, Class<? extends Interceptor> injectIntersClass2, Class<? extends Interceptor> injectIntersClass3) {
		return (T)enhance(targetClass, createInjectInters(injectIntersClass1, injectIntersClass2, injectIntersClass3));
	}
	
	public static <T> T getTarget(String singletonKey) {
		return (T)singleton.get(singletonKey);
	}
	
	public static <T> T enhance(String singletonKey, Class<T> targetClass) {
		Object target = singleton.get(singletonKey);
		if (target == null) {
			target = enhance(targetClass);
			singleton.put(singletonKey, target);
		}
		return (T)target;
	}
	
	public static <T> T enhance(String singletonKey, Class<T> targetClass, Interceptor... injectInters) {
		Object target = singleton.get(singletonKey);
		if (target == null) {
			target = enhance(targetClass, injectInters);
			singleton.put(singletonKey, target);
		}
		return (T)target;
	}
	
	public static <T> T enhance(String singletonKey, Class<T> targetClass, Class<? extends Interceptor>... injectIntersClasses) {
		Object target = singleton.get(singletonKey);
		if (target == null) {
			target = enhance(targetClass, injectIntersClasses);
			singleton.put(singletonKey, target);
		}
		return (T)target;
	}
	
	public static <T> T enhance(Object target) {
		return (T)net.sf.cglib.proxy.Enhancer.create(target.getClass(), new Callback(target));
	}
	
	public static <T> T enhance(Object target, Interceptor... injectInters) {
		return (T)net.sf.cglib.proxy.Enhancer.create(target.getClass(), new Callback(target, injectInters));
	}
	
	public static <T> T enhance(Object target, Class<? extends Interceptor>... injectIntersClasses) {
		return (T)enhance(target, createInjectInters(injectIntersClasses));
	}
	
	public static <T> T enhance(Object target, Class<? extends Interceptor> injectIntersClass) {
		return (T)enhance(target, createInjectInters(injectIntersClass));
	}
	
	public static <T> T enhance(Object target, Class<? extends Interceptor> injectIntersClass1, Class<? extends Interceptor> injectIntersClass2) {
		return (T)enhance(target, createInjectInters(injectIntersClass1, injectIntersClass2));
	}
	
	public static <T> T enhance(Object target, Class<? extends Interceptor> injectIntersClass1, Class<? extends Interceptor> injectIntersClass2, Class<? extends Interceptor> injectIntersClass3) {
		return (T)enhance(target, createInjectInters(injectIntersClass1, injectIntersClass2, injectIntersClass3));
	}
	
	public static <T> T enhance(String singletonKey, Object target) {
		Object result = singleton.get(singletonKey);
		if (result == null) {
			result = enhance(target);
			singleton.put(singletonKey, result);
		}
		return (T)result;
	}
	
	public static <T> T enhance(String singletonKey, Object target, Interceptor... injectInters) {
		Object result = singleton.get(singletonKey);
		if (result == null) {
			result = enhance(target, injectInters);
			singleton.put(singletonKey, result);
		}
		return (T)result;
	}
	
	public static <T> T enhance(String singletonKey, Object target, Class<? extends Interceptor>... injectIntersClasses) {
		Object result = singleton.get(singletonKey);
		if (result == null) {
			result = enhance(target, injectIntersClasses);
			singleton.put(singletonKey, result);
		}
		return (T)result;
	}
	
	private static Interceptor[] createInjectInters(Class<? extends Interceptor>... injectInterClasses) {
		return InterceptorManager.me().createInterceptor(injectInterClasses);
	}
	
	/**
	 * Add global service interceptor, the same as me.addGlobalServiceInterceptor(...)
	 * in YourGcdsConfig.configInterceptor(Interceptors me)
	 */
	// public static void addGlobalServiceInterceptor(Interceptor... inters) {
		// InterceptorManager.me().addGlobalServiceInterceptor(inters);
	// }
}


