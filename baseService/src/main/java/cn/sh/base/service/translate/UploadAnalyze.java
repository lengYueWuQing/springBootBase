package cn.sh.base.service.translate;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.sh.Utils.StringUtils;

@Service
public class UploadAnalyze {

	private static final Logger LOG = LoggerFactory.getLogger(UploadAnalyze.class);

	public void run(HttpServletRequest request, HttpServletResponse response) {
		LOG.debug("解析文件开始");

		JSONObject jSONObject = RequestUtils.getDataObject(request, response);
		if (jSONObject == null) {
			return;
		}
		String filePath = RequestUtils.getParamStr(jSONObject.get("fipa"));
		if (filePath == null) {
			ResponseUtils.setErrorMessage(response, "fipa变量未获取内容");
			return;
		}
		if (!filePath.endsWith(".xml")) {
			ResponseUtils.setErrorMessage(response, filePath + " 不是后缀.xml的文件");
			return;
		}
		File file = new File(filePath);
		if (!file.exists()) {
			ResponseUtils.setErrorMessage(response, filePath + " 获取不到本地文件");
			return;
		}
		Map<String, String> translatedatas = new HashMap<String, String>();
		if(!DOM4JAnyle(file, translatedatas)){
			ResponseUtils.setErrorMessage(response, filePath + " 解析本地文件失败");
		}else{
			JSONArray jSONArray = new JSONArray();
			for(Entry<String, String> translatedata:translatedatas.entrySet()){
				JSONObject json = new JSONObject();
				json.put("name", translatedata.getKey());
				json.put("con", translatedata.getValue());
				jSONArray.add(json);
			}
			try {
				response.getWriter().println(jSONArray.toJSONString());
			} catch (IOException e) {
				LOG.error("", e);
			}
			
		}
	}

	public static boolean DOM4JAnyle(File file, Map<String, String> translatedatas) {
		// 解析books.xml文件
		// 创建SAXReader的对象reader
		SAXReader reader = new SAXReader();
		try {
			// 通过reader对象的read方法加载books.xml文件,获取docuemnt对象。
			Document document = reader.read(file);
			// 通过document对象获取根节点bookstore
			Element bookStore = document.getRootElement();

			if (!elementAnyle(bookStore, translatedatas)) {
				return false;
			}
		} catch (DocumentException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 解析Element
	 * 
	 * @param element
	 * @param dateList
	 * @param xmlAttribute
	 * @param message
	 */
	public static boolean elementAnyle(Element element, Map<String, String> translatedatas) {
		@SuppressWarnings("rawtypes")
		Iterator it = element.elementIterator();
		// 遍历迭代器，获取根节点中的信息（书籍）
		while (it != null && it.hasNext()) {
			Element book = (Element) it.next();
			if (!book.isTextOnly()) {
				elementAnyle(book, translatedatas);
			}else{
				// 获取book的属性名以及 属性值
				@SuppressWarnings("unchecked")
				List<Attribute> bookAttrs = (List<Attribute>) (book.attributes());
				if (bookAttrs != null) {
					for (Attribute attr : bookAttrs) {
						String attrValue = attr.getValue().trim();
						String attrName = attr.getName().trim();
						try {
							attrValue = new String(attr.getValue().trim().getBytes(), "utf-8");
							attrName = new String(attr.getName().trim().getBytes(), "utf-8");
						} catch (UnsupportedEncodingException e) {

						}
						attrName = attr.getName().trim();
						if ("name".equals(attrName) && !"".equals(attrValue)) {
							// 全是有汉字 ，字符 ，符号 组成的内容不翻译
							String xinxi = book.getTextTrim();
							if (xinxi != null && !"".equals(xinxi = xinxi.trim())) {
								if (translatedatas.containsKey(attrValue)) {
									LOG.warn("name=" + attrValue + " 重复，请删除一个");
									continue;

								}
								
								Pattern p = Pattern.compile("@android:string/");
								Pattern p1 = Pattern.compile("@:string/");
								Matcher m = p.matcher(xinxi);
								Matcher m1 = p1.matcher(xinxi);
								if (m.find() || m1.find()) {
									continue;
								}

								// 排除不需要翻译的
								String[] moveDatas = { "true", "false", "auto" };
								if (Arrays.asList(moveDatas).contains(xinxi)) {
									
									continue;
								}
								String tmp = xinxi.replaceAll("\\p{P}", "");// 去掉所有字符符号
								tmp = tmp.replaceAll("\\s*", "");
								char[] by = tmp.toCharArray();
								for (int z = 0; z < by.length; z++) {
									if (!StringUtils.isChineseChar(by[z]) && !StringUtils.isNumeric(by[z])) {
										translatedatas.put(attrValue, xinxi);
										break;
									}
								}

							}
						}

					}
				}
				
			}

		}
		return true;
	}

}
