package com.goodcol.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.core.plugin.activerecord.tx.Tx;
import com.goodcol.core.upload.UploadFile;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.MissionControlUtil;
import com.goodcol.util.MissionUtil;
import com.goodcol.util.PropertiesContent;
import com.goodcol.util.execl.ExcelRead;
import com.goodcol.util.execl.ExcelRenderx;
import com.goodcol.util.ext.anatation.RouteBind;

/**
 * 任务配置
 * 
 * @author cxy
 */
@RouteBind(path = "/missionconfig")
@Before({ ManagerPowerInterceptor.class })
public class MissionConfigCtl extends BaseCtl {
	public static Logger log = Logger.getLogger(MissionConfigCtl.class);

	//@Before(PermissionInterceptor.class)
	public void index() {
		render("index.jsp");
	}

	/**
	 * 获取已配置的任务列表
	 */
	public void getList() {
		List<String> listStr = new ArrayList<String>();
		Record user = getCurrentUser();
		/*和数据权限有关的  加机构条件 
		用户角色
		String roleNames = AppUtils.getRoleNames(getCurrentUser().getStr("ID"));
		获取当前机构号
		String orgId = getCurrentUser().getStr("ORG_ID");
		获取当前用户最高等级权限
		String roleLevel = AppUtils.getLevByRole(roleNames);
		最大权限的机构号	
		String orgNum = AppUtils.getUpOrg(orgId, roleLevel);
		//id代表9位机构号
		sb.append(" and id in (select id from sys_org_info where id = '" + orgNum + "' or by5 like '%" + orgNum + "%')");
		*/
		String sql = "SELECT * ";
		StringBuffer extrasql = new StringBuffer("  FROM YYGL_MISSION_CONFIG WHERE 1=1 ");
		
		//判定是否最高权限
		if(MissionUtil.getIntanceof().isSysManagerByUser(user)){//高权限人员
			//不限制筛选范围
		}else{
			//限定筛选自己创建的
			extrasql.append(" AND MISSION_LAUNCH_USERNO  = ? AND  MISSION_LAUNCH_ORG = ? ");
			listStr.add(user.getStr("USER_NO")); //登录人
			listStr.add(user.getStr("ORG_ID"));  //登录机构
		}
		
		//组装查询条件
		
		//任务名称模糊查询
		//任务分类精确查新
		setFindPara(extrasql, listStr, "mission_name".split(regex), "mission_flag,delete_flag".split(regex));
		
		//任务创建时间范围查询
		betweenPara(extrasql, listStr, "createdate".split(regex));
		
		//特殊逻辑条件
		String deleteflag = getPara("delete_flag");
		if(AppUtils.StringUtil(deleteflag) == null){
			extrasql.append("  AND DELETE_FLAG = '1' ");
		}
		
		Page<Record> r = defualtPaginate(sql, extrasql.toString()+" ORDER BY CREATEDATE DESC", listStr.toArray());
		// 打印日志
		log.debug("listUser--r.getList():" + r.getList());
		log.debug("listUser--r.getTotalRow():" + r.getTotalRow());
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "任务配置", "3", "任务配置-查询");
		renderJson();
	}

	/**
	 * 任务添加页面
	 */
	//@Before(PermissionInterceptor.class)
	public void add() {
		setAttr("id", AppUtils.getStringSeq());
		setAttr("pageType", "add");
		render("form.jsp");
	}

	/**
	 * 任务添加页面
	 */
	//@Before(PermissionInterceptor.class)
	public void edit() {
		setAttr("id", getPara("id"));
		setAttr("pageType", "edit");
		render("form.jsp");
		
	}
	
	
	
	/**
	 * 获取编辑时的回显数据
	 */
	public void getDetail() {
		String id = getPara("id");
		String sql = "select * from YYGL_MISSION_CONFIG where 1 = 1 ";
		StringBuffer sb = new StringBuffer();
		List<String> listStr = new ArrayList<String>();

		if (AppUtils.StringUtil(id) != null) {
			sb.append(" and id = ? ");
			listStr.add(id.trim());
		}
		List<Record> list = Db.find(sql + sb.toString(), listStr.toArray());
		setAttr("data", list.get(0));
		log.debug("任务配置-查询详情");
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "任务配置", "3","任务配置-查询详情");
		renderJson();

	}
	
	
	/**
	 * 保存新增的任务信息
	 */
	@Before(Tx.class)
	public void save() {
		//获取登录人员信息
		Record user = getCurrentUser();
		
		
		String copyflag = getPara("copyflag","0"); //任务复制标识 0-非复制，1-复制
		String model_cfg_id = getPara("model_cfg_id");
		
		//任务创建信息
		Record task = getParaByParasBystr("id,mission_name,assign_feedbank_group,mission_type,mission_deadline,mission_deadline_hh,mission_deadline_mm,message_type,message_id,mission_loop_cycel,mission_strat_day,mission_flag,mission_require,model_cfg_id,mission_isfeedback,mission_sign,mission_activiti");
		
		//对任务数据进行验证对于常见的验证可以的放在验证拦截器中，但此处为了速度写在此处
		//空值校验等常规校验放在页面空值,后台对非空字段放在数据库验证？
		String flag = "0";
		//可能需要验证模板是否已经保存到数据库中
		//生成任务编号
		//复制参数
		Record param = getParaByParasBystr("copyflag,model_cfg_id,id,mission_flag");
		//判断任务是否需要复制
		if("1".equals(copyflag)){
			param.set("new_model_cfg_id",AppUtils.getStringSeq());
			//通过任务分类调度器去复制对应任务数据,反馈执行结果，flag 1-正常执行，其他代码- 错误
			Record result = MissionControlUtil.getIntanceof().copyModelConfig(param); 
			if(!"1".equals(result.get("flag"))){
				flag = "-1"; //模板信息保存失败
				renderText(flag);
				return;
			}else{
				task.set("model_cfg_id", param.get("new_model_cfg_id"));
			}
			
			
		}
		
		//保存配置到数据库
		task
		//.set("ID", AppUtils.getStringSeq())
		.set("CREATEDATE", DateTimeUtil.getTime())
		.set("DELETE_FLAG","1")
		.set("MISSION_ISFEEDBACK","1")
		.set("MISSION_SIGN","1")
		.set("MISSION_LAUNCH_ORG", user.get("ORG_ID"))
		.set("MISSION_LAUNCH_USERNO",  user.get("USER_NO"));
		
		Db.save("YYGL_MISSION_CONFIG", task);
		
		log.debug("任务配置-新增");
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "任务配置", "4", "任务配置-新增");
		
		renderText(flag);
	}

	
	
	/**
	 * 更新任务信息
	 **/
	@Before(Tx.class)
	public void update() {
		//获取登录人员信息
		//Record user = getCurrentUser();
		String columns = "id,mission_name,assign_feedbank_group,mission_type,mission_deadline,mission_deadline_hh,mission_deadline_mm,message_type,message_id,mission_loop_cycel,mission_strat_day,mission_flag,mission_require,model_cfg_id,mission_isfeedback,mission_sign,mission_activiti";
		Record  task  = getParaByParasBystr(columns);
		//任务创建信息
		
		//对任务数据进行验证对于常见的验证可以的放在验证拦截器中，但此处为了速度写在此处
		//空值校验等常规校验放在页面空值,后台对非空字段放在数据库验证？
		String flag = "0";
		
	
		
		//判定任务是否存在进行中的任务，对于进行中的任务不允许修改
		boolean isTaskConduct = false;

		Record r = Db.findFirst(" SELECT COUNT(1) CUT FROM YYGL_MISSION_ISSUE WHERE MISSION_CONFIG_ID = ? AND MISSION_END_YMD > ? ",task.get("id"),DateTimeUtil.getTime());
		if("0".equals(r.get("CUT"))){
			isTaskConduct = true;
		}
		
		if(isTaskConduct){
			flag = "-1"; //进行中的任务不允许修改
			renderText(flag);
			return;
		}
		
		//可能需要验证模板是否已经保存到数据库中
		
		//保存配置到数据库
		task.set("CREATEDATE",  DateTimeUtil.getTime());
		
		Db.update("YYGL_MISSION_CONFIG", "id", task);
		log.debug("任务配置-更新");
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "任务配置", "5", "任务配置-更新");
		renderJson(flag);
	}

	/**
	 * 删除任务配置
	 */
	@Before(Tx.class)
	public void del() {
		String id = getPara("id");
		String flag = "0";
		//目前任务只做逻辑删除，对于身份验证，由于安全级别不高，只要能看到就能操作，不对数据做严格验证
		//判定任务是否存在进行中的任务，对于进行中的任务不允许删除
		boolean isTaskConduct = false;

		Record r = Db.findFirst(" SELECT COUNT(1) CUT FROM YYGL_MISSION_ISSUE WHERE MISSION_CONFIG_ID = ? AND MISSION_END_YMD > ? ",id,DateTimeUtil.getTime());
		if("0".equals(r.get("CUT"))){
			isTaskConduct = true;
		}
		
		if(isTaskConduct){
			flag = "-1"; //进行中的任务不允许删除
			renderText(flag);
			return;
		}
		
		int count = Db.update("UPDATE YYGL_MISSION_CONFIG SET DELETE_FLAG = 0 WHERE ID = ? ", id);
		if(count == 0){
			flag = "-2";//该任务已被他人删除 
		}
		
		log.info("任务配置-删除");
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "任务配置", "6", "任务配置-删除");
		renderText(flag);
	}
	
	/**
	 * 启用/停用
	 */
	@Before(Tx.class)
	public void changeUnseal(){
		String id = getPara("id");
		String action = getPara("action");
		String flag = "0";
		//任务激活标识（0-激活 1-未激活）
		
		//激活任务，激活的任务进入任务调度中
		int count = Db.update("UPDATE YYGL_MISSION_CONFIG SET MISSION_ACTIVITI = ? WHERE ID = ?  AND DELETE_FLAG  = '1' ",action, id);
		if(count == 0){
			flag = "-1";//该任务已被他人删除 
		}
		// 记录操作日志
		log.info("任务配置-激活任务");
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "激活/禁用任务", "5", "任务配置-激活/禁用");
		renderText(flag);
		
	}
	
	/**
	 * 下发
	 */
	@Before(Tx.class)
	public void lowerHair() {
		//获取登录人员信息
		//Record user = getCurrentUser();
		String flag = "0";
		String id = getPara("id");
		
		//判断该任务如果是临时性的任务，下发时执行下发逻辑，循环性的任务下发时开始纳入下发任务调度中
		Record r = Db.findFirst("SELECT MISSION_ACTIVITI,MISSION_TYPE FROM YYGL_MISSION_CONFIG WHERE ID = ? AND DELETE_FLAG  = '1' ",id);
		if(r == null){
			flag = "-1"; //任务已被他人删除
			renderText(flag);
			return;
		}
		//下发任务成功标识
		boolean isIssue = false;
		//如果当前任务是一次性的任务并且任务
		if("1".equals(r.get("MISSION_TYPE"))){
			//进行下发动作,反回下发结果
			isIssue = MissionControlUtil.getIntanceof().releaseMission(new Record().set("id", id)); 
			if(!isIssue){
				flag = "-2"; //任务下发失败
				renderText(flag);
				return;
			}
			
		}
		
		//激活任务，激活的任务进入任务调度中
		int count = Db.update("UPDATE YYGL_MISSION_CONFIG SET MISSION_ACTIVITI = 0 WHERE ID = ?  AND DELETE_FLAG  = '1' ", id);
		
		if(count == 0){
			flag = "-1";//该任务已被他人删除 
		}
		// 记录操作日志
		log.info("任务配置-下发");
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "下发", "5", "任务配置-下发");
		renderText(flag);
	}
	/**
	 * 下载任务配置列表
	 */
	@Before(Tx.class)
	public void download(){
		getList();
		List<Record> list = getAttr("data");
		String[] headers = getPara("headers","").split(regex);
		String[] columns = getPara("columns","").split(regex);
		String fileName = "";
		try {
		    fileName = new String(("任务配置列表"+System.currentTimeMillis()+".xls").getBytes("GB2312"),"ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		}
		ExcelRenderx er = new ExcelRenderx(fileName, columns, headers, list,getResponse());
		er.render();
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "任务配置".toString(), "7","任务配置-下载");
		log.info(getLogPoint().exit("任务配置-下载").toString());
		renderNull();
	}
	
	/**
	 * 下载模板
	 */
	@Before(Tx.class)
	public void downloadTemplet(){
		//可根据任务类型打开任务模板页
		String forwardAction =  MissionControlUtil.getIntanceof().downloadTemplet(getPara("id"));
		if(AppUtils.StringUtil(forwardAction) != null){
			forwardAction(forwardAction);
		}else{
			renderHtml("<script type='text/javascript'>parent.callback(\"未找到该任务分类对应配置项,或不提供下载!\"); </script>");
		}
		
		//MissionControlUtil.getIntanceof().downloadTemplet(new Record().set("id", getPara("id")).set("ctl","MissionConfigCtl"), getResponse());
		
		/*//查询任务模板配置
		Record task  = Db.findById("YYGL_MISSION_CONFIG", id);
		//查询任务模板配置
		Record mode  = Db.findById("YYGL_MISSION_MODEL_CONFIG", task.get("MODEL_CFG_ID"));
		//查询任务列配置,按照字段名从小到大排序
		List<Record> modecols  = Db.find("SELECT * FROM YYGL_MISSION_COLUMN_CONFIG WHERE MODEL_CFG_ID  = ? ORDER BY COL_NAME ", task.get("MODEL_CFG_ID"));
		//查询模板内容数据
			//头部
		List<Record> modedatascol  = Db.find("SELECT * FROM YYGL_MISSION_ORIGINAL_DATA WHERE MODEL_CFG_ID  = ? AND DATA_TYPE = '0' ", task.get("MODEL_CFG_ID"));
			//数据行
		List<Record> modedatasrow  = Db.find("SELECT * FROM YYGL_MISSION_ORIGINAL_DATA WHERE MODEL_CFG_ID  = ? AND DATA_TYPE = '1' ", task.get("MODEL_CFG_ID"));		
		
		//取到群组信息
		String group = task.getStr("ASSIGN_FEEDBANK_GROUP");
		
		//根据当前登录人抽取机构信息
		
		//根据
		
		//对模板头内容进行处理
		for (int i = 0,len = modedatascol.size(); i < len; i++) {
			//找到需要抽取数据的列，目前提供抽取机构号，机构名称，按照角色抽取人
			
		}
		
		//合并单元参数
		
		
		
		
		
		//对模板内容进行处理
		for (int i = 0,len = modedatasrow.size(); i < len; i++) {
			
		}
		
		//生成数据模板
		List<Record> list = getAttr("list");
		String[] headers = "".split(regex);
		String[] columns = "".split(regex);
		String fileName = "";
		try {
		    fileName = new String(("任务配置模板"+System.currentTimeMillis()+".xls").getBytes("GB2312"),"ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		}
		ExcelRenderx er = new ExcelRenderx(fileName, columns, headers, list,getResponse());
		er.render();
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "任务配置".toString(), "7","任务配置-模板下载");
		log.info(getLogPoint().exit("任务配置-模板下载").toString());
		renderNull();
		*/
		// 记录操作日志
		
	}
	
	
	public void showExcelDetail() throws Exception {
		String fileName = ""; // 上传的文件名称
		List<UploadFile> ufList = new ArrayList<UploadFile>();
		String maxPost = "15MB";
		try {
			int maxPostSize = PropertiesContent.getToInteger("maxPostSize", 15 * 1024 * 1024);
			maxPost = AppUtils.bytesToSize(maxPostSize);
			String uploadpath = PropertiesContent.get("upload_path");//上传文件地址
			ufList = getFiles(uploadpath, maxPostSize);
		} catch (Exception e) {
			renderHtml("<script type='text/javascript'>parent.callback(\"文件大小不得超过" + maxPost + ",请检查！\"); </script>");
			return;
		}
		
		Boolean isAutoHeads = getParaToBoolean("isAutoHeads",false);
		String  autoHeadKey = getPara("autoHeadKey","");
		
		Boolean isArr = getParaToBoolean("isArr",false);
		
		String[] columns = new String[]{};
		List<Record> res = new ArrayList<Record>();
		
		List<Record> resList = new ArrayList<Record>();
		
		List<String []> resArr = new ArrayList<String []>();
		
		if (ufList.size() > 0) {
			for (UploadFile up : ufList) {
				fileName = up.getFileName();
				File tempfile = up.getFile();
				long fileSize = tempfile.length();
				if (fileSize > 15 * 1024 * 1024) {
					renderText("<script>parent.callback(\"文件大小不能超过15M!\"); </script>");
					tempfile.delete();
					return;
				}
				String newName = fileName.toLowerCase().substring(fileName.indexOf(".")+1);
				if(( newName.length() == 4 && !newName.equals("xlsx") ) || ( newName.length() == 3 && !newName.equals("xls") )){
					renderHtml("<script type='text/javascript'>parent.callback(\"除Excel之外的文档不可预览,请检查！\"); </script>");
					return;
				}
				ExcelRead er = new ExcelRead();
				
				if(isArr){
					resArr = er.readExcelToCol(tempfile);
				}else{
					//er.setInintRow(0);
					er.setAutoHeads(isAutoHeads);
					er.setAutoHeadKey(autoHeadKey);
					// 读取Excel数据到List中
					//res = er.readExcel(tempfile);
					resList = er.readExcel(tempfile);
					//columns = er.getHeads();
				}
				
				tempfile.delete();
			}
		}
		Record r1 = new Record();
		if(isArr){
			r1.set("data", resArr);
		}else{
			r1.set("data", resList);
		}
		
		//r1.set("columns", columns);
		renderText(r1.toJson());
	}
	
	/**
	 * 模板配置页面
	 */
	public void addModel(){
		String id = getPara("id");
		String mission_flag = getPara("mission_flag");
		if(AppUtils.StringUtil(id) == null){
			id = AppUtils.getStringSeq();
		}
		setAttr("id", id);
		setAttr("pid", getPara("pid"));
		//可根据任务类型打开任务模板页
		String forwardAction =  MissionControlUtil.getIntanceof().getAddModelPage(getPara("pid"),mission_flag);
		if(AppUtils.StringUtil(forwardAction) != null){
			forwardAction(forwardAction);
		}else{
			renderText("未找到该任务分类对应配置页！");
		}
	}
	
}
