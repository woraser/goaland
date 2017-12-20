package com.anosi.asset.model.jpa;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.alibaba.fastjson.annotation.JSONField;

/***
 * 流程发起时的表单字段
 * 
 * @author jinyao
 *
 */
@Entity
@Table(name = "startDetail")
@Indexed
@Analyzer(impl = IKAnalyzer.class)
public class StartDetail extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7148672408140355700L;

	@IndexedEmbedded(depth = 1)
	private Project project;// 涉及的项目

	private Belong belong;// 归属

	private String productName;// 产品名称

	private String productNo;// 产品编号

	private String productSpecifications;// 产品规格

	private ProductType productType;// 产品类型

	private String customerMan;// 客户联系人

	private String customerNumber;// 客户联系人电话

	private String projectMan;// 项目联系人

	private String projectNumber;// 项目联系人电话

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date estimatedTime;// 预估维修时间

	private String baseDemands;// 基本要求

	private String specialDemands;// 特殊要求

	private Account nextAssignee;// 下一步办理人

	@JSONField(serialize=false)  
	private CustomerServiceProcess customerServiceProcess;

	public Belong getBelong() {
		return belong;
	}

	public void setBelong(Belong belong) {
		this.belong = belong;
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

	public String getProductSpecifications() {
		return productSpecifications;
	}

	public void setProductSpecifications(String productSpecifications) {
		this.productSpecifications = productSpecifications;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public String getCustomerMan() {
		return customerMan;
	}

	public void setCustomerMan(String customerMan) {
		this.customerMan = customerMan;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getProjectMan() {
		return projectMan;
	}

	public void setProjectMan(String projectMan) {
		this.projectMan = projectMan;
	}

	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}

	public Date getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(Date estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	@Type(type = "text")
	public String getBaseDemands() {
		return baseDemands;
	}

	public void setBaseDemands(String baseDemands) {
		this.baseDemands = baseDemands;
	}

	@Type(type = "text")
	public String getSpecialDemands() {
		return specialDemands;
	}

	public void setSpecialDemands(String specialDemands) {
		this.specialDemands = specialDemands;
	}

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Account.class)
	public Account getNextAssignee() {
		return nextAssignee;
	}

	public void setNextAssignee(Account nextAssignee) {
		this.nextAssignee = nextAssignee;
	}

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Project.class)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "startDetail")
	public CustomerServiceProcess getCustomerServiceProcess() {
		return customerServiceProcess;
	}

	public void setCustomerServiceProcess(CustomerServiceProcess customerServiceProcess) {
		this.customerServiceProcess = customerServiceProcess;
	}

	public static enum ProductType {
		DC, FACTS, NEWENERGY, LABPROJECT, OTHER;
	}

	public static enum Belong {
		SOUTHERNPART, NORTHPART;
	}

}
