package com.goodcol.util.activiti;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.ProcessDiagramGenerator;

import com.goodcol.util.PropertiesContent;
import com.goodcol.util.activiti.command.JumpTaskCmd;

public class ActivitiUtil {
	private static ProcessEngine processEngine = null;
	
	static {
		processEngine = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
				.setDatabaseSchemaUpdate("false") // 很重要，不能被修改
				.setMailServerHost(PropertiesContent.get("activitvi.mailServerHost"))
				.setMailServerPort(Integer.valueOf(PropertiesContent.get("activitvi.mailServerPort")))
				.setMailServerUsername(PropertiesContent.get("activitvi.mailServerUsername"))
				.setMailServerUseSSL(true)
				.setMailServerPassword(PropertiesContent.get("activitvi.mailServerPassword"))
				.setJdbcUrl(PropertiesContent.get("jdbc.url")).setJdbcPassword(PropertiesContent.get("jdbc.username"))
				.setJdbcUsername(PropertiesContent.get("jdbc.password"))
				.setJdbcDriver(PropertiesContent.get("jdbc.driverClassName"))
				.buildProcessEngine();
	}
	
	/**
	 * 获取工作流引擎
	 * @return ProcessEngine 工作流引擎对象
	 */
	public static ProcessEngine getProcessEngine() {
		return processEngine;
	}
	
	
	
	
	/**
	 * 发布流程 用于将画好的流程存入数据库，
	 */
	public static void deploy(String bpmFile) {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.createDeployment().addClasspathResource(bpmFile).deploy();
	}
	
	
	/**
	 * 删除流程
     * 能级联的删除
     * 能删除启动的流程，会删除和当前规则相关的所有信息，正在执行的信息，也包括历史信息
     */
	public static void deleteWFByManager(String deploymentId) {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.deleteDeployment(deploymentId,true);
	}
	
	/**
	 * 删除流程
     * 不带级联的删除
     * 只能删除没有启动的流程，如果流程启动，就会抛出异常
     */
	public static void deleteWF(String deploymentId) {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.deleteDeployment(deploymentId);
	}
    /**
     * 创建流程实例
     */ 
    public static ProcessInstance startInstanceByKey(String instanceId) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(instanceId);
        return instance;
    } 
    /**
     * 挂起某个流程实例
     */ 
    public static void supseInstanceByKey(String instanceId) {
        processEngine.getRuntimeService().suspendProcessInstanceById(instanceId);
    }
    /**
     * 删除某个流程实例
     */ 
    public static void delInstanceByKey(String instanceId) {
        processEngine.getRuntimeService().deleteProcessInstance(instanceId,"");
    } 
    
    
    
    /**
     * 查询流程实例状态
     */ 
    public static String queryInstanceStatusByKey(String instanceId) {
    	ProcessInstance pi = processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        String result = "";
    	if( pi != null ){
        	result = "当前节点:" +pi.getActivityId();
        }else{
        	result = "流程已结束";
        }
    	return result;
    }
    
    
    /**
     * 获取流程当前节点信息
     */
    public static ActivityImpl getProcessMap(String procDefId,String executionId) throws Exception {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
        ProcessDefinitionImpl pdImpl = (ProcessDefinitionImpl) processDefinition;
        String processDefinitionId = pdImpl.getId();
        ProcessDefinitionEntity def = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(processDefinitionId);
        ActivityImpl actImpl = null;
        
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ExecutionEntity execution = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(executionId).singleResult();
        // 执行实例
        String activitiId = execution.getActivityId();// 当前实例的执行到哪个节点
        List<ActivityImpl> activitiList = def.getActivities();// 获得当前任务的所有节点
        for (ActivityImpl activityImpl : activitiList) {
            String id = activityImpl.getId();
            if (id.equals(activitiId)) {// 获得执行到那个节点
                actImpl = activityImpl;
                break;
            }
        }
        return actImpl;
    }
    
    /**
     * 获取流程图并 显示
     */
    public static InputStream findProcessPic(String procDefId) throws Exception {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
        String diagramResourceName = procDef.getDiagramResourceName();
        InputStream in = repositoryService.getResourceAsStream(procDef.getDeploymentId(), diagramResourceName);
        return in;
    }
    
    /**
     * 根据执行人查询任务
     */ 
    public static List<Task> findTaskByAssignee(String assignee) {
        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().taskAssignee(assignee).list();
        return taskList;
    }
    
    /**
     * 认领任务
     */ 
    public static void claimTaskByAssignee(String taskid , String assignee) {
        TaskService taskService = processEngine.getTaskService();
        taskService.claim(taskid , assignee);   
    }
    
    /**
     * 执行任务
     */ 
	public static void takeCompleteTask(String taskid) {
		TaskService taskService = processEngine.getTaskService();
		taskService.complete(taskid);
	}
    
	/**
	 * 提交任务, 并保存意见
	 * @param taskId 任务ID
	 * @param procInsId 流程实例ID，如果为空，则不保存任务提交意见
	 * @param comment 任务提交意见的内容
	 * @param title			流程标题，显示在待办任务标题
	 * @param vars 任务变量
	 */
	public void complete(String taskId, String procInsId, String comment, String title, Map<String, Object> vars){
		addTaskComment(taskId, procInsId, comment);
		if (vars == null){
			vars = new HashMap<String, Object>();
		}
		if( title != null && !!title.equals(""))
			vars.put("title", title);
		// 提交任务,含变量
		processEngine.getTaskService().complete(taskId, vars);
	}
	/**
     * 查询所有任务 
     */ 
    public static List<Task> findAllTask() {
        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().list();
        return taskList;
    }
	/**
	 * 添加任务意见
	 */
	public void addTaskComment(String taskId, String procInsId, String comment){
		processEngine.getTaskService().addComment(taskId, procInsId, comment);
	}
	
	
	/**
	 * 获取批注内容
	 * @param taskId
	 * @return
	 */
	public List<Comment> getProcessComments(String taskId) {
		TaskService taskService = getProcessEngine().getTaskService();
		HistoryService historyService= getProcessEngine().getHistoryService();
		RuntimeService runtimeService=getProcessEngine().getRuntimeService();
        List<Comment> historyCommnets = new ArrayList<>();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessInstance pi =runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        List<HistoricActivityInstance> hais = historyService.createHistoricActivityInstanceQuery().processInstanceId(pi.getId()).activityType("userTask").list();
        for (HistoricActivityInstance hai : hais) {
            String historytaskId = hai.getTaskId();
            List<Comment> comments = taskService.getTaskComments(historytaskId);
            if(comments!=null && comments.size()>0){
                historyCommnets.addAll(comments);
            }
        }
         return historyCommnets;
    }
	
    
    /**
	 * 任务后退一步
	 */
	public static void taskBack(String procInsId, Map<String, Object> variables) {
		taskBack(getCurrentTask(procInsId), variables);
	}
	/**
	 * 任务后退至指定活动
	 */
	public static void taskBack(TaskEntity currentTaskEntity, Map<String, Object> variables) {
		ActivityImpl activity = (ActivityImpl)getActivity(processEngine, currentTaskEntity.getProcessDefinitionId(), currentTaskEntity.getTaskDefinitionKey()).getIncomingTransitions().get(0).getSource();
		jumpTask(currentTaskEntity, activity, variables);
	}
	
	/**
	 * 设置任务组
	 * @param vars
	 * @param candidateGroupIdExpressions
	 */
	public static void setTaskGroup(Map<String, Object> vars, Set<Expression> candidateGroupIdExpressions) {
		String roles = "";
		for (Expression expression : candidateGroupIdExpressions) {
			String expressionText = expression.getExpressionText();
			String roleName = processEngine.getIdentityService().createGroupQuery().groupId(expressionText).singleResult().getName();
			roles += roleName;
		}
		vars.put("任务所属角色", roles);
	}

	/**
	 * 设置当前处理人信息
	 * @param vars
	 * @param currentTask
	 */
	public static void setCurrentTaskAssignee(Map<String, Object> vars, Task currentTask) {
		String assignee = currentTask.getAssignee();
		if (assignee != null) {
			org.activiti.engine.identity.User assigneeUser = processEngine.getIdentityService().createUserQuery().userId(assignee).singleResult();
			String userInfo = assigneeUser.getFirstName() + " " + assigneeUser.getLastName();
			vars.put("当前处理人", userInfo);
		}
	}
    
	/**
	 * 读取带跟踪的图片
	 * @param 执行 环节ID
	 * @return	 
	 */
	public static InputStream tracePhoto(String processDefinitionId, String executionId) {
		BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(processDefinitionId);
		
		List<String> activeActivityIds = new ArrayList<String>();
		if (processEngine.getRuntimeService().createExecutionQuery().executionId(executionId).count() > 0){
			activeActivityIds = processEngine.getRuntimeService().getActiveActivityIds(executionId);
		}
		
		ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl)ProcessEngines.getDefaultProcessEngine();
		Context.setProcessEngineConfiguration(defaultProcessEngine.getProcessEngineConfiguration());
		ProcessEngineConfiguration conf=Context.getProcessEngineConfiguration();
		ProcessDiagramGenerator pdg = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
		/**
		 * 检查到默认配置中将字体置为空，使用复杂配置方式手动强制传入字体
		 */
		return pdg.generateDiagram(bpmnModel, "png", activeActivityIds,new ArrayList<String>(),conf.getActivityFontName(),conf.getLabelFontName(),conf.getAnnotationFontName(),null,1.0D);
	}
	
	public static TaskEntity getTaskEntity(String taskId) {
		return (TaskEntity) processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
	}
	
	private static TaskEntity getCurrentTask(String procInsId) {
		return (TaskEntity) processEngine.getTaskService().createTaskQuery().processInstanceId(procInsId).active().singleResult();
	}
	/**
	 * 跳转（包括回退和向前）至指定活动节点
	 * @param currentTaskEntity 当前任务节点
	 * @param targetActivity 目标任务节点（在模型定义里面的节点名称）
	 * @throws Exception
	 */
	public static void jumpTask(TaskEntity currentTaskEntity, ActivityImpl targetActivity, Map<String, Object> variables) {
		CommandExecutor commandExecutor = ((RuntimeServiceImpl) processEngine.getRuntimeService()).getCommandExecutor();
		commandExecutor.execute(new JumpTaskCmd(currentTaskEntity, targetActivity, variables));
	}
	
	public static ActivityImpl getActivity(ProcessEngine processEngine, String processDefId, String activityId) {
		ProcessDefinitionEntity pde = getProcessDefinition(processEngine, processDefId);
		return (ActivityImpl) pde.findActivity(activityId);
	}

	private static ProcessDefinitionEntity getProcessDefinition(ProcessEngine processEngine, String processDefId) {
		return (ProcessDefinitionEntity) ((RepositoryServiceImpl) processEngine.getRepositoryService()).getDeployedProcessDefinition(processDefId);
	}
	
	/**
	 * 
	 * @return
	 */
	public static HistoryService getHistoryService(){
		HistoryService historyService= getProcessEngine().getHistoryService();
		return historyService;
	}
	
	
	/**
	 * 获取HistoricProcessInstanceQuery
	 * @return
	 */
	public static HistoricProcessInstanceQuery getHistoricProcessInstanceQuery(){
		HistoryService historyService= getProcessEngine().getHistoryService();
		HistoricProcessInstanceQuery dynamicQuery = historyService.createHistoricProcessInstanceQuery();
		return dynamicQuery;
	}
	
	
	/**
	 * 获取ProcessInstance，启动startProcessInstanceByKey流程
	 * @return
	 */
	public static ProcessInstance getProcessInstance(String processDefinitionKey,String businessKey){
		ProcessInstance processInstance=getProcessEngine().getRuntimeService().startProcessInstanceByKey(processDefinitionKey,businessKey);
	    return processInstance;
	}
	
	/**
	 * 获取ProcessInstance，启动startProcessInstanceByKey流程
	 * @return
	 */
	public static ProcessInstance getProcessInstanceWithMap(String processDefinitionKey,String businessKey,Map<String,Object> variables){
		ProcessInstance processInstance=getProcessEngine().getRuntimeService().startProcessInstanceByKey(processDefinitionKey,businessKey,variables);
	    return processInstance;
	}
	
	
	/**
	 * 设置当前线程setAuthenticatedUserId
	 * @return
	 */
	public static void authenticatedUserId(String id){
		getProcessEngine().getIdentityService().setAuthenticatedUserId(id);
	}
	
	/**
	 * 获取ProcessInstanceQuery
	 * @return
	 */
	public static ProcessInstanceQuery getProcessInstanceQuery(){
		ProcessInstanceQuery query =getProcessEngine().getRuntimeService().createProcessInstanceQuery();
	    return query;
	}
	
	
	/**
	 * 获取TaskService
	 * @return
	 */
	public static TaskService getTaskService(){
		TaskService taskService = getProcessEngine().getTaskService();
		return taskService;
	}
	
	/**
	 * 获取TaskQuery
	 * @return
	 */
	public static TaskQuery getTaskQuery(){
		TaskQuery taskQuery = ActivitiUtil.getProcessEngine().getTaskService().createTaskQuery();
		return taskQuery;
	}
	
	/**
	 * 激活流程
	 */
	public static void activateProcessInstanceById(String processInstanceId){
		ActivitiUtil.getProcessEngine().getRuntimeService().activateProcessInstanceById(processInstanceId);
	}
	
	/**
	 * 挂起流程
	 */
	public static void suspendProcessInstanceById(String processInstanceId){
		ActivitiUtil.getProcessEngine().getRuntimeService().suspendProcessInstanceById(processInstanceId);
	}
	
	
}
