
package com.goodcol.i18n;

import com.goodcol.aop.Interceptor;
import com.goodcol.aop.Invocation;
import com.goodcol.core.Const;
import com.goodcol.core.Controller;
import com.goodcol.kit.StrKit;
import com.goodcol.render.Render;

/**
 * I18nInterceptor is used to change the locale by request para,
 * and it is also switch the view or pass Res object to the view.
 * 
 * you can extends I18nInterceptor and override the getLocaleParaName() and getResName()
 * to customize configuration for your own i18n Interceptor
 */
public class I18nInterceptor implements Interceptor {
	
	private String localeParaName = "_locale";
	private String resName = "_res";
	private boolean isSwitchView = false;
	
	public I18nInterceptor() {
	}
	
	public I18nInterceptor(String localeParaName, String resName) {
		if (StrKit.isBlank(localeParaName)) {
			throw new IllegalArgumentException("localeParaName can not be blank.");
		}
		if (StrKit.isBlank(resName)) {
			throw new IllegalArgumentException("resName can not be blank.");
		}
		
		this.localeParaName = localeParaName;
		this.resName = resName;
	}
	
	public I18nInterceptor(String localeParaName, String resName, boolean isSwitchView) {
		this(localeParaName, resName);
		this.isSwitchView = isSwitchView;
	}
	
	public I18nInterceptor(boolean isSwitchView) {
		this.isSwitchView = isSwitchView;
	}
	
	/**
	 * Return the localeParaName, which is used as para name to get locale from the request para and the cookie.
	 */
	protected String getLocaleParaName() {
		return localeParaName;
	}
	
	/**
	 * Return the resName, which is used as attribute name to pass the Res object to the view.
	 */
	protected String getResName() {
		return resName;
	}
	
	/**
	 * Return the baseName, which is used as base name of the i18n resource file.
	 */
	protected String getBaseName() {
		return I18n.defaultBaseName;
	}
	
	/**
	 * 1: use the locale from request para if exists. change the locale write to the cookie
	 * 2: use the locale from cookie para if exists.
	 * 3: use the default locale
	 * 4: use setAttr(resName, resObject) pass Res object to the view.
	 */
	public void intercept(Invocation inv) {
		Controller c = inv.getController();
		String localeParaName = getLocaleParaName();
		String locale = c.getPara(localeParaName);
		
		if (StrKit.notBlank(locale)) {	// change locale, write cookie
			c.setCookie(localeParaName, locale, Const.DEFAULT_I18N_MAX_AGE_OF_COOKIE);
		}
		else {							// get locale from cookie and use the default locale if it is null
			locale = c.getCookie(localeParaName);
			if (StrKit.isBlank(locale))
				locale = I18n.defaultLocale;
		}
		
		inv.invoke();
		
		if (isSwitchView) {
			switchView(locale, c);
		}
		else {
			Res res = I18n.use(getBaseName(), locale);
			c.setAttr(getResName(), res);
		}
	}
	
	/**
	 * 在有些 web 系统中，页面需要国际化的文本过多，并且 css 以及 html 也因为际化而大不相同，
	 * 对于这种应用场景先直接制做多套同名称的国际化视图，并将这些视图以 locale 为子目录分类存放，
	 * 最后使用本拦截器根据 locale 动态切换视图，而不必对视图中的文本逐个进行国际化切换，省时省力。
	 */
	public void switchView(String locale, Controller c) {
		Render render = c.getRender();
		if (render != null) {
			String view = render.getView();
			if (view != null) {
				if (view.startsWith("/")) {
					view = "/" + locale + view;
				} else {
					view = locale + "/" + view;
				}
				
				render.setView(view);
			}
		}
	}
}




