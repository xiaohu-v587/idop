
package com.goodcol.template.stat.ast;

import java.io.Writer;
import com.goodcol.template.Env;
import com.goodcol.template.stat.Scope;

/**
 * Continue
 */
public class Continue extends Stat {
	
	public static final Continue me = new Continue();
	
	private Continue() {
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		scope.getCtrl().setContinue();
	}
}




