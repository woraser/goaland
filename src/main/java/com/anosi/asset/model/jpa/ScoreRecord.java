package com.anosi.asset.model.jpa;

import javax.persistence.*;
import java.util.Date;

/**
 * 积分变更记录
 *
 * @ProjectName: goaland
 * @Package: com.anosi.asset.model.jpa
 * @Description:
 * @Author: jinyao
 * @CreateDate: 2018/1/9 15:07
 * @UpdateUser: jinyao
 * @UpdateDate: 2018/1/9 15:07
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@Entity
@Table(name = "scoreRecord")
public class ScoreRecord extends BaseEntity {

    public ScoreRecord() {
        super();
    }

    public ScoreRecord(Integral integral, Operate operate, int score, String reason) {
        this.integral = integral;
        this.operate = operate;
        this.score = score;
        this.reason = reason;
        calculateRemainScore();
    }

    private Integral integral;

    private Operate operate;

    private int score;// 本次操作的积分数

    private int remainScore;// 本次剩余积分数

    private String reason;

    private Date operateTime = new Date();

    public static enum Operate {
        PLUS, MINUS
    }

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Integral.class)
    public Integral getIntegral() {
        return integral;
    }

    public void setIntegral(Integral integral) {
        this.integral = integral;
    }

    public Operate getOperate() {
        return operate;
    }

    public void setOperate(Operate operate) {
        this.operate = operate;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getRemainScore() {
        return remainScore;
    }

    public void setRemainScore(int remainScore) {
        this.remainScore = remainScore;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    /**
     * 计算剩余分数
     */
    @Transient
    private void calculateRemainScore() {
        int total = integral.getTotal();
        switch (operate) {
            case PLUS:
                remainScore = total + score;
                break;
            case MINUS:
                remainScore = total - score;
                break;
        }
        integral.setTotal(remainScore);
    }

}
