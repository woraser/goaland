package com.anosi.asset.model.jpa;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.anosi.asset.util.DateFormatUtil;

/****
 * 物料
 * 
 * @author jinyao
 *
 */
@Entity
@Table(name = "materiel", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "device_id" }) })
public class Materiel extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6709768657814585431L;

	private String name;

	private Device device;

	private Date beginTime;// 开始时间

	private int checkYear = 0, checkMonth = 0, checkDay = 0;// 设备检测周期

	private int remindYear = 0, remindMonth = 0, remindDay = 0;// 提前预警年数,月数,天数

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "device_id")
	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public int getCheckYear() {
		return checkYear;
	}

	public void setCheckYear(int checkYear) {
		this.checkYear = checkYear;
	}

	public int getCheckMonth() {
		return checkMonth;
	}

	public void setCheckMonth(int checkMonth) {
		this.checkMonth = checkMonth;
	}

	public int getCheckDay() {
		return checkDay;
	}

	public void setCheckDay(int checkDay) {
		this.checkDay = checkDay;
	}

	public int getRemindYear() {
		return remindYear;
	}

	public void setRemindYear(int remindYear) {
		this.remindYear = remindYear;
	}

	public int getRemindMonth() {
		return remindMonth;
	}

	public void setRemindMonth(int remindMonth) {
		this.remindMonth = remindMonth;
	}

	public int getRemindDay() {
		return remindDay;
	}

	public void setRemindDay(int remindDay) {
		this.remindDay = remindDay;
	}

	/****
	 * 判断是否需要预警
	 * 
	 * <P>
	 * 先将开始时间上叠加周期，利用递归叠加到当前时间所处的周期，然后周期减去预警时间，再和当前时间比较
	 * </P>
	 * 
	 * @return
	 */
	@Transient
	public boolean needRemind() {
		Date nowDate = new Date();
		Date cycleDate = ensureCycle(nowDate, beginTime);
		Date remindTime = DateFormatUtil.getBeforeDayTime(DateFormatUtil.getBeforeMonthTime(
				DateFormatUtil.getBeforeYearTime(cycleDate, this.remindYear), this.remindMonth), this.remindDay);
		if (nowDate.getTime() < remindTime.getTime()) {
			return false;
		} else {
			return true;
		}
	}

	/***
	 * 递归获取比当前时间大一级的周期
	 * 
	 * @param nowDate
	 * @param cycleDate
	 * @return
	 */
	@Transient
	private Date ensureCycle(Date nowDate, Date cycleDate) {
		cycleDate = DateFormatUtil.getLaterDayTime(DateFormatUtil.getLaterMonthTime(
				DateFormatUtil.getLaterYearTime(cycleDate, this.checkYear), this.checkMonth), this.checkDay);
		if (cycleDate.getTime() < nowDate.getTime()) {
			return ensureCycle(nowDate, cycleDate);
		} else {
			return cycleDate;
		}
	}

}
