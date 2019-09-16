package com.goodcol.json;

/**
 * IJsonFactory 的 fastjson 实现.
 */
public class FastJsonFactory implements IJsonFactory {
	
	private static final FastJsonFactory me = new FastJsonFactory();
	
	public static FastJsonFactory me() {
		return me;
	}
	
	public Json getJson() {
		return new FastJson();
	}
}

