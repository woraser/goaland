package com.anosi.asset.dao.mongo;

import java.math.BigInteger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.anosi.asset.model.mongo.FileMetaData;


public interface FileMetaDataDao extends MongoRepository<FileMetaData, BigInteger>{

	public Page<FileMetaData> findByIdentification(String identification, Pageable pageable);
	
	public FileMetaData findByObjectId(BigInteger objectId);
	
}
