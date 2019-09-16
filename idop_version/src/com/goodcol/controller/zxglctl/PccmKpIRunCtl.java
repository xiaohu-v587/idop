/**
 * 
 */
package com.goodcol.controller.zxglctl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.anatation.RouteBind;

/**
 * @author dinggang
 *
 */
@RouteBind(path = "/pccm_kpi_run")
@Before({ ManagerPowerInterceptor.class,OperationLogRecordInterceptor.class})
public class PccmKpIRunCtl extends BaseCtl{
	/**
	 * 列表页面
	 */
	@Override
	public void index() {
		render("index.jsp");
	}
	/**
	 * 获取重跑数据
	 */
	public void getList(){
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		List<String> listStr = new ArrayList<String>();
		String sql = "select g.id,(case when g.quartz_status='0' then '待跑批' when g.quartz_status='1' then '跑批成功' else '跑批失败' end) as quratz_status,g.run_time,g.start_time,g.end_time";
		String extrasql = " from pccm_kpi_run g order by g.run_time desc";
		Page<Record> r = Db.use("default").paginate(pageNum, pageSize, sql,
				extrasql, listStr.toArray());
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		renderJson();
	}
	
	/**
	 * kpi重跑页面
	 */
	public void form() {
		render("form.jsp");
	}
	/**
	 * 重跑数据
	 */
	public void save(){
		String run_time=getPara("run_time");
		Db.update("insert into pccm_kpi_run (id,quartz_status,run_time,create_time) values(?,?,?,?)",new Object[]{AppUtils.getStringSeq(),"0",run_time,BolusDate.getDateTime("yyyy-MM-dd")});
		setAttr("flag",1);
		renderJson();
	}
	public void getDetail(){
		String year_month=BolusDate.getDateTime("yyyy-MM");
		setAttr("year_month",year_month);
		renderJson();
	}
}
