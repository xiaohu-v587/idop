package com.goodcol.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.util.ext.plugin.quartz.OuartzJob;

public class myJob extends OuartzJob {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		if(super.pdsx.equals("no")){
			return;
		}
		System.out.println("这是myjob定时任务==========");
		test();
	}

	public void test() {
		try {
			String a[] = { "1" };
			String aString = a[6];
			System.out.println(aString);
		} catch (Exception e) {
			error();
		}
		
	}
}
