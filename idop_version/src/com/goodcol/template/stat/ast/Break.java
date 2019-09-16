
package com.goodcol.template.stat.ast;

import java.io.Writer;
import com.goodcol.template.Env;
import com.goodcol.template.stat.Scope;

/**
 * Break
 * java 中 break、continue 可出现在 for 中的最后一行，不一定要套在 if 中
 */
public class Break extends Stat {
	
	public static final Break me = new Break();
	
	private Break() {
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		scope.getCtrl().setBreak();
	}
}



