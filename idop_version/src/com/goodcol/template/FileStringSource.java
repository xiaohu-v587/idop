
package com.goodcol.template;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import com.goodcol.core.Const;

/**
 * FileStringSource
 */
public class FileStringSource implements IStringSource {
	
	private String finalFileName;
	private String fileName;
	private String encoding;
	
	public FileStringSource(String baseTemplatePath, String fileName, String encoding) {
		this.finalFileName = buildFinalFileName(baseTemplatePath, fileName);
		this.fileName = fileName;
		this.encoding= encoding;
	}
	
	public FileStringSource(String baseTemplatePath, String fileName) {
		this(baseTemplatePath, fileName, Const.DEFAULT_ENCODING);
	}
	
	public String getKey() {
		return fileName;
	}
	
	public StringBuilder getContent() {
		return loadFile(finalFileName, encoding);
	}
	
	public String getEncoding() {
		return encoding;
	}
	
	public String getFinalFileName() {
		return finalFileName;
	}
	
	private String buildFinalFileName(String baseTemplatePath, String fileName) {
		char firstChar = fileName.charAt(0);
		String finalFileName;
		if (firstChar == '/' || firstChar == '\\') {
			finalFileName = baseTemplatePath + fileName;
		} else {
			finalFileName = baseTemplatePath + File.separator + fileName;
		}
		return finalFileName;
	}
	
	public static StringBuilder loadFile(String fileName, String encoding) {
		File file = new File(fileName);
		if (!file.exists()) {
			throw new RuntimeException("File not found : " + fileName);
		}
		
		StringBuilder ret = new StringBuilder((int)file.length() + 3);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
			// br = new BufferedReader(new FileReader(fileName));
			String line = br.readLine();
			if (line != null) {
				ret.append(line);
			} else {
				return ret;
			}
			
			while ((line=br.readLine()) != null) {
				ret.append("\n").append(line);
			}
			return ret;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					com.goodcol.kit.LogKit.error(e.getMessage(), e);
				}
			}
		}
	}
}




