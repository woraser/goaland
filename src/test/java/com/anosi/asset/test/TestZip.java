package com.anosi.asset.test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class TestZip {

	@Test
	public void testZip() throws Exception {
		File file = new File("F:/testUpdate/test1/12345.xlsx");

		String name = file.getAbsolutePath();
		String subName = name.substring(0, name.lastIndexOf("."));
		file.renameTo(new File(subName + ".zip"));
		//修改后缀的文件
		File newFile = new File(subName + ".zip");

		ZipFile zipFile = new ZipFile(newFile);
		// 提取文件进行修改
		zipFile.extractFile("docProps/core.xml", "F:/testUpdate/test1/");
		File coreFile = new File("F:/testUpdate/test1/docProps/core.xml");
		// 处理xml节点
		modifyXML(coreFile);

		zipFile.removeFile("docProps/core.xml");

		ZipParameters parameters = new ZipParameters();
		parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

		parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
		parameters.setRootFolderInZip("docProps/");
		zipFile.addFile(new File("F:/testUpdate/test1/docProps/core.xml"), parameters);

		FileUtils.deleteDirectory(new File("F:/testUpdate/test1/docProps/"));
		//修改回xlsx
		newFile.renameTo(new File(subName + ".xlsx"));
	}

	private void modifyXML(File file) throws Exception {
		SAXReader reader = new SAXReader();
		Document document = reader.read(file);
		Element root = document.getRootElement();

		Element lastModifiedBy = root.element("lastModifiedBy");
		lastModifiedBy.setText("jinyao");
		Element modified = root.element("modified");
		modified.setText("2014-09-14T05:51:23Z");

		// 把创建的Document对象写到xml文件
		// 指定文件输出位置
		FileOutputStream out = new FileOutputStream("F:/testUpdate/test1/docProps/core.xml");
		// OutputFormat format =
		// OutputFormat.createCompactFormat();//生成物理文件，布局较乱适合电脑
		OutputFormat format = OutputFormat.createPrettyPrint();// 标准化布局，适合查看时显示。
		// 1.创建写入文件
		format.setEncoding("gbk");// 指定文件格式
		XMLWriter writer = new XMLWriter(out, format);
		writer.write(document);// 写入文件
		writer.close();
	}
	
	@Test
	public void testRandom(){
		Random random = new Random();
		System.out.println(random.nextDouble());
		System.out.println(random.nextDouble()*(1-0.6)+0.6);
	}

}
