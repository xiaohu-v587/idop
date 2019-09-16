package com.goodcol.controller.activiti;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.runtime.ProcessInstance;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.model.OaLeave;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.activiti.ActivitiUtil;
import com.goodcol.util.ext.anatation.RouteBind;

@RouteBind(path = "/getProductData")
public class GetProductDataController extends BaseCtl {
	
	protected Logger logger =  Logger.getLogger(GetProductDataController.class);
	
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
    
    public String getProcessKey(){
    	String requestUrl=getRequest().getRequestURL().toString();
    	String requestUrlArr[]=requestUrl.split("/");
    	String url=requestUrlArr[requestUrlArr.length-2];
    	Record r=Db.findFirst("select * from sys_menu_info where url=?",url);
    	 processDefinitionKey=r.getStr("actikey");
    	return processDefinitionKey;
    }
    
}
