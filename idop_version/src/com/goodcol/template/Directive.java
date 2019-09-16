
package com.goodcol.template;

import com.goodcol.template.expr.ast.ExprList;
import com.goodcol.template.stat.ast.Stat;

/**
 * Directive 供用户继承并扩展自定义指令
 */
public abstract class Directive extends Stat {
	
	protected ExprList exprList;
	protected Stat stat;
	
	public void setExprList(ExprList exprList) {
		this.exprList = exprList;
	}
	
	public void setStat(Stat stat) {
		this.stat = stat;
	}
}





