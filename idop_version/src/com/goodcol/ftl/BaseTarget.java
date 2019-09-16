package com.goodcol.ftl;
import java.io.File;
import java.net.URL;
import freemarker.template.Configuration;
import freemarker.template.TemplateDirectiveModel;
/**
 * 引用方法为将此标签类实例设置到request中,然后直接使用<@tage 自定义freemarker 标签
 */
public abstract class BaseTarget implements TemplateDirectiveModel {
	public static Configuration freemarker_cfg = new Configuration();
	static {
		try {
			URL url = BaseTarget.class.getResource("/");
			freemarker_cfg.setDirectoryForTemplateLoading(new File(url.getFile().replace("classes", "tagftl")));
			freemarker_cfg.setDefaultEncoding("UTF-8");
		} catch (Exception e) {
		}
	}
}
