package com.goodcol.job;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.ParamContainer;
import com.goodcol.util.SendMessageUtil;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

public class SendSmsOfTasktodoJob extends OuartzJob{
	private static final String sm_job = "IdopSendSmsOfTasktodoJob";
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// -----开始时间-----
		String start_time = "";
		// -----结束时间-----
		String end_time = "";
		String incflg = "待处理任务每日短信提醒";
		//记录数
		int record_size = 0;
		//跑批状态
		int run_status = 0;
		try {
			start_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			String userInfoSql =" SELECT SUI.USER_NO, sui.name,                     "  +
			"	       SUI.PHONE,                                         "  +
			"	       SUI.MODEL,                                         "  +
			"	       (SELECT REMARK                                     "  +
			"	          FROM SYS_PARAM_INFO                             "  +
			"	         WHERE KEY = 'dop_ywtype'                         "  +
			"	           AND VAL = SUI.MODEL) MODEL_NAME,               "  +
			"	       SRI.ROLE_LEVEL,                                    "  +
			"	       SUI.ORG_ID                                         "  +
			"	  FROM SYS_USER_INFO SUI                                  "  +
			"	 INNER JOIN SYS_USER_ROLE SUR                             "  +
			"	    ON SUI.ID = SUR.USER_ID                               "  +
			"	 INNER JOIN SYS_ROLE_INFO SRI                             "  +
			"	    ON SUR.ROLE_ID = SRI.ID                               "  +
			"	 WHERE SUR.ROLE_ID IN ('C89B62C161CC4A8282E765A94BF7408B',"  +
			"	                       'BC6576510E3644ACAC08BE5C469D9CFB')"  +
			"	   AND SUI.PHONE IS NOT NULL ";
			List<Record> userList = Db.find(userInfoSql);
			String remark = ParamContainer.getDictName("dop_sms_risk_lvl", "0");
			System.out.println("风险等级:"+remark);
			List<Map<String, String>> itemMapList = ParamContainer.getDictList("dop_warning_lvl");
			StringBuffer warnLvlSql = new StringBuffer(" and p.warning_level between ");
			for (Map<String, String> itemMap : itemMapList) {
				if (itemMap.containsValue(remark)){
					warnLvlSql.append(itemMap.get("val") + " and 5");
				}
			}
			String selectSql = " select count(1) ";
			for (Record user : userList) {
				StringBuffer sm_key = new StringBuffer();
				BigDecimal indentWarning = new BigDecimal(0);
				BigDecimal highLvlIndentWarning = new BigDecimal(0);
				BigDecimal checkWarning = new BigDecimal(0);
				BigDecimal highLvlCheckWarning = new BigDecimal(0);
				BigDecimal approvalWarning = new BigDecimal(0);
				BigDecimal highLvlApprovalWarning = new BigDecimal(0);
				String dataDate = DateTimeUtil.getNowDate();
				String deadHour = '0'==dataDate.charAt(8) ? dataDate
						.substring(9, 10) : dataDate.substring(8, 10);
				StringBuffer cont = new StringBuffer("【多棱镜】截至" + deadHour
						+ "点，" + user.getStr("model_name") + "模块预警");
				sm_key.append(deadHour).append(",").append(user.getStr("model_name"));
				String sjfw = AppUtils.getOrgNumByUser(user);
				String model = user.getStr("model");
				if ("1".equals(AppUtils.getOrgLevel(sjfw))) {
					//待认定  都是省行认定
					String sql1 = "select count(*)  from dop_warning_info i "
							+ "left join dop_warning_param p on p.warning_code=i.warning_code left join ("
							+ "select * from dop_gangedmenu_info k where k.id in "
							+ "('351627F908C7461A9B8F82EB1BDEE750','35915F0EA9DC4B3DBFFD0C267579EA4C',"
							+ "'4349615CE0A5433F9E23BADB7100D990',"
							+ "'54AA6F8373B94F3AB115548D3867C6F7','679BE1CBC3C94B0689ECA17D7480A44D',"
							+ "'8BDA5EE49374486CB87E7832EC8536F4','9DD1BF61F7F3419CAF5EB76AAD8BFDAA',"
							+ "'9ECC18D14F7F4CB9812582C342F70936',"
							+ "'CAAC03E702B84EB792D4CFBA924CB1247','CE1A1A8E9026486BB11124CAE2457D5A',"
							+ "'DF39639549A449E8B1974D3B497165BB')) g on g.val=p.warning_type_code "
							+ "where 1 = 1 "
							+ " and i.indentify_status ='0' and p.busi_module='"
							+ model
							+ "' and  (p.is_use is  null or p.is_use = '1') and p.warning_dimension !='2' ";
					indentWarning = Db.queryBigDecimal(sql1);
					if (indentWarning.intValue() != 0) {
						highLvlIndentWarning = Db.queryBigDecimal(sql1+warnLvlSql);
						cont.append("待认定（"+remark+"风险以上"+highLvlIndentWarning+"笔）"+indentWarning+"笔，");
						sm_key.append(",").append(remark).append(",").append(highLvlIndentWarning).append(",").append(indentWarning);
					}
				}
				String extrasql = " from dop_warning_info i "
						+ "left join dop_warning_param p on p.warning_code=i.warning_code "
						+ " left join dop_gangedmenu_info g on g.val=p.warning_type_code ";
				String whereSql2 = "";
				if ("1".equals(user.getStr("ROLE_LEVEL"))) {
					whereSql2 = " where  (i.last_check_stat='0'  ) and  "
							+ "(p.is_use is  null or p.is_use = '1') and  (p.is_use is  null or p.is_use = '1') and "
							+ "( lvl_3_branch_no ='001001000')   and p.busi_module='"
							+ model + "'  and p.warning_dimension !='2' ";
				} else {
					whereSql2 = " where  (i.last_check_stat='0'  ) and  (p.is_use is  null or p.is_use = '1')"
							+ " and    (p.is_use is  null or p.is_use = '1') and "
							+ "(select case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no "
							+ " when '2' then lvl_3_branch_no "
							+ " when '3' then lvl_4_branch_no end checker_level from dop_warning_info i2 "
							+ "where i2.id=i.id)='"
							+ sjfw
							+ "'   and p.busi_module='" + model + "' ";
				}
				// 待核查
				checkWarning = Db.queryBigDecimal(selectSql + extrasql
						+ whereSql2);
				if (checkWarning.intValue() != 0) {
					highLvlCheckWarning = Db.queryBigDecimal(selectSql
							+ extrasql + whereSql2 + warnLvlSql);
					String toCheckMsg = "【多棱镜】截至"+deadHour+"点，"+user.getStr("model_name")+"模块预警待核查（"+remark+"风险以上"+highLvlCheckWarning+"笔）"+checkWarning+"笔，请及时处理！";
					String key = deadHour + "," + user.getStr("model_name") + "," + remark + "," + highLvlCheckWarning + "," + checkWarning;
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("sm_cont", toCheckMsg);
					map.put("sm_job", sm_job);
					map.put("sm_key", key);
					List<Record> lr = new ArrayList<Record>();
					lr.add(user);
					SendMessageUtil.sendMsgList(lr, map);
					record_size++;
				}
				String whereSql3 = "where p.is_confirm='1' and p.busi_module='"
						+ model
						+ "' "
						+ "and  (p.is_use is  null or p.is_use = '1') and i.last_approval_stat='0'  and "
						+ "i.last_approver_org ='" + sjfw + "' ";
				//" i.LAST_CHECKER_ORG in (select id from sys_org_info start with orgnum = 
//				'"+user.getStr("orgnum")+"' connect by prior orgnum=upid)" ;
				//待审批
				approvalWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql3);
				if (approvalWarning.intValue() != 0) {
					highLvlApprovalWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql3+warnLvlSql);
					cont.append("待复查（"+remark+"风险以上"+highLvlApprovalWarning+"笔）"+approvalWarning+"笔，");
					sm_key.append(",").append(remark).append(",").append(highLvlApprovalWarning).append(",").append(approvalWarning);
				}
				if (approvalWarning.intValue() == 0 && indentWarning.intValue() == 0) {
					continue;
				}
				cont.append("请及时处理！");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("sm_cont", cont.toString());
				map.put("sm_job", sm_job);
				map.put("sm_key", sm_key.toString());
				List<Record> lr = new ArrayList<Record>();
				lr.add(user);
				SendMessageUtil.sendMsgList(lr, map);
				record_size++;
			}
			end_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			run_status=1;
			success();
		} catch (Exception e) {
			error();
			run_status=2;
			e.printStackTrace();
		}finally{
			Db.use("default").update("insert into PCCM_QUARTZ_RUN(id,START_TIME,END_TIME,RECORD_SIZE,INCFLG,RUN_STATUS)" +
					" values(?,?,?,?,?,?)",
					new Object[]{AppUtils.getStringSeq(),start_time,end_time,record_size,incflg,run_status});
		}
	}
}
