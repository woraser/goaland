package com.anosi.asset.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Base64Utils;

import com.anosi.asset.exception.CustomRunTimeException;
import com.aspose.pdf.Document;
import com.aspose.pdf.Image;
import com.aspose.pdf.Page;

public class ImageUtil {

	/***
	 * 将图片转为pdf
	 * 
	 * @param inputStream
	 * @param outputStream
	 * @param suffix
	 *            具体后缀
	 * @throws Exception
	 */
	public static void convert2PDF(InputStream inputStream, OutputStream outputStream, String suffix) throws Exception {
		// instantiate Document instance
		Document doc = new Document();
		// add a page to pages collection of Pdf file
		Page page = doc.getPages().add();
		// create image instance
		Image image = new Image();
		// create BufferedImage instance
		java.awt.image.BufferedImage bufferedImage = ImageIO.read(inputStream);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// write buffered Image to OutputStream instance
		ImageIO.write(bufferedImage, suffix, baos);
		baos.flush();
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		// add image to paragraphs collection of first page
		page.getParagraphs().add(image);
		// set image stream as OutputStream holding Buffered image
		image.setImageStream(bais);
		// save resultant PDF file
		doc.save(outputStream);
	}

	/***
	 * 将base64转为图片
	 * 
	 * @param base64
	 *            data:image/jpeg;base64,xxxxxxx
	 * @throws Exception
	 */
	public static Base64ImageBean convertBase64ToImage(String base64) throws Exception {
		if (StringUtils.isBlank(base64)) {
			throw new NullPointerException();
		}
		String[] base64s = base64.split("base64,");
		String dataPrix;
		String data;
		if (base64s != null && base64s.length == 2) {
			dataPrix = base64s[0];
			data = base64s[1];
		} else {
			throw new CustomRunTimeException("base64 is illegal");
		}
		String suffix = "";
		if ("data:image/jpeg;".equalsIgnoreCase(dataPrix)) {// data:image/jpeg;base64,base64编码的jpeg图片数据
			suffix = ".jpg";
		} else if ("data:image/x-icon;".equalsIgnoreCase(dataPrix)) {// data:image/x-icon;base64,base64编码的icon图片数据
			suffix = ".ico";
		} else if ("data:image/gif;".equalsIgnoreCase(dataPrix)) {// data:image/gif;base64,base64编码的gif图片数据
			suffix = ".gif";
		} else if ("data:image/png;".equalsIgnoreCase(dataPrix)) {// data:image/png;base64,base64编码的png图片数据
			suffix = ".png";
		} else {
			throw new CustomRunTimeException("suffix is illegal");
		}
		String tempFileName = UUID.randomUUID().toString() + suffix;
		
		byte[] bs = Base64Utils.decodeFromString(data);// 将base64转换为数组
		
		Base64ImageBean base64ImageBean = new Base64ImageBean();
		base64ImageBean.setFileName(tempFileName);
		base64ImageBean.setFileSize(bs.length);
		base64ImageBean.setIs(new ByteArrayInputStream(bs));
		
		return base64ImageBean;
	}
	
	public static class Base64ImageBean{
		
		private String fileName;
		
		private long fileSize;
		
		private InputStream is;

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public long getFileSize() {
			return fileSize;
		}

		public void setFileSize(long fileSize) {
			this.fileSize = fileSize;
		}

		public InputStream getIs() {
			return is;
		}

		public void setIs(InputStream is) {
			this.is = is;
		}
		
	}

}
