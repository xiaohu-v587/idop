

package com.goodcol.plugin.activerecord.sql;

import java.io.Writer;

import com.goodcol.plugin.activerecord.SqlPara;
import com.goodcol.template.Directive;
import com.goodcol.template.Env;
import com.goodcol.template.TemplateException;
import com.goodcol.template.expr.ast.Expr;
import com.goodcol.template.expr.ast.ExprList;
import com.goodcol.template.expr.ast.Id;
import com.goodcol.template.stat.ParseException;
import com.goodcol.template.stat.Scope;

/**
 * ParaDirective
 * 
 * #p 与 #para 指令用于在 sql 模板中根据参数名生成问号占位以及查询参数
 * Example：
 * 1：模板内容
 *   #sql("find")
 *     select * from user where nickName = #p(nickName) and age > #p(age)
 *   #end
 *   
 * 2： java 代码
 *   SqlPara sp = getSqlPara("find", JMap.create("nickName", "prettyGirl").put("age", 18));
 *   user.find(sp)
 *   或者：
 *   user.find(sp.getSql(), sp.getPara());
 */
public class ParaDirective extends Directive {
	
	private Id id;
	
	public void setExprList(ExprList exprList) {
		Expr[] exprs = exprList.getExprArray();
		if (exprs.length == 0 || exprs.length > 1) {
			throw new ParseException("only one parameter allowed for #p or #para directive", location);
		}
		if (!(exprs[0] instanceof Id)) {
			throw new ParseException("the parameter of #p or #para directive must be identifier", location);
		}
		
		this.id = (Id)exprs[0];
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		SqlPara sqlPara = (SqlPara)scope.get(SqlKit.SQL_PARA_KEY);
		if (sqlPara == null) {
			throw new TemplateException("#p or #para directive invoked by getSqlPara(...) method only", location);
		}
		
		write(writer, "?");
		sqlPara.addPara(id.eval(scope));
	}
}



