package com.goodcol.controller.dop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.ext.anatation.RouteBind;

/**
 * 预警阀值
 * @author start
 *
 */
@RouteBind(path = "/warningthreshold")
@Before({ManagerPowerInterceptor.class})
public class WarningThresholdCtl extends BaseCtl {
	
	// 记录日志用
	public static Logger log = Logger.getLogger(WarningThresholdCtl.class); 

	@Override
	public void index() {
		render("index.jsp");
	}

	/**
	 * 查询所有预警阀值信息
	 */
	public void getList() {
		// 获取查询条件
		String moduletype = getPara("moduletype");
		String targetno = getPara("targetno");
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		
		// 查询语句
		String selectSql = " select t.module_type moduletype, t.target_type_no targettypeno, t.target_type warningtype," +
				" t.target_no targetno, t.target_val val, t.target_val1 val1, t.target_val2 val2, t.target_val3 val3," +
				" to_date(t.effective_date, 'yyyymmdd') effdate";
		String extraSql = " FROM idop_param_threshold t ";

		StringBuffer whereSql = new StringBuffer(" WHERE 1 = 1 ");
		List<String> sqlStr = new ArrayList<String>();
		// 查询条件
		if (AppUtils.StringUtil(moduletype) != null) {
			whereSql.append(" AND  t.module_type = ? ");
			sqlStr.add(moduletype);
		}
		if (AppUtils.StringUtil(targetno) != null) {
			whereSql.append("  and t.target_no like ? ");
			sqlStr.add("%"+targetno+"%");
		}
		//只需要指标编号不重复的记录
		whereSql.append(" and t.target_no in(select target_no from IDOP_PARAM_THRESHOLD group by target_no having count(target_no) = 1) ");
		extraSql += whereSql.toString();
		// 查询
		Page<Record> r = Db.paginate(pageNum, pageSize, selectSql,extraSql, sqlStr.toArray());
		// 赋值
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		// 打印日志
		log.info("getList--r.getList():" + r.getList());
		log.info("getList--r.getTotalRow():" + r.getTotalRow());
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "预警阀值设置", "3", "预警阀值设置-查询");
		log.info("预警阀值设置-查询");
		// 返回json数据
		renderJson();
	}
	
	public void toEdit() {
		render("form.jsp");
	}
	
	public void getThresholdByKeys() {
		String moduletype = getPara("moduletype");
		String targetno = getPara("targetno");
		String targettypeno = getPara("targettypeno");
		String effdate = getPara("effdate");
		List<String> sqlStr = new ArrayList<String>();
		// 查询语句
		String cmd = " select t.module_type moduletype, t.target_type_no targettypeno, t.target_type warningtype," +
				" t.target_no targetno, t.target_val val, t.target_val1 val1, t.target_val2 val2, t.target_val3 val3," +
				" t.effective_date effdate" +
				" from idop_param_threshold t";
		StringBuffer whereSql = new StringBuffer(" where 1 = 1");
		if (AppUtils.StringUtil(moduletype) != null) {
			whereSql.append(" and  t.module_type = ? ");
			sqlStr.add(moduletype);
		}
		if (AppUtils.StringUtil(targetno) != null) {
			whereSql.append("  and t.target_no = ? ");
			sqlStr.add(targetno);
		}
		if (AppUtils.StringUtil(targettypeno) != null) {
			whereSql.append("  and t.target_type_no = ? ");
			sqlStr.add(targettypeno);
		}
		whereSql.append(" and t.effective_date = ?");
		sqlStr.add(effdate);
		cmd += whereSql.toString();
		// 查询
		Record r = Db.findFirst(cmd, sqlStr.toArray());
		// 赋值
		setAttr("data", r);
		log.info("预警阀值设置-编辑");
		// 返回json数据
		renderJson();
	}
	
	public void update() {
		String moduletype = getPara("moduletype");
		String targetno = getPara("targetno");
		String targettypeno = getPara("targettypeno");
		String val = getPara("val");
		String val1 = getPara("val1");
		String val2 = getPara("val2");
		String val3 = getPara("val3");
		String effdate = getPara("effdate").replaceAll("-", "");
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(calendar.DATE,1);
		String newEffdate = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
		List<String> sqlStr = new ArrayList<String>();
		sqlStr.add(val);
		sqlStr.add(val1);
		sqlStr.add(val2);
		sqlStr.add(val3);
		sqlStr.add(newEffdate);
		String updateSql = "update idop_param_threshold t set t.target_val = ?, target_val1 = ?, t.target_val2 = ?, t.target_val3 = ?, t.effective_date = ? ";
		StringBuffer whereSql = new StringBuffer(" where 1 = 1 ");
		if (AppUtils.StringUtil(moduletype) != null) {
			whereSql.append(" and  t.module_type = ? ");
			sqlStr.add(moduletype);
		}
		if (AppUtils.StringUtil(targetno) != null) {
			whereSql.append("  and t.target_no = ? ");
			sqlStr.add(targetno);
		}
		if (AppUtils.StringUtil(targettypeno) != null) {
			whereSql.append("  and t.target_type_no = ? ");
			sqlStr.add(targettypeno);
		}
		whereSql.append(" and t.effective_date = ?");
		sqlStr.add(effdate);
		updateSql += whereSql.toString();
		Db.update(updateSql, sqlStr.toArray());
		// 记录日志
		log.info("预警阀值设置-修改");
		renderJson();
	}
}
