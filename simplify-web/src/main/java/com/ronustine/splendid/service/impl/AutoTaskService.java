package com.ronustine.splendid.service.impl;

import org.springframework.stereotype.Service;

import com.ronustine.splendid.service.IAutoTaskService;

@Service("AutoTaskService")
public class AutoTaskService implements IAutoTaskService {

	public void printTask() {
		System.out.println("auto task : printTask()");
	}

}
