package com.anosi.asset.dao.jpa;

import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import com.anosi.asset.model.jpa.Device;
import com.anosi.asset.model.jpa.QDevice;

public interface DeviceDao extends BaseJPADao<Device> , QuerydslBinderCustomizer<QDevice>{

	@Override
	default public void customize(QuerydslBindings bindings, QDevice qDevice) {
		bindings.bind(qDevice.serialNo).first((path, value) ->  path.containsIgnoreCase(value));
	}
	
	public Device findBySerialNo(String serialNo);
	
}
