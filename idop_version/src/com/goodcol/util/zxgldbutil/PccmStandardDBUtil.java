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
import com.goodcol.util.date.BolusDate;

/**
 * @author dinggang
 * 
 */
public class PccmStandardDBUtil {

	public Page<Record> getList(Record record) {
		List<String> listStr = new ArrayList<String>();
		String sql = "select soi2.orgname as second_org_num, soi3.orgname as third_org_num, psi.id, psi.create_time, gpi.remark as standard_flag ";
		String extrasql = "from pccm_standard_info psi "
				+ "left join sys_org_info soi2 on soi2.orgnum = psi.second_org_num "
				+ "left join sys_org_info soi3 on soi3.orgnum = psi.third_org_num "
				+ "left join gcms_param_info gpi on gpi.name = psi.standard_flag and key = 'STANDARD_TYPE' "
				+ "order by psi.create_time desc";
		StringBuffer sb = new StringBuffer();
//		String orgnum=record.getStr("orgnum");
//		if(AppUtils.StringUtil(orgnum)!=null){
//			sb.append(" and (p.second_org_num='"+orgnum+"' or p.third_org_num='"+orgnum+"' or p.hibnk='"+orgnum+"')");
//		}
		Page<Record> r = Db.use("default").paginate(record.getInt("pageNum"), record.getInt("pageSize"), sql,
				extrasql + sb.toString(),
				listStr.toArray());
		return r;
	}

	public Object del(String ids) {
		if (ids.contains(",")) {
			String[] array = ids.split(",");
			for (int i = 0; i < array.length; i++) {
				String uuid = array[i];
				Db.use("default").update(
						"delete from pccm_standard_info where id=?",
						new Object[] { uuid });
				Db.use("default").update("delete from pccm_standard_condition where standard_id=?",new Object[]{uuid});
			}
		} else {
			Db.use("default").update(
					"delete from pccm_standard_info where id=?",
					new Object[] { ids });
			Db.use("default").update("delete from pccm_standard_condition where standard_id=?",new Object[]{ids});
		}
		return null;
	}

	public List<Record> getDict(String key, String type) {
		List<Record> list = null;
		if (AppUtils.StringUtil(type) != null) {
			if (type.equals("0")) {
				list = Db
						.use("default")
						.find("select val,name from gcms_param_info where key = ? and remark=?",
								new Object[] { key, "数学比较符号" });
			} else if (type.equals("1")) {
				list = Db
						.use("default")
						.find("select val,name from gcms_param_info where key = ? and remark=?",
								new Object[] { key, "数学运算符号" });
			}
		} else {
			list = Db.use("default").find(
					"select val,name from gcms_param_info where key = ?",
					new Object[] { key });
		}
		return list;
	}

	public List<Record> getSecondOrg() {
		List<Record> records = Db.use("default").find(
				"select orgnum,orgname from sys_org_info where by2=2");
		return records;
	}

	public List<Record> getThridOrg(String orgnum) {
		List<Record> records = Db.use("default").find(
				"select orgnum,orgname from sys_org_info where upid=?",
				new Object[] { orgnum });
		return records;
	}

	public List<Record> getDataFlag(String flag, String key, String subkey) {
		List<Record> records = Db.use("default").find(
				"select id from gcms_param_info where key = ? and val=?",
				new Object[] { key, flag });
		List<Record> list = Db.use("default").find(
				"select val,name from gcms_param_info where key = ? and pid=?",
				new Object[] { subkey, records.get(0).getStr("id") });
		return list;
	}

	public List<Record> getStandardFields(String data_flag_val, String key,
			String subkey, String thirdsubkey) {
		List<Record> records = Db.use("default").find(
				"select * from gcms_param_info where key=? and val=?",
				new Object[] { key, data_flag_val });
		List<Record> rs = Db.use("default").find(
				"select id from gcms_param_info where key = ? and pid=?",
				new Object[] { subkey, records.get(0).getStr("id") });
		List<Record> list = null;
		if (rs != null && !rs.isEmpty()) {
			list = new ArrayList<Record>();
			;
			for (int i = 0; i < rs.size(); i++) {
				List<Record> ss = Db
						.use("default")
						.find("select val,remark from gcms_param_info where key = ? and pid=?",
								new Object[] { thirdsubkey,
										rs.get(0).getStr("id") });
				if (ss != null && !ss.isEmpty()) {
					list.addAll(ss);
				}
			}
		}
		return list;
	}

	public String saveStandardInfo(String id,String standard_flag, String data_flag,
			String standard_remark, String second_org_num, String third_org_num, String removeConjudges, String removeCons, String removeRatios, String removeConendjudges) {
		int flag=0;
		if(AppUtils.StringUtil(id)!=null){
			flag = Db
					.use("default")
					.update("update pccm_standard_info set standard_flag=?,data_flag=?,standard_remark=?,modify_time=?,second_org_num=?,third_org_num=? where id=?",
							new Object[] {standard_flag,data_flag,standard_remark,BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"),second_org_num,third_org_num,id});
		}else{
			id=AppUtils.getStringSeq();
			flag = Db
					.use("default")
					.update("insert into pccm_standard_info (id,standard_flag,data_flag,standard_remark,create_time,second_org_num,third_org_num) values(?,?,?,?,?,?,?)",
							new Object[] {id, standard_flag,data_flag,standard_remark,BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"),second_org_num,third_org_num});
		}
		if(flag<=0){
			id="";
		}
		return id;
	}

	public void delPccmStandardCondition(String standard_id) {
		Db.use("default").update("delete from pccm_standard_condition where standard_id=?",new Object[]{standard_id});
	}

	public void saveCons(String condition, int step, String symbol,
			String rel_symbol, String standard_id) {
		Db.use("default").update("insert into pccm_standard_condition (id,condition,symbol,rel_symbol,step,standard_id,param_flag) values(?,?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),condition,symbol,rel_symbol,step,standard_id,1});
	}

	public void saveConjudges(String condition_val, int step, String symbol,
			String rel_symbol, String standard_id) {
		Db.use("default").update("insert into pccm_standard_condition (id,condition_val,symbol,rel_symbol,step,standard_id,param_flag) values(?,?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),condition_val,symbol,rel_symbol,step,standard_id,2});
	}

	public void saveRatios(String ratio_name, String ratio_val, int step,
			String symbol, String standard_id) {
		Db.use("default").update("insert into pccm_standard_condition(id,ratio_name,ratio_val,symbol,step,standard_id,param_flag) values (?,?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),ratio_name,ratio_val,symbol,step,standard_id,3});
	}

	public void saveConendjudges(String condition_end_val, int step,
			String symbol, String rel_symbol, String standard_id) {
		Db.use("default").update("insert into pccm_standard_condition(id,condition_end_val,symbol,rel_symbol,step,standard_id,param_flag) values(?,?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),condition_end_val,symbol,rel_symbol,step,standard_id,4});
	}

	public String saveStandard(Record record) {
		String id=record.getStr("id");
		String hibnk="000000000";
		if(AppUtils.StringUtil(id)!=null){
			Db.use("default").update("delete from pccm_standard_condition where standard_id=?",new Object[]{id});
			Db.use("default").update("update pccm_standard_info set standard_flag=?,second_org_num=?,third_org_num=?,modify_time=?,hibnk=? where id=?",new Object[]{record.getStr("standard_flag"),record.getStr("second_org_num"),record.getStr("third_org_num"),BolusDate.getDateTime("yyyy-MM-dd"),hibnk,id});
		}else{
			id=AppUtils.getStringSeq();
			Db.use("default").update("insert into pccm_standard_info(id,standard_flag,create_time,second_org_num,third_org_num,hibnk) values(?,?,?,?,?,?)",new Object[]{id,record.getStr("standard_flag"),BolusDate.getDateTime("yyyy-MM-dd"),record.getStr("second_org_num"),record.getStr("third_org_num"),hibnk});
		}
		return id;
	}

	public void saveStandardCondition(Record r) {
		Db.use("default").update("insert into pccm_standard_condition(id,symbol1,condition_val1,symbol2,condition_val2,condition_flag,standard_remark,standard_id,result_value) values(?,?,?,?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),r.getStr("symbol1"),r.getStr("condition_val1"),r.getStr("symbol2"),r.getStr("condition_val2"),r.getStr("condition_flag"),r.getStr("standard_remark"),r.getStr("standard_id"),r.getStr("result_value")});
	}

	public Record getStandardInfo(String id) {
		String sql = "select * from pccm_standard_info p where 1=1";
		StringBuffer sb = new StringBuffer();
		List<String> listStr = new ArrayList<String>();
		if (AppUtils.StringUtil(id) != null) {
			sb.append(" and p.id = ? ");
			listStr.add(id);
		}
		List<Record> list = Db.use("default").find(sql + sb.toString(),
				listStr.toArray());
		Record record = list.get(0);
		return record;
	}

	public List<Record> getStandardCondition(String id) {
		List<Record>records=Db.use("default").find("select a.* from (select c.condition_flag,c.symbol1,c.condition_val1,c.symbol2,c.condition_val2,c.standard_remark,c.result_value,p.id from pccm_standard_info p,pccm_standard_condition c where p.id=c.standard_id) a left join (select name from gcms_param_info where key='STANDARD_TYPE') b on a.symbol1=b.name left join (select name from gcms_param_info where key='STANDARD_TYPE') c on a.symbol2=c.name where a.id=?",new Object[]{id});
		return records;
	}


}
