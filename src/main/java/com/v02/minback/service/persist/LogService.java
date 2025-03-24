package com.v02.minback.service.persist;


import com.v02.minback.model.entity.AccessLogEntity;
import com.v02.minback.repository.AccessLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogService {

    private final AccessLogRepository accessLogRepository;

    public Page<AccessLogEntity> getAccessLogs(Pageable pageable){
        return accessLogRepository.findAll(pageable);
    }

}
