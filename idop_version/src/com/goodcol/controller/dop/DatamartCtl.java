package com.goodcol.controller.dop;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.ext.render.excel.ExcelRender;

/*
 * 数据集市
 * @author 陆磊磊
 * @date 2018-11-20
 */
@RouteBind(path = "/datamart")
@Before({ManagerPowerInterceptor.class})
public class DatamartCtl  extends BaseCtl{

	// 记录日志用
	public static Logger log = Logger.getLogger(DatamartCtl.class); 
		
		
	/**
	 * 加载数据集市主页面
	 */
	@Override
	@Before(PermissionInterceptor.class)
	public void index() {
		render("index.jsp");
	}
	
	
	/**
	 * 查询数据集市指标
	 */
	public void getList() {
		// 获取查询条件
		String busi_module = getPara("busi_module");
		String sub_busi_code = getPara("sub_busi_code");
		String mark_code = getPara("mark_code");
		String mark_name = getPara("mark_name");
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 20);
		Record sub=Db.findFirst("select id,type,remark,val from DOP_GANGEDMENU_INFO where id=?",new Object[]{sub_busi_code});//获取业务类型详细信息

		
		// 查询语句
		String selectSql = " select  t.mark_code,t.mark_name, t.mark_dimension,t.busi_module, t.sub_busi_code," +
				"(select remark from DOP_GANGEDMENU_INFO where val=t.sub_busi_code) sub_re,"+
				"(select remark from DOP_GANGEDMENU_INFO where val=t.mark_type_code) marktype_re,"+
				" (select remark from sys_param_info where key = 'dop_ywtype' and val = t.busi_module) busi_module_re," +
				" t.mark_type_code, t.unit, t.cur, t.value_type, t.proc_level, t.proc_rate,t.summary_level," +
				"t.display_level,t.first_detail,t.first_detail_code,t.second_detail,t.second_detail_code," +
				"t.mark_eval,t.proc_mode,t.mark_direct,t.mark_att,t.mark_group,t.mark_average_mode," +
				"t.mark_minmax_mode,t.total_type,t.dividend,t.divisor,t.is_key_mark,t.is_use,t.source,t.zb3  ";
		String extraSql = " FROM dop_mark_param t ";

		StringBuffer whereSql = new StringBuffer(" WHERE 1 = 1 ");
		List<String> sqlStr = new ArrayList<String>();
		// 查询条件
		if (AppUtils.StringUtil(busi_module) != null) {
			whereSql.append(" AND  t.busi_module = ? ");
			sqlStr.add(busi_module);
		}
		if (AppUtils.StringUtil(sub_busi_code) != null) {
			whereSql.append("  and t.sub_busi_code = ? ");
			sqlStr.add(sub.get("val").toString());
		}
		if (AppUtils.StringUtil(mark_code) != null) {
			whereSql.append("  and t.mark_code like ? ");
			sqlStr.add("%"+mark_code+"%");
		}
		if (AppUtils.StringUtil(mark_name) != null) {
			whereSql.append("  and t.mark_name like ? ");
			sqlStr.add("%"+mark_name+"%");
		}
		// 排序
		whereSql.append(" ORDER BY mark_code ");
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
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "数据集市", "3", "数据集市-查询");
		log.info("数据集市-查询");
		// 返回json数据
		renderJson();
	}
	
	//启用/停用
	@Before(Tx.class)
	public void changeMarkState(){
		String mark_code = getPara("mark_code");
		String is_use=getPara("is_use");
		Db.update("update dop_mark_param set is_use= ? where mark_code= ?", new Object[]{is_use,mark_code});
		// 记录日志
		log.info("数据集市-启用/停用");
		renderJson();
	}
	
	//关键指标设置
	@Before(Tx.class)
	public void setKey(){
		String ids = getPara("ids");
		String is_key_mark=getPara("is_key_mark");
		String[]id=ids.split(",");
		for(String mark_code:id){
			Db.update("update dop_mark_param set is_key_mark= ? where mark_code= ?", new Object[]{is_key_mark,mark_code});
		}
		
		// 记录日志
		log.info("数据集市-关键指标设置");
		renderJson();
	}
	
	
	/**
	 * 指标调整展示层级页面
	 */
	@Before(PermissionInterceptor.class)
	public void adjustDisplayLevel() {
		render("displayLevelForm.jsp");
	}
	
	
	/**
	 * 指标调整展示层级页面
	 */
	@Before(PermissionInterceptor.class)
	public void edit() {
		render("markForm.jsp");
	}
	
	/**
	 * 指标调整展示层级页面
	 */
	@Before(PermissionInterceptor.class)
	public void add() {
		render("addMarkForm.jsp");
	}
	//关键指标设置
	@Before(Tx.class)
	public void updateAdjust(){
		String mark_code = getPara("mark_code");
		String d1=getPara("display_level1").equals("true")?"1":"0";
		String d2=getPara("display_level2").equals("true")?"1":"0";
		String d3=getPara("display_level3").equals("true")?"1":"0";
		String d4=getPara("display_level4").equals("true")?"1":"0";
		String display_level=d1+','+d2+","+d3+","+d4;
		Db.update("update dop_mark_param set display_level= ? where mark_code= ?", new Object[]{display_level,mark_code});
		// 记录日志
		log.info("数据集市-关键指标设置");
		renderJson();
	}
	
	
	//根据主键，获取主键对应指标详细数据
	public void getMarkByCode(){
		String mark_code = getPara("key");
	
		/* 获取id对应指标详细数据*/
		String selectSql = "SELECT t.mark_code,t.mark_name, t.mark_dimension,t.busi_module, t.sub_busi_code, t.mark_type_code, t.unit, t.cur, t.value_type, t.proc_level, t.proc_rate, t.summary_level,"
				+"t.display_level, t.first_detail,t.first_detail_code,t.second_detail,t.second_detail_code,t.mark_eval,t.proc_mode,t.mark_direct,t.mark_att,t.mark_group,t.mark_average_mode,t.mark_minmax_mode," +
				"t.total_type,t.dividend,t.divisor,t.is_key_mark,t.is_use,t.source,t.zb3 "
		+ "  from dop_mark_param t  where t.mark_code =? ";
		
		String sql = "select id from dop_gangedmenu_info where id in(select id from" +
				" dop_gangedmenu_info start with id=(select id from dop_gangedmenu_info where val='1001') " +
				"connect by prior id=upid) and val=?";
		
		Record re = Db.findFirst(selectSql,new Object[]{mark_code});	
		
		if(AppUtils.StringUtil(re.getStr("sub_busi_code"))!=null){
			String warningTypeId = Db.findFirst(sql,
					re.get("sub_busi_code")).getStr("id");
			re.set("sub_busi_code",warningTypeId);
		}
		
		if(AppUtils.StringUtil(re.getStr("mark_type_code"))!=null){
			String warningTypeId = Db.findFirst(sql.replace("1001", "1002"),
					re.get("mark_type_code")).getStr("id");
			re.set("mark_type_code",warningTypeId);
		}
		
		setAttr("record", re);
		// 记录日志
		log.info("数据集市-获取指标详情");
		renderJson();
	}
	
	public void checkMark(){
		String mark_code = getPara("markcode");
		String sql = "select * from dop_mark_param t where t.mark_code=?";
		Record re = Db.findFirst(sql,mark_code);
		if(re==null){
			setAttr("count", 0);
		}else{
			setAttr("count", 1);
		}
		renderJson();
	}
	
	//更新指标信息
	@Before(Tx.class)
	public void updateMark(){
		String mark_code = getPara("mark_code");
		
		String mark_name = getPara("mark_name");
		String mark_dimension = getPara("mark_dimension");
		String busi_module = getPara("busi_module");
		String sub_busi_code = getPara("sub_busi_code");
		String mark_type_code = getPara("mark_type_code");
		String unit = getPara("unit");
		String cur = getPara("cur");
		String value_type = getPara("value_type");
		String proc_level = getPara("proc_level");
		String proc_rate = getPara("proc_rate");
		String first_detail = getPara("first_detail");
		String first_detail_code = getPara("first_detail_code");
		String second_detail = getPara("second_detail");
		String second_detail_code = getPara("second_detail_code");
		String proc_mode = getPara("proc_mode");
		String mark_direct = getPara("mark_direct");
		String mark_att = getPara("mark_att");
		String mark_group = getPara("mark_group");
		String mark_average_mode = getPara("mark_average_mode");
		String mark_minmax_mode = getPara("mark_minmax_mode");
		String total_type = getPara("total_type");
		String dividend = getPara("dividend");
		String divisor = getPara("divisor");
		String is_use = getPara("is_use");
		String source = getPara("source");
		String zb3 = getPara("zb3");
		
		String d1=getPara("display_level1").equals("true")?"1":"0";
		String d2=getPara("display_level2").equals("true")?"1":"0";
		String d3=getPara("display_level3").equals("true")?"1":"0";
		String d4=getPara("display_level4").equals("true")?"1":"0";
		String display_level=d1+','+d2+","+d3+","+d4;
		
		String s1=getPara("summary_level1").equals("true")?"1":"0";
		String s2=getPara("summary_level2").equals("true")?"1":"0";
		String s3=getPara("summary_level3").equals("true")?"1":"0";
		String s4=getPara("summary_level4").equals("true")?"1":"0";
		String summary_level=s1+','+s2+","+s3+","+s4;
		
		Record sub=Db.findFirst("select id,type,upid,val,remark from dop_gangedmenu_info where id=?",sub_busi_code);
		Record mark=Db.findFirst("select id,type,upid,val,remark from dop_gangedmenu_info where id=?",mark_type_code);
		
		Db.update("update dop_mark_param set display_level= ? ,mark_name= ?,mark_dimension= ?,busi_module =?,sub_busi_code= ?,mark_type_code =?" +
				",unit=?,cur=?,value_type=?,proc_level=?,proc_rate=?,summary_level=?,first_detail=?,first_detail_code=?,second_detail=?,second_detail_code=?" +
				",proc_mode=?,mark_direct=?,mark_att=?,mark_group=?,mark_average_mode=?,mark_minmax_mode=?,total_type=?,dividend=?,divisor=?" +
				",is_use=?,source=?,zb3=?" +
				"where mark_code= ?", new Object[]{display_level,mark_name,mark_dimension,busi_module,sub.get("val").toString(),mark.get("val").toString(),unit,cur
				,value_type,proc_level,proc_rate,summary_level,first_detail,first_detail_code,second_detail,second_detail_code,proc_mode
				,mark_direct,mark_att,mark_group,mark_average_mode,mark_minmax_mode,total_type,dividend,divisor,is_use,source,zb3,mark_code});
		// 记录日志
		log.info("数据集市-指标信息编辑更新");
		renderJson();
	}
	
	//更新指标信息
		@Before(Tx.class)
		public void saveMark(){
			String mark_code = getPara("mark_code");			
			String mark_name = getPara("mark_name");
			String mark_dimension = getPara("mark_dimension");
			String busi_module = getPara("busi_module");
			String sub_busi_code = getPara("sub_busi_code");
			String mark_type_code = getPara("mark_type_code");
			String unit = getPara("unit");
			String cur = getPara("cur");
			String value_type = getPara("value_type");
			String proc_level = getPara("proc_level");
			String proc_rate = getPara("proc_rate");
			String first_detail = getPara("first_detail");
			String first_detail_code = getPara("first_detail_code");
			String second_detail = getPara("second_detail");
			String second_detail_code = getPara("second_detail_code");
			String proc_mode = getPara("proc_mode");
			String mark_direct = getPara("mark_direct");
			String mark_att = getPara("mark_att");
			String mark_group = getPara("mark_group");
			String mark_average_mode = getPara("mark_average_mode");
			String mark_minmax_mode = getPara("mark_minmax_mode");
			String total_type = getPara("total_type");
			String dividend = getPara("dividend");
			String divisor = getPara("divisor");
			String is_use = getPara("is_use");
			String source = getPara("source");
			String zb3 = getPara("zb3");
			
			String d1=getPara("display_level1").equals("true")?"1":"0";
			String d2=getPara("display_level2").equals("true")?"1":"0";
			String d3=getPara("display_level3").equals("true")?"1":"0";
			String d4=getPara("display_level4").equals("true")?"1":"0";
			String display_level=d1+','+d2+","+d3+","+d4;
			
			String s1=getPara("summary_level1").equals("true")?"1":"0";
			String s2=getPara("summary_level2").equals("true")?"1":"0";
			String s3=getPara("summary_level3").equals("true")?"1":"0";
			String s4=getPara("summary_level4").equals("true")?"1":"0";
			String summary_level=s1+','+s2+","+s3+","+s4;
			
			Record sub=Db.findFirst("select id,type,upid,val,remark from dop_gangedmenu_info where id=?",sub_busi_code);
			Record mark=Db.findFirst("select id,type,upid,val,remark from dop_gangedmenu_info where id=?",mark_type_code);
			
			Db.update("insert into dop_mark_param (display_level,mark_name,mark_dimension,busi_module,sub_busi_code,mark_type_code" +
					",unit,cur,value_type,proc_level,proc_rate,summary_level,first_detail,first_detail_code,second_detail,second_detail_code" +
					",proc_mode,mark_direct,mark_att,mark_group,mark_average_mode,mark_minmax_mode,total_type,dividend,divisor" +
					",is_use,source,zb3,mark_code)" +
					"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{display_level,mark_name,mark_dimension,busi_module,sub.get("val").toString(),mark.get("val").toString(),unit,cur
					,value_type,proc_level,proc_rate,summary_level,first_detail,first_detail_code,second_detail,second_detail_code,proc_mode
					,mark_direct,mark_att,mark_group,mark_average_mode,mark_minmax_mode,total_type,dividend,divisor,is_use,source,zb3,mark_code});
			// 记录日志
			log.info("数据集市-指标信息新增");
			renderJson();
		}
	
	/**
	 * 导出Excel表格
	 */
	@SuppressWarnings("unchecked")
	@Before(PermissionInterceptor.class)
	public void download(){
		// 获取查询条件
		String busi_module = getPara("busi_module"); //业务模块
		String sub_busi_code = getPara("sub_busi_code"); //业务类型
		String mark_code = getPara("mark_code"); // 指标编号
		String mark_name = getPara("mark_name"); // 指标名称
		
		Record sub=Db.findFirst("select id,type,remark,val from DOP_GANGEDMENU_INFO where id=?",new Object[]{sub_busi_code});//获取业务类型详细信息

		// 查询语句
		String selectSql = " select  t.mark_code,t.mark_name, t.mark_dimension,t.busi_module, t.sub_busi_code," +
				"(select remark from DOP_GANGEDMENU_INFO where val=t.sub_busi_code) sub_re,"+
				"(select remark from DOP_GANGEDMENU_INFO where val=t.mark_type_code) marktype_re,"+
				" (select remark from sys_param_info where key = 'dop_ywtype' and val = t.busi_module) busi_module_re," +
				" t.mark_type_code, t.unit, t.cur, t.value_type, t.proc_level, t.proc_rate,t.summary_level," +
				"t.display_level,t.first_detail,t.first_detail_code,t.second_detail,t.second_detail_code," +
				"t.mark_eval,t.proc_mode,t.mark_direct,t.mark_att,t.mark_group,t.mark_average_mode," +
				"t.mark_minmax_mode,t.total_type,t.dividend,t.divisor,t.is_key_mark,t.is_use,t.source,t.zb3  FROM dop_mark_param t ";

		StringBuffer whereSql = new StringBuffer(" WHERE 1 = 1 ");
		List<String> sqlStr = new ArrayList<String>();
		// 查询条件
		if (AppUtils.StringUtil(busi_module) != null) {
			whereSql.append(" AND  t.busi_module = ? ");
			sqlStr.add(busi_module);
		}
		if (AppUtils.StringUtil(sub_busi_code) != null) {
			whereSql.append("  and t.sub_busi_code = ? ");
			sqlStr.add(sub.get("val").toString());
		}
		if (AppUtils.StringUtil(mark_code) != null) {
			whereSql.append("  and t.mark_code like ? ");
			sqlStr.add("%"+mark_code+"%");
		}
		if (AppUtils.StringUtil(mark_name) != null) {
			whereSql.append("  and t.mark_name like ? ");
			sqlStr.add("%"+mark_name+"%");
		}
		// 排序
		whereSql.append(" ORDER BY mark_code ");
		selectSql += whereSql.toString();
		
		List<Record>list=Db.find(selectSql, sqlStr.toArray());
		
		for (Record record : list) {
			//在查询结果里插入业务类型值
			record.set("is_use",ParamContainer.getDictName("dop_markstate", record.getStr("is_use")));
			record.set("source",ParamContainer.getDictName("dop_marksource", record.getStr("source")));
			record.set("is_key_mark",ParamContainer.getDictName("dop_iskey", record.getStr("is_key_mark")));
			
		}
		// 指定用哪些查询出来的字段填充excel文件
		String[] headers = {"busi_module_re","sub_re","mark_code","mark_name","is_use","source","is_key_mark"};

		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = {"业务模块","业务类型","指标编号","指标名称","指标状态","指标来源","是否关键指标"};
		
	
		String fileName = "";
		try {
			fileName = new String("数据集市.xls".getBytes("GB2312"), "ISO_8859_1");
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
