
package com.goodcol.template.stat.ast;

import java.io.Writer;
import com.goodcol.template.Env;
import com.goodcol.template.TemplateException;
import com.goodcol.template.stat.Location;
import com.goodcol.template.stat.ParseException;
import com.goodcol.template.stat.Scope;
import com.goodcol.template.expr.ast.Expr;
import com.goodcol.template.expr.ast.ExprList;
import com.goodcol.template.expr.ast.Id;

/**
 * Define 定义模板函数：
 * #define funcName(p1, p2, ..., pn)
 *   body
 * #end
 * 
 * 模板函数类型：
 * 1：全局共享的模板函数
 *   通过 engine.addSharedFunction(...) 添加，所有模板中可调用
 * 2：模板中定义的局部模板函数
 *   在模板中定义的模板函数，只在本模板中有效
 * 
 * 高级用法：
 * 1：局部模板函数可以与全局共享模板函数同名，调用时优先调用模板内模板数
 * 2：模板内部不能定义同名的局部模板函数
 */
public class Define extends Stat {
	
	private static final String[] NULL_PARAMETER_NAMES = new String[0];
	
	private String functionName;
	private String[] parameterNames;
	private Stat stat;
	
	public Define(String functionName, ExprList exprList, Stat stat, Location location) {
		setLocation(location);
		this.functionName = functionName;
		this.stat = stat;
		
		Expr[] exprArray = exprList.getExprArray();
		if (exprArray.length == 0) {
			this.parameterNames = NULL_PARAMETER_NAMES;
			return ;
		}
		
		parameterNames = new String[exprArray.length];
		for (int i=0; i<exprArray.length; i++) {
			if (exprArray[i] instanceof Id) {
				parameterNames[i] = ((Id)exprArray[i]).getId();
			} else {
				throw new ParseException("The parameter of template function definition must be identifier", location);
			}
		}
	}
	
	public String getFunctionName() {
		return functionName;
	}
	
	public String[] getParameterNames() {
		return parameterNames;
	}
	
	/**
	 * Define 的继承类可以覆盖此方法实现一些 register 类的动作
	 */
	public void exec(Env env, Scope scope, Writer writer) {
		
	}
	
	/**
	 * 真正调用模板函数
	 */
	public void call(Env env, Scope scope, ExprList exprList, Writer writer) {
		if (exprList.length() != parameterNames.length) {
			throw new TemplateException("Wrong number of argument to call the template function, right number is: " + parameterNames.length, location);
		}
		
		scope = new Scope(scope);
		if (exprList.length() > 0) {
			Object[] parameterValues = exprList.evalExprList(scope);
			for (int i=0; i<parameterValues.length; i++) {
				scope.setLocal(parameterNames[i], parameterValues[i]);	// 参数赋值
			}
		}
		
		stat.exec(env, scope, writer);
		scope.getCtrl().setJumpNone();	// #define 中的 return、continue、break 全部在此消化
	}
	
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append("#define ").append(functionName).append("(");
		for (int i=0; i<parameterNames.length; i++) {
			if (i > 0) {
				ret.append(", ");
			}
			ret.append(parameterNames[i]);
		}
		return ret.append(")").toString();
	}
}



