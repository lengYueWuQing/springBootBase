package cn.sh.base;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.alibaba.fastjson.JSONObject;

import cn.sh.base.service.translate.ResponseUtils;

@Component
public class InterceptorForContext implements HandlerInterceptor {

	private static final Logger LOG = LoggerFactory.getLogger(InterceptorForContext.class);
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String path = request.getRequestURI();
		String method = request.getMethod();
		response.setHeader("Access-Control-Allow-Origin", "*");  
		  
		response.setHeader("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With");  
  
		response.setHeader("Access-Control-Allow-Methods","POST");  
  
		response.setHeader("X-Powered-By","Jetty");  
  
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		if(method.equalsIgnoreCase("get")) {
			return true;
		}
		StringBuilder content = new StringBuilder();
		String line = null;
		try {
			BufferedReader bufferReader = new BufferedReader(request.getReader());
			while ((line = bufferReader.readLine()) != null) {
				content.append(line);
			}
		} catch (IOException e) {
			LOG.error("获取接口：{} 内容失败", path, e);
			ResponseUtils.setErrorMessage(response, "获取内容失败");
			return false;
		}
		LOG.debug("接口：{},获取内容：{}", path, content.toString());
		String data = content.toString();
		if(!"".equals(data)){
			JSONObject json = JSONObject.parseObject(data);
			if(json!=null){
				Object dataObject = json.get("data");
				
				if(dataObject!=null){
					Object headObject = json.get("head");
				    Object pageObject = json.get("page");
					request.setAttribute("data", dataObject.toString().trim());
					request.setAttribute("page", pageObject!=null ? pageObject.toString().trim():null);
					request.setAttribute("head", headObject!=null ? headObject.toString().trim():null);
					return true;
				}
			}
		}
		ResponseUtils.setErrorMessage(response, "请求数据格式有误");
		return false;
	}
	
}
