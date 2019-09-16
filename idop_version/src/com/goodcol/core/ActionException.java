
package com.goodcol.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.goodcol.kit.StrKit;
import com.goodcol.log.Log;
import com.goodcol.render.Render;
import com.goodcol.render.RenderManager;

/**
 * ActionException.
 */
public class ActionException extends RuntimeException {
	
	private static final long serialVersionUID = 1998063243843477017L;
	private static final Log log = Log.getLog(ActionException.class);
	private int errorCode;
	private Render errorRender;
	
	public ActionException(int errorCode, Render errorRender) {
		init(errorCode, errorRender);
	}
	
	private void init(final int errorCode, final Render errorRender) {
		if (errorRender == null) {
			throw new IllegalArgumentException("The parameter errorRender can not be null.");
		}
		
		this.errorCode = errorCode;
		
		if (errorRender instanceof com.goodcol.render.ErrorRender) {
			this.errorRender = errorRender;
		}
		else {
			this.errorRender = new Render() {
				public Render setContext(HttpServletRequest req, HttpServletResponse res, String viewPath) {
					errorRender.setContext(req, res, viewPath);
					res.setStatus(errorCode);	// important
					return this;
				}
				
				public void render() {
					errorRender.render();
				}
			};
		}
	}
	
	public ActionException(int errorCode, String errorView) {
		if (StrKit.isBlank(errorView)) {
			throw new IllegalArgumentException("The parameter errorView can not be blank.");
		}
		
		this.errorCode = errorCode;
		this.errorRender = RenderManager.me().getRenderFactory().getErrorRender(errorCode, errorView);
	}
	
	public ActionException(int errorCode, Render errorRender, String errorMessage) {
		super(errorMessage);
		init(errorCode, errorRender);
		log.warn(errorMessage);
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public Render getErrorRender() {
		return errorRender;
	}
}

