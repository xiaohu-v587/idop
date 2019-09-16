package com.goodcol.controller.zxglctl;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.server.zxglserver.CustClaimServer;
import com.goodcol.server.zxglserver.CustDistrServer;
import com.goodcol.util.AppUtils;
import com.goodcol.util.ext.anatation.LogBind;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.ext.render.excel.ExcelRender;

/**
 * 客户分配
 * 
 * @author start
 * 
 */
@RouteBind(path = "/zxCustDistr")
@Before({ ManagerPowerInterceptor.class, OperationLogRecordInterceptor.class })
public class CustDistrCtl extends BaseCtl {
	
	public static Logger log = Logger.getLogger(CustDistrCtl.class);
	CustDistrServer custDistrServer = new CustDistrServer();
	CustClaimServer custClaimServer = new CustClaimServer();
	
	/**
	 * 主页面
	 */
	public void index() {
		String flag=getPara("flag");
		String orgId = getCurrentUser().getStr("ORG_ID");
		String level = AppUtils.getOrgLevel(orgId);
		String secOrgId = null;
//		if(Integer.parseInt(level)>2){
//			secOrgId = AppUtils.getSecOrg(orgId);
//		}
		String roleNames = AppUtils.getRoleNames(getCurrentUser().getStr("ID"));
		String roleLevel = AppUtils.getLevByRole(roleNames);
		if(AppUtils.StringUtil(roleLevel)!=null){
			//取对应权限级别的orgid
			orgId = AppUtils.getUpOrg(orgId, roleLevel);	
		}
		
		setAttr("orgId", orgId);
		setAttr("flag", flag);
		setAttr("level", level);
		setAttr("secOrgId", secOrgId);
		setAttr("userId", getCurrentUser().getStr("ID"));
		render("index.jsp");
	}
	
	public void compuDistri() {
		render("distribution.jsp");
	}
	
	public void compuDistriMany() {
		render("distrMany.jsp");
	}
	
	/**
	 *分配保存
	 */
	public void distsave() {
		// 获取查询参数
		Map<String, Object> map = new HashMap<String, Object>();
		//获取登陆用户组织代码用于权限控制
		map.put("user_id", getCurrentUser().getStr("id"));
		map.put("user_name", getCurrentUser().getStr("name"));
		
		map.put("id", getPara("id"));
		map.put("customercode", getPara("customercode"));
		map.put("mgr_ids", getPara("mgr_ids"));
		map.put("mgr_names", getPara("mgr_names"));
		map.put("claim_props", getPara("claim_props"));
		
		custDistrServer.distrSave(map);
		
		renderNull();
	}
	
	/**
	 *批量分配保存
	 */
	public void distManySave() {
		String ids = getPara("ids");
		String codes = getPara("codes");
		Map<String, Object> map = null;
		if (AppUtils.StringUtil(ids) != null) {
			String[] idsArr = ids.split(",");
			String[] codesArr = codes.split(",");
			for(int i=0;i<idsArr.length;i++){
				map = new HashMap<String, Object>();
				map.put("user_id", getCurrentUser().getStr("id"));
				map.put("user_name", getCurrentUser().getStr("name"));
				
				map.put("id", idsArr[i]);
				map.put("customercode", codesArr[i]);
				map.put("mgr_ids", getPara("mgr_ids"));
				map.put("mgr_names", getPara("mgr_names"));
				map.put("claim_props", getPara("claim_props"));
				
				custDistrServer.distrSave(map);
			}
		}
		renderNull();
	}
	
	/**
	 * 下载用户数据，保存为excel文件
	 */
	@SuppressWarnings("unchecked")
	@LogBind(menuname = "客户分配", type = "7", remark = "客户分配-下载")
	public void download() {

		// 获取查询参数
		Map<String, Object> map = new HashMap<String, Object>();
		//获取登陆用户组织代码用于权限控制
		String orgId = getPara("orgId");
		map.put("orgArr", "1".equals(getPara("levelStr"))?null:AppUtils.getOrgStr(orgId,"2"));
		
		// 获取页面输入查询条件
		map.put("userId", getCurrentUser().getStr("ID"));
		map.put("cust_no", getPara("cust_no"));
		map.put("flag", getPara("flag"));
		map.put("name", getPara("name"));
		map.put("clas_five", getPara("clas_five"));
		map.put("cust_stat", getPara("cust_stat"));
		map.put("cust_stat_flag", getPara("cust_stat_flag"));
		map.put("cust_type", getPara("cust_type"));
		map.put("is_back", getPara("is_back"));
		map.put("befor_name", getPara("befor_name"));
		map.put("pageFlag", getPara("pageFlag"));
		
		String flag = getPara("flag");

		List<Record> lr = custClaimServer.custList(map);
		int a = 0;
		int prop = 0;
		String poolid = null;
		String claim_mgr = null;
		String creat_name = null;
		List<Record> list = null;
		Set set = null;
		if(null!=lr&&lr.size()>0){
			a = lr.size();
			
			for (int j = 0; j < lr.size(); j++){
				poolid = lr.get(j).getStr("id");
				list = custClaimServer.findClaimList(poolid);
				if(null!=list&&list.size()>0){
					prop = 0;
					claim_mgr = null;
					creat_name = null;
					set = new HashSet<>();
					for (int i = 0; i < list.size(); i++) {
						prop += Integer.parseInt(list.get(i).getStr("claim_prop"));
						if(AppUtils.StringUtil(claim_mgr)==null){//claim_cust_mgr_name || ':' || claim_prop || '%'
							claim_mgr = list.get(i).getStr("claim_cust_mgr_name")+"-"+list.get(i).getStr("claim_prop")+"%";
						}else {
							claim_mgr = claim_mgr +";"+ list.get(i).getStr("claim_cust_mgr_name")+"-"+list.get(i).getStr("claim_prop")+"%";
						}
						set.add(list.get(i).getStr("creat_name"));
					}
				}
				if(null!=set&&set.size()>0){
					for (Object name : set) {
						if(AppUtils.StringUtil(creat_name)==null){
							creat_name = String.valueOf(name);
						}else {
							creat_name = creat_name +","+ String.valueOf(name);
						}
					}
				}
				lr.get(j).set("claim_prop_all", prop+"%");
				lr.get(j).set("to_claim_prop", (100-prop)+"%");
				lr.get(j).set("claim_cust_mgr_name", claim_mgr);
				lr.get(j).set("creat_name", creat_name);
			}
		}
		System.out.println("当前条数：" + a);

//		if (a == 0) {
//			renderText("<script type='text/javascript'>parent.callback(\"当前没有数据可以下载\"); </script>");
//			return;
//		}else if (a >= 3000) {
//			renderText("<script type='text/javascript'>parent.callback(\"下载数据不能超过3000条!\"); </script>");
//			return;
//		}

		// 所有字段
		String[] headArr = { "resp_center_name","cust_no","dummy_cust_no", "name","relate_cust_name","relate_mgr_name",
				"clas_five_cn","stay_date", "cust_stat_cn","claim_prop_all",
				"claim_cust_mgr_name","befor_cust_mgr_name","to_claim_prop","creat_name","befor_cust_mgr_name" };
		//所有字段
		String[] columnArr = {"责任中心名称", "客户号","虚拟客户号", "客户名称","关联客户名称","关联客户经理",
				"五层分类", "停留时间", "客户状态","已认领比例","认领客户经理","上任客户经理","待认领比例","分配客户经理","上任客户经理" };
		
		// 指定用哪些查询出来的字段填充excel文件
		String[] headers = {};
		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = {};
		//要去除的字段
		String hstr = null;
		String cstr = null;
		if("1".equals(flag)){
			cstr=",关联客户名称,关联客户经理,关联客户经理联系方式,停留时间,虚拟客户号,";
			hstr=",relate_cust_name,relate_mgr_name,relate_mgr_mobile,stay_date,dummy_cust_no,";
			headers = AppUtils.delCol(hstr, headArr);
			columns = AppUtils.delCol(cstr, columnArr);
		}else if("2".equals(flag)){
			cstr=",责任中心号,责任中心名称,五层分类,剩余认领时间,上任客户经理,分配客户经理,客户号,";
			hstr=",resp_center_no,resp_center_name,clas_five_cn,remain_date,befor_cust_mgr_name,creat_name,cust_no,";
			headers = AppUtils.delCol(hstr, headArr);
			columns = AppUtils.delCol(cstr, columnArr);
		}

		String fileName = "";
		String xlsName = "客户分配列表.xls";
		try {
			fileName = new String(xlsName.getBytes("GB2312"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			log.error("userctl-download", e);
		}

		// 转换成excel
		ExcelRender er = new ExcelRender(fileName, columns, headers, lr, getResponse());
		er.render();
		// 打印日志
		log.info("download--lr:" + lr);
		renderNull();
	}
}
