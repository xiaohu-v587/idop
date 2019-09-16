
package com.goodcol.template.stat.ast;

import java.io.Writer;
import com.goodcol.template.Env;
import com.goodcol.template.expr.ast.ExprList;
import com.goodcol.template.expr.ast.Logic;
import com.goodcol.template.stat.Location;
import com.goodcol.template.stat.ParseException;
import com.goodcol.template.stat.Scope;

/**
 * If
 */
public class If extends Stat {
	
	private ExprList cond;
	private Stat stat;
	private Stat elseIfOrElse;
	
	public If(ExprList cond, Stat stat, Location location) {
		if (cond.length() == 0) {
			throw new ParseException("The condition expression of #if statement can not be blank", location);
		}
		this.cond = cond;
		this.stat = stat;
	}
	
	/**
	 * take over setStat(...) method of super class
	 */
	public void setStat(Stat elseIfOrElse) {
		this.elseIfOrElse = elseIfOrElse;
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		if (Logic.isTrue(cond.eval(scope))) {
			stat.exec(env, scope, writer);
		} else if (elseIfOrElse != null) {
			elseIfOrElse.exec(env, scope, writer);
		}
	}
}



