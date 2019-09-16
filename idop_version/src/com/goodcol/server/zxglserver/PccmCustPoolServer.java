/**
 * 
 */
package com.goodcol.server.zxglserver;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.YiApiTool;

/**
 * @author dinggang
 * 
 */
public class PccmCustPoolServer implements Runnable {
	public static Logger log = Logger.getLogger(PccmCustPoolServer.class);
	private List<Record> pools;
	private int i;
	private String yj_api_detail_url;
	private String url;
	private String xyd_edmp_key;

	public PccmCustPoolServer(List<Record> pools, int i, String yj_api_detail_url, String url, String xyd_edmp_key) {
		this.pools = pools;
		this.i = i;
		this.yj_api_detail_url = yj_api_detail_url;
		this.url = url;
		this.xyd_edmp_key = xyd_edmp_key;
	}

	@Override
	public void run() {
		PccmCustTranServer pccmCustTranServer = new PccmCustTranServer();
		Record record = pools.get(i);
		String customername = record.getStr("customername");
		String yiApiUrl = yj_api_detail_url + customername;
		log.warn("企查查地址:" + yiApiUrl);
		Map<String, Object> mp = YiApiTool.getCompanyDetail(yiApiUrl, url, xyd_edmp_key);
		Map<String, Object> resultMap = (Map<String, Object>) mp.get("Result");
		if (mp != null) {
			if ("200".equals(mp.get("Status").toString())) {
				String CreditCode = resultMap.get("CreditCode").toString();// 区域代码
				String regAddress = formatObject(resultMap.get("Address"));// 注册地址
				String regDate = DateTimeUtil.changeDateString(formatObject(resultMap.get("StartDate")));// 注册日期
				String scope = formatObject(resultMap.get("Scope"));// 经营范围
				String tel = ""; // 联系电话
				Map<String, Object> tels = new HashMap<String, Object>();
				if (null != resultMap.get("ContactInfo")) {
					tels = (Map) (resultMap.get("ContactInfo"));
					if (null != tels.get("PhoneNumber")) {
						tel = String.valueOf(tels.get("PhoneNumber"));
					}
				}
				String amt = formatObject(resultMap.get("RegistCapi"));
				String regAmt = "";//注册资本
				String regCurcde = "";//注册币种
				if (!StringUtils.isEmpty(amt)) {
					try {
						String CNY = "人民币";
						String GBP = "英镑";
						String HKD = "港币";
						String USD = "美元";
						String CHF = "瑞士法郎";
						String SGD = "新加坡元";
						String SEK = "瑞典克朗";
						String DKK = "丹麦克朗";
						String NOK = "挪威克朗";
						String JPY = "日元";
						String CAD = "加元";
						String AUD = "澳大利亚元";
						String EUR = "欧元";
						String MOP = "澳门元";
						String NZD = "新西兰元";
						String KRW = "韩圆";

						if (amt.indexOf(CNY) > -1) {// CNY人民币
							regCurcde= "CNY";
							amt = amt.replace(CNY, "");
						} else if (amt.indexOf(GBP) > -1) {// GBP英镑
							regCurcde="GBP";
							amt = amt.replace(GBP, "");
						} else if (amt.indexOf(HKD) > -1) {// HKD港币
							regCurcde="HKD";
							amt = amt.replace(HKD, "");
						} else if (amt.indexOf(USD) > -1) {// USD美元
							regCurcde="USD";
							amt = amt.replace(USD, "");
						} else if (amt.indexOf(CHF) > -1) {// CHF瑞士法郎
							regCurcde="CHF";
							amt = amt.replace(CHF, "");
						} else if (amt.indexOf(SGD) > -1) {// SGD新加坡元
							regCurcde="SGD";
							amt = amt.replace(SGD, "");
						} else if (amt.indexOf(SEK) > -1) {// SEK瑞典克朗
							regCurcde="SEK";
							amt = amt.replace(SEK, "");
						} else if (amt.indexOf(DKK) > -1) {// DKK丹麦克朗
							regCurcde="DKK";
							amt = amt.replace(DKK, "");
						} else if (amt.indexOf(NOK) > -1) {// NOK挪威克朗
							regCurcde="NOK";
							amt = amt.replace(NOK, "");
						} else if (amt.indexOf(JPY) > -1) {// JPY日元
							regCurcde="JPY";
							amt = amt.replace(JPY, "");
						} else if (amt.indexOf(CAD) > -1) {// CAD加元
							regCurcde="CAD";
							amt = amt.replace(CAD, "");
						} else if (amt.indexOf(AUD) > -1) {// AUD澳大利亚元
							regCurcde="AUD";
							amt = amt.replace(AUD, "");
						} else if (amt.indexOf(EUR) > -1) {// EUR欧元
							regCurcde="EUR";
							amt = amt.replace(EUR, "");
						} else if (amt.indexOf(MOP) > -1) {// MOP澳门元
							regCurcde="MOP";
							amt = amt.replace(MOP, "");
						} else if (amt.indexOf(NZD) > -1) {// NZD新西兰元
							regCurcde="NZD";
							amt = amt.replace(NZD, "");
						} else if (amt.indexOf(KRW) > -1) {// KRW韩圆
							regCurcde="KRW";
							amt = amt.replace(KRW, "");
						} else {
							regCurcde="CNY";
						}
						if (amt.indexOf("万") > -1) {
							amt = amt.replace("万","");
							amt = amt.replace("元","");
							amt = amt.replace("整","");
							regAmt = new BigDecimal(Double.parseDouble(amt) * 10000).toString();
						} else {
							regAmt= amt;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				String operName = formatObject(resultMap.get("OperName"));// 法人名称
				String isOnStock = formatObject(resultMap.get("IsOnStock"));// 是否上市 0：否，1：是
				
				Map<String, Object> industryMap = new HashMap<String, Object>();
				String idycls = "";
				if (null != resultMap.get("Industry")) {
					industryMap = (Map) (resultMap.get("Industry"));
					if (null != industryMap.get("SubIndustry")&& StringUtils.isNotBlank(industryMap.get("SubIndustry").toString())) {
						idycls = Db.use("default").queryStr("select code from PCCM_INDUSTRY_INFO where INDUSTRY_NAME = '" +industryMap.get("SubIndustry").toString().trim()+"'");
					}
				}
				System.out.println("社会统一信用代码：" + CreditCode);
				CreditCode = CreditCode.substring(2, 8);
				System.out.println(record.getStr("id"));
				record.set("REGADDR", regAddress);
				record.set("REGDATE", regDate);
				record.set("REGCAPITALCURCDE", regCurcde);
				record.set("REGCAPITALAMT", regAmt);
				record.set("TELNO", tel);
				record.set("ARTIFICAL", operName);
				record.set("WORKSCOPE", scope);
				record.set("IDYCLS", idycls);
				record.set("ISONMARKET", isOnStock);
				pccmCustTranServer.updatePool(CreditCode, record);
			}
		}
	}

	private String formatObject(Object obj) {
		if (null != obj) {
			return obj.toString();
		}
		return "";
	}

}
