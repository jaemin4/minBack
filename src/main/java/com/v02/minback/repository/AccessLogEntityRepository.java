package com.v02.minback.repository;

import com.v02.minback.model.entity.AccessLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessLogEntityRepository extends JpaRepository<AccessLogEntity, Long> {
}