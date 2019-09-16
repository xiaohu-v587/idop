package com.goodcol.util.zxgldbutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonToMapTool {

	public static Map<String, Object> convertJsonToMap(String jsonStr) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		return formatJsonStr(jsonStr, resMap);
	}

	private static Map<String, Object> formatJsonStr(String jsonStr, Map<String, Object> resMap) {
		Map<String, Object> map = JSONObject.parseObject(jsonStr, Map.class);
		Iterator<String> iterator = (Iterator<String>) map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			if (map.get(key) != null && map.get(key).getClass().equals(JSONObject.class)) {
				Map<String, Object> _map = new HashMap<String, Object>();
				resMap.put(key, _map);
				formatJsonStr(map.get(key).toString(), _map);
			} else if (map.get(key) != null && map.get(key).getClass().equals(JSONArray.class)) {
				List list = new ArrayList();
				resMap.put(key, list);
				formatJsonArray(map.get(key).toString(), list);
			} else {
				resMap.put(key, map.get(key));
			}
		}
		return resMap;
	}

	private static void formatJsonArray(String jsonStr, List list) {
		JSONArray jsonArray = JSONArray.parseArray(jsonStr);
		for (int i = 0; i < jsonArray.size(); i++) {
			if (jsonArray.get(i).getClass().equals(JSONArray.class)) {
				List _list = new ArrayList();
				list.add(_list);
				formatJsonArray(jsonArray.get(i).toString(), list);
			} else if (jsonArray.get(i).getClass().equals(JSONObject.class)) {
				Map<String, Object> _map = new HashMap<String, Object>();
				list.add(_map);
				formatJsonStr(jsonArray.get(i).toString(), _map);
			} else {
				list.add(jsonArray.get(i));
			}

		}
	}

}
