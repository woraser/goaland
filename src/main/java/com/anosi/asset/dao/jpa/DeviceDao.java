package com.anosi.asset.dao.jpa;

import com.anosi.asset.model.jpa.Device;

public interface DeviceDao extends BaseJPADao<Device>{

	public Device findBySerialNo(String serialNo);
	
}
