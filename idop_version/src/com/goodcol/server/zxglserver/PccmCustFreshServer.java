/**
 * 
 */
package com.goodcol.server.zxglserver;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import sun.security.krb5.internal.SeqNumber;

import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.YiApiTool;
import com.goodcol.util.zxgldbutil.PccmCustFreshDBUtil;

/**
 * @author dinggang
 *
 */
public class PccmCustFreshServer {
	public static Logger log = Logger.getLogger(PccmCustFreshServer.class);
	private PccmCustFreshDBUtil custFreshDBUtil=new PccmCustFreshDBUtil();
	

	public String findYjUrl() {
		return custFreshDBUtil.findYjUrl();
	}


	public String findXydUrl() {
		return custFreshDBUtil.findXydUrl();
	}

	public String findXydKey() {
		return custFreshDBUtil.findXydKey();
	}
	
	public String findCust(String customername) {
		return custFreshDBUtil.findCust(customername);
	}

	private void insertCust(Record record) {
		custFreshDBUtil.insertCust(record);
	}
	
	public String searchFresh(String pageIndex){
		if(null==pageIndex||"".equals(pageIndex)){
			pageIndex = "1";
		}
		Map<String, Object> resMap=new HashMap<String, Object>();
		String yestoday = DateTimeUtil.getYestoday();
		PccmCustFreshServer pccmCustFreshServer = new PccmCustFreshServer();
		String url = pccmCustFreshServer.findXydUrl();
		log.warn("信雅达转发地址:" + url);
		String xyd_edmp_key = pccmCustFreshServer.findXydKey();
		log.warn("信雅达Key:" + xyd_edmp_key);
		String yj_api_refresh_url = pccmCustFreshServer.findYjUrl();
		String yiApiUrl = yj_api_refresh_url +"JS"+"&startDateFrom="+yestoday+"&startDateTo="+yestoday+"&pageSize=20"+"&pageIndex="+pageIndex;;
		log.warn("企查查新增企业URL:" + yiApiUrl);
		Map<String, Object> mp = YiApiTool.getCompanyDetail(yiApiUrl,url,xyd_edmp_key);
		ArrayList<Map<String,Object>> resultList = (ArrayList)mp.get("Result");
		String  status = null;
		status = (String)mp.get("Status");
		if(status.equals("200")){
			Record record = new Record();
			for(int i=0;i<resultList.size();i++){
				HashMap resultMap=(HashMap) resultList.get(i);
				String customername = resultMap.get("Name").toString();//公司名称
				//查询pccm_cust_pool中是否有重复数据
				String id = pccmCustFreshServer.findCust(customername);
				if(id==null || "".equals(id)){
					String CreditCode = resultMap.get("CreditCode").toString();// 区域代码
					String regAddress = formatObject(resultMap.get("Address"));// 注册地址
					String regDate = DateTimeUtil.changeDateString(formatObject(resultMap.get("StartDate")));// 注册日期
					String operName = formatObject(resultMap.get("OperName"));// 法人名称
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
					System.out.println("社会统一信用代码：" + CreditCode);
					String area_code = CreditCode.substring(2, 8);//区域代码
					String datadate = DateTimeUtil.getPathName();
					String nub = ""+System.currentTimeMillis();
					nub = nub.substring(2,12);
					String dummy_cust_no = "Q"+nub;//虚拟客户号
					record.set("ID", AppUtils.getStringSeq());
					record.set("INCFLG", "6");
					record.set("REGADDR", regAddress);
					record.set("REGDATE", regDate);
					record.set("REGCAPITALCURCDE", regCurcde);
					record.set("REGCAPITALAMT", regAmt);
					record.set("ARTIFICAL", operName);
					record.set("CUSTOMERNAME", customername);
					record.set("AREA_CODE", area_code);
					record.set("DATA_DATE", datadate);
					record.set("INDATE", datadate);
					record.set("DUMMY_CUST_NO", dummy_cust_no);
					pccmCustFreshServer.insertCust(record);
				}
			}
			
			
		}
	
		return status;
	}
	

	private String formatObject(Object obj) {
		if (null != obj) {
			return obj.toString();
		}
		return "";
	}
}
