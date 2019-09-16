

package com.goodcol.config;

import java.util.ArrayList;
import java.util.List;
import com.goodcol.handler.Handler;

/**
 * Handlers.
 */
final public class Handlers {
	
	private final List<Handler> handlerList = new ArrayList<Handler>();
	
	public Handlers add(Handler handler) {
		if (handler == null) {
			throw new IllegalArgumentException("handler can not be null");
		}
		handlerList.add(handler);
		return this;
	}
	
	public List<Handler> getHandlerList() {
		return handlerList;
	}
}
