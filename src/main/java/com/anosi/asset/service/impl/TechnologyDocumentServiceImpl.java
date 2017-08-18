package com.anosi.asset.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

	private static final String identification = "technologyDocument";

	@Override
	public TechnologyDocument createTechnologyDocument(File file) throws Exception {
		return createTechnologyDocument(file.getName(), new FileInputStream(file), file.length());
	}

	@Override
	public TechnologyDocument createTechnologyDocument(String fileName, InputStream is, Long fileSize)
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
		return saveTechnologyDocument(fileName, new ByteArrayInputStream(byteArray), fileSize, content);
	}

	private TechnologyDocument saveTechnologyDocument(String fileName, InputStream is, Long fileSize, String content)
			throws Exception {
		logger.debug("saveTechnologyDocument,fileName:{},fileSize:{}", fileName, fileSize);
		FileMetaData fileMetaData = fileMetaDataService.saveFile(identification, fileName, is, fileSize);
		TechnologyDocument td = new TechnologyDocument();
		td.setContent(content);
		td.setFileId(fileMetaData.getObjectId().toString());
		return technologyDocumentDao.save(td);
	}

	@Override
	public List<TechnologyDocument> createTechnologyDocument(List<File> files) throws Exception {
		List<String> contents = FileFetchUtil.fetchContent(files);
		List<TechnologyDocument> documents = new ArrayList<>();
		for (int i = 0; i < files.size(); i++) {
			File file = files.get(i);
			documents.add(
					saveTechnologyDocument(file.getName(), new FileInputStream(file), file.length(), contents.get(i)));
		}
		return documents;
	}

	@Override
	public List<TechnologyDocument> createTechnologyDocument(MultipartFile[] multipartFiles) throws Exception {
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
					multipartFile.getSize()));
		}
		return documents;
	}

	@Override
	public Page<TechnologyDocument> getHighLightContent(String content, Pageable pageable) throws Exception {
		logger.debug("search content:{}", content);
		// 判断是否需要插入中央词库,新开一个线程,不影响用户体验
		new Thread(() -> searchRecordService.insertInto(content, "search_" + SessionUtil.getCurrentUser().getLoginId()))
				.start();
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.matchQuery("content", content)).withPageable(pageable);
		return technologyDocumentDao.getHighLightContent(elasticsearchTemplate, queryBuilder);
	}

}
