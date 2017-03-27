package com.gykan.services.account.service;

import java.util.ArrayList;
import java.util.List;

import com.gykan.services.common.model.Account;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

	private List<Account> accounts = new ArrayList<>();
	
	public Account findById(Integer id) {
		return new Account(id, "1234567890", 4321, 2);
	}
	
	public List<Account> findByCustomerId(Integer customerId) {
		List<Account> l = new ArrayList<>();
		l.add(new Account(1, "1234567890", 4321, customerId));
		l.add(new Account(2, "1234567891", 12346, customerId));
		return l;
	}
	
	public List<Account> findAll() {
		List<Account> l = new ArrayList<>();
		l.add(new Account(1, "1234567890", 4321, 1));
		l.add(new Account(2, "1234567891", 12346, 2));
		return l;
	}
	
	public Account add(Account account) {
		account.setId(accounts.size()+1);
		accounts.add(account);
		return account;
	}
	
}
