
package com.goodcol.config;

import java.io.File;
import java.util.Properties;
import com.goodcol.core.Const;
import com.goodcol.kit.Prop;
import com.goodcol.template.Engine;

/**
 * Gcds.
 * <p>
 * Config order: configConstant(), configRoute(), configEngine(), configPlugin(), configInterceptor(), configHandler()
 */
public abstract class GcdsConfig {
	
	/**
	 * Config constant
	 */
	public abstract void configConstant(Constants me);
	
	/**
	 * Config route
	 */
	public abstract void configRoute(Routes me);
	
	/**
	 * Config engine
	 */
	public abstract void configEngine(Engine me);
	
	/**
	 * Config plugin
	 */
	public abstract void configPlugin(Plugins me);
	
	/**
	 * Config interceptor applied to all actions.
	 */
	public abstract void configInterceptor(Interceptors me);
	
	/**
	 * Config handler
	 */
	public abstract void configHandler(Handlers me);
	
	/**
	 * Call back after Gcds start
	 */
	public void afterGcdsStart(){};
	
	/**
	 * Call back before Gcds stop
	 */
	public void beforeGcdsStop(){};
	
	protected Prop prop = null;
	
	/**
	 * Load property file.
	 * @see #loadPropertyFile(String, String)
	 */
	public Properties loadPropertyFile(String fileName) {
		return loadPropertyFile(fileName, Const.DEFAULT_ENCODING);
	}
	
	/**
	 * Load property file.
	 * Example:<br>
	 * loadPropertyFile("db_username_pass.txt", "UTF-8");
	 * 
	 * @param fileName the file in CLASSPATH or the sub directory of the CLASSPATH
	 * @param encoding the encoding
	 */
	public Properties loadPropertyFile(String fileName, String encoding) {
		prop = new Prop(fileName, encoding);
		return prop.getProperties();
	}
	
	/**
	 * Load property file.
	 * @see #loadPropertyFile(File, String)
	 */
	public Properties loadPropertyFile(File file) {
		return loadPropertyFile(file, Const.DEFAULT_ENCODING);
	}
	
	/**
	 * Load property file
	 * Example:<br>
	 * loadPropertyFile(new File("/var/config/my_config.txt"), "UTF-8");
	 * 
	 * @param file the properties File object
	 * @param encoding the encoding
	 */
	public Properties loadPropertyFile(File file, String encoding) {
		prop = new Prop(file, encoding);
		return prop.getProperties();
	}
	
	public void unloadPropertyFile() {
		this.prop = null;
	}
	
	private Prop getProp() {
		if (prop == null) {
			throw new IllegalStateException("Load propties file by invoking loadPropertyFile(String fileName) method first.");
		}
		return prop;
	}
	
	public String getProperty(String key) {
		return getProp().get(key);
	}
	
	public String getProperty(String key, String defaultValue) {
		return getProp().get(key, defaultValue);
	}
	
	public Integer getPropertyToInt(String key) {
		return getProp().getInt(key);
	}
	
	public Integer getPropertyToInt(String key, Integer defaultValue) {
		return getProp().getInt(key, defaultValue);
	}
	
	public Long getPropertyToLong(String key) {
		return getProp().getLong(key);
	}
	
	public Long getPropertyToLong(String key, Long defaultValue) {
		return getProp().getLong(key, defaultValue);
	}
	
	public Boolean getPropertyToBoolean(String key) {
		return getProp().getBoolean(key);
	}
	
	public Boolean getPropertyToBoolean(String key, Boolean defaultValue) {
		return getProp().getBoolean(key, defaultValue);
	}
}







