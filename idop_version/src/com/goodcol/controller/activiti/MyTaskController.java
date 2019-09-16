package com.goodcol.controller.activiti;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.activiti.ActivitiUtil;
import com.goodcol.util.activiti.BeanToRecordUtil;
import com.goodcol.util.activiti.ActivitiBpmnModel;
import com.goodcol.util.ext.anatation.RouteBind;




/**
 * 我的流程Controller
 * 查询自己发起但未结束流程
 *
 * @author changxy
 */
@RouteBind(path = "/myTask")
@Before( { ManagerPowerInterceptor.class })
public class MyTaskController extends BaseCtl {

    protected Logger logger =  Logger.getLogger(ActivitiController.class);
    @Override
	public void index() {
    	render("index.jsp");
	}
    
    /**
     * 任务流程列表
     *
     * @return
     */
    
    /**
     * task列表
     *
     * @param model
     * @return
     */
    public void list() {
    	Record user = getCurrentUser();
    	int pageNum = getParaToInt("pageIndex");
		int pageSize = getParaToInt("pageSize", 10);
    	Record r ;
    	
    	Date after_createTime = getParaToDate("after_createTime");//任务创建日期开始
		Date before_createTime = getParaToDate("before_createTime");//任务创建日期结束
		
		Date after_dueDate = getParaToDate("after_dueDate");//任务逾期日开始
		Date before_dueDate = getParaToDate("before_dueDate");//任务逾期日结束
		String description = getPara("description");
		String taskDefinitionKey = getPara("taskDefinitionKey");
		String id = getPara("id");
		String owner = getPara("owner");
		String name = getPara("name");
    	
		//数据存储集合
		List<Record> rList = new ArrayList<Record>();
    	BeanToRecordUtil bru = BeanToRecordUtil.getInstance();
    	TaskService taskService = ActivitiUtil.getTaskService();
    	TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateOrAssigned(user.getStr("id")).active();
    	if(taskQuery.count()>0){
    	if(after_createTime!=null){
    		taskQuery.taskCreatedAfter(after_createTime);
    	}
    	if(before_createTime!=null){
    		taskQuery.taskCreatedBefore(before_createTime);
    	}
    	if(after_dueDate!=null){
    		taskQuery.taskDueAfter(after_dueDate);
    	}
    	if(before_dueDate!=null){
    		taskQuery.taskDueBefore(before_dueDate);
    	}
    	if(AppUtils.StringUtil(name)!=null){
    		taskQuery.taskNameLike("%"+name+"%");
		}
    	if(AppUtils.StringUtil(name)!=null){
    		taskQuery.taskNameLike("%"+name+"%");
		}
    	if(AppUtils.StringUtil(owner)!=null){
    		taskQuery.taskOwnerLike("%"+owner+"%");
		}
    	if(AppUtils.StringUtil(id)!=null){
    		taskQuery.taskId(id);
    	}
    	if(AppUtils.StringUtil(taskDefinitionKey)!=null){
    		taskQuery.taskDefinitionKeyLike("%"+taskDefinitionKey+"%");
    	}
    	if(AppUtils.StringUtil(description)!=null){
    		taskQuery.taskDescriptionLike("%"+description+"%");
    	}
    	taskQuery.orderByTaskId().desc();
    	List<Task>  tasks = taskQuery.listPage(pageNum,pageSize);
    	
        for (Task task : tasks) {
        	 r = bru.toRecord(task);
        	 rList.add(r);
		}
        setAttr("data", rList);
        setAttr("total",taskQuery.count());
    	}else{
    		TaskQuery taskQuery2 = taskService.createTaskQuery().taskCandidateGroup(user.getStr("id")).active();
    		if(after_createTime!=null){
    			taskQuery2.taskCreatedAfter(after_createTime);
        	}
        	if(before_createTime!=null){
        		taskQuery2.taskCreatedBefore(before_createTime);
        	}
        	if(after_dueDate!=null){
        		taskQuery2.taskDueAfter(after_dueDate);
        	}
        	if(before_dueDate!=null){
        		taskQuery2.taskDueBefore(before_dueDate);
        	}
        	if(AppUtils.StringUtil(name)!=null){
        		taskQuery2.taskNameLike("%"+name+"%");
    		}
        	if(AppUtils.StringUtil(name)!=null){
        		taskQuery2.taskNameLike("%"+name+"%");
    		}
        	if(AppUtils.StringUtil(owner)!=null){
        		taskQuery2.taskOwnerLike("%"+owner+"%");
    		}
        	if(AppUtils.StringUtil(id)!=null){
        		taskQuery2.taskId(id);
        	}
        	if(AppUtils.StringUtil(taskDefinitionKey)!=null){
        		taskQuery2.taskDefinitionKeyLike("%"+taskDefinitionKey+"%");
        	}
        	if(AppUtils.StringUtil(description)!=null){
        		taskQuery2.taskDescriptionLike("%"+description+"%");
        	}
        	taskQuery2.orderByTaskId().desc();
        	List<Task>  tasks = taskQuery2.listPage(pageNum,pageSize);
        	
            for (Task task : tasks) {
            	 r = bru.toRecord(task);
            	 rList.add(r);
    		}
            setAttr("data", rList);
            setAttr("total",taskQuery2.count());
    	}
        
        renderJson();
    }
    
   
    /**
     * 签收任务
     */
    public void claim() {
    	String taskId = getPara("taskid");
    	Record user = getCurrentUser();
    	ActivitiUtil.claimTaskByAssignee(taskId, user.getStr("id"));
    	renderSuccessJsonMsg("任务已签收");
    }

    
    /**
     * 运行中的流程实例
     */
    public void running() {
    	int pageNum = getParaToInt("pageIndex");
		int pageSize = getParaToInt("pageSize", 10);
		Record r ;
		List<Record> rList = new ArrayList<Record>();
    	BeanToRecordUtil bru = BeanToRecordUtil.getInstance();
    	ProcessInstanceQuery dynamicQuery = ActivitiUtil.getProcessInstanceQuery().orderByProcessInstanceId().desc().active();
        List<ProcessInstance> list = dynamicQuery.listPage(pageNum,pageSize);
        for (ProcessInstance obj : list) {
       	 	r = bru.toRecord(obj);
       	 	rList.add(r);
		}
       setAttr("data", rList);
       setAttr("total",dynamicQuery.count());
       renderJson();
    }
    
    
    
    /**
     * 已结束的流程实例
     *
     * @return
     */
    public void finished() {
        int pageNum = getParaToInt("pageIndex");
		int pageSize = getParaToInt("pageSize", 10);
		Record r ;
		List<Record> rList = new ArrayList<Record>();
    	BeanToRecordUtil bru = BeanToRecordUtil.getInstance();
     	HistoricProcessInstanceQuery dynamicQuery = ActivitiUtil.getHistoricProcessInstanceQuery().finished().orderByProcessInstanceEndTime().desc();
        List<HistoricProcessInstance> list = dynamicQuery.listPage(pageNum, pageSize);
        for (HistoricProcessInstance obj : list) {
          	 r = bru.toRecord(obj);
          	 rList.add(r);
   		}
        setAttr("data", rList);
        setAttr("total",dynamicQuery.count());
        renderJson();
    }
    
    public void updateState(){
    	String id=getPara("id");
    	TaskService taskService = ActivitiUtil.getTaskService();
    	       //根据任务id完成该任务
    	taskService.complete(id);
    }
    
      public void form(){
    	
    	render("form.jsp");
    }
    
      public void spyj(){
    	  String sfty=getPara("sfty");
    	  String yj=getPara("yj");
    	  String taskId=getPara("taskid");
    	  String processInstanceId=getPara("processinstanceid");
    	  Record user = getCurrentUser();
    	  TaskService taskService =ActivitiUtil.getTaskService();
    	  //添加领导审批意见
    	  if(yj!=null){
    		  taskService.addComment(taskId, null, yj);
    	  }
    	  Authentication.setAuthenticatedUserId(user.getStr("id"));
    	  //签收
    	  ActivitiUtil.claimTaskByAssignee(taskId, user.getStr("id"));
    	 if(sfty.equals("0")){
    		 //如果领导同意，进入下一节点
    		 Map<String, Object> variables = new HashMap<String, Object>();  
            // variables.put("yj", 0);  
 		     taskService.complete(taskId,variables);
    	 }else{
    		 //如果领导不同意
    			Map<String, Object> variables = new HashMap<String, Object>();  
              //  variables.put("yj", 1); 
    		    taskService.complete(taskId,variables);
    	 }
    	 renderNull();
      }
    
      
    
}