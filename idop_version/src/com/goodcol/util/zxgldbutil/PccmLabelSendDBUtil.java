/**
 * 
 */
package com.goodcol.util.zxgldbutil;

import java.util.ArrayList;
import java.util.List;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;

/**
 * @author dinggang
 *
 */
public class PccmLabelSendDBUtil {

	public List<Record> getList(Record record) {
		List<String> listStr = new ArrayList<String>();
		String sql = "select *";
		String extrasql = " from pccm_label_send where 1=1 ";
		StringBuffer sb=new StringBuffer();
		if(AppUtils.StringUtil(record.getStr("custno"))!=null){
			sb.append(" and custno like ?");
			listStr.add("%"+record.getStr("custno")+"%");
		}
		if(AppUtils.StringUtil(record.getStr("custname"))!=null){
			sb.append(" and custname like ?");
			listStr.add("%"+record.getStr("custname")+"%");
		}
		if(AppUtils.StringUtil(record.getStr("label_key"))!=null){
			sb.append(" and label_key=?");
			listStr.add(record.getStr("label_key"));
		}
		if(AppUtils.StringUtil(record.getStr("label"))!=null){
			sb.append(" and label=?");
			listStr.add(record.getStr("label"));
		}
		Page<Record> r = Db.use("default").paginate(record.getInt("pageNum"),record.getInt("pageSize"), sql,
				extrasql+ sb.toString()+" order by n.create_time desc", listStr.toArray());
		return r.getList();
	}

}
