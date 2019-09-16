package com.goodcol.util.zxgldbutil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;

public class PccmKPIParaDBUtil {
	private static final String DEFAULT = "default";
	private static final String GBASE = "gbase";

	/**
	 * 获取KPI明星客户经理 2018年5月28日15:19:00
	 * 
	 * @author liutao
	 * @param period
	 *            期次
	 * @return 返回KPI明星客户
	 */
	public List<Record> findKPIStarCustomer(String period, String roleType) {

		String sql = "select tab.*, rownum from ( "
				+ " select sui.name, soi.orgname,pki.kpi, round(pki.kpi/36) as num, gra.head_url "
				+ " from gcms_role_apply gra "
				+ " left join sys_role_info sri on sri.id = gra.role_id and sri.kpi_flag is not null "
				+ " left join SYS_USER_INFO sui on sui.user_no = gra.user_id "
				+ " left join sys_org_info soi on soi.id = sui.org_id "
				+ " left join pccm_kpi_info pki on pki.cust_mgr_no = sui.user_no "
				+ " where pki.period = ? and sri.kpi_flag = ? order by kpi desc "
				+ " ) tab where rownum < 6";
		List<Record> records = Db.use(DEFAULT).find(sql, period, roleType);
		return records;

		// 切换到Gbase数据库进行数据查询 2018年8月20日15:56:16 --liutao
		/*
		 * String sql = "select name, orgname, kpi, num, head_url from ( " +
		 * " select sui.name, soi.orgname,pki.kpi, round(pki.kpi/36) as num, gra.head_url "
		 * + " from gcms_role_apply gra " +
		 * " left join sys_role_info sri on sri.id = gra.role_id and sri.kpi_flag is not null "
		 * + " left join SYS_USER_INFO sui on sui.user_no = gra.user_id " +
		 * " left join sys_org_info soi on soi.id = sui.org_id " +
		 * " left join pccm_kpi_info pki on pki.cust_mgr_no = sui.user_no " +
		 * " where pki.period = ? and sri.kpi_flag = ? order by kpi desc " +
		 * " ) tab limit 5 "; List<Record> records = Db.use(GBASE).find(sql,
		 * period, roleType); return records;
		 */
	}

	/**
	 * 获取过去的所有期次 2018年5月29日10:32:15
	 * 
	 * @author liutao
	 */
	public List<Record> getPeriod() {
		String sql = "select pki.period as period from pccm_kpi_info pki group by pki.period";
		List<Record> records = Db.use(DEFAULT).find(sql);
		return records;
	}

	/**
	 * 根据页面条件查询所有的KPI 2018年5月29日11:31:47
	 * 
	 * @author liutao
	 * @return
	 */
	public Page<Record> getAllKPIList(int pageNum, int pageSize,
			String userName, String userNo, String orgId, String period) {
		String sql = "select pki.period, soi.orgname, sui.user_no, sui.name, "
				+ "sp.name as position_name, pki.kpi, pki.sort_num ";
		String fromSql = "from pccm_kpi_info pki "
				+ "left join sys_user_info sui on pki.cust_mgr_no = sui.user_no "
				+ "left join sys_org_info soi on sui.org_id = soi.id "
				+ "left join sys_position sp on sp.id = sui.cur_post where 1=1 ";
		List<String> paramList = new ArrayList<String>();
		if (StringUtils.isNotBlank(userName)) {
			fromSql += "and sui.name like '%" + userName + "%' ";
		}
		if (StringUtils.isNotBlank(userNo)) {
			fromSql += "and sui.user_no = ? ";
			paramList.add(userNo);
		}
		if (StringUtils.isNotBlank(orgId)) {
			fromSql += "and soi.id = ? ";
			paramList.add(orgId);
		}
		if (StringUtils.isNotBlank(period)) {
			fromSql += "and pki.period = ? ";
			paramList.add(period);
		}
		fromSql += "order by pki.create_time desc ";
		Page<Record> pages = Db.use(GBASE).paginate(pageNum, pageSize, sql,
				fromSql, paramList.toArray());
		return pages;
	}

	public Page<Record> getList(Record record) {
		List<String> listStr = new ArrayList<String>();
		String sql = "select g.id,g.gs,s.name as khjl_type,s1.name as zj,d.zbmc_name,d.zbmc_val";
		String extrasql = " from  pccm_kpi_param g left join (select name,val,sortnum from gcms_param_info where key='KHJL_TYPE') s on g.khjl_type=s.val left join (select name,val,sortnum from gcms_param_info where key='ZJ') s1 on g.zj=s1.val left join pccm_zb_detail d on g.id=d.kpi_id where 1=1";
		StringBuffer sb = new StringBuffer();
		if (AppUtils.StringUtil(record.getStr("khjl_type")) != null) {
			sb.append(" and g.khjl_type=?");
			listStr.add(record.getStr("khjl_type"));
		}
		if (AppUtils.StringUtil(record.getStr("zj")) != null) {
			sb.append(" and g.zj=?");
			listStr.add(record.getStr("zj"));
		}
		Page<Record> r = Db.use("default").paginate(
				record.getInt("pageNum"),
				record.getInt("pageSize"),
				sql,
				extrasql + sb.toString()
						+ " order by s.sortnum asc,s1.sortnum asc",
				listStr.toArray());
		return r;
	}

	public void del(String ids) {
		if (ids.contains(",")) {
			String[] array = ids.split(",");
			for (int i = 0; i < array.length; i++) {
				String uuid = array[i];
				Db.use("default").update(
						"delete from pccm_kpi_param where id=?",
						new Object[] { uuid });
				Db.use("default").update(
						"delete from pccm_zb_detail where kpi_id=?",
						new Object[] { uuid });
			}
		} else {
			Db.use("default").update("delete from pccm_kpi_param where id=?",
					new Object[] { ids });
			Db.use("default").update(
					"delete from pccm_zb_detail where kpi_id=?",
					new Object[] { ids });
		}
	}

	public Record findKpiParam(String id) {
		String sql = "select * from pccm_kpi_param where 1=1";
		StringBuffer sb = new StringBuffer();
		List<String> listStr = new ArrayList<String>();
		if (AppUtils.StringUtil(id) != null) {
			sb.append(" and id = ? ");
			listStr.add(id);
		}
		List<Record> list = Db.use("default").find(sql + sb.toString(),
				listStr.toArray());
		Record record = list.get(0);
		return record;
	}

	public List<Record> findKpiParaDetail(String id) {
		List<Record> records = Db.use("default").find(
				"select * from pccm_zb_detail where kpi_id=?",
				new Object[] { id });
		return records;
	}

	public String saveKpiParam(Record record) {
		String id = record.getStr("id");
		if (AppUtils.StringUtil(id) != null) {
			Db.use("default").update(
					"update pccm_kpi_param set khjl_type=?,zj=? where id=?",
					new Object[] { record.getStr("khjl_type"),
							record.getStr("zj"), id });
			Db.use("default").update(
					"delete from pccm_zb_detail where kpi_id=?",
					new Object[] { id });
		} else {
			id = AppUtils.getStringSeq();
			Db.use("default")
					.update("insert into pccm_kpi_param(id,khjl_type,zj) values(?,?,?)",
							new Object[] { id, record.getStr("khjl_type"),
									record.getStr("zj") });

		}
		return id;
	}

	public void saveKpiParamDetail(Record r) {
		Db.update(
				"insert into pccm_zb_detail (id,zbmc_name,zbmc_val,kpi_id,zb_flag) values(?,?,?,?,?)",
				new Object[] { AppUtils.getStringSeq(), r.getStr("zbmc"),
						r.getStr("val"), r.getStr("kpi_id"),
						r.getStr("zb_flag") });
	}

	/**
	 * 根据用户查询其客户的贷款总和以及存款总和 2018年5月31日14:37:16
	 * 
	 * @param custId
	 *            用户id
	 * @param timePoint
	 *            时间点(1：上周；2：当前；3：上月；4：上年)
	 */
	public Record findFundByCustId(String userId, String timePoint) {
		/*
		 * String sql =
		 * "select nvl((sum(nvl(pccm.loansday, 0) * nvl(pcc.claim_prop, 0)/100)), 0) as loansday, "
		 * +
		 * "nvl((sum(nvl(pccm.incomday, 0) * nvl(pcc.claim_prop, 0)/100)), 0) as incomday "
		 * + "from pccm_cust_claim pcc " +
		 * "left join pccm_cust_pool pcp on pcp.id = pcc.CUST_POOL_ID " +
		 * "left join pccm_cust_pool_money pccm on pccm.cust_pool_id = pcp.id "
		 * + "left join sys_user_info sui on sui.id = pcc.claim_cust_mgr_id " +
		 * "where 1=1 and pcc.claim_cust_mgr_id = ? and pcc.del_stat = '0' " +
		 * " "; sql += getTimePointSql(timePoint); Record r =
		 * Db.use(DEFAULT).findFirst(sql, userId); return r;
		 */

		// 切换到GBASE数据库进行数据查询 2018年8月20日17:20:36 --liutao
		String data_date = findBaseInfoMaxDate();// 查询宽表最新数据日期
		String sql = "select nvl((sum(nvl(pcbi.loan_nrj_zcy, 0) * nvl(pcc.claim_prop, 0)/100)), 0) as loansday,"
				+ " nvl((sum(nvl(pcbi.ck_nrj_zcy, 0) * nvl(pcc.claim_prop, 0)/100)), 0) as incomday "
				+ " from pccm_cust_claim pcc "
				// +
				// " left join pccm_cust_pool pcp on pcp.id = pcc.CUST_POOL_ID "
				+ " left join pccm_cust_base_info pcbi on (pcbi.id||pcbi.cust_no) = pcc.CUST_POOL_ID "
				+ " and pcbi.data_date = ? and pcbi.deptlevel = '3'"
				+ " where 1=1 and pcc.claim_cust_mgr_id = ? and pcc.del_stat = '0' ";
		sql += getTimePointSql(timePoint);
		Record r = Db.use(GBASE).findFirst(sql, data_date, userId);
		return r;
	}

	/**
	 * 根据用户查询其客户的价值客户数或成功营销客户数以及总营销客户数 2018年5月31日15:01:31
	 * 
	 * @author liutao
	 * @param custId
	 *            用户id
	 * @param timePoint
	 *            时间点(1：上周；2：当前；3：上月；4：上年)
	 * @param type
	 *            查询类型 (1：查询价值客户数；2：查询成功营销客户数；3：查询总营销客户数)
	 * @return
	 */
	public Record findCustomerNumByCustId(String custId, String timePoint,
			String type) {
		String sql = "select count(1) as customerNum from pccm_cust_pool pcp "
				+ "left join pccm_cust_claim pcc on pcp.id = pcc.CUST_POOL_ID "
				+ "left join sys_user_info sui on sui.id = pcc.claim_cust_mgr_id "
				+ "where 1=1 and sui.id = ? and pcc.del_stat = '0' ";
		if ("1".equals(type)) {
			sql = "select count(1) as customerNum from pccm_cust_lurk pcl "
					+ "left join pccm_cust_pool pcp on pcp.customercode = pcl.cust_no "
					+ "where 1=1 and (pcp.incflg = '4' or pcp.incflg = '5') "
					+ "and pcl.user_no = ? ";
		} else if ("2".equals(type)) {
			sql += "and pcp.customercode is not null "
					+ "and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3')";
		} else if ("3".equals(type)) {
			sql += "and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3')";
		}
		sql += getTimePointSql(timePoint);
		Record r = Db.use(DEFAULT).findFirst(sql, custId);
		return r;
	}

	/**
	 * 根据用户查询KPI 2018年5月31日15:22:34
	 * 
	 * @author liutao
	 * @param custNo
	 *            用户登录名(EHR号)
	 * @param timePoint
	 *            时间点(1：上周；2：当前；3：上月；4：上年)
	 * @return
	 */
	public Record findKPIByCustNo(String userNo, String timePoint) {
		String sql = "select nvl(sum(pki.kpi), 0) as sumKpi from pccm_kpi_info pki "
				+ "where 1=1 and pki.cust_mgr_no = ? ";
		if ("1".equals(timePoint)) {
			sql += " and to_char(pki.create_time, 'yyyyMMdd') >= (select to_char(trunc(sysdate, 'iw') - 7, 'yyyyMMdd') from dual) "
					+ "and to_char(pki.create_time, 'yyyyMMdd') <= (select to_char(trunc(sysdate, 'iw') - 1, 'yyyyMMdd') from dual)";
		} else if ("2".equals(timePoint)) {
			sql += " and to_char(pki.create_time, 'yyyyMMdd') = (select to_char(sysdate, 'yyyyMMdd') from dual)";
		} else if ("3".equals(timePoint)) {
			sql += " and instr(to_char(pki.create_time, 'yyyyMMdd'), (select to_char(trunc(sysdate, 'MM') - 1, 'yyyyMM') from dual)) > 0";
		} else if ("4".equals(timePoint)) {
			sql += " and instr(to_char(pki.create_time, 'yyyyMMdd'), (select to_char(trunc(sysdate, 'yyyy') - 1, 'yyyy') from dual)) > 0";
		}
		Record r = Db.use(DEFAULT).findFirst(sql, userNo);
		return r;

		// 切换到GBASE数据库进行数据查询 2018年8月20日17:33:47 -liutao
		/*
		 * String sql =
		 * "select nvl(sum(pki.kpi), 0) as sumKpi from pccm_kpi_info pki " +
		 * "where 1=1 and pki.cust_mgr_no = ? "; if ("1".equals(timePoint)) {
		 * sql +=
		 * " and to_char(pki.create_time, 'yyyyMMdd') >= (select to_char(trunc(now(), 'iw') - 7, 'yyyyMMdd') from dual) "
		 * +
		 * "and to_char(pki.create_time, 'yyyyMMdd') <= (select to_char(trunc(now(), 'iw') - 1, 'yyyyMMdd') from dual)"
		 * ; } else if ("2".equals(timePoint)) { sql +=
		 * " and to_char(pki.create_time, 'yyyyMMdd') = (select to_char(now(), 'yyyyMMdd') from dual)"
		 * ; } else if ("3".equals(timePoint)) { sql +=
		 * " and instr(to_char(pki.create_time, 'yyyyMMdd'), (select to_char(trunc(now(), 'MM') - 1, 'yyyyMM') from dual)) > 0"
		 * ; } else if ("4".equals(timePoint)) { sql +=
		 * " and instr(to_char(pki.create_time, 'yyyyMMdd'), (select to_char(trunc(now(), 'yyyy') - 1, 'yyyy') from dual)) > 0"
		 * ; } Record r = Db.use(GBASE).findFirst(sql, userNo); return r;
		 */
	}

	/**
	 * 根据时间点返回相对应的SQL语句 2018年5月31日14:52:26
	 * 
	 * @author liutao
	 * @param timePoint
	 *            时间点
	 * @return sql
	 */
	private String getTimePointSql(String timePoint) {
		/*
		 * String sql =
		 * " and pcp.indate = (select to_char(sysdate, 'yyyyMMdd') from dual) ";
		 * if ("1".equals(timePoint)) { sql =
		 * " and pcp.indate >= (select to_char(trunc(sysdate, 'iw') - 7, 'yyyyMMdd') from dual) "
		 * +
		 * "and pcp.indate <= (select to_char(trunc(sysdate, 'iw') - 1, 'yyyyMMdd') from dual) "
		 * ; } else if ("3".equals(timePoint)) { sql =
		 * " and instr(pcp.indate, (select to_char(trunc(sysdate, 'MM') - 1, 'yyyyMM') from dual)) > 0 "
		 * ; } else if ("4".equals(timePoint)) { sql =
		 * " and instr(pcp.indate, (select to_char(trunc(sysdate, 'yyyy') - 1, 'yyyy') from dual)) > 0 "
		 * ; } return sql;
		 */
		// 查询的时间点不根据客户池的indate进行查询，根据认领表的认领时间claim_time进行查询
		// 2018年8月16日10:58:57---liutao

		// 默认获取当前时间点的sql，先获取当前时间
		String date = getFormatDate(1);
		String sql = " and instr(pcc.claim_time, '" + date + "') > 0 ";
		if ("2".equals(timePoint)) {
			// 获取上周时间点sql
			String lastStarWeek = getFormatDate(2);
			String lastEndWeek = getFormatDate(3);
			sql = " and pcc.claim_time >= '" + lastStarWeek
					+ "' and pcc.claim_time < '" + lastEndWeek + "' ";
		} else if ("3".equals(timePoint)) {
			// 获取上月时间点sql
			String lastStarMonth = getFormatDate(4);
			String lastEndMonth = getFormatDate(6);
			sql = " and pcc.claim_time >= '" + lastStarMonth
					+ "' and pcc.claim_time < '" + lastEndMonth + "' ";
		} else if ("4".equals(timePoint)) {
			// 获取上年时间点sql
			String lastYear = getFormatDate(5);
			sql = " and instr(pcc.claim_time, '" + lastYear + "') > 0 ";
		}
		return sql;
	}

	/**
	 * 查询营销达成率明星客户经理 2018年6月1日10:43:53
	 * 
	 * @author liutao
	 * @return
	 */
	public List<Record> findReachStarCustomer(String roleType) {
		/*
		 * String sql =
		 * "select sui.name, soi.orgname, gra.head_url, round(cp.reach/0.2) as num from ("
		 * + "select claim_cust_mgr_id, round(reach, 2) as reach, rownum from ("
		 * + "select tab.claim_cust_mgr_id, (succNum/sumNum) as reach from (" +
		 * "select pcc.claim_cust_mgr_id, " +
		 * "(select count(1) from pccm_cust_pool pcp " +
		 * "inner join pccm_cust_claim pccm on pcp.customercode = pccm.cust_no "
		 * + "where pcp.customercode is not null " +
		 * "and pccm.claim_cust_mgr_id = pcc.claim_cust_mgr_id and pccm.del_stat = '0' ) as succNum,"
		 * + "decode((select count(1) from pccm_cust_pool pcp " +
		 * "inner join pccm_cust_claim pccm on pcp.customercode = pccm.cust_no "
		 * +
		 * "where pccm.claim_cust_mgr_id = pcc.claim_cust_mgr_id and pccm.del_stat = '0' ), 0, 1) as sumNum "
		 * + "from pccm_cust_claim pcc " + "group by pcc.claim_cust_mgr_id " +
		 * ")tab order by reach desc) where rownum < 6 " +
		 * ") cp left join sys_user_info sui on sui.id = cp.claim_cust_mgr_id "
		 * + "left join sys_org_info soi on soi.id = sui.org_id " +
		 * "left join gcms_role_apply gra on gra.user_id = cp.claim_cust_mgr_id "
		 * + "order by num desc ";
		 */

		String sql = "select tab.*, rownum from ("
				+ " select sui.name, soi.orgname, gra.head_url, round(cp.reach/0.2) as num from ("
				+ " select claim_cust_mgr_id, round(reach, 2) as reach, rownum from ("
				+ " select tab.claim_cust_mgr_id, (succNum/decode(sumNum,0,1,sumNum)) as reach from ("
				+ " select pcc.claim_cust_mgr_id, "
				+ " (sum(case when pcc.del_stat = '0' and pcc.marketing_stat = '1' "
				+ " and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') "
				+ " and pcp.customercode is not null and pcp.dummy_cust_no is not null then 1 else 0 end)) as succNum, "
				+ " (sum(case when (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') "
				+ " and pcp.dummy_cust_no is not null and pcc.del_stat = '0' then 1 else 0 end)) as sumNum "
				+ " from pccm_cust_claim pcc "
				+ " left join gcms_role_apply gra on pcc.claim_cust_mgr_id = gra.user_id "
				+ " left join sys_role_info sri on sri.id = gra.role_id and sri.kpi_flag is not null "
				+ " left join pccm_cust_pool pcp on pcp.id = pcc.cust_pool_id "
				+ " where 1=1 and sri.kpi_flag = ? "
				+ " group by pcc.claim_cust_mgr_id "
				+ " )tab order by reach desc) where rownum < 6 "
				+ " ) cp left join sys_user_info sui on sui.id = cp.claim_cust_mgr_id "
				+ " left join sys_org_info soi on soi.id = sui.org_id "
				+ " left join gcms_role_apply gra on gra.user_id = cp.claim_cust_mgr_id "
				+ " order by num desc) tab where rownum < 6";
		List<Record> records = Db.use(DEFAULT).find(sql, roleType);
		return records;

		// 切换到GBASE数据库进行数据查询 2018年8月20日15:59:22 --liutao
		/*
		 * String sql = "select name, orgname, num, head_url from ( " +
		 * " select sui.name, soi.orgname, gra.head_url, round(cp.reach/0.2) as num from ( "
		 * + " select claim_cust_mgr_id, round(reach, 2) as reach from ( " +
		 * " select tab1.claim_cust_mgr_id, (succNum/decode(sumNum,0,1,sumNum)) as reach from ( "
		 * + " select pcc.claim_cust_mgr_id, " +
		 * " (sum(case when pcc.del_stat = '0' and pcc.marketing_stat = '1' " +
		 * " and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') " +
		 * " and pcp.customercode is not null and pcp.dummy_cust_no is not null then 1 else 0 end)) as succNum, "
		 * +
		 * " (sum(case when (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') "
		 * +
		 * " and pcp.dummy_cust_no is not null and pcc.del_stat = '0' then 1 else 0 end)) as sumNum "
		 * + " from pccm_cust_claim pcc " +
		 * " left join gcms_role_apply gra on pcc.claim_cust_mgr_id = gra.user_id "
		 * +
		 * " left join sys_role_info sri on sri.id = gra.role_id and sri.kpi_flag is not null "
		 * + " left join pccm_cust_pool pcp on pcp.id = pcc.cust_pool_id " +
		 * " where 1=1 and sri.kpi_flag = ? " +
		 * " group by pcc.claim_cust_mgr_id " +
		 * " )tab1 order by reach desc ) tab2 order by claim_cust_mgr_id asc limit 0, 5 "
		 * +
		 * " ) cp left join sys_user_info sui on sui.id = cp.claim_cust_mgr_id "
		 * + " left join sys_org_info soi on soi.id = sui.org_id " +
		 * " left join gcms_role_apply gra on gra.user_id = cp.claim_cust_mgr_id "
		 * + " order by num desc) tab3  "; List<Record> records =
		 * Db.use(GBASE).find(sql, roleType); return records;
		 */
	}

	/**
	 * 待办任务---营销客户 2018年6月4日16:04:25
	 * 
	 * @author liutao
	 * @return 返回所有未营销的客户
	 */
	public Page<Record> findReachCustomer(int pageNum, int pageSize,
			String custNo, String custName, String userNo) {
		String sql = "select pcp.id, pcp.dummy_cust_no, pcp.customername, pcp.relate_cust_name, "
				+ "pcp.cust_manager, pcc.marketing_stat, gpi.name as mark_result, "
				+ "pcp.customercode, pcp.clas_potential,sui.name as user_name, sui.phone, "
				+ "(select gpi.val from gcms_param_info gpi where gpi.key = 'CUST_TASK_LIMIT') - "
				+ "round(to_number(sysdate - to_date(pcc.claim_time, 'yyyyMMddhh24miss')) + 1) as daynum ";
		String fromSql = "from pccm_cust_claim pcc "
				+ "left join pccm_cust_pool pcp on pcc.CUST_POOL_ID = pcp.id "
				+ "left join gcms_param_info gpi on gpi.val = pcc.marketing_stat and gpi.key = 'MARKETING_STAT' "
				+ "left join sys_user_info sui on sui.id = pcp.cust_manager "
				+ "where 1=1 and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') "
				+ "and pcp.dummy_cust_no is not null and pcp.customercode is null "
				+ "and pcc.del_stat = '0' and pcc.claim_cust_mgr_id = ? ";
		List<String> listStr = new ArrayList<String>();
		listStr.add(userNo);
		if (AppUtils.StringUtil(custNo) != null) {
			fromSql += " and pcp.customercode = ? ";
			listStr.add(custNo);
		}
		if (AppUtils.StringUtil(custName) != null) {
			fromSql += " and pcp.customername like ? ";
			listStr.add("%" + custName + "%");
		}
		fromSql += " order by pcc.claim_time desc";
		Page<Record> pages = Db.use(DEFAULT).paginate(pageNum, pageSize, sql,
				fromSql, listStr.toArray());
		return pages;
	}

	/**
	 * 待办任务---潜在提升客户 2018年6月4日16:30:53
	 * 
	 * @author liutao
	 * @return 返回所有潜在提升客户
	 */
	public Page<Record> findLurkCustomer(int pageNum, int pageSize,
			String custNo, String custName, String userNo) {
		/*
		 * String sql =
		 * "select distinct id, customercode, cust_type, customername, orgname, incomday, next_val, "
		 * +
		 * "dayNum, clas_five, clas_potential, case when(next_val - incomday)<0 then 0 "
		 * + "else (next_val - incomday) end as balance "; String fromSql =
		 * "from ( " +
		 * "select pcp.id, pcp.customercode, gpi.remark as cust_type, pcp.customername, soi.orgname, "
		 * +
		 * "(pcpm.incomday + pcpm.finaday) as incomday, (decode(gpi2.val, null, 0, gpi2.val) * 10000) as next_val, "
		 * +
		 * "(select gpi.val from gcms_param_info gpi where gpi.key = 'CUST_TASK_LIMIT') - "
		 * +
		 * "round(to_number(sysdate - to_date(pcp.indate, 'yyyyMMdd')) + 1) as dayNum, pcp.clas_five, "
		 * + "pcp.CLAS_POTENTIAL from pccm_cust_claim pcm " +
		 * "left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id " +
		 * "left join gcms_param_info gpi on gpi.name = pcp.CLAS_POTENTIAL and gpi.key = 'CTYPE' "
		 * +
		 * "left join gcms_param_info gpi2 on gpi2.name = pcp.CLAS_POTENTIAL and gpi2.key = 'LATENT_SUCC' "
		 * + "left join sys_org_info soi on soi.bancsid = pcp.org_id " +
		 * "left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id "
		 * +
		 * "where 1=1 and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or pcp.CLAS_POTENTIAL = 'C3' "
		 * +
		 * "or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3') and pcm.claim_cust_mgr_id = ? "
		 * ;
		 */

		// 查询客户池钱表的最新数据日期
		String date = findPoolMoneyInfoMaxDate();
		String sql = "select distinct id, customercode, cust_type, customername, orgname, incomday, "
				+ "next_val, dayNum, clas_five, clas_potential, case when(next_val - incomday)<0 then 0 "
				+ "else (next_val - incomday) end as balance ";
		String fromSql = "from ( "
				+ "select tab.id, customercode, gpi.remark as cust_type, customername, orgname, incomday,"
				+ "(decode(gpi2.val, null, 0, gpi2.val)*10000) as next_val, clas_potential, clas_five, "
				+ "dayNum from (select pcp.id, pcp.customercode, pcp.customername, soi.orgname, "
				+ "(nvl(pcpm.ck_nrj_zcy, 0)+nvl(pcpm.bn_fic_nrj, 0)+nvl(pcpm.bw_fic_nrj, 0)) as incomday, "
				+ "(case when pcp.sub_clas_potential is not null then pcp.sub_clas_potential "
				+ "when pcp.branch_clas_potential is not null then pcp.branch_clas_potential "
				+ "when pcp.clas_potential is not null then pcp.clas_potential "
				+ "end) as clas_potential, "
				// +
				// "(select gpi.val from gcms_param_info gpi where gpi.key = 'CUST_TASK_LIMIT') - "
				// +
				// "round(to_number(sysdate - to_date(pcm.claim_time, 'yyyyMMddhh24miss')) + 1) as dayNum, pcp.clas_five "
				+ "pcm.claim_time as dayNum, pcp.clas_five "
				+ "from pccm_cust_claim pcm "
				+ " left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id and pcp.deptlevel = '3' "
				+ "left join sys_org_info soi on soi.id = pcp.ORGNUM "
				+ "left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id "
				+ " and pcpm.deptlevel = '3' and pcpm.data_date = '"
				+ date
				+ "' "
				+ "where 1=1 and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or pcp.CLAS_POTENTIAL = 'C3' "
				+ "or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3' "
				+ "or pcp.branch_clas_potential = 'A3' or pcp.branch_clas_potential = 'B3' "
				+ "or pcp.branch_clas_potential = 'C3' or pcp.branch_clas_potential = 'D3' "
				+ "or pcp.branch_clas_potential = 'E3' or pcp.sub_clas_potential = 'A3' "
				+ "or pcp.sub_clas_potential = 'B3' or pcp.sub_clas_potential = 'C3' "
				+ "or pcp.sub_clas_potential = 'D3' or pcp.sub_clas_potential = 'E3' "
				+ ") and pcm.claim_cust_mgr_id = ? and pcm.del_stat = '0' ";
		List<String> listStr = new ArrayList<String>();
		listStr.add(userNo);
		if (AppUtils.StringUtil(custNo) != null) {
			fromSql += " and pcp.customercode = ? ";
			listStr.add(custNo);
		}
		if (AppUtils.StringUtil(custName) != null) {
			fromSql += " and pcp.customername like ? ";
			listStr.add("%" + custName + "%");
		}
		fromSql += " order by pcm.claim_time desc) tab "
				+ "left join gcms_param_info gpi on gpi.name = tab.CLAS_POTENTIAL and gpi.key = 'CTYPE' "
				+ "left join gcms_param_info gpi2 on gpi2.name = tab.CLAS_POTENTIAL and gpi2.key = 'LATENT_SUCC')";
		Page<Record> pages = Db.use(DEFAULT).paginate(pageNum, pageSize, sql,
				fromSql, listStr.toArray());
		return pages;

		// 修改到GBASE数据库进行数据查询 2018年8月24日10:32:55 ---liutao
		/*
		 * String data_date = findBaseInfoMaxDate();// 查询宽表最新数据日期 String sql =
		 * "select distinct id, customercode, cust_type, customername, orgname, incomday, "
		 * +
		 * "next_val, daynum, clas_five, clas_potential, case when(next_val - incomday)<0 then 0 "
		 * + "else (next_val - incomday) end as balance "; String fromSql =
		 * "from ( " +
		 * "select tab.id, customercode, gpi.remark as cust_type, customername, orgname, incomday,"
		 * +
		 * "(decode(gpi2.val, null, 0, gpi2.val) * 10000) as next_val, clas_potential, clas_five, "
		 * +
		 * "daynum from (select pcp.id, pcp.customercode, pcp.customername, soi.orgname, "
		 * +
		 * "(nvl(pcbi.ck_nrj_zcy, 0)+nvl(pcbi.bn_fic_nrj, 0)+nvl(pcbi.bw_fic_nrj, 0)) as incomday, "
		 * +
		 * "(case when pcp.sub_clas_potential is not null and pcp.sub_clas_potential <> '' then pcp.sub_clas_potential "
		 * +
		 * "when pcp.branch_clas_potential is not null and pcp.branch_clas_potential <> '' then pcp.branch_clas_potential "
		 * + "when pcp.clas_potential is not null then pcp.clas_potential " +
		 * "end) as clas_potential, " +
		 * "(select gpi.val from gcms_param_info gpi where gpi.jkey = 'CUST_TASK_LIMIT') - "
		 * +
		 * "round(to_number(now() - to_date(pcm.claim_time, 'yyyyMMddhh24miss')) + 1) as daynum, pcp.clas_five "
		 * +
		 * "from pccm_cust_claim pcm left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id "
		 * + "left join sys_org_info soi on soi.id = pcp.ORGNUM " +
		 * "left join pccm_cust_base_info pcbi on (pcbi.id||pcbi.cust_no) = pcp.id and pcbi.data_date = ? "
		 * +
		 * "where 1=1 and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or pcp.CLAS_POTENTIAL = 'C3' "
		 * + "or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3' " +
		 * "or pcp.branch_clas_potential = 'A3' or pcp.branch_clas_potential = 'B3' "
		 * +
		 * "or pcp.branch_clas_potential = 'C3' or pcp.branch_clas_potential = 'D3' "
		 * +
		 * "or pcp.branch_clas_potential = 'E3' or pcp.sub_clas_potential = 'A3' "
		 * +
		 * "or pcp.sub_clas_potential = 'B3' or pcp.sub_clas_potential = 'C3' "
		 * +
		 * "or pcp.sub_clas_potential = 'D3' or pcp.sub_clas_potential = 'E3' "
		 * + ") and pcm.claim_cust_mgr_id = ? and pcm.del_stat = '0' ";
		 * List<String> listStr = new ArrayList<String>();
		 * listStr.add(data_date); listStr.add(userNo); if
		 * (AppUtils.StringUtil(custNo) != null) { fromSql +=
		 * " and pcp.customercode = ? "; listStr.add(custNo); } if
		 * (AppUtils.StringUtil(custName) != null) { fromSql +=
		 * " and pcp.customername like ? "; listStr.add("%" + custName + "%"); }
		 * fromSql += " order by pcm.claim_time desc) tab " +
		 * " left join gcms_param_info gpi on gpi.name = tab.CLAS_POTENTIAL and gpi.jkey = 'CTYPE' "
		 * +
		 * " left join gcms_param_info gpi2 on gpi2.name = tab.CLAS_POTENTIAL and gpi2.jkey = 'LATENT_SUCC')tab1"
		 * + " order by daynum desc"; Page<Record> pages =
		 * Db.use(GBASE).paginate(pageNum, pageSize, sql, fromSql,
		 * listStr.toArray()); return pages;
		 */
	}

	/**
	 * 查询工作日历 2018年6月6日15:53:56
	 * 
	 * @author liutao
	 * @return
	 */
	public List<Record> findCalendar(String userId, String userNo) {
		/*
		 * String sql = "select dayNum " +
		 * "from (select (select gpi.val from gcms_param_info gpi where gpi.key = 'CUST_TASK_LIMIT') - "
		 * +
		 * "round(to_number(sysdate - to_date(pcp.indate, 'yyyyMMdd')) + 1) as dayNum "
		 * + "from pccm_cust_claim pcc " +
		 * "left join pccm_cust_pool pcp on pcc.CUST_POOL_ID = pcp.id " +
		 * "left join gcms_param_info gpi on gpi.val = pcp.custyp and gpi.key = 'cust_type' "
		 * +
		 * "where 1=1 and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3')"
		 * + "and pcc.del_stat = '0' and pcc.claim_cust_mgr_id = ? " +
		 * "and ((pcp.customercode not in (select pcm.cust_no from pccm_cust_market_record pcm where pcm.EHR_NUM = ?)) or "
		 * +
		 * "pcp.customercode in (select pcm.cust_no from pccm_cust_market_record pcm where pcm.status = '1' and pcm.EHR_NUM = ?))"
		 * + " union all " +
		 * "select (select gpi.val from gcms_param_info gpi where gpi.key = 'CUST_TASK_LIMIT') - "
		 * +
		 * "round(to_number(sysdate - to_date(pcp.indate, 'yyyyMMdd')) + 1) as dayNum "
		 * + "from pccm_cust_lurk pcl " +
		 * "left join pccm_cust_pool pcp on pcp.customercode = pcl.cust_no " +
		 * "left join gcms_param_info gpi on gpi.val = pcp.custyp and gpi.key = 'cust_type' "
		 * +
		 * "where 1=1 and (pcp.incflg = '4' or pcp.incflg = '5') and pcl.user_no = ? "
		 * + ") tab where tab.daynum > -1";
		 */

		String sql = "select dayNum "
				+ " from (select (select gpi.val from gcms_param_info gpi "
				+ " where gpi.key = 'CUST_TASK_LIMIT') - "
				+ " round(to_number(sysdate - to_date(pcc.claim_time, 'yyyyMMddhh24miss'))) as dayNum "
				+ " from pccm_cust_claim pcc "
				+ " left join pccm_cust_pool pcp on pcc.CUST_POOL_ID = pcp.id "
				+ " where 1=1 and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') "
				+ " and pcp.dummy_cust_no is not null and pcp.customercode is null "
				+ " and pcc.del_stat = '0' and pcc.claim_cust_mgr_id = ? "
				+ " union all "
				+ " select (select gpi.val from gcms_param_info gpi "
				+ " where gpi.key = 'CUST_TASK_LIMIT') - "
				+ "round(to_number(sysdate - to_date(pcm.claim_time, 'yyyyMMddhh24miss'))) as dayNum "
				+ "from pccm_cust_claim pcm "
				+ " left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id and pcp.deptlevel = '3' "
				+ " where 1=1 and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' "
				+ " or pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' "
				+ " or pcp.CLAS_POTENTIAL = 'E3' or pcp.branch_CLAS_POTENTIAL = 'A3' "
				+ " or pcp.branch_CLAS_POTENTIAL = 'B3' or pcp.branch_CLAS_POTENTIAL = 'C3' "
				+ " or pcp.branch_CLAS_POTENTIAL = 'D3' or pcp.branch_CLAS_POTENTIAL = 'E3' "
				+ " or pcp.SUB_CLAS_POTENTIAL = 'A3' or pcp.SUB_CLAS_POTENTIAL = 'B3' "
				+ " or pcp.SUB_CLAS_POTENTIAL = 'C3' or pcp.SUB_CLAS_POTENTIAL = 'D3' "
				+ " or pcp.SUB_CLAS_POTENTIAL = 'E3' )"
				+ " and pcm.claim_cust_mgr_id = ? ) tab "
				+ " where 1=1 and tab.daynum > -1 ";
		return Db.use(DEFAULT).find(sql, userNo, userNo);

		// 切换到GBASE数据库进行查询 2018年8月20日15:30:30 --liutao
		/*
		 * String sql = "select daynum " +
		 * "from (select (select gpi.val from gcms_param_info gpi where gpi.jkey = 'CUST_TASK_LIMIT') - "
		 * +
		 * "round(to_number(now() - to_date(pcc.claim_time, 'yyyyMMddhh24miss'))) as daynum "
		 * + "from pccm_cust_claim pcc " +
		 * "left join pccm_cust_pool pcp on pcc.CUST_POOL_ID = pcp.id " +
		 * "where 1=1 and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') "
		 * + "and pcp.dummy_cust_no is not null and pcp.customercode is null " +
		 * "and pcc.del_stat = '0' and pcc.claim_cust_mgr_id = ? " +
		 * " union all " +
		 * "select (select gpi.val from gcms_param_info gpi where gpi.jkey = 'CUST_TASK_LIMIT') - "
		 * +
		 * "round(to_number(now() - to_date(pcm.claim_time, 'yyyyMMddhh24miss'))) as daynum "
		 * + "from pccm_cust_claim pcm " +
		 * "left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id " +
		 * "where 1=1 and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or pcp.CLAS_POTENTIAL = 'C3' "
		 * +
		 * "or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3') and pcm.claim_cust_mgr_id = ? ) tab "
		 * + "where 1=1 and tab.daynum > -1 "; return Db.use(GBASE).find(sql,
		 * userNo, userNo);
		 */
	}

	/**
	 * 查询首页代办任务数（商机客户待处理数、价值客户待处理数、客户池待认领/分配数） 2018年6月7日16:37:06
	 * 
	 * @author liutao
	 * @return
	 */
	public Record findTaskNum(String userNo) {
		Record r = new Record();
		// 价值客户待处理数---修改为潜在待提升客户数
		/*
		 * String sql1 = "select count(1) as num from pccm_cust_lurk pcl " +
		 * "left join pccm_cust_pool pcp on pcp.customercode = pcl.cust_no " +
		 * "where 1=1 and (pcp.incflg = '4' or pcp.incflg = '5') and pcl.user_no = ?"
		 * ;
		 */

		String sql1 = "select count(1) as num from pccm_cust_claim pcm "
				+ " left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id and pcp.deptlevel = '3' "
				+ " where 1=1 and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' "
				+ " or pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' "
				+ " or pcp.CLAS_POTENTIAL = 'E3' or pcp.BRANCH_CLAS_POTENTIAL = 'A3' "
				+ " or pcp.BRANCH_CLAS_POTENTIAL = 'B3' or pcp.BRANCH_CLAS_POTENTIAL = 'C3' "
				+ " or pcp.BRANCH_CLAS_POTENTIAL = 'D3' or pcp.BRANCH_CLAS_POTENTIAL = 'E3' "
				+ " or pcp.SUB_CLAS_POTENTIAL = 'A3' or pcp.SUB_CLAS_POTENTIAL = 'B3' "
				+ " or pcp.SUB_CLAS_POTENTIAL = 'C3' or pcp.SUB_CLAS_POTENTIAL = 'D3' "
				+ " or pcp.SUB_CLAS_POTENTIAL = 'E3' ) "
				+ " and pcm.del_stat = '0' and pcm.claim_cust_mgr_id = ? ";
		Record r1 = Db.use(DEFAULT).findFirst(sql1, userNo);

		// 切换到GBASE数据库进行数据查询 2018年8月20日16:27:10 --liutao
		/*
		 * String sql1 = "select count(1) as num from pccm_cust_claim pcm " +
		 * "left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id " +
		 * "where 1=1 and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or pcp.CLAS_POTENTIAL = 'C3' "
		 * + "or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3') " +
		 * "and pcm.del_stat = '0' and pcm.claim_cust_mgr_id = ? "; Record r1 =
		 * Db.use(GBASE).findFirst(sql1, userNo);
		 */
		r.set("lurkNum", r1.get("num"));
		// 商机客户待处理数---修改为商机待转化客户数
		/*
		 * String sql2 = "select count(1) as num from pccm_cust_claim pcc " +
		 * "left join pccm_cust_pool pcp on pcp.id = pcc.CUST_POOL_ID " +
		 * "where 1=1 and pcc.claim_cust_mgr_id = ? and pcc.del_stat = '0' " +
		 * "and ((pcp.customercode not in (select pcm.cust_no from " +
		 * "pccm_cust_market_record pcm where pcm.EHR_NUM = ? )) or " +
		 * "pcp.customercode in (select pcm.cust_no from pccm_cust_market_record pcm "
		 * + "where pcm.status = '1' and pcm.EHR_NUM = ? ))" +
		 * "and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3')";
		 */

		String sql2 = "select count(1) as num from pccm_cust_claim pcc "
				// +
				// "left join pccm_cust_pool pcp on pcc.CUST_POOL_ID = pcp.id and pcp.deptlevel = '3' "
				+ "left join pccm_cust_pool pcp on pcc.CUST_POOL_ID = pcp.id "
				+ "where 1=1 and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') "
				+ "and pcp.dummy_cust_no is not null and pcp.customercode is null "
				+ "and pcc.del_stat = '0' and pcc.claim_cust_mgr_id = ? ";
		r1 = Db.use(DEFAULT).findFirst(sql2, userNo);

		// 切换到GBASE数据库进行数据查询 2018年8月20日16:27:10 --liutao
		/*
		 * String sql2 = "select count(1) as num from pccm_cust_claim pcc " +
		 * "left join pccm_cust_pool pcp on pcc.CUST_POOL_ID = pcp.id " +
		 * "where 1=1 and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') "
		 * + "and pcp.dummy_cust_no is not null and pcp.customercode is null " +
		 * "and pcc.del_stat = '0' and pcc.claim_cust_mgr_id = ? "; r1 =
		 * Db.use(GBASE).findFirst(sql2, userNo);
		 */

		r.set("reachNum", r1.get("num"));
		// 客户池待认领/分配数
		/*
		 * String sql3 = "select count(1) as num from pccm_cust_pool pcp " +
		 * "where 1=1 and (pcp.cussts = '1' or pcp.cussts = '2') " +
		 * "and pcp.deptlevel='3' and pcp.cust_type ='02' " +
		 * "and pcp.ORGNUM in ( select soi.id  from sys_org_info soi start with soi.id = ( "
		 * + "select soi.orgnum from sys_org_info soi where soi.by2 = ? " +
		 * "start with soi.id = (select org_id from sys_user_info where user_no = ? ) "
		 * +
		 * "connect by prior soi.upid = soi.id )connect by soi.upid = prior soi.id)"
		 * ; r1 = Db.use(DEFAULT).findFirst(sql3, "2", userNo); r.set("bNum",
		 * r1.get("num")); r1 = Db.use(DEFAULT).findFirst(sql3, "3", userNo);
		 * r.set("sNum", r1.get("num"));
		 */
		// 修改不使用递归方式进行查询 2018年8月14日15:12:54 ---liutao
		// 首先查出当前用户所在分行或支行的机构号
		String findSql2 = "select subStr(by5||','||soi.id, 11, 9) as by5 from sys_org_info soi where soi.id = ( "
				+ " select org_id from sys_user_info where user_no = ? )";
		String findSql3 = "select subStr(by5||','||soi.id, 21, 9) as by5 from sys_org_info soi where soi.id = ( "
				+ " select org_id from sys_user_info where user_no = ? )";
		r1 = Db.use(DEFAULT).findFirst(findSql2, userNo);
		String org_id2 = r1.getStr("by5");
		// 查询出机构下的所有子机构
		String ids2 = findChildOrgIdByOrgId(org_id2);
		r1 = Db.use(DEFAULT).findFirst(findSql3, userNo);
		String org_id3 = r1.getStr("by5");
		String ids3 = findChildOrgIdByOrgId(org_id3);
		if (StringUtils.isNotBlank(org_id2) && StringUtils.isNotBlank(org_id3)) {
			String sql_2 = "select count(1) as num from pccm_cust_pool pcp "
					+ "where 1=1 and (pcp.cussts = '1' or pcp.cussts = '2') "
					+ "and pcp.deptlevel='3' and pcp.cust_type!='03' "
					+ "and pcp.ORGNUM in (" + ids2
					+ ") and pcp.THIRD_INDATE is null";
			String sql_3 = "select count(1) as num from pccm_cust_pool pcp "
					+ "where 1=1 and (pcp.cussts = '1' or pcp.cussts = '2') "
					+ "and pcp.deptlevel='3' and pcp.cust_type!='03' "
					+ "and pcp.ORGNUM in (" + ids3
					+ ") and pcp.THIRD_INDATE is not null";
			r1 = Db.use(DEFAULT).findFirst(sql_2);
			r.set("bNum", r1.get("num"));
			r1 = Db.use(DEFAULT).findFirst(sql_3);
			r.set("sNum", r1.get("num"));
		} else {
			r.set("bNum", "0");
			r.set("sNum", "0");
		}
		return r;
	}

	/**
	 * 查询菜单信息 2018年6月8日14:33:26
	 * 
	 * @author liutao
	 */
	public Record findMenuInfoByUrl(String url) {
		String sql = "select smi.id, smi.name as text, smi.url from sys_menu_info smi where URL = ?";
		return Db.use(DEFAULT).findFirst(sql, url);
	}

	/**
	 * 查找工作日历的所有任务 2018年6月9日09:24:21
	 * 
	 * @param pageNum
	 *            当前页面
	 * @param pageSize
	 *            每页数量
	 * @param custNo
	 *            客户号
	 * @param custName
	 *            客户名称
	 * @param userNo
	 *            用户登录名
	 * @author liutao
	 */
	public Page<Record> findAllCalendarTask(int pageNum, int pageSize,
			String custNo, String custName, String userNo) {
		/*
		 * String sql =
		 * "select id, incflg, customercode, cust_type, customername, daynum ";
		 * String fromSql =
		 * "from (select pcp.id, pcp.incflg, pcp.customercode, gpi.name as cust_type, pcp.customername,"
		 * +
		 * "(select gpi.val from gcms_param_info gpi where gpi.key = 'CUST_TASK_LIMIT') - "
		 * +
		 * "round(to_number(sysdate - to_date(pcp.indate, 'yyyyMMdd')) + 1) as dayNum "
		 * + "from pccm_cust_claim pcc " +
		 * "left join pccm_cust_pool pcp on pcc.CUST_POOL_ID = pcp.id " +
		 * "left join gcms_param_info gpi on gpi.val = pcp.custyp and gpi.key = 'cust_type' "
		 * +
		 * "where 1=1 and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3')"
		 * + "and pcc.del_stat = '0' and pcc.claim_cust_mgr_id = ? " +
		 * "and ((pcp.customercode not in (select pcm.cust_no from pccm_cust_market_record pcm where pcm.EHR_NUM = ?)) or "
		 * +
		 * "pcp.customercode in (select pcm.cust_no from pccm_cust_market_record pcm where pcm.status = '1' and pcm.EHR_NUM = ?))"
		 * + " union all " +
		 * "select pcp.id, pcp.incflg, pcp.customercode, gpi.name as cust_type, pcp.customername,"
		 * +
		 * "(select gpi.val from gcms_param_info gpi where gpi.key = 'CUST_TASK_LIMIT') - "
		 * +
		 * "round(to_number(sysdate - to_date(pcp.indate, 'yyyyMMdd')) + 1) as dayNum "
		 * + "from pccm_cust_lurk pcl " +
		 * "left join pccm_cust_pool pcp on pcp.customercode = pcl.cust_no " +
		 * "left join gcms_param_info gpi on gpi.val = pcp.custyp and gpi.key = 'cust_type' "
		 * +
		 * "where 1=1 and (pcp.incflg = '4' or pcp.incflg = '5') and pcl.user_no = ? "
		 * + ") tab where 1=1 ";
		 */

		String sql = "select id, customername, incflg, dayNum";
		String fromSql = "from (select pcp.id, pcp.customername, pcp.incflg, "
				+ "(select gpi.val from gcms_param_info gpi where gpi.key = 'CUST_TASK_LIMIT') - "
				+ "round(to_number(sysdate - to_date(pcc.claim_time, 'yyyyMMddhh24miss'))) as dayNum "
				+ "from pccm_cust_claim pcc "
				+ "left join pccm_cust_pool pcp on pcc.CUST_POOL_ID = pcp.id "
				+ "where 1=1 and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') "
				+ "and pcp.dummy_cust_no is not null and pcp.customercode is null "
				+ "and pcc.del_stat = '0' and pcc.claim_cust_mgr_id = ? "
				+ " union all "
				+ "select pcp.id, pcp.customername, pcp.incflg, "
				+ "(select gpi.val from gcms_param_info gpi where gpi.key = 'CUST_TASK_LIMIT') - "
				+ "round(to_number(sysdate - to_date(pcm.claim_time, 'yyyyMMddhh24miss'))) as dayNum "
				+ "from pccm_cust_claim pcm "
				+ "left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id and pcp.deptlevel = '3' "
				+ "where 1=1 and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' "
				+ " or pcp.CLAS_POTENTIAL = 'C3' "
				+ " or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3' "
				+ " or pcp.branch_CLAS_POTENTIAL = 'A3' "
				+ " or pcp.branch_CLAS_POTENTIAL = 'B3' or pcp.branch_CLAS_POTENTIAL = 'C3' "
				+ " or pcp.branch_CLAS_POTENTIAL = 'D3' or pcp.branch_CLAS_POTENTIAL = 'E3' "
				+ " or pcp.SUB_CLAS_POTENTIAL = 'A3' or pcp.SUB_CLAS_POTENTIAL = 'B3' "
				+ " or pcp.SUB_CLAS_POTENTIAL = 'C3' or pcp.SUB_CLAS_POTENTIAL = 'D3' "
				+ " or pcp.SUB_CLAS_POTENTIAL = 'E3' )"
				+ "and pcm.del_stat = '0' and pcm.claim_cust_mgr_id = ? )tab where 1=1 ";
		List<String> listStr = new ArrayList<String>();
		listStr.add(userNo);
		listStr.add(userNo);
		if (AppUtils.StringUtil(custNo) != null) {
			fromSql += " and tab.customercode = ? ";
			listStr.add(custNo);
		}
		if (AppUtils.StringUtil(custName) != null) {
			fromSql += " and tab.customername like ? ";
			listStr.add("%" + custName + "%");
		}
		fromSql += " order by tab.daynum desc";
		Page<Record> pages = Db.use(DEFAULT).paginate(pageNum, pageSize, sql,
				fromSql, listStr.toArray());
		return pages;

		// 切换到GBASE数据库进行数据查询 2018年8月20日15:38:50 --liutao
		/*
		 * String sql = "select id, customername, incflg, daynum"; String
		 * fromSql = "from (select pcp.id, pcp.customername, pcp.incflg, " +
		 * "(select gpi.val from gcms_param_info gpi where gpi.jkey = 'CUST_TASK_LIMIT') - "
		 * +
		 * "round(to_number(now() - to_date(pcc.claim_time, 'yyyyMMddhh24miss')) + 1) as daynum "
		 * + "from pccm_cust_claim pcc " +
		 * "left join pccm_cust_pool pcp on pcc.CUST_POOL_ID = pcp.id " +
		 * "where 1=1 and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') "
		 * + "and pcp.dummy_cust_no is not null and pcp.customercode is null " +
		 * "and pcc.del_stat = '0' and pcc.claim_cust_mgr_id = ? " +
		 * " union all " + "select pcp.id, pcp.customername, pcp.incflg, " +
		 * "(select gpi.val from gcms_param_info gpi where gpi.jkey = 'CUST_TASK_LIMIT') - "
		 * +
		 * "round(to_number(now() - to_date(pcm.claim_time, 'yyyyMMddhh24miss')) + 1) as daynum "
		 * + "from pccm_cust_claim pcm " +
		 * "left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id " +
		 * "where 1=1 and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or pcp.CLAS_POTENTIAL = 'C3' "
		 * + "or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3') " +
		 * "and pcm.del_stat = '0' and pcm.claim_cust_mgr_id = ? )tab where 1=1 "
		 * ; List<String> listStr = new ArrayList<String>();
		 * listStr.add(userNo); listStr.add(userNo); if
		 * (AppUtils.StringUtil(custName) != null) { fromSql +=
		 * " and tab.customername like ? "; listStr.add("%" + custName + "%"); }
		 * fromSql += " order by tab.daynum desc"; Page<Record> pages =
		 * Db.use(GBASE).paginate(pageNum, pageSize, sql, fromSql,
		 * listStr.toArray()); return pages;
		 */
	}

	/**
	 * 查询我行客户详情 2018年6月9日14:10:11
	 * 
	 * @author liutao
	 */
	public Record findMineCustDetail(String pcpId,String period) {
		if(AppUtils.StringUtil(period) == null){
			//读取KPI最大期次
			period = Db.use(DEFAULT).queryStr(" select max(period) from pccm_kpi_cust_info ");
		}
		// 查询客户池钱表的最新数据日期
		String date = findPoolMoneyInfoMaxDate();
		String sql = "select artifical, telno, incompoint, incomday, loanspoint, loansday,"
				+ "interest_inc, non_interest_inc, payroll, setcard, sms, return_box, "
				+ "cyber_bank, kpi, custyp, customercode, sui.name as m_user_name, "
				+ "sui.phone as m_phone from ("
				+ "select pcpm.cusv_d4_boss_name as artifical, "
				+ "pcpm.CONTACT_PHONE_1 as telno, pcpm.CK_CURR_ZCY as incompoint, "
				+ "pcpm.ck_nrj_zcy as incomday, pcpm.LOAN_CURR_ZCY as loanspoint, "
				+ "pcpm.loan_nrj_zcy as loansday, "
				+ "(nvl(pcpm.ck_INT_INC, 0) + nvl(pcpm.loan_INT_INC, 0) + nvl(pcpm.int_inc, 0)) as interest_inc, "
				+ " (nvl(pcpm.OPERATING_INC_contrib, 0) + nvl(pcpm.ACCOUNT_CONTRIB_before_tax, 0) "
				+ " + nvl(pcpm.mis_contrib,0)) as non_interest_inc, "
				+ "pcpm.is_dfx as payroll, pcpm.is_jscard as setcard, pcpm.is_dxt as sms, "
				+ "pcpm.is_hdx as return_box, pcpm.is_bocnet as cyber_bank, "
				+ "pki.kpi, gpi.name as custyp, pcp.customercode,"
				+ "(select id as user_id from ("
				+ "select sui1.id, rownum from pccm_cust_claim pcc "
				+ "left join sys_user_info sui1 on sui1.id = pcc.claim_cust_mgr_id "
				+ "where pcc.cust_pool_id = ? order by pcc.claim_prop desc ) "
				+ "where rownum = 1 ) as user_id from pccm_cust_pool pcp "
				+ "left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id "
				+ " and pcpm.data_date = '"
				+ date
				+ "' "
				+ "left join pccm_kpi_cust_info pki on pki.cust_no = pcp.customercode and pki.orgnum=pcp.orgnum and pki.period= '"+period+"' "
				+ "left join gcms_param_info gpi on gpi.val = pcp.CLAS_FIVE and gpi.key = 'clas_five' "
				+ "where pcp.id = ? ) tab "
				+ "left join sys_user_info sui on sui.id = tab.user_id ";
		return Db.use(DEFAULT).findFirst(sql, pcpId, pcpId);

		// 修改到GBASE数据库进行数据查询 2018年8月24日15:19:44 --liutao
		/*
		 * String data_date = findBaseInfoMaxDate();// 查询宽表最新数据日期 String
		 * baseInfoId = pcpId.substring(0, 9); String cust_no =
		 * pcpId.substring(9, pcpId.length()); String sql =
		 * "select pcbi.cusv_d4_boss_name as artifical, pcbi.cust_mgr_name as m_user_name, "
		 * + " nvl(pki.kpi, 0) as kpi, pcbi.cust_mgr_contact as m_phone, " +
		 * " pcbi.ck_curr_zcy as incompoint, pcbi.is_dfx as payroll, " +
		 * " pcbi.ck_nrj_zcy as incomday, pcbi.loan_curr_zcy as loanspoint, " +
		 * " pcbi.is_jscard as setcard, pcbi.cust_no as customercode, pcbi.CONTACT_PHONE_1 as telno, "
		 * +
		 * " (select gpi.remark from gcms_param_info gpi where gpi.jkey = 'CTYPE' "
		 * +
		 * " and gpi.name = (select (case when pcp.sub_clas_potential is not null "
		 * + " and pcp.sub_clas_potential <> '' then pcp.sub_clas_potential " +
		 * " when pcp.branch_clas_potential is not null " +
		 * " and pcp.branch_clas_potential <> '' then pcp.branch_clas_potential "
		 * + " when pcp.clas_potential is not null then pcp.clas_potential " +
		 * " end) as clas_potential from pccm_cust_pool pcp " +
		 * " where pcp.id = ? )) as custyp, " +
		 * " pcbi.loan_nrj_zcy as loansday,  pcbi.is_hdx as return_box, pcbi.is_bocnet as cyber_bank, "
		 * +
		 * " (nvl(pcbi.ck_INT_INC, 0) + nvl(pcbi.loan_INT_INC, 0) + nvl(pcbi.int_inc, 0)) as interest_inc, "
		 * + " pcbi.is_dxt as sms, " +
		 * " (nvl(pcbi.OPERATING_INC_contrib, 0) + nvl(pcbi.ACCOUNT_CONTRIB_before_tax, 0) "
		 * + " + nvl(pcbi.mis_contrib,0)) as non_interest_inc " +
		 * " from pccm_cust_base_info pcbi " +
		 * " left join pccm_kpi_cust_info pki on pki.cust_no = pcbi.cust_no " +
		 * " where pcbi.id = ? and pcbi.cust_no = ? and pcbi.data_date = ? ";
		 * return Db.use("gbase").findFirst(sql, pcpId, baseInfoId, cust_no,
		 * data_date);
		 */
	}

	/**
	 * 查询他行客户详情 2018年6月9日14:55:21
	 * 
	 * @author liutao
	 */
	public Record findCustDetail(String pcpId) {

		String sql = "select isboccus, regaddr, regdate, telno, regcapitalamt, generalproduct, "
				+ "industry_name, artifical, user_name, tab.phone, tab.orgname, "
				+ "sui2.name as m_user_name, sui2.phone as m_phone from("
				+ "select pcp.relate_cust_name as isboccus, pcp.regaddr, pcp.regdate, pcp.telno,"
				+ "pcp.regcapitalamt, pcp.generalproduct, pii.industry_name,pcp.artifical,"
				+ "sui.name as user_name, sui.phone, soi.orgname, "
				+ "(select id as user_id from ("
				+ "select sui1.id, rownum from pccm_cust_claim pcc "
				+ "left join sys_user_info sui1 on sui1.id = pcc.claim_cust_mgr_id "
				+ "where pcc.cust_pool_id = ? order by pcc.claim_prop desc ) "
				+ "where rownum = 1 ) as user_id from pccm_cust_pool pcp "
				+ "left join pccm_industry_info pii on pii.code = pcp.idycls "
				+ "left join sys_user_info sui on sui.id = pcp.cust_manager "
				+ "left join sys_org_info soi on soi.id = sui.org_id "
				+ "where 1=1 and pcp.id = ? ) tab "
				+ "left join sys_user_info sui2 on sui2.id = tab.user_id ";
		return Db.use(DEFAULT).findFirst(sql, pcpId, pcpId);

		// 切换到GBASE数据库进行数据查询 2018年8月24日16:32:42 --liutao
		/*
		 * String data_date = findBaseInfoMaxDate();// 查询宽表最新数据日期 String sql =
		 * "select pcp.relate_cust_name as isboccus, pcbi.REG_ADDR as regaddr, "
		 * +
		 * " pcbi.BUS_REGISTER_DATE as regdate, pcbi.CAPITAL_AMOUNT as regcapitalamt, "
		 * +
		 * " pcbi.cusv_d4_boss_name as artifical, pcbi.cust_mgr_name as m_user_name, "
		 * +
		 * " pcbi.cust_mgr_contact as m_phone, pcp.generalproduct, pii.industry_name, "
		 * +
		 * " pcbi.CONTACT_PHONE_1 as telno, sui.name as user_name, sui.phone, soi.orgname "
		 * + " from pccm_cust_pool pcp " +
		 * " left join pccm_cust_base_info pcbi on pcp.id = (pcbi.id||pcbi.cust_no) and pcbi.data_date = ? "
		 * +
		 * " left join pccm_industry_info pii on pii.code = pcbi.INDUSTRY_CODE "
		 * + " left join sys_user_info sui on sui.id = pcp.cust_manager " +
		 * " left join sys_org_info soi on soi.id = sui.org_id " +
		 * " where 1=1 and pcp.id = ? "; return Db.use("gbase").findFirst(sql,
		 * data_date, pcpId);
		 */
	}

	public List<Record> findKpiParams(Record record) {
		StringBuffer sb = new StringBuffer();
		List<String> listStr = new ArrayList<String>();
		listStr.add(record.getStr("khjl_type"));
		listStr.add(record.getStr("zj"));
		String sql = "select * from pccm_kpi_param where khjl_type=? and zj=?";
		if (AppUtils.StringUtil(record.getStr("id")) != null) {
			sb.append(" and id!=?");
			listStr.add(record.getStr("id"));
		}
		List<Record> records = Db.use("default").find(sql + sb.toString(),
				listStr.toArray());
		return records;
	}

	/**
	 * 待办任务---潜在提升客户--数据下载 2018年6月4日16:30:53
	 * 
	 * @author liutao
	 * @return 返回所有潜在提升客户
	 */
	public List<Record> download(String custNo, String custName, String userNo) {
		/*
		 * String sql =
		 * "select pcp.id, pcp.customercode, gpi.remark as cust_type, pcp.customername, soi.orgname, pcpm.incomday,"
		 * +
		 * "(select gpi.val from gcms_param_info gpi where gpi.key = 'CUST_TASK_LIMIT') - "
		 * +
		 * "round(to_number(sysdate - to_date(pcm.claim_time, 'yyyyMMddhh24miss')) + 1) as dayNum, pcp.clas_five "
		 * ; String fromSql = "from pccm_cust_claim pcm " +
		 * "left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id " +
		 * "left join gcms_param_info gpi on gpi.name = pcp.CLAS_POTENTIAL and gpi.key = 'CTYPE' "
		 * + "left join sys_org_info soi on soi.id = pcp.ORGNUM " +
		 * "left join pccm_cust_pool_money pcpm on pcpm.customercode = pcp.customercode "
		 * +
		 * "where 1=1 and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or pcp.CLAS_POTENTIAL = 'C3' "
		 * + "or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3') " +
		 * "and pcm.del_stat = '0' and pcm.claim_cust_mgr_id = ? "; List<String>
		 * listStr = new ArrayList<String>(); listStr.add(userNo); if
		 * (AppUtils.StringUtil(custNo) != null) { fromSql +=
		 * " and pcp.customercode = ? "; listStr.add(custNo); } if
		 * (AppUtils.StringUtil(custName) != null) { fromSql +=
		 * " and pcp.customername like ? "; listStr.add("%" + custName + "%"); }
		 * fromSql += " order by pcp.indate desc"; List<Record> lists =
		 * Db.use(DEFAULT).find(sql + fromSql, listStr.toArray()); return lists;
		 */

		// 修改到GBASE数据库进行数据查询 2018年8月24日10:48:00 --liutao
		String data_date = findBaseInfoMaxDate();// 查询宽表最新数据日期
		String sql = "select pcp.id, pcp.customercode, gpi.remark as cust_type, pcp.customername, "
				+ "(nvl(pcbi.ck_nrj_zcy, 0)+nvl(pcbi.bn_fic_nrj, 0)+nvl(pcbi.bw_fic_nrj, 0)) as incomday, pcp.clas_five, soi.orgname, "
				+ "(select gpi.val from gcms_param_info gpi where gpi.jkey = 'CUST_TASK_LIMIT') - "
				+ "round(to_number(now() - to_date(pcm.claim_time, 'yyyyMMddhh24miss')) + 1) as daynum ";
		String fromSql = "from pccm_cust_claim pcm "
				+ "left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id "
				+ "left join gcms_param_info gpi on gpi.name = pcp.CLAS_POTENTIAL and gpi.jkey = 'CTYPE' "
				+ "left join sys_org_info soi on soi.id = pcp.ORGNUM "
				+ "left join pccm_cust_base_info pcbi on (pcbi.id||pcbi.cust_no) = pcp.id and pcbi.data_date = ? "
				+ "where 1=1 and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or pcp.CLAS_POTENTIAL = 'C3' "
				+ "or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3') "
				+ "and pcm.del_stat = '0' and pcm.claim_cust_mgr_id = ? ";
		List<String> listStr = new ArrayList<String>();
		listStr.add(data_date);
		listStr.add(userNo);
		if (AppUtils.StringUtil(custNo) != null) {
			fromSql += " and pcp.customercode = ? ";
			listStr.add(custNo);
		}
		if (AppUtils.StringUtil(custName) != null) {
			fromSql += " and pcp.customername like ? ";
			listStr.add("%" + custName + "%");
		}
		fromSql += " order by pcm.claim_time desc limit 60000 ";
		List<Record> lists = Db.use(GBASE).find(sql + fromSql,
				listStr.toArray());
		return lists;
	}

	/**
	 * 待办任务---营销客户--数据下载 2018年6月4日16:04:25
	 * 
	 * @author liutao
	 * @return 返回所有未营销的客户
	 */
	public List<Record> downloadReach(String custNo, String custName,
			String userNo) {
		String sql = "select pcp.id, pcp.dummy_cust_no, pcp.customername, pcp.relate_cust_name, "
				+ "pcp.cust_manager, pcc.marketing_stat, "
				+ "(select gpi.val from gcms_param_info gpi where gpi.key = 'CUST_TASK_LIMIT') - "
				+ "round(to_number(sysdate - to_date(pcc.claim_time, 'yyyyMMddhh24miss')) + 1) as daynum ";
		String fromSql = "from pccm_cust_claim pcc "
				+ "left join pccm_cust_pool pcp on pcc.CUST_POOL_ID = pcp.id "
				+ "where 1=1 and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') "
				+ "and pcp.dummy_cust_no is not null and pcp.customercode is null "
				+ "and pcc.del_stat = '0' and pcc.claim_cust_mgr_id = ? ";
		List<String> listStr = new ArrayList<String>();
		listStr.add(userNo);
		if (AppUtils.StringUtil(custNo) != null) {
			fromSql += " and pcp.customercode = ? ";
			listStr.add(custNo);
		}
		if (AppUtils.StringUtil(custName) != null) {
			fromSql += " and pcp.customername like ? ";
			listStr.add("%" + custName + "%");
		}
		fromSql += " order by pcp.indate desc";
		List<Record> Lists = Db.use(DEFAULT).find(sql + fromSql,
				listStr.toArray());
		return Lists;
	}

	/**
	 * 查询雷达图个人加权数 2018年7月4日16:03:46
	 * 
	 * @author liutao
	 * @param userNo
	 *            当前登录人EHR号
	 * @return 返回个人加权数
	 */
	public Record findPersonWeighting(String userNo, String timePoint) {
		String sql = "select decode(sum(newNum), null, 0, sum(newNum)) as newNum from ( "
				+ "select clas_five, decode(clas_five, 1, num*3, 2, num*2, 3, "
				+ "num*1, 4, num*0.6, 5, num*0.06) as newNum from ( "
				+ "select pcp.clas_five, count(1) as num from pccm_cust_claim pcc "
				+ "left join pccm_cust_pool pcp on pcp.id = pcc.cust_pool_id "
				+ "where pcc.claim_cust_mgr_id = ? and pcc.del_stat = '0' "
				+ "and pcp.clas_five is not null and pcp.clas_five != '6' ";
		sql += getTimePointSql(timePoint);
		sql += " group by pcp.clas_five ))";
		return Db.use(DEFAULT).findFirst(sql, userNo);

		// 切换到GBASE数据库进行数据查询 2018年8月20日17:28:39 -liutao
		/*
		 * String sql =
		 * "select decode(sum(newNum), null, 0, sum(newNum)) as newNum from ( "
		 * + "select clas_five, decode(clas_five, 1, num*3, 2, num*2, 3, " +
		 * "num*1, 4, num*0.6, 5, num*0.06) as newNum from ( " +
		 * "select pcp.clas_five, count(1) as num from pccm_cust_claim pcc " +
		 * "left join pccm_cust_pool pcp on pcp.id = pcc.cust_pool_id " +
		 * "where pcc.claim_cust_mgr_id = ? and pcc.del_stat = '0' " +
		 * "and pcp.clas_five is not null and pcp.clas_five != '6' "; sql +=
		 * getTimePointSql(timePoint); sql +=
		 * " group by pcp.clas_five )tab1 )tab2"; return
		 * Db.use(GBASE).findFirst(sql, userNo);
		 */
	}

	/**
	 * 查询个人的商机客户转化率 2018年7月4日16:31:21
	 * 
	 * @author liutao
	 * @param userNo
	 *            当前登录人EHR号
	 * @return 返回个人的商机客户转化率
	 */
	public Record findPersonMarkReach(String userNo, String timePoint) {
		/*
		 * String sql =
		 * "select round((succNum/decode(sumNum, 0, 1, sumNum)), 2) as reach from ("
		 * + "select (select count(1) from pccm_cust_claim pcc " +
		 * "left join pccm_cust_pool pcp on pcc.cust_pool_id = pcp.id " +
		 * "where pcc.claim_cust_mgr_id = ? and pcc.marketing_stat = '1' " +
		 * " "; sql += getTimePointSql(timePoint); sql +=
		 * "and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') and pcc.del_stat = '0') as succNum, "
		 * + "(select count(1) from pccm_cust_claim pcc " +
		 * "left join pccm_cust_pool pcp on pcc.cust_pool_id = pcp.id " +
		 * "where pcc.claim_cust_mgr_id = ? "; sql +=
		 * getTimePointSql(timePoint); sql +=
		 * "and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') and pcc.del_stat = '0') as sumNum "
		 * + "from dual)";
		 */
		// 写的太繁琐了，修改成简单方式 2018年8月16日16:15:47 ---liutao
		String sql = "select round((decode(succNum, null, 0, succNum)/decode(sumNum, 0, 1, sumNum)), 2) "
				+ " as reach from (select count(1) as sumNum,"
				+ " sum(case when pcc.marketing_stat = '1' then 1 else 0 end) as succNum "
				+ " from pccm_cust_claim pcc "
				+ " left join pccm_cust_pool pcp on pcc.cust_pool_id = pcp.id "
				+ " where pcc.claim_cust_mgr_id = ? "
				+ " and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') "
				+ getTimePointSql(timePoint) + " and pcc.del_stat = '0') tab";
		return Db.use(DEFAULT).findFirst(sql, userNo);

		// 切换到GBASE数据库进行数据查询 2018年8月20日17:43:08 -liutao
		/*
		 * String sql =
		 * "select round((decode(succNum, null, 0, succNum)/decode(sumNum, 0, 1, sumNum)), 2) "
		 * + " as reach from (select count(1) as sumNum," +
		 * " sum(case when pcc.marketing_stat = '1' then 1 else 0 end) as succNum "
		 * + " from pccm_cust_claim pcc " +
		 * " left join pccm_cust_pool pcp on pcc.cust_pool_id = pcp.id " +
		 * " where pcc.claim_cust_mgr_id = ? " +
		 * " and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') " +
		 * getTimePointSql(timePoint) + " and pcc.del_stat = '0') tab"; return
		 * Db.use(GBASE).findFirst(sql, userNo);
		 */
	}

	/**
	 * 查询个人的潜在客户达成率数据 2018年7月4日18:01:22
	 * 
	 * @author liutao
	 * @param userNo
	 *            当前登录人EHR号
	 * @return 返回个人的潜在客户达成率数据
	 */
	public Record findPersonLatentReach(String userNo, String timePoint) {
		/*
		 * String sql =
		 * "select round((decode(succNum, null, 0, succNum)/decode(sumNum, null, 1, 0, 1, sumNum)), 2) as reach "
		 * + "from (select (select count(1) from ( " +
		 * "select (pcpm.incomday+pcpm.finaday) as incomday, " +
		 * "gpi.val*10000 as latent_succ_value from pccm_cust_claim pcm " +
		 * "left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id " +
		 * "left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id "
		 * +
		 * "left join gcms_param_info gpi on gpi.name = pcp.clas_potential and key = 'LATENT_SUCC' "
		 * +
		 * "where 1=1 and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or "
		 * +
		 * "pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3') and pcm.del_stat = '0' "
		 * ; sql += getTimePointSql(timePoint); sql +=
		 * " and pcm.claim_cust_mgr_id = ?) tab where tab.incomday > latent_succ_value) as succNum, "
		 * +
		 * "(select count(1) from ( select (pcpm.incomday+pcpm.finaday) as incomday, "
		 * + "gpi.val*10000 as latent_succ_value from pccm_cust_claim pcm " +
		 * "left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id " +
		 * "left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id "
		 * +
		 * "left join gcms_param_info gpi on gpi.name = pcp.clas_potential and key = 'LATENT_SUCC' "
		 * +
		 * "where 1=1 and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or "
		 * +
		 * "pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3') and pcm.del_stat = '0' "
		 * ; sql += getTimePointSql(timePoint); sql +=
		 * "and pcm.claim_cust_mgr_id = ?) tab) as sumNum from dual)";
		 */
		/*
		 * String sql =
		 * "select round((decode(succNum, null, 0, succNum)/decode(sumNum, null, 1, 0, 1, sumNum)), 2) as reach "
		 * + "from (select (select count(1) from ( " +
		 * "select incomday, gpi.val*10000 as latent_succ_value from (" +
		 * "select (pcpm.incomday+pcpm.finaday) as incomday, " +
		 * "(case when pcp.sub_clas_potential is not null then pcp.sub_clas_potential "
		 * +
		 * "when pcp.branch_clas_potential is not null then pcp.branch_clas_potential "
		 * + "when pcp.clas_potential is not null then pcp.clas_potential " +
		 * "end) as clas_potential from pccm_cust_claim pcc " +
		 * "left join pccm_cust_pool pcp on pcp.id = pcc.cust_pool_id " +
		 * "left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id "
		 * +
		 * "where 1=1 and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or "
		 * +
		 * "pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3'"
		 * +
		 * "or pcp.branch_clas_potential = 'A3' or pcp.branch_clas_potential = 'B3' "
		 * +
		 * "or pcp.branch_clas_potential = 'C3' or pcp.branch_clas_potential = 'D3' "
		 * +
		 * "or pcp.branch_clas_potential = 'E3' or pcp.sub_clas_potential = 'A3' "
		 * +
		 * "or pcp.sub_clas_potential = 'B3' or pcp.sub_clas_potential = 'C3' "
		 * +
		 * "or pcp.sub_clas_potential = 'D3' or pcp.sub_clas_potential = 'E3') "
		 * ; sql += getTimePointSql(timePoint); sql +=
		 * "and pcc.del_stat = '0' and pcc.claim_cust_mgr_id = ?) tab " +
		 * "left join gcms_param_info gpi on gpi.name = tab.clas_potential and key = 'LATENT_SUCC')"
		 * + "where incomday > latent_succ_value ) as succNum, " +
		 * "(select count(1) from ( select (pcpm.incomday+pcpm.finaday) as incomday, "
		 * + "gpi.val*10000 as latent_succ_value from pccm_cust_claim pcc " +
		 * "left join pccm_cust_pool pcp on pcp.id = pcc.cust_pool_id " +
		 * "left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id "
		 * +
		 * "left join gcms_param_info gpi on gpi.name = pcp.clas_potential and key = 'LATENT_SUCC' "
		 * +
		 * "where 1=1 and pcc.del_stat = '0' and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or "
		 * +
		 * "pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3') "
		 * ; sql += getTimePointSql(timePoint); sql +=
		 * "and pcc.claim_cust_mgr_id = ? ) tab) as sumNum from dual)" ;
		 */
		// 上面查询太过于复杂，修改简单一点 2018年8月16日16:05:14 ---liutao
		/*
		 * String sql =
		 * "select round((decode(succNum, null, 0, succNum)/decode(sumNum, null, 1, 0, 1, sumNum)), 2) "
		 * + " as reach from (select count(1) as sumNum, " +
		 * " sum(case when incomday > latent_succ_value then 1 else 0 end) as succNum from ("
		 * + " select incomday, gpi.val*10000 as latent_succ_value from (" +
		 * " select (pcpm.incomday+pcpm.finaday) as incomday, " +
		 * " (case when pcp.sub_clas_potential is not null then pcp.sub_clas_potential "
		 * +
		 * " when pcp.branch_clas_potential is not null then pcp.branch_clas_potential "
		 * + " when pcp.clas_potential is not null then pcp.clas_potential " +
		 * " end) as clas_potential from pccm_cust_claim pcc " +
		 * " left join pccm_cust_pool pcp on pcp.id = pcc.cust_pool_id " +
		 * " left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id "
		 * + " where 1=1 " + getTimePointSql(timePoint) +
		 * " and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' " +
		 * " or pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3'"
		 * +
		 * " or pcp.branch_clas_potential = 'A3' or pcp.branch_clas_potential = 'B3' "
		 * +
		 * " or pcp.branch_clas_potential = 'C3' or pcp.branch_clas_potential = 'D3' "
		 * +
		 * " or pcp.branch_clas_potential = 'E3' or pcp.sub_clas_potential = 'A3' "
		 * +
		 * " or pcp.sub_clas_potential = 'B3' or pcp.sub_clas_potential = 'C3' "
		 * +
		 * " or pcp.sub_clas_potential = 'D3' or pcp.sub_clas_potential = 'E3') "
		 * + " and pcc.del_stat = '0' and pcc.claim_cust_mgr_id = ?) tab " +
		 * " left join gcms_param_info gpi on gpi.name = tab.clas_potential and key = 'LATENT_SUCC') t1 ) t2"
		 * ; return Db.use(DEFAULT).findFirst(sql, userNo);
		 */

		// 切换到GBASE数据库进行数据查询 2018年8月20日17:40:55 -liutao
		String data_date = findBaseInfoMaxDate();// 查询宽表最新数据日期
		String sql = "select round((decode(succNum, null, 0, succNum)/decode(sumNum, null, 1, 0, 1, sumNum)), 2) "
				+ " as reach from (select count(1) as sumNum, "
				+ " sum(case when incomday > latent_succ_value then 1 else 0 end) as succNum from ("
				+ " select incomday, gpi.val*10000 as latent_succ_value from ("
				+ " select (nvl(pcbi.ck_nrj_zcy, 0)+nvl(pcbi.bn_fic_nrj, 0)+nvl(pcbi.bw_fic_nrj, 0)) as incomday, "
				+ " (case when pcp.sub_clas_potential is not null then pcp.sub_clas_potential "
				+ " when pcp.branch_clas_potential is not null then pcp.branch_clas_potential "
				+ " when pcp.clas_potential is not null then pcp.clas_potential "
				+ " end) as clas_potential from pccm_cust_claim pcc "
				+ " left join pccm_cust_pool pcp on pcp.id = pcc.cust_pool_id "
				+ " left join pccm_cust_base_info pcbi on (pcbi.id||pcbi.cust_no) = pcp.id and pcbi.data_date = ? "
				+ " where 1=1 "
				+ getTimePointSql(timePoint)
				+ " and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' "
				+ " or pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3'"
				+ " or pcp.branch_clas_potential = 'A3' or pcp.branch_clas_potential = 'B3' "
				+ " or pcp.branch_clas_potential = 'C3' or pcp.branch_clas_potential = 'D3' "
				+ " or pcp.branch_clas_potential = 'E3' or pcp.sub_clas_potential = 'A3' "
				+ " or pcp.sub_clas_potential = 'B3' or pcp.sub_clas_potential = 'C3' "
				+ " or pcp.sub_clas_potential = 'D3' or pcp.sub_clas_potential = 'E3') "
				+ " and pcc.del_stat = '0' and pcc.claim_cust_mgr_id = ?) tab "
				+ " left join gcms_param_info gpi on gpi.name = tab.clas_potential and jkey = 'LATENT_SUCC') t1 ) t2";
		return Db.use(GBASE).findFirst(sql, data_date, userNo);
	}

	/**
	 * 查询用户是否有领导角色 2018年7月5日11:09:08
	 * 
	 * @author liutao
	 * @return 返回查询到的领导角色，如果没有返回空
	 */
	public List<Record> findWhetherLead(String userNo) {
		String sql = "select sri.id, sri.name from sys_user_role sur "
				+ "left join sys_role_info sri on sri.id = sur.role_id "
				+ "where sur.user_id = ? and sri.name like '领导-%' ";
		return Db.use(DEFAULT).find(sql, userNo);
	}

	/**
	 * 查询首页机构雷达图累计存款日均以及累计贷款日均 2018年7月5日15:10:41
	 * 
	 * @author liutao
	 * @param timePoint
	 *            查询时间点
	 * @return 返回机构累计存眷日均以及累计贷款日均
	 */
	public Record findOrgMoneyday(String userNo, String leadRole,
			String timePoint) {
		String sql = "select decode(sum(incomday), null, 0, sum(incomday)) as incomday, "
				+ "decode(sum(loansday), null, 0, sum(loansday)) as loansday from ( "
				+ "select  pcc.claim_cust_mgr_id, "
				+ "(nvl(pcpm.incomday, 0) * nvl(pcc.claim_prop, 0)/100) as incomday, "
				+ "(nvl(pcpm.loansday, 0) * nvl(pcc.claim_prop, 0)/100) as loansday "
				+ "from pccm_cust_claim pcc "
				+ "left join pccm_cust_pool pcp on pcp.id = pcc.cust_pool_id "
				+ "left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id "
				+ "left join sys_user_info sui on pcc.claim_cust_mgr_id = sui.id "
				+ "where 1=1 and pcc.del_stat = '0' and sui.org_id in ( "
				+ "select soi.id  from sys_org_info soi start with soi.id = ( "
				+ "select soi.orgnum from sys_org_info soi where soi.by2 = ? "
				+ "start with soi.id = (select org_id from sys_user_info where user_no = ? ) "
				+ "connect by prior soi.upid = soi.id )connect by soi.upid = prior soi.id)"
				+ "and pcp.clas_five is not null ";
		sql += getTimePointSql(timePoint) + " )";
		/**
		 * 另外一种写法的SQL备份,写法较上面的一种简单一点，目前数据量少查询出来的结果是一致的 select
		 * nvl((sum(nvl(pcpm.incomday, 0) * nvl(pcc.claim_prop, 0)/100)), 0) as
		 * incomday, nvl((sum(nvl(pcpm.loansday, 0) * nvl(pcc.claim_prop,
		 * 0)/100)), 0) as loansday from pccm_cust_claim pcc left join
		 * pccm_cust_pool pcp on pcp.id = pcc.cust_pool_id left join
		 * pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id left join
		 * sys_user_info sui on pcc.claim_cust_mgr_id = sui.id where 1=1 and
		 * pcc.del_stat = '0' and sui.org_id in ( select soi.id from
		 * sys_org_info soi start with soi.id = ( select soi.orgnum from
		 * sys_org_info soi where soi.by2 = 3 start with soi.id = (select org_id
		 * from sys_user_info where user_no = '6049744' ) connect by prior
		 * soi.upid = soi.id )connect by soi.upid = prior soi.id)
		 */
		return Db.use(DEFAULT).findFirst(sql, leadRole, userNo);
	}

	/**
	 * 查询首页机构雷达图加权数据 2018年7月5日16:14:01
	 * 
	 * @author liutao
	 * @param timePoint
	 *            查询时间点
	 * @param userNo
	 *            当前登录人EHR号
	 * @param leadRole
	 *            领导角色级别
	 * @return 返回机构加权
	 */
	public Record findOrgWeighting(String userNo, String leadRole,
			String timePoint) {
		String sql = "select decode(sum(newNum), null, 0, sum(newNum)) as newNum from ( "
				+ "select clas_five, decode(clas_five, 1, num*3, 2, num*2, 3, "
				+ "num*1, 4, num*0.6, 5, num*0.06) as newNum from ( "
				+ "select pcp.clas_five, count(1) as num from sys_user_info sui "
				+ "left join pccm_cust_claim pcc on pcc.claim_cust_mgr_id = sui.id "
				+ "left join pccm_cust_pool pcp on pcp.id = pcc.cust_pool_id "
				+ "where 1=1 and pcc.del_stat = '0' and sui.org_id in ( "
				+ "select soi.id  from sys_org_info soi start with soi.id = ( "
				+ "select soi.orgnum from sys_org_info soi where soi.by2 = ? "
				+ "start with soi.id = (select org_id from sys_user_info where user_no = ? ) "
				+ "connect by prior soi.upid = soi.id )connect by soi.upid = prior soi.id) "
				+ "and pcp.clas_five is not null and pcp.clas_five != '6' ";
		sql += getTimePointSql(timePoint);
		sql += " group by pcp.clas_five))";
		return Db.use(DEFAULT).findFirst(sql, leadRole, userNo);
	}

	/**
	 * 查询首页机构雷达图商机转化率数据 2018年7月6日09:39:42
	 * 
	 * @author liutao
	 * @param userNo
	 *            当前登录人EHR号
	 * @param leadRole
	 *            领导角色级别
	 * @param timePoint
	 *            查询时间点
	 * @return 返回机构雷达图商机转化率数据
	 */
	public Record findOrgMarkReach(String userNo, String leadRole,
			String timePoint) {
		String sql = "select round((succNum/decode(sumNum, 0, 1, sumNum)), 2) as reach from ( "
				+ "select (select count(1) from pccm_cust_claim pcc "
				+ "left join pccm_cust_pool pcp on pcc.cust_pool_id = pcp.id "
				+ "left join sys_user_info sui on sui.id = pcc.claim_cust_mgr_id "
				+ "where pcc.claim_cust_mgr_id = sui.id and pcc.del_stat = '0' "
				+ "and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') ";
		sql += getTimePointSql(timePoint);
		sql += "and pcc.marketing_stat = '1' and sui.org_id in ( "
				+ "select soi.id  from sys_org_info soi start with soi.id = ( "
				+ "select soi.orgnum from sys_org_info soi where soi.by2 = ? "
				+ "start with soi.id = (select org_id from sys_user_info where user_no = ? ) "
				+ "connect by prior soi.upid = soi.id )connect by soi.upid = prior soi.id)) as succNum, "
				+ "(select count(1) from pccm_cust_claim pcc "
				+ "left join pccm_cust_pool pcp on pcc.cust_pool_id = pcp.id "
				+ "left join sys_user_info sui on sui.id = pcc.claim_cust_mgr_id "
				+ "where pcc.claim_cust_mgr_id = sui.id and pcc.del_stat = '0' "
				+ "and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') ";
		sql += getTimePointSql(timePoint);
		sql += "and sui.org_id in ( select soi.id  from sys_org_info soi start with soi.id = ( "
				+ "select soi.orgnum from sys_org_info soi where soi.by2 = ? "
				+ "start with soi.id = (select org_id from sys_user_info where user_no = ? ) "
				+ "connect by prior soi.upid = soi.id )connect by soi.upid = prior soi.id)) as sumNum "
				+ "from dual) ";
		return Db.use(DEFAULT).findFirst(sql, leadRole, userNo, leadRole,
				userNo);
	}

	/**
	 * 查询首页机构雷达图潜在客户达成率数据 2018年7月6日10:18:47
	 * 
	 * @author liutao
	 * @param userNo
	 *            当前登录人EHR号
	 * @param leadRole
	 *            领导角色级别
	 * @param timePoint
	 *            查询时间点
	 * @return 返回潜在客户达成率数据
	 */
	public Record findOrgLatentReach(String userNo, String leadRole,
			String timePoint) {
		/*
		 * String sql =
		 * "select round((decode(succNum, null, 0, succNum)/decode(sumNum, null, 1, 0, 1, sumNum)), 2) "
		 * + "as reach from (select (select count(1) from ( " +
		 * "select (pcpm.incomday+pcpm.finaday) as incomday, " +
		 * "gpi.val*10000 as latent_succ_value from pccm_cust_claim pcm " +
		 * "left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id " +
		 * "left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id "
		 * +
		 * "left join gcms_param_info gpi on gpi.name = pcp.clas_potential and key = 'LATENT_SUCC' "
		 * + "left join sys_user_info sui on sui.id = pcm.claim_cust_mgr_id " +
		 * "where 1=1 and pcm.del_stat = '0' and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or "
		 * +
		 * "pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3') "
		 * ; sql += getTimePointSql(timePoint); sql +=
		 * "and sui.org_id in ( select soi.id  from sys_org_info soi start with soi.id = ( "
		 * + "select soi.orgnum from sys_org_info soi where soi.by2 = ? " +
		 * "start with soi.id = (select org_id from sys_user_info where user_no = ? ) "
		 * +
		 * "connect by prior soi.upid = soi.id )connect by soi.upid = prior soi.id) "
		 * +
		 * "and pcm.claim_cust_mgr_id = sui.id) tab where tab.incomday > latent_succ_value) as succNum, "
		 * + "(select count(1) from ( " +
		 * "select (pcpm.incomday+pcpm.finaday) as incomday, " +
		 * "gpi.val*10000 as latent_succ_value from pccm_cust_claim pcm " +
		 * "left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id " +
		 * "left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id "
		 * +
		 * "left join gcms_param_info gpi on gpi.name = pcp.clas_potential and key = 'LATENT_SUCC' "
		 * + "left join sys_user_info sui on sui.id = pcm.claim_cust_mgr_id " +
		 * "where 1=1 and pcm.del_stat = '0' and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or "
		 * +
		 * "pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3') "
		 * ; sql += getTimePointSql(timePoint); sql +=
		 * " and sui.org_id in ( select soi.id  from sys_org_info soi start with soi.id = ( "
		 * + "select soi.orgnum from sys_org_info soi where soi.by2 = ? " +
		 * "start with soi.id = (select org_id from sys_user_info where user_no = ? ) "
		 * +
		 * "connect by prior soi.upid = soi.id )connect by soi.upid = prior soi.id) "
		 * + "and pcm.claim_cust_mgr_id = sui.id) tab) as sumNum from dual)";
		 */

		String potentialSql = "";
		if ("1".equals(leadRole)) {
			potentialSql = "pcp.clas_potential as clas_potential ";
		} else if ("2".equals(leadRole)) {
			potentialSql = "(case when pcp.branch_clas_potential is not null then pcp.branch_clas_potential "
					+ "when pcp.clas_potential is not null then pcp.clas_potential "
					+ "end) as clas_potential ";
		} else {
			potentialSql = "(case when pcp.sub_clas_potential is not null then pcp.sub_clas_potential "
					+ "when pcp.branch_clas_potential is not null then pcp.branch_clas_potential "
					+ "when pcp.clas_potential is not null then pcp.clas_potential "
					+ "end) as clas_potential ";
		}
		String sql = "select round((decode(succNum, null, 0, succNum)/decode(sumNum, null, 1, 0, 1, sumNum)), 2) "
				+ "as reach from (select (select count(1) from ( "
				+ "select incomday, gpi.val*10000 as latent_succ_value from ("
				+ "select (pcpm.incomday+pcpm.finaday) as incomday, "
				+ potentialSql
				+ " from pccm_cust_claim pcm "
				+ "left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id "
				+ "left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id "
				+ "left join sys_user_info sui on sui.id = pcm.claim_cust_mgr_id "
				+ "where 1=1 and pcm.del_stat = '0' "
				+ "and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or "
				+ "pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3'"
				+ "or pcp.branch_clas_potential = 'A3' or pcp.branch_clas_potential = 'B3' "
				+ "or pcp.branch_clas_potential = 'C3' or pcp.branch_clas_potential = 'D3' "
				+ "or pcp.branch_clas_potential = 'E3' or pcp.branch_clas_potential = 'A3' "
				+ "or pcp.branch_clas_potential = 'B3' or pcp.branch_clas_potential = 'C3' "
				+ "or pcp.branch_clas_potential = 'D3' or pcp.branch_clas_potential = 'E3') ";
		sql += getTimePointSql(timePoint);
		sql += "and sui.org_id in ( select soi.id  from sys_org_info soi start with soi.id = ( "
				+ "select soi.orgnum from sys_org_info soi where soi.by2 = ? "
				+ "start with soi.id = (select org_id from sys_user_info where user_no = ? ) "
				+ "connect by prior soi.upid = soi.id )connect by soi.upid = prior soi.id) "
				+ "and pcm.claim_cust_mgr_id = sui.id) tab "
				+ "left join gcms_param_info gpi on gpi.name = tab.clas_potential and key = 'LATENT_SUCC' )"
				+ "where incomday > latent_succ_value ) as succNum, "
				+ "(select count(1) from ( "
				+ "select (pcpm.incomday+pcpm.finaday) as incomday from pccm_cust_claim pcm "
				+ "left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id "
				+ "left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id "
				+ "left join sys_user_info sui on sui.id = pcm.claim_cust_mgr_id "
				+ "where 1=1 and pcm.del_stat = '0' "
				+ "and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or "
				+ "pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3') ";
		sql += getTimePointSql(timePoint);
		sql += "and sui.org_id in ( select soi.id  from sys_org_info soi start with soi.id = ( "
				+ "select soi.orgnum from sys_org_info soi where soi.by2 = ? "
				+ "start with soi.id = (select org_id from sys_user_info where user_no = ? ) "
				+ "connect by prior soi.upid = soi.id )connect by soi.upid = prior soi.id) "
				+ "and pcm.claim_cust_mgr_id = sui.id) tab) as sumNum from dual)";
		return Db.use(DEFAULT).findFirst(sql, leadRole, userNo, leadRole,
				userNo);
	}

	/**
	 * 查询首页雷达图机构领导视图数据 2018年8月8日18:11:40
	 * 
	 * @author liutao
	 * @param userNo
	 * @param leadRole
	 *            机构等级
	 * @param timePoint
	 * @return
	 */
	public Record findOrgRadarMap(String userNo, String leadRole,
			String timePoint) {
		/*
		 * String sql= "select poli.incomday, poli.loansday, poli.weighting, " +
		 * " poli.latentreach, poli.markreach " +
		 * " from PCCM_ORG_LADERMAP_INFO poli where poli.org_id = ( " +
		 * " select soi.orgnum from sys_org_info soi where soi.by2 = ? " +
		 * " start with soi.id = (select org_id from sys_user_info where user_no = ?) "
		 * + " connect by prior soi.upid = soi.id ) and poli.time_point = ? ";
		 */
		// 不使用递归方式进行查询 2018年8月14日15:38:32 --liutao
		String findSql = "";
		if ("1".equals(leadRole)) {
			findSql = "select subStr(by5||','||soi.id, 1, 9) as by5 from sys_org_info soi where soi.id = ( "
					+ " select org_id from sys_user_info where user_no = ? )";
		} else if ("2".equals(leadRole)) {
			findSql = "select subStr(by5||','||soi.id, 11, 9) as by5 from sys_org_info soi where soi.id = ( "
					+ " select org_id from sys_user_info where user_no = ? )";
		} else if ("3".equals(leadRole)) {
			findSql = "select subStr(by5||','||soi.id, 21, 9) as by5 from sys_org_info soi where soi.id = ( "
					+ " select org_id from sys_user_info where user_no = ? )";
		} else {
			findSql = "select subStr(by5||','||soi.id, 31, 9) as by5 from sys_org_info soi where soi.id = ( "
					+ " select org_id from sys_user_info where user_no = ? )";
		}
		Record r = Db.use(DEFAULT).findFirst(findSql, userNo);
		if (null != r && null != r.getStr("by5") && !"".equals(r.getStr("by5"))) {
			String sql = "select poli.incomday, poli.loansday, poli.weighting, "
					+ " poli.latentreach, poli.markreach "
					+ " from PCCM_ORG_LADERMAP_INFO poli where poli.org_id = ? "
					+ " and poli.time_point = ? ";
			// 查询数据源切换到GBASE数据库，SQL没有问题，直接修改数据源 2018年8月21日10:05:20 --liutao
			r = Db.use(DEFAULT).findFirst(sql, r.getStr("by5"), timePoint);
			/* r = Db.use(GBASE).findFirst(sql, r.getStr("by5"), timePoint); */
			if (null == r) {
				r = new Record();
				r.set("incomday", 0);
				r.set("loansday", 0);
				r.set("weighting", 0);
				r.set("latentreach", 0);
				r.set("markreach", 0);
			}
		} else {
			r = new Record();
			r.set("incomday", 0);
			r.set("loansday", 0);
			r.set("weighting", 0);
			r.set("latentreach", 0);
			r.set("markreach", 0);
		}
		return r;
	}

	/**
	 * 用于获取当前、上周、上月以及上年的时间 获取时间需要根据当前时间是否是15号进行判断并得到正确的时间
	 * 例如：当前时间是20180114则当前数据时间是201712的数据则上月应该是20171115到20171215 ==》
	 * 当前时间是20180115则当前数据时间是201801的数据则上月应该是20171215到20180115
	 * 
	 * 获取当前的时间格式:20180101 获取上周的时间格式: 周一: 20180101 周一: 20180107
	 * 获取上月的开始时间格式:20171215 获取上月的开始时间格式:20180115 获取上年的时间格式:2016
	 * 
	 * @param param
	 *            =1代表获取当前时间 param=2代表获取上周一时间 param=3代表获取上周日时间 param=4代表获取上月开始时间
	 *            param=6代表获取上月结束时间 param=5代表获取上年时间
	 * @return
	 */
	private static String getFormatDate(int param) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf;
		if (2 == param) {
			// 上周一
			sdf = new SimpleDateFormat("yyyyMMdd");
			int day = cal.get(Calendar.DAY_OF_WEEK);
			// 如果当前时间是周日，则直接减去七天
			if (1 == day) {
				cal.add(Calendar.DATE, -7);
			}
			cal.set(Calendar.DAY_OF_WEEK, 1);
			cal.add(Calendar.DATE, -6);
			return sdf.format(cal.getTime());
		} else if (3 == param) {
			// 上周日
			sdf = new SimpleDateFormat("yyyyMMdd");
			int day = cal.get(Calendar.DAY_OF_WEEK);
			// 如果当前时间是周日，则直接减去七天
			if (1 == day) {
				cal.add(Calendar.DATE, -7);
			}
			cal.set(Calendar.DAY_OF_WEEK, 1);
			return sdf.format(cal.getTime());
		} else if (4 == param) {
			// 上月开始时间
			sdf = new SimpleDateFormat("yyyyMM");
			// 获取当前时间是当月几号
			int day = DateTimeUtil.getDayOfMonth();
			if (day < 15) {
				cal.add(Calendar.MONTH, -2);
			} else {
				cal.add(Calendar.MONTH, -1);
			}
			return sdf.format(cal.getTime()) + "15";
		} else if (5 == param) {
			// 上年
			sdf = new SimpleDateFormat("yyyy");
			// 获取当前时间是当月几号
			int day = DateTimeUtil.getDayOfMonth();
			if (day < 15) {
				// 如果小于15号需要判断当前月份是否是一月，
				// 如果是一月则应该获取当前时间的上2年时间，反之获取上年时间
				int month = DateTimeUtil.getMonth();
				if (1 == month) {
					cal.add(Calendar.YEAR, -2);
				} else {
					cal.add(Calendar.YEAR, -1);
				}
				return sdf.format(cal.getTime());
			} else {
				cal.add(Calendar.YEAR, -1);
				return sdf.format(cal.getTime());
			}
		} else if (6 == param) {
			// 上月结束时间
			sdf = new SimpleDateFormat("yyyyMM");
			int day = DateTimeUtil.getDayOfMonth();
			if (day < 15) {
				cal.add(Calendar.MONTH, -1);
			}
			return sdf.format(cal.getTime()) + "15";
		} else {
			sdf = new SimpleDateFormat("yyyyMMdd");
			return sdf.format(cal.getTime());
		}
	}

	/**
	 * 领导（个人）或者业务人员潜在客户转化率排名
	 * 
	 * @param user_no
	 * @param role_id
	 * @param timePoint
	 * @return
	 */
	public int findAllPersonLatentReach(String user_no, String role_id,
			String timePoint) {
		String data_date = findBaseInfoMaxDate();// 查询宽表最新数据日期
		String sql = "select row_number() over (order by t3.reach desc) as num from"
				+ " (select round((decode(succNum, null, 0, succNum)/decode(sumNum, null, 1, 0, 1, sumNum)), 2)"
				+ " as reach from (select count(1) as sumNum,sum(case when incomday > latent_succ_value then 1 else 0 end) as succNum "
				+ " from (select incomday, gpi.val*10000 as latent_succ_value from "
				+ " (select (pcbi.ck_nrj_zcy+pcbi.bn_fic_nrj+pcbi.bw_fic_nrj) as incomday,"
				+ " (case when pcp.sub_clas_potential is not null then pcp.sub_clas_potential"
				+ " when pcp.branch_clas_potential is not null then pcp.branch_clas_potential"
				+ " when pcp.clas_potential is not null then pcp.clas_potential end) as clas_potential,ur.user_id from pccm_cust_claim pcc"
				+ " left join pccm_cust_pool pcp on pcp.id = pcc.cust_pool_id"
				+ " left join pccm_cust_pool_money pcbi on (pcbi.id||pcbi.cust_no) = pcp.id and pcbi.data_date =? left join"
				+ " sys_user_role ur on pcc.claim_cust_mgr_id=ur.user_id where 1=1 "
				+ getTimePointSql(timePoint)
				+ " and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' "
				+ " or pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3'"
				+ " or pcp.branch_clas_potential = 'A3' or pcp.branch_clas_potential = 'B3' "
				+ " or pcp.branch_clas_potential = 'C3' or pcp.branch_clas_potential = 'D3' "
				+ " or pcp.branch_clas_potential = 'E3' or pcp.sub_clas_potential = 'A3' "
				+ " or pcp.sub_clas_potential = 'B3' or pcp.sub_clas_potential = 'C3' "
				+ " or pcp.sub_clas_potential = 'D3' or pcp.sub_clas_potential = 'E3') "
				+ " and pcc.del_stat = '0' and ur.role_id=? and pcc.claim_cust_mgr_id=?) tab "
				+ " left join gcms_param_info gpi on gpi.name = tab.clas_potential and jkey = 'LATENT_SUCC') t1 ) t2)t3"
				+ " order by t3.reach desc";
		Record r = Db.use(DEFAULT).findFirst(sql, data_date, role_id, user_no);
		if (null != r) {
			return r.getLong("num").intValue();
		} else {
			return 0;
		}
	}

	/**
	 * 领导（个人）或者业务人员商机客户转化率排名
	 * 
	 * @param user_no
	 * @param role_id
	 * @param timePoint
	 * @return
	 */
	public int findAllPersonMarkReach(String user_no, String role_id,
			String timePoint) {
		String sql = "select row_number() over (order by t.reach desc) as num "
				+ " from (select round((decode(succNum, null, 0, succNum)/decode(sumNum, 0, 1, sumNum)), 2) "
				+ " as reach from (select count(1) as sumNum,"
				+ " sum(case when pcc.marketing_stat = '1' then 1 else 0 end) as succNum "
				+ " from pccm_cust_claim pcc "
				+ " left join pccm_cust_pool pcp on pcc.cust_pool_id = pcp.id left join "
				+ " sys_user_role ur on pcc.claim_cust_mgr_id=ur.user_id"
				+ " where ur.role_id=? and pcc.claim_cust_mgr_id = ?"
				+ " and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') "
				+ getTimePointSql(timePoint)
				+ " and pcc.del_stat = '0') tab)t order by t.reach desc";
		Record r = Db.use(DEFAULT).findFirst(sql, role_id, user_no);
		if (null != r) {
			return r.getLong("num").intValue();
		} else {
			return 0;
		}
	}

	/**
	 * 查询GBASE宽表数据最大的时间是多少 2018年8月29日18:09:14
	 * 
	 * @author liutao
	 * @return
	 */
	public String findBaseInfoMaxDate() {
		String sql = "select to_char(max(data_date)) as data_date from ap_pccm.pccm_cust_base_info";
		Record r = Db.use(GBASE).findFirst(sql);
		if (null != r) {
			return r.getStr("data_date");
		} else {
			// 如果没有则返回当前时间
			return DateTimeUtil.getPathName();
		}
	}

	/**
	 * 通过机构ID查询所有子集id拼成'id1','id2','id3','id4'
	 * 
	 * @param orgId
	 * @return
	 */
	public String findChildOrgIdByOrgId(String orgId) {
		String sql = "select id from sys_org_info a where (by5 like ? or id = ?)"
				+ CommonUtil.orgNameSql();
		List<Record> list = Db.use(DEFAULT).find(sql, "%" + orgId + "%", orgId);
		if (null != list && list.size() > 0) {
			StringBuffer sbf = new StringBuffer();
			for (Record r : list) {
				sbf.append("'" + r.getStr("id") + "',");
			}
			return sbf.substring(0, sbf.length() - 1);
		} else {
			return "";
		}
	}

	/**
	 * 查询个人雷达图相关数据 2018年9月1日10:20:39
	 * 
	 * @author liutao
	 * @param userNo
	 * @param timePoint
	 * @return
	 */
	public Record findPersonRadarMap(String userNo, String timePoint) {
		String sql = "select * from PCCM_PERSON_LADERMAP_INFO where user_id = ? and time_point = ? ";
		// Record r = Db.use(GBASE).findFirst(sql, userNo, timePoint);

		// 切换到ORACLE数据库进行数据查询 2018年9月3日19:14:45 --liutao
		Record r = Db.use(DEFAULT).findFirst(sql, userNo, timePoint);
		if (r == null) {
			r = new Record();
			r.set("kpi", 0);
			r.set("incomday", 0);
			r.set("loansday", 0);
			r.set("weighting", 0);
			r.set("latentreach", 0);
			r.set("markreach", 0);
		}
		return r;
	}

	/**
	 * 查询潜在达成率明星客户经理 2018年9月7日11:16:25
	 * 
	 * @author liutao
	 * @return
	 */
	public List<Record> findReachStarLatent(String roleType) {
		String sql = "select name, orgname, head_url, num from ( "
				+ " select sui.name, soi.orgname, gra.head_url, round(puli.reach/0.2) as num "
				+ " from pccm_user_latent_info puli "
				+ " left join gcms_role_apply gra on gra.user_id = puli.user_no "
				+ " left join sys_role_info sri on sri.id = gra.role_id and sri.kpi_flag is not null "
				+ " left join sys_user_info sui on sui.id = puli.user_no "
				+ " left join sys_org_info soi on soi.id = sui.org_id "
				+ " where 1=1 and sri.kpi_flag = ? order by reach desc ) where rownum < 6 ";
		List<Record> records = Db.use(DEFAULT).find(sql, roleType);
		return records;
	}

	/**
	 * 查询GBASE宽表数据最大的时间是多少 2018年8月29日18:09:14
	 * 
	 * @author liutao
	 * @return
	 */
	public String findPoolMoneyInfoMaxDate() {
		String sql = "select to_char(max(data_date)) as data_date from pccm_cust_pool_money ";
		Record r = Db.use(DEFAULT).findFirst(sql);
		if (null != r) {
			return r.getStr("data_date");
		} else {
			// 如果没有则返回当前时间
			return DateTimeUtil.getPathName();
		}
	}

	/**
	 * 该业务人员的潜在客户达成率及排名，商机客户转化率及排名
	 * 
	 * @param user_no
	 * @param timePoint
	 * @param role_id
	 * @return
	 */
	public Record findPersonLatentMark(String user_no, String timePoint,
			String roleName) {
		String sql = "select a.latentreach, a.num as latent_rank, b.markreach, b.num as mark_rank"
				+ " from (select p.user_id, p.latentreach, p.num"
				+ " from (select ppli.user_id,"
				+ "ppli.latentreach,"
				+ "row_number() over(order by ppli.latentreach desc) as num"
				+ " from pccm_person_ladermap_info ppli, sys_user_info sui,sys_role_info sri"
				+ " where ppli.user_id = sui.user_no and sui.role_id=sri.id and ppli.user_id=? and sri.name like ? and ppli.time_point=?) p) a,"
				+ "(select p.user_id, p.markreach, p.num"
				+ " from (select ppli.user_id,"
				+ "ppli.markreach,"
				+ "row_number() over(order by ppli.markreach desc) as num"
				+ " from pccm_person_ladermap_info ppli, sys_user_info sui,sys_role_info sri"
				+ " where ppli.user_id = sui.user_no and sui.role_id=sri.id and ppli.user_id=? and sri.name like ? and ppli.time_point=?) p) b"
				+ " where a.user_id = b.user_id";
		Record record = Db.use(DEFAULT).findFirst(sql, user_no,
				"%" + roleName + "%", timePoint, user_no, "%" + roleName + "%",
				timePoint);
		return record;
	}
}
