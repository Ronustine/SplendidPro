package com.ronustine.splendid.config;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ronustine.splendid.simplify.config.AbstractConfig;

public class WechatPublicServerOneConfigTest {

	@Test
	public void testGetInstance() {
		AbstractConfig a = WechatPublicServerOneConfig.getInstance();
		String token = a.get("token");
		String encodingAesKey = a.get("encodingAesKey");
		String corpId = a.get("corpId");
		assertEquals("diaoxin",token);
		assertEquals("7WqDtnEeSYYEP5J8NwJXpvHISK6JG6MEJf7J2KoNASG",encodingAesKey);
		assertEquals("wxae94904d88929f10",corpId);
	}

}
