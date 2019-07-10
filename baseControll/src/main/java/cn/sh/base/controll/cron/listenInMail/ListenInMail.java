package cn.sh.base.controll.cron.listenInMail;

import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FlagTerm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sun.mail.imap.IMAPMessage;

@Component
@EnableScheduling
public class ListenInMail {

	private static final Logger LOG = LoggerFactory.getLogger(ListenInMail.class);

	@SuppressWarnings("restriction")
	@Scheduled(fixedDelayString = "${ListenInMail.run}")
	public void run() {
		LOG.info("监听邮件开始");
		System.setProperty("https.protocols", "TLSv1");
		Security.setProperty("jdk.tls.disabledAlgorithms", "SSLv3, DH keySize < 768");
		Set<String> emails = new HashSet<String>();
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";// ssl加密,jdk1.8无法使用
		String port = "993";
		String imapServer = "imap.qq.com";
		String protocol = "imap";
		final String username = "1126257895@qq.com";
		final String password = "fflxpvuhhowaieea"; // QQ邮箱的授权码

		Properties props = new Properties();
		props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.imap.socketFactory.fallback", "false");
		props.setProperty("mail.transport.protocol", protocol); // 使用的协议
		props.setProperty("mail.imap.port", port);
		props.setProperty("mail.imap.socketFactory.port", port);
		props.put("mail.imap.host", imapServer);
		props.put("mail.imap.auth.login.disable", "false");
		props.put("mail.smtp.host", "smtp.qq.com");
		props.setProperty("mail.smtp.auth", "true");
		// 创建session
		Session session = Session.getDefaultInstance(props, new Authenticator() {
			// 身份认证
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		session.setDebug(false);
		Store store = null;
		Folder folder = null;
		try {
			// 获取Store对象
			store = session.getStore(protocol);
			int n = 0;
			int retyNum = 3;
			do {
				try {
					store.connect(imapServer, username, password); // 登陆认证
					Thread.sleep(1000 * 18);
					break;
				} catch (Exception e) {
					retyNum--;
					if (retyNum <= 0) {
						store.connect(imapServer, username, password); // 登陆认证
					}
				}
			} while (retyNum > 0);

			// 通过imap协议获得Store对象调用这个方法时，邮件夹名称只能指定为"INBOX"
			folder = store.getFolder("INBOX");// 获得用户的邮件帐户
			try {
				folder.open(Folder.READ_WRITE); // 设置对邮件帐户的访问权限
				n = folder.getUnreadMessageCount();// 得到未读数量
			} finally {
				if (!folder.isOpen()) {
					folder.open(Folder.READ_WRITE);
				}
				n = folder.getUnreadMessageCount();// 得到未读数量
			}
			LOG.debug("获取到 {} 未读邮件", n);
			if (n <= 0) {
				return;
			}
			FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false); // false代表未读，true代表已读
			Message messages[] = folder.search(ft);
			for (Message message : messages) {
				if (!message.getFolder().isOpen()) {
					message.getFolder().open(Folder.READ_WRITE);
				}
				message.setFlag(Flags.Flag.SEEN, true);
				String subject = message.getSubject();// 获得邮件主题
				if (message.getFrom() == null || message.getFrom().length == 0) {
					continue;
				}
				Address from = (Address) message.getFrom()[0];// 获得发送者地址
				String mail = decodeText(from.toString());
				int startIndex = mail.lastIndexOf("<");
				int endIndex = mail.lastIndexOf(">");
				LOG.info("邮件的主题为: {}, 发件人: {}", subject, mail);
				Date sendTime = message.getSentDate();
				if (sendTime == null) {
					continue;
				}
				LOG.info("日期: {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(sendTime));
				Long nowLong = new Date().getTime();
				if ((sendTime.getTime() > (nowLong - 30 * 60 * 1000) && sendTime.getTime() <= nowLong)
						|| (sendTime.getTime() >= nowLong && sendTime.getTime() < (nowLong + 5 * 60 * 1000))) {

				} else {
					continue;
				}
				Multipart multipart = (Multipart) message.getContent();
				int count = multipart.getCount();
				for (int idx = 0; idx < count; idx++) {
					BodyPart bodyPart = multipart.getBodyPart(idx);
					LOG.info(bodyPart.getContentType());
					if (bodyPart.isMimeType("text/plain") || bodyPart.isMimeType("text/html")) {
						if (bodyPart.getContent() != null) {
							String content = bodyPart.getContent().toString().trim();
							if (content.equalsIgnoreCase("ssr")) {
								emails.add(mail.substring(startIndex + 1, endIndex));
								break;
							}

						}

					}
				}
				((IMAPMessage) message).invalidateHeaders();
			}
			folder.close(false);// 关闭邮件夹对象
			store.close(); // 关闭连接对象
			if (emails.size() > 0) {
				if (!Getssr.start(new ArrayList<String>(emails))) {
					LOG.error("邮箱：{} 发送失败", Arrays.toString(emails.toArray()));
				} else {
					LOG.info("邮箱：{} 发送成功", Arrays.toString(emails.toArray()));
				}

			}
		} catch (Exception e) {
			try {
				if (folder != null) {
					folder.close(false);
				}
				if (store != null) {
					store.close();
				}
			} catch (Exception e2) {
				LOG.error("", e);
			}

			LOG.error("监听邮箱失败：", e);
		}

	}

	protected static String decodeText(String text) throws UnsupportedEncodingException {
		if (text == null)
			return null;
		if (text.startsWith("=?GB") || text.startsWith("=?gb"))
			text = MimeUtility.decodeText(text);
		else
			text = new String(text.getBytes("ISO8859_1"));
		return text;
	}

}
