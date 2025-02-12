package com.example.service;

import com.example.entity.Process;
import com.example.repository.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessService {

    @Autowired
    ProcessRepository processRepository;

    public String fetchProcessByName(String processName){
        Process process = processRepository.findByProcessName(processName);
        if(process!=null){
            return process.getProcessId();
        }
        return null;
    }
}
