

package com.goodcol.plugin.activerecord.sql;

import java.io.Writer;
import com.goodcol.template.Directive;
import com.goodcol.template.Env;
import com.goodcol.template.TemplateException;
import com.goodcol.template.expr.ast.Const;
import com.goodcol.template.expr.ast.Expr;
import com.goodcol.template.expr.ast.ExprList;
import com.goodcol.template.stat.ParseException;
import com.goodcol.template.stat.Scope;

/**
 * NameSpaceDirective
 */
public class NameSpaceDirective extends Directive {
	
	static final String NAME_SPACE_KEY = "_NAME_SPACE_";
	
	private String nameSpace;
	
	public void setExprList(ExprList exprList) {
		Expr[] exprs = exprList.getExprArray();
		if (exprs.length == 0 || exprs.length > 1) {
			throw new ParseException("only one parameter allowed for #namespace directive", location);
		}
		if (!(exprs[0] instanceof Const) || !((Const)exprs[0]).isStr()) {
			throw new ParseException("the parameter of #namespace directive must be String", location);
		}
		
		this.nameSpace = ((Const)exprs[0]).getStr();
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		if (scope.get(NAME_SPACE_KEY) != null) {
			throw new TemplateException("#namespace directive can not be nested", location);
		}
		scope.set(NAME_SPACE_KEY, nameSpace);
		try {
			stat.exec(env, scope, writer);
		} finally {
			scope.remove(NAME_SPACE_KEY);
		}
	}
	
	public boolean hasEnd() {
		return true;
	}
}





