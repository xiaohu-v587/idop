package com.goodcol.util;



import javax.servlet.http.HttpServletRequest;

import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;

/**
 * 操作记录
 * @author first blush
 */
public class LoggerUtil{
	private static LoggerUtil logger = null;
	protected Logger log =  Logger.getLogger(LoggerUtil.class);
	private LoggerUtil(){
		
	}
	
	public static LoggerUtil getIntanceof(){
		if(logger==null){
			logger = new LoggerUtil();
		}
		return logger;
	}
	
	/**
	 *操作记录日志 
	 * **/
	public String saveLogger(String user_no,String menuname,String type,String remark){
		
		String id = AppUtils.getStringSeq();
		String sql ="insert into SYS_LOG_INFO(ID,USER_NO,CREATE_TIME,MENUNAME,TYPE,REMARK)values(?,?,to_char(SYSDATE, 'yyyymmddhh24miss'),?,?,?)";
		Db.update(sql, new Object[]{id,user_no,menuname,type,remark});
		return id;
	}
	
	public void warn(HttpServletRequest request,String info){
		logByLevel(request.getAttribute("uniqueKeyForLog") + info, "warn");
	}

	public void info(HttpServletRequest request,String info){
		logByLevel(request.getAttribute("uniqueKeyForLog") + info, "info");
	}
	
	public void error(HttpServletRequest request,String info){
		logByLevel(request.getAttribute("uniqueKeyForLog") + info, "error");
	}
	
	public void debug(HttpServletRequest request,String info){
		logByLevel(request.getAttribute("uniqueKeyForLog") + info, "debug");
	}
	/**
	 * 用于在方法中记录相关日志信息
	 * @param c
	 * @param request
	 * @param level=LOGLEVEL_INFO LOGLEVEL_WARN LOGLEVEL_ERROR LOGLEVEL_DEBUG
	 * @param info
	 */
	public void logByLevel(String info,String level){
		switch (level) {
			case "debug":
				log.debug(info);
				break;
			case "info":
				log.info(info);
				break;
			case "warn":
				log.warn(info);
				break;
			case "error":
				log.error(info);
				break;
			default:
				log.warn(info);
				break;
		}
	}
}
