package com.goodcol.controller.zxglctl;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.server.zxglserver.CustClaimServer;
import com.goodcol.util.AppUtils;
import com.goodcol.util.ext.anatation.LogBind;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.ext.render.excel.ExcelRender;
import com.goodcol.util.zxgldbutil.DownDetailUtil;

/**
 * 客户认领
 * 
 * @author start
 * 
 */
@RouteBind(path = "/zxCustClaim")
@Before({ ManagerPowerInterceptor.class, OperationLogRecordInterceptor.class })
public class CustClaimController extends BaseCtl {
	
	public static Logger log = Logger.getLogger(CustClaimController.class);
	CustClaimServer custClaimServer = new CustClaimServer();
	/**
	 * 主页面
	 */
	public void index() {
		String flag=getPara("flag");
		String orgId = getCurrentUser().getStr("ORG_ID");
		String level = AppUtils.getOrgLevel(orgId);
		
		String secOrgId = null;
		if(Integer.parseInt(level)>2){
			secOrgId = AppUtils.getSecOrg(orgId);
		}
		if(Integer.parseInt(level)>3){
			orgId = AppUtils.getThrOrg(orgId);
		}
//		String roleNames = AppUtils.getRoleNames(getCurrentUser().getStr("ID"));
//		if(roleNames.contains("领导")){
//			level = AppUtils.getLevByRole(roleNames);
//		}
		
		setAttr("orgId", orgId);
		setAttr("flag", flag);
		setAttr("level", level);
		setAttr("secOrgId", secOrgId);
		setAttr("userId", getCurrentUser().getStr("ID"));
		render("index.jsp");
	}
	
	/**
	 * 客户认领列表
	 */
	public void findCustList(){
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		
		map.put("pageNum", pageNum);
		map.put("pageSize", pageSize);
		
		//获取登陆用户组织代码用于权限控制
		String orgId = getPara("orgId");
//		String level = getPara("levelStr");
		map.put("orgId", orgId);
		map.put("orgArr", AppUtils.getOrgStr(orgId,"2"));
		map.put("areaArr", AppUtils.getAreaStr(orgId));
		
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
		map.put("area_code", getPara("area_code"));
		map.put("prov_code", getPara("prov_code"));
		map.put("levelStr", getPara("levelStr"));
		if(AppUtils.StringUtil(getPara("orgSelect")) != null){
			map.put("orgSelArr", AppUtils.getOrgStr(getPara("orgSelect"),"2"));
		}
		
		// 从数据库查询指定条数的用户记录
		Page<Record> r =custClaimServer.pageList(map);

		// 将获取到的用户信息填充到request对象的属性中
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());

		// 打印日志
		log.info("listBusi--r.getList():" + r.getList());
		log.info("listBusi--r.getTotalRow():" + r.getTotalRow());
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 客户认领列表总数
	 */
	public void findCustCount(){
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 获取查询参数
		
		//获取登陆用户组织代码用于权限控制
		String orgId = getPara("orgId");
		String level = getPara("levelStr");
		map.put("orgArr", "1".equals(level)?null:AppUtils.getOrgStr(orgId,"2"));
		
		// 获取页面输入查询条件
		map.put("flag", getPara("flag"));
		map.put("levelStr", getPara("levelStr"));
		map.put("orgId", orgId);
		
		// 从数据库查询指定条数的用户记录
		String total =custClaimServer.custListCount(map);
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("totalCnt", total);

		renderJson();
	}
	
	/**
	 * 最新客户入池数据时间
	 */
	public void findNewDate(){
		
//		Map<String, Object> map = new HashMap<String, Object>();
		
		// 获取查询参数
		
		//获取登陆用户组织代码用于权限控制
		//String roleNames = AppUtils.getRoleNames(getCurrentUser().getStr("ID"));
//		String orgId = getPara("orgId");
//		map.put("orgArr", AppUtils.getOrgStr(orgId,"2"));
//		
//		// 获取页面输入查询条件
//		map.put("flag", getPara("flag"));
//		
//		// 从数据库查询指定条数的用户记录
//		String newDate =custClaimServer.findNewDate(map);
		
		String newDate =AppUtils.findGNewDate();
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("newDate", newDate);

		renderJson();
	}
		
	/**
	 * 跳转认领界面
	 */
	public void claim() {
		render("claim.jsp");
	}
	
	/**
	 * 认领保存
	 */
	public void claimSave() {
		// 获取查询参数
		Map<String, Object> map = new HashMap<String, Object>();
		//获取登陆用户组织代码用于权限控制
		map.put("user_id", getCurrentUser().getStr("id"));
		map.put("user_name", getCurrentUser().getStr("name"));
		
		map.put("customercode", getPara("customercode"));
		map.put("id", getPara("id"));
		map.put("pro", getPara("prop"));
		map.put("cust_stat", getPara("cust_stat"));
		
		custClaimServer.claimSave(map);
		renderNull();
	}
	
	/**
	 * 客户申诉
	 */
	public void claimComp() {
		render("claimComp.jsp");
	}
	
	/**
	 * 客户申诉保存
	 */
	public void claimCompSave() {
		// 获取查询参数
		Map<String, Object> map = new HashMap<String, Object>();
		//获取登陆用户组织代码用于权限控制
		map.put("user_id", getCurrentUser().getStr("id"));
		map.put("user_name", getCurrentUser().getStr("name"));
		
		map.put("cust_claim_id", getPara("id"));
		map.put("appeal_reas", getPara("remark"));
		
		// 
		custClaimServer.claimCompSave(map);
		renderNull();
	}
	
	/**
	 * 客户详情
	 */
	public void getDetail() {
		String id = getPara("id");
		if (AppUtils.StringUtil(id) == null) {
			renderNull();
			return;
		}
		Record re = custClaimServer.getDetail(id);
		setAttr("record", re);
		renderJson();
	}
	
	/**
	 * 下载用户数据，保存为excel文件
	 */
	@LogBind(menuname = "客户认领", type = "7", remark = "客户认领-下载")
	public void download() {

		// 获取查询参数
		Map<String, Object> map = new HashMap<String, Object>();
		//获取登陆用户组织代码用于权限控制
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
		map.put("area_code", getPara("area_code"));
		map.put("prov_code", getPara("prov_code"));
		
		String flag = getPara("flag");
		String levelStr = getPara("levelStr");
 		int level = AppUtils.StringUtil(levelStr)!=null?Integer.parseInt(levelStr):0;

		List<Record> lr = custClaimServer.custList(map);
		int a = 0;
		int prop = 0;
		String poolid = null;
		String claim_mgr = null;
		List<Record> list = null;
		if(null!=lr&&lr.size()>0){
			a = lr.size();
			
			for (int j = 0; j < lr.size(); j++){
				poolid = lr.get(j).getStr("id");
				list = custClaimServer.findClaimList(poolid);
				if(null!=list&&list.size()>0){
					prop = 0;
					claim_mgr = null;
					for (int i = 0; i < list.size(); i++) {
						prop += Integer.parseInt(list.get(i).getStr("claim_prop"));
						if(AppUtils.StringUtil(claim_mgr)==null){//claim_cust_mgr_name || ':' || claim_prop || '%'
							claim_mgr = list.get(i).getStr("claim_cust_mgr_name")+"-"+list.get(i).getStr("claim_prop")+"%";
						}else {
							claim_mgr = claim_mgr +";"+ list.get(i).getStr("claim_cust_mgr_name")+"-"+list.get(i).getStr("claim_prop")+"%";
						}
					}
				}
				lr.get(j).set("claim_prop_all", prop+"%");
				lr.get(j).set("to_claim_prop", (100-prop)+"%");
				lr.get(j).set("claim_cust_mgr_name", claim_mgr);
			}
		}
		System.out.println("当前条数：" + a);

//		if (a == 0) {
//			renderText("<script type='text/javascript'>parent.callback(\"当前没有数据可以下载\"); </script>");
//			return;
//		}else if (a >= 3000) {
//			renderText("<script type='text/javascript'>parent.callback(\"下载数据不能超过3000条!\"); </script>");
//			return;
//		

		// 所有字段
		String[] headArr = {"resp_center_no","resp_center_name","cust_no","dummy_cust_no", "name","admi_area","relate_cust_name","relate_mgr_name",
				"relate_mgr_mobile", "clas_five_cn", "remain_date","stay_date", "cust_stat_cn","claim_prop_all",
				"claim_cust_mgr_name","to_claim_prop","befor_cust_mgr_name" };
		//所有字段
		String[] columnArr = {"责任中心号", "责任中心名称", "客户号","虚拟客户号", "客户名称","行政区域","关联客户名称","关联客户经理","关联客户经理联系方式", 
				"五层分类", "剩余认领时间（天）", "客户池剩余停留时间（天）", "客户状态","已认领比例","认领客户经理","待认领比例","上任客户经理" };
		
		// 指定用哪些查询出来的字段填充excel文件
		String[] headers = {};
		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = {};
		//要去除的字段
		String hstr = null;
		String cstr = null;
		if("1".equals(flag)&&level>=3){
			cstr=",关联客户名称,关联客户经理,关联客户经理联系方式,剩余认领时间（天）,虚拟客户号,";
			hstr=",relate_cust_name,relate_mgr_name,relate_mgr_mobile,remain_date,dummy_cust_no,";
			headers = AppUtils.delCol(hstr, headArr);
			columns = AppUtils.delCol(cstr, columnArr);
		}else if("1".equals(flag)&&level<3){
			cstr=",行政区域,关联客户名称,关联客户经理,关联客户经理联系方式,客户池剩余停留时间（天）,虚拟客户号,";
			hstr=",admi_area,relate_cust_name,relate_mgr_name,relate_mgr_mobile,stay_date,,dummy_cust_no,";
			headers = AppUtils.delCol(hstr, headArr);
			columns = AppUtils.delCol(cstr, columnArr);
		}else if("2".equals(flag)&&level>=3){
			cstr=",责任中心号,责任中心名称,五层分类,剩余认领时间（天）,客户号,";
			hstr=",resp_center_no,resp_center_name,clas_five_cn,remain_date,cust_no,";
			headers = AppUtils.delCol(hstr, headArr);
			columns = AppUtils.delCol(cstr, columnArr);
		}else if("2".equals(flag)&&level<3){
			cstr=",责任中心号,责任中心名称,五层分类,客户池剩余停留时间（天）,客户号,";
			hstr=",resp_center_no,resp_center_name,clas_five_cn,stay_date,cust_no,";
			headers = AppUtils.delCol(hstr, headArr);
			columns = AppUtils.delCol(cstr, columnArr);
		}

		String fileName = "";
		String xlsName = "客户认领列表.xls";
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
	
	/**
	 * 客户批量认领
	 */
	public void claimMany() {
		render("claimMany.jsp");
	}
	
	/**
	 * 客户批量认领校验:看我认领最大值
	 */
	public void claimManyCheck() {
		String ids = getPara("ids");
		StringBuffer param = new StringBuffer();
		if (AppUtils.StringUtil(ids) != null) {
			String[] idsArr = ids.split(",");
			param.append("(");
			for(int i=0;i<idsArr.length;i++){
				param.append("'");
				param.append(idsArr[i]);
				if(i==idsArr.length-1){
					param.append("'");
				}else{
					param.append("',");
				}
			}
			param.append(")");
		}
		
		String claim_prop_max=custClaimServer.claimManyCheck(param.toString());
		setAttr("claim_prop_max", claim_prop_max);

		renderJson();
	}
	
	/**
	 * 客户批量认领保存
	 */
	public void claimManySave() {
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
				
				map.put("customercode", codesArr[i]);
				map.put("id", idsArr[i]);
				map.put("pro", getPara("prop"));
				
				custClaimServer.claimSave(map);
			}
		}
		renderNull();
	}
	
	/**
	 * 客户撤回
	 */
	public void claimBack() {
		String ids = getPara("ids");
		String distrFlag = getPara("distrFlag");
		String mgr_id=null;
		String distr_id=null;
		String backFlag="1";
		String claimDate="";
		if (AppUtils.StringUtil(ids) != null) {
			String[] idsArr = ids.split(",");
			if ("1".equals(distrFlag)) {
				distr_id = getCurrentUser().getStr("id");
			}else{
				mgr_id = getCurrentUser().getStr("id");
			}
			for(int i=0;i<idsArr.length;i++){
				String id = idsArr[i];
				
				claimDate = custClaimServer.claimDate(id, mgr_id, distr_id);
				if(Double.parseDouble(claimDate)>=1){
					backFlag="2";
					break;
				}
			}
			if("1".equals(backFlag)){
				for(int i=0;i<idsArr.length;i++){
					custClaimServer.claimBack(idsArr[i],mgr_id,distr_id);
					backFlag="3";
				}
			}
		}
		
		setAttr("backFlag", backFlag);

		renderJson();
	}

	/**
	 * 客户撤回校验
	 */
	public void claimBackCheck() {
		
	}
	
	/**
	 * 字典映射
	 **/
	public void getAreaList() {
		String pid = getPara("pid");
		List<Record> list = custClaimServer.areaList(pid);;
		
		String jsonStr = "";
		//将下拉列表改成排序的
		if(null!=list&&list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				String columns = JSONObject.fromObject(list.get(i)).getString("columns");
				if (i == 0) {
					jsonStr = columns;
				} else if (i > 0) {
					jsonStr = columns + "," + jsonStr;
				}
			}
		}
		
		jsonStr = "[ " + jsonStr + " ]";
		renderJson(jsonStr);
	}
	
	/**
	 * 认领详情
	 **/
	public void findClaimList(){
		String poolid = getPara("id");
		List<Record> list = custClaimServer.findClaimList(poolid);
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("data", list);
		
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 已认领比例
	 **/
	public void findClaimProp(){
		String poolid = getPara("id");
		List<Record> list = custClaimServer.findClaimList(poolid);
		int prop = 0;
		if(null!=list&&list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				prop += Integer.parseInt(list.get(i).getStr("claim_prop"));
			}
		}
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("prop", prop);
		
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 分配客户经理
	 **/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void findToDistrMan(){
		String poolid = getPara("id");
		List<Record> list = custClaimServer.findClaimList(poolid);
		Set set = new HashSet<>();
		if(null!=list&&list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				set.add(list.get(i).getStr("creat_name"));
			}
		}
		String creat_name = null;
		if(null!=set&&set.size()>0){
			for (Object name : set) {
				if(AppUtils.StringUtil(creat_name)==null){
					creat_name = String.valueOf(name);
				}else {
					creat_name = creat_name +","+ String.valueOf(name);
				}
			}
		}
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("creat_name", creat_name);
		
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 获取组织下拉框的值
	 */
	public void getOrgDatas() {
		// 获取当前用户信息
		
		String orgId = getPara("orgId");
		List<Record> list = AppUtils.getOrgList(orgId);
		setAttr("datas", list);
		// 打印日志
		log.info("getListById--datas:" + list);
		renderJson();
	}
	
	/**
	 * 创建下载任务
	 */
	@LogBind(menuname = "明细下载", type = "7", remark = "明细-下载")
	public void downTask() {
		String org =  getPara("upOrg");
		String flg =  getPara("flg");
		String downFlag = "";
		if("1".equals(flg)){
			downFlag =  "SQL_CUST_CLAIM_MY";
		}else if("2".equals(flg)){
			downFlag =  "SQL_CUST_CLAIM_OTHER";
		}
		
		String user_no = getCurrentUser().getStr("ID");
		String user_name = getCurrentUser().getStr("NAME");
		//创建下载任务
		Map<String, Object> taskmap = new HashMap<String, Object>();
		taskmap.put("org", org);
		taskmap.put("downFlag", downFlag);
		taskmap.put("user_no", user_no);
		taskmap.put("user_name", user_name);
		String rs = DownDetailUtil.DownTask(taskmap);
		setAttr("msg", rs);
		// 返回json数据
		renderJson();
	}
}
