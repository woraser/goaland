package com.anosi.asset.service;

import org.springframework.data.domain.Pageable;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.model.jpa.RepairedDeviceDailyPer;

public interface RepairedDeviceDailyPerService extends BaseJPAService<RepairedDeviceDailyPer>{

	/***
	 * 将每天的维修设备存到数据库
	 */
	void saveRepairedPerDaily();

	/***
	 * 为报表查询数据
	 * @param pageable
	 * @return
	 */
	JSONObject findToReport(Pageable pageable);

}
