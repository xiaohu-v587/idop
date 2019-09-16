package com.goodgol.attpointmode;



import com.goodcol.controller.BaseCtl;

import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.interceptor.ManagerPowerInterceptor;

import com.goodcol.server.zxglserver.PccmKPIParaServer;

import com.goodcol.util.ext.anatation.RouteBind;


@RouteBind(path = "/atticepointmode")
@Before({ ManagerPowerInterceptor.class })
public class attpointmode extends BaseCtl {
	public static Logger log = Logger.getLogger(BaseCtl.class);
	private PccmKPIParaServer server = new PccmKPIParaServer();
	@Override
	public void index() {
		// TODO Auto-generated method stub
		//addAtticepointFocus.jsp
	}
	
   public void atticepoint(){
	   render("attpoint.jsp");
   }
   

}
