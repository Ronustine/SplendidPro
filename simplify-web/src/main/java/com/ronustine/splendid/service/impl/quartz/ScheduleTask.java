package com.ronustine.splendid.service.impl.quartz;

import java.io.Serializable;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class ScheduleTask implements Job, Serializable{  
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private static final Logger log =  LoggerFactory.getLogger(ScheduleTask.class);  

    
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		SchedulerContext schCtx = null;
		try {
			schCtx = context.getScheduler().getContext();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        //获取Spring中的上下文    
		ApplicationContext appCtx = (ApplicationContext)schCtx.get("applicationContext");  

		JobDataMap data = context.getTrigger().getJobDataMap();
		String compoundPaykey = (String) data.get("compoundPaykey");
		log.info("定时更换任务开始:compoundPaykey:{}", compoundPaykey);
	}  
}  