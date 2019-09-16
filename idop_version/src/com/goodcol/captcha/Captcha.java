
package com.goodcol.captcha;

/**
 * 验证码
 */
public class Captcha {
	
	/**
	 * 验证码默认过期时长 180 秒
	 */
	public static final int DEFAULT_EXPIRE_TIME = 180;
	
	/**
	 * 验证码 key，存放在 cookie，或者表单隐藏域中返回给客户端
	 */
	private String key;
	
	/**
	 * 验证码值
	 */
	private String value;
	
	/**
	 * 验证码过期时间
	 */
	private long expireAt;
	
	/**
	 * 验证码构造
	 * @param key
	 * @param value
	 * @param expireTime 过期时长，单位为秒
	 */
	public Captcha(String key, String value, int expireTime) {
		if (key == null || value == null) {
			throw new IllegalArgumentException("key and value can not be null");
		}
		this.key = key;
		this.value = value;
		this.expireAt = expireTime * 1000 + System.currentTimeMillis();
	}
	
	public Captcha(String key, String value) {
		this(key, value, DEFAULT_EXPIRE_TIME);
	}
	
	public String getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
	
	public long getExpireAt() {
		return expireAt;
	}
	
	public boolean isExpired() {
		return expireAt < System.currentTimeMillis();
	}
	
	public boolean notExpired() {
		return !isExpired();
	}
	
	public String toString() {
		return key + " : " + value; 
	}
}


