package com.goodcol.model;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Model;
import com.goodcol.core.plugin.activerecord.Record;



public class BaseModel<T extends BaseModel<T>> extends Model<T> {
	public boolean isOrNo(String sql,Object... paras){
		Record re = Db.findFirst(sql, paras);
		if(re != null){
			return true;
		}
		return false;
	}
	
	
	private String getParas(Object... paras){
		StringBuffer sql = new StringBuffer(); 
		sql.append("(");
		for (int i = 0; i < paras.length; i++) {
			sql.append("?");
			if(i+1!=paras.length)sql.append(",");
		}
		sql.append(")");
		return sql.toString();
	}
	
}