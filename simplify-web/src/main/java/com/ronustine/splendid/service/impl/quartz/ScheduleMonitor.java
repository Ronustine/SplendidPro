package com.founder.isp.fpay.service.impl.quartz;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.founder.isp.common.pojo.CompoundPaykey;
import com.founder.isp.fpay.service.CompoundPayKeyService;

@Configuration
@EnableScheduling
@Component
public class ScheduleMonitor {

    private static final Logger log =  LoggerFactory.getLogger(ScheduleMonitor.class);  

	private static final String NAME = "COMPOUND_PAY_KEY_";
	private static final String JOB_NAME = "JOB_";
	private static final String JOB_GROUP_NAME = "JOB_GROUP_COMPOUND_PAY_KEY_";
	private static final String TRIGGER_NAME = "TRIGGER_";
	private static final String TRIGGER_GROUP_NAME = "TRIGGER_GROUP_COMPOUND_PAY_KEY_";

	@Resource
	Scheduler scheduler;
	
	public Scheduler getScheduler() {  
        return scheduler;  
    }  
  
    public void setScheduler(Scheduler scheduler) {  
        this.scheduler = scheduler;  
    }  
    
    JobKey jobKey;
	JobDetail jobDetail;
	
	@Autowired
	CompoundPayKeyService compoundPayKeyService;

	{
		jobKey = new JobKey(JOB_NAME + NAME, JOB_GROUP_NAME);
		jobDetail = JobBuilder.newJob(ScheduleTask.class).
	            withIdentity(jobKey).
	            storeDurably(true).build();
	}
	
	@Scheduled(fixedRate = 1000 * 10) // 每隔5s查库，并根据查询结果决定是否重新设置定时任务
	public void scheduleUpdateCronTrigger() throws SchedulerException {
		List<CompoundPaykey> compoundPayKeys = compoundPayKeyService.getAllUsingPaykey();
		log.info("需要轮转paykey的数量:{}", compoundPayKeys.size());

		JobDetail temp = scheduler.getJobDetail(jobKey);
		if (null == temp) {
			scheduler.addJob(jobDetail,false);
		}
		
		for (CompoundPaykey cPaykey : compoundPayKeys) {
			StringBuffer triggerName = new StringBuffer();
			triggerName.append(TRIGGER_NAME).append(NAME).append(cPaykey.getCompoundPaykey());
			String compoundPaykeyCron = cPaykey.getRule();
			TriggerKey triggerKey = new TriggerKey(triggerName.toString(), TRIGGER_GROUP_NAME);
			
			log.info("检查触发器，checking triggers：{}",  triggerName.toString());
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			if (null == trigger) {
				log.info("未找到，新增触发器， not found, adding：{}",  triggerName.toString());
				trigger = TriggerBuilder.
                        newTrigger().
                        withIdentity(triggerKey).
                        withSchedule(CronScheduleBuilder.cronSchedule(compoundPaykeyCron)).
                        usingJobData("compoundPaykey", cPaykey.getCompoundPaykey()).
                        forJob(jobDetail).build();
				scheduler.scheduleJob(trigger);
			}

			if (trigger.getCronExpression().equals(compoundPaykeyCron)) {
				log.info("触发器未发生修改");
				continue;
			}
			
			log.info("修改触发器：{}",  triggerName.toString());
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(compoundPaykeyCron);
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder)
					.usingJobData("compoundPaykey", cPaykey.getCompoundPaykey()).build();
			scheduler.rescheduleJob(triggerKey, trigger);
		}

		if(!scheduler.isStarted()) {
			log.info("启动quartz：{}", scheduler.getSchedulerName());
			scheduler.start();
		}
		for (String groupName : scheduler.getJobGroupNames()) {
			for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
				String jobName = jobKey.getName();
				String jobGroup = jobKey.getGroup();
				// get job's trigger
				List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
				Date nextFireTime = triggers.get(0).getNextFireTime();
				log.info("[jobName]:{},[groupName]:{},nextFireTime:{}", jobName, jobGroup, nextFireTime);
			}
		}

	}
	
	private Scheduler getSchedule() throws SchedulerException {
		scheduler = StdSchedulerFactory.getDefaultScheduler();
		// 延时启动，应用启动5秒后
		return scheduler;
	}

}