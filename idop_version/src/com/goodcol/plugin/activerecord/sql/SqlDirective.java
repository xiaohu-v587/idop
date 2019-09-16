

package com.goodcol.plugin.activerecord.sql;

import java.io.Writer;
import com.goodcol.kit.StrKit;
import com.goodcol.template.Directive;
import com.goodcol.template.Env;
import com.goodcol.template.Template;
import com.goodcol.template.expr.ast.Const;
import com.goodcol.template.expr.ast.Expr;
import com.goodcol.template.expr.ast.ExprList;
import com.goodcol.template.stat.ParseException;
import com.goodcol.template.stat.Scope;

/**
 * SqlDirective
 */
public class SqlDirective extends Directive {
	
	private String id;
	
	public void setExprList(ExprList exprList) {
		Expr[] exprs = exprList.getExprArray();
		if (exprs.length == 0 || exprs.length > 1) {
			throw new ParseException("only one parameter allowed for #sql directive", location);
		}
		if (!(exprs[0] instanceof Const) || !((Const)exprs[0]).isStr()) {
			throw new ParseException("the parameter of #sql directive must be String", location);
		}
		
		this.id = ((Const)exprs[0]).getStr();
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		String nameSpace = (String)scope.get(NameSpaceDirective.NAME_SPACE_KEY);
		String key = StrKit.notBlank(nameSpace) ? nameSpace + "." + id : id;
		SqlKit sqlKit = (SqlKit)scope.get(SqlKit.SQL_KIT_KEY);
		try {
			sqlKit.put(key, new Template(env, stat));
		} catch (Exception e) {
			throw new ParseException(e.getMessage(), location);
		}
	}
	
	public boolean hasEnd() {
		return true;
	}
}










