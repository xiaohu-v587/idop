package com.goodcol.util;
import java.util.List;

import com.goodcol.core.plugin.activerecord.Record;
public class MissionUtil {
	private static MissionUtil intanceof = null;

	private MissionUtil() {
 
	}

	public static MissionUtil getIntanceof() {
		if (intanceof == null) {
			intanceof = new MissionUtil();
		}
		return intanceof;
	} 
	
	public boolean isSysManagerByUser(Record user){
		//用户角色
		String roleNames = AppUtils.getRoleNames(user.getStr("ID"));
		return ( roleNames.indexOf(Constant.ROLE_SYSMANAGE_ZH) > 0 || roleNames.indexOf(Constant.ROLE_SYSMANAGE_FH) > 0 )?true:false;
	}
	
	
	
	
}
