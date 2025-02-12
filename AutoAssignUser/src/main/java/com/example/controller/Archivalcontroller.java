package com.example.controller;

import com.example.service.AccountDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Archivalcontroller {

    @Autowired
    private AccountDataService accountDataService;

    @GetMapping("/archival")
    public String archival(@RequestParam(name = "id") String id){
        accountDataService.moveToArchival(id);
        return "success";
    }
}
