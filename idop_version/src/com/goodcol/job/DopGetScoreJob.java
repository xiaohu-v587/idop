package com.goodcol.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

public class DopGetScoreJob extends OuartzJob{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// -----开始时间-----
		String start_time = "";
		// -----结束时间-----
		String end_time = "";
		String incflg = "机构月度得分gbase至Oracle";
		//记录数
		int record_size = 0;
		//跑批状态
		int run_status = 0;
		// 查询月度得分的信息-
		List<Record> scoreList = null;
		try{
			start_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			scoreList = Db.use("gbase").find(" select * from dop_org_score_detail where data_month>=to_char(add_months(curdate(),-6),'yyyyMM')");
			if (scoreList != null && !scoreList.isEmpty()) {
				record_size = scoreList.size();
				String insertSql = "insert into dop_org_score_detail(LVL_2_BRANCH_NO,LVL_2_BRANCH_NAME,LVL_3_BRANCH_NO,LVL_3_BRANCH_NAME,LVL_4_BRANCH_NO,LVL_4_BRANCH_NAME,DEPTLEVEL,MODULE,DATA_MONTH,DATA_DATE,SCORE,SCOREVALUE,INDEXNAME,DEPTNO)" +
						" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				String[][] params = new String[scoreList.size()][14];//共14个字段
				for (int i = 0; i < scoreList.size(); i++) {
					params[i][0] = scoreList.get(i).getStr("lvl_2_branch_no");
					params[i][1] = scoreList.get(i).getStr("lvl_2_branch_name");
					params[i][2] = scoreList.get(i).getStr("lvl_3_branch_no");
					params[i][3] = scoreList.get(i).getStr("lvl_3_branch_name");
					params[i][4] = scoreList.get(i).getStr("lvl_4_branch_no");
					params[i][5] = scoreList.get(i).getStr("lvl_4_branch_name");
					params[i][6] = scoreList.get(i).getStr("deptlevel");
					params[i][7] = scoreList.get(i).getStr("module");
					params[i][8] = scoreList.get(i).getStr("data_month");
					params[i][9] = scoreList.get(i).getStr("data_date");
					params[i][10] = scoreList.get(i).getBigDecimal("score").toString();
					params[i][11] = scoreList.get(i).getStr("scorevalue");
					params[i][12] = scoreList.get(i).getStr("indexname");
					params[i][13] = scoreList.get(i).getStr("deptno");
				}
				//插入上月得分数据之前先清空Oracle中上月数据
				String deleteSql = "delete from dop_org_score_detail where data_month>=to_char(add_months(sysdate,-6),'yyyyMM')";
				Db.use("default").update(deleteSql);
				Db.use("default").batch(insertSql,params,scoreList.size());
			}
			end_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			success();
			run_status=1;
		}catch(Exception e){
			error();
			run_status=2;
			e.printStackTrace();
		}finally{
			Db.use("default").update("insert into PCCM_QUARTZ_RUN(id,START_TIME,END_TIME,RECORD_SIZE,INCFLG,RUN_STATUS) values(?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),start_time,end_time,record_size,incflg,run_status});
		}
		
	}

}
