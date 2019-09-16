package com.goodcol.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.util.ArrayList;
import java.util.List;

public class JsonConverter {
	private static JsonConverter jsonConverter = new JsonConverter();
	private ObjectMapper objMapper;
	private TypeFactory typeFactory;

	private JsonConverter() {
		this.objMapper = new ObjectMapper();
		this.objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.typeFactory = TypeFactory.defaultInstance();
	}

	static JsonConverter getInstance() {
		return jsonConverter;
	}

	String object2Json(Object objValue) {
		String rtnValue = null;
		try {
			rtnValue = this.objMapper.writeValueAsString(objValue);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return rtnValue;
	}

	JsonNode json2JsonNode(String jsonString) {
		try {
			return (JsonNode) this.objMapper.readValue(jsonString, JsonNode.class);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	<T> T json2Object(Class<T> targetClzz, Object jsonObject) {
		try {
			return this.objMapper.readValue(jsonObject.toString(), targetClzz);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	<T> List<T> json2List(Class<T> targetClzz, Object jsonObject) {
		try {
			return (List) this.objMapper.readValue(jsonObject.toString(), this.typeFactory.constructParametricType(ArrayList.class, new Class[] { targetClzz }));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}