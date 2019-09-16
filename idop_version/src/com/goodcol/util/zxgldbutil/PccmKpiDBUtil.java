package com.goodcol.util.zxgldbutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;

public class PccmKpiDBUtil {
	private static final String DEFAULT = "default";
	private static final String GBASE = "gbase";
	
	/**
	 * 客户经理kpi列表
	 */
	public Page<Record> pageList(Map<String, Object> map){
		// 获取查询参数
		int pageNum = (Integer)map.get("pageNum");
		int pageSize = (Integer)map.get("pageSize");;
		Map<String, Object> maps = findSql(map);
		@SuppressWarnings("unchecked")
		Page<Record> records = Db.use("default").paginate(pageNum, pageSize, (String) maps.get("selectSql"), (String) maps.get("extrasql"), ((List<String>) maps.get("sqlStr")).toArray());
		return records;
	}
	
	public Map<String, Object> findSql(Map<String, Object> map) {
		// 获取当前用户信息
//		String selectSql = " select i.kpi_flag cust_type, "
//				+"        k.id, "
//				+"        k.period, "
//				+"        u.name, "
//				+"        u.org_id, "
//				+"        o.unit_name orgname, "
//				+"        k.cust_mgr_no, "
//				+"        o2.unit_name second_orgname, "
//				+"        o1.unit_name third_orgname, "
//				+"        u.cur_post, "
//				+"        c4.name as mgr_level, "
//				+"        k.kpi, "
//				+"        a.promote_stand, "
//				+"        a.trans_stand, "
//				+"        a.next_value, "
//				+"        case when nvl(kpi, 0) < 100 then '否' else '是' end is_stand ";
//		String extrasql = "   from pccm_kpi_info k "
//				+"   left join sys_user_info u on k.cust_mgr_no = u.user_no "
//				+"   left join gcms_role_apply c3 on u.user_no = c3.user_id "
//				+"   left join gcms_param_info c4 on c4.val = u.user_level and c4.jkey = 'ZJ' "
//				+"   left join sys_role_info i on i.id = c3.role_id  "
//				+"   left join hr_org_info o on u.org_id = o.unit_id "
//				+"   left join hr_org_info o1 on o.super_unitid = o1.unit_id "
//				+"   left join hr_org_info o2 on o1.super_unitid = o2.unit_id "
//				+"   left join pccm_apply_role a on a.spec_level = c4.val ";
//		StringBuffer whereSql = new StringBuffer(" where 1=1 and i.kpi_flag is not null ");
		String selectSql = " select i.kpi_flag cust_type, "
				+"        k.id, "
				+"        k.period, "
				+"        u.name, "
				+"        u.org_id, "
				+"        o.orgname orgname, "
				+"        k.cust_mgr_no, "
				+"        o2.orgname second_orgname, "
				+"        o1.orgname third_orgname, "
				+"        i.name cur_post, "
				+"        c4.name as mgr_level, "
				+"        k.kpi, "
				+"        a.promote_stand, "
				+"        a.trans_stand, "
				+"        a.next_value, "
				+"        case when nvl(kpi, 0) < 100 then '否' else '是' end is_stand ";
		String extrasql = "   from pccm_kpi_info k "
				+"   left join sys_user_info u on k.cust_mgr_no = u.user_no "
				+"   left join gcms_role_apply c3 on u.user_no = c3.user_id "
//				+"   left join hr_v_post_info h on h.post_id = u.cur_post "
				+"   left join gcms_param_info c4 on c4.val = u.post_level and c4.key = 'post_level' "
				+"   left join sys_role_info i on i.id = c3.role_id  "
				+"   left join sys_org_info o on u.org_id = o.id "
				+"   left join sys_org_info o1 on o.upid = o1.id "
				+"   left join sys_org_info o2 on o1.upid = o2.id "
				+"   left join pccm_apply_role a on a.spec_level = c4.val and a.role_type= i.kpi_flag ";
		StringBuffer whereSql = new StringBuffer(" where 1=1 and i.kpi_flag is not null ");
		List<String> sqlStr = new ArrayList<String>();
		
		//默认查询条件
//		 whereSql.append(" and p.org_id=? ");
//		 sqlStr.add(orgId.trim());

		// 获取页面输入查询条件
		String cust_no = (String)map.get("cust_no");
		String name = (String)map.get("name");	
		String orgArr = (String)map.get("orgArr");	
		String date	= (String)map.get("date");
		String base_role	= (String)map.get("base_role");
		if (AppUtils.StringUtil(cust_no) != null) {
			 whereSql.append(" and u.user_no=? ");
			 sqlStr.add(cust_no.trim());
		}
		if (AppUtils.StringUtil(name) != null) {
			whereSql.append(" and u.name like ? ");
			sqlStr.add("%" + name.trim() + "%");
		}
		if (AppUtils.StringUtil(orgArr) != null) {
			 whereSql.append(" and u.org_id in "+orgArr);
		}
		if (AppUtils.StringUtil(date) != null) {
			whereSql.append(" and k.period= ? ");
			sqlStr.add(date.trim());
		}
		if (AppUtils.StringUtil(base_role) != null) {
			whereSql.append(" and i.id= ? ");
			sqlStr.add(base_role.trim());
		}
		
		//排序
		whereSql.append(" order by u.org_id ");
		extrasql += whereSql.toString();
		Map<String, Object> mapSql = new HashMap<String, Object>();
		mapSql.put("selectSql", selectSql);
		mapSql.put("extrasql", extrasql);
		mapSql.put("sqlStr", sqlStr);
		return mapSql;
	}
	
	/**
	 * 列表查询
	 */
	public List<Record> findList(Map<String, Object> map) {
		Map<String, Object> maps = findSql(map);
		@SuppressWarnings("unchecked")
		List<Record> reList = Db.use("default").find((String) maps.get("selectSql") + (String) maps.get("extrasql"), ((List<String>) maps.get("sqlStr")).toArray());
		return reList;

	}
	
	
	/**
	 * 详情查询
	 */
	public Page<Record> pageDetail(Map<String, Object> map){
		//List<Record> list = Db.use("default").find(" select * from pccm_kpi_cust_info where cust_mgr_no=? and period=? ", new Object[]{mgr_no,period});
		
		// 获取查询参数
		int pageNum = (Integer)map.get("pageNum");
		int pageSize = (Integer)map.get("pageSize");
		
		String mgr_no = (String)map.get("mgr_no");
		String period = (String)map.get("period");
		String clas_five = (String)map.get("clas_five");
		String sql = " from pccm_kpi_cust_info t "
				+" left join sys_org_info o on o.id= t.orgnum "
				+" where 1=1 ";
		if(AppUtils.StringUtil(mgr_no)!=null){
			sql+="and t.cust_mgr_no= '"+mgr_no+"'";
		}
		if(AppUtils.StringUtil(period)!=null){
			sql+="and t.period= '"+period+"'";
		}
		if(AppUtils.StringUtil(clas_five)!=null){
			sql+="and t.clas_five= '"+clas_five+"'";
		}
		Page<Record> records = Db.use("default").paginate(pageNum, pageSize, " select o.orgname, t.* ", 
				sql, new Object[]{});
		return records;
		
	}
	
	/**
	 * 根据客户经理号查询客户经理类型
	 */
	public String getMgrType(String mgr_no){
		String type = Db.use("default").queryStr(" select t.user_type from sys_user_info t where t.user_no=? ",new Object[]{mgr_no});
		return type;
	}
	
	/**
	 * 五层分类列表查询
	 */
	public List<Record> clasFiveList(Map<String, Object> map) {
		String mgr_id = (String)map.get("mgr_id");
		String period = (String)map.get("period");

		String sql = " select g.name clas_five_cn,g.val clas_five,nvl(kpi,0) kpi "
				+"   from (select nvl(sum(kpi), 0) kpi, "
				+"                     clas_five "
				+"                from pccm_kpi_cust_info "
				+"               where 1 = 1 "
				+"                 and cust_mgr_no = ? "
				+"                 and period = ? "
				+"               group by clas_five) c "
				+"   right join gcms_param_info g "
				+"     on g.val = c.clas_five "
				+"  where g.key='clas_five' "
				+"  order by g.val ";
		List<Record> reList = Db.use("default").find(sql,new Object[]{mgr_id,period});
		return reList;

	}
	
	/**
	 * kpi折线图查询
	 */
	public List<Record> kpiLineData(String mgr_id) {
		String sql = " select id, kpi, to_char(to_number(substr(period, 5)))||'月' period from pccm_kpi_info where substr(period, 1, 4) = to_char(sysdate, 'yyyy') " +
				"and cust_mgr_no = ? order by period ";
		List<Record> reList = Db.use("default").find(sql,new Object[]{mgr_id});
		return reList;

	}
	
	/**
	 * kpi饼图查询
	 */
	public List<Record> kpiPieData(String mgr_id) {
		String sql = " select id, kpi, to_number(substr(period, 5))||'月' period from pccm_kpi_info where substr(period, 1, 4) = to_char(sysdate, 'yyyy') " +
				"and cust_mgr_no = ? order by period ";
		List<Record> reList = Db.use("default").find(sql,new Object[]{mgr_id});
		return reList;
		
	}
	
	/**
	 * 查询平移值，晋升值，中位置
	 */
	public Record getDatas(String mgr_id) {
		String sql = " select pa.trans_stand,pa.promote_stand,pa.next_value from sys_user_info u " 
				+ " left join gcms_role_apply ga on ga.user_id = u.id "
				+ " left join gcms_param_info gp on gp.id = ga.role_id "
				+ " left join pccm_apply_role pa on u.user_level = pa.spec_level  "
				+ " where ga.apply_status='1' and role_type='1' and u.id=? ";
		Record re = Db.use("default").findFirst(sql,new Object[]{mgr_id});
		return re;
	}
	
	/**
	 * 获取期数下拉框到的值
	 */
	public List<Record> getDateDatas() {
		String sql = " select distinct period from pccm_kpi_info order by period asc ";
		List<Record> re = Db.use("default").find(sql);
		return re;
	}
	
	/**
	 * 查询商机转化率数据
	 * 2018年6月26日09:59:44
	 * @author liutao
	 * @param map 查询条件参数
	 * @return 返回商机转化率数据
	 */
	public Page<Record> findMarketCustomer(Map<String, Object> map){
		// 获取查询参数
		int pageNum = (Integer)map.get("pageNum");
		int pageSize = (Integer)map.get("pageSize");
		
		String custName = (String)map.get("custName");
		String userNo = (String)map.get("userNo");
		String sql = "select pcp.id, pcp.dummy_cust_no, pcp.customername, pcp.relate_cust_name, "
				+ "sui.name, sui.phone, pcc.marketing_stat, pcp.customercode, pcp.clas_five, pcp.clas_potential ";
		String fromSql = "from pccm_cust_claim pcc "
				+ "left join pccm_cust_pool pcp on pcc.CUST_POOL_ID = pcp.id "
				+ "left join sys_user_info sui on sui.user_no = pcp.cust_manager "
				+ "where 1=1 and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') "
				+ "and pcp.dummy_cust_no is not null and pcp.deptlevel = '3' "
				+ "and pcc.del_stat = '0' and pcc.claim_cust_mgr_id = ? ";
		List<String> listStr = new ArrayList<String>();
		listStr.add(userNo);
		if (AppUtils.StringUtil(custName) != null) {
			fromSql += " and pcp.customername like ? ";
			listStr.add("%" + custName + "%");
		}
		fromSql += " order by pcp.indate desc";
		Page<Record> pages = Db.use(DEFAULT).paginate(pageNum, pageSize, sql,
				fromSql, listStr.toArray());
		return pages;
		
		//切换到GBASE数据库进行数据查询  2018年8月30日14:51:47 --liutao
		/*int pageNum = (Integer)map.get("pageNum");
		int pageSize = (Integer)map.get("pageSize");
		
		String custName = (String)map.get("custName");
		String userNo = (String)map.get("userNo");
		String sql = "select pcp.id, pcp.dummy_cust_no, pcp.customername, pcp.relate_cust_name, "
				+ "sui.name, sui.phone, pcc.marketing_stat, pcp.customercode, pcp.clas_five, pcp.clas_potential ";
		String fromSql = "from pccm_cust_claim pcc "
				+ "left join pccm_cust_pool pcp on pcc.CUST_POOL_ID = pcp.id "
				+ "left join sys_user_info sui on sui.user_no = pcp.cust_manager "
				+ "where 1=1 and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') "
				+ "and pcp.dummy_cust_no is not null "
				+ "and pcc.del_stat = '0' and pcc.claim_cust_mgr_id = ? ";
		List<String> listStr = new ArrayList<String>();
		listStr.add(userNo);
		if (AppUtils.StringUtil(custName) != null) {
			fromSql += " and pcp.customername like ? ";
			listStr.add("%" + custName + "%");
		}
		fromSql += " order by pcp.indate desc";
		Page<Record> pages = Db.use(GBASE).paginate(pageNum, pageSize, sql,
				fromSql, listStr.toArray());
		return pages;*/
	}
	
	/**
	 * 查询商机客户数、转化客户数以及排名
	 * 2018年6月26日11:08:31
	 * @author liutao
	 * @return
	 */
	public Record findMarketCustomerRank(String userNo/*, String orgLevel*/){
		/*String sql = "select * from ( "
				+ "select a.claim_cust_mgr_id, a.succNum, a.sumNum, round(a.reach, 4)*100 as reach, rownum as rankNum from ( "
				+ "select tab.claim_cust_mgr_id, tab.succNum, tab.sumNum, "
				+ "(tab.succNum/decode(tab.sumNum, 0, 1, tab.sumNum)) as reach from ( "
				+ "select pccm.claim_cust_mgr_id, "
				+ "(select count(1) from pccm_cust_pool pcp "
				+ "left join pccm_cust_claim pcc on pcc.cust_pool_id = pcp.id "
				+ "where pcc.claim_cust_mgr_id = pccm.claim_cust_mgr_id and pcc.del_stat = '0' " 
				+ "and pcc.marketing_stat = '1' ";
		sql += "and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3')) as succNum, "
				+ "(select count(1) from pccm_cust_pool pcp "
				+ "left join pccm_cust_claim pcc on pcc.cust_pool_id = pcp.id "
				+ "where pcc.claim_cust_mgr_id = pccm.claim_cust_mgr_id and pcc.del_stat = '0' " 
				+ " ";
		sql += "and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3')) as sumNum "
				+ "from pccm_cust_claim pccm "
				+ "left join sys_user_info sui on sui.user_no = pccm.claim_cust_mgr_id "
				+ "where sui.org_id in ( "
				+ "select soi.id  from sys_org_info soi start with soi.id = ( "
				+ "select soi.orgnum from sys_org_info soi where soi.by2 = ? "
				+ "start with soi.id = (select org_id from sys_user_info where user_no = ? ) "
				+ "connect by prior soi.upid = soi.id )connect by soi.upid = prior soi.id) "
				+ "group by pccm.claim_cust_mgr_id ) tab order by reach desc) a ) where claim_cust_mgr_id = ? ";*/
		//return Db.use(DEFAULT).findFirst(sql, orgLevel, userNo, userNo);
		
		String sql = "select * from pccm_user_market_info where user_no = ? ";
		return Db.use(DEFAULT).findFirst(sql, userNo);
		
		//切换到GBASE数据库进行数据查询  2018年8月30日14:15:48 --liutao
		/*String sql = "select * from pccm_user_market_info where user_no = ? ";
		return Db.use(GBASE).findFirst(sql, userNo);*/
	}
	
	/**
	 * 查询所有的潜在客户经理
	 * 2018年7月9日09:40:51
	 * @author liutao
	 * @return
	 */
	public List<Record> findLatentUser(){
		/*String sql = "select pcc.claim_cust_mgr_id as user_no " 
				+ "from pccm_cust_claim pcc where 1=1 " 
				+ "and pcc.del_stat = '0' and pcc.claim_cust_mgr_id is not null " 
				+ "group by pcc.claim_cust_mgr_id ";
		return Db.use(DEFAULT).find(sql);*/
		
		//切换到GBASE数据库进行数据查询  2018年8月28日09:42:07 --liutao
		String sql = "select pcc.claim_cust_mgr_id as user_no " 
				+ "from pccm_cust_claim pcc where 1=1 " 
				+ "and pcc.del_stat = '0' and pcc.claim_cust_mgr_id is not null " 
				+ "group by pcc.claim_cust_mgr_id ";
		return Db.use(GBASE).find(sql);
	}
	
	/**
	 * 根据用户登录名查询潜在客户相关数(潜在客户达成数和潜在客户总数)
	 * 2018年7月9日09:46:07
	 * @author liutao
	 * @param user_no
	 * @return
	 */
	public int findLatentNumByUserNo(String user_no, String flag){
		/*String sql = "select count(1) as num from ( select (pcpm.incomday+pcpm.finaday) as incomday, "
			+ "gpi.val*10000 as latent_succ_value from pccm_cust_claim pcm "
			+ "left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id "
			+ "left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id "
			+ "left join gcms_param_info gpi on gpi.name = pcp.clas_potential and key = 'LATENT_SUCC' "
			+ "where 1=1 and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or "
			+ "pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3') and pcm.del_stat = '0' "
			+ "and pcm.claim_cust_mgr_id = ?)";*/
		/*String sql = "select count(1) as num from ("
				+ "select incomday, gpi.val*10000 as latent_succ_value "
				+ "from (select (pcpm.incomday+pcpm.finaday) as incomday, "
				+ "(case when pcp.sub_clas_potential is not null then pcp.sub_clas_potential "
				+ "when pcp.branch_clas_potential is not null then pcp.branch_clas_potential "
				+ "when pcp.clas_potential is not null then pcp.clas_potential "
				+ "end) as clas_potential from pccm_cust_claim pcm "
				+ "left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id "
				+ "left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id "
				+ "where 1=1 and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or "
				+ "pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3' "
				+ "or pcp.branch_clas_potential = 'A3' or pcp.branch_clas_potential = 'B3' "
				+ "or pcp.branch_clas_potential = 'C3' or pcp.branch_clas_potential = 'D3' "
				+ "or pcp.branch_clas_potential = 'E3' or pcp.sub_clas_potential = 'A3' "
				+ "or pcp.sub_clas_potential = 'B3' or pcp.sub_clas_potential = 'C3' "
				+ "or pcp.sub_clas_potential = 'D3' or pcp.sub_clas_potential = 'E3') "
				+ " "
				+ "and pcm.del_stat = '0' and pcm.claim_cust_mgr_id = ?) tab left join "
				+ "gcms_param_info gpi on gpi.name = tab.clas_potential and key = 'LATENT_SUCC')";
		if("succ".equals(flag)){
			sql += "where incomday > latent_succ_value ";
		}
		Record r = Db.use(DEFAULT).findFirst(sql, user_no);
		if(r != null){
			return r.getBigDecimal("num").intValue();
		}else{
			return 0;
		}*/
		
		//切换到GBASE数据库进行数据查询  2018年8月28日09:44:23 --liutao
		String data_date = findBaseInfoMaxDate();//查询宽表最新数据日期
		String sql = "select count(1) as num from ("
				+ "select incomday, gpi.val*10000 as latent_succ_value "
				+ "from (select (nvl(pcbi.ck_nrj_zcy, 0)+nvl(pcbi.bn_fic_nrj, 0)+nvl(pcbi.bw_fic_nrj, 0)) as incomday, "
				+ "(case when pcp.sub_clas_potential is not null and pcp.sub_clas_potential <> '' then pcp.sub_clas_potential "
				+ "when pcp.branch_clas_potential is not null and pcp.branch_clas_potential <> '' then pcp.branch_clas_potential "
				+ "when pcp.clas_potential is not null then pcp.clas_potential "
				+ "end) as clas_potential from pccm_cust_claim pcm "
				+ "left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id "
				+ "left join pccm_cust_base_info pcbi on pcbi.id||pcbi.cust_no = pcp.id and pcbi.data_date = ? "
				+ "where 1=1 and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or "
				+ "pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3' "
				+ "or pcp.branch_clas_potential = 'A3' or pcp.branch_clas_potential = 'B3' "
				+ "or pcp.branch_clas_potential = 'C3' or pcp.branch_clas_potential = 'D3' "
				+ "or pcp.branch_clas_potential = 'E3' or pcp.sub_clas_potential = 'A3' "
				+ "or pcp.sub_clas_potential = 'B3' or pcp.sub_clas_potential = 'C3' "
				+ "or pcp.sub_clas_potential = 'D3' or pcp.sub_clas_potential = 'E3') "
				+ "and pcm.del_stat = '0' and pcm.claim_cust_mgr_id = ?) tab left join "
				+ "gcms_param_info gpi on gpi.name = tab.clas_potential and jkey = 'LATENT_SUCC') t ";
		if("succ".equals(flag)){
			sql += "where incomday > latent_succ_value ";
		}
		Record r = Db.use(GBASE).findFirst(sql, data_date, user_no);
		if(r != null){
			return r.getLong("num").intValue();
		}else{
			return 0;
		}
	}
	
	/**
	 * 根据用户名查询用户省行，支行，分行非排名
	 * 2018年7月9日11:25:17
	 * @author liutao
	 * @param user_no
	 * @return
	 */
	public int findUserRank(String user_no, String orgLevel){
		/*String sql = "select num from ( select user_no, reach, rownum as num from ( "
			+ "select pul.user_no, to_number(pul.reach) as reach from pccm_user_latent_info pul "
			+ "left join sys_user_info sui on sui.user_no = pul.user_no "
			+ "where 1=1 and sui.org_id in ( select soi.id  from sys_org_info soi "
			+ "start with soi.id = ( select soi.orgnum from sys_org_info soi where soi.by2 = ? "
			+ "start with soi.id = (select org_id from sys_user_info where user_no = ? ) "
			+ "connect by prior soi.upid = soi.id )connect by soi.upid = prior soi.id) " 
			+ "order by reach desc)) where user_no = ? ";*/
		//不使用递归方式进行数据查询  改用别的方式进行查询
		//首先查询出当前用户所在机构的所有上级机构，再根据orgLevel得到唯一的机构号
		String findSql = "";
		if("1".equals(orgLevel)){
			findSql = "select subStr(by5||','||soi.id, 1, 9) as by5 from sys_org_info soi where soi.id = ( "
					+ " select org_id from sys_user_info where user_no = ? )";
		}else if("2".equals(orgLevel)){
			findSql = "select subStr(by5||','||soi.id, 11, 9) as by5 from sys_org_info soi where soi.id = ( "
					+ " select org_id from sys_user_info where user_no = ? )";
		}else{
			findSql = "select subStr(by5||','||soi.id, 21, 9) as by5 from sys_org_info soi where soi.id = ( "
					+ " select org_id from sys_user_info where user_no = ? )";
		}
		Record r = Db.use(DEFAULT).findFirst(findSql, user_no);
		/*if(null != r && null != r.getStr("by5") && !"".equals(r.getStr("by5"))){
			String by5 = r.getStr("by5");
			String sql = "select num from ( select user_no, reach, rownum as num from ( "
					+ " select pul.user_no, to_number(pul.reach) as reach from pccm_user_latent_info pul "
					+ " left join sys_user_info sui on sui.user_no = pul.user_no "
					+ " where 1=1 and sui.org_id in ( select soi1.id from sys_org_info soi1 where by5 like ? " 
					+ " or soi1.id = ? )"
					+ " order by reach desc)) where user_no = ? ";
			r = Db.use(DEFAULT).findFirst(sql, "%" + by5 + "%", by5, user_no);
			if(null != r){
				return r.getBigDecimal("num").intValue();
			}else{
				return 0;
			}
		}else{
			return 0;
		}*/
		
		//切换到GBASE数据库进行数据查询  2018年8月28日10:38:29  --liutao
		
		if(null != r && null != r.getStr("by5") && !"".equals(r.getStr("by5"))){
			String by5 = r.getStr("by5");
			//查询机构下的所有子机构
			String sql = "select num from ( select user_no, reach, row_number() over (order by reach desc) as num from ( "
					+ " select pul.user_no, to_number(pul.reach) as reach from pccm_user_latent_info pul "
					+ " left join sys_user_info sui on sui.user_no = pul.user_no "
					+ " where 1=1 and sui.org_id in (select id from sys_org_info where by5 like ? or id = ?)"
					+ " order by reach desc)t1)t2 where user_no = ? ";
			r = Db.use(GBASE).findFirst(sql, "%" + by5 + "%" , by5, user_no);
			if(null != r){
				return r.getLong("num").intValue();
			}else{
				return 0;
			}
		}else{
			return 0;
		}
	}
	
	/**
	 * 保存客户经理潜在客户总数、潜在达成数以及潜在客户达成率
	 * 2018年7月9日10:08:33
	 * @author liutao
	 */
	public void saveUserLatent(Record r){
		/*String delSql = "delete pccm_user_latent_info where user_no = ? ";
		//删除指定的客户经理数据重新添加
		Db.use(DEFAULT).update(delSql, r.getStr("user_no"));
		String sql = "insert into PCCM_USER_LATENT_INFO(user_no, SUCCNUM, SUMNUM, REACH, " 
				+ "P_RANK, B_RANK, S_RANK)values(?, ?, ?, ?, ?, ?, ?)";
		Db.use(DEFAULT).update(sql, r.getStr("user_no"), r.getStr("succnum"), 
				r.getStr("sumnum"), r.getStr("reach"), r.getStr("p_rank"), 
				r.getStr("b_rank"), r.getStr("s_rank"));*/
		
		//切换到GBASE数据库进行数据查询  2018年8月28日11:18:41 --liutao
		String delSql = "delete pccm_user_latent_info where user_no = ? ";
		//删除指定的客户经理数据重新添加
		Db.use(GBASE).update(delSql, r.getStr("user_no"));
		String sql = "insert into PCCM_USER_LATENT_INFO(user_no, SUCCNUM, SUMNUM, REACH, " 
				+ "P_RANK, B_RANK, S_RANK)values(?, ?, ?, ?, ?, ?, ?)";
		Db.use(GBASE).update(sql, r.getStr("user_no"), r.getStr("succnum"), 
				r.getStr("sumnum"), r.getStr("reach"), r.getStr("p_rank"), 
				r.getStr("b_rank"), r.getStr("s_rank"));
	}
	
	/**
	 * 根据登录用户查询潜在客户相关数以及个人排名
	 * 2018年7月9日15:04:03
	 * @author liutao
	 * @param userNo
	 * @return
	 */
	public Record findLatentCustomerRank(String userNo){
		String sql = "select pul.sumnum, pul.succnum, pul.reach, pul.p_rank||'/'||pul.b_rank||'/'||pul.s_rank as ranknum "
				+ "from pccm_user_latent_info pul where pul.user_no = ?";
		return Db.use(DEFAULT).findFirst(sql, userNo);
		
		//切换到GBASE数据库进行数据查询   2018年8月27日16:07:02  --liutao
		/*String sql = "select pul.sumnum, pul.succnum, pul.reach, pul.p_rank||'/'||pul.b_rank||'/'||pul.s_rank as ranknum "
				+ "from pccm_user_latent_info pul where pul.user_no = ?";
		return Db.use(GBASE).findFirst(sql, userNo);*/
	}
	
	/**
	 * 查询潜在客户数据
	 * 2018年6月26日09:59:44
	 * @author liutao
	 * @param map 查询条件参数
	 * @return 返回潜在客户数据
	 */
	public Page<Record> findLatentCustomer(Map<String, Object> map){
		// 获取查询参数
		int pageNum = (Integer)map.get("pageNum");
		int pageSize = (Integer)map.get("pageSize");
		
		String custName = (String)map.get("custName");
		String custNo = (String)map.get("custNo");
		String userNo = (String)map.get("userNo");
		/*String sql = "select pcp.id,(pcpm.incomday+pcpm.finaday) as incomday, pcp.customername,"
				+ "gpi.val*10000 as latent_succ_value, soi.orgname, pcp.customercode, "
				+ "gpi1.name as latent_name, gpi2.name as clas_five_name, pcp.clas_potential, "
				+ "case when (incomday >= (gpi.val*10000)) then '是' else '否'end as flag ";
		String fromSql = "from pccm_cust_claim pcm "
				+ "left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id "
				+ "left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id "
				+ "left join gcms_param_info gpi on gpi.name = pcp.clas_potential and gpi.key = 'LATENT_SUCC' "
				+ "left join gcms_param_info gpi1 on gpi1.val = pcp.clas_potential and gpi1.key = 'LATENT_TYPE' "
				+ "left join gcms_param_info gpi2 on gpi2.val = pcp.clas_five and gpi2.key = 'CLAS_FIVE' "
				+ "left join sys_org_info soi on soi.bancsid = pcp.org_id "
				+ "where 1=1 and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or "
				+ "pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3') "
				+ "and pcm.del_stat = '0' and pcm.claim_cust_mgr_id = ? ";*/
		
		//查询客户池钱表的最新数据日期
		String date = findPoolMoneyInfoMaxDate();
		String sql = "select tab.id, incomday, customername, gpi.val*10000 as latent_succ_value, "
				+ "gpi1.name as latent_name, orgname, customercode, clas_five_name, clas_potential, "
				+ "case when (incomday >= (gpi.val*10000)) then '是' else '否'end as flag ";
		String fromSql = "from (select pcp.id,(nvl(pcpm.ck_nrj_zcy, 0)+nvl(pcpm.bn_fic_nrj, 0)+nvl(pcpm.bw_fic_nrj, 0)) as incomday, pcp.customername,"
				+ "soi.orgname, pcp.customercode, gpi2.name as clas_five_name, "
				+ "(case when pcp.sub_clas_potential is not null then pcp.sub_clas_potential "
				+ "when pcp.branch_clas_potential is not null then pcp.branch_clas_potential "
				+ "when pcp.clas_potential is not null then pcp.clas_potential "
				+ "end) as clas_potential from pccm_cust_claim pcm "
				+ "left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id and pcp.deptlevel = '3' "
				+ "left join pccm_cust_pool_money pcpm on (pcpm.id||pcpm.cust_no) = pcp.id " 
				+ " and pcpm.deptlevel = '3' and pcpm.data_date = '"+ date +"' "
				+ "left join gcms_param_info gpi2 on gpi2.val = pcp.clas_five and gpi2.key = 'CLAS_FIVE' "
				+ "left join sys_org_info soi on soi.id = pcp.ORGNUM "
				+ "where 1=1 and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or "
				+ "pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3' "
				+ "or pcp.branch_clas_potential = 'A3' or pcp.branch_clas_potential = 'B3' "
				+ "or pcp.branch_clas_potential = 'C3' or pcp.branch_clas_potential = 'D3' "
				+ "or pcp.branch_clas_potential = 'E3' or pcp.sub_clas_potential = 'A3' "
				+ "or pcp.sub_clas_potential = 'B3' or pcp.sub_clas_potential = 'C3' "
				+ "or pcp.sub_clas_potential = 'D3' or pcp.sub_clas_potential = 'E3') "
				+ "and pcm.del_stat = '0' and pcm.claim_cust_mgr_id = ? ";
		List<String> listStr = new ArrayList<String>();
		listStr.add(userNo);
		if (AppUtils.StringUtil(custName) != null) {
			fromSql += " and pcp.customername like ? ";
			listStr.add("%" + custName + "%");
		}
		if (AppUtils.StringUtil(custNo) != null) {
			fromSql += " and pcp.customercode = ? ";
			listStr.add(custNo);
		}
		fromSql += " order by pcp.indate desc) tab "
				+ "left join gcms_param_info gpi on gpi.name = tab.clas_potential and gpi.key = 'LATENT_SUCC' "
				+ "left join gcms_param_info gpi1 on gpi1.val = tab.clas_potential and gpi1.key = 'LATENT_TYPE' ";
		Page<Record> pages = Db.use(DEFAULT).paginate(pageNum, pageSize, sql,
				fromSql, listStr.toArray());
		return pages;
		
		//切换到GBASE数据库进行数据查询  2018年8月27日16:11:12 ---liutao
		/*String data_date = findBaseInfoMaxDate();//查询宽表最新数据日期
		String sql = "select tab.id, incomday, customername, gpi.val*10000 as latent_succ_value, "
				+ "gpi1.name as latent_name, orgname, customercode, clas_five_name, clas_potential, "
				+ "case when (incomday >= (gpi.val*10000)) then '是' else '否'end as flag ";
		String fromSql = "from (select pcp.id,(nvl(pcbi.ck_nrj_zcy, 0)+nvl(pcbi.bn_fic_nrj, 0)+nvl(pcbi.bw_fic_nrj, 0)) as incomday, pcp.customername,"
				+ "soi.orgname, pcp.customercode, gpi2.name as clas_five_name, "
				+ "(case when pcp.sub_clas_potential is not null and pcp.sub_clas_potential <> '' then pcp.sub_clas_potential "
				+ "when pcp.branch_clas_potential is not null and pcp.branch_clas_potential <> '' then pcp.branch_clas_potential "
				+ "when pcp.clas_potential is not null then pcp.clas_potential "
				+ "end) as clas_potential from pccm_cust_claim pcm "
				+ "left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id "
				+ "left join pccm_cust_base_info pcbi on pcbi.id||pcbi.cust_no = pcp.id and pcbi.data_date = ? "
				+ "left join gcms_param_info gpi2 on gpi2.val = pcp.clas_five and gpi2.jkey = 'CLAS_FIVE' "
				+ "left join sys_org_info soi on soi.id = pcp.ORGNUM "
				+ "where 1=1 and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or "
				+ "pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3' "
				+ "or pcp.branch_clas_potential = 'A3' or pcp.branch_clas_potential = 'B3' "
				+ "or pcp.branch_clas_potential = 'C3' or pcp.branch_clas_potential = 'D3' "
				+ "or pcp.branch_clas_potential = 'E3' or pcp.sub_clas_potential = 'A3' "
				+ "or pcp.sub_clas_potential = 'B3' or pcp.sub_clas_potential = 'C3' "
				+ "or pcp.sub_clas_potential = 'D3' or pcp.sub_clas_potential = 'E3') "
				+ " "
				+ "and pcm.del_stat = '0' and pcm.claim_cust_mgr_id = ? ";
		List<String> listStr = new ArrayList<String>();
		listStr.add(data_date);
		listStr.add(userNo);
		if (AppUtils.StringUtil(custName) != null) {
			fromSql += " and pcp.customername like ? ";
			listStr.add("%" + custName + "%");
		}
		if (AppUtils.StringUtil(custNo) != null) {
			fromSql += " and pcp.customercode = ? ";
			listStr.add(custNo);
		}
		fromSql += " order by pcp.indate desc) tab "
				+ "left join gcms_param_info gpi on gpi.name = tab.clas_potential and gpi.jkey = 'LATENT_SUCC' "
				+ "left join gcms_param_info gpi1 on gpi1.val = tab.clas_potential and gpi1.jkey = 'LATENT_TYPE' ";
		Page<Record> pages = Db.use(GBASE).paginate(pageNum, pageSize, sql,
				fromSql, listStr.toArray());
		return pages;*/
	}
	
	/**
	 * 根据用户登录名查询潜在客户相关数(潜在客户达成数和潜在客户总数)
	 * 2018年7月12日11:21:22
	 * @author liutao
	 * @param user_no
	 * @return
	 */
	public int findMarketNumByUserNo(String user_no, String flag){
		/*String sql = "select count(1) as num from pccm_cust_pool pcp "
			+ "left join pccm_cust_claim pcc on pcc.cust_pool_id = pcp.id "
			+ "where pcc.claim_cust_mgr_id = ? "
			+ "and pcc.del_stat = '0' and pcp.dummy_cust_no is not null "
			+ "and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3')";
		if("succ".equals(flag)){
			sql += " and pcc.marketing_stat = '1' ";
		}
		Record r = Db.use(DEFAULT).findFirst(sql, user_no);
		if(r != null){
			return r.getBigDecimal("num").intValue();
		}else{
			return 0;
		}*/
		
		//切换到GBASE数据库进行数据查询  2018年8月30日16:49:03 --liutao
		String sql = "select count(1) as num from pccm_cust_pool pcp "
				+ "left join pccm_cust_claim pcc on pcc.cust_pool_id = pcp.id "
				+ "where pcc.claim_cust_mgr_id = ? and pcp.dummy_cust_no is not null "
				+ "and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3')";
		if("succ".equals(flag)){
			sql += " and pcc.marketing_stat = '1' ";
		}
		Record r = Db.use(GBASE).findFirst(sql, user_no);
		if(r != null){
			//return r.getBigDecimal("num").intValue();
			//切换到GBASE数据库的数据类型
			return r.getLong("num").intValue();
		}else{
			return 0;
		}
	}
	
	/**
	 * 根据用户名查询用户省行，支行，分行非排名
	 * 2018年7月12日11:21:16
	 * @author liutao
	 * @param user_no
	 * @return
	 */
	public int findUserMarketRank(String user_no, String orgLevel){
		/*String sql = "select num from ( select user_no, reach, rownum as num from ( "
			+ "select pul.user_no, to_number(pul.reach) as reach from pccm_user_market_info pul "
			+ "left join sys_user_info sui on sui.user_no = pul.user_no "
			+ "where 1=1 and sui.org_id in ( select soi.id  from sys_org_info soi "
			+ "start with soi.id = ( select soi.orgnum from sys_org_info soi where soi.by2 = ? "
			+ "start with soi.id = (select org_id from sys_user_info where user_no = ? )"
			+ "connect by prior soi.upid = soi.id )connect by soi.upid = prior soi.id) " 
			+ "order by reach desc)) where user_no = ? ";
		Record r = Db.use(DEFAULT).findFirst(sql, orgLevel, user_no, user_no);
		if(null != r){
			return r.getBigDecimal("num").intValue();
		}else{
			return 0;
		}*/
		
		//不使用递归方式进行数据查询  改用别的方式进行查询
		//首先查询出当前用户所在机构的所有上级机构，再根据orgLevel得到唯一的机构号
		/*String findSql = "";
		if("1".equals(orgLevel)){
			findSql = "select subStr(by5||','||soi.id, 1, 9) as by5 from sys_org_info soi where soi.id = ( "
					+ " select org_id from sys_user_info where user_no = ? )";
		}else if("2".equals(orgLevel)){
			findSql = "select subStr(by5||','||soi.id, 11, 9) as by5 from sys_org_info soi where soi.id = ( "
					+ " select org_id from sys_user_info where user_no = ? )";
		}else{
			findSql = "select subStr(by5||','||soi.id, 21, 9) as by5 from sys_org_info soi where soi.id = ( "
					+ " select org_id from sys_user_info where user_no = ? )";
		}
		Record r = Db.use(DEFAULT).findFirst(findSql, user_no);
		if(null != r && null != r.getStr("by5") && !"".equals(r.getStr("by5"))){
			String by5 = r.getStr("by5");
			String sql = "select num from ( select user_no, reach, rownum as num from ( "
					+ " select pul.user_no, to_number(pul.reach) as reach from pccm_user_market_info pul "
					+ " left join sys_user_info sui on sui.user_no = pul.user_no "
					+ " where 1=1 and sui.org_id in ( select soi1.id from sys_org_info soi1 where by5 like ? " 
					+ " or soi1.id = ? )"
					+ " order by reach desc)) where user_no = ? ";
			r = Db.use(DEFAULT).findFirst(sql, "%" + by5 + "%", by5, user_no);
			if(null != r){
				return r.getBigDecimal("num").intValue();
			}else{
				return 0;
			}
		}else{
			return 0;
		}*/
		
		//切换到GBASE数据库进行数据查询  2018年8月30日16:53:02 --liutao
		String findSql = "";
		if("1".equals(orgLevel)){
			findSql = "select subStr(by5||','||soi.id, 1, 9) as by5 from sys_org_info soi where soi.id = ( "
					+ " select org_id from sys_user_info where user_no = ? )";
		}else if("2".equals(orgLevel)){
			findSql = "select subStr(by5||','||soi.id, 11, 9) as by5 from sys_org_info soi where soi.id = ( "
					+ " select org_id from sys_user_info where user_no = ? )";
		}else{
			findSql = "select subStr(by5||','||soi.id, 21, 9) as by5 from sys_org_info soi where soi.id = ( "
					+ " select org_id from sys_user_info where user_no = ? )";
		}
		Record r = Db.use(DEFAULT).findFirst(findSql, user_no);
		if(null != r && null != r.getStr("by5") && !"".equals(r.getStr("by5"))){
			String by5 = r.getStr("by5");
			String sql = "select num from ( select user_no, reach, row_number() over (order by reach desc) as num from ( "
					+ " select pul.user_no, to_number(pul.reach) as reach from pccm_user_market_info pul "
					+ " left join sys_user_info sui on sui.user_no = pul.user_no "
					+ " where 1=1 and sui.org_id in ( select soi1.id from sys_org_info soi1 where by5 like ? " 
					+ " or soi1.id = ? )"
					+ " order by reach desc)t1)t2 where user_no = ? ";
			r = Db.use(GBASE).findFirst(sql, "%" + by5 + "%", by5, user_no);
			if(null != r){
				//return r.getBigDecimal("num").intValue();
				
				//切换到GBASE数据库数据类型
				return r.getLong("num").intValue();
			}else{
				return 0;
			}
		}else{
			return 0;
		}
	}
	
	/**
	 * 保存客户经理潜在客户总数、潜在达成数以及潜在客户达成率
	 * 2018年7月12日11:31:16
	 * @author liutao
	 */
	public void saveUserMarket(Record r){
		/*String delSql = "delete pccm_user_market_info where user_no = ? ";
		//删除指定的客户经理数据重新添加
		Db.use(DEFAULT).update(delSql, r.getStr("user_no"));
		String sql = "insert into pccm_user_market_info(user_no, SUCCNUM, SUMNUM, REACH, " 
				+ "P_RANK, B_RANK, S_RANK)values(?, ?, ?, ?, ?, ?, ?)";
		Db.use(DEFAULT).update(sql, r.getStr("user_no"), r.getStr("succnum"), 
				r.getStr("sumnum"), r.getStr("reach"), r.getStr("p_rank"), 
				r.getStr("b_rank"), r.getStr("s_rank"));*/
		
		//切换到GBASE数据库进行数据查询  2018年8月30日17:01:57  --liutao
		String delSql = "delete pccm_user_market_info where user_no = ? ";
		//删除指定的客户经理数据重新添加
		Db.use(GBASE).update(delSql, r.getStr("user_no"));
		String sql = "insert into pccm_user_market_info(user_no, SUCCNUM, SUMNUM, REACH, " 
				+ "P_RANK, B_RANK, S_RANK)values(?, ?, ?, ?, ?, ?, ?)";
		Db.use(GBASE).update(sql, r.getStr("user_no"), r.getStr("succnum"), 
				r.getStr("sumnum"), r.getStr("reach"), r.getStr("p_rank"), 
				r.getStr("b_rank"), r.getStr("s_rank"));
	}
	
	/**
	 * 查询机构下的商机转化率排名数据等(查询机构商机转化客户数，机构商机总数，以及转化率和排名等)
	 * 2018年7月11日19:33:53
	 * @author liutao
	 * @return
	 */
	/*public Page<Record> findOrgMarketRank(int pageNum, int pageSize, String org_id){
		String sql = "select id, orgname as name, by2, succnum, sumnum, round(reach, 4)*100 as reach, rownum ";
		String fromSql = "from ( select id, orgname, by2, decode(succnum, null, 0, succnum) as succnum,"
				+ "decode(sumnum, null, 0, sumnum) as sumnum, "
				+ "(decode(succnum, null, 0, succnum)/decode(sumnum, null, 1, 0, 1, sumnum)) as reach "
				+ "from (select soi1.id, soi1.orgname, soi1.by2,"
				+ "sum((select sum(pum.succnum) from pccm_user_market_info pum "
				+ "left join sys_user_info sui on pum.user_no = sui.user_no "
				+ "where sui.org_id in ( select soi.id  from sys_org_info soi "
				+ "start with soi.id = soi1.id connect by soi.upid = prior soi.id))) as succnum,"
				+ "sum((select sum(pum.sumnum) from pccm_user_market_info pum "
				+ "left join sys_user_info sui on pum.user_no = sui.user_no "
				+ "where sui.org_id in ( select soi.id  from sys_org_info soi "
				+ "start with soi.id = soi1.id connect by soi.upid = prior soi.id))) as sumnum "
				+ "from sys_org_info soi1 where soi1.upid = ? "
				+ "group by soi1.id, soi1.orgname, soi1.by2) order by reach desc)";
		Page<Record> pages = Db.use(DEFAULT).paginate(pageNum, pageSize, sql,
				fromSql, org_id);
		return pages;
	}*/
	
	/**
	 * 查询机构下的商机转化率排名数据等(查询机构商机转化客户数，机构商机总数，以及转化率和排名等)
	 * 2018年7月11日19:33:53
	 * @author liutao
	 * @return
	 */
	public List<Record> findOrgMarketRank(String org_id){
		/*String sql = "select id, orgname as name, by2, succnum, sumnum, round(reach, 4)*100 as reach, rownum ";
		String fromSql = "from ( select id, orgname, by2, decode(succnum, null, 0, succnum) as succnum,"
				+ "decode(sumnum, null, 0, sumnum) as sumnum, "
				+ "(decode(succnum, null, 0, succnum)/decode(sumnum, null, 1, 0, 1, sumnum)) as reach "
				+ "from (select soi1.id, soi1.orgname, soi1.by2,"
				+ "sum((select sum(pum.succnum) from pccm_user_market_info pum "
				+ "left join sys_user_info sui on pum.user_no = sui.user_no "
				+ "where sui.org_id in ( select soi.id  from sys_org_info soi "
				+ "start with soi.id = soi1.id connect by soi.upid = prior soi.id))) as succnum,"
				+ "sum((select sum(pum.sumnum) from pccm_user_market_info pum "
				+ "left join sys_user_info sui on pum.user_no = sui.user_no "
				+ "where sui.org_id in ( select soi.id  from sys_org_info soi "
				+ "start with soi.id = soi1.id connect by soi.upid = prior soi.id))) as sumnum "
				+ "from sys_org_info soi1 where soi1.upid = ? "
				+ "group by soi1.id, soi1.orgname, soi1.by2) order by reach desc)";
		List<Record> list = Db.use(DEFAULT).find(sql+fromSql, org_id);
		return list;*/
		
		//不使用递归查询，使用sql拼接的方式进行查询数据
		String sql = " select tab.orgnum as id, soi.orgname as name, soi.by2, " 
				+ "decode(succnum, null, 0, succnum) as succnum, decode(sumnum, null, 0, sumnum) as sumnum, "
				+ " round((decode(succnum, null, 0, succnum)/decode(sumnum, " 
				+ " null, 1, 0, 1, sumnum)), 4)*100 as reach, rownum from ( ";
		//查询机构下有哪些子机构
		String findChidrenOrgSql = "select id from sys_org_info where upid = ? ";
		List<Record> chidrenOrg = Db.use(DEFAULT).find(findChidrenOrgSql, org_id);
		boolean flag = true;
		for (Record record : chidrenOrg) {
			String chidrenOrgId = record.getStr("id");
			if(flag){
				sql += "select '"+chidrenOrgId+"' as orgnum, sum(pum.sumnum) as sumnum, " 
					+ "sum(pum.succnum) as succnum "
					+ " from pccm_user_market_info pum "
					+ " left join sys_user_info sui on pum.user_no = sui.id "
					+ " where sui.org_id in (select id from sys_org_info where by5 like '%"
					+chidrenOrgId+"%' or id = '"+ chidrenOrgId +"' )";
			}else{
				sql += "union all "
						+ "select '"+chidrenOrgId+"' as orgnum, sum(pum.sumnum) as sumnum, " 
						+ "sum(pum.succnum) as succnum "
						+ " from pccm_user_market_info pum "
						+ " left join sys_user_info sui on pum.user_no = sui.id "
						+ " where sui.org_id in (select id from sys_org_info where by5 like '%"
						+chidrenOrgId+"%' or id = '"+ chidrenOrgId +"' )";
			}
			flag = false;
		}
		sql += ")tab left join sys_org_info soi on soi.id = tab.orgnum "
				+ " order by reach desc";
		return Db.use(DEFAULT).find(sql);
		
		//切换到GBASE数据库进行数据查询  2018年8月30日14:42:14  --liutao
		/*String sql = " select tab.orgnum as id, soi.orgname as name, soi.by2, " 
				+ "decode(succnum, null, 0, succnum) as succnum, decode(sumnum, null, 0, sumnum) as sumnum, "
				+ " round((decode(succnum, null, 0, succnum)/decode(sumnum, " 
				+ " null, 1, 0, 1, sumnum)), 4)*100 as reach from ( ";
		//查询机构下有哪些子机构
		String findChidrenOrgSql = "select id from sys_org_info where upid = ? ";
		List<Record> chidrenOrg = Db.use(DEFAULT).find(findChidrenOrgSql, org_id);
		boolean flag = true;
		for (Record record : chidrenOrg) {
			String chidrenOrgId = record.getStr("id");
			if(flag){
				sql += "select '"+chidrenOrgId+"' as orgnum, sum(pum.sumnum) as sumnum, " 
					+ "sum(pum.succnum) as succnum "
					+ " from pccm_user_market_info pum "
					+ " left join sys_user_info sui on pum.user_no = sui.id "
					+ " where sui.org_id in (select id from sys_org_info " 
					+ " where by5 like '%" + chidrenOrgId + "%' or id = '" + chidrenOrgId + "')";
			}else{
				sql += "union all "
					+ "select '"+chidrenOrgId+"' as orgnum, sum(pum.sumnum) as sumnum, " 
					+ "sum(pum.succnum) as succnum "
					+ " from pccm_user_market_info pum "
					+ " left join sys_user_info sui on pum.user_no = sui.id "
					+ " where sui.org_id in (select id from sys_org_info " 
					+ " where by5 like '%" + chidrenOrgId + "%' or id = '" + chidrenOrgId + "')";
			}
			flag = false;
		}
		sql += ")tab left join sys_org_info soi on soi.id = tab.orgnum "
				+ " order by reach desc";
		return Db.use(GBASE).find(sql);*/
	}
	
	/**
	 * 查询四级机构下的商机转化率数据
	 * 2018年7月17日16:46:16
	 * @author liutao
	 * @param org_id
	 * @return
	 * 返回结果例如：
	 * 张三    3   4   75.00
	 * 李四    1   5   20.00
	 */
	public List<Record> findOrgUserMarketRank(String org_id){
		/*String sql = "select sui.name, pum.succnum, pum.sumnum, pum.reach from pccm_user_market_info pum "
				+ "left join sys_user_info sui on pum.user_no = sui.user_no "
				+ "where sui.org_id in ( select soi.id  from sys_org_info soi "
				+ "start with soi.id = ? connect by soi.upid = prior soi.id)";*/
		
		//不使用sql递归查询
		String sql = "select sui.name, pum.succnum, pum.sumnum, pum.reach from pccm_user_market_info pum "
				+ "left join sys_user_info sui on pum.user_no = sui.user_no "
				+ "where sui.org_id in (select id from sys_org_info where by5 like ? or id = ? )";
		List<Record> list = Db.use(DEFAULT).find(sql, "%" + org_id + "%", org_id);
		return list;
		
		//切换到GBASE数据库进行数据查询  2018年8月30日14:49:48 --liutao
		/*String sql = "select sui.name, pum.succnum, pum.sumnum, pum.reach from pccm_user_market_info pum "
				+ "left join sys_user_info sui on pum.user_no = sui.user_no "
				+ "where sui.org_id in (select id from sys_org_info where by5 like ? or id = ?)";
		List<Record> list = Db.use(DEFAULT).find(sql, "%" + org_id + "%", org_id);
		return list;*/
	}
	
	/**
	 * 查询某个机构下所有的商机客户总数和所有已经转化的商机客户数
	 * 2018年7月17日11:11:19
	 * @author liutao
	 * @param org_id 机构id
	 * @return
	 */
	public Record findOrgMarketSum(String org_id){
		/*String sql = "select succnum, sumnum, round(succnum/decode(sumnum, null, 1, 0, 1, sumnum), 4)*100 as reach "
				+ "from ( select sum(decode(succnum, null, 0, succnum)) as succnum,"
				+ "sum(decode(sumnum, null, 0, sumnum)) as sumnum, "
				+ "(decode(succnum, null, 0, succnum)/decode(sumnum, null, 1, 0, 1, sumnum)) as reach "
				+ "from (select sum((select sum(pum.succnum) from pccm_user_market_info pum "
				+ "left join sys_user_info sui on pum.user_no = sui.user_no "
				+ "where sui.org_id in ( select soi.id  from sys_org_info soi "
				+ "start with soi.id = soi1.id connect by soi.upid = prior soi.id))) as succnum,"
				+ "sum((select sum(pum.sumnum) from pccm_user_market_info pum "
				+ "left join sys_user_info sui on pum.user_no = sui.user_no "
				+ "where sui.org_id in ( select soi.id  from sys_org_info soi "
				+ "start with soi.id = soi1.id connect by soi.upid = prior soi.id))) as sumnum "
				+ "from sys_org_info soi1 where soi1.upid = ? ))";*///sql优化，不使用递归法
		
		String sql = "select succnum, sumnum, round(reach, 4)*100 as reach "
				+ "from ( select sum(decode(succnum, null, 0, succnum)) as succnum,"
		        + "sum(decode(sumnum, null, 0, sumnum)) as sumnum, "
		        + "(decode(succnum, null, 0, succnum)/decode(sumnum, null, 1, 0, 1, sumnum)) as reach "
		        + "from (select sum(pum.succnum) as succnum,sum(pum.sumnum) as sumnum "
		        + "from pccm_user_market_info pum "
		        + "left join sys_user_info sui on pum.user_no = sui.user_no "
		        + "where sui.org_id in ( select id from sys_org_info soi1 " 
		        + "where by5 like ? or soi1.id = ? ))group by succnum, sumnum)";
		return Db.use(DEFAULT).findFirst(sql, "%" + org_id + "%", org_id);
		
		//切换到GBASE数据库进行数据查询  2018年8月27日16:33:45 --liutao
		/*String sql = "select succnum, sumnum, round(reach, 4)*100 as reach "
				+ " from (select sum(succnum) as succnum, sum(sumnum) as sumnum, "
				+ " (succnum/decode(sumnum, 0, 1, sumnum)) as reach "
				+ " from (select decode(sum(pum.succnum), null, 0, sum(pum.succnum)) as succnum, "
				+ " decode(sum(pum.sumnum), null, 0, sum(pum.sumnum)) as sumnum "
				+ " from pccm_user_market_info pum "
				+ " left join sys_user_info sui on pum.user_no = sui.user_no "
				+ " where sui.org_id in ( "
				+ " select id from sys_org_info soi1 "
				+ " where by5 like ? or soi1.id = ? ) "
				+ " )tab1 group by succnum, sumnum )tab2 ";
		return Db.use(GBASE).findFirst(sql,"%"+org_id+"%", org_id);*/
	}
	
	/**
	 * 查询某个四级机构下所有的商机客户总数和所有已经转化的商机客户数
	 * 2018年7月17日16:59:28
	 * @author liutao
	 * @param org_id 机构id
	 * @return
	 */
	/*public Record findOrgUserMarketSum(String org_id){
		String sql = "select succnum, sumnum, round(succnum/decode(sumnum, null, 1, 0, 1, sumnum), 4)*100 as reach "
				+ "from ( select sum(decode(succnum, null, 0, succnum)) as succnum,"
				+ "sum(decode(sumnum, null, 0, sumnum)) as sumnum, "
				+ "(decode(succnum, null, 0, succnum)/decode(sumnum, null, 1, 0, 1, sumnum)) as reach "
				+ "from (select sum((select sum(pum.succnum) from pccm_user_market_info pum "
				+ "left join sys_user_info sui on pum.user_no = sui.user_no "
				+ "where sui.org_id in ( select soi.id  from sys_org_info soi "
				+ "start with soi.id = soi1.id connect by soi.upid = prior soi.id))) as succnum,"
				+ "sum((select sum(pum.sumnum) from pccm_user_market_info pum "
				+ "left join sys_user_info sui on pum.user_no = sui.user_no "
				+ "where sui.org_id in ( select soi.id  from sys_org_info soi "
				+ "start with soi.id = soi1.id connect by soi.upid = prior soi.id))) as sumnum "
				+ "from sys_org_info soi1 where soi1.id = ? ))";
		return Db.use(DEFAULT).findFirst(sql, org_id);
	}*/
	
	/**
	 * 查询机构下的商机转化率排名数据等(查询机构商机转化客户数，机构商机总数，以及转化率和排名等)
	 * 2018年7月11日19:36:44
	 * @author liutao
	 * @return
	 */
	/*public Page<Record> findOrgLatentRank(int pageNum, int pageSize, String org_id){
		String sql = "select id, orgname as name, by2, succnum, sumnum, round(reach, 4)*100 as reach, rownum ";
		String fromSql = "from ( select id, orgname, by2, decode(succnum, null, 0, succnum) as succnum,"
				+ "decode(sumnum, null, 0, sumnum) as sumnum, "
				+ "(decode(succnum, null, 0, succnum)/decode(sumnum, null, 1, 0, 1, sumnum)) as reach "
				+ "from (select soi1.id, soi1.orgname, soi1.by2,"
				+ "sum((select sum(pum.succnum) from pccm_user_latent_info pum "
				+ "left join sys_user_info sui on pum.user_no = sui.user_no "
				+ "where sui.org_id in ( select soi.id  from sys_org_info soi "
				+ "start with soi.id = soi1.id connect by soi.upid = prior soi.id))) as succnum,"
				+ "sum((select sum(pum.sumnum) from pccm_user_latent_info pum "
				+ "left join sys_user_info sui on pum.user_no = sui.user_no "
				+ "where sui.org_id in ( select soi.id  from sys_org_info soi "
				+ "start with soi.id = soi1.id connect by soi.upid = prior soi.id))) as sumnum "
				+ "from sys_org_info soi1 where soi1.upid = ? "
				+ "group by soi1.id, soi1.orgname, soi1.by2) order by reach desc)";
		Page<Record> pages = Db.use(DEFAULT).paginate(pageNum, pageSize, sql,
				fromSql, org_id);
		return pages;
	}*/
	/**
	 * 查询机构下的商机转化率排名数据等(查询机构商机转化客户数，机构商机总数，以及转化率和排名等)
	 * 2018年7月11日19:36:44
	 * @author liutao
	 * @return
	 */
	public List<Record> findOrgLatentRank(String org_id){
		/*String sql = "select id, orgname as name, by2, succnum, sumnum, round(reach, 4)*100 as reach, rownum ";
		String fromSql = "from ( select id, orgname, by2, decode(succnum, null, 0, succnum) as succnum,"
				+ "decode(sumnum, null, 0, sumnum) as sumnum, "
				+ "(decode(succnum, null, 0, succnum)/decode(sumnum, null, 1, 0, 1, sumnum)) as reach "
				+ "from (select soi1.id, soi1.orgname, soi1.by2,"
				+ "sum((select sum(pum.succnum) from pccm_user_latent_info pum "
				+ "left join sys_user_info sui on pum.user_no = sui.user_no "
				+ "where sui.org_id in ( select soi.id  from sys_org_info soi "
				+ "start with soi.id = soi1.id connect by soi.upid = prior soi.id))) as succnum,"
				+ "sum((select sum(pum.sumnum) from pccm_user_latent_info pum "
				+ "left join sys_user_info sui on pum.user_no = sui.user_no "
				+ "where sui.org_id in ( select soi.id  from sys_org_info soi "
				+ "start with soi.id = soi1.id connect by soi.upid = prior soi.id))) as sumnum "
				+ "from sys_org_info soi1 where soi1.upid = ? "
				+ "group by soi1.id, soi1.orgname, soi1.by2) order by reach desc)";*/
		String sql = " select tab.orgnum as id, soi.orgname as name, soi.by2, " 
				+ "decode(succnum, null, 0, succnum) as succnum, decode(sumnum, null, 0, sumnum) as sumnum, "
				+ " round((decode(succnum, null, 0, succnum)/decode(sumnum, " 
				+ " null, 1, 0, 1, sumnum)), 4)*100 as reach from ( ";
		//查询机构下有哪些子机构
		String findChidrenOrgSql = "select id from sys_org_info where upid = ? ";
		List<Record> chidrenOrg = Db.use(DEFAULT).find(findChidrenOrgSql, org_id);
		boolean flag = true;
		for (Record record : chidrenOrg) {
			String chidrenOrgId = record.getStr("id");
			if(flag){
				sql += "select '"+chidrenOrgId+"' as orgnum, sum(pum.sumnum) as sumnum, " 
					+ "sum(pum.succnum) as succnum "
					+ " from pccm_user_latent_info pum "
					+ " left join sys_user_info sui on pum.user_no = sui.id "
					+ " where sui.org_id in (select id from sys_org_info where by5 like '%"
					+chidrenOrgId+"%' or id = '"+ chidrenOrgId +"' )";
			}else{
				sql += "union all "
						+ "select '"+chidrenOrgId+"' as orgnum, sum(pum.sumnum) as sumnum, " 
						+ "sum(pum.succnum) as succnum "
						+ " from pccm_user_latent_info pum "
						+ " left join sys_user_info sui on pum.user_no = sui.id "
						+ " where sui.org_id in (select id from sys_org_info where by5 like '%"
						+chidrenOrgId+"%' or id = '"+ chidrenOrgId +"' )";
			}
			flag = false;
		}
		sql += ")tab left join sys_org_info soi on soi.id = tab.orgnum "
				+ " order by reach desc";
		return Db.use(DEFAULT).find(sql);
		
		//切换到GBASE数据库进行数据查询  2018年8月27日16:46:06 --liutao
		/*String sql = " select tab.orgnum as id, soi.orgname as name, soi.by2, " 
				+ "decode(succnum, null, 0, succnum) as succnum, decode(sumnum, null, 0, sumnum) as sumnum, "
				+ " round((decode(succnum, null, 0, succnum)/decode(sumnum, " 
				+ " null, 1, 0, 1, sumnum)), 4)*100 as reach from ( ";
		//查询机构下有哪些子机构
		String findChidrenOrgSql = "select id from sys_org_info where upid = ? ";
		List<Record> chidrenOrg = Db.use(DEFAULT).find(findChidrenOrgSql, org_id);
		boolean flag = true;
		for (Record record : chidrenOrg) {
			String chidrenOrgId = record.getStr("id");
			if(flag){
				sql += "select '"+chidrenOrgId+"' as orgnum, sum(pum.sumnum) as sumnum, " 
					+ "sum(pum.succnum) as succnum "
					+ " from pccm_user_latent_info pum "
					+ " left join sys_user_info sui on pum.user_no = sui.id "
					+ " where sui.org_id in (select id from sys_org_info " 
					+ " where by5 like '%"+ chidrenOrgId +"%' or id = '"+ chidrenOrgId +"')";
			}else{
				sql += "union all "
					+ "select '"+chidrenOrgId+"' as orgnum, sum(pum.sumnum) as sumnum, " 
					+ "sum(pum.succnum) as succnum "
					+ " from pccm_user_latent_info pum "
					+ " left join sys_user_info sui on pum.user_no = sui.id "
					+ " where sui.org_id in (select id from sys_org_info " 
					+ " where by5 like '%"+ chidrenOrgId +"%' or id = '"+ chidrenOrgId +"')";
			}
			flag = false;
		}
		sql += ")tab left join sys_org_info soi on soi.id = tab.orgnum "
				+ " order by reach desc";
		return Db.use(GBASE).find(sql);*/
	}
	
	/**
	 * 查询四级机构下的商机转化率数据
	 * 2018年7月17日16:46:16
	 * @author liutao
	 * @param org_id
	 * @return
	 * 返回结果例如：
	 * 张三    3   4   75.00
	 * 李四    1   5   20.00
	 */
	public List<Record> findOrgUserLatentRank(String org_id){
		String sql = "select sui.name, pum.succnum, pum.sumnum, pum.reach from pccm_user_latent_info pum "
				+ "left join sys_user_info sui on pum.user_no = sui.user_no "
				+ "where sui.org_id in ( select id from sys_org_info where by5 like ? or id = ? )";
		List<Record> list = Db.use(DEFAULT).find(sql, "%"+org_id+"%", org_id);
		return list;
		
		//切换到GBASE数据库进行数据查询  2018年8月27日16:36:58  --liutao
		/*String sql = "select sui.name, pum.succnum, pum.sumnum, pum.reach from pccm_user_latent_info pum "
				+ "left join sys_user_info sui on pum.user_no = sui.user_no "
				+ "where sui.org_id in ( select id from sys_org_info where by5 like ? or id = ? )";
		List<Record> list = Db.use(GBASE).find(sql, "%"+org_id+"%", org_id);
		return list;*/
	}
	
	/**
	 * 查询某个机构下所有的商机客户总数和所有已经转化的商机客户数
	 * 2018年7月17日11:11:19
	 * @author liutao
	 * @param org_id 机构id
	 * @return
	 */
	public Record findOrgLatentSum(String org_id){
//		String sql = "select succnum, sumnum, round(succnum/decode(sumnum, null, 1, 0, 1, sumnum), 4)*100 as reach "
//				+ "from ( select sum(decode(succnum, null, 0, succnum)) as succnum,"
//				+ "sum(decode(sumnum, null, 0, sumnum)) as sumnum, "
//				+ "(decode(succnum, null, 0, succnum)/decode(sumnum, null, 1, 0, 1, sumnum)) as reach "
//				+ "from (select sum((select sum(pum.succnum) from pccm_user_latent_info pum "
//				+ "left join sys_user_info sui on pum.user_no = sui.user_no "
//				+ "where sui.org_id in ( select soi.id  from sys_org_info soi "
//				+ "start with soi.id = soi1.id connect by soi.upid = prior soi.id))) as succnum,"
//				+ "sum((select sum(pum.sumnum) from pccm_user_latent_info pum "
//				+ "left join sys_user_info sui on pum.user_no = sui.user_no "
//				+ "where sui.org_id in ( select soi.id  from sys_org_info soi "
//				+ "start with soi.id = soi1.id connect by soi.upid = prior soi.id))) as sumnum "
//				+ "from sys_org_info soi1 where soi1.upid = ? ))";
		
		String sql = "select succnum, sumnum, round(reach, 4)*100 as reach "
				+ "from ( select sum(decode(succnum, null, 0, succnum)) as succnum,"
		        + "sum(decode(sumnum, null, 0, sumnum)) as sumnum, "
		        + "(decode(succnum, null, 0, succnum)/decode(sumnum, null, 1, 0, 1, sumnum)) as reach "
		        + "from (select sum(pum.succnum) as succnum,sum(pum.sumnum) as sumnum "
		        + "from pccm_user_Latent_info pum "
		        + "left join sys_user_info sui on pum.user_no = sui.user_no "
		        + "where sui.org_id in ( select id from sys_org_info soi1 " 
		        + "where by5 like ? or soi1.id = ? ))group by succnum, sumnum)";
		return Db.use(DEFAULT).findFirst(sql,"%"+org_id+"%", org_id);
		
		//切换到GBASE数据库进行数据查询  2018年8月27日16:33:45 --liutao
		/*String sql = "select succnum, sumnum, round(reach, 4)*100 as reach "
				+ " from (select sum(succnum) as succnum, sum(sumnum) as sumnum, "
				+ " (succnum/decode(sumnum, 0, 1, sumnum)) as reach "
				+ " from (select decode(sum(pum.succnum), null, 0, sum(pum.succnum)) as succnum, "
				+ " decode(sum(pum.sumnum), null, 0, sum(pum.sumnum)) as sumnum "
				+ " from pccm_user_Latent_info pum "
				+ " left join sys_user_info sui on pum.user_no = sui.user_no "
				+ " where sui.org_id in ( "
				+ " select id from sys_org_info soi1 "
				+ " where by5 like ? or soi1.id = ? ) "
				+ " )tab1 group by succnum, sumnum )tab2 ";
		return Db.use(GBASE).findFirst(sql,"%"+org_id+"%", org_id);*/
	}
	
	/**
	 * 查询某个四级机构下所有的商机客户总数和所有已经转化的商机客户数
	 * 2018年7月17日16:59:28
	 * @author liutao
	 * @param org_id 机构id
	 * @return
	 */
	public Record findOrgUserLatentSum(String org_id){
		String sql = "select succnum, sumnum, round(succnum/decode(sumnum, null, 1, 0, 1, sumnum), 4)*100 as reach "
				+ " from ( select sum(decode(succnum, null, 0, succnum)) as succnum, "
		        + " sum(decode(sumnum, null, 0, sumnum)) as sumnum, "
		        + " (decode(succnum, null, 0, succnum)/decode(sumnum, null, 1, 0, 1, sumnum)) as reach "
		        + " from (select sum(pum.sumnum) as sumnum, sum(pum.succnum) as succnum from pccm_user_latent_info pum "
		        + " left join sys_user_info sui on pum.user_no = sui.user_no "
		        + " left join sys_org_info soi on soi.id = sui.org_id "
		        + " where sui.org_id in ( "
		        + " select id from sys_org_info soi1 where by5 like ? or soi1.id = ?)))";
		return Db.use(DEFAULT).findFirst(sql, "%" + org_id + "%", org_id);
	}
	
	/**
	 * 查询机构潜在客户数据
	 * 2018年7月12日10:51:25
	 * @author liutao
	 * @param map 查询条件参数
	 * @return 返回机构潜在客户数据
	 */
	public Page<Record> findOrgLatentCustomer(Map<String, Object> map){
		// 获取查询参数
		int pageNum = (Integer)map.get("pageNum");
		int pageSize = (Integer)map.get("pageSize");
		
		String custName = (String)map.get("custName");
		String custNo = (String)map.get("custNo");
		String orgNo = (String)map.get("orgNo");
		//查询机构id属于几级机构，除了一级和二级的机构(by2 = '1' or by2 = '2')其他的都需要从支行的标准向上获取潜在分类
		String level = findOrgLevelByOrgId(orgNo);
		String potentialSql = "";
		String potentialCondition = "";
		if("1".equals(level)){
			potentialSql = "pcp.clas_potential as clas_potential ";
			potentialCondition = " and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' "
					+ " or pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' " 
					+ " or pcp.CLAS_POTENTIAL = 'E3') ";
		}else if("2".equals(level)){
			potentialSql = "(case when pcp.branch_clas_potential is not null and pcp.branch_clas_potential <> '' then pcp.branch_clas_potential "
					+ "when pcp.clas_potential is not null then pcp.clas_potential "
					+ "end) as clas_potential ";
			potentialCondition = " and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' "
					+ " or pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' " 
					+ " or pcp.CLAS_POTENTIAL = 'E3' "
					+ " or pcp.branch_clas_potential = 'A3' or pcp.branch_clas_potential = 'B3' "
					+ " or pcp.branch_clas_potential = 'C3' or pcp.branch_clas_potential = 'D3' "
					+ " or pcp.branch_clas_potential = 'E3') ";
		}else{
			potentialSql = "(case when pcp.sub_clas_potential is not null and pcp.sub_clas_potential <> '' then pcp.sub_clas_potential "
					+ "when pcp.branch_clas_potential is not null and pcp.branch_clas_potential <> '' then pcp.branch_clas_potential "
					+ "when pcp.clas_potential is not null then pcp.clas_potential "
					+ "end) as clas_potential ";
			potentialCondition = " and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' "
					+ " or pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3' "
					+ " or pcp.branch_clas_potential = 'A3' or pcp.branch_clas_potential = 'B3' "
					+ " or pcp.branch_clas_potential = 'C3' or pcp.branch_clas_potential = 'D3' "
					+ " or pcp.branch_clas_potential = 'E3' or pcp.sub_clas_potential = 'A3' "
					+ " or pcp.sub_clas_potential = 'B3' or pcp.sub_clas_potential = 'C3' "
					+ " or pcp.sub_clas_potential = 'D3' or pcp.sub_clas_potential = 'E3') ";
		}
		/*String sql = "select pcp.id,(pcpm.incomday+pcpm.finaday) as incomday, pcp.customername,"
				+ "gpi.val*10000 as latent_succ_value, soi.orgname, pcp.customercode, "
				+ "gpi1.name as latent_name, gpi2.name as clas_five_name, pcp.clas_potential, "
				+ "case when (incomday >= (gpi.val*10000)) then '是' else '否'end as flag ";
		String fromSql = "from pccm_cust_claim pcm "
				+ "left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id "
				+ "left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id "
				+ "left join gcms_param_info gpi on gpi.name = pcp.clas_potential and gpi.key = 'LATENT_SUCC' "
				+ "left join gcms_param_info gpi1 on gpi1.val = pcp.clas_potential and gpi1.key = 'LATENT_TYPE' "
				+ "left join gcms_param_info gpi2 on gpi2.val = pcp.clas_five and gpi2.key = 'CLAS_FIVE' "
				+ "left join sys_org_info soi on soi.bancsid = pcp.org_id "
				+ "where 1=1 and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or "
				+ "pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3') "
				+ "and pcm.del_stat = '0' and pcm.claim_cust_mgr_id in ("
				+ "select sui.id from sys_user_info sui left join sys_org_info soi1 on soi1.id = sui.org_id "
				+ "where soi1.id in ( select soi2.id  from sys_org_info soi2 "
				+ "start with soi2.id = ? connect by soi2.upid = prior soi2.id))";*/
		
		//查询客户池钱表的最新数据日期
		String date = findPoolMoneyInfoMaxDate();
		String sql = "select tab.id, incomday, customername, customercode, "
				+ " orgname, clas_potential, gpi1.name as latent_name, "
				+ " gpi.val*10000 as latent_succ_value, "
				+ " case when (incomday >= (gpi.val*10000)) then '是' else '否' end as flag ";
		String fromSql = "from (select pcp.id,(nvl(pcpm.ck_nrj_zcy, 0)+nvl(pcpm.bn_fic_nrj, 0)+nvl(pcpm.bw_fic_nrj, 0)) as incomday, "
				+ " soi.orgname, pcp.customercode, pcp.customername, " + potentialSql
				+ " from pccm_cust_claim pcm "
				+ " left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id "
				+ " left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id " 
				+ " left join sys_org_info soi on soi.id = pcp.ORGNUM where 1=1 "
				+ " and pcpm.deptlevel = '3' and pcpm.data_date = '"+ date +"' "
				+ potentialCondition
				+ " and pcm.del_stat = '0' and pcp.deptlevel = '3' "
				+ " and pcm.claim_cust_mgr_id in ( select id from sys_user_info sui "
				+ " where sui.org_id in (select id from sys_org_info where by5 like ? or id = ? ))";
		List<String> listStr = new ArrayList<String>();
		listStr.add("%" + orgNo + "%");
		listStr.add(orgNo);
		if (AppUtils.StringUtil(custName) != null) {
			fromSql += " and pcp.customername like ? ";
			listStr.add("%" + custName + "%");
		}
		if (AppUtils.StringUtil(custNo) != null) {
			fromSql += " and pcp.customercode = ? ";
			listStr.add(custNo);
		}
		fromSql += " order by pcp.indate desc ) tab "
				+ " left join gcms_param_info gpi1 on gpi1.val = tab.clas_potential and gpi1.key = 'LATENT_TYPE' "
				+ " left join gcms_param_info gpi on gpi.name = tab.clas_potential and gpi.key = 'LATENT_SUCC' ";
		Page<Record> pages = Db.use(DEFAULT).paginate(pageNum, pageSize, sql,
				fromSql, listStr.toArray());
		return pages;
		
		//切换到GBASE数据库进行数据查询  2018年8月27日17:00:27 --liutao
		/*String data_date = findBaseInfoMaxDate();//查询宽表最新数据日期
		String sql = "select tab.id, incomday, customername, customercode, "
				+ " clas_five_name, orgname, clas_potential, gpi1.name as latent_name, "
				+ " gpi.val*10000 as latent_succ_value, "
				+ " case when (incomday >= (gpi.val*10000)) then '是' else '否' end as flag ";
		String fromSql = "from (select pcp.id,(nvl(pcbi.ck_nrj_zcy, 0)+nvl(pcbi.bn_fic_nrj, 0)+nvl(pcbi.bw_fic_nrj, 0)) as incomday, "
				+ " soi.orgname, pcp.customercode, "
				+ " gpi2.name as clas_five_name, pcp.customername, " + potentialSql
				+ " from sys_user_info sui "
				+ " left join pccm_cust_claim pcm on sui.user_no = pcm.claim_cust_mgr_id "
				+ " left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id "
				+ " left join pccm_cust_base_info pcbi on pcbi.id||pcbi.cust_no = pcp.id and pcbi.data_date = ? "
				+ " left join gcms_param_info gpi2 on gpi2.val = pcp.clas_five and gpi2.jkey = 'CLAS_FIVE' "
				+ " left join sys_org_info soi on soi.id = pcp.ORGNUM where 1=1 "
				+ potentialCondition
				+ " "
				+ " and pcm.del_stat = '0' "
				+ " and sui.org_id in (select id from sys_org_info where by5 like ? or id = ? )";
		List<String> listStr = new ArrayList<String>();
		listStr.add(data_date);
		listStr.add("%" + orgNo + "%");
		listStr.add(orgNo);
		if (AppUtils.StringUtil(custName) != null) {
			fromSql += " and pcp.customername like ? ";
			listStr.add("%" + custName + "%");
		}
		if (AppUtils.StringUtil(custNo) != null) {
			fromSql += " and pcp.customercode = ? ";
			listStr.add(custNo);
		}
		fromSql += " order by pcp.indate desc ) tab "
				+ " left join gcms_param_info gpi1 on gpi1.val = tab.clas_potential and gpi1.jkey = 'LATENT_TYPE' "
				+ " left join gcms_param_info gpi on gpi.name = tab.clas_potential and gpi.jkey = 'LATENT_SUCC' ";
		Page<Record> pages = Db.use(GBASE).paginate(pageNum, pageSize, sql,
				fromSql, listStr.toArray());
		return pages;*/
	}
	
	/**
	 * 查询机构商机转化率数据
	 * 2018年7月13日10:57:01
	 * @author liutao
	 * @param map 查询条件参数
	 * @return 返回机构商机转化率数据
	 */
	public Page<Record> findOrgMarketCustomer(Map<String, Object> map){
		// 获取查询参数
		int pageNum = (Integer)map.get("pageNum");
		int pageSize = (Integer)map.get("pageSize");
		
		String custName = (String)map.get("custName");
		String orgNo = (String)map.get("orgNo");
		String sql = "select pcp.id, pcp.dummy_cust_no, pcp.customername, pcp.relate_cust_name, "
				+ "sui2.name, sui2.phone, pcc.marketing_stat, pcp.customercode ";
		/*String fromSql = "from pccm_cust_claim pcc "
				+ "left join pccm_cust_pool pcp on pcc.CUST_POOL_ID = pcp.id "
				+ "left join sys_user_info sui on sui.user_no = pcp.cust_manager "
				+ "where 1=1 and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') "
				+ " "
				+ "and pcp.dummy_cust_no is not null "
				+ "and pcc.del_stat = '0' and pcc.claim_cust_mgr_id in ( "
				+ "select sui.id from sys_user_info sui left join sys_org_info soi1 on soi1.id = sui.org_id "
				+ "where soi1.id in ( select soi2.id  from sys_org_info soi2 "
				+ "start with soi2.id = ? connect by soi2.upid = prior soi2.id))";*/
		//不使用递归查询方式查询
		
		String fromSql = "from sys_user_info sui "
				+ " left join pccm_cust_claim pcc on sui.user_no = pcc.claim_cust_mgr_id  "
				+ " left join pccm_cust_pool pcp on pcc.CUST_POOL_ID = pcp.id "
				+ " left join sys_user_info sui2 on sui2.user_no = pcp.cust_manager "
				+ " where 1=1 and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') "
				+ " and pcp.dummy_cust_no is not null "
				+ " and pcc.del_stat = '0' and pcp.deptlevel = '3' "
				+ " and sui.org_id in (select id from sys_org_info where by5 like ? or id = ? )";
		List<String> listStr = new ArrayList<String>();
		listStr.add("%" + orgNo + "%");
		listStr.add(orgNo);
		if (AppUtils.StringUtil(custName) != null) {
			fromSql += " and pcp.customername like ? ";
			listStr.add("%" + custName + "%");
		}
		fromSql += " order by pcp.indate desc";
		Page<Record> pages = Db.use(DEFAULT).paginate(pageNum, pageSize, sql,
				fromSql, listStr.toArray());
		return pages;
		
		//切换到GBASE数据库进行数据查询  2018年8月30日14:58:26 --liutao
		/*String fromSql = "from sys_user_info sui "
				+ " left join pccm_cust_claim pcc on sui.user_no = pcc.claim_cust_mgr_id  "
				+ " left join pccm_cust_pool pcp on pcc.CUST_POOL_ID = pcp.id "
				+ " where 1=1 and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') "
				+ " and pcp.dummy_cust_no is not null "
				+ " and pcc.del_stat = '0' "
				+ " and sui.org_id in (select id from sys_org_info where by5 like ? or id = ?)";
		List<String> listStr = new ArrayList<String>();
		listStr.add("%" + orgNo + "%");
		listStr.add(orgNo);
		if (AppUtils.StringUtil(custName) != null) {
			fromSql += " and pcp.customername like ? ";
			listStr.add("%" + custName + "%");
		}
		fromSql += " order by pcp.indate desc";
		Page<Record> pages = Db.use(DEFAULT).paginate(pageNum, pageSize, sql,
				fromSql, listStr.toArray());
		return pages;*/
	}
	
	/**
	 * 根据EHR号和机构等级查询所在机构号
	 * 2018年7月17日10:00:23
	 * @author liutao
	 * @return 返回机构号id
	 */
	public String findOrgIdByUser(String userNo, String OrgLevel, String type){
		
		String findSql = "";
		if("1".equals(OrgLevel)){
			findSql = "select subStr(by5||','||soi.id, 1, 9) as by5 from sys_org_info soi where soi.id = ( "
					+ " select org_id from sys_user_info where user_no = ? )";
		}else if("2".equals(OrgLevel)){
			findSql = "select subStr(by5||','||soi.id, 11, 9) as by5 from sys_org_info soi where soi.id = ( "
					+ " select org_id from sys_user_info where user_no = ? )";
		}else if("3".equals(OrgLevel)){
			findSql = "select subStr(by5||','||soi.id, 21, 9) as by5 from sys_org_info soi where soi.id = ( "
					+ " select org_id from sys_user_info where user_no = ? )";
		}else{
			findSql = "select subStr(by5||','||soi.id, 31, 9) as by5 from sys_org_info soi where soi.id = ( "
					+ " select org_id from sys_user_info where user_no = ? )";
		}
		Record r = Db.use(DEFAULT).findFirst(findSql, userNo);
		if(null != r && null != r.getStr("by5") && !"".equals(r.getStr("by5"))){
			String by5 = r.getStr("by5");
			/*String sql = "select soi.orgnum, soi.orgname from sys_org_info soi where soi.by2 = ? "
					+ "start with soi.id = (select org_id from sys_user_info where user_no = ? ) "
					+ "connect by prior soi.upid = soi.id";*/
			//不采用递归方式
			String sql = "select soi.orgnum, soi.orgname from sys_org_info soi where soi.id = ? ";
			r = Db.use(DEFAULT).findFirst(sql, by5);
			if("orgnum".equals(type)){
				if(null != r){
					return r.getStr("orgnum");
				}else{
					return "";
				}
			}else{
				if(null != r){
					return r.getStr("orgname");
				}else{
					return "";
				}
			}
		}else{
			return "";
		}
	}
	
	/**
	 * 根据机构号查询机构名称
	 * 2018年7月17日14:10:30
	 * @author liutao
	 * @param orgId 机构号id
	 * @return
	 */
	public String findOrgNameByOrgId(String orgId){
		String sql = "select orgname from sys_org_info where id = ?";
		Record r = Db.use(DEFAULT).findFirst(sql, orgId);
		if(null != r){
			return r.getStr("orgname");
		}else{
			return "";
		}
	}
	
	/**
	 * 查询机构所属等级
	 * 2018年7月18日14:25:55
	 * @author liutao
	 * @param orgId
	 * @return
	 */
	public String findOrgLevelByOrgId(String orgId){
		String sql = "select by2 as orglevel from sys_org_info where id = ?";
		Record r = Db.use(DEFAULT).findFirst(sql, orgId);
		if(null != r && !"".equals(r.getStr("orglevel"))){
			return r.getStr("orglevel");
		}else{
			return "";
		}
	}
	
	/**
	 * 下载个人的商机客户数据
	 * 2018年7月18日20:46:18
	 * @author liutao
	 * @param map
	 * @return
	 */
	public List<Record> downloadPersonMarket(Map<String, Object> map){
		// 获取查询参数
		String custName = (String)map.get("custName");
		String userNo = (String)map.get("userNo");
		String sql = "select pcp.id, pcp.dummy_cust_no, pcp.customername, pcp.relate_cust_name, "
				+ "sui.name, sui.phone, pcp.customercode, pcp.clas_five, pcp.clas_potential,"
				+ "decode(pcc.marketing_stat, 0, '转化中', '1', '成功', '2', '转化中', '失败') as marketing_stat ";
		String fromSql = "from pccm_cust_claim pcc "
				+ "left join pccm_cust_pool pcp on pcc.CUST_POOL_ID = pcp.id "
				+ "left join sys_user_info sui on sui.user_no = pcp.cust_manager "
				+ "where 1=1 and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') "
				+ "and pcp.dummy_cust_no is not null "
				+ "and pcc.del_stat = '0' and pcc.claim_cust_mgr_id = ? ";
		List<String> listStr = new ArrayList<String>();
		listStr.add(userNo);
		if (AppUtils.StringUtil(custName) != null) {
			fromSql += " and pcp.customername like ? ";
			listStr.add("%" + custName + "%");
		}
		fromSql += " and rownum < 60000 order by pcp.indate desc";
		List<Record> list = Db.use(DEFAULT).find(sql+fromSql, listStr.toArray());
		return list;
	}
	
	/**
	 * 查询机构商机转化率数据
	 * 2018年7月13日10:57:01
	 * @author liutao
	 * @param map 查询条件参数
	 * @return 返回机构商机转化率数据
	 */
	public List<Record> downloadOrgMarket(Map<String, Object> map){
		// 获取查询参数
		String custName = (String)map.get("custName");
		String orgNo = (String)map.get("orgNo");
		String sql = "select pcp.id, pcp.dummy_cust_no, pcp.customername, pcp.relate_cust_name, "
				+ "sui.name, sui.phone, pcp.customercode, pcp.clas_five, pcp.clas_potential, "
				+ "decode(pcc.marketing_stat, 0, '转化中', '1', '成功', '2', '转化中', '失败') as marketing_stat ";
		/*String fromSql = "from pccm_cust_claim pcc "
				+ "left join pccm_cust_pool pcp on pcc.CUST_POOL_ID = pcp.id "
				+ "left join sys_user_info sui on sui.user_no = pcp.cust_manager "
				+ "where 1=1 and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') "
				+ "and pcp.dummy_cust_no is not null "
				+ "and pcc.del_stat = '0' and pcc.claim_cust_mgr_id in ( "
				+ "select sui.id from sys_user_info sui left join sys_org_info soi1 on soi1.id = sui.org_id "
				+ "where soi1.id in ( select soi2.id  from sys_org_info soi2 "
				+ "start with soi2.id = ? connect by soi2.upid = prior soi2.id))";*/
		//修改为不使用递归方式进行查询数据  2018年8月14日14:47:24 -- liutao
		String fromSql = "from sys_user_info sui1 "
				+ " left join pccm_cust_claim pcc on sui1.id = pcc.claim_cust_mgr_id "
		        + " left join pccm_cust_pool pcp on pcc.CUST_POOL_ID = pcp.id "
		        + " left join sys_user_info sui on sui.user_no = pcp.cust_manager "
		        + " where 1=1 and (pcp.incflg = '1' or pcp.incflg = '2' or pcp.incflg = '3') "
		        + " and pcp.dummy_cust_no is not null "
		        + " and pcc.del_stat = '0' and sui1.org_id in ( "
		        + " select soi.id from sys_org_info soi where soi.by5 like ? or soi.id = ?)";
		List<String> listStr = new ArrayList<String>();
		listStr.add("%" + orgNo + "%");
		listStr.add(orgNo);
		if (AppUtils.StringUtil(custName) != null) {
			fromSql += " and pcp.customername like ? ";
			listStr.add("%" + custName + "%");
		}
		fromSql += " and rownum < 60000 order by pcp.indate desc";
		List<Record> list = Db.use(DEFAULT).find(sql+fromSql, listStr.toArray());
		return list;
	}
	
	/**
	 * 下载个人潜在客户数据
	 * 2018年7月19日09:57:48
	 * @author liutao
	 * @param map 查询条件参数
	 * @return 返回潜在客户数据
	 */
	public List<Record> downloadPersonLatent(Map<String, Object> map){
		// 获取查询参数
		String custName = (String)map.get("custName");
		String custNo = (String)map.get("custNo");
		String userNo = (String)map.get("userNo");
		String sql = "select tab.id, incomday, customername, gpi.val*10000 as latent_succ_value, "
				+ "gpi1.name as latent_name, orgname, customercode, clas_five_name, clas_potential, "
				+ "case when (incomday >= (gpi.val*10000)) then '是' else '否'end as flag ";
		String fromSql = "from (select pcp.id,(pcpm.incomday+pcpm.finaday) as incomday, pcp.customername,"
				+ "soi.orgname, pcp.customercode, gpi2.name as clas_five_name, "
				+ "(case when pcp.sub_clas_potential is not null and pcp.sub_clas_potential <> '' then pcp.sub_clas_potential "
				+ "when pcp.branch_clas_potential is not null and pcp.branch_clas_potential <> '' then pcp.branch_clas_potential "
				+ "when pcp.clas_potential is not null then pcp.clas_potential "
				+ "end) as clas_potential from pccm_cust_claim pcm "
				+ "left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id "
				+ "left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id "
				+ "left join gcms_param_info gpi2 on gpi2.val = pcp.clas_five and gpi2.key = 'CLAS_FIVE' "
				+ "left join sys_org_info soi on soi.id = pcp.ORGNUM "
				+ "where 1=1 and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' or "
				+ "pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3' "
				+ "or pcp.branch_clas_potential = 'A3' or pcp.branch_clas_potential = 'B3' "
				+ "or pcp.branch_clas_potential = 'C3' or pcp.branch_clas_potential = 'D3' "
				+ "or pcp.branch_clas_potential = 'E3' or pcp.sub_clas_potential = 'A3' "
				+ "or pcp.sub_clas_potential = 'B3' or pcp.sub_clas_potential = 'C3' "
				+ "or pcp.sub_clas_potential = 'D3' or pcp.sub_clas_potential = 'E3') "
				+ " "
				+ "and pcm.del_stat = '0' and pcm.claim_cust_mgr_id = ? ";
		List<String> listStr = new ArrayList<String>();
		listStr.add(userNo);
		if (AppUtils.StringUtil(custName) != null) {
			fromSql += " and pcp.customername like ? ";
			listStr.add("%" + custName + "%");
		}
		if (AppUtils.StringUtil(custNo) != null) {
			fromSql += " and pcp.customercode = ? ";
			listStr.add(custNo);
		}
		fromSql += " and rownum < 60000 order by pcp.indate desc) tab "
				+ "left join gcms_param_info gpi on gpi.name = tab.clas_potential and gpi.key = 'LATENT_SUCC' "
				+ "left join gcms_param_info gpi1 on gpi1.val = tab.clas_potential and gpi1.key = 'LATENT_TYPE' ";
		List<Record> list = Db.use(DEFAULT).find(sql+fromSql, listStr.toArray());
		return list;
	}
	
	/**
	 * 下载机构潜在客户数据
	 * 2018年7月19日10:00:51
	 * @author liutao
	 * @param map 查询条件参数
	 * @return 返回机构潜在客户数据
	 */
	public List<Record> downloadOrgLatent(Map<String, Object> map){
		// 获取查询参数
		String custName = (String)map.get("custName");
		String custNo = (String)map.get("custNo");
		String orgNo = (String)map.get("orgNo");
		//查询机构id属于几级机构，除了一级和二级的机构(by2 = '1' or by2 = '2')其他的都需要从支行的标准向上获取潜在分类
		String level = findOrgLevelByOrgId(orgNo);
		String potentialSql = "";
		if("1".equals(level)){
			potentialSql = "pcp.clas_potential as clas_potential ";
		}else if("2".equals(level)){
			potentialSql = "(case when pcp.branch_clas_potential is not null and pcp.branch_clas_potential <> '' then pcp.branch_clas_potential "
					+ "when pcp.clas_potential is not null then pcp.clas_potential "
					+ "end) as clas_potential ";
		}else{
			potentialSql = "(case when pcp.sub_clas_potential is not null and pcp.sub_clas_potential <> '' then pcp.sub_clas_potential "
					+ "when pcp.branch_clas_potential is not null and pcp.branch_clas_potential <> '' then pcp.branch_clas_potential "
					+ "when pcp.clas_potential is not null then pcp.clas_potential "
					+ "end) as clas_potential ";
		}
		String sql = "select tab.id, incomday, customername, customercode, "
				+ "clas_five_name, orgname, clas_potential, gpi1.name as latent_name, "
				+ "gpi.val*10000 as latent_succ_value, "
				+ "case when (incomday >= (gpi.val*10000)) then '是' else '否'end as flag ";
		/*String fromSql = "from (select pcp.id,(pcpm.incomday+pcpm.finaday) as incomday, "
				+ "soi.orgname, pcp.customercode, "
				+ "gpi2.name as clas_five_name, pcp.customername, " + potentialSql
				+ "from pccm_cust_claim pcm "
				+ "left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id "
				+ "left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id "
				+ "left join gcms_param_info gpi2 on gpi2.val = pcp.clas_five and gpi2.key = 'CLAS_FIVE' "
				+ "left join sys_org_info soi on soi.id = pcp.ORGNUM where 1=1 "
				+ "and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' "
				+ "or pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3' "
				+ "or pcp.branch_clas_potential = 'A3' or pcp.branch_clas_potential = 'B3' "
				+ "or pcp.branch_clas_potential = 'C3' or pcp.branch_clas_potential = 'D3' "
				+ "or pcp.branch_clas_potential = 'E3' or pcp.branch_clas_potential = 'A3' "
				+ "or pcp.branch_clas_potential = 'B3' or pcp.branch_clas_potential = 'C3' "
				+ "or pcp.branch_clas_potential = 'D3' or pcp.branch_clas_potential = 'E3') "
				+ " "
				+ "and pcm.del_stat = '0' and pcm.claim_cust_mgr_id in ( "
				+ "select sui.id from sys_user_info sui left join sys_org_info soi1 on soi1.id = sui.org_id "
				+ "where soi1.id in ( select soi2.id  from sys_org_info soi2 "
				+ "start with soi2.id = ? connect by soi2.upid = prior soi2.id))";*/
		//不使用递归方式进行数据查询  2018年8月14日14:57:24  -- liutao
		String fromSql = "from (select pcp.id,(pcpm.incomday+pcpm.finaday) as incomday, "
				+ "soi.orgname, pcp.customercode, "
				+ "gpi2.name as clas_five_name, pcp.customername, " + potentialSql
				+ "from sys_user_info sui1 "
				+ "left join pccm_cust_claim pcm on sui1.id = pcm.claim_cust_mgr_id "
				+ "left join pccm_cust_pool pcp on pcp.id = pcm.cust_pool_id "
				+ "left join pccm_cust_pool_money pcpm on pcpm.cust_pool_id = pcp.id "
				+ "left join gcms_param_info gpi2 on gpi2.val = pcp.clas_five and gpi2.key = 'CLAS_FIVE' "
				+ "left join sys_org_info soi on soi.id = pcp.ORGNUM where 1=1 "
				+ "and (pcp.CLAS_POTENTIAL = 'A3' or pcp.CLAS_POTENTIAL = 'B3' "
				+ "or pcp.CLAS_POTENTIAL = 'C3' or pcp.CLAS_POTENTIAL = 'D3' or pcp.CLAS_POTENTIAL = 'E3' "
				+ "or pcp.branch_clas_potential = 'A3' or pcp.branch_clas_potential = 'B3' "
				+ "or pcp.branch_clas_potential = 'C3' or pcp.branch_clas_potential = 'D3' "
				+ "or pcp.branch_clas_potential = 'E3' or pcp.sub_clas_potential = 'A3' "
				+ "or pcp.sub_clas_potential = 'B3' or pcp.sub_clas_potential = 'C3' "
				+ "or pcp.sub_clas_potential = 'D3' or pcp.sub_clas_potential = 'E3') "
				+ " and pcm.del_stat = '0' "
				+ "and sui1.org_id in ( "
				+ "select soi.id from sys_org_info soi where soi.by5 like ? or soi.id = ?)";
		List<String> listStr = new ArrayList<String>();
		listStr.add("%" + orgNo + "%");
		listStr.add(orgNo);
		if (AppUtils.StringUtil(custName) != null) {
			fromSql += " and pcp.customername like ? ";
			listStr.add("%" + custName + "%");
		}
		if (AppUtils.StringUtil(custNo) != null) {
			fromSql += " and pcp.customercode = ? ";
			listStr.add(custNo);
		}
		fromSql += " and rownum < 60000 order by pcp.indate desc ) tab "
				+ "left join gcms_param_info gpi1 on gpi1.val = tab.clas_potential and gpi1.key = 'LATENT_TYPE' "
				+ "left join gcms_param_info gpi on gpi.name = tab.clas_potential and gpi.key = 'LATENT_SUCC' ";
		List<Record> list = Db.use(DEFAULT).find(sql+fromSql, listStr.toArray());
		return list;
	}
	
	/**
	 * 根据机构号id查询上级机构号
	 * 2018年7月20日14:51:16
	 * @author liutao
	 * @param orgnum 机构号id
	 * @return
	 */
	public String findParentOrgNum(String orgnum){
		String sql = "select upid from sys_org_info where id = ?";
		Record r = Db.use(DEFAULT).findFirst(sql, orgnum);
		if(null != r && !"".equals(r.getStr("upid"))){
			return r.getStr("upid");
		}else{
			return "";
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
}
