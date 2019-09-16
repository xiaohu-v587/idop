
package com.goodcol.render;

import java.io.File;
import javax.servlet.ServletContext;
import com.goodcol.config.Constants;
import com.goodcol.template.Engine;

/**
 * RenderFactory.
 */
public class RenderFactory implements IRenderFactory {
	
	protected Engine engine;
	protected Constants constants;
	protected ServletContext servletContext;
	protected MainRenderFactory mainRenderFactory;
	
	// private static final RenderFactory me = new RenderFactory();
	// private RenderFactory() {}
	
	// public static RenderFactory me() {
	// 	return me;
	// }
	
	public void init(Engine engine, Constants constants, ServletContext servletContext) {
		this.engine = engine;
		this.constants = constants;
		this.servletContext = servletContext;
		
		// create mainRenderFactory
		switch (constants.getViewType()) {
		case GCDS_TEMPLATE:
			mainRenderFactory = new MainRenderFactory();
			break ;
		case FREE_MARKER:
			mainRenderFactory = new FreeMarkerRenderFactory();
			break ;
		case JSP:
			mainRenderFactory = new JspRenderFactory();
			break ;
		case VELOCITY:
			mainRenderFactory = new VelocityRenderFactory();
			break ;
		}
	}
	
	/**
	 * Return Render by default ViewType which config in Config
	 */
	public Render getRender(String view) {
		return mainRenderFactory.getRender(view);
	}
	
	public Render getTemplateRender(String view) {
		return new TemplateRender(view);
	}
	
	public Render getFreeMarkerRender(String view) {
		return new FreeMarkerRender(view);
	}
	
	public Render getJspRender(String view) {
		return new JspRender(view);
	}
	
	public Render getVelocityRender(String view) {
		return new VelocityRender(view);
	}
	
	public Render getJsonRender() {
		return new JsonRender();
	}
	
	public Render getJsonRender(String key, Object value) {
		return new JsonRender(key, value);
	}
	
	public Render getJsonRender(String[] attrs) {
		return new JsonRender(attrs);
	}
	
	public Render getJsonRender(String jsonText) {
		return new JsonRender(jsonText);
	}
	
	public Render getJsonRender(Object object) {
		return new JsonRender(object);
	}
	
	public Render getTextRender(String text) {
		return new TextRender(text);
	}
	
	public Render getTextRender(String text, String contentType) {
		return new TextRender(text, contentType);
	}
	
	public Render getTextRender(String text, ContentType contentType) {
		return new TextRender(text, contentType);
	}
	
	public Render getDefaultRender(String view) {
		return getRender(view + constants.getViewExtension());
	}
	
	public Render getErrorRender(int errorCode, String view) {
		return new ErrorRender(errorCode, view);
	}
	
	public Render getErrorRender(int errorCode) {
		return new ErrorRender(errorCode, constants.getErrorView(errorCode));
	}
	
	public Render getFileRender(String fileName) {
		return new FileRender(fileName);
	}
	
	public Render getFileRender(File file) {
		return new FileRender(file);
	}
	
	public Render getRedirectRender(String url) {
		return new RedirectRender(url);
	}
	
	public Render getRedirectRender(String url, boolean withQueryString) {
		return new RedirectRender(url, withQueryString);
	}
	
	public Render getRedirect301Render(String url) {
		return new Redirect301Render(url);
	}
	
	public Render getRedirect301Render(String url, boolean withQueryString) {
		return new Redirect301Render(url, withQueryString);
	}
	
	public Render getNullRender() {
		return new NullRender();
	}
	
	public Render getJavascriptRender(String jsText) {
		return new JavascriptRender(jsText);
	}
	
	public Render getHtmlRender(String htmlText) {
		return new HtmlRender(htmlText);
	}
	
	public Render getXmlRender(String view) {
		return new XmlRender(view);
	}
	
	public Render getCaptchaRender() {
		return new com.goodcol.captcha.CaptchaRender();
	}
	
	public Render getQrCodeRender(String content, int width, int height) {
		return new QrCodeRender(content, width, height);
	}
	
	public Render getQrCodeRender(String content, int width, int height, char errorCorrectionLevel) {
		return new QrCodeRender(content, width, height, errorCorrectionLevel);
	}
	
	// --------
	private static class MainRenderFactory {
		public Render getRender(String view) {
			return new TemplateRender(view);
		}
	}
	
	private static class FreeMarkerRenderFactory extends MainRenderFactory {
		public Render getRender(String view) {
			return new FreeMarkerRender(view);
		}
	}
	
	private static class JspRenderFactory extends MainRenderFactory {
		public Render getRender(String view) {
			return new JspRender(view);
		}
	}
	
	private static class VelocityRenderFactory extends MainRenderFactory {
		public Render getRender(String view) {
			return new VelocityRender(view);
		}
	}
}


