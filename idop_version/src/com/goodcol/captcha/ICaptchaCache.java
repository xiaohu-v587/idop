package com.goodcol.captcha;

/**
 * ICaptchaCache
 */
public interface ICaptchaCache {
	void put(Captcha captcha);
	Captcha get(String key);
	void remove(String key);
	void removeAll();
}



