package com.anosi.asset.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

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

}
