
package com.goodcol.plugin.activerecord.tx;

/**
 * TxReadUncommitted.
 */
public class TxReadUncommitted extends Tx {
	
    /**
     * A constant indicating that
     * dirty reads, non-repeatable reads and phantom reads can occur.
     * This level allows a row changed by one transaction to be read
     * by another transaction before any changes in that row have been
     * committed (a "dirty read").  If any of the changes are rolled back, 
     * the second transaction will have retrieved an invalid row.
     */
	private int TRANSACTION_READ_UNCOMMITTED = 1;
    
	@Override
	protected int getTransactionLevel(com.goodcol.plugin.activerecord.Config config) {
		return TRANSACTION_READ_UNCOMMITTED;
	}
}