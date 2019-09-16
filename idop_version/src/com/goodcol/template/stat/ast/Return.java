
package com.goodcol.template.stat.ast;

import java.io.Writer;
import com.goodcol.template.Env;
import com.goodcol.template.stat.Scope;

/**
 * Return
 * 通常用于 #define 指令内部，不支持返回值
 */
public class Return  extends Stat {
	
	public static final Return me = new Return();
	
	private Return() {
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		scope.getCtrl().setReturn();
	}
}







