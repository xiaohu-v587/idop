
package com.goodcol.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.goodcol.config.Constants;
import com.goodcol.aop.Invocation;
import com.goodcol.handler.Handler;
import com.goodcol.log.Log;
import com.goodcol.render.Render;
import com.goodcol.render.RenderException;
import com.goodcol.render.RenderManager;

/**
 * ActionHandler
 */
public class ActionHandler extends Handler {
	
	private final boolean devMode;
	private final ActionMapping actionMapping;
	private static final RenderManager renderManager = RenderManager.me();
	private static final Log log = Log.getLog(ActionHandler.class);
	
	public ActionHandler(ActionMapping actionMapping, Constants constants) {
		this.actionMapping = actionMapping;
		this.devMode = constants.getDevMode();
	}
	
	/**
	 * handle
	 * 1: Action action = actionMapping.getAction(target)
	 * 2: new Invocation(...).invoke()
	 * 3: render(...)
	 */
	public final void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		if (target.indexOf('.') != -1) {
			return ;
		}
		
		isHandled[0] = true;
		String[] urlPara = {null};
		Action action = actionMapping.getAction(target, urlPara);
		
		if (action == null) {
			if (log.isWarnEnabled()) {
				String qs = request.getQueryString();
				log.warn("404 Action Not Found: " + (qs == null ? target : target + "?" + qs));
			}
			renderManager.getRenderFactory().getErrorRender(404).setContext(request, response).render();
			return ;
		}
		
		try {
			Controller controller = action.getControllerClass().newInstance();
			controller.init(request, response, urlPara[0]);
			
			if (devMode) {
				if (ActionReporter.isReportAfterInvocation(request)) {
					new Invocation(action, controller).invoke();
					ActionReporter.report(target, controller, action);
				} else {
					ActionReporter.report(target, controller, action);
					new Invocation(action, controller).invoke();
				}
			}
			else {
				new Invocation(action, controller).invoke();
			}
			
			Render render = controller.getRender();
			if (render instanceof ForwardActionRender) {
				String actionUrl = ((ForwardActionRender)render).getActionUrl();
				if (target.equals(actionUrl)) {
					throw new RuntimeException("The forward action url is the same as before.");
				} else {
					handle(actionUrl, request, response, isHandled);
				}
				return ;
			}
			
			if (render == null) {
				render = renderManager.getRenderFactory().getDefaultRender(action.getViewPath() + action.getMethodName());
			}
			render.setContext(request, response, action.getViewPath()).render();
		}
		catch (RenderException e) {
			if (log.isErrorEnabled()) {
				String qs = request.getQueryString();
				log.error(qs == null ? target : target + "?" + qs, e);
			}
		}
		catch (ActionException e) {
			int errorCode = e.getErrorCode();
			String msg = null;
			if (errorCode == 404) {
				msg = "404 Not Found: ";
			} else if (errorCode == 401) {
				msg = "401 Unauthorized: ";
			} else if (errorCode == 403) {
				msg = "403 Forbidden: ";
			}
			
			if (msg != null) {
				if (log.isWarnEnabled()) {
					String qs = request.getQueryString();
					log.warn(msg + (qs == null ? target : target + "?" + qs));
				}
			} else {
				if (log.isErrorEnabled()) {
					String qs = request.getQueryString();
					log.error(qs == null ? target : target + "?" + qs, e);
				}
			}
			
			e.getErrorRender().setContext(request, response, action.getViewPath()).render();
		}
		catch (Exception e) {
			if (log.isErrorEnabled()) {
				String qs = request.getQueryString();
				log.error(qs == null ? target : target + "?" + qs, e);
			}
			renderManager.getRenderFactory().getErrorRender(500).setContext(request, response, action.getViewPath()).render();
		}
	}
}





