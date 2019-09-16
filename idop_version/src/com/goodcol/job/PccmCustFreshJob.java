/**
 * 
 */
package com.goodcol.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.server.zxglserver.PccmCustFreshServer;
import com.goodcol.server.zxglserver.PccmCustPoolServer;
import com.goodcol.server.zxglserver.PccmCustTranServer;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.TimeUtil;
import com.goodcol.util.YiApiTool;
import com.goodcol.util.date.BolusDate;

/**
 * @author dinggang
 * 
 */
public class PccmCustFreshJob implements Job {
	public static Logger log = Logger.getLogger(PccmCustFreshJob.class);
	private ExecutorService executorService;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("定时任务：调用企查查开始：" + DateTimeUtil.getTime());
		log.warn("企查查开始---" + BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		// 查询结束后开始处理数据
		long currentStart = System.currentTimeMillis();
		String nowdate = BolusDate.getDateTime("yyyyMMdd");
		String yestoday = DateTimeUtil.getYestoday();
		try {
			int pageIndex = 1;
			PccmCustFreshServer pccmCustFreshServer = new PccmCustFreshServer();
			String url = pccmCustFreshServer.findXydUrl();
			log.warn("信雅达转发地址:" + url);
			String xyd_edmp_key = pccmCustFreshServer.findXydKey();
			log.warn("信雅达Key:" + xyd_edmp_key);
			String yj_api_refresh_url = pccmCustFreshServer.findYjUrl();
			String yiApiUrl = yj_api_refresh_url +"JS"+"&startDateFrom="+yestoday+"&startDateTo="+yestoday+"&pageSize=20"+"&pageIndex="+pageIndex;;
			log.warn("企查查新增企业URL:" + yiApiUrl);
			Map<String, Object> mp = YiApiTool.getCompanyDetail(yiApiUrl,url,xyd_edmp_key);
			String  status = (String)mp.get("Status");
			if(null!=status&&status.equals("200")){
				while(true){
					status = pccmCustFreshServer.searchFresh(String.valueOf(pageIndex++));
					if(null==status||"".equals(status)||!("200".equals(status)))
					break;
				}
			}else{
				log.warn("未查询到昨日新增企业数据" );
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(nowdate + "企查查发生异常:" + e.toString());
		}
		long currentEnd = System.currentTimeMillis();
		long milliSecond = currentEnd - currentStart;
		log.warn("企查查费时：" + milliSecond + "毫秒;");
		log.warn("企查查结束---" + BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		System.out.println("定时任务：调用企查查结束：" + DateTimeUtil.getTime());
	}
	
	
}
