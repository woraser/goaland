package com.anosi.asset.model.jpa;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/***
 * 技术参数
 * 
 * @author jinyao
 *
 */
@Entity
@Table(name = "technicalParameter", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "device_id" }) })
public class TechnicalParameter extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2807519756448320481L;

	private String name;// 命名规则,例如"tt3"

	private String parameterDescribe;// 描述,例如出水口温度

	private Double maxVal;

	private Double minVal;

	private Device device;
	
	private String unit;
	
	private String actualValue;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParameterDescribe() {
		return parameterDescribe;
	}

	public void setParameterDescribe(String parameterDescribe) {
		this.parameterDescribe = parameterDescribe;
	}

	public Double getMaxVal() {
		return maxVal;
	}

	public void setMaxVal(Double maxVal) {
		this.maxVal = maxVal;
	}

	public Double getMinVal() {
		return minVal;
	}

	public void setMinVal(Double minVal) {
		this.minVal = minVal;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "device_id")
	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Transient
	public String getActualValue() {
		return actualValue;
	}

	public void setActualValue(String actualValue) {
		this.actualValue = actualValue;
	}
	
}
