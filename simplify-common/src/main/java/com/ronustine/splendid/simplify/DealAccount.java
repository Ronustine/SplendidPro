package com.ronustine.splendid.simplify;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DealAccount {
	
	public static XSSFWorkbook readXLSX(String path){
		InputStream is = null;
		try {
			is = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		XSSFWorkbook work = null;
		try {
			work = new XSSFWorkbook(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return work;
	}
	
	public static HSSFWorkbook readXLS(String path){
		InputStream is = null;
		try {
			is = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		HSSFWorkbook work = null;
		try {
			work = new HSSFWorkbook(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return work;
	}
	
	public static Map<String, Double> analysisData(XSSFWorkbook work){
		Map<String, Double> personAmount = new HashMap<String, Double>();
		XSSFSheet sheet = work.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		
		for(int i = 3; i < rows - 3; i++){
			XSSFRow row = sheet.getRow(i);
			XSSFCell amountCell = row.getCell(4);
			XSSFCell personNameCell = row.getCell(8);
			
			String m = amountCell.getStringCellValue();
			if(StringUtils.isBlank(m)){
				continue;
			}
			Double amount = personAmount.get(personNameCell.getStringCellValue());
			if(null == amount){
				personAmount.put(personNameCell.getStringCellValue(), new Double(m));
			}else {
				personAmount.put(personNameCell.getStringCellValue(), amount + new Double(m));
			}
		}
		return personAmount;
	}
	
	public static void main(String[] args) {
		XSSFWorkbook work = readXLSX("C:/0326-0331.xlsx");
		Map<String, Double> map = analysisData(work);
		
		for(String key : map.keySet()){
			Double amount = map.get(key);
			if(0 == amount){
				continue;
			}
			System.out.println("患者：" + key + "，金额总额：" + amount);
		}
		System.out.println("结束");
	}
	
	
}
