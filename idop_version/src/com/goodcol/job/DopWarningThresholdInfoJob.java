package com.goodcol.job;

import java.math.BigDecimal;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

public class DopWarningThresholdInfoJob extends OuartzJob {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// -----开始时间-----
		String start_time = "";
		// -----结束时间-----
		String end_time = "";
		String incflg = "预警阀值信息oracle至gbase";
		// 记录数
		int record_size = 0;
		// 跑批状态
		int run_status = 0;
		List<Record> warningList = null;
		try {
			start_time = BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			String selectSql = "select * ";
			String extraSql = " from idop_param_threshold";
			String deleteSql = "delete from ap_idop.idop_param_threshold";
			Db.use("gbase").update(deleteSql);
			warningList = Db.use("default").paginate(1, 100, selectSql, extraSql).getList();
			int pageIndex = 1;
			while (warningList != null && !warningList.isEmpty()) {
				record_size += warningList.size();
				Object[][] params = new Object[warningList.size()][15];
//				String updateSql = "update ap_idop.idop_param_threshold" +
//					" set TARGET_TYPE = ?, TARGET_VAL = ?, TARGET_VAL1 = ?, TARGET_VAL2 = ?, " +
//						"TARGET_VAL3 = ?, TARGET_VAL4 = ?, TARGET_VAL5 = ?, TARGET_VAL6 = ?, TARGET_VAL7 = ?, TARGET_VAL8 = ?, TARGET_VAL9 = ?" +
//					" where MODULE_TYPE = ? and TARGET_TYPE_NO = ? and TARGET_NO = ? and EFFECTIVE_DATE = ?;";
				String insertSql = "insert into ap_idop.idop_param_threshold(MODULE_TYPE, TARGET_TYPE_NO, TARGET_TYPE, TARGET_NO, TARGET_VAL, TARGET_VAL1, TARGET_VAL2, TARGET_VAL3," +
						" TARGET_VAL4, TARGET_VAL5, TARGET_VAL6, TARGET_VAL7, TARGET_VAL8, TARGET_VAL9, EFFECTIVE_DATE) " +
						"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
				for (int i = 0; i < warningList.size(); i++) {
					Record record = warningList.get(i);
					params[i][0] = record.getStr("module_type");
					params[i][1] = record.getStr("target_type_no");
					params[i][2] = record.getStr("target_type");
					params[i][3] = record.getStr("target_no");
					params[i][4] =  record.get("target_val")==null?null:record.get("target_val").toString();
					params[i][5] =  record.get("target_val1")==null?null:new BigDecimal(record.get("target_val1").toString());
					params[i][6] =  record.get("target_val2")==null?null:new BigDecimal(record.get("target_val2").toString());
					params[i][7] =  record.get("target_val3")==null?null:new BigDecimal(record.get("target_val3").toString());
					params[i][8] =  record.get("target_val4")==null?null:new BigDecimal(record.get("target_val4").toString());
					params[i][9] =  record.get("target_val5")==null?null:new BigDecimal(record.get("target_val5").toString());
					params[i][10] = record.get("target_val6")==null?null:new BigDecimal(record.get("target_val6").toString());
					params[i][11] = record.get("target_val7")==null?null:new BigDecimal(record.get("target_val7").toString());
					params[i][12] = record.get("target_val8")==null?null:new BigDecimal(record.get("target_val8").toString());
					params[i][13] = record.get("target_val9")==null?null:new BigDecimal(record.get("target_val9").toString());
					params[i][14] = record.getStr("effective_date");
				}
				Db.use("gbase").batch(insertSql, params, 100);
				pageIndex++;
				warningList = Db.use("default").paginate(pageIndex, 100, selectSql, extraSql).getList();
			}
			end_time = BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			run_status = 1;
			success();
		} catch (Exception e) {
			error();
			run_status = 2;
			e.printStackTrace();
		} finally {
			Db.use("default")
					.update("insert into PCCM_QUARTZ_RUN(id,START_TIME,END_TIME,RECORD_SIZE,INCFLG,RUN_STATUS) values(?,?,?,?,?,?)",
							new Object[] { AppUtils.getStringSeq(), start_time,
									end_time, record_size, incflg, run_status });
		}
	}

}
