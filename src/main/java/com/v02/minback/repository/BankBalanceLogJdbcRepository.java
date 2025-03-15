package com.v02.minback.repository;


import com.v02.minback.model.entity.BankBalanceLogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BankBalanceLogJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SQL =
            "INSERT INTO bank_balance_log (prev_data, updated_data, class_method,create_at) " +
            "VALUES (?, ?, ?, ?)";
    public void saveAll(List<BankBalanceLogEntity> logs)throws SQLException {

        jdbcTemplate.batchUpdate(SQL, logs, logs.size(), (ps, log) -> {
            ps.setString(1, log.getPrevData());
            ps.setString(2, log.getUpdatedData());
            ps.setString(3, log.getClassMethod());
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(LocalDateTime.now()));

        });
    }

}
