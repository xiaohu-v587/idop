/**
 * 
 */
package com.goodcol.controller.zxglctl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.core.upload.UploadFile;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.server.zxglserver.PccmNoticeServer;
import com.goodcol.util.AppUtils;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.PropertiesContent;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.anatation.RouteBind;

/**
 * @author dinggang
 * 
 */
@RouteBind(path = "/pccm_notice")
@Before({ ManagerPowerInterceptor.class,OperationLogRecordInterceptor.class})
public class PccmNoticeCtl extends BaseCtl {
	public static Logger log = Logger.getLogger(PccmNoticeCtl.class);
	/**
	 * 进入列表页面
	 */
	@Override
	public void index() {
		render("index.jsp");
	}
	/**
	 * 获取公告信息
	 */
	public void getList() {
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String title=getPara("title");
		String notice_type=getPara("notice_type");
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("pageNum", pageNum);
		map.put("pageSize", pageSize);
		map.put("title", title);
		map.put("notice_type", notice_type);
		PccmNoticeServer pccmNoticeServer=new PccmNoticeServer();
		Page<Record> r =pccmNoticeServer.getList(map);
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		renderJson();
	}
	/**
	 * 新增、修改公告页面
	 */
	public void form(){
		render("form.jsp");
	}
	/**
	 * 保存或新增
	 */
	public void save() {
		int flag=0;
		String msg="";
		String title = getPara("title");
		String notice_type = getPara("notice_type");
		String content=getPara("content");
		String id = getPara("id");
		Record record=new Record();
		record.set("id",id);
		record.set("title", title);
		record.set("notice_type",notice_type);
		record.set("content",content);
		PccmNoticeServer pccmNoticeServer=new PccmNoticeServer();
		flag=pccmNoticeServer.saveNotice(record);
		if(flag==0){
			msg="操作失败，请联系管理员";
		}
		setAttr("flag",flag);
		setAttr("msg",msg);
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"), "公告管理", "4",
				"公告管理-新增");
		renderJson();
	}
	/**
	 * 删除公告信息
	 */
	public void del(){
		String ids = getPara("ids");
		PccmNoticeServer pccmNoticeServer=new PccmNoticeServer();
		pccmNoticeServer.del(ids);
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"), "公告管理", "6",
				"公告管理-删除");
		renderNull();
	}
	/**
	 * 获取公告详情
	 */
	public void getDetail(){
		String id = getPara("id");
		PccmNoticeServer pccmNoticeServer=new PccmNoticeServer();
		Record record=pccmNoticeServer.getDetail(id);
		setAttr("record", record);
		renderJson();
	}
	/**
	 * 上传附件页面
	 */
	public void upload(){
		String id=getPara("id");
 		setAttr("id",id);
		render("upload.jsp");
	}
	/**
	 * 上传附件
	 */
	public void saveFile(){
		String id=getPara("id");
 		UploadFile uploadFile=this.getFile("upload_file");
 		String destName="";
 		try {
  			// 判断有文件才进行上传
  			if(uploadFile!=null){
  				File src=uploadFile.getFile();
  				String srcName=src.getName();
  				String upload_path=PropertiesContent.get("upload_path");//上传文件地址
  				destName=BolusDate.getDate()+"/"+BolusDate.getTime()+"/"+srcName;
  				String destPath=upload_path+destName;
  				File dest=new File(destPath);
  				//判断目标目录是否存在
  				if(!dest.getParentFile().exists()){
  					dest.getParentFile().mkdirs();
  				}
  				if(src!=null){
  					src.renameTo(dest);
  				}
  				PccmNoticeServer pccmNoticeServer=new PccmNoticeServer();
  				Record record=new Record();
  				record.set("file_url",destName);
  				record.set("id",id);
  				pccmNoticeServer.updateNotice(record);
  			}
  			renderText("0000","text/html;charset=UTF-8;");
  			
  		} catch (Exception e) {
  			renderText("9999","text/html;charset=UTF-8;");
  		}
	}
	
	/**
	 * 下载文件
	 */
	public void downloadFile(){
		String id=getPara("id");
		Record record=Db.findFirst("select * from pccm_notice where id=?",id);
		String file_url=record.getStr("file_url");
		String upload_path=PropertiesContent.get("upload_path");//上传文件地址
		File file=new File(upload_path+file_url);
		if(file.exists()&&file.isFile()){
			renderFile(file);
		}
	}
	
	/**
	 * 查询首页公告
	 * 2018年5月21日15:19:06
	 * @author liutao
	 */
	public void findNotice(){
		String sql = "select * from ( select a.id, a.title, rownum as num " +
				"from pccm_notice a order by a.create_time desc" +
				") t where t.num < 6";
		List<Record> notices = Db.find(sql);
		setAttr("notices", notices);
		renderJson();
	}
	
	/**
	 * 查看公告详情页面(首页点击打开的页面)
	 * 2018年5月21日17:30:29
	 * @author liutao
	 */
	public void noticeDetail(){
		render("noticeDetail.jsp");
	}
}
