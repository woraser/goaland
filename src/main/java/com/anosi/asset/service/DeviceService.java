package com.anosi.asset.service;

import com.alibaba.fastjson.JSONArray;
import com.anosi.asset.model.jpa.Device;
import com.querydsl.core.types.Predicate;

public interface DeviceService extends BaseJPAService<Device>{

	public Device findBySerialNo(String serialNo);

	/***
	 * 为设备设置经纬度
	 * 
	 * @param device
	 * @return
	 */
	Device setDeviceDistrict(Device device);

	/***
	 * 获取设备的分布
	 * 
	 * @param predicate
	 * @return
	 */
	public JSONArray ascertainArea(Predicate predicate);

}
