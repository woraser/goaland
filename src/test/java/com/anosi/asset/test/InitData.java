package com.anosi.asset.test;

import java.util.UUID;

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
import com.anosi.asset.model.jpa.Device;
import com.anosi.asset.model.jpa.Project;
import com.anosi.asset.service.AccountService;
import com.anosi.asset.service.DevCategoryService;
import com.anosi.asset.service.DeviceService;
import com.anosi.asset.service.ProjectService;
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
	private DeviceService deviceService;
	@Autowired
	private DevCategoryService devCategoryService;
	@Autowired
	private ProjectService projectService;

	@Test
	@Rollback(false)
	public void initData() throws Exception{
		/*initEngineerManager();
		initCustomerServicer();
		initEngineer();*/
		//initDevice();
		initProject();
	}
	
	@Test
	@Rollback(false)
	public void initEngineerManager() throws Exception{
		Account account = new Account();
		account.setName("工程部经理");
		account.setLoginId("gcbjl5");
		account.setPassword("123456");
		account.getRoleList().add(roleService.findByCode("engineerManager"));
		try {
			//设置密码
			PasswordEncry.encrypt(account);
		} catch (Exception e) {
			e.printStackTrace();
		}
		accountService.save(account);
		System.out.println("success");
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
		account.setName("测试工程师");
	}
	
	@Test
	@Rollback(false)
	public void initDevice(){
		Device device = new Device();
		device.setSerialNo(UUID.randomUUID().toString());
		device.setDevCategory(devCategoryService.getOne((long) 1));
		deviceService.save(device);
		device.setLongitude(114.3118287971);
		device.setLatitude(30.5984342798);
		deviceService.setDeviceDistrict(device);
	}
	
	@Test
	@Rollback(false)
	public void initProject(){
		Project project = new Project();
		project.setNumber("123abc");
		project.setName("测试1");
		project.setLocation("无锡");
		projectService.save(project);
	}
	
}
