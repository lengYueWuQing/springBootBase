package cn.sh.base.controll.translate;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.file.Files;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
	
	/**
	   * 页面修改测试
	 * @param request
	 * @param response
	 */
	@RequestMapping(path = "/json/test", method = RequestMethod.GET)
	public void htmlTest(HttpServletRequest request, HttpServletResponse response){
		try {
			response.setContentType("text/html;charset=utf-8");
			InputStream in = this.getClass().getResourceAsStream("/test.html");
			if(in == null) {
				return;
			}
			StringBuilder datas = new StringBuilder();
			BufferedInputStream buff = new BufferedInputStream(in);
			byte[] byt = new byte[1024];
			while((buff.read(byt))!= -1) {
				datas.append(new String(byt, "utf-8"));
			}
			Document document = Jsoup.parse(datas.toString(), "utf-8");
			Element element = document.getElementById("test");
			if(element != null) {
				Elements elements = element.children();
				for(Element el : elements) {
					el.html("成功");
				}
			}
			response.getWriter().print(document.html());
		} catch (Exception e) {
			ResponseUtils.setErrorMessage(response, "保存内容失败");
			LOG.error("", e);
		}
	}
	
}
