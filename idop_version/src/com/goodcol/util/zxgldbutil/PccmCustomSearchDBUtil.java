package com.goodcol.util.zxgldbutil;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;

/**
 * 定制查询数据层
 * 
 * @author start
 * @2018-10-22
 */
public class PccmCustomSearchDBUtil {

	private final static String CONFIG_DEFAULT = "default";

	/**
	 * 分页查询定制列表
	 * 
	 * @param paramMap
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page<Record> getList(Map<String, String> paramMap, int pageNum, int pageSize) {

		String selectSql = " SELECT BASE_ID, p.STATUS,p1.name status_name, LICENSE_STATUS, USER_NO, CREATE_TIME, DATE_FILED_NAME, ORG_FILED_NAME, TABLE_EN_NAME, TABLE_CN_NAME ";
		String fromSql = " FROM PCCM_CUSTOM_BASE_INFO P "
				+"  left join gcms_param_info p1 on p.STATUS = p1.val and p1.key = 'BDZT' where 1 = 1";
		String tableCnName = paramMap.get("tableCnName");
		String tableEnName = paramMap.get("tableEnName");
		if (StringUtils.isNotBlank(tableCnName)) {
			fromSql += (" AND TABLE_CN_NAME LIKE '%" + tableCnName + "%'");
		}
		if (StringUtils.isNotBlank(tableEnName)) {
			fromSql += (" AND TABLE_EN_NAME LIKE '%" + tableEnName + "%'");
		}
		fromSql += (" and base_pid is null ORDER BY CREATE_TIME DESC ");
		Page<Record> page = Db.use(CONFIG_DEFAULT).paginate(pageNum, pageSize, selectSql, fromSql);
		return page;
	}

	/**
	 * 添加基础模板
	 * 
	 * @param paramMap
	 */
	public void addBaseModel(Map<String, String> paramMap) {
		Record record = new Record();
		record.set("BASE_ID", paramMap.get("base_id"));
		record.set("LICENSE_STATUS", paramMap.get("license_status"));
		record.set("USER_NO", paramMap.get("user_no"));
		record.set("CREATE_TIME", DateTimeUtil.getNowDate());
		record.set("TABLE_EN_NAME", paramMap.get("table_en_name"));
		record.set("TABLE_CN_NAME", paramMap.get("table_cn_name"));
		record.set("DATE_FILED_NAME", paramMap.get("date_filed_name"));
		record.set("ORG_FILED_NAME", paramMap.get("org_filed_name"));
		record.set("BASE_SEQ_NAME", paramMap.get("base_seq_name"));
		Db.use(CONFIG_DEFAULT).save("PCCM_CUSTOM_BASE_INFO", "BASE_ID", record);
	}

	/**
	 * 删除模板
	 * 
	 * @param baseId
	 */
	public void deleteModel(String baseId) {
		if (StringUtils.isNotBlank(baseId)) {
			Db.use(CONFIG_DEFAULT).deleteById("PCCM_CUSTOM_BASE_INFO","BASE_ID", baseId);
		}
	}
	
	/**
	 * 删除模板字段
	 * 
	 * @param baseId
	 */
	public void deleteFILED(String baseId) {
		if (StringUtils.isNotBlank(baseId)) {
			Db.use(CONFIG_DEFAULT).deleteById("PCCM_CUSTOM_BASE_FILED","BASE_ID", baseId);
		}
	}
	
	/**
	 * 删除子模板模板字段
	 * 
	 * @param baseId
	 */
	public void deleteChildFILED(String baseId) {
		if (StringUtils.isNotBlank(baseId)) {
			Db.use(CONFIG_DEFAULT).deleteById("PCCM_CUSTOM_SEARCH_REL","BASE_ID", baseId);
		}
	}
	
	/**
	 * 权限机构
	 * 
	 * @param baseId
	 */
	public void deleteAuthOrgs(String baseId) {
		if (StringUtils.isNotBlank(baseId)) {
			Db.use(CONFIG_DEFAULT).deleteById("PCCM_CUSTOM_LICENSE_ORG_REL","BASE_ID", baseId);
		}
	}

	/**
	 * 更新主模板信息
	 * 
	 * @param paramMap
	 */
	public void updateBaseModel(Map<String, String> paramMap) {
		Record record = new Record();
		record.set("BASE_ID", paramMap.get("base_id"));
//		record.set("STATUS", paramMap.get("status"));
		record.set("LICENSE_STATUS", paramMap.get("license_status"));
		record.set("USER_NO", paramMap.get("userNo"));
//		record.set("CREATE_TIME", DateTimeUtil.getNowDate());
		record.set("TABLE_EN_NAME", paramMap.get("table_en_name"));
		record.set("TABLE_CN_NAME", paramMap.get("table_cn_name"));
		record.set("DATE_FILED_NAME", paramMap.get("date_filed_name"));
		record.set("ORG_FILED_NAME", paramMap.get("org_filed_name"));
		Db.use(CONFIG_DEFAULT).update("PCCM_CUSTOM_BASE_INFO", "BASE_ID", record);
	}

	/**
	 * 添加模板基础字段
	 * 
	 * @param requestMap
	 * @param baseId
	 */
	public void addBaseFileds(Map<String, Object> requestMap, String baseId) {
		if (requestMap.get("list") != null) {

			@SuppressWarnings("unchecked")
			List<Map<String, Object>> list = (List<Map<String, Object>>) requestMap.get("list");

			for (Map<String, Object> paramMap : list) {
				Record record = new Record();
				record.set("BASE_ID", baseId);
				record.set("FILED_ID", AppUtils.getStringSeq());
				record.set("FILED_EN_NAME", paramMap.get("filed_en_name"));
				record.set("FILED_CN_NAME", paramMap.get("filed_cn_name"));
				record.set("FILED_TYPE", paramMap.get("filed_type"));
				record.set("DESCRIPTION", paramMap.get("description"));
				record.set("FILED_LENGTH", paramMap.get("filed_length"));
				record.set("SORT_NUM", paramMap.get("sort_num"));
				Db.use(CONFIG_DEFAULT).save("PCCM_CUSTOM_BASE_FILED", "FILED_ID", record);
			}
		}
	}

	/**
	 * 子模板字段添加
	 * 
	 * @param baseId
	 * @param filedIds
	 */
	public void addChildModelFileds(String baseId, String filedIds) {
		if (StringUtils.isNotBlank(filedIds)&&StringUtils.isNotBlank(baseId)) {
			String str[] = filedIds.split(",");
			for (String filedId : str) {
				Record record = new Record();
				record.set("BASE_ID", baseId);
				record.set("FILED_ID", filedId);
				record.set("ID", AppUtils.getStringSeq());
				Db.use(CONFIG_DEFAULT).save("PCCM_CUSTOM_SEARCH_REL", "ID", record);
			}
		}
	}
	
	/**
	 * 查询父表明细
	 */
	public Record getModelDetail(String base_id) {
		Record re = null;
		if (StringUtils.isNotBlank(base_id)) {
			re = Db.use(CONFIG_DEFAULT).findFirst(" select * from pccm_custom_base_info where base_id =? ", new Object[]{base_id});
		}
		return re;
	}
	
	/**
	 * 查询父表字段明细
	 */
	public List<Record> getFieldDetail(String base_id) {
		List<Record> re = null;
		if (StringUtils.isNotBlank(base_id)) {
			re = Db.use(CONFIG_DEFAULT).find(" select * from pccm_custom_base_filed where base_id = ? order by to_number(nvl(sort_num,0)) ", new Object[]{base_id});
		}
		return re;
	}
	
	/**
	 * 查询子表字段明细
	 */
	public List<Record> getChildFieldDetail(String base_id,String downFlag) {
		List<Record> re = null;
		String sql ="  select f.* from pccm_custom_search_rel r " 
				+" inner join  pccm_custom_base_filed f on f.filed_id = r.filed_id "
				+" where r.base_id = ? ";
		if ("count".equals(downFlag)) {
			sql+=" and f.filed_type in ('int','decimal') ";
		}		
		sql+=" order by f.sort_num ";
		if (StringUtils.isNotBlank(base_id)) {
			re = Db.use(CONFIG_DEFAULT).find(sql, new Object[]{base_id});
		}
		return re;
	}
	
	/**
	 * 是否有子表
	 */
	public boolean isExistChildTable(String base_id) {
		boolean flag = false;
		if (StringUtils.isNotBlank(base_id)) {
			int re = Db.use(CONFIG_DEFAULT).queryBigDecimal(" select count(*) from pccm_custom_search_rel where base_id = ? ", new Object[]{base_id}).intValue();
			if(re>0){
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 更改表单状态
	 */
	public void changeStatus(String base_id,String status){
		if (StringUtils.isNotBlank(base_id)&&StringUtils.isNotBlank(status)) {
			Db.use("default").update(" update pccm_custom_base_info set status=? where base_id=? ",new Object[] { status,base_id});
		}
	}
	
	/**
	 * 查询授权机构
	 */
	public List<Record> getAuthOrgs(String base_id) {
		List<Record> re = null;
		if (StringUtils.isNotBlank(base_id)) {
			re = Db.use(CONFIG_DEFAULT).find(" select r.*,o.orgname,o.bancsid,o.id,o.upid "
					  +" from pccm_custom_license_org_rel r "
					  +" left join sys_org_info o on r.org_num = o.id where base_id = ? order by o.id ", new Object[]{base_id});
		}
		return re;
	}
	
	/**
	 * 授权
	 * 
	 */
	public void addAuthOrgs(String baseId,String orgIds,String userNo) {
		if (StringUtils.isNotBlank(baseId)&&StringUtils.isNotBlank(orgIds)) {
			String[] orgIdsArr = orgIds.split(",");
			for (String orgnum : orgIdsArr) {
				Db.use(CONFIG_DEFAULT).update(" insert into PCCM_CUSTOM_LICENSE_ORG_REL(org_num, base_id, create_time, user_no) values(?, ?, ?, ?) ",
						new Object[]{orgnum,baseId,DateTimeUtil.getNowDate(),userNo});
			}
		}
	}
	
	/**
	 * 分页查询业务定制父列表
	 * 
	 * @param paramMap
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page<Record> getBusiList(Map<String, String> paramMap, int pageNum, int pageSize) {
		String orgId = paramMap.get("orgId");
		//获取用户权限链：用户ID+所有上级ID
		String upOrgs = Db.use(CONFIG_DEFAULT).queryStr(" select id||by5 from sys_org_info where id=? ",new Object[]{orgId});
		Page<Record> page = null;
		if(StringUtils.isNotBlank(upOrgs)){
			String selectSql = " select p.base_id, p.table_en_name, p.table_cn_name,p.base_seq_name ";
			String fromSql = " from pccm_custom_base_info p "
					+"  left join pccm_custom_license_org_rel c on p.base_id = c.base_id "
					+"	where 1=1 and p.license_status='1' ";
			String tableCnName = paramMap.get("tableCnName");
			String tableEnName = paramMap.get("tableEnName");
			fromSql += (" and  instr('"+upOrgs+"', c.org_num) > 0 ");
			if (StringUtils.isNotBlank(tableCnName)) {
				fromSql += (" and table_cn_name like '%" + tableCnName + "%'");
			}
			if (StringUtils.isNotBlank(tableEnName)) {
				fromSql += (" and table_en_name like '%" + tableEnName + "%'");
			}
			fromSql += (" and p.base_pid is null order by p.create_time desc ");
			page = Db.use(CONFIG_DEFAULT).paginate(pageNum, pageSize, selectSql, fromSql);
		}
		return page;
	}
	
	/**
	 * 添加子模板
	 * 
	 * @param paramMap
	 */
	public void addChildModel(Map<String, String> paramMap) {
		Record record = new Record();
		record.set("BASE_ID", paramMap.get("base_id"));
		record.set("BASE_PID", paramMap.get("base_pid"));
		record.set("LICENSE_STATUS", paramMap.get("license_status"));
		record.set("USER_NO", paramMap.get("user_no"));
		record.set("CREATE_TIME", DateTimeUtil.getNowDate());
		record.set("TABLE_EN_NAME", paramMap.get("table_en_name"));
		record.set("TABLE_CN_NAME", paramMap.get("table_cn_name"));
		record.set("DATE_FILED_NAME", paramMap.get("date_filed_name"));
		record.set("ORG_FILED_NAME", paramMap.get("org_filed_name"));
		Db.use(CONFIG_DEFAULT).save("PCCM_CUSTOM_BASE_INFO", "BASE_ID", record);
	}
	
	/**
	 * 修改子模板
	 * 
	 * @param paramMap
	 */
	public void updateChildModel(Map<String, String> paramMap) {
		Record record = new Record();
		record.set("BASE_ID", paramMap.get("base_id"));
		record.set("LICENSE_STATUS", paramMap.get("license_status"));
		record.set("TABLE_CN_NAME", paramMap.get("table_cn_name"));
		Db.use(CONFIG_DEFAULT).update("PCCM_CUSTOM_BASE_INFO", "BASE_ID", record);
	}
	
	/**
	 * 创建序列
	 * 
	 * @param paramMap
	 */
	public void addModelSeq(String seq_name) {
		if (StringUtils.isNotBlank(seq_name)) {
			String sql=" create sequence " + seq_name
					+" minvalue 1 "
					+" maxvalue 9999999999999999999999999999 "
					+" start with 1 "
					+" increment by 1 "
					+" cache 20 ";
			Db.use(CONFIG_DEFAULT).update(sql);
		}
	}
	
	/**
	 * 分页查询业务定制子列表
	 * 
	 * @param paramMap
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page<Record> getBusiChildList(Map<String, String> paramMap, int pageNum, int pageSize) {
		String user_no = paramMap.get("user_no");
		Page<Record> page = null;
		if(StringUtils.isNotBlank(user_no)){
			String selectSql = " select p.base_id,p.base_pid, p.table_en_name, p.table_cn_name,p1.name status_name,p.status,"
					+"p.license_status,p2.table_cn_name table_cn_pname,b.filed_ids,r.org_ids ";
			String fromSql = " from pccm_custom_base_info p "
					+"  left join gcms_param_info p1 on p.STATUS = p1.val and p1.key = 'BDZT' "
					+"  left join pccm_custom_base_info p2 on p.base_pid = p2.base_id "
					+"  left join (select  base_id,to_char(wm_concat(filed_id)) filed_ids from pccm_custom_search_rel group by base_id) b "
					+"   on p.base_id = b.base_id "
					+"  left join (select  base_id,to_char(wm_concat(org_num)) org_ids from pccm_custom_license_org_rel group by base_id) r "
					+"   on p.base_id = r.base_id "
					+"	where 1=1 ";
			String tableCnName = paramMap.get("tableCnName");
			String tableEnName = paramMap.get("tableEnName");
			fromSql += (" and p.user_no='"+user_no+"' ");
			if (StringUtils.isNotBlank(tableCnName)) {
				fromSql += (" and p.table_cn_name like '%" + tableCnName + "%'");
			}
			if (StringUtils.isNotBlank(tableEnName)) {
				fromSql += (" and p.table_en_name like '%" + tableEnName + "%'");
			}
			fromSql += (" and p.base_pid is not null order by p.create_time desc ");
			page = Db.use(CONFIG_DEFAULT).paginate(pageNum, pageSize, selectSql, fromSql);
		}
		return page;
	}
	
	/**
	 * 使用人员分页查询业务定制子列表
	 * 
	 * @param paramMap
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page<Record> getSerList(Map<String, String> paramMap, int pageNum, int pageSize) {
		String orgId = paramMap.get("orgId");
		//获取用户权限链：用户ID+所有上级ID
		String upOrgs = Db.use(CONFIG_DEFAULT).queryStr(" select id||','||by5 from sys_org_info where id=? ",new Object[]{orgId});
		Page<Record> page = null;
		if(StringUtils.isNotBlank(upOrgs)){
			String selectSql = " select p.base_id, p.table_en_name, p.table_cn_name,p1.table_en_name table_en_pname, "
					+" p1.date_filed_name,p1.org_filed_name ";
			String fromSql = " from pccm_custom_base_info p "
					+"  left join pccm_custom_base_info p1 on p.base_pid = p1.base_id "
					+"  left join pccm_custom_license_org_rel c on p.base_id = c.base_id "
					+"	where 1=1 and p.license_status='1' ";
			String tableCnName = paramMap.get("tableCnName");
			String tableEnName = paramMap.get("tableEnName");
			fromSql += (" and  instr('"+upOrgs+"', c.org_num) > 0 ");
			if (StringUtils.isNotBlank(tableCnName)) {
				fromSql += (" and table_cn_name like '%" + tableCnName + "%'");
			}
			if (StringUtils.isNotBlank(tableEnName)) {
				fromSql += (" and table_en_name like '%" + tableEnName + "%'");
			}
			fromSql += (" and p.base_pid is not null order by p.create_time desc ");
			page = Db.use(CONFIG_DEFAULT).paginate(pageNum, pageSize, selectSql, fromSql);
		}
		return page;
	}
	
}	
