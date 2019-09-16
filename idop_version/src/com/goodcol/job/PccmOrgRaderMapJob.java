/**
 * 
 */
package com.goodcol.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

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
public class PccmOrgRaderMapJob extends OuartzJob {
	private static final String DEFAULT = "default";
	private static final String GBASE = "gbase";
	/**
	 * 同步用户
	 * 2018年7月9日09:30:48
	 * @author liutao
	 */
	@Override
	public void execute(JobExecutionContext arg0){
		
		System.out.println("首页机构雷达图数据查询定时任务开始：" + DateTimeUtil.getTime());
		try{
			//首先获取所有需要跑批的机构
			String sql = getFindOracleSql(null, null, 0, false);
			List<Record> orgList = Db.use(DEFAULT).find(sql);
			String delSql = "delete from PCCM_ORG_LADERMAP_INFO where org_id = ? and time_point = ? ";
			String insertSql = "insert into PCCM_ORG_LADERMAP_INFO(org_id, incomday, loansday, weighting, " 
					+ "latentreach, markreach, time_point) values (?, ?, ?, ?, ?, ?, ?)";
			for (Record record : orgList) {
				String orgId = record.getStr("id");
				//每个机构需要循环四次，分别查询当前、上周、上月以及上年的相关数据
				for(int i = 1; i <= 4; i++){//i就是按照顺序分别代表当前、上周、上月以及上年
					//计算机构存款、贷款
					sql = getFindOracleSql(orgId, "moneyday", i, false);
					//Record moneydayRecord = Db.use(DEFAULT).findFirst(sql);
					//切换到GBASE数据库进行数据查询  2018年8月30日21:05:22  --liutao
					Record moneydayRecord = Db.use(GBASE).findFirst(sql);
					String incomday = moneydayRecord.get("incomday") + "";//存款
					String loansday = moneydayRecord.get("loansday") + "";//贷款
					//计算机构加权
					sql = getFindOracleSql(orgId, "weight", i, false);
					Record weightRecord = Db.use(DEFAULT).findFirst(sql);
					String weighting = weightRecord.get("newNum") + "";//加权
					//计算机构潜在达成率
					sql = getFindOracleSql(orgId, "latent", i, true);
					//Record latentRecord = Db.use(DEFAULT).findFirst(sql);
					//切换到GBASE数据库进行数据查询  2018年8月30日21:05:22  --liutao
					Record latentRecord = Db.use(GBASE).findFirst(sql);
					int succNum = latentRecord.getLong("num").intValue();//潜在达成率(成功数)
					sql = getFindOracleSql(orgId, "latent", i, false);
					//latentRecord = Db.use(DEFAULT).findFirst(sql);
					//切换到GBASE数据库进行数据查询  2018年8月30日21:05:22  --liutao
					latentRecord = Db.use(GBASE).findFirst(sql);
					int sumNum = latentRecord.getLong("num").intValue();//潜在达成率(总数)
					String latentreach = "0";
					if(sumNum > 0){
						float rea = (float)succNum/sumNum*100;
						if(rea != 0){
							latentreach = String.format("%.2f", rea);
						}
					}
					//计算机构商机转化率
					sql = getFindOracleSql(orgId, "mark", i, false);
					Record markRecord = Db.use(DEFAULT).findFirst(sql);
					String markreach = markRecord.get("reach") + "";//商机转化率
					Db.use(DEFAULT).update(delSql, orgId, i);
					Db.use(DEFAULT).update(insertSql, orgId, incomday, loansday, 
							weighting, latentreach, markreach, i);
				}
			}
			success();
		} catch (Exception e) {
			error();
			e.printStackTrace();
		}
		System.out.println("首页机构雷达图数据查询定时任务结束：" + DateTimeUtil.getTime());
	}

	public static void main(String[] args) {
		System.out.println(getFormatDate(1));
		System.out.println(getFormatDate(2));
		System.out.println(getFormatDate(3));
		System.out.println(getFormatDate(4));
		System.out.println(getFormatDate(6));
		System.out.println(getFormatDate(5));
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
	 */
	private static String getFormatDate(int param){
		Calendar cal = Calendar.getInstance();
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
	 * 根据条件获取相关SQL字符串
	 * 2018年8月8日10:51:43
	 * @author liutao
	 * @param sqlType 根据不同的值返回不同的sql字符串
	 * @return
	 */
	private String getFindOracleSql(String orgId, String sqlType, int timePoint, boolean isSuccNum){
		String sql = "";
		if("moneyday".equals(sqlType)){
			//获取查询存款以及贷款数据sql
			/*sql = "select round(decode(sum(incomday), null, 0, sum(incomday)), 2) as incomday, "
				+ " round(decode(sum(loansday), null, 0, sum(loansday)), 2) as loansday from ( "
				+ " select  pcc.claim_cust_mgr_id, "
				+ " (nvl(pcpm.incomday, 0) * nvl(pcc.claim_prop, 0)/100) as incomday, "
				+ " (nvl(pcpm.loansday, 0) * nvl(pcc.claim_prop, 0)/100) as loansday "
				+ " from pccm_cust_claim pcc "
				+ " left join pccm_cust_pool pcp on pcp.id = pcc.cust_pool_id "
				+ " left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id "
				+ " left join sys_user_info sui on pcc.claim_cust_mgr_id = sui.id "
				+ " where 1=1 and pcc.del_stat = '0' and pcp.pa_not_zero <> '0' and sui.org_id in ( "
				+ " select soi.id  from sys_org_info soi where soi.id = '" + orgId + "' "
				+ " or soi.by5 like '%" + orgId + "%')"
				+ getTimePointSql(timePoint) 
				+ " and pcp.clas_five is not null)";*/
			
			//切换到GBASE数据库进行数据查询  2018年8月30日20:52:46 --liutao
			String data_date = findBaseInfoMaxDate();//查询宽表最新数据日期
			sql = "select round(decode(sum(incomday), null, 0, sum(incomday)), 2) as incomday, "
					+ " round(decode(sum(loansday), null, 0, sum(loansday)), 2) as loansday from ( "
					+ " select  pcc.claim_cust_mgr_id, "
					+ " (nvl(pcbi.ck_nrj_zcy, 0) * nvl(pcc.claim_prop, 0)/100) as incomday, "
					+ " (nvl(pcbi.loan_nrj_zcy, 0) * nvl(pcc.claim_prop, 0)/100) as loansday "
					+ " from pccm_cust_claim pcc "
					+ " left join pccm_cust_pool pcp on pcp.id = pcc.cust_pool_id "
					+ " left join pccm_cust_base_info pcbi on (pcbi.id||pcbi.cust_no) = pcp.id " 
					+ " and pcbi.data_date = '"+ data_date +"' "
					+ " left join sys_user_info sui on pcc.claim_cust_mgr_id = sui.id "
					+ " where 1=1 and pcc.del_stat = '0' and pcp.pa_not_zero <> '0' and sui.org_id in ( "
					+ " select soi.id  from sys_org_info soi where soi.id = '" + orgId + "' "
					+ " or soi.by5 like '%" + orgId + "%')"
					+ getTimePointSql(timePoint) 
					+ " and pcp.clas_five is not null)t";
		}else if("weight".equals(sqlType)){
			//获取查询机构加权数据sql
			sql = "select round(decode(sum(newNum), null, 0, sum(newNum)), 2) as newNum from ( "
				+ " select nvl(pcp.level_weight, 0) * nvl(pcp.level_value, 0) as newNum "
				+ " from sys_user_info sui "
				+ " left join pccm_cust_claim pcc on pcc.claim_cust_mgr_id = sui.id "
				+ " left join pccm_cust_pool pcp on pcp.id = pcc.cust_pool_id "
				+ " where 1=1 and pcc.del_stat = '0' and pcp.pa_not_zero <> '0' "
				+ " and pcp.pa_not_zero <> '0' and sui.org_id in ( "
				+ " select soi.id  from sys_org_info soi where soi.id = '" + orgId + "' "
				+ " or soi.by5 like '%" + orgId + "%')"
				+ getTimePointSql(timePoint) 
				+ " and pcp.clas_five is not null and pcp.clas_five != '6')";
		}else if("latent".equals(sqlType)){
			//获取查询潜在达成率数据sql
			String level = findOrgLevelByOrgId(orgId);
			String potentialSql = "";
			if("1".equals(level)){
				potentialSql = "pcp.clas_potential as clas_potential ";
			}else if("2".equals(level)){
				potentialSql = "(case when pcp.branch_clas_potential is not null then pcp.branch_clas_potential "
						+ "when pcp.clas_potential is not null then pcp.clas_potential "
						+ "end) as clas_potential ";
			}else{
				potentialSql = "(case when pcp.sub_clas_potential is not null then pcp.sub_clas_potential "
						+ "when pcp.branch_clas_potential is not null then pcp.branch_clas_potential "
						+ "when pcp.clas_potential is not null then pcp.clas_potential "
						+ "end) as clas_potential ";
			}
			/*sql = "select round((decode(succNum, null, 0, succNum)/decode(sumNum, null, 1, 0, 1, sumNum)), 2) "
				+ " as reach from (select (select count(1) from ( "
				+ " select incomday, gpi.val*10000 as latent_succ_value from ("
		        + " select (pcpm.incomday+pcpm.finaday) as incomday, "
		        + potentialSql + " from pccm_cust_claim pcc "
		        + " left join pccm_cust_pool pcp on pcp.id = pcc.cust_pool_id "
		        + " left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id "
		        + " left join sys_user_info sui on sui.id = pcc.claim_cust_mgr_id "
		        + " where 1=1 and pcc.del_stat = '0' and pcp.pa_not_zero <> '0' and (pcp.CLAS_POTENTIAL = 'A3' " 
		        + " or pcp.CLAS_POTENTIAL = 'B3' or pcp.CLAS_POTENTIAL = 'C3' " 
		        + " or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3' "
		        + " or pcp.branch_clas_potential = 'A3' or pcp.branch_clas_potential = 'B3' "
		        + " or pcp.branch_clas_potential = 'C3' or pcp.branch_clas_potential = 'D3' "
		        + " or pcp.branch_clas_potential = 'E3' or pcp.sub_clas_potential = 'A3' "
		        + " or pcp.sub_clas_potential = 'B3' or pcp.sub_clas_potential = 'C3' "
		        + " or pcp.sub_clas_potential = 'D3' or pcp.sub_clas_potential = 'E3') "
		        + getTimePointSql(timePoint) + " and sui.org_id in ( "
		        + " select soi.id  from sys_org_info soi where soi.id = '" + orgId + "' "
				+ " or soi.by5 like '%" + orgId + "%')"
				+ " and pcc.claim_cust_mgr_id = sui.id) tab "
				+ " left join gcms_param_info gpi on gpi.name = tab.clas_potential and key = 'LATENT_SUCC' )"
				+ " where incomday > latent_succ_value ) as succNum, "
				+ " (select count(1) from ( "
				+ " select (pcpm.incomday+pcpm.finaday) as incomday from pccm_cust_claim pcc "
				+ " left join pccm_cust_pool pcp on pcp.id = pcc.cust_pool_id "
				+ " left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id "
				+ " left join sys_user_info sui on sui.id = pcc.claim_cust_mgr_id "
				+ " where 1=1 and pcc.del_stat = '0' and pcp.pa_not_zero <> '0' and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or "
				+ " pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3') "
				+ getTimePointSql(timePoint) + " and sui.org_id in ( "
				+ " select soi.id  from sys_org_info soi where soi.id = '" + orgId + "' "
				+ " or soi.by5 like '%" + orgId + "%')"
				+ " and pcc.claim_cust_mgr_id = sui.id) tab) as sumNum from dual)";*/
			
			//切换到GBASE数据库下进行数据查询 2018年8月30日20:42:23 --liutao
			String data_date = findBaseInfoMaxDate();//查询宽表最新数据日期
			sql = "select count(1) as num from ( "
				+ " select incomday, gpi.val*10000 as latent_succ_value from ( "
				+ " select (nvl(pcbi.ck_nrj_zcy, 0)+nvl(pcbi.bn_fic_nrj, 0)+nvl(pcbi.bw_fic_nrj, 0)) as incomday, "
				+ potentialSql + " from pccm_cust_claim pcc "
				+ " left join pccm_cust_pool pcp on pcp.id = pcc.cust_pool_id "
				+ " left join pccm_cust_base_info pcbi on (pcbi.id||pcbi.cust_no) = pcp.id " 
				+ " and pcbi.data_date = '"+ data_date +"' "
				+ " left join sys_user_info sui on sui.id = pcc.claim_cust_mgr_id "
				+ " where 1=1 and pcc.del_stat = '0' and pcp.pa_not_zero <> '0' and (pcp.CLAS_POTENTIAL = 'A3' "
				+ " or pcp.CLAS_POTENTIAL = 'B3' or pcp.CLAS_POTENTIAL = 'C3' "
				+ " or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3' "
				+ " or pcp.branch_clas_potential = 'A3' or pcp.branch_clas_potential = 'B3' "
				+ " or pcp.branch_clas_potential = 'C3' or pcp.branch_clas_potential = 'D3' "
				+ " or pcp.branch_clas_potential = 'E3' or pcp.sub_clas_potential = 'A3' "
				+ " or pcp.sub_clas_potential = 'B3' or pcp.sub_clas_potential = 'C3' "
				+ " or pcp.sub_clas_potential = 'D3' or pcp.sub_clas_potential = 'E3') "
				+ getTimePointSql(timePoint) + " and sui.org_id in ( "
				+ " select soi.id  from sys_org_info soi where soi.id = '" + orgId + "' "
				+ " or soi.by5 like '%" + orgId + "%' ) "
				+ " and pcc.claim_cust_mgr_id = sui.id) tab "
				+ " left join gcms_param_info gpi on gpi.name = tab.clas_potential and jkey = 'LATENT_SUCC' ) t "
				+ " where 1=1 ";
			if(isSuccNum){
				sql += " and incomday > latent_succ_value ";
			}
		}else if("mark".equals(sqlType)){
			//获取查询商机转化率数据sql
			sql = "select round((succNum/decode(sumNum, 0, 1, sumNum)), 2) as reach from ( "
					+ " select (select count(1) from pccm_cust_claim pcc "
					+ " left join pccm_cust_pool pcp on pcc.cust_pool_id = pcp.id "
					+ " left join sys_user_info sui on sui.id = pcc.claim_cust_mgr_id "
					+ " where pcc.claim_cust_mgr_id = sui.id and pcp.pa_not_zero <> '0' and pcc.del_stat = '0' "
					+ " and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') "
					+ getTimePointSql(timePoint)
					+ " and pcc.marketing_stat = '1' and sui.org_id in ( "
					+ " select soi.id  from sys_org_info soi where soi.id = '" + orgId + "' "
					+ " or soi.by5 like '%" + orgId + "%')) as succNum, "
					+ " (select count(1) from pccm_cust_claim pcc "
					+ " left join pccm_cust_pool pcp on pcc.cust_pool_id = pcp.id "
					+ " left join sys_user_info sui on sui.id = pcc.claim_cust_mgr_id "
					+ " where pcc.claim_cust_mgr_id = sui.id and pcc.del_stat = '0' "
					+ " and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') "
					+ getTimePointSql(timePoint) + " and sui.org_id in ( "
					+ " select soi.id  from sys_org_info soi where soi.id = '" + orgId + "' "
					+ " or soi.by5 like '%" + orgId + "%')) as sumNum " 
					+ " from dual) ";
		}else{
			//获取查询省行、分行、支行以及责任中心机构sql
			sql = "select id from sys_org_info "
				+ "where by2 = '1' or by2 = '2' or by2 = '3' or by2 = '4' ";
		}
		return sql;
	}
	
	/**
	 * 查询机构所属等级
	 * 2018年7月18日14:25:55
	 * @author liutao
	 * @param orgId
	 * @return
	 */
	private String findOrgLevelByOrgId(String orgId){
		String sql = "select by2 as orglevel from sys_org_info where id = ?";
		Record r = Db.use(DEFAULT).findFirst(sql, orgId);
		if(null != r && !"".equals(r.getStr("orglevel"))){
			return r.getStr("orglevel");
		}else{
			return "";
		}
	}
	
	/**
	 * 根据时间点返回相对应的SQL语句 
	 * 2018年8月8日14:26:40
	 * @author liutao
	 * @param timePoint=1代表获取当前时间点sql
	 * 		  timePoint=2代表获取上周时间点sql
	 * 		  timePoint=3代表获取上月时间点sql
	 * 		  timePoint=4代表获取上年时间点sql
	 * @return sql
	 */
	private String getTimePointSql(int timePoint) {
		//默认获取当前时间点的sql，先获取当前时间
		String date = getFormatDate(1);
		String sql = " and instr(pcc.claim_time, '"+ date +"') > 0 ";
		if(2 == timePoint){
			//获取上周时间点sql
			String lastStarWeek = getFormatDate(2);
			String lastEndWeek = getFormatDate(3);
			sql = " and pcc.claim_time >= '"+ lastStarWeek 
					+"' and pcc.claim_time < '"+ lastEndWeek +"' ";
		}else if(3 == timePoint){
			//获取上月时间点sql
			String lastStarMonth = getFormatDate(4);
			String lastEndMonth = getFormatDate(6);
			sql = " and pcc.claim_time >= '"+ lastStarMonth 
					+"' and pcc.claim_time < '"+ lastEndMonth +"' ";
		}else if(4 == timePoint){
			//获取上年时间点sql
			String lastYear = getFormatDate(5);
			sql = " and instr(pcc.claim_time, '"+ lastYear +"') > 0 ";
		}
		return sql;
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
}
