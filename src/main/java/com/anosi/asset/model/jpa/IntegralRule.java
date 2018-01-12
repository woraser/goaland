package com.anosi.asset.model.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 积分规则
 *
 * @ProjectName: goaland
 * @Package: com.anosi.asset.model.jpa
 * @Description:
 * @Author: jinyao
 * @CreateDate: 2018/1/11 22:18
 * @UpdateUser: jinyao
 * @UpdateDate: 2018/1/11 22:18
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@Entity
@Table(name = "integralRule")
public class IntegralRule extends BaseEntity {

    private String key;

    private String express;

    private String note;

    private RuleContext ruleContext;

    @Column(name = "identifying", unique = true, nullable = false)
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public RuleContext getRuleContext() {
        return ruleContext;
    }

    public void setRuleContext(RuleContext ruleContext) {
        this.ruleContext = ruleContext;
    }

    /***
     * 规则上下文
     */
    public static enum RuleContext {
        PROCESS
    }

}
