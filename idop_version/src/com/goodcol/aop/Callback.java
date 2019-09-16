
package com.goodcol.aop;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import static com.goodcol.aop.InterceptorManager.NULL_INTERS;

/**
 * Callback.
 */
class Callback implements MethodInterceptor {
	
	private Object injectTarget = null;
	private final Interceptor[] injectInters;
	
	private static final Set<String> excludedMethodName = buildExcludedMethodName();
	private static final InterceptorManager interMan = InterceptorManager.me();
	
	public Callback() {
		this.injectInters = NULL_INTERS;
	}
	
	public Callback(Interceptor... injectInters) {
		checkInjectInterceptors(injectInters);
		this.injectInters = injectInters;
	}
	
	public Callback(Object injectTarget, Interceptor... injectInters) {
		if (injectTarget == null) {
			throw new IllegalArgumentException("injectTarget can not be null.");
		}
		checkInjectInterceptors(injectInters);
		this.injectTarget = injectTarget;
		this.injectInters = injectInters;
	}
	
	private void checkInjectInterceptors(Interceptor... injectInters) {
		if (injectInters == null) {
			throw new IllegalArgumentException("injectInters can not be null.");
		}
		for (Interceptor inter : injectInters) {
			if (inter == null) {
				throw new IllegalArgumentException("interceptor in injectInters can not be null.");
			}
		}
	}
	
	public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		if (excludedMethodName.contains(method.getName())) {
			// if (method.getName().equals("finalize"))
			// 	return methodProxy.invokeSuper(target, args);
			// return this.injectTarget != null ? methodProxy.invoke(this.injectTarget, args) : methodProxy.invokeSuper(target, args);
			
			// 保留上面注释部分，此处为优化
			if (this.injectTarget == null || method.getName().equals("finalize")) {
				return methodProxy.invokeSuper(target, args);
			} else {
				return methodProxy.invoke(this.injectTarget, args);
			}
		}
		
		if (this.injectTarget != null) {
			target = this.injectTarget;
			Interceptor[] finalInters = interMan.buildServiceMethodInterceptor(injectInters, target.getClass(), method);
			Invocation invocation = new Invocation(target, method, args, methodProxy, finalInters);
			invocation.useInjectTarget = true;
			invocation.invoke();
			return invocation.getReturnValue();
		}
		else {
			Class<?> targetClass = target.getClass();
			if (targetClass.getName().indexOf("$$EnhancerByCGLIB") != -1) {
				targetClass = targetClass.getSuperclass();
			}
			Interceptor[] finalInters = interMan.buildServiceMethodInterceptor(injectInters, targetClass, method);
			Invocation invocation = new Invocation(target, method, args, methodProxy, finalInters);
			invocation.useInjectTarget = false;
			invocation.invoke();
			return invocation.getReturnValue();
		}
	}
	
	private static final Set<String> buildExcludedMethodName() {
		Set<String> excludedMethodName = new HashSet<String>();
		Method[] methods = Object.class.getDeclaredMethods();
		for (Method m : methods) {
			excludedMethodName.add(m.getName());
		}
		// getClass() registerNatives() can not be enhanced
		// excludedMethodName.remove("getClass");	
		// excludedMethodName.remove("registerNatives");
		return excludedMethodName;
	}
}


