package com.goodcol.plugin.activerecord;

import java.util.HashMap;
import java.util.Map;

/**
 * TableMapping save the mapping between model class and table.
 */
public class TableMapping {
	
	private final Map<Class<? extends Model<?>>, Table> modelToTableMap = new HashMap<Class<? extends Model<?>>, Table>();
	
	private static TableMapping me = new TableMapping(); 
	
	private TableMapping() {}
	
	public static TableMapping me() {
		return me;
	}
	
	public void putTable(Table table) {
		modelToTableMap.put(table.getModelClass(), table);
	}
	
	@SuppressWarnings("rawtypes")
	public Table getTable(Class<? extends Model> modelClass) {
		return modelToTableMap.get(modelClass);
	}
}


