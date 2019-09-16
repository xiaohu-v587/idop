package com.goodcol.controller;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.CacheUtil;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.Dict;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.RandomJpg;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.safe.MD5;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RouteBind(path = "/", viewPath = "/login")
public class MainCtl extends BaseCtl {

	public static Logger log = Logger.getLogger(MainCtl.class); // 记录日志用

	public void index() {
		log.info("开始登录");
		Record m = getCurrentUser();
		
		if (m == null) {
			render("login.jsp");
		} else {
			
				List<Record> menus = fetchMenu(this.getCookie("user_token"),
						m.getStr("ID"));
				if (menus != null && menus.isEmpty() == false) {
					setAttr("menus", menus);
					setAttr("user", m);
					renderJsp("main.jsp");
				} else {
					render("login.jsp");
				}
			
			/*
			 * //判断是否需要进行角色设置 String date = DateTimeUtil.getPreviousDate(30);
			 * boolean flag = selectUserLogInfo(m.getStr("USER_NO"), date);
			 * if(flag){ //查询角色的审批有没有通过 flag =
			 * selectApplyRoleByUID(m.getStr("USER_NO")); if(flag){
			 * //角色申请通过，进入首页 List<Record> menus =
			 * fetchMenu(this.getCookie("user_token"), m.getStr("ID")); if
			 * (menus != null && menus.isEmpty() == false) { setAttr("menus",
			 * menus); setAttr("user", m); renderJsp("main.jsp"); } else {
			 * render("login.jsp"); } }else{ setAttr("name", m.getStr("name"));
			 * renderJsp("/pages/zxrole/roleSetup.jsp"); } }else{
			 * setAttr("name", m.getStr("name"));
			 * renderJsp("/pages/zxrole/roleSetup.jsp"); }
			 */
		}
	}

	public void login4single() {
		render("login_single.jsp");
	}

	public void error() {
		render("error.jsp");
	}

	public void login4singleInit() {
		String userid = getPara("userid");
		String ticket = getPara("ticket");
		log.info("userid:" + userid);

		// 先根据用户和密码进行查询
		Record m1 = Db.findFirst("select * from sys_user_info where user_no = ?   and IS_TELLER='1'",
						new Object[] { userid });
		if (m1 != null) {
			if (StringUtils.isBlank(m1.getStr("ROLE_ID")) || "null".equals(m1.getStr("ROLE_ID"))) {
				Record userRole = Db.use("default").findFirst("select * from SYS_USER_ROLE where user_id ='" + userid + "' order by role_id desc");
				if (userRole != null) {
					Db.use("default").update("update sys_user_info set ROLE_ID='" + userRole.getStr("ROLE_ID") + "' where user_no='" + userid + "'");
				}
			}
			
			// 在关联角色和机构进行查询,增加is_teller用以区分是行政还是物业用户。
			Record m = Db.findFirst("select a.id as id,a.stat as stat, a.user_no as user_no , "
									+ "a.org_id as org_id ,a.name as name , b.orgname as org_name, "
									+ "b.orgnum as orgnum,b.id as borgid, c.name as rolename, "
									+ "c.id as roleid,c.role_level as role_level,c.name as role_name,"
									+ "is_teller,A.WYID,A.XQID,b.bancsid from sys_user_info a "
									+ "left join sys_org_info b on a.org_id = b.id "
									+ "left join sys_role_info c on a.role_id=c.id "
									+ "where 1=1 and a.user_no=? ",
							new Object[] { userid });
		
			if (m != null) {
				String stat = m.get("stat");
				if (AppUtils.StringUtil(stat) == null) {
					renderFailJsonMsg("用户状态异常！");
					return;
				}
				if (stat.equals("1")) {

					renderFailJsonMsg("该用户已被停用！");
					return;
				}
				if (stat.equals("2")) {

					renderFailJsonMsg("该用户已被锁定！");
					return;
				}
				
				m.set("MAX_PERMI_ORGNUM", AppUtils.getOrgNumByUser(m)) ;
				// String nowuser_token = this.getRequest().getRemoteAddr()
				// + "_" + username;
				String nowuser_token = this.getRequest().getRemoteAddr() + "_"
						+ System.currentTimeMillis();
				setCookie("user_token", nowuser_token, 10000, "/");
				// setCookie("ticket",ticket,10000);
				getSession().setAttribute("ticket", ticket);
				// getSession().removeAttribute(nowuser_token);
				/** 唯一登录，即同一用户只可在一处登录 */
				// 判断是否开启redis缓存
				boolean single = true;
				if (single) {
					Set<String> sessionSet = (Set<String>) CacheUtil.getCache(
							"clientSet", this.getRequest());
					if (sessionSet != null && sessionSet.isEmpty() == false) {
						Iterator<String> it = sessionSet.iterator();
						while (it.hasNext()) {
							String user_token = it.next();
							Record r = (Record) CacheUtil.getCache(user_token,
									this.getRequest());
							if (r != null)
								if (!user_token.equals(nowuser_token)
										&& r.get("user_no").equals(
												m.get("user_no"))) {
	
									CacheUtil.delCache(user_token,
											this.getRequest());
								}
						}
					}
				}

				/** 唯一登录结束 */
				CacheUtil.setCache(nowuser_token, m, Dict.SECONDS,
						this.getRequest());
				Record m2 = getCurrentUser();
				System.out.println(m2);
				// fetchMenu(nowuser_token, m.getStr("ID"));
				LoggerUtil.getIntanceof().saveLogger(m.getStr("user_no"), "登陆",
						"1", "登陆成功");
				Map param = (new HashMap<String, String>());
				param.put("userid", m.getStr("user_no"));
				// getSSOUserToken(param);
				// renderSuccessJsonMsg("登录成功");
				List<Record> menus = fetchMenu(this.getCookie("user_token"),
						m.getStr("ID"));
				if (menus != null && menus.isEmpty() == false) {
					setAttr("menus", menus);
					setAttr("user", m);
					render("main.jsp");
				} else {
					render("error.jsp");
				}
			} else {
				render("error.jsp");
			}
		} else {
			render("error.jsp");
		}

	}

	/**
	 * 创建 易慧品台SSO票据
	 * 
	 * @author yinxu
	 */
//	private String getSSOUserToken(Map map) {
//		String host = "22.200.142.212";
//		String port = "8000";
//		String url = "http://"
//				+ host
//				+ ":"
//				+ port
//				+ "/Authentication.asmx/GetSSOUserToken?securityCode=yihui&userID=025621&appid=000493";
//		String res = HttpClientExample.getGetResponseWithHttpClient(url,
//				"utf-8");
//		System.out.println("res:" + res);
//		return res;
//	}

	public void main() {
		render("default.jsp");
	}

	public void tab() {
		renderJsp("tab.jsp");
	}

	public void welcome() {
		Record user = getCurrentUser();
		String rolename = user.getStr("role_name");
		if(rolename.contains("管理层")){
			setAttr("show", "true");
		}else{
			setAttr("show", "false");
		}
		String userno = user.getStr("USER_NO");
		
		String selSql = ("select r.main_page from sys_user_info t "+
						"left join sys_role_info r on t.role_id = r.id "+
						"where t.user_no = ?");
		Record rd = Db.findFirst(selSql,userno);
		String mainpage = rd.getStr("main_page");
		if(mainpage!=null&&!("").equals(mainpage)){
			if("0".equals(mainpage)){
				renderJsp("index.jsp");
			}else if("1".equals(mainpage)){
				renderJsp("../main/index.jsp");
				
			}
			else if("2".equals(mainpage)){
				renderJsp("../atticepointmode/attpoint.jsp");
			}
		
		}else{
			
			renderJsp("index.jsp");
		}
	}

	public void getList() {

		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);

		Page<Record> r = Db.paginate(pageNum, pageSize, "select *",
				"from sys_menu_info");
		setAttr("datas", r.getList());
		setAttr("total", r.getTotalRow());
		renderJson();
	}

	@SuppressWarnings("unchecked")
	public void login() {

		String username = getPara("username");
		String pwd = getPara("passwd");

		if (username == null || "".equals(username.trim()) || pwd == null
				|| "".equals(pwd)) {

			renderFailJsonMsg("用户名或密码不能为空");

			return;
		} else {
			// if ( "1".equals(pwd) )
			// pwd = "c4ca4238a0b923820dcc509a6f75849b";
			// else
			pwd = MD5.getMD5ofStr(pwd);
			// 先根据用户和密码进行查询
			Record m1 = Db.findFirst("select * from sys_user_info where user_no = ? and upper(pwd) = upper(?) and IS_TELLER='1'",
							new Object[] { username, pwd });
			if (m1 != null) {
				if (StringUtils.isBlank(m1.getStr("ROLE_ID"))||"null".equals(m1.getStr("ROLE_ID"))) {
					Record userRole = Db.use("default").findFirst("select * from SYS_USER_ROLE where user_id ='" + username + "' order by role_id desc");
					if (userRole != null) {
						Db.use("default").update("update sys_user_info set ROLE_ID='" + userRole.getStr("ROLE_ID") + "' where user_no='" + username + "'");
					}
				}
				
				// 在关联角色和机构进行查询,增加is_teller用以区分是行政还是物业用户。
				Record m = Db
						.findFirst(
								"select a.id as id,a.stat as stat, a.user_no as user_no , "
										+ "a.org_id as org_id ,a.name as name , b.orgname as org_name, "
										+ "b.orgnum as orgnum,b.id as borgid, c.name as rolename, "
										+ "c.id as roleid,c.role_level as role_level,c.name as role_name,"
										+ "is_teller,A.WYID,A.XQID,b.bancsid from sys_user_info a "
										+ "left join sys_org_info b on a.org_id = b.id "
										+ "left join sys_role_info c on a.role_id=c.id "
										+ "where 1=1 and a.user_no=? and upper(a.pwd)=upper(?)",
								new Object[] { username, pwd });
				
				if (m != null && StringUtils.isNotBlank(m.getStr("rolename"))) {
					String stat = m.get("stat");
					if (AppUtils.StringUtil(stat) == null) {
						renderFailJsonMsg("用户状态异常！");
						return;
					}
					if (stat.equals("1")) {
						renderFailJsonMsg("该用户已被停用！");
						return;
					}
					if (stat.equals("2")) {
						renderFailJsonMsg("该用户已被锁定！");
						return;
					}
					
					m.set("MAX_PERMI_ORGNUM", AppUtils.getOrgNumByUser(m)) ;
					
					// String nowuser_token = this.getRequest().getRemoteAddr()
					// + "_" + username;
					String nowuser_token = this.getRequest().getRemoteAddr()
							+ "_" + System.currentTimeMillis();
					setCookie("user_token", nowuser_token, 10000, "/");
					// getSession().removeAttribute(nowuser_token);
					/** 唯一登录，即同一用户只可在一处登录 */
					// 判断是否开启redis缓存
					boolean single = true;
					if (single) {
						Set<String> sessionSet = (Set<String>) CacheUtil
								.getCache("clientSet", this.getRequest());
						if (sessionSet != null && sessionSet.isEmpty() == false) {
							Iterator<String> it = sessionSet.iterator();
							while (it.hasNext()) {
								String user_token = it.next();
								Record r = (Record) CacheUtil.getCache(
										user_token, this.getRequest());
								if (r != null)
									if (!user_token.equals(nowuser_token)
											&& r.get("user_no").equals(
													m.get("user_no"))) {

										CacheUtil.delCache(user_token,
												this.getRequest());
										// getSession().removeAttribute("menu" +
										// user_token);
									}
							}
						}
					}

					/** 唯一登录结束 */
					CacheUtil.setCache(nowuser_token, m, Dict.SECONDS,
							this.getRequest());
					// fetchMenu(nowuser_token, m.getStr("ID"));
					LoggerUtil.getIntanceof().saveLogger(m.getStr("user_no"),
							"登陆", "1", "登陆成功");
					renderSuccessJsonMsg("登录成功");
				} else {

					renderFailJsonMsg("角色为空，请联系管理员分配角色！");
				}

			} else {

				renderFailJsonMsg("用户名或密码错误");
				return;
			}
		}
	}

	public void getmenu() {
		Record m = getCurrentUser();
		if (m == null) {

			renderFailJsonMsg("尚未登录");
			return;
		}
		List<Record> menu = fetchMenu(getCookie("user_token"), m.getStr("ID"));
		setAttr("datas", menu);
		renderJson();
	}

	private List<Record> fetchMenu(String user_token, String userid) {
		List<Record> menus = Db
				.find("select distinct menu.* from SYS_MENU_INFO menu join SYS_POWER_INFO power on menu.id = power.menuid and enable = '0' join SYS_USER_INFO users on ((users.id = power.objid and power.type = '0') or (power.type = '1' and users.role_id = power.objid) or (power.type = '2' and users.org_id = power.objid)) where users.id=? order by  menu.ordernum asc",
						userid);
		//List<Record> records = Db.find("select * from sys_crud_auto");
		List<Record> newMenus = new ArrayList<Record>();
		if (menus != null && !menus.isEmpty()) {
			for (int i = 0; i < menus.size(); i++) {
				Record record = menus.get(i);
				String id = record.getStr("id");
				/*if (records != null && !records.isEmpty()) {
					for (int j = 0; j < records.size(); j++) {
						Record r = records.get(j);
						String menuId = r.getStr("menu_id");
						if (id.equals(menuId)) {
							String start_status = r.getStr("start_status");
							if (start_status.equals("1")) {
								record.set("url", "module_index?menu_id="
										+ menuId);
							}
						}

					}
				}*/
				newMenus.add(record);
			}
		}
		// List<Record> menus = (List<Record>) getSession().getAttribute("menu"
		// + user_token);
		// if (menus == null || menus.isEmpty())
		// menus =
		// Db.find("select distinct menu.* from SYS_MENU_INFO menu join SYS_POWER_INFO power on menu.id = power.menuid join SYS_USER_INFO users on ((users.id = power.objid and power.type = '0') or (power.type = '1' and users.role_id = power.objid) or (power.type = '2' and users.org_id = power.objid)) where users.id=? order by  menu.ordernum asc",
		// userid);

		// if (menus != null && menus.isEmpty() == false) {
		// getSession().setAttribute("menu" + user_token, menus);
		// }
		return newMenus;
	}

	public void logout() {
		Record u = getCurrentUser();
		if (u == null) {
			renderSuccessJsonMsg("注销成功");
			return;
		}
		String userNo = getCurrentUser().getStr("USER_NO");
		if (userNo == null) {
			renderSuccessJsonMsg("注销成功");
			return;
		}
		String user_token = getCookie("user_token");
		if (user_token == null) {
			renderSuccessJsonMsg("注销成功");
			return;
		}
		CacheUtil.delCache(user_token, this.getRequest());
		// getSession().removeAttribute("menu" + user_token);
		setCookie("user_token", null, 0, "/");
		LoggerUtil.getIntanceof().saveLogger(userNo, "注销", "3", "注销成功");
		renderSuccessJsonMsg("注销成功");
	}

	public void jpg() {
		HttpServletResponse response = this.getResponse();
		HttpSession session = this.getSession();
		response.setContentType("image/jpeg");

		// 防止浏览器缓冲
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		BufferedImage image = new BufferedImage(RandomJpg.WIDTH,
				RandomJpg.HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		char[] rands = RandomJpg.getCode(4);
		RandomJpg.drawBackground(g);
		RandomJpg.drawRands(g, rands);
		g.dispose();
		try {
			ServletOutputStream out = response.getOutputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(image, "PNG", bos);
			byte[] buf = bos.toByteArray();
			response.setContentLength(buf.length);
			out.write(buf);
			bos.close();
			out.close();
			session.setAttribute("check", new String(rands).toLowerCase());
		} catch (Exception e) {
			log.error("mainctl-jpg", e);
		}
		renderNull();
	}

	/*****/
	/**
	 * 修改密码
	 */
	public void alterPwd() {
		render("alterPwd.jsp");
	}

	/**
	 * 验证原密码
	 */
	public void valiPwd() {
		// 获取前台数据
		String oldpwd = getPara("oldpwd");
		// 获取用户信息
		String userNo = getCurrentUser().getStr("USER_NO");
		List<Record> list = Db
				.find(" select * from sys_user_info where user_no = ? and upper(pwd) = upper(?) ",
						new Object[] { userNo, MD5.getMD5ofStr(oldpwd) });
		if (list != null && list.size() == 1) {
			setAttr("flag", "1");
		} else {
			setAttr("flag", "0");
		}
		renderJson();
	}

	/**
	 * 保存修改的密码
	 */
	public void saveAlterPwd() {
		// 获取前台数据
		JSONObject json = getJsonObj("data");
		String oldpwd = json.getString("oldpwd");
		String newpwd = json.getString("newpwd");
		// String renewpwd = json.getString("renewpwd");
		// 获取用户信息
		String userNo = getCurrentUser().getStr("USER_NO");

		int flag = -1;
		// 验证老密码
		List<Record> list = Db
				.find(" select * from sys_user_info where user_no = ? and upper(pwd) = upper(?) ",
						new Object[] { userNo, MD5.getMD5ofStr(oldpwd) });
		if (list == null || list.size() <= 0) {
			flag = -1;
		} else {
			// 老密码验证通过
			// 更新密码
			try {
				flag = Db.update(
						" update sys_user_info set PWD = ? where USER_NO = ? ",
						new Object[] { MD5.getMD5ofStr(newpwd), userNo });
			} catch (Exception e) {
				log.error("Mainctl-saveAlterPwd", e);
			}
			// 打印日志
			log.info("saveAlterPwd--flag:" + flag);
			// 记录操作日志
			LoggerUtil.getIntanceof().saveLogger(
					getCurrentUser().getStr("USER_NO"),
					String.valueOf(getSession().getAttribute("currentMenu")),
					"5", "首页-更新");
		}
		setAttr("flag", flag);
		renderJson();
	}

	/**
	 * 查询指定用户指定时间之内的登陆记录 2018年4月26日16:59:03
	 * 
	 * @author liutao
	 * @param userNo
	 *            用户id
	 * @param date
	 *            时间
	 * @return boolean 查询到数据大于0则指定时间之内有登陆记录 反之没有
	 */
	public boolean selectUserLogInfo(String userNo, String date) {
		String sql = "select count(1) as num from SYS_LOG_INFO a "
				+ "where a.user_no = ? and a.create_time >= ?  and a.type = '1'";
		Record r = Db.findFirst(sql, userNo, date);
		BigDecimal num = r.getBigDecimal("num");
		if (num.intValue()>1) {	//大于1 非 首次登陆
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 查询指定员工的最新的角色申请是否通过 2018年4月27日14:32:07
	 * 
	 * @author liutao
	 * @param userNo
	 * @return
	 */
	public boolean selectApplyRoleByUID(String userNo) {
		String sql = "select count(1) as num from gcms_role_apply a "
				+ "where a.user_id = ?";
		Record r = Db.findFirst(sql, userNo);
		BigDecimal num = r.getBigDecimal("num");
		if (num.intValue() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 查询是否已经进行过角色申请或者是否首次登录(登录周期超过三十天的也算是首次登录) 2018年6月28日09:59:16
	 * 
	 * @author liutao
	 */
	public void findApplyRoleByUserId() {
		Record m = getCurrentUser();
		String date = DateTimeUtil.getPreviousDate(30);
		boolean flag = selectUserLogInfo(m.getStr("USER_NO"), date);
		
		Record rd = Db.findFirst("select org_id from sys_user_info where user_NO = ? ",m.getStr("USER_NO"));
		
		Record rd2 = new Record();
		if(rd!=null){
			rd2 = Db.findFirst("select * from sys_org_info where orgnum =? ",rd.getStr("org_id"));
		}
		
		if (!flag||rd==null||rd2==null) {
			setAttr("flag", true);
		} else {
			setAttr("flag", false);
		}
//		if (flag) {
//			// 查询有没有对角色进行申请过(跳过的也算,也就是说只要数据库有该用户的数据就算进行申请过，不管审核状态，也不管是跳过的)
//			flag = selectApplyRoleByUID(m.getStr("USER_NO"));
//			if (!flag) {
//				setAttr("flag", true);
//			} else {
//				setAttr("flag", false);
//			}
//		} else {
//			setAttr("flag", true);
//		}
		renderJson();
	}
	/**
	 * 跳转到会话超时页面
	 */
	public void nosession(){
		render("../sessionTimeout.jsp");
	}
	
	/**
	 *去切换机构页面
	 */
	public void goSwitchOrg(){
		render("switchOrg.jsp");
	}
	
	public void loadOrgAndRole(){
		String userNo = getCurrentUser().getStr("USER_NO");
		//获取当前登录人的机构
		String orgNum = getCurrentUser().getStr("ORGNUM");
		List<Record> find = Db.find("select t1.orgnum,t2.orgname,t1.role_id,t3.name as rolename from sys_user_org_role  t1 "
				+ "left join sys_org_info t2 on t1.orgnum = t2.orgnum "
				+ "LEFT JOIN sys_role_info t3 on t1.role_id = t3.id "
				+ "where t1.user_no = ? and t2.orgnum != ? "
				+ "union all "
				+ "select t1.org_id as orgnum,t2.orgname,t1.role_id,t3.name as rolename from sys_user_info  t1 "
				+ "left join sys_org_info t2 on t1.org_id = t2.orgnum "
				+ "LEFT JOIN sys_role_info t3 on t1.role_id = t3.id "
				+ "where t1.user_no = ? and t2.orgnum !=?",
				userNo,orgNum,userNo,orgNum);
		
		JSONArray jsonArray = new JSONArray();
		Set<String> orgSet = new HashSet<String>(); //用来存放已经存进去的组织
		for (Record record : find) {
			String orgId = record.getStr("ORGNUM");
			JSONObject orgJson = new JSONObject();
			if(!orgSet.contains(orgId)){
				orgJson.put("id",orgId);
				orgJson.put("text",record.getStr("ORGNAME"));
				orgSet.add(orgId);
				jsonArray.add(orgJson);
			}
			JSONObject roleJson = new JSONObject();
			roleJson.put("id", record.getStr("ROLE_ID"));
			roleJson.put("text", record.getStr("ROLENAME"));
			roleJson.put("pid", orgId);
			jsonArray.add(roleJson);
		}
		renderJson(jsonArray);
	}
	
	/**
	 * 切换结构
	 */
	public void switchOrg(){
		String orgId = getPara("orgnum"); //要切换的机构
		String userNO = getCurrentUser().getStr("USER_NO");
		String curOrgnum = getCurrentUser().getStr("ORGNUM"); //当前登录的机构
		String sql = "select * from ( select u.id as id,u.stat as stat,u.user_no as user_no,u.name as name ,o.orgnum as org_id,u.oaname as oaname,o.orgname as org_name,o.orgnum as orgnum,o.id as borgid,r.name as rolename,r.id as roleid ,r.role_level as role_level,p.name as postname from sys_user_info u"
						+" left join sys_user_org_role ur  on u.user_no = ur.user_no"
						+" left join sys_post_info p on u.cur_post = p.id"
						+" left join sys_org_info o on ur.orgnum = o.orgnum"
						+" left join sys_role_info r on ur.role_id = r.id"
						+" where u.user_no=? and ur.orgnum=?"
						+" union all"
						+" select u.id as id,u.stat as stat, u.user_no as user_no ,u.name as name ,b.orgnum as org_id,u.oaname as oaname, b.orgname as org_name, b.orgnum as orgnum,b.id as borgid, c.name as rolename, c.id as roleid,c.role_level as role_level,d.name as postname from sys_user_info u "
						+" left join sys_post_info d on u.cur_post = d.id"
						+ " left join sys_org_info b on u.org_id = b.orgnum"
						+ " left join sys_role_info c on u.role_id=c.id"
						+ " where u.user_no=?) t where orgnum !=?";
		List<Record> find = Db.find(sql,userNO,orgId,userNO,curOrgnum);
		Record record = find.get(0);
		if(find.size() > 1){
			//说明在该机构下是多角色
			for(int i = 1;i<find.size(); i++){
				
				String roleid = find.get(i).getStr("ROLEID");
				String rolename = find.get(i).getStr("ROLENAME");
				if(StringUtils.isNotBlank(roleid) && StringUtils.isNotBlank(rolename)){
					record.set("ROLEID", record.getStr("ROLEID")+","+roleid);
					record.set("ROLENAME", record.getStr("ROLENAME")+","+rolename);
				}
			}
		}
		//将该信息存入session中
		String nowuser_token = this.getRequest().getRemoteAddr()+ "_" + System.currentTimeMillis();
		setCookie("user_token", nowuser_token, 10000, "/");
		getSession().setAttribute(nowuser_token, record);
		
		renderJson();
	}
}
