
package com.goodcol.log;

/**
 * LogManager.
 */
public class LogManager {
	
	private static final LogManager me = new LogManager();
	
	private LogManager() {}
	
	public static LogManager me() {
		return me;
	}
	
	public void init() {
		Log.init();
	}
	
	public void setDefaultLogFactory(ILogFactory defaultLogFactory) {
		Log.setDefaultLogFactory(defaultLogFactory);
		com.goodcol.kit.LogKit.synchronizeLog();
	}
}


