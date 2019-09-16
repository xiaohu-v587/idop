/**
 * 
 */
package com.goodcol.job;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.server.zxglserver.PccmCustPoolServer;
import com.goodcol.server.zxglserver.PccmCustTranServer;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.date.BolusDate;

/**
 * @author dinggang
 * 
 */
public class PccmCustTranJob implements Job {
	public static Logger log = Logger.getLogger(PccmCustTranJob.class);
	private ExecutorService executorService;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("定时任务：调用企查查开始：" + DateTimeUtil.getTime());
		log.warn("企查查开始---" + BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		// 查询结束后开始处理数据
		long currentStart = System.currentTimeMillis();
		String nowdate = BolusDate.getDateTime("yyyyMMdd");
		try {
			PccmCustTranServer pccmCustTranServer = new PccmCustTranServer();
			String url = pccmCustTranServer.findXydUrl();
			log.warn("信雅达转发地址:" + url);
			String xyd_edmp_key = pccmCustTranServer.findXydKey();
			log.warn("信雅达Key:" + xyd_edmp_key);
			List<Record> pools = pccmCustTranServer.findPoolRecords();
			String yj_api_detail_url = pccmCustTranServer.findYjUrl();
			int updateThreadNum = 1;// 处理数据线程总数
			executorService = Executors.newFixedThreadPool(updateThreadNum);
			if (pools != null && !pools.isEmpty()) {
				for (int i = 0; i < pools.size(); i++) {
					executorService.execute(new PccmCustPoolServer(pools, i,
							yj_api_detail_url,url,xyd_edmp_key));
				}
			}
			executorService.shutdown();
			while (!executorService.isTerminated()) {
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(nowdate + "企查查发生异常:" + e.toString());
		}
		
		String sql = "up  update pccm_cust_pool t set t.workscope = replace(t.workscope,chr(10),'') where t.incflg <> '4' and t.workscope is not null";
		Db.use("default").update(sql);
		sql = "update pccm_cust_pool t set t.workscope = replace(t.workscope,chr(13),'') where t.incflg <> '4' and t.workscope is not null";
		Db.use("default").update(sql);
		sql = "update pccm_cust_pool t set t.relate_cust_name = replace(t.relate_cust_name,chr(10),'') where t.incflg <> '4' and t.relate_cust_name is not null";
		Db.use("default").update(sql);
		sql = "update pccm_cust_pool t set t.relate_cust_name = replace(t.relate_cust_name,chr(13),'') where t.incflg <> '4' and t.relate_cust_name is not null";
		Db.use("default").update(sql);
		long currentEnd = System.currentTimeMillis();
		long milliSecond = currentEnd - currentStart;
		log.warn("企查查费时：" + milliSecond + "毫秒;");
		log.warn("企查查结束---" + BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		System.out.println("定时任务：调用企查查结束：" + DateTimeUtil.getTime());
	}
}
