package com.v02.minback.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.v02.minback.model.entity.AccessLogEntity;
import com.v02.minback.model.entity.BankBalanceLogEntity;
import com.v02.minback.model.param.BalanceLogParam;
import com.v02.minback.repository.AccessLogJdbcRepository;
import com.v02.minback.repository.BankBalanceLogJdbcRepository;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Profile("consumer")
@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerService {

    private final BankBalanceLogJdbcRepository bankBalanceLogJdbcRepository;
    private final AccessLogJdbcRepository accessLogJdbcRepository;
    private final ObjectMapper objectMapper;

    private static final Integer BATCH_SIZE = 30;

    private final List<AccessLogEntity> listAccessLogEntity = new ArrayList<>();
    private final List<BankBalanceLogEntity> listBankLogDeposit = new ArrayList<>();
    private final List<BankBalanceLogEntity> listBankLogWithdraw = new ArrayList<>();
    private final List<BankBalanceLogEntity> listBankLogTransfer = new ArrayList<>();
    @RabbitListener(queues = "bank.log.access", concurrency = "1")
    public void queueAccessLog(AccessLogEntity accessLogEntity) {

        String fullMethodName = "";
        try {
            fullMethodName = this.getClass().getSimpleName() + "." + new Object() {
            }.getClass().getEnclosingMethod().getName();
            log.info("{} 메세지 : {}", fullMethodName, objectMapper.writeValueAsString(accessLogEntity));
            listAccessLogEntity.add(accessLogEntity);

            if (listAccessLogEntity.size() >= BATCH_SIZE) {
                accessLogJdbcRepository.saveAll(listAccessLogEntity);
                listAccessLogEntity.clear();
            }
            log.info("\n {} : save", fullMethodName);

        } catch (Exception e) {
            log.error("ERROR[{}] : {}", fullMethodName, e.getMessage());
        }

    }

    @RabbitListener(queues = "bank.log.deposit" , concurrency = "1")
    public void qeueBankLogDeposit(BalanceLogParam param) {
        String fullMethodName = "";
        try {
            fullMethodName = this.getClass().getSimpleName() + "." + new Object() {
            }.getClass().getEnclosingMethod().getName();
            log.info("{} 메세지 : {}", fullMethodName, objectMapper.writeValueAsString(param));

            BankBalanceLogEntity balanceLogEntity = new BankBalanceLogEntity(
                    objectMapper.writeValueAsString(param.getSavedAccount()),
                    objectMapper.writeValueAsString(param.getUpdatedAccount()),
                    objectMapper.writeValueAsString(param.getClassMethod())
            );

            listBankLogDeposit.add(balanceLogEntity);

            if (listBankLogDeposit.size() >= BATCH_SIZE) {
                bankBalanceLogJdbcRepository.saveAll(listBankLogDeposit);
                listBankLogDeposit.clear();
            }
        } catch (Exception e) {
            log.error("[{}] : {}", fullMethodName, e.getMessage());
        }
    }
    @RabbitListener(queues = "bank.log.withdraw" , concurrency = "1")
    public void queueBankLogWithdraw(BalanceLogParam param) {
        String fullMethodName = "";
        try {
            fullMethodName = this.getClass().getSimpleName() + "." + new Object() {
            }.getClass().getEnclosingMethod().getName();
            log.info("{} 메세지 : {}", fullMethodName, objectMapper.writeValueAsString(param));

            BankBalanceLogEntity balanceLogEntity = new BankBalanceLogEntity(
                    objectMapper.writeValueAsString(param.getSavedAccount()),
                    objectMapper.writeValueAsString(param.getUpdatedAccount()),
                    objectMapper.writeValueAsString(param.getClassMethod())
            );

            listBankLogWithdraw.add(balanceLogEntity);

            if (listBankLogWithdraw.size() >= BATCH_SIZE) {
                bankBalanceLogJdbcRepository.saveAll(listBankLogWithdraw);
                listBankLogWithdraw.clear();
            }
        } catch (Exception e) {
            log.error("[{}] : {}", fullMethodName, e.getMessage());
        }
    }

    @RabbitListener(queues = "bank.log.transfer" , concurrency = "1")
    public void queueBankLogTransfer(BalanceLogParam param) {
        String fullMethodName = "";
        try {
            fullMethodName = this.getClass().getSimpleName() + "." + new Object() {
            }.getClass().getEnclosingMethod().getName();
            log.info("{} 메세지 : {}", fullMethodName, objectMapper.writeValueAsString(param));

            BankBalanceLogEntity balanceLogEntity = new BankBalanceLogEntity(
                    objectMapper.writeValueAsString(param.getSavedAccount()),
                    objectMapper.writeValueAsString(param.getUpdatedAccount()),
                    objectMapper.writeValueAsString(param.getClassMethod())
            );

            listBankLogTransfer.add(balanceLogEntity);

            if (listBankLogTransfer.size() >= BATCH_SIZE) {
                bankBalanceLogJdbcRepository.saveAll(listBankLogTransfer);
                listBankLogTransfer.clear();
            }
        } catch (Exception e) {
            log.error("[{}] : {}", fullMethodName, e.getMessage());
        }
    }


    @PreDestroy
    public void onShutdown() {
        log.info("Graceful Shutdown 시작 - 버퍼 데이터 저장 중...");
        saveLogs("listAccessLogEntity", listAccessLogEntity, accessLogJdbcRepository);

        Map<String, List<BankBalanceLogEntity>> bankLogs = Map.of(
                "listBankLogDeposit", listBankLogDeposit,
                "listBankLogWithdraw", listBankLogWithdraw,
                "listBankLogTransfer", listBankLogTransfer
        );

        bankLogs.forEach((name, list) -> saveLogs(name, list, bankBalanceLogJdbcRepository));
    }

    private <T> void saveLogs(String name, List<T> logList, Object repository) {
        if (!logList.isEmpty()) {
            try {
                if (repository instanceof AccessLogJdbcRepository accessRepo) {
                    accessRepo.saveAll((List<AccessLogEntity>) logList);
                } else if (repository instanceof BankBalanceLogJdbcRepository bankRepo) {
                    bankRepo.saveAll((List<BankBalanceLogEntity>) logList);
                }
                log.info("[{}] SUCCESS Graceful Shutdown saveAll()", name);
                logList.clear();
            } catch (Exception e) {
                log.error("[{}] FAIL Graceful Shutdown: {}", name, e.getMessage(), e);
            }
        }
    }








}
