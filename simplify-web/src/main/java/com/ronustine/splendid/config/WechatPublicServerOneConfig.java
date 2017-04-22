package com.ronustine.splendid.config;

import com.ronustine.splendid.simplify.config.AbstractConfig;

/**
 * 服务号（企业号）配置文件读取
 * @author ronustine
 *
 */
public class WechatPublicServerOneConfig extends AbstractConfig {
	
	private static WechatPublicServerOneConfig configuration = null;
	
	private WechatPublicServerOneConfig() {}
	
	/**
	 * 获取一个配置实例
	 * @return
	 * @throws  
	 */
	public synchronized static WechatPublicServerOneConfig getInstance(){
		if(configuration==null){
			configuration = new WechatPublicServerOneConfig();
		}
		try {
			if(configuration.props==null || configuration.props.isEmpty()){
				configuration.load();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return configuration;
	}
	
	/**
	 * 配置文件路径
	 * @return
	 */
	public String getConfigFilePath() {
		return "conf/WeChatPublicServerOne.properties";
	}
}
