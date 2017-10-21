package com.anosi.asset.service;

import java.util.List;
import java.util.Map;

import com.anosi.asset.model.jpa.DevCategory;
import com.anosi.asset.model.jpa.DevCategoryStructures;

public interface DevCategoryStructuresService extends BaseJPAService<DevCategoryStructures>{
	
	/***
	 * 比对导入数据和数据库数据结构是否相同
	 * @param devCategory
	 * @param subDevCategorys	导入数据
	 * @throws Exception
	 */
	public Map<String,Integer> checkStructures(DevCategory devCategory,List<String> subDevCategorys) throws Exception;
	
	/***
	 * 比对导入数据和数据库数据结构是否相同
	 * @param devCategoryStructures
	 * @param subDevCategorys
	 * @return
	 */
	public Map<String,Integer> checkStructures(List<DevCategoryStructures> devCategoryStructures,List<String> subDevCategorys);
	
}
