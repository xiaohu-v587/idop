/**
 * 
 */
package com.goodcol.server.zxglserver;

import java.util.List;

import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.zxgldbutil.PccmStandardDBUtil;

/**
 * @author dinggang
 *
 */
public class PccmStandardServer {
private static PccmStandardDBUtil standardDBUtil=new PccmStandardDBUtil();
	public Page<Record> getList(Record record) {
		
		return standardDBUtil.getList(record);
	}

	public void del(String ids) {
		PccmStandardDBUtil standardDBUtil=new PccmStandardDBUtil();
		standardDBUtil.del(ids);
	}

	public List<Record> getDict(String key, String type) {
		PccmStandardDBUtil standardDBUtil=new PccmStandardDBUtil();
		return standardDBUtil.getDict(key,type);
	}

	public List<Record> getSecondOrg() {
		PccmStandardDBUtil standardDBUtil=new PccmStandardDBUtil();
		return standardDBUtil.getSecondOrg();
	}

	public List<Record> getThridOrg(String orgnum) {
		PccmStandardDBUtil standardDBUtil=new PccmStandardDBUtil();
		return standardDBUtil.getThridOrg(orgnum);
	}

	public List<Record> getDataFlag(String flag, String key, String subkey) {
		PccmStandardDBUtil standardDBUtil=new PccmStandardDBUtil();
		return standardDBUtil.getDataFlag(flag,key,subkey);
	}

	public List<Record> getStandardFields(String data_flag_val, String key, String subkey, String thirdsubkey) {
		PccmStandardDBUtil standardDBUtil=new PccmStandardDBUtil();
		return standardDBUtil.getStandardFields(data_flag_val,key,subkey,thirdsubkey);
	}

	public String saveStandardInfo(String id,String standard_flag, String data_flag,
			String standard_remark, String second_org_num, String third_org_num, String removeConjudges, String removeCons, String removeRatios, String removeConendjudges) {
		PccmStandardDBUtil standardDBUtil=new PccmStandardDBUtil();
		return standardDBUtil.saveStandardInfo(id,standard_flag,data_flag,standard_remark,second_org_num,third_org_num, removeConjudges, removeCons, removeRatios,removeConendjudges);
	}

	public void delPccmStandardCondition(String standard_id) {
		PccmStandardDBUtil standardDBUtil=new PccmStandardDBUtil();
		standardDBUtil.delPccmStandardCondition(standard_id);
	}

	public void saveCons(String condition, int step, String symbol,
			String rel_symbol, String standard_id) {
		PccmStandardDBUtil standardDBUtil=new PccmStandardDBUtil();
		standardDBUtil.saveCons(condition,step,symbol,rel_symbol,standard_id);
	}

	public void saveConjudges(String condition_val, int step, String symbol,
			String rel_symbol, String standard_id) {
		PccmStandardDBUtil standardDBUtil=new PccmStandardDBUtil();
		standardDBUtil.saveConjudges(condition_val,step,symbol,rel_symbol,standard_id);
		
	}


	public void saveRatios(String ratio_name, String ratio_val, int step,
			String symbol, String standard_id) {
		PccmStandardDBUtil standardDBUtil=new PccmStandardDBUtil();
		standardDBUtil.saveRatios(ratio_name,ratio_val,step,symbol,standard_id);
	}

	public void saveConendjudges(String condition_end_val, int step,
			String symbol, String rel_symbol, String standard_id) {
		PccmStandardDBUtil standardDBUtil=new PccmStandardDBUtil();
		standardDBUtil.saveConendjudges(condition_end_val,step,symbol,rel_symbol,standard_id);
	}


	public String saveStandard(Record record) {
		PccmStandardDBUtil standardDBUtil=new PccmStandardDBUtil();
		return standardDBUtil.saveStandard(record);
	}

	public static void saveStandardCondition(Record r) {
		PccmStandardDBUtil standardDBUtil=new PccmStandardDBUtil();
		standardDBUtil.saveStandardCondition(r);
		
	}

	public Record getStandardInfo(String id) {
		PccmStandardDBUtil standardDBUtil=new PccmStandardDBUtil();
		return standardDBUtil.getStandardInfo(id);
	}

	public List<Record> getStandardCondition(String id) {
		PccmStandardDBUtil standardDBUtil=new PccmStandardDBUtil();
		return standardDBUtil.getStandardCondition(id);
	}


}
