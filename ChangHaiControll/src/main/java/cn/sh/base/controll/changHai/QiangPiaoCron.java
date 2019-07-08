package cn.sh.base.controll.changHai;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
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
import org.apache.logging.log4j.util.Strings;
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

@SuppressWarnings("deprecation")
@Component
@EnableScheduling
@PropertySource("classpath:cron.properties")
public class QiangPiaoCron {

	@Autowired
	private ChangHaiDao changHaiDao;

	@Value("${QiangPiaoCron.doctor.name}")
	private String doctorName;
	@Value("${QiangPiaoCron.mail}")
	private String mail;
	@Value("${QiangPiaoCron.noticeNum:3}")
	private int noticeNum;
	@Value("${QiangPiaoCron.notice.date.existbe}") // 存在时间提醒
	private String existbeDate;

	private static final Logger LOG = LoggerFactory.getLogger(QiangPiaoCron.class);

	@Scheduled(fixedDelayString = "${QiangPiaoCron.run}")
	public void run() {
		LOG.info("开始抢票，开始时间：{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		List<Date> existbeDateList = new ArrayList<Date>();
		if (Strings.isBlank(doctorName)) {
			LOG.error("请配置doctorName");
			return;
		} else {
			doctorName = doctorName.trim();
		}
		if (!Strings.isBlank(existbeDate)) {
			existbeDate = existbeDate.trim();
			String[] dates = existbeDate.split(",", -1);
			if (dates != null && dates.length > 0) {
				for (String date : dates) {
					try {
						existbeDateList.add(new SimpleDateFormat("yyyyMMdd").parse(date));
					} catch (ParseException e) {
						LOG.error("配置 existbe 有误：{}", existbeDate);
						return;
					}

				}
			}
			LOG.info("查询存在时间为：{}", existbeDate);
		}
		String uuid = StringUtils.getRrandomUUID();
		List<Integer> sendStatus = new ArrayList<Integer>();
		sendStatus.add(0);// 存在某时间的发送成功次数
		sendStatus.add(0);// 存在某时间的发送失败次数
		sendStatus.add(0);// 有号的发送成功次数
		sendStatus.add(0);// 有号的发送失败次数
		sendStatus.add(0);// 播放音乐 默认未播放

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				try {
					if (sendStatus.get(1) > 3 || sendStatus.get(0) > 3) {
						timer.cancel();
					}
					if (sendStatus.get(2) > 4 || sendStatus.get(3) > 4) {
						timer.cancel();
					}
					start(uuid, noticeNum, existbeDateList, sendStatus);
				} catch (Exception e) {
					LOG.error("抢票失败：{}", e);
				}

			}

		}, 0, 1000 * 4);

		// changHaiDao.insertSendMailInfo(uuid, mail, "抢票测试", true, new Date());
		// changHaiDao.insertQiangPiaoInfo(uuid, "123", new Date());

		LOG.info("结束抢票");

	}

	public boolean start(String uuid, int noticeNum, List<Date> existbeDateList, List<Integer> sendStatus)
			throws Exception {

		List<String> emails = new ArrayList<String>();
		emails.add(mail);
		String DEPT_CODE = "100804";// 神经内科
		// DEPT_CODE = "300203";//肛肠科
		String APP_UUID = UUID.randomUUID().toString();
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
		// String timeStr = json.getString("time");
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
			for (int j = 0; j < zhibanArray.size(); j++) {
				JSONObject zhibanObject = zhibanArray.getJSONObject(j);
				String zhibanTime = zhibanObject.getString("CLINIC_DATE");
				String haoFlag = zhibanObject.getString("ISTIME");
				String zhibanType = zhibanObject.getString("CLINIC_TYPE");
				String zhibanPay = zhibanObject.getString("SUM_FEE");
				if (haoFlag != null) {
					haoFlag = haoFlag.trim();
					String keshi = "";
					switch (DEPT_CODE) {
					case "100804":
						keshi = "神经内科";
						break;
					case "300203":
						keshi = "肛肠外科";
						break;

					default:
						break;
					}
					if (doctorName.equals(name.trim())) {
						if (sendStatus.get(1) > 3 || sendStatus.get(0) > 3) {
							continue;
						}
						Date zhibanDate = null;
						try {
							zhibanDate = new SimpleDateFormat("yyyy/MM/dd").parse(zhibanTime);
						} catch (Exception e) {
							LOG.error("转化接口返回时间:{} 失败", zhibanTime, e);
						}

						if (zhibanDate != null && existbeDateList.size() > 0 && existbeDateList.contains(zhibanDate)) {
							boolean sendResult = false;
							try {
								sendResult = SendQQMailUtils.sendHtmlMail(keshi, doctorName + "存在预约时间为" + zhibanTime,
										emails, null);
							} catch (Exception e) {
								LOG.error("发送邮件失败", e);
							}
							if (sendResult) {
								sendStatus.set(0, sendStatus.get(0) + 1);
							} else {
								sendStatus.set(1, sendStatus.get(1) + 1);
							}

							// changHaiDao.insertQiangPiaoInfo(uuid, doctorName+"存在预约时间为"+zhibanTime, new
							// Date());
							changHaiDao.insertSendMailInfo(uuid, mail, doctorName + "存在预约时间为" + zhibanTime, sendResult,
									new Date());

						}
					}

					if ("1".equals(haoFlag)) {
						if (sendStatus.get(2) >= 4 || sendStatus.get(3) >= 4) {
							continue;
						}
						haoFlag = "有号";
						if (doctorName.equals(name.trim())) {
							changHaiDao.insertQiangPiaoInfo(uuid, doctorName, new Date());
							String message = "姓名：" + name + " 职位：" + zhiwei + " 预约时间：" + zhibanTime + " 号状态：" + haoFlag
									+ " 金额：" + zhibanPay + " 号类型：" + zhibanType;
							boolean sendResult = false;
							try {
								SendQQMailUtils.sendHtmlMail(keshi, message, emails, null);
							} catch (Exception e) {
								LOG.error("发送邮件失败", e);
							}
							if (sendResult) {
								sendStatus.set(2, sendStatus.get(0) + 1);
							} else {
								sendStatus.set(3, sendStatus.get(1) + 1);
							}
							changHaiDao.insertSendMailInfo(uuid, mail, doctorName + "存在时间为" + zhibanTime + "的号",
									sendResult, new Date());
							if (sendStatus.get(4) == 0) {
								startMusic();
								changHaiDao.updateQiangPiaoInfoByUuid(uuid, true);
								sendStatus.set(4, 1);
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
				if (doctorName.equals(name.trim())) {
					LOG.info("姓名：" + name + " 职位：" + zhiwei + " 预约时间：" + zhibanTime + " 有无号：" + haoFlag + " 金额："
							+ zhibanPay + " 号状态：" + zhibanType);
				}

			}

		}
		return true;

	}

	private final static RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(8000)
			.setConnectTimeout(5000).setConnectionRequestTimeout(5000).build();

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
					LOG.error("开启背景乐失败", e);
				}

			}
		});
	}

}
