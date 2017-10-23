package com.anosi.asset.service.impl;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.component.MapComponent;
import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.DeviceDao;
import com.anosi.asset.model.jpa.Device;
import com.anosi.asset.model.jpa.District;
import com.anosi.asset.service.DeviceService;
import com.anosi.asset.util.MapUtil;

@Service("deviceService")
@Transactional
public class DeviceServiceImpl extends BaseJPAServiceImpl<Device> implements DeviceService{
	
	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private MapComponent mapComponent;

	@Override
	public BaseJPADao<Device> getRepository() {
		return deviceDao;
	}

	@Override
	public Device findBySerialNo(String serialNo) {
		return deviceDao.findBySerialNo(serialNo);
	}
	
	@Override
	public Device setDeviceDistrict(Device device) {
		Double longitude = device.getLongitude();
		Double latitude = device.getLatitude();
		if (longitude == null) {
			throw new RuntimeException(i18nComponent.getMessage("device.longitude.cannot.null"));
		}
		if (latitude == null) {
			throw new RuntimeException(i18nComponent.getMessage("device.latitude.cannot.null"));
		}
		JSONObject addressComponent = MapUtil.getAddressComponent(String.valueOf(longitude), String.valueOf(latitude));
		District district = mapComponent.createMap(addressComponent);
		device.setDistrict(district);
		// 获取转换的百度坐标
		JSONObject convertLocation = MapUtil.convertLocation(String.valueOf(longitude), String.valueOf(latitude));
		device.setBaiduLongitude(
				Double.parseDouble(new String(Base64.getDecoder().decode(convertLocation.getString("x")))));
		device.setBaiduLatitude(
				Double.parseDouble(new String(Base64.getDecoder().decode(convertLocation.getString("y")))));
		return device;
	}

}
