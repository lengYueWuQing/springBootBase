package cn.sh.base.controll.changHai;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.sh.Utils.StringUtils;
import cn.sh.Utils.mail.SendQQMailUtils;
import cn.sh.base.dao.changHai.ChangHaiDao;
import javazoom.jl.player.Player;

@Component
@EnableScheduling
@PropertySource("classpath:cron.properties")
public class QiangPiaoCron {

	@Autowired
	private static ChangHaiDao changHaiDao;

	@Value("${QiangPiaoCron.doctor.name}")
	private static String doctorName;
	@Value("${QiangPiaoCron.mail}")
	private static String mail;

	private static final Logger LOG = LoggerFactory.getLogger(QiangPiaoCron.class);

	@Scheduled(fixedDelayString = "${QiangPiaoCron.run}")
	public void run() {
		LOG.info("开始抢票");
		Date now = new Date();
		LOG.info("开始时间：{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now));
		String uuid = StringUtils.getRrandomUUID();
		try {
			start(uuid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		changHaiDao.insertSendMailInfo(uuid, mail, "抢票测试", true, new Date());
		changHaiDao.insertQiangPiaoInfo(uuid, "123", new Date());

		LOG.info("结束抢票");

	}

	public static boolean start(String uuid) throws Exception {

		List<String> emails = new ArrayList<String>();
		emails.add(mail);
		String DEPT_CODE = "100804";// 神经内科
		// DEPT_CODE = "300203";//肛肠科
		String APP_UUID = UUID.randomUUID().toString();
		System.out.println(APP_UUID);
		String PHONETYPE = "mido";// 手机型号
		String IMEI_ID = "365722dd6d73d894";// 手机imei
		String url = "https://app.quyiyuan.com/APP/appoint/action/AppointActionC.jspx?APPOINT_SOURCE=0&APP_UUID="
				+ APP_UUID + "&BUSSINESS_TYPE=2&CHANNEL_ID=&DEPT_CODE=" + DEPT_CODE + "&IMEI_ID=" + IMEI_ID
				+ "&IS_ONLINE=0&IS_REFERRAL=0&PHONEOPERATINGSYS=1&PHONETYPE=" + PHONETYPE
				+ "&PHONEVERSIONNUM=9&PUBLIC_SERVICE_TYPE=0&USER_ID=17762537&USER_VS_ID=28207469&hospitalID=210004&isLogin=true&loc=c&op=getDoctorListActionC&opVersion=2.6.83&operateCurrent_UserId=17762537&operateUserSource=0&QY_CHECK_SUFFIX=a4a59a6933b8986c6a11af52c9fc4203";
		String result = doGet(url);
		JSONObject json = JSONObject.parseObject(result);
		Boolean resultFlag = json.getBoolean("success");
		if (resultFlag == null || !resultFlag) {
			LOG.error("没有 success 字段");
			return false;
		}
		String timeStr = json.getString("time");
		if (json.getJSONObject("data") == null) {
			LOG.error("没有 data 字段");
			return false;
		}
		JSONArray jsonArray = json.getJSONObject("data").getJSONArray("rows");
		if (jsonArray == null) {
			LOG.error("没有 rows 字段");
		}
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String name = jsonObject.getString("DOCTOR_CODE");
			if (name == null) {
				name = "";
			} else {
				name = name.trim();
			}
			String zhiwei = jsonObject.getString("DOCTOR_TITLE");// 职位
			String haos = jsonObject.getString("DOCTOR_TITLE");// 号类型
			JSONArray zhibanArray = jsonObject.getJSONArray("DOCTOR_SCHEDULE_LIST");// 值班表
			int num = 3;
			for (int j = 0; j < zhibanArray.size(); j++) {
				JSONObject zhibanObject = zhibanArray.getJSONObject(j);
				String zhibanTime = zhibanObject.getString("CLINIC_DATE");
				String haoFlag = zhibanObject.getString("ISTIME");
				String zhibanType = zhibanObject.getString("CLINIC_TYPE");
				String zhibanPay = zhibanObject.getString("SUM_FEE");
				if (haoFlag != null) {
					haoFlag = haoFlag.trim();

					if (DEPT_CODE.equals("100804")) {
						if ("毕晓莹".equals(name.trim())) {
                           							
							changHaiDao.insertQiangPiaoInfo(uuid, "123", new Date());
							changHaiDao.insertSendMailInfo(uuid, mail, "抢票测试", true, new Date());
							if (noticeNum == 6 && (new SimpleDateFormat("yyyyMMdd").parse("20190729"))
									.before(new SimpleDateFormat("yyyy/MM/dd").parse(zhibanTime))) { // flag1 = false;
								SendQQMailUtils.sendHtmlMail("神经内科",
										new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), emails, null);

							}
						}
					}

					if ("1".equals(haoFlag)) {
						haoFlag = "有号";
						if (DEPT_CODE.equals("100804")) {
							if ("毕晓莹".equals(name.trim())) {

								String message = "姓名：" + name + " 职位：" + zhiwei + " 预约时间：" + zhibanTime + " 有无号："
										+ haoFlag + " 金额：" + zhibanPay + " 号类型：" + zhibanType;
								SendQQMailUtils.sendHtmlMail("神经内科", message, emails, null);
								if (noticeNum == 5) {
									startMusic();
								}

							}

						} else if (DEPT_CODE.equals("300203")) {
							if ("毕晓莹".equals(name.trim())) {
								String message = "姓名：" + name + " 职位：" + zhiwei + " 预约时间：" + zhibanTime + " 有无号："
										+ haoFlag + " 金额：" + zhibanPay + " 号类型：" + zhibanType;
								SendQQMailUtils.sendHtmlMail("肛肠科", message, emails, null);
								if (noticeNum == 5) {
									startMusic();
								}
							}

						}

					}
					if ("0".equals(haoFlag)) {
						haoFlag = "无号";
					}
					if ("2".equals(haoFlag)) {
						haoFlag = "过期";
					}
				}

				LOG.error("姓名：" + name + " 职位：" + zhiwei + " 预约时间：" + zhibanTime + " 有无号：" + haoFlag + " 金额："
						+ zhibanPay + " 号类型：" + zhibanType);
			}

		}
		return true;

	}

	private final static RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(8000)
			.setConnectTimeout(5000).setConnectionRequestTimeout(5000).build();

	@SuppressWarnings("deprecation")
	public static String doGet(String url) throws Exception {
		if (url == null || "".equals(url = url.trim())) {
			throw new Exception("url不存在");
		}
		String result = null;
		SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
				SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
				NoopHostnameVerifier.INSTANCE);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(scsf)
				.setDefaultRequestConfig(defaultRequestConfig).build();
		HttpGet httpGet = new HttpGet(url);

		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
			if (response != null) {
				result = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				if (response != null) {
					HttpClientUtils.closeQuietly(response);
				}
				HttpClientUtils.closeQuietly(httpClient);
			} catch (Exception e) {

			}
		}
		return result;
	}

	public static void startMusic() {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(new Runnable() {

			public void run() {
				try {

					while (true) {
						File file = new File("D:", "酷我音乐-无损音质正版在线试听网站.MP3");
						FileInputStream fis = new FileInputStream(file);
						BufferedInputStream stream = new BufferedInputStream(fis);
						Player player = new Player(stream);
						player.play();
						System.out.println("播放");

					}

				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		});
	}

}
