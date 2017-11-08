package com.anosi.asset.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.anosi.asset.model.elasticsearch.SearchRecord;

public interface SearchRecordService extends BaseElasticSearchService<SearchRecord, String>{

	/***
	 * autocomplete
	 * 在词库中根据用户输入的字符查找
	 * @param searchContent
	 * @param pageable
	 * @return
	 */
	public List<SearchRecord> findBySearchContent(String searchContent,Pageable pageable);
	
	/***
	 * 查询个人词库
	 * @param searchContent
	 * @param pageable
	 * @return
	 */
	public List<SearchRecord> findLocal(String searchContent,String key,Pageable pageable);
	
	/***
	 * 查询中央词库
	 * @param searchContent
	 * @param pageable
	 * @return
	 */
	public List<SearchRecord> findCenter(String searchContent,Pageable pageable);
	
	/***
	 * 判断是否插入词库
	 * @param searchContent
	 * @param account
	 */
	public void insertInto(String searchContent,String account);

	/***
	 * 插入本地词库
	 * @param searchContent
	 * @param account
	 * @return 返回true表示需要插入中央词库
	 */
	public boolean insetIntoLocal(String searchContent, String account);

	/***
	 * 插入中央词库
	 * @param searchContent
	 */
	public void insertIntoCenter(String searchContent);

	/***
	 * 清空个人搜索记录
	 * 
	 * @param account
	 */
	void clearLocal(String account);
	
}
