/**
 * 
 */
package com.goodcol.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

/**
 *客户经理离职离岗
 * 
 */
public class PccmCustmgrQuitJob extends OuartzJob {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// -----开始时间-----
		String start_time = "";
		// -----结束时间-----
		String end_time = "";
		String incflg = "客户经理离职离岗";
		//记录数
		int record_size = 0;
		//跑批状态
		int run_status = 0;
		// 查询离职客户经理的信息
		List<Record> custList = null;
		// 查询离职客户经理的客户认领信息
		List<Record> custClaimList = null;
		// 查询离职客户经理的客户认领信息
		Record re = null;
		// 客户经理id
		String custmgrId = null;
		//离职客户经理认领比例
		String claim_prop_mgr = null;
		//客户被认领的比例
		String claim_prop_cust = null;
		//客户池Id
		String cust_pool_id = null;
		//客户状态
		String cust_stat = null;
		try{
			start_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			custList = Db.use("default").find(" select id,org_id from sys_user_info u where u.dele_flag='1' or u.stat='1' ");
			//遍历离职客户经理
			if (custList != null && !custList.isEmpty()) {
				for (int i = 0; i < custList.size(); i++) {
					custmgrId = custList.get(i).get("id");
					custClaimList =  Db.use("default").find(" select * from pccm_cust_claim c where c.del_stat='0' and c.claim_cust_mgr_id=? ",new Object[]{custmgrId});
					//遍历认领的客户,根据认领比例修改客户在客户池的状态,完全认领时间置空
					if (custClaimList != null && !custClaimList.isEmpty()) {
						for (int j = 0; j < custClaimList.size(); j++) {
							claim_prop_mgr = custClaimList.get(j).get("claim_prop");
							cust_pool_id = custClaimList.get(j).get("cust_pool_id");
							re = Db.use("default").findFirst("select p.id, claim_prop from pccm_cust_pool p "
									+" inner join (select cust_pool_id, sum(claim_prop) claim_prop from pccm_cust_claim "
									+" where del_stat = '0' group by cust_pool_id) c on p.id = c.cust_pool_id "
									+" where p.id=? ", new Object[]{cust_pool_id});
							if(null!=re){
								claim_prop_cust = String.valueOf(re.get("claim_prop"));
								//查询此客户的认领状态，认领比例
								if(claim_prop_mgr.equals(claim_prop_cust)){
									cust_stat = "1";
								}else{
									cust_stat = "2";
								}
								Db.use("default").update(" update pccm_cust_pool set cussts=?,all_claim_time='' where id=? and pa_not_zero <> '0'",new Object[]{cust_stat,cust_pool_id});
							}
						}
					}
					//将认领的客户记录删除
					Db.use("default").update(" update pccm_cust_claim set del_stat='1',del_time=to_char(sysdate,'yyyyMMddhh24miss') where claim_cust_mgr_id=? ",new Object[]{custmgrId});
				}
			}
			end_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			run_status=1;
		}catch(Exception e){
			run_status=2;
			e.printStackTrace();
		}finally{
			Db.use("default").update("insert into PCCM_QUARTZ_RUN(id,START_TIME,END_TIME,RECORD_SIZE,INCFLG,RUN_STATUS) values(?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),start_time,end_time,record_size,incflg,run_status});
		}
		
	}	

}
