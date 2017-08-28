package com.anosi.asset.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.anosi.asset.component.I18nComponent;
import com.anosi.asset.component.SessionUtil;
import com.anosi.asset.dao.elasticsearch.TechnologyDocumentDao;
import com.anosi.asset.exception.CustomRunTimeException;
import com.anosi.asset.model.elasticsearch.TechnologyDocument;
import com.anosi.asset.model.mongo.FileMetaData;
import com.anosi.asset.service.FileMetaDataService;
import com.anosi.asset.service.SearchRecordService;
import com.anosi.asset.service.TechnologyDocumentService;
import com.anosi.asset.util.FileFetchUtil;
import com.anosi.asset.util.FileFetchUtil.Suffix;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Service("technologyDocumentService")
@Transactional
public class TechnologyDocumentServiceImpl implements TechnologyDocumentService {

	private static final Logger logger = LoggerFactory.getLogger(TechnologyDocumentServiceImpl.class);

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	@Autowired
	private TechnologyDocumentDao technologyDocumentDao;
	@Autowired
	private SearchRecordService searchRecordService;
	@Autowired
	private FileMetaDataService fileMetaDataService;
	@Autowired
	private I18nComponent i18nComponent;

	@Override
	public TechnologyDocument createTechnologyDocument(File file, String type) throws Exception {
		return createTechnologyDocument(file.getName(), new FileInputStream(file), file.length(), type);
	}

	@Override
	public TechnologyDocument createTechnologyDocument(String fileName, InputStream is, Long fileSize, String type)
			throws Exception {
		byte[] byteArray = IOUtils.toByteArray(is);
		String content;
		try {
			// 如果不在枚举类中直接报错
			content = FileFetchUtil.fetchContent(
					Suffix.valueOf(fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase()),
					new ByteArrayInputStream(byteArray));
		} catch (IllegalArgumentException e) {
			throw new CustomRunTimeException(MessageFormat.format(i18nComponent.getMessage("exception.unSupportSuffix"),
					fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase()));
		}
		return saveTechnologyDocument(fileName, new ByteArrayInputStream(byteArray), fileSize, content, type);
	}

	private TechnologyDocument saveTechnologyDocument(String fileName, InputStream is, Long fileSize, String content,
			String type) throws Exception {
		logger.debug("saveTechnologyDocument,fileName:{},fileSize:{}", fileName, fileSize);
		FileMetaData fileMetaData = fileMetaDataService.saveFile(type, fileName, is, fileSize);
		TechnologyDocument td = new TechnologyDocument();
		td.setContent(content);
		td.setFileId(fileMetaData.getObjectId().toString());
		td.setType(type);
		td.setFileName(fileName);
		td.setUploader(fileMetaData.getUploader());
		td.setUploadTime(fileMetaData.getUploadTime());
		return technologyDocumentDao.save(td);
	}

	@Override
	public List<TechnologyDocument> createTechnologyDocument(List<File> files, String type) throws Exception {
		List<String> contents = FileFetchUtil.fetchContent(files);
		List<TechnologyDocument> documents = new ArrayList<>();
		for (int i = 0; i < files.size(); i++) {
			File file = files.get(i);
			documents.add(saveTechnologyDocument(file.getName(), new FileInputStream(file), file.length(),
					contents.get(i), type));
		}
		return documents;
	}

	@Override
	public List<TechnologyDocument> createTechnologyDocument(MultipartFile[] multipartFiles, String type)
			throws Exception {
		List<String> suffixs = new ArrayList<>();
		for (MultipartFile multipartFile : multipartFiles) {
			String it = multipartFile.getOriginalFilename();
			suffixs.add(it.substring(it.lastIndexOf(".") + 1));
		}
		// 先进行检查,提高效率,否则执行到一半发现问题就太浪费时间了
		FileFetchUtil.checkSuffixs(suffixs);

		List<TechnologyDocument> documents = new ArrayList<>();
		for (MultipartFile multipartFile : multipartFiles) {
			documents.add(createTechnologyDocument(multipartFile.getOriginalFilename(), multipartFile.getInputStream(),
					multipartFile.getSize(), type));
		}
		return documents;
	}

	@Override
	public Page<TechnologyDocument> getHighLight(TechnologyDocument technologyDocument, Pageable pageable)
			throws Exception {
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withPageable(pageable);
		return technologyDocumentDao.getHighLight(elasticsearchTemplate,
				parseToQuery(technologyDocument, queryBuilder));
	}

	/***
	 * 根据technologyDocument的各个属性,parse成query
	 * 
	 * @param technologyDocument
	 * @param queryBuilder
	 * @return
	 */
	private NativeSearchQueryBuilder parseToQuery(TechnologyDocument technologyDocument,
			NativeSearchQueryBuilder queryBuilder) {
		String searchContent = technologyDocument.getSearchContent();
		Date lowerLimit = technologyDocument.getLowerLimit();
		Date upperLimit = technologyDocument.getUpperLimit();
		String uploader = technologyDocument.getUploader();
		String type = technologyDocument.getType();

		// 如果查询内容不为空，那么默认查询内容或者标题
		if (StringUtils.isNoneBlank(searchContent)) {
			// 判断是否需要插入中央词库,新开一个线程,不在主线程上消耗时间影响用户体验
			new Thread(() -> searchRecordService.insertInto(searchContent,
					"search_" + SessionUtil.getCurrentUser().getLoginId())).start();
			queryBuilder.withQuery(boolQuery().should(matchQuery("content", searchContent))
					.should(matchQuery("fileName", searchContent)));
		}

		// 查询文档类型
		if (StringUtils.isNoneBlank(type)) {
			queryBuilder.withFilter(boolQuery().filter(termQuery("type", type)));
		}

		// 查询文档上传人
		if (StringUtils.isNoneBlank(uploader)) {
			queryBuilder.withFilter(boolQuery().filter(termQuery("uploader", uploader)));
		}

		// 查询文档上传时间的下限
		if (lowerLimit != null) {
			queryBuilder.withFilter(boolQuery().filter(rangeQuery("uploadTime").from(lowerLimit)));
		}

		// 查询文档上传时间的上限
		if (upperLimit != null) {
			queryBuilder.withFilter(boolQuery().filter(rangeQuery("uploadTime").to(upperLimit)));
		}

		return queryBuilder;
	}

	@Override
	public List<TechnologyDocument> findByType(String type) {
		return technologyDocumentDao.findByTypeEquals(type);
	}

	@Override
	public List<TechnologyDocument> updateType(String lastType, String nowType) {
		List<TechnologyDocument> technologyDocuments = technologyDocumentDao.findByTypeEquals(lastType);
		if (!CollectionUtils.isEmpty(technologyDocuments)) {
			for (TechnologyDocument technologyDocument : technologyDocuments) {
				technologyDocument.setType(nowType);
			}
			technologyDocumentDao.save(technologyDocuments);
		}
		return technologyDocuments;
	}

}
