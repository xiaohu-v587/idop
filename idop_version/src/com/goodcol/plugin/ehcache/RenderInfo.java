
package com.goodcol.plugin.ehcache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.goodcol.render.FreeMarkerRender;
import com.goodcol.render.JsonRender;
import com.goodcol.render.JspRender;
import com.goodcol.render.Render;
import com.goodcol.render.TemplateRender;
import com.goodcol.render.VelocityRender;
import com.goodcol.render.XmlRender;

/**
 * RenderInfo.
 */
public class RenderInfo implements Serializable {
	
	private static final long serialVersionUID = -7299875545092102194L;
	
	protected String view;
	protected Integer renderType;
	protected Map<String, Object> otherPara = null;
	
	public RenderInfo(Render render) {
		if (render == null) {
			throw new IllegalArgumentException("Render can not be null.");
		}
		
		view = render.getView();
		if (render instanceof TemplateRender) {
			renderType = RenderType.TEMPLATE_RENDER;
		} else if (render instanceof FreeMarkerRender) {
			renderType = RenderType.FREE_MARKER_RENDER;
		} else if (render instanceof JspRender) {
			renderType = RenderType.JSP_RENDER;
		} else if (render instanceof VelocityRender) {
			renderType = RenderType.VELOCITY_RENDER;
		} else if (render instanceof XmlRender) {
			renderType = RenderType.XML_RENDER;
		} else if(render instanceof JsonRender) {
			JsonRender jr = (JsonRender)render;
			renderType = RenderType.JSON_RENDER;
			otherPara = new HashMap<String, Object>();
			otherPara.put("jsonText", jr.getJsonText());
			otherPara.put("attrs", jr.getAttrs());
			otherPara.put("forIE", jr.getForIE());
		}
		else
			throw new IllegalArgumentException("CacheInterceptor can not support the render of the type : " + render.getClass().getName());
	}
	
	public Render createRender() {
		switch (renderType) {
		case RenderType.TEMPLATE_RENDER:
			return new TemplateRender(view);
		case RenderType.FREE_MARKER_RENDER:
			return new FreeMarkerRender(view);
		case RenderType.JSP_RENDER:
			return new JspRender(view);
		case RenderType.VELOCITY_RENDER:
			return new VelocityRender(view);
		case RenderType.XML_RENDER:
			return new XmlRender(view);
		case RenderType.JSON_RENDER:
			JsonRender jr;
			if (otherPara.get("jsonText") != null) {
				jr = new JsonRender((String)otherPara.get("jsonText"));
			} else if (otherPara.get("attrs") != null) {
				jr = new JsonRender((String[])otherPara.get("attrs"));
			} else {
				jr = new JsonRender();
			}
			
			if (Boolean.TRUE.equals(otherPara.get("forIE"))) {
				jr.forIE();
			}
			return jr;
		default :
			throw new IllegalArgumentException("CacheInterceptor can not support the renderType of the value : " + renderType);
		}
	}
}
