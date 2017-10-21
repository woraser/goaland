package com.anosi.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.MessageInfoDao;
import com.anosi.asset.model.jpa.MessageInfo;
import com.anosi.asset.service.MessageInfoService;

@Service("messageInfoService")
@Transactional
public class MessageInfoServiceImpl extends BaseJPAServiceImpl<MessageInfo> implements MessageInfoService {

	@Autowired
	private MessageInfoDao messageInfoDao;

	@Override
	public BaseJPADao<MessageInfo> getRepository() {
		return messageInfoDao;
	}

	@Override
	public Page<MessageInfo> findByToAndReadTime(Long id, Pageable pageable) {
		return messageInfoDao.findByTo_idEqualsAndReadTimeIsNull(id, pageable);
	}

}
