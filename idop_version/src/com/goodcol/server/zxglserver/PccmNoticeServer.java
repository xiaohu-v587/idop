/**
 * 
 */
package com.goodcol.server.zxglserver;

import java.util.List;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.zxgldbutil.PccmNoticeDBUtil;

/**
 * @author dinggang
 *
 */
public class PccmNoticeServer {

	public Page<Record> getList(Map<String, Object> map) {
		PccmNoticeDBUtil pccmNoticeDBUtil=new PccmNoticeDBUtil();
		return pccmNoticeDBUtil.getList(map);
	}

	public int saveNotice(Record record) {
		PccmNoticeDBUtil pccmNoticeDBUtil=new PccmNoticeDBUtil();
		return pccmNoticeDBUtil.saveNotice(record);
	}

	public void del(String ids) {
		PccmNoticeDBUtil pccmNoticeDBUtil=new PccmNoticeDBUtil();
		pccmNoticeDBUtil.del(ids);
		
	}

	public Record getDetail(String id) {
		PccmNoticeDBUtil pccmNoticeDBUtil=new PccmNoticeDBUtil();
		return pccmNoticeDBUtil.getDetail(id);
	}

	public void updateNotice(Record record) {
		PccmNoticeDBUtil pccmNoticeDBUtil=new PccmNoticeDBUtil();
		pccmNoticeDBUtil.updateNotice(record);
		
	}

}
