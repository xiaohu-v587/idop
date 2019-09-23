package com.goodcol.config;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.alibaba.druid.filter.stat.StatFilter;
import com.goodcol.controller.CustPuniCtl;
import com.goodcol.controller.DjbHandlerCtr;
import com.goodcol.controller.ExportSearchCtl;
import com.goodcol.controller.HomeCtl;
import com.goodcol.controller.KpiCtl;
import com.goodcol.controller.LogCtl;
import com.goodcol.controller.MainCtl;
import com.goodcol.controller.MenuCtl;
import com.goodcol.controller.MissionConfigCtl;
import com.goodcol.controller.MissionForUserCtl;
import com.goodcol.controller.MissionDataReportCtl;
import com.goodcol.controller.MissionFileReportCtl;
import com.goodcol.controller.MissionViewCtl;
import com.goodcol.controller.MissionViewForUserCtl;
import com.goodcol.controller.OrgCtl;
import com.goodcol.controller.ParamCtl;
import com.goodcol.controller.PositionCtl;
import com.goodcol.controller.RoleCtl;
import com.goodcol.controller.RolePerMissionCtl;
import com.goodcol.controller.Rolemutual;
import com.goodcol.controller.ScheduleCtl;
import com.goodcol.controller.ScheduletaskCtl;
import com.goodcol.controller.SearchCheckCtl;
import com.goodcol.controller.SearchCheckRecallCtl;
import com.goodcol.controller.SearchCheckSearchCtl;
import com.goodcol.controller.TDjbJcxxCtrler;
import com.goodcol.controller.TelBookCtl;
import com.goodcol.controller.TestCtl;
import com.goodcol.controller.UserCtl;
import com.goodcol.controller.djbApproveCtl;
import com.goodcol.controller.activiti.ActivitiController;
import com.goodcol.controller.activiti.HaveDoneTaskController;
import com.goodcol.controller.activiti.LeaveController;
import com.goodcol.controller.activiti.ModelController;
import com.goodcol.controller.activiti.MyTaskController;
import com.goodcol.controller.activiti.PendingTaskController;
import com.goodcol.controller.activiti.ProcessInstanceController;
import com.goodcol.controller.dop.AlgorithmCtl;
import com.goodcol.controller.dop.AssignedFollowCtl;
import com.goodcol.controller.dop.BiImportCtl;
import com.goodcol.controller.dop.BizMonitorCtl;
import com.goodcol.controller.dop.CommonCtl;
import com.goodcol.controller.dop.ComplexQuery;
import com.goodcol.controller.dop.ComplexQueryPerson;
import com.goodcol.controller.dop.CpcCtl;
import com.goodcol.controller.dop.DatamartCtl;
import com.goodcol.controller.dop.ExpertAnalysisCtl;
import com.goodcol.controller.dop.ExpertCtl;
import com.goodcol.controller.dop.ExpertReportCtl;
import com.goodcol.controller.dop.GangedMenuCtl;
import com.goodcol.controller.dop.ImgSetCfgCtl;
import com.goodcol.controller.dop.ImportantBbView;
import com.goodcol.controller.dop.ModelViewCtl;
import com.goodcol.controller.dop.MyFocusCtl;
import com.goodcol.controller.dop.MyFoucsIndexCtl;
import com.goodcol.controller.dop.OpPortraitCtl;
import com.goodcol.controller.dop.PanoramaViewCtl;
import com.goodcol.controller.dop.PersonMonitorCtl;
import com.goodcol.controller.dop.PersonQuotaDataExhibitCtl;
import com.goodcol.controller.dop.PromptingSearchCtl;
import com.goodcol.controller.dop.QuotaDataExhibitCtl;
import com.goodcol.controller.dop.WarningApproCtl;
import com.goodcol.controller.dop.WarningCountCtl;
import com.goodcol.controller.dop.WarningCtl;
import com.goodcol.controller.dop.WarningManageCtl;
import com.goodcol.controller.dop.WarningModelCtl;
import com.goodcol.controller.dop.WarningSearchCtl;
import com.goodcol.controller.dop.WarningThresholdCtl;
import com.goodcol.controller.dop.addAtticepointFocusCtl;
import com.goodcol.controller.zxglctl.CustAppealCtl;
import com.goodcol.controller.zxglctl.CustClaimController;
import com.goodcol.controller.zxglctl.CustDistrCtl;
import com.goodcol.controller.zxglctl.CustMarketingController;
import com.goodcol.controller.zxglctl.IndexController;
import com.goodcol.controller.zxglctl.MyCustInfosController;
import com.goodcol.controller.zxglctl.ParamController;
import com.goodcol.controller.zxglctl.PccmAccExcavateCtl;
import com.goodcol.controller.zxglctl.PccmAccSearchCtl;
import com.goodcol.controller.zxglctl.PccmCustCntCtl;
import com.goodcol.controller.zxglctl.PccmCustomSearchController;
import com.goodcol.controller.zxglctl.PccmDownTaskCtl;
import com.goodcol.controller.zxglctl.PccmFinanceCtl;
import com.goodcol.controller.zxglctl.PccmKpIParaCtl;
import com.goodcol.controller.zxglctl.PccmKpIRunCtl;
import com.goodcol.controller.zxglctl.PccmLabelSendCtl;
import com.goodcol.controller.zxglctl.PccmManualReportCtl;
import com.goodcol.controller.zxglctl.PccmNoticeCtl;
import com.goodcol.controller.zxglctl.PccmStandardCtl;
import com.goodcol.controller.zxglctl.RoleController;
import com.goodcol.controller.zxglctl.TaskController;
import com.goodcol.controller.zxglctl.UserController;
import com.goodcol.controller.zxglctl.ValidCustController;
import com.goodcol.controller.zxglctl.handDataImpCtl;
import com.goodcol.controller.zxglctl.pccmMyAppealCtl;
import com.goodcol.core.config.Constants;
import com.goodcol.core.config.GCConfig;
import com.goodcol.core.config.Handlers;
import com.goodcol.core.config.Interceptors;
import com.goodcol.core.config.Plugins;
import com.goodcol.core.config.Routes;
import com.goodcol.core.plugin.activerecord.ActiveRecordPlugin;
import com.goodcol.core.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.goodcol.core.plugin.activerecord.dialect.OracleDialect;
import com.goodcol.core.plugin.druid.DruidPlugin;
import com.goodcol.core.render.ViewType;
import com.goodcol.model._MappingKit;
import com.goodcol.util.PropertiesContent;
import com.goodcol.util.activiti.ActivitiPlugins;
import com.goodcol.util.ext.plugin.quartz.QuartzPlugin;
import com.goodcol.util.ext.plugin.quartz.QuartzSchedule;
import com.goodcol.util.ext.plugin.sqlXml.SqlInXmlPlugin;
import com.goodgol.attpointmode.MyComBusNetCtl;
import com.goodgol.attpointmode.attpointmode;

/**
 * API引导式配置
 */
public class OAConfig extends GCConfig {
	
	

	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		me.setDevMode(true);
		me.setFreeMarkerTemplateUpdateDelay(0);
		me.setViewType(ViewType.JSP);
		me.setError404View("/404.html");
		me.setError500View("/500.html");
		me.setBaseViewPath("/pages/");
	}

	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {

		// MyRoutesUtil.add(me);
		me.add("/", MainCtl.class, "/login");
		me.add("/home", HomeCtl.class);
		me.add("/common", CommonCtl.class);
		me.add("/test", TestCtl.class);
		me.add("/position", PositionCtl.class);
		me.add("/log", LogCtl.class);
		me.add("/menu", MenuCtl.class);
		me.add("/org", OrgCtl.class);
		me.add("/param", ParamCtl.class);
		me.add("/role", RoleCtl.class);
		me.add("/permission", RolePerMissionCtl.class);
		me.add("/user", UserCtl.class);
		me.add("/schedule", ScheduleCtl.class);
		me.add("/scheduletask", ScheduletaskCtl.class);
		/*-----------------工作流配置----------------------*/
		me.add("/workflow", ActivitiController.class);
		me.add("/model", ModelController.class);
		me.add("/processinstance", ProcessInstanceController.class);
		me.add("/haveDoneTask", HaveDoneTaskController.class);
		me.add("/pendingTask", PendingTaskController.class);
		me.add("/myTask", MyTaskController.class);
		me.add("/leave", LeaveController.class);

		// 质效管理系统添加控制层
		me.add("/zxuser", UserController.class);
		me.add("/zxparam", ParamController.class);
		me.add("/pccm_kpi_param", PccmKpIParaCtl.class);
		me.add("/pccm_kpi_run", PccmKpIRunCtl.class);

		me.add("/custPuni", CustPuniCtl.class);
		me.add("/telBook", TelBookCtl.class);
		me.add("/pccm_notice", PccmNoticeCtl.class);
		me.add("/kpi", KpiCtl.class);

		me.add("/zxCustClaim", CustClaimController.class);
		me.add("/zxMyCust", MyCustInfosController.class);
		me.add("/zxCustAppeal", CustAppealCtl.class);
		me.add("/zxCustDistr", CustDistrCtl.class);

		me.add("/pccm_finance", PccmFinanceCtl.class);
		me.add("/pccm_acc_search", PccmAccSearchCtl.class);
		me.add("/pccm_acc_excavate", PccmAccExcavateCtl.class);
		me.add("/pccm_standard", PccmStandardCtl.class);
		me.add("/pccm_label_send", PccmLabelSendCtl.class);
		me.add("/handDataImp", handDataImpCtl.class);
		me.add("/zxrole", RoleController.class);
		me.add("/zxtask", TaskController.class);
		me.add("/zxindex", IndexController.class);
		me.add("/zxMarket", CustMarketingController.class);
		me.add("/zxMyAppeal", pccmMyAppealCtl.class);
		me.add("/zxCustCnt", PccmCustCntCtl.class);
		me.add("/validCust", ValidCustController.class);
		me.add("/zxDownTask", PccmDownTaskCtl.class);
		me.add("/manualReport", PccmManualReportCtl.class);
		
		//预警信号
		me.add("/warning", WarningCtl.class);
		me.add("/warning_search", WarningSearchCtl.class);
		me.add("/prompting_search", PromptingSearchCtl.class);
		me.add("/warning_manage",WarningManageCtl.class);
		me.add("/warning_approv", WarningApproCtl.class);
		
		/*-----------------定制报表配置----------------------*/
		me.add("/zxCustomSearch", PccmCustomSearchController.class);
		
		//联动数据管理
		me.add("/gangedmenu", GangedMenuCtl.class);
		//算法配置
		me.add("/algorith", AlgorithmCtl.class);
		//预警阀值
		me.add("/warningthreshold", WarningThresholdCtl.class);
		//我的机构-全景运营视图
		me.add("/panorama", PanoramaViewCtl.class);
		//我的机构-模块全景视图
		me.add("/modelview", ModelViewCtl.class);
		//我的条线-模块全景视图
		me.add("/modelviews", ModelViewCtl.class, "/modelview");
		//关键指标数据展现
		me.add("quotaDataExhibit",QuotaDataExhibitCtl.class);
		//关键指标数据展现
		me.add("quotaDataExhibits",QuotaDataExhibitCtl.class, "/quotaDataExhibit");
		//数据集市
		me.add("datamart",DatamartCtl.class);
		//我的关注
		me.add("myfocus",MyFocusCtl.class);
		//重点报表展示
		me.add("/importantBbView",ImportantBbView.class);
		//组合查询ComplexQuery
		me.add("/complexQuery",ComplexQuery.class);
		//组合查询-人员维度ComplexQueryPerson
		me.add("/complexQueryPerson",ComplexQueryPerson.class);
		//预警模型管理
		me.add("warningmodel",WarningModelCtl.class);
     
		//网点首页
		me.add("/atticepointmode", attpointmode.class);
		//常用业务网址
		me.add("/mycombusnet", MyComBusNetCtl.class);
		
		//预警模型管理
		me.add("warningcount",WarningCountCtl.class);
        

		//我的条线中我的关注
		me.add("myfocus_index",MyFoucsIndexCtl.class);

       //网点中我的关注
		me.add("atticepointfocus",addAtticepointFocusCtl.class);

		//人员维度指标数据展现
		me.add("/personQuotaDataExhibit",PersonQuotaDataExhibitCtl.class);

		
		me.add("/biimport",BiImportCtl.class);
		//专家分析页面
		me.add("/expertAnalysis",ExpertAnalysisCtl.class);
		//专家报告页面
		me.add("/expertReport",ExpertReportCtl.class);
		
		 //数据表下载
		me.add("exportSearch",ExportSearchCtl.class);  
		
		me.add("/djb", TDjbJcxxCtrler.class);//登记簿配置
		me.add("/djbHandler", DjbHandlerCtr.class);//登记簿查看
		me.add("/djbapprove", djbApproveCtl.class);//登记簿审批界面

       //查询查复  searchCheck
		me.add("searchCheck",SearchCheckCtl.class); 
		   //查询查复回复  searchCheck
	   me.add("searchCheckRecall",SearchCheckRecallCtl.class); 
	   	//查询查复查询
	   	me.add("searchCheckSearch", SearchCheckSearchCtl.class);
	   	//组合参数配置
	   	me.add("/cpc", CpcCtl.class);
	   	//专家分析报表参数配置
	   	me.add("/expert", ExpertCtl.class);
	   	//运营画像
	   	me.add("/opPortrait",OpPortraitCtl.class);
	   	//图谱要素配置
	   	me.add("/imgSetCfg", ImgSetCfgCtl.class);
	   	//组合监测
	   	me.add("/orgMonitor",com.goodcol.controller.dop.OrgMonitorCtl.class);//机构监测
	   	me.add("/personMonitor",PersonMonitorCtl.class);//人员监测
	   	me.add("/bizMonitor",BizMonitorCtl.class);//业务监测
	   	
	   	//指定关注配置
	   	me.add("/assignedFollow", AssignedFollowCtl.class);
	   	//我发起的任务
	   	me.add("/missionView",MissionViewCtl.class); 
	    //任务配置
	   	
	   	me.add("/missionconfig",MissionConfigCtl.class);
	    //待处理任务
	   	me.add("/missionForUser",MissionForUserCtl.class);
	    //已处理任务
	   	me.add("/missionViewForUser",MissionViewForUserCtl.class);
	   	
	   	me.add("/missiondatareport",MissionDataReportCtl.class);
	   	
	   	me.add("/missionFileReport",MissionFileReportCtl.class);
	   	
	   	me.add("/rolemutual", Rolemutual.class);
		
		System.out.println("服务器更改！");
		System.out.println("服务器更改！");
		System.out.println("服务器更改！");
		System.out.println("服务器更改！");
		System.out.println("服务器更改！");
		System.out.println("服务器更改！");
		System.out.println("服务器更改！");
		System.out.println("服务器更改！");
		System.out.println("服务器更改！");
		System.out.println("服务器更改！");
		
	   	
	}
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {

		
      		Boolean open_jndi =   PropertiesContent.getToBool("OPEN_JNDI_DICT",false);
		
		if(open_jndi){
			DataSource dataSource = null;
			try {
				dataSource = (DataSource)new  InitialContext().lookup(PropertiesContent.get("JNDI_ORACLE_DEFAULT"));
			} catch (NamingException e) {
				e.printStackTrace();
			}

			//ActiveRecordPlugin arp = new ActiveRecordPlugin("default", druidPlugin);
			ActiveRecordPlugin arp = new ActiveRecordPlugin("default", dataSource);
			arp.setShowSql(true);
			arp.setDialect(new OracleDialect());
			arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));
			me.add(arp);

			me.add(new SqlInXmlPlugin());
			me.add(new QuartzPlugin());
		
			_MappingKit.mapping(arp);
			
			/** 配置gbase数据源 */
//			DataSource gbaseDataSource = null;
//			try {
//				gbaseDataSource = (DataSource)new  InitialContext().lookup(PropertiesContent.get("JNDI_GBASE_DEFAULT"));
//			} catch (NamingException e) {
//				e.printStackTrace();
//			}
//			ActiveRecordPlugin arpGbase = new ActiveRecordPlugin("gbase", gbaseDataSource);
//			arpGbase.setShowSql(true);
//			arpGbase.setDialect(new MysqlDialect());
//			me.add(arpGbase);
			
		}else{DruidPlugin druidPlugin = new DruidPlugin(
				PropertiesContent.get("jdbc.url"),
				PropertiesContent.get("jdbc.username"),
				PropertiesContent.get("jdbc.password"),
				PropertiesContent.get("jdbc.driverClassName"));
		druidPlugin.addFilter(new StatFilter());
		druidPlugin.setValidationQuery("select 1 FROM DUAL");
		//连接进入空闲状态时，是否经过空闲对象驱逐进程的校验
		druidPlugin.setTestWhileIdle(true);
		
		/*
		//空闲对象驱逐进程由运行状态进入休眠状态的时长，值为正时表示不运行任何空闲对象驱逐进程
		druidPlugin.setRemoveAbandonedTimeoutMillis(300 * 1000);
		//连接被空闲对象驱逐进程驱逐前在池中保持空闲状态的最小时间
		druidPlugin.setMinEvictableIdleTimeMillis(300 * 1000);
		*/
		//druidPlugin.setTimeBetweenEvictionRunsMillis(9000);
		//连接被调用时是否需要校验 ，会降低性能
		druidPlugin.setTestOnBorrow(true);
		//是否清除已超过RemoveAbandonedTimeoutMillis所设置时间的无效连接
		druidPlugin.setRemoveAbandoned(true);
		druidPlugin.setRemoveAbandonedTimeoutMillis(300*1000);
		//当清除无效连接时是否在日志中记录
		druidPlugin.setLogAbandoned(false);
		
		//druidPlugin.setMinEvictableIdleTimeMillis(3*60*1000);
		me.add(druidPlugin);   

		ActiveRecordPlugin arp = new ActiveRecordPlugin("default", druidPlugin);
		arp.setShowSql(true);
		arp.setDialect(new OracleDialect());
		arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));
		me.add(arp);

		me.add(new SqlInXmlPlugin());
		me.add(new QuartzPlugin());
		me.add(new ActivitiPlugins(druidPlugin));// 使用默认数据源
		_MappingKit.mapping(arp);
		
		/** 配置gbase数据源 */
//		C3p0Plugin c3p0Plugin = new C3p0Plugin(
//				PropertiesContent.get("gbase.url"),
//				PropertiesContent.get("gbase.username"),
//				PropertiesContent.get("gbase.password"),
//				PropertiesContent.get("gbase.driverClassName"));
//		
//		
//		me.add(c3p0Plugin);
//		
//	
//		ActiveRecordPlugin arpGbase = new ActiveRecordPlugin("gbase", c3p0Plugin);
		
//		arpGbase.setShowSql(true);
//		arpGbase.setDialect(new MysqlDialect());
//		me.add(arpGbase);
		}
		
		   
		/** 初始化定时任务 */
		me.add(new QuartzSchedule());
	}
	
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
	}

	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
	}
}
