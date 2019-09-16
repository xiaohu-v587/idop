
package com.goodcol.captcha;

/**
 * CaptchaManager
 */
public class CaptchaManager {
	
	private static final CaptchaManager me = new CaptchaManager();
	private volatile ICaptchaCache captchaCache = null;
	
	private CaptchaManager() {}
	
	public static CaptchaManager me() {
		return me;
	}
	
	public void setCaptchaCache(ICaptchaCache captchaCache) {
		if (captchaCache == null) {
			throw new IllegalArgumentException("captchaCache can not be null");
		}
		this.captchaCache = captchaCache;
	}
	
	public ICaptchaCache getCaptchaCache() {
		if (captchaCache == null) {
			synchronized (me) {
				if (captchaCache == null) {
					captchaCache = new CaptchaCache();
				}
			}
		}
		return captchaCache;
	}
}


