package com.ronustine.splendid.simplify.excelwell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class DataCompare {

	public static void main(String[] args) {
		List<String> orderIdList = new ArrayList<String>();
		try {
			orderIdList = getOrderId("C:\\Users\\win7\\Desktop\\20161216\\移动支付对账差异表-1101-1115.xls");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0; i < orderIdList.size(); i++){
			System.out.println(orderIdList.get(i));
		}
		

	}

	public static List<String> getOrderId(String file) throws FileNotFoundException, IOException, ParseException {
		HashMap<String, String> orderIdList = new HashMap<String, String>();
		HSSFWorkbook rwb = new HSSFWorkbook(new FileInputStream(new File(file)));
		HSSFSheet sheet = rwb.getSheetAt(0);

		String date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		for (int j = 1; j < sheet.getLastRowNum(); j++) {
			// 创建一个行对象和一个志愿者对象
			HSSFRow row = sheet.getRow(j);

			HSSFCell cell2 = row.getCell(7);
//			System.out.println(getStringCellValue(cell2));
//			orderIdList.add(getStringCellValue(cell2));
			
		}
		return null;
	}

	/**
	 * 获取单元格数据内容为字符串类型的数据
	 * 
	 * @param cell
	 *            Excel单元格
	 * @return String 单元格数据内容
	 */
	private static String getStringCellValue(HSSFCell cell) {
		String strCell = "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			strCell = String.valueOf(cell.getNumericCellValue());
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}
		if (cell == null) {
			return "";
		}
		return strCell;
	}

}
