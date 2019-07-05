package cn.sh.base;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter{

	/*绕过认证限制*/
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/static/index.html", "/static/css/**", "/static/js/**", "/static/img/**", "/static/fonts/**", "/static/content/css/**", "/static/content/js/**", "/static/content/img/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	  http.formLogin()          // 定义当需要用户登录时候，转到的登录页面。
	      .loginPage("/static/index.html")      // 设置登录页面
	      //.loginProcessingUrl("/json/index") // 自定义的登录接口
	      .and()
	      .authorizeRequests()    // 定义哪些URL需要被保护、哪些不需要被保护
	      .antMatchers("/json/**", "/shutDownService").permitAll()   // 设置所有人都可以访问登录页面
	      .anyRequest()        // 任何请求,登录后可以访问
	      .authenticated()
	      .and()
	      .csrf().disable();     // 关闭csrf防护
	}
}
