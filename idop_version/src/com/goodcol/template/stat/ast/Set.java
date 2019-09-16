
package com.goodcol.template.stat.ast;

import java.io.Writer;
import com.goodcol.template.Env;
import com.goodcol.template.expr.ast.Assign;
import com.goodcol.template.expr.ast.Expr;
import com.goodcol.template.expr.ast.ExprList;
import com.goodcol.template.stat.Location;
import com.goodcol.template.stat.ParseException;
import com.goodcol.template.stat.Scope;

/**
 * Set 赋值，从内向外作用域查找变量，找到则替换变量值，否则在顶层作用域赋值
 * 
 * 用法：
 * 1：#set(k = v)
 * 2：#set(k1 = v1, k2 = v2, ..., kn = vn)
 * 3：#set(x = 1+2)
 * 4：#set(x = 1+2, y = 3>4, ..., z = c ? a : b)
 */
public class Set extends Stat {
	
	private ExprList exprList;
	
	public Set(ExprList exprList, Location location) {
		if (exprList.length() == 0) {
			throw new ParseException("The parameter of #set directive can not be blank", location);
		}
		
		for (Expr expr : exprList.getExprArray()) {
			if ( !(expr instanceof Assign) ) {
				throw new ParseException("#set directive only supports assignment expressions", location);
			}
		}
		this.exprList = exprList;
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		scope.getCtrl().setWisdomAssignment();
		exprList.eval(scope);
	}
}

