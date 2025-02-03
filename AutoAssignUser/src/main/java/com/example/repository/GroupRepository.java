package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.GroupTable;
@Repository
public interface GroupRepository extends JpaRepository<GroupTable, Long> {

}
