package com.goodcol.controller.activiti;

import org.activiti.engine.RepositoryService;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.activiti.ActivitiUtil;
import com.goodcol.util.ext.anatation.RouteBind;
/**
 * 流程发起页面
 * 当前用户发起流程后：
 * 若是仅保存了，那么这条流程在"我的代办"中可查看
 * 若是流转了，那么这条流程在"我的已办"中可查看
 * 
 * 暂时开发一个按钮，根据流程的key发起流程，后续在index页面对流程进行展示分类优化
 * @author caojun
 *
 */
@RouteBind(path = "/processStart")
@Before( { ManagerPowerInterceptor.class })
public class ProcessStartCtl extends BaseCtl {
	
	protected Logger logger =  Logger.getLogger(ProcessStartCtl.class);
	
    @Override
	public void index() {
    	render("index.jsp");
	}
    
    /**
     * 展示所有可以使用的流程定义
     */
    public void queryAvailableProcess(){
    	
    }
    
    public void startProcess(){
    	render("index.jsp");
    }
    
    
}
