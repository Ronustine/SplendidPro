package com.ronustine.splendid.simplify.excelwell;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel的数据结构，用于导入导出
 * 
 * @author ronustine
 * @param <T>
 */
public class ExcelData<T> {
	// 列名
	private List<String> columnNameList;
	// 数据
	private List<T> dataList;

	public List<String> getColumnNameList() {
		return columnNameList;
	}

	public void setColumnNameList(List<String> columnNameList) {
		this.columnNameList = columnNameList;
	}

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	/**
	 * 添加一个列名
	 * 
	 * @param columnName
	 *            列名
	 */
	public void addColumnName(String columnName) {
		if (null == columnNameList) {
			// 默认使用ArrayList
			columnNameList = new ArrayList<String>();
		}
		columnNameList.add(columnName);
	}
	
	/**
	 * 移除某个列名
	 * 
	 * @param columnName
	 *            列名
	 * @throws ExcelPortException
	 *             如果列名数组是空的就抛出异常
	 */
	public void removeColumnName(String columnName) throws ExcelPortException {
		if (null == columnNameList) {
			throw new ExcelPortException("this columnNameList is null, remove is not allowed! 包含列名的数组是空的，不能移除！");
		}
		columnNameList.remove(columnName);
	}

	
	/**
	 * 添加一个要导出的数据
	 * 
	 * @param data
	 *            数据
	 */
	public void addData(T data) {
		if (null == dataList) {
			// 默认使用ArrayList
			dataList = new ArrayList<T>();
		}
		dataList.add(data);
	}

	/**
	 * 移除某个数据
	 * 
	 * @param data
	 *            数据
	 * @throws ExcelPortException
	 *             如果数据数组是空的就抛出异常
	 */
	public void removeData(T data) throws ExcelPortException {
		if (null == dataList) {
			throw new ExcelPortException("this dataList is null, remove is not allowed! 包含数据的数组是空的，不能移除！");
		}
		dataList.remove(data);
	}

}