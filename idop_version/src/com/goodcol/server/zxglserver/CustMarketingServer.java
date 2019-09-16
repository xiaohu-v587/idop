package com.goodcol.server.zxglserver;

import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.zxgldbutil.CustMarketingDBUtil;

public class CustMarketingServer {

	private CustMarketingDBUtil dbUtil = new CustMarketingDBUtil();

	/**
	 * 新增营销记录
	 * 
	 * @param custNo
	 * @param userNo
	 * @param date
	 * @param status
	 * @param remark
	 */
	public boolean save(String custNo, String userNo, String date, String status,
			String remark, String id) {
		return dbUtil.save(custNo, userNo, date, status, remark, id);
	}

	/**
	 * 删除营销记录
	 * 
	 * @param custNo
	 * @param userNo
	 */
	public void del(String id) {
		dbUtil.del(id);
	}

	/**
	 * 查询列表
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @param custNo
	 * @param userNo
	 * @return
	 */
	public Page<Record> findList(int pageNum, int pageSize, String custNo,
			String userNo) {
		return dbUtil.findList(pageNum, pageSize, custNo, userNo);
	}
}
