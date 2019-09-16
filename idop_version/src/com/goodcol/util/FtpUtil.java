package com.goodcol.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

public class FtpUtil {
   private static Logger logger=Logger.getLogger(FtpUtil.class);
   private static FTPClient ftp;
   /**
    * 获取ftp连接
    * 
    */
   public  static  boolean connectFtp(Ftp f) throws Exception{
	   /*******************连接服务器*******************/
		ftp=new FTPClient();
       boolean flag=false;
       int reply;
       if(f.getPort()==null){
       	ftp.connect(f.getIpAddr(), 21);
       }else{
       	ftp.connect(f.getIpAddr(),f.getPort());
       }
       ftp.login(f.getUsername(), f.getPwd());
       ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
       reply=ftp.getReplyCode();
       if(!FTPReply.isPositiveCompletion(reply)){
       	ftp.disconnect();
       	return flag;
       }
		boolean fl=ftp.changeWorkingDirectory(f.getPath());
		flag=true;
	return flag;
   }
   
 //关闭ftp连接
 	public static void closeFtp(){
 		if(ftp !=null && ftp.isConnected()){
 			try {
 				ftp.logout();
 				ftp.disconnect();
 			} catch (Exception e) {
 				// TODO: handle exception
 			}
 		}
 	}
 	

	//ftp上传文件
	public static void upload(File f)throws Exception{
		if(f.isDirectory()){
			ftp.makeDirectory(f.getName());
			ftp.changeWorkingDirectory(f.getName());
			String[] files=f.list();
			for(String fstr:files){
				File fi=new File(f.getPath()+"/"+fstr);
				if(fi.isDirectory()){
					upload(fi);
					ftp.changeToParentDirectory();
				}else{
					File fii=new File(f.getPath()+"/"+fstr);
					FileInputStream input=new FileInputStream(fii);
					ftp.storeFile(fii.getName(), input);
					input.close();
				}
			}
		}else{
			File fii=new File(f.getPath());
			FileInputStream input=new FileInputStream(fii);
			ftp.storeFile(fii.getName(), input);
			input.close();
		}
	}
	//下载连接配置
	public static boolean startDown(Ftp f ,String localBaseDir ,String remoteBaseDir,String downloadfilename) throws Exception{
		boolean flag=false;
		if(FtpUtil.connectFtp(f)){
			try {
				FTPFile[] files=null;
				boolean changedir=ftp.changeWorkingDirectory(remoteBaseDir);
				if(changedir){
					ftp.setControlEncoding("GBK");
					files=ftp.listFiles();
					for(int i=0;i<files.length;i++){
						try {
							if(files[i].getName().equals(downloadfilename)){
								downloadFile(files[i],localBaseDir,remoteBaseDir);
								flag=true;
							}
						} catch (Exception e) {
							logger.error(e);
							logger.error("<"+files[i].getName()+">下载失败");
						}
					}
				}
				
			} catch (Exception e) {
				logger.error(e);
				logger.error("下载过程中出现异常");
			}
		}else{
			logger.error("连接失败！");
		}
		return flag;
	}
	/**
	 * 下载ftp文件
	 * 获取文件名，本地地址，远程地址，进行下载
	 */
	public static void downloadFile(FTPFile ftpFile ,String relativeLocationPath,String relativeRemotePath){
		if(ftpFile.isFile()){
			if(ftpFile.getName().indexOf("?")==-1){
				OutputStream outputStream=null;
				try {
					//判断文件夹是否存在
					File filepath=new File(relativeLocationPath);
					if(!filepath.exists()){
						filepath.mkdir();
					}
					File locaFile=new File(relativeLocationPath+ftpFile.getName());
					//判断文件是否存在，存在则返回
					if(locaFile.exists()){
						return;
					}else{
						outputStream=new FileOutputStream(relativeLocationPath+ftpFile.getName());
						ftp.retrieveFile(ftpFile.getName(), outputStream);
						outputStream.flush();
						outputStream.close();
					}
				} catch (Exception e) {
					logger.error(e);
				}finally{
					try {
						if(outputStream !=null){
							outputStream.close();
						}
					} catch (Exception e2) {
						logger.error("输出文件流异常");
					}
				}
			}
		}else{
			String newLocationPath=relativeLocationPath+ftpFile.getName();
			String newRemote= new String(relativeLocationPath+ftpFile.getName().toString());
			File fs=new File(newLocationPath);
			if(!fs.exists()){
				fs.mkdirs();
			}
			try {
				newLocationPath=newLocationPath+"/";
				newRemote=newRemote+"/";
				String currentWorkDir=ftpFile.getName().toString();
				boolean changedir=ftp.changeWorkingDirectory(currentWorkDir);
				if(changedir){
					FTPFile[] ftpfiles=null;
					ftpfiles=ftp.listFiles();
					for(int i=0;i<ftpfiles.length;i++){
						downloadFile(ftpfiles[i],newLocationPath,newRemote);
					}
					ftp.changeToParentDirectory();
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}
}
