package com.anosi.asset.dao.jpa;

import com.anosi.asset.model.jpa.Account;

public interface AccountDao extends BaseJPADao<Account>{

	public Account findByLoginId(String loginId);
	
}
