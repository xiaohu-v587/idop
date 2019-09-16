package com.goodcol.render;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import com.goodcol.template.Engine;

/**
 * TemplateRender
 */
public class TemplateRender extends Render {
	
	private static Engine engine;
	
	private static final String contentType = "text/html; charset=" + getEncoding();
	
	static void init(Engine engine) {
		if (engine == null) {
			throw new IllegalArgumentException("engine can not be null");
		}
		TemplateRender.engine = engine;
	}
	
	public TemplateRender(String view) {
		this.view = view;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public void render() {
		response.setContentType(getContentType());
        
		Map<Object, Object> data = new HashMap<Object, Object>();
		for (Enumeration<String> attrs=request.getAttributeNames(); attrs.hasMoreElements();) {
			String attrName = attrs.nextElement();
			data.put(attrName, request.getAttribute(attrName));
		}
		
		PrintWriter writer = null;
        try {
        	writer = response.getWriter();
        	engine.getTemplate(view).render(data, writer);
		} catch (Exception e) {
			throw new RenderException(e);
		}
		finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	public String toString() {
		return view;
	}
}








