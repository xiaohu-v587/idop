package com.goodcol.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.util.CellRangeAddress;

import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.MissionControlUtil;
import com.goodcol.util.execl.ExcelRenderx;
import com.goodcol.util.ext.anatation.RouteBind;

/**
 * 
 * @ClassName: com.goodcol.controller.MissionFileReportCtl
 * @Description: 文件类型任务controller
 * @author Wangsq
 * @date 2019-08-06
 *
 */
@RouteBind(path = "/missionFileReport")
@Before({ ManagerPowerInterceptor.class })
public class MissionFileReportCtl extends BaseCtl {

	public static Logger log = Logger.getLogger(MissionFileReportCtl.class);

	@Override
	public void index() {

	}

	/**
	 * @Title: retoaction
	 * @Description: 跳转到文件类型反馈页面
	 * @author Wangsq
	 * @date 2019-08-06
	 *
	 */
	public void retoaction() {
		String id = getPara("id");
		String missionIssueId = getPara("missionIssueId");
		String missionRequire = getPara("missionRequire");
		String status = getPara("status");
		setAttr("id", id);
		setAttr("missionIssueId", missionIssueId);
		setAttr("missionRequire", missionRequire);
		setAttr("status", status);
		render("do_missionfile_retoaction.jsp");

	}

	/**
	 * @Title: summaryView
	 * @Description:  跳转到文件类型汇总页面
	 * @author Wangsq
	 * @date 2019-08-06
	 *
	 */
	public void summaryView() {
		String id = getPara("id");
		setAttr("id", id);
		setAttr("type", getPara("type"));
		setAttr("pidtype", getPara("pidtype"));
		render("do_mission_filesummary.jsp");

	}

	/**
	 * @Title: queryFiles
	 * @Description: 查询文件类型汇总结果
	 * @author Wangsq
	 * @date 2019-08-06
	 *
	 */
	public void queryFiles() {
		// 获取分页的索引和每页显示的条数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 20);
		String pid = getPara("id");// 发起表任务的ID
		String type = getPara("type"); // 判断是哪个模快 传过来的查询 user_table - 用户任务查看 ，issue_table - 发起任务查看
		String pidtype = getPara("pidtype"); //判断是上传的模板附件--1   2--上传的说明附件   3--反馈的附件   4--退回附件
		String userno = getPara("userno");// 反馈人
		String selectSql = "select t.*,(select u.name from sys_user_info u where  u.user_no = t.userno ) username ";
		StringBuffer fromSql = new StringBuffer();
		//筛选需要的查询ID  
		if ("issue_table".equals(type)) {
			fromSql.append(" from sys_attachments t left join yygl_mission_user ymu on ymu.id = t.pid where pid in (select id from YYGL_MISSION_USER m where  USER_MISSION_STATUS = '02'");
			fromSql.append("   start with m.id in   (SELECT ID FROM  YYGL_MISSION_USER  WHERE mission_issue_id = '"+pid+"' AND UP_MISSION_ID is null AND USER_MISSION_STATUS = '02') ");
			fromSql.append("  connect by m.UP_MISSION_ID=prior m.id  ) and status = '1' and ymu.user_mission_status = '02' and t.pidtype = '" + pidtype + "' ");
			if (AppUtils.StringUtil(userno) != null) {
				fromSql.append(" and t.userno = '" + userno + "' ");
			}
		} else if("view".equals(type)) {
			fromSql.append("  from sys_attachments t where t.pid = '"+pid+"' and t.status = '1' and  t.pidtype = '"+pidtype+"' ");
		} else {
			fromSql.append(" from sys_attachments t left join yygl_mission_user ymu on ymu.id = t.pid where pid in (select id from YYGL_MISSION_USER m where id = '"+pid+"' ");
			fromSql.append(" AND USER_MISSION_STATUS = '02'  UNION  select id from YYGL_MISSION_USER m where  USER_MISSION_STATUS = '02'  start with m.id in (SELECT ID FROM  ");
			fromSql.append(" YYGL_MISSION_USER  WHERE UP_MISSION_ID = '"+pid+"' AND USER_MISSION_STATUS = '02') connect by m.UP_MISSION_ID=prior m.id ) and status = '1' ");
			fromSql.append(" and ymu.user_mission_status = '02'  and t.pidtype = '" + pidtype + "' ");
		}
		// 拼接sql，查询列表集合
		Page<Record> page = Db.paginate(pageNum, pageSize, selectSql, fromSql + " order by createdate desc");

		setAttr("datas", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
		
		
		
	}
	
	
	
	/**
	 * 查询反馈页面数据
	 */
	public void getRetoactionInfo(){
		
		//查询配置信息
		
		//下载数据模板,暂时不考虑抽取数据情况
		String userTaskid = getPara("id"); // mission_user id
		String viewFlag = getPara("type","edit");
		//获取当前任务数据
		Record userTask = Db.findById("YYGL_MISSION_USER", userTaskid);
		//获取任务配置数据
		Record taskparam= Db.findFirst("SELECT * FROM YYGL_MISSION_CONFIG C WHERE C.ID  =(SELECT MISSION_CONFIG_ID FROM YYGL_MISSION_ISSUE I WHERE I.ID = ? )",userTask.get("MISSION_ISSUE_ID"));
		//判断任务状态，如果该任务状态为已经转发他人情况，并且所有的数据内容都已反馈，此时展示数据为该人员所有数据都查询出来
		List<Object> paramList_user = new ArrayList<Object>();
		StringBuffer childSql = new StringBuffer();
		if("0".equals(userTask.getStr("FORWARD_FLAG"))){
			childSql.append("  AND PID in (   ");
			childSql.append(" select id from YYGL_MISSION_USER m where id =? "+("view".equals(viewFlag) ? " AND USER_MISSION_STATUS = '02' " : "  " ) +"  UNION ");
			childSql.append(" select id from YYGL_MISSION_USER m where  USER_MISSION_STATUS = '02'  start with m.id in ");
			childSql.append(" (SELECT ID FROM  YYGL_MISSION_USER  WHERE UP_MISSION_ID = ? AND USER_MISSION_STATUS = '02') ");
			childSql.append(" connect by m.UP_MISSION_ID=prior m.id ) ");
			paramList_user.add(userTaskid);
			paramList_user.add(userTaskid);
		}else{
			childSql.append(" AND PID = ? ");
			paramList_user.add(userTaskid);
		}
		
		String pidtype = getPara("pidtype");
		// 获取分页的索引和每页显示的条数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		
		String selectSql = "select t.*,to_char(to_date(createdate,'yyyyMMddHH24miss'),'yyyy-MM-dd') createdatef,(select max(u.name) from sys_user_info u where  u.user_no = t.userno ) username";
		String fromSql = " from sys_attachments t where  status = '1' ";
		
		if(AppUtils.StringUtil(pidtype)!= null){
			fromSql += " and pidtype='"+pidtype+"'";
		}
		
		fromSql += childSql.toString();
		// 从数据库查询指定条数的用户记录
		Page<Record> page = Db.paginate(
						pageNum,
						pageSize,selectSql,fromSql + " order by createdate desc",paramList_user.toArray());
		
		setAttr("datas", page.getList());
		setAttr("total", page.getTotalRow());
		setAttr("taskremark", taskparam.get("mission_require"));
		renderJson();
	}
	
	

	// 任务接收人执行情况查看页面
	public void retoactionView() {
		setAttr("id", getPara("id"));
		setAttr("missionIssueId", getPara("missionIssueId"));
		setAttr("userno", getPara("userno"));
		setAttr("pidtype", getPara("pidtype"));
		render("do_mission_retoactionview.jsp");
	}
	
	/**
	 * @Title: queryFiles
	 * @Description:打包下载反馈文件
	 * @author Wangsq
	 * @date 2019-08-06
	 *
	 */
	public void downloadFilesToZip() {
		
		//打包下载时，可以传入的源为 已发起任务表，和 待处理任务表，数据来源分别是， 
		
		//下载数据模板,暂时不考虑抽取数据情况
		
		String id = getPara("id");
		String type = getPara("type"); // user_table -  用户任务查看 ，issue_table - 发起任务查看
		String userTaskid = "";
		
		List<Object> paramList = new ArrayList<Object>();
		//根据任务获取下级任务执行情况
		
		StringBuffer taskListSql = new StringBuffer("SELECT T.*,"
				+ "(SELECT NAME FROM SYS_USER_INFO U WHERE U.USER_NO = T.USER_NO AND U.DELE_FLAG='0') || "
				+ "(SELECT NAME FROM SYS_ROLE_INFO U WHERE U.ID = T.ROLEID AND U.ROLE_DELE_FLAG='1') || "
				+ "(SELECT ORGNAME FROM SYS_ORG_INFO U WHERE U.ORGNUM = T.ORGID AND U.STAT='1')  TASKNAME "
				+ " FROM YYGL_MISSION_USER T WHERE USER_MISSION_STATUS = '02' ");
		if("issue_table".equals(type) ){
			taskListSql.append("  AND MISSION_ISSUE_ID = ?  AND UP_MISSION_ID  IS NULL   ");
			paramList.add(id);
		}else{
			taskListSql.append("  AND UP_MISSION_ID = ?   "); 
			paramList.add(id);
			//未转发时，直接拦截掉，在前端拦截
		}
		//获取到下级任务列表，开始进行循环生成数据文件
		List<Record> taskList =  Db.find(taskListSql.toString(),paramList.toArray()) ;
		
		StringBuffer childSql = new StringBuffer();
		
		
		
		List<Object> paramList_user = new ArrayList<Object>();
		
		//下载的模板依据于最新的模板配置参数进行
		Map<String,List<Record>> attachmentsMap = new HashMap<String, List<Record>>();
		
		for (Record userTask_r : taskList) {
			//清空
			paramList.clear();
			paramList.add(id);
			paramList_user.clear();
			childSql.delete(0, childSql.length());
			
			userTaskid = userTask_r.getStr("ID");
			
			if("0".equals(userTask_r.getStr("FORWARD_FLAG"))){
				childSql.append("  AND pid in (   ");
				childSql.append(" select id from YYGL_MISSION_USER m where id =? AND USER_MISSION_STATUS = '02'  UNION ");
				childSql.append(" select id from YYGL_MISSION_USER m where  USER_MISSION_STATUS = '02'  start with m.id in ");
				childSql.append(" (SELECT ID FROM  YYGL_MISSION_USER  WHERE UP_MISSION_ID = ? AND USER_MISSION_STATUS = '02') ");
				childSql.append(" connect by m.UP_MISSION_ID=prior m.id ) ");
				paramList_user.add(userTaskid);
				paramList_user.add(userTaskid);
			}else{
				childSql.append(" AND pid = ? ");
				paramList_user.add(userTaskid);
			}
			//先检查任务反馈表中是否存放有数据如果无数据的话，采用抽取的模板数据，如果有数据，采用已存放的数据
			// 获取查询参数
			List<Record> list = Db.find("SELECT * FROM sys_attachments WHERE status = '1'  "+childSql,paramList_user.toArray());
			attachmentsMap.put(userTask_r.getStr("TASKNAME"), list);
		}
		
		//String missionIssueID = getPara("id");
		//String userno = getPara("userno");
		ZipOutputStream zipOutputStream = null;
		OutputStream out = null;
		/*String type = getPara("type");//impleStitu--代表任务执行情况的打包下载  summary代表汇总的打包下载
		StringBuffer selectSql = new StringBuffer();
		StringBuffer whereSql = new StringBuffer();
		selectSql.append("select t.* from sys_attachments t ");
		whereSql.append(" where 1=1");
		if("summary".equals(type)) {
			if(AppUtils.StringUtil(missionIssueID) != null) {
				whereSql.append(" and t.id in ("+missionIssueID+")");
			}
		} else if("impleStitu".equals(type)){
			if(AppUtils.StringUtil(missionIssueID) != null && AppUtils.StringUtil(userno) != null) {
				whereSql.append(" and t.pid in ("+missionIssueID+") and t.userno in ("+userno+")");
			}
		} */
		// 检查下级机构是否有上传过附件
		//List<Record> attachmentsList = Db.find(selectSql.toString() + whereSql.toString());
		/*if (attachmentsList.size() == 0) {
			renderHtml("<script type='text/javascript'>parent.callback(\"该任务暂时没有反馈过附件。\"); </script>");
			return;
		}*/
		URL url;
		InputStream in = null;
		try {
			out = getResponse().getOutputStream();
			zipOutputStream = new ZipOutputStream(out);
			String zipfileName =  "汇总文件" + ".zip";
			getResponse().setContentType("application/octet-stream ");
			getResponse().setHeader("Connection", "close"); // 表示不能用浏览器直接打开
			getResponse().setHeader("Accept-Ranges", "bytes");// 告诉客户端允许断点续传多线程连接下载
			getResponse().setHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(zipfileName, "UTF-8") );
			
			Set<String> keys =  attachmentsMap.keySet();
			
			for (String key : keys) {
				
				for (Record record : attachmentsMap.get(key)) {
					
					String urlStr = URLDecoder.decode(record.getStr("filePath"),"utf-8"); 
					in = new FileInputStream(new File(urlStr));//此处后面可以改为非结构化平台模式
					String fileName = record.getStr("FILENAME");
					ZipEntry entry = new ZipEntry(key+"_"+fileName.replace("[template]", ""));
					try {
						zipOutputStream.putNextEntry(entry);
						// 读入需要下载的文件的内容，打包到zip文件
						if (in != null) {
							byte[] b = new byte[8192];
							int len = 0;
							while ((len = in.read(b)) != -1) {
								zipOutputStream.write(b, 0, len);
							}
						}
						if (in != null) 
							in.close();
					} catch (IOException e) {
						e.printStackTrace();
					} 
				}
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		// 关闭输出流
		try {
			zipOutputStream.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		renderNull();
	}

	
	/**
	 * @Title: downLoadFj
	 * @Description:附件下载
	 * @author Wangsq
	 * @date 2019-08-13
	 *
	 */
	public void downLoadFj() {
		String pid = getPara("id");// 附件表的Pid
		String pidtype = getPara("pidtype"); //判断是上传的模板附件--1   2--上传的说明附件   3--反馈的附件   4--退回附件
		String fileName = "";
		String filePath = "";
		String fileType = "";
		String urlStr = null;
		//根据附件pid和附件类型查询附件
		List<Record> fjList = Db.find("select * from sys_attachments where pid = ? and pidtype = ? and status = '1'",pid, pidtype);
		if(fjList.size() > 0) {
			fileName = fjList.get(0).getStr("fileName");
			filePath = fjList.get(0).getStr("filePath");
			fileType = fjList.get(0).getStr("fileType");
			
			try {
				urlStr = URLDecoder.decode(filePath,"utf-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	
			// 获取分页的索引和每页显示的条数
			URL url;
			InputStream in = null;
			HttpServletResponse response = getResponse();
			BigDecimal bd =	Db.queryBigDecimal("select count(id) from sys_param_info where key='1309' and val like ? ","%"+fileType+"%");
			
			try {
				in = new FileInputStream(new File(urlStr));
				if(bd.intValue()==0){
					response.reset();// 清空输出流
					response.setHeader("Content-disposition", "attachment; filename=" + fileName);// 设定输出文件头
					response.setContentType("charset=utf-8");// 定义输出类型
				}
				OutputStream fos = response.getOutputStream();
				if (in != null) {
					byte[] b = new byte[8192];
					int len = 0;
					while ((len = in.read(b)) != -1) {
						fos.write(b, 0, len);
						fos.flush();
					}
				}
				
			} catch (FileNotFoundException e) {
				
			} catch (IOException e) {
			} finally {
				try {
					if(in!=null)
						in.close();
				} catch (IOException e) {
					
				}
			}
		} else {
			renderHtml("<script type='text/javascript'>parent.callback(\"该任务暂时没有反馈过附件。\"); </script>");
			return;
		}
		renderNull();
	}
	

	//下载汇总数据内容
	public void downloadSummary(){
		
		
		String id = getPara("id");
		String type = getPara("type"); // user_table -  用户任务查看 ，issue_table - 发起任务查看
			
		StringBuffer datawheresql = new StringBuffer();
		
		List<Object> paramList = new ArrayList<Object>();
		
		
		//判断任务状态，如果该任务状态为已经转发他人情况，并且所有的数据内容都已反馈，此时展示数据为该人员所有数据都查询出来
		
		if( "issue_table".equals(type) ){
			datawheresql.append("  AND PID in (   ");
			datawheresql.append(" select id from YYGL_MISSION_USER m where  USER_MISSION_STATUS = '02'  start with m.id in ");
			datawheresql.append(" (SELECT ID FROM  YYGL_MISSION_USER  WHERE mission_issue_id = ? AND UP_MISSION_ID is null AND USER_MISSION_STATUS = '02') ");
			datawheresql.append(" connect by m.UP_MISSION_ID=prior m.id ) ");
			paramList.add(id);
		}else{
			Record userTask = Db.findById("YYGL_MISSION_USER", id);
			if( userTask !=null && "0".equals(userTask.getStr("FORWARD_FLAG"))  ){
				datawheresql.append("  AND PID in (   ");
				datawheresql.append(" select id from YYGL_MISSION_USER m where id =? AND USER_MISSION_STATUS = '02'  UNION ");
				datawheresql.append(" select id from YYGL_MISSION_USER m where  USER_MISSION_STATUS = '02'  start with m.id in ");
				datawheresql.append(" (SELECT ID FROM  YYGL_MISSION_USER  WHERE UP_MISSION_ID = ? AND USER_MISSION_STATUS = '02') ");
				datawheresql.append(" connect by m.UP_MISSION_ID=prior m.id ) ");
				paramList.add(id);
				paramList.add(id);
			}else{
				datawheresql.append("  AND PID  in ( SELECT ID FROM  YYGL_MISSION_USER WHERE USER_MISSION_ID = ? AND USER_MISSION_STATUS = '02' )  ");
				paramList.add(id);
			}
		} 
		List<Record> list = Db.find("SELECT t.*,to_char(to_date(createdate,'yyyyMMddHH24miss'),'yyyy-MM-dd') createdatef,(select max(u.name) from sys_user_info u where  u.user_no = t.userno ) username FROM sys_attachments t WHERE status = '1' "+datawheresql.toString(),paramList.toArray());
		
		ZipOutputStream zipOutputStream = null;
		OutputStream out = null;
		//URL url;
		InputStream in = null;
		try {
			out = getResponse().getOutputStream();
			zipOutputStream = new ZipOutputStream(out);
			String zipfileName =  "汇总文件" + ".zip";
			getResponse().setContentType("application/octet-stream ");
			getResponse().setHeader("Connection", "close"); // 表示不能用浏览器直接打开
			getResponse().setHeader("Accept-Ranges", "bytes");// 告诉客户端允许断点续传多线程连接下载
			getResponse().setHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(zipfileName, "UTF-8") );
				
			for (Record record : list) {
				
				String urlStr = URLDecoder.decode(record.getStr("filePath"),"utf-8"); 
				in = new FileInputStream(new File(urlStr));//此处后面可以改为非结构化平台模式
				String fileName = record.getStr("FILENAME");
				ZipEntry entry = new ZipEntry(record.getStr("username")+"_"+fileName.replace("[template]", ""));
				try {
					zipOutputStream.putNextEntry(entry);
					// 读入需要下载的文件的内容，打包到zip文件
					if (in != null) {
						byte[] b = new byte[8192];
						int len = 0;
						while ((len = in.read(b)) != -1) {
							zipOutputStream.write(b, 0, len);
						}
					}
					if (in != null) 
						in.close();
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
				
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		// 关闭输出流
		try {
			zipOutputStream.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		renderNull();
		
		
	}
	
	
}
