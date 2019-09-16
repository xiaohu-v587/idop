package com.goodcol.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.ext.plugin.quartz.QuartzSchedule;

/**
 * 定时任务监控
 *
 * @author puyu
 */
@RouteBind(path = "/scheduletask")
@Before( { ManagerPowerInterceptor.class })
public class ScheduletaskCtl extends BaseCtl{
	
	 protected Logger log =  Logger.getLogger(ScheduletaskCtl.class);
	 
	    @Override
		public void index() {
	    	render("index.jsp");
		}
	    
	    
	    /**
	     * 显示所有的定时任务
	     */
	    public void list(){
			int pageNumber = getParaToInt("pageIndex") + 1;
			int pageSize = getParaToInt("pageSize", 10);
			String rwmc=getPara("rwmc");
			String rwqdsj=getPara("rwqdsj");
			String rwjssj=getPara("rwjssj");
			StringBuffer sb = new StringBuffer();
			List<String> listStr = new ArrayList<String>();
			if (AppUtils.StringUtil(rwmc) != null) {
				sb.append(" and rwmc like ? ");
				listStr.add("%" + rwmc.trim() + "%");
			}
			if (AppUtils.StringUtil(rwqdsj) != null) {
				sb.append(" and to_date(rwkssj, 'yyyy-mm-dd hh24:mi:ss')>=to_date(?, 'yyyy-mm-dd hh24:mi:ss')");
				listStr.add( rwqdsj );
			}
			if (AppUtils.StringUtil(rwjssj) != null) {
				sb.append(" and to_date(rwjssj, 'yyyy-mm-dd hh24:mi:ss')<=to_date(?, 'yyyy-mm-dd hh24:mi:ss')");
				listStr.add( rwjssj);
			}
			String sql = "select * ";
			String extrasql = " from SYS_SCHEDULETASK_MONITOR where 1=1 " + sb.toString()
					+ " order by rwkssj desc, rwmc asc";
			Page<Record> r = Db.paginate(pageNumber, pageSize, sql, extrasql,
					listStr.toArray());
			List<Record> list = r.getList();
			if (list.size() < 1) {
				Page<Record> r1 = Db.paginate(1, 10, sql, extrasql,
						listStr.toArray());
				list = r1.getList();
			}
			setAttr("data", list);
			setAttr("total", r.getTotalRow());
			LoggerUtil.getIntanceof().saveLogger(
					getCurrentUser().getStr("USER_NO"), "定时任务监控", "3", "定时任务监控-查询");
			renderJson();
	    	
	    }
	    
	        
	    
	    public void restart(){
	    	String rwbm=getPara("rwbm");
	    	Record record=Db.findFirst("select * from SYS_SCHEDULE_TASK where rwbm=?",rwbm);
	    	if(record.getStr("rwzt").equals("1")){
	    		setAttr("yxsj", "该任务以是停止状态，请先运行该任务");
		    	renderJson();
	    	}else{
	    	String rwlm=record.getStr("rwlm");
	    	String rwmc=record.getStr("rwmc");
	    	SimpleDateFormat df = new SimpleDateFormat("ss mm HH dd MM ? yyyy");
	    	SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	Date now = new Date();
	    	Date afterDate = new Date(now .getTime() + 60000);
	    	String dqsj=df.format(afterDate);
	    	String xssj=df1.format(afterDate);
	    	String lsrwbm=rwbm+dqsj;
	    	String lsrwmc=rwmc+dqsj;
	    	String rwzt="2";//0：启动，1：停止，2：临时任务
	    	Record r=new Record();
	    	r.set("id",AppUtils.getStringSeq() ).set("RWBM", lsrwbm).set("RWMC", lsrwmc)
	    	.set("RWQDSJ", dqsj).set("RWLM", rwlm).set("RWZT", rwzt).set("BZ", "临时任务")
	    	.set("CJR", getCurrentUser().getStr("USER_NO")).set("CJSJ", DateTimeUtil.getTime())
	    	.set("ZHXGR", getCurrentUser().getStr("USER_NO")).set("ZHXGSJ", DateTimeUtil.getTime())
	    	.set("SFJK", "0");
	    	Db.save("SYS_SCHEDULE_TASK", r);
	    	QuartzSchedule.addJob(rwlm,lsrwmc,dqsj);
	    	LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "临时定时任务", "4", "定时任务-新增");
	    	setAttr("yxsj", "启动成功,重新运行时间为："+xssj);
	    	renderJson();
	    	}
	    }
	    
}
