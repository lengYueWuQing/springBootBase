package cn.sh.base;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.sh.base.dao.user.domain.User;
import cn.sh.base.service.translate.UserService;

@RestController
public class UserControll {
	private static final Logger LOG = LoggerFactory.getLogger(UserControll.class);
	@Autowired
	private UserService userService;
	
	@RequestMapping(path = "/json/user")
	public User getUser(@RequestParam(value="loginName",required=false)String loginName,@RequestParam(value="pass",required=false)String pass, HttpServletResponse response){
		LOG.info("获取User");
		return userService.getUser(loginName, pass);
	}
}
