package com.goodcol.server.zxglserver;

import java.util.Map;

import com.goodcol.util.zxgldbutil.CustDistrDBUtil;

public class CustDistrServer {

	CustDistrDBUtil dbUtil = new CustDistrDBUtil();
	
	/**
	 * 客户分配保存
	 */
	public void distrSave(Map<String, Object> map){
		dbUtil.distrSave(map);
	}
	
}
