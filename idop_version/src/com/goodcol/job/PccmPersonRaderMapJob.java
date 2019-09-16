/**
 * 
 */
package com.goodcol.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.quartz.JobExecutionContext;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

/**
 * 首页机构雷达图数据查询定时任务
 * 2018年8月7日14:20:10
 * @author liutao
 */
public class PccmPersonRaderMapJob extends OuartzJob {
	private static final String GBASE = "gbase";
	
	/**
	 * 同步用户
	 * 2018年7月9日09:30:48
	 * @author liutao
	 */
	@Override
	public void execute(JobExecutionContext arg0){
		System.out.println("首页个人以及机构雷达图数据查询定时任务开始：" + DateTimeUtil.getTime());
		try {
			long jobstartTime = System.currentTimeMillis();
			//获取GBASE数据库宽表数据的最新日期
			String data_date = findBaseInfoMaxDate();
			String currentDate = getNewFormatDate(1, data_date);//当前时间
			String lastWeekEnd = getNewFormatDate(2, data_date);//上周结束时间
			String lastMonthEnd = getNewFormatDate(3, data_date);//上月结束时间
			String lastYearEnd = getNewFormatDate(4, data_date);//上年末时间
			String monthMiddle = getNewFormatDate(0, data_date);//上月中旬
			
			/*********************更新个人雷达图数据******************/
			
			//第一步做好数据的备份
			String dropTmpSql = " drop table if exists PCCM_PERSON_LADERMAP_INFO_tmp ";
			Db.use(GBASE).update(dropTmpSql);
			String createTmpsql = " create table PCCM_PERSON_LADERMAP_INFO_tmp as " 
					+ " select * from PCCM_PERSON_LADERMAP_INFO ";
			Db.use(GBASE).update(createTmpsql);
			String alterSql = " alter table PCCM_PERSON_LADERMAP_INFO_tmp add( "
					+ " back_date varchar(20) comment '备份日期')";
			Db.use(GBASE).update(alterSql);
			String updateSql = " update PCCM_PERSON_LADERMAP_INFO_tmp " 
					+ " set back_date = '"+ DateTimeUtil.getTime() +"' ";
			Db.use(GBASE).update(updateSql);
			
			//第二步清空个人雷达图数据表
			String dropInfoSql = " truncate table PCCM_PERSON_LADERMAP_INFO ";
			Db.use(GBASE).update(dropInfoSql);
			
			//第三步、执行存储过程
			for(int i = 1; i< 5; i++){
				//每循环一次都需要把对应的时间点数据先插入到个人雷达图数据表中
				//数据表中除了用户id和时间点(time_point)其他数据默认为0
				String insertsql = " insert into PCCM_PERSON_LADERMAP_INFO " 
						+ " (user_id, incomday, loansday, latent_sum_num, latent_succ_num, "
						+ " weighting, latentreach, markreach, time_point, kpi, market_sum_num, " 
						+ " market_succ_num) select CLAIM_CUST_MGR_ID, '0' incomday, '0' loansday, " 
						+ " '0' latent_sum_num, '0' latent_succ_num, '0' weighting,'0' latentreach, " 
						+ " '0' markreach, '"+ i +"' time_point,'0' kpi,'0' market_sum_num ," 
						+ " '0' market_succ_num "
						+ " from pccm_cust_claim group by CLAIM_CUST_MGR_ID ";
				Db.use(GBASE).update(insertsql);
				String validSql = "";
				if(i == 1){
					String startTime = currentDate;
					validSql = " CALL ap_pccm.DPRO_PCCM_PERSON_LADERMAP_INFO('"
							+ i +"','"+ startTime +"','',@a,@b)";
				}else if(i == 2){
					String endTime = lastWeekEnd;
					validSql = " CALL ap_pccm.DPRO_PCCM_PERSON_LADERMAP_INFO('"
							+ i +"','"+ endTime +"','',@a,@b)";
				}else if(i == 3){
					String endTime = lastMonthEnd;
					validSql = " CALL ap_pccm.DPRO_PCCM_PERSON_LADERMAP_INFO('"
							+ i +"','"+ endTime +"','"+ monthMiddle +"',@a,@b)";
				}else{
					String startTime = lastYearEnd;
					validSql = " CALL ap_pccm.DPRO_PCCM_PERSON_LADERMAP_INFO('"
							+ i +"','"+ startTime +"','"+ monthMiddle +"',@a,@b)";
				}
				Db.use(GBASE).update(validSql);
			}
			//更新一下数据的时间，用于查询存储过程保存的数据时间
			String data_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			String updatePersonSql = "update PCCM_person_LADERMAP_INFO set data_time = ? ";
			Db.use(GBASE).update(updatePersonSql, data_time);
			/*********************更新机构雷达图数据******************/
			//需要把机构雷达图数据也跑出来
			//第一步做好数据的备份
			String dropOrgTmpSql = " drop table if exists PCCM_ORG_LADERMAP_INFO_tmp ";
			Db.use(GBASE).update(dropOrgTmpSql);
			String createOrgTmpsql = " create table PCCM_ORG_LADERMAP_INFO_tmp as " 
					+ " select * from PCCM_ORG_LADERMAP_INFO ";
			Db.use(GBASE).update(createOrgTmpsql);
			String alterOrgSql = " alter table PCCM_ORG_LADERMAP_INFO_tmp add( "
					+ " back_date varchar(20) comment '备份日期')";
			Db.use(GBASE).update(alterOrgSql);
			String updateOrgSql = " update PCCM_ORG_LADERMAP_INFO_tmp " 
					+ " set back_date = '"+ DateTimeUtil.getTime() +"' ";
			Db.use(GBASE).update(updateOrgSql);
			
			//第二步清空个人雷达图数据表
			String dropOrgInfoSql = " truncate table PCCM_ORG_LADERMAP_INFO ";
			Db.use(GBASE).update(dropOrgInfoSql);
			for(int i = 1; i< 5; i++ ){
				//每循环一次都需要把对应的时间点数据先插入到机构雷达图数据表中
				//数据表中除了机构id和时间点(time_point)其他数据默认为0
				String insertsql = " insert into PCCM_ORG_LADERMAP_INFO " 
						+ " (org_id, incomday, loansday, latent_sum_num, latent_succ_num, "
						+ " weighting, latentreach, markreach, time_point, market_sum_num, " 
						+ " market_succ_num) select id as org_id, '0' incomday, '0' loansday, " 
						+ " '0' latent_sum_num, '0' latent_succ_num, '0' weighting, '0' latentreach, " 
						+ " '0' markreach, '"+ i +"' time_point, '0' market_sum_num ," 
						+ " '0' market_succ_num from sys_org_info " 
						+ " where by2 in ('1', '2', '3', '4') group by id ";
				Db.use(GBASE).update(insertsql);
			}
			//第三步、更新机构数据
			//首先查询全部机构
			/*String sql = "select id from sys_org_info "
				+ "where by2 = '1' or by2 = '2' or by2 = '3' or by2 = '4' group by id ";
			List<Record> orgList = Db.use(GBASE).find(sql);
			System.out.println("机构数据总和: " + orgList.size());
			int num = 0;
			String updateOrgInfoSql = "update PCCM_ORG_LADERMAP_INFO a, " 
					+ " (select nvl(sum(ppl.incomday),0) as incomday, "
					+ " nvl(sum(ppl.loansday), 0) as loansday, " 
					+ " nvl(sum(ppl.weighting), 0) as weighting, "
					+ " nvl(sum(ppl.latent_sum_num), 0) as latent_sum_num, "
					+ " nvl(sum(ppl.latent_succ_num), 0) as latent_succ_num, "
					+ " nvl(sum(ppl.latentreach), 0) as latentreach, " 
					+ " nvl(sum(ppl.markreach), 0) as markreach, "
					+ " nvl(sum(ppl.market_sum_num), 0) as market_sum_num, "
					+ " nvl(sum(ppl.market_succ_num), 0) as market_succ_num "
					+ " from sys_user_info sui "
					+ " left join PCCM_person_LADERMAP_INFO ppl on sui.id = ppl.user_id "
					+ " where sui.org_id in (select id from sys_org_info soi "
					+ " where soi.by5 like ? or id = ?) and ppl.time_point = ?) b "
					+ " set a.incomday = b.incomday, a.loansday = b.loansday, "
					+ " a.weighting = b.weighting, a.latent_sum_num = b.latent_sum_num, "
					+ " a.latent_succ_num = b.latent_succ_num, a.latentreach = b.latentreach, "
					+ " a.markreach = b.markreach, a.market_sum_num = b.market_sum_num, "
					+ " a.market_succ_num = b.market_succ_num where a.org_id = ? "
					+ "and a.time_point = ? ";
			for (Record record : orgList) {
				String orgId = record.getStr("id");
				System.out.println("更新机构雷达图数据, 当前条数: " + (++num) + ",机构号: " + orgId);
				for(int i = 1; i< 5; i++){
					Db.use(GBASE).update(updateOrgInfoSql, "%"+ orgId +"%", orgId, i+"", orgId, i+"");
				}
			}*/
			
			//第三步进行程序优化  2018年9月4日11:34:44 --liutao
			String updateOrgInfoSql = "update pccm_org_ladermap_info a ,"
					+ " (select org_id, time_point, incomday, loansday, weighting, "
					+ " latent_sum_num, latent_succ_num, market_sum_num, market_succ_num, "
					+ " round((latent_succ_num/decode(latent_sum_num, 0, 1, latent_sum_num)), 2) as latentreach, "
					+ " round((market_succ_num/decode(market_sum_num, 0, 1, market_sum_num)), 2) as markreach "
					+ " from ( "
					+ " select si.id as org_id, li.time_point, sum(incomday) as incomday, sum(loansday) as loansday, "
					+ " sum(weighting) as weighting, sum(latent_sum_num) as latent_sum_num, "
					+ " sum(latent_succ_num) as latent_succ_num, sum(market_sum_num) as market_sum_num, "
					+ " sum(market_succ_num) as market_succ_num "
					+ " from sys_org_info si "
					+ " left join sys_user_info ui on ui.org_id = si.id "
					+ " left join pccm_person_ladermap_info li on ui.id=li.user_id "
					+ " where si.by2 in ('1', '2', '3', '4')  and time_point is not null "
					+ " group by si.id,li.time_point ) t) b "
					+ " set a.incomday = b.incomday, a.loansday = b.loansday, a.weighting = b.weighting, "
					+ " a.latent_sum_num = b.latent_sum_num, a.latent_succ_num = b.latent_succ_num, "
					+ " a.market_sum_num = b.market_sum_num, a.market_succ_num = b.market_succ_num, "
					+ " a.latentreach = b.latentreach, a.markreach = b.markreach "
					+ " where a.org_id = b.org_id and a.time_point = b.time_point ";
			int update = Db.use(GBASE).update(updateOrgInfoSql);
			updateOrgSql = "update PCCM_ORG_LADERMAP_INFO set data_time = ? ";
			Db.use(GBASE).update(updateOrgSql, data_time);
			System.out.println("个人以及机构雷达图数据定时任务更新结束。机构雷达图数据更新条数: " + update);
			long jobEndTime = System.currentTimeMillis();
			System.out.println("个人以及机构雷达图数据定时任务执行结束, 耗时：" + (jobEndTime - jobstartTime) + "毫秒");
			success();
		} catch (Exception e) {
			error();
			e.printStackTrace();
		}
		System.out.println("首页个人以及机构雷达图数据查询定时任务结束：" + DateTimeUtil.getTime());
	}
	
	
	/**
	 * 用于获取当前、上周、上月以及上年的时间
	 * 获取时间需要根据当前时间是否是15号进行判断并得到正确的时间
	 * 例如：当前时间是20180114则当前数据时间是201712的数据则上月应该是20171115到20171215
	 * ==》  当前时间是20180115则当前数据时间是201801的数据则上月应该是20171215到20180115
	 * 
	 * 获取当前的时间格式:20180101
	 * 获取上周的时间格式:
	 * 				周一: 20180101
	 * 				周一: 20180107
	 * 获取上月的开始时间格式:20171215
	 * 获取上月的开始时间格式:20180115
	 * 获取上年的时间格式:2016
	 * @param param=1代表获取当前时间
	 * 		  param=2代表获取上周一时间
	 * 		  param=3代表获取上周日时间
	 * 		  param=4代表获取上月开始时间
	 * 		  param=6代表获取上月结束时间
	 * 		  param=5代表获取上年时间
	 * @return
	 * @throws ParseException 
	 */
	public static String getFormatDate(int param, String currentTime) throws ParseException{
		Calendar cal = Calendar.getInstance();
		cal.setTime(new SimpleDateFormat("yyyyMMdd").parse(currentTime));
		SimpleDateFormat sdf;
		if(2 == param){
			//上周一
			sdf = new SimpleDateFormat("yyyyMMdd");
			int day = cal.get(Calendar.DAY_OF_WEEK);
			//如果当前时间是周日，则直接减去七天
			if(1 == day){
				cal.add(Calendar.DATE, -7);
			}
			cal.set(Calendar.DAY_OF_WEEK, 1);
			cal.add(Calendar.DATE, -6);
			return sdf.format(cal.getTime());
		}else if(3 == param){
			//上周日
			sdf = new SimpleDateFormat("yyyyMMdd");
			int day = cal.get(Calendar.DAY_OF_WEEK);
			//如果当前时间是周日，则直接减去七天
			if(1 == day){
				cal.add(Calendar.DATE, -7);
			}
			cal.set(Calendar.DAY_OF_WEEK, 1);
			return sdf.format(cal.getTime());
		}else if(4 == param){
			//上月开始时间
			sdf = new SimpleDateFormat("yyyyMM");
			//获取当前时间是当月几号
			int day = DateTimeUtil.getDayOfMonth();
			if(day < 15){
				cal.add(Calendar.MONTH, -2);
			}else{
				cal.add(Calendar.MONTH, -1);
			}
			return sdf.format(cal.getTime()) + "15";
		}else if(5 == param){
			//上年
			sdf = new SimpleDateFormat("yyyy");
			//获取当前时间是当月几号
			int day = DateTimeUtil.getDayOfMonth();
			if(day < 15){
				//如果小于15号需要判断当前月份是否是一月，
				//如果是一月则应该获取当前时间的上2年时间，反之获取上年时间
				int month = DateTimeUtil.getMonth();
				if(1 == month){
					cal.add(Calendar.YEAR, -2);
				}else{
					cal.add(Calendar.YEAR, -1);
				}
				return sdf.format(cal.getTime());
			}else{
				cal.add(Calendar.YEAR, -1);
				return sdf.format(cal.getTime());
			}
		}else if(6 == param){
			//上月结束时间
			sdf = new SimpleDateFormat("yyyyMM");
			int day = DateTimeUtil.getDayOfMonth();
			if(day < 15){
				cal.add(Calendar.MONTH, -1);
			}
			return sdf.format(cal.getTime()) + "15";
		}else{
			sdf = new SimpleDateFormat("yyyyMMdd");
			return sdf.format(cal.getTime());
		}
	}
	
	/**
	 * 查询GBASE宽表数据最大的时间是多少
	 * 2018年8月29日18:09:14
	 * @author liutao
	 * @return
	 */
	public String findBaseInfoMaxDate(){
		String sql = "select to_char(max(data_date)) as data_date from ap_pccm.pccm_cust_base_info";
		Record r = Db.use(GBASE).findFirst(sql);
		if (null != r) {
			return r.getStr("data_date");
		} else {
			//如果没有则返回当前时间
			return DateTimeUtil.getPathName();
		}
	}
	
	private static String getNewFormatDate(int timePoint, String currentTime) throws ParseException{
		Calendar cal = Calendar.getInstance();
		cal.setTime(new SimpleDateFormat("yyyyMMdd").parse(currentTime));
		SimpleDateFormat sdf;
		if(0 == timePoint){
			//上月中旬
			sdf = new SimpleDateFormat("yyyyMM");
			cal.add(Calendar.MONTH, -1);
			return sdf.format(cal.getTime()) + "15";
		}else if(1 == timePoint){
			//当前
			sdf = new SimpleDateFormat("yyyyMMdd");
			return sdf.format(cal.getTime());
		}else if(2 == timePoint){
			//上周末
			sdf = new SimpleDateFormat("yyyyMMdd");
			int day = cal.get(Calendar.DAY_OF_WEEK);
			//如果当前时间是周日，则直接减去七天
			if(1 == day){
				cal.add(Calendar.DATE, -7);
			}
			cal.set(Calendar.DAY_OF_WEEK, 1);
			return sdf.format(cal.getTime());
		}else if(3 == timePoint){
			//上月末
			sdf = new SimpleDateFormat("yyyyMMdd");
			//cal.add(Calendar.MONTH, -1);
			cal.set(Calendar.DAY_OF_MONTH, 0);
			return sdf.format(cal.getTime());
		}else{
			//上年末
			sdf = new SimpleDateFormat("yyyy");
			cal.add(Calendar.YEAR, -1);
			return sdf.format(cal.getTime()) + "1231";
		}
	}

}
