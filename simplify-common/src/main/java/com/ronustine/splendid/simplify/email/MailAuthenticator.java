package com.ronustine.splendid.simplify.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import com.ronustine.splendid.simplify.config.MailConfig;

/**
 * 验证用户密码
 * @author diaox
 *
 */
public class MailAuthenticator extends Authenticator {

	private  String username = MailConfig.senderEmail;     
	private  String password = MailConfig.senderPassword;     
	      
	public MailAuthenticator() {  
		super();  
	}  

	/** 
	* 设置验证的用户名和密码 
	*/  
	public MailAuthenticator(String userName , String password) {  
		super();  
		this.username = userName;  
		this.password = password;  
	}
	
	protected PasswordAuthentication getPasswordAuthentication()  {     
		return new PasswordAuthentication(this.username,this.password);     
	}     

}
