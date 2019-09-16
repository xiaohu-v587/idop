package com.goodcol.server.zxglserver;

import java.util.List;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.zxgldbutil.TelBookDBUtil;

public class TelBookServer {

	TelBookDBUtil dbUtil = new TelBookDBUtil();
	/**
	 * 列表页面
	 */
	public Page<Record> pageList(Map<String, Object> map){
		return dbUtil.pageList(map);
	}
	
	/**
	 * 列表
	 */
	public List<Record> custMgrList(Map<String, Object> map){
		return dbUtil.custMgrList(map);
	}
	
	
}
