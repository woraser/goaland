package com.anosi.asset.model.jpa;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="customerServiceProcess")
public class CustomerServiceProcess extends BaseProcess{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2663257381437985964L;

	public CustomerServiceProcess() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public CustomerServiceProcess(String processInstanceId) {
		super();
		this.processInstanceId = Objects.requireNonNull(processInstanceId);
	}
	
}
