package com.anosi.asset.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.anosi.asset.util.TXTUtil;
import com.aspose.cad.fileformats.cad.CadImage;
import com.aspose.cad.fileformats.cad.cadconsts.CadEntityTypeName;
import com.aspose.cad.fileformats.cad.cadobjects.CadAttDef;
import com.aspose.cad.fileformats.cad.cadobjects.CadBaseEntity;
import com.aspose.cad.fileformats.cad.cadobjects.CadBlockEntity;
import com.aspose.cad.fileformats.cad.cadobjects.CadInsertObject;
import com.aspose.cad.fileformats.cad.cadobjects.CadText;

public class TestCad {

	@Test
	public void testReadCad() throws UnsupportedEncodingException, FileNotFoundException{
		String dataDir = "C:/Users/jinyao/Documents/Tencent Files/573380618/FileRecv/高澜资料/";

		// The path to the resource directory.
		String srcFile = dataDir + "GLTH2-1416002-02-1 水蓄冷系统原理图（竣工）.dwg";
		
		System.out.println(TXTUtil.charsetName(new FileInputStream(new File(srcFile))));
		
		CadImage cadImage = (CadImage) CadImage.load(new FileInputStream(new File(srcFile)));
		for (CadBaseEntity entity : cadImage.getEntities()) {
			IterateCADNodeEntities(entity);
		}

		// Search for text in the block section
		for (CadBlockEntity blockEntity : cadImage.getBlockEntities().getValues()) {
			for (CadBaseEntity entity : blockEntity.getEntities()) {
				IterateCADNodeEntities(entity);
			}
		}
	}

	private static void IterateCADNodeEntities(CadBaseEntity obj) throws UnsupportedEncodingException {
		switch (obj.getTypeName()) {
		case CadEntityTypeName.TEXT:
			CadText childObjectText = (CadText) obj;
			
			String defaultValue = childObjectText.getDefaultValue();
			
			String gbk = new String(defaultValue.getBytes("utf-8"), "gbk");
			String utf8 = new String(gbk.getBytes("gbk"), "utf-8");
			System.out.println(utf8);

			break;

		case CadEntityTypeName.INSERT:
			CadInsertObject childInsertObject = (CadInsertObject) obj;

			for (CadBaseEntity tempobj : childInsertObject.getChildObjects()) {
				IterateCADNodeEntities(tempobj);
			}
			break;

		case CadEntityTypeName.ATTDEF:
			CadAttDef attDef = (CadAttDef) obj;

			System.out.println(attDef.getDefaultString());
			break;
		}
	}
	
}
