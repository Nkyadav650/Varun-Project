package com.example.service;

import com.example.entity.AccountData;
import com.example.entity.GroupTable;
import com.example.entity.UserTable;
import com.example.model.request.ProcessDetails;
import com.example.repository.AccountDataRepository;
import com.example.repository.GroupRepository;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.PublishMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class MessageTriggerService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ZeebeClient zeebeClient;

    @Autowired
    private AccountDataRepository repository;

    public String triggerMessage(List<ProcessDetails> processDetailsList){
        log.info("process details in service : {}",processDetailsList);

        for(ProcessDetails processDetails : processDetailsList){
            String groupName = processDetails.getAssignedGroup();
            log.info("group name is : {}",groupName);
            GroupTable group = groupRepository.findByGroupName(groupName).orElseThrow(() -> new RuntimeException("Group name not exist"));
            Optional<UserTable> user = group.getUsers().stream()
                    .filter(u -> u.getUserName().equalsIgnoreCase(processDetails.getAssignedUser()))
                    .findFirst();
            if(user.isPresent()){
                triggerMessageEvent(processDetails);
                AccountData byInstanceId = repository.findByInstanceId(processDetails.getInstanceId());
                byInstanceId.setAssignedUser(processDetails.getAssignedUser());
                byInstanceId.setStatus("ASSIGNED");
                repository.save(byInstanceId);
            }
        }
        return "success";
    }

    private void triggerMessageEvent(ProcessDetails processDetails){
        String correlationKey = "assignUser "+processDetails.getInstanceId();

        PublishMessageResponse response = zeebeClient
                .newPublishMessageCommand()
                .messageName("assignUser")
                .correlationKey(correlationKey)
                .variable("assignedUserName", processDetails.getAssignedUser())
                .send()
                .join();

        log.info("Message triggered successfully for message key - {}", response.getMessageKey());

    }

}

