package cn.sh.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShutDownService implements ApplicationContextAware {
	private static final Logger LOG = LoggerFactory.getLogger(ShutDownService.class);
	private ApplicationContext context;

	@RequestMapping("/shutDownService")
	public String shutdownContext() {
		LOG.info("关闭服务");
		try {
			((ConfigurableApplicationContext) context).close();
		} catch (Exception e) {
			return "关闭服务失败";
		}
		
		return "关闭服务成功";
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

}
