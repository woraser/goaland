package com.anosi.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.MaterielDao;
import com.anosi.asset.model.jpa.Materiel;
import com.anosi.asset.service.MaterielService;

@Service("materielService")
@Transactional
public class MaterielServiceImpl extends BaseJPAServiceImpl<Materiel> implements MaterielService{

	@Autowired
	private MaterielDao materielDao;
	
	@Override
	public BaseJPADao<Materiel> getRepository() {
		return materielDao;
	}

}
