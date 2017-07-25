package com.anosi.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.MessageInfoDao;
import com.anosi.asset.model.jpa.MessageInfo;
import com.anosi.asset.service.MessageInfoService;

@Service("messageInfoService")
@Transactional
public class MessageInfoServiceImpl implements MessageInfoService {
	
	@Autowired
	private MessageInfoDao messageInfoDao;

	@Override
	public MessageInfo save(MessageInfo messageInfo) {
		return messageInfoDao.save(messageInfo);
	}

}
