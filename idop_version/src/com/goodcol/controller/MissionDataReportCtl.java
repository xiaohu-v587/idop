package com.goodcol.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.util.CellRangeAddress;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.core.plugin.activerecord.tx.Tx;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.MissionControlUtil;
import com.goodcol.util.execl.ExcelRenderx;
import com.goodcol.util.ext.anatation.RouteBind;

/**
 * 数据类业务服务
 * 
 * @author cxy
 */
@RouteBind(path = "/missiondatareport")
@Before({ ManagerPowerInterceptor.class })
public class MissionDataReportCtl extends BaseCtl {
	public static Logger log = Logger.getLogger(MissionConfigCtl.class);
	/**
	 * 不提供首页
	 */
	@Override
	public void index() {
		
	}
	/**
	 * 根据 配置的合并单元格的配置信息，转化为execl 合并对象数据
	 * @param mergecells json 配置的合并信息
	 * @return poi 识别的合并数据
	 */
	private List<CellRangeAddress> HandsontableMergeToPoiMerge(String mergecells){
		List<CellRangeAddress>  regionList = new ArrayList<CellRangeAddress>();
		JSONArray data_array = JSON.parseArray(mergecells);
		for (int i = 0,len = data_array.size(); i < len ; i++) {
			JSONObject item = data_array.getJSONObject(i);
			regionList.add(new CellRangeAddress(item.getIntValue("row"), item.getIntValue("col"), (item.getIntValue("row")+item.getIntValue("rowspan")-1), (item.getIntValue("col")+item.getIntValue("colspan")-1)));
		}
		return regionList;
	}
	
	
	/**
	 * 任务模板下载
	 * @param taskid
	 * @param response
	 */
	/*public void downloadTemplet(Record params ,HttpServletResponse response){
		//下载数据模板,暂时不考虑抽取数据情况
		String taskid = params.getStr("id"); //配置id
		String type = params.getStr("type"); //阶段
		
		//获取任务配置数据
		Record taskparam =  Db.findById("YYGL_MISSION_CONFIG",taskid);
		
		String id = taskparam.getStr("model_cfg_id");
		//查询配置信息
		Record model = Db.findById("YYGL_MISSION_MODEL_CONFIG", id);
			
		String mergecells = model.getStr("mergecells");
		
		//转换为可识别的合并方式
		List<CellRangeAddress> mergecellsRange = HandsontableMergeToPoiMerge(mergecells);
		//保存数据表内容，包括头以及数据列 row,col,rowto,colto
		 
		
		//查询列配置信息
		List<Record> columns = Db.find("SELECT * FROM YYGL_MISSION_COLUMN_CONFIG T WHERE MODEL_CFG_ID=? ORDER BY TO_NUMBER(SUBSTR(T.COL_NAME,10))", id);
		StringBuffer sqlcol = new StringBuffer("*");
		String[] headers = new String [columns.size()];
		boolean isfir = true;
		for (int j = 0,len = columns.size(); j < len ; j++) {
			if(isfir){
				sqlcol.delete(0, sqlcol.length());
				isfir = false;
			}else{
				sqlcol.append(",");
			}
			sqlcol.append(columns.get(j).getStr("col_name"));
			headers[j] = columns.get(j).getStr("col_name");
		}
		//查询数据内容
		List<Record> list = Db.find("SELECT "+sqlcol.toString()+" FROM YYGL_MISSION_ORIGINAL_DATA WHERE MODEL_CFG_ID = ? ORDER BY ROW_NUMBER", id);
		
		String[] columns_title = new String[]{};
		String fileName = "";
		try {
		    fileName = new String(("任务模板"+System.currentTimeMillis()+".xls").getBytes("GB2312"),"ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		}
		ExcelRenderx er = new ExcelRenderx(fileName, columns_title, headers, list,response);
		er.setMergecells(mergecellsRange);
		er.render();
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "任务配置".toString(), "7","任务配置-模板下载");
		log.info(getLogPoint().exit("任务配置-模板下载").toString());
		renderNull();
	}*/
	
	/**
	 * 下载任务模板信息
	 */
	public void downloadTemplet(){
		//下载数据模板,暂时不考虑抽取数据情况
		String taskid = getPara("id"); //配置id
		String type = getPara("type"); //阶段 例如配置/已发起阶段，此时查看模板是查看全量的情况，如果存在模板配置了按照 XXXX 拆分的情况，此时需要根据阶段来处理是否根据人员机构角色获取对应机构数据
		
		//获取任务配置数据
		Record taskparam =  Db.findById("YYGL_MISSION_CONFIG",taskid);
		
		String id = taskparam.getStr("model_cfg_id");
		//查询配置信息
		Record model = Db.findById("YYGL_MISSION_MODEL_CONFIG", id);
			
		String mergecells = model.getStr("mergecells");
		
		//转换为可识别的合并方式
		List<CellRangeAddress> mergecellsRange = HandsontableMergeToPoiMerge(mergecells);
		//保存数据表内容，包括头以及数据列 row,col,rowto,colto
		 
		
		//查询列配置信息
		List<Record> columns = Db.find("SELECT * FROM YYGL_MISSION_COLUMN_CONFIG T WHERE MODEL_CFG_ID=? ORDER BY TO_NUMBER(SUBSTR(T.COL_NAME,10))", id);
		StringBuffer sqlcol = new StringBuffer("*");
		String[] headers = new String [columns.size()]; //存储数据头部字段
		boolean isfir = true;
		for (int j = 0,len = columns.size(); j < len ; j++) {
			if(isfir){
				sqlcol.delete(0, sqlcol.length());
				isfir = false;
			}else{
				sqlcol.append(",");
			}
			sqlcol.append(columns.get(j).getStr("col_name"));
			
			headers[j] = columns.get(j).getStr("col_name");//主要是列名 例如 DATA_C_1, DATA_C_2....
		}
		//查询数据内容
		List<Record> list = Db.find("SELECT "+sqlcol.toString()+" FROM YYGL_MISSION_ORIGINAL_DATA WHERE MODEL_CFG_ID = ? ORDER BY ROW_NUMBER", id);
		
		String[] columns_title = new String[]{};
		String fileName = "";
		try {
		    fileName = new String(("任务模板"+System.currentTimeMillis()+".xls").getBytes("GB2312"),"ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		}
		ExcelRenderx er = new ExcelRenderx(fileName, columns_title, headers, list,getResponse());
		er.setMergecells(mergecellsRange);
		er.render();
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "任务配置".toString(), "7","任务配置-模板下载");
		log.info(getLogPoint().exit("任务配置-模板下载").toString());
		renderNull();
	}
	
	
	/**
	 * 下载任务模板信息
	 */
	public void downloadTaskTemplet(){
		//下载数据模板,暂时不考虑抽取数据情况
		String userTaskid = getPara("id"); //配置id
		
		String type = getPara("type"); //阶段 例如配置/已发起阶段，此时查看模板是查看全量的情况，如果存在模板配置了按照 XXXX 拆分的情况，此时需要根据阶段来处理是否根据人员机构角色获取对应机构数据

		//主要应用于 待处理和已处理阶段。一般来讲会传入 任务 id ,不会传入配置id,所以需要对id进行查询
		
		//下载的模板依据于最新的模板配置参数进行
		
		
		//获取当前任务数据
		Record userTask = Db.findById("YYGL_MISSION_USER", userTaskid);
		//获取任务配置数据
		Record taskparam= Db.findFirst("SELECT * FROM YYGL_MISSION_CONFIG C WHERE C.ID  =(SELECT MISSION_CONFIG_ID FROM YYGL_MISSION_ISSUE I WHERE I.ID = ? )",userTask.get("MISSION_ISSUE_ID"));
		
		String id = taskparam.getStr("model_cfg_id");
		//查询配置信息
		Record model = Db.findById("YYGL_MISSION_MODEL_CONFIG", id);
			
		String mergecells = model.getStr("mergecells");
		
		String org_break_type =  model.getStr("org_break_type");//是否有列使用了拆分 0- 有，1-没有
		
		
		
		//转换为可识别的合并方式
		List<CellRangeAddress> mergecellsRange = HandsontableMergeToPoiMerge(mergecells);
		//保存数据表内容，包括头以及数据列 row,col,rowto,colto
		
		//查询列配置信息
		List<Record> columns = Db.find("SELECT * FROM YYGL_MISSION_COLUMN_CONFIG T WHERE MODEL_CFG_ID=? ORDER BY TO_NUMBER(SUBSTR(T.COL_NAME,10))", id);
		StringBuffer sqlcol = new StringBuffer("*");
		String[] headers = new String [columns.size()]; //存储数据头部字段
		
		
		
		StringBuffer datawheresql = new StringBuffer(" WHERE MODEL_CFG_ID = ? ");
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(id);
		
		boolean isfir = true,isSplit = "0".equals(org_break_type);
		Record column = null;
		for (int j = 0,len = columns.size(); j < len ; j++) {
			column = columns.get(j);
			if(isfir){
				sqlcol.delete(0, sqlcol.length());
				isfir = false;
			}else{
				sqlcol.append(",");
			}
			
			String col_is_data =  model.getStr("COL_IS_DATA");//是否有列使用了数据抽取 0- 有，1-没有
			//如果有数据抽取情况，此时可以拼装sql的，具体如何拼装就要看数据了
			if("0".equals(col_is_data)){
				//这里可以写成配置化的，目前数据抽取先放放
				
			}
			
			sqlcol.append(column.getStr("col_name"));
			
			headers[j] = column.getStr("col_name");//主要是列名，用于导出使用 例如 DATA_C_1, DATA_C_2....
			
			if(isSplit){//存在拆分情况，检查列参数
				
				//如果当前列设置了拆分,目前只做了按照机构，角色，人员进行拆分，如果有其他场景可以自行扩展
				if("0".equals(column.get("col_is_break"))){
					
					//主要是考虑后期拆分场景多样化的话，可以放到单独配置表中
					MissionControlUtil.getIntanceof().getDataReportSplitSqlByUserTask(datawheresql, paramList, userTask, column);
					
				}
				
			}
		}
		datawheresql.append(" ORDER BY ROW_NUMBER ");
		
		StringBuffer datasql = new StringBuffer("SELECT "+sqlcol.toString()+" FROM YYGL_MISSION_ORIGINAL_DATA  "+datawheresql.toString());
		//查询数据内容内容
		List<Record> list = Db.find(datasql.toString(), paramList.toArray());
		
		String[] columns_title = new String[]{};
		String fileName = "";
		try {
		    fileName = new String(("任务模板"+System.currentTimeMillis()+".xls").getBytes("GB2312"),"ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		}
		ExcelRenderx er = new ExcelRenderx(fileName, columns_title, headers, list,getResponse());
		er.setMergecells(mergecellsRange);
		er.render();
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "任务配置".toString(), "7","任务配置-模板下载");
		log.info(getLogPoint().exit("任务配置-模板下载").toString());
		renderNull();
	}
	
	/**
	 * 任务下发-特殊逻辑
	 * @param taskid
	 */
	public void releaseMission(String taskid) {
		//查找任务配置获取任务接收人 
		//record{org_id,role_id,user_no}
		List<Record> userList =  MissionControlUtil.getIntanceof().getUserList(new Record().set("taskid", taskid));
		//向任务表中插入任务下发记录
	}
	
	/**
	 * 保存模板配置信息
	 * @param params
	 * @return 
	 */
	public Record saveModel(Record params){
		return params;
	}
	
	/**
	 * 模板配置页面
	 */
	public void addModel(){
		String id = getPara("id");
		if(AppUtils.StringUtil(id) == null){
			id = AppUtils.getStringSeq();
		}
		setAttr("id", id);
		setAttr("pid", getPara("pid"));
		//可根据任务类型打开任务模板页
		render("do_configdata.jsp");
	}
	
	/**
	 * 跳转到任务配置页面
	 */
	public void retoaction(){
		String id = getPara("id");
		setAttr("id", id);
		setAttr("missionIssueId", getPara("missionIssueId"));
		//可根据任务类型打开任务模板页
		//获取当前任务数据
		Record userTask = Db.findById("YYGL_MISSION_USER", id);
		setAttr("status", userTask.get("user_mission_status"));
		//允许导入
		setAttr("imptEnabled", "true");
		//判断任务状态，如果该任务状态为已经转发他人情况，并且所有的数据内容都已反馈，此时展示数据为该人员所有数据都查询出来
		if("0".equals(userTask.getStr("forward_flag"))){
			//检查当前子任务是否都已反馈
			BigDecimal bd = Db.queryBigDecimal("select count(1) from YYGL_MISSION_USER where UP_MISSION_ID = ? and USER_MISSION_STATUS in ('01','04') ", id);
			if(bd.intValue() > 0){
				setAttr("errcode", "-1"); //他人未反馈，强制不处理
				setAttr("errmsg", "当前子任务还有未反馈或退回情况!"); //他人未反馈，强制不处理
				renderJson();
				return ;
			}else{
				//不允许导入
				setAttr("imptEnabled", "false");
			}
		}
		
		//只有当前任务为 未反馈和 退回时才可以修改，其他情况下只能查看
		if("01".equals(userTask.getStr("user_mission_status")) || "04".equals(userTask.getStr("user_mission_status"))){
			setAttr("readonly", "false");
		}else{
			setAttr("readonly", "true");
		}
		
		//获取任务配置数据
		Record taskparam= Db.findFirst("SELECT id FROM YYGL_MISSION_CONFIG C WHERE C.ID  =(SELECT MISSION_CONFIG_ID FROM YYGL_MISSION_ISSUE I WHERE I.ID = ? )",userTask.get("MISSION_ISSUE_ID"));
		setAttr("taskid", taskparam.get("id"));		
		
		
		render("do_configdata_retoaction.jsp");
	}
	
	/**
	 * 跳转到列属性配置页面
	 */
	public void geColumnCfg(){
		render("do_configdata_setcolumn.jsp");
	}
	//跳转到预览页面
	public void preview(){
		render("do_configdata_preview.jsp");
	}
	/**
	 * 判断表中存储数据的列是否充足，不足时自动新建填充
	 * @param collen
	 * @return
	 */
	private String ectendColumn(int collen){
		//检查表字段长度是否足够
		BigDecimal maxIn =  Db.queryBigDecimal("select max(to_number(substr(t.column_name,10))) maxIn from user_col_comments t where t.table_name = 'YYGL_MISSION_ORIGINAL_DATA' and t.column_name like 'DATA_COL_%' ");
		
		//表字段最大扩至 500个字段，超出后需要授权修改（修改字段配置）
		//如果数据库字段不足，自动添加列字段
		if(maxIn.intValue() < collen){
			//在数据库中扩展字段
			Record param = Db.findFirst("select val from sys_param_info where key = 'mission_max_column' ");
			int maxcolumn = 500;
			if(param != null && AppUtils.StringUtil(param.getStr("val"))!=null){
				try {
					maxcolumn = Integer.valueOf(param.getStr("val"));
				} catch (NumberFormatException e) {
					//如果出错不处理，按照最大500的来
				}
			}
			if(collen > maxcolumn){
				return "-1";
			}
			for(int i = maxIn.intValue()+1;i<collen;i++){
				Db.update("alter table YYGL_MISSION_ORIGINAL_DATA add ( DATA_COL_"+i+" varchar2(500) )");
				Db.update("comment on column YYGL_MISSION_ORIGINAL_DATA.DATA_COL_"+i+" is '"+DateTimeUtil.getTime()+"自动添加字段,字段数量不足' ");
			}
			
			//扩展对应反馈表字段
			BigDecimal maxIn_t =  Db.queryBigDecimal("select max(to_number(substr(t.column_name,10))) maxIn from user_col_comments t where t.table_name = 'YYGL_MISSION_ORIGINAL_DATA' and t.column_name like 'DATA_COL_%' ");
			for(int i = maxIn_t.intValue()+1;i<collen;i++){
				Db.update("alter table YYGL_MISSION_UPLOAD_DATA add ( DATA_COL_"+i+" varchar2(500) )");
				Db.update("comment on column YYGL_MISSION_ORIGINAL_DATA.DATA_COL_"+i+" is '"+DateTimeUtil.getTime()+"自动添加字段,字段数量不足' ");
			}
		}
		return "1";
	}
	/**
	 * 执行保存动作
	 * */
	@Before(Tx.class)
	public void save(){
		String taskid = getPara("taskid");
		String id = getPara("model_cfg_id");
		String sourcedata = getPara("sourcedata");
		String columnscfg = getPara("columnscfg");
		Integer startrow = getParaToInt("data_start_row");
		String mergecells = getPara("mergecells");
		Integer collen = getParaToInt("collen");
		
		String flag = ectendColumn(collen);
		if(!"1".equals(flag)){
			renderText(flag); // 表字段最大扩至 500个字段，超出后需要授权修改（修改字典[mission_max_column]的配置）;
			return;
		}
		
		//保存数据表内容，包括头以及数据列
		JSONArray data_array = JSON.parseArray(sourcedata);
		Record data = new Record();
		String data_type;
		for (int i = 0,len = data_array.size(); i < len ; i++) {
			JSONArray item_array = data_array.getJSONArray(i);
			data = new Record();
			//data.clear();
			data_type =  i < startrow  ? "0" : "1" ;
			data.set("id",AppUtils.getStringSeq())
			.set("model_cfg_id", id) //模板ID
			.set("data_type", data_type) //列字段名称
			.set("row_number", i); //行号
			for (int j = 0,jlen = item_array.size(); j < jlen ; j++) {
				data.set("data_col_"+(j+1), item_array.getString(j));
			}
			Db.save("YYGL_MISSION_ORIGINAL_DATA", data);
		}
		int org_break_type = 1;
		
		//保存列配置
		JSONArray col_array = JSON.parseArray(columnscfg);
		Record column = new Record();
		String [] readCol = "col_type,col_binddata,col_data_upcol,col_v_isnull,col_is_edit,col_valid,col_v_type_com,col_is_length,col_min_length,col_max_length,col_is_break,col_break_type,col_is_data,col_data_sou,col_is_keyword,col_is_summary,col_formart_type".split(regex);
		for (int i = 0,len = col_array.size(); i < len ; i++) {
			JSONObject item = col_array.getJSONObject(i);
			column = new Record();
			column.set("id",AppUtils.getStringSeq())
			.set("model_cfg_id", id)		//模板ID
			.set("col_data_type", "0")		//数据源配置方式  0-字典1-sql2-文本
			.set("col_v_isnull", "0")		//允许该列为空 0-允许，1-不允许
			.set("col_name", "DATA_COL_"+(i+1)) //列字段名称
			.set("col_type", "text") 		//列类型 
			.set("col_valid", "1")			//允许列验证格式 0-允许，1-不允许
			.set("col_v_type", "0")			//数据验证方式 0-字典
			.set("col_is_length", "1")		//允许字段长度限制 0-允许，1-不允许
			.set("col_is_break", "1")		//该列作为拆分列0-允许，1-不允许
			.set("col_is_data", "1")		//该列作为数据抽取列 0-允许，1-不允许
			.set("col_is_keyword", "1")		//是否关键字列 0-允许，1-不允许
			.set("col_is_summary", "1")		//是否数据汇总列（该列进行计算）0-允许，1-不允许
			.set("col_is_edit", "0");		//是否可编辑0-允许，1-不允许
			//判断是否为空对象
			if(!item.isEmpty() && item.keySet().size() > 0){
				for (int j = 0; j < readCol.length; j++) {
					column.set(readCol[j],item.get(readCol[j]));
				}
			}
			//判断是否存在拆分情况
			if("0".equals(column.get("col_is_break"))){
				org_break_type  = 0;
			}
			
			Db.save("YYGL_MISSION_COLUMN_CONFIG", column);
		}
		//保存数据配置
		Record model = new Record();
		model
		.set("id", id)
		.set("common_config_id", taskid)	//任务配置id
		.set("colheaders", "")				//ColHeaders 配置存放
		.set("mergecells", mergecells)		//MergeCells 配置存放
		.set("data_start_row", startrow) 	//数据开始行
		.set("org_break_type", org_break_type) //是否是否使用拆分 0-是，1-否
		.set("data_range_ymd_start", "")	//下次任务数据取得开始日
		.set("data_range_ymd_end", "")		//下次任务数据取得结束日
		.set("data_collen", collen);		//列字段数量
		Db.save("YYGL_MISSION_MODEL_CONFIG", model);
		
		renderText(flag);
	}
	
	/**
	 *	更新模板内容 
	 */
	public void update(){
		String taskid = getPara("taskid");
		String id = getPara("model_cfg_id");
		String sourcedata = getPara("sourcedata");
		String columnscfg = getPara("columnscfg");
		Integer startrow = getParaToInt("data_start_row");
		String mergecells = getPara("mergecells");
		Integer collen = getParaToInt("collen");
		
		String flag = ectendColumn(collen);
		if(!"1".equals(flag)){
			renderText(flag); // 表字段最大扩至 500个字段，超出后需要授权修改（修改字典[mission_max_column]的配置）;
			return;
		}
		
		//保存数据表内容，包括头以及数据列
		Db.deleteById("YYGL_MISSION_ORIGINAL_DATA","model_cfg_id", id);
		JSONArray data_array = JSON.parseArray(sourcedata);
		Record data = new Record();
		for (int i = 0,len = data_array.size(); i < len ; i++) {
			JSONArray item_array = data_array.getJSONArray(i);
			data = new Record();
			//data.clear();
			data.set("id",AppUtils.getStringSeq())
			.set("model_cfg_id", id) //模板ID
			.set("data_type", ( i < startrow  ? "0" : "1" )) //列字段名称
			.set("row_number", i); //行号
			for (int j = 0,jlen = item_array.size(); j < jlen ; j++) {
				data.set("data_col_"+(j+1), item_array.getString(j));
			}
			Db.save("YYGL_MISSION_ORIGINAL_DATA", data);
		}
		int org_break_type = 1;
		
		//保存列配置
		Db.deleteById("YYGL_MISSION_COLUMN_CONFIG","model_cfg_id", id);
		
		JSONArray col_array = JSON.parseArray(columnscfg);
		Record column = new Record();
		String [] readCol = "id,col_type,col_binddata,col_data_upcol,col_v_isnull,col_is_edit,col_valid,col_v_type_com,col_is_length,col_min_length,col_max_length,col_is_break,col_break_type,col_is_data,col_data_sou,col_is_keyword,col_is_summary,col_formart_type".split(regex);
		for (int i = 0,len = col_array.size(); i < len ; i++) {
			JSONObject item = col_array.getJSONObject(i);
			column = new Record();
			column.set("id",AppUtils.getStringSeq())
			.set("model_cfg_id", id)		//模板ID
			.set("col_data_type", "0")		//数据源配置方式  0-字典1-sql2-文本
			.set("col_v_isnull", "0")		//允许该列为空 0-允许，1-不允许
			.set("col_name", "DATA_COL_"+(i+1)) //列字段名称
			.set("col_type", "text") 		//列类型 
			.set("col_valid", "1")			//允许列验证格式 0-允许，1-不允许
			.set("col_v_type", "0")			//数据验证方式 0-字典
			.set("col_is_length", "1")		//允许字段长度限制 0-允许，1-不允许
			.set("col_is_break", "1")		//该列作为拆分列0-允许，1-不允许
			.set("col_is_data", "1")		//该列作为数据抽取列 0-允许，1-不允许
			.set("col_is_keyword", "1")		//是否关键字列 0-允许，1-不允许
			.set("col_is_summary", "1")		//是否数据汇总列（该列进行计算）0-允许，1-不允许
			.set("col_is_edit", "0");		//是否可编辑0-允许，1-不允许
			//判断是否为空对象
			if(!item.isEmpty() && item.keySet().size() > 0){
				for (int j = 0; j < readCol.length; j++) {
					column.set(readCol[j],item.get(readCol[j]));
				}
			}
			//判断是否存在拆分情况
			if("0".equals(column.get("col_is_break"))){
				org_break_type  = 0;
			}
			
			Db.save("YYGL_MISSION_COLUMN_CONFIG", column);
		}
		//保存数据配置
		Record model = new Record();
		model
		.set("id", id)
		.set("common_config_id", taskid)	//任务配置id
		.set("colheaders", "")				//ColHeaders 配置存放
		.set("mergecells", mergecells)		//MergeCells 配置存放
		.set("data_start_row", startrow) 	//数据开始行
		.set("org_break_type", org_break_type) //是否是否使用拆分 0-是，1-否
		.set("data_range_ymd_start", "")	//下次任务数据取得开始日
		.set("data_range_ymd_end", "")		//下次任务数据取得结束日
		.set("data_collen", collen);		//列字段数量
		Db.update("YYGL_MISSION_MODEL_CONFIG","id", model);
		
		renderText(flag);
	}
	/**
	 * 获取模板配置信息
	 */
	public void getModelConfigInfo(){
		String id = getPara("model_cfg_id");
		//查询配置信息
		Record model = Db.findById("YYGL_MISSION_MODEL_CONFIG", id);

		//查询列配置信息
		List<Record> columns = Db.find("SELECT * FROM YYGL_MISSION_COLUMN_CONFIG T WHERE MODEL_CFG_ID=? ORDER BY TO_NUMBER(SUBSTR(T.COL_NAME,10))", id);
		StringBuffer sqlcol = new StringBuffer("*");
		boolean isfir = true;
		for (int j = 0,len = columns.size(); j < len ; j++) {
			if(isfir){
				sqlcol.delete(0, sqlcol.length());
				isfir = false;
			}else{
				sqlcol.append(",");
			}
			sqlcol.append(columns.get(j).getStr("col_name"));
		}
		//查询数据内容
		List<Record> datas = Db.find("SELECT "+sqlcol.toString()+" FROM YYGL_MISSION_ORIGINAL_DATA WHERE MODEL_CFG_ID = ? ORDER BY ROW_NUMBER", id);
	
		setAttr("model", model);
		setAttr("columns", columns);
		setAttr("datas", datas);
		renderJson();
	}
	
	

	public void downloadPreview(){
		
	}
	/**
	 * 复制模板配置到新的id中去
	 *  copyflag,model_cfg_id,id,mission_flag
	 * @param param
	 * @return
	 */
	@Before(Tx.class)
	public Record copyModelConfig(Record param) {
		String model_cfg_id = param.getStr("model_cfg_id");
		String new_model_cfg_id = param.getStr("new_model_cfg_id");
		String newId = param.getStr("id");
		Record result = new Record();
		
		//检查当前模板是否已经打开保存过模板信息。如果没有保存过，直接copy，保存过就不处理
		
		//如果保存过数据，那么新的 配置id 肯定已经绑定了新的模板配置id,去查询下
		Record modelR =  Db.findFirst("SELECT * FROM  YYGL_MISSION_MODEL_CONFIG WHERE COMMON_CONFIG_ID = ?",newId);
		if(modelR != null){
			result.set("flag", "1");
		}else{
			Record temp = null;
			//保存数据表内容，包括头以及数据列
			List<Record> dataList =  Db.find("SELECT * FROM  YYGL_MISSION_ORIGINAL_DATA WHERE MODEL_CFG_ID = ?",model_cfg_id);
			for (int i = 0,len = dataList.size(); i < len ; i++) {
				temp = dataList.get(i);
				temp.set("id", AppUtils.getStringSeq())
				.set("MODEL_CFG_ID", new_model_cfg_id);
				Db.save("YYGL_MISSION_ORIGINAL_DATA",temp);
			}
			
			//保存列配置内容
			List<Record> columnList =  Db.find("SELECT * FROM  YYGL_MISSION_COLUMN_CONFIG WHERE MODEL_CFG_ID = ?",model_cfg_id);
			for (int i = 0,len = columnList.size(); i < len ; i++) {
				temp = columnList.get(i);
				temp.set("id", AppUtils.getStringSeq())
				.set("MODEL_CFG_ID", new_model_cfg_id);
				Db.save("YYGL_MISSION_COLUMN_CONFIG",temp);
			}
			
			//保存数据表内容，包括头以及数据列
			Record model =  Db.findFirst("SELECT * FROM  YYGL_MISSION_MODEL_CONFIG WHERE ID = ?",model_cfg_id);
			model.set("id",new_model_cfg_id).set("COMMON_CONFIG_ID", newId);
			Db.save("YYGL_MISSION_MODEL_CONFIG", model);
			
			result.set("flag", "1");
		}
		return result;
	}
	/**
	 * 查询反馈页面数据
	 */
	public void getRetoactionInfo(){
		
		//查询配置信息
		
		//下载数据模板,暂时不考虑抽取数据情况
		String userTaskid = getPara("id"); // mission_user id
		
		
		
		//获取当前任务数据
		Record userTask = Db.findById("YYGL_MISSION_USER", userTaskid);
		
		
		
		
		
		//获取任务配置数据
		Record taskparam= Db.findFirst("SELECT * FROM YYGL_MISSION_CONFIG C WHERE C.ID  =(SELECT MISSION_CONFIG_ID FROM YYGL_MISSION_ISSUE I WHERE I.ID = ? )",userTask.get("MISSION_ISSUE_ID"));
		
		
		//判断任务状态，如果该任务状态为已经转发他人情况，并且所有的数据内容都已反馈，此时展示数据为该人员所有数据都查询出来
		List<Object> paramList_user = new ArrayList<Object>();
		StringBuffer childSql = new StringBuffer();
		if("0".equals(userTask.getStr("FORWARD_FLAG"))){
			/*//检查当前子任务是否都已反馈
			BigDecimal bd = Db.queryBigDecimal("select count(1) from YYGL_MISSION_USER where UP_MISSION_ID = ? and USER_MISSION_STATUS in ('01','03') ",userTaskid);
			if(bd.intValue() > 0){
				setAttr("errcode", "-1"); //他人未反馈，强制不处理
				setAttr("taskremark", taskparam.get("mission_require"));
				renderJson();
				return ;
			}*/
			
			//AND USER_MISSION_STATUS = '02'
			childSql.append("  AND USER_MISSION_ID in (   ");
			childSql.append(" select id from YYGL_MISSION_USER m where id =?   UNION ");
			childSql.append(" select id from YYGL_MISSION_USER m where  USER_MISSION_STATUS = '02'  start with m.id in ");
			childSql.append(" (SELECT ID FROM  YYGL_MISSION_USER  WHERE UP_MISSION_ID = ? AND USER_MISSION_STATUS = '02') ");
			childSql.append(" connect by m.UP_MISSION_ID=prior m.id ) ");
			paramList_user.add(userTaskid);
			paramList_user.add(userTaskid);
		}else{
			childSql.append(" AND USER_MISSION_ID = ? ");
			paramList_user.add(userTaskid);
		}
		
		
		
		String id = taskparam.getStr("model_cfg_id");
		//查询配置信息
		Record model = Db.findById("YYGL_MISSION_MODEL_CONFIG", id);
		
		String org_break_type =  model.getStr("org_break_type");//是否有列使用了拆分 0- 有，1-没有
		
		
		//查询列配置信息
		List<Record> columns = Db.find("SELECT * FROM YYGL_MISSION_COLUMN_CONFIG T WHERE MODEL_CFG_ID=? ORDER BY TO_NUMBER(SUBSTR(T.COL_NAME,10))", id);
		
		
		
		
		
		StringBuffer sqlcol = new StringBuffer("*");
		String[] headers = new String [columns.size()]; //存储数据头部字段
		
		StringBuffer datawheresql = new StringBuffer(" WHERE MODEL_CFG_ID = ? ");
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(id);
		
		boolean isfir = true,isSplit = "0".equals(org_break_type);
		Record column = null;
		for (int j = 0,len = columns.size(); j < len ; j++) {
			column = columns.get(j);
			if(isfir){
				sqlcol.delete(0, sqlcol.length());
				isfir = false;
			}else{
				sqlcol.append(",");
			}
			
			String col_is_data =  model.getStr("COL_IS_DATA");//是否有列使用了数据抽取 0- 有，1-没有
			//如果有数据抽取情况，此时可以拼装sql的，具体如何拼装就要看数据了
			if("0".equals(col_is_data)){
				//这里可以写成配置化的，目前数据抽取先放放
				
			}
			
			sqlcol.append(column.getStr("col_name"));
			
			headers[j] = column.getStr("col_name");//主要是列名，用于导出使用 例如 DATA_C_1, DATA_C_2....
			
			if(isSplit){//存在拆分情况，检查列参数
				
				//如果当前列设置了拆分,目前只做了按照机构，角色，人员进行拆分，如果有其他场景可以自行扩展
				if("0".equals(column.get("col_is_break"))){
					
					//主要是考虑后期拆分场景多样化的话，可以放到单独配置表中
					MissionControlUtil.getIntanceof().getDataReportSplitSqlByUserTask(datawheresql, paramList, userTask, column);
					
				}
				
			}
		}
		datawheresql.append(" ORDER BY ROW_NUMBER ");
		
		//StringBuffer datasql = new StringBuffer("SELECT ID DATA_CID_0,"+sqlcol.toString()+" FROM YYGL_MISSION_ORIGINAL_DATA  "+datawheresql.toString());
		
		//先检查任务反馈表中是否存放有数据如果无数据的话，采用抽取的模板数据，如果有数据，采用已存放的数据
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex");
		int pageSize = getParaToInt("pageSize", 10);
		
		Page<Record> pageList = Db.paginate(pageNum, pageSize,"SELECT ID DATA_CID_0,"+sqlcol.toString()+" ","FROM YYGL_MISSION_UPLOAD_DATA WHERE 1=1 "+childSql+" ORDER BY ROW_NUMBER ",paramList_user.toArray());
		List<Record> datas = new ArrayList<Record>();
		if(pageList.getList() != null && pageList.getList().size()  == 0){
			//查询数据内容内容
			//datas = Db.find(datasql.toString(), paramList.toArray());
			 pageList = Db.paginate(pageNum, pageSize,"SELECT ID DATA_CID_0,"+sqlcol.toString()," FROM YYGL_MISSION_ORIGINAL_DATA  "+datawheresql.toString(),paramList.toArray());
			 datas = pageList.getList();
			 
		}else{
			datas = Db.find("SELECT ID DATA_CID_0,"+sqlcol.toString()+" FROM YYGL_MISSION_ORIGINAL_DATA  WHERE DATA_TYPE = '0' AND MODEL_CFG_ID = ? ORDER BY ROW_NUMBER ",id);
			List<Record> temp = pageList.getList();
			for (int i = 0,len = temp.size(); i < len; i++) {
				datas.add(temp.get(i));
			}
		}
		setAttr("model", model);
		setAttr("columns", columns);
		setAttr("datas", datas);
		setAttr("total", pageList.getTotalRow());
		setAttr("taskremark", taskparam.get("mission_require"));
		renderJson();
	}
	
	
	
	
	public void validSave(){
		String id = getPara("id");
		String missionIssueId = getPara("missionIssueId");
		String sourcedata = getPara("sourcedata");
		String exportFlag = getPara("exportFlag");
		String delIds = getPara("delIds");
		
		//获取任务配置数据
		Record taskparam= Db.findFirst("SELECT * FROM YYGL_MISSION_CONFIG C WHERE C.ID  =(SELECT MISSION_CONFIG_ID FROM YYGL_MISSION_ISSUE I WHERE I.ID = ? )",missionIssueId);
		
		//查询列配置信息
		List<Record> columns = Db.find("SELECT * FROM YYGL_MISSION_COLUMN_CONFIG T WHERE MODEL_CFG_ID=? ORDER BY TO_NUMBER(SUBSTR(T.COL_NAME,10))", taskparam.getStr("model_cfg_id"));
		//针对数据内容校验数据格式是否正确
		//转换为 MAP
		Map<String,Record> colMap = new HashMap<String, Record>();
		for (Record record : columns) {
			colMap.put(record.getStr("col_name"),record);
		}
		//记录错误日志信息
		
		StringBuffer msg = new StringBuffer();
		
		
		List<Record> saveDatas = new ArrayList<Record>();
		
		List<Record> updateDatas = new ArrayList<Record>();
		
		//保存数据表内容，包括头以及数据列
		JSONArray data_array = JSON.parseArray(sourcedata);
		Record data = new Record();
		Record validR  = null;
		boolean isUpd = false;
		for (int i = 0,len = data_array.size(); i < len ; i++) {
			JSONObject item_obj = data_array.getJSONObject(i);
			Set<String> keys =  item_obj.keySet();
			data = new Record();
			isUpd = false;
			
			for (String key : keys) {
				
				if("DATA_CID_0".equalsIgnoreCase(key)){
					if( AppUtils.StringUtil( item_obj.getString(key) ) == null){
						data
						.set("id",AppUtils.getStringSeq())
						.set("user_mission_id", id)
						.set("data_type", "1") 
						.set("row_number", i); //行号 //模板ID
					}else{
						data.set("id", item_obj.getString(key));
						isUpd = true;
					}
				}else{
					
					data.set(key, item_obj.getString(key));
					//检查该列是否合规
					validR = colMap.get(key.toUpperCase());
					
					if("1".equals(validR.getStr("col_is_edit")) || "0".equals(validR.get("col_is_keyword"))){
						continue;
					}
					
					if("1".equals(validR.get("COL_V_ISNULL"))){
						if(AppUtils.StringUtil( item_obj.getString(key) ) == null){
							msg.append("第["+(i+1)+"]行["+key+"]列,的值不允许为空！<br/>");
						}
					}else {
						
						if("0".equals(validR.get("COL_IS_LENGTH"))){
							if( !( Integer.parseInt(validR.getStr("COL_MIN_LENGTH")) <= item_obj.getString(key).length()  &&   item_obj.getString(key).length() <= Integer.parseInt(validR.getStr("COL_MAX_LENGTH")) )){
								msg.append("第["+(i+1)+"]行["+key+"]列,的值字段长度["+item_obj.getString(key).length()+"],该字段长度必须在"+validR.getStr("COL_MIN_LENGTH")+"和"+validR.getStr("COL_MAX_LENGTH")+"长度范围内！<br/>");
							}
						}
						
					}
					
					
					/*if("date".equals(validR.get("COL_TYPE"))){
						
					}
					if("numeric".equals(validR.get("COL_TYPE"))){
						
					}
					if("dropdown".equals(validR.get("COL_TYPE"))){
						
					}*/
				}
			}
			//该数据未传递视为新建
			if(!item_obj.containsKey("data_cid_0")){
				data
				.set("id",AppUtils.getStringSeq())
				.set("user_mission_id", id)
				.set("data_type", "1") 
				.set("row_number", i); //行号 //模板ID
			}
			
			if(isUpd){
				updateDatas.add(data);
			}else{
				saveDatas.add(data);
			}
			/*data = new Record();
			isUpd = false;
			for (int j = 0,jlen = item_array.size(); j < jlen ; j++) {
				if(j == 0 ){
					if( AppUtils.StringUtil( item_array.getString(j) ) == null){
						data
						.set("id",AppUtils.getStringSeq())
						.set("user_mission_id", id)
						.set("data_type", "1") 
						.set("row_number", i); //行号 //模板ID
					}else{
						data.set("id",item_array.getString(j));
						isUpd = true;
					}
				}else{
					
					
					
				}
			}*/
			
		}
		String flag = "1";
		if(msg.length() == 0){
			
			if("1".equals(exportFlag)){ //导入时，删除原有的，保存现有的
				Db.deleteById("YYGL_MISSION_UPLOAD_DATA", "user_mission_id", id);
			}
			
			if(AppUtils.StringUtil(delIds)!=null){
				String [] ids =  delIds.split(regex);
				if(ids.length > 0 )
					Db.update("delete from YYGL_MISSION_UPLOAD_DATA where id in "+getParasToStringRegex(ids),ids);
			}
			
			//检查数据量
			if(saveDatas.size() > 50){
				//复制字段
				String [] names =  saveDatas.get(0).getColumnNames();
				StringBuffer column = new StringBuffer();
				StringBuffer placeholder = new StringBuffer();
				boolean isFir = true;
				for (int j = 0; j < names.length; j++) {
					if(isFir){
						isFir = false;
					}else{
						column.append(",");
						placeholder.append(",");
					}
					column.append(names[j]);
					placeholder.append("?");
				}
				
				Db.batch("insert into YYGL_MISSION_UPLOAD_DATA ("+column.toString()+") values ("+placeholder.toString()+")",column.toString(), saveDatas, 100);
			
			}else{
				for (Record r : saveDatas) {
					Db.save("YYGL_MISSION_UPLOAD_DATA", r);
				}
			}
			
			for (Record r : updateDatas) {
				Db.update("YYGL_MISSION_UPLOAD_DATA","id", r);
			}
			
		}else{
			flag = "-1";
		}
		
		setAttr("flag", flag);
		setAttr("msg",msg.toString());
		renderJson();
	}
	/**
	 * 提交反馈
	 */
	public void retoactionTask(){
		String id = getPara("id");
		//String sflag = getPara("flag");//提交标识，如果该标识存在将会保存数据后提交
		String mission_remark = getPara("mission_remark"); //退回说明
		Record user = getCurrentUser();//获取当前登录人的用户信息
		String userno = user.getStr("USER_NO");
		String sql = "";
		//向反馈往来记录表中插入一条数据
		sql = "insert into yygl_mission_dealings(id,user_mission_id,action_type,action_user_no,action_date,action_remark) values('"+AppUtils.getStringSeq()+"',"
				+ "'"+id+"','0','"+userno+"','"+DateTimeUtil.getTime()+"','"+mission_remark+"')";
		Db.update(sql);
		Db.update(" UPDATE  YYGL_MISSION_USER set USER_MISSION_STATUS= '02' WHERE ID = ? ",id);
		setAttr("flag", "1");
		setAttr("msg","");
		renderJson();;
		
	}
	
	public void downloadRetoaction(){
			//下载数据模板,暂时不考虑抽取数据情况
			String userTaskid = getPara("id"); // mission_user id
			
			//获取当前任务数据
			Record userTask = Db.findById("YYGL_MISSION_USER", userTaskid);
			
			//获取任务配置数据
			Record taskparam= Db.findFirst("SELECT * FROM YYGL_MISSION_CONFIG C WHERE C.ID  =(SELECT MISSION_CONFIG_ID FROM YYGL_MISSION_ISSUE I WHERE I.ID = ? )",userTask.get("MISSION_ISSUE_ID"));
			
			
			//判断任务状态，如果该任务状态为已经转发他人情况，并且所有的数据内容都已反馈，此时展示数据为该人员所有数据都查询出来
			
			StringBuffer childSql = new StringBuffer();
			
		
			
			
			String id = taskparam.getStr("model_cfg_id");
			//查询配置信息
			Record model = Db.findById("YYGL_MISSION_MODEL_CONFIG", id);
			
			String org_break_type =  model.getStr("org_break_type");//是否有列使用了拆分 0- 有，1-没有
			
			
			//查询列配置信息
			List<Record> columns = Db.find("SELECT * FROM YYGL_MISSION_COLUMN_CONFIG T WHERE MODEL_CFG_ID=? ORDER BY TO_NUMBER(SUBSTR(T.COL_NAME,10))", id);
			
			StringBuffer sqlcol = new StringBuffer("*");
			String[] headers = new String [columns.size()]; //存储数据头部字段
			
			StringBuffer datawheresql = new StringBuffer(" WHERE MODEL_CFG_ID = ? ");
			List<Object> paramList = new ArrayList<Object>();
			paramList.add(id);
			List<Object> paramList_user = new ArrayList<Object>();
			
			if("0".equals(userTask.getStr("FORWARD_FLAG"))){
				//检查当前子任务是否都已反馈
				BigDecimal bd = Db.queryBigDecimal("select count(1) from YYGL_MISSION_USER where UP_MISSION_ID = ? and USER_MISSION_STATUS in ('01','04') ",userTaskid);
				if(bd.intValue() > 0){
					setAttr("errcode", "-1"); //他人未反馈，强制不处理
					setAttr("taskremark", taskparam.get("mission_require"));
					renderJson();
					return ;
				}
				
				childSql.append("  AND USER_MISSION_ID in (   ");
				childSql.append(" select id from YYGL_MISSION_USER m where id =? AND USER_MISSION_STATUS = '02'  UNION ");
				childSql.append(" select id from YYGL_MISSION_USER m where  USER_MISSION_STATUS = '02'  start with m.id in ");
				childSql.append(" (SELECT ID FROM  YYGL_MISSION_USER  WHERE UP_MISSION_ID = ? AND USER_MISSION_STATUS = '02') ");
				childSql.append(" connect by m.UP_MISSION_ID=prior m.id ) ");
				paramList_user.add(userTaskid);
				paramList_user.add(userTaskid);
			}else{
				childSql.append(" AND USER_MISSION_ID = ? ");
				paramList_user.add(userTaskid);
			}
			
			
			
			
			
			
			
			boolean isfir = true,isSplit = "0".equals(org_break_type);
			Record column = null;
			for (int j = 0,len = columns.size(); j < len ; j++) {
				column = columns.get(j);
				if(isfir){
					sqlcol.delete(0, sqlcol.length());
					isfir = false;
				}else{
					sqlcol.append(",");
				}
				
				String col_is_data =  model.getStr("COL_IS_DATA");//是否有列使用了数据抽取 0- 有，1-没有
				//如果有数据抽取情况，此时可以拼装sql的，具体如何拼装就要看数据了
				if("0".equals(col_is_data)){
					//这里可以写成配置化的，目前数据抽取先放放
					
				}
				
				sqlcol.append(column.getStr("col_name"));
				
				headers[j] = column.getStr("col_name");//主要是列名，用于导出使用 例如 DATA_C_1, DATA_C_2....
				
				if(isSplit){//存在拆分情况，检查列参数
					
					//如果当前列设置了拆分,目前只做了按照机构，角色，人员进行拆分，如果有其他场景可以自行扩展
					if("0".equals(column.get("col_is_break"))){
						
						//主要是考虑后期拆分场景多样化的话，可以放到单独配置表中
						MissionControlUtil.getIntanceof().getDataReportSplitSqlByUserTask(datawheresql, paramList, userTask, column);
						
					}
					
				}
			}
			datawheresql.append(" ORDER BY ROW_NUMBER ");
			
			//StringBuffer datasql = new StringBuffer("SELECT ID DATA_CID_0,"+sqlcol.toString()+" FROM YYGL_MISSION_ORIGINAL_DATA  "+datawheresql.toString());
			
			//先检查任务反馈表中是否存放有数据如果无数据的话，采用抽取的模板数据，如果有数据，采用已存放的数据
			// 获取查询参数
			
			List<Record> list = Db.find("SELECT ID DATA_CID_0,"+sqlcol.toString()+" FROM YYGL_MISSION_UPLOAD_DATA WHERE 1=1 "+childSql+" ORDER BY ROW_NUMBER ",paramList_user.toArray());
			if(list != null && list.size()  == 0){
				//查询数据内容内容
				list = Db.find("SELECT ID DATA_CID_0,"+sqlcol.toString()+" FROM YYGL_MISSION_ORIGINAL_DATA  "+datawheresql.toString(),paramList.toArray());
				 
			}else{
				List<Record>  datas = Db.find("SELECT ID DATA_CID_0,"+sqlcol.toString()+" FROM YYGL_MISSION_ORIGINAL_DATA  WHERE DATA_TYPE = '0' AND MODEL_CFG_ID = ? ORDER BY ROW_NUMBER ",id);
				datas.addAll(list);
				list = datas;
			}
		

			//主要应用于 待处理和已处理阶段。一般来讲会传入 任务 id ,不会传入配置id,所以需要对id进行查询
			
			//下载的模板依据于最新的模板配置参数进行
			
			String mergecells = model.getStr("mergecells");
			//转换为可识别的合并方式
			List<CellRangeAddress> mergecellsRange = HandsontableMergeToPoiMerge(mergecells);
			//保存数据表内容，包括头以及数据列 row,col,rowto,colto
			
			String[] columns_title = new String[]{};
			String fileName = "";
			try {
			    fileName = new String(("填报内容"+System.currentTimeMillis()+".xls").getBytes("GB2312"),"ISO_8859_1");
			} catch (UnsupportedEncodingException e) {
			    e.printStackTrace();
			}
			ExcelRenderx er = new ExcelRenderx(fileName, columns_title, headers, list,getResponse());
			er.setMergecells(mergecellsRange);
			er.render();
			// 记录操作日志
			LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "待处理任务".toString(), "7","待处理任务-填报内容下载");
			log.info(getLogPoint().exit("待处理任务-填报内容下载").toString());
			renderNull();
	}
	
	/**
	 * 查看汇总
	 */
	public void summaryView(){
		setAttr("id", getPara("id"));
		setAttr("type", getPara("type"));
		render("do_configdata_summary.jsp");
	}
	
	/**
	 * 获取汇总数据
	 */
	public void getSummaryData(){
		String id = getPara("id");
		String type = getPara("type"); // user_table -  用户任务查看 ，issue_table - 发起任务查看
		String mission_issue_id = ""; 
		String model_cfg_id = ""; 
		 
			
		//获取当前任务数据
		Record userTask = null;
		
		if("user_table".equals(type)){
			userTask = Db.findById("YYGL_MISSION_USER", id);
			mission_issue_id = 	userTask.get("MISSION_ISSUE_ID");
		}else{
			mission_issue_id = 	id;
		}
		
		//获取任务配置数据
		Record taskparam= Db.findFirst("SELECT * FROM YYGL_MISSION_CONFIG C WHERE C.ID  =(SELECT MISSION_CONFIG_ID FROM YYGL_MISSION_ISSUE I WHERE I.ID = ? )",mission_issue_id);
		
		model_cfg_id = taskparam.getStr("model_cfg_id");
		
		//查询配置信息
		Record model = Db.findById("YYGL_MISSION_MODEL_CONFIG", model_cfg_id);
		
		//查询列配置信息
		List<Record> columns = Db.find("SELECT * FROM YYGL_MISSION_COLUMN_CONFIG T WHERE MODEL_CFG_ID=? ORDER BY TO_NUMBER(SUBSTR(T.COL_NAME,10))",model_cfg_id);
		
		StringBuffer selectSql = new StringBuffer(" SELECT ");
		
		StringBuffer datawheresql = new StringBuffer(" FROM YYGL_MISSION_UPLOAD_DATA WHERE 1=1 ");
		
		StringBuffer groupSql = new StringBuffer();
		
		List<Object> paramList = new ArrayList<Object>();
		
		StringBuffer sqlcolHead = new StringBuffer();
		
		StringBuffer sqlcol = new StringBuffer();
		
		//判断任务状态，如果该任务状态为已经转发他人情况，并且所有的数据内容都已反馈，此时展示数据为该人员所有数据都查询出来
		
		if( "issue_table".equals(type) ){
			datawheresql.append("  AND USER_MISSION_ID in (   ");
			datawheresql.append(" select id from YYGL_MISSION_USER m where  USER_MISSION_STATUS = '02'  start with m.id in ");
			datawheresql.append(" (SELECT ID FROM  YYGL_MISSION_USER  WHERE mission_issue_id = ? AND UP_MISSION_ID is null AND USER_MISSION_STATUS = '02') ");
			datawheresql.append(" connect by m.UP_MISSION_ID=prior m.id ) ");
			paramList.add(mission_issue_id);
		}else{
			if( userTask !=null && "0".equals(userTask.getStr("FORWARD_FLAG"))  ){
				datawheresql.append("  AND USER_MISSION_ID in (   ");
				datawheresql.append(" select id from YYGL_MISSION_USER m where id =? AND USER_MISSION_STATUS = '02'  UNION ");
				datawheresql.append(" select id from YYGL_MISSION_USER m where  USER_MISSION_STATUS = '02'  start with m.id in ");
				datawheresql.append(" (SELECT ID FROM  YYGL_MISSION_USER  WHERE UP_MISSION_ID = ? AND USER_MISSION_STATUS = '02') ");
				datawheresql.append(" connect by m.UP_MISSION_ID=prior m.id ) ");
				paramList.add(id);
				paramList.add(id);
			}else{
				datawheresql.append("  AND USER_MISSION_ID  in ( SELECT ID FROM  YYGL_MISSION_USER WHERE USER_MISSION_ID = ? AND USER_MISSION_STATUS = '02' )  ");
				paramList.add(id);
			}
		} 
		
		boolean isfir = true;
		Record column = null;
		
		//是否存在关键字列，此处可以放在模板配置中处理
		boolean isGroup = false,isGroupFir = true;
		for (int j = 0,len = columns.size(); j < len ; j++) {
			//是否关键字列
			if(	"0".equals(columns.get(j).get("COL_IS_KEYWORD")) ){
				isGroup = true;
				groupSql.append("GROUP BY ");
				break;
			}
		}
		
		
		
		//组装sql
		for (int j = 0,len = columns.size(); j < len ; j++) {
			column = columns.get(j);
			if(isfir){
				isfir = false;
			}else{
				sqlcol.append(",");
				sqlcolHead.append(",");
			}
			
			//是否关键字列
			if(isGroup){
				if("0".equals(column.get("COL_IS_KEYWORD")) ){
					sqlcol.append(column.getStr("col_name"));
					if(isGroupFir){
						isGroupFir = false;
					}else{
						groupSql.append(",");
					}
					groupSql.append(column.getStr("col_name"));
				}else{ 
					
					
					//关键不可作为汇总部分,汇总如果不指定聚合方式时，按照拼接方式来做以，号分隔方式
					if("0".equals(column.get("COL_IS_SUMMARY")) ){//指定汇总统计方式
						sqlcol.append( column.getStr("COL_FORMART_TYPE")+"("+column.getStr("col_name")+") "+  column.getStr("col_name"));
					}else{
						sqlcol.append( "wm_concat("+column.getStr("col_name")+") "+  column.getStr("col_name"));
					}
				}
			}else{
				//按照明细方式查询sql
				sqlcol.append(column.getStr("col_name"));
			}
			
			sqlcolHead.append(column.getStr("col_name"));
		}
		if(isGroup){
			datawheresql.append(groupSql);
		}else{
			datawheresql.append(" ORDER BY ROW_NUMBER ");
		}
		
		selectSql.append(sqlcol).append(datawheresql);
		
		//先检查任务反馈表中是否存放有数据如果无数据的话，采用抽取的模板数据，如果有数据，采用已存放的数据
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex");
		int pageSize = getParaToInt("pageSize", 10);
		List<Record> datas = Db.find("SELECT "+sqlcolHead.toString()+" FROM YYGL_MISSION_ORIGINAL_DATA  WHERE DATA_TYPE = '0' AND MODEL_CFG_ID = ?  ORDER BY ROW_NUMBER " ,model_cfg_id);
		int total = 0;
		if(sqlcol.length() != 0){
			Page<Record> pageList = Db.paginate(pageNum, pageSize,"SELECT * ","FROM ("+selectSql+")  ",paramList.toArray());
			datas.addAll(pageList.getList());
			total = pageList.getTotalRow();
		}
		setAttr("model", model);
		setAttr("columns", columns);
		setAttr("datas", datas);
		setAttr("total", total);
		renderJson();
	}
	
	//查看已反馈内容
	public void retoactionView(){
		setAttr("id", getPara("id"));
		setAttr("type", getPara("type"));
		setAttr("missionIssueId", getPara("missionIssueId"));
		render("do_configdata_retoactionview.jsp");
	}
	
	//下载汇总数据内容
	public void downloadSummary(){
		
		
		String id = getPara("id");
		String type = getPara("type"); // user_table -  用户任务查看 ，issue_table - 发起任务查看
		String mission_issue_id = ""; 
		String model_cfg_id = ""; 
		 
			
		//获取当前任务数据
		Record userTask = null;
		
		if("user_table".equals(type)){
			userTask = Db.findById("YYGL_MISSION_USER", id);
			mission_issue_id = 	userTask.get("MISSION_ISSUE_ID");
		}else{
			mission_issue_id = 	id;
		}
		
		//获取任务配置数据
		Record taskparam= Db.findFirst("SELECT * FROM YYGL_MISSION_CONFIG C WHERE C.ID  =(SELECT MISSION_CONFIG_ID FROM YYGL_MISSION_ISSUE I WHERE I.ID = ? )",mission_issue_id);
		
		model_cfg_id = taskparam.getStr("model_cfg_id");
		
		//查询配置信息
		Record model = Db.findById("YYGL_MISSION_MODEL_CONFIG", model_cfg_id);
		
		//查询列配置信息
		List<Record> columns = Db.find("SELECT * FROM YYGL_MISSION_COLUMN_CONFIG T WHERE MODEL_CFG_ID=? ORDER BY TO_NUMBER(SUBSTR(T.COL_NAME,10))",model_cfg_id);
		
		StringBuffer selectSql = new StringBuffer(" SELECT ");
		
		StringBuffer datawheresql = new StringBuffer(" FROM YYGL_MISSION_UPLOAD_DATA WHERE 1=1 ");
		
		StringBuffer groupSql = new StringBuffer();
		
		List<Object> paramList = new ArrayList<Object>();
		
		StringBuffer sqlcol = new StringBuffer();
		
		//判断任务状态，如果该任务状态为已经转发他人情况，并且所有的数据内容都已反馈，此时展示数据为该人员所有数据都查询出来
		
		if( "issue_table".equals(type) ){
			datawheresql.append("  AND USER_MISSION_ID in (   ");
			datawheresql.append(" select id from YYGL_MISSION_USER m where  USER_MISSION_STATUS = '02'  start with m.id in ");
			datawheresql.append(" (SELECT ID FROM  YYGL_MISSION_USER  WHERE mission_issue_id = ? AND UP_MISSION_ID is null AND USER_MISSION_STATUS = '02') ");
			datawheresql.append(" connect by m.UP_MISSION_ID=prior m.id ) ");
			paramList.add(mission_issue_id);
		}else{
			if( userTask !=null && "0".equals(userTask.getStr("FORWARD_FLAG"))  ){
				datawheresql.append("  AND USER_MISSION_ID in (   ");
				datawheresql.append(" select id from YYGL_MISSION_USER m where id =? AND USER_MISSION_STATUS = '02'  UNION ");
				datawheresql.append(" select id from YYGL_MISSION_USER m where  USER_MISSION_STATUS = '02'  start with m.id in ");
				datawheresql.append(" (SELECT ID FROM  YYGL_MISSION_USER  WHERE UP_MISSION_ID = ? AND USER_MISSION_STATUS = '02') ");
				datawheresql.append(" connect by m.UP_MISSION_ID=prior m.id ) ");
				paramList.add(id);
				paramList.add(id);
			}else{
				datawheresql.append("  AND USER_MISSION_ID  in ( SELECT ID FROM  YYGL_MISSION_USER WHERE USER_MISSION_ID = ? AND USER_MISSION_STATUS = '02' )  ");
				paramList.add(id);
			}
		} 
		
		boolean isfir = true;
		Record column = null;
		String[] headers = new String [columns.size()]; //存储数据头部字段
		//是否存在关键字列，此处可以放在模板配置中处理
		boolean isGroup = false,isGroupFir = true;
		for (int j = 0,len = columns.size(); j < len ; j++) {
			//是否关键字列
			if(	"0".equals(columns.get(j).get("COL_IS_KEYWORD")) ){
				isGroup = true;
				groupSql.append("GROUP BY ");
				break;
			}
		}
		
		
		
		//组装sql
		for (int j = 0,len = columns.size(); j < len ; j++) {
			column = columns.get(j);
			if(isfir){
				isfir = false;
			}else{
				sqlcol.append(",");
			}
			
			//是否关键字列
			if(isGroup){
				if("0".equals(column.get("COL_IS_KEYWORD")) ){
					sqlcol.append(column.getStr("col_name"));
					if(isGroupFir){
						isGroupFir = false;
					}else{
						groupSql.append(",");
					}
					groupSql.append(column.getStr("col_name"));
				}else{
					//关键不可作为汇总部分,汇总如果不指定聚合方式时，按照拼接方式来做以，号分隔方式
					if("0".equals(column.get("COL_IS_SUMMARY")) ){//指定汇总统计方式
						sqlcol.append( column.getStr("COL_FORMART_TYPE")+"("+column.getStr("col_name")+") "+  column.getStr("col_name"));
					}else{
						sqlcol.append( "wm_concat("+column.getStr("col_name")+") "+  column.getStr("col_name"));
					}
				}
			}else{
				//按照明细方式查询sql
				sqlcol.append(column.getStr("col_name"));
			}
			
			headers[j] = column.getStr("col_name");//主要是列名，用于导出使用 例如 DATA_C_1, DATA_C_2....
		}
		if(isGroup){
			datawheresql.append(groupSql);
		}else{
			datawheresql.append(" ORDER BY ROW_NUMBER ");
		}
		
		selectSql.append(sqlcol).append(datawheresql);
		
		//先检查任务反馈表中是否存放有数据如果无数据的话，采用抽取的模板数据，如果有数据，采用已存放的数据
		// 获取查询参数
		List<Record> datas = Db.find("SELECT "+sqlcol.toString()+" FROM YYGL_MISSION_ORIGINAL_DATA  WHERE DATA_TYPE = '0' AND MODEL_CFG_ID = ?  "+(isGroup ? groupSql.toString() : " ORDER BY ROW_NUMBER ") ,model_cfg_id);
		if(sqlcol.length() != 0){
			List<Record> pageList = Db.find("SELECT * FROM ("+selectSql+")  ",paramList.toArray());
			datas.addAll(pageList);
		}
		
		
			
		String mergecells = model.getStr("mergecells");
		
		//转换为可识别的合并方式
		List<CellRangeAddress> mergecellsRange = HandsontableMergeToPoiMerge(mergecells);
		
		
		String[] columns_title = new String[]{};
		String fileName = "";
		try {
		    fileName = new String(("任务模板"+System.currentTimeMillis()+".xls").getBytes("GB2312"),"ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		}
		ExcelRenderx er = new ExcelRenderx(fileName, columns_title, headers, datas,getResponse());
		er.setMergecells(mergecellsRange);
		er.render();
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "任务配置".toString(), "7","任务配置-模板下载");
		log.info(getLogPoint().exit("任务配置-模板下载").toString());
		renderNull();
	}
	/**
	 * 打包下载会议已反馈内容，以多个sheet模式制取
	 */
	public void downloadPackRetoaction(){
		
		//打包下载时，可以传入的源为 已发起任务表，和 待处理任务表，数据来源分别是， 
		
		
		//下载数据模板,暂时不考虑抽取数据情况
		
		String id = getPara("id");
		String type = getPara("type"); // user_table -  用户任务查看 ，issue_table - 发起任务查看
		String mission_issue_id = ""; 
		String userTaskid = "";
		String model_cfg_id = ""; 
		 
			
		//获取当前任务数据
		Record userTask = null;
		
		if("user_table".equals(type)){
			userTask = Db.findById("YYGL_MISSION_USER", id);
			mission_issue_id = 	userTask.get("MISSION_ISSUE_ID");
			userTaskid = id;
		}else{
			mission_issue_id = 	id;
		}
		
		//获取任务配置数据
		Record taskparam= Db.findFirst("SELECT * FROM YYGL_MISSION_CONFIG C WHERE C.ID  =(SELECT MISSION_CONFIG_ID FROM YYGL_MISSION_ISSUE I WHERE I.ID = ? )",mission_issue_id);
		List<Object> paramList = new ArrayList<Object>();
		//根据任务获取下级任务执行情况
		
		StringBuffer taskListSql = new StringBuffer("SELECT T.*,"
				+ "(SELECT NAME FROM SYS_USER_INFO U WHERE U.USER_NO = T.USER_NO AND U.DELE_FLAG='0') || "
				+ "(SELECT NAME FROM SYS_ROLE_INFO U WHERE U.ID = T.ROLEID AND U.ROLE_DELE_FLAG='1') || "
				+ "(SELECT ORGNAME FROM SYS_ORG_INFO U WHERE U.ORGNUM = T.ORGID AND U.STAT='1')  TASKNAME "
				+ " FROM YYGL_MISSION_USER T WHERE USER_MISSION_STATUS = '02' ");
		if("issue_table".equals(type) ){
			taskListSql.append("  AND MISSION_ISSUE_ID = ?  AND UP_MISSION_ID  IS NULL   ");
			paramList.add(mission_issue_id);
		}else{
			if( userTask !=null && "0".equals(userTask.getStr("FORWARD_FLAG"))  ){
				taskListSql.append("  AND UP_MISSION_ID = ?   "); 
				paramList.add(id);
			}else{
				taskListSql.append("  AND UP_MISSION_ID = ?   "); 
				paramList.add(id);
			}
			//未转发时，直接拦截掉，在前端拦截
		}
		//获取到下级任务列表，开始进行循环生成数据文件
		List<Record> taskList =  Db.find(taskListSql.toString(),paramList.toArray()) ;
		
		
		
		
		StringBuffer childSql = new StringBuffer();
		
		model_cfg_id = taskparam.getStr("model_cfg_id");
		//查询配置信息
		Record model = Db.findById("YYGL_MISSION_MODEL_CONFIG", model_cfg_id);
		
		String org_break_type =  model.getStr("org_break_type");//是否有列使用了拆分 0- 有，1-没有
		
		
		//查询列配置信息
		List<Record> columns = Db.find("SELECT * FROM YYGL_MISSION_COLUMN_CONFIG T WHERE MODEL_CFG_ID=? ORDER BY TO_NUMBER(SUBSTR(T.COL_NAME,10))", model_cfg_id);
		
		StringBuffer sqlcol = new StringBuffer("*");
		String[] headers = new String [columns.size()]; //存储数据头部字段
		
		StringBuffer datawheresql ;
		List<Object> paramList_user = new ArrayList<Object>();
		List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
		
		//下载的模板依据于最新的模板配置参数进行
		
		String mergecells = model.getStr("mergecells");
		//转换为可识别的合并方式
		List<CellRangeAddress> mergecellsRange = HandsontableMergeToPoiMerge(mergecells);
		//保存数据表内容，包括头以及数据列 row,col,rowto,colto
		
		
		boolean isfir = true,isSplit = "0".equals(org_break_type);
		Record column = null;
		for (Record userTask_r : taskList) {
			datawheresql = new StringBuffer(" WHERE MODEL_CFG_ID = ? ");
			//清空
			paramList.clear();
			paramList.add(id);
			paramList_user.clear();
			sqlcol = new StringBuffer("*");
			headers = new String [columns.size()];
			childSql.delete(0, childSql.length());
			
			userTaskid = userTask_r.getStr("ID");
			
			if("0".equals(userTask_r.getStr("FORWARD_FLAG"))){
				childSql.append("  AND USER_MISSION_ID in (   ");
				childSql.append(" select id from YYGL_MISSION_USER m where id =? AND USER_MISSION_STATUS = '02'  UNION ");
				childSql.append(" select id from YYGL_MISSION_USER m where  USER_MISSION_STATUS = '02'  start with m.id in ");
				childSql.append(" (SELECT ID FROM  YYGL_MISSION_USER  WHERE UP_MISSION_ID = ? AND USER_MISSION_STATUS = '02') ");
				childSql.append(" connect by m.UP_MISSION_ID=prior m.id ) ");
				paramList_user.add(userTaskid);
				paramList_user.add(userTaskid);
			}else{
				childSql.append(" AND USER_MISSION_ID = ? ");
				paramList_user.add(userTaskid);
			}
			
			isfir = true;
			for (int j = 0,len = columns.size(); j < len ; j++) {
				column = columns.get(j);
				if(isfir){
					sqlcol.delete(0, sqlcol.length());
					isfir = false;
				}else{
					sqlcol.append(",");
				}
				
				String col_is_data =  model.getStr("COL_IS_DATA");//是否有列使用了数据抽取 0- 有，1-没有
				//如果有数据抽取情况，此时可以拼装sql的，具体如何拼装就要看数据了
				if("0".equals(col_is_data)){
					//这里可以写成配置化的，目前数据抽取先放放
					
				}
				
				sqlcol.append(column.getStr("col_name"));
				
				headers[j] = column.getStr("col_name");//主要是列名，用于导出使用 例如 DATA_C_1, DATA_C_2....
				
				if(isSplit){//存在拆分情况，检查列参数
					
					//如果当前列设置了拆分,目前只做了按照机构，角色，人员进行拆分，如果有其他场景可以自行扩展
					if("0".equals(column.get("col_is_break"))){
						
						//主要是考虑后期拆分场景多样化的话，可以放到单独配置表中
						MissionControlUtil.getIntanceof().getDataReportSplitSqlByUserTask(datawheresql, paramList, userTask_r, column);
						
					}
					
				}
			}
			datawheresql.append(" ORDER BY ROW_NUMBER ");
			
			//先检查任务反馈表中是否存放有数据如果无数据的话，采用抽取的模板数据，如果有数据，采用已存放的数据
			// 获取查询参数
			
			List<Record> list = Db.find("SELECT "+sqlcol.toString()+" FROM YYGL_MISSION_UPLOAD_DATA WHERE 1=1 "+childSql+" ORDER BY ROW_NUMBER ",paramList_user.toArray());
			if(list != null && list.size()  == 0){
				//查询数据内容内容
				list = Db.find("SELECT "+sqlcol.toString()+" FROM YYGL_MISSION_ORIGINAL_DATA  "+datawheresql.toString(),paramList.toArray());
			}else{
				List<Record>  datas = Db.find("SELECT "+sqlcol.toString()+" FROM YYGL_MISSION_ORIGINAL_DATA  WHERE DATA_TYPE = '0' AND MODEL_CFG_ID = ? ORDER BY ROW_NUMBER ",model_cfg_id);
				datas.addAll(list);
				list = datas;
			}

			Map<String, Object> type02 = new HashMap<String, Object>();
			type02.put("sheetName", userTask_r.get("taskname"));
			type02.put("headers", new String[]{});
			type02.put("columns", headers);
			type02.put("list", list);
			type02.put("mergecells",mergecellsRange);
			params.add(type02);
		}
		

		
		

		//主要应用于 待处理和已处理阶段。一般来讲会传入 任务 id ,不会传入配置id,所以需要对id进行查询
		
		
		
		String fileName = "";
		try {
		    fileName = new String(("填报内容"+System.currentTimeMillis()+".xls").getBytes("GB2312"),"ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		}
		/*ExcelRenderx er = new ExcelRenderx(fileName, columns_title, headers, list,getResponse());
		er.setMergecells(mergecellsRange);
		er.render();*/
		
		ExcelRenderx er = new ExcelRenderx(fileName, params,getResponse());
		er.render();
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "待处理任务".toString(), "7","待处理任务-填报内容下载");
		log.info(getLogPoint().exit("待处理任务-填报内容下载").toString());
		renderNull();
}
	
}
