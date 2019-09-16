
package com.goodcol.template.expr.ast;

import com.goodcol.template.TemplateException;
import com.goodcol.template.stat.Location;
import com.goodcol.template.stat.ParseException;
import com.goodcol.template.stat.Scope;

/**
 * forCtrl : ID : expression
 * 		   | exprList? ';' expr? ';' exprList?
 * 
 * 两种用法
 * 1：#for(id : list) #end
 *    #for(entry : map) #end
 * 
 * 2：#for(init; cond; update) #end
 */
public class ForCtrl extends Expr {
	
	private String id;
	private Expr expr;
	
	private Expr init;
	private Expr cond;
	private Expr update;
	
	/**
	 * ID : expr
	 */
	public ForCtrl(Id id, Expr expr, Location location) {
		if (expr == null) {
			throw new ParseException("The iterator target of #for statement can not be null", location);
		}
		this.id = id.getId();
		this.expr = expr;
		this.init = null;
		this.cond = null;
		this.update = null;
		this.location = location;
	}
	
	/**
	 * exprList? ';' expr? ';' exprList?
	 */
	public ForCtrl(Expr init, Expr cond, Expr update, Location location) {
		this.init = init;
		this.cond = cond;
		this.update = update;
		this.id = null;
		this.expr = null;
		this.location = location;
	}
	
	public boolean isIterator() {
		return id != null;
	}
	
	public String getId() {
		return id;
	}
	
	public Expr getExpr() {
		return expr;
	}
	
	public Expr getInit() {
		return init;
	}
	
	public Expr getCond() {
		return cond;
	}
	
	public Expr getUpdate() {
		return update;
	}
	
	public Object eval(Scope scope) {
		throw new TemplateException("The eval(Scope scope) method can not be invoked", location);
	}
}


