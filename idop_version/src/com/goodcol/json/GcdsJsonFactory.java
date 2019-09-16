
package com.goodcol.json;

/**
 * IJsonFactory 的   实现.
 */
public class GcdsJsonFactory implements IJsonFactory {
	
	private static final GcdsJsonFactory me = new GcdsJsonFactory();
	
	public static GcdsJsonFactory me() {
		return me;
	}
	
	public Json getJson() {
		return new GcdsJson();
	}
}


