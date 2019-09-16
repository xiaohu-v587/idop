package com.goodcol.controller.zxglctl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.core.upload.UploadFile;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.render.MyFileRender;
import com.goodcol.server.zxglserver.ReportTaskServer;
import com.goodcol.util.AppUtils;
import com.goodcol.util.ParamContainer;
import com.goodcol.util.SendMessageUtil;
import com.goodcol.util.ext.anatation.LogBind;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.ext.render.excel.ExcelRender;
import com.goodcol.util.zxgldbutil.CommonUtil;

/**
 * 报表管理
 * 
 * @author wangzhen
 * @date 2018-10-11 下午3:06:35
 */
@RouteBind(path = "/manualReport")
@Before({ ManagerPowerInterceptor.class, OperationLogRecordInterceptor.class })
public class PccmManualReportCtl extends BaseCtl {

	ReportTaskServer server = new ReportTaskServer();
	public static String report_msg = ParamContainer.getDictName("pccm_msg", "7");

	@Override
	public void index() {
		render("index.jsp");
	}

	// 添加页面
	public void form() {
		render("form.jsp");
	}

	// 打开手工报表详情页面
	public void list() {
		render("list.jsp");
	}
	
	// 打开手工报表详情页面
		public void listOfsp() {
			render("listOfsp.jsp");
		}

	// 打开手工报表状态图页面
	public void echart() {
		render("echart.jsp");
	}

	// 预览接受人填报数据
	public void view() {
		render("view.jsp");
	}
	

	// 预览接受人填报数据
	public void views() {
		render("views.jsp");
	}

	// 转发页面
	public void goForward() {
		render("forward.jsp");
	}

	// 报送页面
	public void goReply() {
		render("reply.jsp");
	}

	// 获取报表任务列表
	@LogBind(menuname = "手工报表", type = "3", remark = "报表管理-手工报表列表")
	public void getReports() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageNum", getParaToInt("pageIndex") + 1);
		map.put("pageSize", getParaToInt("pageSize", 10));
		map.put("user_no", getCurrentUser().getStr("user_no"));
		map.put("name", getPara("name"));
		map.put("end_time", getPara("end_time"));
		map.put("rate", getPara("rate"));
		map.put("status", getPara("status"));
		Page<Record> page = server.getListReportSql(map);
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}
	
	//获取报表详情
	public void getReport(){
		String id = getPara("id");
		Record record = server.getDetail(id);
		renderJson(record);
	}
	
	// 上传excel获取表头
	public void saveFile() {
		List<UploadFile> files = getFiles("report");
		Record record = new Record();
		List<Record> list = null;
		for (UploadFile uploadFile : files) {
			String fileName = uploadFile.getOriginalFileName();
			String filePath = null;
			try {
				Record user = getCurrentUser();
				String userNo = user.getStr("user_no");
				filePath = System.currentTimeMillis() + userNo;
				if (uploadFile != null) {
					File file = uploadFile.getFile();
					String suffix = file.getName().substring(file.getName().lastIndexOf("."));
					String path = file.getParent();
					filePath = path + File.separator + filePath + suffix;
					filePath = filePath.replace("\\", "/");
					filePath = filePath.substring(filePath.indexOf("upload"));
					String destPath=ParamContainer.getDictName("report_file_path", "1") + filePath;
					File dest=new File(destPath);
					if(!dest.getParentFile().exists()){
	  					dest.getParentFile().mkdirs();
	  				}
					uploadFile.getFile().renameTo(dest);
					if("true".equals(getPara("excel"))){
						list = server.uploadData(destPath);
						record.set("table", list.subList(0, 1));
						record.set("filepath", filePath);
						record.set("filename", fileName);
					}else{
						server.saveReportFile(getPara("id"), filePath, fileName);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
//		返回json数据IE不兼容
		renderHtml(record.toJson());
	}
	
	//获取文件列表
	public void getFileList(){
		List<Record> list = server.getReportFiles(getPara("id"));
		renderJson(list);
	}
	
	//移除附件
	public void delFile(){
		server.deleteReportFiles(getPara("id"));
		File file = new File(ParamContainer.getDictName("report_file_path", "1") + getPara("filepath"));
		if(file.exists()){
			file.delete();
			setAttr("result", "success");
		}
		renderJson();
	}
	
	//下载附件
	public void download(){
		Record record = server.getFileDetail(getPara("id"));
		File file = new File(ParamContainer.getDictName("report_file_path", "1") + record.getStr("file_url"));
		render(new MyFileRender(file, record.getStr("file_name")));
	}

	// 保存手工报表
	@SuppressWarnings("unchecked")
	public void saveReport() {
		String form = getPara("form");
		String grid = getPara("grid");
		Map<String, Object> map = (Map<String, Object>) JSON.parse(form);
		JSONArray array = JSON.parseArray(grid);
		StringBuffer zdmc = new StringBuffer("");
		StringBuffer zdlx = new StringBuffer("");
		StringBuffer xlpz = new StringBuffer("");
		for (int i = 0; i < array.size(); i++) {
			JSONObject item = array.getJSONObject(i);
			Map<String, Object> temp = (Map<String, Object>) JSON.parse(item.toJSONString());
			zdmc.append(temp.get("zdmc")+",");
			zdlx.append(temp.get("zdlx")==null?",":temp.get("zdlx")+",");
			xlpz.append(temp.get("xlpz")==null?",":((String)temp.get("xlpz")).replaceAll(";", "；")+",");
		}
		map.put("zdmc", zdmc.toString());
		map.put("zdlx", zdlx.toString());
		map.put("xlpz", xlpz.toString());
		map.put("user_no", getCurrentUser().getStr("user_no"));
		map.put("org_no", getCurrentUser().getStr("org_id"));
		server.saveReportTask(map);
		setAttr("result", "success");
		renderJson();
	}
	
	//发布报表任务
	public void start(){
		server.updateTaskStatus(getPara("id"), "2");
		
		//短信提醒  方法自remind
		//Map<String, Object> map = server.findMessIpAndPort();
		List<Record> users = server.remind(getPara("id"), null);
		//SendMessageUtil.sendMessage(users, report_msg, null);
		
		setAttr("result", "success");
		renderJson();
	}
	
	//召回报表任务
	public void recall(){
		server.updateTaskStatus(getPara("id"), "1");
		setAttr("result", "success");
		renderJson();
	}
	
	//停止报表任务
	public void stop(){
		server.updateTaskStatus(getPara("id"), "3");
		setAttr("result", "success");
		renderJson();
	}

	// 删除手工报表
	@LogBind(menuname = "手工报表管理", type = "6", remark = "手工报表-删除")
	public void delReport() {
		server.delReportTask(getPara("id"));
		renderJson();
	}

	//获取我的群组到下拉框
	public void myGroup(){
		List<Record> list = server.getGroupList(getCurrentUser().getStr("user_no"));
		renderJson(list);
	}
	
	//打开附件管理页面
	public void attachment(){
		render("attachment.jsp");
	}
	
	//获取流程图数据
	public void getEchart(){
		List<Record> list = server.getEchart(getPara("id"));
		renderJson(list);
	}
	
	// 获取任务接受人处理情况
	public void getReceives() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageNum", getParaToInt("pageIndex") + 1);
		map.put("pageSize", getParaToInt("pageSize", 10));
		map.put("rid", getPara("id"));
		Page<Record> page = server.getUserReport(map);
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}
	// 获取任务接受人处理情况
		public void getReceivesOfsp() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pageNum", getParaToInt("pageIndex") + 1);
			map.put("pageSize", getParaToInt("pageSize", 10));
			map.put("rid", getPara("id"));
			Page<Record> page = server.getUserReportOfsp(map);
			setAttr("data", page.getList());
			setAttr("total", page.getTotalRow());
			renderJson();
		}
	
	//催办
	public void remind(){
		Map<String, Object> map = server.findMessIpAndPort();
		List<Record> users = server.remind(getPara("rid"), getPara("id"));
		if(StringUtils.isNotEmpty(getPara("id"))&&users.size()==0){
			setAttr("count", users.size());
			setAttr("result", "success");
			setAttr("wlq", "wlq");
			renderJson();
		}
		String sql = "";
		if(StringUtils.isNotEmpty(getPara("rid"))){
			sql = "select t.task_name from  pccm_report_task t where t.id = '"+getPara("rid")+"'";
		}else{
			sql = "select tt.task_name from  pccm_user_report_task t left join pccm_report_task tt on tt.id = t.report_id   where t.id = '"+getPara("id")+"'";
		}
		
		String msg = Db.findFirst(sql).getStr("task_name");
		System.out.println(msg);
		msg = report_msg.replace("task_name", msg);
		SendMessageUtil.sendMessage(users, msg, map);
		setAttr("count", users.size());
		setAttr("result", "success");
		renderJson();
	}
	
	//催办
		public void checkdata(){
			
			String rid = getPara("rid");
			String id  = getPara("id");
			String sql = "select * from pccm_user_report_task t where t.id = '"+id+"'";
			String userno = Db.findFirst(sql).getStr("task_receive_user");
			String flag ="true";
			if(StringUtils.isEmpty(userno)){
				flag="false";
			}
			setAttr("flag", flag);
			renderJson();
		}
		
	
	//退回
	public void sendback(){
		server.updateUserStatus(getPara("id"), "2", getPara("reason"));
		setAttr("result", "success");
		renderJson();
	}
	
	// 导出excel文件
	@LogBind(menuname = "报表管理-手工报表", type = "1", remark = "手工报表-下载")
	public void export() {
		Record record = server.getDetail(getPara("rid"));
		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] column = ((String) record.get("table_head")).split(",");
		// 指定用哪些查询出来的字段填充excel文件
//		String[] headers = new String[column.length];
//		String[] columns = new String[column.length];
//		for(int i=0;i<column.length;i++){
//			headers[i] = "column" + (i+1);
//			columns[i] = column[i];
//		}
		String[] headers = new String[column.length + 2];
		String[] columns = new String[column.length + 2];
		for(int i=0;i<column.length;i++){
			headers[i] = "column" + (i+1);
			columns[i] = column[i];
		}
		headers[column.length] = "submit_date";
		columns[column.length] = "填报时间";
		headers[column.length + 1] = "submit_user_id";
		columns[column.length + 1] = "填报人";
		String fileName = "report.xls";
		try {
//			if(record.getStr("task_file_name") != null){
//				fileName = new String(record.getStr("task_file_name").getBytes("GB2312"), "ISO_8859_1");
//			}else{
				fileName = new String((record.getStr("task_name") + ".xls").getBytes("GB2312"), "ISO_8859_1");
//			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("task_table_name", record.getStr("task_table_name"));
		List<Record> lr = server.getTableData(map);
		// 转换成excel
		ExcelRender er = new ExcelRender(fileName, columns, headers, lr, getResponse());
		er.render();
		// 打印日志
		// log.info("download--lr:" + lr);
		renderNull();
	}
		
	// 获取接收到的任务
	public void getMyReports() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageNum", getParaToInt("pageIndex") + 1);
		map.put("pageSize", getParaToInt("pageSize", 10));
		map.put("user_no", getCurrentUser().getStr("user_no"));
		map.put("org_no", getCurrentUser().getStr("org_id"));
		map.put("org_id", getCurrentUser().getStr("MAX_PERMI_ORGNUM"));
		map.put("taskName", getPara("taskName"));
		String firstOrgNum =  AppUtils.StringUtil(getPara("first_org_num"));
		String secondOrgNum =  AppUtils.StringUtil(getPara("second_org_num"));
		String thirdOrgNum =  AppUtils.StringUtil(getPara("third_org_num"));
		String sendOrg = "";
		if(thirdOrgNum == null){
			if(secondOrgNum == null){
				sendOrg = firstOrgNum;
			}else{
				sendOrg = secondOrgNum;
			}
		}else{
			sendOrg = thirdOrgNum;
		}
		map.put("sendOrg", sendOrg);
		Page<Record> page = server.getMyReportList(map);
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}
	
	public void detail(){
		render("detail.jsp");
	}

	//认领报表任务
	public void claim(){
		server.claimReportTask(getPara("id"), getCurrentUser().getStr("user_no"));
		setAttr("result", "success");
		renderJson();
	}

	// 无需报送
	public void end() {
		server.updateUserStatus(getPara("id"), "3", null);
		setAttr("result", "success");
		renderJson();
	}
	
	public void getForward(){
		Map<String, String> map = server.getTransfer(getPara("id"));
		renderJson(map);
	}

	// 转发
	@SuppressWarnings("unchecked")
	public void forward() {
		String form = getPara("form");
		Map<String, Object> map = (Map<String, Object>) JSON.parse(form);
		map.put("user_no", getCurrentUser().getStr("user_no"));
		map.put("org_no", getCurrentUser().getStr("org_id"));
		server.taskTransfer(map);
		String status = (String) map.get("finish");
		if("3".equals(status) || "4".equals(status)){
			server.updateUserStatus((String)map.get("id"), status, null);
		}
		setAttr("result", "success");
		renderJson();
	}
	
	//获取表数据
	public void getTable(){
		Record record = server.getDetail(getPara("rid"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("org_field", record.getStr("org_field"));
		map.put("fillway", record.getStr("task_fillway"));
		map.put("task_table_name", record.getStr("task_table_name"));
		String org_id = getCurrentUser().getStr("org_id");
		String user_id = getCurrentUser().getStr("user_no");
		if(AppUtils.StringUtil(getPara("uid"))!=null){
			user_id = getPara("uid");
		}
		if(AppUtils.StringUtil(getPara("org"))!=null){
			org_id = getPara("org");
		}
		map.put("user_no", user_id);
		map.put("org_id", org_id);
		List<Record> list = server.getTableData(map);
//		if(list.size()>1000){
//			list = list.subList(0, 1000);
//		}
		setAttr("zdmc", record.getStr("table_head"));
		setAttr("zdlx", record.getStr("fields_location"));
		setAttr("xlpz", record.getStr("serial_number"));
		setAttr("table", list);
		renderJson();
	}
	
	//获取表数据通过填报人ID
	public void getTableByUid(){
		Record record = server.getDetail(getPara("rid"));
		String user_id = getPara("uid");
		String orgid =  getPara("orgid");
		Map<String, Object> map = new HashMap<String, Object>();
		//String level = getCurrentUser().getStr("ROLE_LEVEL");
		map.put("task_table_name", record.getStr("task_table_name"));
		map.put("user_no", user_id);
		map.put("org_field", record.getStr("org_field"));
		map.put("org_id", orgid);
		//map.put("level", level);
		String taskIssuerId = record.getStr("taskIssuerId");
		map.put("taskIssuerId", taskIssuerId);
		List<Record> list = server.getTableData(map);
		setAttr("zdmc", record.getStr("table_head"));
		setAttr("zdlx", record.getStr("fields_location"));
		setAttr("xlpz", record.getStr("serial_number"));
		setAttr("table", list);
		renderJson();
	}
	
	//获取表数据通过填报人ID
		public void getTableByUids(){
			Record record = server.getDetails(getPara("rid"));
			String user_id = getPara("uid");
			String orgid =  getPara("orgid");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("task_table_name", record.getStr("task_table_name"));
			map.put("user_no", user_id);
			map.put("org_field", record.getStr("org_field"));
			map.put("org_id", orgid);

			List<Record> list = server.getTableDatas(map);
			setAttr("zdmc", record.getStr("table_head"));
			setAttr("zdlx", record.getStr("fields_location"));
			setAttr("xlpz", record.getStr("serial_number"));
			setAttr("table", list);
			renderJson();
		}
	
	//获取表数据通过填报人ID
		public void querydata(){
			
			String id = getPara("id");
			String report_id = getPara("rid");
			String flag = "false";
			String sql = "select * from pccm_user_report_task t where t.task_status != '4' and  t.parent_id = '"+id+"'";
			List<Record> r = Db.find(sql);
			if(r.size()==0){
				flag ="true";
			}
			setAttr("flag", flag);			
			renderJson();
		}
		
	
	//保存表数据
	public void saveTable(){
		String grid = getPara("grid");
		JSONArray array = JSON.parseArray(grid);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", array);
		map.put("rid", getPara("rid"));
		map.put("user_no", getCurrentUser().getStr("user_no"));
		server.saveTableDatas(map);
		//保存并上报
		if(getParaToBoolean("reply")){
			server.updateUserStatus(getPara("id"), "4", null);
		}
		setAttr("result", "success");
		renderJson();
	}
	
	// 撤回
	public void retract() {
		server.updateUserStatus(getPara("id"), "1", null);
		setAttr("result", "success");
		renderJson();
	}
	
	
	// 效率页面
	public void time() {
		render("time.jsp");
	}

	// 效率详情页面
	public void timeform() {
		render("timeform.jsp");
	}

	// 获取报表任务效率列表
	public void getTimeList() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageNum", getParaToInt("pageIndex") + 1);
		map.put("pageSize", getParaToInt("pageSize", 10));
		map.put("user_no", getCurrentUser().getStr("user_no"));
		map.put("name", getPara("name"));
		map.put("end_time", getPara("end_time"));
		map.put("rate", getPara("rate"));
		Page<Record> page = server.getTimeList(map);
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}
	
	// 获取报表任务效率详情
	public void getTimeDetail() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageNum", getParaToInt("pageIndex") + 1);
		map.put("pageSize", getParaToInt("pageSize", 10));
		map.put("id", getPara("id"));
		map.put("user_no", getCurrentUser().getStr("user_no"));
		Page<Record> page = server.getTimeDetail(map);
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}

	// 群组管理
	public void group() {
		render("group.jsp");
	}

	//群组新增修改页面
	public void groupform() {
		String groupId = getPara("groupId");
		if(AppUtils.StringUtil(groupId) ==null ){
			groupId = AppUtils.getStringSeq();
		}
		setAttr("groupId", groupId);
		render("groupform.jsp");
	}

	// 发送对象选择-机构
	public void groupitem() {
		String org = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		setAttr("org",org);
		render("groupitem.jsp");
	}
	
	// 发送对象选择
	public void groupuser() {
		String org = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		setAttr("org",org);
		render("groupuser.jsp");
	}

	// 发送对象选择-角色
	public void grouprole() {
		String org = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		setAttr("org",org);
		render("grouprole.jsp");
	}
	
	// 发送对象选择-机构和角色
	public void grouporgrole() {
		String org = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		setAttr("org",org);
		render("grouporgrole.jsp");
	}
	
	// 发送对象选择-机构和人员
	public void grouporguser() {
		String org = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		setAttr("org",org);
		render("grouporguser.jsp");
	}
	
	// 发送对象选择-角色和人员
	public void grouproleuser() {
		String org = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		setAttr("org",org);
		render("grouproleuser.jsp");
	}
	
	// 发送对象选择-人员（机构+角色+人员）
	public void groupnewuser() {
		String org = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		setAttr("org",org);
		render("groupnewuser.jsp");
	}
	
	// 获取机构树
	public void getOrgTree() {
		List<Record> list = server.getOrgList();
		setAttr("datas", list);
		renderJson();
	}
	
	// 获取人员列表
	public void getUserList() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageNum", getParaToInt("pageIndex") + 1);
		map.put("pageSize", getParaToInt("pageSize", 10));
		map.put("org_id", getCurrentUser().getStr("org_id"));// 当前用户的机构
		map.put("name", getPara("name"));
		map.put("user_no", getPara("user_no"));
		Page<Record> page = server.getListOrg(map);
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}
	
	// 获取角色列表
	public void getRoleList() {
		String flag = getPara("level_flag","");//获取当前级别，根据级别显示相应角色
		List<Record> list = null;
		// 获取当前用户信息
	    String listsql = "";
		if(AppUtils.StringUtil(flag)!=null && flag.length() == 1){
			listsql = "SELECT VAL AS SHOWNUM, REMARK AS SHOWNAME, '' AS UPID FROM SYS_PARAM_INFO WHERE KEY = 'ROLE_LEVEL' AND STATUS = '1' AND VAL = '"+flag+"' "
					+ "UNION SELECT T.ID , T.NAME , T.ROLE_LEVEL  FROM SYS_ROLE_INFO T WHERE T.ROLE_DELE_FLAG = '1' AND T.ROLE_LEVEL = '"+flag+"' ";
		}else{
			listsql = "SELECT VAL AS SHOWNUM, REMARK AS SHOWNAME, '' AS UPID FROM SYS_PARAM_INFO WHERE KEY = 'ROLE_LEVEL' AND STATUS = '1' "
					+ "UNION SELECT T.ID , T.NAME , T.ROLE_LEVEL  FROM SYS_ROLE_INFO T WHERE T.ROLE_DELE_FLAG = '1' ";
		}
		list = Db.find(listsql);
		setAttr("datas", list);
		// 打印日志
		renderJson();
	}
	
	
	// 获取角色列表
	public void getRoleUserList() {
		String flag = getPara("level_flag","");//获取当前级别，根据级别显示相应角色
		//员工号
		String userid = getPara("userid"); 
		//员工姓名
		String username = getPara("username"); 
		List<Record> list = null;
		// 获取当前用户信息
		StringBuffer whereSql = new StringBuffer();
		if(AppUtils.StringUtil(flag)!=null && flag.length() == 1){
			whereSql.append(" SELECT SHOWNUM,SHOWNAME,UPID,FLAG FROM ( SELECT VAL AS SHOWNUM, REMARK AS SHOWNAME, '' AS UPID,'ROLE' FLAG FROM SYS_PARAM_INFO WHERE KEY = 'ROLE_LEVEL' AND STATUS = '1' AND VAL = '"+flag+"' ");
			whereSql.append(" UNION SELECT T.ID , T.NAME , T.ROLE_LEVEL, 'ROLE' FLAG  FROM SYS_ROLE_INFO T WHERE T.ROLE_DELE_FLAG = '1' AND T.ROLE_LEVEL = '"+flag+"' ) A WHERE 1=1 ");
		}else{
			whereSql.append(" SELECT SHOWNUM,SHOWNAME,UPID,FLAG FROM ( SELECT VAL AS SHOWNUM, REMARK AS SHOWNAME, '' AS UPID,'ROLE' FLAG FROM SYS_PARAM_INFO WHERE KEY = 'ROLE_LEVEL' AND STATUS = '1' ");
			whereSql.append(" UNION SELECT T.ID , T.NAME , T.ROLE_LEVEL, 'ROLE' FLAG  FROM SYS_ROLE_INFO T WHERE T.ROLE_DELE_FLAG = '1' ) A WHERE 1=1 ");
		}
		
		if (AppUtils.StringUtil(userid) != null ) {
			whereSql.append(" AND A.SHOWNUM IN (SELECT ROLE_ID FROM SYS_USER_ORG_ROLE WHERE USER_NO LIKE '%"+userid+"%' ) ");
		}

		if (AppUtils.StringUtil(username) != null ) {
			whereSql.append(" AND A.SHOWNUM IN (SELECT ROLE_ID FROM SYS_USER_ORG_ROLE WHERE USER_NO IN ( SELECT USER_NO FROM  SYS_USER_INFO WHERE NAME LIKE '%"+username+"%') ) ");
		}
		
		
		whereSql.append(" UNION SELECT T.USER_NO,T.NAME,B.ROLE_ID,'USER' FROM SYS_USER_INFO T LEFT JOIN  SYS_USER_ORG_ROLE B ON T.USER_NO = B.USER_NO WHERE T.DELE_FLAG='0' AND T.ROLE_ID IS NOT NULL AND T.ORG_ID IS NOT NULL AND B.ORGNUM IS NOT NULL");
		if (AppUtils.StringUtil(userid) != null ) {
			whereSql.append(" AND  T.USER_NO LIKE '%"+userid+"%'  ");
		}

		if (AppUtils.StringUtil(username) != null ) {
			whereSql.append(" AND  T.NAME LIKE '%"+username+"%' ");
		}
		
		list = Db.find(whereSql.toString());
		setAttr("datas", list);
		// 打印日志
		renderJson();
	}
	
	
	
	// 获取机构以及机构下的人员列表
	public void getOrgList() {
		
		List<Record> list = null;
		// 获取当前用户信息
		String userNo = getCurrentUser().getStr("USER_NO");
		//是否屏蔽本部，运营部 0-默认屏蔽 ，1-不屏蔽本部 ，运营部
		String noliketype = getPara("noliketype","0"); 
		//员工号
		String userid = getPara("userid"); 
		//员工姓名
		String username = getPara("username"); 
		
		
		
		String  org_id =  getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		String by2 =  AppUtils.getOrgLevel(org_id);
		
		StringBuffer userSql = new StringBuffer();
		StringBuffer whereSql = new StringBuffer();
		
		//select o.ORGNUM as shownum,o.orgname as showname,o.UPID as upid ,o.by2 as by2,'org' as flag
		userSql.append(" SELECT ORGNUM FROM SYS_ORG_INFO A WHERE A.STAT = 1 ");
		whereSql.append("SELECT ORGNAME,ID,ORGNUM,UPID,BY2,FLAG FROM ( SELECT ORGNAME,ID,ORGNUM,UPID,BY2,'ORG' FLAG FROM SYS_ORG_INFO A WHERE A.STAT = 1 ");
		if("0".equals(noliketype)){
			//whereSql.append(CommonUtil.orgNameSql());
			whereSql.append(CommonUtil.orgNameSqlNoBenBu());
			
			//userSql.append(CommonUtil.orgNameSql());
			userSql.append(CommonUtil.orgNameSqlNoBenBu());
			
		}else if("1".equals(noliketype)){
			//whereSql.append(CommonUtil.orgNameSql());
			whereSql.append(CommonUtil.orgNameSqlNoBenBu());
			
			//userSql.append(CommonUtil.orgNameSql());
			userSql.append(CommonUtil.orgNameSqlNoBenBu());
			
		}else if("2".equals(noliketype)){
			whereSql.append(CommonUtil.orgNameSqlToUser());
			
			userSql.append(CommonUtil.orgNameSqlToUser());
		}
		if (AppUtils.StringUtil(org_id) != null ) {
			
			if(!"1".equals(by2)){
				whereSql.append(" and (id = '"+org_id+"' or by5 like '%" + org_id + "%') ");
				
				userSql.append(" and (id = '"+org_id+"' or by5 like '%" + org_id + "%') ");
			}
		}
		
		if (AppUtils.StringUtil(userid) != null ) {
			whereSql.append(" and id in (select orgnum from SYS_USER_ORG_ROLE where user_no like '%"+userid+"%' ) ");
			userSql.append(" and id in (select orgnum from SYS_USER_ORG_ROLE where user_no like '%"+userid+"%' ) ");
		}

		if (AppUtils.StringUtil(username) != null ) {
			whereSql.append(" and id in (select orgnum from SYS_USER_ORG_ROLE where user_no in ( select user_no from  SYS_USER_INFO where name like '%"+username+"%') ) ");
			userSql.append(" and id in (select orgnum from SYS_USER_ORG_ROLE where user_no in ( select user_no from  SYS_USER_INFO where name like '%"+username+"%') ) ");
		}
		whereSql.append(" order by ORGNUM asc ) ");
		whereSql.append(" UNION SELECT T.NAME,T.ID,T.USER_NO,B.ORGNUM,' ','USER' FROM SYS_USER_INFO T LEFT JOIN  SYS_USER_ORG_ROLE B ON T.USER_NO = B.USER_NO WHERE T.DELE_FLAG='0' AND T.ROLE_ID IS NOT NULL AND T.ORG_ID IS NOT NULL AND B.ORGNUM IS NOT NULL");
		if (AppUtils.StringUtil(userid) != null ) {
			whereSql.append(" and  t.user_no like '%"+userid+"%'  ");
		}

		if (AppUtils.StringUtil(username) != null ) {
			whereSql.append(" and  t.name like '%"+username+"%' ");
		}
		
		whereSql.append(" AND T.ORG_ID IN ("+userSql.toString()+") ");
		
		String sql = whereSql.toString();
		list = Db.find(sql);
		setAttr("datas", list);
		// 打印日志
		renderJson();
	}
	
	
	// 获取角色列表-不限制机构和角色
	public void getNewUserList() {
		//员工号
		String userid = getPara("userid"); 
		//员工姓名
		String username = getPara("username"); 
		//员工机构
		String roleid = getPara("roleid"); 
		//员工角色
		String orgnum = getPara("orgnum"); 		
		
		List<String> paramList = new ArrayList<String>();
		// 获取当前用户信息
		StringBuffer selectSql = new StringBuffer();
		StringBuffer whereSql = new StringBuffer();
		selectSql.append(" SELECT A.NAME, A.USER_NO, B.ORGNUM, B.ROLE_ID,C.NAME ROLENAME,D.ORGNAME ");
		whereSql.append(" FROM SYS_USER_ORG_ROLE B ");
		whereSql.append(" INNER JOIN SYS_USER_INFO A ");
		whereSql.append("  ON A.USER_NO = B.USER_NO ");
		whereSql.append(" LEFT JOIN SYS_ROLE_INFO C ");
		whereSql.append("  ON B.ROLE_ID = C.ID ");
		whereSql.append(" LEFT JOIN SYS_ORG_INFO D ");
		whereSql.append("   ON B.ORGNUM = D.ORGNUM ");
		whereSql.append(" WHERE D.ORGNUM IS NOT NULL AND C.ID IS NOT NULL AND C.ROLE_DELE_FLAG = '1' AND A.DELE_FLAG = '0' AND D.STAT = 1");
		
		if (AppUtils.StringUtil(userid) != null ) {
			whereSql.append(" AND  A.USER_NO LIKE ?  ");
			paramList.add("%"+userid.trim()+"%");
		}

		if (AppUtils.StringUtil(username) != null ) {
			whereSql.append(" AND  A.NAME LIKE ? ");
			paramList.add("%"+username+"%");
		}
		
		if (AppUtils.StringUtil(roleid) != null ) {
			String [] roleids = roleid.split(regex);
			whereSql.append(" AND  C.ID IN "+getParasToStringRegexStr(roleid)+" ");
			for (String  num : roleids) {
				paramList.add(num);
			}
		}
		
		if (AppUtils.StringUtil(orgnum) != null ) {
			String [] orgnums = orgnum.split(regex);
			whereSql.append(" AND  D.ORGNUM IN "+getParasToStringRegexStr(orgnum)+" ");
			for (String  num : orgnums) {
				paramList.add(num);
			}
		}
		
		int pageNumber = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		Page<Record> page = Db.paginate(pageNumber, pageSize, selectSql.toString(), whereSql.toString(),paramList.toArray());
		setAttr("datas", page.getList());
		setAttr("total", page.getTotalRow());
		
		/*List<Record> list = Db.find(whereSql.toString(),paramList.toArray());
		setAttr("datas", list);*/
		// 打印日志
		renderJson();
	}
	

	// 获取登录人的群组
	public void getGroups() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageNum", getParaToInt("pageIndex") + 1);
		map.put("pageSize", getParaToInt("pageSize", 10));
		map.put("user_no", getCurrentUser().getStr("user_no"));
		map.put("function_group", getPara("function_group","0"));
		map.put("name", getPara("name"));
		Page<Record> page = server.getGroupList(map);
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}

	// 保存群组
	@SuppressWarnings({ "unchecked" })
	public void saveGroup() {
		String form = getPara("form");
		Map<String, Object> map = (Map<String, Object>) JSON.parse(form);
		map.put("user_no", getCurrentUser().getStr("user_no"));
		map.put("orgid", getCurrentUser().getStr("org_id"));
		map.put("org_id",  getCurrentUser().getStr("MAX_PERMI_ORGNUM"));
		server.saveGroup(map);
		setAttr("result", "success");
		renderJson();
	}

	// 删除群组
	@LogBind(menuname = "手工报表管理", type = "6", remark = "手工报表-删除")
	public void delGroup() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("gid",getPara("id"));
		server.deleteGroup(map);
		renderJson();
	}
	
	/**
	 * 跳转到角色移交页面
	 * 2018年11月16日10:53:14
	 * @author liutao
	 */
	public void roleTransfer() {
		render("roleTransfer.jsp");
	}
	
	/**
	 * 获取所有的用户信息，(角色移交查询所有的人进行选择移交)
	 * 2018年11月16日11:01:09
	 * @author liutao
	 */
	public void getAllUser(){
		Map<String, Object> map = new HashMap<String, Object>();
		String user_no = getPara("user_no");
		String user_name = getPara("user_name");
		map.put("user_no", user_no);
		map.put("user_name", user_name);
		Record user = getCurrentUser();
		String currentUserNo = user.getStr("user_no");
		map.put("currentUserNo", currentUserNo);
		map.put("pageNum", getParaToInt("pageIndex") + 1);
		map.put("pageSize", getParaToInt("pageSize", 10));
		Page<Record> page = server.getAllUser(map);
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}
	
	/**
	 * 保存角色移交信息
	 * 2018年11月16日14:28:34
	 * @author liutao
	 */
	public void saveRoleTransferInfo(){
		Map<String, Object> map = new HashMap<String, Object>();
		String user_id = getPara("user_id");
		Record user = getCurrentUser();
		String currentUserNo = user.getStr("user_no");
		String role_id = user.getStr("roleid");
		map.put("user_id", user_id);
		map.put("role_id", role_id);
		map.put("currentUserNo", currentUserNo);
		int flag = server.saveRoleTransferInfo(map);
		setAttr("flag", flag);
		renderJson();
	}
	public void checkcondition(){
		String rid = getPara("rid");
		String id = getPara("id");
		String sql = "select * from pccm_user_report_task t where t.id = '"+id+"'";
		String parent_id = Db.findFirst(sql).getStr("parent_id");
		String flag = "true";
		if(StringUtils.isEmpty(parent_id)){
			flag = "true";
		}else{
			String sqls = "select * from pccm_user_report_task t where t.id = '"+parent_id+"'";
			String task_status = Db.findFirst(sqls).getStr("task_status");
			if("1".equals(task_status)||"0".equals(task_status)||"2".equals(task_status)){
				flag = "true";
			}else{
				flag = "false";
			}
		}
		setAttr("flag",flag);
		renderJson();
	}
}
