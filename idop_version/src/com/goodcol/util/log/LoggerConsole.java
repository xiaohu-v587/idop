 package com.goodcol.util.log;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.goodcol.util.PropertiesContent;


public class LoggerConsole {
 
	private Logger logger;
	private String app = "";
	private String invokeInfo = "";

	public LoggerConsole(String app ) {
		this.app = app;
		logger = Logger.getLogger(this.app);

		Handler[] hs = logger.getHandlers();
		if (hs.length > 0)
			return;
		else {
			if (PropertiesContent.get("log.console").equalsIgnoreCase("true"))
				logger.setUseParentHandlers(true);
			else
				logger.setUseParentHandlers(false);

			FileHandler f = new FileHandler(this.app);
			logger.setLevel(f.getLevel());
			logger.addHandler(f);
		}
	}

	public void get(){
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		String thisClassName = Logger.class.getName();
		String className="";
		String methodName="";
		for(int i=0;i<stack.length;i++){
			className = stack[i].getClassName();
			methodName = stack[i].getMethodName();
			if( className.equals(thisClassName)   && methodName.equals("getLogger")){
				if( i+1 > stack.length)
					break;
				className = stack[i+1].getClassName();
				methodName = stack[i+1].getMethodName();
				break;
			}
		}
	}
	public void debug(String msg, Throwable ex) {
		logger.log(Level.CONFIG,  invokeInfo + "- ["+Level.CONFIG.getName()+"] - " +msg, ex);
	}

	public void debug(String msg) {
		logger.log(Level.CONFIG,  invokeInfo + "- ["+Level.CONFIG.getName()+"] - " +msg);
	}

	public void debug(Throwable ex) {
		if (ex == null) {
			logger.log(Level.CONFIG, null);
		} else {
			logger.log(Level.CONFIG, invokeInfo + "- ["+Level.CONFIG.getName()+"] - " +ex.getMessage(), ex);
		}
	}

	public void debug(String msg, Object... params) {
		logger.log(Level.CONFIG,  invokeInfo + "- ["+Level.CONFIG.getName()+"] - " +msg, params);
	}

	public void error(String msg, Throwable ex) {
		logger.log(Level.SEVERE,  invokeInfo + "- ["+Level.SEVERE.getName()+"] - " +msg, ex);
	}

	public void error(String msg) {
		logger.log(Level.SEVERE, invokeInfo + "- ["+Level.SEVERE.getName()+"] - " +msg);
	}

	public void error(Throwable ex) {
		if (ex == null) {
			logger.log(Level.SEVERE, null);
		} else {
			logger.log(Level.SEVERE, invokeInfo + "- ["+Level.SEVERE.getName()+"] - " +ex.getMessage(), ex);
		}
	}

	public void error(String msg, Object... params) {
		logger.log(Level.SEVERE, invokeInfo + "- ["+Level.SEVERE.getName()+"] - " +msg, params);
	}

	public void info(String msg, Throwable ex) {
		logger.log(Level.INFO, invokeInfo + "- ["+Level.INFO.getName()+"] - " +msg, ex);
	}

	public void info(String msg) {
		logger.log(Level.INFO,  invokeInfo + "- ["+Level.INFO.getName()+"] - " +msg);
	}

	public void info(Throwable ex) {
		if (ex == null) {
			logger.log(Level.INFO, null);
		} else {
			logger.log(Level.INFO,  invokeInfo + "- ["+Level.INFO.getName()+"] - " +ex.getMessage(), ex);
		}
	}

	public void info(String msg, Object... params) {
		logger.log(Level.INFO,  invokeInfo + "- ["+Level.INFO.getName()+"] - " +msg, params);
	}

	public void warn(String msg, Throwable ex) {
		logger.log(Level.WARNING,  invokeInfo + "- ["+Level.WARNING.getName()+"] - " +msg, ex);
	}

	public void warn(String msg) {
		logger.log(Level.WARNING,  invokeInfo + "- ["+Level.WARNING.getName()+"] - "+msg);
	}

	public void warn(Throwable ex) {
		if (ex == null) {
			logger.log(Level.WARNING, null);
		} else {
			logger.log(Level.WARNING,  invokeInfo + "- ["+Level.WARNING.getName()+"] - " +ex.getMessage(), ex);
		}
	}

	public void warn(String msg, Object... params) {
		logger.log(Level.WARNING,  invokeInfo + "- ["+Level.WARNING.getName()+"] - "+msg, params);
	}

}
