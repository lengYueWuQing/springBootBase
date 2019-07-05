package cn.sh.base.service.translate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

public class ResponseUtils {
	
	/**
	 * 返回错误信息
	 * @param response
	 * @param object
	 */
	public static void setErrorMessage(HttpServletResponse response, String... object){
		List<String> list = Arrays.asList(object);
		ErrorMessages erms = new ErrorMessages();
		erms.setErrorMessage(list);
		try {
			response.getWriter().println(JSONObject.toJSONString(erms));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
