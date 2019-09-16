/**
 * 
 */
package com.goodcol.server.zxglserver;

import java.util.List;

import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.zxgldbutil.PccmCustTranDBUtil;

/**
 * @author dinggang
 *
 */
public class PccmCustTranServer {
	
	private PccmCustTranDBUtil custTranDBUtil=new PccmCustTranDBUtil();
	
	public List<Record> findPoolRecords() {
		return custTranDBUtil.findPoolRecords();
	}

	public String findYjUrl() {
		return custTranDBUtil.findYjUrl();
	}

	public void updatePool(String creditCode, Record record) {
		custTranDBUtil.updatePool(creditCode,record); 
		
	}

	public String findXydUrl() {
		return custTranDBUtil.findXydUrl();
	}

	public String findXydKey() {
		return custTranDBUtil.findXydKey();
	}

}
