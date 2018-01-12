package com.anosi.asset.controller;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.component.QLExpressComponent;
import com.anosi.asset.model.jpa.Device;
import com.anosi.asset.model.jpa.IntegralRule;
import com.anosi.asset.service.CustomerServcieProcessService;
import com.anosi.asset.service.IntegralRuleService;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * 积分规则controller
 *
 * @ProjectName: goaland
 * @Package: com.anosi.asset.controller
 * @Description:
 * @Author: jinyao
 * @CreateDate: 2018/1/11 22:32
 * @UpdateUser: jinyao
 * @UpdateDate: 2018/1/11 22:32
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@RestController
public class IntegralRuleController extends BaseController<IntegralRule> {

    private static final Logger logger = LoggerFactory.getLogger(IntegralRuleController.class);

    @Autowired
    private QLExpressComponent qlExpressComponent;
    @Autowired
    private IntegralRuleService integralRuleService;
    @Autowired
    private CustomerServcieProcessService customerServcieProcessService;

    /***
     * 进入查看<b>所有规则</b>的页面
     *
     * @return
     */
    @RequestMapping(value = "/integralRule/management/view", method = RequestMethod.GET)
    public ModelAndView toViewIntegralRuleManage() {
        logger.debug("view integralRule manage");
        return new ModelAndView("integralRule/integralRuleMgr");
    }

    /***
     * 获取规则数据
     *
     * @param showType
     * @param pageable
     * @param predicate
     * @param showAttributes
     * @param rowId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/integralRule/management/data/{showType}", method = RequestMethod.GET)
    public JSONObject findIntegralRuleManageData(@PathVariable ShowType showType,
                                                 @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, page = 0, size = 20) Pageable pageable,
                                                 @QuerydslPredicate(root = Device.class) Predicate predicate,
                                                 @RequestParam(value = "showAttributes", required = false) String showAttributes,
                                                 @RequestParam(value = "rowId", required = false, defaultValue = "id") String rowId) throws Exception {
        logger.info("find integralRule");
        logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);

        return parseToJson(integralRuleService.findAll(predicate, pageable), rowId, showAttributes, showType);
    }

    /***
     * 进入save/update device的页面
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/integralRule/save", method = RequestMethod.GET)
    public ModelAndView toSaveDevicePage(@RequestParam(value = "id", required = false) Long id) throws Exception {
        IntegralRule integralRule;
        if (id == null) {
            integralRule = new IntegralRule();
        } else {
            integralRule = integralRuleService.getOne(id);
        }
        return new ModelAndView("integralRule/save").addObject("integralRule", integralRule)
                .addObject("ruleContexts", IntegralRule.RuleContext.values())
                .addObject("keys", customerServcieProcessService.getTaskDefinitionKeys());
    }

    /****
     * 在执行update前，先获取持久化的对象
     *
     * @param id
     * @param model
     *
     */
    @ModelAttribute
    public void getDevice(@RequestParam(value = "integralRuleId", required = false) Long id, Model model) {
        if (id != null) {
            model.addAttribute("integralRule", integralRuleService.getOne(id));
        }
    }

    /**
     * save or update integralRule
     *
     * @param integralRule
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/integralRule/save", method = RequestMethod.POST)
    public JSONObject save(@ModelAttribute("integralRule") IntegralRule integralRule) throws Exception {
        logger.debug("saveOrUpdate integralRule");
        integralRuleService.save(integralRule);
        return new JSONObject(ImmutableMap.of("result", "success"));
    }

    /***
     * 测试表达式
     * @param ruleContext
     * @param exp
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/integralRule/test", method = RequestMethod.GET)
    public JSONObject testExpress(@RequestParam(value = "ruleContext") IntegralRule.RuleContext ruleContext,
                                  @RequestParam(value = "exp") String exp) throws Exception {
        qlExpressComponent.testExcuteExpress(ruleContext, exp);
        return new JSONObject(ImmutableMap.of("result", "success"));
    }

    /**
     * 按照某些属性判断是否存在
     *
     * @param predicate
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/integralRule/checkExist", method = RequestMethod.GET)
    public JSONObject checkExist(@QuerydslPredicate(root = IntegralRule.class) Predicate predicate) throws Exception {
        return new JSONObject(ImmutableMap.of("result", integralRuleService.exists(predicate)));
    }

}
