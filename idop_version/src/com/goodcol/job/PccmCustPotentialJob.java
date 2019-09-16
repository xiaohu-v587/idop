package com.goodcol.job;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

/**
 *潜在客户
 * 
 */
public class PccmCustPotentialJob extends OuartzJob {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// -----开始时间-----
		String start_time = "";
		// -----结束时间-----
		String end_time = "";
		//数据标记
		String incflg = "";
		//记录数
		int record_size = 0;
		//跑批状态
		int run_status = 0;
		
		try{
			start_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			end_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			run_status=1;
		}catch(Exception e){
			run_status=2;
			e.printStackTrace();
		}finally{
			Db.use("default").update("insert into PCCM_QUARTZ_RUN(id,START_TIME,END_TIME,RECORD_SIZE,INCFLG,RUN_STATUS) values(?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),start_time,end_time,record_size,incflg,run_status});
		}
		
	}
	
	/**
	 * 一、个人贷款：
	 *	1、从CPB表中筛选出符合条件的客户
	 *	2、根据客户ID，从RLMS--CCTPER_RL表中找到PERCMPNM（工作单位/就读学校）
	 *	   或者ROCRM--ELOAN_LIMIT表中代发薪企业名称CUST_COMPANY_NAME，代发薪企业客户号CUST_COMPANY_NO
	 *	3、根据公司名称/k客户号匹配对公客户信息(企查查接口)
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private List<Record> findCPBCustList() {
		//从MIIS--BAS_CUST_PROD_NEW_BR5表中ADVANCE（中高端）找到 PARTY_ID（核心客户号）
		//从ROCRM--ELOAN_LIMIT表中代发薪企业名称CUST_COMPANY_NAME，代发薪企业客户号CUST_COMPANY_NO
		
		List<Record> rlmsList = Db.use("default").find("select distinct party_id from pccm_bas_cust_prod_new_br5 where advance is not null");
		List<Record> rocrmList = Db.use("default").find("select case when cust_company_name is not null then cust_company_name else cust_company_no end key "
				+" from pccm_eloan_limit where cust_company_name is not null or cust_company_no is not null ");
		
		rlmsList.addAll(rocrmList);
		
		//根据公司名称/k客户号匹配对公客户信息
		Set qcc = new HashSet();
		if(null!=rlmsList&&rlmsList.size()>0){
			Record re = null;
			for(int i=0;i<rlmsList.size();i++){
				//调用企查查接口
				String url="http://i.yjapi.com/ECIV4/GetDetailsByName?key=AppKey&keyword="+rlmsList.get(i);
				//将接口返回结果放入sets
				qcc.add(re);
			}
		}
		return null;
	}

}
