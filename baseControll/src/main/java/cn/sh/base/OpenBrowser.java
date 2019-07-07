package cn.sh.base;

import java.awt.Desktop;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

//@Component
//@Order(1)
public class OpenBrowser implements ApplicationRunner {

	private static final Logger LOG = LoggerFactory.getLogger(OpenBrowser.class);
	@Autowired
	SpringBootServerConfig springBootServerConfig;
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		String urlStr=springBootServerConfig.getUrl();
		LOG.error("打开:{} 地址浏览器开始", urlStr);
		try {
			URI uri = new URI(urlStr);
			Desktop.getDesktop().browse(uri);
		} catch (Exception e) {
			try {
				Runtime.getRuntime().exec("cmd /c start "+urlStr);
			} catch (Exception e2) {
				try {
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+urlStr);
				} catch (Exception e3) {
					LOG.error("打开{}地址浏览器失败",urlStr);
				}
			}

		}

	}

}
