
package com.goodcol.plugin.activerecord;

/**
 * NestedTransactionHelpException
 * <br>
 * Notice the outer transaction that the nested transaction return false
 */
public class NestedTransactionHelpException extends RuntimeException {
	
	private static final long serialVersionUID = 7933557736005738819L;
	
	public NestedTransactionHelpException(String message) {
		super(message);
	}
}



