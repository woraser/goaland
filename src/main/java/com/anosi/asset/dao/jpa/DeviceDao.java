package com.anosi.asset.dao.jpa;

import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import com.anosi.asset.model.jpa.Device;
import com.anosi.asset.model.jpa.QDevice;

public interface DeviceDao extends BaseJPADao<Device> , QuerydslBinderCustomizer<QDevice>{

	@Override
	default public void customize(QuerydslBindings bindings, QDevice qDevice) {
		bindings.bind(qDevice.serialNo).first((path, value) ->  {
			if(value.startsWith("like$")){
				value = value.replace("like$", "");
				return path.startsWithIgnoreCase(value);
			}else{
				return path.eq(value);
			}
		});
	}
	
	public Device findBySerialNo(String serialNo);
	
}
