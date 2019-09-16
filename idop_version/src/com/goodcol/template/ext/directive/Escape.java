
package com.goodcol.template.ext.directive;

import java.io.Writer;
import com.goodcol.template.Directive;
import com.goodcol.template.Env;
import com.goodcol.template.stat.Scope;

/**
 * Escape 对字符串进行转义
 * 用法:
 * #escape(value)
 */
public class Escape extends Directive {
	
	public void exec(Env env, Scope scope, Writer writer) {
		Object value = exprList.eval(scope);
		if (value != null) {
			write(writer, escape(value.toString()));
		}
	}
	
	// TODO 挪到 StrKit 中
	private String escape(String str) {
		if (str == null || str.length() == 0) {
			return str;
		}
		
		int len = str.length();
		StringBuilder ret = new StringBuilder(len * 2);
		for (int i = 0; i < len; i++) {
			char cur = str.charAt(i);
			switch (cur) {
			case '<':
				ret.append("&lt;");
				break;
			case '>':
				ret.append("&gt;");
				break;
			case '\"':
				ret.append("&quot;");
				break;
			case '\'':
				ret.append("&apos;");	// IE 不支持 &apos; 考虑 &#39;
				break;
			case '&':
				ret.append("&amp;");
				break;
			default:
				ret.append(cur);
				break;
			}
		}

		return ret.toString();
	}
}
