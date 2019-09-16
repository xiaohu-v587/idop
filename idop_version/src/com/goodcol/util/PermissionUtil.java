package com.goodcol.util;

/**
 * 权限判断公共类
 * 
 * @author huangzf 2015.5.15
 * 
 */
import java.util.List;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;

public class PermissionUtil {

	private static PermissionUtil perUtil = null;

	public PermissionUtil() {
	}
	
	public static PermissionUtil getInstanceOf(){
		if(perUtil == null){
			perUtil = new PermissionUtil();
		}
		return perUtil;
	}
	
	public boolean checkPermission(String menu_id, String role_id, String process){
		
		String id = "";
		//获取当前用户所对应角色的所有权限
		List<Record> permissLst = Db.find("select power_id from sys_role_power where role_id = '" + role_id + "'");
		
		//获取当前操作的菜单可做的操作
		List<Record> menuLst = Db.find("select id, objcode from sys_userpower_info where menu_id = '" + menu_id + "'");
		
		//获取当前操作类型对应的操作ID
		for(Record r : menuLst){
			if(r.getStr("OBJCODE").equals(process)){
				id = r.getStr("ID");
			}
		}
		
		//判断当前用户所属角色是否包含对应的操作ID
		for(Record r : permissLst){
			if(r.getStr("POWER_ID").equals(id)){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean checkPermission_aommobile(String menu_id, String role_id, String...process){
		
		
		//获取当前用户所对应角色的所有权限
		List<Record> permissLst = Db.find("select menuid from SYS_POWER_INFO where menuid='"+menu_id+"' and objid = '" + role_id + "' and targettype = '"+process[1]+"'");
		
		//获取当前操作的菜单可做的操作
		//List<Record> menuLst = Db.find("select id, objcode from sys_userpower_info where menu_id = '" + menu_id + "'");
		
		if(permissLst!=null&&permissLst.size()>0){
			return true;
		}
		
		return false;
	}
	
	public static void main(String[] args){
		new PermissionUtil().checkPermission("28F3D90FFD6542348B421BD80FBB5BA6", 
				"CFCA4238A0B923820DCC509A6F75849B", "add");
	}
}
