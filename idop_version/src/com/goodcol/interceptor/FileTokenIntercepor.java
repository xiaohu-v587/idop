package com.goodcol.interceptor;

import com.goodcol.core.aop.Interceptor;
import com.goodcol.core.core.ActionInvocation;
import com.goodcol.core.core.Controller;

public class FileTokenIntercepor implements Interceptor{
	
	/**
	 * 此拦截器用于拦截文件重复刷新提交问题 by changxy 20161223
	 */
	@Override
	public void intercept(ActionInvocation ai) {
		Controller controller = ai.getController();
		boolean isMultipart = reportCommonRequest(controller);
		if(isMultipart){
			controller.getFile();
	        boolean isTranslate = controller.validateToken();
	        controller.createToken();
	        if(!isTranslate){
	    		controller.setAttr("show_outhor_msg", "请不要重复提交");
	    		controller.setAttr("id", controller.getPara("pid"));
	    		controller.setAttr("type", controller.getPara("type"));
	    		controller.render("staffList.jsp");
	    		/*Enumeration<String> en = controller.getParaNames();
	    		while (en.hasMoreElements()) {
					String name = (String) en.nextElement();
					controller.setAttr(name, controller.getPara(name));
				}*/
	        }else {
	        	ai.invoke();
			} 
		}else {
			ai.invoke();
		}
		
	}
	/** 
     * 判断是否是multipart/form-data请求 
     * @param request 
     * @return 
     */  
	public final boolean reportCommonRequest(Controller controller) {
		String content_type = controller.getRequest().getContentType();
		if (content_type == null || content_type.toLowerCase().indexOf("multipart") == -1) {	// if (content_type == null || content_type.indexOf("multipart/form-data") == -1) {
			return false;
		}
		return true;
	} 
}
