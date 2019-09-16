
package com.goodcol.template.ext.directive;

import java.io.Writer;
import com.goodcol.template.Directive;
import com.goodcol.template.Env;
import com.goodcol.template.stat.Scope;

/**
 * 输出随机数
 */
public class Random extends Directive {
	
	private java.util.Random random = new java.util.Random();
	
	public void exec(Env env, Scope scope, Writer writer) {
		write(writer, String.valueOf(random.nextInt()));
	}
}



