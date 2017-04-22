package com.ronustine.splendid.simplify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串生成类
 * 
 * @author ronustine
 *
 */
public class StringThings {
	// 序列号
	private static int seq = 0;

	/**
	 * 获取一定长度的随机字符串
	 * 
	 * @param length
	 *            指定字符串长度
	 * @return 一定长度的字符串
	 */
	public static String getRandomStringByLength(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 获取UUID，去除“-”符号
	 * 
	 * @return
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		// 去掉“-”符号
		return s.replace("-", "");
	}

	/**
	 * id生成器
	 * @return
	 */
	public static String getLongId() {
		return getLongId("");
	}
	
	/**
	 * id生成器
	 * @param flag 
	 * 			带标志位,自定义
	 * 			如果不同的项目在同一个数据库插入这里生成的ID，则必需根据不同的项目传入不同的标志位
	 * @return
	 */
	public static String getLongId(String flag) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssS");
		String date = sdf.format(new Date());
		date = date + flag + getSuffix();
		return date;
	}

	private static synchronized String getSuffix(){
		String ret;
		if(seq >= 99){
			seq = 0;
		}
		seq++;
        if(seq < 10) {
        	ret = "0" + seq;
        }else{
            ret = "" + seq;
        }
        return ret;
	}
	
	/**
	 * 首字母大写
	 * @param str 需要转换的字符串
	 * @return
	 */
    public static String captureName(String str) {
        char[] cs = str.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }
    
    /**
     * 常速比较
     * @param stringOne 字符串1
     * @param stringTwo 字符串2
     * @return 是否相等
     */
    public static boolean slowEquals(byte[] stringOne, byte[] stringTwo) { 
    	int diff = stringOne.length ^ stringTwo.length; 
    	for(int i = 0; i < stringOne.length && i < stringTwo.length; i++) 
    		diff |= stringOne[i] ^ stringTwo[i]; 
    	return diff == 0; 
    }
    
    
	/**
	 * 获取本机IP
	 * 
	 * @return
	 */
	public static String getV4IP() {
		String ip = "";
		String chinaz = "http://ip.chinaz.com/";

		StringBuilder inputLine = new StringBuilder();
		String read = "";
		URL url = null;
		HttpURLConnection urlConnection = null;
		BufferedReader in = null;
		try {
			url = new URL(chinaz);
			urlConnection = (HttpURLConnection) url.openConnection();
			in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
			while ((read = in.readLine()) != null) {
				inputLine.append(read + "\r\n");
			}
			// System.out.println(inputLine.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
		Matcher m = p.matcher(inputLine.toString());
		if (m.find()) {
			String ipstr = m.group(1);
			ip = ipstr;
			// System.out.println(ipstr);
		}
		return ip;

	}

	/**
	 * 获取本机IP
	 * 
	 * @return
	 */
	public static String getRealIp() throws SocketException {
		String localip = null;// 本地IP，如果没有配置外网IP则返回它
		String netip = null;// 外网IP

		Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
		boolean finded = false;// 是否找到外网IP
		while (netInterfaces.hasMoreElements() && !finded) {
			NetworkInterface ni = netInterfaces.nextElement();
			Enumeration<InetAddress> address = ni.getInetAddresses();
			while (address.hasMoreElements()) {
				ip = address.nextElement();
				if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
					netip = ip.getHostAddress();
					finded = true;
					break;
				} else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
						&& ip.getHostAddress().indexOf(":") == -1) {// 内网IP
					localip = ip.getHostAddress();
				}
			}
		}

		if (netip != null && !"".equals(netip)) {
			return netip;
		} else {
			return localip;
		}
	}

	public static void ip() throws SocketException{
		Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
		while(allNetInterfaces.hasMoreElements()){
			NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
			//System.out.println(netInterface.getName());
			Enumeration addresses = netInterface.getInetAddresses();
			while (addresses.hasMoreElements()) {
				ip = (InetAddress) addresses.nextElement();
				if (ip != null && ip instanceof Inet4Address) {
					System.out.println("本机的IP = " + ip.getHostAddress());
				}
			}
		}

	}

	
	public static void main(String[] args) {
		for(int i = 0; i< 30; i++){
			String a = getUUID();
			System.out.println(a);
		}
		
	}

}
