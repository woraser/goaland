package com.anosi.asset.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.MessageFormat;

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
import com.anosi.asset.component.I18nComponent;
import com.anosi.asset.exception.CustomRunTimeException;
import com.anosi.asset.model.mongo.FileMetaData;
import com.anosi.asset.service.FileMetaDataService;
import com.anosi.asset.util.FileConvertUtil;
import com.anosi.asset.util.FileConvertUtil.Suffix;

/***
 * 这个类包含了文件上传、下载、预览功能
 * 
 * @author jinyao
 *
 */
@RestController
public class FileUpDownLoadController extends BaseController<FileMetaData> {

	private static final Logger logger = LoggerFactory.getLogger(FileUpDownLoadController.class);

	@Autowired
	private FileMetaDataService fileMetaDataService;
	@Autowired
	private I18nComponent i18nComponent;

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
		logger.debug("identification:{}", identification);
		JSONObject jsonObject = new JSONObject();
		if (multipartFiles != null && multipartFiles.length > 0) {
			for (MultipartFile multipartFile : multipartFiles) {
				logger.debug("is uploading");
				try {
					this.fileMetaDataService.saveFile(identification, multipartFile.getOriginalFilename(),
							multipartFile.getInputStream(), multipartFile.getSize());
				} catch (Exception e) {
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
	 * 根据上传者来获取可下载文件的列表
	 * 
	 * @param identification
	 * @throws Exception
	 */
	@RequestMapping(value = "/fileDownload/list/{uploader}", method = RequestMethod.GET)
	public Page<FileMetaData> fileDownloadList(@PathVariable String uploader,
			@PageableDefault(sort = {
					"uploadTime" }, direction = Sort.Direction.DESC, page = 0, value = 20) Pageable pageable)
			throws Exception {
		logger.info("to view file list");
		logger.debug("uploader:{}", uploader);
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
		return fileMetaDataService.findByIdentification(uploader, pageable);
	}

	/***
	 * 根据文件组来获取可下载文件的列表
	 * 
	 * @param identification
	 * @throws Exception
	 */
	@RequestMapping(value = "/fileDownload/list/group/{identification}", method = RequestMethod.GET)
	public Page<FileMetaData> fileDownloadListGroup(@PathVariable String identification,
			@PageableDefault(sort = {
					"uploadTime" }, direction = Sort.Direction.DESC, page = 0, value = 20) Pageable pageable)
			throws Exception {
		logger.info("to view file list");
		logger.debug("identification:{}", identification);
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
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
		DoResponseOut doResponseOut = (is, os) -> {
			// 获取文件流
			try (BufferedInputStream bis = new BufferedInputStream(is);
					OutputStream bos = new BufferedOutputStream(response.getOutputStream());) {
				IOUtils.copy(bis, bos);
			}
		};
		return fileResponse(objectId, response, doResponseOut);
	}

	/***
	 * 文件预览
	 * 
	 * @param objectId
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/filePreview/{objectId}", method = RequestMethod.GET)
	public String filePreview(@PathVariable BigInteger objectId, HttpServletResponse response) throws Exception {
		DoResponseOut doResponseOut = (is, os) -> {
			FileMetaData fileMetaData = fileMetaDataService.findByObjectId(objectId);
			String fileName = fileMetaData.getFileName();
			try {
				FileConvertUtil.convert(is,
						Suffix.valueOf(fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase()), os,
						Suffix.PDF);
			} catch (IllegalArgumentException e) {
				throw new CustomRunTimeException(
						MessageFormat.format(i18nComponent.getMessage("exception.unSupportSuffix"),
								fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase()));
			}

		};

		return fileResponse(objectId, response, doResponseOut);
	}

	/***
	 * 文件流返回给浏览器
	 * 
	 * @param objectId
	 * @param response
	 * @param doResponseOut
	 *            response.outPutStream的具体操作
	 * @return
	 * @throws Exception
	 */
	private String fileResponse(BigInteger objectId, HttpServletResponse response, DoResponseOut doResponseOut)
			throws Exception {
		FileMetaData fileMetaData = fileMetaDataService.findByObjectId(objectId);

		JSONObject jsonObject = new JSONObject();

		if (fileMetaData != null) {
			logger.debug("fileMetaData:{}", fileMetaData.toString());
			// 设置http请求头
			response.setHeader("Content-disposition",
					"attachment;filename=" + new String(fileMetaData.getFileName().getBytes("gbk"), "iso-8859-1"));
			response.setContentType("application/force-download;charset=utf-8");
			response.setHeader("Content_length", String.valueOf(fileMetaData.getFileSize()));

			doResponseOut.excute(fileMetaDataService.getFileByObjectId(objectId), response.getOutputStream());

			jsonObject.put("result", "success");
		} else {
			logger.info("fileMetaData is null");
			jsonObject.put("result",
					MessageFormat.format(i18nComponent.getMessage("exception.fileNotFound"), objectId));
		}
		logger.info(jsonObject.toString());
		return jsonObject.toString();
	}

	/***
	 * 内部方法接口,用来执行completeTask中的具体操作
	 * 
	 * @author jinyao
	 *
	 */
	@FunctionalInterface
	private interface DoResponseOut {
		public void excute(InputStream is, OutputStream os) throws Exception;
	}

}
