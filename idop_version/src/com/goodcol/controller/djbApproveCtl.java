package com.goodcol.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;


import com.goodcol.core.aop.Before;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.DbKit;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.Constant;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.MessageSender;
import com.goodcol.util.YyglUtil;
import com.goodcol.util.jfinal.anatation.RouteBind;
import com.goodcol.util.log.Logger;
import com.goodcol.util.log.LoggerConsole;
import com.jfinal.plugin.activerecord.ActiveRecordException;


@RouteBind(path = "/djbapprove")
@Before({ ManagerPowerInterceptor.class })
public class djbApproveCtl extends BaseCtl{
	/**
	 * 日志记录类.
	 */
	public LoggerConsole log = Logger.getLogger(); // 记录日志用
	
	//进入登记簿目录
	public void index(){
		String djbid = getPara("djbid");
		//根据登记簿ID,获取登记簿对应表名
		Record record = new Record();
		List<Record> re= Db.find("select ID,DJBID,DJBTAB,MENUID,CREATETIME,CREATEUSER from T_DJB_RESMENU_TAB"
				+ " where 1=1 and DJBID = '"+djbid+"'");
		if(re!=null&&re.size()>0){
			record = re.get(0);
			// 根据登记簿ID获取参数列表链表数据
			List<Record> jgpzMap = Db.find("select t.id as jgpzid,t.zdm,t.xsxs,t.kd,t.sfmhpp,t.sfxs,j.* from  t_djb_lbzs_jgpz t left join t_djb_jdsrysb j on t.djbid = j.djbid and t.zdm = j.ysm "+   
						"where 1=1 and t.DJBID = '"+record.getStr("djbid")+"' order by t.xsxs");
			// 根据登记簿ID获取查询条件链表数据
			List<Record> cxtjMap = Db.find("select t.id as cxtjid,t.bhqz,t.sffhbm,t.sfrq,t.sfmhpp,j.* from  t_djb_lbzs_cxtj t left join t_djb_jdsrysb j on t.djbid = j.djbid and t.bhqz = j.ysm "  
						+"where 1=1 and t.DJBID = '"+record.getStr("djbid")+"'  order by j.ysm");
			// 组合码表翻译数据
			Record dictMaps = new Record();
			//Map<String, Object> comMaps = new HashMap<String, Object>();
			if (jgpzMap.size() > 0) {
				for (Record map : jgpzMap) {
					if (map.getStr("ZDLX")!=null&&"1".equals(map.getStr("ZDLX"))) {
						dictMaps.set(map.getStr("YSM").toString(), map.getStr("MBLX"));
					}
				}
			}
			setAttr("jgpzdata",jgpzMap);
			setAttr("jgpztotal",total(jgpzMap));
			setAttr("cxtjdata",cxtjMap);
			setAttr("cxtjtotal",total(cxtjMap));
			setAttr("djbHandler",record);
			setAttr("dictMaps",dictMaps);
			setAttr("djbid",djbid);
		}
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿", "4", "登记簿目录");
		log.info(getLogPoint().exit("登记簿目录").toString());
		render("DjbHandlerindex.jsp");
	}
	
	//计算list条数
	private int total(List<Record> list) {
		return list == null ? 0 : list.size();
	}
	
	//获取登记簿目录数据
	public void getDjbmc(){
		// 获取查询条件
		// 获取当前用户信息
		String userno = getCurrentUser().getStr("USER_NO");
		String djbmc = getPara("djbmc");//登记簿名称
		String status = getPara("status");//登记簿名称
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String org = getCurrentUser().getStr("org_id");
		// 查询语句
		String selectSql = " select distinct p.*, d.djbtab ";
		String extraSql = " from t_djb_jcxx p  left join t_djb_apply t on t.djbid = p.id left join t_djb_resmenu_tab d on t.djbid = d.djbid  ";
				
		StringBuffer whereSql = new StringBuffer(" where t.orgnum = '"+org+"' ");
		List<String> sqlStr = new ArrayList<String>();
		// 查询条件
		if (AppUtils.StringUtil(djbmc) != null) {
			whereSql.append(" and P.DJBMC like ? ");
			sqlStr.add("%"+djbmc+"%");
		}
		if (AppUtils.StringUtil(status) != null) {
			whereSql.append(" and  t.status= ? ");
			sqlStr.add(status);
		}
		// 排序
		whereSql.append(" order by djbmc ");
		extraSql += whereSql.toString();
		// 查询
		Page<Record> r = Db.paginate(pageNum, pageSize, selectSql, extraSql, sqlStr.toArray());
		
		//重新拼接结果集
		List<Record> allList = new ArrayList<Record>();
		if(r != null && r.getTotalRow() > 0){
			if("1".equals(status)){
				status="0";
			}else{
				status="1";
			}
			for(Record re : r.getList()){
				String djbtab = re.getStr("djbtab");
				if(djbtab != null){
					
					List<Record> djbList = Db.find(" select t.* from "+djbtab+" t left join t_djb_apply p on t.id = p.sjid "
							+ " where 1 = 1 and t.status = '"+status+"' and t.cjr!='"+userno+"' and p.orgnum = '"+org+"' ");
					if(djbList != null && djbList.size() > 0){
						Record res = new Record();
						res.set("id", re.getStr("id"));
						res.set("djbmc", re.getStr("djbmc"));
						res.set("cjr", re.getStr("cjr"));
						res.set("cjrq", re.getStr("cjrq"));
						res.set("cjjg", re.getStr("cjjg"));
						res.set("flag", re.getStr("flag"));
						res.set("sfpz", re.getStr("sfpz"));
						allList.add(res);
					}
				}
			}
		}
		// 赋值
		setAttr("data", allList);
		setAttr("total", allList.size());
		// 打印日志
		log.info("getList--r.getList():" + r.getList());
		log.info("getList--r.getTotalRow():" + r.getTotalRow());
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿目录数据", "4", "登记簿目录-获取所有数据");
		log.info(getLogPoint().exit("登记簿目录-获取所有数据").toString());
		renderJson();
		
	}
	
	
	//获取登记簿查询条件
	public void queryCxtj(){
		//查询配置的条件
		String djbid = getPara("djbid");
		List<Record> cxtj = Db.find("select t.djbid, t.bhqz, t.sffhbm, t.sfrq, t.sfmhpp,t.isstarttime,t.isendtime,p.* "
					+ " from T_DJB_LBZS_CXTJ t left join t_djb_jdsrysb p on t.djbid = p.djbid and  t.bhqz=p.ysm " 
					+" where t.djbid ='"+djbid+"'");
		setAttr("cxtj",cxtj);
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "获取登记簿查询条件", "4", "获取登记簿查询条件");
		log.info(getLogPoint().exit("获取登记表查询条件").toString());
		renderJson();
	}

	
	/**
	 * 查询表头实体
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void queryHeaders() {
		String djbid = getPara("djbid");
		Map<String,Object>[] result = null;
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<Record> zdlist = Db.find("select t.ysm,t.yshy,t.zdlx,t.cdxz,t.zdpx,t.mblx from T_DJB_JDSRYSB t where 1=1 and t.djbid='"+djbid+"' order by t.zdpx");
		addHeader1(list);
		//拼接配置的字段作为列
		Map<String,Object> singleHeaderData = null;
		for(int i=0;i<zdlist.size();i++){
			String field = zdlist.get(i).get("ysm");
			if("1".equals(zdlist.get(i).getStr("zdlx"))){
				singleHeaderData = new HashMap<String,Object>();
				singleHeaderData.put("header",zdlist.get(i).get("yshy"));
				singleHeaderData.put("field",(field+"s").toString().toLowerCase());
				singleHeaderData.put("headerAlign","center");
				singleHeaderData.put("align","center");
				singleHeaderData.put("allowSort",true);
			}else if("4".equals(zdlist.get(i).getStr("zdlx"))){
				singleHeaderData = new HashMap<String,Object>();
				singleHeaderData.put("header",zdlist.get(i).get("yshy"));
				singleHeaderData.put("field",(field+"s").toString().toLowerCase());
				singleHeaderData.put("headerAlign","center");
				singleHeaderData.put("dateFormat","yyyy-MM-dd HH:mm:ss");
				singleHeaderData.put("align","center");
				singleHeaderData.put("allowSort",true);
			}else{
				singleHeaderData = new HashMap<String,Object>();
				singleHeaderData.put("header",zdlist.get(i).get("yshy"));
				singleHeaderData.put("field",zdlist.get(i).get("ysm").toString().toLowerCase());
				singleHeaderData.put("headerAlign","center");
				singleHeaderData.put("align","center");
				singleHeaderData.put("allowSort",true);
			}
			list.add(singleHeaderData);
		}
		addHeader2(list);
		addHeader4(list);
		result = list.toArray(new HashMap[list.size()]);
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "获取登记簿表头数据", "4", "获取登记簿表头数据");
		log.info(getLogPoint().exit("获取登记簿表头数据").toString());
		renderJson(result);
	}
	//拼接固定列
			private void addHeader4(List<Map<String, Object>> list) {
				Map<String,Object> singleHeaderData4 = new HashMap<String,Object>();
				singleHeaderData4.put("header","登记时间");
				singleHeaderData4.put("field","cjsj");
				singleHeaderData4.put("visible",true);
				singleHeaderData4.put("headerAlign","center");
				singleHeaderData4.put("align","center");
				singleHeaderData4.put("width","100");
				//singleHeaderData4.put("renderer","");
				singleHeaderData4.put("allowSort",true);
				singleHeaderData4.put("dateFormat","yyyy-MM-dd HH:mm:ss");

				list.add(singleHeaderData4);
			}
		
	
	//拼接固定列
	private void addHeader1(List<Map<String, Object>> list) {
		Map<String,Object> singleHeaderData0 = new HashMap<String,Object>();
		singleHeaderData0.put("type","checkcolumn");
		singleHeaderData0.put("width","20");
		list.add(singleHeaderData0);
		Map<String,Object> singleHeaderData1 = new HashMap<String,Object>();
		singleHeaderData1.put("header","序号");
		singleHeaderData1.put("type","indexcolumn");
		singleHeaderData1.put("headerAlign","center");
		singleHeaderData1.put("align","center");
		singleHeaderData1.put("width","30");
		list.add(singleHeaderData1);
		Map<String,Object> singleHeaderData2 = new HashMap<String,Object>();
		singleHeaderData2.put("header","id");
		singleHeaderData2.put("field","id");
		singleHeaderData2.put("visible",false);
		singleHeaderData2.put("headerAlign","center");
		singleHeaderData2.put("align","center");
		singleHeaderData2.put("width","100");
		singleHeaderData2.put("allowSort",true);
		list.add(singleHeaderData2);
	}
	
	//拼接固定列
	private void addHeader2(List<Map<String, Object>> list) {
		Map<String,Object> singleHeaderData3 = new HashMap<String,Object>();
		singleHeaderData3.put("header","状态");
		singleHeaderData3.put("field","statuss");
		singleHeaderData3.put("visible",true);
		singleHeaderData3.put("headerAlign","center");
		singleHeaderData3.put("align","center");
		singleHeaderData3.put("width","100");
		singleHeaderData3.put("renderer","statusRender");
		singleHeaderData3.put("allowSort",true);
		list.add(singleHeaderData3);
	}
	
	
	/*获取登记簿数据
	 * 1.先根据djbid获取登记簿表名
	 * 2.再根据表名来查询数据
	 */
	public void queryList(){
		// 获取查询条件
		String djbid = getPara("djbid");
		String status = getPara("status");
		//当前登录人所在机构
		String org = getCurrentUser().getStr("org_id");
		//当前登录人
		String userno = getCurrentUser().getStr("USER_NO");
		//根据当前登录人所在机构等级获取对应数据
		Record orgRecord = Db.findFirst(" select by2,orgnum from sys_org_info  where stat = '1' and orgnum = '"+org+"'");
		if(orgRecord != null){
			if(orgRecord.getStr("by2") != null){
				String by2 = orgRecord.getStr("by2");
				if("0".equals(by2)){
					org = Constant.HEAD_OFFICE;
				}else if("1".equals(by2)){
					org = orgRecord.getStr("orgnum");
				}
			}
		}
		StringBuffer sbf = new StringBuffer("");
		String sz = getPara("sz");
		String jgcxtj = getPara("jgcxtj");
		//拼接所选机构
		String orgarr = null;
		//所有查询条件
		String[] arr=null;
		//机构查询条件
		String[] jgarr=null;
		if(sz!=null){
			arr = sz.split(",");
		}
		if(jgcxtj != null && !"".equals(jgcxtj)){
			jgarr = jgcxtj.split(",");
			for(int i=0;i<jgarr.length;i++){
				if(!" ".equals(jgarr[i])){
					sbf.append("'"+jgarr[i]+"',");
				}
			}
			if(sbf.length() >0){
				orgarr = sbf.substring(0, sbf.length()-1).toString();
			}
		}
		
		String djbname = null;
		//获取查询条件字段
		List<Record> cxtj = Db.find("select t.djbid, t.bhqz, t.sffhbm, t.sfrq, t.sfmhpp,p.* "
				+ " from T_DJB_LBZS_CXTJ t left join t_djb_jdsrysb p on t.djbid = p.djbid and  t.bhqz=p.ysm " 
				+" where t.djbid ='"+djbid+"'");
		int num = total(cxtj);
		Record re = new Record();
		for(int i=0;i<num;i++){
			if("1".equals(cxtj.get(i).getStr("sffhbm"))){
				re.set("cxtj"+i, cxtj.get(i).getStr("bhqz"));
				re.set("sfmhpp"+i, cxtj.get(i).getStr("sfmhpp"));
			}else if("2".equals(cxtj.get(i).getStr("sffhbm"))){
				re.set("cxtj"+i, cxtj.get(i).getStr("bhqz"));
				re.set("sfmhpp"+i, cxtj.get(i).getStr("sfmhpp"));
			}else if("3".equals(cxtj.get(i).getStr("sffhbm"))){
				re.set("cxtj"+i, cxtj.get(i).getStr("bhqz"));
				re.set("sfmhpp"+i, cxtj.get(i).getStr("sfmhpp"));
			}else{
				re.set("cxtj"+i, cxtj.get(i).getStr("ysm"));
				re.set("sfmhpp"+i, cxtj.get(i).getStr("sfmhpp"));
			}
		}
		//获取登记簿名称
		Record formRecord = Db.findFirst("select t.djbid,t.djbtab from T_DJB_RESMENU_TAB t where t.djbid= '"+djbid+"'");
		if(formRecord != null){
			djbname = formRecord.getStr("djbtab");
		}
				
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		
		//
		List<Record> zdlxList = Db.find("select * from  t_djb_jdsrysb p where 1=1  and  p.djbid='"+djbid+"' ");
		// 查询语句List
		String forech =null;
		StringBuffer selectSql = new StringBuffer(" select t.*,p.status as statuss ");
		if(zdlxList != null){
			for(Record r1 : zdlxList){
				if("1".equals(r1.getStr("zdlx"))){
					selectSql.append(" ,(select remark from sys_param_info where key ='"+r1.getStr("mblx")+"' and status = 1 and val = t."+r1.getStr("ysm")+") "+r1.getStr("ysm")+"s");
				}else if("4".equals(r1.getStr("zdlx"))){
					selectSql.append(" ,to_date("+r1.getStr("ysm")+", 'yyyy-mm-dd hh24:mi:ss') as "+r1.getStr("ysm")+"s");
				}
			}
		}
		String extraSql = " from "+djbname;
				
		StringBuffer whereSql = new StringBuffer(" t left join t_djb_apply p on t.id = p.sjid where 1 = 1 and p.orgnum = '"+org+"' and p.status = '"+status+"' " );
		List<String> sqlStr = new ArrayList<String>();
		
		// 查询条件
		
		for(int j=0;j<=num;j++){
			if(sz!=null&&re.getStr("cxtj"+j)!=null&&arr[j]!=null&&!" ".equals(arr[j])){
				//查询条件不为空
				if("jgcxtj".equals(re.getStr("cxtj"+j))){
					if(arr[j] != null){
						sbf.append("'"+arr[j]+"',");
					}
				}else if(re.getStr("sfmhpp"+j)!=null&&"1".equals(re.getStr("sfmhpp"+j))){
					whereSql.append(" and "+re.getStr("cxtj"+j)+" like '%"+arr[j]+"%' ");
				}else if("startTime".equals(re.getStr("cxtj"+j))){
					whereSql.append(" and substr(cjsj,0,10) >= ? ");
					sqlStr.add(arr[j].substring(0,10));
				}else if("endTime".equals(re.getStr("cxtj"+j))){
					whereSql.append(" and substr(cjsj,0,10) <= ? ");
					sqlStr.add(arr[j].substring(0,10));
				}else{
					String cs = arr[j];
					if(cs.contains("00:00:00")){
						String datestr = cs.substring(0, 10);
						boolean flags = DateTimeUtil.isDate(datestr);
						if(flags==true){
							whereSql.append(" and "+re.getStr("cxtj"+j)+" like '%"+datestr+"%' ");
						}
					}else{
						whereSql.append(" and "+re.getStr("cxtj"+j)+" = ? ");
						sqlStr.add(arr[j]);
					}
				}
			}
			if(j == num-1){
				if(!"".equals(orgarr)&&orgarr != null){
					//whereSql.append(" and jgbh in ("+orgarr+") ");
				}
			}
		}
		whereSql.append(" and cjr != "+"'"+userno+"'");
		// 排序
		extraSql += whereSql.toString();
		// 查询
		Page<Record> r = PaginateX(pageNum, pageSize, selectSql.toString(),forech, extraSql,sqlStr.toArray());
		// 赋值
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		// 打印日志
		log.info("getList--r.getList():" + r.getList());
		log.info("getList--r.getTotalRow():" + r.getTotalRow());
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿", "3", "登记簿数据-查询");
		log.info(getLogPoint().exit("登记簿数据-查询").toString());
		// 返回json数据
		renderJson();

	}
	
	//进入单个登记簿，查询登记簿信息并在界面上拼接
	public void list(){
		//获取当前审批人角色
		String role_id = getCurrentUser().getStr("roleid");
		//根据审批角色获取上一审批人角色，如果为空，则上一审批人可以不选，如果不为空，则上一审批人必须要选
		Record list = Db.findFirst(" select t.* from t_djb_lcpz t left join t_djb_lcpz p on t.current_operator = p.next_operator_group where t.current_operator = '"+role_id+"' and t.djbid = '"+getPara("djbid")+"'");
		if(list != null){
			setAttr("flag","1");
		}else{
			setAttr("flag","0");
		}
		setAttr("djbid",getPara("djbid"));
		setAttr("status",getPara("status"));
		render("TDjbHandlerList.jsp");
	}
	
	//登记簿表单新增展示页
	public void addDetail(){
		String djbid = getPara("djbid");
		String id = AppUtils.getStringSeq();
		setAttr("djbid",djbid);
		setAttr("id",id);
		render("TDjbHandlerForm.jsp");
	}
	
	public void addDetail1(){
		String djbid = getPara("djbid");
		//获取字段
		List<Record> zdList = Db.find("select t.djbid,t.ysm,t.yshy,t.zdlx,t.mblx,t.cdxz,t.zdpx from T_DJB_JDSRYSB t where t.djbid='"+djbid+"' order by t.zdpx ");
		setAttr("zdlist",zdList);
		setAttr("zdlisttotal",total(zdList));
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "获取所有字段", "3", "获取所有字段");
		log.info(getLogPoint().exit("获取所有字段").toString());
		renderJson();
	}
	
	//登记簿表单编辑展示页
	public void editDetail(){
		String djbid = getPara("djbid");
		String id = getPara("id");
		String pageType = getPara("pageType");
		Record fjidRe = Db.findFirst(" select fjid from t_djb_apply where sjid = '"+id+"' and status = 2 ");
		if(fjidRe != null){
			String fjid = fjidRe.getStr("fjid");
			setAttr("fjid",fjid);
		}
		setAttr("djbid",djbid);
		setAttr("id",id);
		setAttr("pageType",pageType);
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿表单编辑展示页", "3", "登记簿表单编辑展示页");
		log.info(getLogPoint().exit("登记簿表单编辑展示页").toString());
		render("editDetail.jsp");
	}
	
	public void approves(){
		String djbid = getPara("djbid");
		String id = getPara("id");
		//获取当前审批人角色
		String role_id = getCurrentUser().getStr("roleid");
		//获取上个审批人的用户号
		List<Record> sprList = Db.find("select t.approver,t.nowrole,t.ordernum from t_djb_apply t where t.djbid = '"+djbid+"' and t.sjid = '"+id+"' and t.status = '1' and t.nextapprover='"+role_id+"' order by to_number(ordernum) desc ");
		if(sprList != null && sprList.size() > 0){
			setAttr("approver",sprList.get(0).getStr("approver"));
			setAttr("nowrole",sprList.get(0).getStr("nowrole"));
		}else{
			setAttr("approver","0");
			setAttr("nowrole","0");
		}
		
		//退回附件ID
		String fjid = AppUtils.getStringSeq();
		//根据审批角色获取上一审批人角色，如果为空，则上一审批人可以不选，如果不为空，则上一审批人必须要选
		Record list = Db.findFirst(" select t.* from t_djb_lcpz t left join t_djb_lcpz p on t.current_operator = p.next_operator_group where t.current_operator = '"+role_id+"' and t.djbid = '"+djbid+"'");
		if(list != null){
			setAttr("flag","1");
		}else{
			setAttr("flag","0");
		}
		setAttr("djbid",djbid);
		setAttr("id",id);
		setAttr("fjid",fjid);
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿表单提交展示页", "3", "登记簿表单提交展示页");
		log.info(getLogPoint().exit("登记簿表单提交展示页").toString());
		render("approve.jsp");
	}
	
	public void getApprover(){
		List<Record> lr = YyglUtil.getUserListByRoleForDjbApply(getCurrentUser().getStr("ROLEID"), getCurrentUser().getStr("ORGNUM"),getCurrentUser().getStr("ROLE_LEVEL"),getPara("djbid"));
		setAttr("nextAppData", lr);
		renderJson();
	}
	
	
	/**
	 * 编辑页面反显数据
	 * @return
	 */
	public void getDatileData(){
		//登记簿id
		String djbid = getPara("djbid");
		//信息id
		String id = getPara("id");
		//获取登记簿名称
		String djbname = null;
		Record formRecord = Db.findFirst("select t.djbid,t.djbtab from T_DJB_RESMENU_TAB t where t.djbid= '"+djbid+"'");
		if(formRecord != null){
			djbname = formRecord.getStr("djbtab");
		}
		Record data = new Record();
		if(djbname != null){
			data = Db.findFirst(" select t.*,(select results from t_djb_apply where sjid = t.id and status = 2) as results from "+djbname + " t where t.id = '"+id+"'");
		}else{
			data = null;
		}
		setAttr("data",data);
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "编辑界面反显数据", "3", "编辑界面反显数据");
		log.info(getLogPoint().exit("编辑界面反显数据").toString());
		renderJson();
	}
	
	
	/**
	 * 新增业务数据保存方法.
	 * @param vo 业务数据
	 */
	public void save() {
		log.info("save");
		JSONObject json = getJsonObj("data");
		String djbid = json.getString("djbid");
		String id = json.getString("id");
		String org = getCurrentUser().getStr("org_id");
		//获取登记簿名称
		String djbname = null;
		Record formRecord = Db.findFirst("select t.djbid,t.djbtab from T_DJB_RESMENU_TAB t where t.djbid= '"+djbid+"'");
		if(formRecord != null){
			djbname = formRecord.getStr("djbtab");
		}
		String pageType = getPara("pageType");
		if(pageType != null){
			Db.update(" delete from "+djbname + " where id = '"+id+"'");
		}
		List<Record> zdList = Db.find("select t.djbid,t.ysm,t.yshy,t.zdlx,t.mblx,t.cdxz,t.zdpx from T_DJB_JDSRYSB t where t.djbid='"+djbid+"'");
		
		Record r_log = new Record();
		r_log.set("id", id);
		r_log.set("jgbh",org);
		r_log.set("status",3);
		for(Record r : zdList){
			r_log.set(r.getStr("ysm"), json.getString(r.getStr("ysm")));
		}
		Db.save(djbname, r_log);
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿—保存", "4", "登记簿-保存");
		log.info(getLogPoint().exit("登记簿-数据保存").toString());
		renderJson();
	}
	
	
	/**
	 * 删除选中的数据.
	 * @param ids 选中的数据列表，逗号分隔
	 */
	public void delete() {
		String id = getPara("ids");
		String djbid = getPara("djbid");
		//获取登记簿名称
		String djbname = null;
		String flag = "0";
		Record formRecord = Db.findFirst("select t.djbid,t.djbtab from T_DJB_RESMENU_TAB t where t.djbid= '"+djbid+"'");
		if(formRecord != null){
			djbname = formRecord.getStr("djbtab");
		}
		String[] arr = id.split(",");
		for(String ar : arr){
			int count = Db.update(" delete from "+djbname + " where id = '"+ar+"'");
			if(count >0){
				flag = "1";
			}
		}
		setAttr("flag",flag);
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿—删除", "6", "登记簿-删除");
		log.info(getLogPoint().exit("登记簿-数据保存").toString());
		renderJson();
	}
	
	/**
	 * 根据当前登录用户的角色获取对应维护的审批角色信息
	 */
	public void getNextOperatorList() {
		List<Record> lr = YyglUtil.getUserListByRoleForDjbApply(getCurrentUser().getStr("ROLEID"), getCurrentUser().getStr("ORGNUM"),getCurrentUser().getStr("ROLE_LEVEL"),getPara("djbid"));
		setAttr("nextAppData", lr);
		renderJson();
	}
	
	/**
	 * 
	 * @param vo 业务数据
	 */
	public void saveApprove() {
		log.info("save");
		JSONObject json = getJsonObj("data");
		String djbid = json.getString("djbid");
		String id = json.getString("id");
		//上一个审批人，上一个人审批人角色
		//String lastnowrole = json.getString("lastnowrole");
		//附件ID
		String fjid = getPara("fjid");
		
		//状态  0.已提交  1.通过
		String org = getCurrentUser().getStr("org_id");
		String user_no = getCurrentUser().getStr("USER_NO");
		String names = getCurrentUser().getStr("NAME");
		
		//获取登记簿名称
		String djbname = null;
		Record formRecord = Db.findFirst("select t.djbid,t.djbtab from T_DJB_RESMENU_TAB t where t.djbid= '"+djbid+"'");
		if(formRecord != null){
			djbname = formRecord.getStr("djbtab");
		}
		//Db.update(" delete from "+djbname + " where id = '"+id+"'");
		List<Record> zdList = Db.find("select t.djbid,t.ysm,t.yshy,t.zdlx,t.mblx,t.cdxz,t.zdpx from T_DJB_JDSRYSB t where t.djbid='"+djbid+"'");
		
		//登记簿信息保存
		Record r_log = new Record();
		r_log.set("id", id);
		r_log.set("checkid", user_no);
		r_log.set("checkname", names);
		for(Record r : zdList){
			r_log.set(r.getStr("ysm"), json.getString(r.getStr("ysm")));
		}
		Db.update(djbname, r_log);
		
		//流程流水保存
		//当前操作人姓名
		String userNo = getCurrentUser().getStr("USER_NO");
		Record userRecord = Db.findFirst(" select * from sys_user_info t where t.stat=0 and t.dele_flag = 0 and t.user_no='"+userNo+"' ");
		//当前操作人角色
		String nowRole = getCurrentUser().getStr("ROLEID");
		
		
		Record djbnames = Db.findFirst(" select djbmc from  t_djb_jcxx where id='"+djbid+"'");
		String name = "";
		if(djbnames != null){
			name = djbnames.getStr("djbmc");
		}
		int flag= 0;
		//获取ordernum
		int status = 2;
		Record orderRe = Db.findFirst(" select max(to_number(ordernum)+1) as ordernum from t_djb_apply where sjid='"+id+"' and status = 1 ");
		String ordernum = orderRe.getBigDecimal("ordernum").toString();
			//flag = Db.update("insert into t_djb_apply(id,djbid,sjid,ordernum,approver,apptime,nowrole,orgnum,status) values "
			//		+ "(?,?,?,?,?,?,?,?,?) ", new Object[]{AppUtils.getStringSeq(),djbid,id,ordernum,userNo,DateTimeUtil.getTime(),nowRole,org,status});
			//如果审核通过，登记簿数据中状态改为1，即status=1
			Db.update(" update " + djbname +" set status =1 where id = '"+id+"'");
			Db.update(" update t_djb_apply set status =2 where sjid = '"+id+"'");
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿流水—保存", "4", "登记簿-保存");
		log.info(getLogPoint().exit("登记簿流水-数据保存").toString());
		renderJson();
	}
	
    // 获取审批状态下拉框 （1.通过 2.拒绝）
    public void getShyj() {
		String sql = "select * from sys_param_info where key = '537' and status ='1' ";
		List<Record> yzcdlist = Db.find(sql);
		renderJson(yzcdlist);
    }
    
    //获取退回节点
    public void getUserApprover(){
    	//数据ID
    	String id = getPara("id");
		//获取当前最大的ordernum
		Record orderRes = Db.findFirst(" select max(to_number(ordernum)+1) as ordernum from t_djb_apply where sjid='"+id+"' and status = 1 ");
		String ordernums = orderRes.getBigDecimal("ordernum").toString();
		String sql = "select t.approver as userno ,p.name as name from t_djb_apply t left join sys_user_info p on t.approver = p.user_no  "
				+ " where t.sjid = '"+id+"' and t.ordernum < '"+ordernums+"' and p.stat=0 and p.dele_flag = 0 ";
		List<Record> yzcdlist = Db.find(sql);
		renderJson(yzcdlist);
    	
    }
    
    //跳转到选择下级审批人界面
    public void selApprove(){
    	//获取数据
    	String ids = getPara("ids");
    	String djbid = getPara("djbid");
    	setAttr("ids",ids);
    	setAttr("djbid",djbid);
    	render("massapprove.jsp");
    }
    
    //处理批量审批通过数据
    public void allApprove(){
    	//获取数据
    	String ids = getPara("ids");
    	//获取登记簿ID
    	String djbid = getPara("djbid");
    	//批量审批情况 flag=0无下一审批人，flag=1，有下一审批人
    	String flag = getPara("flag");
    	//审批结果status=1
    	String status = "1";
    	//获取当前审批人角色
    	String userno = getCurrentUser().getStr("USER_NO");
    	String[] idArr = ids.split(",");
    	for(String id : idArr){
    		//获取上个审批人的角色
    		List<Record> sprList = Db.find("select t.approver,t.nowrole,t.ordernum from t_djb_apply t where t.djbid = '"+djbid+"' and t.sjid = '"+id+"' and t.status = '1' and t.nextapprover='"+userno+"' order by to_number(ordernum) desc ");
    		if(sprList != null && sprList.size() > 0){
    			String lastnowrole = sprList.get(0).getStr("nowrole");
    			//下一审批人
    			String nextApprover = getPara("nextApprover");
    			
    			String org = getCurrentUser().getStr("org_id");
    			
    			//获取登记簿名称
    			String djbname = null;
    			Record formRecord = Db.findFirst("select t.djbid,t.djbtab from T_DJB_RESMENU_TAB t where t.djbid= '"+djbid+"'");
    			if(formRecord != null){
    				djbname = formRecord.getStr("djbtab");
    			}
    			
    			//流程流水保存
    			//当前操作人姓名
    			String userNo = getCurrentUser().getStr("USER_NO");
    			Record userRecord = Db.findFirst(" select * from sys_user_info t where t.stat=0 and t.dele_flag = 0 and t.user_no='"+userNo+"' ");
    			String username = userRecord.getStr("name");
    			//当前操作人角色
    			String nowRole = getCurrentUser().getStr("ROLEID");
    			
    			Record djbnames = Db.findFirst(" select djbmc from  t_djb_jcxx where id='"+djbid+"'");
    			String name = "";
    			if(djbnames != null){
    				name = djbnames.getStr("djbmc");
    			}
    			Record orderRe = Db.findFirst(" select max(to_number(ordernum)+1) as ordernum from t_djb_apply where sjid='"+id+"' and status = 1 ");
    			String ordernum = orderRe.getBigDecimal("ordernum").toString();
    			if("0".equals(flag)){
    				Db.update("insert into t_djb_apply(id,djbid,sjid,ordernum,approver,apptime,nowrole,orgnum,status) values "
    						+ "(?,?,?,?,?,?,?,?,?) ", new Object[]{AppUtils.getStringSeq(),djbid,id,ordernum,userNo,DateTimeUtil.getTime(),nowRole,org,status});
    				//如果审核通过，登记簿数据中状态改为1，即status=1
    				Db.update(" update " + djbname +" set status =1 where id = '"+id+"'");
    			}else{
    				//下一审批人信息
    				Record re = Db.findFirst(" select name,role_id  from sys_user_info where stat = 0 and dele_flag = 0 and user_no = '"+nextApprover+"' ");
    				//下一审批人角色对应字段
    				List<Record> zdmcRe = Db.find(" select zdmc from t_djb_lcpz t where t.djbid = '"+djbid+"'  and instr(t.current_operator,'"+lastnowrole+"') >0 ");
    				String zdmc = "";
    				if(zdmcRe != null && zdmcRe.size() > 0){
    					zdmc = zdmcRe.get(0).getStr("zdmc");
    				}
    				Db.update("insert into t_djb_apply(id,djbid,sjid,ordernum,approver,nowrole,orgnum,status,nextapprover,nextapprovername,results,apptime,nextzdmc) values "
    					+ "(?,?,?,?,?,?,?,?,?,?,?,?,?) ", new Object[]{AppUtils.getStringSeq(),djbid,id,ordernum,userNo,nowRole,org,status,nextApprover,re.getStr("name"),"",DateTimeUtil.getTime(),zdmc});
    					
    				//通过流程短息提醒
    				if(AppUtils.StringUtil(nextApprover) != null){
    					Record re2 = Db.findFirst(" select name,role_id,mobile  from sys_user_info where stat = 0 and dele_flag = 0 and user_no = '"+nextApprover+"' ");
    					if(re2 != null && re2.getStr("mobile") != null){
    						MessageSender.sendMessage(re2.getStr("mobile").trim(), YyglUtil.APPLY_MESSAGE_13+"(登记簿名称："+name+")");
    					}
    				}
    			}
    			
    			//保存审核人，根据当前登录人的角色来判断他是属于哪个审批流程的，进而获取对应审批字段
    			List<Record> roleRecord = Db.find("select zdmc from t_djb_lcpz t where t.djbid = '"+djbid+"' and instr(t.next_operator_group,'"+nowRole+"')>0");
    			//如果审核通过，删除所有退回的流程
    			//删除所有退回流程
    			Db.update(" delete from t_djb_apply where sjid = '"+id+"' and status = 2 ");
    			//保存审批人到登记簿信息
    			if(roleRecord != null && roleRecord.size() > 0){
    				//登记簿信息保存
    				Record r_logs = new Record();
    				r_logs.set("id", id);
    				r_logs.set(roleRecord.get(0).getStr("zdmc"),username);
    				Db.update(djbname, r_logs);
    			}
    		}
    	}
    	renderJson();
    }

    
    /**
	 * 自定义分页后进行字段值关联转换
	 * 
	 * 关联表别名：table_alias
	 * @param pageNumber
	 * @param pageSize
	 * @param select
	 * @param forech
	 * @param sqlExceptSelect
	 * @param paras
	 * @return
	 */
	public Page<Record> PaginateX( int pageNumber, int pageSize, String select,String forech, String sqlExceptSelect, Object... paras){
		if (pageNumber < 1 || pageSize < 1)
			throw new ActiveRecordException("pageNumber and pageSize must be more than 0");
		long totalRow = 0;
		int totalPage = 0;
		StringBuilder sql = new StringBuilder();
		DbKit.getConfig().getDialect().forPaginate(sql, pageNumber, pageSize, select, sqlExceptSelect);
		if(forech!=null&&!forech.isEmpty()){
			sql=sql.replace(sql.indexOf("*"),sql.indexOf("*")+1,"table_alias.*,"+ forech);
		}
		List<Record> list = Db.find(sql.toString(), paras);
		BigDecimal count = Db.queryBigDecimal("select count(*)  "+DbKit.replaceFormatSqlOrderBy(sqlExceptSelect), paras);
		totalRow=count.longValue();
		totalPage = (int) (totalRow / pageSize);
		if (totalRow % pageSize != 0) {
			totalPage++;
		}
		return  new Page<Record>(list, pageNumber, pageSize, totalPage, (int)totalRow);
	}
}
