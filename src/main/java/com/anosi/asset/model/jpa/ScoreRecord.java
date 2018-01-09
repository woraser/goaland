package com.anosi.asset.model.jpa;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
public class ScoreRecord extends BaseEntity{

    private Integral integral;

    private Operate operate;

    private int score;

    private String reason;

    public static enum Operate{
        PLUS,MINUS
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
}
