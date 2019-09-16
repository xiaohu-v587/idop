package com.goodcol.job;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.SendMessageUtil;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

public class SendSmsOfMyFollowJob extends OuartzJob{
	private static final String sm_job = "IdopSendSmsOfMyFollowJob";

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// -----开始时间-----
		String start_time = "";
		// -----结束时间-----
		String end_time = "";
		String incflg = "我的关注每日短信";
		//记录数
		int record_size = 0;
		//跑批状态
		int run_status = 0;
		try {
			start_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			String selSql = "select r.main_page, t.PHONE, t.id, t.USER_NO,t.ORG_ID,r.ROLE_LEVEL, t.model, t.name" +
					" from sys_user_info t "+
					" join sys_role_info r on t.role_id = r.id  WHERE phone IS NOT NULL ";
			List<Record> userList = Db.find(selSql);
			for (Record r : userList) {
				String mainpage = r.getStr("main_page");
				if(mainpage!=null&&!("").equals(mainpage)){
					if("0".equals(mainpage)){//管理员首页
						getMyfocus1(r);
					}else if("1".equals(mainpage)){//运营专家首页
						getMyfocus2(r);
					}else if("2".equals(mainpage)){//网点首页
						getMyfocus3(r);
					}
				}else{//管理员首页
					getMyfocus1(r);
				}
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

	private void getMyfocus1(Record r) {
		String userno = r.getStr("id");
		String  maxPermiOrgnum =  AppUtils.getOrgNumByUser(r);
		//查出关注的预警，0代表预警
		String selectSql = "select followed_org, max(follower) follower, busi_module, sub_busi_code, mark_code, follow_type, followed_teller from (select case when t.followed_org is null then '"
				+ maxPermiOrgnum
				+ "' else t.followed_org end followed_org, t.follower, t.busi_module,t.sub_busi_code,t.mark_code,t.follow_type,t.followed_teller from dop_my_follow t where (follower = ?  and t.follow_type = '0') "
				+ "or (t.assigned_type = '1' and t.follow_type = '0'))"
				+ "  group by followed_org,busi_module,sub_busi_code,mark_code,follow_type,followed_teller order by followed_org,follow_type desc ";
		List<Record> lr = Db.find(selectSql,userno);
		if(lr != null && lr.size() > 0){
			for(Record re : lr){
				Map<String, Object> map = new HashMap<String, Object>();
				StringBuffer sm_key = new StringBuffer();
				StringBuffer sbf = new StringBuffer("【多棱镜】您关注的");
//				String sql = "SELECT orgname from sys_org_info where  orgnum = '"
//						+ re.getStr("followed_org") + "' and stat = '1'";
//				String orgname = Db.findFirst(sql).getStr("orgname");
				String followed_teller = re.getStr("followed_teller");
//				String userName = "";
//				if(AppUtils.StringUtil(followed_teller) != null){
//					 userName=Db.findFirst("select d.name from sys_user_info d where d.user_no = '"+followed_teller+"' ").getStr("name");
//				}
				String sub_busi_code = re.getStr("sub_busi_code");
				String mark_code=re.getStr("mark_code");
				String followed_org = re.getStr("followed_org");
				String busi_module = re.getStr("busi_module");
				String extraSql = " from dop_warning_info i left join dop_warning_param p on" +
						" p.warning_code=i.warning_code where i.deptno in (select id from sys_org_info where id = ? or by5 like ?)  and p.busi_module=?  " +
						"and  (p.is_use is  null or p.is_use = '1')  and p.warning_type_code= ?  and p.warning_code= ? and substr(i.create_time,0,8) >= to_char(" +
						"sysdate-1,'yyyyMMdd')  and substr(i.create_time,0,8) < to_char(SYSDATE, 'yyyyMMdd') and p.warning_dimension !='2'";
				String wheresql = "";
				if(AppUtils.StringUtil(followed_teller) != null){
					wheresql = " and  i.teller_no = '"+followed_teller+"' ";
				}
				List<Record> warning = Db.find("select distinct (select orgname from sys_org_info where orgnum = ? and stat=1) orgname, " +
						" 	(select remark from sys_param_info where key = 'dop_ywtype' and val=p.busi_module) name, " +
						" 	(select warning_name from dop_warning_param where  warning_code=p.warning_code and warning_dimension !='2' ) warncodename, " +//预警名称
						"	(select count(1)" + extraSql + wheresql + "and i.warning_status = '1'  "	+ ("1".equals(r.getStr("ROLE_LEVEL")) ? " and i.last_check_stat in('1', '2') and i.deptno in (select id from sys_org_info where id = '000000000' or by5 like '%000000000%')) checked, ": "and i.last_check_stat in('1', '2') and case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no  when '2' then lvl_3_branch_no when '3' then lvl_4_branch_no end ='"+maxPermiOrgnum+"') checked, ")+//已核查
						"   (select count(i.is_question)" + extraSql + wheresql +"  and i.warning_status='1' and is_question = '1' ) isquestioncount ,"+
						" 	(select d.name from sys_user_info d where d.user_no = i.teller_no ) username,(select count(1) " + extraSql + wheresql +"  and " +
						"  i.warning_status='1' ) warningcount " +extraSql ,followed_org,followed_org,"%"+followed_org+"%",busi_module,sub_busi_code,mark_code,followed_org,"%"+followed_org+"%",busi_module,sub_busi_code,mark_code,followed_org,"%"+followed_org+"%",busi_module,sub_busi_code,mark_code,followed_org,"%"+followed_org+"%",busi_module,sub_busi_code,mark_code);
				if(warning != null && warning.size() > 0 && warning.get(0).getBigDecimal("warningcount").intValue() != 0){
					sbf.append(warning.get(0).getStr("warncodename")+"预警昨日产生"+warning.get(0).getBigDecimal("warningcount")+"笔，已核查"+ warning.get(0).getBigDecimal("checked") +"笔，确认问题"+warning.get(0).getBigDecimal("isquestioncount").toString()+"笔，请密切关注，切实防范风险！");
					sm_key.append(warning.get(0).getStr("warncodename")).append(",").append(warning.get(0).getBigDecimal("warningcount")).append(",").append(warning.get(0).getBigDecimal("checked")).append(",").append(warning.get(0).getBigDecimal("isquestioncount"));
					map.put("sm_key", sm_key.toString());
					map.put("sm_cont", sbf.toString());
					map.put("sm_job", sm_job);
					List<Record> userInfos = new ArrayList<Record>();
					userInfos.add(r);
					SendMessageUtil.sendMsgList(userInfos, map);
				}
			}
		}
	}

	private void getMyfocus2(Record r) {
		String userno = r.getStr("id");
		String  maxPermiOrgnum =  AppUtils.getOrgNumByUser(r);
		//查出关注的预警，0代表预警
		String selectSql = " select followed_org, max(follower) follower, busi_module, sub_busi_code, mark_code, follow_type, followed_teller from (select case when t.followed_org is null then '"+ maxPermiOrgnum +"' else t.followed_org end followed_org, t.follower,t.busi_module,t.sub_busi_code,t.mark_code,t.follow_type,t.followed_teller from dop_my_follow t where t.busi_module = ? and ((follower = ?  and t.follow_type = '0') " + "or (t.assigned_type = '2' and t.follow_type = '0')))" +
				"  group by followed_org,busi_module,sub_busi_code,mark_code,follow_type,followed_teller order by followed_org,follow_type desc ";
		List<Record> lr = Db.find(selectSql,r.getStr("model"),userno);
		if(lr != null && lr.size() > 0){
			for(Record re : lr){
				Map<String, Object> map = new HashMap<String, Object>();
				StringBuffer sm_key = new StringBuffer();
				StringBuffer sbf = new StringBuffer("【多棱镜】您关注的");
//				String sql="SELECT orgname from sys_org_info where  orgnum = '"+re.getStr("followed_org")+"' and stat = '1'";
//				String orgname=Db.findFirst(sql).getStr("orgname");
				String followed_teller = re.getStr("followed_teller");
//				String userName="";
//				if(AppUtils.StringUtil(followed_teller) !=null){
//					 userName=Db.findFirst("select d.name from sys_user_info d left join sys_org_info p on d.org_id=p.orgnum where d.user_no = '"+followed_teller+"' ").getStr("name");
//				}
				String sub_busi_code = re.getStr("sub_busi_code");
				String mark_code=re.getStr("mark_code");
				String followed_org = re.getStr("followed_org");
				String busi_module = re.getStr("busi_module");
				String extraSql = " from dop_warning_info i left join dop_warning_param p on" +
						" p.warning_code=i.warning_code where i.deptno in (select id from sys_org_info where id = ? or by5 like ?) and p.busi_module=?  " +
						"and  (p.is_use is  null or p.is_use = '1')  and p.warning_type_code= ?  and p.warning_code= ? and substr(i.create_time,0,8) >= to_char(" +
						"sysdate-1,'yyyyMMdd')  and substr(i.create_time,0,8) < to_char(SYSDATE, 'yyyyMMdd') and p.warning_dimension !='2'";
				String wheresql = "";
				if(followed_teller != null){
					wheresql = " and  i.teller_no = '"+followed_teller+"' ";
				}
				List<Record> warning = Db.find("select distinct (select orgname from sys_org_info where orgnum = ? and stat=1) orgname, " +
						" 	(select remark from sys_param_info where key = 'dop_ywtype' and val=p.busi_module) name, " +
						" 	(select warning_name from dop_warning_param where warning_code=p.warning_code and warning_dimension !='2') warncodename, " +//预警名称
						"	(select count(1)" + extraSql + wheresql + "and i.warning_status = '1' "	+ ("1".equals(r.getStr("ROLE_LEVEL")) ? " and i.last_check_stat in('1', '2') and i.deptno in (select id from sys_org_info where id = '000000000' or by5 like '%000000000%')) checked, ": "and i.last_check_stat in('1', '2') and case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no  when '2' then lvl_3_branch_no when '3' then lvl_4_branch_no end ='"+maxPermiOrgnum+"') checked, ")+//已核查
						"   (select count(i.is_question)" + extraSql + wheresql +"  and i.warning_status='1' and is_question = '1'  ) isquestioncount ,"+
						" 	(select d.name from sys_user_info d where d.user_no = i.teller_no ) username,(select count(1) " + extraSql + wheresql +"  and " +
						"  i.warning_status='1'  ) warningcount " +extraSql ,followed_org,followed_org,"%"+followed_org+"%",busi_module,sub_busi_code,mark_code,followed_org,"%"+followed_org+"%",busi_module,sub_busi_code,mark_code,followed_org,"%"+followed_org+"%",busi_module,sub_busi_code,mark_code,followed_org,"%"+followed_org+"%",busi_module,sub_busi_code,mark_code);
				if(warning != null && warning.size() > 0 && warning.get(0).getBigDecimal("warningcount").intValue() != 0){
					sbf.append(warning.get(0).getStr("warncodename")+"预警昨日产生"+warning.get(0).getBigDecimal("warningcount")+"笔，已核查"+ warning.get(0).getBigDecimal("checked") +"笔，确认问题"+warning.get(0).getBigDecimal("isquestioncount").toString()+"笔，请密切关注，切实防范风险！");
					sm_key.append(warning.get(0).getStr("warncodename")).append(",").append(warning.get(0).getBigDecimal("warningcount")).append(",").append(warning.get(0).getBigDecimal("checked")).append(",").append(warning.get(0).getBigDecimal("isquestioncount"));
					map.put("sm_key", sm_key.toString());
					map.put("sm_cont", sbf.toString());
					map.put("sm_job", sm_job);
					List<Record> userInfos = new ArrayList<Record>();
					userInfos.add(r);
					SendMessageUtil.sendMsgList(userInfos, map);
				}
			}
		}
	}

	private void getMyfocus3(Record r) {
		String userno = r.getStr("id");
		String  maxPermiOrgnum =  AppUtils.getOrgNumByUser(r);
		//先查出关注的预警
		String selectSql = "select followed_org, max(follower) follower, busi_module, sub_busi_code, mark_code, follow_type, followed_teller from (select case when t.followed_org is null then '"+ maxPermiOrgnum +"' else t.followed_org end followed_org, t.follower, t.busi_module,t.sub_busi_code,t.mark_code,t.follow_type,t.followed_teller from dop_my_follow t where (follower = ?  and t.follow_type = '0') " + "or (t.assigned_type = '3' and t.follow_type = '0'))" +
				"  group by followed_org,busi_module,sub_busi_code,mark_code,follow_type,followed_teller order by followed_org,follow_type desc ";
		List<Record> lr = Db.find(selectSql,userno);//0代表预警
		if(lr != null && lr.size() > 0){
			for(Record re : lr){
				Map<String, Object> map = new HashMap<String, Object>();
				StringBuffer sm_key = new StringBuffer();
				StringBuffer sbf = new StringBuffer("【多棱镜】您关注的");
//				String sql = "SELECT orgname from sys_org_info where  orgnum = '"
//						+ re.getStr("followed_org") + "' and stat = '1'";
//				String orgname = Db.findFirst(sql).getStr("orgname");
				String followed_teller = re.getStr("followed_teller");
//				String userName="";
//				if(AppUtils.StringUtil(followed_teller) !=null){
//					 userName=Db.findFirst("select d.name from sys_user_info d left join sys_org_info p on d.org_id=p.orgnum where d.user_no = '"+followed_teller+"' ").getStr("name");
//				}
				String sub_busi_code = re.getStr("sub_busi_code");
				String mark_code=re.getStr("mark_code");
				String followed_org = re.getStr("followed_org");
				String busi_module = re.getStr("busi_module");
				String extraSql = " from dop_warning_info i left join dop_warning_param p on" +
						" p.warning_code=i.warning_code where i.deptno in (select id from sys_org_info where id = ? or by5 like ?) and p.busi_module=?  " +
						"and  (p.is_use is  null or p.is_use = '1')  and p.warning_type_code= ?  and p.warning_code= ? and substr(i.create_time,0,8) >= to_char(" +
						"sysdate-1,'yyyyMMdd')  and substr(i.create_time,0,8) < to_char(SYSDATE, 'yyyyMMdd') and p.warning_dimension !='2'";
				String wheresql = "";
				if(followed_teller != null){
					wheresql = " and  i.teller_no = '"+followed_teller+"' ";
				}
				List<Record> warning = Db.find("select distinct (select orgname from sys_org_info where orgnum = ? and stat=1) orgname, " +
						" 	(select remark from sys_param_info where key = 'dop_ywtype' and val=p.busi_module) name, " +
						" 	(select warning_name from dop_warning_param where warning_code=p.warning_code and warning_dimension !='2') warncodename, " +//预警名称
						"	(select count(1)" + extraSql + wheresql + "and i.warning_status = '1'  "	+ ("1".equals(r.getStr("ROLE_LEVEL")) ? " and i.last_check_stat in('1', '2') and i.deptno in (select id from sys_org_info where id = '000000000' or by5 like '%000000000%')) checked, ": "and i.last_check_stat in('1', '2') and case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no  when '2' then lvl_3_branch_no when '3' then lvl_4_branch_no end ='"+maxPermiOrgnum+"') checked, ")+//已核查
						"   (select count(i.is_question)" + extraSql + wheresql +"  and i.warning_status='1' and is_question = '1' ) isquestioncount ,"+
						" 	(select d.name from sys_user_info d where d.user_no = i.teller_no ) username,(select count(1) " + extraSql + wheresql +"  and " +
						"  i.warning_status='1' ) warningcount " +extraSql ,followed_org,followed_org,"%"+followed_org+"%",busi_module,sub_busi_code,mark_code,followed_org,"%"+followed_org+"%",busi_module,sub_busi_code,mark_code,followed_org,"%"+followed_org+"%",busi_module,sub_busi_code,mark_code,followed_org,"%"+followed_org+"%",busi_module,sub_busi_code,mark_code);
				if(warning != null && warning.size() > 0 && warning.get(0).getBigDecimal("warningcount").intValue() != 0){
					sbf.append(warning.get(0).getStr("warncodename")+"预警昨日产生"+warning.get(0).getBigDecimal("warningcount")+"笔，已核查"+ warning.get(0).getBigDecimal("checked") +"笔，确认问题"+warning.get(0).getBigDecimal("isquestioncount").toString()+"笔，请密切关注，切实防范风险！");
					sm_key.append(warning.get(0).getStr("warncodename")).append(",").append(warning.get(0).getBigDecimal("warningcount")).append(",").append(warning.get(0).getBigDecimal("checked")).append(",").append(warning.get(0).getBigDecimal("isquestioncount"));
					map.put("sm_key", sm_key.toString());
					map.put("sm_cont", sbf.toString());
					map.put("sm_job", sm_job);
					List<Record> userInfos = new ArrayList<Record>();
					userInfos.add(r);
					SendMessageUtil.sendMsgList(userInfos, map);
				}
			}
		}
	}
}
