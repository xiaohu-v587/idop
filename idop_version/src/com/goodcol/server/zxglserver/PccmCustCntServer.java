package com.goodcol.server.zxglserver;

import java.util.List;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.zxgldbutil.PccmCustCntDBUtil;

public class PccmCustCntServer {

	PccmCustCntDBUtil dbUtil = new PccmCustCntDBUtil();
	
	/**
	 * 客户统计列表查询-按组织
	 */
	public List<Record> custByOrgList(String upOrg,String roleLevel,String dataDate){
		return dbUtil.custByOrgList(upOrg,roleLevel,dataDate);
	}
	
	/**
	 * 客户统计列表下载-按组织
	 */
	public List<Record> downloadCustByOrgList(String upOrg,String roleLevel){
		return dbUtil.downloadCustByOrgList(upOrg,roleLevel);
	}
	
	/**
	 * 客户统计列表查询-按类别
	 */
	public List<Record> custByTypeList(String upOrg,String roleLevel){
		return dbUtil.custByTypeList(upOrg,roleLevel);
	}
	
	/**
	 * 客户统计列表查询-按类别
	 */
	public List<Record> custByFiveList(String upOrg,String roleLevel,String dataDate){
		return dbUtil.custByFiveList(upOrg,roleLevel,dataDate);
	}
	
	/**
	 * 查询数据最新入池时间
	 */
	public String findNewDate(){
		return dbUtil.findNewDate();
	}
	
	/**
	 * 客户详情页面
	 */
	public Page<Record> custCntPage(Map<String, Object> map){
		return dbUtil.custCntPage(map);
	}
	
	/**
	 * 列表查询
	 */
	public List<Record> custCntList(Map<String, Object> map) {
		return dbUtil.custCntList(map);
	}
	
	/**
	 * 客户统计列表查询-按组织
	 */
	public List<Record> custByOrgNewList(String upOrg){
		return dbUtil.custByOrgNewList(upOrg);
	}
	
	/**
	 * 下载客户统计列表查询-按组织
	 */
	public List<Record> downloadCustByOrgNewList(String upOrg, String dataDate) {
		return dbUtil.downloadCustByOrgNewList(upOrg,dataDate);
	}
}
