package cn.sh.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("deprecation")
public class HttpUtils {
	
	private static RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(8000)
			  .setConnectTimeout(5000)
			  .setConnectionRequestTimeout(5000)
			  .build();

	/**
	 * post请求
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url, JSONObject params) throws Exception {
		String entity = null;
		if(params!=null){
			entity = params.toString().trim();
		}
		return doPost(url, entity, null, null);
		
	}
	
	/**
	 * post请求
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url, JSONObject params, Map<String, String> headers) throws Exception {
		String entity = null;
		if(params!=null){
			entity = params.toString().trim();
		}
		return doPost(url, entity, headers, null);
		
	}
	
	/**
	 * post请求
	 * @param url
	 * @param params
	 * @param headers
	 * @param requestConfig
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url, JSONObject params, Map<String, String> headers, RequestConfig requestConfig) throws Exception {
		String entity = null;
		if(params!=null){
			entity = params.toString().trim();
		}
		return doPost(url, entity, headers, requestConfig);
		
	}
	
	/**
	 * post请求
	 * @param url
	 * @param params
	 * @param headers
	 * @param requestConfig
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url, String params, Map<String, String> headers, RequestConfig requestConfig) throws Exception {
		StringEntity entity = null;
		if(params != null && !"".equals(params = params.trim())){
			entity = new StringEntity(params, Charset.forName("UTF-8"));
		}
		return doPost(url, entity, headers, requestConfig);
		
	}
	
	/**
	 * post请求
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url, List<NameValuePair> params) throws Exception {
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
		return doPost(url, entity, null, null);
		
	}
	
	/**
	 * post请求
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url, List<NameValuePair> params, Map<String, String> headers) throws Exception {
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
		return doPost(url, entity, headers, null);
		
	}
	
	/**
	 * post请求
	 * @param url
	 * @param params
	 * @param headers
	 * @param requestConfig
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url, List<NameValuePair> params, Map<String, String> headers, RequestConfig requestConfig) throws Exception {
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
		return doPost(url, entity, headers, requestConfig);
		
	}
	
	/**
	 * post请求
	 * @param url
	 * @param entity
	 * @param headers
	 * @param requestConfig
	 * @return
	 * @throws Exception
	 */

	public static String doPost(String url, StringEntity entity, Map<String, String> headers, RequestConfig requestConfig) throws Exception {
		if(url==null || "".equals(url = url.trim())){
			throw new Exception("url不存在");
		}
		if(requestConfig==null){
			requestConfig = defaultRequestConfig;
		}
		String result = null;
		SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(), NoopHostnameVerifier.INSTANCE);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(scsf).setDefaultRequestConfig(requestConfig).build();
		HttpPost postRequest = new HttpPost(url);
		if(headers!=null && headers.size()>0){
			for(Map.Entry<String, String> header:headers.entrySet()){
				postRequest.addHeader(header.getKey(), header.getValue());
			}
		}
		if(entity!=null){
			entity.setContentEncoding("UTF-8");//相当于在head设置Content-Encoding
			postRequest.setEntity(entity);
		}
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(postRequest);
			if (response != null){
				result = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				EntityUtils.consumeQuietly(entity);
				if(response!=null){
					HttpClientUtils.closeQuietly(response);
				}
				HttpClientUtils.closeQuietly(httpClient);
			} catch (Exception e) {

			}
		}
		return result;
	}
	
	/**
	 * get请求
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String url, Map<String, String> params) throws Exception {
		String paramStr = splitJoinGetRequest(params);
		return doGet(url, paramStr, null, null);
	}
	
	/**
	 * get请求
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
		String paramStr = splitJoinGetRequest(params);
		return doGet(url, paramStr, headers, null);
	}
	
	/**
	 * get请求
	 * @param url
	 * @param params
	 * @param headers
	 * @param requestConfig
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String url, Map<String, String> params, Map<String, String> headers, RequestConfig requestConfig) throws Exception {
		String paramStr = splitJoinGetRequest(params);
		return doGet(url, paramStr, headers, requestConfig);
	}
	
	/**
	 * get请求   请求变量按key值递增排序
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String doAscSortGet(String url, Map<String, String> params) throws Exception {
		String paramStr = splitJoinGetAscSortRequest(params);
		return doGet(url, paramStr, null, null);
	}
	/**
	 * get请求   请求变量按key值递增排序
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public static String doAscSortGet(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
		String paramStr = splitJoinGetAscSortRequest(params);
		return doGet(url, paramStr, headers, null);
	}
	
	/**
	 * get请求   请求变量按key值递增排序
	 * @param url
	 * @param params
	 * @param headers
	 * @param requestConfig
	 * @return
	 * @throws Exception
	 */
	public static String doAscSortGet(String url, Map<String, String> params, Map<String, String> headers, RequestConfig requestConfig) throws Exception {
		String paramStr = splitJoinGetAscSortRequest(params);
		return doGet(url, paramStr, headers, requestConfig);
	}
	
	/**
	 * get请求 
	 * @param url
	 * @param paramStr  格式:  ?key=value&key=value
	 * @param headers
	 * @param requestConfig    
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String url, String paramStr, Map<String, String> headers, RequestConfig requestConfig) throws Exception {
		if(url==null || "".equals(url = url.trim())){
			throw new Exception("url不存在");
		}
		if(paramStr==null){
			paramStr = "";
		}else{
			paramStr = paramStr.trim();
		}
		if(requestConfig==null){
			requestConfig = defaultRequestConfig;
		}
		String result = null;
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
		HttpGet postRequest = new HttpGet(new StringBuilder(url).append(paramStr).toString());
		if(headers!=null && headers.size()>0){
			for(Map.Entry<String, String> header:headers.entrySet()){
				postRequest.addHeader(header.getKey(), header.getValue());
			}
		}
		
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(postRequest);
			if (response != null){
				result = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				if(response!=null){
					HttpClientUtils.closeQuietly(response);
				}
				HttpClientUtils.closeQuietly(httpClient);
			} catch (Exception e) {

			}
		}
		return result;
	}
	
	/**
	 * get请求变量拼接    ?key=value&key=value
	 * @param requestParams
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String splitJoinGetRequest(Map<String, String> requestParams) throws UnsupportedEncodingException {
		if(requestParams==null || requestParams.size()<=0){
			return "";
		}
		StringBuilder sbuilder = new StringBuilder("?");
		Iterator<Map.Entry<String, String>> it = requestParams.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			sbuilder.append(entry.getKey()).append("=");
			sbuilder.append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
		}
		String str = sbuilder.toString();
		return sbuilder.toString().substring(0, str.length()-1);
	}
	
	/**
	 * get请求变量按key递增排序拼接    ?key=value&key=value
	 * @param requestParams
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String splitJoinGetAscSortRequest(Map<String, String> requestParams) throws UnsupportedEncodingException {
		if(requestParams==null || requestParams.size()<=0){
			return "";
		}
		StringBuilder sbuilder = new StringBuilder("?");
		Map<String, String> requestParamsSort = new TreeMap<String, String>();
		Iterator<Map.Entry<String, String>> it = requestParamsSort.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			sbuilder.append(entry.getKey()).append("=");
			sbuilder.append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
		}
		String str = sbuilder.toString();
		return sbuilder.toString().substring(0, str.length()-1);
	}
	
	
}
