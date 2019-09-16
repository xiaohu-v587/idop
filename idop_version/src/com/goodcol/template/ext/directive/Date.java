
package com.goodcol.template.ext.directive;

import java.io.Writer;
import java.text.SimpleDateFormat;
import com.goodcol.template.Directive;
import com.goodcol.template.Env;
import com.goodcol.template.TemplateException;
import com.goodcol.template.expr.ast.Expr;
import com.goodcol.template.expr.ast.ExprList;
import com.goodcol.template.stat.ParseException;
import com.goodcol.template.stat.Scope;

/**
 * 不带参时，按默认 pattern 输出当前日期
 * 
 * #date() 指令支持无参时获取当前指令，第一个参数 string 当成是 pattern
 * 
 * 日期输出指令，第一个参数是被输出的 java.util.Date 对象或其子类对象
 * 无第二个参数时按默认 patter 输出，第二个参数为 expr 表达式，表示 pattern
 * 第二个为 date 时，表示当第一个为 null 时的默认值
 */
public class Date extends Directive {
	
	private Expr valueExpr;
	private Expr datePatternExpr;
	private int paraNum;
	
	public void setExprList(ExprList exprList) {
		this.paraNum = exprList.length();
		if (paraNum > 2) {
			throw new ParseException("Wrong number parameter of #date directive, two parameters allowed at most", location);
		}
		
		if (paraNum == 0) {
			this.valueExpr = null;
			this.datePatternExpr = null;
		} else if (paraNum == 1) {
			this.valueExpr = exprList.getExprArray()[0];
			this.datePatternExpr = null;
		} else if (paraNum == 2) {
			this.valueExpr = exprList.getExprArray()[0];
			this.datePatternExpr = exprList.getExprArray()[1];
		}
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		if (paraNum == 0) {
			outputToday(env, writer);
		} else if (paraNum == 1) {
			outputWithoutDatePattern(env, scope, writer);
		} else if (paraNum == 2) {
			outputWithDatePattern(env, scope, writer);
		}
	}
	
	private void outputToday(Env env, Writer writer) {
		Object value = format(new java.util.Date(), env.getEngineConfig().getDatePattern());
		write(writer, value.toString());
	}
	
	private void outputWithoutDatePattern(Env env, Scope scope, Writer writer) {
		Object value = valueExpr.eval(scope);
		if (value != null) {
			value = format(value, env.getEngineConfig().getDatePattern());
			write(writer, value.toString());
		}
	}
	
	private void outputWithDatePattern(Env env, Scope scope, Writer writer) {
		Object value = valueExpr.eval(scope);
		if (value == null) {
			return ;
		}
		
		Object dp = this.datePatternExpr.eval(scope);
		if ( !(dp instanceof String) ) {
			throw new TemplateException("The sencond parameter dataPattern of #date directive must be String", location);
		}
		value = format(value, (String)dp);
		write(writer, value.toString());
	}
	
	private String format(Object value, String datePattern) {
		try {
			return new SimpleDateFormat(datePattern).format(value);
		} catch (Exception e) {
			throw new TemplateException(e.getMessage(), location, e);
		}
	}
}








