package com.goodcol.util.zxgldbutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;

public class CustAppealDBUtil {
	
	CustClaimDBUtil claimDbUtil = new CustClaimDBUtil();
	
	/**
	 * 客户申诉列表
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
		String orgArr = (String)map.get("orgArr");
		// 获取页面输入查询条件
		String cust_no = (String)map.get("cust_no");
		String name = (String)map.get("name");	
		String cust_mgr = (String)map.get("cust_mgr");	
		String appeal_stat = (String)map.get("appeal_stat");
		String appeal_reas = (String)map.get("appeal_reas");

		String selectSql = "select rownum rn, "
						+"       p.id, "
						+"       p.customercode||p.dummy_cust_no   cust_no, "
						+"       p.customername        name, "
						+"       p.clas_five, "
						+"       p.custyp, "
						+"       p.cussts, "
//						+"       c.claim_prop, "
//						+"       c.claim_cust_mgr_id, "
//						+"       c.claim_cust_mgr_name, "
						+"       p.befor_cust_mgr_id, "
						+"       p.befor_cust_mgr_name, "
						+"       a.id                  appealId, "
						+"       a.appeal_per_name, "
						+"       u.mobile, "
						+"       a.appeal_stat, "
						+"       a.appeal_result, "
						+"       a.appeal_reas, "
						+"       p1.name         appeal_stat_cn, "
						+"       p2.name         appeal_result_cn ";
		String extrasql = "  from pccm_cust_appeal a "
						+"  left join pccm_cust_pool p "
						+"    on a.cust_pool_id = p.id "
//						+"  left join (select cust_pool_id, "
//						+"                    to_char(wm_concat(claim_prop)) claim_prop, "
//						+"                    to_char(wm_concat(claim_cust_mgr_id)) claim_cust_mgr_id, "
//						+"                    to_char(wm_concat(claim_cust_mgr_name)) claim_cust_mgr_name "
//						+"               from pccm_cust_claim "
//						+"              where del_stat = '0' "
//						+"              group by cust_pool_id) c "
//						+"    on p.id = c.cust_pool_id "
						+"    left join sys_user_info u on a.appeal_per_id = u.id "
						+"  left join gcms_param_info p1 "
						+"    on a.appeal_stat = p1.val "
						+"   and p1.key = 'appeal_stat' "
						+"  left join gcms_param_info p2 "
						+"    on a.appeal_result = p2.val "
						+"   and p2.key = 'appeal_result' ";
		StringBuffer whereSql = new StringBuffer(" where 1=1 ");
		List<String> sqlStr = new ArrayList<String>();
		
		//默认查询条件
		if (AppUtils.StringUtil(orgArr) != null) {
			 whereSql.append(" and p.orgnum in "+orgArr);
		}
		
		if (AppUtils.StringUtil(cust_no) != null) {
			 whereSql.append(" and p.customercode||p.dummy_cust_no=? ");
			 sqlStr.add(cust_no.trim());
		}
		if (AppUtils.StringUtil(name) != null) {
			whereSql.append(" and p.customername like ? ");
			sqlStr.add("%" + name.trim() + "%");
		}
		if (AppUtils.StringUtil(cust_mgr) != null) {
			whereSql.append(" and instr(c.appeal_per_name, ?)>0 ");
			sqlStr.add(cust_mgr.trim());
		}
		
		if (AppUtils.StringUtil(appeal_stat) != null) {
			whereSql.append(" and a.appeal_stat= ? ");
			sqlStr.add(appeal_stat.trim());
		}
		
		if (AppUtils.StringUtil(appeal_reas) != null) {
			whereSql.append(" and a.appeal_reas like ? ");
			sqlStr.add("%" + appeal_reas.trim() + "%");
		}
		
		//排序
		//whereSql.append(" order by p.cussts,p.indate,p.customercode ");
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
	public List<Record> custList(Map<String, Object> map) {
		Map<String, Object> maps = findSql(map);
		@SuppressWarnings("unchecked")
		List<Record> reList = Db.use("default").find((String) maps.get("selectSql") + (String) maps.get("extrasql"), ((List<String>) maps.get("sqlStr")).toArray());
		return reList;

	}
	
	/**
	 * 客户强制分配保存
	 */
	public void distrSave(Map<String, Object> map){
		String user_id = (String)map.get("user_id");
		String user_name = (String)map.get("user_name");
		
		String id = (String)map.get("id");
		String appealid = (String)map.get("appealid");
		String customercode = (String)map.get("customercode");
		String mgr_ids = (String)map.get("mgr_ids");
		String mgr_names = (String)map.get("mgr_names");
		String claim_props = (String)map.get("claim_props");
		
		String[] idArr = mgr_ids.split(",");
		String[] nameArr = mgr_names.split(",");
		String[] propArr = claim_props.split(",");
		String result = "";
		String cust_stat = "";
		if(idArr!=null&&idArr.length>0){
			//强制分配删除客户原先被认领的数据
			Db.use("default").update(" update pccm_cust_claim set del_stat='1',del_time=to_char(sysdate,'yyyyMMddhh24miss') where cust_pool_id = ?", new Object[] {id});
			for(int i=0;i<idArr.length;i++){
				result+=nameArr[i]+"-"+propArr[i]+"%;";
				//查询客户来源
				String incflg = Db.use("default").queryStr(" select incflg from pccm_cust_pool where id = ? ",new Object[]{id});
				//插入认领的数据
				Db.use("default").update(" insert into pccm_cust_claim(id,cust_no,claim_prop,claim_cust_mgr_id,"
						+"claim_cust_mgr_name,claim_time,del_stat,cust_pool_id,incflg) values(?,?,?,?,?,to_char(sysdate,'yyyyMMddhh24miss'),'0',?,?) " ,
						new Object[] { AppUtils.getStringSeq(),customercode,propArr[i],idArr[i],nameArr[i],id,incflg });
			}
			//修改客户状态
			cust_stat = claimDbUtil.claimStat(id,null);
			claimDbUtil.updateClaimStat(id, cust_stat);
			//修改申诉状态
			Db.use("default").update("update pccm_cust_appeal set appeal_stat=2,appeal_result=?,proc_per_id=?,proc_per_name=?,"
					+"proc_time=to_char(sysdate,'yyyyMMddhh24miss') where cust_pool_id=? and id=? " , new Object[] { result,user_id,user_name,id,appealid });
		}
	}
	
	/**
	 * 客户驳回保存
	 */
	public void reject(String appealId){
		if (AppUtils.StringUtil(appealId) != null) {
			Db.use("default").update(" update pccm_cust_appeal set appeal_stat='3' where id=? " , new Object[] { appealId });
		}
	}
	
	/**
	 * 详情查询
	 */
	public Record getDetail(String id){
		String sql = " select c.*,p.customercode,p.customername name,p.id "
				+" from pccm_cust_pool p "
				+" left join ( select cust_pool_id, "
				+" to_char(wm_concat(claim_prop)) claim_prop, "
				+" to_char(wm_concat(claim_cust_mgr_id)) claim_cust_mgr_id, "
				+" to_char(wm_concat(claim_cust_mgr_name)) claim_cust_mgr_name "
				+" from pccm_cust_claim where del_stat='0' "
				+" group by cust_pool_id) c "
				+" on c.cust_pool_id = p.id "
				+" where 1=1 ";
		StringBuffer sb = new StringBuffer();
		List<String> listStr = new ArrayList<String>();

		if (AppUtils.StringUtil(id) != null) {
			sb.append(" and p.id = ? ");
			listStr.add(id.trim());
		}
		List<Record> list = Db.use("default").find(sql + sb.toString(), listStr.toArray());
		return list.get(0);
	}
	
	/**
	 * 用户当前组织成员下拉框数据
	 */
	public List<Record> getOrgUser(String orgId){
		List<Record> list = null;
		if (AppUtils.StringUtil(orgId) != null) {
			list = Db.use("default").find(" select rownum rn,u.id,u.name from sys_user_info u "
					+" left join gcms_role_apply a on u.user_no = a.user_id "
					+" left join gcms_param_info p on p.id = a.role_id "
					+" where 1=1 and (dele_flag='0' or stat!='1') and a.apply_status='1' and u.org_id in "+orgId);
		}else{
			list = Db.use("default").find(" select rownum rn,u.id,u.name from sys_user_info u "
					+" left join gcms_role_apply a on u.user_no = a.user_id "
					+" left join gcms_param_info p on p.id = a.role_id "
					+" where 1=1 and (dele_flag='0' or stat!='1') and a.apply_status='1' ");
		}
		return list;
	}
	
	/**
	 * 查询客户是否处于申诉状态
	 */
	public Integer getAppNum(String poolId){
		int num = 0;
		List<Record> list = Db.use("default").find("  select count(1) appeal_num from pccm_cust_appeal where appeal_stat ='1' and cust_pool_id=?  ", new Object[] { poolId });
		if(null!=list&&list.size()>0){
			num = list.get(0).getBigDecimal("APPEAL_NUM").intValue();
		}
		return num;
	}
}
