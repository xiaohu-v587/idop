 package com.goodcol.util.ext.plugin.quartz;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;
import java.util.List;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import com.goodcol.core.plugin.IPlugin;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;

public class QuartzSchedule implements IPlugin {
	
	private static SchedulerFactory schedulerFactory = new StdSchedulerFactory(); 
	private Scheduler sched = null;
	public QuartzSchedule() {
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean start() {
		
		try {
			sched = schedulerFactory.getScheduler();
		} catch (SchedulerException e) {
			new RuntimeException(e); 
		}
		List<Record>list=Db.find("select * from SYS_SCHEDULE_TASK where RWZT='0'");
		for(int i=0;i<list.size();i++) {
			String jobClassName = list.get(i).getStr("rwlm");
			String jobCronExp = list.get(i).getStr("rwqdsj");
			String jobname=list.get(i).getStr("rwbm");
			Class clazz;
			try {
				clazz = Class.forName(jobClassName);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			JobDetail job = newJob(clazz).withIdentity(jobname, jobClassName).build();
			CronTrigger trigger = newTrigger().withIdentity(jobname,jobClassName)
					.withSchedule(cronSchedule(jobCronExp)).build();
			Date ft = null;
			try {
				ft = sched.scheduleJob(job, trigger);
				sched.start();
			} catch (SchedulerException e) {
				new RuntimeException(e);
			}
		}
		
		return true;
	} 


	@Override
	public boolean stop() {
		try {
			if (!sched.isShutdown()) {  
                sched.shutdown();  
            }  
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	 

    /** 
     * @Description: 添加一个定时任务 
     * @param jobName 任务名 
     * @param jobGroupName  任务组名 
     * @param triggerName 触发器名 
     * @param triggerGroupName 触发器组名 
     * @param jobClass  任务 
     * @param cron   时间设置，参考quartz说明文档  
     */  
    @SuppressWarnings({ "unchecked", "rawtypes" })  
    public static void addJob(String triggerName,String jobname,String cron) {  
    	Class clazz =null; 
        try {  
        	clazz =Class.forName(triggerName);
            Scheduler sched = schedulerFactory.getScheduler();  
            JobDetail jobDetail= JobBuilder.newJob(clazz).withIdentity(jobname, triggerName).build();
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            triggerBuilder.withIdentity(jobname, triggerName);
            triggerBuilder.startNow();
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();
            sched.scheduleJob(jobDetail, trigger);  
            if (!sched.isShutdown()) { 
                sched.start(); 
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  

    /** 
     * @Description: 修改一个任务的触发时间
     *  
     * @param jobName 
     * @param jobGroupName
     * @param triggerName 触发器名
     * @param triggerGroupName 触发器组名 
     * @param cron   时间设置，参考quartz说明文档   
     */  
    public static void modifyJobTime(String triggerName,String jobName,String cron) {
        try {  
            Scheduler sched = schedulerFactory.getScheduler();  
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, triggerName);
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);  
            if (trigger == null) {  
                return;  
            }  

            String oldTime = trigger.getCronExpression();  
            if (!oldTime.equalsIgnoreCase(cron)) { 
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                triggerBuilder.withIdentity(jobName, triggerName);
                triggerBuilder.startNow();
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
                trigger = (CronTrigger) triggerBuilder.build();
                sched.rescheduleJob(triggerKey, trigger);
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  

    /** 
     * @Description: 移除一个任务 
     *  
     * @param jobName 
     * @param jobGroupName 
     * @param triggerName 
     * @param triggerGroupName 
     */  
    public static void removeJob(String triggerName,String jobName) {  
        try {  
            Scheduler sched = schedulerFactory.getScheduler();  

            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, triggerName);

            sched.pauseTrigger(triggerKey);// 停止触发器  
            sched.unscheduleJob(triggerKey);// 移除触发器  
            sched.deleteJob(JobKey.jobKey(jobName, triggerName));// 删除任务  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  

    /** 
     * @Description:启动所有定时任务 
     */  
    public static void startJobs() {  
        try {  
            Scheduler sched = schedulerFactory.getScheduler();  
            sched.start();  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  

    /** 
     * @Description:关闭所有定时任务 
     */  
    public static void shutdownJobs() {  
        try {  
            Scheduler sched = schedulerFactory.getScheduler();  
            if (!sched.isShutdown()) {  
                sched.shutdown();  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
}

