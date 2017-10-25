package com.anosi.asset.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anosi.asset.model.elasticsearch.BaseContent;

public interface BaseContentService<T extends BaseContent, ID extends Serializable, OriginalBean> extends BaseElasticSearchService<T, ID>{

	/***
	 * 根据内容模糊查询
	 * 
	 * @param content
	 * @return
	 */
	public List<T> findByContent(String content);
	
	/***
	 * 重载
	 * 
	 * @param content
	 * @param pageable
	 * @return
	 */
	public Page<T> findByContent(String content, Pageable pageable);
	
	/***
	 * 获取o中标记的内容
	 * 
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public String convertContent(OriginalBean o) throws Exception;
	
	/***
	 * 保存
	 * 
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public T saveContent(OriginalBean o) throws Exception;
	
	/***
	 * 批量保存
	 * 
	 * @param obs
	 * @return
	 * @throws Exception
	 */
	public <S extends OriginalBean> Iterable<T> saveContent(Iterable<S> obs) throws Exception;

	/***
	 * 用来设置一些通用的内容
	 * 
	 * @param t
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public T setCommonContent(T t, OriginalBean o) throws Exception;
	
}
