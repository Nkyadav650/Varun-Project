package com.example.entity;

import java.util.List;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class GroupTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long groupId;

    private String groupName;

    @OneToMany(mappedBy = "groups", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserTable> users;
}
