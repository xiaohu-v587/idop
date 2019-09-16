
package com.goodcol.upload;

import java.io.File;
import com.goodcol.kit.LogKit;
import com.goodcol.kit.PathKit;
import com.goodcol.kit.StrKit;
import com.oreilly.servlet.multipart.FileRenamePolicy;

/**
 * OreillyCos.
 */
public class OreillyCos {
	
	public static void init(String uploadPath, int maxPostSize, String encoding) {
		if (StrKit.isBlank(uploadPath)) {
			throw new IllegalArgumentException("uploadPath can not be blank.");
		}
		try {
			Class.forName("com.oreilly.servlet.MultipartRequest");
			doInit(uploadPath, maxPostSize, encoding);
		} catch (ClassNotFoundException e) {
			LogKit.logNothing(e);
		}
	}
	
	public static void setFileRenamePolicy(FileRenamePolicy fileRenamePolicy) {
		if (fileRenamePolicy == null) {
			throw new IllegalArgumentException("fileRenamePolicy can not be null.");
		}
		MultipartRequest.fileRenamePolicy = fileRenamePolicy;
	}
	
	private static void doInit(String uploadPath, int maxPostSize, String encoding) {
		uploadPath = uploadPath.trim();
		uploadPath = uploadPath.replaceAll("\\\\", "/");
		
		String baseUploadPath;
		if (PathKit.isAbsolutelyPath(uploadPath)) {
			baseUploadPath = uploadPath;
		} else {
			baseUploadPath = PathKit.getWebRootPath() + File.separator + uploadPath;
		}
		
		// remove "/" postfix
		if (baseUploadPath.equals("/") == false) {
			if (baseUploadPath.endsWith("/")) {
				baseUploadPath = baseUploadPath.substring(0, baseUploadPath.length() - 1);
			}
		}
		
		MultipartRequest.init(baseUploadPath, maxPostSize, encoding);
	}
}


