package com.anosi.asset.test.account;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.GoalandApplication;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.QAccount;
import com.anosi.asset.service.AccountService;
import com.querydsl.core.types.dsl.PathInits;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GoalandApplication.class)
@Transactional
public class TestQueryDsl {
	
	private static final Logger logger = LoggerFactory.getLogger(TestQueryDsl.class);
	
	@Autowired
	private AccountService accountService;
	
	@Test
	public void testQueryDsl(){
		PathInits inits = new PathInits("role.depGroup.department");
		QAccount account=new QAccount(Account.class, forVariable("account"), inits);
		Iterable<Account> all = accountService.findAll(account.role.depGroup.department.name.eq("网络部"));
		all.forEach(a->logger.debug("account.name:{}",a.getName()));
	}

}
