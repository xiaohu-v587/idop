package com.goodcol.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs2.FileNotFoundException;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.VFS;

import com.goodcol.core.upload.UploadFile;
import com.goodcol.util.date.BolusDate;

/**
 * 文件工具类
 * @author king
 */
public class FileTool {
	
	private static FileSystemManager fsManager = null;
	
	// 静态处理块
	static {
		try {
			fsManager = VFS.getManager();
		} catch (FileSystemException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 读文件转成string
	 * @param filePath 文件名
	 * @param encoding 字符编码
	 * @return
	 * @throws IOException
	 */
	public static String readFileToString(String filePath, String encoding)
			throws IOException {
		FileObject fileObj = null;
		InputStream in = null;

		try {
			fileObj = fsManager.resolveFile(filePath);
			if (fileObj.exists()) {
				if (FileType.FOLDER.equals(fileObj.getType())) {
					throw new IOException("File '" + filePath
							+ "' exists but is a directory");

				} else {
					in = fileObj.getContent().getInputStream();
					return IOUtils.toString(in, encoding);
				}
			} else {
				throw new FileNotFoundException("File '" + filePath
						+ "' does not exist");
			}
		} catch (FileSystemException e) {
			throw new IOException("File '" + filePath + "' resolveFile fail.");
		} finally {
			IOUtils.closeQuietly(in);
			if (fileObj != null) {
				fileObj.close();
			}
		}
	}
	
	/**
	 * 把string数据写入文件
	 * @param filePath 文件名称
	 * @param data 数据
	 * @param encoding 字符编码
	 * @throws IOException
	 */
	public static void writeDataToFile(String filePath, String data, String encoding) throws IOException {
        FileObject fileObj = null;
        OutputStream out = null;

        try {
            fileObj= fsManager.resolveFile(filePath);

            if (!fileObj.exists()) {
                fileObj.createFile();
            }else{
                if (FileType.FOLDER.equals(fileObj.getType())){
                    throw new IOException("Write fail. File '"+ filePath
                            +"' exists but is a directory");
                }
            }

            // 处理文件不可写的异常
            if(!fileObj.isWriteable()) {
                throw new IOException("Write fail. File '"+ filePath
                        +"' exists but isWriteable is false.");
            }

            out= fileObj.getContent().getOutputStream();
            IOUtils.write(data.getBytes(encoding), out);

        }catch(FileSystemException e) {
            throw new IOException("File '"+ filePath + "' resolveFile fail.");
        }finally{ 
            out.close(); 
            IOUtils.closeQuietly(out);
            if (fileObj != null) {
                fileObj.close();
            }
        }
    }
	
	/**
	 * 对文件进行copy
	 * @param sourceFilePath
	 *            源文件的全部路径+文件名
	 * @param targetFilePath
	 *            目标文件的全部路径+文件名
	 * @param overWrite
	 *            如果目标文件存在，是否覆盖。true:覆盖；false:不覆盖(当源文件和目标文件都存在并且不覆盖时,返回true)。
	 * @return true:成功；false:失败; (当源文件和目标文件都存在并且不覆盖时,返回true)。
	 */
	public static boolean copyFile(String sourceFilePath,
			String targetFilePath, boolean overWrite) throws IOException {
		if (StringUtils.isBlank(sourceFilePath)
				|| StringUtils.isBlank(targetFilePath)) {
			throw new IOException("源文件或者目标文件为空");
		}
		FileObject from = fsManager.resolveFile(sourceFilePath);
		FileObject to = fsManager.resolveFile(targetFilePath);
		if (to.exists()) {
			if (to.getType() == FileType.FILE) {
				if (overWrite && !to.delete()) {
					throw new IOException("目标文件[" + targetFilePath
							+ "]被保护，不能被覆盖！");
				} else if (!overWrite) {
					throw new IOException("目标文件[" + targetFilePath + "]已经存在！");
				}
			}
		}
		to.copyFrom(from, Selectors.SELECT_ALL);
		return true;
	}
	
	/** 
     * 移动文件
     * @param srcFile 
     *            源文件 eg: c:\windows\abc.txt 
     * @param targetFile 
     *            目标文件 eg: c:\temp\abc.txt 
     * @param overwrite 
     *            如果目标文件存在，是否覆盖 
     * @return success 
     */
    public static boolean moveFile(String srcFile, String targetFile, boolean overWrite) throws IOException {  
        if (srcFile.equals(targetFile)) {  
            return true;  
        }  
        FileObject src = fsManager.resolveFile(srcFile);  
        if (StringUtils.isNotBlank(srcFile) && !src.exists()) {  
            throw new IOException("源文件[" + srcFile + "]不存在");  
        }
        FileObject to = fsManager.resolveFile(targetFile);  
        if (to.exists()) {  
            if (to.getType() == FileType.FILE) {  
                if (overWrite && !to.delete()) {  
                    throw new IOException("目标文件[" + targetFile + "]被保护，不能被覆盖！");  
                } else if (!overWrite) {  
                    throw new IOException("目标文件[" + targetFile + "]已经存在！");  
                }  
            }  
        }  
        src.moveTo(to);  
        return true;  
    }
    
    /**
     * 生成上传文件相对路径
     * @param file
     * @return
     */
    public static String getUploadFilePath(UploadFile file){
    	String oldName = "";
    	String newName = "";
    	oldName = file.getFileName();
    	newName = BolusDate.getDate()+"/"+BolusDate.getDate()+BolusDate.getTime()+oldName.substring(oldName.indexOf("."));
    	String destName = Dict.Message_FILE_DIR +newName;   	
    	return destName;
    }
	
	/**
	 * 保存上传文件
	 * @param filePath
	 * @param file
	 */
	public static void saveFile(String filePath, UploadFile file){
		File dest = new File(filePath);
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		if (file != null) {
			file.getFile().renameTo(dest);
		}
	}
	
	/**
	 * 获取文件 支持http，ftp，本地文件
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static FileObject getFileObject(String filePath) throws Exception{
		FileObject fileObj = fsManager.resolveFile(filePath);
		return fileObj;
	}
	
	/**
	 * 获取ftp文件url
	 * @param user 用户
	 * @param pwd 地址
	 * @param hostName ip:21
	 * @param path 文件目录
	 * @return
	 */
	public static String getFtpFileUrl(String user,String pwd,String hostName,String path){		  
        if(pwd.indexOf("@")>=0){
        	pwd = pwd.replace("@", "%40");
        }
        String url = "ftp://"+user+":"+pwd+"@"+hostName+"/"+path;    
        return url;
	}
	
	/**
	 * 删除文件
	 * @param path
	 * @throws FileSystemException
	 */
	public static void delete(String path) throws FileSystemException{   
		FileObject fo = fsManager.resolveFile(path);  
        fo.delete();      
    }  
	
	/**
	 * 判断是否为目录
	 * @param path
	 * @return
	 * @throws FileSystemException
	 */
	public static boolean isDirectory(String path) throws FileSystemException {
		FileObject fo = fsManager.resolveFile(path);
		return fo.getType().equals(FileType.FOLDER);
	}
	
	/**
	 * 判断是否为文件
	 * @param path
	 * @return
	 * @throws FileSystemException
	 */
	public static boolean isFile(String path) throws FileSystemException {
		FileObject fo = fsManager.resolveFile(path);
		return fo.getType().equals(FileType.FILE);
	}
	
	public static void main(String[] args) throws Exception {

		
	}

	

}
