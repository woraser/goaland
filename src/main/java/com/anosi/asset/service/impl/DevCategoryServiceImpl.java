package com.anosi.asset.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.dao.jpa.DevCategoryDao;
import com.anosi.asset.model.jpa.DevCategory;
import com.anosi.asset.model.jpa.DevCategory.CategoryType;
import com.anosi.asset.service.DevCategoryService;

@Service("devCategoryService")
@Transactional
public class DevCategoryServiceImpl extends BaseJPAServiceImpl<DevCategory> implements DevCategoryService {

	@Autowired
	private DevCategoryDao devCategoryDao;

	@Override
	public BaseJPADao<DevCategory> getRepository() {
		return devCategoryDao;
	}

	@Override
	public JSONArray countByDevCategory() {
		List<Object[]> countByDevCategory = devCategoryDao.countByDevCategory();

		JSONArray jsonArray = new JSONArray();
		for (Object[] counts : countByDevCategory) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("chinese", CategoryType.values()[((Integer) counts[0])].getChinese());
			jsonObject.put("english", CategoryType.values()[((Integer) counts[0])].getEnglish());
			jsonObject.put("count", counts[1]);
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	@Override
	public JSONArray countByDevCategoryI18n() {
		List<Object[]> countByDevCategory = devCategoryDao.countByDevCategory();

		JSONArray jsonArray = new JSONArray();
		for (Object[] counts : countByDevCategory) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", CategoryType.values()[((Integer) counts[0])]);
			jsonObject.put("count", counts[1]);
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

}
