package com.goodcol.plugin.activerecord;

import java.sql.Connection;
import com.goodcol.aop.Interceptor;
import com.goodcol.aop.Invocation;
import com.goodcol.kit.LogKit;

/**
 * One Connection Per Thread for one request.<br>
 * warning: can not use this interceptor with transaction feature like Tx, Db.tx(...)
 */
public class OneConnectionPerThread implements Interceptor {
	
	public void intercept(Invocation inv) {
		Connection conn = DbKit.config.getThreadLocalConnection();
		if (conn != null) {
			inv.invoke();
			return ;
		}
		
		try {
			conn = DbKit.config.getConnection();
			DbKit.config.setThreadLocalConnection(conn);
			inv.invoke();
		}
		catch (RuntimeException e) {
			throw e;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			DbKit.config.removeThreadLocalConnection();
			if (conn != null) {
				try{conn.close();}catch(Exception e){LogKit.error(e.getMessage(), e);};
			}
		}
	}
}
