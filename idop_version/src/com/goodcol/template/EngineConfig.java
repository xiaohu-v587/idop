
package com.goodcol.template;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.goodcol.core.Const;
import com.goodcol.kit.StrKit;
import com.goodcol.template.expr.ast.ExprList;
import com.goodcol.template.expr.ast.SharedMethodKit;
import com.goodcol.template.stat.Location;
import com.goodcol.template.stat.Parser;
import com.goodcol.template.stat.ast.Define;
import com.goodcol.template.stat.ast.Output;
import com.goodcol.template.stat.ast.Stat;

/**
 * EngineConfig
 */
public class EngineConfig {
	
	private Map<String, Define> sharedFunctionMap = new HashMap<String, Define>();
	private List<SharedFunctionFile> sharedFunctionFiles = new ArrayList<SharedFunctionFile>();	// for devMode only
	private Map<String, Define> sharedFunctionByString = new HashMap<String, Define>();			// for devMode only
	
	Map<String, Object> sharedObjectMap = null;
	
	private IOutputDirectiveFactory outputDirectiveFactory = OutputDirectiveFactory.me;
	private Map<String, Stat> directiveMap = new HashMap<String, Stat>();
	private SharedMethodKit sharedMethodKit = new SharedMethodKit();
	
	private boolean devMode = false;
	private boolean reloadModifiedSharedFunctionInDevMode = true;
	private String baseTemplatePath = null;
	private String encoding = Const.DEFAULT_ENCODING;
	private String datePattern = "yyyy-MM-dd HH:mm";
	
	public EngineConfig() {
		// Add official directive of Template Engine
		addDirective("escape", new com.goodcol.template.ext.directive.Escape());
		addDirective("date", new com.goodcol.template.ext.directive.Date());
		addDirective("string", new com.goodcol.template.ext.directive.Str());
		addDirective("random", new com.goodcol.template.ext.directive.Random());
		
		// Add official shared method of Template Engine
		addSharedMethod(new com.goodcol.template.ext.sharedmethod.Json());
	}
	
	/**
	 * Add shared function with file
	 */
	public synchronized void addSharedFunction(String fileName) {
		FileStringSource fileStringSource = new FileStringSource(baseTemplatePath, fileName, encoding);
		Env env = new Env(this);
		new Parser(env, fileStringSource.getContent(), fileName).parse();
		addToSharedFunctionMap(sharedFunctionMap, env);
		if (devMode) {
			SharedFunctionFile sff = new SharedFunctionFile(fileName, fileStringSource.getFinalFileName());
			sharedFunctionFiles.add(sff);
		}
	}
	
	/**
	 * Add shared function with files
	 */
	public void addSharedFunction(String... fileNames) {
		for (String fileName : fileNames) {
			addSharedFunction(fileName);
		}
	}
	
	/**
	 * Add shared function by string content
	 */
	public synchronized void addSharedFunctionByString(String content) {
		MemoryStringSource memoryStringSource = new MemoryStringSource(content);
		Env env = new Env(this);
		new Parser(env, memoryStringSource.getContent(), null).parse();
		addToSharedFunctionMap(sharedFunctionMap, env);
		if (devMode) {
			for (Entry<String, Define> e : env.getFunctionMap().entrySet()) {
				sharedFunctionByString.put(e.getKey(), e.getValue());
			}
		}
	}
	
	private void addToSharedFunctionMap(Map<String, Define> sharedFunctionMap, Env env) {
		Map<String, Define> funcMap = env.getFunctionMap();
		for (Entry<String, Define> e : funcMap.entrySet()) {
			if (sharedFunctionMap.containsKey(e.getKey())) {
				throw new IllegalArgumentException("Template function already exists : " + e.getKey());
			}
			sharedFunctionMap.put(e.getKey(), e.getValue());
		}
	}
	
	/**
	 * Get shared function by Env
	 */
	Define getSharedFunction(String functionName) {
		if (devMode && reloadModifiedSharedFunctionInDevMode) {
			if (isSharedFunctionFileModified()) {
				synchronized (this) {
					if (isSharedFunctionFileModified()) {
						reloadSharedFunctionFiles();
					}
				}
			}
		}
		return sharedFunctionMap.get(functionName);
	}
	
	private boolean isSharedFunctionFileModified() {
		for (int i=0, size=sharedFunctionFiles.size(); i<size; i++) {
			if (sharedFunctionFiles.get(i).isModified()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Reload shared function file if modified
	 * 
	 * devMode 要照顾到 sharedFunctionFiles，所以暂不提供
	 * removeSharedFunction(String functionName) 功能
	 * 开发者可直接使用模板注释功能将不需要的 function 直接注释掉
	 */
	private void reloadSharedFunctionFiles() {
		Map<String, Define> newMap = new HashMap<String, Define>();
		newMap.putAll(sharedFunctionByString);
		for (int i=0, size=sharedFunctionFiles.size(); i<size; i++) {
			SharedFunctionFile sff = sharedFunctionFiles.get(i);
			sff.updateLastModified();
			FileStringSource fileStringSource = new FileStringSource(baseTemplatePath, sff.fileName, encoding);
			Env env = new Env(this);
			new Parser(env, fileStringSource.getContent(), sff.fileName).parse();
			addToSharedFunctionMap(newMap, env);
		}
		sharedFunctionMap = newMap;
	}
	
	public synchronized void addSharedObject(String name, Object object) {
		if (sharedObjectMap == null) {
			sharedObjectMap = new HashMap<String, Object>();
		} else if (sharedObjectMap.containsKey(name)) {
			throw new IllegalArgumentException("Shared object already exists: " + name);
		}
		sharedObjectMap.put(name, object);
	}
	
	Map<String, Object> getSharedObjectMap() {
		return sharedObjectMap;
	}
	
	/**
	 * Set output directive factory
	 */
	public void setOutputDirectiveFactory(IOutputDirectiveFactory outputDirectiveFactory) {
		if (outputDirectiveFactory == null) {
			throw new IllegalArgumentException("outputDirectiveFactory can not be null");
		}
		this.outputDirectiveFactory = outputDirectiveFactory;
	}
	
	public Output getOutputDirective(ExprList exprList, Location location) {
		return outputDirectiveFactory.getOutputDirective(exprList, location);
	}
	
	/**
	 * Invoked by Engine only
	 */
	void setDevMode(boolean devMode) {
		this.devMode = devMode;
	}
	
	public boolean isDevMode() {
		return devMode;
	}
	
	public void setBaseTemplatePath(String baseTemplatePath) {
		if (StrKit.isBlank(baseTemplatePath)) {
			throw new IllegalArgumentException("baseTemplatePath can not be blank");
		}
		baseTemplatePath = baseTemplatePath.trim();
		if (baseTemplatePath.length() > 1) {
			if (baseTemplatePath.endsWith("/") || baseTemplatePath.endsWith("\\")) {
				baseTemplatePath = baseTemplatePath.substring(0, baseTemplatePath.length() - 1);
			}
		}
		this.baseTemplatePath = baseTemplatePath;
	}
	
	public String getBaseTemplatePath() {
		return baseTemplatePath;
	}
	
	public void setEncoding(String encoding) {
		if (StrKit.isBlank(encoding)) {
			throw new IllegalArgumentException("encoding can not be blank");
		}
		this.encoding = encoding;
	}
	
	public String getEncoding() {
		return encoding;
	}
	
	public void setDatePattern(String datePattern) {
		if (StrKit.isBlank(datePattern)) {
			throw new IllegalArgumentException("datePattern can not be blank");
		}
		this.datePattern = datePattern;
	}
	
	public String getDatePattern() {
		return datePattern;
	}
	
	public void setReloadModifiedSharedFunctionInDevMode(boolean reloadModifiedSharedFunctionInDevMode) {
		this.reloadModifiedSharedFunctionInDevMode = reloadModifiedSharedFunctionInDevMode;
	}
	
	public synchronized void addDirective(String directiveName, Directive directive) {
		if (StrKit.isBlank(directiveName)) {
			throw new IllegalArgumentException("directive name can not be blank");
		}
		if (directive == null) {
			throw new IllegalArgumentException("directive can not be null");
		}
		if (directiveMap.containsKey(directiveName)) {
			throw new IllegalArgumentException("directive already exists : " + directiveName);
		}
		directiveMap.put(directiveName, directive);
	}
	
	public Stat getDirective(String directiveName) {
		return directiveMap.get(directiveName);
	}
	
	public void removeDirective(String directiveName) {
		directiveMap.remove(directiveName);
	}
	
	/**
	 * Add shared method from object
	 */
	public void addSharedMethod(Object sharedMethodFromObject) {
		sharedMethodKit.addSharedMethod(sharedMethodFromObject);
	}
	
	/**
	 * Add shared static method of Class
	 */
	public void addSharedStaticMethod(Class<?> sharedClass) {
		sharedMethodKit.addSharedStaticMethod(sharedClass);
	}
	
	/**
	 * Remove shared Method with method name
	 */
	public void removeSharedMethod(String methodName) {
		sharedMethodKit.removeSharedMethod(methodName);
	}
	
	/**
	 * Remove shared Method of the Class
	 */
	public void removeSharedMethod(Class<?> sharedClass) {
		sharedMethodKit.removeSharedMethod(sharedClass);
	}
	
	/**
	 * Remove shared Method
	 */
	public void removeSharedMethod(Method method) {
		sharedMethodKit.removeSharedMethod(method);
	}
	
	public SharedMethodKit getSharedMethodKit() {
		return sharedMethodKit;
	}
	
	private static class SharedFunctionFile {
		private String fileName;
		private String finalFileName;
		private long lastModified;
		
		SharedFunctionFile(String fileName, String finalFileName) {
			this.fileName = fileName;
			this.finalFileName = finalFileName;
			this.lastModified = new File(finalFileName).lastModified();
		}
		
		boolean isModified() {
			return lastModified != new File(finalFileName).lastModified();
		}
		
		void updateLastModified() {
			this.lastModified = new File(finalFileName).lastModified();
		}
		
		public String toString() {
			return "finalFileName : " + finalFileName + "\nlastModified : " + lastModified;
		}
	}
}





