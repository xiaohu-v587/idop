package com.goodcol.util.zxgldbutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;

public class PccmCustCntDBUtil {
	
	PccmMyCustDBUtil mycustdbUtil = new PccmMyCustDBUtil();
	//gbase宽表最新数据日期
	//private static String gbaseCustBaseDate = AppUtils.findGNewDate();
	/**
	 * 客户统计列表查询-按组织
	 */
	@SuppressWarnings("null")
	public List<Record> custByOrgList(String upOrg,String roleLevel,String dataDate) {
		List<Record> reList = null;
		if(AppUtils.StringUtil(upOrg)!=null&&AppUtils.StringUtil(roleLevel)!=null){
//			int downLevel = Integer.parseInt(roleLevel)+1;
//			int upLevel = Integer.parseInt(roleLevel)-1;
	
//			String selectSql = "select id from sys_org_info where by2=" + downLevel +" start with id ='"+upOrg+"' connect by prior id= upid order by id asc";
//			
//			List<Record> list = Db.use("default").find(selectSql);
//			if (null != list & list.size() >0) {
//				for (Record record : list) {
//					sql += " union all ";
//					sql += "select '" + record.get("ID") +"' as upOrgId, count(1) cnt,sum(level_weight*level_value) level_weight, "
//							+"  sum(decode( p.is_new_cust,'是',1,0)) newCnt from pccm_cust_pool p where p.incflg = '4' "
//							+"  and p.orgnum in (select orgnum from sys_org_info start with id = '" 
//							+ record.get("ID") + "' connect by prior id = upid)";
//				}
//			}
//			sql += " ) b "
//					+" inner join sys_org_info o "
//					+" on o.id = b.upOrgId "
//					+" order by upOrgId ";
//			reList = Db.use("default").find(sql);
			
//			
//			String sql = "select b.upOrgId, o.orgname, o.by2, b.cnt,nvl(b.level_weight,0) level_weight, '1' as flag,'4' as incflg,nvl(b.newCnt,0) newCnt "
//					+"  from (select '"+upOrg+"' as upOrgId, count(1) cnt,sum(level_weight*level_value) level_weight,sum(decode( p.is_new_cust,'1',1,0)) newCnt "
//					+" from pccm_cust_pool p  where p.incflg = '4' and p.cust_type ='02' and p.deptlevel = '"+ upLevel +"'"
//					+" and p.orgnum is not null and p.pa_not_zero <> '0'" 
//					+" and  p.orgnum in (select orgnum from sys_org_info start with id = '" 
//					+ upOrg + "' connect by prior id = upid)";
//			
//			String selectSql = "select id from sys_org_info where by2=" + downLevel +" start with id ='"+upOrg+"' connect by prior id= upid order by id asc";
//			
//			List<Record> list = Db.use("default").find(selectSql);
//			if (null != list & list.size() >0) {
//				for (Record record : list) {
//					sql += " union all ";
//					sql += "select '" + record.get("ID") +"' as upOrgId, count(1) cnt,sum(level_weight*level_value) level_weight, "
//							+"  sum(decode( p.is_new_cust,'1',1,0)) newCnt from pccm_cust_pool p where p.incflg = '4' and p.cust_type ='02' and p.deptlevel = '"+ roleLevel +"'"
//							+"  and p.orgnum is not null and p.pa_not_zero <> '0'" 
//							+"  and p.orgnum in (select orgnum from sys_org_info start with id = '" 
//							+ record.get("ID") + "' connect by prior id = upid)";
//				}
//			}
//			sql += " ) b "
//					+" inner join sys_org_info o "
//					+" on o.id = b.upOrgId "
//					+" order by o.by2,upOrgId ";
//			reList = Db.use("default").find(sql);
			
			
//			String sql = "select b.upOrgId, o.orgname, o.by2, b.cnt,nvl(b.level_weight,0) level_weight, '1' as flag,'4' as incflg,nvl(b.newCnt,0) newCnt "
//					+"  from (select '"+upOrg+"' as upOrgId, count(1) cnt,sum(level_weight*level_value) level_weight,sum(decode( p.is_new_cust,'1',1,0)) newCnt "
//					+" from pccm_cust_pool p  where p.incflg = '4' and p.cust_type ='02' and p.deptlevel = '"+ upLevel +"'"
//					+" and p.orgnum is not null and p.pa_not_zero = '1' and p.orgnum='"+upOrg+"'";
//			
//			String selectSql = "select id from sys_org_info where by2=" + downLevel +" start with id ='"+upOrg+"' connect by prior id= upid order by id asc";
//			
//			List<Record> list = Db.use("default").find(selectSql);
//			if (null != list & list.size() >0) {
//				for (Record record : list) {
//					sql += " union all ";
//					sql += "select '" + record.get("ID") +"' as upOrgId, count(1) cnt,sum(level_weight*level_value) level_weight, "
//							+"  sum(decode( p.is_new_cust,'1',1,0)) newCnt from pccm_cust_pool p where p.incflg = '4' and p.cust_type ='02' and p.deptlevel = '"+ roleLevel +"'"
//							+"  and p.orgnum is not null and p.pa_not_zero = '1' and p.orgnum ='" + record.get("ID") +"'";
//				}
//			}
//			sql += " ) b "
//					+" inner join sys_org_info o "
//					+" on o.id = b.upOrgId "
//					+" order by o.by2,upOrgId ";
//			reList = Db.use("default").find(sql);
			
//			List<Record> detList = new ArrayList<Record>();
//			if(null!=reList && reList.size()>0){
//				for (Record re: reList) {
//					if(re.getBigDecimal("level_weight").doubleValue()==0
//							&&re.getBigDecimal("newCnt").doubleValue()==0){
//						detList.add(re);
//						//reList.remove(re);
//					}
//				}
//				reList.removeAll(detList);
//			}
//			String sql = " select b.* from (select upOrgId, orgname, by2, cnt, round(level_weight,2) level_weight, flag, incflg, newcnt "
//					+"  from pccm_cust_org_count "
//					+" where upOrgId='"+upOrg+"'"
//					+" or upOrgId in (select id from sys_org_info where upid = '"+upOrg+"')"
//					+" ) b order by b.by2,b.upOrgId ";
//			reList = Db.use("gbase").find(sql);
			
			if (StringUtils.isBlank(dataDate)) {
				// 获取最新数据日期
				dataDate = AppUtils.findCustCountMaxDate();
			}
			
			String sql = " select id upOrgId, orgname, deptlevel by2, month_add_cnt level_weight, '1' flag, '4' incflg, month_new_cust_cnt newcnt "
					+" from pccm_cust_org_new_count "
					+" where (id='"+upOrg+"' "
					+" or id in (select id from sys_org_info where upid = '"+upOrg+"')) "
					+" and data_month='" + dataDate + "' "
					+" order by deptlevel, id ";
			
			reList = Db.use("default").find(sql);	
		}
		return reList;
	}
	
	
	public List<Record> downloadCustByOrgList(String upOrg,String roleLevel) {
		List<Record> reList = null;
		if(AppUtils.StringUtil(upOrg)!=null&&AppUtils.StringUtil(roleLevel)!=null){
			 int downLevel = Integer.parseInt(roleLevel)+1;
			 int upLevel = Integer.parseInt(roleLevel)-1;
//			String sql = "select b.upOrgId, o.orgname, o.by2, b.cnt,nvl(b.level_weight,0) level_weight, '1' as flag,'4' as incflg,nvl(b.newCnt,0) newCnt "
//					+" from (select orgnum as upOrgId, count(1) cnt,sum(level_weight*level_value) level_weight,sum(decode( p.is_new_cust,'1',1,0)) newCnt "
//					+" from pccm_cust_pool p  where p.incflg = '4' and p.cust_type ='02' "
//					+" and p.orgnum is not null and p.pa_not_zero = '1' and p.orgnum in (select id from sys_org_info where by5 like '%"+upOrg+"%')" 
//					+" group by orgnum,deptlevel)b "
//					+" right join sys_org_info o "
//					+" on o.id = b.upOrgId "
//					+" order by o.by2,upOrgId ";
			
//			String selectSql = "select id from sys_org_info where by2=" + downLevel +" start with id ='"+upOrg+"' connect by prior id= upid order by id asc";
//			
//			List<Record> list = Db.use("default").find(selectSql);
//			if (null != list & list.size() >0) {
//				for (Record record : list) {
//					sql += " union all ";
//					sql += "select '" + record.get("ID") +"' as upOrgId, count(1) cnt,sum(level_weight*level_value) level_weight, "
//							+"  sum(decode( p.is_new_cust,'1',1,0)) newCnt from pccm_cust_pool p where p.incflg = '4' and p.cust_type ='02' and p.deptlevel = '"+ roleLevel +"'"
//							+"  and p.orgnum is not null and p.pa_not_zero = '1' and p.orgnum ='" + record.get("ID") +"'";
//				}
//			}
//			sql += " ) b "
//					+" inner join sys_org_info o "
//					+" on o.id = b.upOrgId "
//					+" order by o.by2,upOrgId ";
//			reList = Db.use("default").find(sql);
			
//			List<Record> detList = new ArrayList<Record>();
//			if(null!=reList && reList.size()>0){
//				for (Record re: reList) {
//					if(re.getBigDecimal("level_weight").doubleValue()==0
//							&&re.getBigDecimal("newCnt").doubleValue()==0){
//						detList.add(re);
//						//reList.remove(re);
//					}
//				}
//				reList.removeAll(detList);
//			}
			
			 String sql =" select upOrgId, orgname, by2, cnt, round(level_weight,2) level_weight, flag, incflg, newcnt "
						+"  from pccm_cust_org_count "
						+"  where upOrgId in (select id from sys_org_info where by5 like '%"+upOrg+"%' or id ='"+upOrg+"') "
						+"	order by by2,upOrgId ";
			reList = Db.use("default").find(sql);
			
		}
		return reList;
	}
	
	/**
	 * 客户统计列表查询-按组织-五层分类饼图
	 */
	public List<Record> custByFiveList(String upOrg,String roleLevel,String dataDate){
		List<Record> reList = null;
		if(AppUtils.StringUtil(upOrg)!=null&&AppUtils.StringUtil(roleLevel)!=null){
//			int upLevel = Integer.parseInt(roleLevel)-1;
//			String sql="select "
//						+"       p.name clas_five_cn, "
//						+"       p.val clas_five, "
//						+"       nvl(cnt, 0) cnt "
//						+"  from (select clas_five, count(1) cnt "
//						+"          from pccm_cust_pool p "
//						+"        where p.incflg = '4' and p.cust_type='02' "
//						+"        and p.deptLevel = '"+upLevel+"' "
//						+"        	and p.orgnum in (select id from sys_org_info where by5 like '%"+upOrg+"%' or id ='"+upOrg+"') "
//						+"         group by clas_five) b "
//						+"  left join gcms_param_info p "
//						+"    on b.clas_five = p.val "
//						+"   and p.jkey = 'clas_five' "
//						+" order by p.val ";
//			reList = Db.use("gbase").find(sql);
			if (StringUtils.isBlank(dataDate)) {
				// 获取最新数据日期
				dataDate = AppUtils.findCustCountMaxDate();
			}
			String sql= "   select nvl(sum(month_five_cnt),0) month_five_cnt, "
			+"          nvl(sum(month_four_cnt),0) month_four_cnt, "
			+"          nvl(sum(month_three_cnt),0) month_three_cnt, "
			+"          nvl(sum(month_two_cnt),0) month_two_cnt, "
			+"          nvl(sum(month_one_cnt),0) month_one_cnt "
			+"     from pccm_cust_org_new_count "
			+"    where deptlevel = '"+roleLevel+"'"
			+"    and id = '"+upOrg+"'"
			+" 	  and data_month='" + dataDate + "' ";
			reList = Db.use("default").find(sql);
		}
		return reList;
	}
	/**
	 * 客户统计列表查询-按类别
	 */
	public List<Record> custByTypeList(String upOrg,String roleLevel){
		List<Record> reList = null;
		if(AppUtils.StringUtil(upOrg)!=null&&AppUtils.StringUtil(roleLevel)!=null){
			int upLevel = Integer.parseInt(roleLevel)-1;
			String sql="select '2' flag, "
					+"       p.name incflg_cn, "
					+"       p.val incflg, "
					+"       nvl(cnt, 0) cnt "
					+"  from (select incflg, count(1) cnt "
					+"          from pccm_cust_pool c "
					+"        where c.incflg <> '4' "
					+"        	and c.area_code is not null "
					//+"        and c.deptLevel = '"+upLevel+"' "
					//+"        	and c.orgnum in (select id from sys_org_info where by5 like '%"+upOrg+"%' or id ='"+upOrg+"') "
					+"        	and c.area_code in (select distinct a.area_id from pccm_cust_org_are a where a.org_id in(select id from sys_org_info where by5 like '%"+upOrg+"%' or id ='"+upOrg+"')) "
					+"         group by incflg) b "
					+"  right join gcms_param_info p "
					+"  on b.incflg = p.val "
					+"  where p.val <> '4' and p.key = 'incflg' "
					+"  order by p.val ";
			reList = Db.use("default").find(sql);
		}
		return reList;
	}
	
	/**
	 * 查询数据最新入池时间
	 */
	public String findNewDate(){
		String sql =" select max(maxDate) maxDate "
				+"  from (select case "
				+"                 when third_indate is null then "
				+"                  indate "
				+"                 else "
				+"                  third_indate "
				+"               end as maxDate "
				+"          from pccm_cust_pool p )";
		String result =Db.use("default").queryStr(sql) ;
		return result;
	}
	
	/**
	 * 客户详情页面
	 */
	public Page<Record> custCntPage(Map<String, Object> map){
		// 获取查询参数
		int pageNum = (Integer)map.get("pageNum");
		int pageSize = (Integer)map.get("pageSize");
//		String cust_no = (String)map.get("cust_no");
//		String name = (String)map.get("name");	
//		String orgNum = (String)map.get("orgNum");	
//		String clas_five = (String)map.get("clas_five");
		Map<String, Object> maps = myfindSql(map);
		@SuppressWarnings("unchecked")
		Page<Record> records = Db.use("default").paginate(pageNum, pageSize, (String) maps.get("selectSql"), (String) maps.get("extrasql"), ((List<String>) maps.get("sqlStr")).toArray());
		
//		String maxDateSql = "select max(data_date) maxDate from pccm_cust_base_info ";
//		Record record = Db.use("gbase").findFirst(maxDateSql);
//		String dataDate = record.getStr("maxDate");
//		
//		String sql = "select cb.id orgnum, cb.ck_curr_zcy as incompoint,cb.name as customername,si.orgname as resp_center_name," 
//				+"  cb.cust_no as customercode, cb.ck_nrj_zcy as incomday,cb.ck_loan_curr as ilpoint," 
//				+"	cb.ck_loan_nrj as ilday,p1.name clas_five_cn, "
//				+"	(nvl(cb.int_inc,0)+nvl(cb.middle_income,0)+nvl(cb.splitting,0)) busi_inc, "
//				//+"	(nvl(cb.int_inc,0)) busi_inc, "
//				+"	(nvl(cb.bn_fic_profit,0)+nvl(cb.bw_fic_nrj,0)) finaday, "
//				+"  to_char(decode(cb.is_dfx, 1, 20, 0) + decode(cb.is_jscard, 1, 20, 0) +  "
//				+"  decode(cb.is_dxt, 1, 20, 0) + decode(cb.is_hdx, 1, 20, 0) + "
//				+"  decode(cb.is_bocnet, 1, 20, 0)) || '%' as product_prop ";
//		String fromSql = "from pccm_cust_base_info cb "
//				+ "left join sys_org_info si on cb.id = si.id "
//		+" left join gcms_param_info p1 on cb.five_level = p1.val and p1.jkey = 'clas_five' "
//		+" where cust_type ='02' and five_level<>'6' and (nvl(bw_fic_nrj,0)+nvl(bn_fic_nrj,0)+nvl(ck_nrj_zcy,0)) <> 0 " 
//		+" and cb.id ='" + orgNum+"' and cb.data_date = '" + dataDate + "'" ;
//		if (StringUtils.isNotBlank(name)) {
//			fromSql += " and cb.name like '%" + name + "%'";
//		}
//		if (StringUtils.isNotBlank(cust_no)) {
//			fromSql += " and cb.cust_no = '" + cust_no + "'";
//		}
//		if (StringUtils.isNotBlank(clas_five)) {
//			fromSql += " and cb.five_level = '" + clas_five + "'";
//		}
//		fromSql += " order by cb.name";
//		Page<Record> list = Db.use("gbase").paginate(pageNum, pageSize, sql, fromSql);
		String orgnum=null;
		String custNo=null;
		//String claimProp=null;
		//Record gr = new Record();
		String flag	= (String)map.get("flag");
		List<Record> reList = records.getList();
		if(null!=reList&&reList.size()>0&&"1".equals(flag)){
			String andSql = "";
			for (Record record : reList) {
				andSql += "or ( id = '" + record.getStr("orgnum").trim() + "' and cust_no = '"+ record.getStr("customercode").trim() + "')  ";
			}
			if (StringUtils.isNotBlank(andSql)) {
				andSql = andSql.substring(2);
				List<Record> moneyList = getMoneyList(andSql);
				for (Record record : reList) {
					for (Record moneyRecord : moneyList) {
						if (record.getStr("id").equals(moneyRecord.getStr("id"))) {
							record.set("incompoint", moneyRecord.getBigDecimal("ck_curr_zcy").doubleValue());
							record.set("incomday", moneyRecord.getBigDecimal("ck_nrj_zcy").doubleValue());
							record.set("ilpoint", moneyRecord.getBigDecimal("bn_fic_nrj").doubleValue());
							record.set("ilday", moneyRecord.getBigDecimal("bw_fic_nrj").doubleValue());
							record.set("busi_inc", moneyRecord.getBigDecimal("cust_inc").doubleValue());
							record.set("finaday", moneyRecord.getBigDecimal("finaday").doubleValue());
							record.set("product_prop", moneyRecord.getStr("product_prop"));
							continue;
						}
					}
				}
			}
		}
		return records;
	}
	
	/**
	 * 列表查询
	 */
	public List<Record> custCntList(Map<String, Object> map) {
		Map<String, Object> maps = myfindSql(map);
		String orgnum=null;
		String custNo=null;
		//String claimProp=null;
		Record gr = new Record();
		@SuppressWarnings("unchecked")
		List<Record> reList = Db.use("default").find((String) maps.get("selectSql") + (String) maps.get("extrasql"), ((List<String>) maps.get("sqlStr")).toArray());
		if(null!=reList&&reList.size()>0){
			for(int i=0;i<reList.size();i++){
				orgnum = reList.get(i).getStr("orgnum");
				custNo = reList.get(i).getStr("customercode");
				//claimProp = reList.get(i).getStr("claim_prop");
				gr=mycustdbUtil.getBaseInfo(orgnum, custNo);
				if(null!=gr){
					reList.get(i).set("incompoint", gr.getBigDecimal("ck_curr_zcy").doubleValue());
					reList.get(i).set("incomday", gr.getBigDecimal("ck_nrj_zcy").doubleValue());
					reList.get(i).set("ilpoint", gr.getBigDecimal("bn_fic_nrj").doubleValue());
					reList.get(i).set("ilday", gr.getBigDecimal("bw_fic_nrj").doubleValue());
					reList.get(i).set("busi_inc", gr.getBigDecimal("cust_inc").doubleValue());
					reList.get(i).set("finaday", gr.getBigDecimal("finaday").doubleValue());
					reList.get(i).set("product_prop", gr.getStr("product_prop"));
				}
			}
		}
		return reList;
	}
	
	public Map<String, Object> myfindSql(Map<String, Object> map) {
		// 获取页面输入查询条件
		String cust_no = (String)map.get("cust_no");
		String name = (String)map.get("name");	
		String clas_five = (String)map.get("clas_five");	
		String cust_type	= (String)map.get("cust_type");
		
		String type_click	= (String)map.get("type_click");
		String clas_click	= (String)map.get("clas_click");
		String flag	= (String)map.get("flag");
		String roleLevel	= (String)map.get("roleLevel");
		String upOrg	= (String)map.get("upOrg");
		String areaOrg	= (String)map.get("areaOrg");

//		String selectSql = "select "
//						+"       p.id, "
//						+"       o.orgname resp_center_name, "
//						+"       p.customername, "
//						+"       p.customercode, "
//						+"       p.orgnum, "
//						+"       p.dummy_cust_no, "
//						+"       p.clas_five, "
//						+"       c.claim_prop, "
////						+"       (decode(m.payroll, 1, 20, 0) + decode(m.setcard, 1, 20, 0) + "
////						+"       decode(m.sms, 1, 20, 0) + decode(m.return_box, 1, 20, 0) + "
////						+"       decode(m.cyber_bank, 1, 20, 0)) || '%' as product_prop, "
////						+"       (m.incomday * c.claim_prop / 100) as incomday, "
////						+"       (decode(m.loanspoint, 0, 0, round(m.incompoint / m.loanspoint, 3))) as ilpoint, "
////						+"       (decode(m.loansday, 0, 0, round(m.incomday / m.loansday, 3))) as ilday, "
////						+"       m.busi_inc, "
//						+"       p1.name clas_five_cn ";
//		String extrasql = " from pccm_cust_pool p "
//				+" left join ( select cust_pool_id,sum(to_number(nvl(claim_prop, '0'))) claim_prop from pccm_cust_claim where 1=1  and del_stat='0' group by cust_pool_id) c on p.id = c.cust_pool_id " 
//				//+" left join pccm_cust_pool_money m on p.id = m.cust_pool_id " 
//				+" left join sys_org_info o on o.bancsid = p.org_id " 
//				+" left join gcms_param_info p1 on p.clas_five = p1.val and p1.key = 'clas_five' ";
/*******************************************20180909****************************************************/
		String selectSql = " select p.id, p.orgnum,"
				+"        p.customername, "
				+"        p.customercode, "
				+"        p.clas_potential, "
				+"        p.dummy_cust_no, ";
				if("1".equals(flag)){
//					selectSql+="      m.ck_curr_zcy as incompoint, "
//							+"        m.ck_nrj_zcy as incomday, "
//							+"        m.ck_loan_curr as ilpoint, "
//							+"        m.ck_loan_nrj as ilday, "
//							+"        p1.name clas_five_cn, "
//							+"        (nvl(m.int_inc, 0) + nvl(m.middle_income, 0) +nvl(m.splitting, 0)) busi_inc, "
//							//+"        (nvl(m.bn_fic_nrj, 0) + nvl(m.bw_fic_nrj, 0)) finaday, "
//							+"        to_char(decode(m.is_dfx, 1, 20, 0) + decode(m.is_jscard, 1, 20, 0) + "
//							+"                decode(m.is_dxt, 1, 20, 0) + decode(m.is_hdx, 1, 20, 0) + "
//							+"                decode(m.is_bocnet, 1, 20, 0)) || '%' as product_prop, ";
					selectSql+="      p1.name clas_five_cn,si.orgname as resp_center_name, ";
				}else if("2".equals(flag)){
					selectSql+=" u.name relate_mgr_name,u.phone relate_mgr_mobile,p.relate_cust_name,t.name as area_name, ";
				}

				selectSql+="       p.deptlevel ";
		String extrasql = " from pccm_cust_pool p  ";
				if("1".equals(flag)){
//					extrasql+="     left join pccm_cust_pool_money m on p.id = m.id||m.cust_no and m.deptlevel = '"+roleLevel+"' "
//							+"     and m.data_date = '"+AppUtils.findGNewDate()+"' "
					extrasql+="     left join gcms_param_info p1 on p.clas_five = p1.val and p1.key = 'clas_five' "
							+"     left join sys_org_info si on si.id = p.orgnum ";
				}else if("2".equals(flag)){
					extrasql+="     left join sys_user_info u on p.cust_manager = u.id "
							+"     left join pccm_provinces_citys t on p.area_code = t.id ";
				}
				
		StringBuffer whereSql = new StringBuffer(" where 1=1 ");
		List<String> sqlStr = new ArrayList<String>();
		
//		//默认查询条件
//		 whereSql.append(" and p.org_id=? ");
//		 sqlStr.add(orgId.trim());
		if ("1".equals(flag)) {
			whereSql.append(" and p.incflg='4' and p.cust_type='02' ");
			if (AppUtils.StringUtil(roleLevel) != null) {
				whereSql.append(" and p.deptlevel=? ");
				sqlStr.add(roleLevel.trim());
			}
			if (AppUtils.StringUtil(clas_five) != null) {
				whereSql.append(" and p.clas_five=? ");
				 sqlStr.add(clas_five.trim());
			}
			if (AppUtils.StringUtil(upOrg) != null) {
				whereSql.append(" and p.orgnum in  "+upOrg);
			}

		}else if("2".equals(flag)){
			whereSql.append(" and p.incflg<>'4' and p.area_code is not null ");
			if (AppUtils.StringUtil(type_click) != null) {
				whereSql.append(" and p.incflg=? ");
				sqlStr.add(type_click.trim());
			}
			if (AppUtils.StringUtil(areaOrg) != null) {
				whereSql.append(" and p.area_code in  "+areaOrg);
			}
		}
		
		if (AppUtils.StringUtil(cust_no) != null) {
			if ("1".equals(flag)) {
				whereSql.append(" and p.customercode=? ");
			}else if("2".equals(flag)){
				whereSql.append(" and p.dummy_cust_no=? ");
			}
			sqlStr.add(cust_no.trim());
		}
		if (AppUtils.StringUtil(name) != null) {
			whereSql.append(" and p.customername like ? ");
			 sqlStr.add("%"+name.trim()+"%");
		}
		if (AppUtils.StringUtil(clas_click) != null) {
			whereSql.append(" and p.clas_five=? ");
			sqlStr.add(clas_click.trim());
		}
		
		//排序
		//whereSql.append(" order by p.orgnum ");
		extrasql += whereSql.toString();
		Map<String, Object> mapSql = new HashMap<String, Object>();
		mapSql.put("selectSql", selectSql);
		mapSql.put("extrasql", extrasql);
		mapSql.put("sqlStr", sqlStr);
		return mapSql;
	}
/*******************************************20180908****************************************************/
	/**
	 * 客户统计列表查询-按组织
	 */
	public List<Record> custByOrgNewList(String upOrg) {
		List<Record> reList = null;
		if(AppUtils.StringUtil(upOrg)!=null){
			String sql = " select '1' as flag,'4' as incflg,b.* "
					+"  from pccm_cust_org_new_count b "
					+" where data_month='"+AppUtils.findGNewDate()+"'"
					+" and (id='"+upOrg+"'"
					+" or id in (select id from sys_org_info where upid = '"+upOrg+"'))"
					+" order by b.deptlevel,b.id ";
			reList = Db.use("default").find(sql);
			
		}
		return reList;
	}
	
	/**
	 * 下载客户统计列表查询-按组织
	 */
	public List<Record> downloadCustByOrgNewList(String upOrg, String dataDate) {
		List<Record> reList = null;
		if(AppUtils.StringUtil(upOrg)!=null){
			String sql = " select b.* "
					+"  from pccm_cust_org_new_count b "
					+" where data_month='"+dataDate+"'"
					+" and (id='"+upOrg+"'"
					+" or id in (select id from sys_org_info where by5 like '%"+upOrg+"%'))"
					+" order by b.id,b.deptlevel ";
			reList = Db.use("default").find(sql);
		}
		return reList;
	}
	
	/**
	 * 查询money表
	 * 存款时点，存款日均，存贷比时点，存贷比日均，营收，日均理财,产品覆盖率
	 * 营收=净利息收入+中间业务收入+分润
	 * 日均理财=表内理财年日均+表外理财总年日均
	 */
	public Record getMoneyInfo(String orgNo,String custNo){
		String sql = "select ck_curr_zcy,ck_nrj_zcy,ck_loan_curr,ck_loan_nrj, "
				+"	(nvl(int_inc,0)+nvl(middle_income,0)+nvl(splitting,0)) cust_inc, "
				+"	(nvl(bn_fic_nrj,0)+nvl(bw_fic_nrj,0)) finaday, "
				+"  to_char(decode(is_dfx, 1, 20, 0) + decode(is_jscard, 1, 20, 0) +  "
				+"  decode(is_dxt, 1, 20, 0) + decode(is_hdx, 1, 20, 0) + "
				+"  decode(is_bocnet, 1, 20, 0)) || '%' as product_prop "
				+"  from pccm_cust_pool_money "
				+"  where id=? and cust_no=? "
				+"	and data_date='"+AppUtils.findGNewDate()+"'";
		Record re = Db.use("default").findFirst(sql,new Object[]{orgNo,custNo});
		return re;
	}
	
	/**
	 * 查询money表
	 * 存款时点，存款日均，存贷比时点，存贷比日均，营收，日均理财,产品覆盖率
	 * 营收=净利息收入+中间业务收入+分润
	 * 日均理财=表内理财年日均+表外理财总年日均
	 */
	public List<Record> getMoneyList(String andSql){
		String sql = "select id||cust_no as id," 
				+"  nvl(ck_curr_zcy,0) as ck_curr_zcy, "
				+"  nvl(ck_nrj_zcy,0) as ck_nrj_zcy, "
				//+"  ck_loan_curr,ck_loan_nrj, "
//				+"  (decode(nvl(loan_curr_zcy,0), 0, 0, round(ck_curr_zcy / loan_curr_zcy, 4))) as ck_loan_curr, "
//				+"  (decode(nvl(loan_nrj_zcy,0), 0, 0, round(ck_nrj_zcy / loan_nrj_zcy, 4))) as ck_loan_nrj, "
				+"  nvl(bn_fic_nrj,0) as bn_fic_nrj, "
				+"  nvl(bw_fic_nrj,0) as bw_fic_nrj, "
				+"  (decode(nvl(loan_nrj_zcy,0), 0, 0, round(ck_nrj_zcy / loan_nrj_zcy, 4))) as ck_loan_nrj, "
				+"	(nvl(int_inc,0)+nvl(middle_income,0)+nvl(splitting,0)) cust_inc, "
				+"	(nvl(bn_fic_nrj,0)+nvl(bw_fic_nrj,0)) finaday, "
				+"  to_char(decode(is_dfx, 1, 20, 0) + decode(is_jscard, 1, 20, 0) +  "
				+"  decode(is_dxt, 1, 20, 0) + decode(is_hdx, 1, 20, 0) + "
				+"  decode(is_bocnet, 1, 20, 0)) || '%' as product_prop "
				+"  from pccm_cust_pool_money "
				+"  where data_date='"+AppUtils.findGNewDate()+"' and (" + andSql+ ")";
		List<Record>  re = Db.use("default").find(sql);
		return re;
	}
	
}
