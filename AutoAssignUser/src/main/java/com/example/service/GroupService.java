package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.GroupTable;
import com.example.entity.UserTable;
import com.example.repository.GroupRepository;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    // Create operation
    public GroupTable createGroup(GroupTable group) {
        if(groupRepository.existsByGroupName(group.getGroupName())){
            Optional<GroupTable> byGroupName = groupRepository.findByGroupName(group.getGroupName());
            if(byGroupName.isPresent()){
                GroupTable groupTable = byGroupName.get();
                List<UserTable> groupTableUsers = groupTable.getUsers();
                List<UserTable> input = group.getUsers();
                List<UserTable> list = groupTableUsers.parallelStream()
                        .filter(u -> {
                            Optional<UserTable> first = input.parallelStream().filter(user -> user.getUserName().equalsIgnoreCase(u.getUserName())).findFirst();
                            if (first.isPresent()) {
                                return true;
                            } else {
                                return false;
                            }
                        }).toList();
                groupTable.getUsers().addAll(list);
                return groupRepository.save(groupTable);
            }
        }
        return groupRepository.save(group);
    }

    // Read operation (Get all groups)
    public List<GroupTable> getAllGroups() {
        return groupRepository.findAll();
    }

    // Read operation (Get group by ID)
    public Optional<GroupTable> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

    // Update operation
    public GroupTable updateGroup(Long id, GroupTable groupDetails) {
        return groupRepository.findById(id).map(group -> {
            group.setGroupName(groupDetails.getGroupName());
            List<UserTable> users= group.getUsers();
            if(users==null) {
            	List<UserTable> newUsers = new ArrayList<>();
            	newUsers.addAll(groupDetails.getUsers());
            	group.setUsers(newUsers);
            }else {
            	users.addAll(groupDetails.getUsers());
            group.setUsers(users);
            }
            return groupRepository.save(group);
        }).orElseThrow(() -> new RuntimeException("Group not found"));
    }

    // Delete operation
    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }
}