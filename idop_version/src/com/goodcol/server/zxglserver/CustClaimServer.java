package com.goodcol.server.zxglserver;

import java.util.List;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.zxgldbutil.CustClaimDBUtil;

public class CustClaimServer {

	CustClaimDBUtil dbUtil = new CustClaimDBUtil();
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
	 * 客户数
	 */
	public String custListCount(Map<String, Object> map){
		return dbUtil.custListCount(map);
	}
	
	/**
	 * 客户认领保存
	 */
	public void claimSave(Map<String, Object> map){
		dbUtil.claimSave(map);
	}
	
	/**
	 * 客户申诉保存
	 */
	public void claimCompSave(Map<String, Object> map){
		dbUtil.claimCompSave(map);
	}
	
	/**
	 * 客户认领详情
	 */
	public Record getDetail(String id){
		return dbUtil.getDetail(id);
	}
	
	/**
	 * 查询可认领最大值
	 */
	public String claimManyCheck(String ids){
		return dbUtil.claimManyCheck(ids);
	}
	
	/**
	 * 查询已认领的时间
	 */
	public String claimDate(String pool_id,String mgr_id,String distr_id){
		return dbUtil.claimDate(pool_id, mgr_id,distr_id);
	}
	
	/**
	 * 撤回操作
	 */
	public void claimBack(String pool_id,String mgr_id,String distr_id){
		dbUtil.claimBack(pool_id,mgr_id,distr_id);
	}
	
	/**
	 * 查询数据最新入池时间
	 */
	public String findNewDate(Map<String, Object> map){
		return dbUtil.findNewDate(map);
	}
	
	/**
	 * 行政区域列表查询
	 */
	public List<Record> areaList(String pid) {
		return  dbUtil.areaList(pid);

	}
	
	/**
	 * 认领详情
	 **/
	public List<Record> findClaimList(String poolid){
		return  dbUtil.findClaimList(poolid);
	}
	
}
