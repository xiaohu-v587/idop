/**
 * 
 */
package com.goodcol.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;

import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.util.zxgldbutil.JsonToMapTool;

/**
 * @author dinggang
 *
 */
public class YiApiTool {
	public static Logger log = Logger.getLogger(YiApiTool.class);
	//	获得ConnectionManager，设置相关参数
	private static HttpConnectionManager manager = null;

	private static int connectionTimeOut = 60000;

	private static int socketTimeOut = 60000;

	private static int maxConnectionPerHost = 10;

	private static int maxTotalConnections = 5;
	/**
	 * 2017年9月28日10:39:49
	 * 使用静态内部类实现单例
	 * @author liutao
	 *
	 */
//	private static class SingletonHolder{
//		private static final HttpClient CLIENT = new HttpClient(manager);
//	}
	
	/**
	 * 2017年9月28日10:42:47
	 * 获取HttpClient实例
	 * @author liutao
	 * @return HttpClient
	 */
//	public static final HttpClient getInstance(){
//		//设置manager属性
//		SetPara();
//		return SingletonHolder.CLIENT;
//	}

	// 初始化ConnectionManger的方法
//	public static void SetPara() {
//		if(null == manager){
//			System.out.println("*********");
//			manager = new SimpleHttpConnectionManager();
//			manager.getParams().setConnectionTimeout(connectionTimeOut);
//			manager.getParams().setSoTimeout(socketTimeOut);
//			manager.getParams().setDefaultMaxConnectionsPerHost(
//					maxConnectionPerHost);
//			manager.getParams().setMaxTotalConnections(maxTotalConnections);
//		}
//	}
	public static Map<String, Object> getCompanyDetail(String yiApiUrl, String url, String xyd_edmp_key) {
		StringBuffer resultBuffer = new StringBuffer();
		Map<String, Object> resMap = new HashMap<String, Object>();
		PostMethod post = new PostMethod(url);
		String result = null;
		try {
			String param =DESUtils.encrypt(yiApiUrl.getBytes("utf-8"),xyd_edmp_key);
			HttpConnectionManager manager = new SimpleHttpConnectionManager();
			manager.getParams().setConnectionTimeout(connectionTimeOut);
			manager.getParams().setSoTimeout(socketTimeOut);
			manager.getParams().setDefaultMaxConnectionsPerHost(
					maxConnectionPerHost);
			manager.getParams().setMaxTotalConnections(maxTotalConnections);
			HttpClient client = new HttpClient(manager);
			HttpClientExample.SetPara();
			post.setFollowRedirects(false);
			post.setParameter("urlAddress", param);
			//操作用户id
			post.setParameter("userId", "3969027");
			//操作用户
			post.setParameter("userName","周煌");
			//操作机构id
			post.setParameter("userOrgId","07140");
			//数据类型
			post.setParameter("datatypeCode", "GS_");
			client.executeMethod(post);
			BufferedReader in = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream(), post
					.getResponseCharSet()));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				resultBuffer.append(inputLine);
				resultBuffer.append("\n");
			}
			in.close();
			result = resultBuffer.toString();
			result = HttpClientExample.ConverterStringCode(resultBuffer.toString(), post.getResponseCharSet(), "utf-8");
			System.out.println("************调用企查查返回结果Start*************");
			System.out.println(result);
			System.out.println("************调用企查查返回结果end*************");
		} catch (Exception e) {
			e.printStackTrace();
			resMap.put("Status", "9999");
			return resMap;
		} finally {
			post.releaseConnection();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = JsonToMapTool.convertJsonToMap("{result:" + result + "}");
			if (null != map.get("result") && !"".equals(map.get("result").toString())) {
				resMap = JsonToMapTool.convertJsonToMap((String) map.get("result"));
			}
		} catch (Exception e) {
			resMap.put("Status", "9999");
			return resMap;
		}
		return resMap;
	}

}
