package com.goodcol.util;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;

/**
 * 发送短信
 * 
 * @author zhang_zy
 * @date 2017-11-20
 */
public class MessageSender {
	
	private static Logger log = Logger.getLogger(MessageSender.class);
	
	/**
	 * 发送短信
	 * 
	 * @param mobile 手机号
	 * @param message 消息内容
	 */
	public static void sendMessage(String mobile,String message){
		Db.update(" INSERT INTO YYGL_MESSAGE_SENDER(ID,MOBILE,MESSAGE,UPDATE_TIME)VALUES(?,?,?,to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'))",
				new Object[]{AppUtils.getStringSeq(),mobile,message});
		log.info("存储短信发送信息--手机号码："+mobile+"消息内容："+message);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		sendMessage("13585102850","你好");
	}
}