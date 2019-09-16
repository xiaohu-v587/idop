package com.goodcol.controller.zxglctl;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.server.zxglserver.PccmDownTaskServer;
import com.goodcol.util.AppUtils;
import com.goodcol.util.ext.anatation.LogBind;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.ext.render.excel.ExcelRender;

/**
 * 下载任务列表控制层
 * 
 * @author start
 * 
 */
@RouteBind(path = "/zxDownTask")
@Before({ ManagerPowerInterceptor.class, OperationLogRecordInterceptor.class })
public class PccmDownTaskCtl extends BaseCtl {
	
	public static Logger log = Logger.getLogger(PccmDownTaskCtl.class);
	PccmDownTaskServer pccmDownTaskServer = new PccmDownTaskServer();
	
	/**
	 * 主页面
	 */
	public void index() {
		//下载路径
		setAttr("down_path", AppUtils.findDictRemark("down_path", "1"));
		//预览条数
		setAttr("view_size", AppUtils.findDictByKey("viewsize"));
		render("index.jsp");
	}
	
	/**
	 * 下载任务列表
	 */
	public void downTaskList(){
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		map.put("pageNum", pageNum);
		map.put("pageSize", pageSize);
		map.put("taskTxt", getPara("taskTxt"));
		String user_no=getCurrentUser().getStr("ID");
		String orgId = getCurrentUser().getStr("ORG_ID");
		String roleNames = AppUtils.getRoleNames(user_no);
		String level = null;
		//对应的权限ID
		String perm_org_id = AppUtils.findPermOrg(getCurrentUser().getStr("ID"));
		if(AppUtils.StringUtil(perm_org_id)!=null){
			orgId = perm_org_id;
		}else if(roleNames.contains("领导")||roleNames.contains("系统管理员")){
			level = AppUtils.getLevByRole(roleNames);
			orgId = AppUtils.getUpOrg(orgId, level);
			if(AppUtils.StringUtil(orgId)==null){
				orgId = getCurrentUser().getStr("ORG_ID");
			}
		}
		map.put("org_id", orgId);
		map.put("user_no", user_no);
		Page<Record> r =pccmDownTaskServer.downTaskPage(map);
		
		setAttr("data", r.getList());	
		setAttr("total", r.getTotalRow());

		// 打印日志
		log.info("downTaskList--r.getList():" + r.getList());
		log.info("downTaskList--r.getTotalRow():" + r.getTotalRow());
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 预览列表
	 */
	public void viewList(){
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取查询参数
		map.put("view_size", getPara("view_size"));
		map.put("table_name", getPara("table_name"));
		List<Record> lr =pccmDownTaskServer.viewList(map);
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("viewList", lr);
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 下载日志
	 */
	@LogBind(menuname = "下载类型汇总", type = "7", remark = "下载类型汇总-下载")
	public void logDown() {

		String pkid=getPara("pkid");
		// 从数据库查询指定条数的用户记录
		List<Record> lr =pccmDownTaskServer.findById(pkid);
		
		String fileName = "";
		String xlsName = "日志下载_"+pkid+".xls";
		try {
			fileName = new String(xlsName.getBytes("GB2312"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			log.error("userctl-download", e);
		}
		String roleName = AppUtils.getRoleNames(getCurrentUser().getStr("ID"));
		if (roleName.contains("领导") || roleName.contains("系统管理员")) {
			String[] headers = {"pkid","sys_id","user_flg","sql_str","db_user","status","file_status","sql_style","prio_level",
					"prio_level_other","user_schema_name","table_name_full","user_upload_dir","user_upload_bs_dir",
					"upload_file_number","download_file_is_compress","download_file_name","row_number","job_time",
					"begin_time","end_time","remark","job_explain","other_para"};
			String[] columns = { "任务号","系统ID","用户标识","SQL语句","数据库用户","任务处理状态","文件上传状态","SQL类型","处理优先级","未启用",
					"用户所属库名","生成的结果表名","未启用","上传文件目录","上传文件数量","下载文件是否进行压缩","结果文件名",
					"处理结果记录条数","任务提交时间","任务开始处理时间","任务结束时间","任务处理日志","任务说明","增加任务时调用的参数"};
			// 转换成excel
			ExcelRender er = new ExcelRender(fileName, columns, headers, lr, getResponse());
			er.render();
		}else{
			String[] headers = {"pkid","sys_id","user_flg","status","file_status","sql_style","prio_level",
					"prio_level_other","table_name_full","user_upload_dir","user_upload_bs_dir",
					"upload_file_number","download_file_is_compress","download_file_name","row_number","job_time",
					"begin_time","end_time","remark","job_explain","other_para"};
			String[] columns = { "任务号","系统ID","用户标识","任务处理状态","文件上传状态","SQL类型","处理优先级","未启用",
					"生成的结果表名","未启用","上传文件目录","上传文件数量","下载文件是否进行压缩","结果文件名",
					"处理结果记录条数","任务提交时间","任务开始处理时间","任务结束时间","任务处理日志","任务说明","增加任务时调用的参数"};
			// 转换成excel
			ExcelRender er = new ExcelRender(fileName, columns, headers, lr, getResponse());
			er.render();
		}
		
		
		// 打印日志
		log.info("download--lr:" + lr);
		renderNull();
	}
	

	
}
