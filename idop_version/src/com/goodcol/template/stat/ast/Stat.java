package com.goodcol.template.stat.ast;

import java.io.IOException;
import java.io.Writer;
import com.goodcol.template.Env;
import com.goodcol.template.TemplateException;
import com.goodcol.template.expr.ast.ExprList;
import com.goodcol.template.stat.Location;
import com.goodcol.template.stat.Scope;

/**
 * Stat
 */
public abstract class Stat {
	
	protected Location location;
	
	public Stat setLocation(Location location) {
		this.location = location;
		return this;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setExprList(ExprList exprList) {
	}
	
	public void setStat(Stat stat) {
	}
	
	public abstract void exec(Env env, Scope scope, Writer writer);
	
	public boolean hasEnd() {
		return false;
	}
	
	protected void write(Writer writer, String str) {
		try {
			writer.write(str);
		} catch (IOException e) {
			throw new TemplateException(e.getMessage(), location, e);
		}
	}
	
	protected void write(Writer writer, char[] chars) {
		try {
			writer.write(chars);
		} catch (IOException e) {
			throw new TemplateException(e.getMessage(), location, e);
		}
	}
}







