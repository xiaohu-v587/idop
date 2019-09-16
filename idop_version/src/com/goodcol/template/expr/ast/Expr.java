
package com.goodcol.template.expr.ast;

import com.goodcol.template.stat.Location;
import com.goodcol.template.stat.Scope;

/**
 * Expr
 */
public abstract class Expr {
	
	protected Location location;
	
	public abstract Object eval(Scope scope);
}




