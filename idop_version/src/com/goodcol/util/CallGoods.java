package com.goodcol.util;

/**
 * 调用存储过程
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

public class CallGoods implements ICallback {
	
	String oldGoodsId; 
	String newGoodsId;
	String backFlag;

	public String getOldGoodsId() {
		return oldGoodsId;
	}

	public void setOldGoodsId(String oldGoodsId) {
		this.oldGoodsId = oldGoodsId;
	}

	public String getNewGoodsId() {
		return newGoodsId;
	}

	public void setNewGoodsId(String newGoodsId) {
		this.newGoodsId = newGoodsId;
	}

	public String getBackFlag() {
		return backFlag;
	}

	public void setBackFlag(String backFlag) {
		this.backFlag = backFlag; 
	}

	@Override
	public Object call(Connection conn) throws SQLException {
		Config config = DbKit.getConfig();
		
		CallableStatement proc = null;
		
		proc = conn.prepareCall("{call goodstypecheck(?,?,?)}");
		proc.setString(1, oldGoodsId);
		proc.setString(2, newGoodsId);
		proc.registerOutParameter(3, OracleTypes.VARCHAR);
		
		proc.execute();
		
		backFlag = proc.getString(3);
		return backFlag;
	}

}
