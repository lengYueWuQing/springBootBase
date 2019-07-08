package cn.sh.Utils.mail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.Security;
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

public class SendQQMailUtils {

	
	/**
	 * 以文本格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件的信息
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("restriction")
	public static boolean sendHtmlMail(String subject, String message, List<String> emails, List<File> files) {
		// 判断是否需要身份认证
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		final String userName1 = "1126257895";
		final String password1 = "zqfizofzmpepfebd";
		String port = "465";
		String host = "smtp.qq.com";
		
		Properties pro = new Properties();
		pro.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		pro.setProperty("mail.smtp.socketFactory.fallback", "false");
		pro.setProperty("mail.smtp.port", port);
		pro.setProperty("mail.smtp.socketFactory.port", port);
		pro.setProperty("mail.smtp.auth", "true");
		pro.put("mail.smtp.timeout", 10000);
		pro.put("mail.smtp.connectiontimeout", 10000);
		pro.put("mail.smtp.writetimeout", 60000);
		
		// 开启debug调试，以便在控制台查看
		pro.setProperty("mail.debug", "false");
		pro.put("mail.smtp.host", host);
		
		pro.put("mail.smtp.username", userName1);
		pro.put("mail.smtp.password", password1);
		// 发送邮件协议名称smtp
		pro.setProperty("mail.transport.protocol", host);
		
		// 创建session
		Session sendMailSession = Session.getDefaultInstance(pro, new Authenticator() {
			// 身份认证
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName1, password1);
			}
		});
		// 通过session得到transport对象
		/*
		 * Transport ts = null; try { ts = sendMailSession.getTransport("smtp");
		 * ts.connect(host, userName, password);//
		 * 后面的字符是授权码，用qq密码反正我是失败了（用自己的，别用我的，这个号是我瞎编的，为了。。。。） } catch (Exception e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 */
		// 连接邮件服务器：邮箱类型，帐号，授权码代替密码（更安全）

		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress("1126257895@qq.com");
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			for(String email :emails) {
				Address to = new InternetAddress(email);
				mailMessage.setRecipient(Message.RecipientType.TO, to);
			}
			// 设置邮件消息的主题
			mailMessage.setSubject(subject);
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			mailMessage.addHeader("X-Priority", "3"); 
			mailMessage.addHeader("X-MSMail-Priority", "Normal"); 
			mailMessage.addHeader("X-Mailer", "Microsoft Outlook Express 6.00.2900.2869"); //本文以outlook名义发送邮件，不会被当作垃圾邮件 
			mailMessage.addHeader("X-MimeOLE", "Produced By Microsoft MimeOLE V6.00.2900.2869"); 
			mailMessage.addHeader("ReturnReceipt", "1");
			//不被当作垃圾邮件的关键代码--end
			// 设置邮件消息的主要内容
			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			String ht = message;
			// 设置HTML内容
			html.setContent(ht, "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			
			//mailMessage.setText("<div>mesage</div><br>aaaa/rsss/n");
			/* 往邮件中添加附件 */
			if(files != null && files.size() > 0) {
				
				for(File file :files) {
					if(file.exists()) {
						MimeBodyPart messageBodyPart = new MimeBodyPart();
						FileDataSource source = new FileDataSource(file); 
						messageBodyPart.setDataHandler(new DataHandler(source));
						// 处理附件名称中文（附带文件路径）乱码问题 
						messageBodyPart.setFileName(MimeUtility.encodeText(file.getName())); 
						mainPart.addBodyPart(messageBodyPart); 
					}
				}
			}
			
			// 将MiniMultipart对象设置为邮件内容
		    mailMessage.setContent(mainPart);
			Transport.send(mailMessage);
			
			return true;
		} catch (Exception ex) {
			new Exception(ex);
		}
		return false;
	}
}