package com.goodcol.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

public class DopWarningInfoJob extends OuartzJob{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// -----开始时间-----
		String start_time = "";
		// -----结束时间-----
		String end_time = "";
		String incflg = "预警信息gbase至Oracle";
		//记录数
		int record_size = 0;
		//跑批状态
		int run_status = 0;
		List<Record> warningList = null;
		try{
			start_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			String selectSql = "select lvl_2_branch_no,lvl_2_branch_name,lvl_3_branch_no,lvl_3_branch_name,lvl_4_branch_no,lvl_4_branch_name," +
					"deptlevel,data_date,teller_no,warning_code,flownum,create_time,case deptlevel when '0' then '000000000' when '1' then lvl_2_branch_no when '2' then lvl_3_branch_no" +
				" when '3' then lvl_4_branch_no end deptno ";//from idop_warnings_val_list where data_date=to_char(curdate()-1,'yyyyMMdd')
			String extraSql = " from idop_warnings_val_list where 1=1 order by data_date ";//where data_date=to_char(curdate()-1,'yyyyMMdd')
			warningList = Db.use("gbase").paginate(1, 100, selectSql, extraSql).getList();
			int pageIndex = 1;
			while(warningList != null && !warningList.isEmpty()){
				record_size += warningList.size();
				String[][] params = new String[warningList.size()][19];
				//共插入19个字段
				String insertSql = "insert into dop_warning_info (id,lvl_2_branch_no,lvl_2_branch_name,lvl_3_branch_no,lvl_3_branch_name,lvl_4_branch_no,lvl_4_branch_name," +
						"deptlevel,data_date,teller_no,warning_code,flownum,create_time,storage_time,warning_status,indentify_status,deptno,last_check_stat,checker_level)values(?,?,?,?,?,?,?," +
						"?,?,?,?,?,?,?,?,?,?,?,?)";
				
				for (int i = 0; i < warningList.size(); i++) {
					Record record = warningList.get(i);
					params[i][0] = AppUtils.getStringSeq();
					params[i][1] = record.getStr("lvl_2_branch_no");
					params[i][2] = record.getStr("lvl_2_branch_name");
					params[i][3] = record.getStr("lvl_3_branch_no");
					params[i][4] = record.getStr("lvl_3_branch_name");
					params[i][5] = record.getStr("lvl_4_branch_no");
					params[i][6] = record.getStr("lvl_4_branch_name");
					params[i][7] = record.getStr("deptlevel");
					params[i][8] = record.getStr("data_date");
					params[i][9] = record.getStr("teller_no");
					params[i][10] = record.getStr("warning_code");
					params[i][11] = record.getStr("flownum");
					params[i][12] = record.getStr("create_time");
					//需要初始化的字段
					params[i][13] = BolusDate.getDateTime("yyyyMMddhhmmss");//storage_time
					params[i][14] = "1";//warning_status是否是预警
					String is_manual_indentify = Db.use("default").findFirst("select * from dop_warning_param where warning_code=?",record.get("warning_code")).getStr("is_manual_indentify");
					if("0".equals(is_manual_indentify)){//是否需要人工认定0否，1是
						params[i][15] = "9";//indentify_status
						params[i][17] = "0";//无需认定的预警 核查状态为待核查并指定核查层级
						params[i][18] = Db.use("default").findFirst("select * from dop_warning_param where warning_code=?",record.get("warning_code")).getStr("check_level");
					}
					if("1".equals(is_manual_indentify)){
						params[i][15] = "0";//indentify_status待认定
					}
					params[i][16] = record.getStr("deptno");
				}
				String deleteSql = "delete from dop_warning_info where data_date=to_char(sysdate-1,'yyyyMMdd')";
				Db.use("default").update(deleteSql);
				Db.use("default").batch(insertSql, params,10);
				pageIndex++;
				warningList=Db.use("gbase").paginate(pageIndex, 100, selectSql, extraSql).getList();
			}
			/*warningList = Db.use("gbase").find("select lvl_2_branch_no,lvl_2_branch_name,lvl_3_branch_no,lvl_3_branch_name,lvl_4_branch_no,lvl_4_branch_name," +
					"deptlevel,data_date,teller_no,warning_code,flownum,create_time,case deptlevel when '0' then '000000000' when '1' then lvl_2_branch_no when '2' then lvl_3_branch_no" +
				" when '3' then lvl_4_branch_no end deptno ");
			if(warningList != null && !warningList.isEmpty()){
				String[][] params = new String[warningList.size()][19];
				//共插入19个字段
				String insertSql = "insert into dop_warning_info (id,lvl_2_branch_no,lvl_2_branch_name,lvl_3_branch_no,lvl_3_branch_name,lvl_4_branch_no,lvl_4_branch_name," +
						"deptlevel,data_date,teller_no,warning_code,flownum,create_time,storage_time,warning_status,indentify_status,deptno,last_check_stat,checker_level)values(?,?,?,?,?,?,?," +
						"?,?,?,?,?,?,?,?,?,?,?,?)";
				
				for (int i = 0; i < warningList.size(); i++) {
					Record record = warningList.get(i);
					params[i][0] = AppUtils.getStringSeq();
					params[i][1] = record.getStr("lvl_2_branch_no");
					params[i][2] = record.getStr("lvl_2_branch_name");
					params[i][3] = record.getStr("lvl_3_branch_no");
					params[i][4] = record.getStr("lvl_3_branch_name");
					params[i][5] = record.getStr("lvl_4_branch_no");
					params[i][6] = record.getStr("lvl_4_branch_name");
					params[i][7] = record.getStr("deptlevel");
					params[i][8] = record.getStr("data_date");
					params[i][9] = record.getStr("teller_no");
					params[i][10] = record.getStr("warning_code");
					params[i][11] = record.getStr("flownum");
					params[i][12] = record.getStr("create_time");
					//需要初始化的字段
					params[i][13] = BolusDate.getDateTime("yyyyMMddhhmmss");//storage_time
					params[i][14] = "1";//warning_status是否是预警
					String is_manual_indentify = Db.use("default").findFirst("select * from dop_warning_param where warning_code=?",record.get("warning_code")).getStr("is_manual_indentify");
					if("0".equals(is_manual_indentify)){//是否需要人工认定0否，1是
						params[i][15] = "9";//indentify_status
						params[i][17] = "0";//无需认定的预警 核查状态为待核查并指定核查层级
						params[i][18] = Db.use("default").findFirst("select * from dop_warning_param where warning_code=?",record.get("warning_code")).getStr("check_level");
					}
					if("1".equals(is_manual_indentify)){
						params[i][15] = "0";//indentify_status待认定
					}
					params[i][16] = record.getStr("deptno");
				}
				String deleteSql = "delete from dop_warning_info where data_date=to_char(sysdate-1,'yyyyMMdd')";
				Db.use("default").update(deleteSql);
				Db.use("default").batch(insertSql, params,10);
			}*/
			end_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			run_status=1;
			success();
		}catch(Exception e){
			error();
			run_status=2;
			e.printStackTrace();
		}finally{
			Db.use("default").update("insert into PCCM_QUARTZ_RUN(id,START_TIME,END_TIME,RECORD_SIZE,INCFLG,RUN_STATUS) values(?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),start_time,end_time,record_size,incflg,run_status});
		}
	}

}
