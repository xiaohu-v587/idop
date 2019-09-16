package com.goodcol.controller.dop;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.core.plugin.activerecord.tx.Tx;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.PermissionInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.ParamContainer;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.ext.render.excel.ExcelRender;

/*
 * 预警模型管理
 * @author 陆磊磊
 * @date 2018-11-26
 */
@RouteBind(path = "/warningmodel")
@Before({ManagerPowerInterceptor.class})
public class WarningModelCtl  extends BaseCtl{

	// 记录日志用
	public static Logger log = Logger.getLogger(WarningModelCtl.class); 
		
		
	/**
	 * 加载 预警模型管理主页面
	 */
	@Override
	@Before(PermissionInterceptor.class)
	public void index() {
		render("index.jsp");
	}
	
	
	/**
	 * 查询预警模型管理
	 */
	public void getList() {
		// 获取查询条件
		String busi_module = getPara("busi_module");
		String warning_type_code = getPara("warning_type_code");
		String warning_code = getPara("warning_code");
		String warning_name = getPara("warning_name");
		String is_manual_indentify = getPara("is_manual_indentify");
		String is_confirm = getPara("is_confirm");
		String is_use = getPara("is_use");
		String warning_dimension=getPara("warning_dimension");
		String is_key_dxtz=getPara("is_key_dxtz");
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 20);
		
		Record warning=Db.findFirst("select id,type,remark,val from DOP_GANGEDMENU_INFO where id=?",new Object[]{warning_type_code});//获取预警类型详细信息
		// 查询语句
		String selectSql = " select  t.warning_code,t.warning_name, (select remark from sys_param_info where key ='warn_wd' and val=t.warning_dimension ) warning_dimension,t.busi_module, t.warning_type_code," +
				" (select remark from sys_param_info where key = 'dop_ywtype' and val = t.busi_module) busi_module_re," +
				"(select remark from DOP_GANGEDMENU_INFO where val=t.warning_type_code ) warning_type_re,"+
				" t.warning_level,  t.proc_level, t.proc_rate,t.threshold_type," +
				"t.initial_x,t.initial_y," +
				"case t.is_manual_indentify  when '0' then '否' when '1' then '是' end is_manual_indentify," +
				"case t.check_level  when '0' then '省行' when '1' then '二级分行' when '2' then '管辖行' when '3' then '网点' end check_level," +
				"case t.is_confirm  when '0' then '否' when '1' then '是' end is_confirm ," +
				"t.first_detail,t.first_detail_code,t.second_detail,t.second_detail_code,t.warning_eval,t.is_key_warning," +
				"t.remark,t.initial_z,t.is_use,(select remark from sys_param_info where key ='dxtz' and val=t.is_key_dxtz ) is_key_dxtz,t.is_key_jsf";
		String extraSql = " FROM DOP_WARNING_PARAM t ";

		StringBuffer whereSql = new StringBuffer(" WHERE 1 = 1");//and  (t.is_use is  null or t.is_use = '1') 
		List<String> sqlStr = new ArrayList<String>();
		// 查询条件
		if (AppUtils.StringUtil(busi_module) != null) {
			if(!busi_module.equals("1001")){
				whereSql.append(" AND  t.busi_module = ? ");
				sqlStr.add(busi_module);
			}
		}
		if (AppUtils.StringUtil(warning_type_code) != null) {
			whereSql.append("  and t.warning_type_code = ? ");
			sqlStr.add(warning.get("val").toString());
		}
		if (AppUtils.StringUtil(warning_code) != null) {
			whereSql.append("  and t.warning_code like ? ");
			sqlStr.add("%"+warning_code+"%");
		}
		if (AppUtils.StringUtil(warning_name) != null) {
			whereSql.append("  and t.warning_name like ? ");
			sqlStr.add("%"+warning_name+"%");
		}
		if (AppUtils.StringUtil(is_confirm) != null) {
			whereSql.append("  and t.is_confirm = ? ");
			sqlStr.add(is_confirm);
		}
		if (AppUtils.StringUtil(is_manual_indentify) != null) {
			whereSql.append("  and t.is_manual_indentify = ? ");
			sqlStr.add(is_manual_indentify);
		}
		if (AppUtils.StringUtil(warning_dimension) != null) {
			whereSql.append("  and t.warning_dimension = ? ");
			sqlStr.add(warning_dimension);
		}
		if (AppUtils.StringUtil(is_key_dxtz) != null) {
			whereSql.append("  and t.is_key_dxtz = ? ");
			sqlStr.add(is_key_dxtz);
		}
		if (AppUtils.StringUtil(is_use) != null) {
			if("1".equals(is_use)){
				whereSql.append("  and (t.is_use = ? or t.is_use is null)");
			}else{
				whereSql.append("  and t.is_use = ? ");
			}
			sqlStr.add(is_use);
		}
		// 排序
		whereSql.append(" ORDER BY t.warning_code ");
		extraSql += whereSql.toString();
		// 查询
		Page<Record> r = Db.paginate(pageNum, pageSize, selectSql,extraSql, sqlStr.toArray());
		// 赋值
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		// 打印日志
		log.info("getList--r.getList():" + r.getList());
		log.info("getList--r.getTotalRow():" + r.getTotalRow());
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "预警模型管理", "3", "预警模型管理-查询");
		log.info("预警模型管理-查询");
		// 返回json数据
		renderJson();
	}
	
	//启用/停用
	@Before(Tx.class)//事物时候使用的注解
	public void changeWarningModelState(){
		String warning_code = getPara("warning_code");
		String is_use=getPara("is_use");
		Db.update("update dop_warning_param set is_use= ? where warning_code= ?", new Object[]{is_use,warning_code});
		// 记录日志
		log.info("预警模型管理-启用/停用");
		renderJson();
	}
	
	
	
	
	
	
	/**
	 * 预警模型详情页面
	 */
	@Before(PermissionInterceptor.class)
	public void detail() {
		render("warningModelDetail.jsp");
	}
	
	
	/**
	 * 预警模型新增页面
	 */
	@Before(PermissionInterceptor.class)
	public void add(){
		String pageType=getPara("pageType");
		setAttr("pageType",pageType);
		render("warningModelForm.jsp");
	}
	
	/**
	 * 预警模型编辑页面
	 */
	@Before(PermissionInterceptor.class)
	public void edit(){
		String pageType=getPara("pageType");
		setAttr("pageType",pageType);
		render("warningModelForm.jsp");
	}
	
	public void getchangerd(){
		String warning_code = getPara("warning_code");
		List<Record> rds = Db.find("select * from DOP_WARNING_info where last_approval_stat = '0' and warning_code = ?",warning_code);
		if(rds.size()>0){
			setAttr("result","0000");
		}else{
			setAttr("result","1111");
		}
		
		renderJson();
	}
	
	public void getchangerg(){
		String warning_code = getPara("warning_code");
		List<Record> rds = Db.find("select * from DOP_WARNING_info where indentify_status = '0' and warning_code = ?",warning_code);
		if(rds.size()>0){
			setAttr("result","0000");
		}else{
			setAttr("result","1111");
		}
		
		renderJson();
	}
	
	/**
	 * 根据主键，获取主键对应预警模型详细数据
	 */
	public void getWarningModelByCode(){
		String warning_code = getPara("key");
	
		/* 获取id对应预警模型*/
		String sqlSelect = "SELECT t.warning_code,t.warning_name, t.warning_dimension,t.busi_module, t.warning_type_code, t.warning_level,t.proc_level, t.proc_rate, t.threshold_type,"
				+"t.initial_x, t.initial_y,t.is_manual_indentify,t.check_level,t.is_confirm,t.first_detail,t.first_detail_code,t.second_detail,t.second_detail_code,t.warning_eval,t.is_key_warning,t.remark," +
				"t.initial_z,t.is_use,t.is_key_dxtz,t.is_key_jsf,t.message_org,t.message_person"
		+ "  from dop_warning_param t  where t.warning_code =? ";//此时必须先启用指标模型
		
		String sql = "select id from dop_gangedmenu_info where id in(select id from" +
				" dop_gangedmenu_info start with id=(select id from dop_gangedmenu_info where val='1001') " +
				"connect by prior id=upid) and val=?";
		Record re = Db.findFirst(sqlSelect,new Object[]{warning_code});	
		if(AppUtils.StringUtil(re.getStr("warning_type_code"))!=null){
			String warningTypeId = Db.findFirst(sql.replace("1001", "1003"),
					re.get("warning_type_code")).getStr("id");
			re.set("warning_type_code",warningTypeId);
		}
		setAttr("record", re);
		// 记录日志
		log.info("预警模型管理-获取预警模型");
		renderJson();
	}
	
	/**
	 * 新增保存预警模型信息
	 */
	@Before(Tx.class)
	public void saveWarningModel(){
		String warning_code = getPara("warning_code");
		String warning_name = getPara("warning_name");
		String warning_dimension = getPara("warning_dimension");
		String busi_module = getPara("busi_module");
		String warning_type_code = getPara("warning_type_code");
		String warning_level = getPara("warning_level");
		String proc_level = getPara("proc_level");
		String proc_rate = getPara("proc_rate");
		String threshold_type = getPara("threshold_type");//
		String initial_x = getPara("initial_x");
		String initial_y = getPara("initial_y");
		String is_manual_indentify = getPara("is_manual_indentify");
		String check_level = getPara("check_level");
		String is_confirm = getPara("is_confirm");
		String first_detail = getPara("first_detail");
		String first_detail_code = getPara("first_detail_code");
		String second_detail = getPara("second_detail");
		String second_detail_code = getPara("second_detail_code");
		String warning_eval = getPara("warning_eval");
		String is_key_warning = getPara("is_key_warning");
		
		String remark = getPara("remark");
		String initial_z = getPara("initial_z");//
		String is_use = getPara("is_use");
		String is_key_dxtz=getPara("is_key_dxtz");
		String is_key_jsf=getPara("is_key_jsf");
		String message_org=getPara("message_org");
		String message_person=getPara("message_person");
		
		Record r=Db.findFirst("select id,type,upid,val,remark from dop_gangedmenu_info where id=?",warning_type_code);

		//新增保存预警模型信息
		Db.update("insert into DOP_WARNING_PARAM (warning_code,warning_name,warning_dimension,busi_module,warning_type_code,warning_level,proc_level" +
				",proc_rate,threshold_type,initial_x,initial_y,is_manual_indentify,check_level,is_confirm,first_detail,first_detail_code,second_detail,second_detail_code," +
				"warning_eval,is_key_warning,remark,initial_z,is_use,is_key_dxtz,is_key_jsf,message_org,message_person) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",new Object[]{warning_code,warning_name,warning_dimension,busi_module,r.get("val").toString(),
				warning_level,proc_level,proc_rate,threshold_type,initial_x,initial_y,is_manual_indentify,check_level,is_confirm,first_detail,first_detail_code,
				second_detail,second_detail_code,warning_eval,is_key_warning,remark,initial_z,is_use,is_key_dxtz,is_key_jsf,message_org,message_person});
		// 记录日志
		log.info("预警模型管理-预警模型新增保存");
		renderJson();
	}
	
	/**
	 * 编辑更新预警模型信息
	 */
	@Before(Tx.class)
	public void updateWarningModel(){
		String warning_code = getPara("warning_code");
		
		String warning_name = getPara("warning_name");
		String warning_dimension = getPara("warning_dimension");
		String busi_module = getPara("busi_module");
		String warning_type_code = getPara("warning_type_code");
		String warning_level = getPara("warning_level");
		String proc_level = getPara("proc_level");
		String proc_rate = getPara("proc_rate");
		String threshold_type = getPara("threshold_type");
		String initial_x = getPara("initial_x");
		String initial_y = getPara("initial_y");
		String is_manual_indentify = getPara("is_manual_indentify");
		String check_level = getPara("check_level");
		String is_confirm = getPara("is_confirm");
		String first_detail = getPara("first_detail");
		String first_detail_code = getPara("first_detail_code");
		String second_detail = getPara("second_detail");
		String second_detail_code = getPara("second_detail_code");
		String warning_eval = getPara("warning_eval");
		String is_key_warning = getPara("is_key_warning");
		
		String remark = getPara("remark");
		String initial_z = getPara("initial_z");
		String is_use = getPara("is_use");
	    String is_key_dxtz = getPara("is_key_dxtz");
	    String is_key_jsf = getPara("is_key_jsf");
	    String message_org=getPara("message_org");
		String message_person=getPara("message_person");
		
		Record r=Db.findFirst("select id,type,upid,val,remark from dop_gangedmenu_info where id=?",warning_type_code);
		
		Db.update("update DOP_WARNING_PARAM set warning_name= ?,warning_dimension= ?,busi_module =?,warning_type_code =?" +
				",warning_level=?,proc_level=?,proc_rate=?,threshold_type=?,initial_x=?,initial_y=?,is_manual_indentify=?,check_level=?,is_confirm=?" +
				",first_detail=?,first_detail_code=?,second_detail=?,second_detail_code=?" +
				",warning_eval=?,is_key_warning=?,remark=?,initial_z=?,is_use=?,is_key_dxtz=?,is_key_jsf=?,message_org=?,message_person=?" +
				"where warning_code= ? ", new Object[]{warning_name,warning_dimension,busi_module,r.get("val").toString()
				,warning_level,proc_level,proc_rate,threshold_type,initial_x,initial_y,is_manual_indentify,check_level,is_confirm
				,first_detail,first_detail_code,second_detail,second_detail_code,warning_eval,is_key_warning
				,remark,initial_z,is_use,is_key_dxtz,is_key_jsf,message_org,message_person,warning_code});
		
		String date=BolusDate.getDateTime("yyyyMMddhhmmss");
		// 记录日志
		if("0".equals(is_key_dxtz)){
			Db.update("update dop_warning_info set prompt_status='3' where warning_code=? and prompt_status ='0' ",warning_code);
		}else{
			Db.update("update dop_warning_info set prompt_status='0' where warning_code=? and prompt_status ='3' ",warning_code);
		}
		log.info("预警模型管理-预警模型编辑更新");
		renderJson();
	}
	
	
	
	
	/**
	 * 获取预警类型下拉列表
	 */
	public void getWarningtypecodeList(){
		String val = getPara("val");
		String sql="";
		if(val.equals("1001")){
			sql = " select id,remark,val,upid from dop_gangedmenu_info start with id=(select id from dop_gangedmenu_info where val='1003')  connect by prior id = upid";
		}else{
			sql = "select id,remark,val,upid from dop_gangedmenu_info start with id=(select id from " +
					"dop_gangedmenu_info where val=? and id in(select id from dop_gangedmenu_info start with" +
							" id=(select id from dop_gangedmenu_info where val='1003') connect by prior id=upid)) " +
							"connect by prior id = upid";
		}
		
		List<Record> list = val.equals("1001")?Db.find(sql):Db.find(sql ,new Object[]{val});
		setAttr("data",list);
		renderJson();
	}
	
	/**
	 * 导出Excel表格
	 */
	@SuppressWarnings("unchecked")
	@Before(PermissionInterceptor.class)
	public void download(){
		// 获取查询条件
		String busi_module = getPara("busi_module");
		String warning_type_code = getPara("warning_type_code");
		String warning_code = getPara("warning_code");
		String warning_name = getPara("warning_name");
		Record warning=Db.findFirst("select id,type,remark,val from DOP_GANGEDMENU_INFO where id=?",new Object[]{warning_type_code});//获取预警类型详细信息
		// 查询语句
		String selectSql = " select  t.warning_code,t.warning_name, (select remark from sys_param_info where key ='warn_wd' and val=t.warning_dimension ) warning_dimension,t.busi_module, t.warning_type_code," +
				" (select remark from sys_param_info where key = 'dop_ywtype' and val = t.busi_module) busi_module_re," +
				"(select remark from DOP_GANGEDMENU_INFO where val=t.warning_type_code) warning_type_re,"+
				" t.warning_level,  t.proc_level, t.proc_rate,t.threshold_type," +
				"t.initial_x,t.initial_y," +
				"case t.is_manual_indentify  when '0' then '否' when '1' then '是' end is_manual_indentify," +
				"case t.check_level  when '0' then '省行' when '1' then '二级分行' when '2' then '管辖行' when '3' then '网点' end check_level," +
				"case t.is_confirm  when '0' then '否' when '1' then '是' end is_confirm ," +
				"t.first_detail,t.first_detail_code,t.second_detail,t.second_detail_code,t.warning_eval,t.is_key_warning," +
				"t.remark,t.initial_z,t.is_use,(select remark from sys_param_info where key ='dxtz' and val=t.is_key_dxtz ) is_key_dxtz,t.is_key_jsf  FROM DOP_WARNING_PARAM t";

		 
		StringBuffer whereSql = new StringBuffer(" WHERE 1 = 1 and  (t.is_use is  null or t.is_use = '1') ");
		List<String> sqlStr = new ArrayList<String>();
		// 查询条件
		if (AppUtils.StringUtil(busi_module) != null) {
			if(!busi_module.equals("1001")){
				whereSql.append(" AND  t.busi_module = ? ");
				sqlStr.add(busi_module);
			}
		}
		if (AppUtils.StringUtil(warning_type_code) != null) {
			whereSql.append("  and t.warning_type_code = ? ");
			sqlStr.add(warning.get("val").toString());
		}
		if (AppUtils.StringUtil(warning_code) != null) {
			whereSql.append("  and t.warning_code like ? ");
			sqlStr.add("%"+warning_code+"%");
		}
		if (AppUtils.StringUtil(warning_name) != null) {
			whereSql.append("  and t.warning_name like ? ");
			sqlStr.add("%"+warning_name+"%");
		}
		// 排序
		whereSql.append(" ORDER BY t.warning_code ");
		selectSql += whereSql.toString();
		// 查询
		List<Record>list=Db.find(selectSql, sqlStr.toArray());
		
		for (Record record : list) {
			record.set("is_use",ParamContainer.getDictName("dop_markstate", record.getStr("is_use")));//在查询结果里插入业务类型值
			record.set("warning_level",ParamContainer.getDictName("dop_warning_lvl", record.getStr("warning_level")));
			
		}
		// 指定用哪些查询出来的字段填充excel文件
		String[] headers = {"busi_module_re","warning_type_re","warning_code","warning_name","is_use","warning_level","is_manual_indentify","check_level","is_confirm","warning_dimension","is_key_dxtz"};

		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = {"业务模块","模型类型","模型编号","模型名称","模型状态","模型等级","是否需要人工认定","核查层级","是否需要上级确认","模型维度","是否发送短信"};
		
	
		String fileName = "";
		try {
			fileName = new String("模型.xls".getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
			log.error("userctl-download", e);
		}
		// 转换成excel
		ExcelRender er = new ExcelRender(fileName, columns, headers, list, getResponse());
		er.render();
		// 打印日志
		log.info("download--list:" + list);
		
		renderNull();
		
		
		
	}
	
	

}
