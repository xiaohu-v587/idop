package com.goodcol.util.ext.plugin.quartz;

import java.math.BigDecimal;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.RedisUtil;

public abstract class OuartzJob implements Job {
	public String rwkssj="";
	private String key=super.getClass().getName();
	public String pdsx="ok";
	public OuartzJob(){
		String rwlm=super.getClass().getName();
		Record record=Db.findFirst("select * from SYS_SCHEDULE_TASK where rwzt='0' and rwlm='"+rwlm+"'");
		if(RedisUtil.isRedisOpen()&&record.getStr("jq").equals("0")){
		  if(RedisUtil.existsKey(key)&&RedisUtil.getObject(key).equals(super.getClass().getName())){
				 pdsx="no";
		   }else{
			   RedisUtil.setKeyTimeOut(key, super.getClass().getName(), 60);
			   rwkssj=DateTimeUtil.getTime();
		       }
		}else{
			 rwkssj=DateTimeUtil.getTime();
		}
	}

	public abstract void execute(JobExecutionContext arg0) throws JobExecutionException;
	
	//public abstract void before(JobExecutionContext arg0) throws JobExecutionException;
	//public abstract void exec(JobExecutionContext arg0) throws JobExecutionException;
	//public abstract void after(JobExecutionContext arg0) throws JobExecutionException;
	/**
	 * 定时任务执行成功
	 */
	public void success(){
	    String uuid = AppUtils.getStringSeq();
		String rwjssj=DateTimeUtil.getTime();
		String rwlm=super.getClass().getName();
		BigDecimal rwxh = new BigDecimal("0");//任务序号
		BigDecimal js=new BigDecimal("1");
		//获取该任务类名下的所有数据
		List<Record> tasks=Db.find("select * from SYS_SCHEDULE_TASK where rwlm='"+rwlm+"'");
		for (Record task : tasks) {
			//判断是否启动监控 0为是 1为否
			if(task.getStr("sfjk").equals("0")&&!task.getStr("rwzt").equals("2")){
						//获取定时任务监控中的该条定时任务，取序号，相加
						Record monitor=Db.findFirst("select * from(select * from SYS_SCHEDULETASK_MONITOR where rwbm='"+task.getStr("rwbm")+"' order by RWXH desc) where rownum=1");
						if(monitor!=null){
							 rwxh = monitor.getBigDecimal("rwxh").add(js);
						}
						String rwbm=task.getStr("rwbm");
						String rwmc=task.getStr("rwmc");
						//String bz=task.getStr("bz");
						String bz = AppUtils.getIPAddress();
						String rwzt="0";
						Record record=new Record();
						record.set("id", uuid).set("rwbm", rwbm).set("rwmc",rwmc)
						.set("rwkssj", rwkssj).set("rwjssj", rwjssj).set("rwzt", rwzt).set("rwxh", rwxh)
						.set("bz", bz);
						Db.save("SYS_SCHEDULETASK_MONITOR", record);
				        }else if(task.getStr("sfjk").equals("0")&&task.getStr("rwzt").equals("2")){
				        	//获取定时任务管理中的任务状态，若为临时任务，则删除该条定时任务
				        	QuartzSchedule.removeJob(rwlm,task.getStr("rwbm"));
							Db.deleteById("SYS_SCHEDULE_TASK", task.getStr("id"));
				        }
			
			}
		}
        
		
	/**
	 * 定时任务执行失败
	 */
	public void error(){
		String uuid = AppUtils.getStringSeq();
		String rwjssj=DateTimeUtil.getTime();
		String rwlm=super.getClass().getName();
		BigDecimal rwxh=new BigDecimal("0");//任务序号
		BigDecimal js=new BigDecimal("1");
		//获取该任务类名下的所有数据
		List<Record> tasks=Db.find("select * from SYS_SCHEDULE_TASK where rwlm='"+rwlm+"'");
		for (Record task : tasks) {
			//判断是否启动监控 0为是 1为否
			if(task.getStr("sfjk").equals("0")&&!task.getStr("rwzt").equals("2")){
						//获取定时任务监控中的该条定时任务，取序号，相加
						Record monitor=Db.findFirst("select * from(select * from SYS_SCHEDULETASK_MONITOR where rwbm='"+task.getStr("rwbm")+"' order by RWXH desc) where rownum=1");
						if(monitor!=null){
							 rwxh=monitor.getBigDecimal("rwxh").add(js);
						}
						String rwbm=task.getStr("rwbm");
						String rwmc=task.getStr("rwmc");
						//String bz=task.getStr("bz");
						String bz = AppUtils.getIPAddress();
						String rwzt="1";
						Record record=new Record();
						record.set("id", uuid).set("rwbm", rwbm).set("rwmc",rwmc)
						.set("rwkssj", rwkssj).set("rwjssj", rwjssj).set("rwzt", rwzt).set("rwxh", rwxh)
						.set("bz", bz);
						Db.save("SYS_SCHEDULETASK_MONITOR", record);
				        }else if(task.getStr("sfjk").equals("0")&&task.getStr("rwzt").equals("2")){
				        	//获取定时任务管理中的任务状态，若为临时任务，则删除该条定时任务
				        	QuartzSchedule.removeJob(rwlm,task.getStr("rwbm"));
							Db.deleteById("SYS_SCHEDULE_TASK", task.getStr("id"));
				        }
			
			}
	}
	
	
	
}
