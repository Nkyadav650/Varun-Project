package com.example.camund;

import com.example.entity.Action;
import com.example.service.ActionDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

@Service
@Slf4j
public class ActionStorageHandler {

    @Autowired
    private ZeebeClient zeebeClient;

    @Autowired
    private ActionDataService actionDataService;

    @JobWorker(type="store-data-start",autoComplete = true)
    public void storeActionStart(ActivatedJob job){
        log.info("entering store-data-start job for processInstanceId - {} and ElementInstanceId - {}", job.getProcessInstanceKey(), job.getElementInstanceKey());
        Action action = Action.builder()
                .instanceId(String.valueOf(job.getProcessInstanceKey()))
                .elementInstanceKey(String.valueOf(job.getElementInstanceKey()))
                .activityType(job.getElementId())
                .startTime(LocalDateTime.now())
                .build();
        log.info("Build Action data - {}",action);
        actionDataService.storeData(action);

    }

    @JobWorker(type = "store-data-end")
    public void storeActionEnd(ActivatedJob job) throws JsonProcessingException {
        log.info("entering store-data-end job for processInstanceId - {} and ElementInstanceId - {}",
                job.getProcessInstanceKey(), job.getElementInstanceKey());
        if(job.getElementId().equalsIgnoreCase("StartEvent_1")){
            zeebeClient.newCompleteCommand(job.getKey())
                    .variable("processInstanceId",String.valueOf(job.getProcessInstanceKey()))
                    .send().join();
        }else{
            actionDataService.storeData(job.getElementInstanceKey(),job.getVariablesAsMap(),job);
        }
    }

}
