package com.anosi.asset.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anosi.asset.model.jpa.BaseEntity;
import com.anosi.asset.util.CodeUtil;

/****
 * 二维码相关的controller
 * 
 * @author jinyao
 *
 */
@RestController
public class QRController extends BaseController<BaseEntity>{

	@RequestMapping(value = "/QRCode", method = RequestMethod.GET)
	public void fileDownload(@RequestParam(value="message") String message, HttpServletResponse response) throws Exception {
		BufferedImage image = CodeUtil.getRQ(message, 200);
		// image转inputStream
		ByteArrayOutputStream os = new ByteArrayOutputStream();  
		ImageIO.write(image, "gif", os);  
		InputStream is = new ByteArrayInputStream(os.toByteArray());  
		
		response.setHeader("Content-disposition",
				"attachment;filename=" + new String(UUID.randomUUID().toString().getBytes("gbk"), "iso-8859-1"));
		response.setContentType("application/force-download;charset=utf-8");
		// 获取文件流
		try (BufferedInputStream bis = new BufferedInputStream(is);
				OutputStream bos = new BufferedOutputStream(response.getOutputStream());) {
			IOUtils.copy(bis, bos);
		}
		
	}
	
}
