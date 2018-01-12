package com.anosi.asset.component;

import com.anosi.asset.exception.CustomRunTimeException;
import com.anosi.asset.model.jpa.IntegralRule;
import com.anosi.asset.model.jpa.ScoreRecord;
import com.anosi.asset.service.ScoreRecordServcie;
import com.anosi.asset.util.DateFormatUtil;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 规则引擎组件
 *
 * @ProjectName: goaland
 * @Package: com.anosi.asset.component
 * @Description:
 * @Author: jinyao
 * @CreateDate: 2018/1/12 8:13
 * @UpdateUser: jinyao
 * @UpdateDate: 2018/1/12 8:13
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@Component
public class QLExpressComponent {

    private static final Logger logger = LoggerFactory.getLogger(QLExpressComponent.class);

    @Autowired
    private ScoreRecordServcie scoreRecordServcie;
    @Autowired
    private SessionComponent sessionComponent;

    /***
     * 执行规则
     * @param params
     * @param integralRule
     */
    public void excuteExpress(Map<String, Object> params, IntegralRule integralRule) {
        try {
            ExpressRunner runner = new ExpressRunner();
            IExpressContext<String, Object> context = new DefaultContext<String, Object>();

            setCommmon(runner);

            String exp = integralRule.getExpress();

            switch (integralRule.getRuleContext()) {
                case PROCESS:
                    context.put("任务创建时间", params.get("startTime"));
                    context.put("任务完成时间", params.get("endTime"));
                    runner.addFunctionOfClassMethod("计算相差小时", DateFormatUtil.class.getName(), "daysBetweenHours",
                            new String[]{"java.util.Date", "java.util.Date"}, null);
                    runner.addFunctionOfClassMethod("积分增加", this.getClass().getName(), "integralPlus",
                            new String[]{"int"}, null);
                    runner.addFunctionOfClassMethod("积分扣减", this.getClass().getName(), "integralPlus",
                            new String[]{"int"}, null);
                    runner.addMacro("任务办理小时数", "计算相差小时(任务创建时间,任务完成时间)");
                    break;
            }
            Object score = runner.execute(exp, context, null, false, false, null);
            if (score instanceof Integer) {
                int scoreInt = (int) score;
                if (scoreInt >= 0) {
                    scoreRecordServcie.createScoreRecord(scoreInt, ScoreRecord.Operate.PLUS, integralRule.getNote(), sessionComponent.getCurrentUser().getIntegral());
                } else {
                    scoreRecordServcie.createScoreRecord(-scoreInt, ScoreRecord.Operate.MINUS, integralRule.getNote(), sessionComponent.getCurrentUser().getIntegral());
                }
            }
        } catch (Exception e) {
            throw new CustomRunTimeException(e.getMessage());
        }
    }

    public int integralPlus(int score) {
        return score;
    }

    public int integralMinus(int score) {
        return -score;
    }

    /***
     * 测试规则
     * @param ruleContext
     * @param exp 表达式
     */
    public void testExcuteExpress(IntegralRule.RuleContext ruleContext, String exp) {
        try {
            ExpressRunner runner = new ExpressRunner();
            IExpressContext<String, Object> context = new DefaultContext<String, Object>();

            setCommmon(runner);
            switch (ruleContext) {
                case PROCESS:
                    context.put("任务创建时间", DateFormatUtil.getDateByParttern("2017-12-22 00:00:00"));
                    context.put("任务完成时间", DateFormatUtil.getDateByParttern("2018-1-11 00:00:00"));
                    runner.addFunctionOfClassMethod("计算相差小时", DateFormatUtil.class.getName(), "daysBetweenHours",
                            new String[]{"java.util.Date", "java.util.Date"}, null);
                    runner.addFunctionOfClassMethod("积分增加", this.getClass().getName(), "integralPlusTest",
                            new String[]{"int"}, null);
                    runner.addFunctionOfClassMethod("积分扣减", this.getClass().getName(), "integralPlusTest",
                            new String[]{"int"}, null);
                    runner.addMacro("任务办理小时数", "计算相差小时(任务创建时间,任务完成时间)");
                    break;
            }
            runner.execute(exp, context, null, false, false, null);
        } catch (Exception e) {
            throw new CustomRunTimeException(e.getMessage());
        }
    }

    private void setCommmon(ExpressRunner runner) throws Exception {
        runner.addOperatorWithAlias("如果", "if", null);
        runner.addOperatorWithAlias("则", "then", null);
        runner.addOperatorWithAlias("否则", "else", null);
        runner.addOperatorWithAlias("超过", ">", null);
        runner.addOperatorWithAlias("少于", "<", null);
        runner.addOperatorWithAlias("并且", "&&", null);
        runner.addOperatorWithAlias("或者", "||", null);
    }

    public void integralPlusTest(int score) {
        logger.info("test plus integral:{}", score);
    }

    public void integralMinusTest(int score) {
        logger.info("test minux integral:{}", score);
    }

}
