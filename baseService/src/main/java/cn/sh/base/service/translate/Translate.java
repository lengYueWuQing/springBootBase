package cn.sh.base.service.translate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.sh.Utils.StringUtils;

@Service
public class Translate {

	private static final Logger LOG = LoggerFactory.getLogger(Translate.class);
	public void run(HttpServletRequest request, HttpServletResponse response) throws Exception{
		LOG.debug("翻译开始");
		List<String> splitArr = Arrays.asList(" ;;;;; "," ;;;;;; "," ;;;;;; ");
		JSONArray jSONArray = RequestUtils.getDataArray(request, response);
		if(jSONArray==null){
			return;
		}
		if(jSONArray.size()<=0){
			response.getWriter().println("[]");
			return;
		}
		Map<String, JSONObject> mapData = new HashMap<String, JSONObject>();
		StringBuilder dataStr = new StringBuilder();
		int splitIndex=0;
		boolean flag = true;
		List<String> tranlateList = new ArrayList<String>();
		for(int i=0;i<jSONArray.size();i++){
			JSONObject json = jSONArray.getJSONObject(i);
			if(json==null){
				ResponseUtils.setErrorMessage(response, "未获取到翻译内容");
				return;
			}
			Object nameOb= json.get("name");
			String name = "";
			if(nameOb==null || "".equals(name = nameOb.toString().trim())){
				continue;
			}
			String con = RequestUtils.getParamStr(json.get("con"));
			if("".equals(con)){
				continue;
			}
			JSONObject jsondata = new JSONObject();
			jsondata.put("name", name);
			jsondata.put("con", con);
			mapData.put(name, jsondata);
			tranlateList.add(name);
			if(flag){
				dataStr.append(con);
				flag = false;
			}else{
				dataStr.append(splitArr.get(splitIndex)).append(con);
			}
			
			if(dataStr.length()>4000){
				while(true){
					String translatedStr = StringUtils.cToe(new TranslateUtils().getInstance().translateText(dataStr.toString(), "auto", "zh_cn"));
					String[] translateds = translatedStr.split(splitArr.get(splitIndex).trim(), -1);
					if(translateds.length==tranlateList.size()){
						flag = true;
						dataStr = new StringBuilder();
						for(int j=0;j<translateds.length;j++){
							JSONObject jSONObje = mapData.get(tranlateList.get(j));
							jSONObje.put("trco", translateds[j]);
						}
						tranlateList.clear();
						if(i!=(jSONArray.size()-1)){
							Thread.sleep(1500);
						}
						break;
					}else{
						splitIndex++;
						if(splitIndex>(splitArr.size()-1)){
							LOG.warn("尝试 {} 次返回翻译结果个数少", splitArr.size());
							splitIndex =0;
							flag = true;
							dataStr = new StringBuilder();
							tranlateList.clear();
							break;
						}
						dataStr = new StringBuilder(dataStr.toString().replaceAll(splitArr.get(splitIndex-1).trim(), splitArr.get(splitIndex).trim()));
					}
				}
			}
		}
		while(true){
			if(dataStr.length()>0){
				String translatedStr = StringUtils.cToe(new TranslateUtils().getInstance().translateText(dataStr.toString(), "auto", "zh_cn"));
				String[] translateds = translatedStr.split(splitArr.get(splitIndex).trim(), -1);
				if(translateds.length==tranlateList.size()){
					
					for(int j=0;j<translateds.length;j++){
						JSONObject jSONObje = mapData.get(tranlateList.get(j));
						jSONObje.put("trco", translateds[j]);
					}
					tranlateList.clear();
					break;
				}else{
					splitIndex++;
					if(splitIndex>(splitArr.size()-1)){
						LOG.warn("尝试 {} 次返回翻译结果个数少", splitArr.size());
						
						tranlateList.clear();
						dataStr = new StringBuilder();
						break;
					}
					
					dataStr = new StringBuilder(dataStr.toString().replaceAll(splitArr.get(splitIndex-1).trim(), splitArr.get(splitIndex).trim()));
				}
			}	
		}
		JSONArray JSONArray = new JSONArray();
		for(Entry<String, JSONObject> data:mapData.entrySet()){
			JSONArray.add(data.getValue());
		}
		response.getWriter().println(JSONArray.toJSONString());
		LOG.debug("翻译结束");
	}
	
}
