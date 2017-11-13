package com.anosi.asset.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.anosi.asset.model.jpa.Account;

public class TestList {

	@Test
	public void testList(){
		List<Account> accounts = new ArrayList<>();
		Account account = new Account();
		account.setName("abc");
		accounts.add(account);
		for (Account account2 : accounts) {
			account2.setName("123");
		}
		for (Account account3 : accounts) {
			System.out.println(account3.getName());
		}
	}
	
}
