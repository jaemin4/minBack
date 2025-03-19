package com.v02.minback.service.log.front;

import com.v02.minback.model.param.AccessLogParam;
import org.springframework.stereotype.Service;

@Service
public class ThreadLocalAccessLogService {

    private ThreadLocal<AccessLogParam> threadLocal = new ThreadLocal<>();

    public void putAccessLog(AccessLogParam accessLogDto) {
        this.threadLocal.set(accessLogDto);
    }

    public AccessLogParam getAccessLog() {
        return this.threadLocal.get();
    }

    public void removeThreadLocal() {
        this.threadLocal.remove();
    }

}
