package com.anosi.asset.service.impl;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.mongo.BaseMongoDao;
import com.anosi.asset.dao.mongo.FileMetaDataDao;
import com.anosi.asset.dao.mongo.GridFsDao;
import com.anosi.asset.model.mongo.FileMetaData;
import com.anosi.asset.service.FileMetaDataService;

@Service("fileMetaDataService")
@Transactional
public class FileMetaDataServiceImpl extends BaseMongoServiceImpl<FileMetaData> implements FileMetaDataService{
	
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
	public FileMetaData saveFile(String identification, String fileName,InputStream is,Long fileSize) throws Exception{
		FileMetaData fileMetaData=new FileMetaData();
		fileMetaData.setIdentification(identification);
		fileMetaData.setUploader(sessionComponent.getCurrentUser()==null?identification:sessionComponent.getCurrentUser().getLoginId());
		fileMetaData.setUploadTime(new Date());
		fileMetaData.setFileName(fileName);
		fileMetaData.setFileSize(fileSize);
		
		return this.saveFileAndAttributes(fileMetaData, is);
	}
	
	private FileMetaData saveFileAndAttributes(FileMetaData fileMetaData,InputStream in) throws Exception {
		//gridfs存储文件
		logger.info("upload file to gridfs");
		Object id = gridFsDao.uploadFileToGridFS(in,fileMetaData.getFileName());
		fileMetaData.setObjectId(FileMetaData.ObjectIdToBigIntegerConverter((ObjectId) id));
		fileMetaDataDao.save(fileMetaData);
		return fileMetaData;
	}
	
	

	@Override
	public void deleteFile(FileMetaData fileMetaData) {
		ObjectId id=FileMetaData.BigIntegerToObjectIdConverter(fileMetaData.getObjectId());
		fileMetaDataDao.delete(fileMetaData);
		//gridfs删除文件
		logger.info("delete file from gridfs");
		gridFsDao.deleteFileFromGridFS(id);
	}

	@Override
	public Page<FileMetaData> findByIdentification(String identification,Pageable pageable) {
		return fileMetaDataDao.findByIdentification(identification,pageable);
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

}
