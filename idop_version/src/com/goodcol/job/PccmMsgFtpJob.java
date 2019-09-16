package com.goodcol.job;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.ftp.FTPClientTool;
import com.goodcol.ftp.FtpUploadStatus;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.ParamContainer;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

/**
 * 短信批量发送任务
 * @author start
 */
public class PccmMsgFtpJob extends OuartzJob {
	/** 地址 */
	String host = null;
	/** 端口 */
	int port = 21;
	/** 用户名*/
	String name = null;
	/** 密码*/
	String pwd = null;
	/** 字符编码 */
	String encodeing = null;
	/** 本地文件目录 */
	String filePath = null;
	/** 上传文件目录 */
	String uploadPath = null;
	/** 系统号 */
	String SYS_NUM = null;
	/** 默认编号 */
	String DEFAULT_NUM = null;
	/** 设置默认发送的机构号 */
	String orgId = ParamContainer.getDictName("PCCM_MSG_ORG", "1");
	/** 逗号 */
	String COMMA = ",";
	/** 默认数据源 */
	String DEFAULT = "default";
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		//短信开关
		if (!getFlag()) {
			System.out.println("短信开关未打开");
			return;
		}
		// 获取参数
		if (!getParam()) {
			System.out.println("PccmMsgFtpJob FTP参数未配置，任务执行失败!");
			error();
			return;
		}
		// 生成文件
		List<Record> fileList = createFile();
		if (fileList == null || fileList.size() < 1) {
			System.out.println("PccmMsgFtpJob 未查询到需要发送的短信!");
			success();
			return;
		}
		
		// 短信内容写入文件
		if(!writeMsg(fileList)){
			return;
		}

		// 连接FTP服务器
		try {
			FTPClientTool ftpClientTool = new FTPClientTool(host, port, name, pwd, encodeing);
			System.out.println("连接FTP服务器：地址【" + host + "】 端口【" + port + "】 用户名【" + name + "】 密码【" + pwd + "】");
			if (ftpClientTool.connect()) {
				if (ftpClientTool.login()) {
					for (Record record : fileList) {
						String smg_part = record.getStr("SMG_PART");//短信拆分
						String ftpUploadPath;// 服务器文件路径
						// 上传文件
						if (StringUtils.isNotBlank(smg_part)) {
							ftpUploadPath = uploadPath + record.getStr("SM_JOB") + "_" + smg_part + ".txt";
						} else {
							ftpUploadPath = uploadPath + record.getStr("SM_JOB") + ".txt";
						}
						FtpUploadStatus status = ftpClientTool.upload(ftpUploadPath, record.getStr("path"), false);
						if (FtpUploadStatus.UPLOAD_SUCC_NEW.equals(status)) {
							System.out.println("文件上传成功！");
							// 更新短信发送状态
							updateSendStatus(record.getStr("SM_JOB"), "1", smg_part);
						} else {
							System.out.println("文件上传失败，返回状态码:" + status);
							// 更新短信发送状态
							updateSendStatus(record.getStr("SM_JOB"), "2", smg_part);
						}
					}
					success();
				} else {
					System.out.println("FTP服务器登陆失败：地址【" + host + "】 端口【" + port + "】 用户名【" + name + "】 密码【" + pwd + "】");
					error();
				}
			} else {
				System.out.println("FTP服务器连接失败：地址【" + host + "】 端口【" + port + "】 用户名【" + name + "】 密码【" + pwd + "】");
				error();
			}
		} catch (Exception e) {
			e.printStackTrace();
			error();
		}
	}
	
	/**
	 * 获取参数
	 * @return
	 */
	private boolean getParam(){
		List<Record> dictList = AppUtils.findDictListByKey("PCCM_FTP");
		if (dictList == null || dictList.size() == 0) {
			return false;
		}
		for (Record record : dictList) {
			if ("1".equals(record.get("val"))) {
				host = record.get("remark");
			} else if ("2".equals(record.get("val"))) {
				port = Integer.parseInt(record.getStr("remark"));
			} else if ("3".equals(record.get("val"))) {
				name = record.get("remark");
			} else if ("4".equals(record.get("val"))) {
				pwd = record.get("remark");
			} else if ("5".equals(record.get("val"))) {
				encodeing = record.get("remark");
			} else if ("6".equals(record.get("val"))) {
				filePath = record.get("remark");
			} else if ("7".equals(record.get("val"))) {
				uploadPath = record.get("remark");
			} else if ("8".equals(record.get("val"))) {
				SYS_NUM = record.get("remark");
			} else if ("9".equals(record.get("val"))) {
				DEFAULT_NUM = record.get("remark");
			}
		}
		return true;
	}
	
	/**
	 * 创建短信文件
	 */
	public List<Record> createFile(){ 
		String sql = null;
		if (StringUtils.isNotBlank(orgId)) {
			
			// 指定机构，则发送指定机构及下级机构短信
			sql = "SELECT PM.SM_JOB,PM.SMG_PART FROM PCCM_MSG_INFO PM "
					+ " INNER JOIN SYS_USER_INFO UI ON PM.USER_NO = UI.USER_NO "
					+ " INNER JOIN SYS_ORG_INFO OI ON UI.ORG_ID = OI.ID "
					+ " WHERE PM.SEND_TYPE = '2' "
					+ "   AND PM.SEND_STATUS <> '1' " 
					+ "   AND (OI.ID = '" + orgId + "' OR OI.BY5 LIKE '%" + orgId + "%') "
					+ "   AND PM.USER_NO IS NOT NULL "
					+ " GROUP BY PM.SM_JOB,PM.SMG_PART ORDER BY PM.SM_JOB, PM.SMG_PART ";
			
		} else {
			// 未指定机构，则发送全部短信
			sql = "SELECT SM_JOB,SMG_PART FROM PCCM_MSG_INFO PM WHERE SEND_TYPE = '2' AND SEND_STATUS <> '1' GROUP BY PM.SM_JOB,SMG_PART ORDER BY SM_JOB, SMG_PART";
		}
		List<Record> list = Db.use(DEFAULT).find(sql);
		
		for (Record record : list) {
			String smg_part = record.getStr("SMG_PART");
			String path = null;
			if (StringUtils.isNotBlank(smg_part)) {
				path = filePath + record.getStr("SM_JOB") + "_" + smg_part + ".txt";
			} else {
				path = filePath + record.getStr("SM_JOB") + ".txt";
			}
			
			record.set("path", path);
			System.out.println("生成短信的文件路径：" + path);
			File file = new File(path);
			try {
				if (!file.exists()) {
					file.createNewFile();
				} else {
					file.delete();
					file.createNewFile();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return list;
	}
	
	/**
	 * 根据文件名生成短信内容
	 * @param fileList
	 */
	private boolean writeMsg(List<Record> fileList){
		for (Record record : fileList) {
			String smJob = record.getStr("SM_JOB");
			String smg_part = record.getStr("SMG_PART");
			String selectSql = "";
			String fromSql = "";
			
			if (StringUtils.isNotBlank(orgId)) {
				// 指定机构，则发送指定机构及下级机构短信
				selectSql = "SELECT PM.* ";
				fromSql = " FROM PCCM_MSG_INFO PM "
						+ " INNER JOIN SYS_USER_INFO UI ON PM.USER_NO = UI.USER_NO "
						+ " INNER JOIN SYS_ORG_INFO OI ON UI.ORG_ID = OI.ID "
						+ " WHERE PM.SEND_TYPE = '2' "
						+ "   AND PM.SEND_STATUS <> '1' " 
						+ "   AND (OI.ID = '" + orgId + "' OR OI.BY5 LIKE '%" + orgId + "%') "
						+ "   AND PM.SM_JOB = '" + smJob + "'";
				if (StringUtils.isNotBlank(smg_part)) {
					fromSql += " AND PM.SMG_PART ='" + smg_part + "'";
				} 
				
				fromSql +=	"   AND PM.USER_NO IS NOT NULL "
						+ " ORDER BY PM.SM_JOB, PM.USER_NO, PM.SMG_PART ";
				
			} else {
				// 未指定机构，则发送全部短信
				selectSql = "SELECT * ";
				fromSql = " FROM PCCM_MSG_INFO PM WHERE SEND_TYPE = '2' " 
						+ "AND SEND_STATUS <> '1' AND SM_JOB = '" + smJob + "'";
				if (StringUtils.isNotBlank(smg_part)) {
					fromSql += " AND SMG_PART ='" + smg_part + "'";
				} 
				fromSql += " ORDER BY PM.SM_JOB, PM.USER_NO, PM.SMG_PART ";
			}
			boolean writeMsgToFile = writeMsgToFile(selectSql, fromSql, record.getStr("path"));
			if (!writeMsgToFile) {
				return false;
			} 
		}
		return true;
	}
	
	/**
	 * @param selectSql
	 * @param fromSql
	 * @param path
	 */
	private boolean writeMsgToFile(String selectSql, String fromSql,String path) {
		int pageNumber = 1;
		int pageSize = 1000;
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(new File(path)));
			for (int i = 0; i < 100; i++) {
				Page<Record> page = Db.use(DEFAULT).paginate(pageNumber, pageSize, selectSql, fromSql);
				List<Record> list = page.getList();
				for (Record record : list) {
					String smCont = record.getStr("MOBILE")+ COMMA + SYS_NUM + COMMA + DEFAULT_NUM + COMMA + record.getStr("SM_KEY");
					System.out.println("生成的短信内容：" + smCont);
					bufferedWriter.write(smCont + "\n");
					bufferedWriter.flush();
				}
				if (list.size() == 0) {
					break;
				}
				pageNumber++;
			}
			
		} catch (Exception e) {
			System.out.println("文件:" + path + ">>> 写入内容失败！");
			e.printStackTrace();
			return false;
		} finally{
			if (bufferedWriter != null) {
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	/**
	 * 更新发送状态及发送时间
	 */
	private void updateSendStatus(String sm_job, String status, String smg_part) {
		String updateSql = "";
		if (StringUtils.isNotBlank(orgId)) {
			// 指定机构，则发送指定机构及下级机构短信
			updateSql = "UPDATE PCCM_MSG_INFO SET SEND_STATUS = '" + status + "', SEND_TIME='" + DateTimeUtil.getNowDate() + "'"
					+ " WHERE ID IN (SELECT PM.ID FROM PCCM_MSG_INFO PM "
					+ " INNER JOIN SYS_USER_INFO UI ON PM.USER_NO = UI.USER_NO "
					+ " INNER JOIN SYS_ORG_INFO OI ON UI.ORG_ID = OI.ID "
					+ " WHERE PM.SEND_TYPE = '2' "
					+ "   AND PM.SEND_STATUS <> '1' " 
					+ "   AND (OI.ID = '" + orgId + "' OR OI.BY5 LIKE '%" + orgId + "%') "
					+ "   AND PM.USER_NO IS NOT NULL) "
					+ "   AND SEND_STATUS <> '1' AND SM_JOB = '" + sm_job + "'";
			if (StringUtils.isNotBlank(smg_part)) {
				updateSql += " AND SMG_PART = '" + smg_part + "'";
			}
		} else {
			// 未指定机构，则发送全部短信
			updateSql = "UPDATE PCCM_MSG_INFO set SEND_STATUS = '" + status + "', SEND_TIME='" + DateTimeUtil.getNowDate() + "'"
					+ "WHERE SEND_TYPE = '2' AND SEND_STATUS <> '1' ";
			if (StringUtils.isNotBlank(smg_part)) {
				updateSql += " AND SMG_PART = '" + smg_part + "'";
			}
		}
		Db.use(DEFAULT).update(updateSql);
	}
	
	private boolean getFlag() {
		return "0".equals(ParamContainer.getDictList("sms_switch").get(0).get("val")) ? true : false;
	}
	
}
