
package com.goodcol.plugin.activerecord.tx;

import java.sql.SQLException;
import java.util.regex.Pattern;
import com.goodcol.aop.Interceptor;
import com.goodcol.aop.Invocation;
import com.goodcol.kit.StrKit;
import com.goodcol.plugin.activerecord.Config;
import com.goodcol.plugin.activerecord.DbKit;
import com.goodcol.plugin.activerecord.DbPro;
import com.goodcol.plugin.activerecord.IAtom;

/**
 * TxByActionKeyRegex.
 * The regular expression match the controller key.
 */
public class TxByActionKeyRegex implements Interceptor {
	
	private Pattern pattern;
	
	public TxByActionKeyRegex(String regex) {
		this(regex, true);
	}
	
	public TxByActionKeyRegex(String regex, boolean caseSensitive) {
		if (StrKit.isBlank(regex))
			throw new IllegalArgumentException("regex can not be blank.");
		
		pattern = caseSensitive ? Pattern.compile(regex) : Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	}
	
	public void intercept(final Invocation inv) {
		Config config = Tx.getConfigWithTxConfig(inv);
		if (config == null)
			config = DbKit.getConfig();
		
		if (pattern.matcher(inv.getActionKey()).matches()) {
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


