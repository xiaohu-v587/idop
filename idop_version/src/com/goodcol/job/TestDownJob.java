package com.goodcol.job;

import java.util.HashMap;
import java.util.Map;

import org.quartz.JobExecutionContext;

import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;
import com.goodcol.util.zxgldbutil.DownDetailUtil;


public class TestDownJob extends OuartzJob {
	@Override
	public void execute(JobExecutionContext arg0) {
		System.out.println("明细下载测试定时任务开始：" + DateTimeUtil.getTime());
		try {
			String org =  "000000000";
			String downFlag =  "SQL_CUST_CNT_MY";
			String user_no="8888";
			String user_name="系统管理员";
			String data_date=AppUtils.findGNewDate();
			//创建下载任务
			Map<String, Object> taskmap = new HashMap<String, Object>();
			taskmap.put("org", org);
			taskmap.put("downFlag", downFlag);
			taskmap.put("user_no", user_no);
			taskmap.put("user_name", user_name);
			String rs = DownDetailUtil.DownTask(taskmap);
			if("T".equals(rs)){
				System.out.println("明细下载任务创建成功!");
			}else {
				System.out.println("明细下载测试任务创建失败!" );
			}
			success();
		} catch (Exception e) {
			error();
			System.out.println("明细下载测试定时任务执行失败：" + e);
		}
		System.out.println("明细下载测试定时任务结束：" + DateTimeUtil.getTime());

	}

}
