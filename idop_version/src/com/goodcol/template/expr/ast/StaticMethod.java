

package com.goodcol.template.expr.ast;

import com.goodcol.template.TemplateException;
import com.goodcol.template.stat.Location;
import com.goodcol.template.stat.ParseException;
import com.goodcol.template.stat.Scope;

/**
 * StaticMethod : ID_list : '::' ID '(' exprList? ')'
 * 用法： com.goodcol.kit.Str::isBlank("abc")
 */
public class StaticMethod extends Expr {
	
	private Class<?> clazz;
	private String methodName;
	private ExprList exprList;
	
	public StaticMethod(String className, String methodName, Location location) {
		init(className, methodName, ExprList.NULL_EXPR_LIST, location);
	}
	
	public StaticMethod(String className, String methodName, ExprList exprList, Location location) {
		if (exprList == null || exprList.length() == 0) {
			throw new ParseException("exprList can not be blank", location);
		}
		init(className, methodName, exprList, location);
	}
	
	private void init(String className, String methodName, ExprList exprList, Location location) {
		try {
			this.clazz = Class.forName(className);
		} catch (Exception e) {
			throw new ParseException(e.getMessage(), location, e);
		}
		this.methodName = methodName;
		this.exprList = exprList;
		this.location = location;
	}
	
	public Object eval(Scope scope) {
		Object[] argValues = exprList.evalExprList(scope);
		MethodInfo methodInfo;
		try {
			methodInfo = MethodKit.getMethod(clazz, methodName, argValues);
		} catch (Exception e) {
			throw new TemplateException(e.getMessage(), location, e);
		}
		
		// StaticMethod 是固定的存在，不支持 null safe，null safe 只支持具有动态特征的用法
		if (methodInfo == null) {
			throw new TemplateException("Can not found public static method with name " + methodName + " in class " + clazz.getName(), location);
		}
		if (!methodInfo.isStatic()) {
			throw new TemplateException(clazz.getName() + "." + methodName + " is not public static method", location);
		}
		
		try {
			return methodInfo.invoke(null, argValues);
		} catch (Exception e) {
			throw new TemplateException(e.getMessage(), location, e);
		}
	}
}




