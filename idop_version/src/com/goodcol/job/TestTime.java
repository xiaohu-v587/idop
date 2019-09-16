package com.goodcol.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;


public class TestTime extends OuartzJob {
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		if(super.pdsx.equals("no")){
			return;
		}
		System.out.println("这是TestTime测试任务========");
		test();
	}

	public void test() {
		success();
	}

}
