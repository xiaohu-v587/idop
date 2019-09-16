package com.goodcol.server.zxglserver;

import java.util.List;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.zxgldbutil.CustAppealDBUtil;

public class CustAppealServer {

	CustAppealDBUtil dbUtil = new CustAppealDBUtil();
	/**
	 * 客户认领列表页面
	 */
	public Page<Record> pageList(Map<String, Object> map){
		return dbUtil.pageList(map);
	}
	
	/**
	 * 客户认领列表
	 */
	public List<Record> custList(Map<String, Object> map){
		return dbUtil.custList(map);
	}
	
	/**
	 * 客户强制分配保存
	 */
	public void distrSave(Map<String, Object> map){
		dbUtil.distrSave(map);
	}
	
	/**
	 * 客户驳回保存
	 */
	public void reject(String appealId){
		dbUtil.reject(appealId);
	}
	
	/**
	 * 客户详情
	 */
	public Record getDetail(String id){
		return dbUtil.getDetail(id);
	}
	
	/**
	 * 用户当前组织成员下拉框数据
	 */
	public List<Record> getOrgUser(String orgId){
		return dbUtil.getOrgUser(orgId);
	}
	
	/**
	 * 查询客户是否处于申诉状态
	 */
	public Integer getAppNum(String poolId){
		return dbUtil.getAppNum(poolId);
	}
}
