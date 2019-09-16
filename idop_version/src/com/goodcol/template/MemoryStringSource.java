
package com.goodcol.template;

import com.goodcol.core.Const;
import com.goodcol.kit.HashKit;
import com.goodcol.kit.StrKit;

/**
 * MemoryStringSource
 */
public class MemoryStringSource implements IStringSource {
	
	private String key;
	private StringBuilder content;
	
	public MemoryStringSource(String content) {
		if (StrKit.isBlank(content)) {
			throw new IllegalArgumentException("content can not be blank");
		}
		this.content = new StringBuilder(content);
		this.key = HashKit.md5(content);
	}
	
	public MemoryStringSource(StringBuilder content) {
		if (content == null || content.length() == 0) {
			throw new IllegalArgumentException("content can not be blank");
		}
		this.content = content;
		this.key = HashKit.md5(content.toString());
	}
	
	public String getKey() {
		return key;
	}
	
	public StringBuilder getContent() {
		return content;
	}
	
	public String getEncoding() {
		return Const.DEFAULT_ENCODING;
	}
}







