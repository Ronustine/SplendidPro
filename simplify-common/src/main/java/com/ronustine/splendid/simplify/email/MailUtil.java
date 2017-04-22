package com.ronustine.splendid.simplify.email;

import java.util.Date;
import java.util.Properties;  

import javax.mail.Address;  
import javax.mail.Authenticator;  
import javax.mail.Message;  
import javax.mail.Session;  
import javax.mail.Transport;  
import javax.mail.internet.InternetAddress;  
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ronustine.splendid.simplify.config.MailConfig;


/**
 * 邮件工具类
 * @author diaox
 *
 */
public class MailUtil {
	protected final Logger logger = LoggerFactory.getLogger(MailUtil.class);  
    
	/**
	 * 发送邮件
	 * @param receiverEmail 收件人
	 * @param mailType 1为注册时邮件，2为找回密码类邮件
	 * @return
	 */
	public boolean send(String receiverEmail, String mailType) {
    	//获取系统环境    
    	Properties prop = new Properties();    
    	Authenticator auth = null;  
    	if (MailConfig.isValidate){  
    		// 邮件服务器认证   用户名和密码  
    		auth = new MailAuthenticator();  
    	}   

		// 添加必要的信息  
		prop.put("mail.smtp.host", MailConfig.host);     
		prop.put("mail.smtp.auth", "true");     
         
		// 设置对话和邮件服务器进行通讯  
		Session session = Session.getDefaultInstance(prop, auth);     
		// 在控制台显示Debug信息  
		session.setDebug(true);  
		// 设置邮件对象  
		Message message = new MimeMessage(session);     
		try {
			message.setHeader("Header", MailConfig.emailHeader);
			message.setSubject(MailConfig.emailSubject);     
			message.setSentDate(new Date());  

			// 设置发信人地址  和 名字  
			Address address = new InternetAddress(MailConfig.senderEmail, MailConfig.senderName);     
			// 把发件人信息添加到信息中  
			message.setFrom(address); 
			
			// 设置收件人地址  
			Address toAddress = new InternetAddress(receiverEmail);   
			// 设置接收人地址  
			message.setRecipient(Message.RecipientType.TO, toAddress);     
           
			// 设置多个收件人地址     
			// message.addRecipient(Message.RecipientType.TO,new InternetAddress("xxx@xxx.com"));     
 
//			// 设置邮件格式  
//			message.setContent("Content", MailConfig.emailContentType);   
//			// 设置邮件内容    必须在设置文件格式后面  
//			message.setText(MailConfig.emailContent);  
			// 发送注册信息邮件
			MailConfig.emailContentType = "text/html; charset=utf-8";  
			if(MailConfig.REGISTER_MAIL.equals(mailType)){
				String emailContentRegister = new String(MailConfig.emailContentRegister);
				emailContentRegister.replace("&userName", "asd");
				emailContentRegister.replace("&replace", "asd");
				message.setContent(emailContentRegister, MailConfig.emailContentType);
			}
			// 发送找回密码邮件
			if(MailConfig.FIND_BACK_SECRETE_MAIL.equals(mailType)){
				String emailContentFindBackSecrete = new String(MailConfig.emailContentFindBackSecrete);
				emailContentFindBackSecrete.replace("&userName", "asd");
				emailContentFindBackSecrete.replace("&replace", "asd");
				message.setContent(MailConfig.emailContentFindBackSecrete, MailConfig.emailContentType);
			}

			message.saveChanges();     
			Transport.send(message);
            logger.debug(MailConfig.senderEmail + " 发送邮件到  " + receiverEmail + "成功");  
			System.out.println("发送成功！");     
			return true;
		} catch (Exception e) {     
			System.out.println("出错");
			logger.debug(MailConfig.senderEmail + " 发送邮件到  " + receiverEmail + " 失败"); 
			e.printStackTrace();     
		}  
		return false;
	}
    
    public static void main(String[] args){
    	MailConfig mailConfig = new MailConfig();
    	//mailConfig.setReceiverEmail("447202073@qq.com");
    	MailUtil mailUtil = new MailUtil();
    	mailUtil.send("447202073@qq.com", "1");
    	
    }
}
