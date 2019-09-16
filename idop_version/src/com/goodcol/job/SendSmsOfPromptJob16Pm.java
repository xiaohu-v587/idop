package com.goodcol.job;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.SendMessageUtil;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

public class SendSmsOfPromptJob16Pm extends OuartzJob{
	private static final String sm_job = "IdopSendSmsOfPromptJob16Pm";
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// -----开始时间-----
		String start_time = "";
		// -----结束时间-----
		String end_time = "";
		String incflg = "提示信息短信提醒16pm";
		//记录数
		int record_size =0;
		//跑批状态
		int run_status = 0;
		try {
			start_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
		
			//各个角色需要发送的人员
			String userSql0="  select sui.user_no, sui.name, sui.phone,              " +
					" 			      sui.org_id,sri.role_level                      " +
					"   from sys_user_info sui ,sys_user_role sur ,sys_role_info sri " +
					"  where sri.name = '系统管理员'                                     " +
					"    and sur.role_id = sri.id       				             " +
					"	 and sui.user_no=sur.user_id                  		         " ;
			
			String userSql1="  select sui.user_no, sui.name, sui.phone,     		 " +
					" 			      sui.org_id,sri.role_level                      " +
					"   from sys_user_info sui ,sys_user_role sur ,sys_role_info sri " +
					"  where sri.name = '系统管理员（省行）'                               	 " +
					"    and sur.role_id = sri.id       				             " +
					"	 and sui.user_no=sur.user_id                  		         " ;
			
			String userSql2="  select sui.user_no, sui.name, sui.phone,     		 " +
					" 			      sui.org_id,sri.role_level                      " +
					"   from sys_user_info sui ,sys_user_role sur ,sys_role_info sri " +
					"  where sri.name = '系统管理员（分行）'                              	 " +
					"    and sur.role_id = sri.id       				             " +
					"	 and sui.user_no=sur.user_id                  		         " ;
					
			String userSql3="  select sui.user_no, sui.name, sui.phone,     		 " +
					" 			      sui.org_id,sri.role_level                      " +
					"   from sys_user_info sui ,sys_user_role sur ,sys_role_info sri " +
					"  where sri.name = '管理层角色（省行）'                              	 " +
					"    and sur.role_id = sri.id       				             " +
					"	 and sui.user_no=sur.user_id                  		         " ;
									
			String userSql4="  select sui.user_no, sui.name, sui.phone,    		 	 " +
					" 			      sui.org_id,sri.role_level                      " +
					"   from sys_user_info sui ,sys_user_role sur ,sys_role_info sri " +
					"  where sri.name = '管理层角色（分行）'                              	 " +
					"    and sur.role_id = sri.id       				             " +
					"	 and sui.user_no=sur.user_id                  		         " ;
							
			String userSql5="  select sui.user_no, sui.name, sui.phone,     		 " +
					" 			      sui.org_id,sri.role_level                      " +
					"   from sys_user_info sui ,sys_user_role sur ,sys_role_info sri " +
					"  where sri.name = '管理层角色（管辖支行）'                           	 " +
					"    and sur.role_id = sri.id       				             " +
					"	 and sui.user_no=sur.user_id                  		         " ;
										
			String userSql6="  select sui.user_no, sui.name, sui.phone,     		 " +
					" 			      sui.org_id,sri.role_level           " +
					"   from sys_user_info sui ,sys_user_role sur ,sys_role_info sri " +
					"  where sri.name = '运营专家角色（省行）'                               " +
					"    and sui.model=?                                             " +
					"    and sur.role_id = sri.id       				             " +
					"	 and sui.user_no=sur.user_id                  		         " ;
							
			String userSql7="  select sui.user_no, sui.name, sui.phone,        		 " +
					" 			      sui.org_id,sri.role_level,sui.model            " +
					"   from sys_user_info sui ,sys_user_role sur ,sys_role_info sri " +
					"  where sri.name = '运营专家角色（分行）'                               " +
					"    and sui.model=?                                             " +
					"    and sur.role_id = sri.id       				             " +
					"	 and sui.user_no=sur.user_id                  		         " ;
					
			String userSql8="  select sui.user_no, sui.name, sui.phone,		         " +
					" 			      sui.org_id,sri.role_level,sui.model            " +
					"   from sys_user_info sui ,sys_user_role sur ,sys_role_info sri " +
					"  where sri.name = '运营专家角色（管辖支行）'                           	 " +
					"    and sur.role_id = sri.id       				             " +
					"	 and sui.user_no=sur.user_id                  		         " ;
					
			
			String userSql9="  select sui.user_no, sui.name, sui.phone,			     " +
					" 			      sui.org_id,sri.role_level                      " +
					"   from sys_user_info sui ,sys_user_role sur ,sys_role_info sri " +
					"  where sri.name = '网点负责人'                         		  	 " +
					"    and sur.role_id = sri.id       				             " +
					"	 and sui.user_no=sur.user_id                  		         " ;
			
			String userSql10="  select sui.user_no, sui.name, sui.phone,	     	 " +
					" 			      sui.org_id,sri.role_level                      " +
					"   from sys_user_info sui ,sys_user_role sur ,sys_role_info sri " +
					"  where sri.name = '网点授权角色'                         		  	 " +
					"    and sur.role_id = sri.id       				             " +
					"	 and sui.user_no=sur.user_id                  		         " ;
			
			String userSql11="  select sui.user_no, sui.name, sui.phone,    " +
					" 			      sui.org_id,sri.role_level                      " +
					"   from sys_user_info sui ,sys_user_role sur ,sys_role_info sri " +
					"  where sri.name = '网点经办角色'                         		  	 " +
					"    and sur.role_id = sri.id       				             " +
					"	 and sui.user_no=sur.user_id                  		         " ;
			
			String userSql12="  select sui.user_no, sui.name, sui.phone    " +
					"     from dop_warning_info dwi, dop_warning_param dwp,sys_user_info sui " +
					"    where dwp.warning_code = dwi.warning_code                " +
					"      and dwp.warning_dimension = '2'                        " +
					"      and dwp.is_key_dxtz = '1'                              " +
					"      and substr(dwi.teller_no,14) = sui.user_no		 		          " +
					"      and instr(dwp.is_key_jsf,'个人')>0  					  " +
					"      and sui.user_no=?          	    					  ";
			
		
			
			//设置查询时间段
			String now=BolusDate.getDate();
			String nowdate=now+"160000";
			String olddate=now+"110000";
			
			//查询发送的提示名称
			String listSql="select dwp.busi_module,dwp.warning_code,dwp.warning_name,dwp.is_key_jsf,dwp.message_org,dwp.message_person " +
					" from dop_warning_info dwi " +
					" left join dop_warning_param dwp " +
					" on dwi.warning_code = dwp.warning_code " +
					" where dwp.warning_dimension = '2' " +
					" and dwp.is_key_dxtz = '1' " +
					" and dwi.prompt_status = '0' " +
					" and '"+olddate+"' < dwi.create_time " +
					" and dwi.create_time < '"+nowdate+"' " +
					" group by dwp.busi_module,dwp.warning_code,dwp.warning_name,dwp.is_key_jsf,dwp.message_org,dwp.message_person";
			List<Record> lists = Db.find(listSql);
			
			
			
			//查询发送的提示信息
			for (Record list : lists) {
				String warning_code=list.getStr("warning_code");
				String warning_name=list.getStr("warning_name");
				String jsf=list.getStr("is_key_jsf");
				String message_org=list.getStr("message_org");
				String message_person=list.getStr("message_person");
				String model=list.getStr("busi_module");
				
				//提示状态 0：未发送短信 1：已发送短信 2：短信发送失败
				int status0=0;
				int status1=0;
				int status2=0;
				int status3=0;
				int status4=0;
				int status5=0;
				int status6=0;
				int status7=0;
				int status8=0;
				int status9=0;
				int status10=0;
				int status11=0;
				int status12=0;
				
				if(jsf.contains("系统管理员")){
					List<Record> users0s=Db.find(userSql0);
					if(!users0s.isEmpty()){
						for (Record users0 : users0s) {
						
							Record c=Db.findFirst("select count(*) count from dop_warning_info dwi where warning_code=? and dwi.prompt_status='0'  " +
								"and  '"+olddate+"'< dwi.create_time and dwi.create_time<'"+nowdate+"' ",warning_code);
							String count=c.getBigDecimal("count")+"";
							
							StringBuffer cont = new StringBuffer();
							cont.append("【多棱镜】【"+warning_name+"】"+count+"笔,"+message_org+"。");	
							
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("sm_cont", cont.toString());
							map.put("sm_job", sm_job);
							String sm_key =warning_name+","+count+","+message_org;
							map.put("sm_key", sm_key);
							List<Record> lr = new ArrayList<Record>();
							lr.add(users0);
							SendMessageUtil.sendMsgList(lr, map);
							record_size+=lr.size();	
						}
						status0=1;
					}
					
				}
				
				if(jsf.contains("系统管理员（省行）")){
					List<Record> users1s=Db.find(userSql1);
					if(!users1s.isEmpty()){
						for (Record users1 : users1s) {
							Record c=Db.findFirst("select count(*) count from dop_warning_info dwi where warning_code=?  and dwi.prompt_status='0'  " +
								"and  '"+olddate+"'< dwi.create_time and dwi.create_time<'"+nowdate+"' ",warning_code);
							String count=c.getBigDecimal("count")+"";
							
							StringBuffer cont = new StringBuffer();
							cont.append("【多棱镜】【"+warning_name+"】"+count+"笔,"+message_org+"。");	
							
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("sm_cont", cont.toString());
							map.put("sm_job", sm_job);
							String sm_key =warning_name+","+count+","+message_org;
							map.put("sm_key", sm_key);
							List<Record> lr = new ArrayList<Record>();
							lr.add(users1);
							SendMessageUtil.sendMsgList(lr, map);
							record_size+=lr.size();	
						}
						status1=1;
					}
				}
				
				if(jsf.contains("系统管理员（分行）")){
					List<Record> users2s=Db.find(userSql2);
					if(!users2s.isEmpty()){
						for (Record users2 : users2s) {
							String sjfw=AppUtils.getOrgNumByUser(users2);
							String roleLevel=users2.getStr("ROLE_LEVEL");
							List<Record> c=Db.find("select count(*) count,lvl_2_branch_no from dop_warning_info dwi where warning_code=? and dwi.prompt_status='0'  " +
								"and  '"+olddate+"'< dwi.create_time and dwi.create_time<'"+nowdate+"' group by lvl_2_branch_no  ",warning_code);
							for (Record record : c) {
								String count=record.getBigDecimal("count")+"";
								String org=record.getStr("lvl_2_branch_no");
								if(sjfw.equals(org)){
									StringBuffer cont = new StringBuffer();
									cont.append("【多棱镜】【"+warning_name+"】"+count+"笔,"+message_org+"。");	
									
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("sm_cont", cont.toString());
									map.put("sm_job", sm_job);
									String sm_key =warning_name+","+count+","+message_org;
									map.put("sm_key", sm_key);
									List<Record> lr = new ArrayList<Record>();
									lr.add(users2);
									SendMessageUtil.sendMsgList(lr, map);
									record_size+=lr.size();
								}
							}
						}
						status2=1;
					}
				}
				
				if(jsf.contains("管理层角色（省行）")){
					List<Record> users3s=Db.find(userSql3);
					if(!users3s.isEmpty()){
						for (Record users3 : users3s) {
							Record c=Db.findFirst("select count(*) count from dop_warning_info dwi where warning_code=? and dwi.prompt_status='0'  " +
								"and  '"+olddate+"'< dwi.create_time and dwi.create_time<'"+nowdate+"' ",warning_code);
							String count=c.getBigDecimal("count")+"";
							
							StringBuffer cont = new StringBuffer();
							cont.append("【多棱镜】【"+warning_name+"】"+count+"笔,"+message_org+"。");	
							
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("sm_cont", cont.toString());
							map.put("sm_job", sm_job);
							String sm_key =warning_name+","+count+","+message_org;
							map.put("sm_key", sm_key);
							List<Record> lr = new ArrayList<Record>();
							lr.add(users3);
							SendMessageUtil.sendMsgList(lr, map);
							record_size+=lr.size();	
						}
						status3=1;
					}
				}
				
				if(jsf.contains("管理层角色（分行）")){
					List<Record> users4s=Db.find(userSql4);
					if(!users4s.isEmpty()){
						for (Record users4 : users4s) {
							String sjfw=AppUtils.getOrgNumByUser(users4);
							String roleLevel=users4.getStr("ROLE_LEVEL");
							List<Record> c=Db.find("select count(*) count,lvl_2_branch_no from dop_warning_info dwi where warning_code=? and dwi.prompt_status='0'  " +
								"and  '"+olddate+"'< dwi.create_time and dwi.create_time<'"+nowdate+"' group by lvl_2_branch_no  ",warning_code);
							for (Record record : c) {
								String count=record.getBigDecimal("count")+"";
								String org=record.getStr("lvl_2_branch_no");
								if(sjfw.equals(org)){
									StringBuffer cont = new StringBuffer();
									cont.append("【多棱镜】【"+warning_name+"】"+count+"笔,"+message_org+"。");	
									
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("sm_cont", cont.toString());
									map.put("sm_job", sm_job);
									String sm_key =warning_name+","+count+","+message_org;
									map.put("sm_key", sm_key);
									List<Record> lr = new ArrayList<Record>();
									lr.add(users4);
									SendMessageUtil.sendMsgList(lr, map);
									record_size+=lr.size();
								}
							}
						}
						status4=1;
					}
				}
					
				if(jsf.contains("管理层角色（管辖支行）")){
					List<Record> users5s=Db.find(userSql5);
					if(!users5s.isEmpty()){
						for (Record users5 : users5s) {
							String sjfw=AppUtils.getOrgNumByUser(users5);
							String roleLevel=users5.getStr("ROLE_LEVEL");
							List<Record> c=Db.find("select count(*) count,lvl_3_branch_no from dop_warning_info dwi where warning_code=? and dwi.prompt_status='0'  " +
								"and  '"+olddate+"'< dwi.create_time and dwi.create_time<'"+nowdate+"' group by lvl_3_branch_no  ",warning_code);
							for (Record record : c) {
								String count=record.getBigDecimal("count")+"";
								String org=record.getStr("lvl_3_branch_no");
								if(sjfw.equals(org)){
									StringBuffer cont = new StringBuffer();
									cont.append("【多棱镜】【"+warning_name+"】"+count+"笔,"+message_org+"。");	
									
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("sm_cont", cont.toString());
									map.put("sm_job", sm_job);
									String sm_key =warning_name+","+count+","+message_org;
									map.put("sm_key", sm_key);
									List<Record> lr = new ArrayList<Record>();
									lr.add(users5);
									SendMessageUtil.sendMsgList(lr, map);
									record_size+=lr.size();
								}
							}
						}
						status5=1;
					}
				}	
					
				
				if(jsf.contains("运营专家角色（省行）")){
					List<Record> users6s=Db.find(userSql6,model);
					if(!users6s.isEmpty()){
						for (Record users6 : users6s) {
							Record c=Db.findFirst("select count(*) count from dop_warning_info dwi where warning_code=? and dwi.prompt_status='0'  " +
								"and  '"+olddate+"'< dwi.create_time and dwi.create_time<'"+nowdate+"'  ",warning_code);
							String count=c.getBigDecimal("count")+"";
							
							StringBuffer cont = new StringBuffer();
							cont.append("【多棱镜】【"+warning_name+"】"+count+"笔,"+message_org+"。");	
							
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("sm_cont", cont.toString());
							map.put("sm_job", sm_job);
							String sm_key =warning_name+","+count+","+message_org;
							map.put("sm_key", sm_key);
							List<Record> lr = new ArrayList<Record>();
							lr.add(users6);
							SendMessageUtil.sendMsgList(lr, map);
							record_size+=lr.size();	
						}
						status6=1;
					}
				}
				
				if(jsf.contains("运营专家角色（分行）")){
					List<Record> users7s=Db.find(userSql7,model);
					if(!users7s.isEmpty()){
						for (Record users7 : users7s) {
							String sjfw=AppUtils.getOrgNumByUser(users7);
							String roleLevel=users7.getStr("ROLE_LEVEL");
							List<Record> c=Db.find("select count(*) count,lvl_2_branch_no from dop_warning_info dwi where warning_code=? and dwi.prompt_status='0'  " +
								"and  '"+olddate+"'< dwi.create_time and dwi.create_time<'"+nowdate+"' group by lvl_2_branch_no  ",warning_code);
							for (Record record : c) {
								String count=record.getBigDecimal("count")+"";
								String org=record.getStr("lvl_2_branch_no");
								if(sjfw.equals(org)){
									StringBuffer cont = new StringBuffer();
									cont.append("【多棱镜】【"+warning_name+"】"+count+"笔,"+message_org+"。");	
									
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("sm_cont", cont.toString());
									map.put("sm_job", sm_job);
									String sm_key =warning_name+","+count+","+message_org;
									map.put("sm_key", sm_key);
									List<Record> lr = new ArrayList<Record>();
									lr.add(users7);
									SendMessageUtil.sendMsgList(lr, map);
									record_size+=lr.size();
								}
							}
						}
						status7=1;
					}
				}
				
				if(jsf.contains("运营专家角色（管辖支行）")){
					List<Record> users8s=Db.find(userSql8);
					if(!users8s.isEmpty()){
						for (Record users8 : users8s) {
							String sjfw=AppUtils.getOrgNumByUser(users8);
							String roleLevel=users8.getStr("ROLE_LEVEL");
							List<Record> c=Db.find("select count(*) count,lvl_3_branch_no from dop_warning_info dwi where warning_code=? and dwi.prompt_status='0'  " +
								"and  '"+olddate+"'< dwi.create_time and dwi.create_time<'"+nowdate+"' group by lvl_3_branch_no  ",warning_code);
							for (Record record : c) {
								String count=record.getBigDecimal("count")+"";
								String org=record.getStr("lvl_3_branch_no");
								if(sjfw.equals(org)){
									StringBuffer cont = new StringBuffer();
									cont.append("【多棱镜】【"+warning_name+"】"+count+"笔,"+message_org+"。");	
									
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("sm_cont", cont.toString());
									map.put("sm_job", sm_job);
									String sm_key =warning_name+","+count+","+message_org;
									map.put("sm_key", sm_key);
									List<Record> lr = new ArrayList<Record>();
									lr.add(users8);
									SendMessageUtil.sendMsgList(lr, map);
									record_size+=lr.size();
								}
							}
						}
						status8=1;
					}
				}
					
				if(jsf.contains("网点负责人角色")){
					List<Record> users9s=Db.find(userSql9);
					if(!users9s.isEmpty()){
						for (Record users9 : users9s) {
							String sjfw=AppUtils.getOrgNumByUser(users9);
							String roleLevel=users9.getStr("ROLE_LEVEL");
							List<Record> c=Db.find("select count(*) count,lvl_4_branch_no from dop_warning_info dwi where warning_code=? and dwi.prompt_status='0'  " +
								"and  '"+olddate+"'< dwi.create_time and dwi.create_time<'"+nowdate+"' group by lvl_4_branch_no  ",warning_code);
							for (Record record : c) {
								String count=record.getBigDecimal("count")+"";
								String org=record.getStr("lvl_4_branch_no");
								if(sjfw.equals(org)){
									StringBuffer cont = new StringBuffer();
									cont.append("【多棱镜】【"+warning_name+"】"+count+"笔,"+message_org+"。");	
									
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("sm_cont", cont.toString());
									map.put("sm_job", sm_job);
									String sm_key =warning_name+","+count+","+message_org;
									map.put("sm_key", sm_key);
									List<Record> lr = new ArrayList<Record>();
									lr.add(users9);
									SendMessageUtil.sendMsgList(lr, map);
									record_size+=lr.size();
								}
							}
						}
						status9=1;
					}
				}	
				
				if(jsf.contains("网点授权角色")){
					List<Record> users10s=Db.find(userSql10);
					if(!users10s.isEmpty()){
						for (Record users10 : users10s) {
							String sjfw=AppUtils.getOrgNumByUser(users10);
							String roleLevel=users10.getStr("ROLE_LEVEL");
							List<Record> c=Db.find("select count(*) count,lvl_4_branch_no from dop_warning_info dwi where warning_code=? and dwi.prompt_status='0'  " +
								"and  '"+olddate+"'< dwi.create_time and dwi.create_time<'"+nowdate+"' group by lvl_4_branch_no  ",warning_code);
							for (Record record : c) {
								String count=record.getBigDecimal("count")+"";
								String org=record.getStr("lvl_4_branch_no");
								if(sjfw.equals(org)){
									StringBuffer cont = new StringBuffer();
									cont.append("【多棱镜】【"+warning_name+"】"+count+"笔,"+message_org+"。");	
									
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("sm_cont", cont.toString());
									map.put("sm_job", sm_job);
									String sm_key =warning_name+","+count+","+message_org;
									map.put("sm_key", sm_key);
									List<Record> lr = new ArrayList<Record>();
									lr.add(users10);
									SendMessageUtil.sendMsgList(lr, map);
									record_size+=lr.size();
								}
							}
						}
						status10=1;
					}
				}
				
				if(jsf.contains("网点经办角色")){
					List<Record> users11s=Db.find(userSql11);
					if(!users11s.isEmpty()){
						for (Record users11 : users11s) {
							String sjfw=AppUtils.getOrgNumByUser(users11);
							String roleLevel=users11.getStr("ROLE_LEVEL");
							List<Record> c=Db.find("select count(*) count,lvl_4_branch_no from dop_warning_info dwi where warning_code=? and dwi.prompt_status='0'  " +
								"and  '"+olddate+"'< dwi.create_time and dwi.create_time<'"+nowdate+"' group by lvl_4_branch_no  ",warning_code);
							for (Record record : c) {
								String count=record.getBigDecimal("count")+"";
								String org=record.getStr("lvl_4_branch_no");
								if(sjfw.equals(org)){
									StringBuffer cont = new StringBuffer();
									cont.append("【多棱镜】【"+warning_name+"】"+count+"笔,"+message_org+"。");	
									
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("sm_cont", cont.toString());
									map.put("sm_job", sm_job);
									String sm_key =warning_name+","+count+","+message_org;
									map.put("sm_key", sm_key);
									List<Record> lr = new ArrayList<Record>();
									lr.add(users11);
									SendMessageUtil.sendMsgList(lr, map);
									record_size+=lr.size();
								}
							}
						}
						status11=1;
					}
				}
					
					if(jsf.contains("个人")){	
						StringBuffer cont = new StringBuffer();
						cont.append("【多棱镜】"+message_person+"。");	
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("sm_cont", cont.toString());
						map.put("sm_job", sm_job);
						
						String sm_key =message_person;
						map.put("sm_key", sm_key);
						
						String usql="select id,substr(teller_no,14) teller_no from dop_warning_info dwi " +
								"where dwi.warning_code=? and dwi.prompt_status='0'  " +
								"and  '"+olddate+"'< dwi.create_time and dwi.create_time<'"+nowdate+"'";
						List<Record> users12s=Db.find(usql,warning_code);
						
						for (Record users12 : users12s) {
							String teller_no=users12.getStr("teller_no");
							String id=users12.getStr("id");
							if(teller_no!=null){
								Record user12 = Db.findFirst(userSql12,teller_no);
								List<Record> lr = new ArrayList<Record>();
								lr.add(user12);
								SendMessageUtil.sendMsgList(lr, map);
								record_size++;				
								status12=1;
							}
							
							if((status0+status1+status2+status3+status4+status5+status6+status7+status8+status9+status10+status11+status12)>0){
								Db.update(" update dop_warning_info dwi set prompt_status='1' where prompt_status='0' " +
										" and dwi.warning_code=? " +
										" and dwi.id=? " +
										" and  '"+olddate+"'< dwi.create_time " +
										" and dwi.create_time<'"+nowdate+"'",warning_code,id);
							}else{
								Db.update(" update dop_warning_info dwi set prompt_status='2' where prompt_status='0'  " +
										" and dwi.warning_code=? " +
										" and dwi.id=? " +
										" and  '"+olddate+"'< dwi.create_time " +
										" and dwi.create_time<'"+nowdate+"'",warning_code,id);
							}		
						}
						
						
					}else{
						if((status0+status1+status2+status3+status4+status5+status6+status7+status8+status9+status10+status11)>0){
							Db.update(" update dop_warning_info dwi set prompt_status='1' where prompt_status='0' " +
									" and dwi.warning_code=? " +
									" and  '"+olddate+"'< dwi.create_time " +
									" and dwi.create_time<'"+nowdate+"'",warning_code);
						}else{
							Db.update(" update dop_warning_info dwi set prompt_status='2' where prompt_status='0'  " +
									" and dwi.warning_code=? " +
									" and  '"+olddate+"'< dwi.create_time " +
									" and dwi.create_time<'"+nowdate+"'",warning_code);
						}	
					}		
						
					
				
			}
			end_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			run_status=1;
			success();
		} catch (Exception e) {
			error();
			run_status=2;
			e.printStackTrace();
		}finally{
			Db.use("default").update("insert into PCCM_QUARTZ_RUN(id,START_TIME,END_TIME,RECORD_SIZE,INCFLG,RUN_STATUS)" +
					" values(?,?,?,?,?,?)",
					new Object[]{AppUtils.getStringSeq(),start_time,end_time,record_size,incflg,run_status});
		}
	}
}
