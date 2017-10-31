package com.anosi.asset.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.bean.FileMetaDataBean;
import com.anosi.asset.dao.mongo.BaseMongoDao;
import com.anosi.asset.dao.mongo.FileMetaDataDao;
import com.anosi.asset.dao.mongo.GridFsDao;
import com.anosi.asset.model.mongo.FileMetaData;
import com.anosi.asset.service.FileMetaDataService;
import com.anosi.asset.util.FileConvertUtil;
import com.anosi.asset.util.FileConvertUtil.Suffix;

@Service("fileMetaDataService")
@Transactional
public class FileMetaDataServiceImpl extends BaseMongoServiceImpl<FileMetaData> implements FileMetaDataService {

	private static final Logger logger = LoggerFactory.getLogger(FileMetaDataServiceImpl.class);

	@Autowired
	private FileMetaDataDao fileMetaDataDao;
	@Autowired
	private GridFsDao gridFsDao;

	@Override
	public BaseMongoDao<FileMetaData> getRepository() {
		return fileMetaDataDao;
	}

	@Override
	public FileMetaData saveFile(String identification, String fileName, InputStream is, Long fileSize)
			throws Exception {
		return fileMetaDataDao.save(createFileMetaData(identification, fileName, is, fileSize));
	}

	public List<FileMetaData> saveFile(List<FileMetaDataBean> fileMetaDataBeans) throws Exception {
		List<FileMetaData> fileMetaDatas = new ArrayList<>();
		for (FileMetaDataBean fileMetaDataBean : fileMetaDataBeans) {
			fileMetaDatas.add(createFileMetaData(fileMetaDataBean.getIdentification(), fileMetaDataBean.getFileName(),
					fileMetaDataBean.getIs(), fileMetaDataBean.getFileSize()));
		}
		return fileMetaDataDao.save(fileMetaDatas);
	}

	private FileMetaData createFileMetaData(String identification, String fileName, InputStream is, Long fileSize)
			throws Exception {
		FileMetaData fileMetaData = new FileMetaData();
		fileMetaData.setIdentification(identification);
		fileMetaData.setUploader(sessionComponent.getCurrentUser() == null ? identification
				: sessionComponent.getCurrentUser().getLoginId());
		fileMetaData.setUploadTime(new Date());
		fileMetaData.setFileName(fileName);
		fileMetaData.setFileSize(fileSize);
		this.saveFileAndAttributes(fileMetaData, is);
		return fileMetaData;
	}

	private FileMetaData saveFileAndAttributes(FileMetaData fileMetaData, InputStream in) throws Exception {
		// gridfs存储文件
		logger.info("upload file to gridfs");
		Object id = gridFsDao.uploadFileToGridFS(in, fileMetaData.getFileName());
		fileMetaData.setObjectId(FileMetaData.ObjectIdToBigIntegerConverter((ObjectId) id));
		return fileMetaData;
	}

	@Override
	public void deleteFile(FileMetaData fileMetaData) {
		ObjectId id = FileMetaData.BigIntegerToObjectIdConverter(fileMetaData.getObjectId());
		fileMetaDataDao.delete(fileMetaData);
		// gridfs删除文件
		logger.info("delete file from gridfs");
		gridFsDao.deleteFileFromGridFS(id);
	}

	@Override
	public Page<FileMetaData> findByIdentification(String identification, Pageable pageable) {
		return fileMetaDataDao.findByIdentification(identification, pageable);
	}

	@Override
	public FileMetaData findByObjectId(BigInteger objectId) {
		return fileMetaDataDao.findByObjectId(objectId);
	}

	@Override
	public InputStream getFileByObjectId(BigInteger objectId) {
		return gridFsDao.getFileFromGridFS(FileMetaData.BigIntegerToObjectIdConverter(objectId));
	}

	@Override
	public List<FileMetaData> findByIdentification(String identification) {
		return fileMetaDataDao.findByIdentification(identification);
	}

	@Override
	public List<FileMetaData> updateIdentification(String lastIdentification, String nowIdentification) {
		List<FileMetaData> fileMetaDatas = fileMetaDataDao.findByIdentification(lastIdentification);
		for (FileMetaData fileMetaData : fileMetaDatas) {
			fileMetaData.setIdentification(nowIdentification);
		}
		fileMetaDataDao.save(fileMetaDatas);
		return fileMetaDatas;
	}

	@Override
	@Async
	public Future<FileMetaData> createPreview(FileMetaData fileMetaData) throws Exception {
		FileMetaData preview = createFileMetaDataPreview(fileMetaData);
		fileMetaDataDao.save(preview);
		return new AsyncResult<FileMetaData>(preview);
	}

	@Override
	@Async
	public Future<List<FileMetaData>> createPreview(List<FileMetaData> fileMetaDatas) throws Exception {
		List<FileMetaData> previews = new ArrayList<>();
		for (FileMetaData fileMetaData : fileMetaDatas) {
			FileMetaData preview = createFileMetaDataPreview(fileMetaData);
			previews.add(preview);
		}
		previews = fileMetaDataDao.save(previews);
		return new AsyncResult<List<FileMetaData>>(previews);
	}

	private FileMetaData createFileMetaDataPreview(FileMetaData fileMetaData) throws Exception {
		String fileName = fileMetaData.getFileName();
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
		if (FileConvertUtil.checkSuffix(suffix)) {
			// 为filemetadata存储预览pdf文件
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			FileConvertUtil.convert(getFileByObjectId(fileMetaData.getObjectId()), Suffix.valueOf(suffix), os,
					Suffix.PDF);

			// 预览文件的元数据
			FileMetaData preview;
			preview = saveFile(fileMetaData.getIdentification(),
					fileName.substring(0, fileName.lastIndexOf(".")) + ".pdf",
					new ByteArrayInputStream(os.toByteArray()), fileMetaData.getFileSize());
			fileMetaData.setPreview(preview.getObjectId());
			return fileMetaData;
		}
		return null;
	}

}
