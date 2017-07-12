package com.anosi.asset.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.GoalandApplication;
import com.anosi.asset.dao.elasticsearch.RepairDocumentDao;
import com.anosi.asset.model.elasticsearch.RepairDocument;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GoalandApplication.class)
@Transactional
public class ElasticsearchTest {

	@Autowired
	private RepairDocumentDao repairDocumentDao;
	
	@Test
	public void testSave(){
		RepairDocument repairDocument = new RepairDocument();
		repairDocument.setId(System.currentTimeMillis());
		repairDocument.setProblemDescription("水机漏水");
		repairDocument.setFailureCause("年久失修");
		repairDocument.setProcessMode("返厂维修");
		repairDocumentDao.save(repairDocument);
	}
	
	@Test
	public void testFind(){
		List<RepairDocument> repairDocuments = repairDocumentDao.findByProblemDescriptionOrFailureCauseOrProcessMode("水机", "", "返厂");
		repairDocuments.stream().forEach(repairDocument->System.out.println(repairDocument.getProblemDescription()));
	}
	
}
