package com.orisun.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.orisun.mining.uitl.FileUtil;
import com.orisun.mining.uitl.Path;

public class TestSendMail {

	private static String basePath = Path.getCurrentPath();
	private static String confPath = basePath + "/config/";
	private static String dataPath = basePath + "/data/";

	public static void main(String[] args) throws IOException {
		// 从配置文件中读取 mail相关配置
		MailSenderInfo mailInfo = new MailSenderInfo(confPath + "mail.properties");
		// 邮件主题
		mailInfo.setSubject("这里是邮件标题");
		StringBuilder contents = new StringBuilder(); // 邮件正文内容
		List<String> attachments = new ArrayList<String>(); // 附件内容

		contents.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"5\"><tr bgColor=\"#00B38A\"><th align=\"center\" colspan=\"3\">这里是表格标题</th></tr><tr bgColor=\"#00B38A\"><th align=\"center\">列1</th><th align=\"center\">列2</th><th align=\"center\">列3</th></tr><tr><td>张三</td><td>12</td><td>54</td></tr><tr><td>李四</td><td>42</td><td>3</td></tr><tr><td>王五</td><td>756</td><td>43</td></tr></table>");
		contents.append("<br><br>");
		contents.append("来几张图片");
		contents.append("<br><br>");
		contents.append("<img src=\"cid:2.png\" />");
		contents.append("<br><br>");
		contents.append("<img src=\"cid:3.png\" />");
		contents.append("<br><br>");
		contents.append("<img src=\"cid:5.png\" />");
		contents.append("<br><br>");
		contents.append("<img src=\"cid:4.png\" />");
		contents.append("<br><br>");
		contents.append("还有两个附件，一个是zip压缩过的，一个没有压缩");

		// 正文中的图片同时要回到附件中，文件名要跟cid后面的内容对应
		attachments.add(dataPath + "2.png");
		attachments.add(dataPath + "3.png");
		attachments.add(dataPath + "4.png");
		attachments.add(dataPath + "5.png");

		attachments.add(dataPath + "dirtyword");
		FileUtil.zip(dataPath + "company");
		attachments.add(dataPath + "company.zip");

		mailInfo.setContent(contents.toString());
		mailInfo.setAttachFilePaths(attachments);
		MailSender.sendHtmlMail(mailInfo);
	}
}
