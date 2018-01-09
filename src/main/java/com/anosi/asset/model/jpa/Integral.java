package com.anosi.asset.model.jpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 积分
 *
 * @ProjectName: goaland
 * @Package: com.anosi.asset.model.jpa
 * @Description:
 * @Author: jinyao
 * @CreateDate: 2018/1/9 15:01
 * @UpdateUser: jinyao
 * @UpdateDate: 2018/1/9 15:01
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@Entity
@Table(name = "integral")
public class Integral extends BaseEntity{

    private int total = 0;

    private Account account;

    private List<ScoreRecord> scoreRecords = new ArrayList<>();

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @OneToOne(fetch = FetchType.LAZY)
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "integral", targetEntity = ScoreRecord.class)
    public List<ScoreRecord> getScoreRecords() {
        return scoreRecords;
    }

    public void setScoreRecords(List<ScoreRecord> scoreRecords) {
        this.scoreRecords = scoreRecords;
    }
}
