
package com.goodcol.aop;

import java.util.ArrayList;
import java.util.List;

/**
 * InterceptorStack.
 */
public abstract class InterceptorStack implements Interceptor {
	
	private Interceptor[] inters;
	private List<Interceptor> interList;
	
	public InterceptorStack() {
 		config();
 		
		if (interList == null)
			throw new RuntimeException("You must invoke addInterceptors(...) to config your InterceptorStack");
		
		inters = interList.toArray(new Interceptor[interList.size()]);
		interList.clear();
		interList = null;
	}
	
	protected final InterceptorStack addInterceptors(Interceptor... interceptors) {
		if (interceptors == null || interceptors.length == 0)
			throw new IllegalArgumentException("Interceptors can not be null");
		
		if (interList == null)
			interList = new ArrayList<Interceptor>();
		
		for (Interceptor ref : interceptors)
			interList.add(ref);
		
		return this;
	}
	
	public final void intercept(Invocation inv) {
		new InvocationWrapper(inv, inters).invoke();
	}
	
	public abstract void config();
}



