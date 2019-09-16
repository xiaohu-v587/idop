/**
 * 
 */
package com.goodcol.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import com.goodcol.core.kit.PathKit;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * @author dinggang
 *
 */
public class FreeMarkerUtil {
	/**
	 * 利用freemark、ftl生成java类
	 * 
	 * @param root
	 * @param map
	 * @param table_name
	 * @param dest_path
	 * @param template_path
	 * @param className
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static String gen(Map<String, Object> root, Map<String, Object> map, String table_name, String className,
			String template_path, String dest_path) throws IOException, TemplateException {
		Configuration cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(new File(template_path));
		cfg.setDefaultEncoding("utf-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		genearte(cfg, dest_path, className, root, map);
		return "success";
	}

	/**
	 * 生成java文件、jsp文件
	 * 
	 * @param cfg
	 * @param dest_path
	 * @param className
	 * @param root
	 * @param map
	 * @throws IOException
	 * @throws TemplateException
	 */
	private static void genearte(Configuration cfg, String dest_path, String className, Map<String, Object> root,
			Map<String, Object> map) throws IOException, TemplateException {
		genarateJava(cfg, dest_path, className, root);
		generateJsp(cfg, dest_path, map);

	}

	/**
	 * 生成后缀名为.jsp的文件
	 * 
	 * @param cfg
	 * @param dest_path
	 * @param map
	 * @throws IOException
	 * @throws TemplateException
	 */
	private static void generateJsp(Configuration cfg, String dest_path, Map<String, Object> map)
			throws IOException, TemplateException {
		generateIndexJsp(cfg, dest_path, map);
		generateFormJsp(cfg, dest_path, map);
	}

	/**
	 * 生成form.jsp
	 * @param cfg
	 * @param dest_path
	 * @param map
	 * @throws IOException
	 * @throws TemplateException
	 */
	private static void generateFormJsp(Configuration cfg, String dest_path, Map<String, Object> map)
			throws IOException, TemplateException {
		Template temp = cfg.getTemplate("form.ftl");
		File dir = new File(dest_path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		OutputStream fos = new FileOutputStream(new File(dir, "form.jsp"));
		Writer out = new OutputStreamWriter(fos);
		temp.process(map, out);
		fos.flush();
		fos.close();
	}
	
	/**
	 * 生成index.jsp
	 * 
	 * @param cfg
	 * @param dest_path
	 * @param map
	 * @throws IOException
	 * @throws TemplateException
	 */
	private static void generateIndexJsp(Configuration cfg, String dest_path, Map<String, Object> map)
			throws IOException, TemplateException {
		Template temp = cfg.getTemplate("index.ftl");
		File dir = new File(dest_path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		OutputStream fos = new FileOutputStream(new File(dir, "index.jsp"));
		Writer out = new OutputStreamWriter(fos);
		temp.process(map, out);
		fos.flush();
		fos.close();

	}

	/**
	 * 生成后缀名为.java的文件
	 * 
	 * @param cfg
	 * @param dest_path
	 * @param className
	 * @param root
	 * @throws IOException
	 * @throws TemplateException
	 */
	private static void genarateJava(Configuration cfg, String dest_path, String className, Map<String, Object> root)
			throws IOException, TemplateException {
		Template temp = cfg.getTemplate("ctl.ftl");
		File dir = new File(dest_path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		OutputStream fos = new FileOutputStream(new File(dir, className + ".java"));
		Writer out = new OutputStreamWriter(fos);
		temp.process(root, out);
		fos.flush();
		fos.close();

	}

	/**
	 * 转换非首字母（从大变小）
	 * 
	 * @param table_name
	 * @return
	 */
	public static String exChange(String table_name) {
		StringBuffer sb = new StringBuffer();
		if (table_name != null) {
			for (int i = 0; i < table_name.length(); i++) {
				char c = table_name.charAt(i);
				if (i != 0) {
					if (Character.isUpperCase(c)) {
						sb.append(Character.toLowerCase(c));
					}
				} else {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}
}
