package com.orisun.mail;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 邮件发送器
 */
public class MailSender {

	private static Log logger = LogFactory.getLog(MailSender.class);

	/**
	 * 以文本格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件的信息
	 */
	public boolean sendTextMail(MailSenderInfo mailInfo) {
		// 判断是否需要身份认证
		MailAuthenticator authenticator = null;
		Properties pro = mailInfo.getMailProperties();
		// 如果需要身份认证，则创建一个密码验证器
		if (mailInfo.isValidate()) {
			authenticator = new MailAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address to = new InternetAddress(mailInfo.getToAddress());
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// 设置邮件消息的主要内容
			String mailContent = mailInfo.getContent();
			mailMessage.setText(mailContent);
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			logger.error("send mail failed.", ex);
		}
		return false;
	}

	/**
	 * 以HTML格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件信息
	 */
	public static boolean sendHtmlMail(MailSenderInfo mailInfo) {
		// 判断是否需要身份认证
		MailAuthenticator authenticator = null;
		Properties pro = mailInfo.getMailProperties();
		// 如果需要身份认证，则创建一个密码验证器
		if (mailInfo.isValidate()) {
			authenticator = new MailAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			if (mailInfo.getToAddress().contains(";")) {
				String[] adds = mailInfo.getToAddress().split(";");
				Address[] to = new Address[adds.length];
				for (int i = 0; i < adds.length; i++) {
					to[i] = new InternetAddress(adds[i]);
				}
				// 还可以设置RecipientType.CC(抄送)和RecipientType.BCC(密送)人列表
				mailMessage.setRecipients(Message.RecipientType.TO, to);
			} else {
				Address to = new InternetAddress(mailInfo.getToAddress());
				mailMessage.setRecipient(Message.RecipientType.TO, to);
			}

			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mp = new MimeMultipart("related");// related意味着可以发送html格式的邮件
			/** *************************************************** */
			BodyPart bodyPart = new MimeBodyPart();// 正文
			bodyPart.setDataHandler(new DataHandler(mailInfo.getContent(),
					"text/html;charset=UTF-8"));// 网页格式
			/** *************************************************** */

			if (mailInfo.getAttachFilePaths() != null) {
				for (int i = 0; i < mailInfo.getAttachFilePaths().size(); i++) {
					MimeBodyPart attachBodyPart = new MimeBodyPart();// 普通附件
					FileDataSource fds = new FileDataSource(mailInfo
							.getAttachFilePaths().get(i));
					attachBodyPart.setDataHandler(new DataHandler(fds));
					attachBodyPart.setFileName(fds.getName());
					String fileName = fds.getName();
					attachBodyPart.setContentID(fileName);// 在html中使用该图片方法src="cid:fileName"
					attachBodyPart.setFileName(MimeUtility.encodeText(fds
							.getName()));// 解决附件名为中文时乱码的情况
					mp.addBodyPart(attachBodyPart);
				}
			}

			mp.addBodyPart(bodyPart);
			mailMessage.setContent(mp);// 设置邮件内容对象
			Transport.send(mailMessage);// 发送邮件

			return true;
		} catch (MessagingException ex) {
			logger.error("send mail failed.", ex);
		} catch (UnsupportedEncodingException e) {
			logger.error("encoding error.", e);
		}
		return false;
	}

	/**
	 * 读取文件
	 * 
	 * @param file
	 *            文件路径
	 * @return 返回二进制数组
	 * @throws IOException
	 */
	public static byte[] readFile(String file) throws IOException {
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			fis = new FileInputStream(file);
			bos = new ByteArrayOutputStream();
			int bytesRead;
			byte buffer[] = new byte[1024 * 1024];
			while ((bytesRead = fis.read(buffer)) != -1) {
				bos.write(buffer, 0, bytesRead);
				Arrays.fill(buffer, (byte) 0);
			}
		} catch (IOException e1) {
			logger.error("readFile failed.", e1);
		} finally {
			try {
				if (bos != null)
					bos.close();
			} catch (IOException e) {
				logger.error("close file failed.", e);
			}
		}
		fis.close();
		return bos.toByteArray();
	}

}
