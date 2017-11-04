package com.anosi.asset.dao.jpa;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import com.anosi.asset.model.jpa.Device;
import com.anosi.asset.model.jpa.QDevice;

public interface DeviceDao extends BaseJPADao<Device>, QuerydslBinderCustomizer<QDevice> {

	@Override
	default public void customize(QuerydslBindings bindings, QDevice qDevice) {
		bindings.bind(qDevice.serialNo).first((path, value) -> {
			if (value.startsWith("like$")) {
				value = value.replace("like$", "");
				return path.startsWithIgnoreCase(value);
			} else {
				return path.eq(value);
			}
		});
	}

	public Device findBySerialNo(String serialNo);

	default public Page<Device> findBySearchContent(EntityManager entityManager, String searchContent,
			Pageable pageable) {
		return findBySearchContent(entityManager, searchContent, pageable, Device.class, "project.name",
				"project.number", "project.location", "productName", "productNo", "productSpecifications", "serialNo",
				"rfid");
	}

}
