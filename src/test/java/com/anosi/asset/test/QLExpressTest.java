package com.anosi.asset.test;

import com.anosi.asset.util.DateFormatUtil;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;
import org.junit.Test;

/**
 * 测试规则引擎
 *
 * @ProjectName: goaland
 * @Package: com.anosi.asset.test
 * @Description:
 * @Author: jinyao
 * @CreateDate: 2018/1/11 13:59
 * @UpdateUser: jinyao
 * @UpdateDate: 2018/1/11 13:59
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
public class QLExpressTest {

    @Test
    public void test1() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        context.put("a", 1);
        context.put("b", 2);
        context.put("c", 3);
        String express = "a+b*c";
        Object r = runner.execute(express, context, null, true, false);
        System.out.println(r);
    }

    @Test
    public void test2() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        runner.addOperatorWithAlias("如果", "if", null);
        runner.addOperatorWithAlias("则", "then", null);
        runner.addOperatorWithAlias("否则", "else", null);

        String exp = "如果 1==2 则 false 否则 true";
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        runner.execute(exp, context, null, false, false, null);
    }

    @Test
    public void test3() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        runner.addMacro("计算平均成绩", "(语文+数学+英语)/3.0");
        runner.addMacro("是否优秀", "计算平均成绩>90");
        IExpressContext<String, Object> context =new DefaultContext<String, Object>();
        context.put("语文", 88);
        context.put("数学", 99);
        context.put("英语", 95);
        Object result = runner.execute("是否优秀", context, null, false, false);
        System.out.println(result);
    }

    @Test
    public void test4() throws Exception{
        ExpressRunner runner = new ExpressRunner();
        runner.addFunctionOfClassMethod("取绝对值", Math.class.getName(), "abs",
                new String[] { "double" }, null);
        String exp = "取绝对值(-100)";
        Object result = runner.execute(exp, null, null, false, false);
        System.out.println(result);
    }

    @Test
    public void test5() throws Exception{
        // 如果 任务办理时间 超过 5 天 则 增加 100 积分 否则 扣减 50 积分
        ExpressRunner runner = new ExpressRunner();
        IExpressContext<String, Object> context =new DefaultContext<String, Object>();
        context.put("任务创建时间", DateFormatUtil.getDateByParttern("2017-12-22 00:00:00"));
        context.put("任务完成时间",DateFormatUtil.getDateByParttern("2018-1-11 00:00:00"));

        runner.addOperatorWithAlias("如果", "if", null);
        runner.addOperatorWithAlias("则", "then", null);
        runner.addOperatorWithAlias("否则", "else", null);
        runner.addOperatorWithAlias("超过", ">", null);
        runner.addOperatorWithAlias("少于", "<", null);
        runner.addFunctionOfClassMethod("计算相差小时", DateFormatUtil.class.getName(), "daysBetween",
                new String[] { "java.util.Date","java.util.Date" }, null);
        runner.addFunctionOfClassMethod("积分增加", QLExpressTest.class.getName(), "plusIntegral",
                new String[] { "int" }, null);
        runner.addMacro("任务办理小时数", "计算相差小时(任务创建时间,任务完成时间)");

        String exp = "如果 任务办理小时数 超过 5 则 积分增加 100";
        Object result = runner.execute(exp, context, null, false, false, null);
        System.out.println(result);
    }

    public void plusIntegral(int score){
        System.out.println(score);
    }

}
