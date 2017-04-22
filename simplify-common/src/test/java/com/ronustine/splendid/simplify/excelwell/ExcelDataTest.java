package com.ronustine.splendid.simplify.excelwell;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExcelDataTest {

	@Test
	public void testAddColumnName() {
		ExcelData<String> excelData = new ExcelData<String>();
		excelData.addColumnName("列名1");
		assertEquals(excelData.getColumnNameList().size(), 1);
	}
	
	@Test
	public void testRemoveColumnName() {
		ExcelData<String> excelData = new ExcelData<String>();
		try {
			excelData.removeColumnName("列名1");
			fail("没有抛出异常");
		} catch (ExcelPortException e) {
			assertEquals("1", "1");
//			e.printStackTrace();
		}
	}
	
	@Test
	public void testAddData() {
		ExcelData<String> excelData = new ExcelData<String>();
		excelData.addData("data1");
		assertEquals(excelData.getDataList().size(), 1);
	}

	@Test
	public void testRemoveData() {
		ExcelData<String> excelData = new ExcelData<String>();
		try {
			excelData.removeData("asd");
			fail("没有抛出异常");
		} catch (ExcelPortException e) {
			assertEquals("1", "1");
//			e.printStackTrace();
		}
	}

}
