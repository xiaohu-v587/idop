package com.goodcol.ext.interceptor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import com.goodcol.aop.Interceptor;
import com.goodcol.aop.Invocation;
import com.goodcol.core.Controller;

/**
 * SessionInViewInterceptor.
 */
public class SessionInViewInterceptor implements Interceptor {
	
	private boolean createSession = false;
	
	public SessionInViewInterceptor() {
	}
	
	public SessionInViewInterceptor(boolean createSession) {
		this.createSession = createSession;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})	
	public void intercept(Invocation inv) {
		inv.invoke();
		
		Controller c = inv.getController();
		if (c.getRender() instanceof com.goodcol.render.JsonRender) {
			return ;
		}
		
		HttpSession hs = c.getSession(createSession);
		if (hs != null) {
			Map session = new GcdsSession(hs);
			for (Enumeration<String> names=hs.getAttributeNames(); names.hasMoreElements();) {
				String name = names.nextElement();
				session.put(name, hs.getAttribute(name));
			}
			c.setAttr("session", session);
		}
	}
}

@SuppressWarnings({"rawtypes", "deprecation"})
class GcdsSession extends HashMap implements HttpSession {
	private static final long serialVersionUID = -6148316613614087335L;
	private HttpSession session;
	
	public GcdsSession(HttpSession session) {
		this.session = session;
	}
	
	public Object getAttribute(String key) {
		return session.getAttribute(key);
	}
	
	@SuppressWarnings("unchecked")
	public Enumeration getAttributeNames() {
		return session.getAttributeNames();
	}
	
	public long getCreationTime() {
		return session.getCreationTime();
	}
	
	public String getId() {
		return session.getId();
	}
	
	public long getLastAccessedTime() {
		return session.getLastAccessedTime();
	}
	
	public int getMaxInactiveInterval() {
		return session.getMaxInactiveInterval();
	}
	
	public ServletContext getServletContext() {
		return session.getServletContext();
	}
	
	public javax.servlet.http.HttpSessionContext getSessionContext() {
		return session.getSessionContext();
	}
	
	public Object getValue(String key) {
		return session.getValue(key);
	}
	
	public String[] getValueNames() {
		return session.getValueNames();
	}
	
	public void invalidate() {
		session.invalidate();
	}
	
	public boolean isNew() {
		return session.isNew();
	}
	
	public void putValue(String key, Object value) {
		session.putValue(key, value);
	}
	
	public void removeAttribute(String key) {
		session.removeAttribute(key);
	}
	
	public void removeValue(String key) {
		session.removeValue(key);
	}
	
	public void setAttribute(String key, Object value) {
		session.setAttribute(key, value);
	}
	
	public void setMaxInactiveInterval(int maxInactiveInterval) {
		session.setMaxInactiveInterval(maxInactiveInterval);
	}
}

/*
public void intercept(Invocation inv) {
	inv.invoke();
	
	Controller c = inv.getController();
	HttpSession hs = c.getSession(createSession);
	if (hs != null) {
		c.setAttr("session", new GcdsSession(hs));
	}
}
*/
