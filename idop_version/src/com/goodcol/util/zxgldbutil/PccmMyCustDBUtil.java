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

public class PccmMyCustDBUtil {
	
	//gbase宽表最新数据日期
	private static String gbaseCustBaseDate = AppUtils.findGNewDate();
	
	/**
	 * 汇总列表查询
	 */
	public List<Record> mainList(Map<String, Object> map) {
		Map<String, Object> maps = findMainSql(map);
		@SuppressWarnings("unchecked")
		List<Record> reList = Db.use("default").find((String) maps.get("selectSql") + (String) maps.get("extrasql"), ((List<String>) maps.get("sqlStr")).toArray());
		return reList;

	}
	
	/**
	 * 分类明细页面
	 */
	public Page<Record> detailPage(Map<String, Object> map){
		// 获取查询参数
		int pageNum = (Integer)map.get("pageNum");
		int pageSize = (Integer)map.get("pageSize");;
		Map<String, Object> maps = findSql(map);
		@SuppressWarnings("unchecked")
		Page<Record> records = Db.use("default").paginate(pageNum, pageSize, (String) maps.get("selectSql"), (String) maps.get("extrasql"), ((List<String>) maps.get("sqlStr")).toArray());
		return records;
	}
	
	/**
	 * 汇总列表查询
	 */
	public List<Record> detailList(Map<String, Object> map) {
		Map<String, Object> maps = findSql(map);
		@SuppressWarnings("unchecked")
		List<Record> reList = Db.use("default").find((String) maps.get("selectSql") + (String) maps.get("extrasql"), ((List<String>) maps.get("sqlStr")).toArray());
		return reList;
	}
	
	public Map<String, Object> findMainSql(Map<String, Object> map) {
		// 获取当前用户信息
		//String orgId = (String)map.get("orgId");
		String userId = (String)map.get("userId");
		// 获取页面输入查询条件
		String cust_no = (String)map.get("cust_no");
		String name = (String)map.get("name");	
		String clas_five = (String)map.get("clas_five");	
		String cust_type	= (String)map.get("cust_type");
		String mark_prog	= (String)map.get("mark_prog");

		String selectSql = " select p.clas_five, pa.remark clas_five_cn, clas_five_num, loancash, null other ";
		String extrasql = " from (select clas_five, count(1) clas_five_num, nvl(sum(loancash), 0) loancash "
				+" from pccm_cust_pool t left join pccm_cust_claim c on c.cust_pool_id=t.id "
				+" where incflg = '4' and c.del_stat='0' " ;
		
		if (AppUtils.StringUtil(userId) != null) {
			extrasql +=" and claim_cust_mgr_id='"+userId.trim()+"'";
		}
		if (AppUtils.StringUtil(cust_no) != null) {
			extrasql +=" and customercode='"+cust_no.trim()+"' ";
		}
		if (AppUtils.StringUtil(name) != null) {
			extrasql +=" and customername like '%" + name.trim() + "%'";
		}
		if (AppUtils.StringUtil(clas_five) != null) {
			extrasql +=" and clas_five ='"+clas_five.trim()+"' ";
		}
		
		if (AppUtils.StringUtil(mark_prog) != null) {
			extrasql +=" and mark_prog='"+mark_prog.trim()+"' ";
		}
		
		if (AppUtils.StringUtil(cust_type) != null) {
			extrasql +=" and custyp='"+cust_type.trim()+"' ";
		}
		extrasql +=" group by clas_five) p  "
				+" left join gcms_param_info pa on p.clas_five = pa.val and pa.key = 'CTYPE' ";
		StringBuffer whereSql = new StringBuffer(" where 1=1 ");
		List<String> sqlStr = new ArrayList<String>();
		
		/*//默认查询条件
		 whereSql.append(" and p.org_id=? ");
		 sqlStr.add(orgId.trim());*/
		
		//排序
		extrasql += whereSql.toString();
		Map<String, Object> mapSql = new HashMap<String, Object>();
		mapSql.put("selectSql", selectSql);
		mapSql.put("extrasql", extrasql);
		mapSql.put("sqlStr", sqlStr);
		return mapSql;
	}
	
	public Map<String, Object> findSql(Map<String, Object> map) {
		// 获取当前用户信息
//		String orgId = (String)map.get("orgId");
		String userId = (String)map.get("userId");
		// 获取页面输入查询条件
		String cust_no = (String)map.get("cust_no");
		String name = (String)map.get("name");	
		String clas_five = (String)map.get("clas_five");	
		String cust_type	= (String)map.get("cust_type");
		String mark_prog	= (String)map.get("mark_prog");
		String clas_click	= (String)map.get("clas_click");

		String selectSql = " select p.id,c.id claim_id,p.org_id, o.orgname, p.customercode, p1.remark clas_five_cn,p.customername,c.claim_prop, " +
				"p2.name cust_type_cn, loancash, incomday, incompoint, busi_inc, p3.name mark_prog_cn, p.remark " ;
		String extrasql = " from ( select cust_pool_id,claim_prop,id from pccm_cust_claim where 1=1  and del_stat='0' ";
				if (AppUtils.StringUtil(userId) != null) {
					extrasql +=" and claim_cust_mgr_id='"+userId.trim()+"' ";
				}		
		extrasql +=" ) c " +
				"left join pccm_cust_pool p on p.id = c.cust_pool_id " +
				"left join sys_org_info o on o.id = p.org_id " +
				"left join gcms_param_info p1 on p.clas_five = p1.val and p1.key = 'CTYPE' " +
				"left join gcms_param_info p2 on p.custyp = p2.val and p2.key = 'cust_type' " +
				"left join gcms_param_info p3 on p.mark_prog = p3.val and p3.key = 'mark_prog'";
		StringBuffer whereSql = new StringBuffer(" where 1=1 ");
		List<String> sqlStr = new ArrayList<String>();
		
//		//默认查询条件
//		 whereSql.append(" and p.org_id=? ");
//		 sqlStr.add(orgId.trim());
		if (AppUtils.StringUtil(cust_no) != null) {
			whereSql.append(" and p.customercode=? ");
			 sqlStr.add(cust_no.trim());
		}
		if (AppUtils.StringUtil(name) != null) {
			whereSql.append(" and shortname like "+"%" + name.trim() + "%");
		}
		if (AppUtils.StringUtil(clas_five) != null) {
			whereSql.append(" and p.clas_five=? ");
			 sqlStr.add(clas_five.trim());
		}
		if (AppUtils.StringUtil(mark_prog) != null) {
			whereSql.append(" and p.mark_prog=? ");
			 sqlStr.add(mark_prog.trim());
		}
		if (AppUtils.StringUtil(cust_type) != null) {
			whereSql.append(" and p.custyp=? ");
			 sqlStr.add(cust_type.trim());
		}
		if (AppUtils.StringUtil(clas_click) != null) {
			whereSql.append(" and p.clas_five=? ");
			 sqlStr.add(clas_click.trim());
		}
		
		//排序
		extrasql += whereSql.toString();
		Map<String, Object> mapSql = new HashMap<String, Object>();
		mapSql.put("selectSql", selectSql);
		mapSql.put("extrasql", extrasql);
		mapSql.put("sqlStr", sqlStr);
		return mapSql;
	}
	
	/**
	 * 移交保存
	 */
	public void transfersave(Map<String, Object> map){
		int claim_prop = (Integer)map.get("claim_prop");
		String claim_id=(String)map.get("claim_id");
		String cust_no=(String)map.get("cust_no");
		String mgrId=(String)map.get("mgrId");
		String poolId=(String)map.get("poolId");
		String mgrName=(String)map.get("mgrName");
		int to_mgr_pro=(Integer)map.get("to_mgr_pro");
		int toProp=(Integer)map.get("toProp");
		//修改原客户经理的认领状态
		if(claim_prop!=0){
			Db.use("default").update(" update pccm_cust_claim set claim_prop=? where del_stat='0' and id=? ",new Object[]{claim_prop,claim_id});
		}else if(claim_prop==0){
			deleteClaim(claim_id);
		}
		
		//判断移交的客户经理是否是领过
		if(to_mgr_pro!=0){
			Db.use("default").update(" update pccm_cust_claim set claim_prop=? where del_stat='0' and claim_cust_mgr_id=? and claim_cust_mgr_name=? and cust_pool_id=? ",new Object[]{toProp,mgrId,mgrName,poolId});
		}else if(to_mgr_pro==0){
			//查询客户来源
			String incflg = Db.use("default").queryStr(" select incflg from pccm_cust_pool where id = ? ",new Object[]{poolId});
			//插入数据
			Db.use("default").update(" insert into pccm_cust_claim(id,cust_no,claim_prop,claim_cust_mgr_id,"
					+"	claim_cust_mgr_name,claim_time,del_stat,cust_pool_id,incflg) values(?,?,?,?,?,to_char(sysdate,'yyyyMMddhh24miss'),'0',?,?) " ,
					new Object[] { AppUtils.getStringSeq(),cust_no,toProp,mgrId,mgrName,poolId,incflg});
		}
		
	}
	
	/**
	 * 判断是否是人领过客户
	 */
	public Integer getProByIds(String mgrId,String pool_id){
		int result=0;
		List<Record> reList = Db.use("default").find("select nvl(claim_prop,0) claim_prop from pccm_cust_claim where del_stat='0' and claim_cust_mgr_id=?and cust_pool_id=?",new Object[]{mgrId,pool_id});
		if(null!=reList&&reList.size()>0){
			result = Integer.parseInt((String) reList.get(0).get("claim_prop"));
		}
		return result;
	}
	
	/**
	 * 删除认领记录
	 */
	public void deleteClaim(String claimId){
		Db.use("default").update("update pccm_cust_claim set del_stat='1',del_time=to_char(sysdate,'yyyyMMdd') where 1=1 and " +
				"id=?",new Object[]{claimId});
					
	}
	
	/**
	 * 查询认领记录
	 */
	public Record getClaim(String claimId){
		return Db.use("default").findFirst(" select * from pccm_cust_claim where id=?",new Object[]{claimId});
	}
	
	/***20180623*********************************************************************************************************/
	
	/**
	 * 五层分类列表查询
	 */
	public List<Record> clasFiveList(String userId) {
//		String sql = " select p.name clas_five_cn, "
//					+"        p.val clas_five, "
//					+"        nvl(level_weight, 0) level_weight, "
//					+"        nvl(newCnt, 0) newCnt, "
//					+"        nvl(cnt, 0) cnt, "
//					+"        nvl(incomday, 0) incomday, "
//					+"        nvl(finaday, 0) finaday, "
//					+"        '1' flag, '4' incflg "
//					+"   from gcms_param_info p "
//					+"   left join (select clas_five, "
//					+"                     sum(t.level_value * t.level_weight) as level_weight, "
//					+"                     sum(to_number(nvl(t.is_new_cust, '0'))) newCnt, "
//					+"                     sum(nvl(m.ck_curr_zcy, 0)*(to_number(c.claim_prop)/100)) incomday, "
//					+"                     sum((nvl(m.bn_fic_nrj, 0) + nvl(m.bw_fic_nrj, 0))*(to_number(c.claim_prop)/100)) finaday, "
//					+"                     count(1) cnt "
//					+"                from pccm_cust_claim c "
//					+"               left join pccm_cust_pool t on c.cust_pool_id = t.id "
//					+"     left join pccm_cust_pool_money m on t.id = m.id||m.cust_no and m.deptlevel = '3' and m.data_date = '"+AppUtils.findGNewDate()+"' "
//					+"               where c.del_stat = '0' "
//					+"                 and c.incflg = '4' "
//					+"                 and c.claim_cust_mgr_id = '"+userId+"' "
//					+"               group by clas_five) b "
//					+"     on p.val = b.clas_five "
//					+"  where p.key = 'clas_five' "
//					+"  order by p.val ";
		
		String sql = " select cust_pool_id,substr(cust_pool_id,1,9) as orgnum , cust_no "
				+ "from pccm_cust_claim " + "where claim_cust_mgr_id ='"
				+ userId + "' " + "and del_stat = '0' and incflg = '4'";
		List<Record> reList = Db.use("default").find(sql);
		String ids = "";
		String andSql = "";
		for (Record record : reList) {
			ids += "or id ='" + record.getStr("cust_pool_id").trim() + "' ";
			andSql += "or ( id = '" + record.getStr("orgnum").trim()
					+ "' and cust_no = '" + record.getStr("cust_no").trim()
					+ "')  ";
		}
		
		if (StringUtils.isNotBlank(andSql)) {
			ids = ids.substring(2);
			andSql = andSql.substring(2);
			sql = " select p.name clas_five_cn, "
					+ "        p.val clas_five, "
					+ "        nvl(level_weight, 0) level_weight, "
					+ "        nvl(newCnt, 0) newCnt, "
					+ "        nvl(cnt, 0) cnt, "
					+ "        nvl(incomday, 0) incomday, "
					+ "        nvl(finaday, 0) finaday, "
					+ "        '1' flag, '4' incflg "
					+ "   from gcms_param_info p "
					+ "   left join (select t.clas_five, "
					+ "                     sum(t.level_value * t.level_weight) as level_weight, "
					+ "                     sum(to_number(nvl(t.is_new_cust, '0'))) newCnt, "
					+ "                     sum(nvl(m.ck_curr_zcy, 0)*(to_number(k.claim_prop)/100)) incomday, "
					+ "                     sum((nvl(m.bn_fic_nrj, 0) + nvl(m.bw_fic_nrj, 0))*(to_number(k.claim_prop)/100)) finaday, "
					+ "                     count(1) cnt from ((select id,clas_five,level_value,level_weight,is_new_cust from pccm_cust_pool where "
					+ ids + ") t "
					+ "left join (select * from pccm_cust_pool_money "
					+ "where data_date='" + AppUtils.findGNewDate()
					+ "' and (" + andSql + ")) m"
					+ " on t.id = m.id||m.cust_no left join (select * from pccm_cust_claim  where claim_cust_mgr_id ='"
					+ userId + "'  and del_stat = '0' and incflg = '4') k on t.id = k.cust_pool_id) group by t.clas_five) b"
					+ "     on p.val = b.clas_five "
					+ "  where p.key = 'clas_five' order by p.val ";
		} else {
			sql = " select p.name clas_five_cn, "
					+ "        p.val clas_five, "
					+ "        0 level_weight, "
					+ "        0 newCnt, "
					+ "        0 cnt, "
					+ "        0 incomday, "
					+ "        0 finaday, "
					+ "        '1' flag, '4' incflg "
					+ "   from gcms_param_info p "
					+ "  where p.key = 'clas_five' order by p.val ";
		}
		//System.out.println(sql);
		return Db.use("default").find(sql);
	}
	/**
	 * 客户分类列表查询
	 */
	public List<Record> custClasList(String userId) {
		String sql = "select p.name incflg_cn,p.val incflg, nvl(cnt,0) cnt,'2' flag "
					+"  from (select c.incflg, count(1) cnt "
					+"               from pccm_cust_claim c "
					+"               left join pccm_cust_pool t "
					+"                 on c.cust_pool_id = t.id "
					+"              where 1=1 "
					+"                and c.del_stat = '0' "
					+"                and c.incflg != '4' "
					+"                and t.area_code is not null "
					+"                and c.claim_cust_mgr_id = ? "
					+"              group by c.incflg) b "
					+"  right join gcms_param_info p "
					+"    on b.incflg = p.val "
					+"   where  p.key = 'incflg' and  p.val != '4' "
					+" order by p.val ";
		List<Record> reList = Db.use("default").find(sql,new Object[]{userId});
		return reList;
	}
	/**
	 * 我的客户详情列表
	 */
	public Page<Record> myCustPage(Map<String, Object> map){
		// 获取查询参数
		int pageNum = (Integer)map.get("pageNum");
		int pageSize = (Integer)map.get("pageSize");;
		Map<String, Object> maps = myfindSql(map);
		@SuppressWarnings("unchecked")
		Page<Record> records = Db.use("default").paginate(pageNum, pageSize, (String) maps.get("selectSql"), (String) maps.get("extrasql"), ((List<String>) maps.get("sqlStr")).toArray());
//		List<Record> reList=records.getList();
//		String orgnum=null;
//		String custNo=null;
//		String claimProp=null;
//		Record gr = new Record();
//		if(null!=reList&&reList.size()>0){
//			for(int i=0;i<reList.size();i++){
//				orgnum = reList.get(i).getStr("orgnum");
//				custNo = reList.get(i).getStr("customercode");
//				claimProp = reList.get(i).getStr("claim_prop");
//				gr=getBaseInfo(orgnum, custNo);
//				if(null!=gr){
//					reList.get(i).set("incompoint", (gr.getBigDecimal("ck_curr_zcy").doubleValue())*(Double.parseDouble(claimProp)/100));
//					reList.get(i).set("incomday", (gr.getBigDecimal("ck_nrj_zcy").doubleValue())*(Double.parseDouble(claimProp)/100));
//					reList.get(i).set("ilpoint", gr.getBigDecimal("ck_loan_curr"));
//					reList.get(i).set("ilday", gr.getBigDecimal("ck_loan_nrj"));
//					reList.get(i).set("busi_inc", (gr.getBigDecimal("cust_inc").doubleValue())*(Double.parseDouble(claimProp)/100));
//					reList.get(i).set("finaday", (gr.getBigDecimal("finaday").doubleValue())*(Double.parseDouble(claimProp)/100));
//					reList.get(i).set("product_prop", gr.getStr("product_prop"));
//				}
//			}
//		}
		return records;
	}
	
	/**
	 * 汇总列表查询
	 */
	public List<Record> myCustList(Map<String, Object> map) {
		Map<String, Object> maps = myfindSql(map);
//		String orgnum=null;
//		String custNo=null;
//		String claimProp=null;
//		Record gr = new Record();
		@SuppressWarnings("unchecked")
		List<Record> reList = Db.use("default").find((String) maps.get("selectSql") + (String) maps.get("extrasql"), ((List<String>) maps.get("sqlStr")).toArray());
//		if(null!=reList&&reList.size()>0){
//			for(int i=0;i<reList.size();i++){
//				orgnum = reList.get(i).getStr("orgnum");
//				custNo = reList.get(i).getStr("customercode");
//				claimProp = reList.get(i).getStr("claim_prop");
//				gr=getBaseInfo(orgnum, custNo);
//				if(null!=gr){
//					reList.get(i).set("incompoint", (gr.getBigDecimal("ck_curr_zcy").doubleValue())*(Double.parseDouble(claimProp)/100));
//					reList.get(i).set("incomday", (gr.getBigDecimal("ck_nrj_zcy").doubleValue())*(Double.parseDouble(claimProp)/100));
//					reList.get(i).set("ilpoint", gr.getBigDecimal("ck_loan_curr"));
//					reList.get(i).set("ilday", gr.getBigDecimal("ck_loan_nrj"));
//					reList.get(i).set("busi_inc", (gr.getBigDecimal("cust_inc").doubleValue())*(Double.parseDouble(claimProp)/100));
//					reList.get(i).set("finaday", (gr.getBigDecimal("finaday").doubleValue())*(Double.parseDouble(claimProp)/100));
//					reList.get(i).set("product_prop", gr.getStr("product_prop"));
//				}
//			}
//		}
		return reList;
	}
	
	public Map<String, Object> myfindSql(Map<String, Object> map) {
		// 获取当前用户信息
//		String orgId = (String)map.get("orgId");
		String userId = (String)map.get("userId");
		// 获取页面输入查询条件
		String cust_no = (String)map.get("cust_no");
		String name = (String)map.get("name");	
		String clas_five = (String)map.get("clas_five");	
		String cust_type	= (String)map.get("cust_type");
		String clas_click	= (String)map.get("clas_click");
		String type_click	= (String)map.get("type_click");
		String flag	= String.valueOf(map.get("flag"));

		String selectSql = " select p.id, p.orgnum,"
				+"        p.customername, "
				+"        p.customercode, "
				+"        p.clas_potential, "
				+"        c.claim_prop, "
				+"        c.id as claim_id, "
				+"        p.dummy_cust_no, ";
				if("1".equals(flag)){
					selectSql+="      nvl(m.ck_curr_zcy,0)*(to_number(c.claim_prop)/100) as incompoint, "
							+"        nvl(m.ck_nrj_zcy,0)*(to_number(c.claim_prop)/100) as incomday, "
//							+"        m.ck_loan_curr as ilpoint, "
//							+"        m.ck_loan_nrj as ilday, "
//							+"  	  (decode(nvl(m.loan_curr_zcy,0), 0, '-', round(m.ck_curr_zcy / m.loan_curr_zcy, 4))) as ilpoint, "
//							+"		  (decode(nvl(m.loan_nrj_zcy,0), 0, '-', round(m.ck_nrj_zcy / m.loan_nrj_zcy, 4))) as ilday, "
							+"  	  m.bn_fic_nrj as ilpoint, "
							+"		  m.bw_fic_nrj as ilday, "
							+"        p1.name clas_five_cn, "
							+"        (nvl(m.int_inc, 0) + nvl(m.middle_income, 0) +nvl(m.splitting, 0))*(to_number(c.claim_prop)/100) busi_inc, "
							//+"        (nvl(m.bn_fic_nrj, 0) + nvl(m.bw_fic_nrj, 0))*(to_number(c.claim_prop)/100) finaday, "
							+"        to_char(decode(m.is_dfx, 1, 20, 0) + decode(m.is_jscard, 1, 20, 0) + "
							+"                decode(m.is_dxt, 1, 20, 0) + decode(m.is_hdx, 1, 20, 0) + "
							+"                decode(m.is_bocnet, 1, 20, 0)) || '%' as product_prop, "
							+"               o.orgname as resp_center_name, ";
				}else if("2".equals(flag)){
					selectSql+=" u.name relate_mgr_name,u.phone relate_mgr_mobile,p.relate_cust_name,t.name as area_name, ";
				}

				selectSql+="        p.deptlevel ";
		String extrasql = " from ( select cust_pool_id,claim_prop,id,incflg from pccm_cust_claim where 1=1  and del_stat='0' ";
				if (AppUtils.StringUtil(userId) != null) {
					extrasql +=" and claim_cust_mgr_id='"+userId.trim()+"' ";
				}		
		extrasql +=" ) c " +
				" inner join pccm_cust_pool p on p.id = c.cust_pool_id ";
				if("1".equals(flag)){
					extrasql+="     left join pccm_cust_pool_money m on p.id = m.cust_pool_id "
							+"     and m.data_date = '"+AppUtils.findGNewDate()+"' "
							+"     left join gcms_param_info p1 on p.clas_five = p1.val and p1.key = 'clas_five' "
							+" 	   left join sys_org_info o on o.bancsid = p.org_id";
				}else if("2".equals(flag)){
					extrasql+="   left join sys_user_info u on p.cust_manager = u.id  "
							+"     left join pccm_provinces_citys t on p.area_code = t.id ";
				}
				
		//extrasql +=	" left join sys_org_info o on o.bancsid = p.org_id";
		StringBuffer whereSql = new StringBuffer(" where 1=1 ");
		List<String> sqlStr = new ArrayList<String>();
		
//		//默认查询条件
//		 whereSql.append(" and p.org_id=? ");
//		 sqlStr.add(orgId.trim());
		if ("1".equals(flag)) {
			whereSql.append(" and p.incflg = '4' ");
			if (AppUtils.StringUtil(clas_click) != null) {
				whereSql.append(" and p.clas_five=? ");
				sqlStr.add(clas_click.trim());
			}
			if (AppUtils.StringUtil(clas_five) != null) {
				whereSql.append(" and p.clas_five=? ");
				 sqlStr.add(clas_five.trim());
			}
		}else if("2".equals(flag)){
			whereSql.append(" and p.incflg <> '4' and p.area_code is not null ");
			if (AppUtils.StringUtil(type_click) != null) {
				whereSql.append(" and p.incflg=? ");
				sqlStr.add(type_click.trim());
			}
		}
		
		if (AppUtils.StringUtil(cust_no) != null) {
			if (AppUtils.StringUtil(cust_no) != null) {
				if ("1".equals(flag)) {
					whereSql.append(" and p.customercode=? ");
				}else if("2".equals(flag)){
					whereSql.append(" and p.dummy_cust_no=? ");
				}
				sqlStr.add(cust_no.trim());
			}
			//sqlStr.add(cust_no.trim());
		}
		if (AppUtils.StringUtil(name) != null) {
			whereSql.append(" and p.customername like ? ");
			 sqlStr.add("%"+name.trim()+"%");
		}
		
		if (AppUtils.StringUtil(cust_type) != null) {
			whereSql.append(" and p.custyp=? ");
			 sqlStr.add(cust_type.trim());
		}
		
		//排序
		extrasql += whereSql.toString();
		Map<String, Object> mapSql = new HashMap<String, Object>();
		mapSql.put("selectSql", selectSql);
		mapSql.put("extrasql", extrasql);
		mapSql.put("sqlStr", sqlStr);
		return mapSql;
	}
	
	/**
	 * 产品覆盖率查询
	 */
	public Record custdetail(String pool_id) {
//		String sql = "select p.org_id resp_center_no, p.customername, p.customercode, "
//					+" case when m.payroll='1' then '是' when m.payroll='0' then '否' else '否' end payroll, "
//					+" case when m.setcard='1' then '是' when m.setcard='0' then '否' else '否' end setcard, "
//					+" case when m.sms='1' then '是' when m.sms='0' then '否' else '否' end sms, "
//					+" case when m.return_box='1' then '是' when m.return_box='0' then '否' else '否' end return_box, "
//					+" case when m.cyber_bank='1' then '是' when m.cyber_bank='0' then '否' else '否' end cyber_bank "
//					+" from pccm_cust_pool p "
//					+" left join pccm_cust_pool_money m "
//					+" on p.id = m.cust_pool_id "
//					//+" left join sys_org_info o on o.bancsid = p.org_id"
//					+" where  p.id=? ";
//		Record reList = Db.use("default").findFirst(sql,new Object[]{pool_id});
//		return reList;
		return null;
	}
	
//	/**
//	 * gbase查询产品覆盖率详情
//	 */
//	public Record custdetail(String cust_no,String orgnum) {
//		String sql = " select o.bancsid resp_center_no, m.name customername, m.cust_no customercode,"
//				+" 	case when m.is_dfx='1' then '是' when m.is_dfx='0' then '否' else '否' end payroll,"
//				+" 	case when m.is_jscard='1' then '是' when m.is_jscard='0' then '否' else '否' end setcard,"
//				+" 	case when m.is_dxt='1' then '是' when m.is_dxt='0' then '否' else '否' end sms,"
//				+" 	case when m.is_hdx='1' then '是' when m.is_hdx='0' then '否' else '否' end return_box,"
//				+" 	case when m.is_bocnet='1' then '是' when m.is_bocnet='0' then '否' else '否' end cyber_bank 	"
//				+" from pccm_cust_base_info m "
//				+" 	left join sys_org_info o on o.id = m.id"
//				+"  where m.id=? and m.cust_no=? "
//				+"	and m.data_date='"+gbaseCustBaseDate+"' limit 10 ";
//		Record reList = Db.use("gbase").findFirst(sql,new Object[]{orgnum,cust_no});		
//		//gbase查询产品覆盖率
//		return reList;
//	}
	
	/**
	 * money表查询产品覆盖率详情
	 * 20180909
	 */
	public Record custdetail(String cust_no,String orgnum) {
		String sql = " select o.bancsid resp_center_no, p.customername, p.customercode,"
				+" 	case when m.is_dfx='1' then '是' when m.is_dfx='0' then '否' else '否' end payroll,"
				+" 	case when m.is_jscard='1' then '是' when m.is_jscard='0' then '否' else '否' end setcard,"
				+" 	case when m.is_dxt='1' then '是' when m.is_dxt='0' then '否' else '否' end sms,"
				+" 	case when m.is_hdx='1' then '是' when m.is_hdx='0' then '否' else '否' end return_box,"
				+" 	case when m.is_bocnet='1' then '是' when m.is_bocnet='0' then '否' else '否' end cyber_bank 	"
				+" from pccm_cust_pool_money m "
				+"  inner join pccm_cust_pool p on p.id = m.id || m.cust_no "
				+" 	left join sys_org_info o on o.id = m.id"
				+"  where m.id=? and m.cust_no=? "
				+"	and m.data_date='"+AppUtils.findGNewDate()+"'";
		Record reList = Db.use("default").findFirst(sql,new Object[]{orgnum,cust_no});		
		//gbase查询产品覆盖率
		return reList;
	}
	
	
	
	/**
	 * 五层分类说明
	 */
	public List<Record> clasFiveText() {
		String sql = "select a.*,d.name||'客户' clas_five_name "
					+"   from (select c.condition_flag, "
					+"                c.symbol1, "
					+"                c.condition_val1, "
					+"                c.symbol2, "
					+"               c.condition_val2, "
					+"               c.standard_remark, "
					+"               c.result_value, "
					+"               p.id "
					+"           from pccm_standard_info p, pccm_standard_condition c "
					+"          where p.id = c.standard_id) a "
					+"   left join (select name from gcms_param_info where key = 'STANDARD_TYPE') b "
					+"     on a.symbol1 = b.name "
					+"   left join (select name from gcms_param_info where key = 'STANDARD_TYPE') c "
					+"     on a.symbol2 = c.name "
					+"     left join (select val ,name from gcms_param_info where key = 'clas_five') d "
					+"     on a.result_value = d.val "
					+"  where a.condition_flag = 'deposit_financial' "
					+"  order by result_value ";
		List<Record> reList = Db.use("default").find(sql);
		return reList;
	}
	
	/**
	 * 查询数据最新入池时间
	 */
	public String findNewDate(String userId){
		/*String sql =" select max(maxDate) maxDate "
				+"  from (select case "
				+"                 when third_indate is null then "
				+"                  indate "
				+"                 else "
				+"                  third_indate "
				+"               end as maxDate "
				+"          from pccm_cust_pool p "
				+"         inner join pccm_cust_claim c "
				+"            on p.id = c.cust_pool_id "
				+"         where c.del_stat = '0' ";
		if (AppUtils.StringUtil(userId) != null) {
			sql += " and c.claim_cust_mgr_id = "+userId;
		}
		sql+=" ) ";*/
		String sql =" select max(data_date) maxDate "
				+"  from pccm_cust_pool_money ";
		String result =Db.use("default").queryStr(sql) ;
		return result;
	}
	
	/**
	 * 用户当前组织成员下拉框数据
	 */
	public Page<Record> getOrgUser(Map<String, Object> map){
		// 获取查询参数
		int pageNum = (Integer)map.get("pageNum");
		int pageSize = (Integer)map.get("pageSize");
		
		String user_no = (String)map.get("user_no");	
		String name	= (String)map.get("name");
	//		String orgId	= (String)map.get("orgId");
		String selectSql = " select u.id,u.name,u.org_id, o.orgname, '0' as cnt ";
	//		+" (select count(1) cnt from pccm_cust_claim where del_stat = '0' and claim_cust_mgr_id = u.id) cnt ";
	//		String extrasql=" from sys_user_info u "
	//				+" left join gcms_role_apply a on u.user_no = a.user_id "
	//				+" left join gcms_param_info p on p.id = a.role_id "
	//				+" left join sys_org_info o on u.org_id = o.id "
	//				+" where 1=1 ";
	//		if (AppUtils.StringUtil(user_no) != null) {
	//			extrasql+=" and u.user_no = '"+user_no+"' ";
	//		}
	//		if (AppUtils.StringUtil(name) != null) {
	//			extrasql+=" and u.name like '%"+name+"%' ";
	//		}
	//		extrasql+=" and (u.dele_flag='0' or u.stat!='1') and a.apply_status='1'";
	//		if (AppUtils.StringUtil(orgId) != null) {
	//			extrasql+=" and u.org_id in "+orgId;
	//		}
	//		extrasql+=" order by u.org_id ";
		
		String extrasql = " from sys_user_info u "
				+ " left join sys_org_info o on u.org_id = o.id "
				+ " where 1=1 ";
		if (AppUtils.StringUtil(user_no) != null) {
			extrasql += " and u.user_no = '" + user_no + "' ";
		}
		if (AppUtils.StringUtil(name) != null) {
			extrasql += " and u.name like '%" + name + "%' ";
		}
		extrasql += " order by u.id,u.org_id ";
		
		Page<Record> records = Db.use("default").paginate(pageNum, pageSize, selectSql, extrasql, new Object[]{});
		List<Record> list = records.getList();
		String ids = "";
		for (Record record : list) {
			ids += (",'" + record.getStr("id") + "'");
		}
		if (StringUtils.isNotBlank(ids)) {
			ids = ids.substring(1);
			selectSql = " select u.id,u.name,u.org_id, o.orgname, t.cnt ";
			extrasql = " from sys_user_info u "
			+ " left join sys_org_info o on u.org_id = o.id "
			+ " left join (select count(1) as cnt,claim_cust_mgr_id " 
			+ " from pccm_cust_claim where del_stat = '0' and claim_cust_mgr_id in(" + ids + ")"
			+ " group by claim_cust_mgr_id) t on t.claim_cust_mgr_id=u.id "
			+ " where 1=1 ";
			if (AppUtils.StringUtil(user_no) != null) {
				extrasql += " and u.user_no = '" + user_no + "' ";
			}
			if (AppUtils.StringUtil(name) != null) {
				extrasql += " and u.name like '%" + name + "%' ";
			}
			extrasql += " order by u.id,u.org_id ";
			records = Db.use("default").paginate(pageNum, pageSize, selectSql, extrasql, new Object[]{});
		}
		return records;
	}
	
	/**
	 * 查询gbase宽表
	 * 存款时点，存款日均，存贷比时点，存贷比日均，营收，日均理财,产品覆盖率
	 * 营收=净利息收入+中间业务收入+分润
	 * 日均理财=表内理财年日均+表外理财总年日均
	 */
	public Record getBaseInfo(String orgNo,String custNo){
		String sql = "select ck_curr_zcy,ck_nrj_zcy,ck_loan_curr,ck_loan_nrj, "
				+"	(nvl(int_inc,0)+nvl(middle_income,0)+nvl(splitting,0)) cust_inc, "
				//+"	nvl(int_inc,0) cust_inc, "
				+"	(nvl(bn_fic_nrj,0)+nvl(bw_fic_nrj,0)) finaday, "
				+"  to_char(decode(is_dfx, 1, 20, 0) + decode(is_jscard, 1, 20, 0) +  "
				+"  decode(is_dxt, 1, 20, 0) + decode(is_hdx, 1, 20, 0) + "
				+"  decode(is_bocnet, 1, 20, 0)) || '%' as product_prop "
				+"  from pccm_cust_base_info "
				+"  where id=? and cust_no=? "
				+"	and data_date='"+gbaseCustBaseDate+"'  ";
		Record re = Db.use("gbase").findFirst(sql,new Object[]{orgNo,custNo});
		return re;
	}
	
}
