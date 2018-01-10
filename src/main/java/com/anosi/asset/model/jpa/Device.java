package com.anosi.asset.model.jpa;

import com.alibaba.fastjson.annotation.JSONField;
import com.anosi.asset.model.elasticsearch.Content;
import org.hibernate.search.annotations.*;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.persistence.*;
import javax.persistence.Index;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "device", indexes = { @Index(columnList = "rfid", name = "device_rfid") })
@Indexed
@Analyzer(impl = IKAnalyzer.class)
public class Device extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 587030319812196854L;

	@Content(extractFields = { "project.name", "project.number", "project.location" })
	@IndexedEmbedded(depth = 1)
	@JSONField(serialize = false)
	private Project project;

	@Content
	@Field
	@JSONField(serialize = false)
	private String productName;// 产品名称

	@Content
	@Field(analyze = Analyze.NO)
	@JSONField(serialize = false)
	private String productNo;// 产品编号

	@Content
	@Field
	@JSONField(serialize = false)
	private String productSpecifications;// 产品规格

	@Content
	@Field(analyze = Analyze.NO)
	private String serialNo;

	@Content
	@Field(analyze = Analyze.NO)
	private String rfid;// rfid的值等于二维码的值

	@JSONField(serialize = false)
	private DevCategory devCategory;

	@JSONField(serialize = false)
	private Date commissioningTime;// 投运时间

	@JSONField(serialize = false)
	@ContainedIn
	private List<Materiel> materielList = new ArrayList<>();// 物料

	@JSONField(serialize = false)
	private List<TechnicalParameter> technicalParameterList = new ArrayList<>();// 技术参数

	@JSONField(serialize = false)
	private Double longitude;// 经度

	@JSONField(serialize = false)
	private Double latitude;// 纬度

	@JSONField(serialize = false)
	// 为了在百度地图上显示点，还需要存储百度坐标
	private Double baiduLongitude;// 经度

	@JSONField(serialize = false)
	private Double baiduLatitude;// 纬度

	@JSONField(serialize = false)
	private District district;// 所属区县

	@JSONField(serialize = false)
	private List<RepairDetail> repairDetailList = new ArrayList<>();

	@JSONField(serialize = false)
	private List<EntrustDetail> entrustDetailList = new ArrayList<>();

	@JSONField(serialize = false)
	private List<RepairedDeviceDailyPer> repairedDeviceDailyPerList = new ArrayList<>();

	@JSONField(serialize = false)
	private List<Account> remindReceiverList = new ArrayList<>();

	@JSONField(serialize = false)
	@IndexedEmbedded
	private List<Account> ownerList = new ArrayList<>();

	// fbox
	private String boxId;

	private String boxSN;

	@Column(unique = true, nullable = false)
	public String getBoxId() {
		return boxId;
	}

	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}

	@Column(unique = true, nullable = false)
	public String getBoxSN() {
		return boxSN;
	}

	public void setBoxSN(String boxSN) {
		this.boxSN = boxSN;
	}

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

	public String getRfid() {
		return rfid;
	}

	public void setRfid(String rfid) {
		this.rfid = rfid;
	}

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DevCategory.class)
	@JoinColumn(nullable = false)
	public DevCategory getDevCategory() {
		return devCategory;
	}

	public void setDevCategory(DevCategory devCategory) {
		this.devCategory = devCategory;
	}

	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "device", targetEntity = Materiel.class)
	public List<Materiel> getMaterielList() {
		return materielList;
	}

	public void setMaterielList(List<Materiel> materielList) {
		this.materielList = materielList;
	}

	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "device", targetEntity = TechnicalParameter.class)
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

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "deviceList")
	public List<RepairDetail> getRepairDetailList() {
		return repairDetailList;
	}

	public void setRepairDetailList(List<RepairDetail> repairDetailList) {
		this.repairDetailList = repairDetailList;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "deviceList")
	public List<EntrustDetail> getEntrustDetailList() {
		return entrustDetailList;
	}

	public void setEntrustDetailList(List<EntrustDetail> entrustDetailList) {
		this.entrustDetailList = entrustDetailList;
	}

	public String getProductSpecifications() {
		return productSpecifications;
	}

	public void setProductSpecifications(String productSpecifications) {
		this.productSpecifications = productSpecifications;
	}

	public Date getCommissioningTime() {
		return commissioningTime;
	}

	public void setCommissioningTime(Date commissioningTime) {
		this.commissioningTime = commissioningTime;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "deviceList", targetEntity = RepairedDeviceDailyPer.class)
	public List<RepairedDeviceDailyPer> getRepairedDeviceDailyPerList() {
		return repairedDeviceDailyPerList;
	}

	public void setRepairedDeviceDailyPerList(List<RepairedDeviceDailyPer> repairedDeviceDailyPerList) {
		this.repairedDeviceDailyPerList = repairedDeviceDailyPerList;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	public List<Account> getRemindReceiverList() {
		return remindReceiverList;
	}

	public void setRemindReceiverList(List<Account> remindReceiverList) {
		this.remindReceiverList = remindReceiverList;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	public List<Account> getOwnerList() {
		return ownerList;
	}

	public void setOwnerList(List<Account> ownerList) {
		this.ownerList = ownerList;
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

	public Device(Long id, String serialNo) {
		super();
		setId(id);
		this.serialNo = serialNo;
	}

	public Device() {
		super();
		// TODO Auto-generated constructor stub
	}

}
