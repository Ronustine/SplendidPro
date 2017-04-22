package com.ronustine.splendid.simplify.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;


/**
 * 邮件设置类
 * @author diaox
 *
 */
public class MailConfig {

	// 注册类邮件
	public static final String REGISTER_MAIL = "1";
	// 找回密码类邮件
	public static final String FIND_BACK_SECRETE_MAIL = "2";
    
    // 收件人的邮箱
    private String receiverEmail;
    
	public static final String ENCODEING = "UTF-8";
	
	// 服务器地址
	public static String host;
    
    // 发件人的邮箱
    public static String senderEmail;
    
    // 发件人昵称
    public static String senderName;
    
    // 密码
    public static String senderPassword;
    
    // 邮件的类型
    public static String emailContentType = "text/plain";
    
    // 标题
    public static String emailHeader;

    // 主题 
    public static String emailSubject;

    // 注册信息(支持HTML)
    public static String emailContentRegister; 
    
    // 找回密码信息
    public static String emailContentFindBackSecrete;

    // 发送时间
    public static Date sendDate = new Date();
    
    // 是否需要验证用户名和密码 
    public static boolean isValidate = true;
    
    
    static{
    	//TODO 设置配置文件路径
    	initConfig("");
    }
    
    private static void initConfig(String configFilePath){
    	InputStream inputStream = null;
    	Properties config;
    	
    	try {
    		System.out.println(MailConfig.class.getResource("/conf/mail.properties").getFile());
			inputStream = new FileInputStream(MailConfig.class.getResource("/conf/mail.properties").getFile());
			config = new Properties();
			config.load(inputStream);
			//加载配置文件
			host = config.getProperty("host");
			senderEmail = config.getProperty("senderEmail");
			senderName = new String(config.getProperty("senderName").getBytes("ISO-8859-1"), MailConfig.ENCODEING);
			senderPassword = config.getProperty("senderPassword");
			emailContentType = config.getProperty("emailContentType");
			emailHeader = new String(config.getProperty("emailHeader").getBytes("ISO-8859-1"), MailConfig.ENCODEING);
			emailSubject = new String(config.getProperty("emailSubject").getBytes("ISO-8859-1"), MailConfig.ENCODEING);
			emailContentRegister = new String(config.getProperty("emailContentRegister").getBytes("ISO-8859-1"), MailConfig.ENCODEING);
			emailContentFindBackSecrete = new String(config.getProperty("emailContentFindBackSecrete").getBytes("ISO-8859-1"), MailConfig.ENCODEING);
			isValidate = Boolean.parseBoolean(config.getProperty("isValidate"));
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(null != inputStream){
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }

	public String getReceiverEmail() {
		return receiverEmail;
	}

	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}
	
    public static void main(String[] args){
    	MailConfig mailConfig = new MailConfig();
    	mailConfig.setReceiverEmail("447202073@qq.com");
    }
    
}
