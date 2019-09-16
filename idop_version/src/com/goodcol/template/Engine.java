
package com.goodcol.template;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import com.goodcol.kit.HashKit;
import com.goodcol.kit.StrKit;
import com.goodcol.template.stat.Parser;
import com.goodcol.template.stat.ast.Stat;

/**
 * Engine
 * 
 * Example：
 * Engine.use().getTemplate(fileName).render(...);
 * Engine.use().getTemplate(fileName).renderToString(...);
 */
public class Engine {
	
	public static final String MAIN_ENGINE_NAME = "main";
	
	private static Engine MAIN_ENGINE;
	private static Map<String, Engine> engineMap = new HashMap<String, Engine>();
	
	// Create main engine
	static {
		MAIN_ENGINE = new Engine(MAIN_ENGINE_NAME);
		engineMap.put(MAIN_ENGINE_NAME, MAIN_ENGINE);
	}
	
	private String name;
	private boolean devMode = false;
	private EngineConfig config = new EngineConfig();
	
	private Map<String, Template> templateCache = new HashMap<String, Template>();
	
	/**
	 * Create engine without management of   
	 */
	public Engine() {
		this.name = "NO_NAME";
	}
	
	/**
	 * Create engine by engineName without management of   
	 */
	public Engine(String engineName) {
		this.name = engineName;
	}
	
	/**
	 * Using the main Engine
	 */
	public static Engine use() {
		return MAIN_ENGINE;
	}
	
	/**
	 * Using the engine with engine name
	 */
	public static Engine use(String engineName) {
		return engineMap.get(engineName);
	}
	
	/**
	 * Create engine with engine name managed by  
	 */
	public synchronized static Engine create(String engineName) {
		if (StrKit.isBlank(engineName)) {
			throw new IllegalArgumentException("Engine name can not be blank");
		}
		engineName = engineName.trim();
		if (engineMap.containsKey(engineName)) {
			throw new IllegalArgumentException("Engine already exists : " + engineName);
		}
		Engine newEngine = new Engine(engineName);
		engineMap.put(engineName, newEngine);
		return newEngine;
	}
	
	/**
	 * Remove engine with engine name managed by  
	 */
	public synchronized static Engine remove(String engineName) {
		Engine removed = engineMap.remove(engineName);
		if (removed != null && MAIN_ENGINE_NAME.equals(removed.name)) {
			Engine.MAIN_ENGINE = null;
		}
		return removed;
	}
	
	/**
	 * Set main engine
	 */
	public synchronized static void setMainEngine(Engine engine) {
		if (engine == null) {
			throw new IllegalArgumentException("Engine can not be null");
		}
		engine.name = Engine.MAIN_ENGINE_NAME;
		engineMap.put(Engine.MAIN_ENGINE_NAME, engine);
		Engine.MAIN_ENGINE = engine;
	}
	
	/**
	 * Get template with file name
	 */
	public Template getTemplate(String fileName) {
		if (fileName.charAt(0) != '/') {
			char[] arr = new char[fileName.length() + 1];
			fileName.getChars(0, fileName.length(), arr, 1);
			arr[0] = '/';
			fileName = new String(arr);
		}
		
		Template template = templateCache.get(fileName);
		if (template == null) {
			template = buildTemplateByFileStringSource(fileName);
			templateCache.put(fileName, template);
		} else if (devMode) {
			if (template.isModified()) {
				template = buildTemplateByFileStringSource(fileName);
				templateCache.put(fileName, template);
			}
		}
		return template;
	}
	
	private Template buildTemplateByFileStringSource(String fileName) {
		FileStringSource fileStringSource = new FileStringSource(config.getBaseTemplatePath(), fileName, config.getEncoding());
		Env env = new Env(config);
		if (devMode) {
			env.addTemplateFinalFileName(fileStringSource.getFinalFileName());
		}
		Parser parser = new Parser(env, fileStringSource.getContent(), fileName);
		Stat stat = parser.parse();
		Template template = new Template(env, stat);
		return template;
	}
	
	/**
	 * Get template by string content
	 */
	public Template getTemplateByString(String content) {
		if (devMode) {
			return buildTemplateByStringSource(new MemoryStringSource(content));
		}
		
		String key = HashKit.md5(content);
		Template template = templateCache.get(key);
		if (template == null) {
			template = buildTemplateByStringSource(new MemoryStringSource(content));
			templateCache.put(key, template);
		}
		return template;
	}
	
	/**
	 * Get template with implementation of IStringSource
	 */
	public Template getTemplate(IStringSource stringSource) {
		if (devMode) {
			return buildTemplateByStringSource(stringSource);
		}
		
		String key = stringSource.getKey();
		Template template = templateCache.get(key);
		if (template == null) {
			template = buildTemplateByStringSource(stringSource);
			templateCache.put(key, template);
		}
		return template;
	}
	
	private Template buildTemplateByStringSource(IStringSource stringSource) {
		Env env = new Env(config);
		Parser parser = new Parser(env, stringSource.getContent(), null);
		Stat stat = parser.parse();
		Template template = new Template(env, stat);
		return template;
	}
	
	/**
	 * Add shared function with file
	 */
	public Engine addSharedFunction(String fileName) {
		config.addSharedFunction(fileName);
		return this;
	}
	
	/**
	 * Add shared function with files
	 */
	public Engine addSharedFunction(String... fileNames) {
		config.addSharedFunction(fileNames);
		return this;
	}
	
	/**
	 * Add shared function by string content
	 */
	public Engine addSharedFunctionByString(String content) {
		config.addSharedFunctionByString(content);
		return this;
	}
	
	/**
	 * Add shared object
	 */
	public Engine addSharedObject(String name, Object object) {
		config.addSharedObject(name, object);
		return this;
	}
	
	/**
	 * Set output directive factory
	 */
	public Engine setOutputDirectiveFactory(IOutputDirectiveFactory outputDirectiveFactory) {
		config.setOutputDirectiveFactory(outputDirectiveFactory);
		return this;
	}
	
	/**
	 * Add directive
	 */
	public Engine addDirective(String directiveName, Directive directive) {
		config.addDirective(directiveName, directive);
		return this;
	}
	
	/**
	 * Remove directive
	 */
	public Engine removeDirective(String directiveName) {
		config.removeDirective(directiveName);
		return this;
	}
	
	/**
	 * Add shared method from object
	 */
	public Engine addSharedMethod(Object sharedMethodFromObject) {
		config.addSharedMethod(sharedMethodFromObject);
		return this;
	}
	
	/**
	 * Add shared static method of Class
	 */
	public Engine addSharedStaticMethod(Class<?> sharedClass) {
		config.addSharedStaticMethod(sharedClass);
		return this;
	}
	
	/**
	 * Remove shared Method with method name
	 */
	public Engine removeSharedMethod(String methodName) {
		config.removeSharedMethod(methodName);
		return this;
	}
	
	/**
	 * Remove shared Method of the Class
	 */
	public Engine removeSharedMethod(Class<?> clazz) {
		config.removeSharedMethod(clazz);
		return this;
	}
	
	/**
	 * Remove shared Method
	 */
	public Engine removeSharedMethod(Method method) {
		config.removeSharedMethod(method);
		return this;
	}
	
	/**
	 * Remove template cache with template key
	 */
	public void removeTemplateCache(String templateKey) {
		templateCache.remove(templateKey);
	}
	
	/**
	 * Remove all template cache
	 */
	public void removeAllTemplateCache() {
		templateCache.clear();
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return "Template Engine: " + name;
	}
	
	// Engine config below ---------
	
	public EngineConfig  getEngineConfig() {
		return config;
	}
	
	public Engine setDevMode(boolean devMode) {
		this.devMode = devMode;
		this.config.setDevMode(devMode);
		if (this.devMode) {
			removeAllTemplateCache();
		}
		return this;
	}
	
	public boolean getDevMode() {
		return devMode;
	}
	
	public Engine setBaseTemplatePath(String baseTemplatePath) {
		config.setBaseTemplatePath(baseTemplatePath);
		return this;
	}
	
	public String getBaseTemplatePath() {
		return config.getBaseTemplatePath();
	}
	
	public Engine setDatePattern(String datePattern) {
		config.setDatePattern(datePattern);
		return this;
	}
	
	public String getDatePattern() {
		return config.getDatePattern();
	}
	
	public Engine setEncoding(String encoding) {
		config.setEncoding(encoding);
		return this;
	}
	
	public String getEncoding() {
		return config.getEncoding();
	}
	
	public Engine setReloadModifiedSharedFunctionInDevMode(boolean reloadModifiedSharedFunctionInDevMode) {
		config.setReloadModifiedSharedFunctionInDevMode(reloadModifiedSharedFunctionInDevMode);
		return this;
	}
}





