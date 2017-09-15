package com.anosi.asset.service;

import com.anosi.asset.model.jpa.Device;

public interface DeviceService extends BaseService<Device, Long>{

	public Device findBySerialNo(String serialNo);
	
}
