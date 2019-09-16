/**
 * 
 */
package com.goodcol.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import msg.SMSServiceSoap;
import msg.SMSServiceSoapService;

import org.apache.commons.lang.StringUtils;

import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;

/**
 * @author dinggang
 * 
 */
public class SendMessageUtil {
	public static Logger log = Logger.getLogger(SendMessageUtil.class);
	public static String appCode = AppUtils.findDictRemark("pccm_msg", "3");
	public static String appPwd = AppUtils.findDictRemark("pccm_msg", "4");
	public static String priority = AppUtils.findDictRemark("pccm_msg", "5");
	public static String systemName = AppUtils.findDictRemark("pccm_msg", "6");
	
	
	private static String getFlag() {
		return Db.use("default").findFirst(" select val from gcms_param_info where key = 'MESSAGE_FLAG'").getStr("val");
	}

	/**
	 * 发送短信
	 * 
	 * @param mobile
	 * @param mob_cnt
	 * @param sm_cont
	 * @param map
	 * @return
	 */
	public static String sendMessage(String mobile, String mob_cnt,
			String sm_cont, Map<String, Object> map) {
		String mana_id = numLeftAddZero("07140", 20, 0);
		String rspCode = "01";
		int sm_contLen = sm_cont.length() * 2;
		if (!(sm_cont.length() <= 70 && sm_contLen <= 141)) {
			int total = 0;
			if (sm_cont.length() % 70 == 0) {
				total = sm_cont.length() / 70;
			} else {
				total = sm_cont.length() / 70 + 1;
			}
			for (int i = 0; i < total; i++) {
				String subStr = sm_cont.substring(i * 70, sm_cont.length());
				String msg = "";
				if (subStr.length() > 0 && subStr.length() < 70) {
					msg = sm_cont.substring(i * 70, sm_cont.length());
				} else {
					msg = sm_cont.substring(i * 70, i * 70 + 70);
				}
				rspCode = sendMessage(mana_id, mob_cnt, mobile, msg, map);
			}
		} else {
			rspCode = sendMessage(mana_id, mob_cnt, mobile, sm_cont, map);
		}
		return rspCode;
	}

	/**
	 * 
	 * @param mana_id
	 * @param mob_cnt
	 * @param mobile
	 * @param sm_cont
	 * @param map
	 * @return
	 */
	private static String sendMessage(String mana_id, String mob_cnt,
			String mobile, String sm_cont, Map<String, Object> map) {
		try {
			if ("0".equals(getFlag())) {
				StringBuffer sbf = new StringBuffer();
				sbf.append(mana_id);// CRM编号
				sbf.append(strLeftAddSpace(sm_cont,141));// 短信内容
				sbf.append(mob_cnt);// 目标手机号的个数
				sbf.append(mobile);// 手机号
				String length = (4 + sbf.toString().getBytes("gbk").length)
						+ "";
				System.out.println("发送地址、端口:" + map.get("ip") + ":" + map.get("port"));
				System.out.println("发送报文:" + sbf.toString());
				SocketClient sc = new SocketClient();
				String rspCode = sc.client(numLeftAddZero(length, 4, 0) + sbf.toString(),
						map.get("ip").toString(), Integer.parseInt(map.get("port").toString()));
				System.out.println("返回报文:" + rspCode);
				return rspCode;
			} else {
				int num = Integer.parseInt(mob_cnt);
				for (int i = 0; i < num; i++) {
					String phone = mobile.substring(i * 11, (i + 1) * 11);
					String id = AppUtils.getStringSeq();
					Db.use("default").update(" insert into PCCM_MSG_INFO (ID,MOBILE,SM_CONT,SEND_STATUS,DATA_DATE ) values (?,?,?,?,?)",
									new Object[] { id, phone, sm_cont, "0", DateTimeUtil.getNowDate() });
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 发送短信
	 * 
	 * @param userInfos
	 * @param sm_cont
	 */
	public static void sendMessage(List<Record> userInfos, String sm_cont,
			Map<String, Object> map) {
		int mobCnt = userInfos.size();// 手机号总个数
		int part = 0;
		int size = 9;// 手机批量发送每次9个
		if (mobCnt <= size) {// 手机总个数小于等于9
			sendMessageDetail(userInfos, sm_cont, map);
		} else {// 手机总个数大于9
			if (mobCnt % size == 0) {
				part = mobCnt / size;
			} else {
				part = mobCnt / size + 1;
			}
			for (int i = 0; i < part; i++) {
				List<Record> records = null;
				if (i == 0) {
					records = userInfos.subList(0, size);// 当第一次循环的时候，手机总个数肯定大于9，所以直接截取前9个手机号
				} else {// 当i>0时，先判断剩下的手机号个数是否大于9，如果大于9则继续截取前9个手机号
					records = userInfos.subList(i * size, mobCnt);
					if (records.size() > size) {
						records = userInfos.subList(i * size, i * size + size);
					}
				}
				sendMessageDetail(records, sm_cont, map);
			}
		}
	}

	/**
	 * 发送短信详情
	 * 
	 * @param records
	 * @param sm_cont
	 * @param map
	 */
	public static void sendMessageDetail(List<Record> records, String sm_cont,
			Map<String, Object> map) {
//		int mobSize = records.size();
//		StringBuffer mobileStr = new StringBuffer();
//		for (int s = 0; s < records.size(); s++) {
//			Record record = records.get(s);
//			mobileStr.append(record.getStr("phone"));
//		}
//		String mobile = mobileStr.toString();
//		String rspCode = SendMessageUtil.sendMessage(mobile, "0" + mobSize,
//				sm_cont, map);
//		if (!"00".equals(rspCode)) {
//			System.out.println("返回应答码：" + rspCode + "；手机号：" + mobile
//					+ "；目标手机号的个数：" + mobSize + "；短信内容:" + sm_cont);
//			log.warn("返回应答码：" + rspCode + "；手机号：" + mobile + "；目标手机号的个数："
//					+ mobSize + "；短信内容:" + sm_cont);
//		} else {
//			log.warn("成功发送短信：" + "手机号：" + mobile + "；目标手机号的个数：" + mobSize
//					+ "；短信内容:" + sm_cont);
//		}
		//8888
		Map<String, Object> msgMap = null;
		for (int s = 0; s < records.size(); s++) {
			Record record = records.get(s);
			msgMap = new HashMap<>();
			msgMap.put("phone",record.getStr("phone"));
			msgMap.put("user_no",record.getStr("user_no"));
			msgMap.put("name",record.getStr("name"));
			sendNewMessage(sm_cont, msgMap);
		}
	}

	/**
	 * 左补零0
	 * 
	 * @param str
	 * @param len
	 * @param len1
	 * @return
	 */
	private static String numLeftAddZero(String str, int len, int len1) {
		String zero = "0000000000";
		String result1 = "";
		String result2 = "";
		boolean flag = false;
		if (StringUtils.isNotBlank(str)) {
			if (str.indexOf(".") > -1) {
				flag = true;
				result1 = str.substring(0, str.lastIndexOf("."));
			} else {
				result1 = str;
			}
			if (result1.length() < len) {
				result1 = String.format("%0" + len + "d",
						Long.parseLong(result1));
			} else {
				result1 = result1.substring(0, len);
			}
		} else {
			result1 = String.format("%0" + len + "d", 0);
		}
		if (len1 != 0) {
			if (flag) {
				result2 = str.substring(str.lastIndexOf(".") + 1);
				if (len1 - result2.length() > 0) {
					result2 += zero.substring(0, len1 - result2.length());
				}
			} else {
				result2 += zero.substring(0, len1);
			}
		}
		return result1 + result2;
	}
	
	/***************************************************************************
	 * 右补空格
	 * 
	 * @param str
	 * @param len
	 * @return
	 */
	private static String strLeftAddSpace(String str, int len) {
		String strSpace = "";
		if (!StringUtils.isEmpty(str) && null != str && !"null".equals(str)) {
			try {
				int gbkLen = str.getBytes("gbk").length;
				if (gbkLen < len) {
					for (int i = 0; i < len - gbkLen; i++) {
						strSpace += " ";
					}
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			str = "";
			for (int i = 0; i < len; i++) {
				strSpace += " ";
			}
		}
		return str + strSpace;
	}
	
	/**
	 * 发送短信，短信正文不能超过65个字符
	 */
	public static String sendNewMessage(String sm_cont, Map<String, Object> map) {
		String rspCode = "";
		if (sm_cont.length() >65) {
			int total = 0;
			if (sm_cont.length() % 65 == 0) {
				total = sm_cont.length() / 65;
			} else {
				total = sm_cont.length() / 65 + 1;
			}
			for (int i = 0; i < total; i++) {
				String subStr = sm_cont.substring(i * 65, sm_cont.length());
				String msg = "";
				if (subStr.length() > 0 && subStr.length() < 65) {
					msg = sm_cont.substring(i * 65, sm_cont.length());
				} else {
					msg = sm_cont.substring(i * 65, i * 65 + 65);
				}
				rspCode = sendNewMsg(msg, map);
			}
		} else {
			rspCode = sendNewMsg(sm_cont, map);
		}
		return rspCode;
	}
	
	/**
	 * @return
	 */
	public static String sendNewMsg(String sm_msg, Map<String, Object> map) {
		try {
			String send_status = "0";//发送状态（0：未发送，1：已发送，2：发送失败）
			String result=null;
			//短信开关0开1关 MESSAGE_FLAG
			if ("0".equals(getFlag())) {
				SMSServiceSoapService service=new SMSServiceSoapService();
				SMSServiceSoap serviceSoap=service.getSMSServiceSoap();
				result = serviceSoap.smsAdd(appCode, appPwd,
						String.valueOf(map.get("phone")), String.valueOf(map.get("user_no")),
						String.valueOf(map.get("name")), sm_msg, priority,
						systemName);
				//result:0成功SMS1001短信正文超长 SMS1002系统编码不能为空 SMS1003编码不能为空 SMS1005
				//系统编号不存在或者密码为空 SMS1008 Exception Message
				if("0".equals(result)){
					send_status = "1";
				}else{
					send_status = "2";
					//发送失败将返回码存到短信内容里
					sm_msg = result+"_"+sm_msg;
				}
			}
			Db.use("default").update(" insert into PCCM_MSG_INFO (ID,MOBILE,SM_CONT,SEND_STATUS,DATA_DATE,USER_NO ) values (?,?,?,?,?,?)",
					new Object[] { AppUtils.getStringSeq(), map.get("phone"), sm_msg, send_status, DateTimeUtil.getNowDate(),map.get("user_no")});
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*******************短信发送方式修改成先入表，再生成txt文本方式20190521************/
	
	
	/**
	 * 发送短信
	 */
	public static void sendMsgList(List<Record> userInfos, Map<String, Object> map) {
		int mobCnt = userInfos.size();// 总个数
		int part = 0;
		int size = 50;// 批量每次50个 防止内存溢出
		if (mobCnt <= size) {
			sendMsgArray(userInfos, map);
		} else {
			if (mobCnt % size == 0) {
				part = mobCnt / size;
			} else {
				part = mobCnt / size + 1;
			}
			for (int i = 0; i < part; i++) {
				List<Record> records = null;
				if (i == 0) {
					records = userInfos.subList(0, size);// 当第一次循环的时候，手机总个数肯定大于50，所以直接截取前50个手机号
				} else {// 当i>0时，先判断剩下的手机号个数是否大于50，如果大于50则继续截取前50个手机号
					records = userInfos.subList(i * size, mobCnt);
					if (records.size() > size) {
						records = userInfos.subList(i * size, i * size + size);
					}
				}
				sendMsgArray(userInfos,map);
			}
		}
	}
	
	public static void sendMsgArray(List<Record> records, Map<String, Object> map) {
		Map<String, Object> msgMap = null;
		for (int s = 0; s < records.size(); s++) {
			Record record = records.get(s);
			msgMap = new HashMap<>();
			msgMap.put("phone",record.getStr("phone"));
			msgMap.put("user_no",record.getStr("user_no"));
			msgMap.put("name",record.getStr("name"));
			msgMap.put("sm_cont",  map.get("sm_cont"));
			msgMap.put("sm_job",  map.get("sm_job"));
			if("PccmMsgLastMonBusiJob".equals(map.get("sm_job"))){
				msgMap.put("smg_part","1");
				msgMap.put("sm_key",map.get("sm_key1"));
				sendMsgInfo(msgMap);
				msgMap.put("smg_part","2");
				msgMap.put("sm_key",map.get("sm_key2"));
				sendMsgInfo(msgMap);
			}else{
//				msgMap.put("smg_part","1");
				msgMap.put("sm_key", map.get("sm_key"));
				sendMsgInfo(msgMap);
			}
		}
	}
	
	/**
	 * @return
	 */
	public static void sendMsgInfo(Map<String, Object> map) {
		try {
			String send_status = "0";//发送状态（0：未发送，1：已发送，2：发送失败）
			Db.use("default").update(" insert into PCCM_MSG_INFO (ID,MOBILE,SM_CONT,SEND_STATUS,DATA_DATE,USER_NO,USER_NAME," +
					"SM_KEY,SM_JOB,SMG_PART,SEND_TYPE ) values (?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { AppUtils.getStringSeq(), map.get("phone"), map.get("sm_cont"), send_status, DateTimeUtil.getNowDate(), 
					map.get("user_no"), map.get("name"), map.get("sm_key"), map.get("sm_job"), map.get("smg_part"),"2"});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
