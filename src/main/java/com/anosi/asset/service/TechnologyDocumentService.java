package com.anosi.asset.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.anosi.asset.model.elasticsearch.TechnologyDocument;

public interface TechnologyDocumentService extends BaseElasticSearchService<TechnologyDocument, String> {

	/***
	 * 创建TechnologyDocument
	 * 
	 * @param fileName
	 * @param is
	 * @param fileSize
	 * @param type
	 * @param identification
	 * @return
	 * @throws Exception
	 */
	TechnologyDocument createTechnologyDocument(String fileName, InputStream is, Long fileSize, String type,
			String identification) throws Exception;

	/**
	 * 重載
	 * 
	 * @param file
	 * @param type
	 * @param identification
	 * @return
	 * @throws Exception
	 */
	TechnologyDocument createTechnologyDocument(File file, String type, String identification) throws Exception;

	/****
	 * 为List<File>进行重载重载
	 * 
	 * @param files
	 * @param type
	 * @param identification
	 * @return
	 * @throws Exception
	 */
	List<TechnologyDocument> createTechnologyDocument(List<File> files, String type, String identification)
			throws Exception;

	/****
	 * 为MultipartFile进行重载
	 * 
	 * @param files
	 * @param type
	 * @param identification
	 * @return
	 * @throws Exception
	 */
	List<TechnologyDocument> createTechnologyDocument(MultipartFile[] multipartFiles, String type,
			String identification) throws Exception;

	/***
	 * 根据内容查找文档
	 * 
	 * @param technologyDocument
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	Page<TechnologyDocument> getHighLight(TechnologyDocument technologyDocument, Pageable pageable) throws Exception;

	/***
	 * 根据类型精确查找文档
	 * 
	 * @param type
	 * @return
	 */
	List<TechnologyDocument> findByType(String type);

	/***
	 * 更新type
	 * 
	 * @param lastType
	 * @param nowType
	 * @return
	 */
	List<TechnologyDocument> updateType(String lastType, String nowType);

}
