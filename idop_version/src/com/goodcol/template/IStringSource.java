
package com.goodcol.template;

/**
 * IStringSource
 */
public interface IStringSource {
	
	/**
	 * key used to cache
	 */
	String getKey();
	
	/**
	 * content of StringSource
	 */
	StringBuilder getContent();
	
	/**
	 * encoding of content
	 */
	String getEncoding();
}


