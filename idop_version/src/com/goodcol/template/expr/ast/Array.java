
package com.goodcol.template.expr.ast;

import java.util.ArrayList;
import java.util.List;
import com.goodcol.template.stat.Location;
import com.goodcol.template.stat.ParseException;
import com.goodcol.template.stat.Scope;

/**
 * Array
 * 
 * 用法：
 * 1：[1, 2, 3]  或者  ["a", 1, "b", 2, false, 3.14]
 * 2：[1..3] 或者 [3..1]
 */
public class Array extends Expr {
	
	private Expr[] exprList;
	
	public Array(Expr[] exprList, Location location) {
		if (exprList == null) {
			throw new ParseException("exprList can not be null", location);
		}
		this.exprList = exprList;
	}
	
	public Object eval(Scope scope) {
		List<Object> array = new ArrayList<Object>(exprList.length + 3);
		for (Expr expr : exprList) {
			array.add(expr.eval(scope));
		}
		return array;
	}
}








