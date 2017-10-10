package com.anosi.asset.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anosi.asset.model.jpa.MessageInfo;

public interface MessageInfoService extends BaseService<MessageInfo, Long> {

	public Page<MessageInfo> findByToAndReadTime(Long id, Pageable pageable);

}
