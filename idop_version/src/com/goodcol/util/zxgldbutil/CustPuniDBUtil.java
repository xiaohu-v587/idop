package com.goodcol.util.zxgldbutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;

public class CustPuniDBUtil {
	
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
		String selectSql = " select rownum rn,c.id,o.orgname,c.cust_name, p1.remark punish_type, c.deduction, c.remark, "
				+" uc.name creater_name, to_char(c.create_time, 'yyyy-MM-dd') create_time ";
		String extrasql = " from pccm_cust_puni c "
				+" left join sys_user_info u on c.cust_id = u.id "
				+" left join sys_user_info uc on c.creater = uc.id "
				+" left join sys_org_info o on u.org_id = o.id "
				+" left join gcms_param_info p1 on c.punish_type = p1.val and p1.key = 'punish_type' ";
		StringBuffer whereSql = new StringBuffer(" where 1=1 and (c.del_stat = '0' or c.stat !='1') ");
		List<String> sqlStr = new ArrayList<String>();

		// 获取页面输入查询条件
		String org_id = (String)map.get("org_id");
		String punish_type = (String)map.get("punish_type");	
		String cust_name = (String)map.get("cust_name");	
		String creater	= (String)map.get("creater");
		String create_time	= (String)map.get("create_time");
		if (AppUtils.StringUtil(org_id) != null) {
			 whereSql.append(" and o.id=? ");
			 sqlStr.add(org_id.trim());
		}
		if (AppUtils.StringUtil(punish_type) != null) {
			whereSql.append(" and c.punish_type= ? ");
			sqlStr.add(punish_type.trim());
		}
		if (AppUtils.StringUtil(cust_name) != null) {
			whereSql.append(" and c.cust_name like ? ");
			sqlStr.add("%" + cust_name.trim() + "%");
		}
		if (AppUtils.StringUtil(creater) != null) {
			whereSql.append(" and uc.name like ? ");
			sqlStr.add("%" + creater.trim() + "%");
		}
		if (AppUtils.StringUtil(create_time) != null) {
			whereSql.append(" and to_char(create_time,'yyyy-MM-dd') = ? ");
			sqlStr.add(create_time.trim());
		}

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
		String customercode = (String)map.get("customercode");
		String mgr_ids = (String)map.get("mgr_ids");
		String mgr_names = (String)map.get("mgr_names");
		String claim_props = (String)map.get("claim_props");
		
		String[] idArr = mgr_ids.split(",");
		String[] nameArr = mgr_names.split(",");
		String[] propArr = claim_props.split(",");
		if(idArr!=null&&idArr.length>0){
			Db.use("default").update(" update pccm_cust_claim set del_stat='1',del_time=to_char(sysdate,'yyyyMMddhh24miss') where cust_pool_id = ?", new Object[] {id});
			for(int i=0;i<idArr.length;i++){
				//插入认领的数据
				Db.use("default").update(" insert into pccm_cust_claim(id,cust_no,claim_prop,claim_cust_mgr_id,"
						+"claim_cust_mgr_name,claim_time,del_stat,cust_pool_id) values(?,?,?,?,?,to_char(sysdate,'yyyyMMdd'),'0',?) " ,
						new Object[] { AppUtils.getStringSeq(),customercode,propArr[i],idArr[i],nameArr[i],id });
			}
			//修改客户状态
			Db.use("default").update("update pccm_cust_pool set cussts=3,all_claim_time=to_char(sysdate,'yyyyMMdd') where id=?" , new Object[] { id });
			//修改申诉状态
			Db.use("default").update("update pccm_cust_appeal set appeal_stat=2,proc_per_id=?,proc_per_name=?,"
					+"proc_time=to_char(sysdate,'yyyyMMdd') where cust_pool_id=?" , new Object[] { user_id,user_name,id });
		}
	}
	
	/**
	 * 客户驳回保存
	 */
	public void reject(String appealId){
		if (AppUtils.StringUtil(appealId) != null) {
			Db.use("default").update(" update pccm_cust_appeal set appeal_stat='2' where id=? " , new Object[] { appealId });
		}
	}
	
	/**
	 * 详情查询
	 */
	public Record getDetail(String id){
		String sql = " select c.*,p.customercode,p.shortname name,p.id "
				+" from pccm_cust_pool p "
				+" left join ( select cust_no, "
				+" to_char(wm_concat(claim_prop)) claim_prop, "
				+" to_char(wm_concat(claim_cust_mgr_id)) claim_cust_mgr_id, "
				+" to_char(wm_concat(claim_cust_mgr_name)) claim_cust_mgr_name "
				+" from pccm_cust_claim where del_stat='0' "
				+" group by cust_no) c "
				+" on c.cust_no = p.customercode "
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
		List<Record> list = Db.use("default").find(" select u.id,u.name from sys_user_info u "
				+" left join gcms_role_apply a on u.user_no = a.user_id "
				+" left join gcms_param_info p on p.id = a.role_id "
				+" where 1=1 and (dele_flag='0' or stat!='1') and a.apply_status='1' and u.org_id in "+orgId);
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
