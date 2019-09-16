/**
 * 
 */
package com.goodcol.util.zxgldbutil;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;

/**
 * @author dinggang
 * 
 */
public class PccmCustTranDBUtil {
	private static final String DEFAULT = "default";

	public List<Record> findPoolRecords() {
		List<Record> records = Db
				.use(DEFAULT)
				.find("select * from pccm_cust_pool where (incflg='1' or incflg='2' or incflg='3') and customername is not null"
						+ " and customername!='null' and customername!='NULL' and (area_code is null or area_code='null' or area_code='NULL')"
						+ " and dummy_cust_no is not null and dummy_cust_no!='null' and dummy_cust_no!='NULL'");
		return records;
	}

	public String findYjUrl() {
		String yj_api_detail_url = Db.use(DEFAULT).queryStr(
				"select remark from gcms_param_info where key=?",
				new Object[] { "YJ_API_DETAIL_URL" });
		return yj_api_detail_url;
	}

	public void updatePool(String creditCode, Record record) {
		String updateSql = "";
		if (StringUtils.isNotBlank(record.getStr("REGADDR"))) {
			updateSql += (", REGADDR ='" + record.getStr("REGADDR") + "'");
		}
		if (StringUtils.isNotBlank(record.getStr("REGDATE"))) {
			updateSql += (", REGDATE ='" + record.getStr("REGDATE") + "'");
		}
		if (StringUtils.isNotBlank(record.getStr("REGCAPITALCURCDE"))) {
			updateSql += (", REGCAPITALCURCDE ='" + record.getStr("REGCAPITALCURCDE") + "'");
		}
		if (StringUtils.isNotBlank(record.getStr("REGCAPITALAMT"))) {
			updateSql += (", REGCAPITALAMT ='" + record.getStr("REGCAPITALAMT") + "'");
		}
		if (StringUtils.isNotBlank(record.getStr("TELNO"))) {
			updateSql += (", TELNO ='" + record.getStr("TELNO") + "'");
		}
		if (StringUtils.isNotBlank(record.getStr("ARTIFICAL"))) {
			updateSql += (", ARTIFICAL ='" + record.getStr("ARTIFICAL") + "'");
		}
		if (StringUtils.isNotBlank(record.getStr("IDYCLS"))) {
			updateSql += (", IDYCLS ='" + record.getStr("IDYCLS") + "'");
		}
		if (StringUtils.isNotBlank(record.getStr("WORKSCOPE"))) {
			updateSql += (", WORKSCOPE ='" + record.getStr("WORKSCOPE") + "'");
		}
		if (StringUtils.isNotBlank(record.getStr("ISONMARKET"))) {
			updateSql += (", ISONMARKET ='" + record.getStr("ISONMARKET") + "'");
		}
		Db.use(DEFAULT).update("update pccm_cust_pool set area_code= '" + creditCode + "' " + updateSql + " where id='" + record.getStr("id") + "'");
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
}
