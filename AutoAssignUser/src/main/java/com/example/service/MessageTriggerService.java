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
        for(ProcessDetails processDetails : processDetailsList){
            String groupName = processDetails.getGroupName();
            GroupTable group = groupRepository.findByGroupName(groupName).orElseThrow(() -> new RuntimeException("Group name not exist"));
            Optional<UserTable> user = group.getUsers().parallelStream().
                    filter(u -> {
                        log.info(u.getUserName()+" "+processDetails.getUserName());
                      return  u.getUserName().equalsIgnoreCase(processDetails.getUserName());
                    }).
                    findFirst();
            if(user.isPresent()){
                triggerMessageEvent(processDetails);
                AccountData byInstanceId = repository.findByInstanceId(processDetails.getProcessInstanceId());
                byInstanceId.setAssignedUser(processDetails.getUserName());
                byInstanceId.setStatus("ASSIGNED");
                repository.save(byInstanceId);
            }
            else{
                throw new RuntimeException("Given user name is not exist please provide correct user name");
            }
        }
        return "success";
    }

    private void triggerMessageEvent(ProcessDetails processDetails){
        String correlationKey = "assignUser "+processDetails.getProcessInstanceId();

        PublishMessageResponse response = zeebeClient
                .newPublishMessageCommand()
                .messageName("assignUser") // Must match the BPMN message name
                .correlationKey(correlationKey) // This should match the process instance variable
                .variable("assignedUserName", processDetails.getUserName())
                .send()
                .join();

        log.info("Message triggered successfully");

    }

}

