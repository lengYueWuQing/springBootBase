package cn.sh.base.service.translate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class RequestUtils {

	/**
	 * 获取data JSONObject
	 * @param request
	 * @param response
	 * @return
	 */
	public static JSONObject getDataObject(HttpServletRequest request, HttpServletResponse response){
		String data = request.getAttribute("data").toString();
		return JSONObject.parseObject(data);
	}
	
	/**
	 * 获取data JSONArray
	 * @param request
	 * @param response
	 * @return
	 */
	public static JSONArray getDataArray(HttpServletRequest request, HttpServletResponse response){
		String data = request.getAttribute("data").toString();
		return JSONArray.parseArray(data);
	}
	
	public static String getParamStr(Object object){
		if(object==null){
			return null;
		}else{
			return object.toString().trim();
		}
	}
}
