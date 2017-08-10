package com.anosi.asset.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.exception.CustomRunTimeException;
import com.anosi.asset.model.mongo.FileMetaData;
import com.anosi.asset.service.FileMetaDataService;

@RestController
public class FileUpDownLoadController extends BaseController<FileMetaData> {

	private static final Logger logger = LoggerFactory.getLogger(FileUpDownLoadController.class);
	
	@Autowired
	private FileMetaDataService fileMetaDataService;

	/***
	 * 上传文件
	 * 
	 * @param multipartFiles
	 * @param identification
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/fileUpload/multipartFiles/{identification}", method = RequestMethod.POST)
	public String fileUpload(@RequestParam("file_upload") MultipartFile[] multipartFiles,
			@PathVariable String identification) throws Exception {
		logger.info("file upload");
		logger.debug("identification:{}",identification);
		JSONObject jsonObject = new JSONObject();
		if (multipartFiles != null && multipartFiles.length > 0) {
			for (MultipartFile multipartFile : multipartFiles) {
				logger.debug("is uploading");
				try {
					this.fileMetaDataService.saveFile(identification, multipartFile.getOriginalFilename(),
							multipartFile.getInputStream(), multipartFile.getSize());
				}  catch (Exception e) {
					throw new CustomRunTimeException("upload fail");
				}
			}
			jsonObject.put("result", "upload success");
		} else {
			jsonObject.put("result", "file is null");
		}
		logger.info(jsonObject.toString());
		return jsonObject.toString();
	}

	/***
	 * 可下载文件的列表
	 * 
	 * @param identification
	 * @throws Exception
	 */
	@RequestMapping(value = "/fileDownload/list/{identification}", method = RequestMethod.GET)
	public Page<FileMetaData> fileDownloadList(@PathVariable String identification,
			@PageableDefault(sort = {"uploadTime" }, direction = Sort.Direction.DESC, page = 0, value = 20) Pageable pageable)throws Exception {
		logger.info("to view file list");
		logger.debug("identification:{}",identification);
		logger.debug("page:{},size{},sort{}",pageable.getPageNumber(),pageable.getPageSize(),pageable.getSort());
		return fileMetaDataService.findByIdentification(identification, pageable);
	}

	/****
	 * 下载文件
	 * 
	 * @param identification
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/fileDownload/{objectId}", method = RequestMethod.GET)
	public String fileDownload(@PathVariable BigInteger objectId, HttpServletResponse response) throws Exception {
		logger.info("file download");
		FileMetaData fileMetaData = fileMetaDataService.findByObjectId(objectId);
		
		JSONObject jsonObject = new JSONObject();
		
		if(fileMetaData!=null){
			logger.debug("fileMetaData:{}",fileMetaData.toString());
			// 设置http请求头
			response.setHeader("Content-disposition", "attachment;filename=" + new String(fileMetaData.getFileName().getBytes("gbk"),"iso-8859-1"));
			response.setContentType("application/force-download;charset=utf-8");
			response.setHeader("Content_length", String.valueOf(fileMetaData.getFileSize()));
			
			/*start:trywithresource*/
			try (BufferedInputStream bis = new BufferedInputStream(fileMetaDataService.getFileByObjectId(objectId));// 获取文件流
					OutputStream os = new BufferedOutputStream(response.getOutputStream())) {
				IOUtils.copy(bis, os);
			}
			/*end:trywithresource*/
			jsonObject.put("result", "success");
		}else{
			logger.info("fileMetaData is null");
			jsonObject.put("result", objectId+"	该文件不存在");
		}
		logger.info(jsonObject.toString());
		return jsonObject.toString();
	}

}
