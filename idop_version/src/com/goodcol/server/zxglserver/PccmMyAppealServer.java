package com.goodcol.server.zxglserver;

import java.util.List;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.zxgldbutil.PccmMyAppealDBUtil;

public class PccmMyAppealServer {

	PccmMyAppealDBUtil dbUtil = new PccmMyAppealDBUtil();
	/**
	 * 列表页面
	 */
	public Page<Record> pageList(Map<String, Object> map){
		return dbUtil.pageList(map);
	}
	
	/**
	 * 列表
	 */
	public List<Record> custList(Map<String, Object> map){
		return dbUtil.custList(map);
	}
	
	
}
