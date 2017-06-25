package com.orisun.mail;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class MailSenderInfo {
	// 发送邮件的服务器的IP和端口
	private String mailServerHost;
	private String mailServerPort = "25";
	// 邮件发送者的地址
	private String fromAddress;
	// 邮件接收者的地址
	private String toAddress;
	// 登陆邮件发送服务器的用户名和密码
	private String userName;
	private String password;
	// 是否需要身份验证
	private boolean validate = false;
	// 邮件主题
	private String subject;
	// 邮件的文本内容
	private String content;
	// 邮件附件的文件名
	private List<String> attachFileNames;
	private List<String> attachFilePaths;
	Properties mailProperties;

	/**
	 * 获得邮件会话属性
	 * 
	 * @throws IOException
	 */
	public MailSenderInfo(String propertyFile) throws IOException {
		mailProperties = new Properties();
		InputStream is = new FileInputStream(propertyFile);
		mailProperties.load(is);
		setMailServerHost(mailProperties.getProperty("mail.smtp.host"));
		setMailServerPort(mailProperties.getProperty("mail.smtp.port"));
		setValidate(Boolean.parseBoolean(mailProperties
				.getProperty("mail.smtp.auth")));
		setUserName(mailProperties.getProperty("userName"));
		setPassword(mailProperties.getProperty("password"));
		setFromAddress(mailProperties.getProperty("fromAddress"));
		setToAddress(mailProperties.getProperty("toAddress"));
	}

	public Properties getMailProperties() {
		return mailProperties;
	}

	public void setMailProperties(Properties mailProperties) {
		this.mailProperties = mailProperties;
	}

	public String getMailServerHost() {
		return mailServerHost;
	}

	public void setMailServerHost(String mailServerHost) {
		this.mailServerHost = mailServerHost;
	}

	public String getMailServerPort() {
		return mailServerPort;
	}

	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public List<String> getAttachFileNames() {
		return attachFileNames;
	}

	public void setAttachFileNames(List<String> attachFileNames) {
		this.attachFileNames = attachFileNames;
	}

	public List<String> getAttachFilePaths() {
		return attachFilePaths;
	}

	public void setAttachFilePaths(List<String> attachFilePaths) {
		this.attachFilePaths = attachFilePaths;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String textContent) {
		this.content = textContent;
	}
}
