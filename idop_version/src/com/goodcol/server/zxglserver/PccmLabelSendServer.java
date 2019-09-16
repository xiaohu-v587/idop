/**
 * 
 */
package com.goodcol.server.zxglserver;

import java.util.List;

import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.zxgldbutil.PccmLabelSendDBUtil;

/**
 * @author dinggang
 *
 */
public class PccmLabelSendServer {


	public List<Record> getList(Record record) {
		PccmLabelSendDBUtil pccmLabelSendDBUtil=new PccmLabelSendDBUtil();
		return pccmLabelSendDBUtil.getList(record);
	}

}
