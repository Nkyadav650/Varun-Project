package com.example.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.AccountData;
@Repository
public interface AccountDataRepository extends JpaRepository<AccountData, Long> {

	AccountData findByInstanceId(String instanceId);

	@Modifying
	@Transactional
	@Query(value = "CALL move_data_between_tables(:sourceTable, :targetTable, :processInstanceId)", nativeQuery = true)
	void moveToArchival(@Param("sourceTable") String sourceTable,
						@Param("targetTable") String targetTable,
						@Param("processInstanceId") String processInstanceId);
}
