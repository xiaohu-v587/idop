package com.goodcol.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

public class DopAlgoriOtgJob extends OuartzJob {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// 权重参数Oracle至gbase
		try{
			List<Record> algorithmsonList = Db.use("default").find("select * from dop_algorithmson_info");
			List<Record> algorithmList = Db.use("default").find("select * from dop_algorithm_info");
			//12个字段
			String columns1 = "id,pid,element1,operator1,element2,operator2,levels,marks,byzd1,byzd2,byzd3,numbers";
			//13个字段
			String columns2 = "id,ywtype,indexnum,indexname,assigntype,caltype,apply,marks,weight,describtion,byzd1,byzd2,flag";
			String insertSql1 = "insert into dop_algorithmson_info("+columns1+")values(?,?,?,?,?,?,?,?,?,?,?,?)";
			String insertSql2 = "insert into dop_algorithm_info("+columns2+")values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			String delSql = "delete from ";
			Db.use("gbase").update(delSql+"dop_algorithmson_info");
			Db.use("gbase").batch(insertSql1, columns1,algorithmsonList,algorithmsonList.size());
			Db.use("gbase").update(delSql+"dop_algorithm_info");
			Db.use("gbase").batch(insertSql2, columns2,algorithmList,algorithmList.size());
			success();
		}catch(Exception e){
			error();
			e.printStackTrace();
		}
	}

}
