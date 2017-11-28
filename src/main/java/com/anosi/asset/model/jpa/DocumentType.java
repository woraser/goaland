package com.anosi.asset.model.jpa;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "documentType")
public class DocumentType extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5756091079453622117L;

	private TypeValue typeValue;
	
	public TypeValue getTypeValue() {
		return typeValue;
	}

	public void setTypeValue(TypeValue typeValue) {
		this.typeValue = typeValue;
	}

	public static enum TypeValue {
		
		TECHNOLOGYDOCUMENT, BREAKDOWNDOCUMENT
		
	}

}
