package com.goodcol.plugin.activerecord;

import java.util.ArrayList;
import java.util.List;

/**
 * SqlPara
 * 封装查询使用的 sql 与参数，主要用于 getSqlPara(...) 返回值
 */
public class SqlPara {
	
	String sql;
	List<Object> paraList = new ArrayList<Object>();
	
	public SqlPara setSql(String sql) {
		this.sql = sql;
		return this;
	}
	
	public SqlPara addPara(Object para) {
		paraList.add(para);
		return this;
	}
	
	public String getSql() {
		return sql;
	}
	
	public Object[] getPara() {
		if (paraList.size() == 0) {
			return DbKit.NULL_PARA_ARRAY;
		} else {
			return paraList.toArray(new Object[paraList.size()]);
		}
	}
	
	public SqlPara clear() {
		sql = null;
		paraList.clear();
		return this;
	}
	
	public String toString() {
		return "Sql: " + sql + "\nPara: " + paraList.toString();
	}
}
