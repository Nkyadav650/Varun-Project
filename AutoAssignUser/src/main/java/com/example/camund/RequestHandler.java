package com.example.camund;

import com.example.service.AccountDataService;
import com.example.service.ProcessService;
import com.example.service.ActionDataService;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RequestHandler {

    @Autowired
    private ZeebeClient client;

    @Autowired
    private ProcessService processService;

    @Autowired
    private ActionDataService actionDataService;

    @Autowired
    private AccountDataService accountDataService;

    @JobWorker(type = "fetch-process", autoComplete = false)
    public void fetchProcess(ActivatedJob job){
        String processName = (String)job.getVariable("process");
        String processId = processService.fetchProcessByName(processName);

        log.info("fetching process with name - {}",processName);

        client.newCompleteCommand(job.getKey())
                .variable("process",processId)
                .send().join();
    }

    @JobWorker(type = "archival-transfer")
    public void archivalTransfer(ActivatedJob job){
        String calledProcessInstanceId = String.valueOf(job.getVariablesAsMap().get("calledProcessInstanceId"));

        log.info("Archival transferring process started for processInstanceId - {}", calledProcessInstanceId);
        accountDataService.moveToArchival(calledProcessInstanceId);
        actionDataService.moveToArchival(calledProcessInstanceId);
    }
}
