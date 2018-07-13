package org.zdxue.microservice.xxx.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

/**
 * @author xuezd
 */
public class HttpClientUtil {
	
	/**
	 * 参数描述：
	 * setMaxTotal, 设置池子的最大连接数
	 * setDefaultMaxPerRoute, 设置每个路由的最大连接数
	 * 
	 * CONNECT_TIMEOUT, 等待连接的超时时间, 即连接超时
	 * SO_TIMEOUT, (即SOCKET TIMEOUT), 等待数据的超时时间, 即读数据超时
	 * 
	 * 举例: 
	 * setMaxTotal=600
	 * setDefaultMaxPerRoute=200
	 * 
	 * CONNECT_TIMEOUT=1000
	 * SO_TIMEOUT=5000
	 * 
	 * 假如要连接到四个外部服务器，分别为 http://a.com, http://b.com, http://c.com, http://d.com
	 * 那么每个服务器就占一个路由
	 * 极限情况下，每个路由最大只支持200个连接, 一共需要消耗掉池中800个连接,
	 * 如果此时setMaxTotal=600, 那么将会出现无法从池中获取到连接的异常抛出。
	 */
	
	private static final int MAX_CONNECTION_SIZE_IN_POOL = 1000;
	private static final int MAX_CONNECTION_SIZE_PER_ROUTE = 300;
	
	private static final int CONNECT_TIMEOUT = 1000;
	private static final int SO_TIMEOUT = 3000;
	
	private static final String CHARSET = "UTF-8";
	private static final String JSON_MIMETYPE = "application/json";
	private static final String DEFAULT_MIMETYPE = "application/x-www-form-urlencoded";
	
	private static final PoolingHttpClientConnectionManager cm;
	
	static {
		cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(MAX_CONNECTION_SIZE_IN_POOL);
		cm.setDefaultMaxPerRoute(MAX_CONNECTION_SIZE_PER_ROUTE);
	}
	
	/**
	 * 自定义SSL
	 * 
	 * @param keyStorePath 密钥库路径
	 * @param keyStorePwd 密钥库密码
	 * @return
	 */
	public static SSLContext custom(String keyStorePath, String keyStorePwd) {
		SSLContext sslContext = null;
		FileInputStream instream = null;
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			instream = new FileInputStream(new File(keyStorePath));
			trustStore.load(instream, keyStorePwd.toCharArray());

			// Trust own CA and all self-signed certs
			sslContext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (instream != null)
					instream.close();
			} catch (IOException e) {
				// ignore
			}
		}
		return sslContext;
	}
	
	public static CloseableHttpClient getHttpClient() {
		// 代理设置
		//HttpHost proxy = new HttpHost("101.53.101.172", 9999, "http");
		//DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		//httpClientBuilder.setRoutePlanner(routePlanner);
		CloseableHttpClient httpClient = httpClientBuilder.setConnectionManager(cm).setConnectionManagerShared(true).build();
		return httpClient;
    }

	public static String postJson(String url, String json) throws ConnectTimeoutException, SocketTimeoutException, Exception {
		return postBody(url, json, JSON_MIMETYPE, null, CHARSET, CONNECT_TIMEOUT, SO_TIMEOUT);
	}
	
	public static String postJson(String url, Map<String, String> header, String json) throws ConnectTimeoutException, SocketTimeoutException, Exception {
        return postBody(url, json, JSON_MIMETYPE, header, CHARSET, CONNECT_TIMEOUT, SO_TIMEOUT);
    }
	
	public static String postData(String url, String body) throws ConnectTimeoutException, SocketTimeoutException, Exception {
		return postBody(url, body, DEFAULT_MIMETYPE, null, CHARSET, CONNECT_TIMEOUT, SO_TIMEOUT);
	}

	public static String postData(String url, String body, String charset, Integer connectTimeout, Integer soTimeout) throws ConnectTimeoutException, SocketTimeoutException, Exception {
		return postBody(url, body, DEFAULT_MIMETYPE, null, charset, connectTimeout, soTimeout);
	}

	public static String postForm(String url, Map<String, String> params) throws ConnectTimeoutException, SocketTimeoutException, Exception {
		return postForm(url, params, CONNECT_TIMEOUT, SO_TIMEOUT);
	}
	
	public static String postForm(String url, Map<String, String> header, Map<String, String> params) throws ConnectTimeoutException, SocketTimeoutException, Exception {
		return postForm(url, header, params, CONNECT_TIMEOUT, SO_TIMEOUT);
	}

	public static String postForm(String url, Map<String, String> params, Integer connectTimeout, Integer soTimeout) throws ConnectTimeoutException, SocketTimeoutException, Exception {
		return postForm(url, null, params, connectTimeout, soTimeout);
	}
	
	public static String postForm(String url, Map<String, String> header, Map<String, String> params, Integer connectTimeout, Integer soTimeout) throws ConnectTimeoutException, SocketTimeoutException, Exception {
		return postForm(url, CHARSET, header, params, connectTimeout, soTimeout);
	}
	
	public static String postMultipartForm(String url, Map<String, String> params, Map<String, File> files) throws ConnectTimeoutException, SocketTimeoutException, Exception {
		return postMultipartForm(url, params, files, CONNECT_TIMEOUT, SO_TIMEOUT);
	}
	
	public static String postMultipartForm(String url, Map<String, String> params, Map<String, File> files, Integer connectTimeout, Integer soTimeout) throws ConnectTimeoutException, SocketTimeoutException, Exception {
		return postMultipartForm(url, null, params, files, connectTimeout, soTimeout);
	}
	
	public static String postMultipartForm(String url, Map<String, String> headers, Map<String, String> params, Map<String, File> files, Integer connectTimeout, Integer soTimeout) throws ConnectTimeoutException, SocketTimeoutException, Exception {
		return postMultipartForm(url, CHARSET, headers, params, files, connectTimeout, soTimeout);
	}
	
	public static String get(String url) throws Exception {
		return get(url, CHARSET);
	}
	
	public static String get(String url, String charset) throws Exception {
		return get(url, charset, null);
	}
	
	public static String get(String url, String charset, Map<String, String> headers) throws Exception {
		return get(url, charset, headers, CONNECT_TIMEOUT, SO_TIMEOUT);
	}

	/**
	 * 提交form表单
	 * 
	 * @param url
	 * @param params
	 * @param connectTimeout
	 * @param soTimeout
	 * @return
	 * @throws ConnectTimeoutException
	 * @throws SocketTimeoutException
	 * @throws Exception
	 */
	public static String postForm(String url, String charset, Map<String, String> headers, Map<String, String> params, Integer connectTimeout, Integer soTimeout) throws ConnectTimeoutException, SocketTimeoutException, Exception {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {
			HttpPost post = new HttpPost(url);
			
			//add http header
			if (headers != null && !headers.isEmpty()) {
				for (Entry<String, String> entry : headers.entrySet()) {
					post.addHeader(entry.getKey(), entry.getValue());
				}
			}
			
			//set post form params
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> formParams = new ArrayList<NameValuePair>();
				for (Entry<String, String> entry : params.entrySet()) {
					formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
				post.setEntity(entity);
			}

			Builder customReqConf = RequestConfig.custom();
			if (connectTimeout != null) {
				customReqConf.setConnectTimeout(connectTimeout);
			}
			if (soTimeout != null) {
				customReqConf.setSocketTimeout(soTimeout);
			}
			post.setConfig(customReqConf.build());
			
			httpClient = getHttpClient();
			response = httpClient.execute(post);
			return IOUtils.toString(response.getEntity().getContent(), charset);
		} finally {
			if (response != null) {
				EntityUtils.consume(response.getEntity()); // Closing the input stream will trigger connection release
				response.close();
			}
			if(httpClient != null) {
				httpClient.close();
			}
		}
	}
	
	public static String postMultipartForm(String url, String charset, Map<String, String> headers, Map<String, String> params, Map<String, File> files, Integer connectTimeout, Integer soTimeout) throws ConnectTimeoutException, SocketTimeoutException, Exception {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {
			HttpPost post = new HttpPost(url);
			
			//add http header
			if (headers != null && !headers.isEmpty()) {
				for (Entry<String, String> entry : headers.entrySet()) {
					post.addHeader(entry.getKey(), entry.getValue());
				}
			}
			
			//set multipart
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
			if (params != null && !params.isEmpty()) {
				for(Entry<String, String> entry : params.entrySet()) {
					multipartEntityBuilder.addTextBody(entry.getKey(), entry.getValue());
				}
			}
			if (files != null && !files.isEmpty()) {
				for(Entry<String, File> entry : files.entrySet()) {
					multipartEntityBuilder.addBinaryBody(entry.getKey(), entry.getValue());
				}				
			}
			post.setEntity(multipartEntityBuilder.build());
		
			Builder customReqConf = RequestConfig.custom();
			if (connectTimeout != null) {
				customReqConf.setConnectTimeout(connectTimeout);
			}
			if (soTimeout != null) {
				customReqConf.setSocketTimeout(soTimeout);
			}
			post.setConfig(customReqConf.build());
			
			httpClient = getHttpClient();
			response = httpClient.execute(post);
			return IOUtils.toString(response.getEntity().getContent(), charset);
		} finally {
			if (response != null) {
				EntityUtils.consume(response.getEntity()); // Closing the input stream will trigger connection release
				response.close();
			}
			if(httpClient != null) {
				httpClient.close();
			}
		}
	}
	
	/**
	 * 发送一个 Post 请求, 使用指定的字符集编码.
	 * 
	 * @param url
	 * @param body RequestBody
	 * @param mimeType 例如 application/xml "application/x-www-form-urlencoded" a=1&b=2&c=3
	 * @param charset 编码
	 * @param connectTimeout 建立链接超时时间,毫秒.
	 * @param soTimeout 响应超时时间,毫秒.
	 * @return ResponseBody, 使用指定的字符集编码.
	 * @throws ConnectTimeoutException 建立链接超时异常
	 * @throws SocketTimeoutException 响应超时
	 * @throws Exception
	 */
	public static String postBody(String url, String body, String mimeType, Map<String, String> headers, String charset, Integer connectTimeout, Integer soTimeout) throws ConnectTimeoutException, SocketTimeoutException, Exception {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {
			HttpPost post = new HttpPost(url);
			
			//add http header
            if (headers != null && !headers.isEmpty()) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    post.addHeader(entry.getKey(), entry.getValue());
                }
            }
            
			//set post mime body
			if (StringUtils.isNotBlank(body)) {
				HttpEntity entity = new StringEntity(body, ContentType.create(mimeType, charset));
				post.setEntity(entity);
			}
			
			Builder customReqConf = RequestConfig.custom();
			if (connectTimeout != null) {
				customReqConf.setConnectTimeout(connectTimeout);
			}
			if (soTimeout != null) {
				customReqConf.setSocketTimeout(soTimeout);
			}
			post.setConfig(customReqConf.build());

			httpClient = getHttpClient();
			response = httpClient.execute(post);
			return IOUtils.toString(response.getEntity().getContent(), charset);
		} finally {
			if (response != null) {
				EntityUtils.consume(response.getEntity()); // Closing the input stream will trigger connection release
				response.close();
			}
			if(httpClient != null) {
				httpClient.close();
			}
		}
	}
	
	/**
	 * 发送一个 GET 请求
	 * 
	 * @param url
	 * @param charset
	 * @param connectTimeout 建立链接超时时间,毫秒.
	 * @param soTimeout 响应超时时间,毫秒.
	 * @return
	 * @throws ConnectTimeoutException 建立链接超时
	 * @throws SocketTimeoutException 响应超时
	 * @throws Exception
	 */
	public static String get(String url, String charset, Map<String, String> headers, Integer connectTimeout, Integer soTimeout) throws ConnectTimeoutException, SocketTimeoutException, Exception {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {
			HttpGet get = new HttpGet(url);
			
			if(headers != null && !headers.isEmpty()) {
				for(Entry<String, String> entry : headers.entrySet()) {
					get.addHeader(entry.getKey(), entry.getValue());
				}
			}
			
			Builder customReqConf = RequestConfig.custom();
			if (connectTimeout != null) {
				customReqConf.setConnectTimeout(connectTimeout);
			}
			if (soTimeout != null) {
				customReqConf.setSocketTimeout(soTimeout);
			}
			get.setConfig(customReqConf.build());

			httpClient = getHttpClient();
			response = httpClient.execute(get);
			return IOUtils.toString(response.getEntity().getContent(), charset);
		} finally {
			if (response != null) {
				EntityUtils.consume(response.getEntity()); // Closing the input stream will trigger connection release
				response.close();
			}
			if(httpClient != null) {
				httpClient.close();
			}
		}
	}
	
	public static void download(String url, String store) throws ConnectTimeoutException, SocketTimeoutException, Exception {
		download(url, store, null, null, -1);
	}
	
	public static void download(String url, String store, String proxyScheme, String proxyHost, int proxyPort) throws ConnectTimeoutException, SocketTimeoutException, Exception {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			HttpGet get = new HttpGet(url);
			
			if(StringUtils.isNotEmpty(proxyScheme) && StringUtils.isNotEmpty(proxyHost) && proxyPort > 0) {
				HttpHost proxy = new HttpHost(proxyHost, proxyPort, proxyScheme);
				get.setConfig(RequestConfig.custom().setProxy(proxy).build());				
			}
			
			httpClient = getHttpClient();
			response = httpClient.execute(get);
			
			bis = new BufferedInputStream(response.getEntity().getContent());
			bos = new BufferedOutputStream(new FileOutputStream(store));
			int len = -1;
			byte[] buf = new byte[1024000];
			while((len = bis.read(buf, 0, buf.length)) > 0) {
				bos.write(buf, 0, len);
				bos.flush();
			}
		} catch(Exception e) {
			throw e;
		} finally {
			if(bos != null) bos.close();
			if(bis != null) bis.close();
			
			if (response != null) {
				EntityUtils.consume(response.getEntity()); // Closing the input stream will trigger connection release
				response.close();
			}
			if(httpClient != null) {
				httpClient.close();
			}
		}
	}
	
}