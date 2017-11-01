package com.anosi.asset.test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.GoalandApplication;
import com.anosi.asset.dao.mongo.GridFsDao;
import com.anosi.asset.model.mongo.AbstractDocument;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GoalandApplication.class)
@Transactional
public class GridFSTest {

	@Autowired
	private GridFsDao gridFsDao;
	
	@Test
	public void testFindFile() throws IOException{
		InputStream inputStream = gridFsDao.getFileFromGridFS(AbstractDocument.BigIntegerToObjectIdConverter(new BigInteger("27842144589593079390651535565")));
		System.out.println(inputStream.available());
	}
	
}
