package com.goodcol.server.zxglserver;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.zxgldbutil.ValidCustDBUtil;

public class ValidCustServer {

	private ValidCustDBUtil DBUtil = new ValidCustDBUtil();

	/**
	 * 有效户统计列表
	 * 
	 * @param orgNum
	 * @param month
	 * @return
	 */
	public List<Record> getValidCustCountList(String orgNum, String month,
			String lastMonth) {
		return DBUtil.getValidCustCountList(orgNum, month, lastMonth);
	}

	/**
	 * 客户信息列表
	 * 
	 * @param page
	 * @param pageSize
	 * @param month
	 * @param orgNum
	 * @param name
	 * @return
	 */
	public Page<Record> getCustList(int page, int pageSize, String month,
			String orgNum, String name, String cust_no, String deptLevel) {
		if (StringUtils.isBlank(month)) {
			month = DateTimeUtil.getPathName();
		}
		return DBUtil.getCustList(page, pageSize, month, orgNum, name, cust_no, deptLevel);
	}

	/**
	 * 根据机构id获取PA不为零统计表 2018年8月3日12:55:53
	 * 
	 * @author liutao
	 * @return
	 */
	public List<Record> findValidCustNumByOrgId(Map<String, String> map) {
		return DBUtil.findValidCustNumByOrgId(map);
	}

	/**
	 * 根据机构号获取当前PA不为零客户数据 2018年8月9日18:55:12
	 * 
	 * @author liutao
	 * @param map
	 * @return
	 */
	public Page<Record> findValidCustInfoByOrgId(Map<String, String> map) {
		return DBUtil.findValidCustInfoByOrgId(map);
	}

	/**
	 * 有效户统计列表
	 * 
	 * @param orgNum
	 * @param month
	 * @return
	 */
	public List<Record> downLoadValidCustCountList(String month, String orgNum) {
		return DBUtil.downLoadValidCustCountList(month, orgNum);
	}

	/**
	 * 查询下载PA不为零客户数据 2018年8月15日16:24:46
	 * 
	 * @author liutao
	 */
	public List<Record> findDownloadOrgPaNotZero(String month, String orgNum) {
		return DBUtil.findDownloadOrgPaNotZero(month, orgNum);
	}
	
	public List<Record> validCustCountDate() {
		return DBUtil.validCustCountDate();
	}
	
	public List<Record> validCustPaDate() {
		return DBUtil.validCustPaDate();
	}
	
}
