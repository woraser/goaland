package com.anosi.asset.service;

import org.springframework.data.domain.Pageable;

import com.anosi.asset.model.jpa.Account;
import com.querydsl.core.types.Predicate;

public interface AccountService extends BaseService<Account, Long> {
	
	public Account findByLoginId(String loginId);

	public Account save(Account account);
	
	public Iterable<Account> findAll(Predicate predicate);

	public Iterable<Account> findAll(Predicate predicate, Pageable pageable);
	
}
