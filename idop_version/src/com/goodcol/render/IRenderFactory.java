package com.goodcol.render;

import java.io.File;
import javax.servlet.ServletContext;
import com.goodcol.config.Constants;
import com.goodcol.template.Engine;

/**
 * IRenderFactory.
 */
public interface IRenderFactory {
	
	public void init(Engine engine, Constants constants, ServletContext servletContext);
	
	/**
	 * Create Render for Controller.render(String view)
	 */
	public Render getRender(String view);
	
	public Render getTemplateRender(String view);
	
	public Render getFreeMarkerRender(String view);
	
	public Render getJspRender(String view);
	
	public Render getVelocityRender(String view);
	
	public Render getJsonRender();
	
	public Render getJsonRender(String key, Object value);
	
	public Render getJsonRender(String[] attrs);
	
	public Render getJsonRender(String jsonText);
	
	public Render getJsonRender(Object object);
	
	public Render getTextRender(String text);
	
	public Render getTextRender(String text, String contentType);
	
	public Render getTextRender(String text, ContentType contentType);
	
	public Render getDefaultRender(String view);
	
	public Render getErrorRender(int errorCode, String view);
	
	public Render getErrorRender(int errorCode);
	
	public Render getFileRender(String fileName);
	
	public Render getFileRender(File file);
	
	public Render getRedirectRender(String url);
	
	public Render getRedirectRender(String url, boolean withQueryString);
	
	public Render getRedirect301Render(String url);
	
	public Render getRedirect301Render(String url, boolean withQueryString);
	
	public Render getNullRender();
	
	public Render getJavascriptRender(String jsText);
	
	public Render getHtmlRender(String htmlText);
	
	public Render getXmlRender(String view);
	
	public Render getCaptchaRender();
	
	public Render getQrCodeRender(String content, int width, int height);
	
	public Render getQrCodeRender(String content, int width, int height, char errorCorrectionLevel);
}


