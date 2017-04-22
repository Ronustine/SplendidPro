package com.ronustine.splendid.simplify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class YBFileTest {

	public static void main(String[] args) {
		Calendar cDate = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd"); 
		try {
			Date date = dateFormat.parse("20160801");
			cDate.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < 200; i++){
			String fileTime = dateFormat.format(cDate.getTime());
			System.out.println("解析的日期：" + fileTime);
			if("20170212".equals(fileTime)){
				break;
			}
			String fileName = "SYYDYB_" + fileTime + ".txt";
			//1_2016_2017
			//2_20160810_20161213
			//3_20160810_20161213
			String filePath = "D:\\1_2016_2017\\" + fileName;
			parseYbFile(filePath);
			cDate.add(Calendar.DAY_OF_MONTH, 1);
		}
		System.out.println("解析完成");
		
		
	}

	/**
	 * 解析医保文件
	 * 
	 * @param filename
	 * @throws ParseException
	 */
	public static void parseYbFile(String filePath) {

		File file = new File(filePath);

		if (file.exists() && file.isFile()) {
			System.out.println("----------开始解析------------");
			InputStreamReader isr = null;
			BufferedReader br = null;

			try {
				isr = new InputStreamReader(new FileInputStream(file));
				br = new BufferedReader(isr);
				String lineTxt = null;
				while ((lineTxt = br.readLine()) != null) {
					if (lineTxt.startsWith("0")) {
						continue;
					} else if (lineTxt.startsWith("9")) {
						break;
					} else {
						System.out.println(lineTxt);
						String dateYear = lineTxt.substring(0, 4);
						String transType = lineTxt.substring(8, 11);
						String thridNum = lineTxt.substring(17, 23);
						String payTime = lineTxt.substring(23, 33);
						String cardNo = lineTxt.substring(65, 84);
						String transAt = lineTxt.substring(84, 96);
						String own = lineTxt.substring(214, 511);
						String orderId = own.split(" ")[0];

						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
						Date tradeTime = sdf.parse(dateYear + payTime);
						String amount = transAt.replaceFirst("^0*", "");// 000000000400

						FileWriter fileWriter = new FileWriter("D:/YB1.txt", true);
						String s = new String(lineTxt);
						fileWriter.write(s);
						fileWriter.close(); // 关闭数据流
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("----------医保对账文件不存在-----------");
		}
	}

}
