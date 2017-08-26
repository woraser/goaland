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
import com.anosi.asset.service.AccountService;
import com.anosi.asset.service.RoleService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GoalandApplication.class)
@Transactional
public class InitData {
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private RoleService roleService;

	@Test
	@Rollback(false)
	public void initEngineerManager(){
		Account account = new Account();
		account.setName("工程部经理");
		account.setLoginId("gcbjl");
		account.setPassword("123456");
		account.setRole(roleService.findByCode("engineerManager"));
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
		account.setRole(roleService.findByCode("customerServicer"));
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
		account.setRole(roleService.findByCode("engineer"));
		try {
			//设置密码
			PasswordEncry.encrypt(account);
		} catch (Exception e) {
			e.printStackTrace();
		}
		accountService.save(account);
	}
	
}
