package com.ronustine.splendid.service;

public interface IWeChatCommonService {
	/**
	 * 验证URL
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @param echoStr
	 * @return
	 */
	public String verifyURL(String signature, String timestamp, String nonce, String echostr);
}
