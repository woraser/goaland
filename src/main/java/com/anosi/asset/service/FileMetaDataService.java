package com.anosi.asset.service;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anosi.asset.bean.FileMetaDataBean;
import com.anosi.asset.model.mongo.FileMetaData;

public interface FileMetaDataService extends BaseMongoService<FileMetaData> {

	/****
	 * 保存文件
	 * 
	 * @param identification
	 *            文件存储的一个组标识
	 * @param fileName
	 *            文件名
	 * @param is
	 *            文件输入流
	 * @return
	 * @throws Exception
	 */
	public FileMetaData saveFile(String identification, String fileName, InputStream is, Long fileSize)
			throws Exception;

	/****
	 * 保存文件(批量形式)
	 * 
	 * @param fileMetaDataBeans
	 * @return
	 * @throws Exception
	 */
	public List<FileMetaData> saveFile(List<FileMetaDataBean> fileMetaDataBeans) throws Exception;

	/***
	 * 删除文件和文件属性
	 * 
	 * @param fileAttributes
	 */
	public void deleteFile(FileMetaData fileAttributes);

	public Page<FileMetaData> findByIdentification(String identification, Pageable pageable);

	public List<FileMetaData> findByIdentification(String identification);

	/***
	 * 批量更新Identification
	 * 
	 * @param lastIdentification
	 * @param nowIdentification
	 * @return
	 */
	List<FileMetaData> updateIdentification(String lastIdentification, String nowIdentification);

	/***
	 * 创建预览文件(批量形式)
	 * 
	 * @param fileMetaDatas
	 * @return 由于是异步任务，返回的是Future
	 * @throws Exception 
	 * 
	 */
	Future<List<FileMetaData>> createPreview(List<FileMetaData> fileMetaDatas) throws Exception;

	/***
	 * 创建预览文件
	 * 
	 * @param fileMetaData
	 * @return 由于是异步任务，返回的是Future
	 * @throws Exception 
	 * 
	 */
	Future<FileMetaData> createPreview(FileMetaData fileMetaData) throws Exception;

	public FileMetaData findByObjectId(BigInteger objectId);

	public InputStream getFileByObjectId(BigInteger objectId);

}
