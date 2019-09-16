/**
 * 
 */
package com.goodcol.util.zxgldbutil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.date.BolusDate;

/**
 * @author dinggang
 *
 */
public class PccmNoticeDBUtil {

	public Page<Record> getList(Map<String, Object> map) {
		List<String> listStr = new ArrayList<String>();
		String sql = "select n.id,n.title,n.content,n.create_time,n.file_url,s.name";
		String extrasql = " from pccm_notice n left join gcms_param_info s on n.notice_type=s.val and s.key='NOTICE_TYPE' where 1=1 ";
		StringBuffer sb=new StringBuffer();
		if(map.get("title")!=null&&AppUtils.StringUtil(map.get("title").toString())!=null){
			sb.append(" and n.title like ?");
			listStr.add("%"+map.get("title").toString()+"%");
		}
		if(map.get("notice_type")!=null&&AppUtils.StringUtil(map.get("notice_type").toString())!=null){
			sb.append(" and n.notice_type= ?");
			listStr.add(map.get("notice_type").toString());
		}
		Page<Record> r = Db.use("default").paginate(Integer.parseInt(map.get("pageNum").toString()), Integer.parseInt(map.get("pageSize").toString()), sql,
				extrasql+ sb.toString()+" order by n.create_time desc", listStr.toArray());
		return r;
	}

	public int saveNotice(Record record) {
		int flag=0;
		if (AppUtils.StringUtil(record.getStr("id")) != null) {
			flag=Db.update("update pccm_notice set title=?,content=?,notice_type=?,modify_time=? where id=?",new Object[]{record.getStr("title"),record.getStr("content"),record.getStr("notice_type"),BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"),record.getStr("id")});
		} else {
			flag=Db.update("insert into pccm_notice(id,title,content,notice_type,create_time) values(?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),record.getStr("title"),record.getStr("content"),record.getStr("notice_type"),BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss")});
		}
		return flag;
	}

	public void del(String ids) {
		if (ids.contains(",")) {
			String[] array = ids.split(",");
			for (int i = 0; i < array.length; i++) {
				String uuid = array[i];
				Db.use("default").update(
						"delete from pccm_notice where id=?",
						new Object[] { uuid });
			}
		} else {
			Db.use("default").update("delete from pccm_notice where id=?",
					new Object[] { ids });
		}
	}

	public Record getDetail(String id) {
		String sql = "select * from pccm_notice where 1=1";
		StringBuffer sb = new StringBuffer();
		List<String> listStr = new ArrayList<String>();
		if (AppUtils.StringUtil(id) != null) {
			sb.append(" and id = ? ");
			listStr.add(id);
		}
		List<Record> list = Db.use("default").find(sql + sb.toString(),
				listStr.toArray());
		Record record = list.get(0);
		return record;
	}

	public void updateNotice(Record record) {
		Db.update("update pccm_notice set file_url=? where id=?",new Object[]{record.getStr("file_url"),record.getStr("id")});
	}

}
