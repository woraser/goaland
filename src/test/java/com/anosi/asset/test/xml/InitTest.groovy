package com.anosi.asset.test.xml;

import static org.junit.Assert.*

import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional

import com.anosi.asset.GoalandApplication
import com.anosi.asset.init.InitCityRelated
import com.anosi.asset.init.InitDepRelated
import com.anosi.asset.init.InitRoleFunctionRelated

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GoalandApplication.class)
@Transactional
class InitTest {
	
	@Autowired
	private InitDepRelated initDepRelated;
	@Autowired
	private InitRoleFunctionRelated initRoleFunctionRelated;
	@Autowired
	private InitCityRelated initCityRelated;

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	@Test
	@Rollback(false)
	public void testInitDep(){
		initDepRelated.initDepRelated()
	}
	
	@Test
	@Rollback(false)
	public void testInitCity(){
		initCityRelated.initProvince();
		initCityRelated.initCity();
		initCityRelated.initDistrict();
	}
	
	@Test
	@Rollback(false)
	public void testInitRoleFunction(){
		
	}

}
