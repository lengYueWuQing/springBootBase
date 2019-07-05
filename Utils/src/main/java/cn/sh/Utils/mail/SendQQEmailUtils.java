package cn.sh.Utils.mail;

import java.io.File;
import java.io.UnsupportedEncodingException;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class SendQQEmailUtils {

	/**
	 * 以文本格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件的信息
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	public static boolean sendMessageMail(String userName, String password, String subject, String message,
			List<String> addressList, List<File> fileList) throws Exception {
		
		if (addressList == null || addressList.size() <= 0) {
			throw new Exception("接收邮箱为空");
		}
		if (userName == null || "".equals(userName)) {
			throw new Exception("用户名为空");
		}
		if (password == null || "".equals(password)) {
			throw new Exception("密码为空");
		}
		String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		// String userName = "1126257895";
		// String password = "zqfizofzmpepfebd";
		String host = "smtp.qq.com";
		String port = "465";
		Properties pro = new Properties();
		pro.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		pro.setProperty("mail.smtp.socketFactory.fallback", "false");
		pro.setProperty("mail.smtp.port", port);
		pro.setProperty("mail.smtp.socketFactory.port", port);
		pro.setProperty("mail.smtp.auth", "true");
		// 开启debug调试，以便在控制台查看
		// pro.setProperty("mail.debug", "true");
		pro.put("mail.smtp.host", host);
		pro.put("mail.smtp.username", userName);
		pro.put("mail.smtp.password", password);
		// 发送邮件协议名称
		pro.setProperty("mail.transport.protocol", "smtp");

		// 创建session
		Session sendMailSession = Session.getDefaultInstance(pro, new Authenticator() {
			// 身份认证
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		});
		// 通过session得到transport对象
		Transport ts = sendMailSession.getTransport();
		ts.connect(host, userName, password);// 后面的字符是授权码，用qq密码反正我是失败了（用自己的，别用我的，这个号是我瞎编的，为了。。。。）

		// 连接邮件服务器：邮箱类型，帐号，授权码代替密码（更安全）

		// 根据session创建一个邮件消息
		Message mailMessage = new MimeMessage(sendMailSession);
		// 创建邮件发送者地址
		Address from = new InternetAddress(userName + "@qq.com");
		// 设置邮件消息的发送者
		mailMessage.setFrom(from);
		// 创建邮件的接收者地址，并设置到邮件消息中
		Address[] tos = new InternetAddress[addressList.size()];
		for (int i = 0; i < addressList.size(); i++) {
			tos[i] = new InternetAddress(addressList.get(i));
		}
		mailMessage.setRecipients(Message.RecipientType.TO, tos);

		// 设置邮件消息的主题
		if (subject == null) {
			subject = "";
		}
		mailMessage.setSubject(subject.trim());

		// 设置邮件消息发送的时间
		// 不被当作垃圾邮件的关键代码--end
		mailMessage.setSentDate(new Date());
		mailMessage.addHeader("X-Priority", "3");
		mailMessage.addHeader("X-MSMail-Priority", "Normal");
		mailMessage.addHeader("X-Mailer", "Microsoft Outlook Express 6.00.2900.2869"); // 本文以outlook名义发送邮件，不会被当作垃圾邮件
		mailMessage.addHeader("X-MimeOLE", "Produced By Microsoft MimeOLE V6.00.2900.2869");
		mailMessage.addHeader("ReturnReceipt", "1");

		// 设置邮件消息的主要内容
		// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
		Multipart mainPart = new MimeMultipart();
		// 创建一个包含HTML内容的MimeBodyPart
		BodyPart html = new MimeBodyPart();
		if (message == null) {
			message = "";
		}
		// 设置HTML内容
		html.setContent(message.trim(), "text/html; charset=utf-8");
		mainPart.addBodyPart(html);

		// mailMessage.setText("<div>mesage</div><br>aaaa/rsss/n");
		/* 往邮件中添加附件 */
		if (fileList != null && fileList.size() > 0) {
			for (File file : fileList) {
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				FileDataSource source = new FileDataSource(file);
				messageBodyPart.setDataHandler(new DataHandler(source));
				// 处理附件名称中文（附带文件路径）乱码问题
				messageBodyPart.setFileName(MimeUtility.encodeText(file.getName()));
				mainPart.addBodyPart(messageBodyPart);
			}
		}

		// 将MiniMultipart对象设置为邮件内容
		mailMessage.setContent(mainPart);
		Transport.send(mailMessage);
		return true;
	}
}
