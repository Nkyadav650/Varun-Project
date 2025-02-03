package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.AccountData;
import com.example.repository.AccountDataRepository;

@Service
public class AccountDataService {

	@Autowired
	private AccountDataRepository accountRepository;

	// Create operation
	public AccountData createAccount(AccountData account) {
		return accountRepository.save(account);
	}

	// Read operation (Get all accounts)
	public List<AccountData> getAllAccounts() {
		return accountRepository.findAll();
	}

	// Read operation (Get account by ID)
	public Optional<AccountData> getAccountById(Long id) {
		return accountRepository.findById(id);
	}

	// Update operation
	public AccountData updateAccount(Long id, AccountData accountDetails) {
		return accountRepository.findById(id).map(account -> {
			account.setAssignedGroup(accountDetails.getAssignedGroup());
			account.setAccountNumber(accountDetails.getAccountNumber());
			account.setAssignedUser(accountDetails.getAssignedUser());
			return accountRepository.save(account);
		}).orElseThrow(() -> new RuntimeException("Account not found"));
	}

	// Delete operation
	public void deleteAccount(Long id) {
		accountRepository.deleteById(id);
	}
}
