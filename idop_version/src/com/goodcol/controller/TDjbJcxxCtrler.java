package com.goodcol.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import com.goodcol.core.aop.Before;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.core.plugin.activerecord.tx.Tx;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.Constant;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.ObjectTable;
import com.goodcol.util.OrgUtil;
import com.goodcol.util.YyglUtil;
import com.goodcol.util.jfinal.anatation.RouteBind;
import com.goodcol.util.log.Logger;
import com.goodcol.util.log.LoggerConsole;

@RouteBind(path = "/djb")
@Before({ ManagerPowerInterceptor.class })
public class TDjbJcxxCtrler  extends BaseCtl  {
	
	/**
	 * 日志记录类.
	 */
	public LoggerConsole log = Logger.getLogger(); // 记录日志用

	/**
	 * 新增业务数据保存方法.
	 * @param vo 业务数据
	 */
	public void save() {
		//获取当前登录人姓名
		String cjr = getCurrentUser().getStr("NAME");
		//获取当前登录人机构
		String cjjg = getCurrentUser().getStr("ORG_ID");
    	Record orgRecord = Db.findFirst(" select t.orgnum,t.by2  from sys_org_info t where t.orgnum = '"+cjjg+"' and t.stat='1'   ");
    	String org = null;
    	String by2 = orgRecord.getStr("by2");
    	if(orgRecord != null){
    		if("0".equals(by2)){
    			org = Constant.HEAD_OFFICE;
    		}else if("1".equals(by2)){
    			org = orgRecord.getStr("orgnum");
    		}else{
    			org = orgRecord.getStr("orgnum");
    		}
    	}
		JSONObject json = getJsonObj("data");
		String djbmc = json.getString("djbmc");
		String orgid = json.getString("orgid");
		String sfpz = json.getString("sfpz");
		//String sfpz = json.getString("sfpz");
		String cjrq = DateTimeUtil.getDqrqStr();
		Record r_log = new Record();
		String id = AppUtils.getStringSeq();
		r_log.set("ID", id)
		.set("CJR", cjr)
		.set("DJBMC", djbmc)
		.set("CJJG", org)
		.set("ORGID",orgid)
		.set("useFlag","0")
		.set("CJRQ", cjrq).set("SFPZ", sfpz);
		Db.save("t_djb_jcxx", r_log);
		/*//判断登记簿是否需要配置流程，如果不需要，直接将当前操作角色保存
		if(sfpz != null && "0".equals(sfpz)){
			//适用角色
			String role = json.getString("role");
			
		}*/
		String[] orgids = orgid.split(",");
		//判断当前登录人是否为总行人员，如果是，先判断所选机构中是否有总行机构号，如果有，则保存所有机构号；如果没有，根据当前机构来查询所管辖的所有机构
		if("0".equals(by2)){
			//如果包括总行
			if(orgid.contains(Constant.HEAD_OFFICE)){
				List<Record> orgRes = Db.find("select orgnum from sys_org_info where stat = '1' start with ORGNUM = '"+Constant.HEAD_OFFICE+"' connect by prior ORGNUM = upid");
				for(Record orgRe : orgRes){
					Record p_log = new Record();
					p_log.set("ID", AppUtils.getStringSeq())
					.set("DJBID", id)
					.set("ORGID", orgRe.getStr("orgnum"));
					Db.save("t_djb_org", p_log);
				}
			}else{
			//若不包括总行
				for(int i=0;i<orgids.length;i++){
					//如果是总行运管部
					if(Constant.HEAD_YGBOFFICE.equals(orgids[i])){
						List<Record> orgRes = Db.find("select orgnum from sys_org_info where stat = '1' and (upid = '"+orgids[i]+"' or orgnum = '"+orgids[i]+"')");
						for(Record orgRe : orgRes){
							Record p_log = new Record();
							p_log.set("ID", AppUtils.getStringSeq())
							.set("DJBID", id)
							.set("ORGID", orgRe.getStr("orgnum"));
							Db.save("t_djb_org", p_log);
						}
					}else{
						List<Record> orgRes = Db.find("select orgnum from sys_org_info where stat = '1' and ssfh = '"+orgids[i]+"'");
						for(Record orgRe : orgRes){
							Record p_log = new Record();
							p_log.set("ID", AppUtils.getStringSeq())
							.set("DJBID", id)
							.set("ORGID", orgRe.getStr("orgnum"));
							Db.save("t_djb_org", p_log);
						}
					}
				}
			}
		}else{
			//当前登录人非总行人员
			for(int i=0;i<orgids.length;i++){
				Record p_log = new Record();
				p_log.set("ID", AppUtils.getStringSeq())
				.set("DJBID", id)
				.set("ORGID", orgids[i]);
				Db.save("t_djb_org", p_log);
			}
		}
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿", "4", "登记簿-保存");
		log.info(getLogPoint().exit("登记簿-保存").toString());
		renderJson();
	}
	
	/**
	 * 展现流程配置详情页面
	 */
	public void lcpzDetail() {
		String djbid = getPara("djbid");
		setAttr("djbid",djbid);
		render("TDjbLcpzForm.jsp");
	}
	
	//是否配置流程
	public void getSfpzList(){
		List<Record> list = Db.find(" select val,remark from sys_param_info where key = '528' and status = '1' ");
		renderJson(list);
	}
	
	/**
	 * 删除选中的数据.
	 * @param ids 选中的数据列表，逗号分隔
	 */
	public void delete() {
		String ids = getPara("ids");
		String[] arr = ids.split(",");
		int flag = 0;
		String remark = "删除失败！";
		//逐条删除数据
		for(int i=0;i<arr.length;i++){
			//删除登记簿
			String delSql = "delete from T_DJB_JCXX where ID ='"+arr[i]+"'";
			flag = Db.update(delSql);
			//删除登记簿对应机构
			String delSql1 = "delete from t_djb_org where DJBID ='"+arr[i]+"'";
			Db.update(delSql1);
			//删除对应生成的表结构
			Record formRe = Db.findFirst("select t.djbtab from t_djb_resmenu_tab t where t.djbid= '"+arr[i]+"'");
			if(formRe != null){
				Db.update("drop table "+formRe.getStr("DJBTAB"));
			}
			//删除登记簿对应的字段配置
			Db.update("delete from t_djb_jdsrysb where djbid ='"+arr[i]+"'");
			//删除登记簿对应的结果配置
			Db.update("delete from t_djb_lbzs_jgpz where djbid ='"+arr[i]+"'");
			//删除登记簿对应的查询条件配置
			Db.update("delete from t_djb_lbzs_cxtj where djbid ='"+arr[i]+"'");
			//删除登记簿对应的流程配置
			Db.update("delete from t_djb_lcpz where djbid ='"+arr[i]+"'");
			//删除登记簿对应的表名对应关系
			Db.update("delete from t_djb_resmenu_tab where djbid ='"+arr[i]+"'");
		}
		if(flag>0){
			remark = "删除成功！";
		}
		setAttr("remark", remark);
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿", "6", "登记簿-删除");
		log.info(getLogPoint().exit("登记簿-删除").toString());
		renderJson();
	}
	
	/**
	 * 获取机构列表  总行登录，获取总行运管部以及各分行；分行登录获取分行及其所管辖机构；支行获取本机构
	 */
	public void getListByUser() {
		List<Record> list = null;
		// 获取当前用户信息
		String userNo = getCurrentUser().getStr("USER_NO");

		List<Record> lr = Db
				.find("select ri.name,ri.role_level,ui.org_id from sys_role_info ri, sys_user_info ui where ui.role_id=ri.id and ui.user_no='"
						+ userNo + "' and ui.stat='0' and (ui.dele_flag='0' or ui.dele_flag is null) and ri.ROLE_DELE_FLAG='1'");
		//String roleName = lr.get(0).getStr("NAME");
		String role_level = lr.get(0).getStr("role_level");
		String org_id = lr.get(0).getStr("org_id");

		StringBuffer whereSql = new StringBuffer();
		whereSql.append(" select ORGNAME,ID,UPID,ORGNUM,ORDERNUM,LINKMAN,PHONENUM,BY2,BY3,ORGADRESS from sys_org_info where stat = '1' ");

		if (AppUtils.StringUtil(role_level) == null || role_level.equals("0")
				|| role_level.equals("1")) {
			// 柜员和支行只取自己的机构号
			whereSql.append(" and orgnum in ("
					+ OrgUtil.getIntanceof().getZhOrgId(org_id) + ") ");
		} else if (role_level.equals("2")) {
			// 分行查询机构号相等或下级
			whereSql.append(" and orgnum in ( "
					+ OrgUtil.getIntanceof().getFhOrgId(org_id) + " ) ");
		} else if (role_level.equals("3")) {
			// 总行和系统管理员，总行运管部以及各分行
			whereSql.append(" and (orgnum='"+Constant.HEAD_OFFICE+"' or upid='"+Constant.HEAD_OFFICE+"' ) ");
		}
		whereSql.append(" order by orgnum Asc ");
		String sql = whereSql.toString();
		list = Db.find(sql);
		setAttr("datas", list);
		// 打印日志
		log.debug("getListById--datas:" + list);
		renderJson();
	}

	/**
	 * 修改登记簿数据.
	 * @param vo 修改后的业务数据
	 */
	public void update() {
		log.info("update");
		//获取当前登录人机构
		String cjjg = getCurrentUser().getStr("ORG_ID");
    	Record orgRecord = Db.findFirst(" select t.orgnum,t.by2  from sys_org_info t where t.orgnum = '"+cjjg+"' and t.stat='1'   ");
    	String by2 = orgRecord.getStr("by2");
		String djbmc = getPara("djbmc");
		String orgid = getPara("orgid");
		String sfpz = getPara("sfpz");
		/*String role = getPara("role");*/
		String id = getPara("id");
		int j = Db.update("update T_DJB_JCXX set DJBMC=?,ORGID=?,SFPZ=? where ID = ?",djbmc,orgid,sfpz,id);
		String[] orgids = orgid.split(",");
		Db.update("delete from t_djb_org where djbid = '"+id+"'");
		//判断当前登录人是否为总行人员，如果是，先判断所选机构中是否有总行机构号，如果有，则保存所有机构号；如果没有，根据当前机构来查询所管辖的所有机构
		if("0".equals(by2)){
			//如果包括总行
			if(orgid.contains(Constant.HEAD_OFFICE)){
				List<Record> orgRes = Db.find("select orgnum from sys_org_info where stat = '1' start with ORGNUM = '"+Constant.HEAD_OFFICE+"' connect by prior ORGNUM = upid");
				for(Record orgRe : orgRes){
					Record p_log = new Record();
					p_log.set("ID", AppUtils.getStringSeq())
					.set("DJBID", id)
					.set("ORGID", orgRe.getStr("orgnum"));
					Db.save("t_djb_org", p_log);
				}
			}else{
			//若不包括总行
				for(int i=0;i<orgids.length;i++){
					//如果是总行运管部
					if(Constant.HEAD_YGBOFFICE.equals(orgids[i])){
						List<Record> orgRes = Db.find("select orgnum from sys_org_info where stat = '1' and (upid = '"+orgids[i]+"' or orgnum = '"+orgids[i]+"')");
						for(Record orgRe : orgRes){
							Record p_log = new Record();
							p_log.set("ID", AppUtils.getStringSeq())
							.set("DJBID", id)
							.set("ORGID", orgRe.getStr("orgnum"));
							Db.save("t_djb_org", p_log);
						}
					}else{
						List<Record> orgRes = Db.find("select orgnum from sys_org_info where stat = '1' and ssfh = '"+orgids[i]+"'");
						for(Record orgRe : orgRes){
							Record p_log = new Record();
							p_log.set("ID", AppUtils.getStringSeq())
							.set("DJBID", id)
							.set("ORGID", orgRe.getStr("orgnum"));
							Db.save("t_djb_org", p_log);
						}
					}
				}
			}
		}else{
			//当前登录人非总行人员
			for(int i=0;i<orgids.length;i++){
				Record p_log = new Record();
				p_log.set("ID", AppUtils.getStringSeq())
				.set("DJBID", id)
				.set("ORGID", orgids[i]);
				Db.save("t_djb_org", p_log);
			}
		}
		setAttr("flag",j);
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿", "5", "登记簿-修改");
		log.info(getLogPoint().exit("登记簿-修改").toString());
		renderJson();
	}

	/**
	 * 根据页面输入查询条件，分页查询数据.
	 */
	public void query() {
		String orgid = getPara("orgid");
		// 获取当前用户信息
		String orgId = getCurrentUser().getStr("ORG_ID");
		//根据当前用户机构获取所属分行机构
    	Record orgRecord = Db.findFirst(" select t.orgnum,t.by2  from sys_org_info t where t.orgnum = '"+orgId+"' and t.stat='1'   ");
    	String org = null;
    	String by2 = orgRecord.getStr("by2");
    	if(orgRecord != null){
    		if("0".equals(by2)){
    			org = Constant.HEAD_OFFICE;
    		}else if("1".equals(by2)){
    			org = orgRecord.getStr("orgnum");
    		}else{
    			org = orgRecord.getStr("orgnum");
    		}
    	}
		// 获取查询条件
		String djbmc = getPara("djbmc");//登记簿名称
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		// 查询语句
		String selectSql = " select FLAG,SFPZ,ID,DJBMC,CJR,CJRQ,CJJG,orgid,useflag,startusetime,endusetime,(SELECT ORGNAME FROM SYS_ORG_INFO WHERE STAT=1 AND ORGNUM=CJJG) AS ORGNAME,(SELECT ORGNAME FROM SYS_ORG_INFO WHERE STAT=1 AND ORGNUM=orgid) AS ORG_NAME ";
		String extraSql = " from T_DJB_JCXX ";
				
		StringBuffer whereSql = new StringBuffer(" where 1=1 and CJJG in (select orgnum from sys_org_info where stat = '1' start with ORGNUM = '"+org+"' connect by prior ORGNUM = upid)  ");
		List<String> sqlStr = new ArrayList<String>();
		// 查询条件
		if (AppUtils.StringUtil(djbmc) != null) {
			whereSql.append(" and DJBMC like ? ");
			sqlStr.add("%"+djbmc+"%");
		}
		if (AppUtils.StringUtil(orgid) != null) {
			whereSql.append(" and orgid = ? ");
			sqlStr.add(orgid);
		}
		// 排序
		whereSql.append(" order by CJRQ DESC");
		extraSql += whereSql.toString();
		// 查询
		Page<Record> r = Db.paginate(pageNum, pageSize, selectSql, extraSql, sqlStr.toArray());
		// 赋值
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		// 打印日志
		
		log.info("getList--r.getList():" + r.getList());
		log.info("getList--r.getTotalRow():" + r.getTotalRow());
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿", "3", "登记簿-查询");
		log.info(getLogPoint().exit("登记簿-查询").toString());
		// 返回json数据
		renderJson();
	}

	/**
	 * 功能模块入口方法，返回功能模块主界面
	 * @return 功能模块页面
	 */
	public void list() {
		render("TDjbJcxxList.jsp");
	}

	/**
	 * 展现业务信息详情页面
	 * @return 业务信息详情编辑页面
	 */
	public void detail() {
		initDict("500001,500002", null);
		render("TDjbJcxxForm.jsp");
	}

	/**
	 * 根据主键，获取主键对应详细数据
	 * @param id 业务主键
	 * @return 业务主键对应业务详细数据
	 */
	public void getTDjbJcxxVOById() {
		String id = getPara("id");
		//获取当前登录人机构
		String cjjg = getCurrentUser().getStr("ORG_ID");
    	Record orgRecord = Db.findFirst(" select t.orgnum,t.by2  from sys_org_info t where t.orgnum = '"+cjjg+"' and t.stat='1'   ");
    	String by2 = orgRecord.getStr("by2");
		String sql ="select ID,DJBMC,CJR,CJRQ,SFPZ from T_DJB_JCXX where ID = '"+id+"'";
		Record re = Db.findFirst(sql);
		List<Record> list =new ArrayList<Record>();
		if("0".equals(by2)){
			list = Db.find(" select orgid from t_djb_org where djbid = '"+id+"' and orgid in (select orgnum from sys_org_info t where stat=1 and (t.orgnum='"+Constant.HEAD_OFFICE+"' or t.upid='"+Constant.HEAD_OFFICE+"' ))");
		}else{
			list = Db.find(" select orgid from t_djb_org where djbid = '"+id+"' ");
		}
		StringBuffer orgid = new StringBuffer("");
		String orgnum = "";
		for(Record res : list){
			orgid.append(res.getStr("orgid")+",");
		}
		if(AppUtils.StringUtil(orgid.toString()) != null){
			orgnum = orgid.substring(0, orgid.length()-1);
		}
		re.set("ORGID", orgnum);
		setAttr("record",re);
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿", "3", "登记簿-详情");
		log.info(getLogPoint().exit("登记簿-详情").toString());
		renderJson();
	}
	

	/*******************列表展示页配置代码*开始*******************************/
	
	/**
	 * 展现列表展示配置详情页面
	 * @return 列表展示配置详情编辑页面
	 */
	public void lbzspzDetail() {
		getPara();
		render("TDjbLbzsForm.jsp");
	}
	
	/**
	 * 展现列表展示配置详情页面
	 * 
	 */
	public void findLbzspz() {
		String djbid = getPara("djbid");
		log.info("list");
		Record record = new Record();
		List<Record> re= Db.find("select ID,DJBID,DJBTAB,MENUID,CREATETIME,CREATEUSER from T_DJB_RESMENU_TAB"
				+ " where 1=1 and DJBID = '"+djbid+"'");
		if(re != null && re.size() > 0){
			record = re.get(0);
			// 根据登记簿ID获取参数列表链表数据
			List<Record> jgpzMap = Db.find("select t.id as jgpzid,t.zdm,t.xsxs,t.kd,t.sfmhpp,t.sfxs,j.* from  t_djb_lbzs_jgpz t left join t_djb_jdsrysb j on t.djbid = j.djbid and t.zdm = j.ysm "+   
						"where 1=1  and t.DJBID = '"+record.getStr("djbid")+"' order by t.xsxs");
			// 根据登记簿ID获取查询条件链表数据(除查询机构外)
			List<Record> cxtjMap = Db.find("select t.id as cxtjid,t.bhqz,t.sffhbm,t.sfrq,t.sfmhpp,j.* from  t_djb_lbzs_cxtj t left join t_djb_jdsrysb j on t.djbid = j.djbid and t.bhqz = j.ysm "  
						+"where 1=1  and t.sffhbm ='0'  and t.DJBID = '"+record.getStr("djbid")+"'  order by j.ysm");
			//根据登记簿ID获取是否配置了查询机构
			List<Record> cxjg = Db.find("select t.id as cxtjid,t.bhqz,t.sffhbm,t.sfrq,t.sfmhpp,j.* from  t_djb_lbzs_cxtj t left join t_djb_jdsrysb j on t.djbid = j.djbid and t.bhqz = j.ysm "  
					+"where 1=1  and t.sffhbm ='1'   and t.DJBID = '"+record.getStr("djbid")+"'  order by j.ysm");
			//根据登记簿ID获取是否配置了查询起始时间
			List<Record> cxsjstart = Db.find("select t.id as cxtjid,t.bhqz,t.sffhbm,t.sfrq,t.sfmhpp,j.* from  t_djb_lbzs_cxtj t left join t_djb_jdsrysb j on t.djbid = j.djbid and t.bhqz = j.ysm "  
					+"where 1=1  and t.sffhbm ='2'   and t.DJBID = '"+record.getStr("djbid")+"'  order by j.ysm");
			//根据登记簿ID获取是否配置了查询起始时间
			List<Record> cxsjend = Db.find("select t.id as cxtjid,t.bhqz,t.sffhbm,t.sfrq,t.sfmhpp,j.* from  t_djb_lbzs_cxtj t left join t_djb_jdsrysb j on t.djbid = j.djbid and t.bhqz = j.ysm "  
					+"where 1=1  and t.sffhbm ='3'   and t.DJBID = '"+record.getStr("djbid")+"'  order by j.ysm");
			// 组合码表翻译数据
			Record dictMaps = new Record();
			if (jgpzMap.size() > 0) {
				for (Record map : jgpzMap) {
					if (map.getStr("ZDLX") != null && "1".equals(map.getStr("ZDLX"))) {
						dictMaps.set(map.getStr("YSM").toString(), map.getStr("MBLX"));
					}
				}
			}

			setAttr("jgpzdata",jgpzMap);
			setAttr("jgpztotal",total(jgpzMap));
			setAttr("cxtjdata",cxtjMap);
			setAttr("cxtjtotal",total(cxtjMap));
			if(cxjg != null && cxjg.size() > 0){
				setAttr("cxjg",cxjg.size());
			}else{
				setAttr("cxjg",0);
			}
			if(cxsjstart != null && cxsjstart.size() > 0){
				setAttr("cxsjstart",cxsjstart.size());
			}else{
				setAttr("cxsjstart",0);
			}
			if(cxsjend != null && cxsjend.size() > 0){
				setAttr("cxsjend",cxsjend.size());
			}else{
				setAttr("cxsjend",0);
			}
		}
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "列表展示页配置", "3", "列表展示页配置-获取详情");
		log.info(getLogPoint().exit("列表展示页配置-获取详情").toString());
		renderJson();
	}
	
	private int total(List<Record> list) {
		return list == null ? 0 : list.size();
	}

	/**
	 * 新增列表展示配置保存方法.
	 * @param vo 业务数据
	 */
	public void saveLbzspz() {
		log.info("save lcpz");
		JSONObject json = getJsonObj("data");
		//查询条件配置的个数
		int cxtjNum =0;
		if(json.getString("cxtjNum") != null && !"".equals(json.getString("cxtjNum")) ){
			cxtjNum = json.getInt("cxtjNum");
		}
		//结果字段的个数
		int jgpzNum = json.getInt("jgpzNum");
		//删除结果配置
		String delJgpz = "delete from T_DJB_LBZS_JGPZ where DJBID ='"+json.getString("djbid")+"'";
		//删除查询条件
		String delCxtj = "delete from T_DJB_LBZS_CXTJ where DJBID ='"+json.getString("djbid")+"'";
		Db.update(delCxtj);
		Db.update(delJgpz);
		
		//条件配置数据拼接
		List<Record> cxtjRecord1 = new ArrayList<Record>();
		for (int i=1;i<=cxtjNum;i++) {
			if(json.containsKey("findPrefix")&&json.containsKey(json.getString("findPrefix")+i)){
				
				Record cxtjRecord = new Record();
				cxtjRecord.set("sfmhpp", json.getString(json.getString("findPrefix")+i));
				cxtjRecord.set("bhqz", json.getString("node_find_bhqz_"+i));
				cxtjRecord1.add(cxtjRecord);
			}
		}
		
		//结果配置数据拼接
		List<Record> jgpzRecord1 = new ArrayList<Record>();
		for (int i=0;i<=jgpzNum;i++) {
			if(json.containsKey("listPrefix")&&json.containsKey(json.getString("listPrefix")+i)){
				Record jgpzRecord = new Record();
				jgpzRecord.set("xsxs", json.getString(json.getString("listPrefix")+i));
				jgpzRecord.set("kd", json.getString("node_list_kd_"+i));
				jgpzRecord.set("zdm", json.getString("node_list_zdm_"+i));
				jgpzRecord.set("sfxs", json.getString("node_list_sfxs_"+i));
				jgpzRecord1.add(jgpzRecord);
			}
		}
		
		if("true".equals(json.getString("sffhbm"))){
			//组合机构条件数据
			Record r_log = new Record();
			r_log.set("ID", AppUtils.getStringSeq())
			.set("DJBID", json.getString("djbid"))
			.set("BHQZ", "jgcxtj")
			.set("SFFHBM", "1")
			.set("SFMHPP", "0")
			.set("SFRQ", "0");
			Db.save("t_djb_lbzs_cxtj", r_log);
		}
		if("true".equals(json.getString("startTime"))){
			//组合机构条件数据
			Record r_log = new Record();
			r_log.set("ID", AppUtils.getStringSeq())
			.set("DJBID", json.getString("djbid"))
			.set("BHQZ", "startTime")
			.set("SFFHBM", "2")
			.set("SFMHPP", "0")
			.set("ISSTARTTIME", "1");
			
			Db.save("t_djb_lbzs_cxtj", r_log);
		}
		if("true".equals(json.getString("endTime"))){
			//组合机构条件数据
			Record r_log = new Record();
			r_log.set("ID", AppUtils.getStringSeq())
			.set("DJBID", json.getString("djbid"))
			.set("BHQZ", "endTime")
			.set("SFMHPP", "0")
			.set("SFFHBM", "3")
			.set("ISENDTIME", "1");
			Db.save("t_djb_lbzs_cxtj", r_log);
		}
		
		//动态添加的查询条件参数组合
		for (Record r : cxtjRecord1) {
			Record r_log = new Record();
			r_log.set("ID", AppUtils.getStringSeq())
			.set("DJBID", json.getString("djbid"))
			.set("BHQZ", r.get("bhqz"))
			.set("SFFHBM", "0")
			.set("SFMHPP", "true".equals(r.get("sfmhpp"))?"1":"0");
			Db.save("t_djb_lbzs_cxtj", r_log);
		}
		
		//反显的结果列表数据组合
		for (Record r : jgpzRecord1) {
			Record r_log = new Record();
			r_log.set("ID", AppUtils.getStringSeq())
			.set("DJBID", json.getString("djbid"))
			.set("KD", r.get("kd"))
			.set("SFXS", "true".equals(r.get("sfxs")))
			.set("XSXS",r.get("xsxs"))
			.set("SFMHPP", "0")
			.set("ZDM", r.get("zdm"));
			Db.save("t_djb_lbzs_jgpz", r_log);
		}
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "列表展示页配置", "4", "列表展示页配置-新增");
		log.info(getLogPoint().exit("列表展示页配置-新增").toString());
		renderJson();
	}

	@Override
	public void index() {
		initDict("500001,500002", null);
		String userNo = getCurrentUser().getStr("USER_NO");
		List<Record> lr = Db
				.find("select ri.name,ri.role_level,ui.org_id from sys_role_info ri, sys_user_info ui where ui.role_id=ri.id and ui.user_no='"
						+ userNo + "'");
		String role_level = lr.get(0).getStr("role_level");
		String name = lr.get(0).getStr("name");

		setAttr("role_level",role_level);
		setAttr("name",name);

		render("index.jsp");
	}
	
	
	/*******************列表展示页配置代码*结束*******************************/
	
	/*******************要素配置代码*开始*******************************/
	public void updateuse(){
		String id = getPara("id");
		SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMdd");
		String sql = "update t_djb_jcxx set useFlag = '1',startusetime="+sdf.format(new Date())+",endusetime = null where id = '"+id+"'";
		String remark = "";
		try{
			Db.update(sql);
			remark = "启用成功";
		}catch(Exception e){
			remark = "启用失败";
			e.printStackTrace();
		}
		setAttr("remark", remark);
		renderJson();
	}
	public void updatenouse(){
		SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMdd");

		String id = getPara("id");
		String sql = "update t_djb_jcxx set useFlag = '0',endusetime="+sdf.format(new Date())+" where id = '"+id+"'";
		String remark = "";
		try{
			Db.update(sql);
			remark = "停用成功";
		}catch(Exception e){
			remark = "停用失败";
			e.printStackTrace();
		}
		setAttr("remark", remark);
		renderJson();
	}
	
	/**
	 * 保存配置的字段
	 */
	public void saveSyb() {
		log.info("save");
		JSONObject json = getJsonObj("data");
		// 主键
		String djbid = json.getString("djbid");
		int node = Integer.parseInt(getPara("node"));
			//在创建登记簿要素数据表之前需要先删除掉他，通过T_DJB_RESMENU_TAB找到DJBTAB,在创建,删除表操作

			List<Record> tableNameList = Db.find("select DJBTAB from T_DJB_RESMENU_TAB where 1=1 and DJBID in ('"+json.getString("djbid")+"')");
			if(tableNameList != null && tableNameList.size() > 0){
				Record dropTableName = tableNameList.get(0);
				Db.update("drop table "+dropTableName.getStr("DJBTAB"));
				Db.update("delete from T_DJB_RESMENU_TAB where DJBTAB = '"+dropTableName.getStr("DJBTAB")+"'");
				Db.update("delete from T_DJB_JDSRYSB where djbid = '"+djbid+"'");
				Db.update("delete from T_DJB_LBZS_CXTJ where djbid = '"+djbid+"'");
				Db.update("delete from T_DJB_LBZS_JGPZ where djbid = '"+djbid+"'");
			}
			
			//获取流程节点参数列表
			Record vo = null;
			
			List<Record> ObjectColumnVOs = new ArrayList<Record>();
			Record objectColumnVO = new Record();
			Record objectColumnVO1 = null;
			objectColumnVO.set("length", 200);
			objectColumnVO.set("name", "ID");
			objectColumnVO.set("type", "VARCHAR2");
			objectColumnVO.set("null", false);
			objectColumnVO.set("id", true);
			objectColumnVO.set("comment", "主键");
			ObjectColumnVOs.add(objectColumnVO);
			
			Record objectColumnVO2 = new Record();
			objectColumnVO2.set("length", 1000);
			objectColumnVO2.set("name", "jgbh");
			objectColumnVO2.set("type", "VARCHAR2");
			objectColumnVO2.set("null", true);
			objectColumnVO2.set("id", true);
			objectColumnVO2.set("comment", "机构");
			ObjectColumnVOs.add(objectColumnVO2);
			
			Record objectColumnVO3 = new Record();
			objectColumnVO3.set("length", 10);
			objectColumnVO3.set("name", "status");
			objectColumnVO3.set("type", "VARCHAR2");
			objectColumnVO3.set("null", true);
			objectColumnVO3.set("id", true);
			objectColumnVO3.set("comment", "审批状态（0.已提交 1.通过，2.未提交 3.已退回）");
			ObjectColumnVOs.add(objectColumnVO3);
			
			Record objectColumnVO4 = new Record();
			objectColumnVO4.set("length", 100);
			objectColumnVO4.set("name", "cjr");
			objectColumnVO4.set("type", "VARCHAR2");
			objectColumnVO4.set("null", true);
			objectColumnVO4.set("id", true);
			objectColumnVO4.set("comment", "创建人");
			ObjectColumnVOs.add(objectColumnVO4);
			
			Record objectColumnVO5 = new Record();
			objectColumnVO5.set("length", 30);
			objectColumnVO5.set("name", "cjsj");
			objectColumnVO5.set("type", "VARCHAR2");
			objectColumnVO5.set("null", true);
			objectColumnVO5.set("id", true);
			objectColumnVO5.set("comment", "创建时间");
			ObjectColumnVOs.add(objectColumnVO5);
			
			Record objectColumnVO6 = new Record();
			objectColumnVO6.set("length", 30);
			objectColumnVO6.set("name", "updatesj");
			objectColumnVO6.set("type", "VARCHAR2");
			objectColumnVO6.set("null", true);
			objectColumnVO6.set("id", true);
			objectColumnVO6.set("comment", "更新时间");
			ObjectColumnVOs.add(objectColumnVO6);
			
			Record objectColumnVO7 = new Record();
			objectColumnVO7.set("length", 30);
			objectColumnVO7.set("name", "updatepersonid");
			objectColumnVO7.set("type", "VARCHAR2");
			objectColumnVO7.set("null", true);
			objectColumnVO7.set("id", true);
			objectColumnVO7.set("comment", "最后更新人ID");
			ObjectColumnVOs.add(objectColumnVO7);
			
			Record objectColumnVO8 = new Record();
			objectColumnVO8.set("length", 30);
			objectColumnVO8.set("name", "updatepersonname");
			objectColumnVO8.set("type", "VARCHAR2");
			objectColumnVO8.set("null", true);
			objectColumnVO8.set("id", true);
			objectColumnVO8.set("comment", "最后更新人名称");
			ObjectColumnVOs.add(objectColumnVO8);
			
			Record objectColumnVO9 = new Record();
			objectColumnVO9.set("length", 30);
			objectColumnVO9.set("name", "checkid");
			objectColumnVO9.set("type", "VARCHAR2");
			objectColumnVO9.set("null", true);
			objectColumnVO9.set("id", true);
			objectColumnVO9.set("comment", "复核人ID");
			ObjectColumnVOs.add(objectColumnVO9);

			Record objectColumnVO10 = new Record();
			objectColumnVO10.set("length", 30);
			objectColumnVO10.set("name", "checkname");
			objectColumnVO10.set("type", "VARCHAR2");
			objectColumnVO10.set("null", true);
			objectColumnVO10.set("id", true);
			objectColumnVO10.set("comment", "复核人名称");
			ObjectColumnVOs.add(objectColumnVO10);
			
			Record objectColumnVO11 = new Record();
			objectColumnVO11.set("length", 30);
			objectColumnVO11.set("name", "registid");
			objectColumnVO11.set("type", "VARCHAR2");
			objectColumnVO11.set("null", true);
			objectColumnVO11.set("id", true);
			objectColumnVO11.set("comment", "复核人编号");
			ObjectColumnVOs.add(objectColumnVO11);
			
			Record objectColumnVO12 = new Record();
			objectColumnVO12.set("length", 30);
			objectColumnVO12.set("name", "registname");
			objectColumnVO12.set("type", "VARCHAR2");
			objectColumnVO12.set("null", true);
			objectColumnVO12.set("id", true);
			objectColumnVO12.set("comment", "复核人名称");
			ObjectColumnVOs.add(objectColumnVO12);
			
			
			
			String tableName = "";
			for(int i=1;i<=node;i++){
				if(json.containsKey("node_elem1_ysm_"+i)){
					String elem = json.getString("node_elem1_ysm_"+i);
					if(elem!=null&&!"".equals(elem)){
						vo = new Record();
						if(json.containsKey("node_elem1_cdxz_"+i)){
							if(json.getString("node_elem1_cdxz_"+i)!=null&&!"".equals(json.getString("node_elem1_cdxz_"+i))){
								vo.set("cdxz", json.getString("node_elem1_cdxz_"+i));
							}
						}
						if(json.containsKey("djbid")){
							if(json.getString("djbid")!=null){
								vo.set("djbid",json.getString("djbid")); 
								djbid = json.getString("djbid");
							}
						}
						if(json.containsKey("node_elem1_mblx_"+i)){
							if(json.getString("node_elem1_mblx_"+i)!=null&&!"".equals(json.getString("node_elem1_mblx_"+i))){
								vo.set("mblx", json.getString("node_elem1_mblx_"+i));
							}
						}
						if(json.containsKey("node_elem1_ysm_"+i)){
							if(json.getString("node_elem1_ysm_"+i)!=null&&!"".equals(json.getString("node_elem1_ysm_"+i))){
								vo.set("ysm", json.getString("node_elem1_ysm_"+i));
							}
						}
						if(json.containsKey("node_elem1_zdlx_"+i)){
							if(json.getString("node_elem1_zdlx_"+i)!=null&&!"".equals(json.getString("node_elem1_zdlx_"+i))){
								vo.set("zdlx", json.getString("node_elem1_zdlx_"+i));
							}
						}
						if(json.containsKey("node_elem1_yshy_"+i)){
							if(json.getString("node_elem1_yshy_"+i)!=null&&!"".equals(json.getString("node_elem1_yshy_"+i))){
								vo.set("yshy", json.getString("node_elem1_yshy_"+i));
							}
						}
						if(json.containsKey("node_elem1_zdpx_"+i)){
							if(json.getString("node_elem1_zdpx_"+i)!=null&&!"".equals(json.getString("node_elem1_zdpx_"+i))){
								vo.set("zdpx", json.getString("node_elem1_zdpx_"+i));
							}
						}
						if(json.containsKey("node_elem1_zdsm_"+i)){
							if(json.getString("node_elem1_zdsm_"+i)!=null&&!"".equals(json.getString("node_elem1_zdsm_"+i))){
								vo.set("zdsm", json.getString("node_elem1_zdsm_"+i));
							}
						}
						if(json.containsKey("node_elem1_sfym_"+i)){
							if(json.getString("node_elem1_sfym_"+i)!=null&&!"".equals(json.getString("node_elem1_sfym_"+i))){
								vo.set("sfym", json.getString("node_elem1_sfym_"+i));
							}
						}
						objectColumnVO1 = new Record();
						objectColumnVO1.set("length", Integer.parseInt(json.getString("node_elem1_cdxz_"+i))+10);
						if(json.containsKey("node_elem1_ysm_"+i)){
							if(json.getString("node_elem1_ysm_"+i)!=null){
								objectColumnVO1.set("name", json.getString("node_elem1_ysm_"+i));
							}
						}
						//新增判断修改类型
						if("3".equals(json.getString("node_elem1_zdlx_"+i))){
							objectColumnVO1.set("type", "number");
						}else{
							objectColumnVO1.set("type", "VARCHAR2");
						}
						objectColumnVO1.set("null", true);
						objectColumnVO1.set("id", true);
						objectColumnVO1.set("comment", json.getString("node_elem1_ysm_"+i) + "要素名");
						ObjectColumnVOs.add(objectColumnVO1);
						
						vo.set("id",AppUtils.getStringSeq());
						Db.save("t_djb_jdsrysb", vo);
					}
				}
			}
			tableName = "DJB" + DateTimeUtil.getMSTime();
			
			//根据用户选择的要素点创建出登记簿数据表，在配置完整体流程之后，操作数据需要存在该表中
			ObjectTable ObjectTableVO = new ObjectTable();
			ObjectTableVO.setFileds(ObjectColumnVOs);
			ObjectTableVO.setTableName(tableName);
			ObjectTableVO.setComment("登记簿数据表");
			String createsql = ObjectTableVO.createSql();
			Db.update(createsql);
			
			//获取当前登录人姓名
			String userName = getCurrentUser().getStr("name");
			
			//创建表完成之后，需要在向T_DJB_RESMENU_TAB表中插入数据，将表名添加进DJBTAB,并且获取登记簿id：djbid插入，作为连接标识
			Record tabVO = new Record();
			tabVO.set("id",AppUtils.getStringSeq());
			tabVO.set("djbid",djbid);
			tabVO.set("djbtab",tableName);
			tabVO.set("createtime",DateTimeUtil.getMSTime());
			tabVO.set("createuser",userName);
			Db.save("t_djb_resmenu_tab", tabVO);
			// 记录日志
			LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "要素配置", "4", "要素配置-新增");
			log.info(getLogPoint().exit("要素配置-新增").toString());
			renderJson();
		}

	
	/**
	 * 获取所有要素字段
	 */
	public void findAll() {
		log.info("list");
		String djbid = getPara("djbid");
		String sql ="select ID,DJBID,YSM,YSHY,ZDLX,MBLX,CDXZ,ZDPX,ZDSM,SFYM "
		+" from T_DJB_JDSRYSB where 1=1 and DJBID = '"+djbid+"' order by to_number(zdpx) asc";
		List<Record> list = Db.find(sql);
		setAttr("data",list);
		setAttr("total",list.size());
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "要素配置", "3", "要素配置-获取详情");
		log.info(getLogPoint().exit("要素配置-获取详情").toString());
		renderJson();
	}

	/**
	 * 功能模块入口方法，返回功能模块主界面
	 * @return 功能模块页面
	 */
	public void ysblist() {
		render("TDjbJdsrysbList.jsp");
	}
	
	/**
	 * 根据主键，获取主键对应详细数据
	 * @param id 业务主键
	 * @return 业务主键对应业务详细数据
	 */
	public void getTDjbJdsrysbVOById() {
		String id = getPara("id");
		String sql ="select ID,DJBID,YSM,YSHY,ZDLX,MBLX,CDXZ,ZDPX,ZDSM from T_DJB_JDSRYSB where ID = '"+id+"'";
		Record re = Db.findFirst(sql);
		setAttr("record",re);
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "要素配置", "4", "要素配置-获取详情");
		log.info(getLogPoint().exit("要素配置-获取详情").toString());
		renderJson();
	}
	
	/*******************要素配置代码*结束*******************************/
	
	
	/*******************流程配置*开始*******************************/
	
	/**
	 * 获取登记簿流程维护信息
	 */
	public void getList1() {
		int pageNumber = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String djbid = getPara("djbid");
		List<String> listStr = new ArrayList<String>();
		String sql = "select t.id,t.current_operator_name, t.next_operator_group_name,t.djbid,(select yshy from t_djb_jdsrysb where djbid = t.djbid and ysm = t.zdmc) as zdmc " ;
		String extrasql = " from t_djb_lcpz t  where t.djbid = '"+djbid+"' ";
		Page<Record> r = Db.paginate(pageNumber, pageSize, sql, extrasql, listStr.toArray());
		setAttr("datas", r.getList());
		setAttr("total", r.getTotalRow());
		
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿流程维护", "3", "登记簿流程维护-查询");
		renderJson();
	}
	/**
	 * 跳转新增页面
	 */
	public void addObj() {
		String djbid = getPara("djbid");
		setAttr("djbid",djbid);
		render("add.jsp");
	}
	
	/**
	 * 获取下拉框角色信息
	 * 
	 * @throws IOException
	 */
	public void getRoleList() {
		List<Record> lr = Db.find("select id, name from sys_role_info where role_dele_flag='1'");
		renderJson(lr);
	}
	
	//获取除去当前已经选择角色以外的角色
	public void getCurrentRoleList(){
		String djbid = getPara("djbid");
		List<Record> lr = Db.find("select id, name from sys_role_info where role_dele_flag='1' and id not in( select t.current_operator from t_djb_lcpz t where t.djbid= '"+djbid+"' )");
		renderJson(lr);
	}
	
	/**
	 * 判断文本框事务类别是否重复
	 */
	public void getAffairsName(){
		String id = getPara("id");
		String djbid = getPara("djbid");
		String current_operator = getPara("current_operator");
		List<Record> list = Db.find("select id from t_djb_lcpz  where current_operator='"+current_operator+"' and djbid = '"+djbid+"'");
		
		// 当前操作员已存在
		if(list != null && list.size() > 0){
			// 修改操作
			if(id.equals(list.get(0).getStr("ID"))){
				renderText("0");
			}else{
				// 新增操作
				renderText("1");
			}
		}else{
			// 新增操作
			renderText("0");
		}
	}
	
	/**
	 * 刪除登记簿流程维护
	 */
	public void delObj() {
		String id = getPara("key");
		if (id != null) {
			Db.update("delete from t_djb_lcpz  where id = ?",id);
		}
		
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "通用事务审批流程维护", "5", "通用事务审批流程维护-删除");
		renderJson();
	}
	
	/**
	 * 新增登记簿流程维护
	 */
	@Before(Tx.class)
	public void saveDjb() {
		JSONObject json = getJsonObj("data");
		
		// 主键
		String id = json.getString("id");
		
		// 当前操作类型
		String pageType = json.getString("pageType");
		
		// 当前操作角色
		String current_operator = json.getString("current_operator");
		
		// 下级审批角色
		String next_operator_group = json.getString("next_operator_group");
		
		// 下级审批角色对应字段
		String zdmc =json.getString("zdmc");
		
		//登记簿id
		String djbid = json.getString("djbid");
		
		// 当前操作角色名称
		String current_operator_name = null;
		
		//是否是发起人
		String flag = json.getString("flag");
		if("true".equals(flag)){
			flag = "1";
		}else{
			flag = "0";
		}
		
		// 下级审批角色名称
		String next_operator_group_name = null;
		
		List<Record> roleList = null;
		
		
		// 获取角色名称
		if(!YyglUtil.isEmpty(current_operator)){
			String[] current_operator_group_list = current_operator.split(",");
			if(current_operator_group_list != null && current_operator_group_list.length > 0){
				for(String next_operator:current_operator_group_list){
					roleList = Db.find("select id, name from sys_role_info where role_dele_flag='1' and id='"+next_operator+"'");
					if(roleList != null && roleList.size() > 0){
						if(!YyglUtil.isEmpty(current_operator_name)){
							current_operator_name += ",";
							current_operator_name += roleList.get(0).getStr("NAME");
						}else{
							current_operator_name = roleList.get(0).getStr("NAME");
						}
					}
				}
			}
		}

		// 获取角色名称
		if(!YyglUtil.isEmpty(next_operator_group)){
			String[] next_operator_group_list = next_operator_group.split(",");
			if(next_operator_group_list != null && next_operator_group_list.length > 0){
				for(String next_operator:next_operator_group_list){
					roleList = Db.find("select id, name from sys_role_info where role_dele_flag='1' and id='"+next_operator+"'");
					if(roleList != null && roleList.size() > 0){
						if(!YyglUtil.isEmpty(next_operator_group_name)){
							next_operator_group_name += ",";
							next_operator_group_name += roleList.get(0).getStr("NAME");
						}else{
							next_operator_group_name = roleList.get(0).getStr("NAME");
						}
					}
				}
			}
		}
		
		// 新增
		if(YyglUtil.PAGE_TYPE_ADD.equals(pageType)){
			Db.update("insert into t_djb_lcpz("
						+ " id, current_operator, current_operator_name, next_operator_group, next_operator_group_name,djbid,zdmc,flag"
						+ " )values(?,?,?,?,?,?,?,?)",
					new Object[]{ AppUtils.getStringSeq(),current_operator, current_operator_name, next_operator_group, next_operator_group_name,djbid,zdmc,flag});
			
			// 记录日志
			LoggerUtil.getIntanceof().saveLogger(
					getCurrentUser().getStr("USER_NO"), "登记簿流程维护", "4", "登记簿流程维护-新增");
		}else{
			
			// 修改
			Db.update(
					"update t_djb_lcpz set"
							+ " current_operator=?, current_operator_name=?, next_operator_group=?, next_operator_group_name=?,zdmc=?,flag=? "
							+ " where id=?",
							new Object[]{current_operator, current_operator_name, next_operator_group, next_operator_group_name,zdmc,flag, id});
			// 记录日志
			LoggerUtil.getIntanceof().saveLogger(
					getCurrentUser().getStr("USER_NO"), "登记簿流程维护", "5", "登记簿流程维护-更新");
		}
		renderJson();
	}
	
	/**
	 * 修改事务工作流维护信息
	 */
	@Before(Tx.class)
	public void updateDjb() {
		saveDjb();
	}
	
	/**
	 * 获取编辑时的回显数据(登记簿流程维护)
	 */
	public void getDetail() {
		String id = getPara("key");
		String sql = "select id, current_operator, current_operator_name, next_operator_group, next_operator_group_name,djbid,zdmc,flag from t_djb_lcpz where 1 = 1";
		StringBuffer sb = new StringBuffer();
		List<String> listStr = new ArrayList<String>();
		if (AppUtils.StringUtil(id) != null) {
			sb.append(" and id = ?");
			listStr.add(id.trim());
		}
		List<Record> list = Db.find(sql + sb.toString(), listStr.toArray());
		setAttr("record", list.get(0));
		renderJson();
	}
	
	//获取审批人员对应字段
	public void getZdmc(){
		String djbid = getPara("djbid");
		List<Record> lr = Db.find("select t.ysm,t.yshy from t_djb_jdsrysb t where t.zdlx='0' and t.djbid = '" + djbid + "'");
		renderJson(lr);
	}
	
	/*******************流程配置*结束*******************************/
	

}
