
package com.goodcol.template.stat.ast;

import java.io.Writer;
import com.goodcol.template.Env;
import com.goodcol.template.expr.ast.ExprList;
import com.goodcol.template.stat.Location;
import com.goodcol.template.stat.ParseException;
import com.goodcol.template.stat.Scope;

/**
 * Output 输出指令
 * 
 * 用法：
 * 1：#(value)
 * 2：#(x = 1, y = 2, x + y)
 * 3：#(seoTitle ?? '  极速开发社区')
 */
public class Output extends Stat {
	
	private ExprList exprList;
	
	public Output(ExprList exprList, Location location) {
		if (exprList.length() == 0) {
			throw new ParseException("The expression of output directive like #(expression) can not be blank", location);
		}
		this.exprList = exprList;
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		Object value = exprList.eval(scope);
		if (value != null) {
			write(writer, value.toString());
		}
	}
}




