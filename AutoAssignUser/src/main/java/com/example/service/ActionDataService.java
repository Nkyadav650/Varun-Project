package com.example.service;

import com.example.entity.Action;
import com.example.repository.ActionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
public class ActionDataService {

    @Autowired
    ActionRepository actionRepository;

    private final String SOURCE_TABLE = "action";
    private final String TARGET_TABLE = "action_archival";

    public void storeData(Action action) {
        Action saved = actionRepository.save(action);
        log.info("saved action - {}",saved);
    }

    public void storeData(long elementInstanceKey, Map<String, Object> variables, ActivatedJob job)
            throws JsonProcessingException {
        log.info("updating action for ElementInstanceId - {}",elementInstanceKey);
        ObjectMapper mapper = new ObjectMapper();
        String variablesAsString = mapper.writeValueAsString(variables);

        Action action = actionRepository.findByElementInstanceKey(String.valueOf(elementInstanceKey));

        if(action == null){
            action = Action.builder()
                    .instanceId(String.valueOf(job.getProcessInstanceKey()))
                    .activityType(job.getElementId())
                    .elementInstanceKey(String.valueOf(elementInstanceKey))
                    .build();
        }
        log.info("before saving action - {}",action);
        action.setVariables(variablesAsString);
        action.setEndTime(LocalDateTime.now());
        System.out.println(variables);
        Action saved = actionRepository.save(action);
        log.info("after saved action - {}",saved);
    }

    public void moveToArchival(String processInstanceId){
        log.info("Calling moveToArchival for Action_Table");
        actionRepository.moveToArchival(SOURCE_TABLE,TARGET_TABLE,processInstanceId);
    }
}
