/**
 * 
 */
package com.goodcol.util.zxgldbutil;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;

/**
 * @author xingpengcheng
 * 
 */
public class PccmCustFreshDBUtil {
	private static final String DEFAULT = "default";

	public String findYjUrl() {
		String yj_api_detail_url = Db.use(DEFAULT).queryStr(
				"select remark from gcms_param_info where key=?",
				new Object[] { "YJ_API_REFRESH_URL" });
		return yj_api_detail_url;
	}


	public String findXydUrl() {
		String url = Db.use("default").queryStr(
				"select remark from gcms_param_info where key=?",
				new Object[] { "XYD_EDMP_URL" });
		return url;
	}

	public String findXydKey() {
		String xyd_key = Db.use("default").queryStr(
				"select remark from gcms_param_info where key=?",
				new Object[] { "XYD_EDMP_KEY" });
		return xyd_key;
	}
	
	public String findCust(String customername){
		String id = Db.use("default").queryStr(
				"select id from pccm_cust_pool where customername = ?", 
				new Object[] { customername });
		return id ;
		
	}


	public void insertCust(Record record) {
		Db.use("default").update("insert into PCCM_CUST_POOL(ID,INCFLG,INDATE,REGADDR,REGDATE,REGCAPITALCURCDE,REGCAPITALAMT,ARTIFICAL,CUSTOMERNAME,AREA_CODE,DATA_DATE,DUMMY_CUST_NO) values(?,?,?,?,?,?,?,?,?,?,?,?)",
				new Object[]{record.getStr("ID"),record.getStr("INCFLG"),record.getStr("INDATE"),record.getStr("REGADDR"),record.getStr("REGDATE"),
				record.getStr("REGCAPITALCURCDE"),record.getStr("REGCAPITALAMT"),record.getStr("ARTIFICAL"),record.getStr("CUSTOMERNAME"),
				record.getStr("AREA_CODE"),record.getStr("DATA_DATE"),record.getStr("DUMMY_CUST_NO")});
	}
}
