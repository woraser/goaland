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

	/***
	 * 获取各种类设备数量的统计结果，设备种类名称可在前台转为i18n
	 * 
	 * @return
	 */
	JSONArray countByDevCategoryI18n();

}
