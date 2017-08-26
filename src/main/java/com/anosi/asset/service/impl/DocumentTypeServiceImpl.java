package com.anosi.asset.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.DocumentTypeDao;
import com.anosi.asset.model.jpa.DocumentType;
import com.anosi.asset.service.DocumentTypeService;
import com.anosi.asset.service.FileMetaDataService;
import com.anosi.asset.service.TechnologyDocumentService;

@Service("documentTypeService")
@Transactional
public class DocumentTypeServiceImpl extends BaseServiceImpl<DocumentType> implements DocumentTypeService {

	@Autowired
	private DocumentTypeDao documentTypeDao;
	@Autowired
	private FileMetaDataService fileMetaDataService;
	@Autowired
	private TechnologyDocumentService technologyDocumentService;

	@Override
	public BaseJPADao<DocumentType> getRepository() {
		return documentTypeDao;
	}

	@Override
	public void updateDocumentTypeName(DocumentType documentType, String lastTypeName) {
		String nowName = documentType.getName();
		documentTypeDao.save(documentType);
		// 用nowName更新FileMetaData和technologyDocument
		fileMetaDataService.updateIdentification(lastTypeName, nowName);
		technologyDocumentService.updateType(lastTypeName, nowName);
	}

}
