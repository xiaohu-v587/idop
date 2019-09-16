package com.goodcol.server.zxglserver;

import java.util.List;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.zxgldbutil.PccmDownTaskDBUtil;

public class PccmDownTaskServer {

	PccmDownTaskDBUtil dbUtil = new PccmDownTaskDBUtil();
	/**
	 * 下载任务列表
	 */
	public Page<Record> downTaskPage(Map<String, Object> map){
		return dbUtil.downTaskPage(map);
	}
	/**
	 * 任务预览列表
	 */
	public List<Record> viewList(Map<String, Object> map){
		return dbUtil.viewList(map);
	}
	
	/**
	 * 根据PKID查找日志
	 */
	public List<Record> findById(String pkid){
		return dbUtil.findById(pkid);
	}
	
}
