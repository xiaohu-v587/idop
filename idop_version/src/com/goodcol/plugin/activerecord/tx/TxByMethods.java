

package com.goodcol.plugin.activerecord.tx;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import com.goodcol.aop.Interceptor;
import com.goodcol.aop.Invocation;
import com.goodcol.plugin.activerecord.Config;
import com.goodcol.plugin.activerecord.DbKit;
import com.goodcol.plugin.activerecord.DbPro;
import com.goodcol.plugin.activerecord.IAtom;

/**
 * TxByMethods
 */
public class TxByMethods implements Interceptor {
	
	private Set<String> methodSet = new HashSet<String>();
	
	public TxByMethods(String... methods) {
		if (methods == null || methods.length == 0)
			throw new IllegalArgumentException("methods can not be null.");
		
		for (String method : methods)
			methodSet.add(method.trim());
	}
	
	public void intercept(final Invocation inv) {
		Config config = Tx.getConfigWithTxConfig(inv);
		if (config == null)
			config = DbKit.getConfig();
		
		if (methodSet.contains(inv.getMethodName())) {
			DbPro.use(config.getName()).tx(new IAtom() {
				public boolean run() throws SQLException {
					inv.invoke();
					return true;
				}});
		}
		else {
			inv.invoke();
		}
	}
}







