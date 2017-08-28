package com.anosi.asset.service.impl;

import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anosi.asset.component.PasswordEncry;
import com.anosi.asset.dao.jpa.AccountDao;
import com.anosi.asset.dao.jpa.BaseJPADao;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.service.AccountService;

@Service("accountService")
@Transactional
public class AccountServiceImpl extends BaseServiceImpl<Account> implements AccountService{
	
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
	
	@Autowired
	private AccountDao accountDao;
	
	@Override
	public BaseJPADao<Account> getRepository() {
		return accountDao;
	}
	
	@Override
	public Account findByLoginId(String loginId) {
		logger.debug("findByLoginId:{}",loginId);
		return this.accountDao.findByLoginId(loginId);
	}
	
	/***
	 * 复写save方法
	 * 
	 */
	@Override
	public Account save(Account account, String password){
		account.setPassword(password);
		try {
			//设置密码
			PasswordEncry.encrypt(account);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accountDao.save(account);
	}

}
