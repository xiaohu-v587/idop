
package com.goodcol.render;

/**
 * XmlRender use  template
 */
public class XmlRender extends TemplateRender {
	
	private static final String contentType = "text/xml; charset=" + getEncoding();
	
	public XmlRender(String view) {
		super(view);
	}
	
	public String getContentType() {
		return contentType;
	}
}





