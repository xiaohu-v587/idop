package com.goodcol.util.zxgldbutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;

public class PccmMyAppealDBUtil {
	
	/**
	 * 申诉列表
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
		String mgr_id = (String)map.get("mgr_id");
		// 获取页面输入查询条件
		String cust_no = (String)map.get("cust_no");
		String name = (String)map.get("name");
		String appeal_stat	= (String)map.get("appeal_stat");
		String appeal_result	= (String)map.get("appeal_result");

		String selectSql =  " select rownum          rn, "
							+"       p.id, "
							+"       p.customercode||p.dummy_cust_no  cust_no, "
							+"       p.customername  name, "
							+"       a.id            appealId, "
							+"       a.appeal_stat, "
							+"       a.appeal_result, "
							+"       a.appeal_reas, "
							+"       p1.name         appeal_stat_cn, "
							+"       p2.name         appeal_result_cn ";
		String extrasql = "  from pccm_cust_appeal a "
						+"  left join pccm_cust_pool p on a.cust_pool_id = p.id "
						+"  left join gcms_param_info p1 on a.appeal_stat = p1.val and p1.key = 'appeal_stat' "
						+"  left join gcms_param_info p2 on a.appeal_result = p2.val and p2.key = 'appeal_result' ";
		StringBuffer whereSql = new StringBuffer(" where 1=1 ");
		List<String> sqlStr = new ArrayList<String>();
		
		//默认查询条件
		
		if (AppUtils.StringUtil(cust_no) != null) {
			 whereSql.append(" and p.customercode||p.dummy_cust_no=? ");
			 sqlStr.add(cust_no.trim());
		}
		if (AppUtils.StringUtil(name) != null) {
			whereSql.append(" and p.customername like ? ");
			sqlStr.add("%" + name.trim() + "%");
		}
		if (AppUtils.StringUtil(mgr_id) != null) {
			whereSql.append(" and a.appeal_per_id=? ");
			sqlStr.add(mgr_id.trim());
		}
		
		if (AppUtils.StringUtil(appeal_stat) != null) {
			whereSql.append(" and a.appeal_stat= ? ");
			sqlStr.add(appeal_stat.trim());
		}
		
		if (AppUtils.StringUtil(appeal_result) != null) {
			whereSql.append(" and a.appeal_result= ? ");
			sqlStr.add(appeal_result.trim());
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
	
}
