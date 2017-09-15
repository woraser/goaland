package com.anosi.asset.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.aspose.cad.Color;
import com.aspose.cad.Image;
import com.aspose.cad.fileformats.cad.CadImage;
import com.aspose.cad.fileformats.cad.cadconsts.CadEntityTypeName;
import com.aspose.cad.fileformats.cad.cadobjects.CadAttDef;
import com.aspose.cad.fileformats.cad.cadobjects.CadBaseEntity;
import com.aspose.cad.fileformats.cad.cadobjects.CadBlockEntity;
import com.aspose.cad.fileformats.cad.cadobjects.CadInsertObject;
import com.aspose.cad.fileformats.cad.cadobjects.CadText;
import com.aspose.cad.imageoptions.CadRasterizationOptions;
import com.aspose.cad.imageoptions.PdfOptions;

public class CadUtil {

	/***
	 * 将cad转换为pdf
	 * 
	 * @param inputStream
	 * @param outputStream
	 * @throws Exception
	 */
	public static void convert2PDF(InputStream inputStream, OutputStream outputStream) throws Exception {
		Image image = Image.load(inputStream);

		// Create an instance of CadRasterizationOptions and set its various
		// properties
		CadRasterizationOptions rasterizationOptions = new CadRasterizationOptions();
		rasterizationOptions.setBackgroundColor(Color.getWhite());
		rasterizationOptions.setPageWidth(1600);
		rasterizationOptions.setPageHeight(1600);

		// Create an instance of PdfOptions
		PdfOptions pdfOptions = new PdfOptions();
		// Set the VectorRasterizationOptions property
		pdfOptions.setVectorRasterizationOptions(rasterizationOptions);

		// Export the DWG to PDF
		image.save(outputStream, pdfOptions);
	}

	/***
	 * 读取cad中的文本内容
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 * 
	 * @deprecated 无法解决gbk和utf8的编码问题,所以暂时弃用
	 */
	@Deprecated
	public static String readCad(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		CadImage cadImage = (CadImage) CadImage.load(is);
		for (CadBaseEntity entity : cadImage.getEntities()) {
			iterateCADNodeEntities(entity, sb);
		}

		// Search for text in the block section
		for (CadBlockEntity blockEntity : cadImage.getBlockEntities().getValues()) {
			for (CadBaseEntity entity : blockEntity.getEntities()) {
				iterateCADNodeEntities(entity, sb);
			}
		}
		return sb.toString();
	}

	private static void iterateCADNodeEntities(CadBaseEntity obj, StringBuilder sb){
		switch (obj.getTypeName()) {
		case CadEntityTypeName.TEXT:
			CadText childObjectText = (CadText) obj;
			sb.append(childObjectText.getDefaultValue());
			sb.append("\n");
			break;

		case CadEntityTypeName.INSERT:
			CadInsertObject childInsertObject = (CadInsertObject) obj;

			for (CadBaseEntity tempobj : childInsertObject.getChildObjects()) {
				iterateCADNodeEntities(tempobj, sb);
			}
			break;

		case CadEntityTypeName.ATTDEF:
			CadAttDef attDef = (CadAttDef) obj;
			sb.append(attDef.getDefaultString());
			sb.append("\n");
			break;
		}
	}
	
}
