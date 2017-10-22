package com.anosi.asset.init;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.anosi.asset.component.PasswordEncry;
import com.anosi.asset.exception.CustomRunTimeException;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.service.AccountService;
import com.anosi.asset.service.RoleService;

/***
 * 进行一些数据的初始化工作
 * @author jinyao
 *
 */
@Component
public class InitData {

	private static final Logger logger = LoggerFactory.getLogger(InitData.class);
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private TransactionTemplate transactionTemplate;
	@Autowired
	private InitDepRelated initDepRelated;
	@Autowired
	private InitRoleFunctionRelated initRoleFunctionRelated;
	@Autowired
	private RoleService roleService;
	
	@PostConstruct
	public void init(){
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus arg0) {
				logger.debug("init data");
				initDepRelated.initDepRelated();
				initAdmin();
				initRoleFunctionRelated.initRoleFunctionRelated();
			}
		});
	}
	
	/***
	 * 初始化admin
	 */
	private void initAdmin(){
		Account account = this.accountService.findByLoginId("admin");
		
		if(account==null){
			account = new Account();
			account.setName("admin");
			account.setLoginId("admin");
			account.setPassword("123456");
			account.getRoleList().add(roleService.findByCode("admin"));
			try {
				//设置密码
				PasswordEncry.encrypt(account);
			} catch (Exception e) {
				throw new CustomRunTimeException();
			}
			accountService.save(account);
		}
	}
	
}
