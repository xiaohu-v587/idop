package com.goodcol.util.zxgldbutil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.DateTimeUtil;

public class ValidCustDBUtil {
	private final static String DEFAULT = "default";
	String lastYear = (Integer.parseInt(DateTimeUtil.getYear())-1) + "12";// 上年末
	String cust_type = "02";
	
	public List<Record> getValidCustCountList(String orgNum, String month, String lastMonth){
		String sql=" select * from PCCM_VALID_CUST_COUNT where data_month = '" + month +"' and (id='" + orgNum +"'";
		sql+= " or id in (select id from sys_org_info where upid = '"+ orgNum +"')) order by deptlevel,id asc";
		System.out.println("有效户统计列表startTime："+DateTimeUtil.getNowDate());	
		//List<Record> list = Db.use("gbase").find(sql);
		List<Record> list = Db.use(DEFAULT).find(sql);
		System.out.println("有效户统计列表endTime："+DateTimeUtil.getNowDate());
		return list;
	}
	
	public Page<Record> getCustList(int page, int pageSize,String month, String orgNum, String name, String cust_no, String deptLevel) {
		// gabse
		//		String sql = "select cb.id,si.orgname,cb.name,cb.cust_no, "
		//				+ "cb.valid_cust, " 
		//				+ "round(cb.ck_nrj_zcy/10000,2) as ck_nrj_zcy, " 
		//				+ "round(cb.bn_fic_nrj/10000,2) as bn_fic_nrj, "
		//				+ "round(cb.ACCOUNT_CONTRIB_before_tax/10000,2) as  ACCOUNT_CONTRIB_before_tax ";
		//		String fromSql = "from pccm_cust_base_info cb "
		//				+ "left join sys_org_info si on cb.id=si.id "
		//				+ "where cb.id ='" + orgNum + "' and pa_date ='" + month + "'";
		
		// oracle
		//		String sql = "select cp.id,si.orgname,cp.customername as name,cb.cust_no, "
		//				+ "cp.valid_cust, " 
		//				+ "round(cb.ck_nrj_zcy/10000,2) as ck_nrj_zcy, " 
		//				+ "round(cb.bn_fic_nrj/10000,2) as bn_fic_nrj, "
		//				+ "round(cb.ACCOUNT_CONTRIB_before_tax/10000,2) as  ACCOUNT_CONTRIB_before_tax ";
		//		String fromSql = "from pccm_cust_pool cp  "
		//				+ "left join pccm_cust_pool_money cb on cb.id = substr(cp.id,1,9) and cb.deptlevel = "+deptLevel
		//				+" and cb.cust_no= cp.customercode "
		//				+ "left join sys_org_info si on cb.id=si.id "
		//				+ "where cb.id ='" + orgNum + "' and cb.pa_date ='" + month + "' "
		//				+ "and cp.deptlevel = " + deptLevel;
		//		if (StringUtils.isNotBlank(name)) {
		//			fromSql += " and cp.customername like '%" + name + "%'";
		//		}
		//		if (StringUtils.isNotBlank(cust_no)) {
		//			fromSql += " and cb.cust_no = '" + cust_no + "'";
		//		}
		//		fromSql += " order by cp.customername";
		
		String sql = "select cp.id,cp.orgnum,si.orgname,cp.customername as name,"
				+ "cp.CUSTOMERCODE as cust_no, " + "cp.valid_cust, "
				+ "'0' as ck_nrj_zcy, " + "'0' as bn_fic_nrj, "
				+ "'0' as account_contrib_before_tax ";
		String fromSql = "from pccm_cust_pool cp  "
				+ "left join sys_org_info si on cp.ORGNUM=si.id "
				+ "where cp.ORGNUM ='" + orgNum + "' ";
				//+ "and cp.deptlevel = '"+ deptLevel + "' ";
		if (StringUtils.isNotBlank(name)) {
			fromSql += " and cp.customername like '%" + name + "%'";
		}
		if (StringUtils.isNotBlank(cust_no)) {
			fromSql += " and cp.cust_no = '" + cust_no + "'";
		}
		fromSql += " order by cp.customername";
		Page<Record> pageRecord = Db.use(DEFAULT).paginate(page, pageSize, sql, fromSql);
		
		List<Record> list = pageRecord.getList();
		String andSql = "";
		String dataDate = findPoolMoneyInfoMaxDate();
		for (Record record : list) {
			andSql += "or ( id = '" + record.getStr("orgnum").trim() + "' and cust_no = '"+ record.getStr("cust_no").trim() + "')  ";
		}
		
		if (StringUtils.isNotBlank(andSql)) {
			andSql = andSql.substring(2);
			String moneySql = "select id||cust_no as id,"
					+ " round(ck_nrj_zcy/10000,2) as ck_nrj_zcy,"
					+ " round(bn_fic_nrj/10000,2) as bn_fic_nrj,"
					+ " round(ACCOUNT_CONTRIB_BEFORE_TAX/10000,2) as ACCOUNT_CONTRIB_BEFORE_TAX"
					+ " from pccm_cust_pool_money where 1 = 1 "
					+ " and data_date = '" + dataDate + "' and (" + andSql
					+ ")";
			List<Record> moneyList = Db.use(DEFAULT).find(moneySql);
			for (Record record : list) {
				for (Record moneyRecord : moneyList) {
					if (record.getStr("id").equals(moneyRecord.getStr("id"))) {
						record.set("ck_nrj_zcy", moneyRecord.getBigDecimal("ck_nrj_zcy"));
						record.set("bn_fic_nrj", moneyRecord.getBigDecimal("bn_fic_nrj"));
						record.set("account_contrib_before_tax", moneyRecord.getBigDecimal("ACCOUNT_CONTRIB_BEFORE_TAX"));
						continue;
					}
				}
			}
		}
		
		//System.out.println("有效户详细列表startTime："+DateTimeUtil.getNowDate());
		//Page<Record> list = Db.use("gbase").paginate(page, pageSize, sql, fromSql);
		//System.out.println("有效户详细列表endTime："+DateTimeUtil.getNowDate());
		return pageRecord;
	}

	
	/**
	 * 根据用户获取PA不为零统计表
	 * 2018年8月3日12:28:18
	 * @author liutao
	 * @return
	 */
	public Record findValidCustNumByUserId(Map<String, String> map){
		String sql = "select user_id, user_name, monthSumNum, monthNum, "
			+ "lastYearSumNum, lastYearNum, lastMonthSumNum, lastMonthNum, "
			+ "(monthSumNum - lastYearSumNum) as incYearSumNum, "
			+ "(monthSumNum - lastYearSumNum)/lastYearSumNum as ampliYearSumNum, "
			+ "(monthNum - lastYearNum) as incYearNum, "
			+ "(monthNum - lastYearNum)/lastYearNum as ampliYearNum, "
			+ "(monthSumNum - lastMonthSumNum) as incMonthSumNum, "
			+ "(monthSumNum - lastMonthSumNum)/lastMonthSumNum as ampliLastMonthSumNum, "
			+ "(monthNum - lastMonthNum) as incMonthNum, "
			+ "(monthNum - lastMonthNum)/lastMonthNum as ampliLastMonthNum "
			+ "from ( "
			+ "select t1.claim_cust_mgr_id as user_id, sui.name as user_name, "
			+ "t1.sumNum as lastYearSumNum, t1.num as lastYearNum, "
			+ "t2.sumNum as monthSumNum, t2.num as monthNum, "
			+ "t3.sumNum as lastMonthSumNum, t3.num as lastMonthNum from ( "
			+ "select pcc.claim_cust_mgr_id, count(*) as sumNum, "
			+ "sum(case when pcbi.ACCOUNT_CONTRIB_before_tax <> 0 "
			+ "then 1 else 0 end) as num "
			+ "from pccm_cust_claim pcc "
			+ "left join pccm_cust_base_info pcbi "
			+ "on pcc.cust_pool_id = (pcbi.id||pcbi.cust_no) "
			+ "where substr(pcbi.pa_date,1,6)  = ? "
			+ "and pcc.claim_cust_mgr_id = ? "
			+ "group by pcc.claim_cust_mgr_id) t1 "
			+ "left join "
			+ "(select pcc.claim_cust_mgr_id, count(*) as sumNum, "
			+ "sum(case when pcbi.ACCOUNT_CONTRIB_before_tax <> 0 "
			+ "then 1 else 0 end) as num "
			+ "from pccm_cust_claim pcc "
			+ "left join pccm_cust_base_info pcbi "
			+ "on pcc.cust_pool_id = (pcbi.id||pcbi.cust_no) "
			+ "where pcbi.pa_date = ? "
			+ "and pcc.claim_cust_mgr_id = ? "
			+ "group by pcc.claim_cust_mgr_id) t2 "
			+ "on t1.claim_cust_mgr_id = t2.claim_cust_mgr_id "
			+ "left join "
			+ "(select pcc.claim_cust_mgr_id, count(*) as sumNum, "
			+ "sum(case when pcbi.ACCOUNT_CONTRIB_before_tax <> 0 "
			+ "then 1 else 0 end) as num "
			+ "from pccm_cust_claim pcc "
			+ "left join pccm_cust_base_info pcbi "
			+ "on pcc.cust_pool_id = (pcbi.id||pcbi.cust_no) "
			+ "where pcbi.pa_date = ? "
			+ "and pcc.claim_cust_mgr_id = ? "
			+ "group by pcc.claim_cust_mgr_id) t3 "
			+ "on t1.claim_cust_mgr_id = t3.claim_cust_mgr_id "
			+ "left join sys_user_info sui "
			+ "on sui.id = t1.claim_cust_mgr_id ) tab";
		//参数说明：第一个参数：年初，第二个参数：用户id， 第三个参数：上月，第四个参数：用户id，第五个参数：上上月，第六个参数：用户id
		String year = map.get("lastyear");
		String month = map.get("month");
		String lastmonth = map.get("lastmonth");
		String user_id = map.get("user_id");
		return Db.use("gbase").findFirst(sql, year, user_id, 
				month, user_id, lastmonth, user_id);
	}
	
	/**
	 * 根据用户id查询PA不为零的客户清单明细表
	 * 2018年8月3日12:39:33
	 * @author liutao
	 * @param map
	 * @return
	 */
	public Page<Record> findValidCustInfoByUserId(Map<String, String> map){
		String user_id = map.get("user_id");
		String month = map.get("month");
		int pageNum = Integer.parseInt(map.get("pageNum"));
		int pageSize = Integer.parseInt(map.get("pageSize"));
		String selSql = "select pcbi.ACCOUNT_CONTRIB_before_tax, soi.orgname, "
			+ "pcbi.cust_no, pcbi.name as cust_name ";
		String fromSql = "from pccm_cust_claim pcc "
			+ "left join pccm_cust_base_info pcbi "
			+ "on pcc.cust_pool_id = (pcbi.id||pcbi.cust_no) "
			+ "left join sys_org_info soi "
			+ "on soi.id = pcbi.id "
			+ "where pcbi.pa_date = ?"
			+ "and pcc.claim_cust_mgr_id = ?";
		List<String> listStr = new ArrayList<String>();
		listStr.add(month);
		listStr.add(user_id);
		return Db.use("gbase").paginate(pageNum, pageSize, selSql, 
				fromSql, listStr.toArray());
	}
	
	/**
	 * 根据机构id获取PA不为零统计表
	 * 2018年8月3日12:55:53
	 * @author liutao
	 * @return
	 */
	public List<Record> findValidCustNumByOrgId(Map<String, String> map){
		//修改使用定时任务跑存储过程之后查询单表方式
		String sql = "select id, orgname, monthSumNum, monthNum, lastYearSumNum, "
				+ " lastYearNum, lastMonthSumNum, lastMonthNum, incYearSumNum, "
				+ " ampliYearSumNum, incYearNum, ampliYearNum, incMonthSumNum, "
				+ " ampliLastMonthSumNum, incMonthNum, ampliLastMonthNum "
				+ " from pccm_pa_not_zero "
				+ " where data_month = ? "
				+ " and (id = ? or id in ("
				+ " select id from sys_org_info where upid = ?))"
				+ " order by deptlevel,id asc";
		List<String> listStr = new ArrayList<String>();
		String month = map.get("month");
		String org_id = map.get("org_id");
		listStr.add(month);
		listStr.add(org_id);
		listStr.add(org_id);
		//return Db.use("gbase").find(sql, listStr.toArray());
		
		//数据源切换到ORACLE 2018年9月7日14:33:30  -liutao
		return Db.use(DEFAULT).find(sql, listStr.toArray());
	}
	
	/**
	 * 根据机构号获取当前PA不为零客户数据
	 * 2018年8月9日18:55:12
	 * @author liutao 
	 * @param map
	 * @return
	 */
	public Page<Record> findValidCustInfoByOrgId(Map<String, String> map){
		//String month = map.get("month");
		String org_id = map.get("org_id");
		String name = map.get("name");
		String cust_no = map.get("cust_no");
		int pageNum = Integer.parseInt(map.get("pageNum"));
		int pageSize = Integer.parseInt(map.get("pageSize"));
		/*String sql = "select pcbi.ACCOUNT_CONTRIB_before_tax, soi.orgname, "
				+ " pcbi.cust_no, pcbi.name as cust_name ";
		String fromSql = " from pccm_cust_base_info pcbi "
				+ " left join sys_org_info soi "
				+ " on soi.id = pcbi.id "
				+ " where pcbi.pa_date = ? "
				+ " and pcbi.id = ? ";
		if(StringUtils.isNotBlank(name)){
			fromSql += " and pcbi.name like '%"+ name +"%' ";
		}
		if(StringUtils.isNotBlank(cust_no)){
			fromSql += " and pcbi.cust_no = '"+ cust_no +"' ";
		}
		return Db.use("gbase").paginate(pageNum, pageSize, sql, fromSql, month, org_id);*/
		
		//查询客户池钱表的最新数据日期
		String date = findPoolMoneyInfoMaxDate();
		//切换到Oracle数据源进行数据查询  2018年9月7日14:40:36  --liutao
		String sql = "select soi.orgname, pcp.id, '0' as account_contrib_before_tax,"
				+ " pcp.customercode as cust_no,pcp.orgnum, "
				+ " pcp.customername as cust_name ";
		String fromSql = " from pccm_cust_pool pcp "
				+ " left join sys_org_info soi on soi.id = pcp.orgnum "
				+ " where pcp.orgnum = ? ";
		if (StringUtils.isNotBlank(name)) {
			fromSql += " and pcp.customername like '%" + name + "%' ";
		}
		if (StringUtils.isNotBlank(cust_no)) {
			fromSql += " and pcp.customercode = '" + cust_no + "' ";
		}
		Page<Record> paginate = Db.use(DEFAULT).paginate(pageNum, pageSize, sql, fromSql, org_id);
		List<Record> list = paginate.getList();
		String andSql = "";
		for (Record record : list) {
			andSql += "or ( id = '" + record.getStr("orgnum").trim() + "' and cust_no = '"+ record.getStr("cust_no").trim() + "')  ";
		}
		if (StringUtils.isNotBlank(andSql)) {
			andSql = andSql.substring(2);
			String moneySql = " select id||cust_no as id,account_contrib_before_tax "
					+ "from pccm_cust_pool_money where data_date = '" + date
					+ "' and (" + andSql + ")";
			List<Record> moneyList = Db.use(DEFAULT).find(moneySql);
			for (Record record : list) {
				for (Record moneyRecord : moneyList) {
					if (record.getStr("id").equals(moneyRecord.getStr("id"))) {
						record.set("account_contrib_before_tax", moneyRecord.getBigDecimal("account_contrib_before_tax"));
						continue;
					}
				}
			}
		}
		
		return paginate;
	}

	/**
	 * 获取统计列表
	 * @param month
	 * @return
	 */
	public List<Record> downLoadValidCustCountList(String month, String orgNum) {
		String sql=" select * from PCCM_VALID_CUST_COUNT  where data_month = '" + month + "'" 
				+ " and id in (select id from sys_org_info where id = '" + orgNum + "' or by5 like '%" + orgNum + "%')" 
				+ " order by deptlevel,id asc";
		List<Record> list = Db.use(DEFAULT).find(sql);
		return list;
	}
	
	/**
	 * 查询下载PA不为零客户数据
	 * 2018年8月15日16:24:46
	 * @author liutao
	 */
	public List<Record> findDownloadOrgPaNotZero(String month, String orgNum){
		/*String sql = "select id, orgname, monthSumNum, monthNum, lastYearSumNum, "
				+ " lastYearNum, lastMonthSumNum, lastMonthNum, incYearSumNum, "
				+ " ampliYearSumNum, incYearNum, ampliYearNum, incMonthSumNum, "
				+ " ampliLastMonthSumNum, incMonthNum, ampliLastMonthNum "
				+ " from pccm_pa_not_zero "
				+ " where data_month = ? order by id asc";
		return Db.use("gbase").find(sql, month);*/
		
		//切换到Oracle数据源进行数据查询  2018年9月7日16:20:17 --liutao
		String sql = "select id, orgname, monthSumNum, monthNum, lastYearSumNum, "
				+ " lastYearNum, lastMonthSumNum, lastMonthNum, incYearSumNum, "
				+ " ampliYearSumNum, incYearNum, ampliYearNum, incMonthSumNum, "
				+ " ampliLastMonthSumNum, incMonthNum, ampliLastMonthNum "
				+ " from pccm_pa_not_zero "
				+ " where data_month = ? " 
				+ " and id in (select id from sys_org_info where id = '" + orgNum + "' or by5 like '%" + orgNum + "%')" 
				+ " order by deptlevel,id asc";
		return Db.use(DEFAULT).find(sql, month);
	}
	
	/**
	 * 查询GBASE宽表数据最大的时间是多少
	 * 2018年8月29日18:09:14
	 * @author liutao
	 * @return
	 */
	public String findPoolMoneyInfoMaxDate(){
		String sql = "select to_char(max(data_date)) as data_date from pccm_cust_pool_money ";
		Record r = Db.use(DEFAULT).findFirst(sql);
		if (null != r) {
			return r.getStr("data_date");
		} else {
			//如果没有则返回当前时间
			return DateTimeUtil.getPathName();
		}
	}

	/**
	 * 有效户数据日期
	 */
	public List<Record> validCustCountDate() {
		String sql = "select substr(data_month,1,6) as month, data_month as data_date from pccm_valid_cust_count group by data_month order by data_month desc";
		return Db.use(DEFAULT).find(sql);
	}
	
	/**
	 * PA不为0数据日
	 */
	public List<Record> validCustPaDate() {
		String sql = "select substr(data_month,1,6) as month, data_month as data_date from pccm_pa_not_zero group by data_month  order by data_month desc";
		return Db.use(DEFAULT).find(sql);
	}
}
