package com.goodcol.plugin.activerecord;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"rawtypes", "unchecked"})
public class OrderedFieldContainerFactory implements IContainerFactory {
	
	public Map<String, Object> getAttrsMap() {
		return new LinkedHashMap();
	}
	
	public Map<String, Object> getColumnsMap() {
		return new LinkedHashMap();
	}
	
	public Set<String> getModifyFlagSet() {
		return new HashSet();
	}
}
