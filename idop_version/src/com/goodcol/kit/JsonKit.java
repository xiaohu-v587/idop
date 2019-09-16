
package com.goodcol.kit;

import com.goodcol.json.GcdsJson;
import com.goodcol.json.Json;

/**
 * JsonKit.
 */
public class JsonKit {
	
	public static String toJson(Object object) {
		return Json.getJson().toJson(object);
	}
	
	public static <T> T parse(String jsonString, Class<T> type) {
		return Json.getJson().parse(jsonString, type);
	}
	
	/**
	 * 兼容   2.1 之前版本
	 */
	@Deprecated
	public static String toJson(Object value, int depth) {
		Json json = Json.getJson();
		// 仅 GcdsJson 实现支持 int depth 参数
		if (json instanceof GcdsJson) {
			((GcdsJson)json).setConvertDepth(depth);
		}
		return json.toJson(value);
	}
	
	/*
	public static String toJson(Object target, String dataPattern) {
		return null;
	}
	*/
}

