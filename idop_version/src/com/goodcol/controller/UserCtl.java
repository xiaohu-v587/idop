package com.goodcol.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;

import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.server.zxglserver.UserServer;
import com.goodcol.util.AppUtils;
import com.goodcol.util.OrgUtil;
import com.goodcol.util.activiti.ActivitiUtil;
import com.goodcol.util.ext.anatation.LogBind;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.ext.render.excel.ExcelRender;
import com.goodcol.util.safe.MD5;

/**
 * 用户管理
 * 
 * @author sjgl011
 * 
 */
@RouteBind(path = "/user")
@Before( { ManagerPowerInterceptor.class,OperationLogRecordInterceptor.class})
// 路由
public class UserCtl extends BaseCtl {
	public static Logger log = Logger.getLogger(UserCtl.class);

	/**
	 * 控制器默认访问的方法
	 */
	public void index() {
		render("index.jsp");
	}

	/**
	 * 获取员工信息列表
	 */
	@LogBind(menuname = "用户管理", type = "3", remark = "用户信息-查询")
	public void listUser() {
		// 获取查询参数
		int pageNum = 0;
		int pageSize = 0;
		String downflag = getAttr("download");
		if("down".equals(downflag)){
			pageSize = 999999;
			pageNum = 1;
		}else{
			pageNum = getParaToInt("pageIndex") + 1;
			pageSize = getParaToInt("pageSize", 10);
		}
		Map<String, Object> map = organSql();
		// 从数据库查询指定条数的用户记录
		@SuppressWarnings("unchecked")
		Page<Record> r = Db.paginate(pageNum, pageSize, (String) map.get("selectSql"), (String) map.get("extrasql"), ((List<String>) map.get("sqlStr")).toArray());

		// 将获取到的用户信息填充到request对象的属性中
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());

		// 打印日志
		log.info("listUser--r.getList():" + r.getList());
		log.info("listUser--r.getTotalRow():" + r.getTotalRow());
		// 返回json数据
		renderJson();
	}

	public void getOrgList() throws IOException {

		// 只查询当前登录用户所属机构的子机构
		List<Record> lr = Db.find("select orgnum id, orgname name from sys_org_info where stat='1'");
		renderJson(lr);
	}

	public void getRoleList() {
		List<Record> lr = Db.find("select id, name from sys_role_info where role_dele_flag='1' and flag='0'");
		renderJson(lr);
	}
	
	/**
	 * 普通角色列表
	 */
	public void getCommonRoleList() {
		Record m2 = getCurrentUser();
		String role_level = m2.getStr("role_level");
		List<Record> lr = Db.find("select id, name from sys_role_info where role_dele_flag='1' and flag='0' and (role_type <> '1' or role_type is null) and to_number(role_level)>=?",role_level);
		renderJson(lr);
	}
	
	/**
	 * 基础角色列表
	 */
	public void getBasicRoleList() {
		List<Record> lr = Db.find("select id, name from sys_role_info where role_dele_flag='1' and flag='0' and role_type = '1'");
		renderJson(lr);
	}

	/**
	 * 添加
	 */
	public void form() {
		render("form.jsp");
	}

	/**
	 * 保存或是新增
	 */
	@LogBind(menuname = "用户管理", type = "4", remark = "用户信息-新增")
	public void save() {
		String id = getPara("id");
		int flag = 0;
		String result="success";
		String userno = getPara("user_no");
		String username = getPara("name");
		String org_id = getPara("org_id");
		String role_id = getPara("role_id");
		String email = getPara("email");
		String base_role = getPara("base_role");
		String common_role = getPara("common_role");
		String phone = getPara("phone");
		String hrSyn = getPara("hr_syn");
		String synDate = getPara("syn_date");
		// 新增用户密码默认为 123456
		String pwd = MD5.getMD5ofStr("123456");
		List<Record> records=null;
		String orgnum="";
		// 根据用户id是否为空来判断是新增还是修改用户
		if ("".equals(id.trim()) || id == null) {
			/*records=Db.find("select * from sys_user_info where user_no=?",new Object[]{userno});
			if(records!=null&&!records.isEmpty()){
				result="fail";
			}else{
				Record recrod=Db.findFirst("select orgnum from sys_org_info where id=?",new Object[]{org_id});
				orgnum=recrod.getStr("orgnum");
				id = AppUtils.getStringSeq();
				// 拼接用户插入语句
				flag = Db.update("insert into sys_user_info(id,user_no, name,org_id,pwd, role_id,phone,stat,is_teller) " + "values(?,?,?,?,?,?,?,?,?)", new Object[] { id, userno, username, orgnum, pwd,
						role_id,phone, "0" ,"1"});
				flag = Db.update("insert into sys_user_info(id,user_no, name,org_id,pwd, role_id,stat,is_teller) " + "values(?,?,?,?,?,?,?)", new Object[] { id, userno, username, orgnum, pwd,
						"0" ,"1"});
			}*/
			//暂不新增
			result="fail";
		} else {
			String roleIds = "";
			if (StringUtils.isNotBlank(base_role)) {
				roleIds += base_role;
				
			}
			if (StringUtils.isNotBlank(common_role)) {
				if (common_role.startsWith(",")) {
					roleIds += common_role;
				} else {
					roleIds += ("," + common_role);
				}
			}
			if (roleIds.startsWith(",")) {
				roleIds = roleIds.substring(1);
			}
			String[] roleIdArr = roleIds.split(",");
			for (String string : roleIdArr) {
				role_id = string;
				break;
			}
			try {
				UserServer userServer = new UserServer();
				userServer.updateUserRole(userno, roleIds);
			} catch (Exception e) {
				result="fail";
				setAttr("result",result);
				renderJson();
			}
			
			records=Db.find("select * from sys_user_info where user_no=? and id!=?",new Object[]{userno,id});
			if(records != null && records.size() > 1){
				result="fail";
			}else{
				Record recrod=Db.findFirst("select org_id from sys_user_info where id=?",new Object[]{id});
				String orgId=recrod.getStr("org_id");
				if(!orgId.equals(org_id)){
					Record r=Db.findFirst("select orgnum from sys_org_info where id=?",new Object[]{org_id});
					orgnum=r.getStr("orgnum");
				}else{
					orgnum=org_id;
				}
				// 参数的顺序和？的顺序一定要保持一致

				flag = Db.update("update sys_user_info set id=?, user_no=?, name=?,org_id=?, role_id=?, hr_syn = ?, syn_date = ?  where id = ? ",
						new Object[] { userno,userno, username, orgnum,role_id, hrSyn, synDate, id });
			}
		}
		Record userRoleRec = Db.findFirst("select count(1) as num from sys_user_role sur " +
				"where sur.user_id = ? and sur.role_id = ? ", userno, role_id);
		BigDecimal num = userRoleRec.getBigDecimal("num");
		if(num.intValue() == 0){
			Db.update("insert into sys_user_role(user_id, role_id, remark) values(?, ?, ?)", userno, role_id, "用户管理--角色更改");
		}
		/**
		 * 为整合activiti工作流，需同步当前系统用户数据到activiti数据库中
		 */
		
		/*-----------------------------------以下为整合部分-------------------------------------*/
		
		/*IdentityService identityService =  ActivitiUtil.getProcessEngine().getIdentityService();
		User user = identityService.createUserQuery().userId(id).singleResult();
	    if (user == null) {
	        user = identityService.newUser(id);
	    }
	    user.setFirstName("");//名
	    user.setLastName(username);//姓
	    user.setEmail(email); //邮箱暂时为空
	    if (StringUtils.isNotBlank(pwd)) {
	       user.setPassword(pwd);
	    }
	    identityService.saveUser(user);*/
		setAttr("result",result);
		// 打印日志
		log.info("saveUser-flag:"+flag);
		renderJson();
		/*-----------------------------------以上为整合部分-------------------------------------*/
//		renderNull();
		// 页面跳转
		// render("index.html");
	}

	/**
	 * 根据id唯一查询（不用）
	 */
/*	public void getDetail() {

		String ID = getPara("id");
		String sql = "select ui.*, ri.NAME ROLENAME, oi.ORGNAME from SYS_USER_INFO ui "
				+ "left join sys_role_info ri on ui.role_id=ri.id "
				+ "left join sys_org_info oi on ui.org_id=oi.orgnum "
				+ "where 1 = 1 and is_teller='1'";
		StringBuffer sb = new StringBuffer();
		List<String> listStr = new ArrayList<String>();

		if (AppUtils.StringUtil(ID) != null) {
			sb.append(" and ui.user_no = ? ");
			listStr.add(ID.trim());
		}
		List<Record> list = Db.find(sql + sb.toString(), listStr.toArray());
		log.info("sql=" + sql);
		setAttr("record", list.get(0));
		renderJson();
	}*/
	
	/**
	 * 根据id唯一查询（不用）
	 */
	public void getDetail() {
		
		String ID = getPara("id");
		String sql = "select ui.*,T1.base_role,T2.common_role, ri.NAME ROLENAME, oi.ORGNAME from SYS_USER_INFO ui "
				+ "left join (select ri.id as base_role,ur.user_id from sys_user_role ur left join sys_role_info ri on ur.role_id=ri.id where ri.role_type='1' ";
		StringBuffer sb = new StringBuffer();
		List<String> listStr = new ArrayList<String>();
		if (AppUtils.StringUtil(ID) != null) {
			sql += " and ur.user_id= ? ) T1 on T1.user_id = ui.user_no ";
			listStr.add(ID.trim());
			sql += " left join ( select wm_concat(ri.id) as common_role,ur.user_id from sys_user_role ur left join sys_role_info ri on ur.role_id=ri.id where (ri.role_type <> '1' or ri.role_type is null) ";
			sql += " and ur.user_id= ? group by ur.user_id) T2 on T2.user_id = ui.user_no ";
			listStr.add(ID.trim());
		}
		sql += " left join sys_role_info ri on ui.role_id=ri.id ";
		sql += " left join sys_org_info oi on ui.org_id=oi.orgnum ";
		sql += " where 1 = 1 and is_teller='1' ";
		if (AppUtils.StringUtil(ID) != null) {
			sb.append(" and ui.user_no = ? ");
			listStr.add(ID.trim());
		}
		List<Record> list = Db.find(sql + sb.toString(), listStr.toArray());
		log.info("sql=" + sql);
		setAttr("record", list.get(0));
		renderJson();
	}

	/**
	 * 删除（不用）
	 */
	@LogBind(menuname = "用户管理", type = "6", remark = "用户信息-删除")
	public void del() {

		String uuids = getPara("ids");
		/**
		 * 为整合activiti工作流，需同步当前系统用户数据到activiti数据库中
		 */
		IdentityService identityService =  ActivitiUtil.getProcessEngine().getIdentityService();
		if (uuids.contains(",")) {
			String[] array = uuids.split(",");
			for (int i = 0; i < array.length; i++) {
				String uuid = array[i];
				// Db.update("delete from SYS_user_INFO where id = ? ", uuid);
				Db.update("update SYS_user_INFO set stat='1' where id = ? ", uuid);

				/*-----------------------------------以下为整合部分-------------------------------------*/
				identityService.deleteUser(uuid);
				/*-----------------------------------以上为整合部分-------------------------------------*/
			}

		} else {
			// Db.update("delete from SYS_user_INFO where id = ? ", uuids);
			Db.update("update SYS_user_INFO set stat='1' where id = ? ", uuids);

			/*-----------------------------------以下为整合部分-------------------------------------*/
			identityService.deleteUser(uuids);
			/*-----------------------------------以上为整合部分-------------------------------------*/
		}
		renderNull();
	}

	/**
	 * 重置密码
	 */
	@LogBind(menuname = "用户管理", type = "5", remark = "用户管理-清密")
	public void resetPwd() {

		// 获取参数
		String uuids = getPara("ids");
		String[] arr = uuids.split(",");
		/**
		 * 为整合activiti工作流，需同步当前系统用户数据到activiti数据库中
		 */
		IdentityService identityService =  ActivitiUtil.getProcessEngine().getIdentityService();
		for (String id : arr) {
			int flag = Db.update("update SYS_user_INFO set pwd = ? where id = ? ", new Object[] { MD5.getMD5ofStr("123456"), uuids });
			log.info("resetPwd--flag:" + flag+"("+id+")");
			
			/*-----------------------------------以下为整合部分-------------------------------------*/
			 User user = identityService.createUserQuery().userId(id).singleResult();
			 if (user != null) {
				 user.setPassword(MD5.getMD5ofStr("123456"));
				 identityService.saveUser(user);
			 }
			/*-----------------------------------以上为整合部分-------------------------------------*/
		}
		renderNull();
	}

	/**
	 * 下载用户数据，保存为excel文件
	 */
	@LogBind(menuname = "用户管理", type = "7", remark = "用户管理-下载")
	public void download() {

		// 获取查询参数
		Map<String, Object> map = organSql1();
		String fromSql = (String) map.get("fromSql");

		List<Record> lr = null;
		Record r = Db.findFirst("select count(1) as count1 " + (String) map.get("extrasql"), ((List<String>) map.get("sqlStr")).toArray());

		// System.out.println(r.getStr("COUNT1"));
		// String count =r.getStr("COUNT1");

		String count = r.getBigDecimal("COUNT1").toString();
		int a = Integer.parseInt(count);
		System.out.println("当前条数：" + a);

		if (a == 0) {
			renderText("<script type='text/javascript'>parent.callback(\"当前没有数据可以下载\"); </script>");
			return;
		}

		if (a < 3000) {
			lr = Db.find((String) map.get("selectSql") + (String) map.get("extrasql"), ((List<String>) map.get("sqlStr")).toArray());

		} else {
			renderText("<script type='text/javascript'>parent.callback(\"下载数据不能超过3000条!\"); </script>");
			return;
		}

		// 从数据库查询指定条数的用户记录
		// @SuppressWarnings("unchecked")
		// List<Record> lr = Db.find(
		// (String) map.get("selectSql") + (String) map.get("extrasql"),
		// ((List<String>) map.get("sqlStr")).toArray());

		// 指定用哪些查询出来的字段填充excel文件
		String[] headers = { "user_no", "name", "ORGNAME", "ORG_ID", "ROLENAME", "STAT1" };

		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = { "员工号", "员工姓名", "所属机构", "机构号", "角色", "状态" };

		String fileName = "";
		try {
			fileName = new String("用户信息表.xls".getBytes("GB2312"), "ISO_8859_1");
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
	 * 下载用户数据，保存为excel文件2
	 */
//	@LogBind(menuname = "用户管理", type = "7", remark = "用户管理-下载")
	public void download2() {
		setAttr("download","down");
		listUser();
		// 获取查询参数
		Map<String, Object> map = organSql();
		//String fromSql = (String) map.get("fromSql");
		List<Record> lr = null;
		//Record r = Db.findFirst("select count(1) as count1 " + (String) map.get("extrasql"), ((List<String>) map.get("sqlStr")).toArray());
		String count = getAttr("total").toString();
		int a = Integer.parseInt(count);
		System.out.println("当前条数：" + a);
		if (a == 0) {
			renderText("<script type='text/javascript'>parent.callback(\"当前没有数据可以下载\"); </script>");
			return;
		}
		if (a < 30000) {
			lr = getAttr("data");
		} else {
			renderText("<script type='text/javascript'>parent.callback(\"下载数据不能超过30000条!\"); </script>");
			return;
		}
		// 指定用哪些查询出来的字段填充excel文件
		String[] headers = { "user_no", "name", "ORGNAME", "bancsid", "ROLENAME", "STAT1" };
		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = { "员工号", "员工姓名", "所属机构", "机构号", "角色", "状态" };
		String fileName = "";
		try {
			fileName = new String("用户信息表.xls".getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
			log.error("userctl-download", e);
		}
		List<Record> list = getAttr("data");
		// 转换成excel
		ExcelRender er = new ExcelRender(fileName, columns, headers, list, getResponse());
		er.render();
		// 打印日志
		log.info("download--lr:" + lr);
		renderNull();
	}

	public Map<String, Object> 	organSql1() {
		// 获取当前用户信息
		String userNo = getCurrentUser().getStr("USER_NO");
		String orgNo = getCurrentUser().getStr("ORG_ID");
		// String ROLE = getCurrentUser().getStr("ROLE_LEVEL");
		// System.out.println("等级为："+ROLE);
		List<Record> lr = Db.find("select ri.name,ri.role_level,ui.org_id from sys_role_info ri, sys_user_info ui where ui.role_id=ri.id and ui.user_no='" + userNo + "' and is_teller='1'");
		String roleName = lr.get(0).getStr("NAME");
		String role_level = lr.get(0).getStr("role_level");
		String org_id = lr.get(0).getStr("org_id");
		// System.out.println("等级为："+role_level);
		// System.out.println("机构号为："+org_id);
		// 获取页面输入查询条件
		String orgnum = getPara("orgnum1"); // 机构号 （下拉框）
		String jgh = getPara("jgh1"); // 机构（手输）
		String userno = getPara("userno1");// 用户号
		String name = getPara("name1"); // 用户姓名

		String selectSql = " select u.*, case u.stat when '1' then  '不在职'  when '0' then  '在职' end Stat1, o.orgname orgname,o.bancsid, r.name rolename ";
		String extrasql = " from sys_user_info u " + " left join sys_org_info o on u.org_id = o.orgnum " + " left join sys_role_info r on u.role_id = r.id ";
		StringBuffer whereSql = new StringBuffer(" where 1 = 1 and u.stat='0' and is_teller='1' ");
		List<String> sqlStr = new ArrayList<String>();

		if (AppUtils.StringUtil(role_level) == null || role_level.equals("0")) {
			// 柜员只允许查询本人
			whereSql.append(" and u.user_no = ? ");
			sqlStr.add(userNo);
		} else if (role_level.equals("1")) {
			// 支行查询机构号相等
			whereSql.append(" and u.org_id = '" + org_id + "' ");
		} else if (role_level.equals("2")) {
			// 分行查询机构号相等或下级
			whereSql.append(" and u.org_id in ( " + OrgUtil.getIntanceof().getSubOrgId(org_id) + " ) ");
		} else if (role_level.equals("3")) {
			// 总行和系统管理员，查询所有
			// whereSql.append(" and u.org_id in ( " +
			// OrgUtil.getIntanceof().getSubOrg(userNo) + " ) ");
		}

		/*
		 * // 权限 if (Constant.ROLENAME_GY.equals(roleName)) { // 柜员
		 * whereSql.append(" and u.user_no = ? "); sqlStr.add(userNo); } else if
		 * (Constant.ROLENAME_YYJL.equals(roleName)) { // 营运经理
		 * whereSql.append(" and u.org_id in ( " +
		 * OrgUtil.getIntanceof().getSubOrg(userNo) + " ) ");
		 * //sqlStr.add(OrgUtil.getIntanceof().getSubOrg(userNo)); } else if
		 * (Constant.ROLENAME_XTGLY.equals(roleName)) { // 系统管理员
		 * 
		 * }
		 */

		// 查询条件
		if (AppUtils.StringUtil(orgnum) != null) {
			// whereSql.append(" and o.orgnum = ? ");
			// sqlStr.add(orgnum.trim());
			whereSql.append(" and u.org_id in ( " + OrgUtil.getIntanceof().getSubOrgbyorg(orgnum) + " ) ");
		}
		if (AppUtils.StringUtil(jgh) != null) {
			whereSql.append(" and u.org_id like ? ");
			sqlStr.add("%" + jgh.trim() + "%");
		}
		if (AppUtils.StringUtil(userno) != null) {
			whereSql.append(" and u.user_no like ? ");
			sqlStr.add("%" + userno.trim() + "%");
		}
		if (AppUtils.StringUtil(name) != null) {
			whereSql.append(" and u.name like ? ");
			sqlStr.add("%" + name.trim() + "%");
		}

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

	/**
	 * 获取页面输入的条件，拼接sql，组织参数
	 * 
	 * @return map
	 */
	public Map<String, Object> organSql() {
		// 获取当前用户信息
		String userNo = getCurrentUser().getStr("USER_NO");
		String orgNo = getCurrentUser().getStr("ORG_ID");
		// String ROLE = getCurrentUser().getStr("ROLE_LEVEL");
		// System.out.println("等级为："+ROLE);
		List<Record> lr = Db.find("select ri.name,ri.role_level,ui.org_id from sys_role_info ri, sys_user_info ui where ui.role_id=ri.id and ui.user_no='" + userNo + "'");
		String roleName = lr.get(0).getStr("NAME");
		String role_level = lr.get(0).getStr("role_level");
		String org_id = lr.get(0).getStr("org_id");
		// System.out.println("等级为："+role_level);
		// System.out.println("机构号为："+org_id);
		// 获取页面输入查询条件
		String orgnum = getPara("orgnum"); // 机构号 （下拉框）
		String jgh = getPara("jgh"); // 机构（手输）
		String userno = getPara("userno");// 用户号
		String name = getPara("name"); // 用户姓名
		String rolename = getPara("rolename");//角色名称

		String selectSql = " select u.*, case u.stat when '1' then  '不在职'  when '0' then  '在职' end Stat1, o.orgname orgname, r.name rolename ,o.bancsid ";
		String extrasql = " from sys_user_info u " + " left join sys_org_info o on u.org_id = o.orgnum " + " left join sys_role_info r on u.role_id = r.id  ";
		StringBuffer whereSql = new StringBuffer(" where 1 = 1 and u.stat='0' and u.is_teller='1'");
		List<String> sqlStr = new ArrayList<String>();
		
		
//		if ("责任中心管理员".equals(roleName)) {
//			List<Record> list = Db.find("  select id from (select * from sys_org_info start with id='"+org_id+"' connect by prior UPID=id )where by2 = '4' ");
//			if (null != list && list.size() > 0) {
//				org_id = list.get(0).get("ID");
//			}
//			whereSql.append(" and u.org_id in ( " + OrgUtil.getIntanceof().getSubOrgId(org_id) + " ) ");
//		} else if ("中心支行管理员".equals(roleName)) {
//			List<Record> list = Db.find("  select id from (select * from sys_org_info start with id='"+org_id+"' connect by prior UPID=id )where by2 = '3' ");
//			if (null != list && list.size() > 0) {
//				org_id = list.get(0).get("ID");
//			}
//			whereSql.append(" and u.org_id in ( " + OrgUtil.getIntanceof().getSubOrgId(org_id) + " ) ");
//		} else if ("分行管理员".equals(roleName)) {
//			List<Record> list = Db.find("  select id from (select * from sys_org_info start with id='"+org_id+"' connect by prior UPID=id )where by2 = '2' ");
//			if (null != list && list.size() > 0) {
//				org_id = list.get(0).get("ID");
//			}
//			whereSql.append(" and u.org_id in ( " + OrgUtil.getIntanceof().getSubOrgId(org_id) + " ) ");
//		} else{
//			if (AppUtils.StringUtil(role_level) == null || role_level.equals("0")) {
//				// 社区 查本机构
//				whereSql.append(" and u.org_id = '" + org_id + "' ");
//			} else if (role_level.equals("1")) {
//				// 街道 查街道及下辖社区
//				whereSql.append(" and u.org_id in ( " + OrgUtil.getIntanceof().getSubOrgId(org_id) + " ) ");
//				
//			} else if (role_level.equals("2")) {
//				// 区镇 查区镇下所有子机构
//				whereSql.append(" and u.org_id in ( " + OrgUtil.getIntanceof().getSubOrgId(org_id) + " ) ");
//			} else if (role_level.equals("3")) {
//				// 市局级查所有
//				// whereSql.append(" and u.org_id in ( " +
//				// OrgUtil.getIntanceof().getSubOrg(userNo) + " ) ");
//			}
//		}
		

		/*
		 * // 权限 if (Constant.ROLENAME_GY.equals(roleName)) { // 柜员
		 * whereSql.append(" and u.user_no = ? "); sqlStr.add(userNo); } else if
		 * (Constant.ROLENAME_YYJL.equals(roleName)) { // 营运经理
		 * whereSql.append(" and u.org_id in ( " +
		 * OrgUtil.getIntanceof().getSubOrg(userNo) + " ) ");
		 * //sqlStr.add(OrgUtil.getIntanceof().getSubOrg(userNo)); } else if
		 * (Constant.ROLENAME_XTGLY.equals(roleName)) { // 系统管理员
		 * 
		 * }
		 */

		// 查询条件 中的机构
		if (AppUtils.StringUtil(orgnum) != null) {
			 whereSql.append(" and (  o.id = ? or o.by5 like ?)");
			 sqlStr.add(orgnum.trim());
			 sqlStr.add("%" + orgnum.trim()+ "%");
//			whereSql.append(" and u.org_id in ( " + OrgUtil.getIntanceof().getSubOrgbyorg(orgnum) + " ) ");
		}
		else
		{	//如果查询条件没有指定机构，这里安装用户的数据范围、角色级别 来查， 数据范围优先 包括机构未匹配数据
			String sjfw=AppUtils.getOrgNumByUser( getCurrentUser());		//数据范围
			if(AppUtils.StringUtil(sjfw)!=null){
				 whereSql.append(" and (o.id is null or o.id = ? or o.by5 like ?)");
				 sqlStr.add(sjfw.trim());
				 sqlStr.add("%" + sjfw.trim()+ "%");
			}
		}
		if (AppUtils.StringUtil(jgh) != null) {
			whereSql.append(" and o.bancsid like ? ");
			sqlStr.add("%" + jgh.trim() + "%");
		}
		if (AppUtils.StringUtil(userno) != null) {
			whereSql.append(" and u.user_no like ? ");
			sqlStr.add("%" + userno.trim() + "%");
		}
		if (AppUtils.StringUtil(name) != null) {
			whereSql.append(" and u.name like ? ");
			sqlStr.add("%" + name.trim() + "%");
		}
		if (AppUtils.StringUtil(rolename) != null) {
			whereSql.append(" and r.name like ? ");
			sqlStr.add("%" + rolename.trim() + "%");
		}
		

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
   /**
    *权限管理机构树页面
    * **/
    public void permission(){ 
      render("permission.jsp");
    }
    /**
     *获取机构数据
     * **/
     public void getOrgTree(){
        String sql = "select ORGNAME,ID,UPID,ORGNUM,ORDERNUM,LINKMAN,PHONENUM,ORGADRESS from sys_org_info where stat = 1 order by ORGNUM asc";
        List<Record> list = Db.find(sql);
        setAttr("datas",list);
        renderJson();
     }
    /**
     *查询已有的菜单，已有的角色权限
     **/
	 public void getHaveOrg() {
		String roleid = getPara("key");
		List<Record> list = Db.find("select ORGNUM from  SYS_USER_PERMISSION where USER_NO=?",
				new Object[] { roleid });
		String orgNums = "";
		for (Record record : list) {
			orgNums += ","+record.getStr("ORGNUM");
		}
		setAttr("orgNums", orgNums);
		renderJson();
	 }
     /**
      *权限管理机构修改
      * **/
     public void permissionOrgEdit(){
        String orgid = getPara("org_id");
        String userid = getPara("id");
        //清空权限
        Db.update("delete from SYS_USER_PERMISSION where USER_NO=?",
		           new Object[] {userid});  
        //添加权限
        if (orgid.contains(",")) {
            String[] array = orgid.split(",");
            for (int i = 0; i < array.length; i++) {
                 orgid = array[i];	 
                 Db.update("insert into SYS_USER_PERMISSION(USER_NO,ORGNUM) values(?,?)",
		           new Object[] { userid,orgid});
            }
       }else{	
             Db.update("insert into SYS_USER_PERMISSION(USER_NO,ORGNUM) values(?,?)",
	               new Object[] { userid,orgid});  
       }
        renderNull();
   }   
}
