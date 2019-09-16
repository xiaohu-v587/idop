package com.goodcol.interceptor;

import java.util.List;

import com.goodcol.util.CacheUtil;
import com.goodcol.util.PermissionUtil;
import com.goodcol.util.RedisUtil;
import com.goodcol.core.aop.Interceptor;
import com.goodcol.core.core.ActionInvocation;
import com.goodcol.core.core.Controller;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;

/**
 * 权限验证拦截器
 */
public class PermissionInterceptor implements Interceptor {

	@Override
	public void intercept(ActionInvocation ai) {
		String menuId = null;
		
				
		//获取控制器的url
		String menuUrl = ai.getControllerKey();
		
		menuUrl = menuUrl.substring(menuUrl.lastIndexOf("/")+1);
		
		//根据控制器url获取对应的菜单的id
		
		List<Record> lr = Db.find("select id from sys_menu_info where url='" + menuUrl + "'");
		if(lr.size()>0){
			menuId = lr.get(0).getStr("ID");
		}
		
		//获取当前操作的action
		String objCode  = ai.getMethodName();
		
		Controller ctrl = ai.getController();
		String user_token = ctrl.getCookie("user_token");
		
		Record po = (Record) CacheUtil.getCache(user_token, ctrl.getRequest());
		String roleId = po.getStr("ROLEID");
		
		//调用权限验证函数
		boolean perFlag = PermissionUtil.getInstanceOf().checkPermission(menuId, roleId, objCode);
		
		//权限验证通过
		if(perFlag){
			
			//执行具体的代码
			ai.invoke();
		}else{
			
			//权限验证失败
			if("index".equals(objCode)||"download".equals(objCode)||"downloadWd".equals(objCode)){	
				if ("modelview".equals(menuUrl)||"quotaDataExhibit".equals(menuUrl)||"personQuotaDataExhibit".equals(menuUrl))			//modelview、modelviews公用的模块全景视图，页面上跳转都指向modelview
				{
					ai.invoke();
				}	
				else
				{
					// 点击菜单打开页面时进行权限判断
					ctrl.renderHtml("<script type='text/javascript'> alert('无此权限,请与管理员联系!'); parent.removeTab(); </script>");
				}
			}else if("detail".equals(objCode)){
				
				// mini.open打开页面时进行权限判断
				ctrl.renderHtml("<script type='text/javascript'> function CloseWindow(action) {if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action); else window.close();} alert('无此权限,请与管理员联系!'); function SetData(obj){};  setTimeout(function xx(){CloseWindow(\"cancel\");},10);</script>");
			}else{
				if( isRestQuest(ctrl) ){
					
					// 普通的按钮点击操作时进行权限判断
					ctrl.renderText("1000");
				}else{
					ctrl.renderHtml("<div style=\" text-align:center; width:100%; padding-top:88px;\">无此权限,请与管理员联系!</div>");
				}
			}
		}
	}
	
	// 判断是AJAX还是URL请求
	private boolean isRestQuest(Controller controller) {
		String acceptStr = controller.getRequest().getHeader("Accept");
		if (acceptStr.indexOf("html") > 0 || acceptStr.indexOf("xml") > 0 || acceptStr.indexOf("application") > 0 || acceptStr.indexOf("image") > 0) {
			return false;
		} else {
			return true;
		}
	}

}
