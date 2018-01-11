package com.anosi.asset.service;

import com.anosi.asset.model.jpa.Integral;
import com.anosi.asset.model.jpa.ScoreRecord;

/**
 * 积分记录service
 *
 * @ProjectName: goaland
 * @Package: com.anosi.asset.service
 * @Description:
 * @Author: jinyao
 * @CreateDate: 2018/1/9 15:48
 * @UpdateUser: jinyao
 * @UpdateDate: 2018/1/9 15:48
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
public interface ScoreRecordServcie extends BaseJPAService<ScoreRecord>{

    /***
     * 创建积分记录
     * @param score
     * @param operate
     * @param reason
     * @param integralId
     */
    void createScoreRecord(int score, ScoreRecord.Operate operate,String reason, Long integralId);

    void createScoreRecord(int score, ScoreRecord.Operate operate, String reason, Integral integral);
}
