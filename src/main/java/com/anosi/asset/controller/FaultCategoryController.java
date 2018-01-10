package com.anosi.asset.controller;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.model.jpa.FaultCategory;
import com.anosi.asset.service.FaultCategoryService;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class FaultCategoryController extends BaseController<FaultCategory> {

    private static final Logger logger = LoggerFactory.getLogger(FaultCategoryController.class);

    @Autowired
    private FaultCategoryService faultCategoryService;


    /***
     * 获取故障分类数据
     *
     * @param showType
     * @param pageable
     * @param predicate
     * @param showAttributes
     * @param rowId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/faultCategory/management/data/{showType}", method = RequestMethod.GET)
    public JSONObject findFaultCategoryManageData(@PathVariable ShowType showType,
                                                  @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, page = 0, size = 20) Pageable pageable,
                                                  @QuerydslPredicate(root = FaultCategory.class) Predicate predicate,
                                                  @RequestParam(value = "showAttributes", required = false) String showAttributes,
                                                  @RequestParam(value = "rowId", required = false, defaultValue = "id") String rowId,
                                                  HttpServletRequest request) throws Exception {
        logger.info("find device");
        logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);

        if (StringUtils.isBlank(request.getParameter("page")) && StringUtils.isBlank(request.getParameter("size"))) {
            return new JSONObject(ImmutableMap.of("faultCategorys", faultCategoryService.findAll()));
        }
        return parseToJson(faultCategoryService.findAll(predicate, pageable), rowId, showAttributes, showType);
    }

}
