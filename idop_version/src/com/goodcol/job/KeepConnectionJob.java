package com.goodcol.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

public class KeepConnectionJob extends OuartzJob{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String sql = "select 1 from dual";
		Db.find(sql);
		Db.use("gbase").find(sql);
	}

}
