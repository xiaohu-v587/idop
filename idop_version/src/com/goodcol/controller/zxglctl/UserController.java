package com.goodcol.controller.zxglctl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.core.plugin.activerecord.tx.Tx;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.server.zxglserver.UserServer;
import com.goodcol.util.AppUtils;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.ext.anatation.LogBind;
import com.goodcol.util.ext.anatation.RouteBind;

/**
 * 质效管理系统--用户管理模块
 * 2018年5月2日15:43:54
 * @author liutao
 *
 */
@RouteBind(path = "/zxuser")
@Before( { ManagerPowerInterceptor.class,OperationLogRecordInterceptor.class})
public class UserController extends BaseCtl{
	
	public void index() {

	}
	
	/**
	 * 用户权限管理
	 * 2018年5月2日15:48:53
	 * @author liutao
	 */
	public void userRole(){
		render("userRole.jsp");
	}
	
	/**
	 * 打开用户角色列表页面
	 * 2018年5月3日09:41:42
	 * @author liutao
	 */
	public void findUserRole(){
		render("findUserRole.jsp");
	}
	
	/**
	 * 查询用户角色
	 * 2018年5月2日15:48:53
	 * @author liutao
	 */
	public void userRoleList(){
		String uid = getPara("id");
		if(uid == null){
			renderNull();
		}
		UserServer userServer = new UserServer();
		List<Record> records = userServer.userRoleList(uid);
		setAttr("datas", records);
		setAttr("data", records);
		renderJson();
	}
	
	/**
	 * 查询待分配角色
	 * 2018年5月2日15:48:53
	 * @author liutao
	 */
	public void distributeRoleList(){
		String uid = getPara("id");
		if(uid == null){
			renderNull();
		}
		UserServer userServer = new UserServer();
		List<Record> records = userServer.distributeRoleList(uid);
		setAttr("datas", records);
		renderJson();
	}
	
	/**
	 * 保存用户角色
	 * 2018年5月3日17:08:37
	 * @author liutao
	 */
	@LogBind(menuname = "用户角色管理", type = "5", remark = "人员角色分配-更新")
	@Before(Tx.class)
	public void updateUserRole(){
		try {
			String uid = getPara("uid");
			String roleIds = getPara("saveIds");
			UserServer userServer = new UserServer();
			boolean flag = userServer.updateUserRole(uid, roleIds);
			if(flag){
				setAttr("flag", "1");
			}else{
				setAttr("flag", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			setAttr("flag", "-1");
		} finally {
			renderJson();
		}
	}
	
	/**
	 * 获取当前登录用户信息
	 * 2018年4月27日16:39:24
	 * @author liutao
	 */
	public void getUserInfo(){
		Record m = getCurrentUser();
		if (m == null) {
			render("login.jsp");
		} else {
			String uId = m.getStr("USER_NO");
			String sql = "select b.user_no as user_id, b.role_id as role_id,c.orgnum org_id, " 
					+ " b.name as user_name, c.orgname,b.model,"
					+ "b.phone "
					+ "from sys_user_info b ,sys_org_info c " 
					+ "where  b.org_id=c.orgnum "
					+ "and b.user_no = ?";
			Record r = Db.findFirst(sql, uId);
			if(null == r){
				r = new Record();
				r.set("user_id", uId);
				r.set("user_name", m.getStr("name"));
				r.set("orgname", m.getStr("orgname"));
			}
			setAttr("record", r);
			renderJson();
		}
	}
	
	/**
	 * 获取当前登录用户信息
	 * 2018年4月27日16:39:24
	 * @author liutao
	 */
	public void getUserRoleInfo(){
		Record m = getCurrentUser();
		if (m == null) {
			render("login.jsp");
		} else {
			String uId = m.getStr("USER_NO");
			Record r = new UserServer().getUserRoleInfo(uId);
			setAttr("record", r);
			renderJson();
		}
	}
	
	/**
	 * 营销客户管理页面
	 * 2018年6月6日17:43:38
	 * @author liutao
	 */
	public void reachCustomer(){
		render("marketCustomer.jsp");
	}

	/**
	 * 修改用户的默认角色
	 * 2018年7月25日14:39:20
	 * @author liutao
	 */
	public void saveDefaultRole(){
		String roleId = getPara("id");
		String userId = getCurrentUser().getStr("USER_NO");
		boolean flag = new UserServer().saveDefaultRole(userId, roleId);
		setAttr("flag", flag);
		renderJson();
	}
	
	
	
	/**
	 * 首次登陆修改机构数据
	 */
	public void  saveSetupUser(){
		String orgId = getPara("orgId");
		Record user = getCurrentUser();
		String id = user.getStr("ID");
		String orgnum = user.getStr("orgnum"); 
		String model = getPara("model");
		int cut = 0;
		//修改用户的业务模块
		Db.update("update sys_user_info set model = ? where id = ?", model,id);
		if(AppUtils.StringUtil(orgnum)==null){//如果该用户机构在本系统中不存在，允许修改
			cut = Db.update("update sys_user_info set org_id = ? where id = ?", orgId,id);
		}else{
			cut = 1;
		}
		
		setAttr("flag", cut == 1? true:false);
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"),String.valueOf(getSession().getAttribute("currentMenu")),"1", "首页-首次登陆");
		renderJson();
	}
	
	//updateMode
	public void  updateMode(){
		Record user = getCurrentUser();
		String id = user.getStr("ID");
		String model = getPara("model");
		int cut = 0;
		//修改用户的业务模块
		cut=Db.update("update sys_user_info set model = ? where id = ?", model,id);
		setAttr("flag", cut == 1? true:false);
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"),String.valueOf(getSession().getAttribute("currentMenu")),"1", "修改业务模块");
		renderJson();
	
	}
	
	public static void main(String[] args) throws Exception {
		findUser();
	}
	
	public static void findUser() throws Exception {
		BufferedWriter roleWriter = new BufferedWriter(new FileWriter(new File("E:\\zx\\updateRoleInfo.sql")));
		String url = "jdbc:oracle:thin:@22.200.142.46:1521:pccm";
		String driverName = "oracle.jdbc.driver.OracleDriver";
		String user = "pccm";
		String password = "pccm_2016";
		
		Class.forName(driverName);
		Connection conn = DriverManager.getConnection(url, user, password);
		String sql = "select soi.id, soi.by5 from sys_org_info soi ";
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()){
			String id = rs.getString("id");
			String by5 = rs.getString("by5");
			roleWriter.write("update sys_org_info set by5='" + by5 + "' where id='" + id + "';\n");
		}
		rs.close();
		roleWriter.flush();
		roleWriter.close();
		stmt.close();
		conn.close();
	}
}
