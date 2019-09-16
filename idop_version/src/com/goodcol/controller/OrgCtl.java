package com.goodcol.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.UserEntity;

import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.interceptor.PermissionInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.OrgUtil;
import com.goodcol.util.activiti.ActivitiUtil;
import com.goodcol.util.ext.anatation.LogBind;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.ext.render.excel.ExcelRender;
import com.goodcol.util.zxgldbutil.CommonUtil;

/**
 * 机构管理
 * 
 * @author sjgl011
 */
@RouteBind(path = "/org")
@Before( { ManagerPowerInterceptor.class,OperationLogRecordInterceptor.class })
public class OrgCtl extends BaseCtl {

	public static Logger log = Logger.getLogger(OrgCtl.class);

	/**
	 * 控制器默认访问方法
	 */
	public void index() {
		render("index.jsp");

	}

	/**
	 * 机构树的json数据 下拉框的json数据
	 */
	public void getList() {
		String sql = "select ORGNAME,ID,UPID,ORGNUM,ORDERNUM,LINKMAN,PHONENUM,ORGADRESS from sys_org_info where stat = 1 ";
		List<Record> list = Db.find(sql);
		setAttr("datas", list);
		// 打印日志
		log.info("getList--datas:" + list);
		renderJson();
	}

	public void getList1() {
		// String input07_oldOrg =getCurrentUser().getStr("org_id");
		String input07_oldOrg = getPara("input07_oldOrg");
		String sql = " select * from sys_org_info where orgnum in( " + OrgUtil.getIntanceof().getSubOrgId(input07_oldOrg) + " ) ";
		List<Record> list = Db.find(sql);
		setAttr("datas", list);
		// 打印日志
		log.info("getList--datas:" + list);
		renderJson();
	}

	public void getOrgList() {
		String sql = "select ORGNAME,ID,UPID,ORGNUM,ORDERNUM,LINKMAN,PHONENUM,ORGADRESS from sys_org_info a where a.stat = 1 " + CommonUtil.orgNameSql() + " order by ORGNUM asc";
		List<Record> list = Db.find(sql);
		setAttr("datas", list);
		renderJson();
	}
	/**
	 * 首次登陆使用， by 20181225 cxy
	 */
	public void getStepOrgList() {
		Record user = getCurrentUser();
		String org_id = user.getStr("org_id"); 
		String orgnum = user.getStr("orgnum"); 
		//如果机构号在本机构找不到对应机构 该机构号为空
		String sql = "select ORGNAME,ID,UPID,ORGNUM,ORDERNUM,LINKMAN,PHONENUM,ORGADRESS from sys_org_info a where a.stat = 1 " + CommonUtil.orgNameSqlToUser() + " order by ORGNUM asc";
		if(AppUtils.StringUtil(orgnum) == null ){
			//获取
			Record r =  Db.use("gbase").findFirst("select SUPER_UNITID from hr_org_info  where unit_id = ?", org_id);
			if(r!=null&&AppUtils.StringUtil(r.getStr("SUPER_UNITID"))!=null){
				sql = "select ORGNAME,ID,UPID,ORGNUM,ORDERNUM,LINKMAN,PHONENUM,ORGADRESS from sys_org_info a where a.stat = 1 " + CommonUtil.orgNameSqlToUser() + " and (id = '"+r.getStr("SUPER_UNITID")+"' or by5 like '%" + r.getStr("SUPER_UNITID") + "%')  order by ORGNUM asc";
			}
		}
		List<Record> list = Db.find(sql);
		setAttr("datas", list);
		renderJson();
	}
	/**
	 * 机构树的json数据 下拉框的json数据 根据用户号查询本机构和所有的子机构
	 */
	public void getListByUser() {
		List<Record> list = null;
		// 获取当前用户信息
		String userNo = getCurrentUser().getStr("USER_NO");
		//是否屏蔽本部，运营部 0-默认屏蔽 ，1-不屏蔽本部 ，运营部
		String noliketype = getPara("noliketype","0"); 
		String  org_id =  getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		/*
		 * // 获取参数 List<Record> lr = Db.find(
		 * "select ri.name from sys_role_info ri, sys_user_info ui where ui.role_id=ri.id and ui.user_no='"
		 * + userNo + "'"); String roleName = lr.get(0).getStr("NAME"); if
		 * (Constant.ROLENAME_XTGLY.equals(roleName)) { sql =
		 * "select ORGNAME,ID,UPID,ORGNUM,ORDERNUM,LINKMAN,PHONENUM,ORGADRESS from sys_org_info where stat = 1 "
		 * ; list = Db.find(sql); } else { // 获取参数 sql =
		 * " select ORGNAME,ID,UPID,ORGNUM,ORDERNUM,LINKMAN,PHONENUM,ORGADRESS "
		 * + "	from sys_org_info where stat = 1 " + " start with orgnum = " +
		 * " ? " + " connect by prior orgnum = upid "; list = Db.find(sql,
		 * orgNo); }
		 */
		//Record user = getCurrentUser();
		
		//用户角色
		//String roleNames = AppUtils.getRoleNames(user.getStr("ID"));
		//获取当前用户最高等级权限
		//String org_id = AppUtils.getOrgNumByUser(user);
		
//		List<Record> lr = Db.find("select ri.name,ri.role_level,ui.org_id from sys_role_info ri, sys_user_info ui where ui.role_id=ri.id and ui.user_no='" + userNo + "'");
//		String roleName = lr.get(0).getStr("NAME");
//		//String role_level = lr.get(0).getStr("role_level");
//		String org_id = lr.get(0).getStr("org_id");

		String by2 =  AppUtils.getOrgLevel(org_id);
		
		
		StringBuffer whereSql = new StringBuffer();
		whereSql.append(" select ORGNAME,ID,UPID,ORGNUM,ORDERNUM,LINKMAN,PHONENUM,ORGADRESS " + "	from sys_org_info a where a.stat = 1 ");
		
		if("0".equals(noliketype)){
			whereSql.append(CommonUtil.orgNameSql());
			whereSql.append(CommonUtil.orgNameSqlNoBenBu());
		}else if("1".equals(noliketype)){
			whereSql.append(CommonUtil.orgNameSql());
			whereSql.append(CommonUtil.orgNameSqlNoBenBu());
		}else if("2".equals(noliketype)){
			whereSql.append(CommonUtil.orgNameSqlToUser());
		}
		//role_level = AppUtils.getLevelByRoleName(roleName);
		
		if (AppUtils.StringUtil(org_id) != null ) {
			
			if(!"1".equals(by2)){
				whereSql.append(" and (id = '"+org_id+"' or by5 like '%" + org_id + "%') ");
			}
		}
		whereSql.append(" order by a.ORGNUM asc");
		String sql = whereSql.toString();
		list = Db.find(sql);
		setAttr("datas", list);
		// 打印日志
		log.info("getListById--datas:" + list);
		renderJson();
	}

	public void getListByOrg() {
		List<Record> list = null;
		// 获取当前用户信息
		//是否屏蔽本部，运营部 0-默认屏蔽 ，1-不屏蔽本部 ，运营部
		String noliketype = getPara("noliketype","2"); 
		String  org_id ="000000000";

		String by2 =  AppUtils.getOrgLevel(org_id);
		
		
		StringBuffer whereSql = new StringBuffer();
		whereSql.append(" select ORGNAME,ID,UPID,ORGNUM,ORDERNUM,LINKMAN,PHONENUM,ORGADRESS " + "	from sys_org_info a where a.stat = 1 ");
		
		if("0".equals(noliketype)){
			whereSql.append(CommonUtil.orgNameSql());
			whereSql.append(CommonUtil.orgNameSqlNoBenBu());
		}else if("1".equals(noliketype)){
			whereSql.append(CommonUtil.orgNameSql());
			whereSql.append(CommonUtil.orgNameSqlNoBenBu());
		}else if("2".equals(noliketype)){
			whereSql.append(CommonUtil.orgNameSqlToUser());
		}
		//role_level = AppUtils.getLevelByRoleName(roleName);
		
		if (AppUtils.StringUtil(org_id) != null ) {
			
			if(!"1".equals(by2)){
				whereSql.append(" and (id = '"+org_id+"' or by5 like '%" + org_id + "%') ");
			}
		}
		whereSql.append(" order by a.ORGNUM asc");
		String sql = whereSql.toString();
		list = Db.find(sql);
		setAttr("datas", list);
		// 打印日志
		log.info("getListById--datas:" + list);
		renderJson();
	}
	
	/**
	 * 根据id获取机构的详细信息 （点击左边的机构数，右边显示该机构的详细信息）
	 */
	@LogBind(menuname = "机构管理", type = "3", remark = "机构管理-查询")
	@SuppressWarnings("unchecked")
	public void getListOrg() {
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);

		// 获取查询参数
		Map<String, Object> map = organSql();
		// 从数据库查询指定条数的用户记录
		Page<Record> page = Db.paginate(pageNum, pageSize, (String) map.get("selectSql"), (String) map.get("extrasql"), ((List<String>) map.get("sqlStr")).toArray());
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		// 打印日志
		log.info("getListOrg--lr:" + page);
		renderJson();
	}

	/**
	 * 根据id获取机构的详细信息 （修改时使用）
	 */
	@LogBind(menuname = "机构管理", type = "3", remark = "机构管理-唯一查询")
	public void getDetail() {
		String id = getPara("id");
		String sql = "select * from sys_org_info where id=? ";
		List<Record> list = Db.find(sql, id);
		setAttr("datas", list.get(0));
		// 打印日志
		log.info("getDetail--datas:" + list.get(0));
		renderJson();
	}

	/**
	 * 增加 （增加和修改都使用）
	 */
	public void add() {
		render("form.jsp");
	}

	/**
	 * 根据机构号判断是否重复 （增加和修改都使用）
	 */
	@LogBind(menuname = "机构管理", type = "3", remark = "机构管理-机构号验重")
	public void checkOrgNum() {
		// 获取参数
		String orgNum = getPara("orgNum");
		String edit = getPara("edit");
		String qjgh = getPara("qjgh");
		List<Record> lr = null;
		if (edit == null) {
			// 增加前的判断是否orgnum重复
			lr = Db.find("select * from sys_org_info where orgnum = ?", orgNum);
		} else {
			// 修改前的判断是否orgnum重复
			lr = Db.find("select * from sys_org_info where orgnum = ? and orgnum != ?", orgNum, qjgh);
		}
		setAttr("msg", lr.size());
		// 打印日志
		log.info("checkOrgNum--msg:" + lr.size());
		renderJson();
	}

	/**
	 * 保存数据（增加）
	 */
	@LogBind(menuname = "机构管理", type = "4", remark = "机构管理-新增")
	public void save() {
		// 获取前台数据
		String id = getPara("pnum");// 上级机构id
		String orgName = getPara("orgname");
		String orgNum = getPara("orgnum");
		String linkMan = getPara("linkman");
		String phoneNum = getPara("phonenum");
		String orgAdress = getPara("orgadress");
		String qycj = getPara("by2");
		String by5="";
		String upId="";
		//获取该节点直接父级机构的所有上级机构
	    if(!id.equals("")&&id!=null){
		Record record=Db.findFirst("select * from sys_org_info where orgnum=?",id);
		by5=record.getStr("by5");//该节点的父级的所有上级
		upId=record.getStr("id");//该节点的父级的id
	    }
		// 保存
		int flag = 0;
		try {
			flag = Db.update("insert into sys_org_info(id,orgname,orgnum,upid,linkman,phonenum,orgadress,stat,by2) " + "values(?,?,?,?,?,?,?,?,?)", new Object[] { AppUtils.getStringSeq(), orgName,
					orgNum, upId, linkMan, phoneNum, orgAdress, "1", qycj });
		} catch (Exception e) {
			log.error("OrgCtl-save", e);
		}
		//获取该节点自己的id
		Record r=Db.findFirst("select * from sys_org_info where orgnum=?",orgNum);
		String rId=r.getStr("id");
		 by5=rId+","+by5;
		
		//更新by5字段，存入该节点所有的上级id	
			try {
				flag = Db.update("update sys_org_info set by5='"+by5+"' where id='"+rId+"'");
			} catch (Exception e) {
				log.error("OrgCtl-save-update-by5", e);
			}
			
			
		// 打印日志
		log.info("save--flag:" + flag);
		setAttr("flag", flag);
		renderJson();
	}

	/**
	 * 修改数据（修改更新）
	 */
	@LogBind(menuname = "机构管理", type = "5", remark = "机构管理-更新")
	public void update() {
		// 获取前台数据
		String id = getPara("id");// 本机构id
		String orgName = getPara("orgname");
		String orgNum = getPara("orgnum");
		String linkMan = getPara("linkman");
		String phoneNum = getPara("phonenum");
		String orgAdress = getPara("orgadress");
		String qycj = getPara("by2");
		// 更新
		int flag = 0;
		try {
			flag = Db
					.update("update sys_org_info set ORGNAME=?,ORGNUM=?,LINKMAN=?,PHONENUM=?,ORGADRESS=?,BY2=? where id= ? ", new Object[] { orgName, orgNum, linkMan, phoneNum, orgAdress, qycj, id });
		} catch (Exception e) {
			log.error("OrgCtl-update", e);
		}
		setAttr("flag", flag);
		// 打印日志
		log.info("update--flag:" + flag);
		renderJson();
	}

	/**
	 * 删除机构
	 */
	@LogBind(menuname = "机构管理", type = "5", remark = "机构管理-删除")
	public void del() {
		String id = getPara("id");
		// 判断此机构下是否有人员是处于正常状态
		Record r = Db.findFirst("select count(*) cou from sys_user_info u, sys_org_info o " + " where u.org_id = o.id and o.id = ? and u.stat = '0' ", id);
		int res = Integer.parseInt(r.get("cou") + "");
		if (res == 0) {
			// 此机构下面没有关联的人员，可以删除
			Record c = Db.findFirst("SELECT COUNT(*) c FROM sys_org_info WHERE upid= ? and stat='1'", id);// 判断是否有下级机构
			int pcount = Integer.parseInt(c.get("c").toString());
			int flag = 0;
			if (pcount == 0) {
				flag = Db.update("update  sys_org_info set stat = 0 where id = ? ", id);
				setAttr("flag", flag);
			} else {
				setAttr("flag", flag);
			}
			// 打印日志
			log.info("del--flag:" + flag);
		} else {
			// 此机构下面有人员，不能删除
			setAttr("flag", 2);
		}
		renderJson();
	}

	/**
	 * 下载机构数据，保存为excel文件
	 */
	@LogBind(menuname = "机构管理", type = "7", remark = "机构管理-下载")
	public void download() {

		// 获取参数
		Map<String, Object> map = organSql();
		// 从数据库查询指定条数的用户记录
		@SuppressWarnings("unchecked")
		List<Record> lr = Db.find((String) map.get("selectSql") + (String) map.get("extrasql"), ((List<String>) map.get("sqlStr")).toArray());

		// 指定用哪些查询出来的字段填充excel文件
		String[] headers = { "ORGNAME", "ORGNUM","BY2" ,"SJORGNAME", "SJORGNUM" };

		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = { "本机构名称", "本机构号","层级" ,"上级机构名称", "上级机构号" };

		String fileName = "";
		try {
			fileName = new String("机构信息表.xls".getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
			log.error("OrgCtl-download", e);
		}

		// 转换成excel
		ExcelRender er = new ExcelRender(fileName, columns, headers, lr, getResponse());
		er.render();
		// 打印日志
		log.info("download--lr:" + lr);
		renderNull();
	}

	/**
	 * 获取页面输入的条件，拼接sql，组织参数 （查询和下载使用）
	 * 
	 * @return map
	 */
	public Map<String, Object> organSql() {
		// 获取页面输入查询条件
		String orgNum = getPara("orgnum"); // 机构号 （下拉框）
		String jgh = getPara("jgh"); // 机构（手输）
		String orgName = getPara("orgname");// 机构名称
		String pId = getPara("pid");
		String orgId = getPara("orgid");
		String selectSql = " select a.id id ,a.upid upid ,a.orgname orgname,a.orgnum orgnum, " + " b.id sjid, b.orgname sjorgname, b.orgnum sjorgnum,a.by2 by2 ";
		String extrasql = " from sys_org_info a left join sys_org_info b " + " on a.upid = b.id ";
		StringBuffer whereSql = new StringBuffer(" where 1 = 1 and a.stat=1 ");
		List<String> sqlStr = new ArrayList<String>();
		if (AppUtils.StringUtil(orgNum) != null) {
			whereSql.append(" and a.orgnum = ? ");
			sqlStr.add(orgNum.trim());
		}
		if (AppUtils.StringUtil(orgId) != null) {
			whereSql.append(" and a.by5 like ? ");
			sqlStr.add("%" + orgId.trim() + "%");
		}
		if (AppUtils.StringUtil(jgh) != null) {
			whereSql.append(" and a.orgnum like ? ");
			sqlStr.add("%" + jgh.trim() + "%");
		}
		if (AppUtils.StringUtil(orgName) != null) {
			whereSql.append(" and a.orgname like ? ");
			sqlStr.add("%" + orgName.trim() + "%");
		}
		if (AppUtils.StringUtil(pId) != null) {
			whereSql.append(" and a.upid = ? ");
			sqlStr.add(pId.trim());
		}
		
		whereSql.append(CommonUtil.orgNameSql());
		whereSql.append(" order by orgnum ASC");
		
		extrasql += whereSql.toString();
		// selectSql,extrasql,sqlStr 装入map，返回
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("selectSql", selectSql);
		map.put("extrasql", extrasql);
		map.put("sqlStr", sqlStr);
		// 打印日志
		log.info("organSql--map:" + map);
		return map;
	}

	/*-----------------------加入岗位信息-----------------------*/
	/**
	 *岗位新增or编辑页面 
	 */
	public void addPosition(){
		render("positionForm.jsp");
	}
	@LogBind(menuname = "机构管理-岗位", type = "3", remark = "机构管理-岗位-唯一查询")
	public void getPositionDetail(){
		String id = getPara("id");
		String sql = "select * from sys_position where id=? ";
		List<Record> list = Db.find(sql, id);
		setAttr("datas", list.get(0));
		// 打印日志
		log.info("getDetail--datas:" + list.get(0));
		renderJson();
	}
	@LogBind(menuname = "机构管理-岗位", type = "3", remark = "机构管理-岗位-查询")
	public void positionList(){
		super.defualtList(positionOrganSql());
		// 打印日志
		log.info("listUser--r.getList():" +getAttr("data"));
		log.info("listUser--r.getTotalRow():" + getAttr("total"));
		// 返回json数据
		renderJson();		
	}
	
	/**
	 * 条件组合函数
	 * @return
	 */
	protected Map<String, Object> positionOrganSql() {
		String sortField = getPara("sortField");
		String sortOrder = getPara("sortOrder");
		String orgNo = getCurrentUser().getStr("ORG_ID");
		String jgh = getPara("jgh"); // 机构（手输）
		String selectSql = " select t.*,(select orgname from SYS_ORG_INFO where stat=1 and orgnum=t.orgnum) as orgname ";
		String extrasql = " from  sys_position t ";
		StringBuffer whereSql = new StringBuffer(" where 1 = 1 and data_state='0' ");
		List<String> sqlStr = new ArrayList<String>();
	
		super.setFindPara(whereSql, sqlStr, "name,describe,duty".split(regex), "data_state,orgnum".split(regex));
		super.betweenPara(whereSql, sqlStr, "term,term_days".split(regex));
		if (AppUtils.StringUtil(jgh) != null) {
			whereSql.append(" and orgnum like ? ");
			sqlStr.add("%" + jgh.trim() + "%");
		}else{
			whereSql.append("and orgnum in (select orgnum from SYS_ORG_INFO where stat=1 start with orgnum = ? connect by prior id = upid) ");
			sqlStr.add(orgNo);
		}
		extrasql += whereSql.toString() + ((AppUtils.StringUtil(sortField) != null) ? " order by " + sortField + " " + sortOrder : " order by c_date desc,u_date desc ");;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("selectSql", selectSql);
		map.put("extrasql", extrasql);
		map.put("sqlStr", sqlStr);
		log.info("organSql--map:" + map);
		return map;		
	}
	
	/**
	 * 删除岗位
	 */
	@LogBind(menuname = "机构管理-岗位", type = "5", remark = "机构管理-岗位-删除")
	public void delPosition(){
		/*------------------业务规则说明-----------------------*/
		/*
		 
		 因与工作流进行合并，需同步用户与用户组（岗位）
		 1.因此删除岗位动作时，需要级联删除该岗位下的所有用户与岗位级联关系
		 2.考虑是否需要判断工作流使用情况
		 */
		String uuids = getPara("ids");
		
		/**
		 * 为整合activiti工作流，需同步当前系统用户组数据到activiti数据库中
		 */
		/*-----------------------------------以下为整合部分-------------------------------------*/
		IdentityService identityService =  ActivitiUtil.getProcessEngine().getIdentityService();
		/*-----------------------------------以上为整合部分-------------------------------------*/
		int flag = 0;
		//检查是否有流程在使用岗位
		Record r = new Record();//此处模拟无流程在使用
		r.set("_count", "0");
		if(r == null || r.get("_count").equals("0"))
		{
			if (uuids.contains(",")) 
			{
				String[] array = uuids.split(regex);
				if(array!=null && array.length>0)
				{
					String sql = "update sys_position set data_state='1' where id in "+super.getParasToStringRegex(array);
					flag = Db.update(sql, array);
					sql = "delete sys_user_position  where position_no  in "+super.getParasToStringRegex(array);
					if(flag>0)flag = Db.update(sql, array);	
					
					/*-----------------------------------以下为整合部分-------------------------------------*/
					for (String grupid : array) 
					{
						identityService.deleteGroup(grupid);
					}
					/*-----------------------------------以上为整合部分-------------------------------------*/
				}
			} else 
			{
				flag = Db.update("update sys_position set data_state='1' where id = ? ", uuids);
				if(flag>0)flag = Db.update("delete sys_user_position  where position_no = ?", uuids);	
				
				/*-----------------------------------以下为整合部分-------------------------------------*/
				identityService.deleteGroup(uuids);
				/*-----------------------------------以上为整合部分-------------------------------------*/
			}
		}
		else
		{
			flag = 2;// 此岗位有工作流在使用，不能删除
		}
		
		
		
		
		setAttr("flag", flag);
		// 打印日志
		log.info("del-position-flag:" + flag);
		renderJson();
	}
	
	/**
	 * 新增岗位数据
	 */
	@LogBind(menuname = "机构管理-岗位", type = "4", remark = "机构管理-岗位")
	public void savePosition(){
		// 获取前台数据
		String [] paras = "id,position_no,name,describe,duty,term,term_days,orgnum,data_state,type".split(regex);
		Record r  = super.getParaByParas(paras);
		// 保存
		boolean  flag = false;
		try {
			if(r.getStr("id")!=null&&!r.getStr("id").isEmpty()){//id非空视为编辑
				r.remove("data_state");
				r.set("u_date", DateTimeUtil.getTime()).set("usernum", getCurrentUser().getStr("USER_NO"));
				flag = Db.update("sys_position", "id", r);
			}else{
				r.set("id", AppUtils.getStringSeq()).set("data_state", "0")
				.set("c_date", DateTimeUtil.getTime()).set("usernum", getCurrentUser().getStr("USER_NO"));
				flag = Db.save("sys_position", "id", r); 	
			}
			
			/**
			 * 为整合activiti工作流，需同步当前系统用户组数据到activiti数据库中
			 */
			
			/*-----------------------------------以下为整合部分-------------------------------------*/
			
			IdentityService identityService =  ActivitiUtil.getProcessEngine().getIdentityService();
			Group group = identityService.createGroupQuery().groupId(r.getStr("position_no")).singleResult();
			if (group == null) {
	            group = identityService.newGroup(r.getStr("position_no"));
	        }
	        group.setName(r.getStr("name"));
	        group.setType(r.getStr("type"));
	        identityService.saveGroup(group);    
			
			/*-----------------------------------以上为整合部分-------------------------------------*/
			
			
		} catch (Exception e) {
			log.error("OrgCtl-position-save", e);
		}
		
		// 打印日志
		log.info("save-position-flag:" + flag);
		// 记录操作日志
		log.warn("机构管理-岗位-"+(AppUtils.StringUtil(r.getStr("id"))==null?"新增":"编辑"));
		setAttr("flag", flag);
		renderJson();
	}
	
	
	/**
	 * 下载机构数据，保存为excel文件
	 */
	@LogBind(menuname = "机构管理-岗位", type = "7", remark = "机构管理-岗位-下载")
	public void downloadPosition() {

		// 获取参数
		Map<String, Object> map = positionOrganSql();
		// 从数据库查询指定条数的用户记录
		@SuppressWarnings("unchecked")
		List<Record> lr = Db.find((String) map.get("selectSql") + (String) map.get("extrasql"), ((List<String>) map.get("sqlStr")).toArray());

		// 指定用哪些查询出来的字段填充excel文件
		String[] headers = { "name", "describe","duty" ,"term", "term_days","orgname","orgnum","type" };

		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = { "岗位名称", "岗位描述","岗位职责" ,"岗位任期", "任期天数","所属机构","所属机构号","岗位类型" };

		String fileName = "";
		try {
			fileName = new String("机构-岗位信息表.xls".getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
			log.error("OrgCtl-download", e);
		}

		// 转换成excel
		ExcelRender er = new ExcelRender(fileName, columns, headers, lr, getResponse());
		er.render();
		// 打印日志
		log.info("download--lr:" + lr);
		renderNull();
	}
	
	
	/**
	 * 岗位分配页面
	 */
	public void positionAllot() {
		render("positionAllot.jsp");
	}

	
	/**
	 * 查询所有的待分配的岗位的机构人员树
	 */
	public void getOrgUserList() {
		/*------------查询所有未分配的人员非本岗位的本机构下的所有人员----------*/
		//获取岗位id
		String id = getPara("position_no");
		// 获取当前用户信息
		String userNo = getCurrentUser().getStr("USER_NO");
	
		//String orgNo = getCurrentUser().getStr("ORG_ID");
		// 获取用户角色等级和机构号
		List<Record> lr = Db.find("select ri.name,ri.role_level,ui.org_id from sys_role_info ri, sys_user_info ui where ui.role_id=ri.id and ui.user_no='" + userNo + "'");
		//String roleName = lr.get(0).getStr("NAME");
		String role_level = lr.get(0).getStr("role_level");
		String org_id = lr.get(0).getStr("org_id");		
		StringBuffer sql = new StringBuffer();
		sql.append("select orgnum as id, ORGNAME as name,' ' as user_no, UPID, 'org' as flag ,'icon-node' as \"iconCls\" from sys_org_info " + " where stat = '1' and id in " + "  (select id from sys_org_info  "
				+ "     where stat = '1' ");

		if (AppUtils.StringUtil(role_level) == null || role_level.equals("0") || role_level.equals("1")) {
			// 柜员和支行只允许查询本人所在机构
			sql.append(" and orgnum = '" + org_id + "' ");
		} else if (role_level.equals("2")) {
			// 分行查询机构号相等或下级
			sql.append(" and orgnum in ( " + OrgUtil.getIntanceof().getSubOrgId(org_id) + " ) ");
		} else if (role_level.equals("3")) {
			// 总行和系统管理员，查询所有
			// whereSql.append(" and u.org_id in ( " +
			// OrgUtil.getIntanceof().getSubOrg(userNo) + " ) ");
		}

		sql.append(" connect by prior orgnum = upid)  " + " union all select * from ( select u.id as id,u.name as name,u.user_no as user_no,o.orgnum as UPID, 'user' as flag,'icon-user' as \"iconCls\" "
				+ " from sys_user_info u, sys_org_info o  " + " where  u.org_id = o.id and u.stat='0' and o.id in  " + "   (select id from sys_org_info where stat = '1' ");

		if (AppUtils.StringUtil(role_level) == null || role_level.equals("0")) {
			// 柜员只允许查询本人
			sql.append(" and u.user_no = '" + userNo + "' ");
		} else if (role_level.equals("1")) {
			// 支行查询机构号相等
			sql.append(" and o.orgnum = '" + org_id + "' ");
		} else if (role_level.equals("2")) {
			// 分行查询机构号相等或下级
			sql.append(" and o.orgnum in ( " + OrgUtil.getIntanceof().getSubOrgId(org_id) + " ) ");
		} else if (role_level.equals("3")) {
			// 总行和系统管理员，查询所有
			// whereSql.append(" and u.org_id in ( " +
			// OrgUtil.getIntanceof().getSubOrg(userNo) + " ) ");
		}
		sql.append(" connect by prior orgnum = upid) " + "  and u.id not in (select t.user_no from  sys_user_position t where t.position_no = ? ) order by nlssort(u.name,'NLS_SORT=SCHINESE_PINYIN_M')) ");

		String sqlStr = sql.toString();
		List<Record> list = Db.find(sqlStr,id);
		setAttr("datas", list);
		// 打印日志
		// log.info("getList--datas:" + list);
		renderJson();
	}

	
	/**
	 * 根据岗位查询所有的属于本岗位的人员
	 */
	@LogBind(menuname = "机构管理-岗位", type = "3", remark = "岗位人员分配-查询")
	public void getUsersByPositionId() {
		// 获取参数
		//获取岗位id
		String position_no = getPara("position_no");
		String delIds = getPara("delIds");
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		// sql
		String selectSql = "";
		String extraSql = "";
		List<Object> listStr = new ArrayList<Object>();
		// 查询
		selectSql = " select u.id id,u.user_no, u.name name, o.orgname pname, o.orgnum upid, 'user' as flag,'icon-user' as \"iconCls\" ";
		extraSql = " from sys_user_info u, sys_org_info o where u.stat='0' and  u.org_id = o.id and    u.id  in (select t.user_no from  sys_user_position t where t.position_no=? ) ";
		listStr.add(position_no);
		if (AppUtils.StringUtil(delIds) != null) {
			String [] _delIds = delIds.split(regex);
			extraSql += " and u.id not in " + super.getParasToStringRegex(_delIds) ;
			listStr.add(Arrays.asList(_delIds));
		}
		
		Page<Record> r = Db.paginate(pageNum, pageSize, selectSql, extraSql, listStr.toArray());
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("datas", r.getList());
		setAttr("total", r.getTotalRow());
		// 记录操作日志
		renderJson();
	}
	/**
	 * 保存修改的员工岗位
	 */
	@LogBind(menuname = "机构管理-岗位", type = "5", remark = "岗位人员分配-更新")
	@Before(PermissionInterceptor.class)
	public void updatePositionUser() {
		String position_no = getPara("position_no");
		// 获取参数
		String saveIds = getPara("saveIds");
		String delIds = getPara("delIds");
		/**
		 * 为整合activiti工作流，需同步当前系统用户组数据到activiti数据库中
		 */
		
		IdentityService identityService =  ActivitiUtil.getProcessEngine().getIdentityService();

		int saveFlag = 0;
		int delFlag = 0;
		if (AppUtils.StringUtil(saveIds) != null) {
			// 保存sql
			/*String sql = " delete sys_user_position where user_no in ( " + saveIds + " ) ";
			saveFlag = Db.update(sql);*/
			String [] f_saveIds = saveIds.split(regex);
			String sql = "insert into sys_user_position(id,user_no,position_no) values(?,?,?)";
			Object [][] paras = new Object[f_saveIds.length][3];
			for (int i = 0; i < f_saveIds.length; i++) {
				paras[i][0]=AppUtils.getStringSeq();
				paras[i][1]=f_saveIds[i];
				paras[i][2]=position_no;
			}
			int [] counts = Db.batch(sql, paras, 10);
			saveFlag = counts.length;
			/*-----------------------------------以下为整合部分-------------------------------------*/
			List<Group> groupInDb = identityService.createGroupQuery().groupId(position_no).list();
			if(groupInDb!=null && groupInDb.size()>0){
				for (String userId : f_saveIds) {
					 User user1=new UserEntity(); // 实例化用户实体
				     user1.setId(userId);
				     identityService.saveUser(user1);
					identityService.createMembership(userId, position_no);
				}
			}
	            
			/*-----------------------------------以上为整合部分-------------------------------------*/
		}
		if (AppUtils.StringUtil(delIds) != null) {
			// 删除sql
			String [] delIdss = delIds.split(regex);
			String sql2 = " delete sys_user_position where user_no in "+super.getParasToStringRegex(delIdss)+"  and position_no=?";
			
			List<Object> paras = new ArrayList<Object>();
			paras.addAll(Arrays.asList(delIdss));
			paras.add(position_no);
			delFlag = Db.update(sql2,paras.toArray());
			
			/*-----------------------------------以下为整合部分-------------------------------------*/
			List<Group> groupInDb = identityService.createGroupQuery().groupId(position_no).list();
			if(groupInDb!=null && groupInDb.size()>0){
				for (String userId : delIdss) {
					 identityService.deleteMembership(userId, position_no);
					 identityService.deleteUser(userId); 
				}
			}
			/*-----------------------------------以上为整合部分-------------------------------------*/
		}
		
		// 打印日志
		log.info("updatePositionUser--saveFlag:" + saveFlag);
		log.info("updatePositionUser--delFlag:" + delFlag);
		// 返回前台
		setAttr("saveFlag", saveFlag);
		setAttr("delFlag", delFlag);
		renderJson();
	}
	
}
