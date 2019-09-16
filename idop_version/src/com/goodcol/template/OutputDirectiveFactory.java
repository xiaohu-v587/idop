
package com.goodcol.template;

import com.goodcol.template.expr.ast.ExprList;
import com.goodcol.template.stat.Location;
import com.goodcol.template.stat.ast.Output;

/**
 * OutputDirectiveFactory
 */
public class OutputDirectiveFactory implements IOutputDirectiveFactory {
	
	public static final OutputDirectiveFactory me = new OutputDirectiveFactory();
	
	public Output getOutputDirective(ExprList exprList, Location location) {
		return new Output(exprList, location);
	}
}

