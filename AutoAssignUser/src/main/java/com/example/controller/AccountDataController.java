package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.AccountData;
import com.example.service.AccountDataService;

@RestController
@RequestMapping("/api/accounts")
public class AccountDataController {

    @Autowired
    private AccountDataService accountDataService;

    @PostMapping
    public ResponseEntity<AccountData> createAccount(@RequestBody AccountData account) {
        return ResponseEntity.ok(accountDataService.createAccount(account));
    }

    @GetMapping
    public ResponseEntity<List<AccountData>> getAllAccounts() {
        return ResponseEntity.ok(accountDataService.getAllAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<AccountData>> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountDataService.getAccountById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountData> updateAccount(@PathVariable Long id, @RequestBody AccountData account) {
        return ResponseEntity.ok(accountDataService.updateAccount(id, account));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountDataService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
