

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
 * TxByActionKeys
 */
public class TxByActionKeys implements Interceptor {
	
	private Set<String> actionKeySet = new HashSet<String>();
	
	public TxByActionKeys(String... actionKeys) {
		if (actionKeys == null || actionKeys.length == 0)
			throw new IllegalArgumentException("actionKeys can not be blank.");
		
		for (String actionKey : actionKeys)
			actionKeySet.add(actionKey.trim());
	}
	
	public void intercept(final Invocation inv) {
		Config config = Tx.getConfigWithTxConfig(inv);
		if (config == null)
			config = DbKit.getConfig();
		
		if (actionKeySet.contains(inv.getActionKey())) {
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







