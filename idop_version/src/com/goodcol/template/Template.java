
package com.goodcol.template;

import java.io.Writer;
import java.util.Map;
import com.goodcol.template.stat.Scope;
import com.goodcol.template.stat.ast.Stat;

/**
 * Template
 * 
 * 用法：
 * Template template = Engine.use().getTemplate(...);
 * template.render(data, writer);
 * template.renderToString(data);
 */
public class Template {
	
	private Env env;
	private Stat ast;
	
	public Template(Env env, Stat ast) {
		if (env == null || ast == null) {
			throw new IllegalArgumentException("env and ast can not be null");
		}
		this.env = env;
		this.ast = ast;
	}
	
	/**
	 * 渲染到 Writer 中去
	 */
	public void render(Map<?, ?> data, Writer writer) {
		ast.exec(env, new Scope(data, env.engineConfig.sharedObjectMap), writer);
	}
	
	/**
	 * 渲染到 FastStringWriter 中去
	 */
	public void render(Map<?, ?> data, FastStringWriter fastStringWriter) {
		ast.exec(env, new Scope(data, env.engineConfig.sharedObjectMap), fastStringWriter);
	}
	
	/**
	 * 渲染到 StringBuilder 中去
	 */
	public StringBuilder renderToStringBuilder(Map<?, ?> data) {
		FastStringWriter fsw = new FastStringWriter();
		render(data, fsw);
		return fsw.getBuffer();
	}
	
	/**
	 * 渲染到 String 中去
	 */
	public String renderToString(Map<?, ?> data) {
		return renderToStringBuilder(data).toString();
	}
	
	boolean isModified() {
		return env.isTemplateFileModified();
	}
}





