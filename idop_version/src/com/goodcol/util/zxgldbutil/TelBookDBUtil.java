package com.goodcol.util.zxgldbutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;

public class TelBookDBUtil {
	
	/**
	 * 列表
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
	
	/**
	 * 列表查询
	 */
	public List<Record> custMgrList(Map<String, Object> map) {
		Map<String, Object> maps = findSql(map);
		@SuppressWarnings("unchecked")
		List<Record> reList = Db.use("default").find((String) maps.get("selectSql") + (String) maps.get("extrasql"), ((List<String>) maps.get("sqlStr")).toArray());
		return reList;

	}
	
	public Map<String, Object> findSql(Map<String, Object> map) {
		String selectSql = " select u.id, o.orgname,u.dept_name, u.name, u.phone, u.mobile, u.remark ";
		String extrasql = " from  sys_user_info u "
				+" left join sys_org_info o on u.org_id = o.orgnum ";
		StringBuffer whereSql = new StringBuffer(" where u.dele_flag = '0' ");
		List<String> sqlStr = new ArrayList<String>();

		// 获取页面输入查询条件
		String orgArr = (String)map.get("orgArr");
		String id = (String)map.get("id");	
		String name = (String)map.get("name");	
		String mobile	= (String)map.get("mobile");
		
		if (AppUtils.StringUtil(orgArr) != null) {
			 whereSql.append(" and u.org_id in "+orgArr);
		}
		if (AppUtils.StringUtil(id) != null) {
			whereSql.append(" and u.id = '" + id+"' " );
		}
		if (AppUtils.StringUtil(name) != null) {
			whereSql.append(" and u.name like ? ");
			sqlStr.add("%" + name.trim() + "%");
		}
		if (AppUtils.StringUtil(mobile) != null) {
			whereSql.append(" and u.phone like ? ");
			sqlStr.add(mobile.trim()+ "%");
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
	
	
	
}
