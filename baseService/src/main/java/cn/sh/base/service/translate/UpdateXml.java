package cn.sh.base.service.translate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * xml文件修改
 * @author hanyongtao
 *
 */
@Component
public class UpdateXml {

	private static final Logger LOG = LoggerFactory.getLogger(UpdateXml.class);
	
	public void run(HttpServletRequest request, HttpServletResponse response){
		LOG.debug("修改xml开始");
		JSONObject jSONObject = RequestUtils.getDataObject(request, response);
		if (jSONObject == null) {
			return;
		}
		String filePath = RequestUtils.getParamStr(jSONObject.get("fipa"));
		if (filePath == null) {
			ResponseUtils.setErrorMessage(response, "fipa变量未获取内容");
		}
		if (!filePath.endsWith(".xml")) {
			ResponseUtils.setErrorMessage(response, filePath + " 不是后缀.xml的文件");
		}
		File file = new File(filePath);
		if (!file.exists()) {
			ResponseUtils.setErrorMessage(response, filePath + " 获取不到本地文件");
		}
		JSONArray JSONArray = jSONObject.getJSONArray("datas");
		if(JSONArray==null || JSONArray.size()==0){
			return;
		}
		Map<String, String> translatedatas = new HashMap<String, String>();
		for(int i=0;i<JSONArray.size();i++){
			JSONObject jsonObject2 = JSONArray.getJSONObject(i);
			String name = RequestUtils.getParamStr(jsonObject2.get("name"));
			if("".equals(name)){
				continue;
			}
			String trco = RequestUtils.getParamStr(jsonObject2.get("trco"));
			if("".equals(trco)){
				continue;
			}
			translatedatas.put(name, trco);
			
		}
		if(translatedatas.size()<=0){
			return;
		}
		if(!DOM4JAnyle(file, translatedatas)){
			ResponseUtils.setErrorMessage(response, filePath + " 解析本地文件失败");
		}else{
			try {
				response.getWriter().println("{}");
			} catch (IOException e) {
				LOG.error("", e);
			}
		}
		LOG.debug("修改xml结束");
	}

	public static boolean DOM4JAnyle(File file, Map<String, String> translatedatas) {
		// 解析books.xml文件
		// 创建SAXReader的对象reader
		SAXReader reader = new SAXReader();
		try {
			// 通过reader对象的read方法加载books.xml文件,获取docuemnt对象。
			FileInputStream inputStream = null;
			try {
				inputStream = new FileInputStream(file);
			} catch (FileNotFoundException e1) {
				LOG.error("获取文件流", e1);
				return false;
			}
			Document document = reader.read(inputStream);
			// 通过document对象获取根节点bookstore
			Element bookStore = document.getRootElement();
			boolean flag = elementAnyle(bookStore, translatedatas);
			try {
				inputStream.close();
			} catch (IOException e) {
				LOG.error("关闭文件流", e);
				return false;
			}
			if (!flag) {
				return false;
			}else{
				OutputFormat format = OutputFormat.createPrettyPrint();
				//设置编码格式
				format.setEncoding("UTF-8");
				format.setIndent(true); // 设置是否缩进
				format.setIndent("    "); // 以四个空格方式实现缩进
				format.setNewlines(true); // 设置是否换行
				XMLWriter xMLWriter = null;
				FileOutputStream outputStream = null;
				try {
					outputStream = new FileOutputStream(file);
					xMLWriter = new XMLWriter(format);
					xMLWriter.setOutputStream(outputStream);
					xMLWriter.write(document);
				} catch (Exception e) {
					LOG.error("修改xml错误", e);
					return false;
				}finally {
					if(xMLWriter!=null){
						try {
							xMLWriter.close();
						} catch (IOException e) {
							LOG.error("关闭XMLWriter", e);
						}
					}
					if(outputStream!=null){
						try {
							outputStream.close();
						} catch (IOException e) {
							LOG.error("关闭输出流", e);
						}
					}
					
				}
				
			}
		} catch (DocumentException e) {
			LOG.error("", e);
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
									String con = translatedatas.get(attrValue);
									book.setText(con);

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
