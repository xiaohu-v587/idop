
package com.goodcol.template.expr.ast;

import com.goodcol.template.TemplateException;
import com.goodcol.template.stat.Location;
import com.goodcol.template.stat.ParseException;
import com.goodcol.template.stat.Scope;
import java.lang.reflect.Field;

/**
 * StaticField : ID_list '::' ID
 * 动态获取静态变量值，变量值改变时仍可正确获取
 * 用法：com.goodcol.core.Const::
 */
public class StaticField extends Expr {
	
	private Class<?> clazz;
	private String fieldName;
	private Field field;
	
	public StaticField(String className, String fieldName, Location location) {
		try {
			this.clazz = Class.forName(className);
			this.fieldName = fieldName;
			this.field = clazz.getField(fieldName);
			this.location = location;
		} catch (Exception e) {
			throw new ParseException(e.getMessage(), location, e);
		}
	}
	
	public Object eval(Scope scope) {
		try {
			return field.get(null);
		} catch (Exception e) {
			throw new TemplateException(e.getMessage(), location, e);
		}
	}
	
	public String toString() {
		return clazz.getName() + "::" + fieldName;
	}
}







