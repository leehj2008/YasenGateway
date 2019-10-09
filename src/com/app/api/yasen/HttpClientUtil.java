package com.app.api.yasen;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {
	private RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000)
			.setConnectionRequestTimeout(15000).build();

	private static HttpClientUtil instance = null;


	Logger log = LoggerFactory.getLogger(this.getClass());
	public static HttpClientUtil getInstance() {
		if (instance == null) {
			instance = new HttpClientUtil();
		}
		return instance;
	}

	/**
	 * ���ͱ����⴫�Ͳ������� PUT
	 * 
	 * @param httpUrl
	 * @param parMap
	 * @return
	 */
	public String sendHttpPut(String httpUrl, Map<String, Object> parMap) throws Exception {
		log.info("httpUrl="+httpUrl);
		HttpPut httpPut = new HttpPut(httpUrl);// ����httpPost
		Map<String, String> headerMap = (Map<String, String>) parMap.get("header");
		String param = parMap.get("paramObj") == null ? "" : parMap.get("paramObj").toString();
		if (headerMap != null && headerMap.size() > 0) {
			for (String headerKey : headerMap.keySet()) {
				httpPut.setHeader(headerKey, headerMap.get(headerKey));
			}
		}

		if (!param.equals("")) {
			httpPut.setEntity(new StringEntity(param, "UTF-8"));
		}
		return sendHttpRequest(httpPut, parMap);
	}

	/**
	 * ���� post����
	 * 
	 * @param httpUrl
	 *            ��ַ
	 * @param parMap
	 *            ����
	 */
	public String sendHttpPost(String httpUrl, Map<String, Object> parMap) throws Exception {
		log.info("httpUrl="+httpUrl);
		
		HttpPost httpPost = new HttpPost(httpUrl);// ����httpPost
		Map<String, String> headerMap = (Map<String, String>) parMap.get("header");
		String param = parMap.get("paramObj") == null ? "" : parMap.get("paramObj").toString();
		if (headerMap != null && headerMap.size() > 0) {
			for (String headerKey : headerMap.keySet()) {
				httpPost.setHeader(headerKey, headerMap.get(headerKey));
			}
		}
		if (!param.equals("")) {
			httpPost.setEntity(new StringEntity(param, "UTF-8"));
		}
		return sendHttpRequest(httpPost, parMap);
	}

	/**
	 * ���� get��������
	 * 
	 * @param httpUrl
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	public String sendHttpGet(String httpUrl, Map<String, Object> parMap) throws Exception {
		HttpGet httpGet = new HttpGet();// ����get����
		Map<String, String> headerMap = (Map<String, String>) parMap.get("header");
		Map<String, String> param = parMap.get("paramObj").equals("") ? new HashMap<String, String>()
				: (Map<String, String>) parMap.get("paramObj");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (headerMap != null && headerMap.size() > 0) {
			for (String headerKey : headerMap.keySet()) {
				httpGet.setHeader(headerKey, headerMap.get(headerKey));
			}
		}
		if (param != null && param.size() > 0) {
			for (String key : param.keySet()) {
				nameValuePairs.add(new BasicNameValuePair(key, param.get(key)));
			}
		}
		String str = EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		URI uri = new URI(httpUrl + "?" + str);
		httpGet.setURI(uri);
		return sendHttpRequest(httpGet, parMap);
	}

	/**
	 * ����Get����
	 * 
	 * @param httpRequest
	 * @param parMap
	 * @return
	 */
	private String sendHttpRequest(HttpRequestBase httpRequest, Map<String, Object> parMap) throws Exception {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			String contentType = parMap.get("contentType").toString();
			httpRequest.addHeader("Content-type", contentType);
			// httpRequest.setHeader("Accept", "application/json");
			httpRequest.setConfig(requestConfig);
			httpClient = HttpClients.createDefault();
			response = httpClient.execute(httpRequest);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		} finally {
			this.closeResource(httpClient, response);
		}
		return responseContent;
	}

	/**
	 * ���ݲ����DELETE ����
	 * 
	 * @param httpUrl
	 * @param parMap
	 * @return
	 */
	public String delete(String httpUrl, Map<String, Object> parMap) throws Exception {
		HttpDeleteWithBody httpDeleteWithBody = new HttpDeleteWithBody(httpUrl);
		Map<String, String> headerMap = (Map<String, String>) parMap.get("header");
		String delParam = parMap.get("paramObj") == null ? "" : parMap.get("paramObj").toString();
		String contentType = parMap.get("contentType").toString();
		if (headerMap != null && headerMap.size() > 0) {
			for (String headerKey : headerMap.keySet()) {
				httpDeleteWithBody.setHeader(headerKey, headerMap.get(headerKey));
			}
		}
		httpDeleteWithBody.addHeader("Content-type", contentType);
		// httpDeleteWithBody.setHeader("Accept", "application/json");
		httpDeleteWithBody.setEntity(new StringEntity(delParam, "utf-8"));
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = httpClient.execute(httpDeleteWithBody);
		HttpEntity entity = response.getEntity();
		String responseContent = EntityUtils.toString(entity, "UTF-8");
		httpClient.getConnectionManager().closeExpiredConnections();
		return responseContent;
	}

	private void closeResource(CloseableHttpClient httpClient, CloseableHttpResponse response) throws Exception {
		// �ر�����,�ͷ���Դ
		if (response != null) {
			response.close();
		}
		if (httpClient != null) {
			httpClient.close();
		}
	}

	public static String transmit(Map<String, Object> map) throws Exception {
		String returnData = "";
		HttpClientUtil client = HttpClientUtil.getInstance();
		String method = map.get("method").toString();
		String httpUrl = map.get("url").toString();
		if (method.equals("POST")) {
			returnData = client.sendHttpPost(httpUrl, map);
		} else if (method.equals("GET")) {
			returnData = client.sendHttpGet(httpUrl, map);
		} else if (method.equals("PUT")) {
			returnData = client.sendHttpPut(httpUrl, map);
		} else if (method.equals("DELETE")) {
			returnData = client.delete(httpUrl, map);
		}
		return returnData;
	}
	class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {

		public static final String METHOD_NAME = "DELETE";

		public String getMethod() {
			return METHOD_NAME;
		}

		public HttpDeleteWithBody(final String uri) {
			super();
			setURI(URI.create(uri));
		}

		public HttpDeleteWithBody(final URI uri) {
			super();
			setURI(uri);
		}

		public HttpDeleteWithBody() {
			super();
		}

	}
	
	
	
	
	
	

}
