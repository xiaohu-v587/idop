/**
 * 
 */
package com.goodcol.job;


import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

/**
 * @author
 * 
 */
public class PccmSyncInsertUserJob extends OuartzJob {

	public static Logger log = Logger.getLogger(PccmSyncInsertUserJob.class);

	//private static String dbLinkName = "@LK_DAPKF";
	//private static String dbLinkName = "@LK_DAP";
	private static String DEFAULT = "gbase";

	/**
	 * 同步用户 2018年7月9日09:30:48
	 * 
	 * @author liutao
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		try {
			/** gbase数据导入oracle，取消oracledblink处理 */
			//String sql = "drop table HR_V_STAFF_INFO if exists";
			//Db.use(DEFAULT).update(sql);
			//sql = "drop table HR_V_POST_INFO if exists";
			//Db.use(DEFAULT).update(sql);
			//sql = "create table HR_V_STAFF_INFO as select * from HR_V_STAFF_INFO"
			//		+ dbLinkName;
			//Db.use(DEFAULT).update(sql);
			//sql = "create table HR_V_POST_INFO as select * from HR_V_POST_INFO"
			//		+ dbLinkName;
			//Db.use(DEFAULT).update(sql);

			String insertSql = "insert into SYS_USER_INFO "
					+ "  (id, user_no, org_id, name, stat, birthday, "
					+ "  gender, pwd,  worktime, idcard, dele_flag, "
					+ "  cur_post, podate, dept_name, post_level, "
					+ "  user_type, is_teller, phone) "
					+ "  select a.ehr_code, a.ehr_code, a.org_code, "
					+ "       a.staff_name, '0', a.birth_date, "
					+ "       a.gender, 'E10ADC3949BA59ABBE56E057F20F883E', "
					+ "       a.enter_date, a.cert_no, '0', "
					+ "       a.pos_code, a.pos_date, a.staff_id, "
					+ "       b.post_level, a.pos_n_kind, '1',a.MOBLE_PHONE1 "
					+ "  from hr_staff_info a "
					+ "  left join hr_post_info b "
					+ "    on a.pos_code = b.post_id "
					+ "  where a.ehr_code is not null "
					+ "   and a.stf_status <> '5' " 
					+ "   and a.gender not in ('gender') "
					+ "   and a.ehr_code not in (select id from SYS_USER_INFO)";
			Db.use(DEFAULT).update(insertSql);

			String updateSql = " update sys_user_info a "
					+ "   set a.post_level = "
					+ "       (select post_level"
					+ "          from (select b.ehr_code, c.post_level, b.moble_phone1, pos_pay_level "
					+ "                  from hr_staff_info b "
					+ "                  left join hr_post_info c "
					+ "                    on b.pos_code = c.post_id "
					+ "                 where b.stf_status <> '5' "
					+ "                   and b.ehr_code is not null) t "
					+ "         where t.ehr_code = a.id "
					+ "           and t.ehr_code is not null "
					+ "           and a.id is not null), "
					+ "   a.phone = "
					+ "       (select moble_phone1"
					+ "          from (select b.ehr_code, c.post_level, b.moble_phone1, pos_pay_level "
					+ "                  from hr_staff_info b "
					+ "                  left join hr_post_info c "
					+ "                    on b.pos_code = c.post_id "
					+ "                 where b.stf_status <> '5' "
					+ "                   and b.ehr_code is not null) t "
					+ "         where t.ehr_code = a.id "
					+ "           and t.ehr_code is not null "
					+ "           and a.id is not null), "
					+ "   a.pay_level = "
					+ "       (select pos_pay_level"
					+ "          from (select b.ehr_code, c.post_level, b.moble_phone1, pos_pay_level "
					+ "                  from hr_staff_info b "
					+ "                  left join hr_post_info c "
					+ "                    on b.pos_code = c.post_id "
					+ "                 where b.stf_status <> '5' "
					+ "                   and b.ehr_code is not null) t "
					+ "         where t.ehr_code = a.id "
					+ "           and t.ehr_code is not null "
					+ "           and a.id is not null) "
					+ " where a.id in (select ehr_code from hr_staff_info) ";
			Db.use(DEFAULT).update(updateSql);
			
			updateSql = "update sys_user_info u set u.user_level = null" ;
			Db.use(DEFAULT).update(updateSql);
			
			updateSql = "update sys_user_info u set u.user_level=(	select case when  h.pos_pay_level='Y060010' then '03' "
					+" when  h.pos_pay_level='Y060020' then '04' "
					+" when  h.pos_pay_level='Y070010' then '05' "
					+" when  h.pos_pay_level='Y070020' then '06' "
					+" when  h.pos_pay_level='Y080010' then '07' "
					+" when  h.pos_pay_level='Y080020' then '08' "
					+" when  h.pos_pay_level='Y090000' then '09' "
					+" when  h.pos_pay_level='Y100000' then '10' "
					+" when  h.pos_pay_level='Y110000' then '11' "
					+" else '10' end "
					+" from HR_STAFF_INFO h where h.stf_status != '5'"
					+"  and h.ehr_code = u.id ) ";
			Db.use(DEFAULT).update(updateSql);
			
			updateSql = "update sys_user_info u set u.user_level = '10' where u.user_level is null";	
			Db.use(DEFAULT).update(updateSql);
			
			/*List<com.goodcol.core.plugin.activerecord.Record> list = Db.use("gbase").find("select * from sys_user_info");
			if (null != list && list.size() > 1) {
				for (com.goodcol.core.plugin.activerecord.Record record2 : list) {
					Db.use("default").save("sys_user_info", record2);
				}
				
			}*/
			
			success();
		} catch (Exception e) {
			error();
			e.printStackTrace();
		}

	}
}
