package com.goodcol.server.zxglserver;

import java.util.List;
import java.util.Map;

import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.zxgldbutil.PccmCustomSearchDBUtil;

/**
 * 定制查询服务层
 * 
 * @author start
 * @2018-10-22
 */
public class PccmCustomSearchServer {

	public static Logger log = Logger.getLogger(PccmCustomSearchServer.class);

	private PccmCustomSearchDBUtil searchDBUtil = new PccmCustomSearchDBUtil();

	/**
	 * 分页查询定制列表
	 */
	public Page<Record> getList(Map<String, String> paramMap, int pageNum, int pageSize) {
		Page<Record> page = searchDBUtil.getList(paramMap, pageNum, pageSize);
		return page;
	}

	/**
	 * 添加基础模板
	 * 
	 * @param paramMap
	 */
	public void addBaseModel(Map<String, String> paramMap) {
		searchDBUtil.addBaseModel(paramMap);
	}

	/**
	 * 删除模板
	 * 
	 * @param baseId
	 */
	public void deleteModel(String baseId) {
		searchDBUtil.deleteModel(baseId);
	}
	
	/**
	 * 删除模板
	 * 
	 * @param baseId
	 */
	public void deleteFILED(String baseId) {
		searchDBUtil.deleteFILED(baseId);
	}
	
	/**
	 * 删除权限
	 * 
	 * @param baseId
	 */
	public void deleteAuthOrgs(String baseId) {
		searchDBUtil.deleteAuthOrgs(baseId);
	}
	
	/**
	 * 删除子模板模板字段
	 * 
	 * @param baseId
	 */
	public void deleteChildFILED(String baseId) {
		searchDBUtil.deleteChildFILED(baseId);
	}

	/**
	 * 更新模板
	 * am paramMap
	 */
	public void updateBaseModel(Map<String, String> paramMap) {
		searchDBUtil.updateBaseModel(paramMap);
	}

	/**
	 * 添加模板基础字段
	 * 
	 * @param requestMap
	 * @param baseId
	 */
	public void addBaseFileds(Map<String, Object> requestMap, String baseId) {
		searchDBUtil.addBaseFileds(requestMap, baseId);
	}

	/**
	 * 子模板字段添加
	 * 
	 * @param baseId
	 * @param filedIds
	 */
	public void addChildModelFileds(String baseId, String filedIds) {
		searchDBUtil.addChildModelFileds(baseId, filedIds);
	}
	
	/**
	 * 查询父表明细
	 */
	public Record getModelDetail(String base_id) {
		return searchDBUtil.getModelDetail(base_id);
	}
	
	/**
	 * 查询父表字段明细
	 */
	public List<Record> getFieldDetail(String base_id) {
		return searchDBUtil.getFieldDetail(base_id);
	}
	
	/**
	 * 查询子表字段明细
	 */
	public List<Record> getChildFieldDetail(String base_id,String downFlag) {
		return searchDBUtil.getChildFieldDetail(base_id,downFlag);
	}
	
	/**
	 * 是否有子表
	 */
	public boolean isExistChildTable(String base_id) {
		return searchDBUtil.isExistChildTable(base_id);
	}
	
	/**
	 * 更改表单状态
	 */
	public void changeStatus(String base_id,String status){
		searchDBUtil.changeStatus(base_id, status);
	}
	
	/**
	 * 查询授权机构
	 */
	public List<Record> getAuthOrgs(String base_id) {
		return searchDBUtil.getAuthOrgs(base_id);
	}
	
	/**
	 * 授权
	 */
	public void addAuthOrgs(String baseId,String orgIds,String userNo) {
		searchDBUtil.addAuthOrgs(baseId, orgIds, userNo);
	}
	
	/**
	 * 分页查询业务定制列表
	 */
	public Page<Record> getBusiList(Map<String, String> paramMap, int pageNum, int pageSize) {
		return searchDBUtil.getBusiList(paramMap, pageNum, pageSize);
	}
	
	/**
	 * 创建序列
	 */
	public void addModelSeq(String seq_name) {
		searchDBUtil.addModelSeq(seq_name);
	}
	
	/**
	 * 分页查询业务定制子列表
	 */
	public Page<Record> getBusiChildList(Map<String, String> paramMap, int pageNum, int pageSize) {
		return searchDBUtil.getBusiChildList(paramMap, pageNum, pageSize);
	}
	
	/**
	 * 添加子模板
	 */
	public void addChildModel(Map<String, String> paramMap) {
		searchDBUtil.addChildModel(paramMap);
	}
	
	/**
	 * 修改子模板
	 */
	public void updateChildModel(Map<String, String> paramMap) {
		searchDBUtil.updateChildModel(paramMap);
	}
	
	/**
	 * 使用人员分页查询业务定制子列表
	 * 
	 */
	public Page<Record> getSerList(Map<String, String> paramMap, int pageNum, int pageSize) {
		return searchDBUtil.getSerList(paramMap, pageNum, pageSize);
	}
}
