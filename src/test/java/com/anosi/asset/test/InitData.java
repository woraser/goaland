package com.anosi.asset.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.GoalandApplication;
import com.anosi.asset.component.PasswordEncry;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.DocumentType;
import com.anosi.asset.service.AccountService;
import com.anosi.asset.service.DocumentTypeService;
import com.anosi.asset.service.RoleService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GoalandApplication.class)
@Transactional
public class InitData {
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private DocumentTypeService documentTypeService;

	@Test
	@Rollback(false)
	public void initData(){
		initEngineerManager();
		initCustomerServicer();
		initEngineer();
		initType();
	}
	
	@Test
	@Rollback(false)
	public void initEngineerManager(){
		Account account = new Account();
		account.setName("工程部经理");
		account.setLoginId("gcbjl");
		account.setPassword("123456");
		account.getRoleList().add(roleService.findByCode("engineerManager"));
		try {
			//设置密码
			PasswordEncry.encrypt(account);
		} catch (Exception e) {
			e.printStackTrace();
		}
		accountService.save(account);
	}
	
	@Test
	@Rollback(false)
	public void initCustomerServicer(){
		Account account = new Account();
		account.setName("售后服务组人员");
		account.setLoginId("shfwz");
		account.setPassword("123456");
		account.getRoleList().add(roleService.findByCode("customerServicer"));
		try {
			//设置密码
			PasswordEncry.encrypt(account);
		} catch (Exception e) {
			e.printStackTrace();
		}
		accountService.save(account);
	}
	
	@Test
	@Rollback(false)
	public void initEngineer(){
		Account account = new Account();
		account.setName("工程师");
		account.setLoginId("gcs");
		account.setPassword("123456");
		account.getRoleList().add(roleService.findByCode("engineer"));
		try {
			//设置密码
			PasswordEncry.encrypt(account);
		} catch (Exception e) {
			e.printStackTrace();
		}
		accountService.save(account);
	}
	
	@Test
	@Rollback(false)
	public void initType(){
		DocumentType type = new DocumentType();
		type.setName("故障文档");
		documentTypeService.save(type);
		DocumentType type2 = new DocumentType();
		type2.setName("技术文档");
		documentTypeService.save(type2);
		DocumentType type3 = new DocumentType();
		type3.setName("设备文档");
		documentTypeService.save(type3);
	}
	
}
