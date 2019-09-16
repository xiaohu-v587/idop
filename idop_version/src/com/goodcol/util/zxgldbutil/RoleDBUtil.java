package com.goodcol.util.zxgldbutil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;

public class RoleDBUtil {
	private final static String DEFAULT = "default";
	/**
	 * 获取字典数据中角色列表
	 * 2018年5月4日14:41:26
	 * @author liutao
	 * @return
	 */
	public List<Record> getAllRoleList() {
		String sql = "select a.id as id, a.name as name from sys_role_info a";
		List<Record> roles = Db.find(sql);
		return roles;
	}
	
	/**
	 * 保存用户申请角色
	 * 2018年5月9日17:34:50
	 * @author liutao
	 * @param user_id 用户id
	 * @param role_id 字典表角色id
	 * @param nick_name 用户昵称
	 * @param head_url 头像地址
	 */
	public boolean saveApplyRole(String user_id, String role_id, 
			String nick_name, String head_url, String orgId, String phone){
		//String delSql = "delete from gcms_role_apply where user_id = ? and role_id = ?";
		//Db.update(delSql, user_id, role_id);
		String findSql = "select count(1) as num from gcms_role_apply where user_id = ? ";
		Record r = Db.findFirst(findSql, user_id);
		int num = 0;
		if(r != null && r.getBigDecimal("num").intValue() > 0){
			//执行修改
			String updSql = "update gcms_role_apply set org_id = ?, nick_name = ?, " 
					+ "head_url = ? where user_id = ? ";
			num = Db.update(updSql, orgId, nick_name, head_url, user_id);
		}else{
			//执行新增
			String time = DateTimeUtil.getNowDate();
			String sql = "insert into gcms_role_apply (id, user_id, role_id, " +
					"applicant_time, nick_name, head_url, org_id) values (?, ?, ?, ?, ?, ?, ?)";
			num = Db.update(sql, AppUtils.getStringSeq(), user_id,  role_id, time, nick_name, head_url, orgId);
		}
		if(num > 0){
			String sql = "update sys_user_info set phone = ? where id = ?";
			Db.update(sql, phone, user_id);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 获取所有的申请角色列表
	 * 2018年5月10日14:22:27
	 * @author liutao
	 */
	public Page<Record> getApplyRoleList(int pageNum, int pageSize, String name, String applyStatus){
		String selSql = "select gra.id as id, sui.name as name, gpi.name as roleName, gra.apply_status as status, " +
				"soi.orgname as orgname, case gra.apply_status when '-1' then '未审核' " +
				"when '0' then '未通过' when '1' then '通过' else '未审核' end apply_status ";
		String fromSql = "from gcms_role_apply gra left join sys_user_info sui " +
				"on sui.user_no = gra.user_id left join sys_org_info soi on gra.org_id = soi.id " +
				"left join gcms_param_info gpi on gpi.id = gra.role_id where 1 = 1 ";
		List<String> params = new ArrayList<>();
		if(StringUtils.isNotBlank(name)){
			fromSql += " and sui.name like ?";
			params.add("%"+ name +"%");
		}
		if(StringUtils.isNotBlank(applyStatus)){
			fromSql += " and gra.apply_status = ?";
			params.add(applyStatus);
		}
		Page<Record> r = Db.paginate(pageNum, pageSize, selSql, fromSql, params.toArray());
		return r;
	}
	
	/**
	 * 保存角色申请的审核结果
	 * 2018年5月10日18:58:16
	 * @author liutao
	 */
	public boolean saveApproval(String ids, String applyStatus, String approvalState){
		String ids1 = ids;
		ids = ids.replace(",", "','");
		String sql = "update gcms_role_apply set apply_status = ?, approval_state = ? where 1 = 1 ";
		if(StringUtils.isNotBlank(ids)){
			sql += " and id in ('"+ ids +"')";
		}
		int updNum = Db.update(sql, applyStatus, approvalState);
		if(updNum > 0){
			if("1".equals(applyStatus)){//通过审核的进行机构修改
				sql = "select user_id, org_id from gcms_role_apply where id = ? ";
				String[] idsArr = ids1.split(",");
				for (String id : idsArr) {
					Record r = Db.use(DEFAULT).findFirst(sql, id);
					//对用户进行机构修改
					sql = "update sys_user_info set org_id = ? where id = ? ";
					Db.use(DEFAULT).update(sql, r.getStr("org_id"), r.getStr("user_id"));
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 查询所有角色(首次登录系统可申请的角色)
	 * 2018年6月14日10:40:39
	 * @author liutao
	 */
	public Page<Record> applyRoleList(int pageNum, int pageSize, String name) {
		String selSql = "select par.id, par.role_id, par.spec_level, par.trans_stand, gpi1.name as role_name, "
				+ "par.promote_stand, par.next_value, par.modify_time, gpi.name as spec_level_name ";
		String fromSql = "from pccm_apply_role par " 
				+ "left join gcms_param_info gpi on gpi.val = par.spec_level and gpi.key = 'ZJ' "
				+ "left join gcms_param_info gpi1 on gpi1.id = par.role_id and gpi1.key = 'role' "
				+ "where 1=1 ";
		List<String> params = new ArrayList<>();
		if(StringUtils.isNotBlank(name)){
			fromSql += " and gpi1.name like ?";
			params.add("%"+ name +"%");
		}
		Page<Record> r = Db.paginate(pageNum, pageSize, selSql, fromSql, params.toArray());
		return r;
	}
	
	/**
	 * 查询角色信息
	 * 2018年6月14日15:50:22
	 * @author liutao
	 */
	public Record findRoleInfoById(String id){
		String sql = "select par.id, gpi.name as name, par.spec_level, par.trans_stand,"
				+ "par.promote_stand, par.next_value, par.modify_time "
				+ "from pccm_apply_role par " 
				+ "left join gcms_param_info gpi on gpi.id = par.role_id and gpi.key = 'role' "
				+ "where par.id = ?";
		return Db.use(DEFAULT).findFirst(sql, id);
	}
	
	/**
	 * 保存角色信息
	 * 2018年6月14日16:13:39
	 * @author liutao
	 */
	public boolean saveApplyRoleInfo(Record r){
		return Db.use(DEFAULT).update("pccm_apply_role", "id", r);
	}
	
	/**
	 * 进行首次登录系统时取消角色申请记录保存
	 * 2018年7月3日11:22:34
	 * @author liutao
	 * @param user_id
	 * @return
	 */
	public boolean cancel(String user_id){
		String delSql = "delete from gcms_role_apply where user_id = ?";
		Db.use(DEFAULT).update(delSql, user_id);
		//查询当前人员是否是领导角色，如果有则默认角色为大公司客户经理
		String roleSql = "select count(1) as num from sys_user_role sur "
				+ "left join sys_role_info sri on sri.id = sur.role_id "
				+ "where sur.user_id = ? and sri.name like '领导%'";
		Record r1 = Db.use(DEFAULT).findFirst(roleSql, user_id);
		BigDecimal num = r1.getBigDecimal("num");
		int insertNum = 0;
		if(num.intValue() > 0){
			String sql = "select gpi.id from gcms_param_info gpi where gpi.key = 'role' and gpi.val = '1'";
			Record roleRec = Db.use(DEFAULT).findFirst(sql);
			String roleId = roleRec.getStr("id");
			sql = "insert into gcms_role_apply (id, user_id, role_id) values (?, ?, ?)";
			insertNum = Db.use(DEFAULT).update(sql, AppUtils.getStringSeq(), user_id, roleId);
		}else{
			String sql = "insert into gcms_role_apply (id, user_id) values (?, ?)";
			insertNum = Db.use(DEFAULT).update(sql, AppUtils.getStringSeq(), user_id);
		}
		if(insertNum > 0){
			return true;
		}else{
			return false;
		}
	}
}
