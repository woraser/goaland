package com.anosi.asset.model.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "device")
public class Device extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 587030319812196854L;

	private Project project;

	private String productName;// 产品名称

	private String productNo;// 产品编号

	private String serialNo;

	private DevCategory devCategory;

	private List<Materiel> materielList = new ArrayList<>();// 物料

	private List<TechnicalParameter> technicalParameterList = new ArrayList<>();// 技术参数

	private Double longitude;// 经度

	private Double latitude;// 纬度

	// 为了在百度地图上显示点，还需要存储百度坐标
	private Double baiduLongitude;// 经度

	private Double baiduLatitude;// 纬度

	private District district;// 所属区县

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	@Column(unique = true, nullable = false)
	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DevCategory.class)
	@JoinColumn(nullable = false)
	public DevCategory getDevCategory() {
		return devCategory;
	}

	public void setDevCategory(DevCategory devCategory) {
		this.devCategory = devCategory;
	}

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "device", targetEntity = Materiel.class)
	public List<Materiel> getMaterielList() {
		return materielList;
	}

	public void setMaterielList(List<Materiel> materielList) {
		this.materielList = materielList;
	}

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "device", targetEntity = TechnicalParameter.class)
	public List<TechnicalParameter> getTechnicalParameterList() {
		return technicalParameterList;
	}

	public void setTechnicalParameterList(List<TechnicalParameter> technicalParameterList) {
		this.technicalParameterList = technicalParameterList;
	}

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Project.class)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getBaiduLongitude() {
		return baiduLongitude;
	}

	public void setBaiduLongitude(Double baiduLongitude) {
		this.baiduLongitude = baiduLongitude;
	}

	public Double getBaiduLatitude() {
		return baiduLatitude;
	}

	public void setBaiduLatitude(Double baiduLatitude) {
		this.baiduLatitude = baiduLatitude;
	}

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = District.class)
	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((serialNo == null) ? 0 : serialNo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Device other = (Device) obj;
		if (serialNo == null) {
			if (other.serialNo != null)
				return false;
		} else if (!serialNo.equals(other.serialNo))
			return false;
		return true;
	}

}
