package com.goodcol.util.activiti;

import java.util.List;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.form.StartFormDataImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.goodcol.core.plugin.activerecord.Record;

public class BeanToRecordUtil {
	private Record r;
	private List<Record> rList;
	private static class BeanToRecordUtilHolder{
		private static final BeanToRecordUtil INSTANCE = new BeanToRecordUtil();
	}	
	private BeanToRecordUtil (){}
	public static BeanToRecordUtil getInstance() {
		return BeanToRecordUtilHolder.INSTANCE;
	}
	/**
	 * 取出ProcessDefinition中属性放置到Record中
	 * @param ProcessDefinition
	 * @return
	 */
	
	public Record toRecord(ProcessDefinition obj){
		r = new Record();
		r.set("id", obj.getId());
    	r.set("category", obj.getCategory());
    	r.set("deploymentid", obj.getDeploymentId());
    	r.set("description", obj.getDescription());
    	r.set("diagramresourcename", obj.getDiagramResourceName());
    	r.set("key", obj.getKey());
    	r.set("name", obj.getName());
    	r.set("resourcename", obj.getResourceName());
    	r.set("tenantid", obj.getTenantId());
    	r.set("version", obj.getVersion());
    	r.set("suspended", obj.isSuspended());
    	r.set("hasstartformkey", obj.hasStartFormKey());
		return r;
	}
	

	public Record toRecord(Deployment obj){
		r = new Record();
		r.set("id", obj.getId());
        r.set("category", obj.getCategory());
        r.set("deploymentTime", obj.getDeploymentTime());
        r.set("name", obj.getName());
        r.set("tenantId", obj.getTenantId());
		return r;
	}
	
	public Record toRecord(Model obj){
		r = new Record();
		r.set("id", obj.getId());
        r.set("category", obj.getCategory());
        r.set("createtime", obj.getCreateTime());
        r.set("name", obj.getName());
        r.set("tenantid", obj.getTenantId());
        r.set("deploymentid", obj.getDeploymentId());
        r.set("key", obj.getKey());
        r.set("lastupdatetime", obj.getLastUpdateTime());
        r.set("metainfo", obj.getMetaInfo());
        r.set("version", obj.getVersion());
        r.set("haseditorsource", obj.hasEditorSource());
        r.set("haseditorsourceextra", obj.hasEditorSourceExtra());
        r.set("hashcode", obj.hashCode());
		return r;
	}
	public Record toRecord(ProcessInstance obj) {
		r = new Record();
		r.set("id", obj.getId());
        r.set("activityid", obj.getActivityId());
        r.set("businesskey", obj.getBusinessKey());
        r.set("name", obj.getName());
        r.set("tenantid", obj.getTenantId());
        r.set("deploymentid", obj.getDeploymentId());
        r.set("description", obj.getDescription());
        r.set("localizeddescription", obj.getLocalizedDescription());
        r.set("parentid", obj.getParentId());
        r.set("processdefinitionid", obj.getProcessDefinitionId());
        r.set("processdefinitionkey", obj.getProcessDefinitionKey());
        r.set("processdefinitionname", obj.getProcessDefinitionName());
        r.set("processdefinitionversion", obj.getProcessDefinitionVersion());
        r.set("processinstanceid", obj.getProcessInstanceId());
        r.set("hashcode", obj.hashCode());
        r.set("ended", obj.isEnded());
        r.set("suspended", obj.isSuspended());
		return r;
	}
	public Record toRecord(Task obj) {
		r = new Record();
		r.set("id", obj.getId());
        r.set("assignee", obj.getAssignee());
        r.set("category", obj.getCategory());
        r.set("createtime", obj.getCreateTime());
        r.set("name", obj.getName());
        r.set("tenantid", obj.getTenantId());
        r.set("delegationstate", obj.getDelegationState());
        r.set("description", obj.getDescription());
        r.set("duedate", obj.getDueDate());
        r.set("executionid", obj.getExecutionId());
        r.set("formkey", obj.getFormKey());
        r.set("owner", obj.getOwner());
        r.set("parenttaskid", obj.getParentTaskId());
        r.set("priority", obj.getPriority());
        r.set("processdefinitionid", obj.getProcessDefinitionId());
        r.set("hashcode", obj.hashCode());
        r.set("processinstanceid", obj.getProcessInstanceId());
        r.set("suspended", obj.isSuspended());
        r.set("taskdefinitionkey", obj.getTaskDefinitionKey());
		return r;
	}
	public Record toRecord(HistoricProcessInstance obj) {
		r = new Record();
		r.set("id", obj.getId());
        r.set("businesskey", obj.getBusinessKey());
        r.set("deletereason", obj.getDeleteReason());
        r.set("deploymentid", obj.getDeploymentId());
        r.set("name", obj.getName());
        r.set("tenantid", obj.getTenantId());
        r.set("durationinmillis", obj.getDurationInMillis());
        r.set("description", obj.getDescription());
        r.set("endactivityid", obj.getEndActivityId());
        r.set("endtime", obj.getEndTime());
        r.set("processdefinitionid", obj.getProcessDefinitionId());
        r.set("processdefinitionkey", obj.getProcessDefinitionKey());
        r.set("processdefinitionname", obj.getProcessDefinitionName());
        r.set("processdefinitionversion", obj.getProcessDefinitionVersion());
        r.set("startactivityid", obj.getStartActivityId());
        r.set("starttime", obj.getStartTime());
        r.set("startuserid", obj.getStartUserId());
        r.set("superprocessinstanceid", obj.getSuperProcessInstanceId());
        r.set("hashcode", obj.hashCode());
		return r;
	}
	public Object toRecord(StartFormDataImpl obj) {
		r = new Record();
		r.set("deploymentid", obj.getDeploymentId());
        r.set("formkey", obj.getFormKey());
        r.set("formproperties", obj.getFormProperties());
        r.set("processdefinition", obj.getProcessDefinition());
        r.set("hashcode", obj.hashCode());
		return r;
	}
	
	
}
