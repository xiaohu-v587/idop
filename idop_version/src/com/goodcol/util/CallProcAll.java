package com.goodcol.util;

/**
 * 调用存储过程，并返回List<Record>
 */
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import oracle.jdbc.OracleTypes;

import com.goodcol.core.plugin.activerecord.Config;
import com.goodcol.core.plugin.activerecord.DbKit;
import com.goodcol.core.plugin.activerecord.ICallback;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.core.plugin.activerecord.RecordBuilder;

public class CallProcAll implements ICallback {
	
	String userNo;
	ResultSet rs;
	int totalRow;
	int pageIndex;
	List<Record> list;
	String errorCode;
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public List<Record> getList() {
		return list;
	}

	public void setList(List<Record> list) {
		this.list = list;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	int pageSize;
	
	public int getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}

	@Override
	public Object call(Connection conn) throws SQLException {
		Config config = DbKit.getConfig();
		
		CallableStatement proc = null;
		
		//计算每次查询的记录范围
		int startRow = 0;
		int endRow = 0;
		
		startRow = (pageIndex-1)*pageSize+1;
		endRow = pageIndex*pageSize;
		
		
		proc = conn.prepareCall("{call queryPost_all(?,?,?)}");
		proc.setString(1, userNo);
		//proc.setInt(2, startRow);
		//proc.setInt(3, endRow);
		proc.registerOutParameter(2, OracleTypes.VARCHAR);
		proc.registerOutParameter(3, OracleTypes.NUMBER);
		//proc.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);
		
		proc.execute();
		
		errorCode = proc.getString(2);
		if("9999".equals(errorCode)){	//没有查询到信息
			totalRow = 0;
			rs = null;
			list = null;
			//System.out.println("callprocall 9999");
		}else if("0000".equals(errorCode)){
			totalRow = proc.getInt(3);
			//rs = (ResultSet)proc.getObject(4);
			
			//list = RecordBuilder.build(config, rs);
		}
		
		//System.out.println(list);
		return rs;
	}

}
