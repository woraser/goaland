package com.anosi.asset.dao.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anosi.asset.model.jpa.MessageInfo;

public interface MessageInfoDao extends BaseJPADao<MessageInfo> {

	public Page<MessageInfo> findByTo_idEqualsAndReadTimeIsNull(Long id, Pageable pageable);
	
}
