
package com.goodcol.template.expr.ast;

import com.goodcol.template.stat.Scope;

/**
 * Id
 */
public class Id extends Expr {
	
	private String id;
	
	public Id(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public Object eval(Scope scope) {
		return scope.get(id);
	}
	
	public String toString() {
		return id;
	}
}


