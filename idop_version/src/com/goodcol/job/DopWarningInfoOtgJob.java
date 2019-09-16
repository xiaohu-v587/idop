package com.goodcol.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

public class DopWarningInfoOtgJob extends OuartzJob {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// -----开始时间-----
		String start_time = "";
		// -----结束时间-----
		String end_time = "";
		//始终同步最近31天的数据，因为有些预警会隔几天才会认定、核查、复查。  ---wangyang
		String incflg = "预警信息表Oracle至gbase";
		//记录数
		int record_size = 0;
		//跑批状态
		int run_status = 0;
		List<Record> warningList = null;
		try{
			start_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			//String selectSql = "select * from dop_warning_info";
			warningList = Db.use("default").paginate(1, 10000, "select * ", "from dop_warning_info where data_date>=to_char(sysdate-290,'yyyyMMdd')").getList();//where data_date=to_char(sysdate-1,'yyyyMMdd')
			int pageIndex = 1;
			String deleteSql = "delete from dop_warning_info where data_date>=to_char(curdate()-290,'YYYYMMDD') ";//where data_date=to_char(sysdate-1,'yyyyMMdd')
			Db.use("gbase").update(deleteSql);
			while(warningList != null && !warningList.isEmpty()){
				record_size = warningList.size();
				//共插入30个字段
				String columns = "id,lvl_2_branch_no,lvl_2_branch_name,lvl_3_branch_no,lvl_3_branch_name,lvl_4_branch_no,lvl_4_branch_name," +
						"deptlevel,data_date,teller_no,warning_code,flownum,create_time,storage_time,warning_status,indentify_status," +
						"indentifier,indent_org,indentify_time,last_checker,last_checker_org,last_check_time,last_check_stat,last_approver,last_approver_org," +
						"last_approval_time,checker_level,checker_remark,last_approval_stat,deptno,is_question,indentify_remark,approver_remark,search_check_status,prompt_status ";
				String insertSql = "insert into dop_warning_info ("+columns+")" +
						"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				Db.use("gbase").batch(insertSql, columns, warningList, warningList.size());
				pageIndex++;
				warningList = Db.use("default").paginate(pageIndex, 10000, "select * ", "from dop_warning_info where data_date>=to_char(sysdate-290,'yyyyMMdd')").getList();
			}
			end_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			String start_date=BolusDate.getDateTime("yyyyMMdd");
			String insertSql ="INSERT INTO bs_dap_para.T_DAPLOADIFNO (ENTITY_UID,VERSION,FILEDATE,FILENAME,TABLENAME,PPFID,PRI,STATE)" +
	        " VALUES   ('1111',now(),?,'0','IDOP_IMP_WARNINGINFO','0','0','0')";
			Db.use("gbase").update(insertSql,start_date);
			success();
			run_status=1;
		}catch(Exception e){
			run_status=2;
			error();
			e.printStackTrace();
		}finally{
			Db.use("default").update("insert into PCCM_QUARTZ_RUN(id,START_TIME,END_TIME,RECORD_SIZE,INCFLG,RUN_STATUS) values(?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),start_time,end_time,record_size,incflg,run_status});
		}
	}
}
