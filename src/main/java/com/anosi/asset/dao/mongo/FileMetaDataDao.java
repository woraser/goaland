package com.anosi.asset.dao.mongo;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.anosi.asset.model.mongo.FileMetaData;


public interface FileMetaDataDao extends MongoRepository<FileMetaData, BigInteger>{

	public List<FileMetaData> findByIdentification(String identification, Pageable pageable);
	
	public FileMetaData findByObjectId(BigInteger objectId);
	
}
