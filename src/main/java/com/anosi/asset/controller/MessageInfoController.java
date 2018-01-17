package com.anosi.asset.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.model.jpa.MessageInfo;
import com.anosi.asset.service.MessageInfoService;
import com.anosi.asset.util.StringUtil;
import com.querydsl.core.types.Predicate;

import java.util.Date;

@RestController
public class MessageInfoController extends BaseController<MessageInfo> {

    private static final Logger logger = LoggerFactory.getLogger(MessageInfoController.class);

    @Autowired
    private MessageInfoService messageInfoService;

    /***
     * 查看某条信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/messageInfo/{id}/view", method = RequestMethod.GET)
    public ModelAndView toViewFileMetaData(@PathVariable Long id) {
        logger.debug("view messageInfo");
        MessageInfo messageInfo = messageInfoService.getOne(id);
        // 设置阅读时间
        if (!messageInfo.isRead()) {
            messageInfo.setReadTime(new Date());
            messageInfo.setMessageStatus(MessageInfo.MessageStatus.READ);
            messageInfoService.save(messageInfo);
        }
        return new ModelAndView("messageInfo/viewMessageInfo", "id", id);
    }

    /***
     * 查询某条具体的messageInfo
     *
     * @param predicate
     * @param showAttributes
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/messageInfo/management/data/one", method = RequestMethod.GET)
    public JSONObject findMessageInfoManageDataOne(@QuerydslPredicate(root = MessageInfo.class) Predicate predicate,
                                                   @RequestParam(value = "showAttributes", required = false) String showAttributes) throws Exception {
        logger.info("find messageInfo one");
        return jsonUtil.parseAttributesToJson(StringUtil.splitAttributes(showAttributes),
                messageInfoService.findOne(predicate));
    }

    /***
     * 获取messageInfo数据
     *
     * @param pageable
     * @param predicate
     * @param showAttributes
     * @param rowId
     * @param companyCode
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/messageInfo/management/data/{showType}", method = RequestMethod.GET)
    public JSONObject findMessageInfoManageData(@PathVariable ShowType showType,
                                                @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, page = 0, size = 20) Pageable pageable,
                                                @QuerydslPredicate(root = MessageInfo.class) Predicate predicate,
                                                @RequestParam(value = "showAttributes", required = false) String showAttributes,
                                                @RequestParam(value = "rowId", required = false, defaultValue = "id") String rowId) throws Exception {
        logger.info("find messageInfo");
        logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);

        return parseToJson(messageInfoService.findAll(predicate, pageable), rowId, showAttributes, showType);
    }

}
