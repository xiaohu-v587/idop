package com.goodcol.controller.activiti;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.activiti.ActivitiUtil;
import com.goodcol.util.activiti.BeanToRecordUtil;
import com.goodcol.util.ext.anatation.RouteBind;




/**
 * 已办结流程Controller
 *0.查询自己审批过的流程
 *1.查询自己发起已结束审批流程
 *
 * @author changxy
 */
@RouteBind(path = "/haveDoneTask")
@Before( { ManagerPowerInterceptor.class })
public class HaveDoneTaskController extends BaseCtl {

    protected Logger logger =  Logger.getLogger(ActivitiController.class);
    @Override
	public void index() {
    	render("index.jsp");
	}
    
    /**
     * 查询已办结流程
     *0.查询自己审批过的流程
     *1.查询自己发起已结束审批流程 
     */
	public void list() {
    	Record r = null ;
        int pageNum = getParaToInt("pageIndex");
		int pageSize = getParaToInt("pageSize", 10);
		Date after_startTime = getParaToDate("after_startTime");//流程启动时间开始
		Date before_startTime = getParaToDate("before_startTime");//流程启动时间结束
		
		Date after_endTime = getParaToDate("after_endTime");//流程启动时间开始
		Date before_endTime = getParaToDate("before_endTime");//流程启动时间结束
		
		
		String  findType = getPara("findtype");//自己发起但已结束流程（与isThis必须2选一）
		String processdefKey = getPara("processdefKey");
		Record user = getCurrentUser();
		List<Record> rList = new ArrayList<Record>();
    	BeanToRecordUtil bru = BeanToRecordUtil.getInstance();
    	HistoryService historyService= ActivitiUtil.getHistoryService();
		HistoricProcessInstanceQuery dynamicQuery = historyService.createHistoricProcessInstanceQuery();
    	if(after_startTime!=null){
    		dynamicQuery.startedAfter(after_startTime);
    	}
    	if(before_startTime!=null){
    		dynamicQuery.startedBefore(before_startTime);
    	}
    	
    	if(after_endTime!=null){
    		dynamicQuery.finishedAfter(after_endTime);
    	}
    	if(before_endTime!=null){
    		dynamicQuery.finishedBefore(before_endTime);
    	}
    	
    	if(AppUtils.StringUtil(findType)==null){
    		findType = "0";
		}
    	if(AppUtils.StringUtil(processdefKey)!=null){
    		dynamicQuery.processDefinitionKey(processdefKey);
    	}
    	
    	if(findType.equals("1")){
    		dynamicQuery.startedBy(user.getStr("id"));
		}
    	
    	dynamicQuery.finished().orderByProcessInstanceEndTime().desc();
    	List<HistoricProcessInstance> list = dynamicQuery.listPage(pageNum, pageSize);
        for (HistoricProcessInstance obj : list) {
        	if(findType.equals("0")){
        		//查询根据proc_def_id 查询 ACT_HI_PROCINST表中是否有本人审批记录
        		long count =  historyService.createHistoricTaskInstanceQuery().taskAssignee(user.getStr("id")).processDefinitionId(obj.getProcessDefinitionId()).count();
        		//如果该流程存在登陆人员审批记录,截留数据，否则剔除
        		if(count>0){
        			r = bru.toRecord(obj);
        		}
        	}else{
        		r = bru.toRecord(obj);
        	}
        	if(r!=null)
        		rList.add(r);
   		}
        setAttr("data", rList);
        setAttr("total",dynamicQuery.count());
        renderJson();
    }
}