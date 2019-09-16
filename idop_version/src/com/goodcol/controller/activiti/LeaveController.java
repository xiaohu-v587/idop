package com.goodcol.controller.activiti;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.engine.test.Deployment;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.model.OaLeave;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.activiti.ActivitiUtil;
import com.goodcol.util.activiti.BeanToRecordUtil;
import com.goodcol.util.ext.anatation.RouteBind;

/**
 * 请假控制器，包含保存、启动流程，获取批注
 *
 * @author HenryYan
 */
@RouteBind(path = "/leave")
@Before( { ManagerPowerInterceptor.class })
public class LeaveController  extends BaseCtl {

	protected Logger logger =  Logger.getLogger(ActivitiController.class);
	
	protected String processDefinitionKey;
	
    @Override
	public void index() {
    	
    	render("index.jsp");
	}
  
    public void form(){
    	render("form.jsp");
    }
    
    
    /**
     * 启动请假流程
     *
     */
    public void startWorkflow() {
    	Record user = getCurrentUser();
        OaLeave leave = getModel(OaLeave.class);
        String businessKey =  AppUtils.getStringSeq();
        String startTime=getPara("starttime");
        String endTime=getPara("endtime");
        leave.set("id", businessKey);
        leave.set("userid", user.getStr("id"));
        leave.set("leavetype", getPara("leavetype"));
        leave.set("starttime", startTime);
        leave.set("endtime", endTime);
        leave.set("reason", getPara("reason"));
        processDefinitionKey=getProcessKey();
        try {
            	ActivitiUtil.authenticatedUserId(user.getStr("id"));
            	ActivitiUtil.getProcessEngine().getIdentityService().setAuthenticatedUserId(user.getStr("id"));
            	ProcessInstance processInstance=ActivitiUtil.getProcessInstance(processDefinitionKey,businessKey);
             	leave.set("processinstanceid", processInstance.getId());
             	leave.set("createdate", DateTimeUtil.getTime());
             	leave.save();
             	renderSuccessJsonMsg( "流程已启动，流程ID：" + processInstance.getId());
             	/*TaskService taskService =ActivitiUtil.getTaskService();
             	TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateOrAssigned(user.getStr("id")).active();
             	if(taskQuery!=null){
             	List<Task>  tasks = taskService.createTaskQuery().processInstanceId( processInstance.getId()).active().list();
             	BeanToRecordUtil bru = BeanToRecordUtil.getInstance();
             	Record r;
             	String taskId="";
             	for (Task task : tasks) {
                	 r = bru.toRecord(task);
                	 taskId=r.getStr("id");
        		}
             	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");  
                 Date date1 = sdf.parse(startTime);  
                 Date date2 = sdf.parse(endTime); 
                 int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
                 List<String> list=new ArrayList<String>();
                 list.add("1");
                 list.add("3D0EC26EAAC24E139E8B221D9409D528");
                 
             	 Map<String, Object> variables = new HashMap<String, Object>();  
                 variables.put("ts", list);
                 ActivitiUtil.claimTaskByAssignee(taskId, user.getStr("id"));
             	 taskService.complete(taskId,variables);
             	 
             	}*/
             	 /*Execution ex = ActivitiUtil.getProcessEngine().getRuntimeService()
                         .createExecutionQuery()
                         .processInstanceId(processInstance.getId())
                         .activityId("task1509956285321")
                         .singleResult();
             	ActivitiUtil.getProcessEngine().getRuntimeService().setVariable(ex.getId(), "测试", 3000);
             	ActivitiUtil.getProcessEngine().getRuntimeService().signal(ex.getId());*/
             	
            }catch (ActivitiException e) {
                if (e.getMessage().indexOf("no processes deployed with key") != -1) {
                    logger.warn("没有部署流程!", e);
                    renderFailJsonMsg("没有部署流程，请在[工作流]->[流程管理]页面点击<发布>");
                } else {
                    logger.error("启动请假流程失败：", e);
                    renderFailJsonMsg("系统内部错误！");
                }
            } catch (Exception e) {
                logger.error("启动请假流程失败：", e);
                renderFailJsonMsg("系统内部错误！");
            } finally {
            	ActivitiUtil.authenticatedUserId(user.getStr("id"));
            }
        
    }

    /**
     * 任务列表
     *
     */
    public void taskList() {
    	Record user = getCurrentUser();
    	int pageNumber = getParaToInt("pageIndex")+1;
		int pageSize = getParaToInt("pageSize", 10);
		List<String> listStr = new ArrayList<String>();
		listStr.add(user.getStr("id"));
		String sql = "select * ";
		String extraSql = " from oa_leave where userid=? order by createdate desc";
		Page<Record> r = Db.paginate(pageNumber, pageSize, sql, extraSql,
				listStr.toArray());
		List<Record> list = r.getList();
		if (list.size() < 1) {
			Page<Record> r1 = Db.paginate(1, 10, sql, extraSql,
					listStr.toArray());
			list = r1.getList();
		}
		setAttr("data", list);
		setAttr("total", r.getTotalRow());
		renderJson();
    }

    /**
     * 读取运行中的流程实例
     *
     * @return
     */
    public void runningList(HttpServletRequest request) {
        int pageNum = getParaToInt("pageIndex");
		int pageSize = getParaToInt("pageSize", 10);
		List<OaLeave> rList = new ArrayList<OaLeave>();
		processDefinitionKey=getProcessKey();
        ProcessInstanceQuery query = ActivitiUtil.getProcessInstanceQuery().processDefinitionKey(processDefinitionKey).active().orderByProcessInstanceId().desc();
        List<ProcessInstance> list = query.listPage(pageNum,pageSize);
         // 关联业务实体
         for (ProcessInstance processInstance : list) {
             String businessKey = processInstance.getBusinessKey();
             if (businessKey == null) {
                 continue;
             }
             OaLeave leave = OaLeave.dao.findById(businessKey);
             leave.set("pi_id", processInstance.getId())
             .set("pi_version", processInstance.getProcessDefinitionVersion())
             .set("pi_processDefinitionId", processInstance.getProcessDefinitionId());

             // 设置当前任务信息
             List<Task> tasks = ActivitiUtil.getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).active().orderByTaskCreateTime().desc().listPage(0, 1);
             leave.set("tk_createTime", tasks.get(0).getCreateTime())
             .set("tk_assignee", tasks.get(0).getAssignee())
             .set("tk_createTime", tasks.get(0).getCreateTime());
             rList.add(leave);
         }
         setAttr("data", rList);
         setAttr("total",query.count());
         renderJson();
         
    }

    /**
     * 读取运行中的流程实例
     *
     * @return
     */
    public void finishedList(HttpServletRequest request) {
    	 int pageNum = getParaToInt("pageIndex");
   		 int pageSize = getParaToInt("pageSize", 10);
   		 List<OaLeave> rList = new ArrayList<OaLeave>();
   	     processDefinitionKey=getProcessKey();
	     ProcessInstanceQuery query = ActivitiUtil.getProcessInstanceQuery().processDefinitionKey(processDefinitionKey).active().orderByProcessInstanceId().desc();
	     List<ProcessInstance> list = query.listPage(pageNum,pageSize);
	
	     // 关联业务实体
	     for (ProcessInstance processInstance : list) {
	         String businessKey = processInstance.getBusinessKey();
	         if (businessKey == null) {
	             continue;
	         }
	         OaLeave leave = OaLeave.dao.findById(businessKey);
             leave.set("pi_id", processInstance.getId())
             .set("pi_version", processInstance.getProcessDefinitionVersion())
             .set("pi_processDefinitionId", processInstance.getProcessDefinitionId());

             // 设置当前任务信息
             List<Task> tasks = ActivitiUtil.getProcessEngine().getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).active().orderByTaskCreateTime().desc().listPage(0, 1);
             leave.set("tk_createTime", tasks.get(0).getCreateTime())
             .set("tk_assignee", tasks.get(0).getAssignee())
             .set("tk_createTime", tasks.get(0).getCreateTime());
             rList.add(leave);
	     }
	     setAttr("data", rList);
         setAttr("total",query.count());
         renderJson();
    }

    /**
     * 签收任务
     */
    public void claim() {
    	String taskId  = getPara("taskid");
    	String assignee = getCurrentUser().getStr("id");
        ActivitiUtil.claimTaskByAssignee(taskId, assignee);
        renderSuccessJsonMsg("任务已签收");
    }

    /**
     * 读取详细数据
     *
     * @param id
     * @return
     */
    public void getLeave() {
    	String id = getPara("id");
    	OaLeave leave = OaLeave.dao.findById(id);
    	setAttr("record", leave);
    	renderJson();
    }

    /**
     * 读取详细数据
     *
     * @param id
     * @return
     */
    public void getLeaveWithVars() {
        String id = getPara("id");
        String taskId = getPara("taskId");
    	OaLeave leave = OaLeave.dao.findById(id);
        Map<String, Object> variables = ActivitiUtil.getTaskService().getVariables(taskId);
        leave.setAttrs(variables);
        setAttr("record", leave);
    	renderJson();
    }

    /**
     * 完成任务
     *Oss
     * @param id
     * @return
     */
    public void complete() {
    	String taskId = getPara("taskid");
    	ActivitiUtil.takeCompleteTask(taskId);
        renderNull();
    }
    
    
    public void getCombox(){
    	String sql = "select val,remark from SYS_PARAM_INFO where key='QJLX'";
		List<Record> list = Db.find(sql);
		renderJson(list);
    }

    /**
     * 获取批注
     */
    public void hqpz(){
    	String taskId=getPara("taskid");
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	List<Object> historyCommnets = new ArrayList<>();
	    TaskService taskService =ActivitiUtil.getTaskService();
	    HistoryService historyService=ActivitiUtil.getHistoryService();
	    List<HistoricActivityInstance> hais = historyService.createHistoricActivityInstanceQuery().processInstanceId(taskId).list();
     for (HistoricActivityInstance hai : hais) {
         String historytaskId = hai.getTaskId();
         List<Comment> comments = taskService.getTaskComments(historytaskId);
         if(comments!=null && comments.size()>0){
             for(int i=0;i<comments.size();i++){
            	 String message=comments.get(i).getFullMessage();
            	 String userId=comments.get(i).getUserId();
            	 Date time=comments.get(i).getTime();
            	 String spTime=sdf.format(time);
            	 Record record=Db.findFirst("select * from sys_user_info where id=?",userId);
            	 String userName=record.getStr("name");
            	 String sp=userName+"["+spTime+"]"+":"+message+";";
            	 historyCommnets.add(sp);
             }
         }
     }
      setAttr("pz", historyCommnets);
      renderJson();
    }
    
    
    public void hqProcessId(){
    	String taskId=getPara("taskid");
    	ProcessInstanceQuery processInstanceQuery = ActivitiUtil.getProcessInstanceQuery();
    	List<ProcessInstance> list = processInstanceQuery.list();
    	 BeanToRecordUtil bru = BeanToRecordUtil.getInstance();
    	 Record r;
    	 String processDefinitionId="1";
    	 String tId="1";
         for (ProcessInstance processInstance : list) {
         	r = bru.toRecord(processInstance);
         	System.out.println(r.toJson());
         	System.out.println(r.getStr("processinstanceid"));
         	if(r.getStr("processinstanceid").equals(taskId)){
         		System.out.println("equalsequaks");
         		processDefinitionId=r.getStr("processdefinitionid");
         		tId=r.getStr("id");
         	}
         }
         setAttr("processDefinitionId", processDefinitionId);
         setAttr("tId", tId);
         renderJson();
    }
    
    /**
     * 读取带跟踪的图片
     */
    public void readResource() throws Exception {
    	String processDefinitionId = getPara("processDefinitionId");
    	String executionId = getPara("executionId");
        InputStream imageStream = ActivitiUtil.tracePhoto(processDefinitionId, executionId) ;
        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len;
        OutputStream out = getResponse().getOutputStream();
        while ((len = imageStream.read(b, 0, 1024)) != -1) {
        	out.write(b, 0, len);
        }
        renderNull();
    }
    
    public String getProcessKey(){
    	String requestUrl=getRequest().getRequestURL().toString();
    	String requestUrlArr[]=requestUrl.split("/");
    	String url=requestUrlArr[requestUrlArr.length-2];
    	Record r=Db.findFirst("select * from sys_menu_info where url=?",url);
    	 processDefinitionKey=r.getStr("actikey");
    	return processDefinitionKey;
    }
    
	
}
