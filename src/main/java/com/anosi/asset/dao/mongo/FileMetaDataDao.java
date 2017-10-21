package com.anosi.asset.dao.mongo;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anosi.asset.model.mongo.FileMetaData;

public interface FileMetaDataDao
		extends BaseMongoDao<FileMetaData> {

	public Page<FileMetaData> findByIdentification(String identification, Pageable pageable);

	public List<FileMetaData> findByIdentification(String identification);

	public Page<FileMetaData> findByUploader(String uploader, Pageable pageable);

	public FileMetaData findByObjectId(BigInteger objectId);

	/***
	 * 根据一组identification查找
	 * 
	 * @param identifications
	 * @param pageable
	 * @return
	 */
	public Page<FileMetaData> findByIdentificationIn(List<String> identifications, Pageable pageable);

}
