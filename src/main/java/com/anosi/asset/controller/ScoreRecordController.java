package com.anosi.asset.controller;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.model.jpa.ScoreRecord;
import com.anosi.asset.service.ScoreRecordServcie;
import com.google.common.collect.ImmutableMap;
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
 * 积分记录controller
 *
 * @ProjectName: goaland
 * @Package: com.anosi.asset.controller
 * @Description:
 * @Author: jinyao
 * @CreateDate: 2018/1/10 9:01
 * @UpdateUser: jinyao
 * @UpdateDate: 2018/1/10 9:01
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@RestController
public class ScoreRecordController extends BaseController<ScoreRecord> {

    private static final Logger logger = LoggerFactory.getLogger(ScoreRecordController.class);

    @Autowired
    private ScoreRecordServcie scoreRecordServcie;

    /***
     * 获取积分记录
     *
     * @param showType
     * @param pageable
     * @param predicate
     * @param showAttributes
     * @param rowId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/scoreRecord/management/data/{showType}", method = RequestMethod.GET)
    public JSONObject findScoreRecordManageData(@PathVariable ShowType showType,
                                                @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, page = 0, size = 20)Pageable pageable,
                                                @QuerydslPredicate(root = ScoreRecord.class) Predicate predicate,
                                                @RequestParam(value = "showAttributes", required = false) String showAttributes,
                                                @RequestParam(value = "rowId", required = false, defaultValue = "id") String rowId) throws Exception {
        logger.info("find scoreRecord");
        logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);

        return parseToJson(scoreRecordServcie.findAll(predicate, pageable), rowId, showAttributes, showType);
    }

    /***
     * 创建积分记录
     * @param score
     * @param operate
     * @param reason
     * @param integralId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/scoreRecord/save", method = RequestMethod.POST)
    public JSONObject createScoreRecord(@RequestParam(value = "score") int score, @RequestParam(value = "operate") ScoreRecord.Operate operate,
                                        @RequestParam(value = "reason") String reason, @RequestParam(value = "integralId", required = false) Long integralId) throws Exception {
        scoreRecordServcie.createScoreRecord(score, operate, reason, integralId);
        return new JSONObject(ImmutableMap.of("result", "success"));
    }

    /***
     * 进入修改积分的页面
     * @param integralId
     * @return
     */
    @RequestMapping(value = "/scoreRecord/save", method = RequestMethod.GET)
    public ModelAndView saveRecord(@RequestParam(value = "integralId") Long integralId) {
        return new ModelAndView("scoreRecord/save").addObject("integralId", integralId)
                .addObject("scoreRecord", new ScoreRecord()).addObject("operates", ScoreRecord.Operate.values());
    }

}
