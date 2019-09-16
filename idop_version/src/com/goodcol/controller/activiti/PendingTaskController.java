package com.goodcol.controller.activiti;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

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
 * 待审批流程Controller
 * 查询到需要自己审批的流程
 *
 * @author changxy
 */
@RouteBind(path = "/pendingTask")
@Before( { ManagerPowerInterceptor.class })
public class PendingTaskController extends BaseCtl {

    protected Logger logger =  Logger.getLogger(ActivitiController.class);
    @Override
	public void index() {
    	render("index.jsp");
	}
    
    /**
     * 查询待审批(待处理)流程
     */
    public void list(){
    	int pageNum = getParaToInt("pageIndex");
		int pageSize = getParaToInt("pageSize", 10);
		Record r ;
		Record user = getCurrentUser();
		String id = getPara("id");//任务id
		String name = getPara("name");//任务名称
		String taskdefinitionkey = getPara("taskdefinitionkey");//任务Key
		String priority = getPara("priority");//优先级
		Date after_createTime = getParaToDate("after_createtime");//任务创建日期开始
		Date before_createTime = getParaToDate("before_createtime");//任务创建日期结束
		Date after_dueDate = getParaToDate("after_dueDate");//任务逾期日开始
		Date before_dueDate = getParaToDate("before_dueDate");//任务逾期日结束
		String owner = getPara("owner");//任务所属人
		
		TaskQuery taskQuery = ActivitiUtil.getTaskQuery().taskCandidateOrAssigned(user.getStr("id"));
		if(taskQuery.count()>0){
		if(AppUtils.StringUtil(id)!=null){
    		taskQuery.taskId(id);
    	}
		if(AppUtils.StringUtil(name)!=null){
			taskQuery.taskNameLike("%"+name+"%");
		}
		if(AppUtils.StringUtil(taskdefinitionkey)!=null){
			taskQuery.taskNameLike("%"+taskdefinitionkey+"%");
		}
		if(AppUtils.StringUtil(priority)!=null){
			taskQuery.taskPriority(Integer.valueOf(priority));
		}
		if(after_createTime!=null){
			taskQuery.taskCreatedAfter(after_createTime);
		}
		if(before_createTime!=null){
			taskQuery.taskCreatedBefore(before_createTime);
		}
		if(after_dueDate!=null){
			taskQuery.taskDueBefore(after_dueDate);
		}
		if(before_dueDate!=null){
			taskQuery.taskDueBefore(before_dueDate);
		}
		
		if(AppUtils.StringUtil(owner)!=null){
			taskQuery.taskOwnerLike("%"+owner+"%");
		}
		//数据存储集合
		List<Record> rList = new ArrayList<Record>();
		BeanToRecordUtil bru = BeanToRecordUtil.getInstance();
    	// 根据当前人的ID查询
        List<Task> tasks = taskQuery.listPage(pageNum,pageSize);
        for (Task task : tasks) {
	       	r = bru.toRecord(task);
	       	rList.add(r);
		}
       setAttr("data", rList);
       setAttr("total",taskQuery.count());
		}else{
			TaskQuery taskQuery2 = ActivitiUtil.getTaskQuery().taskCandidateGroup(user.getStr("id"));
			if(AppUtils.StringUtil(id)!=null){
				taskQuery2.taskId(id);
	    	}
			if(AppUtils.StringUtil(name)!=null){
				taskQuery2.taskNameLike("%"+name+"%");
			}
			if(AppUtils.StringUtil(taskdefinitionkey)!=null){
				taskQuery2.taskNameLike("%"+taskdefinitionkey+"%");
			}
			if(AppUtils.StringUtil(priority)!=null){
				taskQuery2.taskPriority(Integer.valueOf(priority));
			}
			if(after_createTime!=null){
				taskQuery2.taskCreatedAfter(after_createTime);
			}
			if(before_createTime!=null){
				taskQuery2.taskCreatedBefore(before_createTime);
			}
			if(after_dueDate!=null){
				taskQuery2.taskDueBefore(after_dueDate);
			}
			if(before_dueDate!=null){
				taskQuery2.taskDueBefore(before_dueDate);
			}
			
			if(AppUtils.StringUtil(owner)!=null){
				taskQuery2.taskOwnerLike("%"+owner+"%");
			}
			//数据存储集合
			List<Record> rList = new ArrayList<Record>();
			BeanToRecordUtil bru = BeanToRecordUtil.getInstance();
	    	// 根据当前人的ID查询
	        List<Task> tasks = taskQuery2.listPage(pageNum,pageSize);
	        for (Task task : tasks) {
		       	r = bru.toRecord(task);
		       	rList.add(r);
			}
	       setAttr("data", rList);
	       setAttr("total",taskQuery2.count());
		}
       renderJson();
    }
    
    public void form(){
    	
    	render("form.jsp");
    }
    
   
}