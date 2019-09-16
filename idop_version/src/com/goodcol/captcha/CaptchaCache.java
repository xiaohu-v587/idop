
package com.goodcol.captcha;

import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ICaptchaCache 默认实现，可用于单实例部署
 * 集群部署需自行实现 ICaptchaCache 接口，并使用
 * CaptchaManager.setCaptchaCache(...) 进行配置
 */
public class CaptchaCache implements ICaptchaCache {
	
	private ConcurrentHashMap<String, Captcha> map = new ConcurrentHashMap<String, Captcha>();
	private int interval = 90 * 1000;	// timer 调度间隔为 90 秒
	private Timer timer;
	
	public CaptchaCache() {
		autoRemoveExpiredCaptcha();
	}
	
	/**
	 * 定期移除过期的验证码
	 */
	private void autoRemoveExpiredCaptcha() {
		timer = new Timer("CaptchaCache", true);
		timer.schedule(
			new TimerTask() {
				public void run() {
					for (Entry<String, Captcha> e : map.entrySet()) {
						if (e.getValue().isExpired()) {
							map.remove(e.getKey());
						}
					}
				}
			},
			interval,
			interval
		);
	}
	
	public void put(Captcha captcha) {
		map.put(captcha.getKey(), captcha);
	}
	
	public Captcha get(String key) {
		return key != null ? map.get(key) : null;
	}
	
	public void remove(String key) {
		map.remove(key);
	}
	
	public void removeAll() {
		map.clear();
	}
	
	public boolean contains(String key) {
		return map.containsKey(key);
	}
}



