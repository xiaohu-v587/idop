
package com.goodcol.template.expr.ast;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import com.goodcol.template.TemplateException;
import com.goodcol.template.expr.Sym;
import com.goodcol.template.stat.Location;
import com.goodcol.template.stat.ParseException;
import com.goodcol.template.stat.Scope;

/**
 * Logic
 * 
 * 支持逻辑运算： !  &&  ||
 */
public class Logic extends Expr {
	
	private Sym op;
	private Expr left;		// ! 运算没有 left 参数
	private Expr right;
	
	/**
	 * 构造 || && 结点
	 */
	public Logic(Sym op, Expr left, Expr right, Location location) {
		if (left == null) {
			throw new ParseException("The target of \"" + op.value() + "\" operator on the left side can not be blank", location);
		}
		if (right == null) {
			throw new ParseException("The target of \"" + op.value() + "\" operator on the right side can not be blank", location);
		}
		this.op = op;
		this.left = left;
		this.right = right;
		this.location = location;
	}
	
	/**
	 * 构造 ! 结点，left 为 null
	 */
	public Logic(Sym op, Expr right, Location location) {
		if (right == null) {
			throw new ParseException("The target of \"" + op.value() + "\" operator on the right side can not be blank", location);
		}
		this.op = op;
		this.left = null;
		this.right = right;
		this.location = location;
	}
	
	public Object eval(Scope scope) {
		switch (op) {
		case NOT:
			return evalNot(scope);
		case AND:
			return evalAnd(scope);
		case OR:
			return evalOr(scope);
		default:
			throw new TemplateException("Unsupported operator: " + op.value(), location);
		}
	}
	
	Object evalNot(Scope scope) {
		return ! isTrue(right.eval(scope));
	}
	
	Object evalAnd(Scope scope) {
		return isTrue(left.eval(scope)) && isTrue(right.eval(scope));
	}
	
	Object evalOr(Scope scope) {
		return isTrue(left.eval(scope)) || isTrue(right.eval(scope));
	}
	
	/**
	 * 规则：
	 * 1：null 返回 false
	 * 2：boolean 类型，原值返回
	 * 3：Map、Connection(List被包括在内) 返回 size() > 0
	 * 4：数组，返回 length > 0
	 * 5：String、StringBuilder、StringBuffer 等继承自 CharSequence 类的对象，返回 length > 0
	 * 6：Number 类型，返回 intValue() != 0
	 * 7：Iterator 返回 hasNext() 值
	 * 8：其它返回 true
	 */
	public static boolean isTrue(Object v) {
		if (v == null) {
			return false;
		}
		if (v instanceof Boolean) {
			return (Boolean)v;
		}
		if (v instanceof Collection) {
			return ((Collection<?>)v).size() > 0;
		}
		if (v instanceof Map) {
			return ((Map<?, ?>)v).size() > 0;
		}
		if (v.getClass().isArray()) {
			return Array.getLength(v) > 0;
		}
		if (v instanceof CharSequence) {
			return ((CharSequence)v).length() > 0;
		}
		if (v instanceof Number) {
			return ((Number)v).intValue() != 0;
		}
		if (v instanceof Iterator) {
			return ((Iterator<?>)v).hasNext();
		}
		return true;
	}
	
	public static boolean isFalse(Object v) {
		return !isTrue(v);
	}
}



