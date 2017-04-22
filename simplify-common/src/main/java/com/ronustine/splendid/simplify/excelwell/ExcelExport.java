package com.ronustine.splendid.simplify.excelwell;

import java.util.List;

/**
 * 导出
 * 
 * @author ronustine
 * @param <T>
 */
public class ExcelExport<T> {
	// 导出的数据
	private ExcelData<T> excelData;

	public void setExcelData(ExcelData<T> excelData) {
		this.excelData = excelData;
	}
	
	public void export() throws ExcelPortException{
		if(null == excelData){
			throw new ExcelPortException("you need to set the excelData firstly!你要先设置Excel的数据才行！");
		}
		List<String> columnNameList = excelData.getColumnNameList();
		List<T> dataList = excelData.getDataList();
	}
	
}
