package com.goodcol.template.stat.ast;

import java.io.Writer;
import com.goodcol.template.Env;
import com.goodcol.template.expr.ast.Assign;
import com.goodcol.template.expr.ast.Expr;
import com.goodcol.template.expr.ast.ExprList;
import com.goodcol.template.stat.Ctrl;
import com.goodcol.template.stat.Location;
import com.goodcol.template.stat.ParseException;
import com.goodcol.template.stat.Scope;

/**
 * SetLocal 设置全局变量，全局作用域是指本次请求的整个 template
 * 
 * 适用于极少数的在内层作用域中希望直接操作顶层作用域的场景
 */
public class SetGlobal  extends Stat {
	
	private ExprList exprList;
	
	public SetGlobal(ExprList exprList, Location location) {
		if (exprList.length() == 0) {
			throw new ParseException("The parameter of #setGlobal directive can not be blank", location);
		}
		
		for (Expr expr : exprList.getExprArray()) {
			if ( !(expr instanceof Assign) ) {
				throw new ParseException("#setGlobal directive only supports assignment expressions", location);
			}
		}
		this.exprList = exprList;
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		Ctrl ctrl = scope.getCtrl();
		try {
			ctrl.setGlobalAssignment();
			exprList.eval(scope);
		} finally {
			ctrl.setWisdomAssignment();
		}
	}
}





