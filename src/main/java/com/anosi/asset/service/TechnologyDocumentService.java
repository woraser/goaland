package com.anosi.asset.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.anosi.asset.model.elasticsearch.TechnologyDocument;

public interface TechnologyDocumentService {

	/***
	 * 创建TechnologyDocument
	 * 
	 * @param fileName
	 * @param is
	 * @param fileSize
	 * @return
	 * @throws Exception
	 */
	TechnologyDocument createTechnologyDocument(String fileName, InputStream is, Long fileSize) throws Exception;

	/**
	 * 重載
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	TechnologyDocument createTechnologyDocument(File file) throws Exception;

	/****
	 * 为List<File>进行重载重载
	 * 
	 * @param files
	 * @return
	 * @throws Exception
	 */
	List<TechnologyDocument> createTechnologyDocument(List<File> files) throws Exception;

	/****
	 * 为MultipartFile进行重载
	 * 
	 * @param files
	 * @return
	 * @throws Exception
	 */
	List<TechnologyDocument> createTechnologyDocument(MultipartFile[] multipartFiles) throws Exception;

	/***
	 * 根据内容查找文档
	 * 
	 * @param content
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	Page<TechnologyDocument> getHighLightContent(String content, Pageable pageable) throws Exception;

}
