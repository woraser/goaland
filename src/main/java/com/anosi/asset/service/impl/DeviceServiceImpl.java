package com.anosi.asset.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.DeviceDao;
import com.anosi.asset.model.jpa.Device;
import com.anosi.asset.service.DeviceService;

@Service("deviceService")
@Transactional
public class DeviceServiceImpl extends BaseServiceImpl<Device> implements DeviceService{
	
	@Autowired
	private DeviceDao deviceDao;

	@Override
	public BaseJPADao<Device> getRepository() {
		return deviceDao;
	}

	@Override
	public Device findBySerialNo(String serialNo) {
		return deviceDao.findBySerialNo(serialNo);
	}

}
