package com.anosi.asset.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
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

import com.anosi.asset.dao.elasticsearch.BaseElasticSearchDao;
import com.anosi.asset.dao.elasticsearch.TechnologyDocumentDao;
import com.anosi.asset.model.elasticsearch.TechnologyDocument;
import com.anosi.asset.model.mongo.FileMetaData;
import com.anosi.asset.service.AccountService;
import com.anosi.asset.service.FileMetaDataService;
import com.anosi.asset.service.SearchRecordService;
import com.anosi.asset.service.TechnologyDocumentService;
import com.anosi.asset.util.FileConvertUtil;
import com.anosi.asset.util.FileFetchUtil;

@Service("technologyDocumentService")
@Transactional
public class TechnologyDocumentServiceImpl extends BaseElasticSearchServiceImpl<TechnologyDocument, String>
		implements TechnologyDocumentService {

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
	private AccountService accountService;

	@Override
	public BaseElasticSearchDao<TechnologyDocument, String> getRepository() {
		return technologyDocumentDao;
	}

	@Override
	public TechnologyDocument createTechnologyDocument(File file, String type) throws Exception {
		return createTechnologyDocument(file.getName(), new FileInputStream(file), file.length(), type);
	}

	@Override
	public TechnologyDocument createTechnologyDocument(String fileName, InputStream is, Long fileSize, String type)
			throws Exception {
		FileMetaData fileMetaData = fileMetaDataService.saveFile(type, fileName, is, fileSize);
		InputStream fileByObjectId = fileMetaDataService.getFileByObjectId(fileMetaData.getObjectId());
		String content = FileFetchUtil.fetchContent(fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase(),
				fileByObjectId);
		return saveTechnologyDocument(fileName, fileByObjectId, fileSize, content, type, fileMetaData);
	}

	private TechnologyDocument saveTechnologyDocument(String fileName, InputStream is, Long fileSize, String content,
			String type, FileMetaData fileMetaData) throws Exception {
		logger.debug("saveTechnologyDocument,fileName:{},fileSize:{}", fileName, fileSize);

		// 判断文件是否可以预览
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
		if (FileConvertUtil.checkSuffix(suffix)) {
			// 为filemetadata存储预览pdf文件
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			FileConvertUtil.convert(fileMetaDataService.getFileByObjectId(fileMetaData.getObjectId()),
					com.anosi.asset.util.FileConvertUtil.Suffix.valueOf(suffix), os,
					com.anosi.asset.util.FileConvertUtil.Suffix.PDF);

			// 预览文件的元数据
			FileMetaData preview = fileMetaDataService.saveFile(type,
					fileName.substring(0, fileName.lastIndexOf(".")) + ".pdf",
					new ByteArrayInputStream(os.toByteArray()), fileSize);
			fileMetaData.setPreview(preview.getObjectId());
			fileMetaDataService.save(fileMetaData);
		}

		TechnologyDocument td = new TechnologyDocument();
		td.setContent(content);
		td.setFileId(fileMetaData.getObjectId().toString());
		td.setType(type);
		td.setFileName(fileName);
		td.setUploader(fileMetaData.getUploader());
		td.setUploadTime(fileMetaData.getUploadTime());
		accountService.getOne(sessionComponent.getCurrentUser().getId()).setUploadDocument(true);
		return technologyDocumentDao.save(td);
	}

	@Override
	public List<TechnologyDocument> createTechnologyDocument(List<File> files, String type) throws Exception {
		List<TechnologyDocument> documents = new ArrayList<>();
		for (File file : files) {
			documents.add(createTechnologyDocument(file.getName(), new FileInputStream(file), file.length(), type));
		}
		return documents;
	}

	@Override
	public List<TechnologyDocument> createTechnologyDocument(MultipartFile[] multipartFiles, String type)
			throws Exception {
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
		Page<TechnologyDocument> page = technologyDocumentDao.getHighLight(elasticsearchTemplate,
				parseToQuery(technologyDocument, queryBuilder));
		// 把上传人从登录帐号改为用户名
		page.getContent().forEach(t -> t.setUploader(accountService.findByLoginId(t.getUploader()).getName()));
		return page;
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
					"search_" + sessionComponent.getCurrentUser().getLoginId())).start();
			queryBuilder.withQuery(multiMatchQuery(searchContent, "content", "fileName"));
		}

		BoolQueryBuilder boolQueryBuilder = null;

		// 查询文档类型
		if (StringUtils.isNoneBlank(type)) {
			boolQueryBuilder = checkBoolQueryBuilderMust(boolQueryBuilder, termQuery("type", type));
		} else {
			// TODO 根据所有可选type进行查询
		}

		// 查询文档上传人
		if (StringUtils.isNoneBlank(uploader)) {
			boolQueryBuilder = checkBoolQueryBuilderMust(boolQueryBuilder, termQuery("uploader", uploader));
		}

		// 查询文档上传时间的下限,上限
		if (lowerLimit != null && upperLimit != null) {
			boolQueryBuilder = checkBoolQueryBuilderMust(boolQueryBuilder,
					rangeQuery("uploadTime").from(lowerLimit).to(upperLimit));
		} else if (upperLimit != null) {
			boolQueryBuilder = checkBoolQueryBuilderMust(boolQueryBuilder, rangeQuery("uploadTime").to(upperLimit));
		} else if (lowerLimit != null) {
			boolQueryBuilder = checkBoolQueryBuilderMust(boolQueryBuilder, rangeQuery("uploadTime").from(lowerLimit));
		}
		if (boolQueryBuilder != null) {
			queryBuilder.withFilter(boolQueryBuilder);
		}
		return queryBuilder;
	}

	private BoolQueryBuilder checkBoolQueryBuilderMust(BoolQueryBuilder boolQueryBuilder, QueryBuilder queryBuilder) {
		if (boolQueryBuilder == null) {
			boolQueryBuilder = boolQuery().must(queryBuilder);
		} else {
			boolQueryBuilder.must(queryBuilder);
		}
		return boolQueryBuilder;
	}

	@SuppressWarnings("unused")
	private BoolQueryBuilder checkBoolQueryBuilderShould(BoolQueryBuilder boolQueryBuilder, QueryBuilder queryBuilder) {
		if (boolQueryBuilder == null) {
			boolQueryBuilder = boolQuery().should(queryBuilder);
		} else {
			boolQueryBuilder.should(queryBuilder);
		}
		return boolQueryBuilder;
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
