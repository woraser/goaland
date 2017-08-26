package com.anosi.asset.model.jpa;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "documentType")
public class DocumentType extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5756091079453622117L;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
