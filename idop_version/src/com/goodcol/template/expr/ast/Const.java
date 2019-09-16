
package com.goodcol.template.expr.ast;

import com.goodcol.template.expr.Sym;
import com.goodcol.template.stat.Scope;

/**
 * STR INT LONG FLOAT DOUBLE true false null
 */
public class Const extends Expr {
	
	public static final Const TRUE = new Const(Boolean.TRUE, Sym.TRUE);
	public static final Const FALSE = new Const(Boolean.FALSE, Sym.FALSE);
	public static final Const NULL = new Const(null, Sym.NULL);
	
	private Sym type;
	private Object value;
	
	/**
	 * 构造 TRUE FALSE NULL 常量，无需对 value 进行转换
	 */
	private Const(Object value, Sym type) {
		this.type = type;
		this.value = value;
	}
	
	public Const(Sym type, String value) {
		this.type = type;
		this.value = typeConvert(type, value);
	}
	
	private Object typeConvert(Sym type, String value) {
		switch (type) {
		case STR:
			return value;
		case INT:
			return Integer.parseInt(value);
		case LONG:
			return Long.parseLong(value);
		case FLOAT:
			return Float.parseFloat(value);
		case DOUBLE:
			return Double.parseDouble(value);
		/*
		case TRUE:
		case FALSE:
			return Boolean.parseBoolean(value);
		case NULL:
			return null;
		*/
		default:
			throw new RuntimeException("never happend");
		}
	}
	
	public Object eval(Scope scope) {
		return value;
	}
	
	public String toString() {
		return value.toString();
	}
	
	public boolean isStr() {
		return type == Sym.STR;
	}
	
	public boolean isTrue() {
		return type == Sym.TRUE;
	}
	
	public boolean isFalse() {
		return type == Sym.FALSE;
	}
	
	public boolean isBoolean() {
		return type == Sym.TRUE || type == Sym.FALSE;
	}
	
	public boolean isNull() {
		return type == Sym.NULL;
	}
	
	public boolean isInt() {
		return type == Sym.INT;
	}
	
	public boolean isLong() {
		return type == Sym.LONG;
	}
	
	public boolean isFloat() {
		return type == Sym.FLOAT;
	}
	
	public boolean isDouble() {
		return type == Sym.DOUBLE;
	}
	
	public Object getValue() {
		return value;
	}
	
	public String getStr() {
		return (String)value;
	}
	
	public Boolean getBoolean() {
		return (Boolean)value;
	}
	
	public Integer getInt() {
		return (Integer)value;
	}
	
	public Long getLong() {
		return (Long)value;
	}
	
	public Float getFloat() {
		return (Float)value;
	}
	
	public Double getDouble() {
		return (Double)value;
	}
}








