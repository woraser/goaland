package com.anosi.asset.service;

import com.alibaba.fastjson.JSONArray;
import com.anosi.asset.model.jpa.DevCategory;

public interface DevCategoryService extends BaseJPAService<DevCategory>{

	/***
	 * 根据设备种类统计数量
	 * 
	 * @return
	 */
	JSONArray countByDevCategory();

}
