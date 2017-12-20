package com.anosi.asset.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

	/****
	 * 模糊搜索
	 * 
	 * @param searchContent
	 * @param pageable
	 * @return
	 */
	public Page<Device> findByContentSearch(String searchContent, Pageable pageable);
	
	public Device findByRfid(String rfid);
	
	public List<Device> findIdAndSN();

}
