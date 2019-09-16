package com.goodcol.ftp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.regex.Matcher;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
//import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;

public class FTPClientTool {

	// private boolean isAnonymous;
	private String host;
	private int port;
	private String userName;
	private String userPass;
	private boolean isConnected;
	private boolean isLogined;
	private String encoding;

	private int downloadBufSize = 8 * 1024;// default, min 1 max 1024 * 1024;
	private int uploadBufSize = 10 * 1024;// default, min 1 max 1024 * 1024;

	public static final String ISO = "iso-8859-1";
	

	final static int FTP_TIMEOUT = 10000;

	private FTPClient ftpClient;

	public FTPClientTool(String host, int port, String userName, String userPass, String encoding) {
		this.host = host;
		this.port = port;
		this.userName = userName;
		this.userPass = userPass;
		this.isConnected = false;
		this.isLogined = false;
		this.encoding = encoding;

		ftpClient = new FTPClient();
		//ftpClient.setConnectTimeout(FTP_TIMEOUT);
		ftpClient.setDataTimeout(FTP_TIMEOUT);
		ftpClient.setDefaultTimeout(FTP_TIMEOUT);
		ftpClient.setControlEncoding(this.encoding);
	}

	/**
	 * connect to ftpserver
	 * 
	 * @return
	 */
	public boolean connect() {
		try {
			ftpClient.connect(this.host, this.port);
			this.isConnected = FTPReply.isPositiveCompletion(ftpClient
					.getReplyCode());
			ftpClient.setSoTimeout(FTP_TIMEOUT);
		} catch (SocketException e) {
			this.isConnected = false;
		} catch (IOException e) {
			this.isConnected = false;
		}
		return this.isConnected;
	}

	public boolean renameRemoteFile(String fromFileName, String toFileName) {
		boolean ret = false;
		try {
			System.out.println("fromFileName: " + fromFileName + ", toFileName: " + toFileName);
			ret = this.ftpClient.rename(fromFileName, toFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * disconnect from ftpserver
	 */
	public void disConnect() {
		try {
			if (this.ftpClient.isConnected())
				this.ftpClient.disconnect();
		} catch (IOException e) {
		}
		this.isConnected = false;
	}

	/**
	 * login to ftpserver
	 * 
	 * @return
	 */
	public boolean login() {
		if (!this.isConnected)
			return false;

		try {
			if (this.userName.equals("") && this.userPass.equals(""))
				return false;
			isLogined = ftpClient.login(this.userName, this.userPass);
			return isLogined;
		} catch (IOException e) {
		}
		return false;
	}

	/**
	 * logout from ftpserver
	 * 
	 * @return
	 */
	public boolean logout() {
		if (!this.isConnected || !this.ftpClient.isConnected())
			return false;
		try {
			this.isLogined = !this.ftpClient.logout();
			return this.isLogined;
		} catch (IOException e) {
		}
		return false;
	}

	/**
	 * download file from ftpserver
	 * <p>
	 * 1.check remote file if exists
	 * <p>
	 * 2.if Support download from Breakpoint, check the file if downloaded or
	 * downloaded partially, if downloaded partially, download from the
	 * breakpoint
	 * <p>
	 * 3.if not support download from breakpoint, download the file
	 * <p>
	 * 
	 * @param remoteFile
	 *            absolute file path at ftpserver
	 * @param localFile
	 *            absolute file path at local disk
	 * @param isSupportBreakpoint
	 *            true to support download file from breakpoint, false to
	 *            override the file if existed
	 * @return ftpdownloadstatus
	 */
	public FtpDownloadStatus download(String remoteFileName,
			String localFileName, boolean isSupportBreakpoint) {
		FtpDownloadStatus downloadStat = FtpDownloadStatus.DOWNLOAD_UNKNOWN;
		try {
			FTPFile remoteFtpFile = getRemoteFile(remoteFileName);
			if (remoteFtpFile != null) {
				long remoteFileSize = remoteFtpFile.getSize();
				File localFile = new File(localFileName);
				if (localFile.exists() && isSupportBreakpoint) {
					long localFileSize = localFile.length();
					if (localFileSize >= remoteFileSize) {
						downloadStat = FtpDownloadStatus.DOWNLOAD_FILE_DOWNLOADED;
					} else {
						downloadStat = downloadFile(remoteFtpFile,
								remoteFileName, localFile, localFileSize);
					}
				} else {
					downloadStat = downloadFile(remoteFtpFile, remoteFileName,
							localFile, 0);
				}
			} else {
				downloadStat = FtpDownloadStatus.DOWNLOAD_REMOTE_FILE_NOT_EXIST;
			}
		} catch (Exception e) {
		}

		return downloadStat;
	}

	/**
	 * write remote file content to local file
	 * 
	 * @param remoteFtpFile
	 * @param remoteFileName
	 * @param localFile
	 * @param offset
	 * @return
	 */
	private FtpDownloadStatus downloadFile(FTPFile remoteFtpFile,
			String remoteFileName, File localFile, long offset) {
		FtpDownloadStatus downloadStat = FtpDownloadStatus.DOWNLOAD_UNKNOWN;

		if (remoteFileName != null && localFile != null) {
			OutputStream outputStream = null;
			InputStream inputStream = null;
			try {
				if (offset == 0) {
					outputStream = new FileOutputStream(localFile, false);
				} else {
					outputStream = new FileOutputStream(localFile, true);
				}
				this.ftpClient.enterLocalPassiveMode();
				this.ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				this.ftpClient.setRestartOffset(offset);

				inputStream = this.ftpClient.retrieveFileStream(remoteFileName);

				byte[] buf = new byte[this.downloadBufSize];// 8k per read
				int c = 0;
				while ((c = inputStream.read(buf)) != -1) {
					outputStream.write(buf, 0, c);
				}
				outputStream.flush();
				downloadStat = ftpClient.completePendingCommand() ? (offset == 0 ? FtpDownloadStatus.DOWNLOAD_SUCC_NEW
						: FtpDownloadStatus.DOWNLOAD_SUCC_BREAK)
						: (offset == 0 ? FtpDownloadStatus.DOWNLOAD_FAIL_NEW
								: FtpDownloadStatus.DOWNLOAD_FAIL_BREAK);
			} catch (FileNotFoundException e) {
				downloadStat = (offset == 0 ? FtpDownloadStatus.DOWNLOAD_FAIL_NEW
						: FtpDownloadStatus.DOWNLOAD_FAIL_BREAK);
			} catch (SocketTimeoutException e) {
				downloadStat = (offset == 0 ? FtpDownloadStatus.DOWNLOAD_FAIL_TIMEOUT_NEW
						: FtpDownloadStatus.DOWNLOAD_FAIL_TIMEOUT_BREAK);
			} catch (IOException e) {
				downloadStat = (offset == 0 ? FtpDownloadStatus.DOWNLOAD_FAIL_IOEXCEPTION_NEW
						: FtpDownloadStatus.DOWNLOAD_FAIL_IOEXCEPTION_BREAK);
			} catch (Exception e) {
				downloadStat = (offset == 0 ? FtpDownloadStatus.DOWNLOAD_FAIL_NEW
						: FtpDownloadStatus.DOWNLOAD_FAIL_BREAK);
			} finally {
				if (outputStream != null) {
					try {
						outputStream.close();
						outputStream = null;
					} catch (IOException e) {
						downloadStat = FtpDownloadStatus.DOWNLOAD_CLOSEREMOTESTREAM_ERROR;
					}
				}

				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						downloadStat = FtpDownloadStatus.DOWNLOAD_CLOSELOCALSTREAM_ERROR;
					}
				}
			}
		}

		return downloadStat;
	}

	/**
	 * get FTPFile by file name
	 * 
	 * @param remoteFile
	 * @return FTPFile
	 */
	public FTPFile getRemoteFile(String remoteFile) {
		FTPFile ftpFile = null;
		try {
			this.ftpClient.enterLocalPassiveMode();
			FTPFile[] ftpFiles = this.ftpClient.listFiles(remoteFile);
			if (ftpFiles.length == 1)//
				ftpFile = ftpFiles[0];
		} catch (IOException e) {
			ftpFile = null;
		}
		return ftpFile;
	}

	/**
	 * list all files in the specified remote directory
	 * 
	 * @param remotePath
	 * @return
	 */
	public FTPFile[] listFtpFiles(String remotePath) {
		FTPFile[] ftpFiles = null;
		if (this.isConnected()) {
			ftpClient.enterLocalPassiveMode();
			try {
				ftpFiles = ftpClient.listFiles(remotePath);
			} catch (IOException e) {
			}
		}
		return ftpFiles;
	}

	/**
	 * list all files in the specified remote directory with FtpFileFilter
	 * 
	 * @param remotePath
	 * @param fileFilter
	 * @return
	 */
	/*public FTPFile[] listFtpFiles(String remotePath, FTPFileFilter fileFilter) {
		FTPFile[] ftpFiles = null;
		if (this.isConnected()) {
			ftpClient.enterLocalPassiveMode();
			try {
				ftpFiles = ftpClient.listFiles(remotePath, fileFilter);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ftpFiles;
	}*/

	/**
	 * list all files with specific file extension
	 * 
	 * @param remotePath
	 * @param fileExtension
	 * @return
	 */
	/*public FTPFile[] listFtpFiles(String remotePath, final String fileExtension) {
		return listFtpFiles(remotePath, new FTPFileFilter() {
			public boolean accept(FTPFile arg0) {
				if (!arg0.isDirectory()) {
					if (arg0.getName().split("[.]")[1]
							.equalsIgnoreCase(fileExtension)) {
						return true;
					}
				}
				return false;
			}

		});
	}*/

	/**
	 * upload local file to ftpserver 1.check local file exist
	 * <p>
	 * 2.check remote dir exist, if not exist, create dir
	 * <p>
	 * 3.check remote file if need upload from breakpoint
	 * <p>
	 * 
	 * @param remoteFile
	 *            absolute file path at ftpserver. seperator:'/'
	 * @param localFile
	 *            absolute file path at local disk
	 * @param isSupportBreakpoint
	 *            true to support upload file from breakpoint
	 * @return FtpUploadStatus
	 */
	public FtpUploadStatus upload(String remoteFileName, String localFileName,
			boolean isSupportBreakpoint) {
		FtpUploadStatus uploadStat = FtpUploadStatus.UPLOAD_UNKNOWN;
		File localFile = new File(localFileName);
		if (localFile.exists()) {
			FTPFile remoteFtpFile = getRemoteFile(remoteFileName);
			if (remoteFtpFile == null) {// remote file not exist, create dir if
										// need at ftpserver
				if (!createRemoteDir(remoteFileName)) {
					uploadStat = FtpUploadStatus.UPLOAD_REMOTE_DIR_CREATE_FAIL;
					return uploadStat;
				}
			}
			if (isSupportBreakpoint && remoteFtpFile != null) {
				long offset = 0;
				long localFileSize = localFile.length();
				long remoteFileSize = remoteFtpFile.getSize();

				if (remoteFileSize < localFileSize) {
					offset = remoteFileSize;
					uploadStat = uploadFile(remoteFtpFile, remoteFileName,
							localFile, offset);
				} else {// uploaded
					uploadStat = FtpUploadStatus.UPLOAD_FILE_UPLOADED;
				}
			} else {
				uploadStat = uploadFile(remoteFtpFile, remoteFileName,
						localFile, 0);
			}
		} else {
			uploadStat = FtpUploadStatus.UPLOAD_LOCAL_FILE_NOT_EXIST;
		}
		return uploadStat;
	}

	/**
	 * 
	 * @param remoteFileName
	 * @return
	 */
	public boolean createRemoteDir(String remoteFileName) {
		try {
			remoteFileName = remoteFileName.replaceAll(
					Matcher.quoteReplacement("\\"), "/");
			this.ftpClient.changeWorkingDirectory("/");
			int fsIndex = remoteFileName.lastIndexOf("/");
			if (fsIndex > 0) {
				String[] dirs = remoteFileName.split("/");
				StringBuilder parentDir = new StringBuilder("");
				for (int i = 0; i < dirs.length - 1; i++) {
					if (dirs[i].equals(""))
						continue;
					this.ftpClient.makeDirectory(dirs[i]);
					parentDir.append("/" + dirs[i]);
					this.ftpClient.changeWorkingDirectory(parentDir.toString());
				}
			} else {
				return true;
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * write local file content to remote file at ftpserver
	 * 
	 * @param remoteFtpFile
	 * @param remoteFileName
	 * @param localFile
	 * @param offset
	 * @return
	 */
	private FtpUploadStatus uploadFile(FTPFile remoteFtpFile,
			String remoteFileName, File localFile, long offset) {
		FtpUploadStatus uploadStatus = FtpUploadStatus.UPLOAD_UNKNOWN;
		RandomAccessFile raf = null;
		OutputStream outputStream = null;
		try {
			this.ftpClient.enterLocalPassiveMode();
			this.ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			raf = new RandomAccessFile(localFile, "r");
			remoteFileName = remoteFileName.replaceAll(
					Matcher.quoteReplacement("\\"), "/");
			int i = remoteFileName.lastIndexOf("/");
			if (i > 0) {
				this.ftpClient.changeWorkingDirectory(remoteFileName.substring(
						0, i));// make sure
				remoteFileName = remoteFileName.substring(i + 1);
			}
			outputStream = this.ftpClient.appendFileStream(remoteFileName);
			if (offset > 0) {
				this.ftpClient.setRestartOffset(offset);
				raf.seek(offset);
			}
			byte[] buf = new byte[this.uploadBufSize];
			int c = 0;
			while ((c = raf.read(buf)) != -1) {
				outputStream.write(buf, 0, c);
			}
			outputStream.flush();
			outputStream.close();
			raf.close();
			uploadStatus = ftpClient.completePendingCommand() ? (offset == 0 ? FtpUploadStatus.UPLOAD_SUCC_NEW
					: FtpUploadStatus.UPLOAD_SUCC_BREAK)
					: (offset == 0 ? FtpUploadStatus.UPLOAD_FAIL_NEW
							: FtpUploadStatus.UPLOAD_FAIL_BREAK);
		} catch (FileNotFoundException e) {
			uploadStatus = (offset == 0 ? FtpUploadStatus.UPLOAD_FAIL_NEW
					: FtpUploadStatus.UPLOAD_FAIL_BREAK);
		} catch (SocketTimeoutException e) {
			uploadStatus = (offset == 0 ? FtpUploadStatus.UPLOAD_FAIL_TIMEOUT_NEW
					: FtpUploadStatus.UPLOAD_FAIL_TIMEOUT_BREAK);
		} catch (IOException e) {
			uploadStatus = (offset == 0 ? FtpUploadStatus.UPLOAD_FAIL_IOEXCEPTION_NEW
					: FtpUploadStatus.UPLOAD_FAIL_IOEXCEPTION_BREAK);
		} catch (Exception e) {
			uploadStatus = (offset == 0 ? FtpUploadStatus.UPLOAD_FAIL_NEW
					: FtpUploadStatus.UPLOAD_FAIL_BREAK);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
					outputStream = null;
				} catch (IOException e) {
					uploadStatus = FtpUploadStatus.UPLOAD_CLOSEREMOTESTREAM_ERROR;
				}
			}

			if (raf != null) {
				try {
					raf.close();
					raf = null;
				} catch (IOException e) {
					uploadStatus = FtpUploadStatus.UPLOAD_CLOSELOCALSTREAM_ERROR;
				}
			}
		}
		return uploadStatus;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserPass() {
		return userPass;
	}

	public boolean isConnected() {
		return isConnected && ftpClient.isConnected() && this.isLogined;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		if (encoding != null && !encoding.equals(this.encoding)) {
			this.encoding = encoding;
			this.ftpClient.setControlEncoding(this.encoding);
		}
	}

	public int getDownloadBufSize() {
		return downloadBufSize;
	}

	public void setDownloadBufSize(int downloadBufSize) {
		this.downloadBufSize = downloadBufSize > 0
				&& downloadBufSize <= 1024 * 1024 ? downloadBufSize
				: this.downloadBufSize;
		;
	}

	public int getUploadBufSize() {
		return uploadBufSize;
	}

	public void setUploadBufSize(int uploadBufSize) {
		this.uploadBufSize = uploadBufSize > 0 && uploadBufSize <= 1024 * 1024 ? uploadBufSize
				: this.uploadBufSize;
	}

	public void changeToParentDir() {
		if (this.isConnected()) {
			try {
				this.ftpClient.changeToParentDirectory();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String currentDir() {
		try {
			return this.ftpClient.printWorkingDirectory();
		} catch (IOException e) {
			e.printStackTrace();
			return "ERROR";
		}
	}
}