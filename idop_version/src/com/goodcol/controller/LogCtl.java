package com.goodcol.controller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.core.aop.Before;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.Log4jPropertiesContent;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.OrgUtil;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.ext.render.excel.ExcelRender;

@RouteBind(path = "/log")
@Before({ ManagerPowerInterceptor.class, OperationLogRecordInterceptor.class })
public class LogCtl extends BaseCtl {
	
	@Override
	public void index() {
		render("index.jsp");
	}

	/***
	 * 操作日志列表
	 */
	//@LogBind(menuname = "操作日志查询", type = "3", remark = "操作日志信息-查询")
	public void listLog() {
		// 获取分页的索引和每页显示的条数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String type = getPara("type");
		String user_no = getPara("user_no");
		String create_time = getPara("create_time");
		String menuname = getPara("menuname");
		List<String> listStr = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		if (AppUtils.StringUtil(type) != null) {
			sb.append(" and l.type = ? ");
			listStr.add(type.trim());
		}
		if (AppUtils.StringUtil(user_no) != null) {
			sb.append(" and l.user_no like ? ");
			listStr.add("%" + user_no.trim() + "%");
		}
		if (AppUtils.StringUtil(menuname) != null) {
			sb.append(" and l.menuname like ? ");
			listStr.add("%" + menuname.trim() + "%");
		}
		if (AppUtils.StringUtil(create_time) != null) {
			sb.append(" and l.create_time like ? ");
			listStr.add("%"
					+ create_time.trim().substring(0, 10).replace("-", "")
					+ "%");
		}
		String sql = "select l.ID as id,l.USER_NO as user_no,to_date(l.CREATE_TIME,'yyyy-MM-dd HH24:mi:ss') as create_time,l.CREATE_TIME as create_time1,l.MENUNAME as menuname,l.TYPE as type,l.REMARK as remark ";
		String extrasql = " from SYS_LOG_INFO l WHERE 1=1 " + sb.toString()
				+ " order by l.create_time desc";
		Page<Record> r = Db.paginate(pageNum, pageSize, sql, extrasql,
				listStr.toArray());
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		renderJson();
	}

	/**
	 * 删除日志信息
	 */
	//@LogBind(menuname = "操作日志查询", type = "6", remark = "操作日志信息-删除")
	public void del() {
		String id = getPara("id");
		Db.deleteById("SYS_LOG_INFO", id);
	}

	/**
	 * 查看日志信息
	 */
	//@LogBind(menuname = "操作日志查询", type = "3", remark = "操作日志信息-查看")
	public void view() {
		// 根据单条记录的时间和log4j的生成规则获取日志文件
		String logOpreateTime = getPara("create_time").substring(0, 8);
		String suffix = "." + logOpreateTime;
		// log4j当天的日志文件是存储在gcds.log文件中的,前面的日志是通过追加后缀的形式生成log文件 
		String filePath = Log4jPropertiesContent.get("log4j.appender.file.File");
		String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
		if (!logOpreateTime.equals(DateTimeUtil.getShortChinaTime())) {
			fileName += suffix;
		}
		String uniCode = getPara("id");
		String logoInfo = getLogInfoByFile(filePath, uniCode, "0");
		if(logoInfo.split("&&").length>1){
			if(AppUtils.StringUtil(logoInfo.split("&&")[1]) != null){
				setAttr("logInfo", logoInfo.split("&&")[1]);
			}else{
				setAttr("logInfo", "<h2>无该操作日志的详细记录！</h2>");
			}
		}else{
			setAttr("logInfo", "<h2>无该操作日志的详细记录！</h2>");
		}
		
		setAttr("readline", logoInfo.split("&&")[0]);
		setAttr("fileName", fileName);
		setAttr("uniCode", uniCode);
		render("view.jsp");
	}

	/**
	 * 加载更多
	 */
	//@LogBind(menuname = "操作日志查询", type = "3", remark = "操作日志信息-加载更多")
	public void viewMore() {
		String uniCode = getPara("uniCode");
		// 加载更多的次数
		String number = getPara("number");
		String fileName = getPara("fileName");
		String filePath = Log4jPropertiesContent.get("log4j.appender.file.File");
		filePath = filePath.replace(filePath.substring(filePath.lastIndexOf("/")+1,filePath.length()), fileName);
		renderJson("loginfoMore", getLogInfoByFile(filePath, uniCode, number));
	}

	/***
	 * 读取日志文件获得日志信息
	 * 
	 * @param filePath
	 *            文件路径
	 * @param uniCode
	 *            每条操作记录对应的唯一值
	 * @param number
	 * @return
	 */
	private String getLogInfoByFile(String filePath, String uniCode,
			String number) {
		String webAppDir = this.getRequest().getSession().getServletContext().getRealPath("/").replace("WebContent", "");
		//获取日志的路径
		File file = new File(webAppDir + filePath);
		if (!file.exists()) {
			return "<h2>未找到相关日志文件!</h2>";
		}
		StringBuffer sb = new StringBuffer();
		//上一次读取的文件位置
		int lastReadLine = AppUtils.StringUtil(getPara("lastreadline")) == null ? 0:Integer.parseInt(getPara("lastreadline"));
		//读取的第多少行
		long readLine = 0;
		//每次读取的行数
		int readNum = 100;
		long times = Long.parseLong(number);
		//日志文件中是否存在唯一key值
		boolean existKey = false;
		try {
			// HttpServletResponse response = this.getResponse();
			// ServletOutputStream out = response.getOutputStream();
			FileInputStream in = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, "UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				//每次读取上次读取位置后面100行停止读取
				if (readLine == lastReadLine+100) {
					break;
				}
				//当前行存在唯一值，从该行开始读取，readLine=1，将existKey标记为true
				if (line.contains(uniCode)) {
					if (times == 0) {
						sb.append(line).append("<br/>");
					}
					existKey = true;
					readLine = 1;
					continue;
				}
				//times=0表示首次查看日志详情，time>0表示加载更多
				if (times == 0) {
					if (readLine >= 1 && readLine < readNum) {
						if(!"".equals(line.trim())){
							sb.append(line).append("<br/>");
						}
						readLine++;
						continue;
					}
				} else {
					//加载更多从上一次加载的最后一行开始继续加载100行
					if(readLine >= lastReadLine && readLine<lastReadLine+100){
						if(!"".equals(line.trim())){
							sb.append(line).append("<br/>");
						}
						readLine++;
						continue;
					}
				}
				readLine++;
				if(!existKey){
					readLine--;
				}
			}
			// out.write(sb.toString().getBytes());
			// out.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return readLine+"&&"+sb.toString();
	}

	public Map<String, Object> organSql1() {
		// 获取页面输入查询条件
		String orgid = getPara("orgid1"); // 机构号 （下拉框）
		String jgh = getPara("jgh1"); // 机构（手输）
		String userno = getPara("userno1");// 用户号
		String name = getPara("name1"); // 用户姓名
		String startDate = getPara("startDate1"); // 开始日期
		String endDate = getPara("endDate1"); // 结束日期

		String user_no = getCurrentUser().getStr("USER_NO");
		List<Record> lr = Db
				.find("select ri.name,ri.role_level,ui.org_id from sys_role_info ri, sys_user_info ui where ui.role_id=ri.id and ui.user_no='"
						+ user_no + "'");
		String roleName = lr.get(0).getStr("NAME");
		System.out.println("当前用户的角色：" + roleName);
		String role_level = lr.get(0).getStr("role_level");
		System.out.println("当前用户的角色等级：" + role_level);
		String org_id = lr.get(0).getStr("org_id");
		System.out.println("当前用户的机构号：" + org_id);

		// String selectSql = "select ui.name name, li.* ";
		// String fromSql =
		// "from sys_user_info ui, yygl_log_info li where ui.user_no=li.user_no";

		String selectSql = " select u.name name, o.orgname,l.*,to_date(l.create_time,'yyyy-mm-dd hh24:mi:ss') as createtime  ";
		String extrasql = " from sys_user_info u , sys_log_info l , sys_org_info o ";
		// StringBuffer whereSql = new StringBuffer(
		// " where 1 = 1 and u.org_id = o.orgnum  and u.user_no = l.user_no ");
		StringBuffer whereSql = new StringBuffer(
				" where 1 = 1  and u.org_id = o.orgnum ");
		List<String> sqlStr = new ArrayList<String>();
		if (AppUtils.StringUtil(orgid) != null) {
			whereSql.append(" and u.org_id = ? ");
			sqlStr.add(orgid.trim());
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
		if (AppUtils.StringUtil(startDate) != null) {
			startDate = startDate.replace("-", "").replace(":", "")
					.replace(" ", "");
			whereSql.append(" and l.create_time >= ? ");
			sqlStr.add(startDate.trim());
		}
		if (AppUtils.StringUtil(endDate) != null) {
			endDate = endDate.replace("-", "").replace(":", "")
					.replace(" ", "");
			endDate = endDate.substring(0, 8) + "235959";
			whereSql.append(" and l.create_time <= ? ");
			sqlStr.add(endDate.trim());
		}

		if (AppUtils.StringUtil(role_level) == null || role_level.equals("0")) {
			// 柜员只允许查询本人
			whereSql.append(" and l.user_no = ?  and u.user_no=?");
			sqlStr.add(user_no);
			sqlStr.add(user_no);
		} else if (role_level.equals("1")) {
			// 支行查询机构号相等
			whereSql.append(" and u.user_no=l.user_no and u.org_id = '"
					+ org_id + "' ");
		} else if (role_level.equals("2")) {
			// 分行查询机构号相等或下级
			whereSql.append("and u.user_no=l.user_no and u.org_id in ( "
					+ OrgUtil.getIntanceof().getSubOrgId(org_id) + " ) ");
		} else if (role_level.equals("3")) {
			// 总行和系统管理员，查询所有
			whereSql.append(" and u.user_no=l.user_no ");
		}

		extrasql += whereSql.toString();
		// selectSql,extrasql,sqlStr 装入map，返回
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("selectSql", selectSql);
		map.put("extrasql", extrasql);
		map.put("sqlStr", sqlStr);
		// 打印日志
		// log.info("organSql--map:" + map);
		return map;
	}

	public Map<String, Object> organSql() {
		// 获取页面输入查询条件
		String orgid = getPara("orgid"); // 机构号 （下拉框）
		String jgh = getPara("jgh"); // 机构（手输）
		String userno = getPara("userno");// 用户号
		String name = getPara("name"); // 用户姓名
		String startDate = getPara("startDate"); // 开始日期
		String endDate = getPara("endDate"); // 结束日期

		String user_no = getCurrentUser().getStr("USER_NO");
		List<Record> lr = Db
				.find("select ri.name,ri.role_level,ui.org_id from sys_role_info ri, sys_user_info ui where ui.role_id=ri.id and ui.user_no='"
						+ user_no + "'");
		String roleName = lr.get(0).getStr("NAME");
		System.out.println("当前用户的角色：" + roleName);
		String role_level = lr.get(0).getStr("role_level");
		System.out.println("当前用户的角色等级：" + role_level);
		String org_id = lr.get(0).getStr("org_id");
		System.out.println("当前用户的机构号：" + org_id);

		// String selectSql = "select ui.name name, li.* ";
		// String fromSql =
		// "from sys_user_info ui, yygl_log_info li where ui.user_no=li.user_no";

		String selectSql = " select u.name name, o.orgname,l.*,to_date(l.create_time,'yyyy-mm-dd hh24:mi:ss') as createtime  ";
		String extrasql = " from sys_user_info u , yygl_log_info l , sys_org_info o ";
		// StringBuffer whereSql = new StringBuffer(
		// " where 1 = 1 and u.org_id = o.orgnum  and u.user_no = l.user_no ");
		StringBuffer whereSql = new StringBuffer(
				" where 1 = 1  and u.org_id = o.orgnum ");
		List<String> sqlStr = new ArrayList<String>();
		if (AppUtils.StringUtil(orgid) != null) {
			whereSql.append(" and u.org_id = ? ");
			sqlStr.add(orgid.trim());
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
		if (AppUtils.StringUtil(startDate) != null) {
			startDate = startDate.replace("-", "").replace(":", "")
					.replace(" ", "");
			whereSql.append(" and l.create_time >= ? ");
			sqlStr.add(startDate.trim());
		}
		if (AppUtils.StringUtil(endDate) != null) {
			endDate = endDate.replace("-", "").replace(":", "")
					.replace(" ", "");
			endDate = endDate.substring(0, 8) + "235959";
			whereSql.append(" and l.create_time <= ? ");
			sqlStr.add(endDate.trim());
		}

		if (AppUtils.StringUtil(role_level) == null || role_level.equals("0")) {
			// 柜员只允许查询本人
			whereSql.append(" and l.user_no = ?  and u.user_no=?");
			sqlStr.add(user_no);
			sqlStr.add(user_no);
		} else if (role_level.equals("1")) {
			// 支行查询机构号相等
			whereSql.append(" and u.user_no=l.user_no and u.org_id = '"
					+ org_id + "' ");
		} else if (role_level.equals("2")) {
			// 分行查询机构号相等或下级
			whereSql.append("and u.user_no=l.user_no and u.org_id in ( "
					+ OrgUtil.getIntanceof().getSubOrgId(org_id) + " ) ");
		} else if (role_level.equals("3")) {
			// 总行和系统管理员，查询所有
			whereSql.append(" and u.user_no=l.user_no ");
		}
		whereSql.append(" order by createtime desc ");
		extrasql += whereSql.toString();
		// selectSql,extrasql,sqlStr 装入map，返回
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("selectSql", selectSql);
		map.put("extrasql", extrasql);
		map.put("sqlStr", sqlStr);
		// 打印日志
		// log.info("organSql--map:" + map);
		return map;
	}

	/**
	 * 下载用户数据，保存为excel文件
	 */
	@SuppressWarnings("unchecked")
	public void download() {

		// 获取查询参数
		Map<String, Object> map = organSql1();
		// 从数据库查询指定条数的用户记录
		// List<Record> lr = Db.find(
		// (String) map.get("selectSql") + (String) map.get("extrasql"),
		// ((List<String>) map.get("sqlStr")).toArray());

		List<Record> lr = null;
		Record r = Db.findFirst(
				"select count(*) as count1 " + (String) map.get("extrasql"),
				((List<String>) map.get("sqlStr")).toArray());
		String count = r.getBigDecimal("COUNT1").toString();
		int a = Integer.parseInt(count);

		if (a == 0) {
			renderText("<script type='text/javascript'>parent.callback(\"当前没有数据可以下载!\"); </script>");
			return;
		}

		if (a < 3000) {
			lr = Db.find(
					(String) map.get("selectSql")
							+ (String) map.get("extrasql"),
					((List<String>) map.get("sqlStr")).toArray());
		} else {
			renderText("<script type='text/javascript'>parent.callback(\"下载数据不能超过3000条!\"); </script>");
			return;
		}

		// 指定用哪些查询出来的字段填充excel文件
		String[] headers = { "user_no", "name", "orgname", "CREATETIME",
				"MENUNAME", "REMARK" };

		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = { "员工号", "员工姓名", "所属机构", "操作时间", "操作菜单", "操作描述" };

		String fileName = "";
		try {
			fileName = new String("操作日志.xls".getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
		}

		// 转换成excel
		ExcelRender er = new ExcelRender(fileName, columns, headers, lr,
				getResponse());
		er.render();
		// 打印日志
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"), "操作日志查询", "7", "操作日志-下载");

		renderNull();
	}

}
