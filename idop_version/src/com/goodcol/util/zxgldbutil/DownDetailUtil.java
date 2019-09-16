package com.goodcol.util.zxgldbutil;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.ICallback;
import com.goodcol.util.AppUtils;
import com.goodcol.util.Constant;

public class DownDetailUtil {
	/**
	 * 创建下载任务
	 * @param org
	 * @param downFlag
	 * @return
	 */
	public static String DownTask(Map<String, Object> map) {
		String org = (String) map.get("org");
		String downFlag = (String) map.get("downFlag");
		String user_no = (String) map.get("user_no");
		String custType = (String) map.get("custType");
		String user_name = (String) map.get("user_name");
		String data_date = (String) map.get("dataDate");
		String orgLevel = (String) map.get("orgLevel");
		final String downDetailSql = DownDetailSql(org,downFlag,user_no,data_date,orgLevel,custType);
		final String downDetailTxt = DownDetailTxt(downFlag,orgLevel,custType);
		final String downDetailHead = DownDetailHead(downFlag);
		System.out.println(downDetailTxt+"_下载日志：ehr="+user_no+",机构号："+org+",custType:"+custType+"data_date:"+data_date+"下载SQL："+downDetailSql);
		String backTxt = "F";
		final Object pkid = Db.use("gbase").execute(new ICallback() {
			@Override
			public Object call(Connection conn) throws SQLException {
				CallableStatement proc = null;
				String rs = null;
				try {
					proc = conn.prepareCall("{call FN_DSQL.P_ADD_JOB_NEW(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
					proc.setInt(1, 8);
					proc.setString(2, "pccm01");
					proc.setString(3, "CREATE VIEW GBASE_TARGET_TABLE_NAME AS "+downDetailSql+"");
					proc.setString(4, "S");
					proc.setString(5, "5");
					proc.setString(6, ",");
					proc.setString(7, "Y");
					proc.setString(8, "");
					proc.setString(9, "Y");
					proc.setInt(10, 0);
					proc.setString(11, "Y---------");
					
					proc.registerOutParameter(12, java.sql.Types.VARCHAR);
					proc.registerOutParameter(13, java.sql.Types.VARCHAR);
					proc.registerOutParameter(14, java.sql.Types.VARCHAR);
					proc.executeQuery();
					rs =  proc.getString(12);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (proc != null) {
						proc.close();
					}
					if (conn != null) {
						conn.close();
					}
				}
				return rs;
			}
		});
		if (AppUtils.StringUtil(String.valueOf(pkid)) != null) {
			Object returnCode  = Db.use("gbase").execute(new ICallback() {
				@Override
				public Object call(Connection conn) throws SQLException {
					CallableStatement proc = null;
					String rs = null;
					try {
						proc = conn.prepareCall("{call FN_DSQL.P_ADD_JOB_EXPLAIN(?,?,?,?)}");
						proc.setString(1, String.valueOf(pkid));
						proc.setString(2, downDetailTxt);
						
						proc.registerOutParameter(3, java.sql.Types.VARCHAR);
						proc.registerOutParameter(4, java.sql.Types.VARCHAR);
						proc.executeQuery();
						rs =  proc.getString(3);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (proc != null) {
							proc.close();
						}
						if (conn != null) {
							conn.close();
						}
					}
					return rs;
				}
			});
			if(Constant.GBASE_RETURNCODE.equals(returnCode)){
				backTxt = "T";
			}else{
				System.out.println(downDetailTxt+";pkid:"+pkid+";更新任务说明失败！");
			}
			
			//保存创建人信息
			Db.use("gbase").update(" insert into pccm_down_task_info(id,pkid,user_no,user_name,create_date,table_head) "
					+" values(?,?,?,?,to_char(now(),'yyyyMMddhh24miss'),?) " ,
					new Object[] { AppUtils.getStringSeq(),pkid,user_no,user_name,downDetailHead});
			
		}else{
			System.out.println(downDetailTxt+";创建任务失败！");
		}
		return backTxt;
		
	}
	/**
	 * 拼装下载SQL
	 * @param org
	 * @param downFlag
	 * @return
	 */
	public static String DownDetailSql(String org, String downFlag,String user_no,String data_date,String orgLevel,String custType) {
		String downDetaiSql = "";
//		String orgArr = AppUtils.getOrgStr(org, "2");
//		String orgArr = AppUtils.getOrgSqlByLevel(org,orgLevel);
		String orgArr = " select id from sys_org_info where by5 like '%"+org+"%' or id ='"+org+"' ";
		String areaArr = AppUtils.getAreaStr(org);
		switch (downFlag) {
			case "SQL_CUST_CNT_MY":
				if(AppUtils.StringUtil(custType) == null){
					custType="02";
				}
				if (AppUtils.StringUtil(org) != null&&AppUtils.StringUtil(data_date) != null) {
					downDetaiSql = Constant.SQL_CUST_CNT_MY;
					if (AppUtils.StringUtil(orgArr) != null) {
						downDetaiSql += " and p.id in (" + orgArr+")";
					}
					downDetaiSql += "    and p.data_date = '"+data_date+"' ";
					if("N".equals(custType)){
						downDetaiSql += "    and p.cust_type is null ";
					}else{
						downDetaiSql += "    and p.cust_type = '"+custType+"' ";
					}
					if(AppUtils.StringUtil(orgLevel)!=null){
						downDetaiSql += "    and p.deptlevel = '"+orgLevel+"' ";
					}
				}
				break;
			case "SQL_CUST_CNT_OTHER":
				if (AppUtils.StringUtil(org) != null) {
					downDetaiSql = Constant.SQL_CUST_CNT_OTHER;
					if (AppUtils.StringUtil(areaArr) != null) {
						downDetaiSql += " and p.area_code in  " + areaArr;
					}
				}
				break;
			case "SQL_MY_CUST_MY":
				if (AppUtils.StringUtil(user_no) != null) {
					downDetaiSql = Constant.SQL_MY_CUST_MY(user_no,custType);
				}
				break;
			case "SQL_MY_CUST_OTHER":
				if (AppUtils.StringUtil(user_no) != null) {
					downDetaiSql = Constant.SQL_MY_CUST_OTHER(user_no);
				}
				break;
			case "SQL_CUST_CLAIM_MY":
				if (AppUtils.StringUtil(org) != null) {
					downDetaiSql = Constant.SQL_CUST_CLAIM_MY;
					if (AppUtils.StringUtil(orgArr) != null) {
						downDetaiSql += " and p.orgnum in  (" + orgArr+")";
					}
					if(AppUtils.StringUtil(custType) != null){
						if("N".equals(custType)){
							downDetaiSql += "    and p.cust_type is null ";
						}else{
							downDetaiSql += "    and p.cust_type = '"+custType+"' ";
						}
					}
				}
				//downDetaiSql = Constant.SQL_CUST_CLAIM_MY(org, orgArr);
				break;
			case "SQL_CUST_CLAIM_OTHER":
				if (AppUtils.StringUtil(org) != null) {
					downDetaiSql = Constant.SQL_CUST_CLAIM_OTHER;
					if (AppUtils.StringUtil(areaArr) != null) {
						downDetaiSql += " and p.area_code in  " + areaArr;
					}
				}
				break;
			case "SQL_CUST_PA":
				if(AppUtils.StringUtil(custType) == null){
					custType="02";
				}
				if (AppUtils.StringUtil(org) != null&&AppUtils.StringUtil(data_date) != null) {
					downDetaiSql = Constant.SQL_CUST_PA;
					if (AppUtils.StringUtil(orgArr) != null) {
						downDetaiSql += " and p.id in  (" + orgArr+")";
					}
					downDetaiSql += "    and p.data_date = '"+data_date+"' ";
					if("N".equals(custType)){
						downDetaiSql += "    and p.cust_type is null ";
					}else{
						downDetaiSql += "    and p.cust_type = '"+custType+"' ";
					}
					if(AppUtils.StringUtil(orgLevel)!=null){
						downDetaiSql += "    and p.deptlevel = '"+orgLevel+"' ";
					}
				}
				break;
			case "SQL_CUST_VALID":
				if(AppUtils.StringUtil(custType) == null){
					custType="02";
				}
				if (AppUtils.StringUtil(org) != null&&AppUtils.StringUtil(data_date) != null) {
					downDetaiSql = Constant.SQL_CUST_VALID;
					if (AppUtils.StringUtil(orgArr) != null) {
						downDetaiSql += " and p.id in  (" + orgArr+")";
					}
					downDetaiSql += "    and p.data_date = '"+data_date+"' ";
					if("N".equals(custType)){
						downDetaiSql += "    and p.cust_type is null ";
					}else{
						downDetaiSql += "    and p.cust_type = '"+custType+"' ";
					}
					if(AppUtils.StringUtil(orgLevel)!=null){
						downDetaiSql += "    and p.deptlevel = '"+orgLevel+"' ";
					}
				}
				break;
			default:
				break;
		}
		return downDetaiSql;
	}
	
	/**
	 * 明细下载说明
	 * @param org
	 * @param downFlag
	 * @return
	 */
	public static String DownDetailTxt(String downFlag,String orgLevel,String custType) {
		String levelTxt="";
		String downDetaiTxt = "";
		String custTypeTxt = "";
		if(AppUtils.StringUtil(custType)!=null){
			switch (custType) {
			case "02":
				custTypeTxt = "_工商客户";
				break;
			case "03":
				custTypeTxt = "_金融机构客户";
				break;
			case "N":
				custTypeTxt = "_无类型";
				break;
			default:
				break;
			}
		}
		if(AppUtils.StringUtil(orgLevel)!=null){
			switch (orgLevel) {
			case "0":
				levelTxt = "省行层";
				break;
			case "1":
				levelTxt = "分行层";
				break;
			case "2":
				levelTxt = "支行层";
				break;
			case "3":
				levelTxt = "网点层";
				break;
			default:
				break;
			}
		}
		if(AppUtils.StringUtil(downFlag)!=null){
			switch (downFlag) {
				case "SQL_CUST_CNT_MY":
					downDetaiTxt = Constant.CN_CUST_CNT_MY+levelTxt+custTypeTxt;
					break;
				case "SQL_CUST_CNT_OTHER":
					downDetaiTxt = Constant.CN_CUST_CNT_OTHER;
					break;
				case "SQL_MY_CUST_MY":
					downDetaiTxt = Constant.CN_MY_CUST_MY+custTypeTxt;
					break;
				case "SQL_MY_CUST_OTHER":
					downDetaiTxt = Constant.CN_MY_CUST_OTHER;
					break;
				case "SQL_CUST_CLAIM_MY":
					downDetaiTxt = Constant.CN_CUST_CLAIM_MY+custTypeTxt;
					break;
				case "SQL_CUST_CLAIM_OTHER":
					downDetaiTxt = Constant.CN_CUST_CLAIM_OTHER;
					break;
				case "SQL_CUST_PA":
					downDetaiTxt = Constant.CN_CUST_PA+levelTxt+custTypeTxt;
					break;
				case "SQL_CUST_VALID":
					downDetaiTxt = Constant.CN_CUST_VALID+levelTxt+custTypeTxt;
					break;
				default:
					break;
			}
		}
		return downDetaiTxt;
	}
	
	/**
	 * 明细下载表头
	 * @param org
	 * @param downFlag
	 * @return
	 */
	public static String DownDetailHead(String downFlag) {
		String downDetaiTxt = "";
		if(AppUtils.StringUtil(downFlag)!=null){
			switch (downFlag) {
				case "SQL_CUST_CNT_MY":
					downDetaiTxt = Constant.HEAD_CUST_CNT_MY;
					break;
				case "SQL_CUST_CNT_OTHER":
					downDetaiTxt = Constant.HEAD_CUST_CNT_OTHER;
					break;
				case "SQL_MY_CUST_MY":
					downDetaiTxt = Constant.HEAD_MY_CUST_MY;
					break;
				case "SQL_MY_CUST_OTHER":
					downDetaiTxt = Constant.HEAD_MY_CUST_OTHER;
					break;
				case "SQL_CUST_CLAIM_MY":
					downDetaiTxt = Constant.HEAD_CUST_CLAIM_MY;
					break;
				case "SQL_CUST_CLAIM_OTHER":
					downDetaiTxt = Constant.HEAD_CUST_CLAIM_OTHER;
					break;
				case "SQL_CUST_PA":
					downDetaiTxt = Constant.HEAD_CUST_PA;
					break;
				case "SQL_CUST_VALID":
					downDetaiTxt = Constant.HEAD_CUST_VALID;
					break;
				default:
					break;
			}
		}
		return downDetaiTxt;
	}
	
	/**
	 * 定制报表创建下载任务
	 * @param org
	 * @param downFlag
	 * @return
	 */
	public static String DownReportTask(Map<String, String> paramMap) {
		final String downDetailSql = paramMap.get("sql");
		final String downDetailTxt =paramMap.get("task_text");
		final String downDetailHead = paramMap.get("head");
		String user_no = paramMap.get("user_no");
		String user_name = paramMap.get("user_name");
		String backTxt = "F";
		final Object pkid = Db.use("gbase").execute(new ICallback() {
			@Override
			public Object call(Connection conn) throws SQLException {
				CallableStatement proc = null;
				String rs = null;
				try {
					proc = conn.prepareCall("{call FN_DSQL.P_ADD_JOB_NEW(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
					proc.setInt(1, 8);
					proc.setString(2, "pccm01");
					proc.setString(3, "CREATE VIEW GBASE_TARGET_TABLE_NAME AS "+downDetailSql+"");
					proc.setString(4, "S");
					proc.setString(5, "5");
					proc.setString(6, ",");
					proc.setString(7, "Y");
					proc.setString(8, "");
					proc.setString(9, "Y");
					proc.setInt(10, 0);
					proc.setString(11, "Y---------");
					
					proc.registerOutParameter(12, java.sql.Types.VARCHAR);
					proc.registerOutParameter(13, java.sql.Types.VARCHAR);
					proc.registerOutParameter(14, java.sql.Types.VARCHAR);
					proc.executeQuery();
					rs =  proc.getString(12);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (proc != null) {
						proc.close();
					}
					if (conn != null) {
						conn.close();
					}
				}
				return rs;
			}
		});
		if (AppUtils.StringUtil(String.valueOf(pkid)) != null) {
			Object returnCode  = Db.use("gbase").execute(new ICallback() {
				@Override
				public Object call(Connection conn) throws SQLException {
					CallableStatement proc = null;
					String rs = null;
					try {
						proc = conn.prepareCall("{call FN_DSQL.P_ADD_JOB_EXPLAIN(?,?,?,?)}");
						proc.setString(1, String.valueOf(pkid));
						proc.setString(2, downDetailTxt);
						
						proc.registerOutParameter(3, java.sql.Types.VARCHAR);
						proc.registerOutParameter(4, java.sql.Types.VARCHAR);
						proc.executeQuery();
						rs =  proc.getString(3);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (proc != null) {
							proc.close();
						}
						if (conn != null) {
							conn.close();
						}
					}
					return rs;
				}
			});
			if(Constant.GBASE_RETURNCODE.equals(returnCode)){
				backTxt = "T";
			}else{
				System.out.println(downDetailTxt+";pkid:"+pkid+";更新任务说明失败！");
			}
			//保存创建人信息
			Db.use("gbase").update(" insert into pccm_down_task_info(id,pkid,user_no,user_name,create_date,table_head) "
					+" values(?,?,?,?,to_char(now(),'yyyyMMddhh24miss'),?) " ,
					new Object[] { AppUtils.getStringSeq(),pkid,user_no,user_name,downDetailHead});
			
		}else{
			System.out.println(downDetailTxt+";创建任务失败！");
		}
		return backTxt;
	}

}
