package com.goodcol.server.zxglserver;

import java.util.List;

import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.zxgldbutil.UserDBUtil;

public class UserServer {
	private UserDBUtil userDBUtil = new UserDBUtil();

	/**
	 * 查询用户角色 2018年5月3日14:38:41
	 * 
	 * @author liutao
	 * @return
	 */
	public List<Record> userRoleList(String id) {

		return userDBUtil.userRoleList(id);
	}

	/**
	 * 查询待分配角色 2018年5月3日14:38:51
	 * 
	 * @author liutao
	 */
	public List<Record> distributeRoleList(String id) {
		return userDBUtil.distributeRoleList(id);
	}

	/**
	 * 更新用户角色
	 * 
	 * @param uid
	 *            员工id
	 * @param roleIds
	 *            多个角色id
	 * @throws Exception
	 */
	public boolean updateUserRole(String uid, String roleIds) throws Exception {
		return userDBUtil.updateUserRole(uid, roleIds);
	}

	/**
	 * 保存当前用户的默认角色 2018年7月25日14:40:35
	 * 
	 * @author liutao
	 * @param userId
	 *            用户id
	 * @param roleId
	 *            角色id
	 * @return
	 */
	public boolean saveDefaultRole(String userId, String roleId) {
		return userDBUtil.saveDefaultRole(userId, roleId);
	}

	/**
	 * 查询用户角色信息 2018年7月25日15:10:22
	 * 
	 * @author liutao
	 * @param uId
	 * @return
	 */
	public Record getUserRoleInfo(String uId) {
		return userDBUtil.getUserRoleInfo(uId);
	}

	/**
	 * 查找中心支行领导
	 * 
	 * @return
	 */
	public List<Record> findSubLeaders() {
		return userDBUtil.findSubLeaders();
	}

	/**
	 * 查找三级池超期未认领的客户
	 * 
	 * @return
	 */
	public List<Record> findCustByOver() {
		return userDBUtil.findCustByOver();
	}

	/**
	 * 取中心支行领导,该客户推送给中心支行领导
	 * 
	 * @param record
	 * @param leader
	 */
	public void saveCustClaim(Record record, Record leader) {
		userDBUtil.saveCustClaim(record, leader);

	}

	/**
	 * 在二级池超期的客户入三级池
	 */
	public void updateThirdPool() {
		userDBUtil.updateThirdPool();

	}
}
