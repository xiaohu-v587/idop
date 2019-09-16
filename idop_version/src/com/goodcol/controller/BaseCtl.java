package com.goodcol.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.core.core.Controller;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.DbKit;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.core.upload.UploadFile;
import com.goodcol.log.LogPoint;
import com.goodcol.render.JsonRender;
import com.goodcol.util.AppUtils;
import com.goodcol.util.CacheUtil;
import com.goodcol.util.PropertiesContent;
import com.goodcol.util.RedisUtil;
import com.goodcol.util.SysParamsTools;
import com.jfinal.plugin.activerecord.ActiveRecordException;



import net.sf.json.JSONObject;

public abstract class BaseCtl extends Controller {
	protected int pageSize = 10; // 分页用,默认10页
	protected String regex = ","; // 分割参数

	public abstract void index();

	@Override
	public void render(String view) {
		setAttr("root", getRequest().getContextPath());
		super.render(view);
	}

	@Override
	public void renderJsp(String view) {
		setAttr("root", getRequest().getContextPath());
		super.renderJsp(view);
	}

	public String getClientBrowser() {
		String agent = this.getRequest().getHeader("User-Agent");
		//StringTokenizer st = new StringTokenizer(agent, ";");
		//String useros = st.nextToken();
		// 得到用户的浏览器名
		//String userbrowser = st.nextToken();
		// 得到用户的操作系统名
		return agent;
	}

	/** 获取当前系统操作人 */
	public Record getCurrentUser() {
		String user_token = getCookie("user_token");
		if (user_token == null || user_token.equals(""))
			return null;
		Object r = CacheUtil.getCache(user_token, this.getRequest());
		if (r == null)
			return null;
		return (Record) r;
	}

	public JSONObject getJsonObj(String key) {
		String jStr = getPara(key);
		if (jStr == null || jStr.equals(""))
			return null;
		JSONObject obj = JSONObject.fromObject(jStr.replaceAll("\\[", "")
				.replaceAll("\\]", ""));
		return obj;
	}

	public void renderSuccessJsonMsg(String message) {
		setAttr("code", "0000");
		setAttr("desc", message);
		renderJson();
	}

	public void renderFailJsonMsg(String message) {
		setAttr("code", "9999");
		setAttr("desc", message);
		renderJson();
	}
	/**
	 * 处理上传附件
	 */
	public void uploadFile(){
		List<UploadFile> ufList = new ArrayList<>();
		String maxPost = "15MB";//单次文件流总大小
		//String maxFilePost = "5MB"; 
		try {
			int maxPostSize = PropertiesContent.getToInteger("maxPostSize", 15*1024*1024);
			maxPost = AppUtils.bytesToSize(maxPostSize);
			String uploadpath = PropertiesContent.get("upload_path");//上传文件地址
			ufList = getFiles(uploadpath, maxPostSize); //总大小
		} catch (Exception e) {
			renderText("<script>parent.callback(\"文件总大小不得超过"+maxPost+"\"); </script>");
			return;
		}
		String oldName = "";
		String newName = "";
		String filetype = "";
		String pid = getPara("pid","");
		String pidtype = getPara("pidtype","");
		// 新文件名后缀
		String filePrefix = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());// 时间
		
		// 循环获取上传的所有附件
		for (UploadFile uploadFile : ufList) {
			oldName = uploadFile.getFileName();
			File tempfile = uploadFile.getFile();   
			long fileSize = tempfile.length();
			filetype = oldName.substring(oldName.lastIndexOf(".")+1);
			newName = oldName.substring(0, oldName.indexOf(".")) + filePrefix+ oldName.substring(oldName.lastIndexOf("."));
			File dest = new File(uploadFile.getSaveDirectory() + newName);
			
			// 附件重命名
			boolean bool = uploadFile.getFile().renameTo(dest); 
			
			// 如果重命名失败则将上传的文件拷贝一份新文件
			if (!bool) { 
				fileCopy(uploadFile);
			}
			Db.update("insert into SYS_ATTACHMENTS (ID, PID, USERNO, FILENAME, FILEPATH, FILETYPE, FILESIZE, CREATEDATE, STATUS, PIDTYPE, UPLOADTYPE) values (sys_guid(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					pid,getCurrentUser().get("USER_NO"),newName,uploadFile.getSaveDirectory() + newName,filetype,fileSize,filePrefix,"1",pidtype,"0");//uploadType 为新增上传未删除的数据 0 未删除，1 已删除
			
		}
		renderNull();
		
	}
	
	
	
	public String saveFile(UploadFile up) throws Exception {
		String oldName = "";
		String newName = "";
		// 新文件名后缀
		String filePrefix = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());// 时间
		// 循环获取上传的所有附件
		oldName = up.getFileName();
		File tempfile = up.getFile();   
		long fileSize = tempfile.length();
		if (fileSize > 3 * 1024 * 1024) {
			renderText("<script>parent.callback(\"fileoverlarge\"); </script>");
			tempfile.delete();
			return null;
		}
		newName = oldName.substring(0, oldName.indexOf(".")) + filePrefix+ oldName.substring(oldName.indexOf("."));
		File dest = new File(up.getSaveDirectory() + newName);
		//String dest1 = dest.toString();
		boolean bool = up.getFile().renameTo(dest); // 附件重命名
		if (!bool) { // 如果重命名失败则将上传的文件拷贝一份新文件
			fileCopy(up);
		}
		return newName;
	}

	// 文件复制,在上传文件重命名失败时调用
	private void fileCopy(UploadFile up) {
		if (up.getFile() != null && up.getFile().isFile()) {
			FileChannel in = null;
			FileChannel out = null;
			FileInputStream inStream = null;
			FileOutputStream outStream = null;

			String oldName = up.getFileName();
			String filePrefix = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

			String newName = up.getSaveDirectory()+ oldName.substring(0, oldName.indexOf(".")) + filePrefix+ oldName.substring(oldName.indexOf("."));
			try {
				inStream = new FileInputStream(up.getFile());
				outStream = new FileOutputStream(new File(newName));

				in = inStream.getChannel();
				out = outStream.getChannel();

				in.transferTo(0, in.size(), out);
			} catch (IOException e) {
			} finally {
				try {
					in.close();
					inStream.close();
					out.close();
					outStream.close();
				} catch (IOException e) {

				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected Page<Record> defualtList(Map<String, Object> map){
		return defualtPaginate((String) map.get("selectSql"), (String) map.get("extrasql"), ((List<String>) map.get("sqlStr")).toArray());
	}
	
	
	
	/**
	 * 批量组合查询条件 by changxy 2017-1-13 <br/>isLike : true 条件为模糊 ，false 为精确 
	 */
	public void setFindPara(StringBuffer whereSql,List<String> sqlStr,boolean isLike,String...paras){
		if(paras.length>0){
			for (int i = 0; i < paras.length; i++) {
				if (AppUtils.StringUtil(getPara(paras[i])) != null) {
					if(isLike){
						whereSql.append(" and "+paras[i]+" like ? ");
						sqlStr.add("%" + getPara(paras[i]).trim() + "%");
					}else {
						whereSql.append(" and "+paras[i]+" = ? ");
						sqlStr.add(getPara(paras[i]).trim());
					}
					
				}
			}
		}
	}
	/**
	 * 批量组合查询条件 by changxy 2017-1-13 <br/>likeParas  条件为模糊 ，eqParas 为精确 
	 */
	public void setFindPara(StringBuffer whereSql,List<String> sqlStr,String [] likeParas,String [] eqParas){
		setFindPara(whereSql, sqlStr, true, likeParas);
		setFindPara(whereSql, sqlStr, false, eqParas);
	}
	
	/**
	 * 将传参取出放入 Record 中 by changxy  2017-01-18
	 * @param paras
	 * @return
	 */
	public Record getParaByParas(String [] paras){
		Record record = new Record();
		if(paras.length>0){
			for (String key : paras) {
				if(!key.isEmpty())record.set(key, getPara(key));
			}
		}
		return record;
	}
	
	/**
	 * 将传参取出放入 Record 中 by changxy  2019-07-18
	 * @param paras
	 * @return Record
	 */
	public Record getParaByParasBystr(String parastr){
		String [] paras = parastr.split(regex);
		Record record = new Record();
		if(paras.length>0){
			for (String key : paras) {
				if(!key.isEmpty())record.set(key, getPara(key));
			}
		}
		return record;
	}
	
	/**
	 * 自定义封装分页查询  by changxy 2017-1-13
	 * @param select
	 * @param sqlExceptSelect
	 * @param paras
	 */
	public Page<Record> defualtPaginate(String select, String sqlExceptSelect, Object... paras){
		Page<Record> r = paginate(select,sqlExceptSelect,paras);
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		return r;
	}
	
	/**
	 * 自定义封装分页查询  by changxy 2017-1-13
	 * @param select
	 * @param sqlExceptSelect
	 * @param paras
	 */
	public Page<Record> paginate(String select, String sqlExceptSelect, Object... paras){
		return Db.paginate(getParaToInt("pageIndex") + 1,getParaToInt("pageSize", pageSize), select, sqlExceptSelect,paras);
	}

	public void betweenPara(StringBuffer whereSql, List<String> sqlStr, String[] paras) {
		if(paras.length>0){
			for (int i = 0; i < paras.length; i++) {
				if(AppUtils.StringUtil(getPara("between_"+paras[i]))!=null){
					whereSql.append(" and "+paras[i]+" > ?");
					sqlStr.add(getPara("between_"+paras[i]).trim());
				}
				if(AppUtils.StringUtil(getPara("end_"+paras[i]))!=null){
					whereSql.append(" and "+paras[i]+" < ?");
					sqlStr.add(getPara("end_"+paras[i]).trim());
				}
			}
		}
	}
	protected String getParasToStringRegex(Object... paras){
			StringBuffer sql = new StringBuffer(); 
			sql.append("(");
			boolean isFirst = true;
			for (int i = 0; i < paras.length; i++) {
				if(isFirst)
					isFirst = false;
				else
					sql.append(",");
				sql.append("?");
			}
			sql.append(")");
			return sql.toString();
		}
	protected String getParasToStringRegexStr(String parasstr){
		Object [] paras = parasstr.split(regex);
		StringBuffer sql = new StringBuffer(); 
		sql.append("(");
		boolean isFirst = true;
		for (int i = 0; i < paras.length; i++) {
			if(isFirst)
				isFirst = false;
			else
				sql.append(",");
			sql.append("?");
		}
		sql.append(")");
		return sql.toString();
	}
	
	
	public LogPoint getLogPoint(){
		return new LogPoint().set(LogPoint.USRE_ID, getCurrentUser().get("USER_NO"));
	}
	

	/**
	 * 自定义分页后进行字段值关联转换
	 * 
	 * 关联表别名：table_alias
	 * @param pageNumber
	 * @param pageSize
	 * @param select
	 * @param forech
	 * @param sqlExceptSelect
	 * @param paras
	 * @return
	 */
	public Page<Record> PaginateX( int pageNumber, int pageSize, String select,String forech, String sqlExceptSelect, Object... paras){
		if (pageNumber < 1 || pageSize < 1)
			throw new ActiveRecordException("pageNumber and pageSize must be more than 0");
		long totalRow = 0;
		int totalPage = 0;
		StringBuilder sql = new StringBuilder();
		DbKit.getConfig().getDialect().forPaginate(sql, pageNumber, pageSize, select, sqlExceptSelect);
		if(forech!=null&&!forech.isEmpty()){
			sql=sql.replace(sql.indexOf("*"),sql.indexOf("*")+1,"table_alias.*,"+ forech);
		}
		List<Record> list = Db.find(sql.toString(), paras);
		BigDecimal count = Db.queryBigDecimal("select count(*)  "+DbKit.replaceFormatSqlOrderBy(sqlExceptSelect), paras);
		totalRow=count.longValue();
		totalPage = (int) (totalRow / pageSize);
		if (totalRow % pageSize != 0) {
			totalPage++;
		}
		return  new Page<Record>(list, pageNumber, pageSize, totalPage, (int)totalRow);
	}
	
	protected void initDict(String keys,String vals){
		@SuppressWarnings("rawtypes")
		Map dict =  SysParamsTools.getParamInfosToMap(keys, vals);
		setAttr("dict",new Record().setColumns(dict).toJson());
	}
	
	/** 获取当前系统操作人角色ID */
	 public String[] getCurrentRoleId() {
	  
	  Record user = getCurrentUser();
	  if (user == null) 
	   return null;
	  else 
	   return user.getStr("roleid").split(",");
	  
	 }
}
