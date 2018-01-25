package com.anosi.asset.model.jpa;

import com.anosi.asset.util.DateFormatUtil;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.persistence.*;
import java.util.Date;

/****
 * 物料
 *
 * @author jinyao
 *
 */
@Entity
@Table(name = "materiel", uniqueConstraints = {@UniqueConstraint(columnNames = {"number", "device_id"})})
@Indexed
@Analyzer(impl = IKAnalyzer.class)
public class Materiel extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 6709768657814585431L;

    @Field
    private String number;// 物料编码

    @Field
    private String name;

    @IndexedEmbedded
    private Device device;

    private Date beginTime;// 开始时间

    private Date lastCheckTime;// 上一次维护时间

    private int checkYear = 0, checkMonth = 0, checkDay = 0;// 设备检测周期

    private int remindYear = 0, remindMonth = 0, remindDay = 0;// 提前预警年数,月数,天数

    private int remainderDay = 0;// 距离检测时间还剩下的天数，每日更新

    private Status status = Status.NORMAL;

    public Materiel() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Materiel(String number, String name, Device device, Date beginTime, int checkYear, int checkMonth, int checkDay,
                    int remindYear, int remindMonth, int remindDay) {
        super();
        this.number = number;
        this.name = name;
        this.device = device;
        this.beginTime = beginTime;
        this.checkYear = checkYear;
        this.checkMonth = checkMonth;
        this.checkDay = checkDay;
        this.remindYear = remindYear;
        this.remindMonth = remindMonth;
        this.remindDay = remindDay;
    }

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

    public int getRemainderDay() {
        return remainderDay;
    }

    public void setRemainderDay(int remainderDay) {
        this.remainderDay = remainderDay;
    }

    public Date getLastCheckTime() {
        return lastCheckTime;
    }

    public void setLastCheckTime(Date lastCheckTime) {
        this.lastCheckTime = lastCheckTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
        if (checkYear != 0 || checkMonth != 0 || checkDay != 0) {
            Date nowDate = new Date();// 当前日期
            Date cycleDate = ensureCycle(lastCheckTime == null ? beginTime : lastCheckTime);// 比上一次维护时间大的一个周期
            remainderDay = DateFormatUtil.daysBetween(nowDate, cycleDate);// 顺手把剩余天数设置上

            Date remindTime = DateFormatUtil
                    .getBeforeDayTime(
                            DateFormatUtil.getBeforeMonthTime(
                                    DateFormatUtil.getBeforeYearTime(cycleDate, this.remindYear), this.remindMonth),
                            this.remindDay);

            // 防止手动修改维护周期,检测status
            if (nowDate.getTime() < remindTime.getTime()) {
                status = Status.NORMAL;
            } else if (nowDate.getTime() > cycleDate.getTime()) {
                status = Status.OVER;// 超时
            } else {
                status = Status.REMIND;
            }

            // 如果年月日相等
            if (DateFormatUtil.compareYear(nowDate, remindTime) && DateFormatUtil.compareMonth(nowDate, remindTime)
                    && DateFormatUtil.compareDay(nowDate, remindTime)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /***
     * 获取现在的周期
     *
     * @param cycleDate
     * @return
     */
    @Transient
    private Date ensureCycle(Date cycleDate) {
        cycleDate = DateFormatUtil.getLaterDayTime(DateFormatUtil.getLaterMonthTime(
                DateFormatUtil.getLaterYearTime(cycleDate, this.checkYear), this.checkMonth), this.checkDay);
        return cycleDate;
    }

    /***
     * 获取预警周期
     *
     * @return
     */
    @Transient
    public String getRemindDate() {
        StringBuilder sb = new StringBuilder();
        if (remindYear != 0) {
            sb.append(remindYear + "年");
        }
        if (remindMonth != 0) {
            sb.append(remindMonth + "月");
        }
        if (remindDay != 0) {
            sb.append(remindDay + "日");
        }
        if (sb.length() != 0) {
            sb.insert(0, "提前");
        }
        return sb.toString();
    }

    /***
     * 获取维护周期
     *
     * @return
     */
    @Transient
    public String getCheckDate() {
        StringBuilder sb = new StringBuilder();
        if (checkYear != 0) {
            sb.append(checkYear + "年");
        }
        if (checkMonth != 0) {
            sb.append(checkMonth + "月");
        }
        if (checkDay != 0) {
            sb.append(checkDay + "日");
        }
        return sb.toString();
    }

    public static enum Status {
        NORMAL, REMIND, OVER
    }

}
