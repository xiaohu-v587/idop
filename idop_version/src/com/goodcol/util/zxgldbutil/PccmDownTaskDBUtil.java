package com.goodcol.util.zxgldbutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;

public class PccmDownTaskDBUtil {
	

	/**
	 * 下载任务列表
	 */
	public Page<Record> downTaskPage(Map<String, Object> map){
		// 获取查询参数
		int pageNum = (Integer)map.get("pageNum");
		int pageSize = (Integer)map.get("pageSize");
		Map<String, Object> maps = myfindSql(map);
		@SuppressWarnings("unchecked")
		Page<Record> records = Db.use("gbase").paginate(pageNum, pageSize, (String) maps.get("selectSql"), (String) maps.get("extrasql"), ((List<String>) maps.get("sqlStr")).toArray());

		return records;
	}
	

	public Map<String, Object> myfindSql(Map<String, Object> map) {
		// 获取页面输入查询条件
		String taskTxt = (String)map.get("taskTxt");
		String user_no = (String)map.get("user_no");	
		//String org_id = (String)map.get("org_id");
		String orgArr = (String)map.get("orgArr");

		String selectSql = " select j.pkid,j.job_explain,j.job_time,j.begin_time,j.end_time,g.name task_status, "
				+"	j.row_number,j.status,j.table_name_full,j.download_file_name,d.user_name job_person,d.table_head  ";
		String extrasql = " from job_list_dsql j  "
				+" left join pccm_down_task_info d on j.pkid = d.pkid "
				+" left join sys_user_info u on u.id = d.user_no "
				+" left join gcms_param_info  g on j.status = g.val and g.jkey ='task_status' ";

				
		StringBuffer whereSql = new StringBuffer(" where 1=1 ");
		List<String> sqlStr = new ArrayList<String>();

		if (AppUtils.StringUtil(taskTxt) != null) {
			whereSql.append(" and j.job_explain like '%" + taskTxt.trim() + "%' ");
		}
		if (AppUtils.StringUtil(orgArr) != null) {
			whereSql.append(" and u.org_id in "+orgArr);
		}else{
			if (AppUtils.StringUtil(user_no) != null) {
				whereSql.append(" and d.user_no =?  ");
				sqlStr.add(user_no.trim());
			}
		}
		
		//排序
		whereSql.append(" order by job_time desc ");
		extrasql += whereSql.toString();
		Map<String, Object> mapSql = new HashMap<String, Object>();
		mapSql.put("selectSql", selectSql);
		mapSql.put("extrasql", extrasql);
		mapSql.put("sqlStr", sqlStr);
		return mapSql;
	}
	
	/**
	 * 预览列表
	 */
	public List<Record> viewList(Map<String, Object> map){
		// 获取查询参数
		String view_size = (String) map.get("view_size");
		String table_name = (String)map.get("table_name");
		List<Record> lr =  Db.use("gbase").find(" select * from "+table_name+" limit " +view_size);
		return lr;
	}
	
	/**
	 * 根据PKID查找日志
	 */
	public List<Record> findById(String pkid){
		List<Record> re = null;
		if (AppUtils.StringUtil(pkid) != null) {
			re =  Db.use("gbase").find(" select * from job_list_dsql where pkid = '"+pkid+"' limit 1 ");
		}
		return re;
	}

}
