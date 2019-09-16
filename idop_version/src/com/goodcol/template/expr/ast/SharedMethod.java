
package com.goodcol.template.expr.ast;

import com.goodcol.template.TemplateException;
import com.goodcol.template.expr.ast.SharedMethodKit.SharedMethodInfo;
import com.goodcol.template.stat.Location;
import com.goodcol.template.stat.ParseException;
import com.goodcol.template.stat.Scope;

/**
 * SharedMethod
 * 
 * 用法：
 * engine.addSharedMethod(object);
 * engine.addSharedStaticMethod(Xxx.class);
 * #(method(para))
 */
public class SharedMethod extends Expr {
	
	private SharedMethodKit sharedMethodKit;
	private String methodName;
	private ExprList exprList;
	
	public SharedMethod(SharedMethodKit sharedMethodKit, String methodName, ExprList exprList, Location location) {
		if (MethodKit.isForbiddenMethod(methodName)) {
			throw new ParseException("Forbidden method: " + methodName, location); 
		}
		this.sharedMethodKit = sharedMethodKit;
		this.methodName = methodName;
		this.exprList = exprList;
		this.location = location;
	}
	
	public Object eval(Scope scope) {
		Object[] argValues = exprList.evalExprList(scope);
		SharedMethodInfo sharedMethodInfo = sharedMethodKit.getSharedMethodInfo(methodName, argValues);
		
		// ShareMethod 相当于是固定的静态的方法，不支持 null safe，null safe 只支持具有动态特征的用法
		if (sharedMethodInfo == null) {
			throw new TemplateException("Shared method not found: " + methodName, location);
		}
		try {
			return sharedMethodInfo.invoke(argValues);
		} catch (Exception e) {
			throw new TemplateException(e.getMessage(), location, e);
		}
	}
}




