package com.ronustine.splendid.service.impl;

import org.springframework.stereotype.Service;

import com.ronustine.splendid.config.WechatPublicServerOneConfig;
import com.ronustine.splendid.service.IWeChatCommonService;
import com.ronustine.splendid.simplify.WeChatPublicServerSecret.qq.weixin.mp.aes.AesException;
import com.ronustine.splendid.simplify.WeChatPublicServerSecret.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.ronustine.splendid.simplify.config.AbstractConfig;

@Service
public class WeChatCommonService implements IWeChatCommonService {

	@Override
	public String verifyURL(String signature, String timestamp, String nonce, String echostr) {
		String result = "";
		AbstractConfig weChatConfig = WechatPublicServerOneConfig.getInstance();
		try {
			WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(weChatConfig.get("token"), weChatConfig.get("encodingAesKey"), weChatConfig.get("corpId"));
			result = wxBizMsgCrypt.VerifyURL(signature, timestamp, nonce, echostr);
		} catch (AesException e) {
			e.printStackTrace();
		}
		return result;
	}

}
