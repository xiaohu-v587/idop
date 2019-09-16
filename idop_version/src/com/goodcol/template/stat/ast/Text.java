package com.goodcol.template.stat.ast;

import java.io.Writer;
import com.goodcol.template.Env;
import com.goodcol.template.stat.Scope;

/**
 * Text 输出纯文本块以及使用 "#[[" 与 "]]#" 指定的非解析块 
 */
public class Text extends Stat {
	
	private char[] text;
	
	public Text(StringBuilder content) {
		this.text = new char[content.length()];
		content.getChars(0, content.length(), this.text, 0);
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		write(writer, text);
	}
	
	public String toString() {
		return text != null ? new String(text) : "";
	}
}



