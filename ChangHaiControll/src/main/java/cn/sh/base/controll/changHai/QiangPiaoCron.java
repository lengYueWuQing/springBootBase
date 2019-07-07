package cn.sh.base.controll.changHai;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.sh.base.dao.changHai.ChangHaiDao;

@Component
@EnableScheduling
@PropertySource("classpath:cron.properties")
public class QiangPiaoCron {

	@Autowired
	private ChangHaiDao changHaiDao;
	
	@Value("${QiangPiaoCron.doctor.name}")
	private String doctorName;
	
	private static final Logger LOG = LoggerFactory.getLogger(QiangPiaoCron.class);
	@Scheduled(fixedDelayString = "${QiangPiaoCron.run}")
	public void run() {
		LOG.info("开始抢票");
		System.out.println(doctorName);
		//changHaiDao.insertSendMailInfo(UUID.randomUUID().toString(), "抢票测试", null, new Date());
		changHaiDao.insertQiangPiaoInfo(UUID.randomUUID().toString(), doctorName, new Date());
		
		
		
		LOG.info("结束抢票");
		
	}
}
