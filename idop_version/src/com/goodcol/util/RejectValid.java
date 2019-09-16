package com.goodcol.util;

import java.util.List;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.exception.RoleRejectException;

/**
 * 互斥校验工具类
 * @author huangzhf 20181122
 *
 */
public class RejectValid {
	
	/**
	 * 角色互斥校验
	 * @param r1 角色id
	 * @param r2 角色id
	 * @return true：互斥，false：不互斥
	 */
	public static boolean roleReject(String r1, String r2) {
		
		//有一个角色是系统管理员，直接返回互斥
		if (r1.equals("CFCA4238A0B923820DCC509A6F75849B") 
				|| r2.equals("CFCA4238A0B923820DCC509A6F75849B")) {
			return true;
		}
		
		List<Record> lst = Db.find("select roleid,rejectid from sys_rolereject_info where stat = '0'");
		
		for (Record r : lst) {
			String r1tmp = r.getStr("roleid");
			String r2tmp = r.getStr("rejectid");
			if ((r1tmp.equals(r1) && r2tmp.equals(r2)) 
					|| (r1tmp.equals(r2tmp) && r2tmp.equals(r1))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 角色互斥校验
	 * @param roles 角色ID数组，元素个数不低于2个
	 * @return true：互斥，false：不互斥
	 * @throws RoleRejectException 
	 */
	public static boolean roleReject(String[] roles) throws RoleRejectException {
		if (roles.length < 2) {
			throw new RoleRejectException("元素个数不低于2个！");
		}
		
		List<Record> lst = Db.find("select roleid,rejectid from sys_rolereject_info where stat = '0'");
		
		for (int i=0; i<roles.length; i++) {
			for (int j=i+1; j<roles.length; j++) {
				String r1 = roles[i];
				String r2 = roles[j];
				
				//有一个角色是系统管理员，直接返回互斥
				if (r1.equals("CFCA4238A0B923820DCC509A6F75849B") 
						|| r2.equals("CFCA4238A0B923820DCC509A6F75849B")) {
					return true;
				}
			
				for (Record r : lst) {
					String r1tmp = r.getStr("roleid");
					String r2tmp = r.getStr("rejectid");
					if ((r1tmp.equals(r1) && r2tmp.equals(r2)) 
							|| (r1tmp.equals(r2) && r2tmp.equals(r1))) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
}
