package com.anosi.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.DocumentTypeDao;
import com.anosi.asset.model.jpa.DocumentType;
import com.anosi.asset.service.DocumentTypeService;

@Service("documentTypeService")
@Transactional
public class DocumentTypeServiceImpl extends BaseJPAServiceImpl<DocumentType> implements DocumentTypeService {

	@Autowired
	private DocumentTypeDao documentTypeDao;

	@Override
	public BaseJPADao<DocumentType> getRepository() {
		return documentTypeDao;
	}

}
