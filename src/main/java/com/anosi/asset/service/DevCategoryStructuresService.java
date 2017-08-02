package com.anosi.asset.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anosi.asset.model.jpa.DevCategory;
import com.anosi.asset.model.jpa.DevCategoryStructures;
import com.querydsl.core.types.Predicate;

public interface DevCategoryStructuresService {
	
	public DevCategoryStructures save(DevCategoryStructures devCategoryStructures);
	
	public Page<DevCategoryStructures> findAll(Predicate predicate, Pageable pageable);
	
	public Iterable<DevCategoryStructures> save(Iterable<DevCategoryStructures> entities);

	/***
	 * 比对导入数据和数据库数据结构是否相同
	 * @param devCategory
	 * @param subDevCategorys	导入数据
	 * @throws Exception
	 */
	public void checkStructures(DevCategory devCategory,List<String> subDevCategorys) throws Exception;
	
	/***
	 * 比对导入数据和数据库数据结构是否相同
	 * @param devCategoryStructures
	 * @param subDevCategorys
	 * @return
	 */
	public Map<String,Integer> checkStructures(List<DevCategoryStructures> devCategoryStructures,List<String> subDevCategorys);
	
}
