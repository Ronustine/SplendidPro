package com.ronustine.splendid.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ronustine.splendid.service.impl.WeChatCommonService;

@Controller
@RequestMapping("/WeChatPublicServerOne")
public class WeChatPublicServerOneController {
	@Autowired
	private WeChatCommonService weChatCommonService;
	
	/**
	 * 微信服务器配置
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/comfirm", method = RequestMethod.GET)
	public void checkUrl(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String signature = request.getParameter("msg_signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		PrintWriter out = response.getWriter();
		String result = weChatCommonService.verifyURL(signature, timestamp, nonce, echostr);
		out.print(result);
		out.close();
		out = null;
	}

}
