package com.goodcol.json;

import com.goodcol.json.FastJson;
import com.goodcol.json.IJsonFactory;
import com.goodcol.json.GcdsJson;
import com.goodcol.json.Json;

/**
 
 * 注意：
 * 1：需要添加 fastjson 相关 jar 包
 * 2：parse 方法转对象依赖于 setter 方法
 * 3：MixedJson 内部使用了 static 共享变量，在使用时不要改变其内部属性值，以免影响其它线程
 */
public class MixedJsonFactory implements IJsonFactory {
	
	private static final MixedJsonFactory me = new MixedJsonFactory();
	
	public static MixedJsonFactory me() {
		return me;
	}

	private static MixedJson mixedJson =  new MixedJson();

	public Json getJson() {
		return mixedJson;
	}

	private static class MixedJson extends Json {

		private static GcdsJson gJson = GcdsJson.getJson();
		private static FastJson fastJson = FastJson.getJson();

		public String toJson(Object object) {
			return gJson.toJson(object);
		}

		public <T> T parse(String jsonString, Class<T> type) {
			return fastJson.parse(jsonString, type);
		}
	}
}
