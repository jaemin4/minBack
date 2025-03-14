package com.pro.repository;

import com.pro.model.entity.AccessLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessLogEntityRepository extends JpaRepository<AccessLogEntity, Long> {
}