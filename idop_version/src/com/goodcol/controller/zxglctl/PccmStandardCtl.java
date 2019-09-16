/**
 * 
 */
package com.goodcol.controller.zxglctl;

import java.util.List;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.server.zxglserver.PccmStandardServer;
import com.goodcol.util.AppUtils;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.ext.anatation.RouteBind;

/**
 * @author dinggang
 *
 */
@RouteBind(path = "/pccm_standard")
@Before({ ManagerPowerInterceptor.class,OperationLogRecordInterceptor.class })
public class PccmStandardCtl extends BaseCtl{
	private static PccmStandardServer pccmStandardServer=new PccmStandardServer();
	/**
	 * 进入首页
	 */
	@Override
	public void index() {
		render("index.jsp");
	}
	/**
	 * 获取标准数据
	 */
	public void getList(){
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String orgnum=getCurrentUser().getStr("orgnum");
		Record record=new Record();
		record.set("pageNum", pageNum);
		record.set("pageSize", pageSize);
		record.set("orgnum", orgnum);
		Page<Record> r=pccmStandardServer.getList(record);
		setAttr("data", r.getList());
		setAttr("total",r.getTotalRow());
		renderJson();
	}
	/**
	 * 进入新增、修改页面
	 */
	public void form(){
		render("form.jsp");
	}
	
	/**
	 * 获取二级机构数据
	 */
	public void getSecondOrg(){
		PccmStandardServer pccmStandardServer=new PccmStandardServer();
		List<Record> records=pccmStandardServer.getSecondOrg();
		renderJson(records);
	}
	/**
	 * 根据二级机构号获取三级机构数据
	 */
	public void getThridOrg(){
		String orgnum=getPara("orgnum");
		PccmStandardServer pccmStandardServer=new PccmStandardServer();
		List<Record> records=pccmStandardServer.getThridOrg(orgnum);
		renderJson(records);
	}
	/**
	 * 新增、修改标准数据
	 */
	public void save(){
		PccmStandardServer pccmStandardServer=new PccmStandardServer();
		int flag=0;
		String id=getPara("id");
		String standard_flag=getPara("standard_flag");
		String second_org_num=getPara("second_org_num");
		String third_org_num=getPara("third_org_num");
		int total=getParaToInt("total");
		Record record=new Record();
		record.set("standard_flag", standard_flag);
		record.set("second_org_num", second_org_num);
		record.set("third_org_num", third_org_num);
		record.set("id",id);
		id=pccmStandardServer.saveStandard(record);
		if(AppUtils.StringUtil(id)!=null){
			for(int i=0;i<total;i++){
				Record r=new Record();
				String condition_flag=getPara("condition_flag_"+i);
				String symbol1=getPara("symbol1_"+i);
				String condition_val1=getPara("condition_val1_"+i);
				String symbol2=getPara("symbol2_"+i);
				String condition_val2=getPara("condition_val2_"+i);
				String standard_remark=getPara("standard_remark_"+i);
				String result_value=getPara("result_value_"+i);
				r.set("standard_id",id).set("condition_flag",condition_flag).set("symbol1", symbol1).set("condition_val1",condition_val1).set("symbol2", symbol2).set("condition_val2",condition_val2).set("result_value",result_value).set("standard_remark",standard_remark);
				PccmStandardServer.saveStandardCondition(r);
			}
		}
		flag=1;
		setAttr("flag",flag);
		renderJson();
	}
	/**
	 * 删除标准数据
	 */
	public void del(){
		String ids = getPara("ids");
		PccmStandardServer pccmStandardServer=new PccmStandardServer();
		pccmStandardServer.del(ids);
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"), "标准管理", "6",
				"标准管理-删除");
		renderNull();
	}
	/**
	 * 获取标准详情
	 */
	public void getDetail(){
		String id = getPara("id");
		PccmStandardServer pccmStandardServer=new PccmStandardServer();
		Record record=pccmStandardServer.getStandardInfo(id);
		List<Record>records=pccmStandardServer.getStandardCondition(id);
		setAttr("record", record);
		setAttr("records",records);
		renderJson();
	}
}
