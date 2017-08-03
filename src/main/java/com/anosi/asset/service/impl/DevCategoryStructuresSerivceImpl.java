package com.anosi.asset.service.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.anosi.asset.component.I18nComponent;
import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.DevCategoryStructuresDao;
import com.anosi.asset.exception.CustomRunTimeException;
import com.anosi.asset.model.jpa.DevCategory;
import com.anosi.asset.model.jpa.DevCategoryStructures;
import com.anosi.asset.service.DevCategoryStructuresService;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

@Service("devCategoryStructuresSerivce")
@Transactional
public class DevCategoryStructuresSerivceImpl extends BaseServiceImpl<DevCategoryStructures> implements DevCategoryStructuresService {
	
	private static final Logger logger = LoggerFactory.getLogger(DevCategoryStructuresSerivceImpl.class);

	@Autowired
	private I18nComponent i18nComponent;
	@Autowired
	private DevCategoryStructuresDao devCategoryStructuresDao;
	
	@Override
	public BaseJPADao<DevCategoryStructures> getRepository() {
		return devCategoryStructuresDao;
	}

	@Override
	public void checkStructures(DevCategory devCategory, List<String> subDevCategorys) throws Exception {
		Map<String, Integer> result = checkStructures(devCategory.getMainDevCategoryStructures(), subDevCategorys);
		if (!CollectionUtils.isEmpty(result)) {
			logger.debug("checkStructures is not empty");
			throw new CustomRunTimeException(MessageFormat.format(i18nComponent.getMessage("checkStructures.template"),
					devCategory.getName(), result.toString()));
		}
	}

	@Override
	public Map<String, Integer> checkStructures(List<DevCategoryStructures> devCategoryStructures,
			List<String> subDevCategorys) {
		Multiset<String> subMultiset = HashMultiset.create();
		subMultiset.addAll(subDevCategorys);
		logger.debug("outside category:{}",subMultiset);
		
		//parallelStream并行计算，提高速度
		//分为两步，第一步过滤掉数量匹配的devcategory
		//第二步将不匹配的devcategory的name和数量之差转为map
		Map<String, Integer> result = devCategoryStructures.parallelStream().filter(s -> s.getAmount() != subMultiset.count(s.getSubDevCategory().getName()))
				.collect(Collectors.toMap(s->s.getSubDevCategory().getName(), s->subMultiset.count(s.getSubDevCategory().getName())-s.getAmount()));
		
		return result;
	}

}
