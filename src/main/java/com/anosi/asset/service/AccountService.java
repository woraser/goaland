package com.anosi.asset.service;

import com.anosi.asset.model.jpa.Account;

public interface AccountService extends BaseService<Account, Long>{
	
	public Account findByLoginId(String loginId);
	
	/***
	 * 
	 * @param account
	 * @param password
	 * @return
	 */
	public Account save(Account account, String password);

}
