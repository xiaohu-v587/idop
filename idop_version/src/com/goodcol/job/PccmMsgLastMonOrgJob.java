/**
 * 
 */
package com.goodcol.job;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.server.zxglserver.PccmSendMessageServer;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.SendMessageUtil;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

/**
 * 上月经营结果统计-领导（所处机构）
 * 
 */
public class PccmMsgLastMonOrgJob extends OuartzJob {
	public static Logger log = Logger.getLogger(PccmMsgLastMonOrgJob.class);
	private static final String DEFAULT = "default";
	private static final String sm_job = "PccmMsgLastMonOrgJob";

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("触发事件--上月经营统计结果，推送领导（所处机构）begin"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		try {
			//判断是否工作日
			if(AppUtils.isMonMinWorkDay()){
				//领导（所处机构）
				String[] roleNames = {"领导-二级分行","领导-中心支行","领导-责任中心"};
				//机构级别
				String[] levelArr = {"2","3","4"};
				// 上月
				String timePoint = "3";
				Map<String, Object> map = null;
				for(int i=0;i<levelArr.length;i++){
					//机构数量
					//int orgCount = Db.use(DEFAULT).queryBigDecimal("select count(1) from sys_org_info o where o.by2='2'").intValue();
					List<Record> orglist = Db.use(DEFAULT).find(" select rank() over(order by latent_reach desc) as latent_order,  "
							+"        rank() over(order by market_reach desc) as market_order,  "
							+"        id, orgname, latent_reach,market_reach "
							+"   from (select id, orgname,  "
							+"                decode(sum(nvl(latent_sum_num, 0)), 0, 0, "
							+"                round(sum(nvl(latent_succ_num, 0)) / sum(nvl(latent_sum_num, 0)), 4) * 100) as latent_reach, "
							+"                decode(sum(nvl(market_sum_num, 0)), 0, 0, "
							+"                round(sum(nvl(market_succ_num, 0)) / sum(nvl(market_sum_num, 0)), 4) * 100) as market_reach "
							+"         from "
							+"         (select s.id, s.orgname, l.latent_sum_num, l.latent_succ_num,l.market_sum_num,l.market_succ_num "
							+"            from sys_org_info s "
							+"           inner join pccm_org_ladermap_info l  on s.id=l.org_id "
//							+"              on l.org_id in (select o.id from sys_org_info o where (by5 like '%' || s.id || '%' or id = s.id)) "
							+"           where s.by2 = '"+levelArr[i]+"' and l.time_point = '"+timePoint+"') group by id, orgname)");
					if (orglist != null && !orglist.isEmpty()) {
						for(Record org:orglist){
							String org_id = org.getStr("id");
							//领导
							List<Record> leaderList = Db.use(DEFAULT).find(" select u.user_no, u.phone, u.name "
									+"   from sys_user_info u "
									+"  inner join sys_user_role s "
									+"     on u.id = s.user_id "
									+"  inner join sys_role_info r "
									+"     on s.role_id = r.id "
									+"  where u.phone is not null "
									+"    and u.org_id in (select id from sys_org_info s where (s.by5 like ? or id = ?) and s.stat='1') "
									+"    and r.name = ? ", new Object[] { "%" + org_id + "%",org_id,roleNames[i] });
							if (leaderList != null && !leaderList.isEmpty()) {
								String sm_cont = "【公金联盟】战报来啦！" + org.getStr("orgname")
										+ "上月末商机客户转化率" + org.getBigDecimal("market_reach").doubleValue() + "，排名"
										+ org.getBigDecimal("market_order").intValue() + "/" + orglist.size()
										+ "；潜在客户提升率" + org.getBigDecimal("latent_reach").doubleValue() + "，排名"
										+ org.getBigDecimal("latent_order").intValue() + "/"
										+ orglist.size() + "。再接再厉！";
								String sm_key = org.getStr("orgname")
										+","+org.getBigDecimal("market_order")+","+orglist.size()
										+","+org.getBigDecimal("latent_order")
										+","+org.getBigDecimal("market_reach")+","+org.getBigDecimal("latent_reach");
								map = new HashMap<String, Object>();
								map.put("sm_cont", sm_cont);
								map.put("sm_job", sm_job);
								map.put("sm_key", sm_key);
								SendMessageUtil.sendMsgList(leaderList, map);
							}
						}
					} else {
						System.out.println("触发事件--上月经营统计结果，推送领导（所处机构）,查询到的经营统计结果记录为空");
						
					}
					
				};
				success();
			}else{
				System.out.println("非本月指定工作日不推送短信！");
			}
		} catch (Exception e) {
			error();
			e.printStackTrace();
			System.out.println("触发事件--上月经营统计结果，推送领导（所处机构），发送短信发生异常:" + e.toString());
		}
		System.out.println("触发事件--上月经营统计结果，推送领导（所处机构）end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
	}

}
