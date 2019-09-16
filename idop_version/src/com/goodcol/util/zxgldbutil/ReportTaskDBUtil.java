package com.goodcol.util.zxgldbutil;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.ParamContainer;
import com.goodcol.util.ext.anatation.LogBind;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ReportTaskDBUtil {

	public static Logger log = Logger.getLogger(ReportTaskDBUtil.class);
	private static final String DEFAULE = "default";

	/**
	 * 根据条件查询报表任务列表
	 */
	@LogBind(menuname = "报表管理-手工报表", type = "3", remark = "手工报表列表-查询")
	public Page<Record> getListReportSql(Map<String, Object> map) {
		// 获取查询参数
		int pageNum = (Integer) map.get("pageNum");
		int pageSize = (Integer) map.get("pageSize");
		String userNo = (String) map.get("user_no");// 获取当前用户信息
		// 获取页面输入查询条件
		String task_name = (String) map.get("name");// 任务名
		String endTime = (String) map.get("end_time");// 任务截止时间
		String frequency = (String) map.get("rate");// 报送频次
		String status = (String) map.get("status");// 任务状态
		String selectSql = " select a.id,a.task_name,a.task_issuer_id,to_date(a.task_startdate,'yyyymmddhh24miss') as task_startdate,"
				+ " to_date(a.task_enddate,'yyyymmddhh24miss') as task_enddate,task_create_date,task_table_name,"
				+ " a.task_status, c.name as task_status_name, a.task_detail, a.task_frequency,a.task_fillway,b.name task_frequency_name,"
				+ " case when(select count(1) from pccm_user_report_task ut where ut.report_id = a.id) <>0 then "
				+ " (select count(1) from pccm_user_report_task ut where ut.task_status between 3 and 4 and ut.report_id = a.id and ut.parent_id is null)/"
				+ " (select count(1) from pccm_user_report_task ut where ut.report_id = a.id and ut.parent_id is null) else 0 end as rate ";
		String extrasql = " from PCCM_REPORT_TASK a "
				+ " left join GCMS_PARAM_INFO b on a.task_frequency = b.val and b.key='RATE_TYPE'  "
				+ " left join GCMS_PARAM_INFO c on a.task_status = c.val and c.key='report_task_status'";
		StringBuffer whereSql = new StringBuffer(" where 1 = 1 ");
		List<String> sqlStr = new ArrayList<String>();
		if (AppUtils.StringUtil(userNo) != null) {
			whereSql.append(" and  task_issuer_id = ? ");
			sqlStr.add(userNo.trim());
		}
		if (AppUtils.StringUtil(task_name) != null) {
			whereSql.append(" and task_name like ? ");
			sqlStr.add("%" + task_name.trim() + "%");
		}
		if (AppUtils.StringUtil(endTime) != null) {
//			String[] sp = endTime.toString().split(" ");
//			String[] sp2 = sp[0].toString().split("-");
//			String endDate = sp2[0] + sp2[1] + sp2[2];
			whereSql.append(" and task_enddate like ? ");
			sqlStr.add(endTime.trim() + "%");
		}
		if (AppUtils.StringUtil(frequency) != null) {
			whereSql.append(" and task_frequency = ? ");
			sqlStr.add(frequency.trim());
		}
		if (AppUtils.StringUtil(status) != null) {
			whereSql.append(" and task_status = ? ");
			sqlStr.add(status.trim());
		}
		whereSql.append(" order by task_status asc, task_create_date desc ");

		extrasql += whereSql.toString();
		// selectSql,extrasql,sqlStr 装入map，返回
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("selectSql", selectSql);
		map2.put("extrasql", extrasql);
		map2.put("sqlStr", sqlStr);
		// 打印日志
		log.info("organSql--map:" + map);

		Page<Record> records = Db.use(DEFAULE).paginate(pageNum, pageSize, selectSql, extrasql, sqlStr.toArray());
		return records;
	}
	
	/**
	 * 报表管理-手工报表--删除
	 */
	@LogBind(menuname = "报表管理-手工报表", type = "5", remark = "报表管理-删除")
	public void delReportTask(String id) {
//		String userNo = (String) map.get("user_no");// 获取当前用户编号
//		String id = (String) map.get("id");// 报表任务编号
		String tableName = Db.use(DEFAULE).queryStr("select task_table_name from pccm_report_task where id = ?", id);
		List<String> sql = new ArrayList<String>(); //存放SQL语句
		sql.add("delete from PCCM_REPORT_TASK where id= '" + id + "'");
		if(AppUtils.StringUtil(tableName) != null){
			sql.add("drop table " + tableName);
		}
		sql.add("delete from PCCM_USER_REPORT_TASK where REPORT_ID= '" + id + "'");
		sql.add("delete from PCCM_REPORT_FILE where REPORT_TASK_ID= '" + id + "'");
		if(sql.size() > 0){
			Db.use(DEFAULE).batch(sql, 10000);
		}
	}

	
	/**
	 * 发送对象机构树展示
	 */
	public List<Record> getOrgList() {
		String sql = "select ORGNAME,ID,UPID,ORGNUM,ORDERNUM,LINKMAN,PHONENUM,ORGADRESS from sys_org_info a where a.stat = 1 "
				+ CommonUtil.orgNameSql() + " order by ORGNUM asc";
		List<Record> list = Db.use(DEFAULE).find(sql);
		return list;
	}

	/**
	 * 发送对象人员列表
	 */
	public Page<Record> getListOrg(Map<String, Object> map) {
		// 获取查询参数
		int pageNum = (Integer) map.get("pageNum");
		int pageSize = (Integer) map.get("pageSize");
		// 获取页面输入查询条件
//		String orgNo = (String) map.get("org_id");// 当前用户所在机构
		String name = (String) map.get("name");
		String userNo = (String) map.get("user_no");
		String selectSql = " select u.id, orgname, user_no, name ";
		String extrasql = " from sys_user_info u left join sys_org_info o on u.org_id = o.id ";
		StringBuffer whereSql = new StringBuffer(" where 1 = 1 ");
		List<String> sqlStr = new ArrayList<String>();
		if (AppUtils.StringUtil(name) != null) {
			whereSql.append(" and name like ? ");
			sqlStr.add("%" + name.trim() + "%");
		}
		if (AppUtils.StringUtil(userNo) != null) {
			whereSql.append(" and user_no like ? ");
			sqlStr.add("%" + userNo.trim() + "%");
		}
		whereSql.append(" order by user_no asc");
		extrasql += whereSql.toString();
		Page<Record> records = Db.use(DEFAULE).paginate(pageNum, pageSize, selectSql, extrasql, sqlStr.toArray());
		return records;
	}
	
	/**
	 * 读取上传的附件
	 */
	public List<Record> readExcel(String filePath) {
		List<Record> list = new ArrayList<Record>();
		FileInputStream fs = null;
		try {
			Workbook wb = WorkbookFactory.create(fs = new FileInputStream(filePath));
			Sheet sheet1 = wb.getSheetAt(0);
			int rowNum = sheet1.getLastRowNum();
			int colNum = 0;
			for(int m = 0;m <= rowNum; m++){
				Row row = sheet1.getRow(m);
				if(m == 0){
					colNum = row.getPhysicalNumberOfCells();
				}
				Record record = new Record();
				for(int n = 0;n < colNum; n++){
					Cell cell = row.getCell(n);
					if(cell == null){
						continue;
					}
					Object cellValue;
					switch(cell.getCellType()){
						case Cell.CELL_TYPE_BOOLEAN:
							cellValue = cell.getBooleanCellValue() ? "true" : "false";
							break;
						case Cell.CELL_TYPE_NUMERIC:
							if(DateUtil.isCellDateFormatted(cell)){
								cellValue = DateTimeUtil.dateToString(cell.getDateCellValue());
							}else{
								cellValue = cell.getNumericCellValue();
							}
							break;
						case Cell.CELL_TYPE_FORMULA:
							try {
								cellValue = cell.getStringCellValue();
							} catch (IllegalStateException e) {
								try {
									cellValue = cell.getNumericCellValue();
								} catch (IllegalStateException e1) {
									cellValue = cell.getCellFormula();
								}
							} 
							break;
						case Cell.CELL_TYPE_STRING:
							cellValue = cell.getStringCellValue();
							break;
						case Cell.CELL_TYPE_BLANK:
							cellValue = "";
							break;
						case Cell.CELL_TYPE_ERROR:
							cellValue = "error";
							break;
						default:
							cellValue = null;
							break;
					}
					record.set("column" + (n+1), cellValue);
				}
				record.set("id", AppUtils.getStringSeq());
				list.add(record);
			}
		} catch (Exception e) {
			e.printStackTrace();
			list = new ArrayList<>();
			list.add(new Record().set("column1", "注意：读取表格出错,请检查文件"));
		} finally {
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	/**
	 * 将带有数据的模板数据插入表
	 */
	public void writeExcel(String filePath, String tableName) {
		List<Record> list = readExcel(ParamContainer.getDictName("report_file_path", "1") + filePath);
		String[] columns = list.get(0).getColumnNames();
		String col = "";
		String val = "";
		for(String column : columns){
			col += "," + column;
			val += ",?";
		}
		col = col.substring(1);
		val = val.substring(1);
		String sql = "insert into " + tableName + "(" + col + ") values (" + val + ")";
		Db.use(DEFAULE).batch(sql, col, list.subList(1, list.size()), 10000);
	}

	/**
	 * 查询手工报表——列表查看
	 */
	@LogBind(menuname = "报表管理报送详情和效率查询", type = "3", remark = "报送详情和效率查询列表-查询")
	public Page<Record> getUserReport(Map<String, Object> map) {
		// 获取查询参数
		int pageNum = (Integer) map.get("pageNum");
		int pageSize = (Integer) map.get("pageSize");
		// 获取页面输入查询条件
		String rid = (String) map.get("rid");// 任务编号
		String selectSql = " select a.id, a.task_receive_user user_id,o.id as orgid,"
				+ " case when a.task_receive_org is null then c.name else o.orgname end as name,"
				+ " to_date(a.task_submitdate,'yyyymmddhh24miss') as task_submitdate,"
				+ " to_date(b.task_startdate,'yyyymmddhh24miss') as task_startdate,"
				+ " to_date(b.task_enddate,'yyyymmddhh24miss') as task_enddate,"
				+ " (to_date(a.task_submitdate,'yyyymmddhh24miss')-to_date(b.task_startdate,'yyyymmddhh24miss')) as time_length,"
				+ " d.name as status_name, d.val as status ";
		String extrasql = " from  PCCM_USER_REPORT_TASK a left join PCCM_REPORT_TASK b on a.report_id=b.id "
				+ " left join SYS_USER_INFO c on a.task_receive_user=c.user_no "
				+ " left join SYS_ORG_INFO o on a.task_receive_org=o.orgnum "
				+ " left join GCMS_PARAM_INFO d on a.task_status = d.val and d.key='task_user_status'";
		StringBuffer whereSql = new StringBuffer(" where 1 = 1 and a.parent_id is null ");
		List<String> sqlStr = new ArrayList<String>();

		if (AppUtils.StringUtil(rid) != null) {
			whereSql.append(" and a.report_id = ? ");
			sqlStr.add(rid.trim());
		}
		whereSql.append(" order by a.task_status desc, to_date(a.task_submitdate,'yyyymmddhh24miss') desc, a.task_receive_org, a.task_receive_user ");
		extrasql += whereSql.toString();
		Page<Record> records = Db.use(DEFAULE).paginate(pageNum, pageSize, selectSql, extrasql, sqlStr.toArray());
		return records;
	}
	/**
	 * 查询手工报表——列表查看
	 */
	@LogBind(menuname = "报表管理报送详情和效率查询", type = "3", remark = "报送详情和效率查询列表-查询")
	public Page<Record> getUserReportOfsp(Map<String, Object> map) {
		// 获取查询参数
		int pageNum = (Integer) map.get("pageNum");
		int pageSize = (Integer) map.get("pageSize");
		// 获取页面输入查询条件
		String rid = (String) map.get("rid");// 任务编号
		String selectSql = " select a.id, a.task_receive_user user_id,a.task_receive_org orgid,"
				+ " case when a.task_receive_org is null then c.name else o.orgname end as name,"
				+ " to_date(a.task_submitdate,'yyyymmddhh24miss') as task_submitdate,"
				+ " to_date(b.task_startdate,'yyyymmddhh24miss') as task_startdate,"
				+ " to_date(b.task_enddate,'yyyymmddhh24miss') as task_enddate,"
				+ " (to_date(a.task_submitdate,'yyyymmddhh24miss')-to_date(b.task_startdate,'yyyymmddhh24miss')) as time_length,"
				+ " d.name as status_name, d.val as status ";
		String extrasql = " from  PCCM_USER_REPORT_TASK a left join PCCM_REPORT_TASK b on a.report_id=b.id "
				+ " left join SYS_USER_INFO c on a.task_receive_user=c.user_no "
				+ " left join SYS_ORG_INFO o on a.task_receive_org=o.orgnum "
				+ " left join GCMS_PARAM_INFO d on a.task_status = d.val and d.key='task_user_status'";
		StringBuffer whereSql = new StringBuffer(" where 1 = 1 ");
		List<String> sqlStr = new ArrayList<String>();

		if (AppUtils.StringUtil(rid) != null) {
			whereSql.append(" and a.parent_id = ? ");
			sqlStr.add(rid.trim());
		}
		whereSql.append(" order by a.task_status desc, to_date(a.task_submitdate,'yyyymmddhh24miss') desc, a.task_receive_org, a.task_receive_user ");
		extrasql += whereSql.toString();
		Page<Record> records = Db.use(DEFAULE).paginate(pageNum, pageSize, selectSql, extrasql, sqlStr.toArray());
		return records;
	}
	/**
	 * 效率查询列表
	 */
	@LogBind(menuname = "报表管理-效率查询", type = "3", remark = "效率查询列表-查询")
	public Page<Record> getTimeList(Map<String, Object> map) {
		// 获取查询参数
		int pageNum = (Integer) map.get("pageNum");
		int pageSize = (Integer) map.get("pageSize");
		// 获取页面输入查询条件
		String name = (String) map.get("name");//报表名称
		String stopTime = (String) map.get("end_time");//报送频次
		String userNo = (String) map.get("user_no");// 当前用户编号

		String selectSql = " select *";
		String extrasql = " from ( select r.id,r.task_name name,substr(r.task_startdate,0,8) as start_time,substr(r.task_enddate,0,8) as stop_time,r.task_issuer_id as user_no,r.task_status as status,"
						+ " sum(case when ur.task_submitdate is null then to_date(r.task_stop_date,'yyyymmddhh24miss') else to_date(ur.task_submitdate,'yyyymmddhh24miss') end-to_date(r.task_startdate,'yyyymmddhh24miss')) as sum_time,"
						+ " avg(case when ur.task_submitdate is null then to_date(r.task_stop_date,'yyyymmddhh24miss') else to_date(ur.task_submitdate,'yyyymmddhh24miss') end-to_date(r.task_startdate,'yyyymmddhh24miss')) as avg_time"
						+ " from PCCM_REPORT_TASK r left join pccm_user_report_task ur on r.id = ur.report_id"
						+ " where main_id is null and r.task_status = '3' group by r.id,r.task_name,r.task_startdate,r.task_enddate,r.task_issuer_id,r.task_status"
						+ " union all"
						+ " select m.main_id id,m.main_name name,substr(m.start_time,0,8) as start_time,substr(m.stop_time,0,8) as stop_time,m.user_no,m.task_status as status,"
						+ " sum(case when ur.task_submitdate is null then to_date(r.task_stop_date,'yyyymmddhh24miss') else to_date(ur.task_submitdate,'yyyymmddhh24miss') end - to_date(r.task_startdate,'yyyymmddhh24miss')) as sum_time,"
						+ " avg(case when ur.task_submitdate is null then to_date(r.task_stop_date,'yyyymmddhh24miss') else to_date(ur.task_submitdate,'yyyymmddhh24miss') end - to_date(r.task_startdate,'yyyymmddhh24miss')) as avg_time"
						+ " from pccm_main_task m left join pccm_report_task r on m.main_id = r.main_id left join pccm_user_report_task ur on r.id = ur.report_id"
						+ " where m.task_status = '1' group by m.main_id,m.main_name,m.start_time,m.stop_time,m.user_no,m.task_status) a";
		StringBuffer whereSql = new StringBuffer(" where 1 = 1 ");
		List<String> sqlStr = new ArrayList<String>();
		if (AppUtils.StringUtil(userNo) != null) {
			whereSql.append(" and a.user_no = ? ");
			sqlStr.add(userNo);
		}
		if (AppUtils.StringUtil(name) != null) {
			whereSql.append(" and instr(a.name, ? )>0 ");
			sqlStr.add(name.trim());
		}
		if (AppUtils.StringUtil(stopTime) != null) {
			whereSql.append(" and a.stop_time = ? ");
			sqlStr.add(stopTime.trim());
		}
		whereSql.append(" order by a.start_time desc");
		extrasql += whereSql.toString();
		// selectSql,extrasql,sqlStr 装入map，返回
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("selectSql", selectSql);
		map2.put("extrasql", extrasql);
		map2.put("sqlStr", sqlStr);
		// 打印日志
		log.info("organSql--map:" + map2);

		Page<Record> records = Db.use(DEFAULE).paginate(pageNum, pageSize, selectSql, extrasql, sqlStr.toArray());
		return records;
	}
	
	/**
	 * 效率查询列表
	 */
	@LogBind(menuname = "报表管理-效率查询", type = "3", remark = "效率查询列表-查询")
	public Page<Record> getTimeDetail(Map<String, Object> map) {
		// 获取查询参数
		int pageNum = (Integer) map.get("pageNum");
		int pageSize = (Integer) map.get("pageSize");
		// 获取页面输入查询条件
		String id = (String) map.get("id");//报送频次
//		String userNo = (String) map.get("user_no");// 当前用户编号

		String selectSql = " select *";
		String extrasql = " from ( select r.id,ur.task_receive_user as user_no, case when ur.task_receive_org is not null then o.orgname else u.name end as name,substr(r.task_startdate,0,8) as start_time,substr(ur.task_submitdate,0,8) as finish_time,"
						+ " (case when ur.task_submitdate is null then to_date(r.task_stop_date,'yyyymmddhh24miss') else to_date(ur.task_submitdate,'yyyymmddhh24miss') end - to_date(r.task_startdate,'yyyymmddhh24miss')) as take_time "
						+ " from PCCM_REPORT_TASK r left join pccm_user_report_task ur on r.id = ur.report_id "
						+ " left join sys_user_info u on ur.task_receive_user = u.user_no "
						+ " left join sys_org_info o on ur.task_receive_org = o.orgnum "
						+ " where main_id is null and r.task_status = 3 "
						+ " union all"
						+ " select m.main_id id,ur.task_receive_user as user_no, case when ur.task_receive_org is not null then o.orgname else u.name end as name,substr(r.task_startdate,0,8) as start_time,substr(ur.task_submitdate,0,8) as finish_time,"
						+ " (case when ur.task_submitdate is null then to_date(r.task_stop_date,'yyyymmddhh24miss') else to_date(ur.task_submitdate,'yyyymmddhh24miss') end - to_date(r.task_startdate,'yyyymmddhh24miss')) as take_time"
						+ " from pccm_main_task m left join pccm_report_task r on m.main_id = r.main_id"
						+ " left join pccm_user_report_task ur on r.id = ur.report_id left join sys_user_info u on ur.task_receive_user = u.user_no "
						+ " left join sys_org_info o on ur.task_receive_org = o.orgnum "
						+ " where m.task_status = 1) a";
		StringBuffer whereSql = new StringBuffer(" where 1=1");
		List<String> sqlStr = new ArrayList<String>();
		if (AppUtils.StringUtil(id) != null) {
			whereSql.append(" and a.id = ? ");
			sqlStr.add(id);
		}
		whereSql.append(" order by a.take_time, a.name");
		extrasql += whereSql.toString();
		// selectSql,extrasql,sqlStr 装入map，返回
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("selectSql", selectSql);
		map2.put("extrasql", extrasql);
		map2.put("sqlStr", sqlStr);
		// 打印日志
		log.info("organSql--map:" + map2);

		Page<Record> records = Db.use(DEFAULE).paginate(pageNum, pageSize, selectSql, extrasql, sqlStr.toArray());
		return records;
	}


	/**
	 * 查询群组列表
	 */
	@LogBind(menuname = "报表管理-群组管理", type = "3", remark = "群组列表-查询")
	public Page<Record> getGroupList(Map<String, Object> map) {
		// 获取查询参数
		int pageNum = (Integer) map.get("pageNum");
		int pageSize = (Integer) map.get("pageSize");
		// 获取页面输入查询条件
		String userNo = (String) map.get("user_no");// 当前用户编号
		String groupName = (String) map.get("name");// 群组名称
		
		String function_group = (String)map.get("function_group"); //限制

		String selectSql = " select id,group_name,create_userid,group_note,create_date,send_level,function_group,group_mode,flag  ";
		String extrasql = " from PCCM_GROUP_INFO ";
		StringBuffer whereSql = new StringBuffer(" where 1 = 1 ");

		List<String> sqlStr = new ArrayList<String>();
		if (AppUtils.StringUtil(userNo) != null) {
			whereSql.append(" and create_userid = ? ");
			sqlStr.add(userNo.trim());
		}
		if (AppUtils.StringUtil(groupName) != null) {
			whereSql.append(" and group_name like ? ");
			sqlStr.add("%" + groupName.trim() + "%");
		}
		if (AppUtils.StringUtil(function_group) != null) {
			String [] items = function_group.split(","); 
			whereSql.append(" and function_group in ( ");
			boolean isFir = true;
			for (String item : items) {
				if(isFir){
					isFir = false;
				}else{
					whereSql.append(",");
				}
				whereSql.append("?");
				sqlStr.add(item);
			}
			whereSql.append(")");
			
			
		}
		
		
		extrasql += whereSql.toString();
		// selectSql,extrasql,sqlStr 装入map，返回
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("selectSql", selectSql);
		map2.put("extrasql", extrasql);
		map2.put("sqlStr", sqlStr);

		Page<Record> records = Db.use(DEFAULE).paginate(pageNum, pageSize, selectSql, extrasql, sqlStr.toArray());
		if (null != records.getList() && records.getList().size() > 0) {
			for (Record record : records.getList()) {
				Record r = Db.findById("pccm_group_info", record.getStr("id"));
				if(r != null){
					if("1".equals(record.get("flag"))){
						List<Record> groupItems = getNewGroupItems(record.getStr("id"));
						if( groupItems != null){
							StringBuffer itemNames = new StringBuffer();
							boolean isFir = true;
							for (Record r1 : groupItems) {
								if(isFir){
									isFir = false;
								}else{
									itemNames.append(",");
								}
								itemNames.append(r1.getStr("itemname"));
							}
							
							record.set("itemNames", itemNames.toString());
							record.set("items", groupItems);
						}
					}else{
						Map<String, String> groupItems = getGroupItems(record.getStr("id"));
						if( groupItems != null){
							record.set("itemNames", groupItems.get("itemNames"));
							record.set("itemUserIds", groupItems.get("itemUserIds"));
							record.set("itemOrgIds", groupItems.get("itemOrgIds"));
							record.set("orgNames", groupItems.get("orgNames"));
							record.set("userNames", groupItems.get("userNames"));
							record.set("sendLevel", groupItems.get("send_level"));
						}
					}
				}
			}
		}
		return records;
	}

	/**
	 * 拼接选中的群组字段
	 */
	public Map<String, String> getGroupItems(String group_id) {
		Map<String, String> map = new HashMap<String, String>();
		String orgNames = "";
		String userNames = "";
		String orgIds = "";
		String userIds = "";
		String itemNames = "";
		if (AppUtils.StringUtil(group_id) != null) {
			String sql = "select i.*,u.name username,o.orgname,u.name||o.orgname itemname from pccm_group_item_info i "
					+ " left join sys_user_info u on i.item_no = u.id and i.item_type = '1' "
					+ " left join sys_org_info o on i.item_no = o.id and i.item_type = '0'"
					+ " where i.id='" + group_id + "'";
			List<Record> itemsList = Db.use(DEFAULE).find(sql);
			if (null != itemsList && itemsList.size() > 0) {
				for (Record item : itemsList) {
					if ("0".equals(item.getStr("item_type"))) {
						orgIds = orgIds + "," + item.getStr("item_no");
						orgNames = orgNames + "," + item.getStr("orgname");
					}
					if ("1".equals(item.getStr("item_type"))) {
						userIds = userIds + "," + item.getStr("item_no");
						userNames = userNames + "," + item.getStr("username");
					}
				}
			}
		}
		if (AppUtils.StringUtil(orgNames) != null) {
			itemNames = orgNames.replaceFirst(",", "") + userNames;
		} else {
			itemNames = userNames.replaceFirst(",", "");
		}
		map.put("itemNames", itemNames);
		map.put("itemUserIds", userIds.replaceFirst(",", ""));
		map.put("itemOrgIds", orgIds.replaceFirst(",", ""));
		map.put("orgNames", orgNames.replaceFirst(",", ""));
		map.put("userNames", userNames.replaceFirst(",", ""));
		return map;
	}
	
	/**
	 * 反馈数据模式，
	 * @param group_id
	 * @return
	 */
	public List<Record> getNewGroupItems(String group_id) {
		Map<String, String> map = new HashMap<String, String>();
		//StringBuffer itemNames = new StringBuffer();
		List<Record> result = new ArrayList<Record>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select i.id,i.item_no,i.item_type,i.user_no,i.orgnum,i.role_id,i.flag,u.name username,o.orgname,r.name rolename, u.name||o.orgname||r.name  itemname from pccm_group_item_info i ");
		sql.append(" left join sys_user_info u on i.USER_NO = u.id");
		sql.append(" left join sys_org_info o on i.ORGNUM = o.id");
		sql.append(" left join sys_role_info r on i.ROLE_ID = r.id");
		sql.append(" where i.id = ? ");
		
		result = Db.find(sql.toString(),group_id);
		/*boolean isFir = true;
		for (Record record : result) {
			if(isFir){
				isFir = false;
			}else{
				itemNames.append(",");
			}
			itemNames.append(record.getStr("itemname"));
		}	*/
		return result;
	}
	

	/**
	 * 新增群组
	 */
	@SuppressWarnings("unchecked")
	@LogBind(menuname = "报表管理-群组管理", type = "4", remark = "新增群组")
	public void saveGroup(Map<String, Object> map) {
		// 获取参数
		String userNo = (String) map.get("user_no");// 当前用户编号
		String pageType = (String) map.get("pageType");// id
		String groupId = (String) map.get("groupId");// id
		String groupName = (String) map.get("groupName");// 群组名称
		String groupNote = (String) map.get("groupNote");// 群组备注
		String itemUserIds = (String) map.get("itemUserIds");// 群组人员编号
		String itemOrgIds = (String) map.get("itemOrgIds");// 群组人员编号
		String sendLevel = (String) map.get("level");// 群组人员层级
		String orgid = (String) map.get("orgid");// 群组人员层级
		String org_id = (String) map.get("org_id");// 群组人员层级
		
		String function_group = (String) map.get("function_group"); //功能群组
		String group_mode = (String) map.get("group_mode"); //群组模式 0-机构 2-角色 3-机构和角色 4-机构和人员 5-角色和人员 6-机构角色人员
		String flag = (String) map.get("flag");	// 特殊标识，扩展方式导入的使用新模式 0-原有的，1-新的
		
		JSONArray items =  (JSONArray) map.get("items");  //在服务调取端处理好结果并返回

		if ( "add".equals(pageType) ){//AppUtils.StringUtil(groupId) == null) {// 添加群组
			groupId = AppUtils.getStringSeq();
			Db.use(DEFAULE).update("insert into pccm_group_info(id,group_name,create_userid,group_note,create_date,send_level,function_group,group_mode,flag) values(?,?,?,?,?,?,?,?,?)",
							new Object[] { groupId, groupName, userNo, groupNote, DateTimeUtil.getNowDate(),sendLevel,function_group,group_mode,flag});
			// 添加群组人员
			if("1".equals(flag)){
				saveGroupItems(groupId, group_mode, items);
			}else{
				saveGroupItems(groupId, itemUserIds, itemOrgIds);
			}
		} else {// 编辑群组
			Db.use(DEFAULE).update("update pccm_group_info set group_name=?,group_note=?, function_group=?, group_mode=? where id = ?",
							new Object[] { groupName, groupNote,function_group,group_mode, groupId});
			// 先删除群组人员
			Db.use(DEFAULE).update("delete from pccm_group_item_info where id = ? ", new Object[] { groupId });
			
			// 添加群组人员
			if("1".equals(flag)){
				saveGroupItems(groupId, group_mode, items);
			}else{
				saveGroupItems(groupId, itemUserIds, itemOrgIds);
			}
		}
	}

	/**
	 * 保存群组成员信息
	 */
	public void saveGroupItems(String groupId, String itemUserIds,
			String itemOrgIds) {
		// 添加群组人员
		if (AppUtils.StringUtil(itemUserIds) != null) {
			String[] userIdsArr = itemUserIds.split(",");
			for (int i = 0; i < userIdsArr.length; i++) {
				Db.use(DEFAULE).update("insert into pccm_group_item_info(id,item_no,item_type) values(?,?,?)",
								new Object[] { groupId, userIdsArr[i], "1" });
			}
		}
		// 添加群组机构
		if (AppUtils.StringUtil(itemOrgIds) != null) {
			String[] orgIdsArr = itemOrgIds.split(",");
			for (int i = 0; i < orgIdsArr.length; i++) {
				Db.use(DEFAULE).update("insert into pccm_group_item_info(id,item_no,item_type) values(?,?,?)",
								new Object[] { groupId, orgIdsArr[i], "0" });
			}
		}
	}
	
	/**
	 * 保存群组成员信息
	 * @param groupId 群组id
	 * @param type 分类 {"val":"0","remark":"机构"},{"val":"2","remark":"角色"},{"val":"3","remark":"机构和角色"},{"val":"4","remark":"机构人员"},{"val":"5","remark":"角色人员"},{"val":"6","remark":"人员"}]
	 * @param items 数据体内容[{user_no:"ssss",orgnum:"ss",role_id:"sss"}]
	 */
	public void saveGroupItems(String groupId,String type,JSONArray items) {
		JSONObject item ;
		for (int i = 0,len = items.size(); i < len; i++) {
			item = (JSONObject) items.get(i);
			Db.use(DEFAULE).update("insert into pccm_group_item_info(id,item_no,item_type,user_no,orgnum,role_id,flag) values(?,?,?,?,?,?,'1')",new Object[] { groupId,"",type,item.get("user_no"), item.get("orgnum"),item.get("role_id")});
		}
	}
	
	/**
	 * 保存群组成员信息
	 */
	public void saveGroupItems(String groupId, String itemUserIds,
			String itemOrgIds,String level,String orgid,String org_id) {
		String sql = "";
		
		itemOrgIds = "";
		if("2".equals(level)){//此时为分行
			sql ="select * from sys_org_info t where t.by2='2' and id not in ('001000000','X00000000','021000000')";
			List<Record> list = Db.find(sql);
			for(Record r:list){
				itemOrgIds +=r.getStr("id")+",";
			}
		}else if("3".equals(level)){//此时为支行
			if(StringUtils.isEmpty(org_id)){
				org_id = orgid;
			}
			sql ="select * from sys_org_info t where t.by2='3' and t.by5 not like '%021000000%' and  t.by5 not like '%X00000000%' and  t.by5 not like '%001000000%' and t.by5 like '%"+org_id+"%'";
			List<Record> list = Db.find(sql);
			for(Record r:list){
				itemOrgIds +=r.getStr("id")+",";
			}
		}else if("4".equals(level)){//此时为支行
			if(StringUtils.isEmpty(org_id)){
				org_id = orgid;
			}
			sql ="select * from sys_org_info t where t.by2='4' and t.by5 not like '%021000000%' and  t.by5 not like '%X00000000%' and  t.by5 not like '%001000000%' and t.by5 like '%"+org_id+"%'";
			List<Record> list = Db.find(sql);
			for(Record r:list){
				itemOrgIds +=r.getStr("id")+",";
			}
		}
		if(StringUtils.isNotEmpty(itemOrgIds)){
			itemOrgIds = itemOrgIds.substring(0,itemOrgIds.length());
		}
		// 添加群组机构
		if (AppUtils.StringUtil(itemOrgIds) != null) {
			String[] orgIdsArr = itemOrgIds.split(",");
			for (int i = 0; i < orgIdsArr.length; i++) {
				Db.use(DEFAULE).update("insert into pccm_group_item_info(id,item_no,item_type) values(?,?,?)",
								new Object[] { groupId, orgIdsArr[i], "0" });
			}
		}
	}

	/**
	 * 删除群组
	 */
	@LogBind(menuname = "报表管理-群组管理", type = "6", remark = "删除群组")
	public void deleteGroup(Map<String, Object> map) {
		// 获取参数
		String gid = (String) map.get("gid");// 群组编号
		// 删除群组人员
		Db.use(DEFAULE).update("delete from pccm_group_item_info where id = ? ", new Object[] { gid });
		// 删除群组
		Db.use(DEFAULE).update("delete from pccm_group_info where id = ? ", new Object[] { gid });
	}

	/**
	 * 查询待办任务列表
	 */
	@LogBind(menuname = "待办任务-报表报送", type = "3", remark = "报表报送-列表")
	public Page<Record> getMyReportList(Map<String, Object> map) {
		// 获取查询参数
		int pageNum = (Integer) map.get("pageNum");
		int pageSize = (Integer) map.get("pageSize");
		// 获取页面输入查询条件
		String userNo = (String) map.get("user_no");// 当前用户编号
		String orgNo = (String) map.get("org_no");// 当前用户机构
		String taskName = (String) map.get("taskName");// 任务名称
		String sendOrg = (String) map.get("sendOrg");// 发送部门编号
		String orgid = (String) map.get("org_id");// 发送部门编号


		String selectSql = " select distinct t.task_name, t.task_status status, o.orgname as issuer_org,substr(t.task_enddate,0,8) task_enddate, ut.return_reason, "
				+ " ceil(to_date(t.task_enddate,'yyyymmddhh24miss')-sysdate) as remain_day,ut.task_receive_user, ut.task_receive_org, ut.task_status,"
				+ " p.name as task_status_name, t.org_field, (select count(1) from pccm_user_report_task where parent_id = ut.id) as forward, "
				+ " t.id report_id,ut.id,t.task_create_date,t.task_detail,t.task_fillway,t.table_head,t.task_table_name,t.fields_location,t.serial_number  ";
		String extrasql = " from ("
				+ " select ut.* from pccm_user_report_task ut"
				+ " where  (ut.task_receive_org  ='"+orgid+"'  or ut.task_receive_org = '"+orgNo+"')"
//				+ " union all "
//				+ " select ut.* "
//				+ " from pccm_user_report_task ut "
//				+ " left join sys_org_info o on o.id = ut.task_receive_org or instr(o.by5,ut.task_receive_org) > 0 "
//				+ " left join sys_user_info u on u.org_id = o.id "
//				+ " left join sys_user_role ur on ur.user_id = u.id "
//				+ " left join sys_role_info r on r.id = ur.role_id "
//				+ " where "
////				+ " instr(r.name, ( select case when o.by2=2 then '报表管理员-二级分行'  when o.by2=3 then '报表管理员-中心支行'  when o.by2=4 then '报表管理员-责任中心' end from sys_org_info o where o.orgnum = ut.task_receive_org )"
////				+ " ) > 0 and"
//				+ "  u.id = ? and (ut.task_receive_user = u.id or ut.task_receive_user is null)"
				+ " and ut.report_id not in( select ut.report_id from pccm_user_report_task ut where ut.task_receive_org is null )"
				+ " ) ut "
				+ " left join pccm_report_task t on t.id = ut.report_id "
				+ " left join sys_user_info u on u.id = t.task_issuer_id "
				+ " left join sys_org_info o on o.id = u.org_id "
				+ " left join GCMS_PARAM_INFO p on ut.task_status = p.val and p.key='task_user_status'";
		StringBuffer whereSql = new StringBuffer(" where 1 = 1 and t.task_status between 2 and 3 ");
		List<String> sqlStr = new ArrayList<String>();
		
		if (AppUtils.StringUtil(taskName) != null) {
			whereSql.append(" and task_name like ? ");
			sqlStr.add("%" + taskName.trim() + "%");
		}
		if (AppUtils.StringUtil(sendOrg) != null) {
			whereSql.append(" and task_receive_org  = ? ");
			sqlStr.add(sendOrg.trim());
		}
		whereSql.append(" order by t.task_status asc, ut.task_status asc, t.task_create_date desc");
		extrasql += whereSql.toString();
		// selectSql,extrasql,sqlStr 装入map，返回
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("selectSql", selectSql);
		map2.put("extrasql", extrasql);
		map2.put("sqlStr", sqlStr);
		// 打印日志
		log.info("organSql--map:" + map2);

		Page<Record> records = Db.use(DEFAULE).paginate(pageNum, pageSize, selectSql, extrasql, sqlStr.toArray());
		return records;
	}

	/**
	 * 管理员报表任务认领
	 */
	@LogBind(menuname = "待办任务-报表报送", type = "6", remark = "管理员报表任务-认领")
	public void adminReportClaim(Map<String, Object> map) {
		String userNo = (String) map.get("user_no");// 获取当前用户编号
		String orgNo = (String) map.get("ORG_NO");// 获取当前用户所属机构
		String rid = (String) map.get("rid");// 报表任务编号

		Db.use(DEFAULE).update("delete from PCCM_USER_REPORT_TASK where id = ? and task_receive_id in (select user_no from SYS_USER_INFO where ORG_ID = ? and ????=????) and task_receive_id != ?",
						new Object[] { rid, orgNo, userNo });
	}

	/**
	 * 保存新建的手工报表
	 */
	@LogBind(menuname = "报表管理-手工报表", type = "4", remark = "报表管理-保存新建手工报表")
	public void saveReportTask(Map<String, Object> map) {
		// 获取页面输入的参数
		String taskName = (String) map.get("tname");// 任务名
		String issuerId = (String) map.get("user_no");// 当前用户编号
		String endDate = (String) map.get("end_time");// 任务截止日期
		String taskDetail = (String) map.get("detail");// 任务详情
		String rate = (String) map.get("rate");// 报送频次
		String fillway = (String) map.get("fillway");// 填报方式
		String zdmc = (String) map.get("zdmc");// 字段名称
		String zdlx = (String) map.get("zdlx");// 字段类型
		String xlpz = (String) map.get("xlpz");// 下拉配置
		String orgField = (String) map.get("org_field");// 机构号关联字段
		String filePath = (String) map.get("file_path");// 模板文件路径
		String fileName = (String) map.get("file_name");// 模板文件路径
		String groupId = (String) map.get("groupId");// 选中的群组ID
		String itemUserIds = (String) map.get("itemUserIds");// 选中的组织
		String itemOrgIds = (String) map.get("itemOrgIds");// 选中的人员
		String reportId = (String) map.get("reportId");// id
		String task_table_name = (String) map.get("task_table_name");// 报表数据库名称
		String busi_module = (String) map.get("busi_module");// 报表数据库名称

		if (AppUtils.StringUtil(reportId) == null) {
			reportId = AppUtils.getStringSeq();
		} else {
			// 删除模板信息
			Db.use(DEFAULE).update("delete from PCCM_REPORT_TASK where id= ? ", reportId );
			// 删除临时表信息
			Db.use(DEFAULE).update("drop table " + task_table_name);
			// 删除任务对象信息
			Db.use(DEFAULE).update("delete from PCCM_USER_REPORT_TASK where REPORT_ID= ? ", reportId );
		}
		// 创建手工报表数据表
		String tableName = "report_" + DateTimeUtil.getNowDate() + issuerId;
		String[] split = zdmc.toString().split(",");
		String createSql = "create table " + tableName + " ( id VARCHAR2(40),";
		for (int i = 1; i < split.length + 1; i++) {
			createSql += "column" + i + " VARCHAR2(255),";
		}
		createSql += "submit_user_id VARCHAR2(20), submit_date  VARCHAR2(14),submit_status varchar2(5) default 0 )"; // submit_user_id 数据提交人，submit_date 提交日期
		// 创建手工报表数据表
		Db.use(DEFAULE).update(createSql);
		// 0代表待发布，1代表任务已撤回，2表示任务已发布，3代表任务已结束
		String endTime = DateTimeUtil.changeDateStringAndH(endDate);
		Db.use(DEFAULE).update("insert into pccm_report_task (id, task_name, task_enddate, task_status, task_detail, table_head, task_frequency, task_fillway, org_field, task_issuer_id,"
						+ " task_table_name, task_attachments, fields_location, serial_number, group_id, task_create_date, task_file_name, busi_module,usrs,orgs) "
						+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
						new Object[] { reportId, taskName, endTime, "0", taskDetail, zdmc, rate, fillway, orgField,
								issuerId, tableName, filePath, zdlx, xlpz, groupId, DateTimeUtil.getNowDate(), fileName,busi_module,itemUserIds,itemOrgIds });
		List<String> usrs = AppUtils.StringUtil(itemUserIds) == null?null:new ArrayList<String>(Arrays.asList(itemUserIds.split(",")));
		List<String> orgs = AppUtils.StringUtil(itemOrgIds) == null?null:new ArrayList<String>(Arrays.asList(itemOrgIds.split(",")));
		// 将模板表中的数据插入创建的任务数据表
		if ("1".equals(fillway)) {
			writeExcel(filePath, tableName);
		}
		allotReport(reportId, null, groupId, orgs, usrs);// 将报表接收对象插入表中
	}

	/**
	 * 保存新建的手工报表附件
	 */
	@LogBind(menuname = "报表管理-手工报表", type = "4", remark = "报表管理-保存新建手工报表附件")
	public void saveReportFile(String reportId, String fileUrl, String fileName) {
		Db.use(DEFAULE).update(" insert into pccm_report_file (file_id, report_task_id, file_url, file_name) values(?,?,?,?)",
						new Object[] { AppUtils.getStringSeq(), reportId, fileUrl, fileName });
	}

	/**
	 * 获取手工报表附件
	 */
	public List<Record> getReportFiles(String reportId) {
		return Db.use(DEFAULE).find(" select * from PCCM_REPORT_FILE where report_task_id=?", new Object[] { reportId });
	}

	/**
	 * 获取附件详情
	 */
	public Record getFileDetail(String id){
		return Db.use(DEFAULE).findFirst(" select * from pccm_report_file where file_id = ? ", id);
	}
	
	/**
	 * 删除附件
	 */
	public void deleteReportFiles(String fileId) {
		Db.use(DEFAULE).update(" delete from PCCM_REPORT_FILE where file_id = ? ", fileId);
	}

	/**
	 * 报表管理-群组下拉框
	 */
	@LogBind(menuname = "报表管理-群组下拉框", type = "4", remark = "报表管理-群组下拉框")
	public List<Record> getGroupList(String user_no) {
		List<Record> re = null;
		if (AppUtils.StringUtil(user_no) != null) {
			re = Db.use(DEFAULE).find(" select id,group_name from pccm_group_info " +
					" where create_userid='"+ user_no +
							 "'" +
							 " order by to_number(create_date) ");
		}
		return re;
	}

	/**
	 * 获取群组成员信息
	 */
	public List<Record> getGroupItemList(String groupId) {
		List<Record> re = null;
		if (AppUtils.StringUtil(groupId) != null) {
			re = Db.use(DEFAULE).find(" select * from pccm_group_item_info where id='" + groupId + "' ");
		}
		return re;
	}
	
	/**
	 * 获取群组成员ID
	 * @param gid 群组ID
	 * @return org机构，usr用户
	 */
	public Record getGroupItem(String gid) {
		Record record = new Record();
		if (AppUtils.StringUtil(gid) != null) {
			List<String> org = Db.use(DEFAULE).query("select item_no from pccm_group_item_info where item_type='0' and id = ? ", gid);
			List<String> usr = Db.use(DEFAULE).query("select item_no from pccm_group_item_info where item_type='1' and id = ? ", gid);
			record.set("org", org);
			record.set("usr", usr);
		}
		return record;
	}

	/**
	 * 获取报表详细信息
	 */
	public Record getDetail(String reportId) {
		Record record = null;
		if (AppUtils.StringUtil(reportId) != null) {
//			record = Db.use(DEFAULE).findById("PCCM_REPORT_TASK", reportId);
			record = Db.use(DEFAULE).findFirst("select id, task_name, to_date(task_startdate,'yyyymmddhh24miss') as task_startdate, "
					+ " to_date(task_enddate,'yyyymmddhh24miss') as task_enddate, task_status, task_detail, table_head, "
					+ " task_frequency, task_fillway, org_field, task_issuer_id, task_table_name, task_attachments, "
					+ " fields_location, serial_number, loop_sign, group_id, task_create_date, task_file_name,busi_module, "
					+ " main_id from pccm_report_task where id = ? ", reportId);
			if (null != record.getStr("id")) {
				Map<String, String> taskItems = getTaskItems(record.getStr("id"));
				record.set("itemNames", taskItems.get("itemNames"));
				record.set("itemUserIds", taskItems.get("itemUserIds"));
				record.set("itemOrgIds", taskItems.get("itemOrgIds"));
				record.set("orgNames", taskItems.get("orgNames"));
				record.set("userNames", taskItems.get("userNames"));
				record.set("taskIssuerId", taskItems.get("task_issuer_id"));
			}
		}
		return record;
	}
	/**
	 * 获取报表详细信息
	 */
	public Record getDetails(String reportId) {
		Record record = null;
		if (AppUtils.StringUtil(reportId) != null) {
//			record = Db.use(DEFAULE).findById("PCCM_REPORT_TASK", reportId);
			record = Db.use(DEFAULE).findFirst("select id, task_name, to_date(task_startdate,'yyyymmddhh24miss') as task_startdate, "
					+ " to_date(task_enddate,'yyyymmddhh24miss') as task_enddate, task_status, task_detail, table_head, "
					+ " task_frequency, task_fillway, org_field, task_issuer_id, task_table_name, task_attachments, "
					+ " fields_location, serial_number, loop_sign, group_id, task_create_date, task_file_name, "
					+ " main_id from pccm_report_task where id = (select tt.report_id from pccm_user_report_task tt where id = ?) ", reportId);
			if (null != record.getStr("id")) {
				Map<String, String> taskItems = getTaskItems(record.getStr("id"));
				record.set("itemNames", taskItems.get("itemNames"));
				record.set("itemUserIds", taskItems.get("itemUserIds"));
				record.set("itemOrgIds", taskItems.get("itemOrgIds"));
				record.set("orgNames", taskItems.get("orgNames"));
				record.set("userNames", taskItems.get("userNames"));
			}
		}
		return record;
	}

	/**
	 * 任务对象中拼接字段
	 */
	public Map<String, String> getTaskItems(String report_id) {
		Map<String, String> map = new HashMap<String, String>();
		String orgNames = "";
		String userNames = "";
		String orgIds = "";
		String userIds = "";
		String itemNames = "";
		if (AppUtils.StringUtil(report_id) != null) {
			String sql = "select i.task_receive_user, i.task_receive_org,u.name username,o.orgname,u.name||o.orgname itemname "
					+ " from pccm_user_report_task i  "
					+ " left join sys_user_info u on i.task_receive_user = u.id and i.task_receive_org is null "
					+ " left join sys_org_info o on i.task_receive_org = o.id and i.task_receive_org is not null "
					+ " where i.report_id='" + report_id + "' and i.parent_id is null ";
			List<Record> itemsList = Db.use(DEFAULE).find(sql);
			if (null != itemsList && itemsList.size() > 0) {
				for (Record item : itemsList) {
					if (null != item.getStr("task_receive_org")) {
						orgIds = orgIds + "," + item.getStr("task_receive_org");
						orgNames = orgNames + "," + item.getStr("orgname");
					} else {
						userIds = userIds + "," + item.getStr("task_receive_user");
						userNames = userNames + "," + item.getStr("username");
					}
				}
			}
		}
		if (AppUtils.StringUtil(orgNames) != null) {
			itemNames = orgNames.replaceFirst(",", "") + userNames;
		} else {
			itemNames = userNames.replaceFirst(",", "");
		}
		map.put("itemNames", itemNames);
		map.put("itemUserIds", userIds.replaceFirst(",", ""));
		map.put("itemOrgIds", orgIds.replaceFirst(",", ""));
		map.put("orgNames", orgNames.replaceFirst(",", ""));
		map.put("userNames", userNames.replaceFirst(",", ""));
		return map;
	}

	/**
	 * 修改完成状态
	 */
	public void updateTaskStatus(String reportId, String status) {
		//0未发布，1召回，2进行中，3已结束
		if (AppUtils.StringUtil(reportId) != null && AppUtils.StringUtil(status) != null) {
			String sql = " update pccm_report_task set task_status = '" + status + "'";
			//召回
			if("1".equals(status)){
				sql += " , task_startdate = null ";
			}
			//发布
			if("2".equals(status)){
				sql += " , task_startdate = '" + DateTimeUtil.getNowDate() + "'";
			}
			//结束
			if("3".equals(status)){
				sql += " , task_stop_date = '" + DateTimeUtil.getNowDate() + "'";
			}
			sql += " where id = '" + reportId + "'";
			Db.use(DEFAULE).update(sql);
		}
	}

	/**
	 * 认领任务
	 */
	public void claimReportTask(String id, String user_no) {
		if (AppUtils.StringUtil(id) != null && AppUtils.StringUtil(user_no) != null) {
			Db.use(DEFAULE).update(" update pccm_user_report_task set task_receive_user = ? where id=? ", new Object[] { user_no, id });
		}
	}

	/**
	 * 个人任务状态修改
	 */
	public void updateUserStatus(String id, String status, String reason) {
		if (AppUtils.StringUtil(id) != null && AppUtils.StringUtil(status) != null) {
			List<String> sql = new ArrayList<String>(); //存放SQL语句
			String sql1 = " update pccm_user_report_task set task_status = '" + status + "'";
			if(AppUtils.StringUtil(reason) != null){
				sql1 += " ,return_reason = '" + reason + "'";
			}
			//撤回&退回
			if("1".equals(status) || "2".equals(status)){
				sql1 += " ,task_submitdate = null ";
			}
			//无需报送&报送
			if("3".equals(status) || "4".equals(status)){
				sql1 += " ,task_submitdate = '" + DateTimeUtil.getNowDate() + "'";
			}
			sql1 += " where id = '" + id + "'";
			sql.add(sql1);
			if("3".equals(status)){
				Record report = Db.use(DEFAULE).findFirst("select t.* from pccm_report_task t left join pccm_user_report_task ut on t.id = ut.report_id where ut.id= ? ", id);
				String fillway = report.get("task_fillway");
				String tableName = report.get("task_table_name");
				String uid = Db.use(DEFAULE).queryStr("select task_receive_user from pccm_user_report_task where id = ? ", id);
				if("1".equals(fillway)){
					sql.add("update " + tableName + " set submit_user_id = null, submit_date = null where submit_user_id = '" + uid + "'");
				}else if("2".equals(fillway)){
					sql.add("delete from " + tableName + " where submit_user_id = '" + uid + "'");
				}
				sql.add("delete from pccm_user_report_task where parent_id = '" + id + "' and task_receive_user = '" + uid + "'");
			}
			if(sql.size() > 0){
				Db.use(DEFAULE).batch(sql, 10000);
			}
		}
	}

	/**
	 * 获取手工表单数据
	 */
	public List<Record> getTableData(Map<String, Object> map) {
		String tableName = (String) map.get("task_table_name");
		String orgField = (String) map.get("org_field");
		String fillway = (String) map.get("fillway");
		String userNo = (String) map.get("user_no");
		String orgNum = (String) map.get("org_id");
		List<Record> re = null;
		//String level = (String) map.get("level");
		String sql = "";
		if (AppUtils.StringUtil(tableName) != null) {
			sql += " select r.* from " + tableName + " r ";
			if (AppUtils.StringUtil(orgField) != null && AppUtils.StringUtil(orgNum) != null) {	
				sql += " left join sys_org_info o on o.bancsid = r. " + orgField;
			}
			sql += " where 1 = 1 ";
//			if (AppUtils.StringUtil(userNo) != null){
//				sql += " and submit_user_id = '" + userNo + "'";
//			}
			if (AppUtils.StringUtil(orgField) != null && AppUtils.StringUtil(orgNum) != null) {
				String sqls = "select wm_concat(bancsid) as orgs  from  sys_org_info t where t.by5 like '%"+orgNum+"%' or orgnum ='"+orgNum+"'";
				String str = Db.findFirst(sqls).getStr("orgs");
				String[] strs = str.split(",");
				String sj = "";
				for (String s :strs){
					sj+="'"+s+"',";
				}
				sj=sj.substring(0,sj.length()-1);
				sql += " and o.bancsid in("+sj+")";
			}else if("1".equals(fillway)){
				sql += " or 1 = 1";
			}
//			sql += " order by submit_date asc ";
			re = Db.use(DEFAULE).find(sql);
		}
		return re;
	}

	/**
	 * 获取手工表单数据
	 */
	public List<Record> getTableDatas(Map<String, Object> map) {
		String tableName = (String) map.get("task_table_name");
		String orgField = (String) map.get("org_field");
		String fillway = (String) map.get("fillway");
		String userNo = (String) map.get("user_no");
		String orgNum = (String) map.get("org_id");
		List<Record> re = null;
		String sql = "";
		if (AppUtils.StringUtil(tableName) != null) {
			sql += " select r.* from " + tableName + " r ";
			if (AppUtils.StringUtil(orgField) != null && AppUtils.StringUtil(orgNum) != null) {	
				sql += " left join sys_org_info o on o.bancsid = r. " + orgField;
			}
			sql += " where 1 = 1 ";
			if (AppUtils.StringUtil(userNo) != null){
				sql += " and submit_user_id = '" + userNo + "'";
			}
			if (AppUtils.StringUtil(orgField) != null && AppUtils.StringUtil(orgNum) != null) {
				sql += " or instr(o.by5||','||o.orgnum,'" + orgNum + "') > 0 ";
			}else if("1".equals(fillway)){
				sql += " or 1 = 1";
			}
//			sql += " order by submit_date asc ";
			re = Db.use(DEFAULE).find(sql);
		}
		return re;
	}

	/**
	 * 保存上报数据
	 */
	@SuppressWarnings("unchecked")
	public void saveTableDatas(Map<String, Object> requestMap) {
		List<Map<String, Object>> grid = (List<Map<String, Object>>) requestMap.get("list");
		List<String> sql = new ArrayList<String>();
		String rid = (String) requestMap.get("rid");
		String tableName = Db.use(DEFAULE).queryStr("select task_table_name from pccm_report_task where id = ?", rid);
		String user_no = (String) requestMap.get("user_no");
		if (requestMap.get("list") != null && AppUtils.StringUtil(tableName) != null) {
			// 查询表字段数量
			int col_length = Db.use(DEFAULE).queryBigDecimal("select count(1) from user_tab_columns t where t.TABLE_NAME = '"
									+ tableName.toUpperCase() + "'").intValue();
			StringBuffer col = new StringBuffer("id, ");
			for (int k = 1; k <= col_length - 4; k++) {
				col.append("column" + k + ", ");
			}
			col.append("submit_user_id, submit_date");
			for (Map<String, Object> paramMap : grid) {
				StringBuffer row = new StringBuffer("");
				String id = (String) paramMap.get("id");
				if ("added".equals(paramMap.get("_state"))) {
					for (int m = 1; m <= col_length - 4; m++) {
						String cell = paramMap.get("column" + m) == null ? "" : paramMap.get("column" + m).toString();
						row.append(", '" + cell + "'");
					}
					sql.add("insert into " + tableName + " (" + col + ") values ('" + AppUtils.getStringSeq() + "'"
							+ row + ", '" + user_no + "', '" + DateTimeUtil.getNowDate() + "')");
				} else if("modified".equals(paramMap.get("_state"))) {
					for (int n = 1; n <= col_length - 4; n++) {
						if(paramMap.containsKey("column" + n)){
							String cell = paramMap.get("column" + n).toString();
							row.append("column" + n + "='" + cell + "', ");
						}
					}
					sql.add("update " + tableName + " set " + row + "submit_user_id = '" + user_no
							+ "', submit_date='" + DateTimeUtil.getNowDate() + "' where id = '" + id + "'");
				} else if("removed".equals(paramMap.get("_state"))){
					sql.add("delete from " + tableName + " where id = '" + id + "'");
				}
			}
			if(sql.size() > 0){
				Db.use(DEFAULE).batch(sql, 10000);
			}
		}
	}

	/**
	 * 转发
	 */
	public void taskTransfer(Map<String, Object> map) {
		String itemUserIds = (String) map.get("itemUserIds");// 选中的人员
		String itemOrgIds = (String) map.get("itemOrgIds");// 选中的机构
		String groupId = (String) map.get("groupId");// 选中的群组
		String id = (String) map.get("id");//接受对象任务id
		Record report = Db.use(DEFAULE).findFirst("select t.* from pccm_report_task t left join pccm_user_report_task ut on t.id = ut.report_id where ut.id= ? ", id);
		String reportId = report.getStr("id");
		String fillway = report.get("task_fillway");
		String tableName = report.get("task_table_name");
		String org_field = report.get("org_field");
		List<String> sql = new ArrayList<String>(); //存放SQL语句
		//查出已收到任务的人员和机构，已有不再派送
//		List<String> usrs = Db.use(DEFAULE).query("select task_receive_user from pccm_user_report_task where task_receive_user is not null and report_id = ? ", reportId);
//		List<String> orgs =	Db.use(DEFAULE).query("select task_receive_org from pccm_user_report_task where task_receive_user is null and report_id = ? ", reportId);
		//新增填报
//		if("2".equals(fillway)){
//			sql.add("delete from " + tableName + " where submit_user_id in ( "
//				       +"select task_receive_user from pccm_user_report_task "
//				       +"where parent_id = '" + id + "' and task_receive_user is not null ) ");
//		}
//		sql.add("delete from pccm_user_report_task where parent_id = '" + id + "'");
		List<String> usrs = AppUtils.StringUtil(itemUserIds) == null?null:new ArrayList<String>(Arrays.asList(itemUserIds.split(",")));
		List<String> orgs = AppUtils.StringUtil(itemOrgIds) == null?null:new ArrayList<String>(Arrays.asList(itemOrgIds.split(",")));
		if (AppUtils.StringUtil(groupId) != null){
			Record group = getGroupItem(groupId);
			usrs = group.get("usr");
			orgs = group.get("org");
		}
		/*找出新增和移除的子代对象，保留未变化对象的报表
		List<String> childUsrs = Db.use(DEFAULE).query("select task_receive_user from pccm_user_report_task where task_receive_org is null and parent_id = ? ", id);
		List<String> childOrgs = Db.use(DEFAULE).query("select task_receive_org from pccm_user_report_task where task_receive_org is not null and parent_id = ? ", id);
		if(childOrgs != null){
			Collection<String> removeOrg = new ArrayList<String>(childOrgs);
			if(orgs != null){
				removeOrg.removeAll(orgs);//移除的原有发送机构
			}
			for(String oid:removeOrg){
				if("1".equals(fillway)){
					sql.add("update " + tableName + " set submit_user_id = null, submit_date = null where submit_user_id = "
							+ " (select task_receive_user from pccm_user_report_task where report_id = '" + reportId + "' and task_receive_org = '" + oid + "')");
				}else if("2".equals(fillway)){
					sql.add("delete from " + tableName + " where submit_user_id = "
							+ " (select task_receive_user from pccm_user_report_task where report_id = '" + reportId + "' and task_receive_org = '" + oid + "')");
				}
				sql.add("delete from pccm_user_report_task where parent_id = '" + id + "' and task_receive_org = '" + oid + "'");
			}
		}
		if(childUsrs != null){
			Collection<String> removeUsr = new ArrayList<String>(childUsrs);
			if(usrs != null){
				removeUsr.removeAll(usrs);
			}
			for(String uid:removeUsr){
				if("1".equals(fillway)){
					sql.add("update " + tableName + " set submit_user_id = null, submit_date = null where submit_user_id = '" + uid + "'");
				}else if("2".equals(fillway)){
					sql.add("delete from " + tableName + " where submit_user_id = '" + uid + "'");
				}
				sql.add("delete from pccm_user_report_task where parent_id = '" + id + "' and task_receive_user = '" + uid + "'");
			}
		}
		if(orgs != null){
			orgs.removeAll(childOrgs);
		}
		if(usrs != null){
			usrs.removeAll(childUsrs);
		}*/
		//移除转发的后代
		if("1".equals(fillway)){
			sql.add("update " + tableName + " set submit_user_id = null, submit_date = null where submit_user_id in (select task_receive_user from pccm_user_report_task where task_receive_user is not null start with parent_id = '" + id + "' connect by prior id = parent_id )");
		}else if("2".equals(fillway)){
			sql.add("delete from " + tableName + " where submit_user_id in (select task_receive_user from pccm_user_report_task where task_receive_user is not null start with parent_id = '" + id + "' connect by prior id = parent_id )");
		}
		sql.add("delete from pccm_user_report_task where id in (select id from pccm_user_report_task start with parent_id = '" + id + "' connect by prior id = parent_id )");
		if(sql.size() > 0){
			Db.use(DEFAULE).batch(sql, 10000);
		}
		List<String> orgact = new ArrayList<String>();
		if("1".equals(fillway)&&StringUtils.isNotEmpty(org_field)&&orgs!=null){//此时补充填报，有机构关联
			List<Record> list = new ArrayList<Record>();
			String sqls ="";
			String ids = "";
			String querysql = "";
			int count =0;
			//判断当前表中是否有所分配行或者是下级所属行的数据，如果没有，则移除orgs当中的orgid
			for(String orgid:orgs){
				ids="";
				sqls = "select bancsid from sys_org_info t where t.id = '"+orgid+"' or t.by5 like '%"+orgid+"%'";//根据orgid查询出所有的下属机构id
				list = Db.find(sqls);
				for(Record l:list){
					if(StringUtils.isNotEmpty(l.getStr("bancsid")))
					ids+="'"+l.getStr("bancsid")+"',";
				}
				if(ids.length()==0){
					ids="";
				}else{
					ids = ids.substring(0,ids.length()-1);
					System.out.println(ids);
					querysql = "select * from "+tableName+" t where " +org_field+ " in ("+ids+")";
					Record r = Db.findFirst(querysql);
					if(r!=null){//说明表格中没有该机构下面的数据
						orgact.add(orgid); 
					}
				}
				
			}
		}else{
			orgact = orgs;
		}
		//分配报表任务
		allotReport(reportId, id, null, orgact, usrs);
	}
	
	/**
	 * 分配任务
	 * @param rid 任务
	 * @param pid 转发来自父ID
	 * @param gid 群组（非空时忽略orgs、usrs两个参数）
	 * @param orgs 机构
	 * @param usrs 人员
	 */
	public void allotReport(String rid, String pid, String gid, List<String> orgs, List<String> usrs){
		List<String> sql = new ArrayList<String>();
		pid = pid == null ? "" : pid;
		if (AppUtils.StringUtil(gid) != null){
			Record group = getGroupItem(gid);
			usrs = group.get("usr");
			orgs = group.get("org");
		}
		//人员
		if (usrs != null && usrs.size() > 0) {
			for(String uid: usrs){
				sql.add("insert into pccm_user_report_task (id, task_receive_user, task_receive_org, report_id, parent_id, create_date)"
						+ " values ('" + AppUtils.getStringSeq() + "', '" + uid + "', null, '" + rid + "', '" + pid + "', '" + DateTimeUtil.getNowDate() + "')");
			}
		}
		//机构
		if (orgs != null && orgs.size() > 0) {
			Record report = Db.use(DEFAULE).findFirst("select t.* from pccm_report_task t where t.id= ? ", rid);
			String fillway = report.get("task_fillway");
			String tableName = report.get("task_table_name");
			String org_field = report.get("org_field");
			List<String> orgact = new ArrayList<String>();
			if("1".equals(fillway)&&StringUtils.isNotEmpty(org_field)&&orgs!=null){//此时补充填报，有机构关联
				List<Record> list = new ArrayList<Record>();
				String sqls ="";
				String ids = "";
				String querysql = "";
				int count =0;
				//判断当前表中是否有所分配行或者是下级所属行的数据，如果没有，则移除orgs当中的orgid
				for(String orgid:orgs){
						querysql = "select * from "+tableName+" tt where exists (select bancsid from sys_org_info t where (t.id = '"+orgid+"' or t.by5 like '%"+orgid+"%') and t.bancsid = tt."+org_field+")";
						Record r = Db.findFirst(querysql);
						if(r!=null){//说明表格中没有该机构下面的数据
							orgact.add(orgid); 
						}
					
					
				}
			}else{
				orgact = orgs;
			}
			for(String oid: orgact){
				sql.add("insert into pccm_user_report_task (id, task_receive_user, task_receive_org, report_id, parent_id, create_date)"
						+ " values ('" + AppUtils.getStringSeq() + "', null, '" + oid + "', '" + rid + "', '" + pid + "', '" + DateTimeUtil.getNowDate() + "')");
			}
		}
		if(sql.size() > 0){
			Db.use(DEFAULE).batch(sql, 10000);
		}
	}
	
	/**
	 * 获取已转发的对象
	 * @param map
	 */
	public Map<String, String> getTransfer(String id){
		List<Record> list = Db.use(DEFAULE).find("select"
						+ " ut.task_receive_user, u.name as username, ut.task_receive_org, o.orgname "
						+ " from pccm_user_report_task ut "
						+ " left join sys_user_info u on ut.task_receive_user = u.user_no "
						+ " left join sys_org_info o on ut.task_receive_org = o.orgnum "
						+ " where parent_id = ? ", id);
		String orgIds = "";
		String orgNames = "";
		String userIds = "";
		String userNames = "";
		String names = "";
		if (list != null && list.size() > 0) {
			for (Record item : list) {
				if (item.getStr("task_receive_org") != null) {
					orgIds += "," + item.getStr("task_receive_org");
					orgNames += "," + item.getStr("orgname");
				} else {
					userIds += "," + item.getStr("task_receive_user");
					userNames += "," + item.getStr("username");
				}
			}
		}
		if (AppUtils.StringUtil(orgNames) != null) {
			names = orgNames.replaceFirst(",", "") + userNames;
		} else {
			names = userNames.replaceFirst(",", "");
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("itemNames", names);
		map.put("itemUserIds", userIds.replaceFirst(",", ""));
		map.put("itemOrgIds", orgIds.replaceFirst(",", ""));
		map.put("orgNames", orgNames.replaceFirst(",", ""));
		map.put("userNames", userNames.replaceFirst(",", ""));
		return map;
	}
	
	/**
	 * 获取树形流程图
	 */
	public List<Record> getEchart(String id){
		List<Record> list = Db.use(DEFAULE).find("select t.id,null as parent_id, o.orgname as name, u.name||'/'||u.phone as value, case when t.task_status=3 then 1 else 0 end as status "
				+ " from pccm_report_task t "
				+ " left join sys_user_info u on t.task_issuer_id = u.user_no "
				+ " left join sys_org_info o on u.org_id = o.orgnum "
				+ " where t.id = ? "
				+ " union all "
				+ " select ut.id, case when parent_id is null then report_id else parent_id end as parent_id, "
				+ " o.orgname as name,u.name||'/'||u.phone as value, case when ut.task_status between 3 and 4 then 1 else 0 end as status "
				+ " from pccm_user_report_task ut "
				+ " left join sys_user_info u on ut.task_receive_user = u.user_no "
				+ " left join sys_org_info o on ut.task_receive_org = o.orgnum "
				+ " where report_id = ? ", id, id);
		return list;
	}
	
	/**
	 * 查找短信发送的IP和端口
	 * 
	 * @return
	 */
	public Map<String, Object> findMessIpAndPort() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = Db.use(DEFAULE).queryStr("select remark from gcms_param_info where key=?", new Object[] { "MESSAGE_IP" });
		String port = Db.use(DEFAULE).queryStr("select remark from gcms_param_info where key=?", new Object[] { "MESSAGE_PORT" });
		map.put("ip", ip);
		map.put("port", port);
		return map;
	}
	
	/**
	 * 催办
	 * @param rid 获取未完成人员的信息
	 * @param id 通过接受任务id催办
	 * @return
	 */
	public List<Record> remind(String rid, String id){
		String selectSql = "select * ";
		String extrasql = " from( select rn.id, rn.report_id, rn.task_status, u.name, u.user_no, u.phone "
							+ " from sys_user_info u "
							+ " left join sys_org_info o on u.org_id = o.id "
							+ " left join sys_user_role ur on ur.user_id = u.id "
							+ " left join sys_role_info r on r.id = ur.role_id "
							+ " left join ( "
									+ " select case when o.by2=2 then '报表管理员-二级分行' "
									+ " when o.by2=3 then '报表管理员-中心支行' "
									+ " when o.by2=4 then '报表管理员-责任中心' end as role_name, orgnum, ut.id, ut.report_id, ut.task_status "
									+ " from sys_org_info o left join pccm_user_report_task ut on o.orgnum = ut.task_receive_org "
									+ " where ut.task_receive_user is null "
									+ " ) rn on instr(o.by5||','||o.orgnum,rn.orgnum)>0 "
							+ " where r.name=rn.role_name "
							+ " union all "
							+ " select ut.id, ut.report_id, ut.task_status, u.name, u.user_no, u.phone "
							+ " from pccm_user_report_task ut "
							+ " left join sys_user_info u on ut.task_receive_user = u.id "
							+ " where ut.task_receive_user is not null "
						+ " ) a ";
		StringBuffer whereSql = new StringBuffer(" where 1 = 1 and a.phone is not null ");
		List<String> sqlStr = new ArrayList<String>();
		if(AppUtils.StringUtil(id) != null){
			whereSql.append(" and a.id = ? ");
			sqlStr.add(id.trim());
		}else if(AppUtils.StringUtil(rid) != null){
			whereSql.append(" and a.report_id = ? and a.task_status between 0 and 2 ");
			sqlStr.add(rid.trim());
		}
		List<Record> user = Db.use(DEFAULE).find(selectSql + extrasql + whereSql, sqlStr.toArray());
		return user;
	}
	
	/**
	 * 获取所有的用户信息，(角色移交查询所有的人进行选择移交)
	 * 2018年11月16日11:01:09
	 * @author liutao
	 */
	public Page<Record> getAllUser(Map<String, Object> map) {
		// 获取查询参数
		int pageNum = (Integer) map.get("pageNum");
		int pageSize = (Integer) map.get("pageSize");
		String currentUserNo = (String)map.get("currentUserNo");
		String sql = "select sui.user_no, sui.name, soi.orgname ";
		String fromSql = " from sys_user_info sui "
						+ " left join sys_org_info soi on soi.id = sui.org_id where 1=1 ";
		List<String> param = new ArrayList<String>();
		if(null != map.get("user_no") && StringUtils.isNotBlank((String)map.get("user_no"))){
			fromSql += " and sui.user_no = ? ";
			param.add((String)map.get("user_no"));
		}
		if(null != map.get("user_name") && StringUtils.isNotBlank((String)map.get("user_name"))){
			fromSql += " and sui.name like ? ";
			param.add("%" + (String)map.get("user_name") + "%");
		}
		fromSql += " and sui.user_no <> ? ";
		param.add(currentUserNo);
		return Db.use(DEFAULE).paginate(pageNum, pageSize, sql, fromSql, param.toArray());
	}
	
	/**
	 * 保存角色移交信息
	 * 2018年11月16日14:28:34
	 * @author liutao
	 */
	public int saveRoleTransferInfo(Map<String, Object> map){
		try {
			String user_id = (String)map.get("user_id");
			String role_id = (String)map.get("role_id");
			String currentUserNo = (String)map.get("currentUserNo");
			//1、首先给选择的用户添加上role_id角色，
			String sql1 = " select * from sys_user_role where 1=1 and user_id = ? ";
			String sql2 = sql1 + " and role_id = ? ";
			String insertRoleSql = " insert into sys_user_role(user_id,role_id) values(?, ?) ";
			String sql3 = " select * from sys_user_info sui where sui.user_no = ? ";
			String updateUserRoleSql = " update sys_user_info set role_id = ? where user_no = ? ";
			String delRoleSql = " delete sys_user_role where 1=1 and user_id = ? and role_id = ? ";
			List<Record> user_roles = Db.use(DEFAULE).find(sql2, user_id, role_id);
			if(null == user_roles || user_roles.size() == 0){
				//如果选择的这个用户没有role_id角色,则进行新增操作
				Db.use(DEFAULE).update(insertRoleSql, user_id, role_id);
				//判断选择的这个用户没有当前角色则使用新增的这个角色ID
				Record record = Db.use(DEFAULE).findFirst(sql3, user_id);
				if(null != record && StringUtils.isBlank(record.getStr("role_id"))){
					//如果选择的这个用户没有默认角色则使用新增的role_id角色
					Db.use(DEFAULE).update(updateUserRoleSql, role_id, user_id);
				}
			}
			
			//2、选择的用户设置好角色后需要删掉当前用户的role_id角色
			Db.use(DEFAULE).update(delRoleSql, currentUserNo, role_id);
			//判断删除掉这个角色后，当前用户还有没有别的角色，如果有则把默认角色随机设置一个，如果没有则新增一个普通操作员的角色，并设置为默认角色
			List<Record> currentUserRoles = Db.use(DEFAULE).find(sql1, currentUserNo);
			String roleId = "";
			if(null != currentUserRoles && currentUserRoles.size() > 0){
				//有别的角色，从已有的角色列表中随机设置一个为默认 角色
				roleId = currentUserRoles.get(0).getStr("role_id");
			}else{
				//没有别的角色，进行新增一个普通操作员的角色
				String defaultRoleSql = " select id from sys_role_info sri where 1=1 and sri.name = '普通操作人员' ";
				Record r = Db.use(DEFAULE).findFirst(defaultRoleSql);
				if(null != r && StringUtils.isNotBlank(r.getStr("id"))){
					roleId = r.getStr("id");
					Db.use(DEFAULE).update(insertRoleSql, currentUserNo, roleId);
				}
			}
			//设置当前用户的默认角色
			Db.use(DEFAULE).update(updateUserRoleSql, roleId, currentUserNo);
			
			//3、记录角色移交信息
			String insertRoleTransferRecSql = " insert into pccm_cust_role_transfer_rec(id,from_user_id,to_user_id,role_id,transfer_time) values (?, ?, ?, ?, ?) ";
			String id = AppUtils.getStringSeq();
			String transferTime = DateTimeUtil.getNowDate();
			Db.use(DEFAULE).update(insertRoleTransferRecSql, id, currentUserNo, user_id, roleId, transferTime);
			//4、修改报表用户ID
			String updateReportSql = " update pccm_report_task set task_issuer_id = ? where task_issuer_id = ? ";
			Db.use(DEFAULE).update(updateReportSql, user_id, currentUserNo);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 定时任务保存
	 * @throws Exception 
	 */
	public void saveJobTask(String rate){
		List<Record> list = null;
		String nowTime = DateTimeUtil.getNowDate();
		try {
			if(AppUtils.StringUtil(rate)!=null){
				//查询对应频次的报表任务并且跑批月份不能等于任务发布月份
				list = Db.use("default").find(" select round(to_date(substr(r.task_enddate, 1, 8), 'yyyyMMdd') - to_date(substr(r.task_startdate, 1, 8), 'yyyyMMdd')) days,"
						+"r.* from PCCM_REPORT_TASK r where r.TASK_ISSUER_STATUS='1' and r.TASK_FREQUENCY='"+rate+"' and substr(r.task_startdate, 1, 6)!= to_char(sysdate,'yyyyMM') ");
				if(null!=list&&list.size()>0){
					for(Record task:list){
						String report_id = task.getStr("id");
						String task_startdate  = task.getStr("task_startdate");
						int days = task.getBigDecimal("days").intValue();
						Map<String, String> taskTime = taskTime(task_startdate,days);
						String startTime = taskTime.get("startTime");
						String endTime = taskTime.get("endTime");
						//校验当前月份天数与任务要跑批日期的是否一致
						if(startTime.substring(4, 8).equals(nowTime.substring(4, 8))){
							//校验当前月份任务是否跑过 
							if(!checkTask(report_id,rate)){
								String head = task.getStr("table_head");
								String task_table_name = createTable(head);
								Record newTask = task;
								String id= AppUtils.getStringSeq();
								newTask.set("ID", id);
								newTask.set("TASK_STARTDATE", startTime);
								newTask.set("TASK_ENDDATE", endTime);
								newTask.set("TABLE_HEAD", head);
								//任务跑批出来的设置为单频次
								newTask.set("TASK_FREQUENCY", "0");
								newTask.set("TASK_TABLE_NAME", task_table_name);
								//任务跑批出来的设置为已发布
								newTask.set("TASK_ISSUER_STATUS", "1");
								newTask.set("PARENT_ID", report_id);
								newTask.set("TASK_CREATE_DATE", DateTimeUtil.getNowDate());
								newTask.set("TASK_STATUS", "2");//进行中
								newTask.remove("DAYS");
								Db.use("default").save("PCCM_REPORT_TASK", "ID", newTask);
								
								//保存任务对象
								saveJobTaskUser(report_id,id);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *跑批任务时间处理
	 * @throws Exception 
	 */
	public Map<String, String> taskTime(String start_time,int days) throws Exception{
		//当前时间
		String dayOfNow = DateTimeUtil.getChinaTime(); 
		//当前月份
		String monOfNow = dayOfNow.substring(5, 7);
		//任务开始时间 月份修改成当月
		StringBuilder starttimeBui = new StringBuilder(start_time);
		String startTime = starttimeBui.replace(4,6, monOfNow).toString();
		//任务结束时间=开始时间+间隔天数
		String endTime = DateTimeUtil.getAddDay(startTime.substring(0,8), days);
		Map<String, String> map = new HashMap<String, String>();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		return map;
	}
	
	/**
	 *校验任务是否跑过
	 * @throws Exception 
	 */
	public boolean checkTask(String report_id,String rate){
		boolean flag = false;
		//月初第一天
		String firstDayOfMonth = DateTimeUtil.getFirstDayOfMonth();
		//每季度第一天
		String firstDayOfQuarter = DateTimeUtil.getFirstDayOfQuarter();
		//每年第一天
		String firstDayOfYear = DateTimeUtil.getFirstDayOfYear();
		//每月最后一天
		String lastDayOfMonth = DateTimeUtil.getLastDayOfMonth();
		//每季度最后一天
		String lastDayOfQuarter = DateTimeUtil.getLastDayOfQuarter();
		//每年最后一天
		String lastDayOfYear = DateTimeUtil.getLastDayOfYear();
		String sql = "";
		if(AppUtils.StringUtil(rate)!=null&&AppUtils.StringUtil(report_id)!=null){
			sql += "select count(1) from PCCM_REPORT_TASK where PARENT_ID='"+report_id+"'";
			if("1".equals(rate)){
				sql += " and to_date(task_startdate,'yyyyMMddhh24miss')>=to_date('"+firstDayOfMonth+"','yyyy-MM-dd')"
						+" and to_date(task_startdate,'yyyyMMddhh24miss')<=to_date('"+lastDayOfMonth+"','yyyy-MM-dd')";
			}else if("2".equals(rate)){
				sql += " and to_date(task_startdate,'yyyyMMddhh24miss')>=to_date('"+firstDayOfQuarter+"','yyyy-MM-dd')"
					+" and to_date(task_startdate,'yyyyMMddhh24miss')<=to_date('"+lastDayOfQuarter+"','yyyy-MM-dd')";
			}else if("3".equals(rate)){
				sql += " and to_date(task_startdate,'yyyyMMddhh24miss')>=to_date('"+firstDayOfYear+"','yyyy-MM-dd')"
					+" and to_date(task_startdate,'yyyyMMddhh24miss')<=to_date('"+lastDayOfYear+"','yyyy-MM-dd')";
			}
			int cnt = Db.use("default").queryBigDecimal(sql).intValue();
			if(cnt>0){
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 任务对象保存
	 */
	public void saveJobTaskUser(String report_id,String id){
		List<Record> list = null;
		if(AppUtils.StringUtil(report_id)!=null&&AppUtils.StringUtil(id)!=null){
			list = Db.use("default").find(" select * from PCCM_USER_REPORT_TASK where parent_id is null and report_id='"+report_id+"' ");
			if(null!=list&&list.size()>0){
				for(Record userTask:list){
					Record newUserTask = new Record();
					newUserTask.set("ID", AppUtils.getStringSeq());
					newUserTask.set("TASK_RECEIVE_USER", userTask.get("TASK_RECEIVE_USER"));
//					newUserTask.set("TASK_ISSUER_ID", userTask.get("TASK_ISSUER_ID"));
					newUserTask.set("TASK_RECEIVE_ORG", userTask.get("TASK_RECEIVE_ORG"));
					newUserTask.set("REPORT_ID", id);
					Db.use("default").save("PCCM_USER_REPORT_TASK", "ID", newUserTask);
				}
			}
		}
	}
	
	/**
	 * 创建临时表
	 */
	public String createTable(String head){
		String[] split;
		// 创建手工报表数据表
		// int number = (int)(Math.random()*10);
		String nword = "";
		for (int i = 0; i < 5; i++) {
			char word = (char) (int) (Math.random() * 26 + 97);
			nword += word;
		}
		String tablename = "report_"+ DateTimeUtil.getNowDate() + nword;
		split = head.split(",");
		String createSql = "create table " + tablename + " ( id VARCHAR2(40),";
		for (int i = 1; i < split.length + 1; i++) {
			createSql += "column" + i + "  VARCHAR2(255),";
		}
		createSql += "submit_user_id  VARCHAR2(20), submit_date  VARCHAR2(14),sjstatus varchar2(10) default 0   )"; // submit_user_id
																					// 数据提交人，submit_date
																					// 提交日期
		// 创建手工报表数据表
		Db.use(DEFAULE).update(createSql);
		return tablename;
	}
	
}
