package com.anosi.asset.controller;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.model.jpa.Integral;
import com.anosi.asset.service.IntegralService;
import com.anosi.asset.util.StringUtil;
import com.querydsl.core.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * 积分controller
 *
 * @ProjectName: goaland
 * @Package: com.anosi.asset.controller
 * @Description:
 * @Author: jinyao
 * @CreateDate: 2018/1/9 16:11
 * @UpdateUser: jinyao
 * @UpdateDate: 2018/1/9 16:11
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@RestController
public class IntegralController extends BaseController<Integral>{

    private static final Logger logger = LoggerFactory.getLogger(IntegralController.class);

    @Autowired
    private IntegralService integralService;

    /***
     * 进入查看<b>我的积分</b>的页面
     *
     * @return
     */
    @RequestMapping(value = "/integral/myIntegral/view", method = RequestMethod.GET)
    public ModelAndView toViewMyIntegralManage() {
        logger.debug("view myIntegral manage");
        return new ModelAndView("integral/myIntegral");
    }

    /***
     * 根据条件查询某个Integral
     *
     * @param showType
     * @param predicate
     * @param showAttributes
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/integral/management/data/one", method = RequestMethod.GET)
    public JSONObject findIntegralManageDataOne(@QuerydslPredicate(root = Integral.class) Predicate predicate,
                                              @RequestParam(value = "showAttributes", required = false) String showAttributes) throws Exception {
        logger.info("find integral one");
        return jsonUtil.parseAttributesToJson(StringUtil.splitAttributes(showAttributes),
                integralService.findOne(predicate));
    }

    /***
     * 获取积分
     *
     * @param showType
     * @param pageable
     * @param predicate
     * @param showAttributes
     * @param rowId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/integral/management/data/{showType}", method = RequestMethod.GET)
    public JSONObject findIntegralManageData(@PathVariable ShowType showType,
                                                @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, page = 0, size = 20) Pageable pageable,
                                                @QuerydslPredicate(root = Integral.class) Predicate predicate,
                                                @RequestParam(value = "showAttributes", required = false) String showAttributes,
                                                @RequestParam(value = "rowId", required = false, defaultValue = "id") String rowId) throws Exception {
        logger.info("find integral");
        logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);

        return parseToJson(integralService.findAll(predicate, pageable), rowId, showAttributes, showType);
    }

    /***
     * 进入查看<b>积分管理</b>的页面
     *
     * @return
     */
    @RequestMapping(value = "/integral/integralMgr/view", method = RequestMethod.GET)
    public ModelAndView toViewIntegralManage() {
        logger.debug("view integralMgr manage");
        return new ModelAndView("integral/integralMgr");
    }

}
