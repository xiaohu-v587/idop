package com.goodcol.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.core.upload.UploadFile;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.FileTool;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.OrgUtil;
import com.goodcol.util.PropertiesContent;
import com.goodcol.util.ext.anatation.RouteBind;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
/**
 * 公告信息维护
 *
 * @author puyu
 */
@RouteBind(path = "/notice")
@Before({ ManagerPowerInterceptor.class })
public class NoticeCtl extends BaseCtl {

	protected Logger log = Logger.getLogger(NoticeCtl.class);

	@Override
	public void index() {
		// TODO Auto-generated method stub
		
		render("index.jsp");
	}

	/**
	 * 显示所有的公告通知
	 */
	public void getList() {
		int pageNumber = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String bt = getPara("bt");
		StringBuffer sb = new StringBuffer();
		List<String> listStr = new ArrayList<String>();
		if (AppUtils.StringUtil(bt) != null) {
			sb.append(" and SYS_NOTICE_INFO.bt like ? ");
			listStr.add("%" + bt.trim() + "%");
		}
		String sql = "select SYS_NOTICE_INFO.*,(select listagg(SYS_ROLE_INFO.\"NAME\", ',') within group(order by SYS_ROLE_INFO.\"ID\")from SYS_ROLE_INFO where instr(',' || SYS_NOTICE_INFO.JSJS || ',', ',' || SYS_ROLE_INFO.\"ID\" || ',') > 0) jsjsmc";
		String extrasql = " from SYS_NOTICE_INFO WHERE 1=1 "
				+ sb.toString() + " order by SYS_NOTICE_INFO.fbsj desc";
		Page<Record> r = Db.paginate(pageNumber, pageSize, sql, extrasql, listStr.toArray());
		List<Record> list = r.getList();
		if (list.size() < 1) {
			Page<Record> r1 = Db.paginate(1, 10, sql, extrasql, listStr.toArray());
			list = r1.getList();
		}
		setAttr("data", list);
		setAttr("total", r.getTotalRow());
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "公告信息维护", "3", "公告信息维护-查询");
		renderJson();

	}

	public void form() {
		render("form.jsp");
	}

	public void getRoleList() {
		List<Record> lr = Db.find("select id, name from sys_role_info where role_dele_flag='1' and flag='0'");
		renderJson(lr);
	}

	/**
	 * 新增公告
	 * 
	 * @param jsjs
	 *            接收角色
	 * @param bt
	 *            标题
	 * @param nr
	 *            内容
	 * @param id
	 *            id
	 */
	public void save() {
		String id = getPara("id");
		String jsjs = getPara("jsjsbm");
		String bt = getPara("bt");
		String nr = getPara("nr");
		if (id.equals("")) {
			Db.update("insert into SYS_NOTICE_INFO(id,fbr,fbsj,jsjs,bt,nr,llcs) values(?,?,?,?,?,?,?)",
					new Object[] { AppUtils.getStringSeq(), getCurrentUser().getStr("USER_NO"), DateTimeUtil.getTime(),
							jsjs, bt, nr, "0" });
		} else {
			Db.update("update SYS_NOTICE_INFO set jsjs=?,bt=?,nr=? where id=?", new Object[] { jsjs, bt, nr, id });
		}
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "公告信息维护", "4", "公告信息-新增");
		renderNull();
	}

	/**
	 * 保存上传文件
	 */
	public void saveFile() {
		UploadFile file = getFile("fjdz");
		String id = getPara("id");
		if (file != null) {
			if (id != null) {
				Db.deleteById("SYS_NOTICE_INFO", id);
			}
			String fileUrl = PropertiesContent.get("fileUrl");
			String url = fileUrl + "\\" + file.getFileName();
			FileTool.saveFile(url, file);
			String uuid = AppUtils.getStringSeq();
			Db.update("insert into SYS_NOTICE_INFO(id,fbr,fbsj,llcs,fjdz) values(?,?,?,?,?)",
					new Object[] { uuid, getCurrentUser().getStr("USER_NO"), DateTimeUtil.getTime(), "0", url });
			setAttr("id", uuid);
			renderJson();
		}
	}

	/**
	 * 删除公告
	 */
	public void del() {
		String id = getPara("id");
		String fjdz = getPara("fjdz");
		if(fjdz!=null&&fjdz.equals("")){
			try {
				FileTool.delete(fjdz);
			} catch (FileSystemException e) {
				// TODO Auto-generated catch block
				log.info("删除公告附件："+e);
			}
		}
		Db.deleteById("SYS_NOTICE_INFO", id);
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "公告信息维护", "6", "删除公告信息");
	}

	/**
	 * 首页展示公告信息详细信息
	 */
	public void wetofrom() {
		String id = getPara("td");
		Record record = Db.findFirst("select SYS_NOTICE_INFO.*, (select listagg(SYS_ROLE_INFO.\"NAME\", ',') within group(order by SYS_ROLE_INFO.\"ID\")from SYS_ROLE_INFO where instr(',' || SYS_NOTICE_INFO.JSJS || ',', ',' || SYS_ROLE_INFO.\"ID\" || ',') > 0) jsjsmc from SYS_NOTICE_INFO  where id=?", id);
		String llcsold = record.getStr("llcs");
		String llcsnew = String.valueOf((Integer.valueOf(llcsold) + 1));
		Db.update("update SYS_NOTICE_INFO set llcs=? where id=?", new Object[] { llcsnew, id });
		setAttr("bt", record.getStr("bt"));
		setAttr("nr", record.getStr("nr"));
		setAttr("fbsj", record.getStr("fbsj"));
		setAttr("fjdz", record.getStr("fjdz"));
		setAttr("jsjsmc", record.getStr("jsjsmc"));
		renderJson();
	}

	/**
	 * 下载文件
	 * @param fjdz
	 *            附件地址
	 */
	public void fileDown() {
		String fjdz = getPara("fjdz");
		File file = new File(fjdz);
		renderFile(file);
	}
	
	public void checkFile(){
		String fjdz=getPara("fjdz");
		try {
			FileObject file=FileTool.getFileObject(fjdz);
			if(file.exists()){
				setAttr("cf", "yes");
				renderJson();
			}else{
				setAttr("cf", "no");
				renderJson();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info("检查文件是否存在："+e);
		}
		
		
	}
			
	/**
	 * 首页展示公告信息页面
	 */
	public void welcome() {
		String user = getCurrentUser().getStr("USER_NO");
		int pageNumber = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		Record record=Db.findFirst("SELECT role_id from SYS_USER_INFO where user_no=?",user);
		List<String> listStr = new ArrayList<String>();
		String sql = "select * ";
		String extraSql = " from SYS_NOTICE_INFO where jsjs like '%"+record.getStr("role_id")+"%' ORDER BY FBSJ DESC";
		Page<Record> r = Db.paginate(pageNumber, pageSize, sql, extraSql, listStr.toArray());
		List<Record> list = r.getList();
		if (list.size() < 1) {
			Page<Record> r1 = Db.paginate(1, 10, sql, extraSql, listStr.toArray());
			list = r1.getList();
		}
		setAttr("data", list);
		setAttr("total", r.getTotalRow());
		renderJson();
	}
	
    public void jsjsSelector(){
    	render("jsjsSelector.jsp");
    }

    /**
     * 获取所有接收角色
     */
    public void getAllJsjs(){
    	List<Record> list=Db.find("select * from SYS_ROLE_INFO");
    	setAttr("data", list);
		renderJson();
    }
}
