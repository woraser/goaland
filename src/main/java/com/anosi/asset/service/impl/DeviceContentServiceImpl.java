package com.anosi.asset.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.elasticsearch.BaseContentDao;
import com.anosi.asset.dao.elasticsearch.DeviceContentDao;
import com.anosi.asset.model.elasticsearch.DeviceContent;
import com.anosi.asset.model.jpa.Device;
import com.anosi.asset.service.DeviceContentService;

@Service("deviceContentService")
@Transactional
public class DeviceContentServiceImpl extends BaseContentServiceImpl<DeviceContent, String, Device>
		implements DeviceContentService {

	@Autowired
	private DeviceContentDao deviceContentDao;

	@Override
	public BaseContentDao<DeviceContent, String> getRepository() {
		return deviceContentDao;
	}

	@Override
	public DeviceContent saveContent(Device device) throws Exception {
		String id = String.valueOf(device.getId());
		DeviceContent deviceContent = deviceContentDao.findOne(id);
		if (deviceContent == null) {
			deviceContent = new DeviceContent();
			deviceContent.setId(id);
		}
		deviceContent = setCommonContent(deviceContent, device);
		return deviceContentDao.save(deviceContent);
	}

	@Override
	public <S extends Device> Iterable<DeviceContent> saveContent(Iterable<S> obs) throws Exception {
		List<DeviceContent> deviceContents = new ArrayList<>();
		for (Device device : obs) {
			String id = String.valueOf(device.getId());
			DeviceContent accountContent = deviceContentDao.findOne(id);
			if (accountContent == null) {
				accountContent = new DeviceContent();
				accountContent.setId(id);
			}
			accountContent = setCommonContent(accountContent, device);
			deviceContents.add(accountContent);
		}
		return deviceContentDao.save(deviceContents);
	}

}
