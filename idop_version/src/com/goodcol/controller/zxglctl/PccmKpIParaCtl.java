package com.goodcol.controller.zxglctl;

import java.util.List;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.server.zxglserver.PccmKPIParaServer;
import com.goodcol.util.AppUtils;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.ext.anatation.RouteBind;

/**
 * @author dinggang
 * 
 */
@RouteBind(path = "/pccm_kpi_param")
@Before({ ManagerPowerInterceptor.class,OperationLogRecordInterceptor.class})
public class PccmKpIParaCtl extends BaseCtl {
	public static Logger log = Logger.getLogger(PccmKpIParaCtl.class);
	private PccmKPIParaServer pccmKPIParaServer=new PccmKPIParaServer();

	/**
	 * 列表页面
	 */
	@Override
	public void index() {
		render("index.jsp");
	}

	/**
	 * 获取kpi设置参数
	 */
	public void getList() {
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String khjl_type = getPara("khjl_type");
		String zj = getPara("zj");
		Record record=new Record();
		record.set("pageNum",pageNum);
		record.set("pageSize", pageSize);
		record.set("khjl_type", khjl_type);
		record.set("zj",zj);
		
		Page<Record> r=pccmKPIParaServer.getList(record);
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		renderJson();
	}

	/**
	 * 新增、修改kpi参数页面
	 */
	public void form() {
		render("form.jsp");
	}

	/**
	 * 保存或新增
	 */
	public void save() {
		String khjl_type = getPara("khjl_type");
		String zj = getPara("zj");
		String id = getPara("id");
		Record record=new Record();
		record.set("khjl_type",khjl_type);
		record.set("zj",zj);
		record.set("id",id);
		int flag = 0;
		String msg = "";
		String total = getPara("total");
		List<Record> records =pccmKPIParaServer.findKpiParams(record);
		if(records==null||records.isEmpty()){
			if (AppUtils.StringUtil(total) != null) {
				id=pccmKPIParaServer.saveKpiParam(record);
				for(int i=0;i<Integer.parseInt(total);i++){
					Record r=new Record();
					String zbmc = getPara("zbmcs" + i);
					String val = getPara("vals" + i);
					String zb_flag=getPara("zb_flag"+i);
					r.set("zbmc",zbmc);
					r.set("val",val);
					r.set("zb_flag",zb_flag);
					r.set("kpi_id",id);
					pccmKPIParaServer.saveKpiParamDetail(r);
				}
				flag=1;
			} else {
				flag = 2;
			}
		}
		if (flag == 1) {
			msg = "操作成功";
		} else if (flag == 2) {
			msg = "指标个数必须大于0";
		} else {
			msg = "该职级的客户经理类型已存在";
		}
		setAttr("flag", flag);
		setAttr("msg", msg);
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"), "KPI参数设置", "4",
				"KPI参数设置-新增");
		renderJson();
	}

	// public void save() {
	// String khjl_type = getPara("khjl_type");
	// String zj = getPara("zj");
	// String id = getPara("id");
	// String gs = getPara("gs");
	// // 获取指标个数
	// int ss = getParaToInt("ss");
	// String removeKpi=getPara("removeKpi");
	// String [] kpiIndexs=null;
	// if(AppUtils.StringUtil(removeKpi)!=null){
	// kpiIndexs=removeKpi.split(",");
	// }
	// int flag = 0;
	// String msg = "";
	// if(ss>0){
	// if (AppUtils.StringUtil(id) != null) {
	// List<Record> records = Db
	// .use("default")
	// .find("select * from pccm_kpi_param where khjl_type=? and zj=? and id!=?",
	// new Object[] { khjl_type, zj, id });
	// if (records == null || records.isEmpty()) {
	// flag = Db
	// .use("default")
	// .update("update pccm_kpi_param set khjl_type=?,zj=?,gs=? where id=?",
	// new Object[] { khjl_type, zj, gs, id });
	// Db.use("default").update("delete from pccm_zb_detail where kpi_id=?",new
	// Object[]{id});
	// }
	// } else {
	// id = AppUtils.getStringSeq();
	// List<Record> records = Db
	// .use("default")
	// .find("select * from pccm_kpi_param where khjl_type=? and zj=?",
	// new Object[] { khjl_type, zj });
	// if (records == null || records.isEmpty()) {
	// flag = Db
	// .use("default")
	// .update("insert into pccm_kpi_param(id,khjl_type,zj,gs) values(?,?,?,?)",
	// new Object[] { id, khjl_type, zj, gs });
	// }
	//
	// }
	// if(kpiIndexs!=null){
	// ss=ss+kpiIndexs.length;
	// }
	// for (int i = 1; i<=ss; i++) {
	// String zbmc = getPara("zbmcs" + i);
	// if(AppUtils.StringUtil(zbmc)!=null){
	// String val = getPara("vals" + i);
	// String zb_flag=getPara("zb_flag"+i);
	// Db.update("insert into pccm_zb_detail (id,zbmc_name,zbmc_val,kpi_id,zb_flag) values(?,?,?,?)",new
	// Object[]{AppUtils.getStringSeq(),zbmc,val,id,zb_flag});
	// }
	// }
	// }else{
	// flag=2;
	// }
	// if (flag == 1) {
	// msg = "操作成功";
	// } else if(flag==2){
	// msg = "指标个数必须大于0";
	// }else {
	// msg = "该职级的客户经理类型已存在，如要重新设置，请修改或删除原有的数据再添加！";
	// }
	// setAttr("flag", flag);
	// setAttr("msg", msg);
	// LoggerUtil.getIntanceof().saveLogger(
	// getCurrentUser().getStr("USER_NO"), "KPI参数设置", "4",
	// "KPI参数设置-新增");
	// renderJson();
	// }

	/**
	 * 删除kpi参数
	 */
	public void del() {
		String ids = getPara("ids");
		pccmKPIParaServer.del(ids);
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"), "KPI参数设置", "6",
				"KPI参数设置-删除");
		renderNull();
	}

	/**
	 * 获取kpi参数详情
	 */
	public void getDetail() {
		String id = getPara("id");
		Record record=pccmKPIParaServer.findKpiParam(id);
		// 获得指标详情
		List<Record> records =pccmKPIParaServer.findKpiParaDetail(id);
		setAttr("record", record);
		setAttr("records", records);
		renderJson();
	}

	/**
	 * 获取KPI明星客户经理 2018年5月28日15:19:00
	 * 
	 * @author liutao
	 * @param period
	 *            期次
	 * @return 返回KPI明星客户
	 */
	public void findKPIStarCustomer() {
		String roleType = getPara("roleType");
		List<Record> records = pccmKPIParaServer.findKPIStarCustomer(roleType);
		setAttr("records", records);
		renderJson();
	}

	/**
	 * 获取过去的所有期次 2018年5月29日10:32:15
	 * 
	 * @author liutao
	 */
	public void getPeriod() {
		List<Record> periods = pccmKPIParaServer.getPeriod();
		renderJson(periods);
	}

	/**
	 * 根据页面条件查询所有的KPI 2018年5月29日11:31:47
	 * 
	 * @author liutao
	 * @return
	 */
	public void getAllKPIList() {
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String userName = getPara("userName");
		String userNo = getPara("userNo");
		String orgId = getPara("orgId");
		String period = getPara("period");
		Page<Record> pages = pccmKPIParaServer.getAllKPIList(pageNum, pageSize, userName,
				userNo, orgId, period);
		setAttr("data", pages.getList());
		setAttr("total", pages.getTotalRow());
		renderJson();
	}

}
