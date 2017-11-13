package com.anosi.asset.service;

import com.anosi.asset.model.jpa.CustomerServiceProcessDailyPer;

public interface CustomerServiceProcessDailyPerService extends BaseJPAService<CustomerServiceProcessDailyPer>{

	/***
	 * 将每天的流程的完成与未完成数量存入数据库
	 */
	void saveProcessPerDaily();

}
