package com.anosi.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.AgreementStatusDao;
import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.model.jpa.AgreementStatus;
import com.anosi.asset.service.AgreementStatusService;

@Service("agreementStatusService")
@Transactional
public class AgreementStatusServiceImpl extends BaseJPAServiceImpl<AgreementStatus> implements AgreementStatusService {

	@Autowired
	private AgreementStatusDao agreementStatusDao;
	
	@Override
	public BaseJPADao<AgreementStatus> getRepository() {
		return agreementStatusDao;
	}

}
