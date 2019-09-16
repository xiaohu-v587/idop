package com.goodcol.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

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
import com.goodcol.util.ParamContainer;
import com.goodcol.util.ext.anatation.LogBind;
import com.goodcol.util.ext.anatation.RouteBind;

/**
 * 角色管理
 * 
 * @author first blush
 */
@RouteBind(path = "/role")
@Before( { ManagerPowerInterceptor.class,OperationLogRecordInterceptor.class})
public class RoleCtl extends BaseCtl {

	public static Logger log = Logger.getLogger(DateTimeUtil.class);

	/**
	 * 角色管理主界面
	 * */
	@Before(PermissionInterceptor.class)
	public void index() {
		render("index.jsp");
	}

	/**
	 * 主界面详细信息展示
	 * */
	@LogBind(menuname = "角色管理", type = "3", remark = "角色管理-查询")
	public void getList() {
		String name = getPara("key");
		int pageNumber = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		StringBuffer sb = new StringBuffer();
		// 获取当前用户信息
		String userNo = getCurrentUser().getStr("USER_NO");
		// 控制各角色只能看到本等级以下的角色
		// 获取该角色的等级
		String sqlP = "select r.role_level  from sys_user_info a,sys_role_info r " + "where a. user_no = '" + userNo + "' " + "and a.role_id=r.id ";
		String roleLevel = "";
		List<Record> list = Db.find(sqlP);
		if (list.size() > 0) {
			roleLevel = list.get(0).getStr("ROLE_LEVEL");
		}
		List<String> listStr = new ArrayList<String>();
		if (AppUtils.StringUtil(name) != null) {
			sb.append(" and c.name like ? ");
			listStr.add("%" + name.trim() + "%");
		}
		if (AppUtils.StringUtil(roleLevel) != null) {
			sb.append(" and c.ROLE_LEVEL >= ? ");
			listStr.add(roleLevel.trim());
		}
		String sql = "select c.* ";
		String extrasql = " from  ( select a.name sjname,b.* from SYS_ROLE_INFO a right join SYS_ROLE_INFO b on a.id=b.pid ) c where c.role_dele_flag='1' and c.flag='0' " + sb.toString() + " order by c.name asc";
		Page<Record> r = Db.paginate(pageNumber, pageSize, sql, extrasql, listStr.toArray());
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());

		// 打印日志
		log.info("getList--r.getList():" + r.getList());
		log.info("getList--r.getTotalRow():" + r.getTotalRow());
		renderJson();
	}

	/**
	 * 获取编辑时的回显数据
	 */
	public void getDetail() {
		String id = getPara("key");
		String sql = " select c.* from  ( select a.name sjname,b.* from SYS_ROLE_INFO a right join SYS_ROLE_INFO b on a.id=b.pid ) c where 1=1 ";
		StringBuffer sb = new StringBuffer();
		List<String> listStr = new ArrayList<String>();

		if (AppUtils.StringUtil(id) != null) {
			sb.append(" and c.id = ? ");
			listStr.add(id.trim());
		}
		List<Record> list = Db.find(sql + sb.toString(), listStr.toArray());
		setAttr("record", list.get(0));
		renderJson();

	}

	/**
	 * 角色添加页面
	 */
	public void form() {
		render("form.jsp");
	}

	/**
	 * 判断角色名称是否有重复 从save和update方法中把相关代码提取出来
	 */
	public void check() {
		String name = getPara("name");
		String flag = "0";
		// 判断角色名称是否有重复
		List<Record> list = Db.find("select name from SYS_ROLE_INFO where role_dele_flag='1' ");
		for (int i = 0; i < list.size(); i++) {
			if (name.equals(list.get(i).getStr("name"))) {
				flag = "1";
				break;
			}
		}
		setAttr("flag", flag);
		renderText("" + flag);
	}

	/**
	 * 角色保存
	 * **/
	@LogBind(menuname = "角色管理", type = "4", remark = "角色管理-新增")
	@Before(PermissionInterceptor.class)
	public void save() {
		String name = getPara("name");
		String pid = getPara("pid");
		String remark = getPara("remark");
		String level = getPara("role_level");
		String roleType = getPara("role_type");
		if ("true".equals(roleType)) {
			roleType = "1";// 是基础角色
		} else {
			roleType = "0";// 非基础角色
		}
		Db.update("insert into SYS_ROLE_INFO(ID,NAME,PID,REMARK,ROLE_LEVEL,FLAG,ROLE_TYPE) values(?,?,?,?,?,?,?)", new Object[] { AppUtils.getStringSeq(), name, pid, remark, level,"0",roleType });
		renderJson();
	}

	/**
	 * 角色更新保存
	 * */
	@LogBind(menuname = "角色管理", type = "5", remark = "角色管理-更新")
	@Before(PermissionInterceptor.class)
	public void update() {
		String id = getPara("id");
		String name = getPara("name");
		String pid = getPara("pid");
		String remark = getPara("remark");
		String level = getPara("role_level");
		String roleType = getPara("role_type");
		if ("true".equals(roleType)) {
			roleType = "1";// 是基础角色
		} else {
			roleType = "0";// 非基础角色
		}
		Db.update("update SYS_ROLE_INFO set NAME=?,PID=?,REMARK=? ,ROLE_LEVEL=?, ROLE_TYPE=? where ID=?", new Object[] { name, pid, remark, level,roleType, id});
		renderJson();
	}

	/**
	 * 删除角色 前台修改控制一次只能删除一个角色 后台修改判断是否存在从属角色
	 * 原来可以同时删除多个角色，现在只删一个，故本方法只运行if判断条件else后的代码 防止以后功能变动，其他代码不作删除
	 * */
	@LogBind(menuname = "角色管理", type = "6", remark = "角色管理")
	@Before(PermissionInterceptor.class)
	public void del() {
		String flag = "";
		String uuids = getPara("ids");
		if (uuids.contains(",")) {
			String[] array = uuids.split(",");
			for (int i = 0; i < array.length; i++) {
				String uuid = array[i];
				// 判断该角色没有从属角色
				List<Record> list = Db.find("select id from SYS_ROLE_INFO where pid = '" + uuid + "'");
				if (list.size() > 0) {
					flag = flag + uuid + "有从属角色，不能删除！  ";
					flag = "0";
					renderText(flag);
					break;
				}
				Db.update("update SYS_ROLE_INFO set ROLE_DELE_FLAG=? where id = ? ", "0", uuid);
				Db.update("delete from  SYS_POWER_INFO where objid=?", uuid);
			}
			flag = "1";
		} else {
			// 判断该角色没有从属角色
			List<Record> list = Db.find("select id from SYS_ROLE_INFO where pid = '" + uuids + "'");
			// 判断该角色下有没有用户
			List<Record> userNum = Db.find("select id from SYS_USER_INFO where role_id = '" + uuids + "'");
			// 判断吗该角色是否有分配权限
			List<Record> powerNum = Db.find("select id from SYS_ROLE_POWER where role_id = '" + uuids + "'");
			if (list.size() > 0) {
				flag = "5";
				// renderText(flag);
			} else if (userNum.size() > 0) {
				flag = "3";
				// renderText(flag);
			} else if (powerNum.size() > 0) {
				flag = "4";
				// renderText(flag);
			} else {
				Db.update("update SYS_ROLE_INFO set ROLE_DELE_FLAG=? where id = ? ", "0", uuids);
				Db.update("delete from  SYS_POWER_INFO where objid=?", uuids);
				flag = "1";
				log.warn("角色管理-删除");
			}
		}
		setAttr("record", flag);

		renderText("" + flag);
	}

	/**
	 * 上级角色combox框
	 * **/
	public void getCombox() {
		String sql = "select ID,NAME from SYS_ROLE_INFO where ROLE_DELE_FLAG='1' and flag='0'";
		List<Record> list = Db.find(sql);
		renderJson(list);
	}

	/***/
	/**
	 * 角色分配页面
	 */
	public void allot() {
		render("allot.jsp");
	}

	/**
	 * 查询所有的待分配的角色的机构人员树
	 */
	public void getOrgUserList() {
		
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
				+ " from sys_user_info u, sys_org_info o  " + " where u.org_id = o.orgnum and o.id in  " + "   (select id from sys_org_info where stat = '1' ");

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

		sql.append(" connect by prior orgnum = upid) " + " and u.role_id is null order by nlssort(u.name,'NLS_SORT=SCHINESE_PINYIN_M')) ");

		String sqlStr = sql.toString();

		List<Record> list = Db.find(sqlStr);
		setAttr("datas", list);
		// 打印日志
		// log.info("getList--datas:" + list);
		renderJson();
	}

	/**
	 * 根据角色查询所有的属于本角色的人员
	 */
	@LogBind(menuname = "角色管理", type = "3", remark = "角色人员分配-查询")
	public void getUsersByRoleId() {
		// 获取参数
		String roleId = getPara("roleId");
		String delIds = getPara("delIds");
		String addIds = getPara("addIds");
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		// sql
		String selectSql = "";
		String extraSql = "";
		List<String> listStr = new ArrayList<String>();
		// 查询
		selectSql = " select U.id id,u.USER_NO, u.name name, o.orgname pname, o.orgnum upid, 'user' as flag,'icon-user' as \"iconCls\" ";
		extraSql = " from sys_user_info u, sys_org_info o where u.org_id = o.orgnum and  ( u.role_id = ? ";
		listStr.add(roleId);
		if (AppUtils.StringUtil(addIds) != null) {
			extraSql += " or u.id in ( " + addIds + " )) ";
		} else {
			extraSql += " ) ";
		}
		if (AppUtils.StringUtil(delIds) != null) {
			extraSql += " and u.id not in ( " + delIds + " ) ";
		}
		Page<Record> r = Db.paginate(pageNum, pageSize, selectSql, extraSql, listStr.toArray());
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("datas", r.getList());
		setAttr("total", r.getTotalRow());
		renderJson();
	}

	/**
	 * 保存修改的员工角色
	 */
	@LogBind(menuname = "角色管理", type = "5", remark = "角色人员分配-更新")
	@Before(PermissionInterceptor.class)
	public void updateRoleUser() {

		// 获取参数
		String roleId = getPara("roleId");
		String saveIds = getPara("saveIds");
		String delIds = getPara("delIds");

		int saveFlag = 0;
		int delFlag = 0;
		if (AppUtils.StringUtil(saveIds) != null) {
			// 保存sql
			String sql = " update sys_user_info set role_id = '" + roleId + "' where id in ( " + saveIds + " ) ";
			saveFlag = Db.update(sql);
		}
		if (AppUtils.StringUtil(delIds) != null) {
			// 删除sql
			String sql2 = " update sys_user_info set role_id = '' where id in ( " + delIds + " ) ";
			delFlag = Db.update(sql2);
		}
		// 打印日志
		log.info("updateRoleUser--saveFlag:" + saveFlag);
		log.info("updateRoleUser--delFlag:" + delFlag);
		// 返回前台
		setAttr("saveFlag", saveFlag);
		setAttr("delFlag", delFlag);
		renderJson();
	}

	/*
	 * 暂时不用
	 */
	public void removeHelp() {
		renderNull();
	}

	/**
	 * 首次登陆页面进行角色的设置
	 * 2018年4月26日11:30:57
	 * @author liutao
	 */
	public void roleSetup() {
		render("roleSetup.jsp");
	}
	
}
