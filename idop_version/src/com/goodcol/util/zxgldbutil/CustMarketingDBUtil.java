package com.goodcol.util.zxgldbutil;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;

public class CustMarketingDBUtil {
	private static final String DEFAULT = "default";

	/**
	 * 新增营销记录
	 * 
	 * @param custNo
	 * @param userNo
	 * @param date
	 * @param status
	 * @param remark
	 */
	public boolean save(String custNo, String userNo, String date, String status, String remark, String id) {
		int update = Db.use("default").update(" insert into PCCM_CUST_MARKET_RECORD(ID,CUST_NO,EHR_NUM,"
						+ "MARKET_TIME,STATUS,REMARK,CUST_POOL_ID) values(?,?,?,?,?,?,?) ",
				new Object[] { AppUtils.getStringSeq(), custNo, userNo, date,
						status, remark, id});
		if(update > 0){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 删除营销记录
	 * @param custNo
	 * @param userNo
	 */
	public void del(String id) {
		Db.use("default").update(" DELETE FROM PCCM_CUST_MARKET_RECORD WHERE CUST_NO=? AND EHR_NUM = ?",
						new Object[] { id });
	}

	/**
	 * 查询营销反馈列表
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @param custNo
	 * @param userNo
	 * @return
	 */
	public Page<Record> findList(int pageNum, int pageSize, String custNo, String userNo) {
		String sql = "SELECT T1.ID,T1.CUST_NO,T1.EHR_NUM,T1.MARKET_TIME,T1.STATUS,T1.REMARK ";
		String fromSql = "FROM PCCM_CUST_MARKET_RECORD T1 "
				+ "LEFT JOIN SYS_USER_INFO SUI ON T1.EHR_NUM = SUI.USER_NO "
				+ "LEFT JOIN PCCM_CUST_POOL CP on CP.CUST_NO = T1.CUST_NO  WHERE 1=1 ";
		List<String> paramList = new ArrayList<String>();
		if (StringUtils.isNotBlank(custNo)) {
			fromSql += "AND T1.CUST_NO = ? ";
			paramList.add(userNo);
		}
		if (StringUtils.isNotBlank(userNo)) {
			fromSql += "AND T1.USER_NO = ? ";
			paramList.add(userNo);
		}
		fromSql += "ORDER BY T1.MARKET_TIME DESC ";
		Page<Record> pages = Db.use(DEFAULT).paginate(pageNum, pageSize, sql,
				fromSql, paramList.toArray());
		return pages;
	}
}
