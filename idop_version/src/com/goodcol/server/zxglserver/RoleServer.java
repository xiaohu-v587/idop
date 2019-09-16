package com.goodcol.server.zxglserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.zxgldbutil.RoleDBUtil;

public class RoleServer {
	private RoleDBUtil roleDBUtil = new RoleDBUtil();
	
	/**
	 * 获取字典数据中角色列表
	 * 2018年5月4日14:41:26
	 * @author liutao
	 * @return
	 */
	public List<Map<String, String>> getAllRoleList() {
		List<Map<String, String>> list = new ArrayList<>();
		List<Record> roles = roleDBUtil.getAllRoleList();
		for (Record r : roles) {
			Map<String, String> map = new HashMap<>();
			String id = r.getStr("id");
			String name = r.getStr("name");
			map.put("id", id);
			map.put("name", name);
			list.add(map);
		}
		return list;
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
		return roleDBUtil.saveApplyRole(user_id, role_id, nick_name, head_url, orgId, phone);
	}
	
	/**
	 * 获取所有的申请角色列表
	 * 2018年5月10日14:22:27
	 * @author liutao
	 */
	public Page<Record> getApplyRoleList(int pageNum, int pageSize, String name, String applyStatus){
		return roleDBUtil.getApplyRoleList(pageNum, pageSize, name, applyStatus);
	}
	
	/**
	 * 保存角色申请的审核结果
	 * 2018年5月10日18:58:16
	 * @author liutao
	 * @return 
	 */
	public boolean saveApproval(String ids, String applyStatus, String approvalState){
		return roleDBUtil.saveApproval(ids, applyStatus, approvalState);
	}
	
	/**
	 * 查询所有角色(首次登录系统可申请的角色)
	 * 2018年6月14日10:40:39
	 * @author liutao
	 */
	public Page<Record> applyRoleList(int pageNum, int pageSize, String name){
		return roleDBUtil.applyRoleList(pageNum, pageSize, name);
	}
	
	/**
	 * 查询角色信息
	 * 2018年6月14日15:50:22
	 * @author liutao
	 */
	public Record findRoleInfoById(String id){
		return roleDBUtil.findRoleInfoById(id);
	}
	
	/**
	 * 保存角色信息
	 * 2018年6月14日16:13:39
	 * @author liutao
	 */
	public boolean saveApplyRoleInfo(Record r){
		return roleDBUtil.saveApplyRoleInfo(r);
	}
	
	/**
	 * 进行首次登录系统时取消角色申请记录保存
	 * 2018年7月3日11:22:34
	 * @author liutao
	 * @param user_id
	 * @return
	 */
	public boolean cancel(String user_id){
		return roleDBUtil.cancel(user_id);
	}
}
