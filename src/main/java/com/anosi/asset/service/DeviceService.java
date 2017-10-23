package com.anosi.asset.service;

import com.anosi.asset.model.jpa.Device;

public interface DeviceService extends BaseJPAService<Device>{

	public Device findBySerialNo(String serialNo);

	/***
	 * 为设备设置经纬度
	 * 
	 * @param device
	 * @return
	 */
	Device setDeviceDistrict(Device device);
	
}
