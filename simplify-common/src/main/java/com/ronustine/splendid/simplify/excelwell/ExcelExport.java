package com.ronustine.splendid.simplify.excelwell;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.List;
import java.util.concurrent.*;

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


	ExecutorService executorService = Executors.newFixedThreadPool(100);

	public void concurrentTest () {
		CompletionService completionService = new ExecutorCompletionService(executorService);
		Future<ExcelData> future = new FutureTask<ExcelData>(
				new Callable<ExcelData>() {
					@Override
					public ExcelData call() throws Exception {
						return null;
					}
				}
		);

		Future<ExcelData> future1 = new FutureTask<ExcelData>(
				new Callable<ExcelData>() {
					@Override
					public ExcelData call() throws Exception {
						return null;
					}
				}
		);
		executorService.invokeAll()



	}
	
	public void export() throws ExcelPortException{
		if(null == excelData){
			throw new ExcelPortException("you need to set the excelData firstly!你要先设置Excel的数据才行！");
		}
		/*List<String> columnNameList = excelData.getColumnNameList();
		List<T> dataList = excelData.getDataList();*/

		Workbook wb = new SXSSFWorkbook();
		Sheet sheet = wb.createSheet("sheeeet1");
		wb.createSheet("sheeeeet2");
		wb.getSheet("sheeeet1");

	}
	
}
