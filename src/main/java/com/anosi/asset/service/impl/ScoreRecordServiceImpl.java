package com.anosi.asset.service.impl;

import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.ScoreRecordDao;
import com.anosi.asset.model.jpa.Integral;
import com.anosi.asset.model.jpa.ScoreRecord;
import com.anosi.asset.service.IntegralService;
import com.anosi.asset.service.ScoreRecordServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 积分记录service实现类
 *
 * @ProjectName: goaland
 * @Package: com.anosi.asset.service.impl
 * @Description:
 * @Author: jinyao
 * @CreateDate: 2018/1/9 16:07
 * @UpdateUser: jinyao
 * @UpdateDate: 2018/1/9 16:07
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@Service("scoreRecordService")
public class ScoreRecordServiceImpl extends BaseJPAServiceImpl<ScoreRecord> implements ScoreRecordServcie {

    @Autowired
    private ScoreRecordDao scoreRecordDao;
    @Autowired
    private IntegralService integralService;

    @Override
    public BaseJPADao<ScoreRecord> getRepository() {
        return scoreRecordDao;
    }

    @Transactional
    @Override
    public void createScoreRecord(int score, ScoreRecord.Operate operate, String reason, Long integralId) {
        Integral integral;
        if (integralId == null) {
            integral = integralService.getOne(integralId);
        } else {
            integral = sessionComponent.getCurrentUser().getIntegral();
        }
        this.createScoreRecord(score, operate, reason, integral);
    }

    @Transactional
    @Override
    public void createScoreRecord(int score, ScoreRecord.Operate operate, String reason, Integral integral) {
        ScoreRecord scoreRecord = new ScoreRecord(integral, operate, score, reason);
        scoreRecordDao.save(scoreRecord);
    }
}
