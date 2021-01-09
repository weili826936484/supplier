package com.water.supplier.util.jddj;  
      
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.CoreConnectionPNames;
      
/**
 * HTTP工具类
 * @author wang-ql
 *
 */
@SuppressWarnings("deprecation")
public class HttpUtil {
	
	/**
	 * 默认编码为UTF-8
	 */
	private static final String HTTP_CONTENT_CHARSET = "UTF-8";


	public static final Integer MAX_TIME_OUT = 10000;
	public static final Integer MAX_IDLE_TIME_OUT = 20000;
	public static final Long MAX_MANAGER_TIME = 1000l;
	public static final Integer MAX_CONN = 128;
	public static final Integer MAX_HOST_CONN = 32;
	
	public static final HttpClient httpClient = new HttpClient(); 

	static {
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		connectionManager.closeIdleConnections(MAX_IDLE_TIME_OUT);
		connectionManager.getParams().setParameter(HttpConnectionManagerParams.MAX_TOTAL_CONNECTIONS, MAX_CONN);
		connectionManager.getParams().setDefaultMaxConnectionsPerHost(MAX_HOST_CONN);
		httpClient.setHttpConnectionManager(connectionManager);
		httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, MAX_TIME_OUT);
		httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, MAX_TIME_OUT);
		httpClient.getParams().setLongParameter(ClientPNames.CONN_MANAGER_TIMEOUT, MAX_MANAGER_TIME);
	}

	/**
	 * POST方式发送HTTP请求
	 * sendSimplePostRequest </br>
	 * @param url
	 * @param param
	 * @return
	 * @throws Exception </br>
	 * String
	 */
	public static String sendJdPostRequest(String url, Map<String, Object> param) throws Exception {
		String result=null;
		PostMethod post = new PostMethod(url);
		try {
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, HTTP_CONTENT_CHARSET);
			if (param != null) {
				for (Entry<String, Object> entry : param.entrySet()) {
					if (entry.getValue() != null) {
						post.addParameter(entry.getKey(), (String)entry.getValue());
					}
				}
			}
			post.addRequestHeader(new Header("Connection", "close"));
			httpClient.executeMethod(post);
			if (post.getStatusCode() == HttpStatus.SC_OK) {
				return post.getResponseBodyAsString();
			} else {
				result = "{\"code\":\"" + post.getStatusCode() + "\", \"msg\":\"" + post.getStatusText() + "\"}";
				post.abort();
			}
		} catch (ConnectTimeoutException e) {
			result = "{\"code\":\"-1\", \"msg\":\"请求连接超时\"}";
			e.printStackTrace();
			throw e;
		} catch (SocketTimeoutException e) {
			result = "{\"code\":\"-1\", \"msg\":\"请求响应超时\"}";
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			result = "{\"code\":\"-1\", \"msg\":\"请求响应超时\"}";
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			result = "{\"code\":\"-1\", \"msg\":\"服务开小差，系统努力修复中\"}";
			e.printStackTrace();
			throw e;
		} finally {
			post.releaseConnection();
		}
		return result;
	}
	
	/**
	 * GET方式发送HTTP请求
	 * sendSimpleGetRequest </br>
	 * @param url
	 * @param param
	 * @return </br>
	 * String
	 */
	public static String sendSimpleGetRequest(String url, Map<String, Object> param) throws Exception {
		String result = null;
		StringBuilder sb = new StringBuilder();
		GetMethod get = null;
		try {
			String paramStr = "";
			if (param != null) {
				for (Entry<String, Object> entry : param.entrySet()) {
					if (entry.getValue() != null) {
						sb.append(entry.getKey()).append("=").append(URLEncoder.encode((String)entry.getValue(), "utf-8")).append("&");
					}
				}
				paramStr = sb.toString();
				paramStr = paramStr.substring(0,paramStr.length()-1);
			}
			get = new GetMethod(url + "?" + paramStr);
			get.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, HTTP_CONTENT_CHARSET);
			get.addRequestHeader(new Header("Connection", "close"));
			httpClient.executeMethod(get);
			if (get.getStatusCode() == HttpStatus.SC_OK) {
				return get.getResponseBodyAsString();
			} else {
				get.abort();
			}
		} catch (IOException ex) {
			result = "{\"code\":\"-1\", \"msg\":\"请求响应超时\"}";
			throw ex;
		} catch (Exception ex) {
			result = "{\"code\":\"-1\", \"msg\":\"服务开小差，系统努力修复中\"}";
			throw ex;
		} finally {
			if (get != null) {
				get.releaseConnection();
			}
		}
		return result;
	}
		
}  