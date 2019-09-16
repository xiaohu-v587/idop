/**
 * 
 */
package com.goodcol.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * @author dinggang
 *
 */
public class HttpClientExample {
	// 获得ConnectionManager，设置相关参数
		private static MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();

		private static int connectionTimeOut = 20000;

		private static int socketTimeOut = 10000;

		private static int maxConnectionPerHost = 5;

		private static int maxTotalConnections = 40;

		// 标志初始化是否完成的flag
		private static boolean initialed = false;

		// 初始化ConnectionManger的方法
		public static void SetPara() {
			manager.getParams().setConnectionTimeout(connectionTimeOut);
			manager.getParams().setSoTimeout(socketTimeOut);
			manager.getParams().setDefaultMaxConnectionsPerHost(maxConnectionPerHost);
			manager.getParams().setMaxTotalConnections(maxTotalConnections);
			initialed = true;
		}

		// 通过get方法获取网页内容
		public static String getGetResponseWithHttpClient(String url, String encode) {
			HttpClient client = new HttpClient(manager);

			if (initialed) {
				HttpClientExample.SetPara();
			}

			GetMethod get = new GetMethod(url);
			get.setFollowRedirects(true);

			String result = null;
			StringBuffer resultBuffer = new StringBuffer();

			try {
				
				client.executeMethod(get);

				// 在目标页面情况未知的条件下，不推荐使用getResponseBodyAsString()方法
				// String strGetResponseBody = post.getResponseBodyAsString();
				BufferedReader in = new BufferedReader(new InputStreamReader(get.getResponseBodyAsStream(), get
						.getResponseCharSet()));

				String inputLine = null;

				while ((inputLine = in.readLine()) != null) {
					resultBuffer.append(inputLine);
					resultBuffer.append("\n");
				}

				in.close();

				result = resultBuffer.toString();

				// iso-8859-1 is the default reading encode
				result = HttpClientExample.ConverterStringCode(resultBuffer.toString(), get.getResponseCharSet(), encode);
			} catch (Exception e) {
				e.printStackTrace();

				result = "";
			} finally {
				get.releaseConnection();
			}
			return result;
		}

		public static String getPostResponseWithHttpClient(String url, String encode) {
			HttpClient client = new HttpClient(manager);
			
			if (initialed) {
				HttpClientExample.SetPara();
			}

			PostMethod post = new PostMethod(url);
			post.setFollowRedirects(false);

			StringBuffer resultBuffer = new StringBuffer();

			String result = null;

			try {
				client.executeMethod(post);

				BufferedReader in = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream(), post
						.getResponseCharSet()));
				String inputLine = null;

				while ((inputLine = in.readLine()) != null) {
					resultBuffer.append(inputLine);
					resultBuffer.append("\n");
				}

				in.close();

				// iso-8859-1 is the default reading encode
				result = HttpClientExample.ConverterStringCode(resultBuffer.toString(), post.getResponseCharSet(), encode);
			} catch (Exception e) {
				e.printStackTrace();

				result = "";
			} finally {
				post.releaseConnection();
			}
			return result;
		}

		public static String getPostResponseAsJsonString(String url, String encode) {
			HttpClient client = new HttpClient(manager);

			if (initialed) {
				HttpClientExample.SetPara();
			}

			PostMethod post = new PostMethod(url);
			post.setFollowRedirects(false);
			String result = null;

			try {
				client.executeMethod(post);
				// iso-8859-1 is the default reading encode
				result = HttpClientExample.ConverterStringCode(post.getResponseBodyAsString(), post.getResponseCharSet(),
						encode);
			} catch (Exception e) {
				e.printStackTrace();

				result = "";
			} finally {
				post.releaseConnection();
			}
			return result;
		}

		public static String getPostResponseWithHttpClient(String url, String encode, NameValuePair[] nameValuePair) {
			HttpClient client = new HttpClient(manager);

			if (initialed) {
				HttpClientExample.SetPara();
			}

			PostMethod post = new PostMethod(url);
			post.setRequestBody(nameValuePair);
			post.setFollowRedirects(false);

			String result = null;
			StringBuffer resultBuffer = new StringBuffer();

			try {
				client.executeMethod(post);
				BufferedReader in = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream(), post
						.getResponseCharSet()));
				String inputLine = null;

				while ((inputLine = in.readLine()) != null) {
					resultBuffer.append(inputLine);
					resultBuffer.append("\n");
				}

				in.close();

				// iso-8859-1 is the default reading encode
				result = HttpClientExample.ConverterStringCode(resultBuffer.toString(), post.getResponseCharSet(), encode);
			} catch (Exception e) {
				e.printStackTrace();

				result = "";
			} finally {
				post.releaseConnection();
			}
			return result;
		}

		public static String ConverterStringCode(String source, String srcEncode, String destEncode) {
			if (source != null) {
				try {
					return new String(source.getBytes(srcEncode), destEncode);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "";
				}
			} else {
				return "";
			}
		}
}
