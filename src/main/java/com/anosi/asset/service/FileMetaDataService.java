package com.anosi.asset.service;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anosi.asset.model.mongo.FileMetaData;
import com.querydsl.core.types.Predicate;

public interface FileMetaDataService {
	
	/****
	 * 保存文件
	 * @param identification	文件存储的一个组标识
	 * @param fileName	文件名
	 * @param is	文件输入流
	 * @return
	 * @throws Exception
	 */
	public FileMetaData saveFile(String identification,String fileName,InputStream is,Long fileSize) throws Exception;

	/***
	 * 删除文件和文件属性
	 * @param fileAttributes
	 */
	public void deleteFile(FileMetaData fileAttributes);
	
	public Page<FileMetaData> findByIdentification(String identification,Pageable pageable);
	
	public List<FileMetaData> findByIdentification(String identification);
	
	/***
	 * 批量更新Identification
	 * @param lastIdentification
	 * @param nowIdentification
	 * @return
	 */
	List<FileMetaData> updateIdentification(String lastIdentification,String nowIdentification);
	
	public FileMetaData findByObjectId(BigInteger objectId);
	
	public InputStream getFileByObjectId(BigInteger objectId);
	
	Page<FileMetaData> findAll(Predicate predicate, Pageable pageable);
	
	public FileMetaData save(FileMetaData fileMetaData);
	
}
