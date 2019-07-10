package cn.sh.base;

import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import cn.sh.Utils.FileUtils;

@SpringBootApplication
public class Application {

	private static final Logger LOG = LoggerFactory.getLogger(Application.class);
	
	public static String start(String[] args) {
		LOG.info("启动SpringApplication");
		//SpringApplication.run(Application.class, args);
		String text = "start";
		try {
			File file = new File("./bin/shutdown.pid");
			SpringApplicationBuilder app = new SpringApplicationBuilder(Application.class);
			app.build().addListeners(new ApplicationPidFileWriter(file));
			app.run();
			text = "end";
			try {
				text = FileUtils.readToString(file);
			} catch (IOException e) {
				LOG.error("获取文件信息错误", e);
				text = null;
			}
		} catch (Exception e) {
			LOG.error("启动出错", e);
		}
		
		LOG.info("启动SpringApplication结束");
		
		return text;
		
	}
	
	
	
	public static void main(String[] args) {
		start(args);
		
	}

}
