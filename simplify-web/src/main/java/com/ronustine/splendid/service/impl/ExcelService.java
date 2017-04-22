package com.ronustine.splendid.service.impl;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.stereotype.Service;

import com.ronustine.splendid.simplify.excelwell.DataCompare;

@Service
public class ExcelService {

	public void compareData(String filPath){
		try {
			DataCompare.getOrderId(filPath);
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
