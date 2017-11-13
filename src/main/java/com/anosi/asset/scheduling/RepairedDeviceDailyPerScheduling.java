package com.anosi.asset.scheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.anosi.asset.service.RepairedDeviceDailyPerService;

@Component
public class RepairedDeviceDailyPerScheduling {

	@Autowired
	private RepairedDeviceDailyPerService repairedDeviceDailyPerService;
	
	/***
	 * 每天0点保存前一天的维修设备数量
	 */
	@Scheduled(cron="0 0 0 * * *")
	public void saveRepairedPerDaily(){
		repairedDeviceDailyPerService.saveRepairedPerDaily();
	}
	
}
