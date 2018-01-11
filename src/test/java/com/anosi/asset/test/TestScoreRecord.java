package com.anosi.asset.test;

import com.anosi.asset.GoalandApplication;
import com.anosi.asset.model.jpa.ScoreRecord;
import com.anosi.asset.service.IntegralService;
import com.anosi.asset.service.ScoreRecordServcie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * 测试积分
 *
 * @ProjectName: goaland
 * @Package: com.anosi.asset.test
 * @Description:
 * @Author: jinyao
 * @CreateDate: 2018/1/10 16:04
 * @UpdateUser: jinyao
 * @UpdateDate: 2018/1/10 16:04
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GoalandApplication.class)
@Transactional
public class TestScoreRecord {

    @Autowired
    private ScoreRecordServcie scoreRecordServcie;
    @Autowired
    private IntegralService integralService;

    @Test
    @Rollback(false)
    public void initRecord(){
        scoreRecordServcie.save( new ScoreRecord(integralService.getOne((long) 1), ScoreRecord.Operate.PLUS, 300, "测试增加积分"));
        scoreRecordServcie.save(new ScoreRecord(integralService.getOne((long) 1), ScoreRecord.Operate.MINUS, 100, "测试扣减积分"));
    }



}
