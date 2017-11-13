package com.anosi.asset.model.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "devCategory")
public class DevCategory extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7974456468757773048L;

	private CategoryType categoryType;

	private List<Device> deviceList = new ArrayList<>();

	private List<DevCategoryStructures> mainDevCategoryStructures = new ArrayList<>();

	private List<DevCategoryStructures> subDevCategoryStructures = new ArrayList<>();

	private List<RepairedDeviceDailyPer> repairedDeviceDailyPers = new ArrayList<>();

	@OneToMany(targetEntity = Device.class, cascade = {
			CascadeType.MERGE }, fetch = FetchType.LAZY, mappedBy = "devCategory")
	public List<Device> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<Device> deviceList) {
		this.deviceList = deviceList;
	}

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "mainDevCategory", targetEntity = DevCategoryStructures.class)
	public List<DevCategoryStructures> getMainDevCategoryStructures() {
		return mainDevCategoryStructures;
	}

	public void setMainDevCategoryStructures(List<DevCategoryStructures> mainDevCategoryStructures) {
		this.mainDevCategoryStructures = mainDevCategoryStructures;
	}

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "subDevCategory", targetEntity = DevCategoryStructures.class)
	public List<DevCategoryStructures> getSubDevCategoryStructures() {
		return subDevCategoryStructures;
	}

	public void setSubDevCategoryStructures(List<DevCategoryStructures> subDevCategoryStructures) {
		this.subDevCategoryStructures = subDevCategoryStructures;
	}

	public CategoryType getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(CategoryType categoryType) {
		this.categoryType = categoryType;
	}

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "devCategory", targetEntity = RepairedDeviceDailyPer.class)
	public List<RepairedDeviceDailyPer> getRepairedDeviceDailyPers() {
		return repairedDeviceDailyPers;
	}

	public void setRepairedDeviceDailyPers(List<RepairedDeviceDailyPer> repairedDeviceDailyPers) {
		this.repairedDeviceDailyPers = repairedDeviceDailyPers;
	}

	public static enum CategoryType {

		HVSCCOOLING("直流输电换流阀水冷设备", "HVSC COOLING"), FACTS("柔性交流输配电晶闸管阀纯水冷却设备", "FACTS"), ELECTRICDRIVECOOLING(
				"新能源发电变流器纯水冷却设备", "ELECTRIC DRIVE COOLING"), RENEWABLEENERGYCOOLING("大功率电气传动变频器纯水冷却设备",
						"RENEWABLE ENERGY COOLING "), ACCUMULATIONOFCOLD("蓄冷设备", "ACCUMULATION OF COLD");

		private String chinese;

		private String english;

		private CategoryType(String chinese, String english) {
			this.chinese = chinese;
			this.english = english;
		}

		public String getChinese() {
			return chinese;
		}

		public void setChinese(String chinese) {
			this.chinese = chinese;
		}

		public String getEnglish() {
			return english;
		}

		public void setEnglish(String english) {
			this.english = english;
		}

	}

}
