/**
 * 
 */
package com.goodcol.server.zxglserver;

import java.util.List;

import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.job.PccmKpiRunJob;
import com.goodcol.util.KplUtil;
import com.goodcol.util.date.BolusDate;

/**
 * @author dinggang
 * 
 */
public class PccmKpiRunServer {
	private static Logger log=Logger.getLogger(PccmKpiRunServer.class);
	/**
	 * 查找当天提交的批次数据，并重跑kpi
	 * @param nowDate
	 */
	public void kpITask(String nowDate) {
		List<Record> records = Db.use("default").find(
				"select * from pccm_kpi_run where create_time=?",
				new Object[] { nowDate });
		if (records != null && !records.isEmpty()) {
			for (int i = 0; i < records.size(); i++) {
				// -----开始时间-----
				String start_time = "";
				// -----结束时间-----
				String end_time = "";
				//跑批状态
				int quartz_status = 0;
				String excuTime="";
				try{
					start_time = BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
					Record record = records.get(i);
					excuTime = record.getStr("run_time");
					KplUtil.kplTask(excuTime);
					quartz_status=1;
				}catch(Exception e){
					quartz_status=2;
					e.printStackTrace();
					log.error(nowDate+"重跑"+excuTime+"月份数据，重跑失败，发生异常："+e.getMessage());
				}finally{
					end_time = BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
					Db.use("default").update("update pccm_kpi_run set start_time=?,end_time=?,quartz_status=? where create_time=?",new Object[]{start_time,end_time,quartz_status,nowDate});
					log.warn(nowDate+"从"+start_time+"开始重跑"+excuTime+"月份数据,在"+end_time+"结束重跑");
				}
				
			}
		}
		
	}		
}
