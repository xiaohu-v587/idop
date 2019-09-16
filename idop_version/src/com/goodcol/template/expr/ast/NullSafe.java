
package com.goodcol.template.expr.ast;

import com.goodcol.template.stat.Ctrl;
import com.goodcol.template.stat.Location;
import com.goodcol.template.stat.ParseException;
import com.goodcol.template.stat.Scope;

/**
 * NullSafe
 * 在原则上只支持具有动态特征的用法，例如：方法调用、字段取值、Map 与 List 取值
 * 而不支持具有静态特征的用法，例如：static method 调用、shared method 调用
 * 
 * 用法：
 
 */
public class NullSafe extends Expr {
	
	private Expr left;
	private Expr right;
	
	public NullSafe(Expr left, Expr right, Location location) {
		if (left == null) {
			throw new ParseException("The expression on the left side of null coalescing and safe access operator \"??\" can not be blank", location);
		}
		this.left = left;
		this.right = right;
		this.location = location;
	}
	
	public Object eval(Scope scope) {
		Ctrl ctrl = scope.getCtrl();
		boolean oldNullSafeValue = ctrl.isNullSafe();
		
		Object ret;
		try {
			ctrl.setNullSafe(true);
			ret = left.eval(scope);
		} finally {
			ctrl.setNullSafe(oldNullSafeValue);
		}
		
		return ret == null && right != null ? right.eval(scope) : ret;
	}
}






