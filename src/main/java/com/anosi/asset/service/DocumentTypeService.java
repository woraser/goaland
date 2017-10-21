package com.anosi.asset.service;

import com.anosi.asset.model.jpa.DocumentType;

public interface DocumentTypeService extends BaseJPAService<DocumentType> {

	/***
	 * 更新type的name,会一同更新fileMetaData和technologyDocument
	 * @param documentType
	 * @param lastTypeName
	 */
	public void updateDocumentTypeName(DocumentType documentType, String lastTypeName);

}
