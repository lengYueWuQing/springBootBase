package cn.sh.base.controll.translate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.sh.base.Application;
import cn.sh.base.service.translate.ResponseUtils;
import cn.sh.base.service.translate.Translate;
import cn.sh.base.service.translate.UpdateXml;
import cn.sh.base.service.translate.UploadAnalyze;
@RestController
public class Controll {
	
	private static final Logger LOG = LoggerFactory.getLogger(Application.class);
	
	@Autowired
	private UploadAnalyze uploadAnalyze;
	@RequestMapping(path = "/json/uploadFile", method = RequestMethod.POST)
	public void upload(HttpServletRequest request, HttpServletResponse response){
		try {
			uploadAnalyze.run(request, response);
		} catch (Exception e) {
			ResponseUtils.setErrorMessage(response, "解析内容失败");
			LOG.error("", e);
		}
		
	}
	
	@Autowired
	private Translate translate;
	@RequestMapping(path = "/json/translate", method = RequestMethod.POST)
	public void translate(HttpServletRequest request, HttpServletResponse response){
		try {
			translate.run(request, response);
		} catch (Exception e) {
			ResponseUtils.setErrorMessage(response, "翻译内容失败");
			LOG.error("", e);
		}
		
	}
	
	@Autowired
	private UpdateXml updateXml;
	@RequestMapping(path = "/json/saveContent", method = RequestMethod.POST)
	public void updateXml(HttpServletRequest request, HttpServletResponse response){
		try {
			updateXml.run(request, response);
		} catch (Exception e) {
			ResponseUtils.setErrorMessage(response, "保存内容失败");
			LOG.error("", e);
		}
	}
	
}
