package com.goodcol.controller.dop;

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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.PropertiesContent;
import com.goodcol.util.ext.anatation.RouteBind;


/**
 * 公共controller
 * @author 常显阳 2018-12-05
 *
 */
@RouteBind(path = "/common")
@Before({ManagerPowerInterceptor.class})
public class CommonCtl extends BaseCtl {
	private static Logger log = Logger.getLogger(CommonCtl.class);
	
	@Override
	public void index() {
		renderNull();
	}
	
	/**
	 * 跳转到文件上传页面
	 */
	public void goFileUpload() {
		setAttr("pid", getPara("pid"));
		
		//是否开启附件类型选项
		String ebavleType = getPara("ebavleType");
		if(AppUtils.StringUtil(ebavleType)==null){
			ebavleType = "false";
		}
		setAttr("ebavleType",ebavleType);
		
		//上传类型数据源路径
		String ebavleUrl = getPara("ebavleUrl");
		if(AppUtils.StringUtil(ebavleUrl)!=null){
			setAttr("ebavleUrl",ebavleUrl);
		}
		
		//上传类型数据源路径
		String limitType = getPara("limitType");
		if(AppUtils.StringUtil(limitType)!=null){
			setAttr("limitType",limitType);
		}
		
		//默认最大上传数据大小为15M,可在码表中9999下修改maxPostSize的长度！
		int maxPostSize = PropertiesContent.getToInteger("maxPostSize",15*1024*1024);
		setAttr("limitSize",AppUtils.bytesToSize(maxPostSize));
		
		//render("uploadwindow.jsp");
		render("attachment.jsp");
	}
	/**
	 * 查询附件
	 */
	public void queryFiles() {
		String pid = getPara("pid");
		String pidtype = getPara("pidtype");
		// 获取分页的索引和每页显示的条数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 5);
		
		String selectSql = "select t.*,to_char(to_date(createdate,'yyyyMMddHH24miss'),'yyyy-MM-dd') createdatef,(select max(u.name) from sys_user_info u where  u.user_no = t.userno ) username";
		String fromSql = " from sys_attachments t where pid=? and status = '1'";
		if(AppUtils.StringUtil(pidtype)!= null){
			fromSql += " and pidtype='"+pidtype+"'";
		}
		// 从数据库查询指定条数的用户记录
		Page<Record> page = Db.paginate(
						pageNum,
						pageSize,selectSql,fromSql + " order by createdate desc",new Object[]{pid});
	
		log.info("查询附件");
		
		setAttr("datas", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}
	
	/**
	 * 删除附件
	 */
	public void delFile() {
		String id = getPara("id");
		int flag = 0;
		//更新本地附件状态
		flag = Db.update("update sys_attachments set status = '0' where id = ?", id);
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"), "附件", "6", "删除附件");
		setAttr("flag", flag);
		renderJson();
	}
	
	/**
	 * 查看附件
	 */
	public void showFiles() {
		String pid = getPara("pid");
		String pidtype = getPara("pidtype");
		
		String selectSql = "select * ";
		String fromSql = " from sys_attachments where pid=? and status = '1'";
		if(AppUtils.StringUtil(pidtype)!= null){
			fromSql += " and pidtype='"+pidtype+"'";
		}
		List<Record> datas = Db.find(selectSql+fromSql,new Object[]{pid});
		setAttr("datas", datas);
		renderJson();
	}
	
	/**
	 * 获取 分页用户列表
	 */
	public void getUserCombox(){
		String user_no = getPara("user_no");
		int maxPageSize = getParaToInt("maxPageSize",10);
		List<Record> typeList = new ArrayList<Record>();
		if(AppUtils.StringUtil(user_no) != null){
			Page<Record>	page =  Db.paginate(1, maxPageSize, "SELECT USER_NO,NAME  ", "FROM SYS_USER_INFO U  WHERE  1=1  AND U.STAT='0' AND U.DELE_FLAG='0' AND USER_NO LIKE ?",new Object[]{"%"+user_no+"%"});
			typeList = page.getList();
		}
		renderJson(typeList); 
	}
	
	/**
	 * 跳转到文件查看页面
	 */
	public void uploadview(){
		Enumeration paras = getParaNames();
	     while (paras.hasMoreElements()) {
	       String name = (String)paras.nextElement();
	       setAttr(name, getPara(name));
	     }
	     render("uploadview.jsp");
	}
	
	public void receiveFileByURL() {
    	String fileName = getPara("fileName");
    	
		
		
	
    	String urlStr = null;
	
		try {
			urlStr = URLDecoder.decode(getPara("filePath"),"utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(urlStr);
		log.error(urlStr);
    	String fileType = getPara("fileType");
		// 获取分页的索引和每页显示的条数
		URL url;
		InputStream in = null;
		HttpServletResponse response = getResponse();
		//FileInputStream filein = null;
		BigDecimal bd =	Db.queryBigDecimal("select count(id) from sys_param_info where key='1309' and val like ? ","%"+fileType+"%");
		
		try {
			/*url = new URL(urlStr);
			in = url.openStream();*/
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
		/*	OutputStream out = getResponse().getOutputStream();
			filein = new FileInputStream(file);
			if (filein != null) {
				byte[] b = new byte[8192];
				int len = 0;
				while ((len = filein.read(b)) != -1) {
					out.write(b, 0, len);
				}
			}*/
			
		} catch (FileNotFoundException e) {
			//log.error("unitedaccess http -- GetFileServer: " + e.toString());
		} catch (IOException e) {
		} finally {
			try {
				if(in!=null)
					in.close();
				//filein.close();
			} catch (IOException e) {
				//log
					//	.error("unitedaccess http -- GetFileServer: "
						//		+ e.toString());
			}
		}
		renderNull();
	}
	
	public void allotUser(){
		render("allotUser.jsp");
	}
}
