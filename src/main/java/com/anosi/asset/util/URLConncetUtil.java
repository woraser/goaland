package com.anosi.asset.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * java后台发送http请求的工具类
 * 
 * @author jinyao
 *
 */
public class URLConncetUtil {

	private static final Logger logger = LoggerFactory.getLogger(URLConncetUtil.class);

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static URLConnection sendGet(String url, String param) {
		return sendGet(url, param, null);
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static URLConnection sendPost(String url, String param) {
		return sendPost(url, param, null);
	}

	/***
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @param headers
	 *            自定义的请求头
	 * @return
	 */
	public static URLConnection sendGet(String url, String param, Map<String, String> headers) {
		URLConnection connection = null;
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			logger.debug("connect url:{}", urlNameString);
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			connection = realUrl.openConnection();
			setRequestProperty(connection, headers);

			// connection.setRequestProperty("Cookie",
			// "JSESSIONID=a434c43e-7df5-49fa-a05a-214aeae19511");

			// 建立实际的连接
			connection.connect();
		} catch (Exception e) {
			logger.debug("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return connection;
	}

	/****
	 * 发送get请求，返回字符串
	 * 
	 * @param url
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @param headers
	 *            自定义的请求头
	 * @return
	 */
	public static String sendGetString(String url, String param, Map<String, String> headers) {
		URLConnection connection = sendGet(url, param, headers);
		return readAndAppendLines(connection);
	}

	/***
	 * 发送post请求，返回字符串
	 * 
	 * @param url
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @param headers
	 *            自定义的请求头
	 * @return
	 */
	public static String sendPostString(String url, String param, Map<String, String> headers) {
		URLConnection connection = sendPost(url, param, headers);
		return readAndAppendLines(connection);
	}

	/****
	 * 发送get请求，返回字符串
	 * 
	 * @param url
	 * @param parameterMap
	 *            请求参数，需要解析
	 * @param headers
	 *            自定义的请求头
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String sendGetString(String url, Map<String, String[]> parameterMap, Map<String, String> headers)
			throws UnsupportedEncodingException {
		URLConnection connection = sendGet(url, convertParams(parameterMap, true), headers);
		return readAndAppendLines(connection);
	}

	/***
	 * 发送post请求，返回字符串
	 * 
	 * @param url
	 * @param parameterMap
	 *            请求参数，需要解析
	 * @param headers
	 *            自定义的请求头
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String sendPostString(String url, Map<String, String[]> parameterMap, Map<String, String> headers)
			throws UnsupportedEncodingException {
		URLConnection connection = sendPost(url, convertParams(parameterMap, false), headers);
		return readAndAppendLines(connection);
	}

	/***
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @param headers
	 *            自定义请求头
	 * @return
	 */
	public static URLConnection sendPost(String url, String param, Map<String, String> headers) {
		PrintWriter out = null;
		BufferedReader in = null;
		URLConnection conn = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			conn = realUrl.openConnection();
			setRequestProperty(conn, headers);
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
		} catch (Exception e) {
			logger.debug("发送POST请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return conn;
	}

	/***
	 * 将parameterMap处理成 name1=value1&name2=value2 的形式
	 * 
	 * @param parameterMap
	 * @param encoding
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String convertParams(Map<String, String[]> parameterMap, boolean encoding)
			throws UnsupportedEncodingException {
		if (parameterMap != null && parameterMap.size() != 0) {
			StringBuilder params = new StringBuilder();
			for (Entry<String, String[]> entry : parameterMap.entrySet()) {
				String[] values = entry.getValue();
				for (String value : values) {
					if (encoding) {
						value = URLEncoder.encode(value, "utf-8");
					}
					params.append(entry.getKey() + "=" + value + "&");
				}
			}
			params.deleteCharAt(params.length() - 1);// 删除最后一个"&"
			return params.toString();
		}
		return null;
	}

	/***
	 * 将返回的流读成一行string
	 * 
	 * @param conn
	 * 
	 */
	public static String readAndAppendLines(URLConnection conn) {
		StringBuilder result = new StringBuilder();
		try (InputStream inputStream = conn.getInputStream(); // 获取流
				BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result.toString();
	}

	private static void setRequestProperty(URLConnection conn, Map<String, String> headers) {
		if (headers != null && !headers.isEmpty()) {
			// 设置通用的请求属性
			if (!headers.containsKey("accept")) {
				conn.setRequestProperty("accept", "*/*");
			}
			if (!headers.containsKey("connection")) {
				conn.setRequestProperty("connection", "Keep-Alive");
			}
			if (!headers.containsKey("user-agent")) {
				conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			}
			for (Entry<String, String> entry : headers.entrySet()) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
		} else {
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		}

	}

}
