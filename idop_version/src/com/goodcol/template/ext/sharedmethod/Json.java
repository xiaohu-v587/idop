
package com.goodcol.template.ext.sharedmethod;

/**
 * Json shared method
 */
public class Json {
	
	private com.goodcol.json.Json json = com.goodcol.json.Json.getJson(); 
	
	public String toJson(Object target) {
		return json.toJson(target);
	}
}

