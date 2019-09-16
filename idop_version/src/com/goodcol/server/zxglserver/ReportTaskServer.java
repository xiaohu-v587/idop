package com.goodcol.server.zxglserver;

import java.util.List;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.zxgldbutil.ReportTaskDBUtil;

public class ReportTaskServer {

	ReportTaskDBUtil dbUtil = new ReportTaskDBUtil();

	/**
	 * 根据条件查询报表任务列表
	 */
	public Page<Record> getListReportSql(Map<String, Object> map2) {
		return dbUtil.getListReportSql(map2);
	}

	/**
	 * 读取上传的附件
	 */
	public List<Record> uploadData(String filePath) {
		return dbUtil.readExcel(filePath);
	}

	/**
	 * 新建报表机构树展示
	 */
	public List<Record> getOrgList() {
		return dbUtil.getOrgList();

	}

	/**
	 * 根据id获取机构的详细信息 （点击左边的机构树，右边显示该机构下员工的详细信息）
	 */
	public Page<Record> getListOrg(Map<String, Object> map) {
		return dbUtil.getListOrg(map);
	}

	/**
	 * 保存新建的手工报表
	 */
	public void saveReportTask(Map<String, Object> map) {
		dbUtil.saveReportTask(map);
	}

	/**
	 * 删除手工报表
	 */
	public void delReportTask(String id) {
		dbUtil.delReportTask(id);
	}

	/**
	 * 查询报表—列表查看 详情
	 */
	public Page<Record> getUserReport(Map<String, Object> map) {
		return dbUtil.getUserReport(map);
	}
	/**
	 * 查询报表—列表查看 详情
	 */
	public Page<Record> getUserReportOfsp(Map<String, Object> map) {
		return dbUtil.getUserReportOfsp(map);
	}

	/**
	 * 效率查询列表
	 */
	public Page<Record> getTimeList(Map<String, Object> map) {
		return dbUtil.getTimeList(map);
	}

	/**
	 * 效率查询详情
	 */
	public Page<Record> getTimeDetail(Map<String, Object> map) {
		return dbUtil.getTimeDetail(map);
	}

	/**
	 * 查询群组列表
	 */
	public Page<Record> getGroupList(Map<String, Object> map) {
		return dbUtil.getGroupList(map);
	}

	/**
	 * 新增群组
	 */
	public void saveGroup(Map<String, Object> map) {
		dbUtil.saveGroup(map);
	}

	/**
	 * 删除群组
	 */
	public void deleteGroup(Map<String, Object> map) {
		dbUtil.deleteGroup(map);
	}

	/**
	 * 查询待办任务列表
	 */
	public Page<Record> getMyReportList(Map<String, Object> map) {
		return dbUtil.getMyReportList(map);
	}

	/**
	 * 保存新建的手工报表附件
	 */
	public void saveReportFile(String reportId, String fileUrl, String fileName) {
		dbUtil.saveReportFile(reportId, fileUrl, fileName);
	}

	/**
	 * 报表管理-群组下拉框
	 */
	public List<Record> getGroupList(String user_no) {
		return dbUtil.getGroupList(user_no);
	}

	/**
	 * 获取详细信息
	 */
	public Record getDetail(String reportId) {
		return dbUtil.getDetail(reportId);
	}
	/**
	 * 获取详细信息
	 */
	public Record getDetails(String reportId) {
		return dbUtil.getDetails(reportId);
	}

	/**
	 * 修改发布状态
	 */
//	public void updateIssuerStatus(String reportId, String status) {
//		dbUtil.updateIssuerStatus(reportId, status);
//	}

	/**
	 * 修改完成状态
	 */
	public void updateTaskStatus(String reportId, String status) {
		dbUtil.updateTaskStatus(reportId, status);
	}

	/**
	 * 获取手工报表附件列表
	 */
	public List<Record> getReportFiles(String reportId) {
		return dbUtil.getReportFiles(reportId);
	}

	/**
	 * 删除附件
	 */
	public void deleteReportFiles(String fileId) {
		dbUtil.deleteReportFiles(fileId);
	}

	public Record getFileDetail(String id) {
		return dbUtil.getFileDetail(id);
	}

	/**
	 * 个人任务认领
	 */
	public void claimReportTask(String reportChildId, String user_no) {
		dbUtil.claimReportTask(reportChildId, user_no);
	}

	/**
	 * 个人任务状态修改
	 */
	public void updateUserStatus(String reportChildId, String status, String reason) {
		dbUtil.updateUserStatus(reportChildId, status, reason);
	}

	/**
	 * 获取手工表单数据
	 */
	public List<Record> getTableData(Map<String, Object> map) {
		return dbUtil.getTableData(map);
	}
	/**
	 * 获取手工表单数据
	 */
	public List<Record> getTableDatas(Map<String, Object> map) {
		return dbUtil.getTableData(map);
	}

	/**
	 * 保存上报数据
	 */
	public void saveTableDatas(Map<String, Object> requestMap) {
		dbUtil.saveTableDatas(requestMap);
	}
	
	public Map<String, String> getTransfer(String id){
		return dbUtil.getTransfer(id);
	}

	/**
	 * 转发
	 */
	public void taskTransfer(Map<String, Object> map) {
		dbUtil.taskTransfer(map);
	}

	/**
	 * 流程图
	 */
	public List<Record> getEchart(String id) {
		return dbUtil.getEchart(id);
	}

	/**
	 * 查找短信发送的IP和端口
	 * 
	 * @return
	 */
	public Map<String, Object> findMessIpAndPort() {
		return dbUtil.findMessIpAndPort();
	}

	/**
	 * 获取所有未完成报表任务人员信息
	 */
	public List<Record> remind(String rid, String id) {
		return dbUtil.remind(rid, id);
	}

	/**
	 * 保存角色移交信息 2018年11月16日14:28:34
	 * 
	 * @author liutao
	 */
	public int saveRoleTransferInfo(Map<String, Object> map) {
		return dbUtil.saveRoleTransferInfo(map);
	}

	/**
	 * 获取所有的用户信息，(角色移交查询所有的人进行选择移交) 2018年11月16日11:01:09
	 * 
	 * @author liutao
	 */
	public Page<Record> getAllUser(Map<String, Object> map) {
		return dbUtil.getAllUser(map);
	}

	/**
	 * 定时任务保存
	 */
	public void saveJobTask(String rate){
		dbUtil.saveJobTask(rate);

	}

}
