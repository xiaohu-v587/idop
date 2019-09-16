package com.goodcol.job;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.ICallback;

import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

/**
 * 客户池数据同步任务
 * 
 * @author start
 * @2018-9-12
 */
public class PccmDataBatchJob extends OuartzJob {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {

			Db.execute(new ICallback() {
				@Override
				public Object call(Connection conn) throws SQLException {
					CallableStatement proc = null;
					try {
						proc = conn.prepareCall("{CALL P_PCCM_CUST_POOL(?,?)}");
						proc.setString(1, DateTimeUtil.geYesterDay(1));
						proc.registerOutParameter(2, java.sql.Types.VARCHAR);
						proc.execute();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (proc != null) {
							proc.close();
						}
						if (conn != null) {
							conn.close();
						}
					}
					return null;
				}
			});
			success();
		} catch (Exception e) {
			error();
			e.printStackTrace();
		}
	}
}
