package com.anosi.asset.model.jpa;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.search.annotations.*;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/***
 * 项目
 * 
 * @author jinyao
 *
 */
@Entity
@Table(name = "project")
@Indexed
@Analyzer(impl = IKAnalyzer.class)
public class Project extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8182240180903969356L;

	@Field
	private String name;// 项目名称

	@Field(analyze = Analyze.NO)
	private String number;// 项目编号

	@Field
	private String location;// 项目地址

	@JSONField(serialize=false)  
	@ContainedIn
	private List<StartDetail> startDetailList = new ArrayList<>();

	@JSONField(serialize=false)  
	@ContainedIn
	private List<Device> deviceList = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(unique = true, nullable = false)
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "project")
	public List<StartDetail> getStartDetailList() {
		return startDetailList;
	}

	public void setStartDetailList(List<StartDetail> startDetailList) {
		this.startDetailList = startDetailList;
	}

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "project", targetEntity = Device.class)
	public List<Device> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<Device> deviceList) {
		this.deviceList = deviceList;
	}

}
