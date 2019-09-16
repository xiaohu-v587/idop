package com.goodcol.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;

public class AppUtils {

	private final static Log log = LogFactory.getLog(AppUtils.class);

	public final static SimpleDateFormat simpleFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public final static SimpleDateFormat sf_time = new SimpleDateFormat(
			"HH:mm:ss");
	public final static String dateStr = "yyyy-MM-dd";

	public static String encrypt(String str) {
		if (StringUtils.isNotBlank(str)) {
			try {
				return "->" + Base64.encodeBase64(str.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				log.error(e.getMessage(), e);
			}
		}
		return str;
	}
	/**
	 * 通过key查询列表
	 * @param key
	 * @return
	 */
	public static List<Record> findDictListByKey(String key) {
		List<Record> list = Db.use("default").find("select * from sys_param_info where key = ? ",
				new Object[] { key });
		return list;
	}

	/**
	 * 判断是否每月最早的工作日
	 */
	public static Boolean isMonMinWorkDay() {
		Boolean flag = false;
		Record re = Db.use("default").findFirst(
				"select t.remark from pccm_calendar_info t where substr(t.rq,0,10) = to_char(sysdate,'yyyy-MM-dd')");
		if (null != re && "minWorkday".equals(re.getStr("remark"))) {
			flag = true;
		}
		return flag;
	}
	public static String decrypt(String str) {
		if (StringUtils.isNotBlank(str)) {
			if (str.startsWith("->")) {
				try {
					return new String(Base64.decodeBase64(str.substring(
							"->".length()).getBytes("UTF-8")));
				} catch (UnsupportedEncodingException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		return str;
	}

	/**
	 * 获取本机IP地址
	 * 
	 * @return
	 */
	public static String getIPAddress() {
		String ip = "UNKNOW";
		Enumeration<NetworkInterface> niList = null;
		try {
			niList = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			log.error("获取本机IP地址时发生异常", e);
			return ip;
		}
		boolean isFind = false;
		while (niList.hasMoreElements()) {
			if (isFind) {
				break;
			}
			NetworkInterface nic = niList.nextElement();
			Enumeration<InetAddress> addrs = nic.getInetAddresses();
			while (addrs.hasMoreElements()) {
				InetAddress ia = addrs.nextElement();
				if (!ia.isLoopbackAddress() && ia.isSiteLocalAddress()
						&& ia.getHostAddress().indexOf(":") == -1) {
					ip = ia.getHostAddress();
					isFind = true;
					break;
				}
			}
		}
		return ip;
	}

	/**
	 * 获取当前日期
	 * 
	 * @param startTime
	 * @return
	 */

	public static String getCurrentTimeStr() {

		String startTime = "2011-12-01 00:00:00";

		try {
			Calendar calendar = Calendar.getInstance();

			Date startDate = calendar.getTime();

			startTime = simpleFormat.format(startDate);
		} catch (Exception e) {
			log.error("日期转换出现异常:", e);
		}
		return startTime;
	}

	/**
	 * 获取当前时间，去除秒、毫秒并去整数时间
	 * 
	 * @return
	 */
	public static long getCurrentTime() {
		Calendar calendar1 = Calendar.getInstance();
		// 将秒设置为0
		calendar1.set(Calendar.SECOND, 0);
		calendar1.set(Calendar.MILLISECOND, 0);

		int minuteSection = calendar1.get(Calendar.MINUTE);
		if (minuteSection % 2 > 0) {
			calendar1.set(Calendar.MINUTE, minuteSection + 1);
		}

		long ctime = calendar1.getTimeInMillis();
		return ctime;
	}

	/**
	 * 判断是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 将字符串转化为数字
	 * 
	 * @param str
	 * @return
	 */
	public static int parseStr2Num(String str) {
		int num = 0;
		if (str != null) {
			try {
				num = Integer.parseInt(str.trim());
			} catch (Exception e) {
				log.error("异常：" + e);
			}
		}
		return num;
	}

	/**
	 * 将字符串转化为double
	 * 
	 * @param str
	 * @return
	 */
	public static double parseStr2Double(String str) {
		double num = 0F;
		if (str != null) {
			try {
				num = Double.parseDouble(str.trim());
			} catch (Exception e) {
				log.error("异常：" + e);
			}
		}
		return num;
	}

	/**
	 * 字符串转化
	 * 
	 * @param str
	 * @return
	 */
	public static String StringUtil(String str) {
		if (str == null || "null".equals(str) || "".equals(str.trim())
				|| "NULL".equals(str)) {
			return null;
		} else {
			return str.trim();
		}
	}

	/**
	 * 获取起始日期
	 * 
	 * @param startTime
	 * @return
	 */

	public static String getStartTime(String startTime) {
		SimpleDateFormat simpleFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();

		// 如果查询日期为空，则将当前日期减去五天作为起始日期
		calendar.add(Calendar.DAY_OF_MONTH, -5);
		calendar.set(Calendar.HOUR_OF_DAY, 00);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 00);
		Date startDate = calendar.getTime();

		if (startTime == null || "null".equals(startTime)) {
			startTime = simpleFormat.format(startDate);
		}
		return startTime;
	}

	/**
	 * 获取结束日期
	 * 
	 * @param endTime
	 * @return
	 */
	public static String getEndTime(String endTime) {
		SimpleDateFormat simpleFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		// 当前时间作为结束日期
		calendar = Calendar.getInstance();
		if (endTime == null || "null".equals(endTime) || "".equals(endTime)) {
			endTime = simpleFormat.format(calendar.getTime());
		}
		return endTime;
	}

	/**
	 * 获取登录用户信息
	 * 
	 * @param request
	 * @return
	 */
	/*
	 * public static UserInforDto getLoginUserInfor(HttpServletRequest request){
	 * if (request.getSession().getAttribute("UserInfor") == null) { return
	 * null; }else{ UserInforDto userinfo =(UserInforDto)
	 * request.getSession().getAttribute("UserInfor"); return userinfo; } }
	 */

	/**
	 * 将list元素 转成 逗号隔开的字符串 <功能详细描述>
	 * 
	 * @param list
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String listParseString(String[] list) {
		StringBuilder bd = new StringBuilder();

		for (int i = 0, size = list.length; i < size; i++) {
			if (i == size - 1) {
				bd.append(list[i]);
			} else {
				bd.append(list[i]).append(",");
			}
		}
		return bd.toString();
	}

	/**
	 * 将字符串(日期)转化为date ** <功能详细描述>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static Date getStrParseDate(String dateStr) {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = df.parse(dateStr);// 2011-09-15 09:53:46
		} catch (ParseException e) {
			// e.printStackTrace();
			log.error("ParseException:", e);
		}
		return d;
	}

	/**
	 * date类型转String <功能详细描述>
	 * 
	 * @param date
	 * @param formatString
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String dateParseStrByFormatStr(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = df.format(date);
		return str;
	}

	/**
	 * date类型转String <功能详细描述>
	 * 
	 * @param date
	 * @param formatString
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String dateParseStrByFormatStrSt(Date date, String str) {
		SimpleDateFormat df = new SimpleDateFormat(str);
		String str1 = df.format(date);
		return str1;
	}

	/**
	 * 获取字符串的长度，如果有中文，则每个中文字符计为2位
	 * 
	 * @param value
	 *            指定的字符串
	 * @return 字符串的长度
	 */
	public static final int length(String value) {
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		/* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
		for (int i = 0; i < value.length(); i++) {
			/* 获取一个字符 */
			String temp = value.substring(i, i + 1);
			/* 判断是否为中文字符 */
			if (temp.matches(chinese)) {
				/* 中文字符长度为2 */
				valueLength += 2;
			} else {
				/* 其他字符长度为1 */
				valueLength += 1;
			}
		}
		return valueLength;
	}

	/**
	 * 截取中英文混合字符串
	 * 
	 * @param text
	 *            目标字符串
	 * @param length
	 *            截取长度
	 * @param encode
	 *            采用的编码方式
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static final String substring(String text, int length, String encode)
			throws UnsupportedEncodingException {
		if (text == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		int currentLength = 0;
		for (char c : text.toCharArray()) {
			currentLength += String.valueOf(c).getBytes(encode).length;
			if (currentLength <= length) {
				sb.append(c);
			} else {
				break;
			}
		}
		return sb.toString();
	}

	public static String getStringSeq() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "").toUpperCase();
	}

	public static void main(String[] args) {
		System.out.println(getStringSeq());
	}

	/**
	 * 根据组织id获取机构下属机构 flag=1取bancsid 2取org_id
	 */
	public static String getOrgStr(String orgId, String flag) {
		// String level = getOrgLevel(orgId);
		List<Record> list = getOrgList(orgId);
		;
		String orgArr = null;
		// if(Integer.parseInt(level)>3){
		// orgId = getThrOrg(orgId);
		// list = getOrgList(orgId);
		// }else if(Integer.parseInt(level)>1&&Integer.parseInt(level)<=3){
		// list = getOrgList(orgId);
		// }
		if (null != list && list.size() > 0 && !"000000000".equals(orgId)) {// 判断是否省行
			StringBuffer param = new StringBuffer();
			param.append("(");
			for (int i = 0; i < list.size(); i++) {
				param.append("'");
				param.append("1".equals(flag) ? list.get(i).getStr("bancsid")
						: list.get(i).getStr("id"));
				if (i == list.size() - 1) {
					param.append("'");
				} else {
					param.append("',");
				}
			}
			param.append(")");
			orgArr = param.toString();
		}
		return orgArr;
	}

	/**
	 * 读取当前组织级别
	 */
	public static String getOrgLevel(String orgId) {
		String level = Db.use("default").queryStr(
				" select by2 from sys_org_info where id=? ",
				new Object[] { orgId });
		if (StringUtil(level) == null) {
			level = "-1";
		}
		return level;
	}

	/**
	 * 递归查询当前组织所有下级组织
	 */
	public static List<Record> getOrgList(String orgId) {
		List<Record> reList = null;
		// 判断是否省行
		// if(!"000000000".equals(orgId)){
		reList = Db.use("default").find(
				" select id, orgname, upid, by2, qycj, bancsid"
						+ " from sys_org_info where by5 like ? or id = ? "
						+ " order by id,by2 ",
				new Object[] { "%" + orgId + "%", orgId });
		// }
		return reList;
	}

	/**
	 * 下级网点读取支行组织id
	 */
	public static String getThrOrg(String orgId) {
		String subOrg = Db
				.use("default")
				.queryStr(
						" select id "
								+ " from sys_org_info where by2 = '3' start with id = ? connect by prior upid = id ",
						new Object[] { orgId });
		return subOrg;
	}

	/**
	 * 下级网点读取分行组织id
	 */
	public static String getSecOrg(String orgId) {
		String subOrg = Db
				.use("default")
				.queryStr(
						" select id "
								+ " from sys_org_info where by2 = '2' start with id = ? connect by prior upid = id ",
						new Object[] { orgId });
		return subOrg;
	}

	/**
	 * 获取用户审核角色
	 */
	public static String getApplyRole(String userNo) {
		// String subOrg =
		// Db.use("default").queryStr(" select val from sys_user_info u "
		// +" left join gcms_role_apply ga on ga.user_id = u.id "
		// +" left join gcms_param_info gp on gp.id = ga.role_id "
		// +" where u.id=? and ga.apply_status='1' ",
		// new Object[]{userNo});
		String subOrg = Db
				.use("default")
				.queryStr(
						" select kpi_flag from sys_user_info u "
								+ " left join gcms_role_apply ga on ga.user_id = u.id "
								+ " left join sys_role_info sui on sui.id = ga.role_id and kpi_flag is not null "
								+ " where u.id=? ", new Object[] { userNo });
		return subOrg;
	}

	/**
	 * 获取用户角色
	 */
	public static String getRole(String userNo) {
		String subOrg = Db
				.use("default")
				.queryStr(
						" select val from sys_user_info u "
								+ " left join gcms_role_apply ga on ga.user_id = u.id "
								+ " left join gcms_param_info gp on gp.id = ga.role_id "
								+ " where u.id=? and ga.apply_status='1' ",
						new Object[] { userNo });
		return subOrg;
	}

	/**
	 * 删除数组元素
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String[] delCol(String colArr, String[] arr) {
		List list = new ArrayList<>();
		for (int i = 0; i < arr.length; i++) {
			if (!colArr.contains("," + arr[i] + ",")) {
				list.add(arr[i]);
			}
		}
		String[] newArr = new String[list.size()];
		for (int j = 0; j < list.size(); j++) {
			newArr[j] = (String) list.get(j);
		}
		return newArr;
	}

	/**
	 * 读取用户角色
	 */
	public static String getRoleNames(String user_id) {
		String roleNames = Db
				.use("default")
				.queryStr(
						" select to_char(wm_concat(name)) role_names "
								+ "  from (select r.user_id, r.role_id, i.name "
								+ "          from sys_user_role r "
								+ "          left join sys_role_info i "
								+ "            on r.role_id = i.id where r.user_id=? ) "
								+ " group by user_id ",
						new Object[] { user_id });
		if (StringUtil(roleNames) == null) {
			roleNames = "";
		}
		return roleNames;
	}

	/**
	 * 根据领导角色判定领导级别
	 */
	public static String getLevByRole(String roleName) {
		String roelevel = null;
		if (StringUtil(roleName) != null) {
			if (roleName.contains("领导-省行") || roleName.contains("省行系统管理员")) {
				roelevel = "1";
			} else if (roleName.contains("领导-二级分行")
					|| roleName.contains("二级分行系统管理员")) {
				roelevel = "2";
			} else if (roleName.contains("领导-中心支行")) {
				roelevel = "3";
			} else if (roleName.contains("领导-责任中心")) {
				roelevel = "4";
			}
		}
		return roelevel;
	}
	
	/**
	 * 根据角色判定角色级别 <br/>
	 *
	 * 根据角色关键字判定角色所属级别<br/>
	 * 
	 *  1 - like '%省行%'<br/>
	 *  2 - like '%二级分行%'<br/>
	 *  3 - like '%中心支行%'<br/>
	 *  4 - 其他
	 * 
	 * @author 常显阳 20181205
	 * return String
	 * 1-省行
	 * 2-分行
	 * 3-支行
	 * 4-网点
	 */
	public static String getLevelByRoleName(String roleName) {
		String roelevel = null;
		if (StringUtil(roleName) != null) {
			if (roleName.contains("省行")) {
				roelevel = "1";
			} else if (roleName.contains("分行")) {
				roelevel = "2";
			} else if (roleName.contains("支行")) {
				roelevel = "3";
			} else {
				roelevel = "4";
			}
		}
		return roelevel;
	}

	/**
	 * 获取对应级别的父级组织ID
	 */
	public static String getUpOrg(String orgId, String roelevel) {
		String upOrg = Db.use("default").queryStr(" select id from sys_org_info where by2 = ? start with id = ? connect by prior upid = id ",
						new Object[] { roelevel, orgId });
		return upOrg;
	}

	/**
	 * 获取上两级组织IDs
	 */
	public static String getUpOrgs(String orgId) {
		String pOrgs = Db.use("default").queryStr(
				" select by5 " + " from sys_org_info where id = ? ",
				new Object[] { orgId });
		return pOrgs;
	}

	/**
	 * 根据三级组织id判断是否是省本部 001001000 江苏省分行本部 001100000 省行营业部
	 */
	public static boolean isProv(String orgId) {
		boolean flag = false;
		if (orgId.equals("001001000") || orgId.equals("001100000")) {
			flag = true;
		}
		String pOrgs = getUpOrgs(orgId);
		if (StringUtil(pOrgs) != null) {
			if (pOrgs.contains("001001000") || pOrgs.contains("001100000")) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 五层分类加权系数
	 */
	public static double getFiveParam(String clas_five) {
		double valflag = 0;
		String val = Db
				.use("default")
				.queryStr(
						" select val "
								+ " from gcms_param_info where key = 'clas_five_param' and name=? ",
						new Object[] { "param" + clas_five });
		if (StringUtil(val) != null) {
			valflag = Double.parseDouble(val);
		}
		return valflag;
	}

	/**
	 * 根据组织id获取机构下属机构所对应的区域编码
	 */
	public static String getAreaStr(String orgId) {

		String areaArr = null;
		List<Record> list = null;
		if (!"000000000".equals(orgId)) {// 判断是否省行
			list = Db
					.use("default")
					.find(" select  distinct a.area_id "
							+ "    from pccm_cust_org_are a "
							+ "   where a.org_id in (select id "
							+ "                        from sys_org_info "
							+ "                       start with id = ? "
							+ "                      connect by prior id = upid) ",
							new Object[] { orgId });
		}
		if (null != list && list.size() > 0) {
			StringBuffer param = new StringBuffer();
			param.append("(");
			for (int i = 0; i < list.size(); i++) {
				param.append("'");
				param.append(list.get(i).getStr("area_id"));
				if (i == list.size() - 1) {
					param.append("'");
				} else {
					param.append("',");
				}
			}
			param.append(")");
			areaArr = param.toString();
		}

		return areaArr;
	}

	/**
	 * 查询宽表最新数据日期
	 */
	public static String findGNewDate() {
		// String sql =" select max(data_date) maxDate "
		// +"  from pccm_cust_base_info ";
		// String result =Db.use("gbase").queryStr(sql) ;
		String sql = " select max(data_date) maxDate "
				+ "  from pccm_cust_pool_money ";
		String result = Db.use("default").queryStr(sql);
		return result;
	}
	
	/**
	 * 查询PA宽表最新数据日期
	 */
	public static String findPaNewDate(String paDate) {
		 String sql =" select max(data_date) maxDate "
		 +"  from pccm_cust_base_info where 1=1 ";
		if(null!=StringUtil(paDate)){
			sql+=" and pa_date = '"+paDate+"'";
		}
		String result = Db.use("default").queryStr(sql);
		return result;
	}

	/**
	 * 查找字典信息
	 */
	public static String findDict(String key, String name) {
		String result = Db.use("default").queryStr(
				"select val from gcms_param_info where key = ? and name=? ",
				new Object[] { key, name });
		return result;
	}
	/**
	 * 获取客户统计最新日期
	 * @return
	 */
	public static String findCustCountMaxDate(){
		String sql = "select max(data_month) from pccm_cust_org_new_count";
		// 获取最新数据日期
		return Db.use("default").queryStr(sql);
	}
	/**
	 * 查找字典信息
	 */
	public static String findDictByKey(String key) {
		String result = Db.use("default").queryStr(
				"select val from gcms_param_info where key = ? ",
				new Object[] { key });
		return result;
	}
	/**
	 * 查找字典信息
	 */
	public static String findDictRemark(String key, String val) {
		String result = Db.use("default").queryStr(
				"select remark from gcms_param_info where key = ? and val=? ",
				new Object[] { key, val });
		return result;
	}
	
	/**
	 * 根据用户查找对应的权限
	 */
	public static String findPermOrg(String user_no) {
		String result = Db.use("default").queryStr(
				"select orgnum from sys_user_permission where user_no = ? ",
				new Object[] { user_no });
		return result;
	}
	
	/**
	 * 查找对应序列
	 */
	public static BigDecimal findSeq(String seq_name) {
		BigDecimal result = Db.use("default").queryBigDecimal(
				" select "+seq_name+".nextval from dual ");
		return result;
	}
	/**
	 * 转换文件长度为 单位
	 * @param bytes
	 * @return
	 */
	public static String bytesToSize(long bytes){
		int k = 1024;
		if(bytes<k){
			return String.valueOf(bytes)+"B";
		}else{
			bytes = bytes/k;
		}
		
		if(bytes<k){
			return String.valueOf(bytes)+"KB";
		}else{
			bytes = bytes/k;
		}
			
		if(bytes<k){
			bytes = bytes * 100;
			return String.valueOf(bytes/100)+"."+String.valueOf(bytes%100)+"MB";
		}else{
			bytes = bytes * 100/k;
			return String.valueOf(bytes/100)+"."+String.valueOf(bytes%100)+"GB";
		}
	}
	
	/**
	 * 去除字符串中的 重复数据,号分割
	 * @param str
	 * @return
	 */
	public static String getStrByDistinct(String str){
		StringBuffer sBuffer = new StringBuffer();
		if(AppUtils.StringUtil(str)!=null){
			String [] strs = str.split(","); 
			Map<String,String> dismap = new HashMap<>();
			for (String estr : strs) {
				dismap.put(estr, estr);
			}
			
			
			boolean isFir = true;
			Set<String> keys = dismap.keySet();
			for (String estr : keys) {
				if(isFir){
					isFir = false;
				}else{
					sBuffer.append(",");
				}
				sBuffer.append(estr);
			}
		}
		return sBuffer.toString();
	}
	/**
	 * 根据角色级别、用户所在机构号获取最高权限机构
	 * @param userInfo 	登录用户信息
	 * @return
	 */
	public static String getOrgNumByUser(Record userInfo){
		
		String roleLevel = userInfo.getStr("ROLE_LEVEL");
		String userOrg = userInfo.getStr("ORG_ID");
		String userNo = userInfo.getStr("USER_NO");
		String orgNum = "";
		if (StringUtils.isNotBlank(userNo)) {// 根据客户号查询客户池权限表(数据范围中的值)
			String sql = "select m.orgnum from SYS_USER_PERMISSION m " 
						+ "	left join sys_org_info n on m.orgnum = n.orgnum	" 
						+ " where m.user_no ='" + userNo + "'" 
						+ " order by n.by2 asc";
			Record record = Db.use("default").findFirst(sql);
			if (record != null) {	//如果设置了数据范围，则返回数据范围所对应的机构号
				return record.getStr("orgnum");
			}
		}
		
		if (StringUtils.isBlank(roleLevel) || StringUtils.isBlank(userOrg)) {
			return "";
		}
		// 向上查询机构
		String sql = " select orgnum, by2, by5 from sys_org_info where orgnum ='" + userOrg + "'";
		Record userOrgInfo = Db.use("default").findFirst(sql);
		if (userOrgInfo != null) {//获取当前本身
			if (roleLevel.equals(userOrgInfo.getStr("BY2")) || "1".equals(userOrgInfo.getStr("BY2"))) {
				return userOrgInfo.getStr("orgnum");
			} else {
				String by5 = userOrgInfo.getStr("BY5");
				if (by5.startsWith(",")) {
					by5 = by5.substring(1);
				}
				if (StringUtils.isNotBlank(by5) ) {
					by5 = "'" + by5.replaceAll(",", "','") + "'";
				}
				sql = " select orgnum, by2, by5 from sys_org_info where 1=1 and orgnum in (" + by5 + ")";
				List<Record> list = Db.use("default").find(sql);
				for (Record record : list) {
					if (roleLevel.equals(record.getStr("BY2"))) {
						orgNum = record.getStr("ORGNUM");
						return orgNum;
					}
				}
			}
		}
		
		// 向下查询机构
		sql = " select orgnum,by2,upid from sys_org_info where by5 like '%" + userOrg + "%' and by2 ='" + roleLevel + "' order by by2 asc";
		List<Record> list = Db.use("default").find(sql);
		if (list != null && list.size()>0) {
			for (Record record : list) {
				if (roleLevel.equals(record.getStr("BY2"))) {
					orgNum = record.getStr("ORGNUM");
					return orgNum;
				}
			}
		}
		return userOrg;
	}
	
	/**
	 * 根据角色级别、用户所在机构号获取书记权限机构LIST
	 * @param userInfo 	登录用户信息
	 * @return
	 */
	public static List<String> getOrgNumsByUser(Record userInfo){
		List<String> resList = new ArrayList<>();
		String roleLevel = userInfo.getStr("ROLE_LEVEL");
		String userOrg = userInfo.getStr("ORG_ID");
		String userNo = userInfo.getStr("USER_NO");
		if (StringUtils.isNotBlank(userNo)) {// 根据客户号查询客户池权限表
			String sql = "select m.orgnum from SYS_USER_PERMISSION m " 
					+ "	left join sys_org_info n on m.orgnum = n.orgnum	" 
					+ " where m.user_no ='" + userNo + "'" 
					+ " order by n.by2 asc";
			List<Record> list = Db.use("default").find(sql);
			if (list != null && list.size() > 0) {
				for (Record record : list) {
					resList.add(record.getStr("orgnum"));
				}
				return resList;
			}
		}
		
		if (StringUtils.isBlank(roleLevel) || StringUtils.isBlank(userOrg)) {
			return null;
		}
		
		// 向上查询机构
		String sql = " select orgnum, by2, by5 from sys_org_info where orgnum ='" + userOrg + "'";
		Record userOrgInfo = Db.use("default").findFirst(sql);
		if (userOrgInfo != null) {//获取当前本身
			if (roleLevel.equals(userOrgInfo.getStr("BY2")) || "1".equals(userOrgInfo.getStr("BY2"))) {
				List<Record> list = getOrgList(userOrgInfo.getStr("orgnum"));
				if (list != null && list.size() > 0) {
					for (Record record : list) {
						resList.add(record.getStr("orgnum"));
					}
					return resList;
				}
			} else {
				String by5 = userOrgInfo.getStr("BY5");
				if (by5.startsWith(",")) {
					by5 = by5.substring(1);
				}
				if (StringUtils.isNotBlank(by5) ) {
					by5 = "'" + by5.replaceAll(",", "','") + "'";
				}
				sql = " select orgnum, by2, by5 from sys_org_info where 1=1 and orgnum in (" + by5 + ")";
				List<Record> list = Db.use("default").find(sql);
				for (Record record : list) {
					if (roleLevel.equals(record.getStr("BY2"))) {
						List<Record> list1 = getOrgList(userOrgInfo.getStr("orgnum"));
						if (list1 != null && list1.size() > 0) {
							for (Record record1 : list1) {
								resList.add(record1.getStr("orgnum"));
							}
							return resList;
						}
					}
				}
			}
		}
		
		// 向下查询机构
		sql = " select orgnum,by2,upid from sys_org_info where by5 like '%" + userOrg + "%' and by2 ='" + roleLevel + "' order by by2 asc";
		List<Record> list = Db.use("default").find(sql);
		if (list != null && list.size() > 0) {
			for (Record record : list) {
				if (roleLevel.equals(record.getStr("BY2"))) {
					List<Record> list1 = getOrgList(userOrgInfo.getStr("orgnum"));
					if (list1 != null && list1.size() > 0) {
						for (Record record1 : list1) {
							resList.add(record1.getStr("orgnum"));
						}
						return resList;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 将以逗号分隔的字符串加以单引号
	 * @param str
	 * @return
	 */
	public static String getRegexStr(String str){
		StringBuffer sBuffer = new StringBuffer();
		if(AppUtils.StringUtil(str)!=null){
			String [] strs = str.split(","); 
			
			boolean isFir = true;
			for (String estr : strs) {
				if(isFir){
					isFir = false;
				}else{
					sBuffer.append(",");
				}
				sBuffer.append("'"+estr+"'");
			}
		}
		return sBuffer.toString();
	}
}
