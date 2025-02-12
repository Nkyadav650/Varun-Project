package com.example.repository;

import com.example.entity.Action;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends JpaRepository<Action,Long> {

    Action findByInstanceId(String instanceId);
    Action findByElementInstanceKey(String elementInstanceKey);

    @Modifying
    @Transactional
    @Query(value = "CALL move_data_between_tables(:sourceTable, :targetTable, :processInstanceId)", nativeQuery = true)
    void moveToArchival(@Param("sourceTable") String sourceTable,
                        @Param("targetTable") String targetTable,
                        @Param("processInstanceId") String processInstanceId);
}
