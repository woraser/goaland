package com.anosi.asset.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.dao.elasticsearch.AccountContentDao;
import com.anosi.asset.dao.elasticsearch.BaseContentDao;
import com.anosi.asset.model.elasticsearch.AccountContent;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.service.AccountContentService;

@Service("accountContentService")
@Transactional
public class AccountContentServiceImpl extends BaseContentServiceImpl<AccountContent, String, Account>
		implements AccountContentService {

	@Autowired
	private AccountContentDao accountContentDao;

	@Override
	public BaseContentDao<AccountContent, String> getRepository() {
		return accountContentDao;
	}

	@Override
	public AccountContent saveContent(Account account) throws Exception {
		String id = String.valueOf(account.getId());
		AccountContent accountContent = accountContentDao.findOne(id);
		if (accountContent == null) {
			accountContent = new AccountContent();
			accountContent.setId(id);
		}
		accountContent = setCommonContent(accountContent, account);
		return accountContentDao.save(accountContent);
	}

	@Override
	public <S extends Account> Iterable<AccountContent> saveContent(Iterable<S> obs) throws Exception {
		List<AccountContent> accountContents = new ArrayList<>();
		for (Account account : obs) {
			String id = String.valueOf(account.getId());
			AccountContent accountContent = accountContentDao.findOne(id);
			if (accountContent == null) {
				accountContent = new AccountContent();
				accountContent.setId(id);
			}
			accountContent = setCommonContent(accountContent, account);
			accountContents.add(accountContent);
		}
		return accountContentDao.save(accountContents);
	}

}
