 package com.goodcol.util.ext.plugin.sqlXml;

import com.goodcol.core.plugin.IPlugin;

public class SqlInXmlPlugin implements IPlugin {
	@Override
	public boolean start() {
		try {
			SqlManager.parseSqlXml();
		} catch (Exception e) {
			new RuntimeException(e);
		}
		return true;
	}
	@Override
	public boolean stop() {
		SqlManager.clearSqlMap();
		return true;
	}
}
