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
	/*@Autowired
	private UserService userService;
	@RequestMapping("/users")
	public void getUserInfo(@RequestParam(value="loginName",required=false)String loginName,@RequestParam(value="pass",required=false)String pass, HttpServletResponse response){
		System.out.println("访问users");
		Logger LOG=LoggerFactory.getLogger(Application.class);
		//return userService.getUserMess(loginName, pass);
		//Logger LOG=Logger.getLogger(Application.class);
		LOG.error("success");
		LOG.info("success");
		LOG.warn("success");
		try {
			response.getWriter().print("{\"id\"=5,\"name\"=\"haah\"}");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}*/
	
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
