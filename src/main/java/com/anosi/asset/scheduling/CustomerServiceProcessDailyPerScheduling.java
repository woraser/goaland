package com.anosi.asset.scheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.anosi.asset.service.CustomerServiceProcessDailyPerService;

@Component
public class CustomerServiceProcessDailyPerScheduling {

	@Autowired
	private CustomerServiceProcessDailyPerService customerServiceProcessDailyPerService;
	
	/***
	 * 每天0点保存前一天的任务完成数和为完成数
	 */
	@Scheduled(cron="0 0 0 * * *")
	public void saveProcessPerDaily(){
		customerServiceProcessDailyPerService.saveProcessPerDaily();
	}
	
}
