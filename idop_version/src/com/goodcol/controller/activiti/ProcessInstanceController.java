package com.goodcol.controller.activiti;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;

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
 * 流程实例控制器
 *
 * @author HenryYan
 */
@RouteBind(path = "/processinstance")
@Before( { ManagerPowerInterceptor.class })
public class ProcessInstanceController extends BaseCtl {

    protected Logger logger =  Logger.getLogger(ProcessInstanceController.class);
    @Override
	public void index() {
    	render("index.jsp");
	}
   
    /**
     * 流程实例列表
     *
     * @return
     */
    public void list() {
    	Record r;
    	int pageNum = getParaToInt("pageIndex");
		int pageSize = getParaToInt("pageSize", 10);
		
		String  id = getPara("id");
		String processdefKey = getPara("processdefKey");
		String  name = getPara("name");
		
		//数据存储集合
		List<Record> rList = new ArrayList<Record>();
		ProcessInstanceQuery processInstanceQuery = ActivitiUtil.getProcessInstanceQuery();
    	if(AppUtils.StringUtil(id)!=null){
    		processInstanceQuery.processInstanceId(id);
		}
    	if(AppUtils.StringUtil(name)!=null){
    		processInstanceQuery.processInstanceNameLikeIgnoreCase("%"+name+"%");
		}
    	if(AppUtils.StringUtil(processdefKey)!=null){
    		processInstanceQuery.processDefinitionKey(processdefKey);
    	}
		List<ProcessInstance> list = processInstanceQuery.listPage(pageNum, pageSize);
        //因与前台miniui对接需要json格式数据，但activiti实体对象无法进行序列化（实体中涉及Map,List,或其他类型对象）暂时采用手动组合方式进行转换为record进行json化
        BeanToRecordUtil bru = BeanToRecordUtil.getInstance();
        for (ProcessInstance processInstance : list) {
        	r = bru.toRecord(processInstance);
        	rList.add(r);
        }
        setAttr("data", rList);
		setAttr("total", processInstanceQuery.count());
		renderJson();
    }
    
    
    
    /**
     * 挂起、激活流程实例
     */
    public void updateState() {
       String state = getPara("state");
       String processInstanceId = getPara("processinstanceid");
       String msg = "";
    	if (state.equals("active")) {
            msg = "已激活ID为[" + processInstanceId + "]的流程实例。";
            ActivitiUtil.activateProcessInstanceById(processInstanceId);
        } else if (state.equals("suspend")) {
        	msg = "已挂起ID为[" + processInstanceId + "]的流程定义。";
        	ActivitiUtil.suspendProcessInstanceById(processInstanceId);
        }
    	renderSuccessJsonMsg(msg);
    }

}