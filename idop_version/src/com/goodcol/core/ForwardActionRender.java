
package com.goodcol.core;

import com.goodcol.render.Render;

/**
 * ForwardActionRender
 */
class ForwardActionRender extends Render {
	
	private String actionUrl;
	
	public ForwardActionRender(String actionUrl) {
		this.actionUrl = actionUrl.trim();
	}
	
	public String getActionUrl() {
		return actionUrl;
	}
	
	public void render() {
		
	}
}
