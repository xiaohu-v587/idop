
package com.goodcol.log;

/**
 * Log4jLogFactory.
 */
public class Log4jLogFactory implements ILogFactory {
	
	public Log getLog(Class<?> clazz) {
		return new Log4jLog(clazz);
	}
	
	public Log getLog(String name) {
		return new Log4jLog(name);
	}
}
