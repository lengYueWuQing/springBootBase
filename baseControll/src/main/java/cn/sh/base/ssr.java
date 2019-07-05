package cn.sh.base;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.sh.Utils.AESUtils;

@SuppressWarnings("deprecation")
public class ssr {
	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
	public static void main(String[] args) {
		String url = "https://lncn.org/api/ssr";
		Long co = (-5) * (new Date()).getTime();
		String code = btoa(co.toString());
		System.out.println(code);
        String key = "5432182344981165";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("code", code));

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("accept", "application/json, text/plain, */*");
		headers.put("Connection", "keep-alive");
		headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		headers.put("Referer", "https://lncn.org/");
		headers.put("TE", "Trailers");
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
			String result = doPost(url, entity, null);
			System.out.println(result);
			JSONObject json = JSONObject.parseObject(result);
			
			if(json.get("ssrs")==null){
				return;
			}
			String date ="";
			if(json.get("date")==null){
				date = json.get("date").toString();
			}
			Decoder decoder = Base64.getDecoder();
    		byte[] bytes = decoder.decode(json.get("ssrs").toString());
    		String datas = new String(AESUtils.decrypt(bytes, key.getBytes("utf-8")), "utf-8");
			System.out.println(datas);
			JSONArray jSONArray = JSONObject.parseArray(datas);
			for(int i=0;i<jSONArray.size();i++){
				JSONObject jSONObject = jSONArray.getJSONObject(i);
				String ssrMessge = jSONObject.get("ssr")==null? "":jSONObject.get("ssr").toString();
				String ssrUrl = jSONObject.get("ssrUrl")==null? "":jSONObject.get("ssrUrl").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}

	private final static RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(8000)
			.setConnectTimeout(5000).setConnectionRequestTimeout(5000).build();

	public static String doPost(String url, StringEntity entity, Map<String, String> headers) throws Exception {
		if (url == null || "".equals(url = url.trim())) {
			throw new Exception("url不存在");
		}
		String result = null;
		SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
				SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
				NoopHostnameVerifier.INSTANCE);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(scsf)
				.setDefaultRequestConfig(defaultRequestConfig).build();
		HttpPost postRequest = new HttpPost(url);
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> header : headers.entrySet()) {
				postRequest.addHeader(header.getKey(), header.getValue());
			}
		}
		if (entity != null) {
			entity.setContentEncoding("UTF-8");// 相当于在head设置Content-Encoding
			postRequest.setEntity(entity);
		}
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(postRequest);
			if (response != null) {
				result = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				EntityUtils.consumeQuietly(entity);
				if (response != null) {
					HttpClientUtils.closeQuietly(response);
				}
				HttpClientUtils.closeQuietly(httpClient);
			} catch (Exception e) {

			}
		}
		return result;
	}

	private static String base64hash = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

	public static boolean isMatcher(String inStr, String reg) {
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(inStr);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * btoa method
	 * 
	 * @param inStr
	 * @return
	 */
	public static String btoa(String inStr) {

		// if (/([^\u0000-\u00ff])/.test(s)) {
		// throw new Error('INVALID_CHARACTER_ERR');
		// }
		if (inStr == null || isMatcher(inStr, "([^\\u0000-\\u00ff])")) {
			return null;
		}
		StringBuilder result = new StringBuilder();
		int i = 0;
		int mod = 0;
		int ascii;
		int prev = 0;
		while (i < inStr.length()) {
			ascii = inStr.charAt(i);
			mod = i % 3;
			switch (mod) {
			case 0:
				result.append(String.valueOf(base64hash.charAt(ascii >> 2)));
				break;
			case 1:
				result.append(String.valueOf(base64hash.charAt((prev & 3) << 4 | (ascii >> 4))));
				break;
			case 2:
				result.append(String.valueOf(base64hash.charAt((prev & 0x0f) << 2 | (ascii >> 6))));
				result.append(String.valueOf(base64hash.charAt(ascii & 0x3f)));
				break;
			}
			prev = ascii;
			i++;
		}

		if (mod == 0) {
			result.append(String.valueOf(base64hash.charAt((prev & 3) << 4)));
			result.append("==");
		} else if (mod == 1) {
			result.append(String.valueOf(base64hash.charAt((prev & 0x0f) << 2)));
			result.append("=");
		}
		return result.toString();
	}
	
	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param password
	 *            解密密钥
	 * @return 原文
	 */
	public static byte[] decrypt(byte[] content, byte[] password) throws Exception {
		// 生成密钥
		SecretKeySpec key = new SecretKeySpec(password, "AES");
		// 创建密码器
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		// 初始化密码器，设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] result = cipher.doFinal(content);
		return result;
	}

}
