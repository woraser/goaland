package com.anosi.asset.test;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.anosi.asset.GoalandApplication;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.QAccount;
import com.anosi.asset.model.jpa.QRole;
import com.anosi.asset.service.AccountService;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GoalandApplication.class)
@Transactional
public class TestQueryDsl {

	@Autowired
	private AccountService accountService;
	@Autowired
	protected EntityManager entityManager;

	protected JPAQueryFactory queryFactory;

	/*
	 * @Test public void testQueryDsl(){ PathInits inits = new
	 * PathInits("role.depGroup.department"); QAccount account=new
	 * QAccount(Account.class, forVariable("account"), inits); Iterable<Account>
	 * all =
	 * accountService.findAll(account.role.depGroup.department.name.eq("网络部"));
	 * all.forEach(a->logger.debug("account.name:{}",a.getName())); }
	 */

	@Test
	public void testFindAll() {
		Iterable<Account> iterable = accountService.findAll();
		iterable.forEach(System.out::print);
	}
	
	@Before
	public void init(){
		queryFactory =  new JPAQueryFactory(entityManager);
	}

	@Test
	public void testJoin() {
		List<Account> accounts = queryFactory.select(QAccount.account).from(QAccount.account, QRole.role)
				.where(QRole.role.depGroup.code.eq("customerServiceGroup"),QAccount.account.roleList.contains(QRole.role)).fetch();
		accounts.forEach(account->System.out.println(account.getLoginId()));
	}

}
