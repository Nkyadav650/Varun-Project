package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.model.request.ProcessDetails;
import com.example.service.MessageTriggerService;
import io.camunda.zeebe.client.ZeebeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.AccountData;
import com.example.service.AccountDataService;

@RestController
@RequestMapping("/api/accounts")
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountDataController {

    @Autowired
    private AccountDataService accountDataService;

    @Autowired
    private ZeebeClient client;

    @Autowired
    private MessageTriggerService messageTriggerService;

    @GetMapping("/get-all-account-data")
    public ResponseEntity<List<AccountData>> getAllAccounts() {
        log.info("getAllAccounts in account controller entered for fetch all data");
        return ResponseEntity.ok(accountDataService.getAllAccounts());
    }

    @PostMapping("/assign/users")
    public String assignUsers(@RequestBody List<ProcessDetails> processDetails){
        log.info("processDetails : {}",processDetails);
        return messageTriggerService.triggerMessage(processDetails);
    }


}
