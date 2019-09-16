package com.goodcol.log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.goodcol.util.AppUtils;
import com.jfinal.plugin.activerecord.Record;



/**
 * 用于处理格式化日志格式数据,底层参考Record方式
 * 
 * @author chang
 * @version 2017-09-21
 */

public  class LogPoint implements Serializable{
	
	private static final long serialVersionUID = 1L;
	//private static String LOG_POINT_SORT = "LOG_POINT,SOURCE_TYPE,SOURCE_WS_ID,SOURCE_SYS,TRAN_CODE,USRE_ID,CUSTOMER_ID,SEQ_NO,DEST_SYS,DEST_TXCODE,DURATION,PROGRAM_LINE,RET_CODE,RET_MSG,ERROR_CODE,ERROR_MSG,LOCAL_IP,REMOTE_IP,EVENT_INFO";
	private static String [] defultSortKeys = {"LOG_POINT","SOURCE_TYPE","SOURCE_WS_ID","SOURCE_SYS","TRAN_CODE","USRE_ID","CUSTOMER_ID","SEQ_NO","DEST_SYS","DEST_TXCODE","DURATION","PROGRAM_LINE","RET_CODE","RET_MSG","ERROR_CODE","ERROR_MSG","LOCAL_IP","REMOTE_IP","EVENT_INFO"};
	private Map<String,Object> columns = new HashMap<String,Object>();
	
	
	/**
	 * 日志记录点
	 */
	public interface POINT {
		/**
		 * 本地入口
		 */
		public static String ENTRY = "ENTRY";
		/**
		 * 调用其它
		 */
		public static String START_CALL = "START_CALL";
		/**
		 * 调用结束
		 */
		public static String END_CALL = "END_CALL";
		/**
		 * 出错时
		 */
		public static String ON_ERROR = "ON_ERROR";
		/**
		 * 本地出口
		 */
		public static String EXIT = "EXIT";
		/**
		 * 其他情况
		 */
		public static String OTHER = "OTHER";
	}
	/**
	 * 测试默认填充信息
	 */
	public static String UNKNOWN="Unknown";
	
	/**
	 * 登记时间记录KEY
	 */
	private String LOGDATE="LOGDATE";
	/**
	 * 登记CLASS NAME KEY，如使用 默认组合[模块名][时间][关键要素]
	 */
	public String CLASSNAME="CLASSNAME";
	
	/**
	 * 日志记录点
	 */
	public static String LOG_POINT="LOG_POINT";
	/**
	 * 渠道类型
	 */
	public static String SOURCE_TYPE="SOURCE_TYPE"; 
	/**
	 * 终端标识
	 */
	public static String SOURCE_WS_ID="SOURCE_WS_ID";
	/**
	 * 源系统系统编号
	 */
	public static String SOURCE_SYS="SOURCE_SYS";
	
	/**
	 * 本系统接收要处理交易
	 */
	public static String TRAN_CODE="TRAN_CODE";
	/**
	 * 操作用户ID
	 */
	public static String USRE_ID="USRE_ID";
	/**
	 * 客户号
	 */
	public static String CUSTOMER_ID="CUSTOMER_ID";
	/**
	 * 流水号
	 */
	public static String SEQ_NO="SEQ_NO";
	/**
	 * 请求服务的目标系统或本地服务名称
	 */
	public static String DEST_SYS="DEST_SYS";
	/**
	 * 要调用的交易码(如果调用本地函数则登记调用的函数名)
	 */
	public static String DEST_TXCODE="DEST_TXCODE";
	/**
	 * 毫秒，表示调用所用时间
	 */
	public static String DURATION="DURATION";
	/**
	 * 本系统运行时程序所在行数
	 */
	public static String PROGRAM_LINE="PROGRAM_LINE";
	/**
	 * 本地处理后或调用后返回码
	 */
	public static String RET_CODE="RET_CODE";
	/**
	 * 本地处理后或调用后返回信息
	 */
	public static String RET_MSG="RET_MSG";
	/**
	 * 本地处理后或调用后返回技术错误返回码
	 */
	public static String ERROR_CODE="ERROR_CODE";
	/**
	 * 本地处理后或调用后返回技术错误信息
	 */
	public static String ERROR_MSG="ERROR_MSG";
	/**
	 * 本地IP
	 */
	public static String LOCAL_IP="LOCAL_IP";
	/**
	 *请求方IP
	 */
	public static String REMOTE_IP="REMOTE_IP";
	/**
	 * 用户自定义事件描述
	 */
	public static String EVENT_INFO="EVENT_INFO";
	
	
	
	public LogPoint(){
		
	}
	
	
	
	/**
	 * 
	 * @param LOG_POINT      日志记录点
	 * @param SOURCE_TYPE    渠道类型
	 * @param SOURCE_WS_ID   终端标识
	 * @param SOURCE_SYS     源系统系统编号
	 * @param TRAN_CODE      本系统接收要处理交易
	 * @param USRE_ID        操作用户ID
	 * @param CUSTOMER_ID    客户号
	 * @param SEQ_NO         流水号
	 * @param DEST_SYS       请求服务的目标系统或本地服务名称
	 * @param DEST_TXCODE    要调用的交易码(如果调用本地函数则登记调用的函数名)
	 * @param DURATION       耗时（一般在结束出口时记录）
	 * @param PROGRAM_LINE   异常发生时行号
	 * @param RET_CODE		   本地处理后或调用后返回码
	 * @param RET_MSG        本地处理后或调用后返回信息
	 * @param ERROR_CODE     本地处理后或调用后返回技术错误返回码
	 * @param ERROR_MSG      本地处理后或调用后返回技术错误信息
	 * @param LOCAL_IP       本地IP
	 * @param REMOTE_IP      请求方IP
	 * @param EVENT_INFO     用户自定义事件描述
	 * @return
	 */
	public LogPoint pointAll (String LOG_POINT,String SOURCE_TYPE,String SOURCE_WS_ID,String SOURCE_SYS,String TRAN_CODE,String USRE_ID,String CUSTOMER_ID,String SEQ_NO,String DEST_SYS,String DEST_TXCODE,String DURATION,String PROGRAM_LINE,String RET_CODE,String RET_MSG,String ERROR_CODE,String ERROR_MSG,String LOCAL_IP,String REMOTE_IP,String EVENT_INFO){
		columns.clear();
		columns.put(LogPoint.LOG_POINT, LOG_POINT);
		columns.put(LogPoint.SOURCE_TYPE, SOURCE_TYPE);
		columns.put(LogPoint.SOURCE_WS_ID, SOURCE_WS_ID);
		columns.put(LogPoint.SOURCE_SYS, SOURCE_SYS);
		columns.put(LogPoint.TRAN_CODE, TRAN_CODE);
		columns.put(LogPoint.USRE_ID, USRE_ID);
		columns.put(LogPoint.CUSTOMER_ID, CUSTOMER_ID);
		columns.put(LogPoint.SEQ_NO, SEQ_NO);
		columns.put(LogPoint.DEST_TXCODE, DEST_TXCODE);
		columns.put(LogPoint.PROGRAM_LINE, PROGRAM_LINE);
		columns.put(LogPoint.RET_MSG, RET_MSG);
		columns.put(LogPoint.ERROR_CODE, ERROR_CODE);
		columns.put(LogPoint.ERROR_MSG, ERROR_MSG);
		columns.put(LogPoint.LOCAL_IP, LOCAL_IP);
		columns.put(LogPoint.REMOTE_IP, REMOTE_IP);
		columns.put(LogPoint.EVENT_INFO, EVENT_INFO);
		columns.put(this.LOGDATE, new Date());//记录日志记录时间
		return this;
	}
	 
	/**
	 * 提供继承类传入接口
	 * @param point
	 * @return
	 */
	public LogPoint point(LogPoint point){
		this.columns = point.getColumns();
		return this;
	}
	
	/**
	 * 本地入口，在开始时记录
	 * @param SOURCE_TYPE    渠道类型
	 * @param SOURCE_WS_ID   终端标识
	 * @param SOURCE_SYS     源系统系统编号
	 * @param TRAN_CODE      本系统接收要处理交易
	 * @param USRE_ID        操作用户ID
	 * @param CUSTOMER_ID    客户号
	 * @param SEQ_NO         流水号
	 * @param LOCAL_IP       本地IP
	 * @param REMOTE_IP      请求方IP
	 * @param EVENT_INFO     用户自定义事件描述
	 * @return 当前对象
	 */
	public LogPoint entry(String SOURCE_TYPE,String SOURCE_WS_ID,String SOURCE_SYS,String TRAN_CODE,String USRE_ID,String CUSTOMER_ID,String SEQ_NO,String LOCAL_IP,String REMOTE_IP,String EVENT_INFO) {
		if(AppUtils.StringUtil(LOCAL_IP)==null){
			LOCAL_IP =  getLocIP();
		}
		pointAll(LogPoint.POINT.ENTRY,SOURCE_TYPE,SOURCE_WS_ID,SOURCE_SYS,TRAN_CODE,USRE_ID,CUSTOMER_ID,SEQ_NO,null,null,null,null,null,null,null,null,LOCAL_IP,REMOTE_IP,EVENT_INFO);
		return this;
	}
	/**
	 * 结束，或者本地出口时记录
	 * @param SOURCE_TYPE    渠道类型
	 * @param SOURCE_WS_ID   终端标识
	 * @param SOURCE_SYS     源系统系统编号
	 * @param TRAN_CODE      本系统接收要处理交易
	 * @param USRE_ID        操作用户ID
	 * @param CUSTOMER_ID    客户号
	 * @param SEQ_NO         流水号
	 * @param DURATION       耗时（一般在结束出口时记录）
	 * @param RET_CODE		   本地处理后或调用后返回码
	 * @param RET_MSG        本地处理后或调用后返回信息
	 * @param ERROR_CODE     本地处理后或调用后返回技术错误返回码
	 * @param ERROR_MSG      本地处理后或调用后返回技术错误信息
	 * @param EVENT_INFO     用户自定义事件描述
	 * @return 当前对象
	 */
	public LogPoint exit(String SOURCE_TYPE,String SOURCE_WS_ID,String SOURCE_SYS,String TRAN_CODE,String USRE_ID,String CUSTOMER_ID,String SEQ_NO,String DURATION,String RET_CODE,String RET_MSG,String ERROR_CODE,String ERROR_MSG,String EVENT_INFO) {
		pointAll(LogPoint.POINT.EXIT, SOURCE_TYPE, SOURCE_WS_ID, SOURCE_SYS, TRAN_CODE, USRE_ID, CUSTOMER_ID, SEQ_NO, null, null,
				DURATION, null, RET_CODE, RET_MSG, ERROR_CODE, ERROR_MSG, null, null, EVENT_INFO);
		return this;
	}
	
	/**
	 * 结束，或者本地出口时记录(正常信息记录，简洁版本，保留主要录入信息,basectrl 专用)
	 * @param EVENT_INFO
	 * @return
	 */
	public LogPoint exit(String EVENT_INFO){
		set(LogPoint.LOG_POINT, LogPoint.POINT.EXIT);
		set(LogPoint.EVENT_INFO, EVENT_INFO);
		return this;
	}
	
	/**
	 * 结束访问其他系统时登记
	 * @param SEQ_NO         流水号
	 * @param DEST_SYS       请求服务的目标系统或本地服务名称
	 * @param DEST_TXCODE    要调用的交易码(如果调用本地函数则登记调用的函数名)
	 * @param DURATION       耗时（一般在结束出口时记录）
	 * @param RET_CODE		   本地处理后或调用后返回码
	 * @param RET_MSG        本地处理后或调用后返回信息
	 * @param EVENT_INFO     用户自定义事件描述
	 * @return 当前对象
	 */
	public LogPoint endCall(String SEQ_NO,String DEST_SYS,String DEST_TXCODE,String DURATION,String RET_CODE,String RET_MSG,String EVENT_INFO) {
		pointAll(LogPoint.POINT.END_CALL, null, null, null, null, null, null, SEQ_NO, DEST_SYS, DEST_TXCODE,
				DURATION, null, RET_CODE, RET_MSG, null, null, null, null, EVENT_INFO);
		return this;
	}
	
	/**
	 * 发生异常时登记
	 * @param SOURCE_TYPE    渠道类型
	 * @param SOURCE_WS_ID   终端标识
	 * @param SOURCE_SYS     源系统系统编号
	 * @param TRAN_CODE      本系统接收要处理交易
	 * @param USRE_ID        操作用户ID
	 * @param CUSTOMER_ID    客户号
	 * @param SEQ_NO         流水号
	 * @param PROGRAM_LINE   异常发生时行号
	 * @param ERROR_CODE     本地处理后或调用后返回技术错误返回码
	 * @param ERROR_MSG      本地处理后或调用后返回技术错误信息
	 * @param LOCAL_IP       本地IP
	 * @param REMOTE_IP      请求方IP
	 * @param EVENT_INFO     用户自定义事件描述
	 * @return 当前对象
	 */
	public LogPoint onError(String SOURCE_TYPE,String SOURCE_WS_ID,String SOURCE_SYS,String TRAN_CODE,String USRE_ID,String CUSTOMER_ID,String SEQ_NO,String PROGRAM_LINE,String ERROR_CODE,String ERROR_MSG,String LOCAL_IP,String REMOTE_IP,String EVENT_INFO) {
		if(AppUtils.StringUtil(LOCAL_IP)==null){
			LOCAL_IP =  getLocIP();
		}
		pointAll(LogPoint.POINT.ON_ERROR, SOURCE_TYPE, SOURCE_WS_ID, SOURCE_SYS, TRAN_CODE, USRE_ID, CUSTOMER_ID, SEQ_NO, null, null,null, PROGRAM_LINE, null, null, ERROR_CODE, ERROR_MSG, LOCAL_IP, REMOTE_IP, EVENT_INFO);
		return this;
	}
	
	/**
	 * 
	 * @param TRAN_CODE      本系统接收要处理交易
	 * @param SEQ_NO         流水号
	 * @param EVENT_INFO     用户自定义事件描述
	 * @return 当前对象
	 */
	public LogPoint other(String TRAN_CODE,String SEQ_NO,String EVENT_INFO) {
		pointAll(LogPoint.POINT.OTHER, null, null, null, TRAN_CODE, null, null, SEQ_NO, null, null,null, null, null, null, null, null, null, null, EVENT_INFO);
		return this;
	}
	
	/**
	 * 访问其他系统开始时登记
	 * @param SEQ_NO         流水号
	 * @param DEST_SYS       请求服务的目标系统或本地服务名称
	 * @param DEST_TXCODE    要调用的交易码(如果调用本地函数则登记调用的函数名)
	 * @param EVENT_INFO     用户自定义事件描述
	 * @return 当前对象
	 */
	public LogPoint startCall(String SEQ_NO,String DEST_SYS,String DEST_TXCODE,String EVENT_INFO) {
		pointAll(LogPoint.POINT.START_CALL, null, null, null, null, null, null, SEQ_NO, DEST_SYS, DEST_TXCODE,null, null, null, null, null, null, null, null, EVENT_INFO);
		return this;
	}
	
	/**
	 * Return columns map.
	 */
	public Map<String,Object> getColumns(){
		return columns;
	}
	
	/**
	 * 传入开始时间，将计算结果加入 DURATION 键值存储耗时
	 * 计算方式 当前日志记录时间（long）-传入时间=耗时（long） /毫秒
	 * @param startTime
	 * @return
	 */
	public LogPoint setComputeExitDuration(Long startTime){
		Long tmp = getLogDateToLong()-startTime;
		columns.put(DURATION, String.valueOf(tmp)) ;
		return this;
	}
	
	/**
	 * 设置CLASS NAME 参数，当该参数被启用，toString 时将在[关键要素]前加[模块名][时间]
	 * @param className 例如： com.goodcol.util.log.LogPoint
	 * @return
	 */
	public LogPoint setClassName(String className){
		columns.put(CLASSNAME, className) ;
		return this;
	}
	
	
	/**
	 * Set columns value with map.
	 * @param columns the columns map
	 */
	public LogPoint setColumns(Map<String, Object> columns) {
		this.columns.putAll(columns);
		return this;
	}
	
	/**
	 * Set columns value with record.
	 * @param record the record
	 */
	public LogPoint setColumns(Record record) {
		columns.putAll(record.getColumns());
		return this;
	}
	
	/**
	 * Remove attribute of this record.
	 * @param column the column name of the record
	 */
	public LogPoint remove(String column) {
		columns.remove(column);
		return this;
	}
	
	/**
	 * Remove columns of this record.
	 * @param columns the column names of the record
	 */
	public LogPoint remove(String... columns) {
		if (columns != null)
			for (String c : columns)
				this.columns.remove(c);
		return this;
	}
	
	/**
	 * Set key value to columns
	 * @param key
	 * @param value
	 * @return
	 */
	public LogPoint set(String key,Object value){
		this.columns.put(key, value);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String key){
		return (T)this.columns.get(key);
	}
	
	public String getStr(String key){
		return (String)this.columns.get(key);
	}
	
	/**
	 * 获取本地IP
	 * <br/>
	 * 调取AppUtils.getIPAddress()函数获取IP
	 * @return
	 */
	public String getLocIP(){
		return AppUtils.getIPAddress();
	}
	
	/**
	 * 获取记录时间
	 *<br/>
	 * 每一笔记录创建时均会记录登记时间
	 */
	public Date getLogDate(){
		return (Date)get(this.LOGDATE);
	}
	
	/**
	 * 获取记录时间Long
	 */
	public Long getLogDateToLong(){
		return getLogDate().getTime();
	}
	
	/**
	 * 覆盖 父类 toString方法，如果输入CLASS NAME 参数，在前面组合 [模块名][时间]
	 * 按照默认排序进行 KEY=VALUE 格式组合字符串
	 * 
	 */
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		if(AppUtils.StringUtil(getStr(CLASSNAME))!=null){
			String logDate =  new SimpleDateFormat("yyyy-MM-dd HH-mm-ss SSS").format(getLogDate());
			sb.append("["+getStr(CLASSNAME)+"]");
			sb.append("["+logDate+"]");
		}
		sb.append("[");
		String val = null;
		boolean isFirst = Boolean.TRUE;
		for (String key : defultSortKeys) {//按照默认排序组合参数
			if(this.columns.containsKey(key)){
				val = (String)this.columns.get(key);
				if(val !=null && val.trim().length()>0){
					if(isFirst)
						isFirst = Boolean.FALSE;
					else
						sb.append(" ");
					sb.append(key+"="+checkVal(val));
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * 替换关键字符
	 * <br/>
	 * 1.如果值中包含有空格在其外部加双引号包裹
	 * <br/>
	 * 2.如果值中存在反斜杠，双引号，等于号在其前面加反斜杠
	 * 
	 * @param val
	 * @return
	 */
	public String checkVal(String val){
		
		
		if(val.contains("\\")){
			val = val.replaceAll("\\\\","\\\\\\\\");
		}
		
		if(val.contains("\"")){
			val = val.replaceAll("\"", "\\\\\"");

		}
		
		if(val.contains("=")){
			val = val.replaceAll("=", "\\\\=");
		}
		
		//因前面会替换双引号将双引号放在最后处理
		if(val.contains(" ")){
			val = "\""+val+"\"";
		}
		
		return val;
	}
	
	
	public static void main(String[] args) {
		//使用示例
		//System.out.println(new LogPoint().onError(SOURCE_TYPE, SOURCE_WS_ID, SOURCE_SYS, TRAN_CODE, USRE_ID, CUSTOMER_ID, SEQ_NO, PROGRAM_LINE, ERROR_CODE, ERROR_MSG, null, REMOTE_IP, EVENT_INFO).set("sss", "aa").set(LogPoint.ERROR_MSG, "ssssssssssssss").toString());
	}
}
