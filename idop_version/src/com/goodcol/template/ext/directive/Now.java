
package com.goodcol.template.ext.directive;

import java.io.Writer;
import java.text.SimpleDateFormat;
import com.goodcol.template.Directive;
import com.goodcol.template.Env;
import com.goodcol.template.TemplateException;
import com.goodcol.template.expr.ast.ExprList;
import com.goodcol.template.stat.ParseException;
import com.goodcol.template.stat.Scope;

/**
 * 输出当前时间，默认考虑是输出时间，给 pattern 输出可能是 Date、DateTime、Timestamp
 * 带 String 参数，表示 pattern
 */
public class Now extends Directive {
	
	public void setExrpList(ExprList exprList) {
		if (exprList.length() > 1) {
			throw new ParseException("#now directive support one parameter only", location);	
		}
		super.setExprList(exprList);
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		String dataPattern;
		if (exprList.length() == 0) {
			dataPattern = env.getEngineConfig().getDatePattern();
		} else {
			Object dp = exprList.eval(scope);
			if (dp instanceof String) {
				dataPattern = (String)dp;
			} else {
				throw new TemplateException("The parameter of #new directive must be String", location);
			}
		}
		
		try {
			String value = new SimpleDateFormat(dataPattern).format(new java.util.Date());
			write(writer, value);
		} catch (Exception e) {
			throw new TemplateException(e.getMessage(), location, e);
		}
	}
}



